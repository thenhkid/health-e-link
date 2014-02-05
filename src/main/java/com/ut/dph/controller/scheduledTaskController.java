/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

import com.ut.dph.model.transactionTarget;
import com.ut.dph.service.transactionOutManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author chadmccue
 */
@Controller
@RequestMapping("/scheduleTask")
public class scheduledTaskController {
    
    @Autowired
    private transactionOutManager transactionOutManager;
    
    /**
     * The 'processOutputRecords' function will start the process of generating the
     * output records. 
     */
    private void processOutputRecords() {
        
        /* 
        Need to find all transactionTarget records that are ready to be processed
        statusId (19 - Pending Output)
        
        List<transactionTarget> pendingTransactions = transactionOutManager.getpendingOutPutTransactions();
        */
        
    }
    
}
