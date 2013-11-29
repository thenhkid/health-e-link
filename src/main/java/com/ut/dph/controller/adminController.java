package com.ut.dph.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ut.dph.model.Organization;
import com.ut.dph.model.messageType;
import com.ut.dph.model.configuration;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.service.organizationManager;
import com.ut.dph.service.configurationManager;

/**
 * The adminController class will handle administrator page requests that fall outside specific sections.
 *
 *
 * @author chadmccue
 *
 */
@Controller
public class adminController {

    @Autowired
    private organizationManager organizationManager;

    @Autowired
    private messageTypeManager messagetypemanager;

    @Autowired
    private configurationManager configurationmanager;

    private int maxResults = 3;

    /**
     * The '/administrator' request will serve up the administrator dashboard after a successful login.
     *
     * @param request
     * @param response
     * @return	the administrator dashboard view
     * @throws Exception
     */
    @RequestMapping(value = "/administrator", method = RequestMethod.GET)
    public ModelAndView listConfigurations(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/dashboard");

		//Need to get totals for the dashboard.
        //Return the total list of organizations
        Long totalOrgs = organizationManager.findTotalOrgs();
        mav.addObject("totalOrgs", totalOrgs);

        //Return the latest organizations
        List<Organization> organizations = organizationManager.getLatestOrganizations(maxResults);
        mav.addObject("latestOrgs", organizations);

        //Return the total list of message types
        Long totalMessageTypes = messagetypemanager.findTotalMessageTypes();
        mav.addObject("totalMessageTypes", totalMessageTypes);

        //Return the latest message types created
        List<messageType> messagetypes = messagetypemanager.getLatestMessageTypes(maxResults);
        mav.addObject("latestMessageTypes", messagetypes);

        //Return the total list of configurations
        Long totalConfigs = configurationmanager.findTotalConfigs();
        mav.addObject("totalConfigs", totalConfigs);

        //Return the latest configurations
        List<configuration> configurations = configurationmanager.getLatestConfigurations(maxResults);
        mav.addObject("latestConfigs", configurations);

        return mav;
    }

}
