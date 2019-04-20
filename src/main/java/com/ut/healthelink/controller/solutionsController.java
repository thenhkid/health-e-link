/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author chadmccue
 */
@Controller
@RequestMapping("/support-services")
public class solutionsController {
    
    /**
     * The '' request will display the solutions overview page.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView consultingservices() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/services");
        mav.addObject("pageTitle", "Support Services");
        return mav;
    }
    
    
    /**
     * The '/case-studies' request will display the case studies page.
     */
    @RequestMapping(value = "/case-studies", method = RequestMethod.GET)
    public ModelAndView healthedata() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/casestudies");
        mav.addObject("pageTitle", "Case Studies");
        return mav;
    }
   
    
}
