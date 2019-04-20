package com.ut.healthelink.jobs;

import com.ut.healthelink.service.transactionOutManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 *
 * @author chadmccue
 */
public class processMassOutputRecords implements Job {
    
    @Autowired
    private transactionOutManager transactionOutManager;
    
    @Override
    public void execute(JobExecutionContext context)  throws JobExecutionException {
        try {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            transactionOutManager.processMassOutputBatches();
        } catch (Exception ex) {
            try {
                throw new Exception("Error occurred trying to process output records from schedule task",ex);
            } catch (Exception ex1) {
                Logger.getLogger(processMassOutputRecords.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
    }
    
}
