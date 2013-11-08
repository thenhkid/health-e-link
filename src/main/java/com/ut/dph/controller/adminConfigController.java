package com.ut.dph.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
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

@Controller
@RequestMapping("/administrator/configurations")

public class adminConfigController {
	
	@Autowired 
	private configurationManager configurationmanager;
	
	@Autowired
	private organizationManager organizationmanager;
	
	@Autowired
	private messageTypeManager messagetypemanager;
	
	/**
	 * The private variable configId will hold the configurationId when viewing a configuration
	 * this will be used when on a configuration subsections like Field Mappings, Data Translations,
	 * etc.
	 * We will use this private variable so we don't have to go fetch the id 
	 */
	private static int orgId = 0;
	
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
	

}
