/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

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
@RequestMapping("/administrator/processing-activity")
public class adminProcessingActivity {
    
    /**
     * The private maxResults variable will hold the number of results to show per list page.
     */
    private static int maxResults = 30;
    
    /**
     * The '/inbound' GET request will serve up the existing list of generated referrals and feedback reports
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return	The configuration page list
     *
     * @Objects	(1) An object containing all the found batches
     *
     * @throws Exception
     */
    @RequestMapping(value = "/inbound", method = RequestMethod.GET)
    public ModelAndView listInBoundBatches(@RequestParam(value = "page", required = false) Integer page) throws Exception {
        
        if (page == null) {
            page = 1;
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/inbound");
        
    
        
        
        return mav;
        
    }
    
}
