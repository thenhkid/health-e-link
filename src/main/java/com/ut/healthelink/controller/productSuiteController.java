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
@RequestMapping("/product-suite")
public class productSuiteController {
    
    /**
     * The '' request will display the product suite information page.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView productSuite() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/productSuite");
        mav.addObject("pageTitle", "Product Suite");
        return mav;
    }
    
    /**
     * The '/health-e-net' request will display the health-e-net information page.
     */
    @RequestMapping(value = "/health-e-net", method = RequestMethod.GET)
    public ModelAndView healthenet() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/healthenet");
        mav.addObject("pageTitle", "Health-e-Net");
        return mav;
    }
    
    /**
     * The '/health-e-data' request will display the health-e-data information page.
     */
    @RequestMapping(value = "/health-e-data", method = RequestMethod.GET)
    public ModelAndView healthedata() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/healthedata");
        mav.addObject("pageTitle", "Health-e-Data");
        return mav;
    }
    
    /**
     * The '/health-e-web' request will display the health-e-web information page.
     */
    @RequestMapping(value = "/health-e-web", method = RequestMethod.GET)
    public ModelAndView healtheweb() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/healtheweb");
        mav.addObject("pageTitle", "Health-e-Web");
        return mav;
    }
    
    /**
     * The '/doc-u-link' request will display the DOC-u-Link information page.
     */
    @RequestMapping(value = "/doc-u-link", method = RequestMethod.GET)
    public ModelAndView doculink() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/doculink");
        mav.addObject("pageTitle", "DOC-u-Link");
        return mav;
    }
    
    
}
