/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service.impl;

import com.ut.dph.dao.transactionOutDAO;
import com.ut.dph.model.batchDownloadSummary;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationConnectionReceivers;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.configurationSchedules;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionOutNotes;
import com.ut.dph.model.transactionOutRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.transactionInManager;
import com.ut.dph.service.transactionOutManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private configurationTransportManager configurationTransportManager;
    
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
    
    
    /**
     * The 'processOutputRecords' function will look for pending output records and
     * start the translation process on the records to generate for the target. This function is called 
     * from the processOutputRecords scheduled job but can also be called via a web call to 
     * initiate the process manually. The scheduled job runs every 1 minute.
     * 
     * @param transactionTargetId  The id of a specific transaction to process (defaults to 0)
     */
    @Override
    @Transactional
    public void processOutputRecords(int transactionTargetId) {
       
        /* 
        Need to find all transactionTarget records that are ready to be processed
        statusId (19 - Pending Output)
         */
        List<transactionTarget> pendingTransactions = transactionOutDAO.getpendingOutPutTransactions(transactionTargetId);
        
        /* 
        If pending transactions are found need to loop through and start the processing
        of the outbound records.
        */
        if(!pendingTransactions.isEmpty()) {
           
            for(transactionTarget transaction : pendingTransactions) {
            
                boolean processed = false;
                
                /* Process the output (transactionTargetId, targetConfigId, transactionInId) */
                processed = transactionOutDAO.processOutPutTransactions(transaction.getId(), transaction.getconfigId(), transaction.gettransactionInId());
                    
                /* If processed == true update the status of the batch and transaction */
                if(processed == true) {
                    
                    /* Need to start the transaction translations */
                    boolean recordsTranslated = translateTargetRecords(transaction.getId(), transaction.getconfigId(), transaction.getbatchDLId());
                    
                    /* Once all the processing has completed with no errors need to copy records to the transactionOutRecords to make availble to view */
                    if(recordsTranslated == true) {
                        transactionOutDAO.moveTranslatedRecords(transaction.getId());
                        
                        /* Update the status of the transaction to L (Loaded) (ID = 9) */
                        transactionInManager.updateTransactionStatus(transaction.getbatchUploadId(), transaction.gettransactionInId(), 0, 9);
                        
                        /* Update the status of the transaction target to L (Loaded) (ID = 9) */
                        transactionInManager.updateTransactionTargetStatus(0, transaction.getId(), 0, 9);

                    }
                    
                }
            }
        }
    }
    
    /**
     * The 'generateOutputFiles' function will look to see if any output files need to 
     * be generated. This function is called from the generateOutputFiles scheduled
     * job. Running every 10 minutes.
     * 
     */
    @Override
    @Transactional
    public void generateOutputFiles() {
       
        /* 
        Need to find all transactionTarget records that are loaded ready to moved to a downloadable
        batch
         */
        List<transactionTarget> loadedTransactions = transactionOutDAO.getLoadedOutBoundTransactions();
        
        /* 
        If pending pick up transactions are found need to loop through and check the 
        schedule setting for the configuration.
        */
        if(!loadedTransactions.isEmpty()) {
           
            for(transactionTarget transaction : loadedTransactions) {
                
                batchUploads uploadedBatchDetails = transactionInManager.getBatchDetails(transaction.getbatchUploadId());
            
                configurationSchedules scheduleDetails = configurationManager.getScheduleDetails(transaction.getconfigId());
                
                configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                    
                configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transaction.getconfigId());
                
                /* If no schedule is found or automatic */
                if(scheduleDetails == null || scheduleDetails.gettype() == 5) {
                    
                    beginOutputProcess(configDetails, transaction, transportDetails, uploadedBatchDetails);
                            
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
                            beginOutputProcess(configDetails, transaction, transportDetails, uploadedBatchDetails);
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
                    
            }
            
            /* Generate the file */
            System.out.println("DONE 2");
                
       }
    }
    
    
    /**
     * The 'beginOutputProcess' function will start the process to creating the target download transaction
     * 
     * @param configDetails
     * @param transaction
     * @param transportDetails
     * @param uploadedBatchDetails 
     */
    public void beginOutputProcess(configuration configDetails, transactionTarget transaction, configurationTransport transportDetails, batchUploads uploadedBatchDetails) {
         
        /* Update the status of the uploaded batch to  TBP (Target Batch Creating in process) (ID = 25) */
        transactionInManager.updateBatchStatus(transaction.getbatchUploadId(),25,"");
       
        /* Check to see what outut transport method was set up */
        
        /* ERG */
        if(transportDetails.gettransportMethodId() == 2) {

            /* Generate the batch */
            /* (target configuration Details, transaction details, transport Details for target config, Source OrgId, Source Original Filename, mergeable) */
            int batchId = generateBatch(configDetails, transaction, transportDetails, uploadedBatchDetails.getOrgId(), uploadedBatchDetails.getoriginalFileName(), false);

        }
        /* File Download */
        else if(transportDetails.gettransportMethodId() == 1) {

            int batchId = 0;

            /* 
                If the merge batches option is not checked create the batch right away
            */
           
            if(transportDetails.getmergeBatches() == false) {

                /* Generate the batch */
                /* (target configuration Details, transaction details, transport Details for target config, Source OrgId, Source Original Filename, mergeable) */
                batchId = generateBatch(configDetails, transaction, transportDetails, uploadedBatchDetails.getOrgId(), uploadedBatchDetails.getoriginalFileName(), false);

            }
            else {

                /* We want to merge this transaction with the existing created batch if not yet opened */
                /* 1. Need to see if a mergable batch exists for the org that hasn't been picked up yet */
                int mergeablebatchId = transactionOutDAO.findMergeableBatch(configDetails.getorgId());
                
                /* If no mergable batch is found create a new batch */
                if(mergeablebatchId == 0) {
                    /* Generate the batch */
                    /* (target configuration Details, transaction details, transport Details for target config, Source OrgId, Source Original Filename, mergeable) */
                    batchId = generateBatch(configDetails, transaction, transportDetails, uploadedBatchDetails.getOrgId(), uploadedBatchDetails.getoriginalFileName(), true);

                }
                else {

                   batchId = mergeablebatchId;
                   
                   /* Need to upldate the transaction batchDLId to the new found batch Id */
                   transaction.setbatchDLId(batchId);
                   transactionOutDAO.updateTransactionDetails(transaction);

                   /* Need to add a new entry in the summary table (need to make sure we don't enter duplicates) */
                   batchDownloadSummary summary = new batchDownloadSummary();
                   summary.setbatchId(batchId);
                   summary.settargetConfigId(configDetails.getId());
                   summary.setmessageTypeId(configDetails.getMessageTypeId());
                   summary.settargetOrgId(configDetails.getorgId());
                   summary.settransactionTargetId(transaction.getId());
                   summary.setsourceOrgId(uploadedBatchDetails.getOrgId());

                   transactionOutDAO.submitSummaryEntry(summary);

                }

            }
            
            batchDownloads batchDetails = transactionOutDAO.getBatchDetails(batchId);

            /* Generate the file */
            System.out.println("GENERATE FILE");
            
            /* Update the status of the transaction to PP (Pending Pickup) (ID = 18) */
            transactionInManager.updateTransactionStatus(transaction.getbatchUploadId(), transaction.gettransactionInId(), 0, 18);

            /* Update the status of the transaction target to PP (Pending Pickup) (ID = 18) */
            transactionInManager.updateTransactionTargetStatus(0, transaction.getId(), 0, 18);
            
            /* Update the status of the uploaded batch to  TBP (Target Batch Created) (ID = 28) */
            transactionInManager.updateBatchStatus(transaction.getbatchUploadId(),28,"");
            
            /* Generate the file */
            System.out.println("DONE 1");

        }
    }
    
    
    
    /**
     * The 'generateBatch' function will create the new download batch for the target
     */
    public int generateBatch(configuration configDetails, transactionTarget transaction, configurationTransport transportDetails, int sourceOrgId, String sourceFileName, boolean mergeable) {
        
        /* Create the batch name (OrgId+MessageTypeId+Date/Time) */
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String utbatchName = new StringBuilder().append(configDetails.getorgId()).append(configDetails.getMessageTypeId()).append(dateFormat.format(date)).toString();
        
        
        /* Need to create a new batch */
        String batchName = null;
        
        if(transportDetails.gettargetFileName() == null) {
            /* Create the batch name (OrgId+MessageTypeId) */
            batchName = new StringBuilder().append(configDetails.getorgId()).append(configDetails.getMessageTypeId()).toString();
        }
        else if ("USE SOURCE FILE".equals(transportDetails.gettargetFileName())) {
            int lastPeriodPos = sourceFileName.lastIndexOf(".");
            
            if(lastPeriodPos <= 0) {
                batchName = sourceFileName;
            }
            else {
                batchName = sourceFileName.substring(0,lastPeriodPos);
            }
            
        }
        else {
            batchName = transportDetails.gettargetFileName();
            
        }
        
        /* Append the date time */
        if(transportDetails.getappendDateTime() == true) {
            batchName = new StringBuilder().append(batchName).append(dateFormat.format(date)).toString();
        }

        /* Get the connection id for the configuration */
        List<configurationConnection> connections = configurationManager.getConnectionsByTargetConfiguration(transaction.getconfigId());

        int userId = 0;
        if(!connections.isEmpty()) {
            for(configurationConnection connection : connections) {
                List<configurationConnectionReceivers> receivers = configurationManager.getConnectionReceivers(connection.getId());

                if(!receivers.isEmpty()) {
                    for(configurationConnectionReceivers receiver : receivers) {
                        userId = receiver.getuserId();
                    }
                }

            }
        }
        
        /* Submit a new batch */
        batchDownloads batchDownload = new batchDownloads();
        batchDownload.setOrgId(configDetails.getorgId());
        batchDownload.setuserId(userId);
        batchDownload.setutBatchName(utbatchName);
        batchDownload.settotalErrorCount(0);
        batchDownload.settotalRecordCount(1);
        batchDownload.setdeleted(false);
        batchDownload.settransportMethodId(transportDetails.gettransportMethodId());
        batchDownload.setoutputFIleName(batchName);
        batchDownload.setmergeable(mergeable);

        /* Update the status of the target batch to TBP (Target Batch Created) (ID = 28) */
        batchDownload.setstatusId(28);

        int batchId = (int) transactionOutDAO.submitBatchDownload(batchDownload);

        /* Need to upldate the transaction batchDLId to the new created batch Id */
        transaction.setbatchDLId(batchId);
        transactionOutDAO.updateTransactionDetails(transaction);
        
        /* Need to submit the batch summary */
        batchDownloadSummary summary = new batchDownloadSummary();
        summary.setbatchId(batchId);
        summary.settargetConfigId(configDetails.getId());
        summary.setmessageTypeId(configDetails.getMessageTypeId());
        summary.settargetOrgId(configDetails.getorgId());
        summary.settransactionTargetId(transaction.getId());
        summary.setsourceOrgId(sourceOrgId);
        
        transactionOutDAO.submitSummaryEntry(summary);
        
        return batchId;
        
    }
}
