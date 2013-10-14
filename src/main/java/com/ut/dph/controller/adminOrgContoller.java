package com.ut.dph.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ut.dph.model.Organization;
import com.ut.dph.model.userAccess;
import com.ut.dph.service.organizationManager;
import com.ut.dph.model.User;
import com.ut.dph.service.userManager;
import com.ut.dph.model.siteSections;

/**
 * The adminOrgController class will handle all URL requests that fall inside of
 * the '/administrator/organizations' url path. 
 * 
 * This path will be used when the administrator is managing and existing organization
 * or creating a new organization
 * 
 * @author chadmccue
 *
 */

@Controller
@RequestMapping("/administrator/organizations")
public class adminOrgContoller {
	
	@Autowired 
	private organizationManager organizationManager;
	
	@Autowired
	private userManager userManager;
	
	/**
	 * The private variable orgId will hold the orgId when viewing an organization
	 * this will be used when on a organization subsection like users, etc.
	 * We will use this private variable so we don't have to go fetch the id or
	 * the organization based on the url.
	 */
	private static int orgId = 0;
	
	/**
	 * The private maxResults variable will hold the number of results to show per
	 * list page.
	 */
	private static int maxResults = 20;
	
	/**
	 *  The '/list' GET request will serve up the existing list of organizations
	 *  in the system
	 *  
	 * @param page			The page parameter will hold the page to view when pagination 
	 * 						is built.
	 * @return				The organization page list
	 * 
	 * @Objects				(1) An object containing all the found organizations
	 * 						(2) An object will be returned that hold the organiationManager so we can
	 * 							run some functions on each returned org in the list
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView listOrganizations(@RequestParam(value="page", required=false) Integer page) throws Exception {
		
		if(page == null){
	        page = 1;
	    }
 
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/listOrganizations");
        
        List<Organization> organizations = organizationManager.getOrganizations(page,maxResults);
        mav.addObject("orgFunctions",organizationManager);
        mav.addObject("organizationList", organizations);
        
        //Return the total list of organizations
        Long totalOrgs = organizationManager.findTotalOrgs();
        
        Integer totalPages = Math.round(totalOrgs/maxResults);
        mav.addObject("totalPages",totalPages);
        mav.addObject("currentPage",page);
        return mav;
 
	}
	
	
	/**
	 * The '/list' POST request will be used to search organization from the search form on the
	 * organization list page.
	 * 
	 * @param searchTerm	The searchTerm parameter will hold the string to search on
	 * @return				The organization page list
	 * 
	 * @Objects				(1) An object will be returned holding the requested search term used to 
	 * 							populate the search box
	 * 						(2) An object will be returned that hold the organiationManager so we can
	 * 							run some functions on each returned org in the list
	 * 						(3) An object containing the found organizations
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	public ModelAndView findOrganizations(@RequestParam String searchTerm) throws Exception {
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/listOrganizations");
        
        List<Organization> organizations = organizationManager.findOrganizations(searchTerm);
        mav.addObject("searchTerm",searchTerm);
        mav.addObject("orgFunctions",organizationManager);
        mav.addObject("organizationList", organizations);		
        
        return mav;
 
	}
	
	
	/**
	 * The '/create' GET request will serve up the create new organization page
	 * 
	 * @return				The create new organization form
	 * 
	 * @Objects				(1) An object with a new organization 
	 * @throws Exception
	 */
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView createOrganization() throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/organizations/organizationDetails");
		mav.addObject("organization", new Organization());
		
		//Get the object that will hold the states
		mav.addObject("stateList",getStates());
		return mav;
		
	}
	
	
	/**
	 * The '/create' POST request will submit the new organization once all required fields
	 * are checked, the system will also check to make sure the organziation name is not already
	 * in use.
	 * 
	 * @param organization		The object holding the organization form fields
	 * @param result			The validation result
	 * @param redirectAttr		The variable that will hold values that can be read after the redirect
	 * @param action			The variable that holds which button was pressed
	 * 
	 * @return					Will return the organization list page on "Save & Close"
	 * 							Will return the organization details page on "Save"
	 * 							Will return the organization create page on error
	 * @throws Exception
	 */
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public ModelAndView saveNewOrganization(@Valid Organization organization, BindingResult result, RedirectAttributes redirectAttr,@RequestParam String action) throws Exception {
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/organizations/organizationDetails");
			//Get the object that will hold the states
			mav.addObject("stateList",getStates());
			return mav;
		}
		
		List<Organization> existing = organizationManager.getOrganizationByName(organization.getcleanURL());
	    if (!existing.isEmpty()) {
        	ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/organizations/organizationDetails");
			mav.addObject("id",organization.getId());
			mav.addObject("existingOrg","Organization "+organization.getOrgName()+" already exists.");
			//Get the object that will hold the states
			mav.addObject("stateList",getStates());
			return mav;	
        }
		
		Integer id = null;
		id = (Integer) organizationManager.createOrganization(organization);
		
		//Get the organization name that was just added
		Organization latestorg = organizationManager.getOrganizationById(id);
		
		redirectAttr.addFlashAttribute("savedStatus", "success");
		
		if(action.equals("save")) {
			/**
			 * Set the private variable to hold the id of the new organization.
			 */
			orgId = id;
			ModelAndView mav = new ModelAndView(new RedirectView(latestorg.getcleanURL()+"/"));
			return mav;		
		}
		else {
			ModelAndView mav = new ModelAndView(new RedirectView("list"));
			return mav;			
		}
		
	}
	
	
	/**
	 * The '/{cleanURL}' GET request will display the clicked organization details page.
	 * 
	 * @param cleanURL		The {clearnURL} will be the organizations name with spaces removed. This was set 
	 * 						when the organization was created.
	 * 
	 * @return				Will return the organization details page.
	 * 
	 * @Objects				(1) The object containing all the information for the clicked org
	 * 						(2) The 'id' of the clicked org that will be used in the menu and action bar
	 * 
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="/{cleanURL}", method = RequestMethod.GET)
	public ModelAndView viewOrganizationDetails(@PathVariable String cleanURL) throws Exception {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/organizations/organizationDetails");
		
		List<Organization> organization = organizationManager.getOrganizationByName(cleanURL);
		Organization orgDetails = organization.get(0);
		
		/**
		 * Set the private variable to hold the id of the clicked organization.
		 */
		orgId = orgDetails.getId();
		
		mav.addObject("id",orgId);
		mav.addObject("organization",orgDetails);
		
		//Get the object that will hold the states
		mav.addObject("stateList",getStates());
		
		return mav;
	
	}
	
	
	/**
	 * The '/{cleanURL}' POST request will handle submitting changes for the selected organization.
	 * 
	 * @param organization		The object containing the organization form fields
	 * @param result			The validation result
	 * @param redirectAttr		The variable that will hold values that can be read after the redirect
	 * @param action			The variable that holds which button was pressed
	 * 
	 * @return					Will return the organization list page on "Save & Close"
	 * 							Will return the organization details page on "Save"
	 * 							Will return the organization create page on error
	 * 
	 * @Objects					(1) The object containing all the information for the clicked org
	 * 							(2) The 'id' of the clicked org that will be used in the menu and action bar
	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}", method = RequestMethod.POST)
	public ModelAndView updateOrganization(@Valid Organization organization, BindingResult result, RedirectAttributes redirectAttr,@RequestParam String action) throws Exception {

		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/organizations/organizationDetails");
			mav.addObject("id",orgId);
			//Get the object that will hold the states
			mav.addObject("stateList",getStates());
			return mav;
		}
		
		Organization currentOrg = organizationManager.getOrganizationById(organization.getId());
		
		if(!currentOrg.getcleanURL().trim().equals(organization.getcleanURL().trim())) {
			List<Organization> existing = organizationManager.getOrganizationByName(organization.getcleanURL());
		    if (!existing.isEmpty()) {
	        	ModelAndView mav = new ModelAndView();
				mav.setViewName("/administrator/organizations/organizationDetails");
				mav.addObject("id",orgId);
				mav.addObject("existingOrg","Organization "+organization.getOrgName().trim()+" already exists.");
				//Get the object that will hold the states
				mav.addObject("stateList",getStates());
				return mav;	
	        }
		}
		
		//Update the organization
		organizationManager.updateOrganization(organization);
	
		//This variable will be used to display the message on the details form
		redirectAttr.addFlashAttribute("savedStatus", "updated");
		
		//If the "Save" button was pressed 
		if(action.equals("save")) {
			ModelAndView mav = new ModelAndView(new RedirectView("../"+organization.getcleanURL()+"/"));
			return mav;		
		}
		//If the "Save & Close" button was pressed.
		else {
			ModelAndView mav = new ModelAndView(new RedirectView("../list"));
			return mav;			
		}
	
	}
	
	/**
	 * The '/{cleanURL}/delete POST request will remove the clicked organization and anything associated
	 * to it.
	 * 
	 * @param id	The variable that holds the id of the clicked organization
	 * 
	 * @Return		Will return the organization list page
	 * 
	 */
	@RequestMapping(value="/{cleanURL}/delete", method = RequestMethod.POST)
	public ModelAndView deleteOrganization(@RequestParam int id, RedirectAttributes redirectAttr) throws Exception {
		
		organizationManager.deleteOrganization(id);
		
		//This variable will be used to display the message on the details form
		redirectAttr.addFlashAttribute("savedStatus", "deleted");
		ModelAndView mav = new ModelAndView(new RedirectView("../list"));
		return mav;		
	}
	
	
	/**
	 * The '/{cleanURL/users' GET request will display the list of system users for the selected 
	 * organization.
	 * 
	 * @param cleanURL		The variable that holds the organization that is being viewed
	 * 
	 * @return				Will return the organization user list page
	 * 
	 * @Objects				(1) An object that holds users found for the organization
	 * 						(2) The userManager object so we can run some functions on each
	 * 							user returned.
	 * 						(3)	The orgId used for the menu and action bar
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/users", method = RequestMethod.GET)
	public ModelAndView listOrganizationUsers(@PathVariable String cleanURL, @RequestParam(value="page", required=false) Integer page) throws Exception {
		
		if(page == null) {
			page = 1;
		}
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/organizationUsers");
     
        List<User> users = organizationManager.getOrganizationUsers(orgId,page,maxResults);
        mav.addObject("id",orgId);
        mav.addObject("userFunctions",userManager);
        mav.addObject("userList", users);		
        
        //Return the total list of users for the organization
        Long totalUsers = organizationManager.findTotalUsers(orgId);
        
        Integer totalPages = Math.round(totalUsers/maxResults);
        mav.addObject("totalPages",totalPages);
        mav.addObject("currentPage",page);
        
        return mav;
 
	}
	
	
	/**
	 * The '/{cleanURL}/users' POST request will be used to search users for the selected organization 
	 * 
	 * @param searchTerm	The searchTerm parameter will hold the string to search on
	 * @return				The organization user list page
	 * 
	 * @Objects				(1) An object will be returned holding the requested search term used to 
	 * 							populate the search box
	 * 						(2) An object will be returned that hold the userManager so we can
	 * 							run some functions on each returned user in the list
	 * 						(3) An object containing the found users
	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/users", method = RequestMethod.POST)
	public ModelAndView findUsers(@RequestParam String searchTerm, @PathVariable String cleanURL) throws Exception {
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/organizationUsers");
        
        List<User> users = userManager.findUsers(orgId, searchTerm);
        mav.addObject("id",orgId);
        mav.addObject("searchTerm",searchTerm);
        mav.addObject("userFunctions",userManager);
        mav.addObject("userList", users);		
        
        return mav;
 
	}
	
	
	/**
	 * The '/{cleanURL}/users/newSystemUser' GET request will be used to display the blank new 
	 * system user screen (In a modal)
	 *
	 * 
	 * @return		The organization user blank form page
	 * 
	 * @Objects		(1) An object that will hold all the form fields of a new user
	 * 				(2) An object to hold the button value "Create"
	 *
	 */
	@RequestMapping(value="/{cleanURL}/newSystemUser", method = RequestMethod.GET)
	public @ResponseBody ModelAndView newSystemUser() throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/organizations/userDetails");
		User userdetails = new User();
		
		//Set the id of the organization for the new user
		userdetails.setOrgId(orgId);
		mav.addObject("btnValue","Create");
		mav.addObject("userdetails", userdetails);
		
		//Get All Available user sections
		List<siteSections> sections = userManager.getSections();
		mav.addObject("sections", sections);
		
		return mav;
	}
	
	
	/**
	 * The '/{cleanURL}/users/create' POST request will handle submitting the new organization system user.
	 * 
	 * @param user				The object containing the system user form fields
	 * @param result			The validation result
	 * @param redirectAttr		The variable that will hold values that can be read after the redirect
	 * 
	 * @return					Will return the system user list page on "Save"
	 * 							Will return the system user form page on error
	 * 
	 * @Objects					(1) The object containing all the information for the clicked org
	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/create", method = RequestMethod.POST)
	public @ResponseBody ModelAndView createsystemUser(@Valid @ModelAttribute(value="userdetails") User userdetails, BindingResult result,RedirectAttributes redirectAttr, @PathVariable String cleanURL ) throws Exception {
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/organizations/userDetails");
			mav.addObject("btnValue","Create");
			return mav;
		}
		
		User existing = userManager.getUserByUserName(userdetails.getUsername());
		
	    if (existing != null) {
	    	ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/organizations/userDetails");
			mav.addObject("btnValue","Create");
			mav.addObject("existingUsername","Username "+userdetails.getUsername().trim()+" already exists.");
			return mav;	
        }
		
		userManager.createUser(userdetails);
		
		ModelAndView mav = new ModelAndView("/administrator/organizations/userDetails");
		mav.addObject("success","userCreated");
		return mav;		
	}
	

	 
	 /**
	 * The '/{cleanURL}/users/update' POST request will handle submitting changes for the selected organization system user.
	 * 
	 * @param user				The object containing the system user form fields
	 * @param result			The validation result
	 * @param redirectAttr		The variable that will hold values that can be read after the redirect
	 * 
	 * @return					Will return the system user list page on "Save"
	 * 							Will return the system user form page on error
	 * 
	 * @Objects					(1) The object containing all the information for the clicked org
	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/update", method = RequestMethod.POST)
	public @ResponseBody ModelAndView updatesystemUser(@Valid @ModelAttribute(value="userdetails") User userdetails, BindingResult result,RedirectAttributes redirectAttr, @PathVariable String cleanURL ) throws Exception {
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/organizations/userDetails");
			mav.addObject("btnValue","Update");
			return mav;
		}
		
		User currentUser = userManager.getUserById(userdetails.getId());
		
		if(!currentUser.getUsername().trim().equals(userdetails.getUsername().trim())) {
			User existing = userManager.getUserByUserName(userdetails.getUsername());
		    if (existing != null) {
	        	ModelAndView mav = new ModelAndView();
				mav.setViewName("/administrator/organizations/userDetails");
				mav.addObject("btnValue","Update");
				mav.addObject("existingUsername","Username "+userdetails.getUsername().trim()+" already exists.");
				return mav;	
	        }
		}
		
		userManager.updateUser(userdetails);
	
		ModelAndView mav = new ModelAndView("/administrator/organizations/userDetails");
		mav.addObject("success","userUpdated");
		return mav;		
	}
	
	/**
	 * The '/{cleanURL}/users/{person}?i=##' GET request will be used to return the details of the selected
	 * user.
	 * 
	 * @param 	i	The id of the user selected
	 * 
	 * @return		The organization user details page
	 * 
	 * @Objects		(1) An object that will hold all the details of the clicked user
	 * 				(2) An object that will hold all the available sections the user can have access to
	 *
	 */
	 @RequestMapping(value="/{cleanURL}/{person}", method= RequestMethod.GET)
	 @ResponseBody 
	 public ModelAndView viewUserDetails(@RequestParam(value="i", required=true) Integer userId) throws Exception {
		 
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/organizations/userDetails");	
		
		//Get all the details for the clicked user
		User userDetails = userManager.getUserById(userId);
				
		mav.addObject("userId",userDetails.getId());
		mav.addObject("btnValue","Update");
		mav.addObject("userdetails",userDetails);
		
		//Get All Available user sections
		List<siteSections> sections = userManager.getSections();
		mav.addObject("sections", sections);
		
		//Return the sections for the clicked user
		List<userAccess> userSections = userManager.getuserSections(userId);
		List<Integer> userSectionList = new ArrayList<Integer>();  
		
		for(int i=0;i<userSections.size();i++) {
			userSectionList.add(userSections.get(i).getFeatureId());  
		}
		
		userDetails.setsectionList(userSectionList);
		
		return mav;
		 
	 }
	 
	 protected Map getStates() throws Exception {
		
		LinkedHashMap<String,String> states = new LinkedHashMap<String,String>();
		
		states.put("AL", "Alabama");
		states.put("AK", "Alaska");
		states.put("AZ", "Arizona");
		states.put("AR", "Arkansas");
		states.put("CA", "California");
		states.put("CO", "Colorado");
		states.put("DE", "Delaware");
		states.put("DC", "District of Columbia");
		states.put("FL", "Florida");
		states.put("GA", "Georgia");
		states.put("HI", "Hawaii");
		states.put("ID", "Idaho");
		states.put("IL", "Illinois");
		states.put("IN", "Indiana");
		states.put("IA", "Iowa");
		states.put("KS", "Kansas");
		states.put("KY", "Kentucky");
		states.put("LA", "Louisiana");
		states.put("ME", "Maine");
		states.put("MD", "Maryland");
		states.put("MA", "Massachusetts");
		states.put("MI", "Michigan");
		states.put("MN", "Minnesota");
		states.put("MS", "Mississippi");
		states.put("MO", "Missouri");
		states.put("MT", "Montana");
		states.put("NE", "Nebraska");
		states.put("NV", "Nevada");
		states.put("NH", "New Hampshire");
		states.put("NJ", "New Jersey");
		states.put("NM", "New Mexico");
		states.put("NY", "New York");
		states.put("NC", "North Carolina");
		states.put("ND", "North Dakota");
		states.put("OH", "Ohio");
		states.put("OK", "Oklahoma");
		states.put("OR", "Oregon");
		states.put("PA", "Pennsylvania");
		states.put("RI", "Rhode Island");
		states.put("SC", "South Carolina");
		states.put("SD", "South Dakota");
		states.put("TN", "Tennessee");
		states.put("TX", "Texas");
		states.put("UT", "Utah");
		states.put("VT", "Vermont");
		states.put("VA", "Virginia");
		states.put("WA", "Washington");
		states.put("WV", "West Virginia");
		states.put("WI", "Wisconsin");
		states.put("WY", "Wyoming");
		
		return states;
	}

}
