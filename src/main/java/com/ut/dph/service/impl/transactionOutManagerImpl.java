/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.dph.service.impl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.ut.dph.dao.messageTypeDAO;
import com.ut.dph.dao.transactionOutDAO;
import com.ut.dph.model.HL7Details;
import com.ut.dph.model.HL7ElementComponents;
import com.ut.dph.model.HL7Elements;
import com.ut.dph.model.HL7Segments;
import com.ut.dph.model.Organization;
import com.ut.dph.model.Transaction;
import com.ut.dph.model.User;
import com.ut.dph.model.batchDownloadSummary;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationConnectionReceivers;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.configurationFTPFields;
import com.ut.dph.model.configurationSchedules;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.service.emailMessageManager;
import com.ut.dph.model.mailMessage;
import com.ut.dph.model.pendingDeliveryTargets;
import com.ut.dph.model.systemSummary;
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
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

    private int processingSysErrorId = 5;

    @Override
    @Transactional
    public List<batchDownloads> getInboxBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception {
        return transactionOutDAO.getInboxBatches(userId, orgId, fromDate, toDate);
    }

    @Override
    @Transactional
    public batchDownloads getBatchDetails(int batchId) throws Exception {
        return transactionOutDAO.getBatchDetails(batchId);
    }
    
    @Override
    @Transactional
    public batchDownloads getBatchDetailsByBatchName(String batchName) throws Exception {
        return transactionOutDAO.getBatchDetailsByBatchName(batchName);
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
    public boolean processOutPutTransactions(int transactionTargetId, int configId, int transactionInId) throws Exception {
        return transactionOutDAO.processOutPutTransactions(transactionTargetId, configId, transactionInId);
    }

    @Override
    @Transactional
    public void updateTargetBatchStatus(Integer batchDLId, Integer statusId, String timeField) throws Exception {
        transactionOutDAO.updateTargetBatchStatus(batchDLId, statusId, timeField);
    }

    @Override
    @Transactional
    public void updateTargetTransasctionStatus(int batchDLId, int statusId) {
        transactionOutDAO.updateTargetTransasctionStatus(batchDLId, statusId);
    }

    /**
     * The 'translateTargetRecords' function will attempt to translate the target records based on the translation details set up in the target configuration.
     *
     * @param transactionTargetId The id of the target transaction to be translated
     * @param batchId The id of the batch the target transaction belongs to
     * @param configId The id of the target configuration.
     *
     * @return This function will return either TRUE (If translation completed with no errors) OR FALSE (If translation failed for any reason)
     */
    @Override
    public Integer translateTargetRecords(int transactionTargetId, int configId, int batchId) {

        Integer errorCount = 0;

        /* Need to get the configured data translations */
        List<configurationDataTranslations> dataTranslations = configurationManager.getDataTranslationsWithFieldNo(configId);

        for (configurationDataTranslations cdt : dataTranslations) {
            if (cdt.getCrosswalkId() != 0) {
                try {
                    errorCount = errorCount + transactionInManager.processCrosswalk(configId, batchId, cdt, true, 0);
                } catch (Exception e) {
                    //throw new Exception("Error occurred processing crosswalks. crosswalkId: "+cdt.getCrosswalkId()+" configId: "+configId,e);
                    e.printStackTrace();
                    return 1;
                }
            } else if (cdt.getMacroId() != 0) {
                try {
                    errorCount = errorCount + transactionInManager.processMacro(configId, batchId, cdt, true, 0);
                } catch (Exception e) {
                    //throw new Exception("Error occurred processing macro. macroId: "+ cdt.getMacroId() + " configId: "+configId,e);
                    e.printStackTrace();
                    return 1;
                }
            }
        }
        return errorCount;
    }

    @Override
    @Transactional
    public void moveTranslatedRecords(int transactionTargetId) throws Exception {
        transactionOutDAO.moveTranslatedRecords(transactionTargetId);
    }

    /**
     * The 'processOutputRecords' function will look for pending output records and start the translation process on the records to generate for the target. This function is called from the processOutputRecords scheduled job but can also be called via a web call to initiate the process manually. The scheduled job runs every 1 minute.
     *
     * @param transactionTargetId The id of a specific transaction to process (defaults to 0)
     */
    @Override
    @Transactional
    public void processOutputRecords(int transactionTargetId) throws Exception {

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
            if (!pendingTransactions.isEmpty()) {

                for (transactionTarget transaction : pendingTransactions) {

                    boolean processed = false;
                    String errorMessage = "Error occurred trying to process output transaction. transactionId: " + transaction.getId();

                    try {
                        if (clearOutTables(transaction.getId()) > 0) {
                            processed = false;
                        }
                    } catch (Exception ex) {
                        processed = false;
                        ex.printStackTrace();
                    }

                    /* Process the output (transactionTargetId, targetConfigId, transactionInId) */
                    try {
                        processed = transactionOutDAO.processOutPutTransactions(transaction.getId(), transaction.getconfigId(), transaction.gettransactionInId());
                    } catch (Exception e) {
                        //throw new Exception("Error occurred trying to process output transaction. transactionId: "+transaction.getId(),e);
                        processed = false;
                    }

                    if (!processed) {
                        //we update and log
                        transactionInManager.updateTransactionTargetStatus(0, transaction.getId(), 0, 33);
                        transactionInManager.insertProcessingError(processingSysErrorId, null, 0, null, null, null, null, false, true, errorMessage, transaction.getId());
                    }

                    /* If processed == true update the status of the batch and transaction */
                    if (processed == true) {
                        Integer processingErrors;

                        /* Need to start the transaction translations */
                        try {
                            processingErrors = translateTargetRecords(transaction.getId(), transaction.getconfigId(), transaction.getbatchDLId());
                        } catch (Exception e) {
                            // throw new Exception("Error occurred trying to translate target records. transactionId: "+ transaction.getId(),e);
                            //we log
                            e.printStackTrace();
                            processingErrors = 1;
                        }

                        if (processingErrors != 0) {
                            transactionInManager.updateTransactionTargetStatus(0, transaction.getId(), 0, 33);
                            transactionInManager.insertProcessingError(processingSysErrorId, null, 0, null, null, null, null, false, true, "error applying macros and crosswalks", transaction.getId());
                        }

                        /* Once all the processing has completed with no errors need to copy records to the transactionOutRecords to make available to view */
                        if (processingErrors == 0) { // no errors

                            try {
                                transactionOutDAO.moveTranslatedRecords(transaction.getId());
                            } catch (Exception e) {
                                throw new Exception("Error occurred moving translated records. transactionId: " + transaction.getId(), e);
                            }

                            try {
                                /* Update the status of the transaction to L (Loaded) (ID = 9) */
                                transactionInManager.updateTransactionStatus(transaction.getbatchUploadId(), transaction.gettransactionInId(), 0, 9);
                            } catch (Exception e) {
                                throw new Exception("Error updating the transactionIn status. transactionId: " + transaction.getbatchUploadId(), e);
                            }

                            try {
                                /* Update the status of the transaction target to L (Loaded) (ID = 9) */
                                transactionInManager.updateTransactionTargetStatus(0, transaction.getId(), 0, 9);
                            } catch (Exception e) {
                                throw new Exception("Error updating the transactionTarget status. transactionId: " + transaction.getId(), e);
                            }


                            /* If configuration is set to auto process the process right away */
                            configurationSchedules scheduleDetails = configurationManager.getScheduleDetails(transaction.getconfigId());

                            /* If no schedule is found or automatic */
                            if (scheduleDetails == null || scheduleDetails.gettype() == 5) {

                                try {
                                    int batchId = beginOutputProcess(transaction);

                                    /* Log the last run time */
                                    try {
                                        targetOutputRunLogs log = new targetOutputRunLogs();
                                        log.setconfigId(transaction.getconfigId());

                                        transactionOutDAO.saveOutputRunLog(log);
                                    } catch (Exception e) {
                                        throw new Exception("Error occurred trying to save the run log. configId: " + transaction.getconfigId(), e);
                                    }

                                    /* 
                                     Need to check the transport method for the configuration, if set to file download
                                     or set to FTP.
                                     */
                                    configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transaction.getconfigId());

                                    /* if File Download update the status to Submission Delivery Completed ID = 23 status. This will only
                                     apply to scheduled and not continous settings. */
                                    if (transportDetails.gettransportMethodId() == 1) {
                                        transactionOutDAO.updateBatchStatus(batchId, 23);
                                        transactionInManager.updateBatchStatus(transaction.getbatchUploadId(), 23, "");
                                    } /* If FTP Call the FTP Method */ else if (transportDetails.gettransportMethodId() == 3) {
                                        FTPTargetFile(batchId, transportDetails);
                                    }

                                    if (batchId > 0) {
                                        /* Send the email to primary contact */
                                    	try {
                                            /* Get the batch Details */
                                            batchDownloads batchDLInfo = transactionOutDAO.getBatchDetails(batchId);

                                            /* Get the from user */
                                            Organization fromOrg = organizationManager.getOrganizationById(configurationManager.getConfigurationById(transactionInManager.getTransactionDetails(transaction.gettransactionInId()).getconfigId()).getorgId());
                                            List<User> fromPrimaryContact = userManager.getOrganizationContact(fromOrg.getId(), 1);

                                            /* get the to user details */
                                            List<User> toPrimaryContact = userManager.getOrganizationContact(configurationManager.getConfigurationById(transaction.getconfigId()).getorgId(), 1);
                                            List<User> toSecondaryContact = userManager.getOrganizationContact(configurationManager.getConfigurationById(transaction.getconfigId()).getorgId(), 2);

                                            if (fromPrimaryContact.size() > 0 && (toPrimaryContact.size() > 0 || toSecondaryContact.size() > 0)) {
                                                String toName = null;
                                                mailMessage msg = new mailMessage();
                                                ArrayList<String> ccAddressArray = new ArrayList<String>();
                                                msg.setfromEmailAddress(fromPrimaryContact.get(0).getEmail());

                                                if (toPrimaryContact.size() > 0) {
                                                    toName = toPrimaryContact.get(0).getFirstName() + " " + toPrimaryContact.get(0).getLastName();
                                                    msg.settoEmailAddress(toPrimaryContact.get(0).getEmail());

                                                    if (toPrimaryContact.size() > 1) {
                                                        for (int i = 1; i < toPrimaryContact.size(); i++) {
                                                            ccAddressArray.add(toPrimaryContact.get(i).getEmail());
                                                        }
                                                    }

                                                    if (toSecondaryContact.size() > 0) {
                                                        for (int i = 0; i < toSecondaryContact.size(); i++) {
                                                            ccAddressArray.add(toSecondaryContact.get(i).getEmail());
                                                        }
                                                    }
                                                } else {
                                                    toName = toSecondaryContact.get(0).getFirstName() + " " + toSecondaryContact.get(0).getLastName();
                                                    msg.settoEmailAddress(toSecondaryContact.get(0).getEmail());

                                                    if (toSecondaryContact.size() > 1) {
                                                        for (int i = 1; i < toSecondaryContact.size(); i++) {
                                                            ccAddressArray.add(toSecondaryContact.get(i).getEmail());
                                                        }
                                                    }
                                                }

                                                if (ccAddressArray.size() > 0) {
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
                                                sb.append("BatchId: " + batchDLInfo.getutBatchName());
                                                if (batchDLInfo.getoutputFIleName() != null && !"".equals(batchDLInfo.getoutputFIleName())) {
                                                    sb.append(System.getProperty("line.separator"));
                                                    sb.append("File Name: " + batchDLInfo.getoutputFIleName());
                                                }
                                                sb.append(System.getProperty("line.separator"));
                                                sb.append("From Organization: " + fromOrg.getOrgName());

                                                msg.setmessageBody(sb.toString());

                                                /* Send the email */
                                                try {
                                                	emailMessageManager.sendEmail(msg);
                                                } catch(Exception ex) {
                                                	ex.printStackTrace();
                                                }
                                            }
                                        } catch (Exception e) {
                                            throw new Exception("Error occurred trying to send the alert email for batchId: " + batchId, e);
                                        }

                                    }
                                } catch (Exception e) {
                                    throw new Exception("Error occurred trying to auto process the output file.", e);
                                }

                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Error trying to process output records", e);
        }

    }

    /**
     * The 'generateOutputFiles' function will look to see if any output files need to be generated. This function is called from the generateOutputFiles scheduled job. Running every 10 minutes.
     *
     */
    @Override
    @Transactional
    public void generateOutputFiles() throws Exception {

        /*
        
         1. When the beginOutput Process function returns (RETURN BATCH ID???) check to see if the target transport method 
         for the config is set to FTP, if so then call the FTP method to send off the file. Batch would then get a 
         Submission Delivery Locked ID = 22 status.
         2. For file download transport methods, after the beginOutput Process function returns with the batch Id, the batch
         would then get a Submission Delivery Completed ID = 23 status so we don't keep adding new transactions to already
         created batch files. FOR SCHEDULED PROCESSING ONLY, CONTINUOUS processing will keep adding to the same file until
         the file is downloaded.
         */
        try {
            /* Get a list of scheduled configurations (Daily, Weekly or Monthly) */
            List<configurationSchedules> scheduledConfigs = transactionOutDAO.getScheduledConfigurations();

            if (!scheduledConfigs.isEmpty()) {

                int batchId = 0;
                boolean runProcess;

                Date currDate = new Date();
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(currDate);

                for (configurationSchedules schedule : scheduledConfigs) {

                    batchId = 0;
                    runProcess = false;

                    /* Need to get the latest log to make sure we don't run it again in the same day */
                    List<targetOutputRunLogs> logs = transactionOutDAO.getLatestRunLog(schedule.getconfigId());

                    /* DAILY SCHEDULE */
                    if (schedule.gettype() == 2) {

                        /* if Daily check for scheduled or continuous */
                        if (schedule.getprocessingType() == 1) {
                            double diffInHours;
                            int hourOfDay;

                            /* SCHEDULED */
                            try {
                                hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

                                if (logs.size() > 0) {
                                    targetOutputRunLogs log = logs.get(0);
                                    long diff = currDate.getTime() - log.getlastRunTime().getTime();
                                    diffInHours = diff / ((double) 1000 * 60 * 60);
                                } else {
                                    diffInHours = 0;
                                }
                            } catch (Exception e) {
                                throw new Exception("Error trying to calculate the time difference from run logs", e);
                            }

                            if (hourOfDay >= schedule.getprocessingTime() && (diffInHours == 0 || diffInHours >= 24)) {
                                runProcess = true;
                            }
                        } else {
                            /* CONTINUOUS */

                            double diffInHours;
                            double diffInMinutes;

                            try {
                                if (logs.size() > 0) {
                                    targetOutputRunLogs log = logs.get(0);
                                    long diff = currDate.getTime() - log.getlastRunTime().getTime();
                                    diffInHours = diff / ((double) 1000 * 60 * 60);
                                    diffInMinutes = (diffInHours - (int) diffInHours) * 60;
                                } else {
                                    diffInMinutes = 0;
                                }
                            } catch (Exception e) {
                                throw new Exception("Error trying to calculate the time difference from run logs", e);
                            }

                            if (diffInMinutes == 0 || diffInMinutes >= schedule.getnewfileCheck()) {
                                runProcess = true;
                            }

                        }
                    } 
                    /* WEEKLY SCHEDULE */ 
                    else if (schedule.gettype() == 3) {
                        long diffInWeeks = 0;

                        if (logs.size() > 0) {
                            targetOutputRunLogs log = logs.get(0);
                            long diff = currDate.getTime() - log.getlastRunTime().getTime();

                            diffInWeeks = diff / ((long) 7 * 24 * 60 * 60 * 1000);

                            if (diffInWeeks == 0 || diffInWeeks >= 1) {
                                runProcess = true;
                            }

                        }

                    } 
                    /* MONTHLY SCHEDULE */ 
                    else if (schedule.gettype() == 4) {

                        long diffInDays = 0;

                        if (logs.size() > 0) {
                            targetOutputRunLogs log = logs.get(0);
                            long diff = currDate.getTime() - log.getlastRunTime().getTime();

                            diffInDays = diff / ((long) 365.24 * 24 * 60 * 60 * 1000 / 12);

                            if (diffInDays == 0 || diffInDays >= 30) {
                                runProcess = true;
                            }

                        }

                    }

                    if (runProcess == true) {
                        /* 
                         Need to find all transactionTarget records that are loaded ready to moved to a downloadable
                         batch (Transaction Status Id = 9)
                         */
                        List<transactionTarget> loadedTransactions = transactionOutDAO.getLoadedOutBoundTransactions(schedule.getconfigId());

                        if (!loadedTransactions.isEmpty()) {

                            for (transactionTarget transaction : loadedTransactions) {
                                try {
                                    batchId = beginOutputProcess(transaction);
                                } catch (Exception e) {
                                    throw new Exception("Error in the output process. transactionId: " + transaction.getId(), e);
                                }
                            }

                        }
                    }

                    /* If batchId > 0 then send the email out */
                    if (batchId > 0) {

                        /* Get the batch Details */
                        batchDownloads batchDLInfo = transactionOutDAO.getBatchDetails(batchId);
                        /* 
                         Need to check the transport method for the configuration, if set to file download
                         or set to FTP.
                         */
                        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(schedule.getconfigId());

                        /* if File Download update the status to Submission Delivery Completed ID = 23 status. This will only
                         apply to scheduled and not continous settings. */
                        if (transportDetails.gettransportMethodId() == 1) {
                            transactionOutDAO.updateBatchStatus(batchId, 23);

                            /* Need to find the batch Upload Id */
                            List<transactionTarget> targets = transactionOutDAO.getTransactionsByBatchDLId(batchId);

                            if (!targets.isEmpty()) {
                                for (transactionTarget target : targets) {
                                    transactionInManager.updateBatchStatus(target.getbatchUploadId(), 23, "");
                                }
                            }

                        } /* If FTP Call the FTP Method */ else if (transportDetails.gettransportMethodId() == 3) {
                            FTPTargetFile(batchId, transportDetails);
                        }

                        /* Log the last run time */
                        try {
                            targetOutputRunLogs log = new targetOutputRunLogs();
                            log.setconfigId(schedule.getconfigId());

                            transactionOutDAO.saveOutputRunLog(log);
                        } catch (Exception e) {
                            throw new Exception("Error occurred trying to save the run log. configId: " + schedule.getconfigId(), e);
                        }

                        try {
                            /* Get the list of primary and secondary contacts */
                            List<User> toPrimaryContact = userManager.getOrganizationContact(batchDLInfo.getOrgId(), 1);
                            List<User> toSecondaryContact = userManager.getOrganizationContact(batchDLInfo.getOrgId(), 2);

                            if (toPrimaryContact.size() > 0 || toSecondaryContact.size() > 0) {
                                String toName = null;
                                mailMessage msg = new mailMessage();
                                ArrayList<String> ccAddressArray = new ArrayList<String>();

                                msg.setfromEmailAddress("dphuniversaltranslator@gmail.com");

                                if (toPrimaryContact.size() > 0) {
                                    toName = toPrimaryContact.get(0).getFirstName() + " " + toPrimaryContact.get(0).getLastName();
                                    msg.settoEmailAddress(toPrimaryContact.get(0).getEmail());

                                    if (toPrimaryContact.size() > 1) {
                                        for (int i = 1; i < toPrimaryContact.size(); i++) {
                                            ccAddressArray.add(toPrimaryContact.get(i).getEmail());
                                        }

                                    }

                                    if (toSecondaryContact.size() > 0) {
                                        for (int i = 0; i < toSecondaryContact.size(); i++) {
                                            ccAddressArray.add(toSecondaryContact.get(i).getEmail());
                                        }
                                    }
                                } else {
                                    toName = toSecondaryContact.get(0).getFirstName() + " " + toSecondaryContact.get(0).getLastName();
                                    msg.settoEmailAddress(toSecondaryContact.get(0).getEmail());

                                    if (toSecondaryContact.size() > 1) {
                                        for (int i = 1; i < toSecondaryContact.size(); i++) {
                                            ccAddressArray.add(toSecondaryContact.get(i).getEmail());
                                        }
                                    }
                                }

                                if (ccAddressArray.size() > 0) {
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
                                sb.append("BatchId: " + batchDLInfo.getutBatchName());
                                if (batchDLInfo.getoutputFIleName() != null && !"".equals(batchDLInfo.getoutputFIleName())) {
                                    sb.append(System.getProperty("line.separator"));
                                    sb.append("File Name: " + batchDLInfo.getoutputFIleName());
                                }
                                sb.append(System.getProperty("line.separator"));

                                msg.setmessageBody(sb.toString());

                                /* Send the email */
                                try {
                                	emailMessageManager.sendEmail(msg);
                                } catch (Exception ex) {
                                	ex.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            throw new Exception("Error occurred trying to send the alert email for batchId: " + batchId, e);
                        }

                    }

                }

            }
        } catch (Exception e) {
            throw new Exception("Error occurred trying generate target output files ", e);
        }

    }
    
    /**
     * The 'processManualTransaction' function will start the processing of a transaction 
     * that is set to manual.
     * 
     * @param transaction The transaction object that needs to be translated.
     * 
     * @return This function returns the created batchId.
     */
    @Override
    public int processManualTransaction(transactionTarget transaction) throws Exception {
        return beginOutputProcess(transaction);
    }
    
    /**
     * The 'beginOutputProcess' function will start the process to creating the target download transaction
     *
     * @param configDetails
     * @param transaction
     * @param transportDetails
     * @param uploadedBatchDetails
     */
    public int beginOutputProcess(transactionTarget transaction) throws Exception {

        try {
            int batchId = 0;

            batchUploads uploadedBatchDetails = transactionInManager.getBatchDetails(transaction.getbatchUploadId());

            configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());

            configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transaction.getconfigId());

            /* Check to see what outut transport method was set up */

            /* ERG */
            if (transportDetails.gettransportMethodId() == 2) {

                /* Generate the batch */
                /* (target configuration Details, transaction details, transport Details for target config, Source OrgId, Source Original Filename, mergeable) */
                try {
                    batchId = generateBatch(configDetails, transaction, transportDetails, uploadedBatchDetails.getOrgId(), uploadedBatchDetails.getoriginalFileName(), false);

                    /* Update the status of the uploaded batch to  TBP (Target Batch Creating in process) (ID = 25) */
                    transactionInManager.updateBatchStatus(batchId, 25, "");
                } catch (Exception e) {
                    throw new Exception("Error occurred trying to generate a batch. transactionId: " + transaction.getId(), e);
                }

            } 
            /* File Download || FTP || EMed-Apps */ 
            else if (transportDetails.gettransportMethodId() == 1 || transportDetails.gettransportMethodId() == 3  || transportDetails.gettransportMethodId() == 5) {

                boolean createNewFile = true;
                
                /* 
                 If the merge batches option is not checked create the batch right away
                 */
                if (transportDetails.getmergeBatches() == false) {

                    /* Generate the batch */
                    /* (target configuration Details, transaction details, transport Details for target config, Source OrgId, Source Original Filename, mergeable) */
                    try {
                        batchId = generateBatch(configDetails, transaction, transportDetails, uploadedBatchDetails.getOrgId(), uploadedBatchDetails.getoriginalFileName(), false);

                        /* Update the status of the uploaded batch to  TBP (Target Batch Creating in process) (ID = 25) */
                        transactionInManager.updateBatchStatus(batchId, 25, "");
                    } catch (Exception e) {
                        throw new Exception("Error occurred trying to generate a batch. transactionId: " + transaction.getId(), e);
                    }

                } else {

                    /* We want to merge this transaction with the existing created batch if not yet opened (ID = 28) */
                    /* 1. Need to see if a mergable batch exists for the org that hasn't been picked up yet */
                    int mergeablebatchId = transactionOutDAO.findMergeableBatch(configDetails.getorgId());

                    /* If no mergable batch is found create a new batch */
                    if (mergeablebatchId == 0) {

                        /* Generate the batch */
                        /* (target configuration Details, transaction details, transport Details for target config, Source OrgId, Source Original Filename, mergeable) */
                        try {
                            batchId = generateBatch(configDetails, transaction, transportDetails, uploadedBatchDetails.getOrgId(), uploadedBatchDetails.getoriginalFileName(), true);

                            /* Update the status of the uploaded batch to  TBP (Target Batch Creating in process) (ID = 25) */
                            transactionInManager.updateBatchStatus(batchId, 25, "");
                        } catch (Exception e) {
                            throw new Exception("Error occurred trying to generate a batch. transactionId: " + transaction.getId(), e);
                        }

                    } else {

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

                        try {
                            transactionOutDAO.submitSummaryEntry(summary);
                        } catch (Exception e) {
                            throw new Exception("Error occurred submitting the batch summary. batchId: " + batchId, e);
                        }

                        createNewFile = false;

                    }

                }

                /* Generate the file */
                try {
                    boolean encryptMessage = false;
                    
                    if(transportDetails.gettransportMethodId() == 5) {
                        encryptMessage = true;
                    }
                    generateTargetFile(createNewFile, transaction.getId(), batchId, transportDetails, encryptMessage);
                } catch (Exception e) {
                    throw new Exception("Error occurred trying to generate the batch file. batchId: " + batchId, e);
                }

            }
            

            /* Update the status of the transaction to PP (Pending Pickup) (ID = 18) */
            transactionInManager.updateTransactionStatus(transaction.getbatchUploadId(), transaction.gettransactionInId(), 0, 18);

            /* Update the status of the transaction target to PP (Pending Pickup) (ID = 18) */
            transactionInManager.updateTransactionTargetStatus(0, transaction.getId(), 0, 18);

            /* Update the status of the uploaded batch to  TBP (Target Batch Created) (ID = 28) */
            transactionInManager.updateBatchStatus(transaction.getbatchUploadId(), 28, "");

            return batchId;
        } catch (Exception e) {
            throw new Exception("Error occurred during the process of generating output files. transactionId: " + transaction.getId(), e);
        }

    }

    /**
     * The 'generateBatch' function will create the new download batch for the target
     */
    public int generateBatch(configuration configDetails, transactionTarget transaction, configurationTransport transportDetails, int sourceOrgId, String sourceFileName, boolean mergeable) throws Exception {

        /* Create the batch name (OrgId+MessageTypeId+Date/Time) */
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String utbatchName = new StringBuilder().append(configDetails.getorgId()).append(configDetails.getMessageTypeId()).append(dateFormat.format(date)).toString();


        /* Need to create a new batch */
        String batchName = null;

        if (transportDetails.gettargetFileName() == null) {
            /* Create the batch name (OrgId+MessageTypeId) */
            batchName = new StringBuilder().append(configDetails.getorgId()).append(configDetails.getMessageTypeId()).toString();
        } else if ("USE SOURCE FILE".equals(transportDetails.gettargetFileName())) {
            int lastPeriodPos = sourceFileName.lastIndexOf(".");

            if (lastPeriodPos <= 0) {
                batchName = sourceFileName;
            } else {
                batchName = sourceFileName.substring(0, lastPeriodPos);
            }

        } else {
            batchName = transportDetails.gettargetFileName();

        }

        /* Append the date time */
        if (transportDetails.getappendDateTime() == true) {
            batchName = new StringBuilder().append(batchName).append(dateFormat.format(date)).toString();
        }

        /* Get the connection id for the configuration */
        List<configurationConnection> connections = configurationManager.getConnectionsByTargetConfiguration(transaction.getconfigId());

        int userId = 0;
        if (!connections.isEmpty()) {
            for (configurationConnection connection : connections) {
                List<configurationConnectionReceivers> receivers = configurationManager.getConnectionReceivers(connection.getId());

                if (!receivers.isEmpty()) {
                    for (configurationConnectionReceivers receiver : receivers) {
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
     * The 'generateTargetFile' function will generate the actual file in the correct organizations outpufiles folder.
     */
    public void generateTargetFile(boolean createNewFile, int transactionTargetId, int batchId, configurationTransport transportDetails, boolean encrypt) throws Exception {

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

        if ("hl7".equals(fileType)) {
            hl7 = true;
        }

        int findExt = batchDetails.getoutputFIleName().lastIndexOf(".");

        if (findExt >= 0) {
            fileName = batchDetails.getoutputFIleName();
        } else {
            fileName = new StringBuilder().append(batchDetails.getoutputFIleName()).append(".").append(transportDetails.getfileExt()).toString();
        }

        File newFile = new File(dir.getDir() + fileName);

        /* Create the empty file in the correct location */
        if (createNewFile == true || !newFile.exists()) {
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
            transactionOutDAO.updateBatchOutputFileName(batchDetails.getId(), fileName);

        }
        
        /* Read in the file */
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
         
        if (records != null) {
            FileWriter fw = null;

            try {
                fw = new FileWriter(file, true);
            } catch (IOException ex) {
                Logger.getLogger(transactionOutManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

            /* If an hl7 file is to be generated */
            if (hl7 == true) {

                /* Get the hl7 details */
                HL7Details hl7Details = configurationManager.getHL7Details(transportDetails.getconfigId());

                if (hl7Details != null) {

                    /* Get the hl7 Segments */
                    List<HL7Segments> hl7Segments = configurationManager.getHL7Segments(hl7Details.getId());

                    if (!hl7Segments.isEmpty()) {
                        
                        StringBuilder hl7recordRow = new StringBuilder();

                        for (HL7Segments segment : hl7Segments) {
                            
                            hl7recordRow.append(segment.getsegmentName()).append(hl7Details.getfieldSeparator());

                            /* Get the segment elements */
                            List<HL7Elements> hl7Elements = configurationManager.getHL7Elements(hl7Details.getId(), segment.getId());

                            if (!hl7Elements.isEmpty()) {
                                int elementCounter = 1;
                                for (HL7Elements element : hl7Elements) {

                                    if (!"".equals(element.getdefaultValue())) {
                                        if("~currDate~".equals(element.getdefaultValue())) {
                                            hl7recordRow.append(batchDetails.getdateCreated());
                                        }
                                        else {
                                            hl7recordRow.append(element.getdefaultValue());
                                        }
                                        
                                    } else {

                                        /* Get the element components */
                                        List<HL7ElementComponents> hl7Components = configurationManager.getHL7ElementComponents(element.getId());

                                        if (!hl7Components.isEmpty()) {
                                            int counter = 1;
                                            for (HL7ElementComponents component : hl7Components) {

                                                String colName = new StringBuilder().append("f").append(component.getfieldValue()).toString();

                                                String fieldValue = BeanUtils.getProperty(records, colName);

                                                if (!"".equals(component.getfieldDescriptor()) && component.getfieldDescriptor() != null) {
                                                    hl7recordRow.append(component.getfieldDescriptor()).append(fieldValue);
                                                } else {
                                                    hl7recordRow.append(fieldValue);
                                                }

                                                if (counter < hl7Components.size()) {
                                                    hl7recordRow.append(hl7Details.getcomponentSeparator());
                                                    counter += 1;
                                                }

                                            }
                                            

                                        } else {
                                            hl7recordRow.append("");
                                        }

                                    }

                                    if (elementCounter <= hl7Elements.size()) {
                                        hl7recordRow.append(hl7Details.getfieldSeparator());
                                        elementCounter += 1;
                                    }

                                }
                            }

                            hl7recordRow.append(System.getProperty("line.separator"));

                        }
                        
                        if (!"".equals(hl7recordRow.toString())) {
                            try {
                                if(encrypt == true) {
                                    byte[] encoded = Base64.encode(hl7recordRow.toString().getBytes());
                                    fw.write(new String(encoded));
                                }
                                else {
                                    fw.write(hl7recordRow.toString());
                                }

                            } catch (IOException ex) {
                                throw new IOException(ex);
                            }
                        }

                        fw.close();

                    }

                }

            } else {
               
                for (int i = 1; i <= maxFieldNo; i++) {

                    String colName = new StringBuilder().append("f").append(i).toString();

                    try {
                        String fieldValue = BeanUtils.getProperty(records, colName);

                        if ("null".equals(fieldValue)) {
                            fieldValue = "";
                        }

                        if (i == maxFieldNo) {
                            recordRow = new StringBuilder().append(recordRow).append(fieldValue).append(System.getProperty("line.separator")).toString();
                        } else {
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

                if (recordRow != null) {
                    try {
                         if(encrypt == true) {
                            byte[] encoded = Base64.encode(recordRow.getBytes());
                            fw.write(new String(encoded));
                        }
                        else {
                           fw.write(recordRow);  
                        }
                        
                        fw.close();
                    } catch (IOException ex) {
                        throw new IOException(ex);
                    }
                }
            }

        }

    }

    @Override
    @Transactional
    public List<batchDownloads> getdownloadableBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception {
        return transactionOutDAO.getdownloadableBatches(userId, orgId, fromDate, toDate);
    }

    @Override
    @Transactional
    public void updateLastDownloaded(int batchId) throws Exception {
        transactionOutDAO.updateLastDownloaded(batchId);
    }

    @Override
    @Transactional
    public List<transactionTarget> getTransactionsByBatchDLId(int batchDLId) {
        return transactionOutDAO.getTransactionsByBatchDLId(batchDLId);
    }

    /**
     * The 'FTPTargetFile' function will get the FTP details and send off the generated file
     *
     * @param batchId The id of the batch to FTP the file for
     */
    private void FTPTargetFile(int batchId, configurationTransport transportDetails) throws Exception {

        try {

            /* Update the status of the batch to locked */
            transactionOutDAO.updateBatchStatus(batchId, 22);

            List<transactionTarget> targets = transactionOutDAO.getTransactionsByBatchDLId(batchId);

            if (!targets.isEmpty()) {

                for (transactionTarget target : targets) {

                    /* Need to update the uploaded batch status */
                    transactionInManager.updateBatchStatus(target.getbatchUploadId(), 22, "");

                    /* Need to update the uploaded batch transaction status */
                    transactionInManager.updateTransactionStatus(target.getbatchUploadId(), target.gettransactionInId(), 0, 20);

                    /* Update the downloaded batch transaction status */
                    transactionOutDAO.updateTargetTransasctionStatus(target.getbatchDLId(), 2);

                }

            }

            /* get the batch details */
            batchDownloads batchFTPFileInfo = transactionOutDAO.getBatchDetails(batchId);

            /* Get the FTP Details */
            configurationFTPFields ftpDetails = configurationTransportManager.getTransportFTPDetailsPush(transportDetails.getId());
            
            if("SFTP".equals(ftpDetails.getprotocol())) {
                
                JSch jsch = new JSch();
                Session session = null;
                ChannelSftp channel = null;
                FileInputStream localFileStream = null;

                String user = ftpDetails.getusername();
                int port = ftpDetails.getport();
                String host = ftpDetails.getip();
                
                Organization orgDetails = organizationManager.getOrganizationById(configurationManager.getConfigurationById(transportDetails.getconfigId()).getorgId());
                
                if(ftpDetails.getcertification() != null && !"".equals(ftpDetails.getcertification())) {
                    
                    File newFile = null;
                    
                    fileSystem dir = new fileSystem();
                    dir.setDir(orgDetails.getcleanURL(), "certificates");

                    jsch.addIdentity(new File(dir.getDir() + ftpDetails.getcertification()).getAbsolutePath());
                    session = jsch.getSession(user, host , port);
                }
                else if(ftpDetails.getpassword() != null && !"".equals(ftpDetails.getpassword())) {
                    session = jsch.getSession(user, host , port);
                    session.setPassword(ftpDetails.getpassword());
                }
                
                session.setConfig("StrictHostKeyChecking", "no");
                session.setTimeout(2000);
                
                session.connect();
 
                channel = (ChannelSftp)session.openChannel("sftp");
                
                channel.connect();
                    
                if(ftpDetails.getdirectory() != null && !"".equals(ftpDetails.getdirectory())) {
                   channel.cd(ftpDetails.getdirectory());
                   
                   String fileName = null;

                   int findExt = batchFTPFileInfo.getoutputFIleName().lastIndexOf(".");

                   if (findExt >= 0) {
                       fileName = batchFTPFileInfo.getoutputFIleName();
                   } else {
                       fileName = new StringBuilder().append(batchFTPFileInfo.getoutputFIleName()).append(".").append(transportDetails.getfileExt()).toString();
                   }
                   
                   //Set the directory to save the brochures to
                   fileSystem dir = new fileSystem();

                   String filelocation = transportDetails.getfileLocation();
                   filelocation = filelocation.replace("/bowlink/", "");
                   dir.setDirByName(filelocation);

                   File file = new File(dir.getDir() + fileName);
                   
                   if(file.exists()) {
                       FileInputStream fileInput = new FileInputStream(file);
                   
                       channel.put(fileInput, fileName);
                   }
                   
                }
                
                channel.disconnect();
                session.disconnect();
                
            }
            else {
               FTPClient ftp;

                if ("FTP".equals(ftpDetails.getprotocol())) {
                    ftp = new FTPClient();
                } else {
                    FTPSClient ftps;
                    ftps = new FTPSClient(true);

                    ftp = ftps;
                    ftps.setTrustManager(null);
                }

                ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
                ftp.setDefaultTimeout(3000);
                ftp.setConnectTimeout(3000);

                if (ftpDetails.getport() > 0) {
                    ftp.connect(ftpDetails.getip(), ftpDetails.getport());
                } else {
                    ftp.connect(ftpDetails.getip());
                }

                int reply = ftp.getReplyCode();

                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftp.disconnect();
                } else {
                    ftp.login(ftpDetails.getusername(), ftpDetails.getpassword());

                    ftp.enterLocalPassiveMode();

                    String fileName = null;

                    int findExt = batchFTPFileInfo.getoutputFIleName().lastIndexOf(".");

                    if (findExt >= 0) {
                        fileName = batchFTPFileInfo.getoutputFIleName();
                    } else {
                        fileName = new StringBuilder().append(batchFTPFileInfo.getoutputFIleName()).append(".").append(transportDetails.getfileExt()).toString();
                    }

                    //Set the directory to save the brochures to
                    fileSystem dir = new fileSystem();

                    String filelocation = transportDetails.getfileLocation();
                    filelocation = filelocation.replace("/bowlink/", "");
                    dir.setDirByName(filelocation);

                    File file = new File(dir.getDir() + fileName);

                    FileInputStream fileInput = new FileInputStream(file);

                    ftp.changeWorkingDirectory(ftpDetails.getdirectory());
                    ftp.storeFile(fileName, fileInput);
                    ftp.logout();
                    ftp.disconnect();

                } 
            }

            

        } catch (Exception e) {
            throw new Exception("Error occurred trying to FTP a batch target. batchId: " + batchId, e);
        }

    }

    @Override
    @Transactional
    public void cancelMessageTransaction(int transactionId, int transactionInId) {
        transactionOutDAO.cancelMessageTransaction(transactionId, transactionInId);
    }

    @Override
    public void clearTransactionTranslatedOut(Integer transactionTargetId) {
        transactionOutDAO.clearTransactionTranslatedOut(transactionTargetId);

    }

    @Override
    public void clearTransactionOutRecords(Integer transactionTargetId) {
        transactionOutDAO.clearTransactionOutRecords(transactionTargetId);

    }

    @Override
    public void clearTransactionOutErrors(Integer transactionTargetId) {
        transactionOutDAO.clearTransactionOutErrors(transactionTargetId);
    }

    @Override
    public Integer clearOutTables(Integer transactionTargetId) {
        try {
            clearTransactionOutErrors(transactionTargetId);
            clearTransactionOutRecords(transactionTargetId);
            clearTransactionTranslatedOut(transactionTargetId);
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 1;
        }
    }
    
    @Override
    @Transactional
    public batchDownloadSummary getDownloadSummaryDetails(int transactionTargetId) {
        return transactionOutDAO.getDownloadSummaryDetails(transactionTargetId);
    }
    
    /**
     * The 'generateSystemOutboundSummary' function will return the summary object for
     * outbound system batches
     * 
     * @return This function will return a systemSummary object 
     */
    @Override
    public systemSummary generateSystemOutboundSummary() {

        systemSummary systemSummary = new systemSummary();

        try {

            /* Get batches submitted this hour */
            Calendar thishour = new GregorianCalendar();
            thishour.set(Calendar.MINUTE, 0);
            thishour.set(Calendar.SECOND, 0);
            thishour.set(Calendar.MILLISECOND, 0);

            Calendar nexthour = new GregorianCalendar();
            nexthour.set(Calendar.MINUTE, 0);
            nexthour.set(Calendar.SECOND, 0);
            nexthour.set(Calendar.MILLISECOND, 0);
            nexthour.add(Calendar.HOUR_OF_DAY, 1);

            Integer batchesThisHour = transactionOutDAO.getAllBatches(thishour.getTime(), nexthour.getTime()).size();

            /* Get batches submitted today */
            Calendar starttoday = new GregorianCalendar();
            starttoday.set(Calendar.HOUR_OF_DAY, 0);
            starttoday.set(Calendar.MINUTE, 0);
            starttoday.set(Calendar.SECOND, 0);
            starttoday.set(Calendar.MILLISECOND, 0);

            Calendar starttomorrow = new GregorianCalendar();
            starttomorrow.set(Calendar.HOUR_OF_DAY, 0);
            starttomorrow.set(Calendar.MINUTE, 0);
            starttomorrow.set(Calendar.SECOND, 0);
            starttomorrow.set(Calendar.MILLISECOND, 0);
            starttomorrow.add(Calendar.DAY_OF_MONTH, 1);

            Integer batchesToday = transactionOutDAO.getAllBatches(starttoday.getTime(), starttomorrow.getTime()).size();

            /* Get batches submitted this week */
            Calendar thisweek = new GregorianCalendar();
            thisweek.set(Calendar.HOUR_OF_DAY, 0);
            thisweek.set(Calendar.MINUTE, 0);
            thisweek.set(Calendar.SECOND, 0);
            thisweek.set(Calendar.MILLISECOND, 0);
            thisweek.set(Calendar.DAY_OF_WEEK, thisweek.getFirstDayOfWeek());

            Calendar nextweek = new GregorianCalendar();
            nextweek.set(Calendar.HOUR_OF_DAY, 0);
            nextweek.set(Calendar.MINUTE, 0);
            nextweek.set(Calendar.SECOND, 0);
            nextweek.set(Calendar.MILLISECOND, 0);
            nextweek.set(Calendar.DAY_OF_WEEK, thisweek.getFirstDayOfWeek());
            nextweek.add(Calendar.WEEK_OF_YEAR, 1);

            Integer batchesThisWeek = transactionOutDAO.getAllBatches(thisweek.getTime(), nextweek.getTime()).size();

            systemSummary.setBatchesPastHour(batchesThisHour);
            systemSummary.setBatchesToday(batchesToday);
            systemSummary.setBatchesThisWeek(batchesThisWeek);

            /* Get batches submitted yesterday */
        } catch (Exception ex) {
            Logger.getLogger(transactionInManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return systemSummary;

    }
    
    /**
     * The 'generateSystemWaitingSummary' function will return the summary object for
     * waiting to be processed system transactions
     * 
     * @return This function will return a systemSummary object 
     */
    @Override
    public systemSummary generateSystemWaitingSummary() {

        systemSummary systemSummary = new systemSummary();

        try {

            Integer transactionsToProcess = transactionOutDAO.getTransactionsToProcess().size();

            systemSummary.setbatchesToProcess(transactionsToProcess);

            /* Get batches submitted yesterday */
        } catch (Exception ex) {
            Logger.getLogger(transactionInManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return systemSummary;

    }
    
    @Override
    @Transactional
    public List <batchDownloads> getAllBatches(Date fromDate, Date toDate) throws Exception {
        return transactionOutDAO.getAllBatches(fromDate, toDate);
    }
    
    @Override
    public boolean searchTransactions(Transaction transaction, String searchTerm) throws Exception {

        boolean matchFound = false;
        
        String lcaseSearchTerm = searchTerm.toLowerCase();

        if (transaction.getmessageTypeName() != null && transaction.getmessageTypeName().toLowerCase().matches(".*" + lcaseSearchTerm + ".*")) {
            matchFound = true;
        }


        if (transaction.getstatusValue() != null && transaction.getstatusValue().toLowerCase().matches(".*" + lcaseSearchTerm + ".*")) {
            matchFound = true;
        }
        
        if (transaction.getsourceOrgFields().size() > 0) {

            for (int i = 0; i < transaction.getsourceOrgFields().size(); i++) {
                if (transaction.getsourceOrgFields().get(i).getFieldValue() != null && transaction.getsourceOrgFields().get(i).getFieldValue().toLowerCase().matches(".*" + lcaseSearchTerm + ".*")) {
                    matchFound = true;
                }
            }
        }
        
        if (transaction.gettargetOrgFields().size() > 0) {

            for (int i = 0; i < transaction.gettargetOrgFields().size(); i++) {
                if (transaction.gettargetOrgFields().get(i).getFieldValue() != null && transaction.gettargetOrgFields().get(i).getFieldValue().toLowerCase().matches(".*" + lcaseSearchTerm + ".*")) {
                    matchFound = true;
                }
            }
        }

        return matchFound;

    }
    
    
    @Override
    @Transactional
    public List getTransactionsToProcess() throws Exception {
        return transactionOutDAO.getTransactionsToProcess();
    }
    
    @Override
    @Transactional
    public List getTransactionsToProcessByMessageType(int orgId) throws Exception {
        return transactionOutDAO.getTransactionsToProcessByMessageType(orgId);
    }
    
    @Override
    @Transactional
    public List getAllransactionsToProcessByMessageType(int orgId, int messageTypeId) throws Exception {
        return transactionOutDAO.getAllransactionsToProcessByMessageType(orgId, messageTypeId);
    }

    @Override
    @Transactional
    public List<transactionTarget> getPendingDeliveryTransactions(int orgId, int messageType, Date fromDate, Date toDate) throws Exception {
        return transactionOutDAO.getPendingDeliveryTransactions(orgId, messageType, fromDate, toDate);
    }
    
    @Override
    public boolean searchTransactionsByMessageType(pendingDeliveryTargets transaction, String searchTerm) throws Exception {
        boolean matchFound = false;
        
        String lcaseSearchTerm = searchTerm.toLowerCase();
        
        if (transaction.getMessageType() != null && transaction.getMessageType().toLowerCase().matches(".*" + lcaseSearchTerm + ".*")) {
            matchFound = true;
        }
        
        if (transaction.getOrgDetails().toLowerCase().matches(".*" + lcaseSearchTerm + ".*")) {
            matchFound = true;
        }
        
        return matchFound;
    }
    
    @Override
    public boolean searchPendingTransactions(Transaction transaction, String searchTerm) throws Exception {
        
        boolean matchFound = false;
        
        String lcaseSearchTerm = searchTerm.toLowerCase();
        
        if (transaction.getbatchName() != null && transaction.getbatchName().toLowerCase().matches(".*" + lcaseSearchTerm + ".*")) {
            matchFound = true;
        }

        if (transaction.getsourceOrgFields().size() > 0) {

            for (int i = 0; i < transaction.getsourceOrgFields().size(); i++) {
                if (transaction.getsourceOrgFields().get(i).getFieldValue() != null && transaction.getsourceOrgFields().get(i).getFieldValue().toLowerCase().matches(".*" + lcaseSearchTerm + ".*")) {
                    matchFound = true;
                }
            }
        }
        
        if (transaction.getpatientFields().size() > 0) {

            for (int i = 0; i < transaction.getpatientFields().size(); i++) {
                if (transaction.getpatientFields().get(i).getFieldValue() != null && transaction.getpatientFields().get(i).getFieldValue().toLowerCase().matches(".*" + lcaseSearchTerm + ".*")) {
                    matchFound = true;
                }
            }
        }
        
        return matchFound;
        
        
    }
    
    @Override
    @Transactional
    public void doNotProcessTransaction(int transactionId) throws Exception {
        transactionOutDAO.doNotProcessTransaction(transactionId);
    }
    
    @Override
    @Transactional
    public List<batchDownloads> getInboxBatchesHistory(int userId, int orgId, int fromOrgId, int messageTypeId, Date fromDate, Date toDate) throws Exception {
        return transactionOutDAO.getInboxBatchesHistory(userId, orgId, fromOrgId, messageTypeId, fromDate, toDate);
    }
    
    @Override
    public List<Integer> findInboxBatches(List<batchDownloads> batches, String searchTerm) throws Exception {
        return transactionOutDAO.findInboxBatches(batches, searchTerm);
    }
    
    @Override
    public List<batchDownloadSummary> getBatchesBySentOrg(int srcorgId, int tgtOrgId, int messageTypeId) throws Exception {
        return transactionOutDAO.getBatchesBySentOrg(srcorgId, tgtOrgId, messageTypeId);
    }
}
