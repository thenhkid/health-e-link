/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service.impl;

import com.ut.dph.dao.messageTypeDAO;
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
import com.ut.dph.reference.fileSystem;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.transactionInManager;
import com.ut.dph.service.transactionOutManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
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
    
    @Autowired
    private messageTypeDAO messageTypeDAO;
    
    @Override
    @Transactional
    public List<batchDownloads> getInboxBatches(int userId, int orgId, String searchTerm, Date fromDate, Date toDate, int page, int maxResults) {
        return transactionOutDAO.getInboxBatches(userId, orgId, searchTerm, fromDate, toDate, page, maxResults);
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
    
    @Override
    @Transactional
    public void updateTargetTransasctionStatus(int batchDLId, int statusId) {
        transactionOutDAO.updateTargetTransasctionStatus(batchDLId, statusId);
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
                
                configurationSchedules scheduleDetails = configurationManager.getScheduleDetails(transaction.getconfigId());
                
                /* If no schedule is found or automatic */
                if(scheduleDetails == null || scheduleDetails.gettype() == 5) {
                    
                    beginOutputProcess(transaction);
                            
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
                            beginOutputProcess(transaction);
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
    public void beginOutputProcess(transactionTarget transaction) {
         
        /* Update the status of the uploaded batch to  TBP (Target Batch Creating in process) (ID = 25) */
        transactionInManager.updateBatchStatus(transaction.getbatchUploadId(),25,"");
        
        batchUploads uploadedBatchDetails = transactionInManager.getBatchDetails(transaction.getbatchUploadId());
        
        configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                    
        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transaction.getconfigId());
       
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
            boolean createNewFile = true;

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
                   transactionOutDAO.updateTransactionTargetBatchDLId(batchId, transaction.getId());
                   
                   /* Need to add a new entry in the summary table (need to make sure we don't enter duplicates) */
                   batchDownloadSummary summary = new batchDownloadSummary();
                   summary.setbatchId(batchId);
                   summary.settargetConfigId(configDetails.getId());
                   summary.setmessageTypeId(configDetails.getMessageTypeId());
                   summary.settargetOrgId(configDetails.getorgId());
                   summary.settransactionTargetId(transaction.getId());
                   summary.setsourceOrgId(uploadedBatchDetails.getOrgId());

                   transactionOutDAO.submitSummaryEntry(summary);
                   
                   createNewFile = false;

                }

            }
            
            /* Generate the file */
            try {
                
                generateTargetFile(createNewFile, transaction.getId(), batchId, transportDetails);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(transactionOutManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        /* Update the status of the transaction to PP (Pending Pickup) (ID = 18) */
        transactionInManager.updateTransactionStatus(transaction.getbatchUploadId(), transaction.gettransactionInId(), 0, 18);

        /* Update the status of the transaction target to PP (Pending Pickup) (ID = 18) */
        transactionInManager.updateTransactionTargetStatus(0, transaction.getId(), 0, 18);

        /* Update the status of the uploaded batch to  TBP (Target Batch Created) (ID = 28) */
        transactionInManager.updateBatchStatus(transaction.getbatchUploadId(),28,"");
        
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
        transactionOutDAO.updateTransactionTargetBatchDLId(batchId, transaction.getId());
        
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
    
    /**
     * 
     */
    public void generateTargetFile(boolean createNewFile, int transactionTargetId, int batchId, configurationTransport transportDetails) throws IllegalAccessException, IllegalAccessException {
        
         String fileName = null; 
         
         batchDownloads batchDetails = transactionOutDAO.getBatchDetails(batchId);
         
         InputStream inputStream = null;
         OutputStream outputStream = null;
         
         fileSystem dir = new fileSystem();
         
         String filelocation = transportDetails.getfileLocation();
         filelocation = filelocation.replace("/bowlink/", "");
         
         dir.setDirByName(filelocation);
         
         String fileType = (String) configurationManager.getFileTypesById(transportDetails.getfileType());
         
         int findExt = batchDetails.getoutputFIleName().lastIndexOf(".");
         
         if(findExt >= 0) {
             fileName = batchDetails.getoutputFIleName();
         }
         else {
            fileName = new StringBuilder().append(batchDetails.getoutputFIleName()).append(".").append(fileType).toString(); 
         }
           
         File newFile = new File(dir.getDir() + fileName);
         
         
         /* Create the empty file in the correct location */
         if(createNewFile == true || !newFile.exists()) {
            try {
               
               if (newFile.exists()) {
                  int i = 1;
                  while (newFile.exists()) {
                      int iDot = fileName.lastIndexOf(".");
                      newFile = new File(dir.getDir() + fileName.substring(0, iDot) + "_(" + ++i + ")" + fileName.substring(iDot));
                  }
                  fileName = newFile.getName();
                  newFile.createNewFile();
              } else {
                  newFile.createNewFile();
              }
           } catch (IOException e) {
               e.printStackTrace();
           }
            
           /* Need to update the batch with the updated file name */
           transactionOutDAO.updateBatchOutputFileName(batchDetails.getId(),fileName);
           
         }
         
         
         /* Read in the file */
         try {
            FileInputStream fileInput = null; 
            File file = new File(dir.getDir() + fileName);
            fileInput = new FileInputStream(file);
            
            /* Need to get the records for the transaction */
            String recordRow = "";
            
            transactionOutRecords records = transactionOutDAO.getTransactionRecords(transactionTargetId);
            
            /* Need to get the max field number */
            int maxFieldNo = transactionOutDAO.getMaxFieldNo(transportDetails.getconfigId());
            
            /* Need to get the correct delimiter for the output file */
            String delimChar = (String) messageTypeDAO.getDelimiterChar(transportDetails.getfileDelimiter());
            
            if(records != null) {
                FileWriter fw = null;
                
                try {
                    fw = new FileWriter(file, true);
                } catch (IOException ex) {
                    Logger.getLogger(transactionOutManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }

                for(int i = 1; i <= maxFieldNo; i++) {
                    
                    String colName = new StringBuilder().append("f").append(i).toString();
                
                    try {
                        String fieldValue = BeanUtils.getProperty(records, colName);
                        
                        if("null".equals(fieldValue)) {
                            fieldValue = "";
                        }
                        
                        if(i == maxFieldNo) {
                            recordRow = new StringBuilder().append(recordRow).append(fieldValue).append("\n").toString();
                        }
                        else {
                            recordRow = new StringBuilder().append(recordRow).append(fieldValue).append(delimChar).toString();
                        }
                        
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(transactionOutManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(transactionOutManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchMethodException ex) {
                        Logger.getLogger(transactionOutManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                if(recordRow != null) {
                    try {
                        fw.write(recordRow);
                        fw.close();
                    } catch (IOException ex) {
                        Logger.getLogger(transactionOutManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
            
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         }
         
    }
    
    @Override
    @Transactional
    public List<batchDownloads> getdownloadableBatches(int userId, int orgId, Date fromDate, Date toDate, String searchTerm, int page, int maxResults) {
        return transactionOutDAO.getdownloadableBatches(userId, orgId, fromDate, toDate, searchTerm, page, maxResults);
    }
   
}
