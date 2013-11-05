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

import com.ut.dph.model.Crosswalks;
import com.ut.dph.model.messageType;
import com.ut.dph.model.messageTypeDataTranslations;
import com.ut.dph.model.messageTypeFormFields;
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
	private static int maxResults = 10;
	
	/**
	 * The private variable messageTypeId will hold the messageTypeId when viewing a message type
	 * this will be used when on the message type subsection like mapping fields and data
	 * translations, etc.
	 * We will use this private variable so we don't have to go fetch the id or
	 * the messageTypeId based on the url.
	 */
	private static int messageTypeId = 0;
	
	private static List<messageTypeFormFields> fields = new ArrayList<messageTypeFormFields>();
	
	private static List<messageTypeDataTranslations> translations = null;
	
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
	public ModelAndView createMessageType(@Valid @ModelAttribute(value="messageTypeDetails") messageType messageTypeDetails, BindingResult result,@RequestParam String action, RedirectAttributes redirectAttr) throws Exception {
			
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/messageTypeLibrary/details");
			return mav;
		}
		 
		Integer id = null;
		id = (Integer) messagetypemanager.createMessageType(messageTypeDetails);
		
		redirectAttr.addFlashAttribute("savedStatus", "created");
		
		/**
		 * Set the private variable to hold the id of the new message type.
		 */
		messageTypeId = id;
		
		if(action.equals("save")) {
			
			ModelAndView mav = new ModelAndView(new RedirectView("details?i="+id));
			return mav;		
		}
		else {
			ModelAndView mav = new ModelAndView(new RedirectView("mappings"));
			return mav;			
		}
	 }
	
	/**
	 * The '/details?i={num}' GET request will be used to return the details of the selected
	 * message type.
	 * 
	 * @param	i	The id of the selected message type
	 * 
	 * @Objects	(1) An object will be returned holding the message type
	 * 
	 * @Returns The function will return the message type edit page.
	 */
	 @RequestMapping(value="/details{num}", method = RequestMethod.GET)
	 public ModelAndView viewMessageTypeDetails(@RequestParam(value="i", required=false) Integer Id) throws Exception {
		 
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/messageTypeLibrary/details");
		
		//Set the static variable messageTypeId to hold the passed in id
		if(Id != null) {
			messageTypeId = Id;
		}
		
		//Get the message type
		messageType messageTypeDetails = messagetypemanager.getMessageTypeById(messageTypeId);
		mav.addObject("messageTypeDetails", messageTypeDetails);
		mav.addObject("id",messageTypeId);
		return mav;
	 }
	 
	 /**
	 * The '/details?i={num}' POST request will be used to submit the details of the selected
	 * message type.
	 * 
	 * @param	i	The id of the selected message type
	 * 			messageTypeDetails The messageType object
	 * 
	 * @Objects	(1) An object will be returned holding the message type
	 * 
	 * @Returns The function will return the message type edit page.
	 */
	 @RequestMapping(value="/details{num}", method = RequestMethod.POST)
	 public ModelAndView updateMessageType(@Valid @ModelAttribute(value="messageTypeDetails") messageType messageTypeDetails, BindingResult result) throws Exception {
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/messageTypeLibrary/details");
			mav.addObject("id",messageTypeId);
			return mav;
		}
		 
		messagetypemanager.updateMessageType(messageTypeDetails);
		
		ModelAndView mav = new ModelAndView("/administrator/messageTypeLibrary/details");
		mav.addObject("savedStatus","updated");
		mav.addObject("id",messageTypeId);
		return mav;
	 }
	 
	 /**
	 * *********************************************************
	 * 			MESSAGE TYPE LIBRARY MAPPING FUNCTIONS					
	 * *********************************************************
	 */
	 
	 /**
	 * The '/mappings' GET request will display the Field Mappings page for the selected
	 * message type.
	 * 
	 */
	 @RequestMapping(value="/mappings", method = RequestMethod.GET)
	 public ModelAndView getFieldMappings() throws Exception {
		 
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/messageTypeLibrary/mappings");
		mav.addObject("id",messageTypeId);
		
		//Need to get the name of the message type
		messageType messageTypeDetails = messagetypemanager.getMessageTypeById(messageTypeId);
		
		//Need to return a list of associated fields for the selected message type
		List<messageTypeFormFields> fields = messagetypemanager.getMessageTypeFields(messageTypeId);
		messageTypeDetails.setFields(fields);
		
		mav.addObject("messageTypeDetails", messageTypeDetails);
		
		//Get the list of available information tables
		@SuppressWarnings("rawtypes")
		List infoTables = messagetypemanager.getInformationTables();
		mav.addObject("infoTables", infoTables);
		
		//Get the list of available field validation types
		@SuppressWarnings("rawtypes")
		List validationTypes = messagetypemanager.getValidationTypes();
		mav.addObject("validationTypes", validationTypes);
		
		return mav;
		 
	 }
	 
	 
	 /**
	 * The '/mappings' POST request will submit the fields mapping form from the 
	 * selected message type.
	 * 
	 * #param	messageTypeDetails
	 * 
	 * @Return	This function will redirect the user back to the mappings display page. 
	 * 			A redirect is being used so we can show the fields in the appropriate
	 * 			display order.
	 * 
	 */
	 @RequestMapping(value="/mappings", method = RequestMethod.POST)
	 public ModelAndView submitFieldMappings(@ModelAttribute(value="messageTypeDetails") messageType messageTypeDetails, RedirectAttributes redirectAttr) throws Exception {
		List<messageTypeFormFields> fields = messageTypeDetails.getFields();
	
		if(null != fields && fields.size() > 0) {
			adminLibController.fields = fields;
			
			for(messageTypeFormFields formfield : fields) {
				//Update each mappings
				messagetypemanager.updateMessageTypeFields(formfield);
			}
		}
		
		redirectAttr.addFlashAttribute("savedStatus", "updated");
		ModelAndView mav = new ModelAndView(new RedirectView("mappings"));
		return mav;	
		 
	 }
	 
	 /**
	  * The '/getTableCols.do' GET request will return a list of columns for the passed in
	  * table name
	  * 
	  * @param tableName
	  * 
	  * @return The function will return a list of column names.
	  */
	 @SuppressWarnings("rawtypes")
	 @RequestMapping(value="/getTableCols.do", method = RequestMethod.GET)
	 public @ResponseBody List getTableCols(@RequestParam(value= "tableName", required = true) String tableName) {
		 
		 List columns = messagetypemanager.getTableColumns(tableName);
		 return columns;
	 }
	 
	 /**
	 * *********************************************************
	 * 		MESSAGE TYPE LIBRARY DATA TRANSLATIONS FUNCTIONS					
	 * *********************************************************
	 */
	 
	 /**
	 * The '/translations' GET request will display the Field Mappings page for the selected
	 * message type.
	 * 
	 */
	 @RequestMapping(value="/translations", method = RequestMethod.GET)
	 public ModelAndView getDataTranslations() throws Exception {
		 
		//Set the data translations array to get ready to hold data
	    translations = new ArrayList<messageTypeDataTranslations>();
		 
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/messageTypeLibrary/translations");
		mav.addObject("id",messageTypeId);
		
		//Need to return a list of associated fields for the selected message type
		List<messageTypeFormFields> fields = messagetypemanager.getMessageTypeFields(messageTypeId);
		mav.addObject("fields",fields);
		
		//Return a list of available crosswalks
		List<Crosswalks> crosswalks = messagetypemanager.getCrosswalks(1,0);
		mav.addObject("crosswalks",crosswalks);
		
		return mav;
	 }
	 
	 /**
	 * The '/translations' POST request will submit the selected data translations
	 * and save it to the data base.
	 * 
	 */
	 @RequestMapping(value="/translations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	 public @ResponseBody Integer submitDataTranslations() throws Exception {
		 
		 //Loop through the list of translations
		 for(messageTypeDataTranslations translation : translations) {
			 messagetypemanager.saveDataTranslations(translation);
		 }
		
		 return 1;
	 }
	 
	 /**
	 * The '/getCrosswalks.do' function will return all the available crosswalks.
	 * 
	 * @Return list of crosswalks
	 */
	 @RequestMapping(value="/getCrosswalks.do", method = RequestMethod.GET)
	 public @ResponseBody ModelAndView getCrosswalks(@RequestParam(value="page", required=false) Integer page) throws Exception {
		 
		if(page == null){
		     page = 1;
	    }
		
		ModelAndView mav = new ModelAndView(); 
		mav.setViewName("/administrator/messageTypeLibrary/crosswalks");	 
		
		//Need to return a list of crosswalks
		List<Crosswalks> crosswalks = messagetypemanager.getCrosswalks(page,4);
		mav.addObject("availableCrosswalks", crosswalks);
		
		//Find out the total number of crosswalks
		Long totalCrosswalks = messagetypemanager.findTotalCrosswalks();
		
		double maxCrosswalks = 4;
		
		Integer totalPages = (int) Math.round(totalCrosswalks/maxCrosswalks);
        mav.addObject("totalPages",totalPages);
        mav.addObject("currentPage",page);
		
		return mav;
		
	 }
	 
	 /**
	 * The '/getTranslations.do' function will return the list of existing translations
	 * set up for the selected message type..
	 * 
	 * @Return list of translations
	 */
	 @RequestMapping(value="/getTranslations.do", method = RequestMethod.GET)
	 public @ResponseBody ModelAndView getTranslations() throws Exception {
		 
		ModelAndView mav = new ModelAndView(); 
		mav.setViewName("/administrator/messageTypeLibrary/existingTranslations");	 
		
		//Need to get a list of existing translations
		List<messageTypeDataTranslations> existingTranslations = messagetypemanager.getMessageTypeTranslations(messageTypeId);
		
		for(messageTypeDataTranslations translation : existingTranslations) {
			//Get the field name by id
			String fieldName = messagetypemanager.getFieldName(translation.getFieldId());
			translation.setfieldName(fieldName);
			
			//Get the crosswalk name by id
			String crosswalkName = messagetypemanager.getCrosswalkName(translation.getCrosswalkId());		
			translation.setcrosswalkName(crosswalkName);
			
			translations.add(translation);
		}
		
		mav.addObject("dataTranslations",translations);	
		
		return mav;
		
	 }
	 
	 
	 /**
	 * The '/newCrosswalk' GET request will be used to return a blank crosswalk
	 * form.
	 * 
	 * 
	 * @return		The crosswalk details page
	 * 
	 * @Objects		(1) An object that will hold all the details of the clicked crosswalk
	 *
	 */
	 @RequestMapping(value="/newCrosswalk", method= RequestMethod.GET)
	 public @ResponseBody ModelAndView newCrosswalk() throws Exception {
		 
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/messageTypeLibrary/crosswalkDetails");	
		
		Crosswalks crosswalkDetails = new Crosswalks();
		mav.addObject("crosswalkDetails",crosswalkDetails);
		mav.addObject("btnValue","Create");
		
		//Get the list of available file delimiters
		@SuppressWarnings("rawtypes")
		List delimiters = messagetypemanager.getDelimiters();
		mav.addObject("delimiters", delimiters);
		
		return mav;
	 }
	 
	 /**
	 * The '/createCrosswalk' function will be used to create a new crosswalk
	 * 
	 * @Return The function will either return the crosswalk form on error or 
	 * 		   redirect to the data translation page.
	 */
	 @RequestMapping(value="/createCrosswalk", method = RequestMethod.POST)
	 public @ResponseBody ModelAndView createCrosswalk(@ModelAttribute(value="crosswalkDetails") Crosswalks crosswalkDetails, BindingResult result, RedirectAttributes redirectAttr) throws Exception {
		
		messagetypemanager.createCrosswalk(crosswalkDetails);
	
		redirectAttr.addFlashAttribute("savedStatus", "created");
		ModelAndView mav = new ModelAndView(new RedirectView("translations"));
		return mav;	
	}
	 
	/**
	* The '/viewCrosswals{params}' function will return the details of the selected
	* crosswalk. The results will be displayed in the overlay.
	* 
	* @Param	i	This will hold the id of the selected crosswalk
	* 
	* @Return	This function will return the crosswalk details view.
	*/
	@RequestMapping(value="/viewCrosswalk{params}", method = RequestMethod.GET)
	public @ResponseBody ModelAndView viewCrosswalk(@RequestParam(value="i", required=true) Integer cwId) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/messageTypeLibrary/crosswalkDetails");	
		
		//Get the details of the selected crosswalk
		Crosswalks crosswalkDetails = messagetypemanager.getCrosswalk(cwId);
		mav.addObject("crosswalkDetails", crosswalkDetails);
		
		//Get the data associated with the selected crosswalk
		@SuppressWarnings("rawtypes")
		List crosswalkData = messagetypemanager.getCrosswalkData(cwId);
		mav.addObject("crosswalkData", crosswalkData);
		
		return mav;
		
	}
	
	/**
	* The '/setTranslations{params}' function will handle taking in a selected field
	* and a selected crosswalk and add it to an array of translations. This array
	* will be used when the form is submitted to associate to the existing message type.
	* 
	* @param f	This will hold the id of the selected field
	* 		cw		This will hold the id of the selected crosswalk
	* 		fText	This will hold the text value of the selected field (used for display purposes)
	* 		CWText	This will hold the text value of the selected crosswalk (used for display purposes)
	* 
	* @Return	This function will return the existing translations view that will display the table of
	* 			newly selected translations
	*/
	@RequestMapping(value="/setTranslations{params}", method = RequestMethod.GET)
	public @ResponseBody ModelAndView setTranslations(@RequestParam(value="f", required=true) Integer field, @RequestParam(value="cw", required=true) Integer cwId, @RequestParam(value="fText", required=true) String fieldText, @RequestParam(value="CWText", required=true) String cwText) throws Exception {
		
		int processOrder = translations.size()+1;
		
		messageTypeDataTranslations translation = new messageTypeDataTranslations();
		translation.setMessageTypeId(messageTypeId);
		translation.setFieldId(field);
		translation.setfieldName(fieldText);
		translation.setCrosswalkId(cwId);
		translation.setcrosswalkName(cwText);
		translation.setProcessOrder(processOrder);
		
		translations.add(translation);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/messageTypeLibrary/existingTranslations");	
		mav.addObject("dataTranslations",translations);
		
		return mav;
		
	}

}

