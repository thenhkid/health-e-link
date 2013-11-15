package com.ut.dph.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.messageTypeFormFields;
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
	public ModelAndView findConfigurations(@RequestParam String searchTerm) throws Exception {
		
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
			
			return mav;
		}
		
		Integer id = null;
		id = (Integer) configurationmanager.createConfiguration(configurationDetails);
		
		configId = id;
		
		//Need to set up the online form transport method
		//setupOnlineForm(configId,messageTypeId)
		configurationTransportManager.setupOnlineForm(configId,configurationDetails.getMessageTypeId());
		
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
		
		//Set the variable to hold the number of completed steps for this configuration;
		mav.addObject("completedSteps",configurationDetails.getstepsCompleted());
		
		//Need to get a list of active organizations.
		List<Organization> organizations = organizationmanager.getAllActiveOrganizations();
		mav.addObject("organizations", organizations);
		
		//Need to get a list of active message types
		List<messageType> messageTypes = messagetypemanager.getActiveMessageTypes();
		mav.addObject("messageTypes", messageTypes);
		mav.addObject("id", configId);
		
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
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/configurations/details");
			
			//Need to get a list of active organizations.
			mav.addObject("organizations", organizations);
			//Need to get a list of active message types
			mav.addObject("messageTypes", messageTypes);
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
	 * @Objects		transportDetails will hold a empty object or an object containing the existing
	 * 				transport details for the selected configuration
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
		List<configurationTransport> transportDetails = configurationTransportManager.getTransportDetails(configId);
		
		//Set the variable id to hold the current configuration id
		mav.addObject("id",configId);
		
		//Get the configuration details for the selected config
		configuration configurationDetails = configurationmanager.getConfigurationById(configId);
		configurationDetails.setTransportDetails(transportDetails);
		
		//pass the configuration detail object back to the page.
		mav.addObject("configurationDetails", configurationDetails);
		
		//Set the variable to hold the number of completed steps for this configuration;
		mav.addObject("completedSteps",configurationDetails.getstepsCompleted());
		
		//Get the list of available transport methods
		List transportMethods = configurationTransportManager.getTransportMethods();
		mav.addObject("transportMethods", transportMethods);
		
		//Set a list of transport methods already set up for the configuration
		List <Integer> transportList = new ArrayList<Integer>();
		
		for(configurationTransport details : transportDetails) {
			transportList.add(details.gettransportMethod());
		}
		mav.addObject("usedTransportMethods",transportList);
		
		//Get the list of available file delimiters
		List delimiters = messagetypemanager.getDelimiters();
		mav.addObject("delimiters", delimiters);
		
		//Get the list of available file types
		List fileTypes = configurationmanager.getFileTypes();
		mav.addObject("fileTypes", fileTypes);
		
		return mav;
		
	}
	
	/**
	 * The 'addTransportMethod.do' POST request will associate the configuration to the selected
	 * transport Method.
	 * 
	 * @param	configId			The configId will hold the id of the current configuration
	 * @param	transportMethod		The transportMethod will hold the selected transport method
	 * 
	 * @return	The method will return a 1 back to the calling ajax function which will handle the
	 * 			page load.
	 */
	@RequestMapping(value="/addTransportMethod.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Integer addNewTransportMethod(@RequestParam int configId, @RequestParam int transportMethod, RedirectAttributes redirectAttr) throws Exception {
		
		configurationTransport transportDetails = new configurationTransport();
		transportDetails.setconfigId(configId);
		transportDetails.settransportMethod(transportMethod);
		transportDetails.setFile(null);
		
		configurationTransportManager.updateTransportDetails(transportDetails);
		
		return 1;
	}
	
	/**
	 * The '/transport' POST request will submit the transport details
	 * 
	 * @param	transportDetails	Will contain the contents of the transport form
	 * 
	 * @return	This function will either return to the transport details screen or
	 * 			redirect to the next step (Field Mappings)
	 */
	@RequestMapping(value="/transport", method = RequestMethod.POST)
	public ModelAndView updateTransportDetails(@Valid @ModelAttribute(value="transportDetails") configuration configurationDetails, BindingResult result, RedirectAttributes redirectAttr,@RequestParam String action) throws Exception {
		
		//Need to update the configuration completed step
		if(configurationDetails.getstepsCompleted() < 2) {
			configurationmanager.updateCompletedSteps(configId,2);
		}
		
		List<configurationTransport> details = configurationDetails.getTransportDetails();
		
		for(configurationTransport transport : details) {
			//submit the updates
			configurationTransportManager.updateTransportDetails(transport);
		}
		
		redirectAttr.addFlashAttribute("savedStatus", "updated");
		
		//If the "Save" button was pressed 
		if(action.equals("save")) {
			ModelAndView mav = new ModelAndView(new RedirectView("transport"));
			return mav;
		}
		else {
			ModelAndView mav = new ModelAndView(new RedirectView("mappings"));
			return mav;
		}
		
	}
	
	/**
	 * The '/mappings' GET request will determine based on the selected transport method
	 * what page to display. Either the choose fields page if 'online form' is selected
	 * or 'mappings' if a custom file is being uploaded.
	 */
	@RequestMapping(value="/mappings", method = RequestMethod.GET)
	public ModelAndView getConfigurationMappings() throws Exception {
		ModelAndView mav = new ModelAndView();
		
		configuration configurationDetails = configurationmanager.getConfigurationById(configId);
		
		//Set the variable to hold the number of completed steps for this configuration;
		mav.addObject("completedSteps",configurationDetails.getstepsCompleted());
		
		//Need to get the transport details
		configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configId);
		List<configurationFormFields> fields = configurationTransportManager.getConfigurationFields(configId);
		
		//Set the selected transport method for the selected configuration
		int transportMethod = transportDetails.gettransportMethod();
		
		//If transportMethod == 2 (Online Form) then send to the choose fields
		//page. else send to the mappings page.
		if(transportMethod == 2) {
			mav.setViewName("/administrator/configurations/chooseFields");
			
			
			//Need to return a list of associated fields for the selected message type
			List<configurationFormFields> copiedFields = configurationTransportManager.getConfigurationFields(configId);
			transportDetails.setFields(copiedFields);
			
			mav.addObject("transportDetails", transportDetails);
			
			//Get the list of available field validation types
			@SuppressWarnings("rawtypes")
			List validationTypes = messagetypemanager.getValidationTypes();
			mav.addObject("validationTypes", validationTypes);
			
		}
		else {
			//Need to return a list of associated fields for the selected message type
			List<configurationFormFields> uploadedFields = configurationTransportManager.getConfigurationFields(configId);
			transportDetails.setFields(uploadedFields);
			mav.addObject("transportDetails", transportDetails);
			
			//Need to return a list of selected template fields
			List<messageTypeFormFields> templateFields = messagetypemanager.getMessageTypeFields(configurationDetails.getMessageTypeId());
			mav.addObject("templateFields",templateFields);
			
			mav.setViewName("/administrator/configurations/mappings");
		}
		
		mav.addObject("id",configId);
		
		return mav;
		
	}
	
	
	/**
	 * The 'saveFields' POST method will submit the changes to the form field
	 * settings for the selected configuration. This method is only for configurations
	 * set for 'Online Form' as the data transportation method.
	 * 
	 * @param	transportDetails	The field details from the form
	 * 			action				The field that will hold which button was pressed
	 * 								"Save" or "Next Step"
	 * 	
	 * @return		This method will either redirect back to the Choose Fields page or
	 * 				redirect to the next step data translations page.
	 */
	@RequestMapping(value="/saveFields", method = RequestMethod.POST)
	public ModelAndView saveFormFields(@Valid @ModelAttribute(value="transportDetails") configurationTransport transportDetails, RedirectAttributes redirectAttr, @RequestParam String action) throws Exception {
		
		//Update the configuration completed step
		if(transportDetails.getconfigId() < 3) {
			configurationmanager.updateCompletedSteps(transportDetails.getconfigId(), 2);
		}
				
		//Get the list of fields
		List<configurationFormFields> fields = transportDetails.getFields();
		
		if(null != fields && fields.size() > 0) {
			for(configurationFormFields formfield : fields) {
				//If the message type field id is blank then set the use field to no
				if(formfield.getmessageTypeFieldId() == 0) {
					formfield.setUseField(false);
				}
				//Update each field
				configurationTransportManager.updateConfigurationFormFields(formfield);
			}
		}
		
		redirectAttr.addFlashAttribute("savedStatus", "updated");
		
		//If the "Save" button was pressed 
		if(action.equals("save")) {
			ModelAndView mav = new ModelAndView(new RedirectView("mappings"));
			return mav;
		}
		else {
			ModelAndView mav = new ModelAndView(new RedirectView("translations"));
			return mav;
		}
		
	}

}
