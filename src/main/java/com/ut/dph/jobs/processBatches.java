/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.jobs;
import com.ut.dph.service.transactionInManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 *
 * @author gchan
 */
public class processBatches implements Job {
    
    @Autowired
    private transactionInManager transactionInManager;
    
    @Override
    public void execute(JobExecutionContext context)  throws JobExecutionException {
        
        try {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            System.out.println(new java.util.Date() + " start of process batch");
            transactionInManager.processBatches();
            System.out.println(new java.util.Date() + " end of process batch");
        } catch (Exception ex) {
            try {
                throw new Exception("Error occurred trying to process batch files from schedule task",ex);
            } catch (Exception ex1) {
                Logger.getLogger(processBatches.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
        
    }
    
}