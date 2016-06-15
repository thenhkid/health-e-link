/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.controller;

import com.ut.healthelink.model.Brochure;
import com.ut.healthelink.model.Organization;
import com.ut.healthelink.model.Provider;
import com.ut.healthelink.model.User;
import com.ut.healthelink.model.messageType;
import com.ut.healthelink.model.organizationPrograms;
import com.ut.healthelink.model.providerAddress;
import com.ut.healthelink.model.providerIdNum;
import com.ut.healthelink.reference.USStateList;
import com.ut.healthelink.service.brochureManager;
import com.ut.healthelink.service.messageTypeManager;
import com.ut.healthelink.service.organizationManager;
import com.ut.healthelink.service.providerManager;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

/**
 *
 * @author chadmccue
 */
@Controller
@RequestMapping("/OrgProfile")
public class orgProfileController {
    
    @Autowired
    private organizationManager organizationManager;
    
    @Autowired
    private providerManager providerManager;
    
    @Autowired
    private brochureManager brochureManager;
    
    @Autowired
    private messageTypeManager messagetypemanager;
    
    /**
     * The '/editProfile' request will serve up the organization profile edit page.
     *
     * @param request
     * @param response
     * @return	the organization profile edit page
     * @throws Exception
     */
    @RequestMapping(value = "/editProfile", method = RequestMethod.GET)
    public ModelAndView viewEditOrgProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/OrgProfile/editProfile");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        /* Get the org details */
        Organization orgDetails = organizationManager.getOrganizationById(userInfo.getOrgId());
        
        mav.addObject("organization", orgDetails);
        
        //Get a list of states
        USStateList stateList = new USStateList();
        
        //Get the object that will hold the states
        mav.addObject("stateList", stateList.getStates());
        
        return mav;
        
    }
    
    /**
     * The '/{cleanURL}' POST request will handle submitting changes for the organization profile.
     *
     * @param organization	The object containing the organization form fields
     * @param result	The validation result
     * @param redirectAttr	The variable that will hold values that can be read after the redirect
     * @param action	The variable that holds which button was pressed
     *
     * @return	Will return the organization list page on "Save & Close" Will return the organization details page on "Save" Will return the organization create page on error
     *
     * @Objects	(1) The object containing all the information for the clicked org (2) The 'id' of the clicked org that will be used in the menu and action bar
     * @throws Exception
     */
    @RequestMapping(value = "/editProfile", method = RequestMethod.POST)
    public ModelAndView updateOrganization(@Valid Organization organization, BindingResult result, RedirectAttributes redirectAttr) throws Exception {

        //Get a list of states
        USStateList stateList = new USStateList();

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/OrgProfile/editProfile");
            //Get the object that will hold the states
            mav.addObject("stateList", stateList.getStates());
            return mav;
        }

        Organization currentOrg = organizationManager.getOrganizationById(organization.getId());
        organization.setparsingTemplate(currentOrg.getparsingTemplate());

        if (!currentOrg.getcleanURL().trim().equals(organization.getcleanURL().trim())) {
            List<Organization> existing = organizationManager.getOrganizationByName(organization.getcleanURL());
            if (!existing.isEmpty()) {
                ModelAndView mav = new ModelAndView();
                mav.setViewName("/OrgProfile/editProfile");
                mav.addObject("existingOrg", "Organization " + organization.getOrgName().trim() + " already exists.");
                //Get the object that will hold the states
                mav.addObject("stateList", stateList.getStates());
                return mav;
            }
        }
        
        organization.setStatus(true);
        organization.setDateCreated(currentOrg.getDateCreated());
        organization.setparsingTemplate(currentOrg.getparsingTemplate());
        organization.setParentId(currentOrg.getParentId());
        organization.setHeaderLogo(currentOrg.getHeaderLogo());
        organization.setHeaderBackground(currentOrg.getHeaderBackground());
        organization.setLongitude(currentOrg.getLongitude());
        organization.setLatitude(currentOrg.getLatitude());
        organization.setOrgDesc(currentOrg.getOrgDesc());
        organization.setOrgType(currentOrg.getOrgType());

        //Update the organization
        organizationManager.updateOrganization(organization);

        //This variable will be used to display the message on the details form
        redirectAttr.addFlashAttribute("savedStatus", "updated");

        ModelAndView mav = new ModelAndView(new RedirectView("/OrgProfile/editProfile"));
        return mav;
        
    }
    
    
    /**
     * The '/providers' GET request will handle displaying the providers for the organization.
     * 
     * @return the organization provider list
     */
    @RequestMapping(value = "/providers", method = RequestMethod.GET)
    public ModelAndView viewOrgProviders(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/OrgProfile/providers");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
        
            List<Provider> providers = organizationManager.getOrganizationProviders(userInfo.getOrgId());

            if(!providers.isEmpty()) {

                for(Provider provider : providers) {

                    List<providerAddress> addresses = providerManager.getProviderAddresses(provider.getId());

                    provider.setProviderAddresses(addresses);

                    List<providerIdNum> ids = providerManager.getProviderIds(provider.getId());

                    provider.setProviderIds(ids);

                }

            }

            mav.addObject("providers", providers);
            
            /* Get the org details */
            Organization orgDetails = organizationManager.getOrganizationById(userInfo.getOrgId());

            mav.addObject("organization", orgDetails);

            return mav;
        
        }
        catch(Exception e) {
            throw new Exception ("Error trying to list the providers. OrgId: "+ userInfo.getOrgId(), e);
        }
        
    }
    
    
    /**
     * The '/providers/createProvider' GET request will handle displaying the blank provider form.
     * 
     * @return the organization provider form.
     */
    @RequestMapping(value = "/providers/createProvider", method = RequestMethod.GET)
    public ModelAndView createProvider(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        Provider providerDetails = new Provider();
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        providerDetails.setOrgId(userInfo.getOrgId());
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/OrgProfile/createProvider");

        mav.addObject("providerdetails", providerDetails);
        
        /* Get the org details */
        Organization orgDetails = organizationManager.getOrganizationById(userInfo.getOrgId());
        
        mav.addObject("organization", orgDetails);

        return mav;
    }
    
    /**
     * The '/providers/createProvider' POST request will handle displaying the blank provider form.
     * 
     * @return the organization provider form.
     */
    @RequestMapping(value = "/providers/createProvider", method = RequestMethod.POST)
    public ModelAndView submitNewProvider(@Valid @ModelAttribute(value = "providerdetails") Provider providerdetails, BindingResult result, RedirectAttributes redirectAttr) throws Exception {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/OrgProfile/createProvider");
            

            return mav;
        }

        //Update the provider
        int providerId = providerManager.createProvider(providerdetails);

        //This variable will be used to display the message on the details form
        redirectAttr.addFlashAttribute("savedStatus", "created");

        ModelAndView mav = new ModelAndView(new RedirectView("/OrgProfile/providers/"+providerId));
        return mav;
        

    }
    
    /**
     * The '/providers/{id}' GET request will show the edit form for the selected provider id. If the provider
     * found with the {id} doesn't match the organization the user is logged in under we will return the user
     * back to the provider list page.
     */
    @RequestMapping(value = "/providers/{id}", method = RequestMethod.GET)
    public ModelAndView editProvider(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable Integer id, RedirectAttributes redirectAttr) throws Exception {
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try{
            Provider providerDetails = providerManager.getProviderById(id);
            
            /* Make sure the org id of the provider matches the user logged in */
            if(providerDetails == null) {
                redirectAttr.addFlashAttribute("error", "notFound");
                ModelAndView mav = new ModelAndView(new RedirectView("/OrgProfile/providers"));
                return mav;
            }
            else if(providerDetails.getOrgId() != userInfo.getOrgId()) {
                redirectAttr.addFlashAttribute("error", "notFound");
                ModelAndView mav = new ModelAndView(new RedirectView("/OrgProfile/providers"));
                return mav;
            }
            else {
                ModelAndView mav = new ModelAndView();
                mav.setViewName("/OrgProfile/editProvider");
                
                //Set the provider addresses
                List<providerAddress> addresses = providerManager.getProviderAddresses(id);
                providerDetails.setProviderAddresses(addresses);

                //Set the provder Ids
                List<providerIdNum> ids = providerManager.getProviderIds(id);
                providerDetails.setProviderIds(ids);
                
                mav.addObject("providerdetails", providerDetails);
                
                /* Get the org details */
                Organization orgDetails = organizationManager.getOrganizationById(userInfo.getOrgId());

                mav.addObject("organization", orgDetails);
                
                return mav;
            }
        }
        catch(Exception e) {
            redirectAttr.addFlashAttribute("error", "notFound");
            ModelAndView mav = new ModelAndView(new RedirectView("/OrgProfile/providers"));
            return mav;
        }
        
    }
    
    
    /**
     * The '/providers/{id}' POST request will handle submitting changes for the selected provider.
     *
     * @param providerdetails	The object containing the provider form fields
     * @param result	The validation result
     * @param redirectAttr	The variable that will hold values that can be read after the redirect
     *
     */
    @RequestMapping(value = "/providers/{id}", method = RequestMethod.POST)
    public ModelAndView updateProvider(@Valid @ModelAttribute(value = "providerdetails") Provider providerdetails, BindingResult result, RedirectAttributes redirectAttr) throws Exception {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/OrgProfile/editProvider");

            //Set the provider addresses
            List<providerAddress> addresses = providerManager.getProviderAddresses(providerdetails.getId());
            providerdetails.setProviderAddresses(addresses);

            //Set the provder Ids
            List<providerIdNum> ids = providerManager.getProviderIds(providerdetails.getId());
            providerdetails.setProviderIds(ids);

            mav.addObject("providerId", providerdetails.getId());
            return mav;
        }

        //Update the provider
        providerManager.updateProvider(providerdetails);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/OrgProfile/editProvider");
        
        //Set the provider addresses
        List<providerAddress> addresses = providerManager.getProviderAddresses(providerdetails.getId());
        providerdetails.setProviderAddresses(addresses);

        //Set the provder Ids
        List<providerIdNum> ids = providerManager.getProviderIds(providerdetails.getId());
        providerdetails.setProviderIds(ids);

        mav.addObject("providerdetails", providerdetails);
        
        mav.addObject("savedStatus", "updated");
        return mav;
        

    }
    
    
    /**
     * The '/providerAddress/{address}?i=##' GET request will be used to return the details of the selected address.
     *
     * @param i	The id of the address selected
     *
     * @return	The provider address details page
     *
     * @Objects	(1) An object that will hold all the details of the clicked address
     *
     */
    @RequestMapping(value = "/providerAddress/{address}", method = RequestMethod.GET)
    public @ResponseBody ModelAndView viewAddressDetails(@RequestParam(value = "i", required = true) Integer addressId) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/providers/addressDetails");

        //Get all the details for the clicked address
        providerAddress addressDetails = providerManager.getAddressDetails(addressId);

        mav.addObject("btnValue", "Update");
        mav.addObject("addressDetails", addressDetails);

        //Get a list of states
        USStateList stateList = new USStateList();

        //Get the object that will hold the states
        mav.addObject("stateList", stateList.getStates());

        return mav;

    }
    
    /**
     * The '/address/update' POST request will handle submitting changes for the selected provider address.
     *
     * @param user	The object containing the system user form fields
     * @param result	The validation result
     *
     * @return	Will return the provider details page on "Save" Will return the address form page on error
     *
     * @throws Exception
     */
    @RequestMapping(value = "/address/update", method = RequestMethod.POST)
    public @ResponseBody ModelAndView updateProviderAddress(@Valid @ModelAttribute(value = "addressDetails") providerAddress addressDetails, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("/administrator/organizations/providers/addressDetails");

            //Get a list of states
            USStateList stateList = new USStateList();

            //Get the object that will hold the states
            mav.addObject("stateList", stateList.getStates());

            mav.addObject("btnValue", "Update");
            return mav;
        }

        providerManager.updateAddress(addressDetails);

        ModelAndView mav = new ModelAndView("/administrator/organizations/providers/addressDetails");
        mav.addObject("success", "addressUpdated");
        return mav;
    }
    
    /**
     * The '/newProviderAddress' GET request will be used to return a blank provider address form.
     *
     * @param i	The id of the address selected
     *
     * @return	The provider address details page
     *
     * @Objects	(1) An object that will hold all the details of the clicked address
     *
     */
    @RequestMapping(value = "/newProviderAddress", method = RequestMethod.GET)    
    public @ResponseBody ModelAndView newProviderAddress(@RequestParam(value = "providerId", required = true) Integer providerId) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/providers/addressDetails");
        providerAddress addressDetails = new providerAddress();
        addressDetails.setProviderId(providerId);
        mav.addObject("addressDetails", addressDetails);
        mav.addObject("btnValue", "Create");

        //Get a list of states
        USStateList stateList = new USStateList();

        //Get the object that will hold the states
        mav.addObject("stateList", stateList.getStates());

        return mav;

    }

    /**
     * The '/address/create' POST request will handle submitting the new provider address.
     *
     * @param user	The object containing the system user form fields
     * @param result	The validation result
     *
     * @return	Will return the provider details page on "Save" Will return the address form page on error
     *
     * @throws Exception
     */
    @RequestMapping(value = "/address/create", method = RequestMethod.POST)
    public @ResponseBody ModelAndView createProviderAddress(@Valid @ModelAttribute(value = "addressDetails") providerAddress addressDetails, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("/administrator/organizations/providers/addressDetails");

            //Get a list of states
            USStateList stateList = new USStateList();

            //Get the object that will hold the states
            mav.addObject("stateList", stateList.getStates());

            mav.addObject("btnValue", "Create");
            return mav;
        }

        providerManager.createAddress(addressDetails);

        ModelAndView mav = new ModelAndView("/administrator/organizations/providers/addressDetails");
        mav.addObject("success", "addressCreated");
        return mav;
    }
    
    /**
     * The '/addressDelete/{title}?i=##' GET request will be used to delete the selected address.
     *
     * @param i	The id of the address selected
     *
     * @return	This function returns no value
     *
     */
    @RequestMapping(value = "/addressDelete/{title}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer deleteAddress(@RequestParam(value = "i", required = true) Integer addressId) throws Exception {
        providerManager.deleteAddress(addressId);
        return 1;
    }
    
    /**
     * The '/providerId/{id}' GET request will be used to return the details of the selected provider id.
     *
     * @param i	The id of the provider Id selected
     *
     * @return	The provider Id details page
     *
     * @Objects	(1) An object that will hold all the details of the clicked provider Id
     *
     */
    @RequestMapping(value = "/providerId/{id}", method = RequestMethod.GET)
    public @ResponseBody ModelAndView viewIdDetails(@RequestParam(value = "i", required = true) Integer id) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/providers/idDetails");

        //Get all the details for the clicked providerId
        providerIdNum idDetails = providerManager.getIdDetails(id);

        mav.addObject("btnValue", "Update");
        mav.addObject("idDetails", idDetails);

        return mav;
    }

    /**
     * The '/providerId/update' POST request will handle submitting changes for the selected provider Id.
     *
     * @param id	The object containing the system provider Id form fields
     * @param result	The validation result
     *
     * @return	Will return the provider Id details page on "Save" Will return the id form page on error
     *
     * @throws Exception
     */
    @RequestMapping(value = "/providerId/update", method = RequestMethod.POST)
    public @ResponseBody ModelAndView updateProviderId(@Valid @ModelAttribute(value = "idDetails") providerIdNum idDetails, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("/administrator/organizations/providers/idDetails");

            mav.addObject("btnValue", "Update");
            return mav;
        }

        providerManager.updateId(idDetails);

        ModelAndView mav = new ModelAndView("/administrator/organizations/providers/idDetails");
        mav.addObject("success", "idUpdated");
        return mav;
    }

    /**
     * The '/newProviderId' GET request will be used to return a blank provider id form.
     *
     * @param provierid	The id of the selected provider
     *
     * @return	The provider Id form page
     *
     * @Objects	(1) An object that will hold all the details of a new providerId object
     *
     */
    @RequestMapping(value = "/newProviderId", method = RequestMethod.GET)    
    public @ResponseBody ModelAndView newProviderId(@RequestParam(value = "providerId", required = true) Integer providerId) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/providers/idDetails");
        providerIdNum idDetails = new providerIdNum();
        idDetails.setProviderId(providerId);
        mav.addObject("idDetails", idDetails);
        mav.addObject("btnValue", "Create");

        return mav;
    }

    /**
     * The '/providerId/create' POST request will handle submitting the new provider Id.
     *
     * @param idDetails	The object containing the system providerId form fields
     * @param result	The validation result
     *
     * @return	Will return the provider details page on "Save" Will return the providerId form page on error
     *
     * @throws Exception
     */
    @RequestMapping(value = "/providerId/create", method = RequestMethod.POST)
    public @ResponseBody ModelAndView createProviderId(@Valid @ModelAttribute(value = "idDetails") providerIdNum idDetails, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("/administrator/organizations/providers/idDetails");
            mav.addObject("btnValue", "Create");
            return mav;
        }

        providerManager.createId(idDetails);

        ModelAndView mav = new ModelAndView("/administrator/organizations/providers/idDetails");
        mav.addObject("success", "idCreated");
        return mav;
    }

    /**
     * The '/providerIdDelete/{title}?i=##' GET request will be used to delete the selected provider Id.
     *
     * @param i	The id of the provider Id selected
     *
     * @return	This function returns no value
     *
     */
    @RequestMapping(value = "/providerIdDelete/{title}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer deleteProviderId(@RequestParam(value = "i", required = true) Integer id) throws Exception {
        providerManager.deleteId(id);
        return 1;
    }
    
    /**
     * The '/providerDelete/{i}' GET request will be used to delete the selected provider.
     *
     * @param i	The id of the provider selected
     *
     * @return	Will return the provider list page on "Save"
     *
     */
    @RequestMapping(value = "/providerDelete/{i}", method = RequestMethod.GET)
    public ModelAndView deleteProvider(@RequestParam(value = "i", required = true) Integer providerId, RedirectAttributes redirectAttr) throws Exception {

        providerManager.deleteProvider(providerId);

        //This variable will be used to display the message on the details form
        redirectAttr.addFlashAttribute("savedStatus", "deleted");

        ModelAndView mav = new ModelAndView(new RedirectView("/OrgProfile/providers"));
        return mav;
    }
    
    
    /**
     * The '/brochures' GET request will handle displaying the brochures for the organization.
     * 
     * @return the organization brochure list
     */
    @RequestMapping(value = "/brochures", method = RequestMethod.GET)
    public ModelAndView viewOrgBrochures(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/OrgProfile/brochures");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
        
            List<Brochure> brochures = organizationManager.getOrganizationBrochures(userInfo.getOrgId());

            mav.addObject("brochures", brochures);
            
            /* Get the org details */
            Organization orgDetails = organizationManager.getOrganizationById(userInfo.getOrgId());

            mav.addObject("organization", orgDetails);

            return mav;
        
        }
        catch(Exception e) {
            throw new Exception ("Error trying to list the brochures. OrgId: "+ userInfo.getOrgId(), e);
        }
        
    }
    
    /**
     * The '/newBrochure' GET request will be used to display the blank new brochure screen (In a modal)
     *
     *
     * @return	The organization brochure blank form page
     *
     * @Objects	(1) An object that will hold all the form fields of a new brochure (2) An object to hold the button value "Create"
     *
     */
    @RequestMapping(value = "/newBrochure", method = RequestMethod.GET)
    public @ResponseBody ModelAndView newBrochure(HttpSession session) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/organizations/brochures/details");
        Brochure brochuredetails = new Brochure();
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");

        //Set the id of the organization for the new provider
        brochuredetails.setOrgId(userInfo.getOrgId());
        mav.addObject("btnValue", "Create");
        mav.addObject("brochuredetails", brochuredetails);
        
        /* Get the org details */
        Organization orgDetails = organizationManager.getOrganizationById(userInfo.getOrgId());
        
        mav.addObject("organization", orgDetails);

        return mav;
    }
    
    
    /**
     * The '}/createBrochure' POST request will handle submitting the new organization brochure.
     *
     * @param brochure	The object containing the brochure form fields
     * @param result	The validation result
     * @param redirectAttr	The variable that will hold values that can be read after the redirect
     *
     * @return	Will return the brochure list page on "Save" Will return the brochure form page on error
     *
     * @Objects	(1) The object containing all the information for the clicked org
     * @throws Exception
     */
    @RequestMapping(value = "/createBrochure", method = RequestMethod.POST)
    public @ResponseBody ModelAndView createBrochure(@Valid @ModelAttribute(value = "brochuredetails") Brochure brochuredetails, RedirectAttributes redirectAttr, BindingResult result, HttpSession session) throws Exception {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/administrator/organizations/brochures/details");
            mav.addObject("btnValue", "Create");
          
            return mav;
        }
        
        brochureManager.createBrochure(brochuredetails);

        ModelAndView mav = new ModelAndView(new RedirectView("/OrgProfile/brochures?msg=created"));
        return mav;
    }
    
    
    /**
     * The '/brochureDelete/{title}?i=##' GET request will be used to delete the selected brochure.
     *
     * @param i	The id of the brochure selected
     *
     * @return	Will return the brochure list page on "Save"
     *
     */
    @RequestMapping(value = "/brochureDelete/{title}", method = RequestMethod.GET)
    public ModelAndView deleteBrochure(@RequestParam(value = "i", required = true) Integer brochureId) throws Exception {

        brochureManager.deleteBrochure(brochureId);

        ModelAndView mav = new ModelAndView(new RedirectView("/OrgProfile/brochures?msg=deleted"));
        return mav;
    }
    
    /**
     * The '/resources' GET request will handle displaying the resources for the organization.
     * 
     * @return the organization brochure list
     */
    @RequestMapping(value = "/resources", method = RequestMethod.GET)
    public ModelAndView viewOrgResources(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/OrgProfile/resources");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
        
            //Get a list of message types in the system 
            List<messageType> messageTypes = messagetypemanager.getActiveMessageTypes();

            /* Get a list of modules the program uses */
            List<Integer> usedPrograms = organizationManager.getOrganizationPrograms(userInfo.getOrgId());

            if (!messageTypes.isEmpty()) {
                for (messageType program : messageTypes) {
                    if (usedPrograms.contains(program.getId())) {
                        program.setUseProgram(true);
                    }
                }
            }

            mav.addObject("availPrograms", messageTypes);
            
            /* Get the org details */
            Organization orgDetails = organizationManager.getOrganizationById(userInfo.getOrgId());

            mav.addObject("organization", orgDetails);

            return mav;
        
        }
        catch(Exception e) {
            throw new Exception ("Error trying to list the resources. OrgId: "+ userInfo.getOrgId(), e);
        }
    }
    
     /**
     * The '/resources' POST request will save the program module form.
     *
     * @param List<Imteger>	The list of moduleIds to use.
     *
     * @return	Will return the program details page.
     *
     * @throws Exception
     *
     */
    @RequestMapping(value = "/resources", method = RequestMethod.POST)
    public ModelAndView saveprogramModules(@RequestParam List<Integer> programIds, RedirectAttributes redirectAttr, HttpSession session) throws Exception {

        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        if (programIds.isEmpty()) {
            organizationManager.deletOrganizationPrograms(userInfo.getOrgId());
        } else {
            organizationManager.deletOrganizationPrograms(userInfo.getOrgId());

            for (Integer programId : programIds) {
                organizationPrograms newProgram = new organizationPrograms();
                newProgram.setProgramId(programId);
                newProgram.setOrgId(userInfo.getOrgId());
                organizationManager.saveOrganizationPrograms(newProgram);
            }
        }

        redirectAttr.addFlashAttribute("savedStatus", "save");

        ModelAndView mav = new ModelAndView(new RedirectView("resources"));
        return mav;
    }

}
