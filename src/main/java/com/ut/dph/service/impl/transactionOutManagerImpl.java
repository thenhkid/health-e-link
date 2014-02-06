/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service.impl;

import com.ut.dph.dao.transactionOutDAO;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.configurationSchedules;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionOutNotes;
import com.ut.dph.model.transactionOutRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.transactionInManager;
import com.ut.dph.service.transactionOutManager;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author chadmccue
 */
@Service
public class transactionOutManagerImpl implements transactionOutManager {
    
    @Autowired
    private transactionOutDAO transactionOutDAO;
    
    @Autowired
    private configurationManager configurationManager;
    
    @Autowired
    private transactionInManager transactionInManager;
    
    @Override
    @Transactional
    public List<batchDownloads> getInboxBatches(int userId, int orgId, int page, int maxResults) {
        return transactionOutDAO.getInboxBatches(userId, orgId, page, maxResults);
    }
    
    @Override
    @Transactional
    public List<batchDownloads> findInboxBatches(List<batchDownloads> batches, String searchTerm) {
        return transactionOutDAO.findInboxBatches(batches, searchTerm);
    }
    
    @Override
    @Transactional
    public batchDownloads getBatchDetails(int batchId) {
        return transactionOutDAO.getBatchDetails(batchId);
    }
    
    @Override
    @Transactional
    public List<transactionTarget> getInboxBatchTransactions(int batchId, int userId) {
        return transactionOutDAO.getInboxBatchTransactions(batchId, userId);
    }
    
    @Override
    @Transactional
    public transactionTarget getTransactionDetails(int transactionId) {
        return transactionOutDAO.getTransactionDetails(transactionId);
    }
    
    @Override
    @Transactional
    public transactionOutRecords getTransactionRecords(int transactionTargetId) {
        return transactionOutDAO.getTransactionRecords(transactionTargetId);
    }

    @Override
    @Transactional
    public transactionOutRecords getTransactionRecord(int recordId) {
        return transactionOutDAO.getTransactionRecord(recordId);
    }

    @Override
    @Transactional
    public void changeDeliveryStatus(int batchDLId, int batchUploadId, int transactionTargetId, int transactionInId) {
        transactionOutDAO.changeDeliveryStatus(batchDLId, batchUploadId, transactionTargetId, transactionInId);
    }
    
    @Override
    @Transactional
    public List getInternalStatusCodes() {
        return transactionOutDAO.getInternalStatusCodes();
    }
    
    @Override
    @Transactional
    public void updateTransactionDetails(transactionTarget transactionDetails) {
        transactionOutDAO.updateTransactionDetails(transactionDetails);
    }
    
    @Override
    @Transactional
    public void saveNote(transactionOutNotes note) {
        transactionOutDAO.saveNote(note);
    }
    
    @Override
    @Transactional
    public List<transactionOutNotes> getNotesByTransactionId(int transactionId) {
        return transactionOutDAO.getNotesByTransactionId(transactionId);
    }
    
    @Override
    @Transactional
    public void removeNoteById(int noteId) {
        transactionOutDAO.removeNoteById(noteId);
    }
    
    @Override
    @Transactional
    public Integer getActiveFeedbackReportsByMessageType(int messageTypeId, int orgId) {
        return transactionOutDAO.getActiveFeedbackReportsByMessageType(messageTypeId, orgId);
    }
    
    @Override
    @Transactional
    public List<transactionIn> getFeedbackReports(int transactionId, String fromPage) {
        return transactionOutDAO.getFeedbackReports(transactionId, fromPage);
    }
    
    @Override
    @Transactional
    public transactionTarget getTransactionsByInId(int transactionInId) {
        return transactionOutDAO.getTransactionsByInId(transactionInId);
    }
    
    @Override
    @Transactional
    public List<transactionTarget> getpendingOutPutTransactions(int transactionTargetId) {
        return transactionOutDAO.getpendingOutPutTransactions(transactionTargetId);
    }
    
    @Override
    @Transactional
    public boolean processOutPutTransactions(int transactionTargetId, int configId, int transactionInId) {
        return transactionOutDAO.processOutPutTransactions(transactionTargetId, configId, transactionInId);
    }
    
    @Override
    @Transactional
    public void updateTargetBatchStatus(Integer batchDLId, Integer statusId, String timeField) {
        transactionOutDAO.updateTargetBatchStatus(batchDLId, statusId, timeField);
    }

    /**
     * The 'translateTargetRecords' function will attempt to translate the target records based on the
     * translation details set up in the target configuration.
     * 
     * @param transactionTargetId   The id of the target transaction to be translated
     * @param batchId               The id of the batch the target transaction belongs to
     * @param configId              The id of the target configuration.
     * 
     * @return This function will return either TRUE (If translation completed with no errors)
     *         OR FALSE (If translation failed for any reason)
     */
    @Override
    public boolean translateTargetRecords(int transactionTargetId, int configId, int batchId) {
        
        boolean translated = false;
        
        /* Need to get the configured data translations */
        List<configurationDataTranslations> dataTranslations = configurationManager.getDataTranslationsWithFieldNo(configId);
        
        for (configurationDataTranslations cdt : dataTranslations) {
            if (cdt.getCrosswalkId() != 0) {
                   translated = transactionInManager.processCrosswalk (configId, batchId, cdt, true);
            } 
            else if (cdt.getMacroId()!= 0)  {
                   translated = transactionInManager.processMacro (configId, batchId, cdt, true);
            }
        }
        
        return translated;
    }
    
    @Override
    @Transactional
    public void moveTranslatedRecords(int transactionTargetId) {
        transactionOutDAO.moveTranslatedRecords(transactionTargetId);
    }
    
    @Override
    @Transactional
    public void processOutputRecords(int transactionTargetId) {
       
        /* 
        Need to find all transactionTarget records that are ready to be processed
        statusId (19 - Pending Output)
         */
        List<transactionTarget> pendingTransactions = transactionOutDAO.getpendingOutPutTransactions(transactionTargetId);
        
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
                    processed = transactionOutDAO.processOutPutTransactions(transaction.getId(), transaction.getconfigId(), transaction.gettransactionInId());
                    
                }
                /* If the setting is for 'Daily' */
                else if(scheduleDetails.gettype() == 2) {
                    
                    /* if Daily check for scheduled or continuous */
                    if(scheduleDetails.getprocessingType() == 1) {
                        /* SCHEDULED */
                        Date date = new Date();
                        Calendar calendar = GregorianCalendar.getInstance();
                        calendar.setTime(date);
                        
                        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                       
                        if(hourOfDay >= scheduleDetails.getprocessingTime()) {
                            System.out.println("RUN");
                        }
                    }
                    else {
                        /* CONTINUOUS */
                        
                    }
                    
                    
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
                    transactionOutDAO.updateTargetBatchStatus(transaction.getbatchDLId(),25,"");
                    
                    /* Need to start the transaction translations */
                    boolean recordsTranslated = translateTargetRecords(transaction.getId(), transaction.getconfigId(), transaction.getbatchDLId());
                    
                    /* Once all the processing has completed with no errors need to copy records to the transactionOutRecords to make availble to view */
                    if(recordsTranslated == true) {
                        transactionOutDAO.moveTranslatedRecords(transaction.getId());
                        
                        /* Update the status of the transaction to PP (Pending Pickup) (ID = 18) */
                        transactionInManager.updateTransactionStatus(transaction.getbatchUploadId(), transaction.gettransactionInId(), 0, 18);
                        
                        /* Update the status of the transaction target to PP (Pending Pickup) (ID = 18) */
                        transactionInManager.updateTransactionTargetStatus(transaction.getbatchUploadId(), transaction.gettransactionInId(), 0, 18);
                        
                        /* Update the status of the uploaded batch to  TBP (Target Batch Created) (ID = 28) */
                        transactionInManager.updateBatchStatus(transaction.getbatchUploadId(),28,"");

                        /* Update the status of the target batch to  TBP (Target Batch Created) (ID = 28) */
                        transactionOutDAO.updateTargetBatchStatus(transaction.getbatchDLId(),28,"");
                    }
                    
                    /* Check to see if an output file is to be generated */
                    
                }
                
            }
            
        }
    }
    
}
