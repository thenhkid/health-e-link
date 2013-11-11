package com.ut.dph.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

import com.ut.dph.model.configuration;
import com.ut.dph.service.configurationManager;
import com.ut.dph.model.Organization;
import com.ut.dph.service.organizationManager;
import com.ut.dph.model.messageType;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.service.configurationTransportManager;


@Controller
@RequestMapping("/administrator/configurations")

public class adminConfigController {
	
	@Autowired 
	private configurationManager configurationmanager;
	
	@Autowired
	private organizationManager organizationmanager;
	
	@Autowired
	private messageTypeManager messagetypemanager;
	
	@Autowired
	private configurationTransportManager configurationTransportManager;
	
	/**
	 * The private variable configId will hold the configurationId when viewing a configuration
	 * this will be used when on a configuration subsections like Field Mappings, Data Translations,
	 * etc.
	 * We will use this private variable so we don't have to go fetch the id 
	 */
	private static int configId = 0;
	
	/**
	 * The private maxResults variable will hold the number of results to show per
	 * list page.
	 */
	private static int maxResults = 20;
	
	/**
	 * The private variable notavailable will hold the value if the configuration can
	 * be set to active. This will only be set to FALSE after the 5th step (Connections)
	 * has been completed.
	 */
	private static boolean notavailable = true;
	
	
	/**
	 *  The '/list' GET request will serve up the existing list of configurations
	 *  in the system
	 *  
	 * @param page			The page parameter will hold the page to view when pagination 
	 * 						is built.
	 * @return				The configuration page list
	 * 
	 * @Objects				(1) An object containing all the found configurations
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView listConfigurations(@RequestParam(value="page", required=false) Integer page) throws Exception {
		
		if(page == null){
	        page = 1;
	    }
 
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/list");
        
        List<configuration> configurations = configurationmanager.getConfigurations(page,maxResults);
        mav.addObject("configurationList", configurations);	
        
        Organization org = null;
        messageType messagetype = null;
        
        for(configuration config : configurations) {
        	org = organizationmanager.getOrganizationById(config.getorgId());
        	config.setOrgName(org.getOrgName());
        	
        	messagetype = messagetypemanager.getMessageTypeById(config.getMessageTypeId());
        	config.setMessageTypeName(messagetype.getName());
        	
        	Long totalConnections = null;
    		totalConnections = (Long) configurationmanager.getTotalConnections(configId);
    		config.setTotalConnections(totalConnections);
    		
        }
        
        //Return the total list of configurations
        Long totalConfigs = configurationmanager.findTotalConfigs();
        
        Integer totalPages = Math.round(totalConfigs/maxResults);
        mav.addObject("totalPages",totalPages);
        mav.addObject("currentPage",page);
        return mav;
 
	}
	
	/**
	 * The '/list' POST request will be used to search configurations from the search form on the
	 * configuration list page.
	 * 
	 * @param searchTerm	The searchTerm parameter will hold the string to search on
	 * @return				The configuration page list
	 * 
	 * @Objects				(1) An object will be returned holding the requested search term used to 
	 * 							populate the search box
	 * 						(2) An object containing the found configurations
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	public ModelAndView findConfigurtions(@RequestParam String searchTerm) throws Exception {
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/list");
        
        List<configuration> configurations = configurationmanager.findConfigurations(searchTerm);
        mav.addObject("searchTerm",searchTerm);
        mav.addObject("configurationList", configurations);		
        
        Organization org = null;
        messageType messagetype = null;
        
        for(configuration config : configurations) {
        	org = organizationmanager.getOrganizationById(config.getorgId());
        	config.setOrgName(org.getOrgName());
        	
        	messagetype = messagetypemanager.getMessageTypeById(config.getMessageTypeId());
        	config.setMessageTypeName(messagetype.getName());
        }
        
        return mav;
 
	}
	
	/**
	 * The '/create' GET request will serve up the create new configuration page
	 * 
	 * @return				The create new configuration form
	 * 
	 * @Objects				(1) An object with a new configuration 
	 * @throws Exception
	 */
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView createConfiguration() throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/configurations/details");
		mav.addObject("configurationDetails", new configuration());
		
		//Need to get a list of active organizations.
		List<Organization> organizations = organizationmanager.getAllActiveOrganizations();
		mav.addObject("organizations", organizations);
		
		//Need to get a list of active message types
		List<messageType> messageTypes = messagetypemanager.getActiveMessageTypes();
		mav.addObject("messageTypes", messageTypes);
		mav.addObject("notavailable", notavailable);
		
		return mav;
		
	}
	
	/**
	 * The '/create' POST request will submit the new configuration once all required fields
	 * are checked, the system will also check to make sure the configuration name is not already
	 * in use.
	 * 
	 * @param configurationDetails		The object holding the configuration details form fields
	 * @param result					The validation result
	 * @param redirectAttr				The variable that will hold values that can be read after the redirect
	 * @param action					The variable that holds which button was pressed
	 * 
	 * @return					Will return the configuration details page on "Save"
	 * 							Will return the configuration create page on error
	 * @throws Exception
	 */
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public ModelAndView saveNewConfiguration(@Valid @ModelAttribute(value="configurationDetails") configuration configurationDetails, BindingResult result, RedirectAttributes redirectAttr,@RequestParam String action) throws Exception {
		
		//Need to get a list of active organizations.
		List<Organization> organizations = organizationmanager.getAllActiveOrganizations();
		
		//Need to get a list of active message types
		List<messageType> messageTypes = messagetypemanager.getActiveMessageTypes();
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/configurations/details");
			
			//Need to get a list of active organizations.
			mav.addObject("organizations", organizations);
			
			//Need to get a list of active message types
			mav.addObject("messageTypes", messageTypes);
			
			mav.addObject("notavailable", notavailable);
			
			return mav;
		}
		
		List<configuration> existing = configurationmanager.getConfigurationByName(configurationDetails.getConfigName());
	    if (!existing.isEmpty()) {
        	ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/configurations/details");
			mav.addObject("id",configurationDetails.getId());
			mav.addObject("existingConfig","configuration "+configurationDetails.getConfigName()+" already exists.");
			
			//Need to get a list of active organizations.
			mav.addObject("organizations", organizations);
			
			//Need to get a list of active message types
			mav.addObject("messageTypes", messageTypes);
			
			mav.addObject("notavailable", notavailable);
			
			return mav;	
        }
		
		Integer id = null;
		id = (Integer) configurationmanager.createConfiguration(configurationDetails);
		
		configId = id;
		redirectAttr.addFlashAttribute("savedStatus", "created");
		ModelAndView mav = new ModelAndView(new RedirectView("details"));
		return mav;	
		
	}
	
	/**
	 * The '/details' GET request will display the clicked configuration details page.
	 * 
	 * @return				Will return the configuration details page.
	 * 
	 * @Objects				(1) The object containing all the information for the clicked configuration
	 * 						(2) The 'id' of the clicked configuration that will be used in the menu and action bar
	 * 
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="/details", method = RequestMethod.GET)
	public ModelAndView viewConfigurationDetails(@RequestParam(value="i", required=false) Integer id) throws Exception {
		
		if(id != null) {
			configId = id;
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/configurations/details");
		
		configuration configurationDetails = configurationmanager.getConfigurationById(configId);
		mav.addObject("configurationDetails", configurationDetails);
		
		//Need to get a list of active organizations.
		List<Organization> organizations = organizationmanager.getAllActiveOrganizations();
		mav.addObject("organizations", organizations);
		
		//Need to get a list of active message types
		List<messageType> messageTypes = messagetypemanager.getActiveMessageTypes();
		mav.addObject("messageTypes", messageTypes);
		mav.addObject("id", configId);
		
		//Test to see if connections have been made, if so set the ability to 
		//set the Status of the configuration to TRUE
		Long totalConnections = null;
		totalConnections = (Long) configurationmanager.getTotalConnections(configId);
		
		if(totalConnections > 0) {
			notavailable = false;
		}
		mav.addObject("notavailable", notavailable);
		
		return mav;
		
	}
	
	
	/**
	 * The '/details' POST request will display the clicked configuration details page.
	 * 
	 * @return				Will return the configuration details page.
	 * 
	 * @Objects				(1) The object containing all the information for the clicked configuration
	 * 						(2) The 'id' of the clicked configuration that will be used in the menu and action bar
	 * 
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="/details", method = RequestMethod.POST)
	public ModelAndView updateConfigurationDetails(@Valid @ModelAttribute(value="configurationDetails") configuration configurationDetails, BindingResult result, RedirectAttributes redirectAttr,@RequestParam String action) throws Exception {
		
		//Need to get a list of active organizations.
		List<Organization> organizations = organizationmanager.getAllActiveOrganizations();
		
		//Need to get a list of active message types
		List<messageType> messageTypes = messagetypemanager.getActiveMessageTypes();
		
		//Test to see if connections have been made, if so set the ability to 
		//set the Status of the configuration to TRUE
		Long totalConnections = null;
		totalConnections = (Long) configurationmanager.getTotalConnections(configId);
		
		if(totalConnections > 0) {
			notavailable = false;
		}
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/configurations/details");
			
			//Need to get a list of active organizations.
			mav.addObject("organizations", organizations);
			//Need to get a list of active message types
			mav.addObject("messageTypes", messageTypes);
			mav.addObject("notavailable", notavailable);
			mav.addObject("id", configId);
			
			return mav;
		}
		
		//submit the updates
		configurationmanager.updateConfiguration(configurationDetails);
		
		//If the "Save" button was pressed 
		if(action.equals("save")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/configurations/details");
			
			//Need to get a list of active organizations.
			mav.addObject("organizations", organizations);			
			//Need to get a list of active message types
			mav.addObject("messageTypes", messageTypes);			
			mav.addObject("notavailable", notavailable);
			mav.addObject("id", configId);
			mav.addObject("savedStatus","updated");
			
			return mav;		
		}
		
		//If the "Next Step" button was pressed.
		else {
			redirectAttr.addFlashAttribute("savedStatus", "updated");
			ModelAndView mav = new ModelAndView(new RedirectView("transport"));
			return mav;			
		}
		
	}
	
	
	/**
	 * The '/transport' GET request will display the clicked configuration transport details form.
	 * 
	 * @return		Will return the configuration transport details form
	 * 
	 * @Objects
	 * 
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/transport", method = RequestMethod.GET)
	public ModelAndView viewTransportDetails() throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/configurations/transport");
		
		//Get the transport details
		configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configId);
		
		//If no transport details have been saved then create new object;
		if(transportDetails == null) {
			transportDetails = new configurationTransport();
		}
		
		mav.addObject("transportDetails", transportDetails);
		mav.addObject("id",configId);
		
		//Get the list of available transport methods
		List transportMethods = configurationTransportManager.getTransportMethods();
		mav.addObject("transportMethods", transportMethods);
		
		//Get the list of available file delimiters
		List delimiters = messagetypemanager.getDelimiters();
		mav.addObject("delimiters", delimiters);
		
		return mav;
		
	}

}
