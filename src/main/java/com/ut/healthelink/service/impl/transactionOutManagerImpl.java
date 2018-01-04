/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.service.impl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.ut.healthelink.service.convertTextToPDF;
import com.ut.healthelink.dao.messageTypeDAO;
import com.ut.healthelink.dao.transactionOutDAO;
import com.ut.healthelink.model.HL7Details;
import com.ut.healthelink.model.HL7ElementComponents;
import com.ut.healthelink.model.HL7Elements;
import com.ut.healthelink.model.HL7Segments;
import com.ut.healthelink.model.Organization;
import com.ut.healthelink.model.Transaction;
import com.ut.healthelink.model.User;
import com.ut.healthelink.model.UserActivity;
import com.ut.healthelink.model.batchDownloadSummary;
import com.ut.healthelink.model.batchDownloads;
import com.ut.healthelink.model.batchClearAfterDelivery;
import com.ut.healthelink.model.batchUploads;
import com.ut.healthelink.model.configuration;
import com.ut.healthelink.model.configurationCCDElements;
import com.ut.healthelink.model.configurationConnection;
import com.ut.healthelink.model.configurationConnectionReceivers;
import com.ut.healthelink.model.configurationDataTranslations;
import com.ut.healthelink.model.configurationFTPFields;
import com.ut.healthelink.model.configurationFormFields;
import com.ut.healthelink.model.configurationRhapsodyFields;
import com.ut.healthelink.model.configurationSchedules;
import com.ut.healthelink.model.configurationTransport;
import com.ut.healthelink.model.configurationWebServiceFields;
import com.ut.healthelink.model.transactionRecords;
import com.ut.healthelink.model.wsMessagesOut;
import com.ut.healthelink.service.emailMessageManager;
import com.ut.healthelink.model.mailMessage;
import com.ut.healthelink.model.pendingDeliveryTargets;
import com.ut.healthelink.model.systemSummary;
import com.ut.healthelink.model.targetOutputRunLogs;
import com.ut.healthelink.model.transactionAttachment;
import com.ut.healthelink.model.transactionIn;
import com.ut.healthelink.model.transactionOutNotes;
import com.ut.healthelink.model.transactionOutRecords;
import com.ut.healthelink.model.transactionTarget;
import com.ut.healthelink.model.custom.ConfigOutboundForInsert;
import com.ut.healthelink.reference.fileSystem;
import com.ut.healthelink.service.configurationManager;
import com.ut.healthelink.service.configurationTransportManager;
import com.ut.healthelink.service.fileManager;
import com.ut.healthelink.service.organizationManager;
import com.ut.healthelink.service.transactionInManager;
import com.ut.healthelink.service.transactionOutManager;
import com.ut.healthelink.service.userManager;
import com.ut.healthelink.service.utilManager;
import com.ut.healthelink.webServices.WSManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.model.fields.FieldUpdater;
import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.model.fields.merge.MailMerger.OutputField;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
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
    
    @Resource(name = "myProps")
    private Properties myProps;
    
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

    @Autowired
    private fileManager filemanager;
    
    @Autowired
    private userManager usermanager;
    
    @Autowired
    private WSManager wsManager;
    
    @Autowired
    private utilManager utilmanager;
    
    @Autowired
    private convertTextToPDF txtToPDF;
    
    private int processingSysErrorId = 5;
    
    private String directoryPath = System.getProperty("directory.rootDir");
    
    private String archivePath = (directoryPath + "archivesOut/");
    
    private String massOutPutPath = (directoryPath + "massoutputfiles/");

    private String massOutPutPathMysqlPath =  System.getProperty("directory.massOutputPath");
   
    
    //list of final status - these records we skip
    private List<Integer> transRELId = Arrays.asList(11, 12, 13, 16, 18, 20);
    
    private List<Integer> rejectIds = Arrays.asList(13, 14);
    
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
    public Integer translateTargetRecords(int transactionTargetId, int configId, int batchId, int categoryId) {

        Integer errorCount = 0;

        /* Need to get the configured data translations */
        List<configurationDataTranslations> dataTranslations = configurationManager.getDataTranslationsWithFieldNo(configId, categoryId);

        for (configurationDataTranslations cdt : dataTranslations) {
            if (cdt.getCrosswalkId() != 0) {
                try {
                    errorCount = errorCount + transactionInManager.processCrosswalk(configId, batchId, cdt, true, transactionTargetId);
                } catch (Exception e) {
                    //throw new Exception("Error occurred processing crosswalks. crosswalkId: "+cdt.getCrosswalkId()+" configId: "+configId,e);
                    e.printStackTrace();
                    return 1;
                }
            } else if (cdt.getMacroId() != 0) {
                try {
                    errorCount = errorCount + transactionInManager.processMacro(configId, batchId, cdt, true, transactionTargetId);
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
            transactionTarget transaction = getTransactionDetails(transactionTargetId);
            if (transaction != null) {
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
                    //email admin
                    transactionInManager.sendEmailToAdmin(("Please check transactionOutErrors - SELECT * from transactionOutErrors where transactionTargetId = " + transaction.getId()), "Process Output Error");
                }

                if (!processed) {
                    //we update and log
                	updateTransactionTargetStatusOutBound(0, transaction.getId(), 0, 33);
                    transactionInManager.insertProcessingError(processingSysErrorId, null, 0, null, null, null, null, false, true, errorMessage, transaction.getId());
                    //email admin
                    transactionInManager.sendEmailToAdmin(("Please check transactionOutErrors - SELECT * from transactionOutErrors where transactionTargetId = " + transaction.getId()), "Process Output Error");
               
                }

                Integer processingErrors;
                /**
                 * pre-process should occur here, we get config and grab all macros associated, pre processing macro is category 2 *
                 */
                try {
                    processingErrors = translateTargetRecords(transaction.getId(), transaction.getconfigId(), transaction.getbatchDLId(), 2);
                } catch (Exception e) {
	                            // throw new Exception("Error occurred trying to translate target records. transactionId: "+ transaction.getId(),e);
                    //we log
                    e.printStackTrace();
                    processingErrors = 1;
                }
                if (processingErrors > 0) {
                    //we stop processing because one of the macros returned STOP
                    processed = false;
                }

                /* If processed == true update the status of the batch and transaction */
                if (processed == true) {

                    /* Need to start the transaction translations */
                    try {
                        processingErrors = translateTargetRecords(transaction.getId(), transaction.getconfigId(), transaction.getbatchDLId(), 1);
                    } catch (Exception e) {
		                            // throw new Exception("Error occurred trying to translate target records. transactionId: "+ transaction.getId(),e);
                        //we log
                        e.printStackTrace();
                        processingErrors = 1;
                    }

                    if (processingErrors != 0) {
                    	updateTransactionTargetStatusOutBound(0, transaction.getId(), 0, 33);
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
                        	updateTransactionTargetStatusOutBound(0, transaction.getId(), 0, 9);
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

                                /* if File Download update the status to Submission Delivery Completed ID = 23 status. 
                                 * This will only apply to scheduled and not continuous settings. */
                                if (transportDetails.gettransportMethodId() == 1) {
                                    transactionOutDAO.updateBatchStatus(batchId, 23);
                                    transactionInManager.updateBatchStatus(transaction.getbatchUploadId(), 23, "");
                                    //check inbound for clearing options
                                    if (configurationTransportManager.getTransportDetails(transactionInManager.getTransactionDetails(transaction.gettransactionInId()).getconfigId()).getclearRecords()) {
                                    	//we insert into clearAfterDelivery
                                    	batchClearAfterDelivery cad = new batchClearAfterDelivery();
            		                    cad.setBatchDLId(batchId);
            		                    cad.setBatchUploadId(transaction.getbatchUploadId());
            		                    cad.setTransactionInId(transaction.gettransactionInId());
            		                    cad.setTransactionTargetId(transaction.getId());
            		                    transactionInManager.saveBatchClearAfterDelivery(cad);
                                    }
                                    
                                } /* If FTP Call the FTP Method */ else if (transportDetails.gettransportMethodId() == 3) {
                                    FTPTargetFile(batchId, transportDetails);
                                } /* Rhapsody Method */ else if (transportDetails.gettransportMethodId() == 5) {
                                	RhapsodyTargetFile(batchId, transportDetails);
                                }/* If soap message  */ else if (transportDetails.gettransportMethodId() == 6) {
                                	SendWSMessage(batchId, transportDetails);
                                }
                                /** end of processing, before email **/
                                /** insert log**/ 
        		                try {
        		                	 //log user activity
        		                    UserActivity ua = new UserActivity();
        		                    ua.setUserId(0);
        		                    ua.setFeatureId(0);
        		                    ua.setAccessMethod("System");
        		                    ua.setActivity("System Processed Output File");
        		                    ua.setBatchUploadId(transaction.getbatchUploadId());
        		                    ua.setBatchDownloadId(batchId);
        		                    ua.setTransactionInIds(String.valueOf(transaction.gettransactionInId()));
        		                    ua.setTransactionTargetIds(String.valueOf(transportDetails.getId()));
        		                    usermanager.insertUserLog(ua);
        		                	
        		                } catch (Exception ex) {
        		                	ex.printStackTrace();
        		                	System.err.println("moveFilesByPath - insert user log" +  ex.toString());
        		                }
                                
                                if (batchId > 0) {
                                    /* Send the email to primary contact */
                                    try {
                                        /* Get the batch Details */
                                        batchDownloads batchDLInfo = transactionOutDAO.getBatchDetails(batchId);

                                        /* Get the from user */
                                        configuration fromConfig = configurationManager.getConfigurationById(transactionInManager.getTransactionDetails(transaction.gettransactionInId()).getconfigId());
                                        String msgType = "";
                                        if(fromConfig.getsourceType() == 1) {
                                            msgType = "Referral";
                                        }
                                        else {
                                            msgType = "Feedback Report";
                                        }
                                        
                                        Integer fromOrgId = 0;
                                        if(transaction.getSourceSubOrgId() > 0) {
                                            fromOrgId = transaction.getSourceSubOrgId();
                                        }
                                        else {
                                            fromOrgId = configurationManager.getConfigurationById(transactionInManager.getTransactionDetails(transaction.gettransactionInId()).getconfigId()).getorgId();
                                        }
                                        /* get the sending organization details */
                                        Organization fromOrg = organizationManager.getOrganizationById(fromOrgId);
                                        
                                        // Get a list of all user who should receive an email regarding the sent referral/FB report
                                        List<User> orgFromContacts = userManager.getUserConnectionListSending(transactionInManager.getTransactionDetails(transaction.gettransactionInId()).getconfigId());
                                       
                                        Integer toOrgId = 0;
                                        if(transaction.getTargetSubOrgId()> 0) {
                                            toOrgId = transaction.getTargetSubOrgId();
                                        }
                                        else {
                                            toOrgId = configurationManager.getConfigurationById(transaction.getconfigId()).getorgId();
                                        }
                                        
                                        /* get the receiving organization details */
                                        Organization toOrg = organizationManager.getOrganizationById(toOrgId);
                                        
                                        /* Send an email to all users in the connection setting who is set to receive an
                                        email when the referral/FB report was sent out */
                                        if (orgFromContacts != null && orgFromContacts.size() > 0) {
                                            String fromName = "";
                                            String fromEmail = "";
                                            mailMessage msg = new mailMessage();
                                            ArrayList<String> fromCCAddressArray = new ArrayList<String>();
                                            msg.setfromEmailAddress("support@health-e-link.net");
 
                                                
                                            for(int i = 0; i < orgFromContacts.size(); i++) {
                                                if("".equals(fromEmail)) {
                                                    fromName = orgFromContacts.get(i).getFirstName() + " " + orgFromContacts.get(i).getLastName();
                                                    fromEmail = orgFromContacts.get(i).getEmail();
                                                }
                                                else {
                                                    fromCCAddressArray.add(orgFromContacts.get(i).getEmail());
                                                }
                                            }
                                            
                                            if(!"".equals(fromEmail)) {
                                                //fromEmail = "support@health-e-link.net";
                                                msg.settoEmailAddress(fromEmail);

                                                if (fromCCAddressArray.size() > 0) {
                                                    String[] fromCCAddressList = new String[fromCCAddressArray.size()];
                                                    fromCCAddressList = fromCCAddressArray.toArray(fromCCAddressList);
                                                    msg.setccEmailAddress(fromCCAddressList);
                                                }

                                                msg.setmessageSubject("Your "+ msgType +" has been successfully delivered ("+myProps.getProperty("server.identity")+")");

                                                /* Build the body of the email */
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("The ").append(msgType).append(" sent to ").append(toOrg.getOrgName()).append(" has been successfully delivered."); 
                                                msg.setmessageBody(sb.toString());

                                                /* Send the email */
                                                try {
                                                    emailMessageManager.sendEmail(msg);
                                                } catch (Exception ex) {
                                                    System.err.println("mail exception");
                                                    //ex.printStackTrace();
                                                }
                                            }
                                        }
                                        
                                        // Get a list of all user who should receive an email regarding the recieived referral/FB report
                                        List<User> orgToContacts = userManager.getUserConnectionListReceiving(transaction.getconfigId());
                                       
                                        /* Send an email to all users in the connection setting who is set to receive an
                                        email when the referral/FB report was recievied */
                                        if (orgToContacts != null && orgToContacts.size() > 0) {
                                            String toName = "";
                                            String toEmail = "";
                                            mailMessage msg = new mailMessage();
                                            ArrayList<String> ccAddressArray = new ArrayList<String>();
                                            msg.setfromEmailAddress("support@health-e-link.net");
 
                                            for(int i = 0; i < orgToContacts.size(); i++) {
                                                if("".equals(toEmail)) {
                                                    toName = orgToContacts.get(i).getFirstName() + " " + orgToContacts.get(i).getLastName();
                                                    toEmail = orgToContacts.get(i).getEmail();
                                                }
                                                else {
                                                    ccAddressArray.add(orgToContacts.get(i).getEmail());
                                                }
                                            }
                                            
                                            if(!"".equals(toEmail)) {
                                                //toEmail = "support@health-e-link.net";
                                                msg.settoEmailAddress(toEmail);
                                                
                                                if (ccAddressArray.size() > 0) {
                                                    String[] ccAddressList = new String[ccAddressArray.size()];
                                                    ccAddressList = ccAddressArray.toArray(ccAddressList);
                                                    msg.setccEmailAddress(ccAddressList);
                                                }

                                                msg.setmessageSubject("You have received a new message from the eReferral System ("+myProps.getProperty("server.identity")+")");

                                                /* Build the body of the email */
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("You have received a new message from ").append(fromOrg.getOrgName()); 
                                                sb.append("<br /><br />");
                                                sb.append("BatchId: ").append(batchDLInfo.getutBatchName());
                                                if (batchDLInfo.getoutputFIleName() != null && !"".equals(batchDLInfo.getoutputFIleName())) {
                                                    sb.append("<br />");
                                                    sb.append("File Name: ").append(batchDLInfo.getoutputFIleName());
                                                }
                                                
                                                msg.setmessageBody(sb.toString());

                                                /* Send the email */
                                                try {
                                                    emailMessageManager.sendEmail(msg);
                                                } catch (Exception ex) {
                                                    System.err.println("mail exception");
                                                    //ex.printStackTrace();
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    	//should send email to admin 
                                    	
                                    	throw new Exception("Error occurred trying to send the alert email for batchId: " + batchId, e);
                                    }

                                }
                            } catch (Exception ex) {
                            	try{
                            		transactionInManager.sendEmailToAdmin((ex.toString() + "<br/>" + Arrays.toString(ex.getStackTrace())), "process output error");
                            	} catch (Exception e) {
                            		throw new Exception("Error occurred trying to send email error for auto process the output file ", e);
                            	}
                                throw new Exception("Error occurred trying to auto process the output file.", ex);
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
         Submission Delivery Locked ID = 22 status. same goes for rhapsody
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
                    switch (schedule.gettype()) {
                    /* WEEKLY SCHEDULE */
                        case 2:
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
                                
                            }   break;
                    /* MONTHLY SCHEDULE */
                        case 3:
                            long diffInWeeks = 0;
                            if (logs.size() > 0) {
                                targetOutputRunLogs log = logs.get(0);
                                long diff = currDate.getTime() - log.getlastRunTime().getTime();
                                
                                diffInWeeks = diff / ((long) 7 * 24 * 60 * 60 * 1000);
                                
                                if (diffInWeeks == 0 || diffInWeeks >= 1) {
                                    runProcess = true;
                                }
                                
                            }   break;
                        case 4:
                            long diffInDays = 0;
                            if (logs.size() > 0) {
                                targetOutputRunLogs log = logs.get(0);
                                long diff = currDate.getTime() - log.getlastRunTime().getTime();
                                
                                diffInDays = diff / ((long) 365.24 * 24 * 60 * 60 * 1000 / 12);
                                
                                if (diffInDays == 0 || diffInDays >= 30) {
                                    runProcess = true;
                                }
                                
                            }   break;
                        default:
                            break;
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
                         apply to scheduled and not continuous settings. */
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
                        } /* If Rhapsody Method */ else if (transportDetails.gettransportMethodId() == 5) {
                        	RhapsodyTargetFile(batchId, transportDetails);
                        }/* If soap message  */ else if (transportDetails.gettransportMethodId() == 6) {
                        	/** soap messages are to be delivery instantaneously. This is for scheduled
                        	 * output only, we error the batch and transactions **/
                        	transactionOutDAO.updateBatchStatus(batchId, 30);
                        	/**insert error**/
                        	transactionInManager.insertProcessingError(20, null, batchId, null, null, null, null, false, true, "Web Service config should not be scheduled.");
                        	updateTargetTransasctionStatus(batchId, 33);
                        	//update transactionIn status
                        	List<transactionTarget> targets = transactionOutDAO.getTransactionsByBatchDLId(batchId);
                            
                            /** we reject is targets are more than one, web service should only be sending one message at a time **/
                        	for (transactionTarget target : targets) {
                        		/* Need to update the uploaded batch status */
                                transactionInManager.updateBatchStatus(target.getbatchUploadId(), 30, "");
                                /* Need to update the uploaded batch transaction status */
                                transactionInManager.updateTransactionStatus(target.getbatchUploadId(), target.gettransactionInId(), 0, 33);
                            }
                        	/** we probably should email admin to fix web configuration **/
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
                            // Get a list of all user who should receive an email regarding the recieived referral/FB report
                            List<User> orgToContacts = userManager.getUserConnectionListReceiving(schedule.getconfigId());
                                 
                            if (orgToContacts.size() > 0) {
                                String toName = "";
                                String toEmail = "";
                                mailMessage msg = new mailMessage();
                                ArrayList<String> ccAddressArray = new ArrayList<String>();
                                msg.setfromEmailAddress("support@health-e-link.net");
                                
                                if (orgToContacts.size() > 0) {
                                                
                                    for(int i = 0; i < orgToContacts.size(); i++) {
                                        if("".equals(toEmail)) {
                                            toName = orgToContacts.get(i).getFirstName() + " " + orgToContacts.get(i).getLastName();
                                            toEmail = orgToContacts.get(i).getEmail();
                                        }
                                        else {
                                            ccAddressArray.add(orgToContacts.get(i).getEmail());
                                        }
                                    }
                                }

                                if("".equals(toEmail)) {
                                    toEmail = "support@health-e-link.net";
                                }

                                msg.settoEmailAddress(toEmail);
                                
                                if (ccAddressArray.size() > 0) {
                                    String[] ccAddressList = new String[ccAddressArray.size()];
                                    ccAddressList = ccAddressArray.toArray(ccAddressList);
                                    msg.setccEmailAddress(ccAddressList);
                                }

                                msg.setmessageSubject("You have received a new message from the eReferral System");

                                /* Build the body of the email */
                                StringBuilder sb = new StringBuilder();
                                sb.append("You have recieved a new message from the eReferral System. "); 
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
     * The 'processManualTransaction' function will start the processing of a transaction that is set to manual.
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
                    batchId = generateBatch(uploadedBatchDetails.getdateSubmitted(), configDetails, transaction, transportDetails, uploadedBatchDetails.getOrgId(), uploadedBatchDetails.getoriginalFileName(), false);

                    /* Update the status of the uploaded batch to  TBP (Target Batch Creating in process) (ID = 25) */
                    transactionInManager.updateBatchStatus(batchId, 25, "");
                } catch (Exception e) {
                    throw new Exception("Error occurred trying to generate a batch. transactionId: " + transaction.getId(), e);
                }

            } /* File Download || FTP || EMed-Apps */ else if (transportDetails.gettransportMethodId() == 1 || transportDetails.gettransportMethodId() == 3 || transportDetails.gettransportMethodId() == 5 ||transportDetails.gettransportMethodId() == 6) {

                boolean createNewFile = true;

                /* 
                 If the merge batches option is not checked create the batch right away
                 */
                if (transportDetails.getmergeBatches() == false) {

                    /* Generate the batch */
                    /* (target configuration Details, transaction details, transport Details for target config, Source OrgId, Source Original Filename, mergeable) */
                    try {
                        batchId = generateBatch(uploadedBatchDetails.getdateSubmitted(), configDetails, transaction, transportDetails, uploadedBatchDetails.getOrgId(), uploadedBatchDetails.getoriginalFileName(), false);

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
                            batchId = generateBatch(uploadedBatchDetails.getdateSubmitted(), configDetails, transaction, transportDetails, uploadedBatchDetails.getOrgId(), uploadedBatchDetails.getoriginalFileName(), true);

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
                        summary.setSourceSubOrgId(transaction.getSourceSubOrgId());
                        summary.setTargetSubOrgId(transaction.getTargetSubOrgId());
                        

                        try {
                            transactionOutDAO.submitSummaryEntry(summary);
                        } catch (Exception e) {
                            throw new Exception("Error occurred submitting the batch summary. batchId: " + batchId, e);
                        }

                        createNewFile = false;

                    }

                }

                /* Generate the file according to transportDetails 
                 * 1. we generate output file according to encoding in transportDetails
                 * 2. we always save an encrypted copy to archivesOut
                 * */
                try {
                    boolean encryptMessage = false;
                    // we only support base64 for now
                    if (transportDetails.getEncodingId() == 2) {        
                        encryptMessage = true;
                	}
                    String generatedFilePath = generateTargetFile(createNewFile, transaction.getId(), batchId, transportDetails, encryptMessage);       
                    
                    try {
                    	//we get dl file info
                    	batchDownloads batchDetails = getBatchDetails(batchId);
                    	
                    	File generatedFile = new File(generatedFilePath);
                    	//file extension
                    	String fileExt = batchDetails.getoutputFIleName().substring(batchDetails.getoutputFIleName().lastIndexOf("."));
                		
                    	fileSystem fileSystem = new fileSystem();
                    	File archiveFile = new File ( fileSystem.setPathFromRoot(archivePath) + batchDetails.getutBatchName() + fileExt);
                    	
                    	//we check to see if our file is encoded
                    	if (!encryptMessage)  {
                    		//we encode here
                    		String strEncodedFile = filemanager.encodeFileToBase64Binary(generatedFile);
                    		if (archiveFile.exists()) {
                    			archiveFile.delete();
                    		}
                    		filemanager.writeFile(archiveFile.getAbsolutePath(), strEncodedFile);
                    	} else  {
                    		Files.copy(generatedFile.toPath(), archiveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    	}
                    
                    } catch (Exception e) {
                    	throw new Exception("Error occurred trying to copy generated file to archiveOut -  batchId: " + batchId, e);    	
                    }
                } catch (Exception e) {
                    throw new Exception("Error occurred trying to generate the batch file. batchId: " + batchId, e);
                }
            }


            /* Update the status of the transaction to PP (Pending Pickup) (ID = 18) */
            transactionInManager.updateTransactionStatus(transaction.getbatchUploadId(), transaction.gettransactionInId(), 0, 18);

            /* Update the status of the transaction target to PP (Pending Pickup) (ID = 18) */
            updateTransactionTargetStatusOutBound(0, transaction.getId(), 0, 18);

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
    public int generateBatch(Date referralDate, configuration configDetails, transactionTarget transaction, configurationTransport transportDetails, int sourceOrgId, String sourceFileName, boolean mergeable) throws Exception {

        /* Create the batch name (OrgId+MessageTypeId+Date/Time) - need milliseconds as computer is fast and files have the same name*/
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
        Date date = new Date();
        String utbatchName = new StringBuilder().append(transportDetails.gettransportMethodId()).append(configDetails.getorgId()).append(configDetails.getMessageTypeId()).append(dateFormat.format(date)).toString();


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
        batchDownload.setdateCreated(referralDate);

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
        summary.setSourceSubOrgId(transaction.getSourceSubOrgId());
        summary.setTargetSubOrgId(transaction.getTargetSubOrgId());

        transactionOutDAO.submitSummaryEntry(summary);

        return batchId;
    }

    /**
     * The 'generateTargetFile' function will generate the actual file in the correct organizations outpufiles folder.
     */
    public String generateTargetFile(boolean createNewFile, int transactionTargetId, int batchId, configurationTransport transportDetails, boolean encrypt) throws Exception {

        String fileName = null;
        String strFileLoc = "";
        batchDownloads batchDetails = transactionOutDAO.getBatchDetails(batchId);

        InputStream inputStream = null;
        OutputStream outputStream = null;

        fileSystem dir = new fileSystem();

        String filelocation = transportDetails.getfileLocation();
        filelocation = filelocation.replace("/bowlink/", "");

        dir.setDirByName(filelocation);

        boolean hl7 = false;
        boolean CCD = false;
        String fileType = (String) configurationManager.getFileTypesById(transportDetails.getfileType());
        
        if ("hl7".equals(fileType)) {
            hl7 = true;
        }
        else if("xml (CCD)".equals(fileType)) {
            CCD = true;
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

        /* Get the target fields */
        //NEW
        List<configurationFormFields> formFields = configurationTransportManager.getConfigurationFields(transportDetails.getconfigId(), transportDetails.getId());

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
            
            /* If a CCD file is to be generated */
            if(CCD == true) {
                
                Organization orgDetails = organizationManager.getOrganizationById(configurationManager.getConfigurationById(transportDetails.getconfigId()).getorgId());
                fileSystem ccdTemplateDir = new fileSystem();
                ccdTemplateDir.setDir(orgDetails.getcleanURL(), "templates");
                
                String ccdSampleTemplate = transportDetails.getCcdSampleTemplate();
                
                Path path = Paths.get(ccdTemplateDir.getDir() + ccdSampleTemplate);
                String ccdSampleContent = new String(Files.readAllBytes(path));
                
                Path newFilePath = Paths.get(dir.getDir() + fileName);
                Files.write(newFilePath, ccdSampleContent.getBytes());
                
                String contentToUpdate = new String(Files.readAllBytes(newFilePath));
                
                /* Get the configurationCCDElements */
                List<configurationCCDElements> ccdElements = configurationManager.getCCDElements(transportDetails.getconfigId());
                
                if(!ccdElements.isEmpty()) {
                    
                    for(configurationCCDElements element : ccdElements) {
                        
                        if(!"".equals(element.getDefaultValue())) {
                            if ("~currDate~".equals(element.getDefaultValue())) {
                                SimpleDateFormat date_format = new SimpleDateFormat("yyyyMMdd");
                                String date = date_format.format(batchDetails.getdateCreated());
                                contentToUpdate = contentToUpdate.replace(element.getElement(), date);
                            }
                            else {
                                contentToUpdate = contentToUpdate.replace(element.getElement(), element.getDefaultValue());
                            }
                           
                        }
                        else {
                             String colName = new StringBuilder().append("f").append(element.getFieldValue()).toString();
                             
                              String fieldValue = BeanUtils.getProperty(records, colName);

                              if (fieldValue == null) {
                                   fieldValue = "";
                              } else if ("null".equals(fieldValue)) {
                                  fieldValue = "";
                              } else if (fieldValue.isEmpty()) {
                                  fieldValue = "";
                              } else if (fieldValue.length() == 0) {
                                  fieldValue = "";
                              }
                              
                              contentToUpdate = contentToUpdate.replace(element.getElement(), fieldValue);
                        }
                        
                    }
                }
                
                /** need to see if we need to  encrypt file here  
                 **/
                if (!encrypt) {
                	Files.write(newFilePath, contentToUpdate.getBytes());
                } else {
                	String strEncodedFile = utilmanager.encodeStringToBase64Binary(contentToUpdate);
                	Files.write(newFilePath, strEncodedFile.getBytes());
                } 
            }
            /* If an hl7 file is to be generated */
            else if (hl7 == true) {

                /* Get the hl7 details */
                HL7Details hl7Details = configurationManager.getHL7Details(transportDetails.getconfigId());

                if (hl7Details != null) {
                    
                    /* Get the hl7 Segments */
                    List<HL7Segments> hl7Segments = configurationManager.getHL7Segments(hl7Details.getId());

                    if (!hl7Segments.isEmpty()) {

                        StringBuilder hl7recordRow = new StringBuilder();

                        for (HL7Segments segment : hl7Segments) {

                            /* Get the segment elements */
                            List<HL7Elements> hl7Elements = configurationManager.getHL7Elements(hl7Details.getId(), segment.getId());

                            if (!hl7Elements.isEmpty()) {

                                hl7recordRow.append(segment.getsegmentName()).append(hl7Details.getfieldSeparator());

                                int elementCounter = 1;
                                for (HL7Elements element : hl7Elements) {
                                    
                                    if("pdfattachment".equals(element.getelementName().toLowerCase()) && transportDetails.getHL7PDFSampleTemplate()!= null && !"".equals(transportDetails.getHL7PDFSampleTemplate())) {
                                       
                                        Organization orgDetails = organizationManager.getOrganizationById(configurationManager.getConfigurationById(transportDetails.getconfigId()).getorgId());
                                        fileSystem hl7PDFTemplateDir = new fileSystem();
                                        hl7PDFTemplateDir.setDir(orgDetails.getcleanURL(), "templates");

                                        String hl7PDFSampleTemplate = transportDetails.getHL7PDFSampleTemplate();
                                        
                                        File inputFile;
                                        
                                        if(hl7PDFSampleTemplate.contains(".docx")) {
                                            String inputfilepath = hl7PDFTemplateDir.getDir() + hl7PDFSampleTemplate;
                                        
                                            String outputfilepath = dir.getDir() + "OUT_variableReplace.docx";

                                            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new java.io.File(inputfilepath));

                                            //MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

                                            List<configurationCCDElements> hl7PDFElements = configurationManager.getCCDElements(transportDetails.getconfigId());

                                            if(!hl7PDFElements.isEmpty()) {
                                                
                                                List<Map<DataFieldName, String>> data = new ArrayList<Map<DataFieldName, String>>();

                                                Map<DataFieldName, String> map1 = new HashMap<DataFieldName, String>();
                                                
                                                for(configurationCCDElements CCDelement : hl7PDFElements) {
                                                    
                                                    String elementName = CCDelement.getElement().replace("<%", "").replace("%>", "");

                                                    if(!"".equals(CCDelement.getDefaultValue())) {

                                                        if ("~currDate~".equals(CCDelement.getDefaultValue())) {
                                                            SimpleDateFormat date_format = new SimpleDateFormat("yyyyMMdd");
                                                            String date = date_format.format(batchDetails.getdateCreated());
                                                            
                                                            map1.put(new DataFieldName(elementName), date);

                                                        }
                                                        else {
                                                             map1.put(new DataFieldName(elementName), CCDelement.getDefaultValue());
                                                        }
                                                    }
                                                    else {
                                                        String colName = new StringBuilder().append("f").append(CCDelement.getFieldValue()).toString();

                                                        String fieldValue = BeanUtils.getProperty(records, colName);

                                                        if (fieldValue == null) {
                                                             fieldValue = "";
                                                        } else if ("null".equals(fieldValue)) {
                                                            fieldValue = "";
                                                        } else if (fieldValue.isEmpty()) {
                                                            fieldValue = "";
                                                        } else if (fieldValue.length() == 0) {
                                                            fieldValue = "";
                                                        }  

                                                         map1.put(new DataFieldName(elementName), fieldValue);
                                                    }
                                                }
                                                data.add(map1);
                                                
                                                org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.REMOVED);
                                                
                                                int x=0;
                                                for(Map<DataFieldName,String> docMapping : data) {
                                                    org.docx4j.model.fields.merge.MailMerger.performMerge(wordMLPackage, docMapping, true);
                                                    wordMLPackage.save(new java.io.File(outputfilepath) );
                                                }
                                                
                                            }
                                            
                                            inputFile = new File(dir.getDir() + "OUT_variableReplace.docx");
                                            
                                            FieldUpdater updater = new FieldUpdater(wordMLPackage);
                                            updater.update(true);
                                            
                                            FOSettings foSettings = Docx4J.createFOSettings();
                                            foSettings.setWmlPackage(wordMLPackage);
                                            
                                            String outputfilepath2 = dir.getDir() + "hl7pdf.pdf";
                                            OutputStream os = new java.io.FileOutputStream(outputfilepath2);
                                            Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);
                                            
                                            if (wordMLPackage.getMainDocumentPart().getFontTablePart()!=null) {
                                               wordMLPackage.getMainDocumentPart().getFontTablePart().deleteEmbeddedFontTempFiles();
                                            }
                                            
                                            updater = null;
                                            foSettings = null;
                                            wordMLPackage = null;

                                            fileSystem attachDir = new fileSystem();
                                            File f = new File(dir.getDir() + "hl7pdf.pdf");
                                            byte[] bytes = attachDir.loadFile(f);
                                            byte[] encoded = Base64.encode(bytes);
                                            String encodedString = new String(encoded);

                                            hl7recordRow.append(encodedString);

                                            /* Decode the encoded content back to a PDF */
                                            /*byte[] decoded = Base64.decode(encodedString.getBytes());
                                            File newfile = new File(dir.getDir() + "sample.pdf");
                                            BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(newfile));
                                            writer.write(decoded);
                                            writer.flush();
                                            writer.close();*/

                                            /* Delete files */
                                            inputFile.delete();
                                            f.delete();
                                        }
                                        else {
                                            
                                           Path path = Paths.get(hl7PDFTemplateDir.getDir() + hl7PDFSampleTemplate);
                                           String hl7PDFSampleContent = new String(Files.readAllBytes(path));


                                            Path newFilePath = Paths.get(dir.getDir() + "hl7pdf.txt");
                                            Files.write(newFilePath, hl7PDFSampleContent.getBytes());

                                            String contentToUpdate = new String(Files.readAllBytes(newFilePath));

                                            List<configurationCCDElements> hl7PDFElements = configurationManager.getCCDElements(transportDetails.getconfigId());

                                            if(!hl7PDFElements.isEmpty()) {

                                                for(configurationCCDElements CCDelement : hl7PDFElements) {

                                                    if(!"".equals(CCDelement.getDefaultValue())) {
                                                        if ("~currDate~".equals(CCDelement.getDefaultValue())) {
                                                            SimpleDateFormat date_format = new SimpleDateFormat("yyyyMMdd");
                                                            String date = date_format.format(batchDetails.getdateCreated());
                                                            contentToUpdate = contentToUpdate.replace(CCDelement.getElement(), date);
                                                        }
                                                        else {
                                                            contentToUpdate = contentToUpdate.replace(CCDelement.getElement(), CCDelement.getDefaultValue());
                                                        }

                                                    }
                                                    else {
                                                         String colName = new StringBuilder().append("f").append(CCDelement.getFieldValue()).toString();

                                                          String fieldValue = BeanUtils.getProperty(records, colName);

                                                          if (fieldValue == null) {
                                                               fieldValue = "";
                                                          } else if ("null".equals(fieldValue)) {
                                                              fieldValue = "";
                                                          } else if (fieldValue.isEmpty()) {
                                                              fieldValue = "";
                                                          } else if (fieldValue.length() == 0) {
                                                              fieldValue = "";
                                                          }

                                                          contentToUpdate = contentToUpdate.replace(CCDelement.getElement(), fieldValue);
                                                    }

                                                }
                                            }

                                            Files.write(newFilePath, contentToUpdate.getBytes()); 
                                            
                                            inputFile = new File(dir.getDir() + "hl7pdf.txt");
                                            
                                            if(txtToPDF.convertTextToPDF(inputFile, dir.getDir(), "hl7pdf.pdf")) {
                                                fileSystem attachDir = new fileSystem();
                                                File f = new File(dir.getDir() + "hl7pdf.pdf");
                                                byte[] bytes = attachDir.loadFile(f);
                                                byte[] encoded = Base64.encode(bytes);
                                                String encodedString = new String(encoded);

                                                hl7recordRow.append(encodedString);

                                                /* Decode the encoded content back to a PDF */
                                                /*byte[] decoded = Base64.decode(encodedString.getBytes());
                                                File newfile = new File(dir.getDir() + "sample.pdf");
                                                BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(newfile));
                                                writer.write(decoded);
                                                writer.flush();
                                                writer.close();*/

                                                /* Delete files */
                                                inputFile.delete();
                                                f.delete();
                                            }
                                        }
                                        
                                    }
                                    
                                    /* If the HL7 requires attachments then we need to look for the "attachments" keyword
                                    in order to loop through and retrieve all attachments to the batch.
                                    */
                                    if("attachments".equals(element.getelementName().toLowerCase())) {
                                        transactionTarget targetDetails = transactionOutDAO.getTransactionDetails(transactionTargetId);
                                        List<transactionAttachment> attachments = transactionInManager.getAttachmentsByTransactionId(targetDetails.gettransactionInId());
                                        
                                        if(!attachments.isEmpty()) {
                                            Integer attachmentCounter = 1;
                                            for(transactionAttachment attachment : attachments) {
                                                fileSystem attachDir = new fileSystem();
                                                attachDir.setDirByName(attachment.getfileLocation() + "/");
                                                File f = new File(attachDir.getDir() + attachment.getfileName());
                                                byte[] bytes = attachDir.loadFile(f);
                                                byte[] encoded = Base64.encode(bytes);
                                                String encodedString = new String(encoded);
                                                if(!"".equals(attachment.gettitle()) && attachment.gettitle() != null) {
                                                    hl7recordRow.append(attachmentCounter).append(hl7Details.getfieldSeparator()).append(attachment.gettitle()).append(hl7Details.getfieldSeparator());
                                                }
                                                else {
                                                    hl7recordRow.append(attachmentCounter).append(hl7Details.getfieldSeparator()).append(attachment.getfileName()).append(hl7Details.getfieldSeparator());
                                                }
                                                
                                                hl7recordRow.append(encodedString);
                                                
                                                if(attachmentCounter < attachments.size()) {
                                                    hl7recordRow.append(System.getProperty("line.separator"));
                                                    hl7recordRow.append(segment.getsegmentName()).append(hl7Details.getfieldSeparator());
                                                    attachmentCounter += 1;
                                                }
                                            }
                                            
                                        }
                                    }
                                    else {

                                        if (!"".equals(element.getdefaultValue()) && element.getdefaultValue() != null) {
                                            if ("~currDate~".equals(element.getdefaultValue())) {
                                                SimpleDateFormat date_format = new SimpleDateFormat("yyyyMMdd");
                                                String date = date_format.format(batchDetails.getdateCreated());
                                                hl7recordRow.append(date);
                                            } else {
                                                hl7recordRow.append(element.getdefaultValue());
                                            }

                                        } else {

                                            /* Get the element components */
                                            List<HL7ElementComponents> hl7Components = configurationManager.getHL7ElementComponents(element.getId());

                                            if (!hl7Components.isEmpty()) {
                                                int counter = 1;
                                                for (HL7ElementComponents component : hl7Components) {
                                                    
                                                    String fieldValue = "";
                                                    
                                                    if(!"".equals(component.getDefaultValue()) && component.getDefaultValue() != null) {
                                                        
                                                        /* If the HL7 requires attachments then we need to look for the "attachments" keyword
                                                        in order to loop through and retrieve all attachments to the batch.
                                                        */
                                                        if("attachments".equals(component.getDefaultValue().toLowerCase())) {
                                                            transactionTarget targetDetails = transactionOutDAO.getTransactionDetails(transactionTargetId);
                                                            List<transactionAttachment> attachments = transactionInManager.getAttachmentsByTransactionId(targetDetails.gettransactionInId());

                                                            if(!attachments.isEmpty()) {
                                                                Integer attachmentCounter = 1;
                                                                for(transactionAttachment attachment : attachments) {
                                                                    fileSystem attachDir = new fileSystem();
                                                                    attachDir.setDirByName(attachment.getfileLocation() + "/");
                                                                    File f = new File(attachDir.getDir() + attachment.getfileName());
                                                                    byte[] bytes = attachDir.loadFile(f);
                                                                    byte[] encoded = Base64.encode(bytes);
                                                                    fieldValue = new String(encoded);
                                                                }
                                                            }
                                                        }
                                                        else {
                                                            fieldValue = component.getDefaultValue();
                                                        }
                                                        
                                                    }
                                                    else {
                                                        String colName = new StringBuilder().append("f").append(component.getfieldValue()).toString();

                                                        fieldValue = BeanUtils.getProperty(records, colName);

                                                        if (fieldValue == null) {
                                                            fieldValue = "";
                                                        } else if ("null".equals(fieldValue)) {
                                                            fieldValue = "";
                                                        } else if (fieldValue.isEmpty()) {
                                                            fieldValue = "";
                                                        } else if (fieldValue.length() == 0) {
                                                            fieldValue = "";
                                                        }
                                                    }


                                                    if (!"".equals(component.getfieldDescriptor()) && component.getfieldDescriptor() != null) {
                                                        hl7recordRow.append(component.getfieldDescriptor()).append(" ").append(fieldValue);
                                                    } else {
                                                        hl7recordRow.append(fieldValue);
                                                    }

                                                    if (!"".equals(component.getFieldAppendText()) && component.getFieldAppendText() != null) {
                                                        hl7recordRow.append(" ").append(component.getFieldAppendText());
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
                                    }

                                    if (elementCounter < hl7Elements.size()) {
                                        hl7recordRow.append(hl7Details.getfieldSeparator());
                                        elementCounter += 1;
                                    }

                                }
                                
                                hl7recordRow.append(System.getProperty("line.separator"));
                            }

                        }

                        if (!"".equals(hl7recordRow.toString())) {
                            try {
                                if (encrypt == true) {
                                    byte[] encoded = Base64.encode(hl7recordRow.toString().getBytes());
                                    fw.write(new String(encoded));
                                } else {
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

                // for (int i = 1; i <= maxFieldNo; i++) {
                //NEW
                for (configurationFormFields field : formFields) {

                    //String colName = new StringBuilder().append("f").append(i).toString();
                    //NEW
                    if (field.getUseField() == true) {
                        //NEW
                        String colName = new StringBuilder().append("f").append(field.getFieldNo()).toString();

                        try {
                            String fieldValue = BeanUtils.getProperty(records, colName);

                            if (fieldValue == null) {
                                fieldValue = "";
                            } else if ("null".equals(fieldValue)) {
                                fieldValue = "";
                            } else if (fieldValue.isEmpty()) {
                                fieldValue = "";
                            } else if (fieldValue.length() == 0) {
                                fieldValue = "";
                            }

                            //if (i == maxFieldNo) {
                            //New
                            if (field.getFieldNo() == maxFieldNo) {
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
                        //NEW
                    }
                }

                if (recordRow != null) {
                    try {
                        if (encrypt == true) {
                            byte[] encoded = Base64.encode(recordRow.getBytes());
                            fw.write(new String(encoded));
                        } else {
                            fw.write(recordRow);
                        }

                        fw.close();
                    } catch (IOException ex) {
                        throw new IOException(ex);
                    }
                }
            }

        }
        	strFileLoc = file.getAbsolutePath();
        	return strFileLoc;
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
                    transactionInManager.updateTransactionStatus(target.getbatchUploadId(), target.gettransactionInId(), 0, 37);

                    /* Update the downloaded batch transaction status */
                    transactionOutDAO.updateTargetTransasctionStatus(target.getbatchDLId(), 37);

                    //check inbound for clearing options
                    if (configurationTransportManager.getTransportDetails(transactionInManager.getTransactionDetails(target.gettransactionInId()).getconfigId()).getclearRecords()) {
                    	//we insert into clearAfterDelivery
                    	batchClearAfterDelivery cad = new batchClearAfterDelivery();
	                    cad.setBatchDLId(batchId);
	                    cad.setBatchUploadId(target.getbatchUploadId());
	                    cad.setTransactionInId(target.gettransactionInId());
	                    cad.setTransactionTargetId(target.getId());
	                    transactionInManager.saveBatchClearAfterDelivery(cad);
                    }
                    
                }

            }

            /* get the batch details */
            batchDownloads batchFTPFileInfo = transactionOutDAO.getBatchDetails(batchId);

            /* Get the FTP Details */
            configurationFTPFields ftpDetails = configurationTransportManager.getTransportFTPDetailsPush(transportDetails.getId());

            if ("SFTP".equals(ftpDetails.getprotocol())) {

                JSch jsch = new JSch();
                Session session = null;
                ChannelSftp channel = null;
                FileInputStream localFileStream = null;

                String user = ftpDetails.getusername();
                int port = ftpDetails.getport();
                String host = ftpDetails.getip();

                Organization orgDetails = organizationManager.getOrganizationById(configurationManager.getConfigurationById(transportDetails.getconfigId()).getorgId());

                if (ftpDetails.getcertification() != null && !"".equals(ftpDetails.getcertification())) {

                    File newFile = null;

                    fileSystem dir = new fileSystem();
                    dir.setDir(orgDetails.getcleanURL(), "certificates");

                    jsch.addIdentity(new File(dir.getDir() + ftpDetails.getcertification()).getAbsolutePath());
                    session = jsch.getSession(user, host, port);
                } else if (ftpDetails.getpassword() != null && !"".equals(ftpDetails.getpassword())) {
                    session = jsch.getSession(user, host, port);
                    session.setPassword(ftpDetails.getpassword());
                }

                session.setConfig("StrictHostKeyChecking", "no");
                session.setTimeout(2000);

                session.connect();

                channel = (ChannelSftp) session.openChannel("sftp");

                channel.connect();

                if (ftpDetails.getdirectory() != null && !"".equals(ftpDetails.getdirectory())) {
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

                    if (file.exists()) {
                        FileInputStream fileInput = new FileInputStream(file);

                        channel.put(fileInput, fileName);
                    }

                }

                channel.disconnect();
                session.disconnect();

            } else {
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

            // we should delete file now that we ftp'ed the file
            try {
            fileSystem dir = new fileSystem();
            String filelocation = transportDetails.getfileLocation();
            filelocation = filelocation.replace("/bowlink/", "");
            dir.setDirByName(filelocation);
            
            File sourceFile = new File(dir.getDir() + batchFTPFileInfo.getoutputFIleName());
            if (sourceFile.exists()) {
            	sourceFile.delete();
            }
            
            transactionOutDAO.updateBatchStatus(batchId, 23);
            
            for (transactionTarget target : targets) {

                /* Need to update the uploaded batch status */
                transactionInManager.updateBatchStatus(target.getbatchUploadId(), 23, "");

                /* Need to update the uploaded batch transaction status */
                transactionInManager.updateTransactionStatus(target.getbatchUploadId(), target.gettransactionInId(), 0, 20);

                /* Update the downloaded batch transaction status */
                transactionOutDAO.updateTargetTransasctionStatus(target.getbatchDLId(), 20);

            }
            
            } catch (Exception e){
            	throw new Exception("Error occurred during FTP - delete file and update statuses. batchId: " + batchId, e);
            	
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
     * The 'generateSystemOutboundSummary' function will return the summary object for outbound system batches
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
     * The 'generateSystemWaitingSummary' function will return the summary object for waiting to be processed system transactions
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
    public List<batchDownloads> getAllBatches(Date fromDate, Date toDate) throws Exception {
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
    public boolean searchBatchForHistory(batchDownloads batchDetails, String searchTerm, Date fromDate, Date toDate) throws Exception {
        return transactionOutDAO.searchBatchForHistory(batchDetails, searchTerm, fromDate, toDate);
    }
   
    @Override
    public List<batchDownloadSummary> getBatchesBySentOrg(int srcorgId, int tgtOrgId, int messageTypeId) throws Exception {
        return transactionOutDAO.getBatchesBySentOrg(srcorgId, tgtOrgId, messageTypeId);
    }

    /** modifying this to select one record at a time so we don't end up with a long list that the
     * scheduler picks up over and over 
     * We also should not pick up transactions from any batches that are defined for mass translation
     */
    
    @Override
    public void selectOutputRecordsForProcess(Integer transactionTargetId) throws Exception {
        try {
            //we get all the transactions that we want to process, we lock them
            List<transactionTarget> pendingTransactions = transactionOutDAO.getpendingOutPutTransactions(transactionTargetId);

            if (pendingTransactions.size() > 0) {
                for (transactionTarget transaction : pendingTransactions) {
                    //we recheck in case another scheduler picked it up
                    if (getTransactionDetails(transaction.getId()).getstatusId() == 19) {
                    	updateTransactionTargetStatusOutBound(0, transaction.getId(), 19, 37);
                        //we process it
                        processOutputRecords(transaction.getId());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error at selectOutputRecordsForProcess");
        }
    }

    /** 
     * The 'RhapsodyTargetFile' function will get the Rhapsody details and move the file to the
     * output folder defined in 
     *
     * @param batchId The id of the batch to move to Rhapsody folder
     */
    private void RhapsodyTargetFile(int batchId, configurationTransport transportDetails)  {

        try {

            /* Update the status of the batch to locked */
            transactionOutDAO.updateBatchStatus(batchId, 22);

            List<transactionTarget> targets = transactionOutDAO.getTransactionsByBatchDLId(batchId);

            if (!targets.isEmpty()) {

                for (transactionTarget target : targets) {

                    /* Need to update the uploaded batch status */
                    transactionInManager.updateBatchStatus(target.getbatchUploadId(), 22, "");

                    /* Need to update the uploaded batch transaction status */
                    transactionInManager.updateTransactionStatus(target.getbatchUploadId(), target.gettransactionInId(), 0, 37);

                    /* Update the downloaded batch transaction status */
                    transactionOutDAO.updateTargetTransasctionStatus(target.getbatchDLId(), 37);

                    //check inbound for clearing options
                    if (configurationTransportManager.getTransportDetails(transactionInManager.getTransactionDetails(target.gettransactionInId()).getconfigId()).getclearRecords()) {
                    	//we insert into clearAfterDelivery
                    	batchClearAfterDelivery cad = new batchClearAfterDelivery();
	                    cad.setBatchDLId(batchId);
	                    cad.setBatchUploadId(target.getbatchUploadId());
	                    cad.setTransactionInId(target.gettransactionInId());
	                    cad.setTransactionTargetId(target.getId());
	                    transactionInManager.saveBatchClearAfterDelivery(cad);
                    }
                }

            }

            /* get the batch details */
            batchDownloads batchDetails = transactionOutDAO.getBatchDetails(batchId);

            /* Get the Rhapsody Details */
            configurationRhapsodyFields rhapsodyDetails = configurationTransportManager.getTransRhapsodyDetailsPush(transportDetails.getId());

            // the file is in output folder already, we need to rebuild path and move it
            
            fileSystem dir = new fileSystem();
            String filelocation = transportDetails.getfileLocation();
            filelocation = filelocation.replace("/bowlink/", "");
            dir.setDirByName(filelocation);
            
            File sourceFile = new File(dir.getDir() + batchDetails.getoutputFIleName());
            File targetFile = new File ( directoryPath + rhapsodyDetails.getDirectory() + batchDetails.getoutputFIleName());
            //move the file over and update the status to complete
            Files.move(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            transactionOutDAO.updateBatchStatus(batchId, 23);
            
            for (transactionTarget target : targets) {

                /* Need to update the uploaded batch status */
                transactionInManager.updateBatchStatus(target.getbatchUploadId(), 23, "");

                /* Need to update the uploaded batch transaction status */
                transactionInManager.updateTransactionStatus(target.getbatchUploadId(), target.gettransactionInId(), 0, 20);

                /* Update the downloaded batch transaction status */
                transactionOutDAO.updateTargetTransasctionStatus(target.getbatchDLId(), 20);

            }
            
        } catch (Exception ex) {
        	try{
        		transactionInManager.sendEmailToAdmin(("RhapsodyTargetFile - Error occurred trying to move a batch target. batchId: " + batchId + "<br/>"+  ex.toString() + "<br/>" + Arrays.toString(ex.getStackTrace())), " RhapsodyTargetFile Error ");
        	} catch (Exception e) {
        		ex.printStackTrace();
                System.err.println("RhapsodyTargetFile - Error occurred trying to move a batch target. batchId: " + batchId);
        	}
        	ex.printStackTrace();
            System.err.println("RhapsodyTargetFile - Error occurred trying to move a batch target. batchId: " + batchId);
        }

    }


    @Override
    public List<batchDownloadSummary> getuploadBatchesByConfigAndSource(Integer configId, Integer orgId, Integer userOrgId) {
        return transactionOutDAO.getuploadBatchesByConfigAndSource(configId, orgId, userOrgId);
    }

	@Override
	public void updateTransactionTargetStatusOutBound(Integer batchDLId,
			Integer transactionId, Integer fromStatusId, Integer toStatusId)
			throws Exception {
		 transactionOutDAO.updateTransactionTargetStatusOutBound(batchDLId, transactionId,fromStatusId , toStatusId);
		
	}
	
	
	
	/** 
     * The 'SendSoapMessage' function will get the WS details and move the file to the
     * output folder defined in 
     *
     * @param batchId The id of the batch to move to archivesOut folder
     */
    private Integer SendWSMessage(int batchId, configurationTransport transportDetails)  {

        try {

            /* Update the status of the batch to locked */
            transactionOutDAO.updateBatchStatus(batchId, 22);

            List<transactionTarget> targets = transactionOutDAO.getTransactionsByBatchDLId(batchId);
            
            /** we reject is targets are more than one, web service should only be sending one message at a time **/
            if (targets.size() > 1) {
            	transactionOutDAO.updateBatchStatus(batchId, 30);
            	/**insert error**/
            	transactionInManager.insertProcessingError(20, null, batchId, null, null, null, null, false, true, "There should only be one message per web service batch.");
            	updateTargetTransasctionStatus(batchId, 33);
            	return null;
            }
        	
        	
            /** we lock targets **/
            if (!targets.isEmpty()) {

                for (transactionTarget target : targets) {

                    /* Need to update the uploaded batch status */
                    transactionInManager.updateBatchStatus(target.getbatchUploadId(), 22, "");

                    /* Need to update the uploaded batch transaction status */
                    transactionInManager.updateTransactionStatus(target.getbatchUploadId(), target.gettransactionInId(), 0, 37);

                    /* Update the downloaded batch transaction status */
                    transactionOutDAO.updateTargetTransasctionStatus(target.getbatchDLId(), 37);

                  //check inbound for clearing options
                    if (configurationTransportManager.getTransportDetails(transactionInManager.getTransactionDetails(target.gettransactionInId()).getconfigId()).getclearRecords()) {
                    	//we insert into clearAfterDelivery
                    	batchClearAfterDelivery cad = new batchClearAfterDelivery();
	                    cad.setBatchDLId(batchId);
	                    cad.setBatchUploadId(target.getbatchUploadId());
	                    cad.setTransactionInId(target.gettransactionInId());
	                    cad.setTransactionTargetId(target.getId());
	                    transactionInManager.saveBatchClearAfterDelivery(cad);
                    }
                }

            }

            /* get the batch details */
            batchDownloads batchDetails = transactionOutDAO.getBatchDetails(batchId);

            /* Get the WS Details  - do we need this? */
            configurationWebServiceFields wsDetails = configurationTransportManager.getTransWSDetailsPush(transportDetails.getId());

            // the file is in output folder already, we need to encrypt it and populate soap message
            
            //1. we read file
            
            fileSystem dir = new fileSystem();
            String filelocation = transportDetails.getfileLocation();
            filelocation = filelocation.replace("/bowlink/", "");
            dir.setDirByName(filelocation);
            
            File sourceFile = new File(dir.getDir() + batchDetails.getoutputFIleName());
            //get content
            String fileContent = filemanager.readTextFile(sourceFile.getAbsolutePath());
            
            
            //to get the original sender's email, we need to get the originalTargetId from TransactionIn and then look up the batchId
            List <String> emails = getWSSenderFromBatchDLId((Arrays.asList(batchId)));
            
            wsMessagesOut wsMessagesOut = new wsMessagesOut();
            wsMessagesOut.setOrgId(batchDetails.getOrgId());
            //String fileContent, String toEmail, String fromEmail, Integer batchId
            wsMessagesOut.setFromEmail(wsDetails.getEmail());
            wsMessagesOut.setToEmail(emails.get(0));
            wsMessagesOut.setBatchDownloadId(batchId);
            wsMessagesOut.setMimeType(wsDetails.getMimeType());
            wsMessagesOut = wsManager.sendHIESoapMessage(wsMessagesOut, fileContent);
            
            String result = wsMessagesOut.getMessageResult();
            
            
        	if (result.equalsIgnoreCase("success")) {
        		transactionOutDAO.updateBatchStatus(batchId, 23);
        	} else {
        		transactionOutDAO.updateBatchStatus(batchId, 30);
        		transactionInManager.insertProcessingError(21, null, batchId, null, null, null, null, false, true, "Web Service Message Failed to send.");
            	updateTargetTransasctionStatus(batchId, 33);
            	return null;
        		
        	}
            for (transactionTarget target : targets) {

                /* Need to update the uploaded batch status */
                transactionInManager.updateBatchStatus(target.getbatchUploadId(), 23, "");

                /* Need to update the uploaded batch transaction status */
                transactionInManager.updateTransactionStatus(target.getbatchUploadId(), target.gettransactionInId(), 0, 20);

                /* Update the downloaded batch transaction status */
                transactionOutDAO.updateTargetTransasctionStatus(target.getbatchDLId(), 20);

            }
            
        } catch (Exception ex) {
        	try{
        		transactionInManager.sendEmailToAdmin(("SendWSMessage - Error occurred trying to move a batch target. batchId: " + batchId + "<br/>"+  ex.toString() + "<br/>" + Arrays.toString(ex.getStackTrace())), " SendWSMessage Error ");
        	} catch (Exception e) {
        		e.printStackTrace();
                System.err.println("SendWSMessage - Error occurred trying to move a batch target. batchId: " + batchId);
        	}
        	ex.printStackTrace();
            System.err.println("SendWSMessage - Error occurred trying to move a batch target. batchId: " + batchId);
            return null;
        }
        return 1;
    }

	@Override
	public List <String> getWSSenderFromBatchDLId(List<Integer> batchDLIds) throws Exception {
		return transactionOutDAO.getWSSenderFromBatchDLId(batchDLIds);
	}
	
	
	@Override
	public List<transactionTarget> getTTByStatusId(int batchId,
			List<Integer> statusIds) throws Exception {
		return transactionOutDAO.getTTByStatusId(batchId, statusIds);
	}

	@Override
	public List<Integer> getTargetConfigsForBatch(int batchId,
			List<Integer> statusIds) throws Exception {
		return transactionOutDAO.getTargetConfigsForBatch(batchId, statusIds);
	}

	@Override
	public Integer writeOutputToTextFile(configurationTransport transportDetails, Integer batchDownLoadId, String filePathAndName, String fieldNos) throws Exception { 
				return transactionOutDAO.writeOutputToTextFile(transportDetails, batchDownLoadId, filePathAndName, fieldNos);
	}
	
	@Override
	public void updateTTBatchIdByUploadBatch(Integer batchUploadId,
			Integer batchDownloadId) throws Exception {
		 	transactionOutDAO.updateTTBatchIdByUploadBatch(batchUploadId, batchDownloadId);
	}
	
	@Override
	public String generateDLBatchName(configurationTransport transportDetails, configuration configDetails,
			batchUploads batchUploadDetails, Date date) throws Exception {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
        
		String batchName = "";
		String sourceFileName = batchUploadDetails.getoriginalFileName();
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
        
        return batchName;
	}
	
	
	@Override
	public void  massInsertSummaryEntry(batchDownloads batchDowloadDetails, configuration configDetails, batchUploads batchUploadDetails) throws Exception {
		transactionOutDAO.massInsertSummaryEntry(batchDowloadDetails, configDetails, batchUploadDetails);
	}
	
	@Override
	public void setUpTransactionTranslatedOut (
			batchDownloads batchDowloadDetails, configuration configDetails, Integer statusId)  throws Exception{
		transactionOutDAO.setUpTransactionTranslatedOut(batchDowloadDetails, configDetails, statusId);
	}
	
	@Override
	public List <ConfigOutboundForInsert> setConfigOutboundForInsert(int configId, int batchDownloadId) throws Exception {
		return transactionOutDAO.setConfigOutboundForInsert(configId, batchDownloadId);
	}
	
	
	@Override
	public void massUpdateTTO(ConfigOutboundForInsert outboundConfig, Integer statusId) throws Exception {
		 transactionOutDAO.massUpdateTTO(outboundConfig, statusId);
	}
	
	@Override
	public String getConfigFieldsForOutput(Integer configId) throws Exception {
		return transactionOutDAO.getConfigFieldsForOutput(configId);
	}
	
	@Override
	public void insertFailedRequiredFields(configurationFormFields cff, Integer batchDLId, Integer transactionTargetId)
	throws Exception {
		transactionOutDAO.insertFailedRequiredFields(cff, batchDLId, transactionTargetId);
	}
	
	
	@Override
	public void moveTranslatedRecordsByBatch(int batchDownloadId) throws Exception{
		transactionOutDAO.moveTranslatedRecordsByBatch(batchDownloadId);
	}
	
	
	
	
	@Override
    public void runValidations(Integer batchDownloadId, Integer configId, Integer transactionId) throws Exception {
        //1. we get validation types
        //2. we skip 1 as that is not necessary
        //3. we skip date (4) as there is no isDate function in MySQL
        //4. we skip the ids that are not null as Mysql will bomb out checking character placement
        //5. back to date, we grab transaction info and we loop (errId 7)

        /**
         * MySql RegEXP validate numeric - ^-?[0-9]+[.]?[0-9]*$|^-?[.][0-9]+$ validate email - ^[a-z0-9\._%+!$&*=^|~#%\'`?{}/\-]+@[a-z0-9\.-]+\.[a-z]{2,6}$ or ^[A-Z0-9._%-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$ validate url - ^(https?://)?([\da-z.-]+).([a-z0-9])([0-9a-z]*)*[/]?$ - need to fix not correct - might have to run in java as mysql is not catching all. validate phone - should be no longer than 11 digits ^[0-9]{7,11}$ validate date - doing this in java
         *
         */
        //TODO was hoping to have one SP but concat in SP not setting and not catching errors correctly. Need to recheck
      
		List<configurationFormFields> configurationFormFields
                = configurationTransportManager.getCffByValidationType(configId, 0);

        for (configurationFormFields cff : configurationFormFields) {
            String regEx = "";
            Integer validationTypeId = cff.getValidationType();
            switch (cff.getValidationType()) {
                case 1:
                    break; // no validation
                //email calling SQL to validation and insert - one statement
                case 2:
                     genericValidation(cff, validationTypeId, batchDownloadId, regEx, transactionId);
                    break;
                //phone  calling SP to validation and insert - one statement 
                case 3:
                    genericValidation(cff, validationTypeId, batchDownloadId, regEx, transactionId);
                    break;
                // need to loop through each record / each field
                case 4:
                    dateValidation(cff, validationTypeId, batchDownloadId, transactionId);
                    break;
                //numeric   calling SQL to validation and insert - one statement      
                case 5:
                    genericValidation(cff, validationTypeId, batchDownloadId, regEx, transactionId);
                    break;
                //url - need to rethink as regExp is not validating correctly
                case 6:
                    urlValidation(cff, validationTypeId, batchDownloadId, transactionId);
                    break;
                //anything new we hope to only have to modify sp
                default:
                    genericValidation(cff, validationTypeId, batchDownloadId, regEx, transactionId);
                    break;
            }
           

        } 
    }

    @Override
    public void genericValidation(configurationFormFields cff,
            Integer validationTypeId, Integer batchDownloadId, String regEx, Integer transactionId) throws Exception {
        	transactionOutDAO.genericValidation(cff, validationTypeId, batchDownloadId, regEx, transactionId);
    }

    @Override
    public void urlValidation(configurationFormFields cff,
            Integer validationTypeId, Integer batchDownloadId, Integer transactionId) throws Exception{
        
            //1. we grab all transactionInIds for messages that are not length of 0 and not null 
            List<transactionRecords> trs = null;
            //1. we grab all transactionInIds for messages that are not length of 0 and not null 
            if (transactionId == 0) {
                trs = getFieldColAndValues(batchDownloadId, cff);
            } else {
                trs = getFieldColAndValueByTransactionId(cff, transactionId);
            }
            //2. we look at each column and check each value to make sure it is a valid url
            for (transactionRecords tr : trs) {
                //System.out.println(tr.getfieldValue());
                if (tr.getfieldValue() != null) {
                	 if (tr.getfieldValue().length() != 0) {
                    //we append http:// if url doesn't start with it
                    String urlToValidate = tr.getfieldValue();
                    if (!urlToValidate.startsWith("http")) {
                        urlToValidate = "http://" + urlToValidate;
                    }
                    if (!transactionInManager.isValidURL(urlToValidate)) {
                        insertValidationError(tr, cff, batchDownloadId);
                    }
                }
                }
            }
    }
    
    @Override
    public List<transactionRecords> getFieldColAndValueByTransactionId(configurationFormFields cff,
            Integer transactionId) throws Exception {
        return transactionOutDAO.getFieldColAndValueByTransactionId(cff, transactionId);
    }
    
    @Override
    public List<transactionRecords> getFieldColAndValues(Integer batchDownloadId, configurationFormFields cff) throws Exception {
        return transactionOutDAO.getFieldColAndValues(batchDownloadId, cff);
    }

    @Override
    public void updateFieldValue(transactionRecords tr, String newValue) throws Exception{
        transactionOutDAO.updateFieldValue(tr, newValue);
    }
    
    @Override
    public void insertValidationError(transactionRecords tr,
            configurationFormFields cff, Integer batchUploadId) throws Exception {
        transactionOutDAO.insertValidationError(tr, cff, batchUploadId);
    }
    
    
	@Override
	public void updateErrorStatusForTT(Integer batchDownloadId,
			Integer newStatusId, Integer transactionTargetId) throws Exception{
		transactionOutDAO.updateErrorStatusForTT(batchDownloadId, newStatusId, transactionTargetId);
	}
    
    
    @Override
    public void dateValidation(configurationFormFields cff, Integer validationTypeId, Integer batchDownloadId, Integer transactionId) throws Exception {
       
            List<transactionRecords> trs = null;
            //1. we grab all transactionInIds for messages that are not length of 0 and not null 
            if (transactionId == 0) {
                trs = getFieldColAndValues(batchDownloadId, cff);
            } else {
                trs = getFieldColAndValueByTransactionId(cff, transactionId);
            }
            //2. we look at each column and check each value by trying to convert it to a date
            for (transactionRecords tr : trs) {
                if (tr.getfieldValue() != null) {
                	 if (tr.getfieldValue().length() != 0) {
                    //sql is picking up dates in mysql format and it is not massive inserting, running this check to avoid unnecessary sql call
                    //System.out.println(tr.getFieldValue());
                    //we check long dates
                    Date dateValue = null;
                    String mySQLDate = transactionInManager.chkMySQLDate(tr.getFieldValue());

                    if (dateValue == null && mySQLDate.equalsIgnoreCase("")) {
                        dateValue = transactionInManager.convertLongDate(tr.getFieldValue());
                    }
                    if (dateValue == null && mySQLDate.equalsIgnoreCase("")) {
                        dateValue = transactionInManager.convertDate(tr.getfieldValue());
                    }

                    String formattedDate = null;
                    if (dateValue != null && mySQLDate.equalsIgnoreCase("")) {
                        formattedDate = transactionInManager.formatDateForDB(dateValue);
                        //3. if it converts, we update the column value
                        updateFieldValue(tr, formattedDate);
                    }

                    if (formattedDate == null && (mySQLDate.equalsIgnoreCase("") || mySQLDate.equalsIgnoreCase("ERROR"))) {
                        insertValidationError(tr, cff, batchDownloadId);
                    }
                 }
                }
            }
    }
    
   
    /** 
	 * this will select an upload batch that has status of 24, check its config to make sure it is for 
	 * mass translation and start translating
	 * configuration that is for mass translation
	 */
	@Override
	public void processMassOutputBatches() throws Exception {
		//get all inbound batches with mass translation = true
		List<batchUploads> inboundBatches = transactionInManager.getMassTranslateBatchForOutput(1);
			//we lock the batch so it won't get picked up again
		for (batchUploads inboundBatch : inboundBatches) {
			transactionInManager.updateBatchStatus(inboundBatch.getId(), 25, "");
			processMassOutputBatch(inboundBatch.getId());
		}
	}
	
	
	@Override
    public void clearTransactionTranslatedOutByBatchId(Integer batchDownloadId) throws Exception {
        transactionOutDAO.clearTransactionTranslatedOutByBatchId(batchDownloadId);
    }
    

	@Override
	public Integer processMassOutputBatch(Integer batchUploadId) throws Exception {
				//we get the configs for the file
				List<Integer> configIds = getTargetConfigsForBatch(batchUploadId, Arrays.asList(19));
				
				//there should be only one - we reject if there is more than one config
				if (configIds.size() != 1) {
					//error out the transactions
					transactionInManager.updateTransactionTargetStatus(batchUploadId, 0, 0, 33);
					
					//we leave transaction in status alone in case admin wants to fix configs and reset batch status by hand
					//transactionInManager.updateTransactionStatus(batchUploadId, 0, 0, 33);
					
					//error out the inbound batch
					transactionInManager.updateBatchStatus(batchUploadId, 30, "endDateTime");
					//insert error 
					transactionInManager.insertProcessingError(30, null, batchUploadId, null, null, null, null, false, false, "Multiple target configurations found for inbound mass translation batch", 0);
                    //email admin
                    transactionInManager.sendEmailToAdmin(("Multiple targets configs were found for inbound batch - " + batchUploadId), "Mass translation config error");
               
					return 1;
					
				}
				
				//we give it a name	
				/* Create the batch name (OrgId+MessageTypeId+Date/Time) - need milliseconds as computer is fast and files have the same name*/
		        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
		        Date date = new Date();
		        
		        //get transport & config details
		        configuration configDetails = configurationManager.getConfigurationById(configIds.get(0));
		        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configDetails.getId());
		        
		        //we get upload details
				batchUploads batchUploadDetails = transactionInManager.getBatchDetails(batchUploadId);
				
		        String utbatchName = new StringBuilder().append(transportDetails.gettransportMethodId()).append("_m_").append(configDetails.getorgId()).append(configDetails.getMessageTypeId()).append(dateFormat.format(date)).toString();
		        
		        /* Get the connection id for the configuration */
		        List<configurationConnection> connections = configurationManager.getConnectionsByTargetConfiguration(configDetails.getId());

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
		        
		        
		        //we create a batchDownloads
				batchDownloads batchDownload  = new batchDownloads();
				
		        //set batch download details
		        batchDownload.setutBatchName(utbatchName);
		        //set it to interim status
		        batchDownload.setstatusId(25);
				
				//we determine output file name
				batchDownload.setoutputFIleName(generateDLBatchName(transportDetails, configDetails, batchUploadDetails, date) + "." + transportDetails.getfileExt());
				batchDownload.setmergeable(false);
				batchDownload.setstartDateTime(new Date());
				batchDownload.settransportMethodId(transportDetails.gettransportMethodId());
				batchDownload.setOrgId(configDetails.getorgId());
				batchDownload.setuserId(userId);
		        batchDownload.settotalErrorCount(0);
		        batchDownload.settotalRecordCount(1);
		        batchDownload.setdeleted(false);
		        batchDownload.setdateCreated(new Date());
		        /* Submit a new batch */
		        int batchDLId = (int) transactionOutDAO.submitBatchDownload(batchDownload);
		        batchDownload.setId(batchDLId);
		        
		        /* Need to update the target transaction with new  batch Id, file only contains good records*/
		        transactionOutDAO.updateTTBatchIdByUploadBatch(batchUploadId, batchDLId);
		        
		        /* Need to submit the batch summary for transactionTarget with statusId = 19 */
		        //mass insert 
		        massInsertSummaryEntry(batchDownload, configDetails, batchUploadDetails);
		        
		        Integer statusId = 37;
		        //we set the transactionTargets for this config in batch to Load 37 
		        updateTransactionTargetStatusOutBound(batchDLId,0, 19, statusId);
		        
		        
		        // we set up transactionTranslatedOut
		        setUpTransactionTranslatedOut(batchDownload,configDetails, statusId);
		        
		        //we look up config info and insert transactions into transactionTranslatedOut
		        List<ConfigOutboundForInsert> setConfigOutboundForInsertList = setConfigOutboundForInsert(configDetails.getId(),  batchDLId);
		        
		        //we loop the configuration and insert into transactionTranslatedOut
		        for (ConfigOutboundForInsert outboundConfig : setConfigOutboundForInsertList) {
		        	massUpdateTTO(outboundConfig, statusId);
		        }
		        
			        Integer transactionId = 0;
			        //we run translations, macros and crosswalks
		            Integer configId = configDetails.getId();
	                //we need to run all checks before insert regardless *
	                
		            /**
	                 * we are reordering 1. cw/macro, 2. required and 3. validate *
	                 */
		       		translateTargetRecords(0, configId, batchDLId, 1);

	                //check R/O
	                List<configurationFormFields> reqFields = transactionInManager.getRequiredFieldsForConfig(configId);

	                for (configurationFormFields cff : reqFields) {
	                		insertFailedRequiredFields(cff, batchDLId, transactionId);
	                }
	                
	                // update status of the failed records to ERR - 14
	                updateErrorStatusForTT(batchDLId, 14,  transactionId);

	                //run validation
	                runValidations(batchDLId, configId, transactionId);
	                
	                // update status of the failed records to ERR - 14
	                updateErrorStatusForTT(batchDLId, 14,  transactionId);
	                
	                //we do not release the batch if there are outbound errors - everything should be in final status
	                // now we are done with processing we set the 37 records to 18
					//we update status of these transactions from 37 to 18
                    updateTransactionTargetStatusOutBound(batchDLId,0, statusId, 18);
					
	                
	                if (transactionInManager.getRecordCounts(batchDLId, transRELId,  true, false) > 0) {
	                	//error out batch
	                	transactionInManager.updateBatchStatus(batchUploadId, 41, "");
	                    transactionOutDAO.updateTargetBatchStatus(batchDLId, 41, "endDateTime");
	                	return 1;
	                } else {
	                
	                //we are not copying to transactionOutRecords 
	                	//moveTranslatedRecordsByBatch(batchDLId);
		           
	                /* Generate the file according to transportDetails 
	                 * 1. we generate output file according to encoding in transportDetails
	                 * 2. we always save an encrypted copy to archivesOut
	                 * */
			       		
			       		
			       		//get list of config fields here
			       		
			       		 String configFields = getConfigFieldsForOutput(configId);
			       		 //we move the file extension
				       	 String fileExt = transportDetails.getfileExt();
		            		
				       	 boolean encryptMessage = false;
		                 // we only support base64 for now
		                 if (transportDetails.getEncodingId() == 2) {        
		                     encryptMessage = true;
		             	}
		                /** **/
			       		String generatedFilePath = generateTargetFile(true, 0, batchDLId, transportDetails, encryptMessage);       
			       		transportDetails.setDelimChar(messageTypeDAO.getDelimiterChar(transportDetails.getfileDelimiter()));
			       		//make sure we remove old file
			       		File generatedFile = new File(generatedFilePath);
			       		if (generatedFile.exists()) {
			       			generatedFile.delete();
	            		}
	            		
			       		
			       		/**
			       		mysql is the fastest way to output a file, but the permissions are tricky
			      		we write to massoutfiles where both tomcat and mysql has permission. 
			      		Then we can create, copy and delete
			      		**/
			       		fileSystem fileSystemOutput = new fileSystem();
                    	File massOutFile = new File ( fileSystemOutput.setPathFromRoot(massOutPutPath) + batchDownload.getutBatchName()+ "." + fileExt);
                    	//check to see if file is there, if so remove old file
                    	if (massOutFile.exists()) {
                    		massOutFile.delete();
                		}
			       		
			       		//writeOutputToTextFile(transportDetails, batchDLId, massOutFile.getAbsolutePath(), configFields);
			       		
                    	//here we need to pass to mysql method the db path from mysql which is massOutPutPathMysqlPath
			       		Integer writeOutCome = writeOutputToTextFile(transportDetails, batchDLId, (massOutPutPathMysqlPath + batchDownload.getutBatchName()+ "." + fileExt), configFields);
			       		if (!massOutFile.exists()) {
			       			//we induce time because file is not done writing
			       			System.out.println("in time delay one");
			       			TimeUnit.SECONDS.sleep(30);
			       		}
			       		//check one more time to be safe
			       		if (!massOutFile.exists()) {
			       			//we induce time because file is not done writing
			       			System.out.println("in time delay two");
			       			TimeUnit.SECONDS.sleep(30);
			       		}
			       		//cp file to archiveOut and correct putput folder
	                    fileSystem fileSystem = new fileSystem();
		                    	File archiveFile = new File ( fileSystem.setPathFromRoot(archivePath) + batchDownload.getutBatchName()+ "." + fileExt);
	                    	
	                    		//at this point, message it not encrypted
	                    		//we always encrypt the archive file
	                    		String strEncodedFile = filemanager.encodeFileToBase64Binary(massOutFile);
	                    		if (archiveFile.exists()) {
	                    			archiveFile.delete();
	                    		}
	                    		//write to archive folder
	                    		filemanager.writeFile(archiveFile.getAbsolutePath(), strEncodedFile);
	                    		
	                    		// if we don't need to encrypt file for users to download
	                    		if (!encryptMessage)  {
	                    			Files.copy(massOutFile.toPath(), generatedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	                    		} else { //we copy the encrypted file over
	                    			Files.copy(archiveFile.toPath(), generatedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	                    		}   
	                    		//we have file already, we just need to move it
	                    		if (transportDetails.gettransportMethodId() == 5) {
	                    			/* Get the Rhapsody Details */
	                                configurationRhapsodyFields rhapsodyDetails = configurationTransportManager.getTransRhapsodyDetailsPush(transportDetails.getId());

	                                // the file is in output folder already, we need to rebuild path and move it
	                                
	                                fileSystem dir = new fileSystem();
	                                String filelocation = transportDetails.getfileLocation();
	                                filelocation = filelocation.replace("/bowlink/", "");
	                                dir.setDirByName(filelocation);
	                                
	                                File targetFile = new File ( directoryPath + rhapsodyDetails.getDirectory() + batchDownload.getoutputFIleName());
	                                Files.copy(archiveFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	                    		}
	                    		
	                    		//now we delete massoutput file
		                    	massOutFile.delete();
	                    	}
	                    
	                		//we need to update status
	                		transactionInManager.updateTransactionStatus(batchUploadId, 0, 19, 40);
	                		updateTransactionTargetStatusOutBound(batchDLId, 0, 18, 40);
	                
	                
	                		
	                		//we need to update our totals
                			transactionInManager.updateRecordCounts(batchDownload.getId(), rejectIds, true, "totalErrorCount");
                			transactionInManager.updateRecordCounts(batchDownload.getId(), new ArrayList<Integer>(), true, "totalRecordCount");
                   
	                    //now we delete the data from transactionTranslatedIn, transactionTranslatedOut, transactionInRecords
	                    //we will schedule a job to delete from message tables as it takes too long if we are not adding batchId there
	                    
                		clearTransactionTranslatedOutByBatchId(batchDLId);
	                    transactionInManager.clearTransactionInRecords(batchUploadId, 0);
	                    transactionInManager.clearTransactionTranslatedIn(batchUploadId, 0);
	                    
	                    //we insert into batchMassTranslate so we can run it as a job and delete
	                    /** do not want to add batchId to every message table and the delete will be slow **/
	                    //if the inbound configuration is set to Clear Records after Delivery , we clear message tables
	                    if (configurationTransportManager.getTransportDetails(batchUploadDetails.getConfigId()).getclearRecords()) {
		                    batchClearAfterDelivery bmt = new batchClearAfterDelivery();
		                    bmt.setBatchDLId(batchDLId);
		                    bmt.setBatchUploadId(batchUploadId);
		                    transactionInManager.saveBatchClearAfterDelivery(bmt);
	                    }
	                    //we update status of batch
	                    transactionInManager.updateBatchStatus(batchUploadId, 28, "");
	                    transactionOutDAO.updateTargetBatchStatus(batchDLId, 28, "endDateTime");
					
	                    
						return 0;
	                }

}








