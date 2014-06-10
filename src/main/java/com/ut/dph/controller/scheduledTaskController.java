/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

import com.ut.dph.service.configurationManager;
import com.ut.dph.service.transactionInManager;
import com.ut.dph.service.transactionOutManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author chadmccue
 */
@Controller
@RequestMapping("/scheduleTask")
public class scheduledTaskController {
    
    @Autowired
    private transactionOutManager transactionOutManager;
    
    @Autowired
    private configurationManager configurationManager;
    
    @Autowired
    private transactionInManager transactionInManager;
    
    /**
     * The '/procesOutputRecords' method will allow a sys admin to enter the correct
     * password and bypass the scheduler and run the processOutputRecords method at 
     * anytime.
     * 
     * @param password  The password to access this page.
     * @param id        The id of a specific transaction to process (not required)
     * 
     * @throws Exception 
     */
    @RequestMapping(value = "/processOutputRecords", method = RequestMethod.GET) 
    public void runProcess(@RequestParam(value = "password", required = true) String password, @RequestParam(value= "id", required = false) Integer transactionTargetId) throws Exception {
        
        if(transactionTargetId == null) {
            transactionTargetId = 0;
        }
        
        if("!sysadmin!".equals(password)) {
            
            try {
                processOutputRecords(transactionTargetId);
            }
            catch (Exception e) {
                throw new Exception("Error occurred trying to process output records. transactionTargetId: " + transactionTargetId,e);
            }
            
        }
    }
    
    
    /**
     * The 'processOutputRecords' function will start the process of generating the
     * output records. 
     * 
     * @param   transactionTargetId This will hold the id of a specific transaction to
     *                              process (Not required)
     */
    private void processOutputRecords(Integer transactionTargetId) throws Exception {
        
        if(transactionTargetId == null) {
            transactionTargetId = 0;
        }
        
        try {
            transactionOutManager.selectOutputRecordsForProcess(transactionTargetId);
        }
        catch (Exception e) {
            throw new Exception("Error occurred trying to process output records. transactionTargetId: " + transactionTargetId,e);
        }
        
        
    }
    
}
