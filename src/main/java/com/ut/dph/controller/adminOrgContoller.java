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

@Controller
@RequestMapping("/administrator/organizations")

public class adminOrgContoller {
	
	@Autowired 
	private organizationManager organizationManager;
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView listOrganizations(@RequestParam(value="page", required=false) Integer page) throws Exception {
		
		if(page == null){
	        page = 0;
	    }
 
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/listOrganizations");
        
        List<Organization> organizations = organizationManager.getOrganizations(page,5);
        mav.addObject("organizationList", organizations);		
        
        return mav;
 
	}
	
	@RequestMapping(value="/list", method = RequestMethod.POST)
	public ModelAndView findOrganizations(@RequestParam String searchTerm) throws Exception {
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/listOrganizations");
        
        List<Organization> organizations = organizationManager.findOrganizations(searchTerm);
        mav.addObject("searchTerm",searchTerm);
        mav.addObject("organizationList", organizations);		
        
        return mav;
 
	}
	
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView createOrganization() throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/organizations/organizationDetails");
		mav.addObject("organization", new Organization());
		return mav;
		
	}
	
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

}
