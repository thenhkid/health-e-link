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

import com.ut.dph.model.Organization;
import com.ut.dph.service.organizationManager;
import com.ut.dph.model.User;
import com.ut.dph.service.userManager;

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
	        page = 0;
	    }
 
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/listOrganizations");
        
        List<Organization> organizations = organizationManager.getOrganizations(page,5);
        mav.addObject("orgFunctions",organizationManager);
        mav.addObject("organizationList", organizations);		
        
        return mav;
 
	}
	
	/**
	 * The '/list' POST request will be used to search oranizations from the search form on the
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
			return mav;
		}
		
		List<Organization> existing = organizationManager.getOrganizationByName(organization.getcleanURL());
	    if (!existing.isEmpty()) {
        	ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/organizations/organizationDetails");
			mav.addObject("id",organization.getId());
			mav.addObject("existingOrg","Organization "+organization.getOrgName()+" already exists.");
			return mav;	
        }
		
		Integer id = null;
		id = (Integer) organizationManager.createOrganization(organization);
		
		//Get the organization name that was just added
		Organization latestorg = organizationManager.getOrganizationById(id);
		
		redirectAttr.addFlashAttribute("savedStatus", "success");
		
		if(action.equals("save")) {
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
		mav.addObject("id",orgDetails.getId());
		mav.addObject("organization",orgDetails);
		
		return mav;
	
	}
	
	
	/**
	 * The '/{cleanURL}' POST request will handle submitting changes for the selected organization.
	 * 
	 * @param organization		The object containing the organiztion form fields
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
			return mav;
		}
		
		Organization currentOrg = organizationManager.getOrganizationById(organization.getId());
		
		if(!currentOrg.getcleanURL().trim().equals(organization.getcleanURL().trim())) {
			List<Organization> existing = organizationManager.getOrganizationByName(organization.getcleanURL());
		    if (!existing.isEmpty()) {
	        	ModelAndView mav = new ModelAndView();
				mav.setViewName("/administrator/organizations/organizationDetails");
				mav.addObject("id",organization.getId());
				mav.addObject("existingOrg","Organization "+organization.getOrgName().trim()+" already exists.");
				return mav;	
	        }
		}
		
		organizationManager.updateOrganization(organization);
	
		redirectAttr.addFlashAttribute("savedStatus", "success");
		
		if(action.equals("save")) {
			ModelAndView mav = new ModelAndView(new RedirectView("../"+organization.getcleanURL()+"/"));
			return mav;		
		}
		else {
			ModelAndView mav = new ModelAndView(new RedirectView("../list"));
			return mav;			
		}
	
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
	public ModelAndView listOrganizationUsers(@PathVariable String cleanURL) throws Exception {
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/organizationUsers");
        
        List<Organization> organization = organizationManager.getOrganizationByName(cleanURL);
		Organization orgDetails = organization.get(0);
        
        Integer orgId = orgDetails.getId();
        
        List<User> users = organizationManager.getOrganizationUsers(orgId);
        mav.addObject("id",orgId);
        mav.addObject("userFunctions",userManager);
        mav.addObject("userList", users);		
        
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
        
        List<Organization> organization = organizationManager.getOrganizationByName(cleanURL);
		Organization orgDetails = organization.get(0);
        
        Integer orgId = orgDetails.getId();
        
        List<User> users = userManager.findUsers(orgId, searchTerm);
        mav.addObject("id",orgId);
        mav.addObject("searchTerm",searchTerm);
        mav.addObject("userFunctions",userManager);
        mav.addObject("userList", users);		
        
        return mav;
 
	}
	

}
