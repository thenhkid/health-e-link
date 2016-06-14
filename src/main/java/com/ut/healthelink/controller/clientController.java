/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.controller;

import com.ut.healthelink.model.User;
import com.ut.healthelink.model.clients;
import com.ut.healthelink.reference.USStateList;
import com.ut.healthelink.service.clientManager;
import com.ut.healthelink.service.organizationManager;
import com.ut.healthelink.service.userManager;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/clients")
public class clientController {
    
    @Autowired
    private userManager usermanager;
    
    @Autowired
    private organizationManager organizationmanager;
    
    @Autowired
    private clientManager clientmanager;

    /**
     * The '/search' request will serve up the client search page after a successful login.
     *
     * @param request
     * @return	the client search page
     * @throws Exception
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView clientSearch(HttpServletRequest request) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/clients/search");
        
         //Get a list of states
        USStateList stateList = new USStateList();
        
        //Get the object that will hold the states
        mav.addObject("stateList", stateList.getStates());
        
        return mav;
    }
    
    /**
     * The '/search' POST request will search the client DB based on the passed in criteria and 
     * return the results.
     *
     * @param request
     * @return	the client search page
     * @throws Exception
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView searchClients(HttpServletRequest request, HttpSession session,
            @RequestParam(value = "firstName", required = false) String firstName,
         @RequestParam(value = "lastName", required = false) String lastName,
         @RequestParam(value = "dob", required = false) String dob,
         @RequestParam(value = "postalCode", required = false) String postalCode) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/clients/searchResults");
        
         /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        List<clients> clients = clientmanager.searchClients(userInfo.getOrgId(), firstName, lastName, dob, postalCode);
        
        mav.addObject("clients", clients);
        
        return mav;
    }
}
