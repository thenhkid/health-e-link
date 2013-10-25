package com.ut.dph.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ut.dph.model.Organization;
import com.ut.dph.model.providerAddress;
import com.ut.dph.model.userAccess;
import com.ut.dph.service.organizationManager;
import com.ut.dph.model.User;
import com.ut.dph.service.userManager;
import com.ut.dph.model.siteSections;
import com.ut.dph.model.Provider;
import com.ut.dph.service.providerManager;
import com.ut.dph.model.Brochure;
import com.ut.dph.service.brochureManager;
import com.ut.dph.reference.USStateList;
import com.ut.dph.reference.fileSystem;

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
	
	@Autowired
	private providerManager providerManager;
	
	@Autowired
	private brochureManager brochureManager;
	
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
		
		//Get a list of states
		USStateList stateList = new USStateList();
		
		//Get the object that will hold the states
		mav.addObject("stateList",stateList.getStates());
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
		
		//Get a list of states
		USStateList stateList = new USStateList();
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/organizations/organizationDetails");
			//Get the object that will hold the states
			mav.addObject("stateList",stateList.getStates());
			return mav;
		}
		
		List<Organization> existing = organizationManager.getOrganizationByName(organization.getcleanURL());
	    if (!existing.isEmpty()) {
        	ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/organizations/organizationDetails");
			mav.addObject("id",organization.getId());
			mav.addObject("existingOrg","Organization "+organization.getOrgName()+" already exists.");
			//Get the object that will hold the states
			mav.addObject("stateList",stateList.getStates());
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
		
		//Get a list of states
		USStateList stateList = new USStateList();
		
		//Get the object that will hold the states
		mav.addObject("stateList",stateList.getStates());
		
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
		
		//Get a list of states
		USStateList stateList = new USStateList();
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/organizations/organizationDetails");
			mav.addObject("id",orgId);
			//Get the object that will hold the states
			mav.addObject("stateList",stateList.getStates());
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
				mav.addObject("stateList",stateList.getStates());
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
	* *********************************************************
	* 				ORGANIZATION USER FUNCTIONS					
	* *********************************************************
	*/
	
	
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
	 * The '/{cleanURL}/create' POST request will handle submitting the new organization system user.
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
	 * The '/{cleanURL}/user/{person}?i=##' GET request will be used to return the details of the selected
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
	 @RequestMapping(value="/{cleanURL}/user/{person}", method= RequestMethod.GET)
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
	 
	 
	 /**
	 * *********************************************************
	 * 			ORGANIZATION PROVIDER FUNCTIONS					
	 * *********************************************************
	 */
	 
	 /**
	 * The '/{cleanURL/providers' GET request will display the list of providers for the selected 
	 * organization.
	 * 
	 * @param cleanURL		The variable that holds the organization that is being viewed
	 * 
	 * @return				Will return the organization provider list page
	 * 
	 * @Objects				(1) An object that holds providers found for the organization
	 * 						(2)	The orgId used for the menu and action bar
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/providers", method = RequestMethod.GET)
	public ModelAndView listOrganizationProviders(@PathVariable String cleanURL, @RequestParam(value="page", required=false) Integer page) throws Exception {
		
		if(page == null) {
			page = 1;
		}
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/providers/list");
     
        List<Provider> providers = organizationManager.getOrganizationProviders(orgId,page,maxResults);
        mav.addObject("id",orgId);
        mav.addObject("providerList", providers);		
        
        //Return the total list of providers for the organization
        Long totalProviders = organizationManager.findTotalProviders(orgId);
        
        Integer totalPages = Math.round(totalProviders/maxResults);
        mav.addObject("totalPages",totalPages);
        mav.addObject("currentPage",page);
        
        return mav;
       
	} 
	
	/**
	 * The '/{cleanURL}/providers' POST request will be used to search providers for the selected organization 
	 * 
	 * @param searchTerm	The searchTerm parameter will hold the string to search on
	 * @return				The organization provider list page
	 * 
	 * @Objects				(1) An object will be returned holding the requested search term used to 
	 * 							populate the search box
	 * 						(2) An object containing the found providers
	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/providers", method = RequestMethod.POST)
	public ModelAndView findProviders(@RequestParam String searchTerm, @PathVariable String cleanURL) throws Exception {
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/providers/list");
        
        List<Provider> providers = providerManager.findProviders(orgId, searchTerm);
        mav.addObject("id",orgId);
        mav.addObject("searchTerm",searchTerm);
        mav.addObject("providerList", providers);		
        
        return mav;
 
	}
	
	/**
	 *  The '/{cleanURL}/provider.create' GET request will be used to create a new provider
	 *  
	 *  @return The blank organization provider page
	 *  
	 *  @Objects (1) An object that will hold the blank provider
	 */
	@RequestMapping(value="/{cleanURL}/provider.create", method = RequestMethod.GET)
	public ModelAndView newProviderForm() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/organizations/providers/details");	
		
		//Create a new blank provider.
		Provider providerDetails = new Provider();
		mav.addObject("providerId",0);
		
		//Set the id of the organization to add the provider to
		providerDetails.setOrgId(orgId);
		
		mav.addObject("id",orgId);
		mav.addObject("providerdetails",providerDetails);
		
		return mav;
	}
	
	/**
	 * The '//{cleanURL}/provider.create' POST request will handle submitting the new provider.
	 * 
	 * @param providerdetails	The object containing the provider form fields
	 * @param result			The validation result
	 * @param redirectAttr		The variable that will hold values that can be read after the redirect
	 * @param action			The variable that holds which button was pressed
	 * 
	 * @return					Will return the provider list page on "Save & Close"
	 * 							Will return the provider details page on "Save"
	 * 							Will return the provider create page on error
	 * 
	 * @Objects					(1) The object containing all the information for the new provider
	 * 							(2) The 'id' of the clicked org that will be used in the menu and action bar
	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/provider.create", method = RequestMethod.POST)
	public ModelAndView createProvider(@Valid @ModelAttribute(value="providerdetails") Provider providerdetails, BindingResult result, RedirectAttributes redirectAttr,@RequestParam String action) throws Exception {
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/organizations/providers/details");
			mav.addObject("providerId",providerdetails.getId());
			mav.addObject("id",orgId);
			return mav;
		}
		
		//Create the provider
		Integer providerId = providerManager.createProvider(providerdetails);
	
		//If the "Save" button was pressed 
		if(action.equals("save")) {
			//This variable will be used to display the message on the details form
			redirectAttr.addFlashAttribute("savedStatus", "created");
			ModelAndView mav = new ModelAndView(new RedirectView("provider."+providerdetails.getFirstName()+providerdetails.getLastName()+"?i="+providerId));
			return mav;
		}
		//If the "Save & Close" button was pressed.
		else {
			//This variable will be used to display the message on the details form
			redirectAttr.addFlashAttribute("savedStatus", "created");
			
			ModelAndView mav = new ModelAndView(new RedirectView("providers"));
			return mav;			
		}
	
	}
	
	
	/**
	 * The '/{cleanURL}/provider.{person}?i=##' GET request will be used to return the details of the selected
	 * provider.
	 * 
	 * @param 	i	The id of the provider selected
	 * 
	 * @return		The organization provider details page
	 * 
	 * @Objects		(1) An object that will hold all the details of the clicked provider
	 * 				(2) An object that will hold all the available sections the provider can have access to
	 *
	 */
	 @RequestMapping(value="/{cleanURL}/provider.{person}", method= RequestMethod.GET)
	 public ModelAndView viewProviderDetails(@RequestParam(value="i", required=true) Integer providerId) throws Exception {
		 
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/organizations/providers/details");	
		
		//Get all the details for the clicked provider
		Provider providerDetails = providerManager.getProviderById(providerId);
		
		//Set the provider addresses
		List<providerAddress> addresses = providerManager.getProviderAddresses(providerId);
		providerDetails.setProviderAddresses(addresses);	
		mav.addObject("providerId",providerDetails.getId());
		mav.addObject("id",orgId);
		mav.addObject("providerdetails",providerDetails);
		
		return mav;
	 }
	 
	 /**
	 * The '//{cleanURL}/provider.{person}?i=##' POST request will handle submitting changes for the selected provider.
	 * 
	 * @param providerdetails	The object containing the provider form fields
	 * @param result			The validation result
	 * @param redirectAttr		The variable that will hold values that can be read after the redirect
	 * @param action			The variable that holds which button was pressed
	 * 
	 * @return					Will return the provider list page on "Save & Close"
	 * 							Will return the provider details page on "Save"
	 * 							Will return the provider details page on error
	 * 
	 * @Objects					(1) The object containing all the information for the provider
	 * 							(2) The 'id' of the clicked org that will be used in the menu and action bar
	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/provider.{person}", method = RequestMethod.POST)
	public ModelAndView updateProvider(@Valid @ModelAttribute(value="providerdetails") Provider providerdetails, BindingResult result, RedirectAttributes redirectAttr,@RequestParam String action) throws Exception {
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/organizations/providers/details");
			//Set the provider addresses
			List<providerAddress> addresses = providerManager.getProviderAddresses(providerdetails.getId());
			providerdetails.setProviderAddresses(addresses);	
			mav.addObject("providerId",providerdetails.getId());
			mav.addObject("id",orgId);
			return mav;
		}
		
		//Update the provider
		providerManager.updateProvider(providerdetails);
	
		
		//If the "Save" button was pressed 
		if(action.equals("save")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/organizations/providers/details");
			//Set the provider addresses
			List<providerAddress> addresses = providerManager.getProviderAddresses(providerdetails.getId());
			providerdetails.setProviderAddresses(addresses);	
			mav.addObject("providerId",providerdetails.getId());
			mav.addObject("savedStatus","updated");
			mav.addObject("id",orgId);
			return mav;
		}
		//If the "Save & Close" button was pressed.
		else {
			//This variable will be used to display the message on the details form
			redirectAttr.addFlashAttribute("savedStatus", "updated");
			
			ModelAndView mav = new ModelAndView(new RedirectView("providers"));
			return mav;			
		}
	
	}
	
	/**
	 * The '/{cleanURL}/providerAddress/{address}?i=##' GET request will be used to return the details of the selected
	 * address.
	 * 
	 * @param 	i	The id of the address selected
	 * 
	 * @return		The provider address details page
	 * 
	 * @Objects		(1) An object that will hold all the details of the clicked address
	 *
	 */
	 @RequestMapping(value="/{cleanURL}/providerAddress/{address}", method= RequestMethod.GET)
	 @ResponseBody 
	 public ModelAndView viewAddressDetails(@RequestParam(value="i", required=true) Integer addressId) throws Exception {
		 
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/organizations/providers/addressDetails");	
		
		//Get all the details for the clicked address
		providerAddress addressDetails = providerManager.getAddressDetails(addressId);
				
		mav.addObject("btnValue","Update");
		mav.addObject("addressDetails",addressDetails);
		
		//Get a list of states
		USStateList stateList = new USStateList();
		
		//Get the object that will hold the states
		mav.addObject("stateList",stateList.getStates());
		
		return mav;
		 
	 }
	 
	 /**
	 * The '/{cleanURL}/address/update' POST request will handle submitting changes for the selected provider address.
	 * 
	 * @param user				The object containing the system user form fields
	 * @param result			The validation result
	 * 
	 * @return					Will return the provider details page on "Save"
	 * 							Will return the address form page on error

	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/address/update", method = RequestMethod.POST)
	public @ResponseBody ModelAndView updateProviderAddress(@Valid @ModelAttribute(value="addressDetails") providerAddress addressDetails, BindingResult result) throws Exception {
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView("/administrator/organizations/providers/addressDetails");
			
			//Get a list of states
			USStateList stateList = new USStateList();
			
			//Get the object that will hold the states
			mav.addObject("stateList",stateList.getStates());
			
			mav.addObject("btnValue","Update");
			return mav;
		}
		
		
		providerManager.updateAddress(addressDetails);
	
		ModelAndView mav = new ModelAndView("/administrator/organizations/providers/addressDetails");
		mav.addObject("success","addressUpdated");
		return mav;		
	}
		 
	 
	 /**
	 * The '/{cleanURL}/newProviderAddress' GET request will be used to return a blank provider
	 * address form.
	 * 
	 * @param 	i	The id of the address selected
	 * 
	 * @return		The provider address details page
	 * 
	 * @Objects		(1) An object that will hold all the details of the clicked address
	 *
	 */
	 @RequestMapping(value="/{cleanURL}/newProviderAddress", method= RequestMethod.GET)
	 @ResponseBody 
	 public ModelAndView newProviderAddress(@RequestParam(value="providerId", required=true) Integer providerId) throws Exception {
		 
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/organizations/providers/addressDetails");	
		providerAddress addressDetails = new providerAddress();
		addressDetails.setProviderId(providerId);
		mav.addObject("addressDetails",addressDetails);
		mav.addObject("btnValue","Create");
		
		//Get a list of states
		USStateList stateList = new USStateList();
		
		//Get the object that will hold the states
		mav.addObject("stateList",stateList.getStates());
		
		return mav;
		 
	 }
	 
	 /**
	 * The '/{cleanURL}/address/create' POST request will handle submitting the new provider address.
	 * 
	 * @param user				The object containing the system user form fields
	 * @param result			The validation result
	 * 
	 * @return					Will return the provider details page on "Save"
	 * 							Will return the address form page on error

	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/address/create", method = RequestMethod.POST)
	public @ResponseBody ModelAndView createProviderAddress(@Valid @ModelAttribute(value="addressDetails") providerAddress addressDetails, BindingResult result) throws Exception {
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView("/administrator/organizations/providers/addressDetails");
			
			//Get a list of states
			USStateList stateList = new USStateList();
			
			//Get the object that will hold the states
			mav.addObject("stateList",stateList.getStates());
			
			mav.addObject("btnValue","Create");
			return mav;
		}
		
		
		providerManager.createAddress(addressDetails);
	
		ModelAndView mav = new ModelAndView("/administrator/organizations/providers/addressDetails");
		mav.addObject("success","addressCreated");
		return mav;		
	}
	
	/**
	 * The '/{cleanURL}/addressDelete/{title}?i=##' GET request will be used to delete the selected
	 * address.
	 * 
	 * @param 	i	The id of the address selected
	 * 
	 * @return		This function returns no value
	 *
	 */
	 @RequestMapping(value="/{cleanURL}/addressDelete/{title}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	 public @ResponseBody Integer deleteAddress(@RequestParam(value="i", required=true) Integer addressId) throws Exception {	 
		providerManager.deleteAddress(addressId);
		return 1;
	 }
	
	
	/**
	 * The '/{cleanURL}/providerDelete/{title}?i=##' GET request will be used to delete the selected
	 * provider.
	 * 
	 * @param 	i	The id of the provider selected
	 * 
	 * @return		Will return the provider list page on "Save"
	 *
	 */
	 @RequestMapping(value="/{cleanURL}/providerDelete/{title}", method= RequestMethod.GET)
	 public ModelAndView deleteProvider(@RequestParam(value="i", required=true) Integer providerId, RedirectAttributes redirectAttr) throws Exception {
		 
		providerManager.deleteProvider(providerId);
		
		//This variable will be used to display the message on the details form
		redirectAttr.addFlashAttribute("savedStatus", "deleted");	
		
		ModelAndView mav = new ModelAndView(new RedirectView("../providers"));
		return mav;		
	 }
	
	
	 /**
	 * *********************************************************
	 * 			ORGANIZATION BROCHURE FUNCTIONS					
	 * *********************************************************
	 */
	
	/**
	 * The '/{cleanURL/brochures' GET request will display the list of brochures for the selected 
	 * organization.
	 * 
	 * @param cleanURL		The variable that holds the organization that is being viewed
	 * 
	 * @return				Will return the organization brochure list page
	 * 
	 * @Objects				(1) An object that holds brochures found for the organization
	 * 						(2)	The orgId used for the menu and action bar
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/brochures", method = RequestMethod.GET)
	public ModelAndView listOrganizationBrochures(@PathVariable String cleanURL, @RequestParam(value="page", required=false) Integer page) throws Exception {
		
		if(page == null) {
			page = 1;
		}
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/organizationBrochures");
     
        List<Brochure> brochures = organizationManager.getOrganizationBrochures(orgId,page,maxResults);
        mav.addObject("id",orgId);
        mav.addObject("brochureList", brochures);		
        
        //Return the total list of brochures for the organization
        Long totalBrochures = organizationManager.findTotalBrochures(orgId);
        
        Integer totalPages = Math.round(totalBrochures/maxResults);
        mav.addObject("totalPages",totalPages);
        mav.addObject("currentPage",page);
        
        return mav;
	} 
	
	/**
	 * The '/{cleanURL}/brochures' POST request will be used to search brochures for the selected organization 
	 * 
	 * @param searchTerm	The searchTerm parameter will hold the string to search on
	 * @return				The organization brochure list page
	 * 
	 * @Objects				(1) An object will be returned holding the requested search term used to 
	 * 							populate the search box
	 * 						(2) An object containing the found brochures
	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/brochures", method = RequestMethod.POST)
	public ModelAndView findBrochures(@RequestParam String searchTerm, @PathVariable String cleanURL) throws Exception {
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/organizationBrochures");
        
        List<Brochure> brochures = brochureManager.findBrochures(orgId, searchTerm);
        mav.addObject("id",orgId);
        mav.addObject("searchTerm",searchTerm);
        mav.addObject("brochureList", brochures);		
        
        return mav;
	}
	
	/**
	 * The '/{cleanURL}/brochure/{title}?i=##' GET request will be used to return the details of the selected
	 * brochure.
	 * 
	 * @param 	i	The id of the brochure selected
	 * 
	 * @return		The organization brochure details page
	 * 
	 * @Objects		(1) An object that will hold all the details of the clicked brochure
	 * 				(2) An object that will hold all the available sections the brochure can have access to
	 *
	 */
	 @RequestMapping(value="/{cleanURL}/brochure/{title}", method= RequestMethod.GET)
	 @ResponseBody 
	 public ModelAndView viewBrochureDetails(@RequestParam(value="i", required=true) Integer brochureId) throws Exception {
		 
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/organizations/brochureDetails");	
		
		//Get all the details for the clicked brochure
		Brochure brochureDetails = brochureManager.getBrochureById(brochureId);
				
		mav.addObject("brochureId",brochureDetails.getId());
		mav.addObject("btnValue","Update");
		mav.addObject("brochuredetails",brochureDetails);
		
		return mav;
	 }
	 
	 /**
	 * The '/{cleanURL}/newBrochure' GET request will be used to display the blank new 
	 * brochure screen (In a modal)
	 *
	 * 
	 * @return		The organization brochure blank form page
	 * 
	 * @Objects		(1) An object that will hold all the form fields of a new brochure
	 * 				(2) An object to hold the button value "Create"
	 *
	 */
	@RequestMapping(value="/{cleanURL}/newBrochure", method = RequestMethod.GET)
	public @ResponseBody ModelAndView newBrochure() throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/organizations/brochureDetails");
		Brochure brochuredetails = new Brochure();
		
		//Set the id of the organization for the new provider
		brochuredetails.setOrgId(orgId);
		mav.addObject("btnValue","Create");
		mav.addObject("brochuredetails", brochuredetails);
		
		return mav;
	}
		
	/**
	 * The '/{cleanURL}/createBrochure' POST request will handle submitting the new organization brochure.
	 * 
	 * @param brochure			The object containing the brochure form fields
	 * @param result			The validation result
	 * @param redirectAttr		The variable that will hold values that can be read after the redirect
	 * 
	 * @return					Will return the brochure list page on "Save"
	 * 							Will return the brochure form page on error
	 * 
	 * @Objects					(1) The object containing all the information for the clicked org
	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/createBrochure", method = RequestMethod.POST)
	public ModelAndView createBrochure(@ModelAttribute(value="brochuredetails") Brochure brochuredetails,RedirectAttributes redirectAttr, @PathVariable String cleanURL ) throws Exception {
		
		brochureManager.createBrochure(brochuredetails);
		
		ModelAndView mav = new ModelAndView(new RedirectView("brochures?msg=created"));
		return mav;		
	}
	
	
	/**
	 * The '/{cleanURL}/updateBrochure' POST request will handle submitting changes for the selected organization brochure.
	 * 
	 * @param brochure			The object containing the brochure form fields
	 * @param result			The validation result
	 * @param redirectAttr		The variable that will hold values that can be read after the redirect
	 * 
	 * @return					Will return the brochure list page on "Save"
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/{cleanURL}/updateBrochure", method = RequestMethod.POST)
	public ModelAndView updateBrochure(@ModelAttribute(value="brochuredetails") Brochure brochuredetails, RedirectAttributes redirectAttr, @PathVariable String cleanURL ) throws Exception {
		
		brochureManager.updateBrochure(brochuredetails);
	
		ModelAndView mav = new ModelAndView(new RedirectView("brochures?msg=updated"));
		return mav;		
	}
	
	/**
	 * The '/{cleanURL}/brochureDelete/{title}?i=##' GET request will be used to delete the selected
	 * brochure.
	 * 
	 * @param 	i	The id of the brochure selected
	 * 
	 * @return		Will return the brochure list page on "Save"
	 *
	 */
	 @RequestMapping(value="/{cleanURL}/brochureDelete/{title}", method= RequestMethod.GET)
	 public ModelAndView deleteBrochure(@RequestParam(value="i", required=true) Integer brochureId) throws Exception {
		 
		brochureManager.deleteBrochure(brochureId);
			
		ModelAndView mav = new ModelAndView(new RedirectView("../brochures?msg=deleted"));
		return mav;		
	 }
	 
	 @RequestMapping(value="/{cleanURL}/brochureView/{title}", method = RequestMethod.GET)
	 public @ResponseBody ModelAndView viewBrochureFile(@RequestParam(value="i", required=true) Integer brochureId, @PathVariable String cleanURL) throws Exception {
		 
		 //Need to get the file name based on the brochure Id passed
		 Brochure brochure = brochureManager.getBrochureById(brochureId);
		 
		 ModelAndView mav = new ModelAndView();
		 mav.setViewName("/administrator/organizations/brochureAttachment");
		 
		 //Set the type of attachment
		 fileSystem dir = new fileSystem();
		 dir.setDir(cleanURL, "brochures");
		 Image image = ImageIO.read(new File(dir.getDir() + brochure.getfileName()));
	     if (image == null) {
	         if(brochure.getfileName().contains(".pdf")) {
	        	 mav.addObject("fileType","pdf");
	         }
	         else {
	        	 mav.addObject("fileType","other");
	         }
	    	
	     }
	     else {
	    	 mav.addObject("fileType","image");
	     }
		 
		 mav.addObject("brochureAttachment", cleanURL+"/brochures/"+brochure.getfileName());
		 
		 return mav;
	 }
	
	 
	/**
	 * *********************************************************
	 * 								
	 * *********************************************************
	 */ 
	 
	

}
