/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.jobs;

import com.ut.dph.service.transactionOutManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author chadmccue
 */
public class processOutputFiles implements Job {
    
    @Autowired
    private transactionOutManager transactionOutManager;
    
    @Override
    public void execute(JobExecutionContext context)  throws JobExecutionException {
        
        transactionOutManager.processOutputRecords(0);
        
    }

}
