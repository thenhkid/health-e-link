/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

import com.ut.dph.model.Connections;
import com.ut.dph.model.Organization;
import com.ut.dph.model.configuration;
import com.ut.dph.model.messageType;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.service.organizationManager;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/Health-e-Web")
public class HealtheWebController {
    
    @Autowired
    private configurationManager configurationManager;
    
    @Autowired
    private messageTypeManager messagetypemanager;
    
    @Autowired
    private organizationManager organizationmanager;
    
    /**
     * The '/inbox' request will serve up the Health-e-Web (ERG) inbox.
     *
     * @param request
     * @param response
     * @return	the health-e-web inbox  view
     * @throws Exception
     */
    @RequestMapping(value = "/inbox", method = RequestMethod.GET)
    public ModelAndView viewinbox(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/inbox");
        
        return mav;
    }
    
    /**
     * The '/sent' request will serve up the Health-e-Web (ERG) sent items page.
     *
     * @param request
     * @param response
     * @return	the health-e-web sent items  view
     * @throws Exception
     */
    @RequestMapping(value = "/sent", method = RequestMethod.GET)
    public ModelAndView viewSentItems(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/sent");
        
        return mav;
    }
    
    /**
     * The '/create' request will serve up the Health-e-Web (ERG) create message page.
     *
     * @param request
     * @param response
     * @return	the health-e-web create new message  view
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createNewMesage(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/create");
        
        /** Need to get all the message types set up for the user */
        int[] userInfo = (int[])session.getAttribute("userInfo");
        List<configuration> configurations = configurationManager.getActiveConfigurationsByOrgId(userInfo[1]);
        
        messageType messagetype;
        for (configuration config : configurations) {
            messagetype = messagetypemanager.getMessageTypeById(config.getMessageTypeId());
            config.setMessageTypeName(messagetype.getName());
            
            List<Connections> connections = configurationManager.getConnections(config.getId());
            
            for (Connections connection : connections) {
                //Need to get the org name;
                Organization orgDetails = organizationmanager.getOrganizationById(connection.getorgId());
                connection.setorgName(orgDetails.getOrgName());
            }
            
            config.setorgConnections(connections);
        }
        
        mav.addObject("configurations", configurations);
        
        return mav;
    }
    
    /**
     * The '/create/details' POST request will take the selected message type and target org and display
     * the new message form.
     * 
     * @param configId  The selected configuration
     * @param targetOrg The selected target organization to receive the new message
     * 
     * @return this request will return the messageDetailsForm
     */
    @RequestMapping(value= "/create/details", method = RequestMethod.POST)
    public ModelAndView showMessageDetailsForm(@RequestParam(value = "configId", required = true) int configId, @RequestParam(value = "targetOrg", required = true) int targetOrg) {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/messageDetailsForm");
        
        /* Create new transactionUpload Object */
        
        /* Create new transactionIn Object */
        
        
        
        
        
        
        
        mav.addObject("configId",configId);
        mav.addObject("targetOrg",targetOrg);
        return mav;
    }
    
}
