/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.controller;

import com.ut.healthelink.model.Organization;
import com.ut.healthelink.model.Provider;
import com.ut.healthelink.model.User;
import com.ut.healthelink.model.providerAddress;
import com.ut.healthelink.model.providerIdNum;
import com.ut.dph.service.organizationManager;
import com.ut.dph.service.providerManager;
import com.ut.dph.service.userManager;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author chadmccue
 */
@Controller
@RequestMapping("/associations")
public class orgController {
    
    @Autowired
    private organizationManager organizationManager;
    
    @Autowired
    private providerManager providerManager;
    
    @Autowired
    private userManager userManager;
    
    /**
     * The private maxResults variable will hold the number of results to show per list page.
     */
    private static int maxResults = 20;
    
    
    /**
     * The '/associations' Get request will display the list of organizations associated to the organization
     * for the logged in user.
     *
     * @param request
     * @param response
     * @return	the associated organization list page
     * @throws Exception
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView viewAssociations(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/organizations/associated");
        
        User userInfo = (User)session.getAttribute("userDetails");
        
        List<Organization> organizations = organizationManager.getAssociatedOrgs(userInfo.getOrgId());
        mav.addObject("organizations", organizations);
        
        return mav;
        
    }
    
    /**
     * The '/associations/ORG NAME' Post request will get the details of the passed in orgId
     * 
     * @param orgId The id of the selected organization
     * 
     * @return The details page of the selected organization
     * 
     */
    @RequestMapping(value = "/{orgName}", method = RequestMethod.POST)
    public ModelAndView viewOrgDetails(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam Integer orgId) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/organizations/associated/orgDetails");
        
        Organization orgDetails = null;
        
        try {
        
            orgDetails = organizationManager.getOrganizationById(orgId);
            mav.addObject("orgDetails", orgDetails);
        
            List<Provider> providers = organizationManager.getOrganizationProviders(orgDetails.getId());

            if(!providers.isEmpty()) {

                for(Provider provider : providers) {

                    List<providerAddress> addresses = providerManager.getProviderAddresses(provider.getId());

                    provider.setProviderAddresses(addresses);

                    List<providerIdNum> ids = providerManager.getProviderIds(provider.getId());

                    provider.setProviderIds(ids);

                }

            }

            mav.addObject("providers", providers);
            
            /* Get a list of main contacts */
            List<User> mainContacts = userManager.getOrganizationContact(orgDetails.getId(), 1);
            mav.addObject("mainContacts", mainContacts);
            
            /* Get a list of secondary contacts */
            List<User> secondaryContacts = userManager.getOrganizationContact(orgDetails.getId(), 2);
            mav.addObject("secondaryContacts", secondaryContacts);

            return mav;
        
        }
        catch(Exception e) {
            throw new Exception ("Error trying to list the organization details. OrgId: "+ orgDetails.getId(), e);
        }
        
        
    }
    
}
