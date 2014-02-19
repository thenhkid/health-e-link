/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service.impl;

import com.ut.dph.dao.messageTypeDAO;
import com.ut.dph.dao.transactionOutDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.model.User;
import com.ut.dph.model.batchDownloadSummary;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationConnectionReceivers;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.configurationSchedules;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.service.emailMessageManager;
import com.ut.dph.model.mailMessage;
import com.ut.dph.model.targetOutputRunLogs;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionOutNotes;
import com.ut.dph.model.transactionOutRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.reference.fileSystem;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.organizationManager;
import com.ut.dph.service.transactionInManager;
import com.ut.dph.service.transactionOutManager;
import com.ut.dph.service.userManager;
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
import java.util.ArrayList;
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
    
    @Autowired
    private userManager userManager;
    
    @Autowired
    private organizationManager organizationManager;
    
    @Autowired
    private emailMessageManager emailMessageManager;
    
    @Override
    @Transactional
    public List<batchDownloads> getInboxBatches(int userId, int orgId, String searchTerm, Date fromDate, Date toDate, int page, int maxResults) throws Exception {
        return transactionOutDAO.getInboxBatches(userId, orgId, searchTerm, fromDate, toDate, page, maxResults);
    }
    
    @Override
    @Transactional
    public batchDownloads getBatchDetails(int batchId) throws Exception {
        return transactionOutDAO.getBatchDetails(batchId);
    }
    
    @Override
    @Transactional
    public List<transactionTarget> getInboxBatchTransactions(int batchId, int userId) throws Exception {
        return transactionOutDAO.getInboxBatchTransactions(batchId, userId);
    }
    
    @Override
    @Transactional
    public transactionTarget getTransactionDetails(int transactionId) throws Exception {
        return transactionOutDAO.getTransactionDetails(transactionId);
    }
    
    @Override
    @Transactional
    public transactionOutRecords getTransactionRecords(int transactionTargetId) throws Exception {
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
    public void updateTransactionDetails(transactionTarget transactionDetails) throws Exception {
        transactionOutDAO.updateTransactionDetails(transactionDetails);
    }
    
    @Override
    @Transactional
    public void saveNote(transactionOutNotes note) throws Exception {
        transactionOutDAO.saveNote(note);
    }
    
    @Override
    @Transactional
    public List<transactionOutNotes> getNotesByTransactionId(int transactionId) throws Exception {
        return transactionOutDAO.getNotesByTransactionId(transactionId);
    }
    
    @Override
    @Transactional
    public void removeNoteById(int noteId) throws Exception {
        transactionOutDAO.removeNoteById(noteId);
    }
    
    @Override
    @Transactional
    public Integer getActiveFeedbackReportsByMessageType(int messageTypeId, int orgId) throws Exception {
        return transactionOutDAO.getActiveFeedbackReportsByMessageType(messageTypeId, orgId);
    }
    
    @Override
    @Transactional
    public List<transactionIn> getFeedbackReports(int transactionId, String fromPage) throws Exception {
        return transactionOutDAO.getFeedbackReports(transactionId, fromPage);
    }
    
    @Override
    @Transactional
    public transactionTarget getTransactionsByInId(int transactionInId) throws Exception {
        return transactionOutDAO.getTransactionsByInId(transactionInId);
    }
    
    @Override
    @Transactional
    public List<transactionTarget> getpendingOutPutTransactions(int transactionTargetId) throws Exception {
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
    public Integer translateTargetRecords(int transactionTargetId, int configId, int batchId) {
        
        Integer errorCount = 0;
        
        /* Need to get the configured data translations */
        List<configurationDataTranslations> dataTranslations = configurationManager.getDataTranslationsWithFieldNo(configId);
        
        for (configurationDataTranslations cdt : dataTranslations) {
            if (cdt.getCrosswalkId() != 0) {
            	errorCount = errorCount + transactionInManager.processCrosswalk (configId, batchId, cdt, true);
            } 
            else if (cdt.getMacroId()!= 0)  {
            	errorCount = errorCount + transactionInManager.processMacro (configId, batchId, cdt, true);
            }
        }
        return errorCount;
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
       
        try {
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
                        Integer recordsTranslated = translateTargetRecords(transaction.getId(), transaction.getconfigId(), transaction.getbatchDLId());

                        /* Once all the processing has completed with no errors need to copy records to the transactionOutRecords to make available to view */
                        if(recordsTranslated == 0) { // no errors
                            transactionOutDAO.moveTranslatedRecords(transaction.getId());

                            /* Update the status of the transaction to L (Loaded) (ID = 9) */
                            transactionInManager.updateTransactionStatus(transaction.getbatchUploadId(), transaction.gettransactionInId(), 0, 9);

                            /* Update the status of the transaction target to L (Loaded) (ID = 9) */
                            transactionInManager.updateTransactionTargetStatus(0, transaction.getId(), 0, 9);

                            /* If configuration is set to auto process the process right away */
                            configurationSchedules scheduleDetails = configurationManager.getScheduleDetails(transaction.getconfigId());

                            /* If no schedule is found or automatic */
                            if(scheduleDetails == null || scheduleDetails.gettype() == 5) {
                                int batchId =  beginOutputProcess(transaction);

                                /* Log the last run time */
                                targetOutputRunLogs log = new targetOutputRunLogs();
                                log.setconfigId(transaction.getconfigId());

                                transactionOutDAO.saveOutputRunLog(log);

                                /* 
                                Need to check the transport method for the configuration, if set to file download
                                or set to FTP.
                                */
                                configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transaction.getconfigId());

                                /* if File Download update the status to Submission Delivery Completed ID = 23 status. This will only
                                apply to scheduled and not continous settings. */
                                if(transportDetails.gettransportMethodId() == 1) {
                                    transactionOutDAO.updateBatchStatus(batchId, 23);
                                }
                                /* If FTP Call the FTP Method */
                                else if(transportDetails.gettransportMethodId() == 3) {
                                    FTPTargetFile(batchId);
                                }

                                if(batchId > 0) {
                                    /* Send the email to primary contact */

                                    /* Get the batch Details */
                                   batchDownloads batchDLInfo = transactionOutDAO.getBatchDetails(batchId);

                                   /* Get the from user */
                                   Organization fromOrg = organizationManager.getOrganizationById(configurationManager.getConfigurationById(transactionInManager.getTransactionDetails(transaction.gettransactionInId()).getconfigId()).getorgId());
                                   List<User> fromPrimaryContact = userManager.getOrganizationContact(fromOrg.getId(),1);

                                   /* get the to user details */
                                   List<User> toPrimaryContact = userManager.getOrganizationContact(configurationManager.getConfigurationById(transaction.getconfigId()).getorgId(),1);
                                   List<User> toSecondaryContact = userManager.getOrganizationContact(configurationManager.getConfigurationById(transaction.getconfigId()).getorgId(),2);

                                   if(fromPrimaryContact.size() > 0 && (toPrimaryContact.size() > 0 || toSecondaryContact.size() > 0)) {
                                       String toName = null;
                                       mailMessage msg = new mailMessage();
                                       ArrayList<String> ccAddressArray = new ArrayList<String>();
                                       msg.setfromEmailAddress(fromPrimaryContact.get(0).getEmail());
                                       

                                       if(toPrimaryContact.size() > 0) {
                                           toName = toPrimaryContact.get(0).getFirstName() + " " + toPrimaryContact.get(0).getLastName();
                                           msg.settoEmailAddress(toPrimaryContact.get(0).getEmail());

                                           if(toPrimaryContact.size() > 1) {
                                               for(int i = 1; i < toPrimaryContact.size(); i++) {
                                                   ccAddressArray.add(toPrimaryContact.get(i).getEmail());
                                               }
                                           }

                                           if(toSecondaryContact.size() > 0) {
                                              for(int i = 0; i <= toSecondaryContact.size(); i++) {
                                                   ccAddressArray.add(toSecondaryContact.get(i).getEmail());
                                               }
                                           }
                                       }
                                       else {
                                           toName = toSecondaryContact.get(0).getFirstName() + " " + toSecondaryContact.get(0).getLastName();
                                           msg.settoEmailAddress(toSecondaryContact.get(0).getEmail());

                                           if(toSecondaryContact.size() > 1) {
                                               for(int i = 1; i < toSecondaryContact.size(); i++) {
                                                   ccAddressArray.add(toSecondaryContact.get(i).getEmail());
                                               }
                                           }
                                       }

                                       if(ccAddressArray.size() > 0) {
                                           String[] ccAddressList = new String[ccAddressArray.size()];
                                           ccAddressList = ccAddressArray.toArray(ccAddressList);
                                           msg.setccEmailAddress(ccAddressList);
                                       }

                                       msg.setmessageSubject("You have received a new message from the Universal Translator");

                                       /* Build the body of the email */
                                       StringBuilder sb = new StringBuilder();
                                       sb.append("Dear " + toName + ", You have recieved a new message from the Universal Translator. ");
                                       sb.append(System.getProperty("line.separator"));
                                       sb.append(System.getProperty("line.separator"));
                                       sb.append("BatchId: "+batchDLInfo.getutBatchName());
                                       if(batchDLInfo.getoutputFIleName() != null && !"".equals(batchDLInfo.getoutputFIleName())) {
                                            sb.append(System.getProperty("line.separator"));
                                            sb.append("File Name: "+batchDLInfo.getoutputFIleName());
                                        }
                                       sb.append(System.getProperty("line.separator"));
                                       sb.append("From Organization: "+fromOrg.getOrgName());

                                       msg.setmessageBody(sb.toString());

                                       /* Send the email */
                                       emailMessageManager.sendEmail(msg);

                                   }
                                }

                            }
                        }

                    }
                }
            }
        }
        catch(Exception ex) {
            try {
                throw new IOException(ex);
            } catch (IOException ex1) {
                Logger.getLogger(transactionOutManagerImpl.class.getName()).log(Level.SEVERE, null, ex1);
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
        
        1. When the beginOutput Process function returns (RETURN BATCH ID???) check to see if the target transport method 
           for the config is set to FTP, if so then call the FTP method to send off the file. Batch would then get a 
           Submission Delivery Locked ID = 22 status.
        2. For file download transport methods, after the beginOutput Process function returns with the batch Id, the batch
           would then get a Submission Delivery Completed ID = 23 status so we don't keep adding new transactions to already
           created batch files. FOR SCHEDULED PROCESSING ONLY, CONTINUOUS processing will keep adding to the same file until
           the file is downloaded.
        */
        
        try{
            /* Get a list of scheduled configurations (Daily, Weekly or Monthly) */
            List<configurationSchedules> scheduledConfigs = transactionOutDAO.getScheduledConfigurations();

            if(!scheduledConfigs.isEmpty()) {

                int batchId = 0;

                for(configurationSchedules schedule : scheduledConfigs) {

                    batchId = 0;

                    /* DAILY SCHEDULE */
                    if(schedule.gettype() == 2) {

                        Date currDate = new Date();
                        Calendar calendar = GregorianCalendar.getInstance();
                        calendar.setTime(currDate);

                        /* Need to get the latest log to make sure we don't run it again in the same day */
                        List<targetOutputRunLogs> logs = transactionOutDAO.getLatestRunLog(schedule.getconfigId());

                        /* if Daily check for scheduled or continuous */
                        if(schedule.getprocessingType() == 1) {

                            /* SCHEDULED */
                            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

                            double diffInHours;
                            if(logs.size() > 0) {
                                targetOutputRunLogs log = logs.get(0);
                                long diff = currDate.getTime() - log.getlastRunTime().getTime();
                                diffInHours = diff / ((double) 1000 * 60 * 60);
                            }
                            else {
                                diffInHours = 0;
                            }

                            if(hourOfDay >= schedule.getprocessingTime() && (diffInHours == 0 || diffInHours >= 24)) {

                                 /* 
                                Need to find all transactionTarget records that are loaded ready to moved to a downloadable
                                batch (Transaction Status Id = 9)
                                 */
                                List<transactionTarget> loadedTransactions = transactionOutDAO.getLoadedOutBoundTransactions(schedule.getconfigId());

                                if(!loadedTransactions.isEmpty()) {

                                    for(transactionTarget transaction : loadedTransactions) {
                                        batchId = beginOutputProcess(transaction);
                                    }

                                    /* 
                                    Need to check the transport method for the configuration, if set to file download
                                    or set to FTP.
                                    */
                                    configurationTransport transportDetails = configurationTransportManager.getTransportDetails(schedule.getconfigId());

                                    /* if File Download update the status to Submission Delivery Completed ID = 23 status. This will only
                                    apply to scheduled and not continous settings. */
                                    if(transportDetails.gettransportMethodId() == 1) {
                                        transactionOutDAO.updateBatchStatus(batchId, 23);
                                    }
                                    /* If FTP Call the FTP Method */
                                    else if(transportDetails.gettransportMethodId() == 3) {
                                        FTPTargetFile(batchId);
                                    }

                                    /* Log the last run time */
                                    targetOutputRunLogs log = new targetOutputRunLogs();
                                    log.setconfigId(schedule.getconfigId());

                                    transactionOutDAO.saveOutputRunLog(log);
                                }

                            }
                        }
                        else {
                            /* CONTINUOUS */

                            double diffInHours;
                            double diffInMinutes;
                            if(logs.size() > 0) {
                                targetOutputRunLogs log = logs.get(0);
                                long diff = currDate.getTime() - log.getlastRunTime().getTime();
                                diffInHours = diff / ((double) 1000 * 60 * 60);
                                diffInMinutes = (diffInHours - (int)diffInHours)*60;
                            }
                            else {
                                diffInMinutes = 0;
                            }

                            if(diffInMinutes == 0 || diffInMinutes >= schedule.getnewfileCheck()) {

                                 /* 
                                Need to find all transactionTarget records that are loaded ready to moved to a downloadable
                                batch
                                 */
                                List<transactionTarget> loadedTransactions = transactionOutDAO.getLoadedOutBoundTransactions(schedule.getconfigId());

                                if(!loadedTransactions.isEmpty()) {

                                    for(transactionTarget transaction : loadedTransactions) {
                                        batchId = beginOutputProcess(transaction);
                                    }

                                    /* 
                                    Need to check the transport method for the configuration, if set to file download
                                    or set to FTP.
                                    */
                                    configurationTransport transportDetails = configurationTransportManager.getTransportDetails(schedule.getconfigId());

                                    /* if File Download update the status to Submission Delivery Completed ID = 23 status. This will only
                                    apply to scheduled and not continous settings. */
                                    if(transportDetails.gettransportMethodId() == 1) {
                                        transactionOutDAO.updateBatchStatus(batchId, 23);
                                    }
                                    /* If FTP Call the FTP Method */
                                    else if(transportDetails.gettransportMethodId() == 3) {
                                        FTPTargetFile(batchId);
                                    }

                                    /* Log the last run time */
                                    targetOutputRunLogs log = new targetOutputRunLogs();
                                    log.setconfigId(schedule.getconfigId());

                                    transactionOutDAO.saveOutputRunLog(log);
                                }
                            }

                        }
                    }
                    /* WEEKLY SCHEDULE */
                    else if(schedule.gettype() == 3) {

                    }

                    /* MONTHLY SCHEDULE */
                    else if(schedule.gettype() == 4) {

                    }

                    /* If batchId > 0 then send the email out */
                    if(batchId > 0) {

                        /* Get the batch Details */
                        batchDownloads batchDLInfo = transactionOutDAO.getBatchDetails(batchId);

                        /* Get the list of primary and secondary contacts */
                        List<User> toPrimaryContact = userManager.getOrganizationContact(batchDLInfo.getOrgId(),1);
                        List<User> toSecondaryContact = userManager.getOrganizationContact(batchDLInfo.getOrgId(),2);
                        
                        if(toPrimaryContact.size() > 0 || toSecondaryContact.size() > 0) {
                            String toName = null;
                            mailMessage msg = new mailMessage();
                            ArrayList<String> ccAddressArray = new ArrayList<String>();
                            
                            msg.setfromEmailAddress("dphuniversaltranslator@gmail.com");
                            
                            if(toPrimaryContact.size() > 0) {
                                toName = toPrimaryContact.get(0).getFirstName() + " " + toPrimaryContact.get(0).getLastName();
                                msg.settoEmailAddress(toPrimaryContact.get(0).getEmail());
                                
                                if(toPrimaryContact.size() > 1) {
                                    for(int i = 1; i < toPrimaryContact.size(); i++) {
                                        ccAddressArray.add(toPrimaryContact.get(i).getEmail());
                                    }
                                    
                                }

                                if(toSecondaryContact.size() > 0) {
                                    for(int i = 0; i < toSecondaryContact.size(); i++) {
                                        ccAddressArray.add(toSecondaryContact.get(i).getEmail());
                                    }
                                }
                            }
                            else {
                                toName = toSecondaryContact.get(0).getFirstName() + " " + toSecondaryContact.get(0).getLastName();
                                msg.settoEmailAddress(toSecondaryContact.get(0).getEmail());

                                if(toSecondaryContact.size() > 1) {
                                    for(int i = 1; i < toSecondaryContact.size(); i++) {
                                        ccAddressArray.add(toSecondaryContact.get(i).getEmail());
                                    }
                                }
                            }

                            if(ccAddressArray.size() > 0) {
                                String[] ccAddressList = new String[ccAddressArray.size()];
                                ccAddressList = ccAddressArray.toArray(ccAddressList);
                                msg.setccEmailAddress(ccAddressList);
                            }

                            msg.setmessageSubject("You have received a new message from the Universal Translator");

                            /* Build the body of the email */
                            StringBuilder sb = new StringBuilder();
                            sb.append("Dear " + toName + ", You have recieved a new message from the Universal Translator. ");
                            sb.append(System.getProperty("line.separator"));
                            sb.append(System.getProperty("line.separator"));
                            sb.append("BatchId: "+batchDLInfo.getutBatchName());
                            if(batchDLInfo.getoutputFIleName() != null && !"".equals(batchDLInfo.getoutputFIleName())) {
                                sb.append(System.getProperty("line.separator"));
                                sb.append("File Name: "+batchDLInfo.getoutputFIleName());
                            }
                            sb.append(System.getProperty("line.separator"));

                            msg.setmessageBody(sb.toString());

                            /* Send the email */
                            emailMessageManager.sendEmail(msg);

                        }

                    }

                }

            }
        }
        catch(Exception ex) {
            try {
                throw new IOException(ex);
            } catch (IOException ex1) {
                Logger.getLogger(transactionOutManagerImpl.class.getName()).log(Level.SEVERE, null, ex1);
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
    public int beginOutputProcess(transactionTarget transaction) {
        
        try {
            int batchId = 0;
         
            batchUploads uploadedBatchDetails = transactionInManager.getBatchDetails(transaction.getbatchUploadId());

            configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());

            configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transaction.getconfigId());

            /* Check to see what outut transport method was set up */

            /* ERG */
            if(transportDetails.gettransportMethodId() == 2) {

                /* Generate the batch */
                /* (target configuration Details, transaction details, transport Details for target config, Source OrgId, Source Original Filename, mergeable) */
                batchId = generateBatch(configDetails, transaction, transportDetails, uploadedBatchDetails.getOrgId(), uploadedBatchDetails.getoriginalFileName(), false);

                /* Update the status of the uploaded batch to  TBP (Target Batch Creating in process) (ID = 25) */
                transactionInManager.updateBatchStatus(batchId,25,"");

            }
            /* File Download */
            else if(transportDetails.gettransportMethodId() == 1) {

                boolean createNewFile = true;

                /* 
                    If the merge batches option is not checked create the batch right away
                */

                if(transportDetails.getmergeBatches() == false) {

                    /* Generate the batch */
                    /* (target configuration Details, transaction details, transport Details for target config, Source OrgId, Source Original Filename, mergeable) */
                    batchId = generateBatch(configDetails, transaction, transportDetails, uploadedBatchDetails.getOrgId(), uploadedBatchDetails.getoriginalFileName(), false);

                    /* Update the status of the uploaded batch to  TBP (Target Batch Creating in process) (ID = 25) */
                    transactionInManager.updateBatchStatus(batchId,25,"");

                }
                else {

                    /* We want to merge this transaction with the existing created batch if not yet opened (ID = 28) */
                    /* 1. Need to see if a mergable batch exists for the org that hasn't been picked up yet */
                    int mergeablebatchId = transactionOutDAO.findMergeableBatch(configDetails.getorgId());

                    /* If no mergable batch is found create a new batch */
                    if(mergeablebatchId == 0) {

                        /* Generate the batch */
                        /* (target configuration Details, transaction details, transport Details for target config, Source OrgId, Source Original Filename, mergeable) */
                        batchId = generateBatch(configDetails, transaction, transportDetails, uploadedBatchDetails.getOrgId(), uploadedBatchDetails.getoriginalFileName(), true);

                        /* Update the status of the uploaded batch to  TBP (Target Batch Creating in process) (ID = 25) */
                        transactionInManager.updateBatchStatus(batchId,25,"");

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

            return batchId; 
        }
        catch(Exception ex) {
            try {
                throw new IOException(ex);
            } catch (IOException ex1) {
                Logger.getLogger(transactionOutManagerImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
            return 0;
        }
        
    }
    
    
    /**
     * The 'generateBatch' function will create the new download batch for the target
     */
    public int generateBatch(configuration configDetails, transactionTarget transaction, configurationTransport transportDetails, int sourceOrgId, String sourceFileName, boolean mergeable) {
        
        try {
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
        catch(Exception ex) {
            try {
                throw new IOException(ex);
            } catch (IOException ex1) {
                Logger.getLogger(transactionOutManagerImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
            return 0;
        }
        
    }
    
    /**
     * The 'generateTargetFile' function will generate the actual file in the correct organizations
     * outpufiles folder.
     */
    public void generateTargetFile(boolean createNewFile, int transactionTargetId, int batchId, configurationTransport transportDetails) throws Exception {
        
         try {
            String fileName = null; 
         
            batchDownloads batchDetails = transactionOutDAO.getBatchDetails(batchId);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            fileSystem dir = new fileSystem();

            String filelocation = transportDetails.getfileLocation();
            filelocation = filelocation.replace("/bowlink/", "");

            dir.setDirByName(filelocation);

            boolean hl7 = false;
            String fileType = (String) configurationManager.getFileTypesById(transportDetails.getfileType());

            if(fileType == "hl7") {
                fileType = "hr";
                hl7 = true;
            }

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
                               recordRow = new StringBuilder().append(recordRow).append(fieldValue).append(System.getProperty( "line.separator" )).toString();
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
                           throw new IOException(ex);
                       }
                   }

               }

            } catch (FileNotFoundException e) {
               e.printStackTrace();
            } 
         }
         catch(IOException ex) {
            try {
                throw new IOException(ex);
            } catch (IOException ex1) {
                Logger.getLogger(transactionOutManagerImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        }
         
    }
    
    @Override
    @Transactional
    public List<batchDownloads> getdownloadableBatches(int userId, int orgId, Date fromDate, Date toDate, String searchTerm, int page, int maxResults) {
        return transactionOutDAO.getdownloadableBatches(userId, orgId, fromDate, toDate, searchTerm, page, maxResults);
    }
    
    @Override
    @Transactional
    public void updateLastDownloaded(int batchId) {
        transactionOutDAO.updateLastDownloaded(batchId);
    }
    
    /**
     * The 'FTPTargetFile' function will get the FTP details and send off the generated file
     * 
     * @param batchId   The id of the batch to FTP the file for
     */
    private void FTPTargetFile(int batchId) {
        
        /* Update the status of the batch to locked */
        transactionOutDAO.updateBatchStatus(batchId, 22);
    }
    
    
}
