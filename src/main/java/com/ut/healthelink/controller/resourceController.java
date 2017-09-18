/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.controller;

import com.ut.healthelink.model.Organization;
import com.ut.healthelink.model.User;
import com.ut.healthelink.model.messageType;
import com.ut.healthelink.reference.USStateList;
import com.ut.healthelink.service.messageTypeManager;
import com.ut.healthelink.service.organizationManager;
import com.ut.healthelink.service.userManager;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author chadmccue
 */
@Controller
@RequestMapping("/resources")
public class resourceController {
    
    @Autowired
    private userManager usermanager;
    
    @Autowired
    private organizationManager organizationmanager;
    
    @Autowired
    private messageTypeManager messageTypeManager;
    

    /**
     * The '/search' request will serve up the resource search page after a successful login.
     *
     * @param request
     * @return	the resource search page
     * @throws Exception
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView resourcesSearch(HttpServletRequest request) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/resources/search");
        
         //Get a list of states
        USStateList stateList = new USStateList();
        
        //Get the object that will hold the states
        mav.addObject("stateList", stateList.getStates());
        
        //Get a list of message types in the system 
        List<messageType> messageTypes = messageTypeManager.getActiveMessageTypes();
        mav.addObject("messageTypes", messageTypes);
        
        return mav;
    }
    
    /**
     * The '/search' POST request will search the organization DB based on the passed in criteria and 
     * return the results.
     *
     * @param request
     * @return	the resource search page
     * @throws Exception
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView searchResources(HttpServletRequest request, HttpSession session,
         @RequestParam(value = "programType", required = false) Integer programType,   
         @RequestParam(value = "town", required = false) String town,
         @RequestParam(value = "county", required = false) String county,
         @RequestParam(value = "state", required = false) String state,
         @RequestParam(value = "postalCode", required = false) String postalCode) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/resources/searchResults");
        
         /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        List<Organization> resources = organizationmanager.searchCBOOrganizations(programType,town,county,state,postalCode);
        
        if(resources != null && resources.size() > 0) {
            //Get a list of message types in the system 
            List<messageType> messageTypes = messageTypeManager.getActiveMessageTypes();
            
            for(Organization org : resources) {
                List<String> programsOffered = new ArrayList<String>();
                
                /* Get a list of modules the program uses */
                List<Integer> usedPrograms = organizationmanager.getOrganizationPrograms(org.getId());

                if (!messageTypes.isEmpty()) {
                    for (messageType program : messageTypes) {
                        if (usedPrograms.contains(program.getId())) {
                            programsOffered.add(program.getDspName());
                        }
                    }
                }
                
                if(programsOffered.size() > 0) {
                    org.setProgramsOffered(programsOffered);
                }
                
            }
        }
        
        mav.addObject("resources", resources);
        
        return mav;
    }
    
    
    @RequestMapping(value = "/activationRequest", method = {RequestMethod.GET})
    public @ResponseBody
    ModelAndView activationRequest(HttpServletRequest request, HttpSession session,
             @RequestParam(value = "targetOrg", required = false) Integer targetOrg) throws Exception {
        
        ModelAndView mav = new ModelAndView();
          /* Need to get all the message types set up for the user */
        User userDetails = (User)session.getAttribute("userDetails");
        mav.addObject("btnValue", "Update");
        mav.addObject("userdetails", userDetails);
        mav.setViewName("/resources/activationRequest");
        
        Organization srcOrgDetails = organizationmanager.getOrganizationById(userDetails.getOrgId());
        Organization tgtOrgDetails = organizationmanager.getOrganizationById(targetOrg);
        
        mav.addObject("srcOrgDetails", srcOrgDetails);
        mav.addObject("tgtOrgDetails", tgtOrgDetails);
        
        return mav;
    }
}
