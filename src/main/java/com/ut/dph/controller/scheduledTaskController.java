/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

import com.ut.dph.model.configurationSchedules;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.transactionInManager;
import com.ut.dph.service.transactionOutManager;
import java.util.List;
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
            processOutputRecords(transactionTargetId);
        }
    }
    
    
    /**
     * The 'processOutputRecords' function will start the process of generating the
     * output records. 
     * 
     * @param   transactionTargetId This will hold the id of a specific transaction to
     *                              process (Not required)
     */
    private void processOutputRecords(Integer transactionTargetId) {
        
        if(transactionTargetId == null) {
            transactionTargetId = 0;
        }
        
        /* 
        Need to find all transactionTarget records that are ready to be processed
        statusId (19 - Pending Output)
         */
        List<transactionTarget> pendingTransactions = transactionOutManager.getpendingOutPutTransactions(transactionTargetId);
        
        /* 
        If pending transactions are found need to loop through and check the 
        schedule setting for the configuration.
        */
        if(!pendingTransactions.isEmpty()) {
            
            for(transactionTarget transaction : pendingTransactions) {
            
                configurationSchedules scheduleDetails = configurationManager.getScheduleDetails(transaction.getconfigId());
                
                boolean processed = false;
                
                /* If no schedule details is found or the setting is for 'automatically' then process now */
                if(scheduleDetails == null || scheduleDetails.gettype() == 5) {
                    
                    /* Process the output (transactionTargetId, targetConfigId, transactionInId) */
                    processed = transactionOutManager.processOutPutTransactions(transaction.getId(), transaction.getconfigId(), transaction.gettransactionInId());
                    
                }
                /* If the setting is for 'Daily' */
                else if(scheduleDetails.gettype() == 2) {
                    
                }
                /* If the setting is for 'Weekly' */
                else if(scheduleDetails.gettype() == 3) {
                    
                }
                /* If the setting is for 'Monthly' */
                else if(scheduleDetails.gettype() == 4) {
                    
                }
                
                /* If processed == true update the status of the batch and transaction */
                if(processed == true) {
                    /* Update the status of the uploaded batch to  TBP (Target Batch Creating in process) (ID = 25) */
                    transactionInManager.updateBatchStatus(transaction.getbatchUploadId(),25,"");
                    
                    /* Update the status of the target batch to  TBP (Target Batch Creating in process) (ID = 25) */
                    transactionOutManager.updateTargetBatchStatus(transaction.getbatchDLId(),25,"");
                    
                    /* Need to start the transaction translations */
                    boolean recordsTranslated = transactionOutManager.translateTargetRecords(transaction.getId(), transaction.getconfigId());
                }
                
            
            }
            
        }
       
        
    }
    
}
