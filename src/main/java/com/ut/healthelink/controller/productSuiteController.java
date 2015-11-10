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
     * The '/universal-hie' request will display the Universal HIE information page.
     */
    @RequestMapping(value = "/universal-hie", method = RequestMethod.GET)
    public ModelAndView universalhie() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/universal-hie");
        mav.addObject("pageTitle", "Universal HIE");
        return mav;
    }
    
    /**
     * The '/clinical-data-warehouse' request will display the Clinical Data Warehouse information page.
     */
    @RequestMapping(value = "/clinical-data-warehouse", method = RequestMethod.GET)
    public ModelAndView clinicaldatawarehouse() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/clinical-data-warehouse");
        mav.addObject("pageTitle", "Clinical Data Warehouse");
        return mav;
    }
    
    /**
     * The '/careConnect' request will display the care connect information page.
     */
    @RequestMapping(value = "/careConnect", method = RequestMethod.GET)
    public ModelAndView careConnect() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/careConnect");
        mav.addObject("pageTitle", "Care Connect");
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
