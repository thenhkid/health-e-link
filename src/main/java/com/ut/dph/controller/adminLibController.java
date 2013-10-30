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

import com.ut.dph.model.messageType;
import com.ut.dph.service.messageTypeManager;

@Controller
@RequestMapping("/administrator/library")
public class adminLibController {
	
	@Autowired 
	private messageTypeManager messagetypemanager;
	
	/**
	 * The private maxResults variable will hold the number of results to show per
	 * list page.
	 */
	private static int maxResults = 20;
	
	/**
	 * The private variable messageTypeId will hold the messageTypeId when viewing a message type
	 * this will be used when on the message type subsection like mapping fields and data
	 * translations, etc.
	 * We will use this private variable so we don't have to go fetch the id or
	 * the messageTypeId based on the url.
	 */
	private static int messageTypeId = 0;
	
	/**
	 *  The '/list' GET request will serve up the existing list of message types
	 *  in the system
	 *  
	 * @param page			The page parameter will hold the page to view when pagination 
	 * 						is built.
	 * @return				The message type page list
	 * 
	 * @Objects				(1) An object containing all the found message types
	 * 				
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView listMessageTypes(@RequestParam(value="page", required=false) Integer page) throws Exception {
		
		if(page == null){
	        page = 1;
	    }
 
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/messageTypeLibrary/list");
        
        List<messageType> messageTypes = messagetypemanager.getMessageTypes(page,maxResults);
        mav.addObject("messageTypesList", messageTypes);
        
        //Return the total list of message types
        Long totalMessageTypes = messagetypemanager.findTotalMessageTypes();
        
        Integer totalPages = Math.round(totalMessageTypes/maxResults);
        mav.addObject("totalPages",totalPages);
        mav.addObject("currentPage",page);
        return mav;
 
	}
	
	/**
	 * The '/list' POST request will be used to search message types from the search form on the
	 * message type library manager list page.
	 * 
	 * @param searchTerm	The searchTerm parameter will hold the string to search on
	 * @return				The message type page list
	 * 
	 * @Objects				(1) An object will be returned holding the requested search term used to 
	 * 							populate the search box
	 * 						(3) An object containing the found message types
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	public ModelAndView findMessageTYpes(@RequestParam String searchTerm) throws Exception {
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/messageTypeLibrary/list");
        
        List<messageType> messageTypes = messagetypemanager.findMessageTypes(searchTerm);
        mav.addObject("searchTerm",searchTerm);
        mav.addObject("messageTypesList", messageTypes);		
        
        return mav;
 
	}
	
	/**
	 * The '/create' GET request will serve up the create new message type page
	 * 
	 * @return				The create new message type form
	 * 
	 * @Objects				(1) An object with a new message type
	 *  
	 * @throws Exception
	 */
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView createMessageType() throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/messageTypeLibrary/details");
		mav.addObject("messageTypeDetails", new messageType());
		return mav;
		
	}
	
	/**
	 * The '/create' POST request will be used to submit the new message type.
	 * 
	 * @param	messageTypeDetails The messageType object
	 * 
	 * @Objects	(1) An object will be returned holding the message type
	 * 
	 * @Returns The function will return the message type edit page.
	 */
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public ModelAndView createMessageType(@Valid messageType messageTypeDetails, BindingResult result,@RequestParam String action) throws Exception {
			
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/messageTypeLibrary/details");
			return mav;
		}
		 
		Integer id = null;
		id = (Integer) messagetypemanager.createMessageType(messageTypeDetails);
		
		if(action.equals("save")) {
			/**
			 * Set the private variable to hold the id of the new message type.
			 */
			messageTypeId = id;
			ModelAndView mav = new ModelAndView(new RedirectView("editMessageType?i="+id));
			return mav;		
		}
		else {
			ModelAndView mav = new ModelAndView(new RedirectView("mappings?i="+id));
			return mav;			
		}
	 }
	
	/**
	 * The '/editMessageType?i={num}' GET request will be used to return the details of the selected
	 * message type.
	 * 
	 * @param	i	The id of the selected message type
	 * 
	 * @Objects	(1) An object will be returned holding the message type
	 * 
	 * @Returns The function will return the message type edit page.
	 */
	 @RequestMapping(value="/editMessageType{num}", method = RequestMethod.GET)
	 public ModelAndView viewMessageTypeDetails(@RequestParam(value="i", required=true) Integer Id) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/messageTypeLibrary/details");
		
		//Set the static variable messageTypeId to hold the passed in id
		messageTypeId = Id;
		
		//Get the message type
		messageType messageTypeDetails = messagetypemanager.getMessageTypeById(Id);
		mav.addObject("messageTypeDetails", messageTypeDetails);
		mav.addObject("id",messageTypeId);
		return mav;
	 }
	 
	 /**
	 * The '/editMessageType?i={num}' POST request will be used to submit the details of the selected
	 * message type.
	 * 
	 * @param	i	The id of the selected message type
	 * 			messageTypeDetails The messageType object
	 * 
	 * @Objects	(1) An object will be returned holding the message type
	 * 
	 * @Returns The function will return the message type edit page.
	 */
	 @RequestMapping(value="/editMessageType{num}", method = RequestMethod.POST)
	 public ModelAndView updateMessageType(@Valid messageType messageTypeDetails, BindingResult result) throws Exception {
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/messageTypeLibrary/details");
			mav.addObject("id",messageTypeId);
			return mav;
		}
		 
		messagetypemanager.updateMessageType(messageTypeDetails);
		
		ModelAndView mav = new ModelAndView("/administrator/messageTypeLibrary/details");
		mav.addObject("success","updated");
		return mav;
	 }

}

