/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

import com.ut.dph.model.Organization;
import com.ut.dph.model.Provider;
import com.ut.dph.model.Transaction;
import com.ut.dph.model.User;
import com.ut.dph.model.batchDownloadSummary;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.batchUploadSummary;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.custom.searchParameters;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.model.providerAddress;
import com.ut.dph.model.providerIdNum;
import com.ut.dph.model.transactionAttachment;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import com.ut.dph.model.transactionOutNotes;
import com.ut.dph.model.transactionOutRecords;
import com.ut.dph.model.transactionRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.service.organizationManager;
import com.ut.dph.service.providerManager;
import com.ut.dph.service.sysAdminManager;
import com.ut.dph.service.transactionInManager;
import com.ut.dph.service.transactionOutManager;
import com.ut.dph.service.userManager;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author chadmccue
 */
@Controller
@RequestMapping("/Health-e-Web")
public class HealtheWebController {
    
    @Autowired
    private configurationManager configurationManager;
    
    @Autowired
    private messageTypeManager messagetypemanager;
    
    @Autowired
    private organizationManager organizationmanager;
    
    @Autowired
    private configurationTransportManager configurationTransportManager;
    
    @Autowired
    private transactionInManager transactionInManager;
    
    @Autowired
    private transactionOutManager transactionOutManager;
    
    @Autowired
    private sysAdminManager sysAdminManager;
    
    @Autowired
    private userManager usermanager;
    
    @Autowired
    private providerManager providermanager;
    
    private int inboxTotal = 0;
    private int pendingTotal = 0;
    
    /**
     * The private maxResults variable will hold the number of results to show per list page.
     */
    private static int maxResults = 20;
    
    /**
     * The 'findTotals' function will set the total number of
     * inbox messages and total number of pending messages 
     */
    public void setTotals(HttpSession session) throws Exception {
        
        User userInfo = (User)session.getAttribute("userDetails");
        
        /* Need to get a list of all pending batches */
        pendingTotal = transactionInManager.getpendingBatches(userInfo.getId(), userInfo.getOrgId(), null, null).size();
        
        /* Need to get a list of all inbox batches */
        try {
            inboxTotal = transactionOutManager.getInboxBatches(userInfo.getId(), userInfo.getOrgId(), null, null).size();
        }
        catch (Exception e) {
            throw new Exception("Error obtaining the inbox total badge number", e);
        }
        
    }
    
    
    /**
     * The '/inbox' request will serve up the Health-e-Web (ERG) inbox.
     *
     * @param request
     * @param response
     * @return	the health-e-web inbox  view
     * @throws Exception
     */
    @RequestMapping(value = "/inbox", method = RequestMethod.GET)
    public ModelAndView viewinbox(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/inbox");
        
        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");
        
        if("".equals(searchParameters.getsection()) || !"inbox".equals(searchParameters.getsection())) {
           searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setpage(1);
            searchParameters.setsection("inbox");
            searchParameters.setsearchTerm("");
        }
        else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
        }
            
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        
        try {
            List<batchDownloads> inboxBatches = transactionOutManager.getInboxBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate);
            
            if(!inboxBatches.isEmpty()) {
                for(batchDownloads batch : inboxBatches) {
                    List<transactionTarget> batchTransactions = transactionOutManager.getInboxBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());
                    
                    /* Get the details of the sender */
                   batchDownloadSummary downloadSummaryDetails = transactionOutManager.getDownloadSummaryDetails(batchTransactions.get(0).getId());
                   int senderOrgId = downloadSummaryDetails.getsourceOrgId();
                   int senderUserId = transactionInManager.getBatchDetails(batchTransactions.get(0).getbatchUploadId()).getuserId();
                    
                   Organization orgDetails = organizationmanager.getOrganizationById(senderOrgId);
                   User userDetails = usermanager.getUserById(senderUserId);
                   
                   String senderDetails = new StringBuilder()
                           .append(orgDetails.getOrgName())
                           .append("<br />")
                           .append(orgDetails.getAddress()).append(" ").append(orgDetails.getAddress2())
                           .append("<br />")
                           .append(orgDetails.getCity()).append(" ").append(orgDetails.getState()).append(",").append(orgDetails.getPostalCode())
                           .append("<br />")
                           .append("(User:").append(" ").append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).append(")").toString();
                   
                   batch.setusersName(senderDetails);
                }
            }

            mav.addObject("inboxBatches", inboxBatches);

            /* Set the header totals */
            setTotals(session);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal);

        }
        catch (Exception e) {
            throw new Exception("Error returning batches for the inbox", e);
        }
        
        return mav;
    }
    
    
   /**
     * The '/inbox' POST request will serve up the Health-e-Web (ERG) page that will list all inbox
     * messages based on the term searched for.
     *
     * @param request
     * @param response
     * * @param searchTerm The term to search pending messages
     * @return	the health-e-web inbox message list view
     * @throws Exception
     */
    @RequestMapping(value = "/inbox", method = RequestMethod.POST)
    public ModelAndView findInboxBatches(@RequestParam Date fromDate, @RequestParam Date toDate, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
       
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/inbox");
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setsection("inbox");
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            /* Need to get a list of all pending batches */
            Integer totalInboxBatches = transactionOutManager.getInboxBatches(userInfo.getId(), userInfo.getOrgId(), null, null).size();
            List<batchDownloads> inboxBatches = transactionOutManager.getInboxBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate);

            if(!inboxBatches.isEmpty()) {
                for(batchDownloads batch : inboxBatches) {
                    List<transactionTarget> batchTransactions = transactionOutManager.getInboxBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    /* Get the details of the sender */
                    batchDownloadSummary downloadSummaryDetails = transactionOutManager.getDownloadSummaryDetails(batchTransactions.get(0).getId());
                    int senderOrgId = downloadSummaryDetails.getsourceOrgId();
                    int senderUserId = transactionInManager.getBatchDetails(batchTransactions.get(0).getbatchUploadId()).getuserId();
                    
                    Organization orgDetails = organizationmanager.getOrganizationById(senderOrgId);
                    User userDetails = usermanager.getUserById(senderUserId);
                   
                    String senderDetails = new StringBuilder()
                           .append(orgDetails.getOrgName())
                           .append("<br />")
                           .append(orgDetails.getAddress()).append(" ").append(orgDetails.getAddress2())
                           .append("<br />")
                           .append(orgDetails.getCity()).append(" ").append(orgDetails.getState()).append(",").append(orgDetails.getPostalCode())
                           .append("<br />")
                           .append("(User:").append(" ").append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).append(")").toString();
                   
                    batch.setusersName(senderDetails);
                }
            }

            mav.addObject("inboxBatches", inboxBatches); 

            /* Set the header totals */
            setTotals(session);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal);
        }
        catch (Exception e) {
            throw new Exception ("Error in the inbox search", e);
        }
        
        return mav;
    }
    
    
    /**
     * The 'batch/inboxTransactions' request will display a page that will show all transactions
     * associated with the clicked inbox batch.
     * 
     * @param batchId   The id of the clicked batch
     * @param session   The session scope variables
     * 
     * @return This method will return the page to display all transactions for the batch.
     */
    @RequestMapping(value = "inbox/batch/Transactions", method = RequestMethod.POST)
    public ModelAndView showInboxBatchTransactions(@RequestParam Integer batchId, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/batchTransactions");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            /* Get the details of the batch */
            batchDownloads batchDetails = transactionOutManager.getBatchDetails(batchId);
            mav.addObject("batchDetails", batchDetails);

            /* Get all the transactions for the batch */
            List<transactionTarget> batchTransactions = transactionOutManager.getInboxBatchTransactions(batchId, userInfo.getId());

            List<Transaction> transactionList = new ArrayList<Transaction>();

            for(transactionTarget transaction : batchTransactions) {

                Transaction transactionDetails = new Transaction();
                transactionDetails.settransactionRecordId(transaction.getId());
                transactionDetails.setstatusId(transaction.getstatusId());
                transactionDetails.setdateSubmitted(transaction.getdateCreated());
                transactionDetails.setconfigId(transaction.getconfigId());

                lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(transaction.getstatusId());
                transactionDetails.setstatusValue(processStatus.getDisplayCode());

                transactionOutRecords records = transactionOutManager.getTransactionRecords(transaction.getId());

                /* Get a list of form fields */
                configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transaction.getconfigId());
                
                try {
                    List<configurationFormFields> sourceInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),1);
                    /* Set all the transaction SOURCE ORG fields */
                    
                    /* Set all the transaction SOURCE ORG fields */
                    List<transactionRecords> fromFields;
                    if(!sourceInfoFormFields.isEmpty()) {
                        fromFields = setInboxFormFields(sourceInfoFormFields, records, 0, true, 0);
                    }
                    else {
                        fromFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transaction.getId()).getsourceOrgId());
                    }
                    transactionDetails.setsourceOrgFields(fromFields);
                    
                }
                catch (Exception e) {
                    throw new Exception ("Error retrieving source fields for configuration id: "+ transaction.getconfigId(), e);
                }
                
                try {
                   List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),3);
                    /* Set all the transaction TARGET fields */
                    List<transactionRecords> toFields = setInboxFormFields(targetInfoFormFields, records, 0, true, 0);
                    transactionDetails.settargetOrgFields(toFields);
                }
                catch (Exception e) {
                    throw new Exception ("Error retrieving target fields for configuration id: "+ transaction.getconfigId(), e);
                }
                
                try {
                    List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),5);
                    /* Set all the transaction PATIENT fields */
                    List<transactionRecords> patientFields = setInboxFormFields(patientInfoFormFields, records, 0, true, 0);
                    transactionDetails.setpatientFields(patientFields);
                }
                catch (Exception e) {
                    throw new Exception ("Error retrieving patient fields for configuration id: "+ transaction.getconfigId(), e);
                }
                
                try {
                  List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),6);
                  /* Set all the transaction DETAIL fields */
                  List<transactionRecords> detailFields = setInboxFormFields(detailFormFields, records, 0, true, 0);
                  transactionDetails.setdetailFields(detailFields);
                }
                catch (Exception e) {
                    throw new Exception ("Error retrieving detail fields for configuration id: "+ transaction.getconfigId(), e);
                }
                
                /* get the message type name */
                configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                transactionList.add(transactionDetails);
            }

            mav.addObject("transactions", transactionList);
            mav.addObject("fromPage", "inbox");

            /* Set the header totals */
            setTotals(session);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal); 
        }
        catch (Exception e) {
            throw new Exception("Error in the returning transactions for the batch: "+ batchId, e);
        }
        
        return mav;
        
    }
    
    
    /**
     * The '/inbox/messageDetails' POST request will display the selected transaction details. This page is 
     * served up from inbox batch transaction list page. So the form will be readOnly.
     * 
     * @param transactionId  The id of the selected transaction
     * @param fromPage       The page the request is coming from (inbox) 
     * 
     * @return this request will return the messageDetailsForm
     */
    @RequestMapping(value="/inbox/messageDetails", method = RequestMethod.POST)
    public ModelAndView showInboxMessageDetails(@RequestParam(value = "transactionId", required = true) Integer transactionId, HttpSession session) throws Exception {
       
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/sentmessageDetails");
        
        try {
            transactionTarget transactionInfo = transactionOutManager.getTransactionDetails(transactionId);
        
            /* Get the details of the sent transaction */
            transactionIn originalTransactionInfo = transactionInManager.getTransactionDetails(transactionInfo.gettransactionInId());

            /* Need to update the status of the transaction to Recieved (id=20) */
            transactionOutManager.changeDeliveryStatus(transactionInfo.getbatchDLId(), transactionInfo.getbatchUploadId(), transactionId, transactionInfo.gettransactionInId());

            /* Get the configuration details */
            configuration configDetails = configurationManager.getConfigurationById(transactionInfo.getconfigId());

            /* Get the organization details for the source (Sender) organization */
            User userInfo = (User)session.getAttribute("userDetails");

            /* Get a list of form fields */
            configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transactionInfo.getconfigId());
            
            Transaction transaction = new Transaction();
            transactionOutRecords records = null;

            batchDownloads batchInfo = transactionOutManager.getBatchDetails(transactionInfo.getbatchDLId());

            transaction.setorgId(batchInfo.getOrgId());
            transaction.settransportMethodId(2);
            transaction.setmessageTypeId(configDetails.getMessageTypeId());
            transaction.setuserId(batchInfo.getuserId());
            transaction.setbatchName(batchInfo.getutBatchName());
            transaction.setstatusId(batchInfo.getstatusId());
            transaction.settransactionStatusId(transactionInfo.getstatusId());
            transaction.setconfigId(transactionInfo.getconfigId());
            transaction.setautoRelease(transportDetails.getautoRelease());
            transaction.setbatchId(batchInfo.getId());
            transaction.settransactionId(transactionId);
            transaction.settransactionTargetId(transactionInfo.getId());
            transaction.setdateSubmitted(transactionInfo.getdateCreated());

            /* Check to see if the message is a feedback report */
            if(originalTransactionInfo.gettransactionTargetId() > 0) {
                transaction.setsourceType(2); /* Feedback report */
                transaction.setorginialTransactionId(transactionOutManager.getTransactionDetails(originalTransactionInfo.gettransactionTargetId()).gettransactionInId());
            }
            else {
                transaction.setsourceType(configDetails.getsourceType());
            }
            transaction.setinternalStatusId(transactionInfo.getinternalStatusId());

            lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(transaction.getstatusId());
            transaction.setstatusValue(processStatus.getDisplayCode());

            /* get the message type name */
            transaction.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

            records = transactionOutManager.getTransactionRecords(transactionId);
            transaction.settransactionRecordId(records.getId());
            
            try {
                List<configurationFormFields> senderInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),1);
                /* Set all the transaction SOURCE ORG fields */
                List<transactionRecords> fromFields;
                if(!senderInfoFormFields.isEmpty()) {
                    fromFields = setInboxFormFields(senderInfoFormFields, records, transactionInfo.getconfigId(), true, 0);
                }
                else {
                    fromFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transactionInfo.getId()).getsourceOrgId());
                }
                transaction.setsourceOrgFields(fromFields);
            
            }
            catch (Exception e) {
                throw new Exception ("Error retrieving sender fields for configuration id: "+ transactionInfo.getconfigId(), e);
            }
            
            try {
              List<configurationFormFields> senderProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),2);
              /* Set all the transaction SOURCE PROVIDER fields */
              List<transactionRecords> fromProviderFields = setInboxFormFields(senderProviderFormFields, records, transactionInfo.getconfigId(), true, 0);
              transaction.setsourceProviderFields(fromProviderFields);
              
            }
            catch (Exception e) {
                throw new Exception ("Error retrieving sender provider fields for configuration id: "+ transactionInfo.getconfigId(), e);
            }
            
            try {
                List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),3);
                /* Set all the transaction TARGET fields */
                
                List<transactionRecords> toFields;
                if(!targetInfoFormFields.isEmpty()) {
                    toFields = setInboxFormFields(targetInfoFormFields, records, transactionInfo.getconfigId(), true, 0);
                    
                    if("".equals(toFields.get(0).getFieldValue()) || toFields.get(0).getFieldValue() == null) {
                        toFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transactionInfo.getId()).gettargetOrgId());
                    }
                }
                else {
                    toFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transactionInfo.getId()).gettargetOrgId());
                }
                transaction.settargetOrgFields(toFields);
            
            }
            catch (Exception e) {
                throw new Exception ("Error retrieving target fields for configuration id: "+ transactionInfo.getconfigId(), e);
            }
            
            try {
                List<configurationFormFields> targetProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),4);
                /* Set all the transaction TARGET PROVIDER fields */
                List<transactionRecords> toProviderFields = setInboxFormFields(targetProviderFormFields, records, transactionInfo.getconfigId(), true, 0);
                transaction.settargetProviderFields(toProviderFields);
            
            }
            catch (Exception e) {
                throw new Exception ("Error retrieving target provider fields for configuration id: "+ transactionInfo.getconfigId(), e);
            }
            
            try {
               List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),5);
               
               /* Set all the transaction PATIENT fields */
               List<transactionRecords> patientFields = setInboxFormFields(patientInfoFormFields, records, transactionInfo.getconfigId(), true, 0);
               transaction.setpatientFields(patientFields);
             
            }
            catch (Exception e) {
                throw new Exception ("Error retrieving patient fields for configuration id: "+ transactionInfo.getconfigId(), e);
            }
            
            try {
                List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),6);
                /* Set all the transaction DETAIL fields */
                List<transactionRecords> detailFields = setInboxFormFields(detailFormFields, records, transactionInfo.getconfigId(), true, 0);
                transaction.setdetailFields(detailFields);

            }
            catch (Exception e) {
                throw new Exception ("Error retrieving the detail fields for configuration id: "+ transactionInfo.getconfigId(), e);
            }
            
            mav.addObject("transactionDetails", transaction);

            /* Get the list of internal message status */
            List internalStatusCodes = transactionOutManager.getInternalStatusCodes();
            mav.addObject("internalStatusCodes",internalStatusCodes);

            /* Get id of the associated feedback report for this message */
            Integer feedbackConfigId = transactionOutManager.getActiveFeedbackReportsByMessageType(configDetails.getMessageTypeId(),userInfo.getOrgId());
            mav.addObject("feedbackConfigId", feedbackConfigId);

            /* Set the header totals */
            setTotals(session);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal);
            mav.addObject("fromPage", "inbox");
            mav.addObject("transactionInId", transactionInfo.gettransactionInId());
        }
        catch (Exception e) {
            throw new Exception("An error occurred in returning the details of the message, id: "+ transactionId, e);
        }
        
        
        
        return mav;
    }
    
    
    /**
     * The '/create' request will serve up the Health-e-Web (ERG) create message page.
     *
     * @param request
     * @param response
     * @return	the health-e-web create new message  view
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createNewMesage(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/create");
        
        /** Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 2);
        
        
            for (configuration config : configurations) {
                config.setMessageTypeName(messagetypemanager.getMessageTypeById(config.getMessageTypeId()).getName());

                /** Get a list of connections */
                List<configurationConnection> connections = configurationManager.getConnectionsByConfiguration(config.getId());

                for(configurationConnection connection : connections) {
                    configuration configDetails = configurationManager.getConfigurationById(connection.gettargetConfigId());
                    connection.settargetOrgName(organizationmanager.getOrganizationById(configDetails.getorgId()).getOrgName());
                    connection.settargetOrgId(configDetails.getorgId());
                }

                config.setconnections(connections);

            }

            mav.addObject("configurations", configurations);
        
            /* Set the header totals */
            setTotals(session);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal);
        }
        catch (Exception e) {
            throw new Exception("An error occurred in returning a list of message types for user " + userInfo.getId(), e);
        }
       
        
        return mav;
    }
    
    
    /**
     * The '/feedbackReport/details' POST request will display the feedback message form
     * 
     * @param configId      The selected feedback report configuration
     * @param transactionId The id of the transaction the feedback report will be for
     * 
     * @return this request will return the messageDetailsForm
     */
    @RequestMapping(value="/feedbackReport/details", method = RequestMethod.POST)
    public ModelAndView showfeedbackReportDetailsForm(@RequestParam(value = "configId", required = true) int configId, @RequestParam(value = "transactionId", required = true) int transactionId, HttpSession session) throws Exception {
        
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/messageDetailsForm");
        mav.addObject("pageHeader", "feedback");
        
        /* Get the feedback report configuration details */
        configuration configDetails = configurationManager.getConfigurationById(configId);
        
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            /* Get the organization details for the source (Sender) organization from the original transaction */
            transactionTarget transactionDetails = transactionOutManager.getTransactionDetails(transactionId);
            transactionIn origTransactionDetails = transactionInManager.getTransactionDetails(transactionDetails.gettransactionInId());
            configuration origConfigDetails = configurationManager.getConfigurationById(origTransactionDetails.getconfigId());
            Organization sendingOrgDetails = organizationmanager.getOrganizationById(origConfigDetails.getorgId());
            
            /* Get the organization details for the target (Receiving) organization from the original transaction */
            Organization receivingOrgDetails = organizationmanager.getOrganizationById(configDetails.getorgId());

            /* Find out the target configuration id */
            Integer targetConnectionId = transactionInManager.getFeedbackReportConnection(configId, origConfigDetails.getorgId());
            configurationConnection connectionDetails = configurationManager.getConnection(targetConnectionId);

            /* Get a list of form fields */
            configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(configId, 2);
            
            Transaction transaction = new Transaction();
            transactionOutRecords records = transactionOutManager.getTransactionRecords(transactionId);

            /* Create a new transaction */
            transaction.setorgId(userInfo.getOrgId());
            transaction.settransportMethodId(2);
            transaction.setmessageTypeId(configDetails.getMessageTypeId());
            transaction.setuserId(userInfo.getId());
            transaction.setbatchName(null);
            transaction.setoriginalFileName(null);
            transaction.setstatusId(0);
            transaction.settransactionStatusId(0);
            transaction.settargetOrgId(origConfigDetails.getorgId());
            transaction.setconfigId(configId);
            transaction.setautoRelease(transportDetails.getautoRelease());
            transaction.settargetConfigId(connectionDetails.gettargetConfigId());
            transaction.setorginialTransactionId(transactionId);
            
            
            try {
                List<configurationFormFields> senderInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),1);
           
                /* Set all the transaction SOURCE ORG fields */
                List<transactionRecords> fromFields = setInboxFormFields(senderInfoFormFields, records, configId, false, transactionDetails.gettransactionInId());
                transaction.setsourceOrgFields(fromFields);
            
            }
            catch (Exception e) {
                throw new Exception ("Error retrieving feedback sender fields for configuration id: "+ configId, e);
            }
            
            try {
                List<configurationFormFields> senderProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),2);
            
                /* Set all the transaction SOURCE PROVIDER fields */
                List<transactionRecords> fromProviderFields = setInboxFormFields(senderProviderFormFields, records, configId, false, transactionDetails.gettransactionInId());
                transaction.setsourceProviderFields(fromProviderFields);
            
            }
            catch (Exception e) {
                throw new Exception ("Error retrieving feedback sender provider fields for configuration id: "+ configId, e);
            }


            try {
                List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),3);
            
                /* Set all the transaction TARGET fields */
                List<transactionRecords> toFields;
                if(!targetInfoFormFields.isEmpty()) {
                    toFields = setInboxFormFields(targetInfoFormFields, records, configId, false, transactionDetails.gettransactionInId());
                    
                    if("".equals(toFields.get(0).getFieldValue()) || toFields.get(0).getFieldValue() == null) {
                        List<transactionRecords> orgDetailFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transactionDetails.getId()).gettargetOrgId());
                        
                        int index = 0;
                        for(transactionRecords record : toFields) {
                            record.setFieldValue(orgDetailFields.get(index).getFieldValue());
                            index++;
                        }
                    }
                }
                else {
                    toFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transactionDetails.getId()).gettargetOrgId());
                }
                transaction.settargetOrgFields(toFields);
                
            
            }
            catch (Exception e) {
                throw new Exception ("Error retrieving feedback target fields for configuration id: "+ configId, e);
            }

            try {
                List<configurationFormFields> targetProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),4);
            
                /* Set all the transaction TARGET PROVIDER fields */
                List<transactionRecords> toProviderFields = setInboxFormFields(targetProviderFormFields, records, configId, false, transactionDetails.gettransactionInId());
                transaction.settargetProviderFields(toProviderFields);
            
            }
            catch (Exception e) {
                throw new Exception ("Error retrieving feedback target provider fields for configuration id: "+ configId, e);
            }

            
            try {
                List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),5);
            
                /* Set all the transaction PATIENT fields */
                List<transactionRecords> patientFields = setInboxFormFields(patientInfoFormFields, records, configId, true, transactionDetails.gettransactionInId());
                transaction.setpatientFields(patientFields);
            
            }
            catch (Exception e) {
                throw new Exception ("Error retrieving feedback patient fields for configuration id: "+ configId, e);
            }
            
            try {
                List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),6);

                /* Set all the transaction DETAIL fields */
                List<transactionRecords> detailFields = setInboxFormFields(detailFormFields, null, configId, false, transactionDetails.gettransactionInId());
                transaction.setdetailFields(detailFields);

            
            }
            catch (Exception e) {
                throw new Exception ("Error retrieving feedback detail fields for configuration id: "+ configId, e);
            }
            
            
            
            mav.addObject(transaction);

            /* Set the header totals */
            setTotals(session);
            
             /* Get a list of organization providers */
            List<Provider> providers = organizationmanager.getOrganizationActiveProviders(configDetails.getorgId());
            mav.addObject("providers", providers);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal);
            
            
        }
        catch (Exception e) {
            throw new Exception("An error occurred in returning the feedback report form for config " + configId, e);
        }
        
        
        return mav;
    }
    
    
    /**
     * The '/create/details' POST request will take the selected message type and target org and display
     * the new message form.
     * 
     * @param configId  The selected configuration
     * @param targetOrg The selected target organization to receive the new message
     * 
     * @return this request will return the messageDetailsForm
     */
    @RequestMapping(value="/{pathVariable}/details", method = RequestMethod.POST)
    public ModelAndView showMessageDetailsForm(@PathVariable String pathVariable, @RequestParam(value = "configId", required = true) int configId, @RequestParam(value = "targetOrg", required = false) Integer targetOrg, @RequestParam(value = "targetConfig", required = false) Integer targetConfig, @RequestParam(value = "transactionId", required = false) Integer transactionId, HttpSession session) throws Exception {
        
        if(transactionId == null) {
            transactionId = 0;
        }
        
        if(targetOrg == null) {
            targetOrg = 0;
        }
        
        if(targetConfig == null) {
            targetConfig = 0;
        }
        
        ModelAndView mav = new ModelAndView();
        if("create".equals(pathVariable)) {
            mav.setViewName("/Health-e-Web/messageDetailsForm");
            mav.addObject("pageHeader", "create");
        }
        else {
            mav.setViewName("/Health-e-Web/pendingmessageDetailsForm");
            mav.addObject("pageHeader", "pending");
        }
        
        try {
            
            /* Get the configuration details */
            configuration configDetails = configurationManager.getConfigurationById(configId);

            /* Get the organization details for the source (Sender) organization */
            User userInfo = (User)session.getAttribute("userDetails");
            Organization sendingOrgDetails = organizationmanager.getOrganizationById(userInfo.getOrgId());

            /* Get a list of form fields */
            configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(configId, 2);

            Transaction transaction = new Transaction();
            transactionInRecords records = null;

            if(transactionId > 0) {
                transactionIn transactionInfo = transactionInManager.getTransactionDetails(transactionId);
                batchUploads batchInfo = transactionInManager.getBatchDetails(transactionInfo.getbatchId());
                transactionTarget transactionTarget = transactionInManager.getTransactionTarget(transactionInfo.getbatchId(), transactionId);

                configuration targetConfigDetails = configurationManager.getConfigurationById(transactionTarget.getconfigId());
                targetOrg = targetConfigDetails.getorgId();

                transaction.setorgId(batchInfo.getOrgId());
                transaction.settransportMethodId(transportDetails.gettransportMethodId());
                transaction.setmessageTypeId(configDetails.getMessageTypeId());
                transaction.setuserId(batchInfo.getuserId());
                transaction.setbatchName(batchInfo.getutBatchName());
                transaction.setoriginalFileName(batchInfo.getoriginalFileName());
                transaction.setstatusId(transactionInfo.getstatusId());
                transaction.settransactionStatusId(transactionInfo.getstatusId());
                transaction.settargetOrgId(targetOrg);
                transaction.setconfigId(transactionInfo.getconfigId());
                transaction.setautoRelease(transportDetails.getautoRelease());
                transaction.setbatchId(batchInfo.getId());
                transaction.settransactionId(transactionId);
                transaction.settransactionTargetId(transactionTarget.getId());
                transaction.setdateSubmitted(transactionInfo.getdateCreated());
                transaction.setsourceType(configDetails.getsourceType());
                transaction.settargetConfigId(transactionTarget.getconfigId());
                transaction.setorginialTransactionId(transactionInfo.gettransactionTargetId());

                lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(transaction.getstatusId());
                transaction.setstatusValue(processStatus.getDisplayCode());

                /* get the message type name */
                transaction.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                records = transactionInManager.getTransactionRecords(transactionId);
                transaction.settransactionRecordId(records.getId());
            }
            else {
                /* Create a new transaction */
                transaction.setorgId(userInfo.getOrgId());
                transaction.settransportMethodId(2);
                transaction.setmessageTypeId(configDetails.getMessageTypeId());
                transaction.setuserId(userInfo.getId());
                transaction.setbatchName(null);
                transaction.setoriginalFileName(null);
                transaction.setstatusId(0);
                transaction.settransactionStatusId(0);
                transaction.settargetOrgId(targetOrg);
                transaction.setconfigId(configId);
                transaction.setautoRelease(transportDetails.getautoRelease());
                transaction.settargetConfigId(targetConfig);
                transaction.setsourceType(configDetails.getsourceType());
            }

            /* Get the organization details for the target (Receiving) organization */
            Organization receivingOrgDetails = organizationmanager.getOrganizationById(targetOrg);

            boolean readOnly = false;

            try {
                List<configurationFormFields> senderInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),1);

                /* Set all the transaction SOURCE ORG fields */
                List<transactionRecords> fromFields = setOutboundFormFields(senderInfoFormFields, records, configId, readOnly, sendingOrgDetails.getId());
                transaction.setsourceOrgFields(fromFields);

            }
            catch (Exception e) {
                throw new Exception ("Error retrieving sender fields for configuration id: "+ configId, e);
            }

            try {
                List<configurationFormFields> senderProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),2);

                /* Set all the transaction SOURCE PROVIDER fields */
                List<transactionRecords> fromProviderFields = setOutboundFormFields(senderProviderFormFields, records, configId, readOnly, sendingOrgDetails.getId());
                transaction.setsourceProviderFields(fromProviderFields);

            }
            catch (Exception e) {
                throw new Exception ("Error retrieving sender provider fields for configuration id: "+ configId, e);
            }

            try {
                List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),3);

                /* Set all the transaction TARGET fields */
                List<transactionRecords> toFields = setOutboundFormFields(targetInfoFormFields, records, configId, readOnly, receivingOrgDetails.getId());
                transaction.settargetOrgFields(toFields);

            }
            catch (Exception e) {
                throw new Exception ("Error retrieving target fields for configuration id: "+ configId, e);
            }

            try {
                List<configurationFormFields> targetProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),4);

                /* Set all the transaction TARGET PROVIDER fields */
                List<transactionRecords> toProviderFields = setOutboundFormFields(targetProviderFormFields, records, configId, readOnly, receivingOrgDetails.getId());
                transaction.settargetProviderFields(toProviderFields);

            }
            catch (Exception e) {
                throw new Exception ("Error retrieving target provider fields for configuration id: "+ configId, e);
            }

            try {
                List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),5);

                /* Set all the transaction PATIENT fields */
                if(configDetails.getsourceType() == 2) {
                    readOnly = true;
                }
                List<transactionRecords> patientFields = setOutboundFormFields(patientInfoFormFields, records, configId, readOnly, 0);
                transaction.setpatientFields(patientFields);


            }
            catch (Exception e) {
                throw new Exception ("Error retrieving patient fields for configuration id: "+ configId, e);
            }

            try {
                List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),6);

                /* Set all the transaction DETAIL fields */
                readOnly = false;
                List<transactionRecords> detailFields = setOutboundFormFields(detailFormFields, records, configId, readOnly, 0);
                transaction.setdetailFields(detailFields);
            }
            catch (Exception e) {
                throw new Exception ("Error retrieving patient fields for configuration id: "+ configId, e);
            }

            mav.addObject(transaction);

            /* Get a list of organization providers */
            List<Provider> providers = organizationmanager.getOrganizationActiveProviders(configDetails.getorgId());
            mav.addObject("providers", providers);

            /* Set the header totals */
            setTotals(session);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal);
        }
        catch (Exception e) {
            throw new Exception("Error occurred in getting the details for the new message form for config "+configId, e);
        }
        
        
        return mav;
    }
    
    /**
     * The '/submitMessage' POST request submit the new message
     * 
     * @param transactionDetails  The details of the new message form
     * 
     * @return this request will return the messageDetailsForm
     */
    @RequestMapping(value= "/submitMessage", method = RequestMethod.POST)
    public ModelAndView submitMessage(@ModelAttribute(value = "transactionDetails") Transaction transactionDetails, HttpSession session, @RequestParam String action, RedirectAttributes redirectAttr, @RequestParam List<Integer> attachmentIds) throws Exception {
        
        User userInfo = (User)session.getAttribute("userDetails");
        Integer currBatchId = transactionDetails.getbatchId();
        Integer currTransactionId = transactionDetails.gettransactionId();
        Integer currRecordId = transactionDetails.gettransactionRecordId();
        Integer currTransactionTargetId = transactionDetails.gettransactionTargetId();
        Integer batchId;
        Integer transactionId;
        Integer transactionRecordId;
        Integer transactionTargetId;
        
        /* If currBatchId == 0 then create a new batch */
        if(currBatchId == 0) {
            
            try {
                /* Create the batch name (OrgId+MessageTypeId+Date/Time) */
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date();
                String batchName = new StringBuilder().append(transactionDetails.getorgId()).append(transactionDetails.getmessageTypeId()).append(dateFormat.format(date)).toString();

                /* Submit a new batch */
                batchUploads batchUpload = new batchUploads();
                batchUpload.setOrgId(transactionDetails.getorgId());
                batchUpload.setuserId(userInfo.getId());
                batchUpload.setutBatchName(batchName);
                batchUpload.settransportMethodId(2);
                batchUpload.setoriginalFileName(batchName);

                /* 
                If the "Save" button was pressed set the
                status to "Submission Saved"
                */
                if(action.equals("save")) {
                    batchUpload.setstatusId(8);
                }
                /* 
                If the "Release" button was pressed 
                set the status to "Submission Release Pending"
                */
                else if(action.equals("release")) {
                    batchUpload.setstatusId(5);
                }
                /* 
                If the "Send" button was pressed 
                set the status to "Submission Released"
                */
                else {
                    batchUpload.setstatusId(6);
                }
                batchUpload.settotalRecordCount(1);

                batchId = (Integer) transactionInManager.submitBatchUpload(batchUpload); 
            }
            catch (Exception e) {
                throw new Exception ("Error occurred in creating new batch", e);
            }
        
            
        }
        
        /* Otherwise update existing batch */
        else {
            batchId = currBatchId;
            
            try {
                /* Get the details of the batch */
                batchUploads batchUpload = transactionInManager.getBatchDetails(batchId);

                /* 
                If the "Save" button was pressed set the
                status to "Submission Saved"
                */
                if(action.equals("save")) {
                    batchUpload.setstatusId(8);
                }
                /* 
                If the "Release" button was pressed 
                set the status to "Submission Release Pending"
                */
                else if(action.equals("release")) {
                    batchUpload.setstatusId(5);
                }
                /* 
                If the "Send" button was pressed 
                set the status to "Submission Released"
                */
                else {
                    batchUpload.setstatusId(6);
                }

                transactionInManager.submitBatchUploadChanges(batchUpload);
            }
            catch (Exception e) {
                throw new Exception("Error occurred in updating a batch id: "+batchId, e);
            }
            
        }
        
        /* If currTransactionId == 0 then create a new transaction */
        if(currTransactionId == 0) {
            
            try {
                /* Submit a new Transaction In record */
                transactionIn transactionIn = new transactionIn();
                transactionIn.setbatchId(batchId);
                transactionIn.setconfigId(transactionDetails.getconfigId());
                transactionIn.settransactionTargetId(transactionDetails.getorginialTransactionId());

                /* 
                If the "Save" button was pressed set the
                status to "Saved"
                */
                if(action.equals("save")) {
                    transactionIn.setstatusId(15);
                }
                /* 
                If the "Release" button was pressed 
                set the status to "Release Pending"
                */
                else if(action.equals("release")) {
                    transactionIn.setstatusId(10);
                }
                /* 
                If the "Send" button was pressed 
                set the status to "Released"
                */
                else {
                    transactionIn.setstatusId(10);
                }

                transactionId = (Integer) transactionInManager.submitTransactionIn(transactionIn);

                /* Need to populate the batchUploadSummary table */
                batchUploadSummary summary = new batchUploadSummary();
                summary.setbatchId(batchId);
                summary.settransactionInId(transactionId);
                summary.setsourceOrgId(transactionDetails.getorgId());
                summary.settargetOrgId(transactionDetails.gettargetOrgId());
                summary.setmessageTypeId(transactionDetails.getmessageTypeId());
                summary.setsourceConfigId(transactionDetails.getconfigId());

                transactionInManager.submitBatchUploadSummary(summary);  
            }
            catch (Exception e) {
                throw new Exception("Error occurred in creating the new transaction for batch "+batchId,e);
            }
        
        }
        
        /* Otherwise update existing batch */
        else {
            transactionId =  currTransactionId;  
            
            try {
                transactionIn transactionIn = transactionInManager.getTransactionDetails(transactionId);
            
                /* 
                If the "Save" button was pressed set the
                status to "Saved"
                */
                if(action.equals("save")) {
                    transactionIn.setstatusId(15);
                }
                /*
                If the "Release" button was pressed 
                set the status to "Release Pending"
                */
                else if(action.equals("release")) {
                    transactionIn.setstatusId(10);
                }
                /* 
                If the "Send" button was pressed 
                set the status to "Released"
                */
                else {
                    transactionIn.setstatusId(10);
                }

                transactionInManager.submitTransactionInChanges(transactionIn);
                
            }
            catch (Exception e) {
                throw new Exception("Error occurred in updating an existing transaction id:"+transactionId,e);
            }
            
        }
        
        /* See if any attachments were uploaded */
        if(!attachmentIds.isEmpty()) {
            
            for(Integer attachmentId: attachmentIds) {
                try {
                   transactionAttachment attachment = transactionInManager.getAttachmentById(attachmentId);
                    attachment.setTransactionInId(transactionId);

                    transactionInManager.submitAttachmentChanges(attachment); 
                }
                catch (Exception e) {
                    throw new Exception("Error occurred in adding an attachment id:"+attachmentId,e);
                }
            }
            
        }
        
        try {
            /* Get the 6 Bucket (Source Org, Source Provider, Target Org, Target Provider, Patient, Details) fields */
            List<transactionRecords> sourceOrgFields = transactionDetails.getsourceOrgFields();
            List<transactionRecords> sourceProviderFields = transactionDetails.getsourceProviderFields();
            List<transactionRecords> targetOrgFields = transactionDetails.gettargetOrgFields();
            List<transactionRecords> targetProviderFields = transactionDetails.gettargetProviderFields();
            List<transactionRecords> patientFields = transactionDetails.getpatientFields();
            List<transactionRecords> detailFields = transactionDetails.getdetailFields();

            transactionInRecords records;
            if(currRecordId == 0) {
                records = new transactionInRecords();
                records.setTransactionInId(transactionId);
            }
            else {
                records = transactionInManager.getTransactionRecord(currRecordId);
            }

            String colName;
            for(transactionRecords field : sourceOrgFields) {
                colName = new StringBuilder().append("f").append(field.getfieldNo()).toString();
                try {
                    BeanUtils.setProperty(records, colName, field.getfieldValue());
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            for(transactionRecords field : sourceProviderFields) {
                colName = new StringBuilder().append("f").append(field.getfieldNo()).toString();
                try {
                    BeanUtils.setProperty(records, colName, field.getfieldValue());
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            for(transactionRecords field : targetOrgFields) {
                colName = new StringBuilder().append("f").append(field.getfieldNo()).toString();
                try {
                    BeanUtils.setProperty(records, colName, field.getfieldValue());
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            for(transactionRecords field : targetProviderFields) {
                colName = new StringBuilder().append("f").append(field.getfieldNo()).toString();
                try {
                    BeanUtils.setProperty(records, colName, field.getfieldValue());
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            for(transactionRecords field : patientFields) {
                colName = new StringBuilder().append("f").append(field.getfieldNo()).toString();
                try {
                    BeanUtils.setProperty(records, colName, field.getfieldValue());
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            for(transactionRecords field : detailFields) {
                colName = new StringBuilder().append("f").append(field.getfieldNo()).toString();
                try {
                    BeanUtils.setProperty(records, colName, field.getfieldValue());
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(currRecordId == 0) {
                transactionRecordId = (Integer) transactionInManager.submitTransactionInRecords(records);
            }
            else {
                transactionRecordId = currRecordId;
                records.setId(transactionRecordId);
                transactionInManager.submitTransactionInRecordsUpdates(records);
            }
            
       }
        catch (Exception e) {
            throw new Exception("Error occurred in saving transaction field values", e);
        }
        
        try {
            transactionInManager.submitTransactionTranslatedInRecords(transactionId, transactionRecordId, transactionDetails.getconfigId());
        
            /* Need to populate the transaction Target */
            if(currTransactionTargetId == 0) {

                /* Submit a new Transaction Target record */
                transactionTarget transactiontarget = new transactionTarget();
                transactiontarget.setbatchUploadId(batchId);
                transactiontarget.settransactionInId(transactionId);
                transactiontarget.setconfigId(transactionDetails.gettargetConfigId());

                /* 
                If the "Save" button was pressed set the
                status to "Saved"
                */
                if(action.equals("save")) {
                    transactiontarget.setstatusId(15);
                }
                /*
                If the "Release" button was pressed 
                set the status to "Release Pending"
                */
                else if(action.equals("release")) {
                    transactiontarget.setstatusId(10);
                }
                /* 
                If the "Send" button was pressed 
                set the status to "Released"
                */
                else {
                    transactiontarget.setstatusId(12);
                }

                transactionInManager.submitTransactionTarget(transactiontarget);
            }

            /* Otherwise update existing batch */
            else {
                transactionTargetId =  currTransactionTargetId;  

                transactionTarget transactiontarget = transactionInManager.getTransactionTargetDetails(transactionTargetId);

                /* 
                If the "Save" button was pressed set the
                status to "Saved"
                */
                if(action.equals("save")) {
                    transactiontarget.setstatusId(15);
                }
                /*
                If the "Release" button was pressed 
                set the status to "Release Pending"
                */
                else if(action.equals("release")) {
                    transactiontarget.setstatusId(10);
                }
                /* 
                If the "Send" button was pressed 
                set the status to "Released"
                */
                else {
                    transactiontarget.setstatusId(12);
                }

                transactionInManager.submitTransactionTargetChanges(transactiontarget);
            }
        }
        catch (Exception e) {
            throw new Exception("Error occurred in submitting the translated records id:"+transactionId,e);
        }
        
        
        if (action.equals("send")) {
            
            /*
            Once the "Send" button is pressed we will send the batch off to the processTransactions
            method to handle all the processing and saving to the final messages_ tables. This method
            will return true if successfully received and false otherwise.
            */
            boolean transactionSentToProcess = transactionInManager.processBatch(batchId);
            
            
            /*
                Send the user to the "Sent" items page
            */
            redirectAttr.addFlashAttribute("savedStatus", "sent");
            ModelAndView mav = new ModelAndView(new RedirectView("sent"));
            return mav;
        }
        else {
            /*
                Sent the user to the "Pending" items page
            */
            redirectAttr.addFlashAttribute("savedStatus", "saved");
            ModelAndView mav = new ModelAndView(new RedirectView("pending"));
            return mav;
        }
    
    }
    
    /**
     * The '/pending' GET request will serve up the Health-e-Web (ERG) page that will list all pending
     * messages.
     *
     * @param request
     * @param response
     * @return	the health-e-web pending message list view
     * @throws Exception
     */
    @RequestMapping(value = "/pending", method = RequestMethod.GET)
    public ModelAndView pendingBatches(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/pending");
        
        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");
        
        if("".equals(searchParameters.getsection()) || !"pending".equals(searchParameters.getsection())) {
           searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setsection("pending");
        }
        else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
        }
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            /* Need to get a list of all pending batches */
            /* Need to get a list of all pending transactions */
            List<batchUploads> pendingBatches = transactionInManager.getpendingBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate);

            if(!pendingBatches.isEmpty()) {
                for(batchUploads batch : pendingBatches) {
                    List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);
                }
            }

            mav.addObject("pendingBatches", pendingBatches);

            /* Set the header totals */
            setTotals(session);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal);

        }
        catch (Exception e) {
            throw new Exception("Error occurred trying to get pending items userId: "+userInfo.getId(),e);
        }
        
        
        return mav;
    }
    
    /**
     * The '/pending' POST request will serve up the Health-e-Web (ERG) page that will list all pending
     * messages based on the term searched for.
     *
     * @param request
     * @param response
     * * @param searchTerm The term to search pending messages
     * @return	the health-e-web pending message list view
     * @throws Exception
     */
    @RequestMapping(value = "/pending", method = RequestMethod.POST)
    public ModelAndView findpendingBatches(@RequestParam Date fromDate, @RequestParam Date toDate, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/pending");
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setsection("pending");
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            List<batchUploads> pendingBatches = transactionInManager.getpendingBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate);


            if(!pendingBatches.isEmpty()) {
                for(batchUploads batch : pendingBatches) {
                    List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);
                }
            }

            mav.addObject("pendingBatches", pendingBatches);    
           
            /* Set the header totals */
            setTotals(session);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal);
            
        }
        catch (Exception e) {
            throw new Exception("Error occurred in searching pending items.",e);
        }
        
        return mav;
    }
    
    /**
     * The '/sent' request will serve up the Health-e-Web (ERG) page that will list all sent
     * messages.
     *
     * @param request
     * @param response
     * @return	the health-e-web sent message list view
     * @throws Exception
     */
    @RequestMapping(value = "/sent", method = RequestMethod.GET)
    public ModelAndView sentMessages(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/sent");
        
        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");
        
        if("".equals(searchParameters.getsection()) || !"sent".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setsection("sent");
        }
        else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
        }
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        
        try {
            List<batchUploads> sentBatches = transactionInManager.getsentBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate);

            if(!sentBatches.isEmpty()) {
                for(batchUploads batch : sentBatches) {
                    List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);
                }
            }

            mav.addObject("sentBatches", sentBatches);

            /* Set the header totals */
            setTotals(session);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal);

        }
        catch (Exception e) {
            throw new Exception("Error occurred in viewing the sent items screen. UserId: " + userInfo.getId(),e);
        }
        
        return mav;
    }
    
    
    /**
     * The '/sent' POST request will serve up the Health-e-Web (ERG) page that will list all sent
     * messages based on the term searched for.
     *
     * @param request
     * @param response
     * @param searchTerm The term to search sent messages 
     * @return	the health-e-web sent message list view
     * @throws Exception
     */
    @RequestMapping(value = "/sent", method = RequestMethod.POST)
    public ModelAndView findsentBatches(@RequestParam Date fromDate, @RequestParam Date toDate, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/sent");
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setsection("sent");
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            List<batchUploads> sentBatches = transactionInManager.getsentBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate);

            if(!sentBatches.isEmpty()) {
                for(batchUploads batch : sentBatches) {
                    List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);
                }
            }

            mav.addObject("sentBatches", sentBatches); 
           
            /* Set the header totals */
            setTotals(session);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal);
            
        }
        catch (Exception e) {
            throw new Exception("Error occurred in searching sent items. UserId: "+ userInfo.getId(),e);
        }
        
        return mav;
    }
    
    /**
     * The 'batch/transactions' request will display a page that will show all transactions
     * associated with the clicked batch.
     * 
     * @param batchId   The id of the clicked batch
     * @param fromPage  The page the user was on when viewing batches (Pending or Sent)
     * @param session   The session scope variables
     * 
     * @return This method will return the page to display all transactions for the batch.
     */
    @RequestMapping(value = "batch/transactions", method = RequestMethod.POST)
    public ModelAndView showBatchTransactions(@RequestParam Integer batchId, @RequestParam String fromPage, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/batchTransactions");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        /* Get the details of the batch */
        batchUploads batchDetails = transactionInManager.getBatchDetails(batchId);
        mav.addObject("batchDetails", batchDetails);
        
        try {
            /* Get all the transactions for the batch */
            List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batchId, userInfo.getId());

            List<Transaction> transactionList = new ArrayList<Transaction>();

            for(transactionIn transaction : batchTransactions) {

                Transaction transactionDetails = new Transaction();
                transactionDetails.settransactionRecordId(transaction.getId());
                transactionDetails.setstatusId(transaction.getstatusId());
                transactionDetails.setdateSubmitted(transaction.getdateCreated());
                transactionDetails.setconfigId(transaction.getconfigId());

                lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(transaction.getstatusId());
                transactionDetails.setstatusValue(processStatus.getDisplayCode());

                transactionInRecords records = transactionInManager.getTransactionRecords(transaction.getId());

                /* Get a list of form fields */
                /*configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transaction.getconfigId(), 2);*/
                configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transaction.getconfigId());
                List<configurationFormFields> sourceInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),1);
                List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),3);
                List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),5);
                List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),6);

                /* Set all the transaction SOURCE ORG fields */
                List<transactionRecords> fromFields;
                if(!sourceInfoFormFields.isEmpty()) {
                    fromFields = setOutboundFormFields(sourceInfoFormFields, records, 0, true, 0);
                }
                else {
                    fromFields = setOrgDetails(batchDetails.getOrgId());
                }
                transactionDetails.setsourceOrgFields(fromFields);

                /* Set all the transaction TARGET fields */
                List<transactionRecords> toFields;
                toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transaction.getId()).gettargetOrgId());
                transactionDetails.settargetOrgFields(toFields);

                /* Set all the transaction PATIENT fields */
                List<transactionRecords> patientFields = setOutboundFormFields(patientInfoFormFields, records, 0, true, 0);
                transactionDetails.setpatientFields(patientFields);


                /* Set all the transaction DETAIL fields */
                List<transactionRecords> detailFields = setOutboundFormFields(detailFormFields, records, 0, true, 0);
                transactionDetails.setdetailFields(detailFields);

                /* get the message type name */
                configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                transactionList.add(transactionDetails);
            }

            mav.addObject("transactions", transactionList);
            mav.addObject("fromPage", fromPage);

            /* Set the header totals */
            setTotals(session);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal);
        }
        catch (Exception e) {
            throw new Exception("Error occurred in getting transactions for a sent batch. batchId: "+ batchId,e);
        }
        
        return mav;
        
    }
    
    
    
    /**
     * The '/sent/messageDetails' POST request will display the selected transaction details. This page is 
     * served up from inbox or sent Items batch transaction list page. So the form will be readOnly.
     * 
     * @param transactionId  The id of the selected transaction
     * @param fromPage       The page the request is coming from (inbox) or (sent)
     * 
     * @return this request will return the messageDetailsForm
     */
    @RequestMapping(value="/sent/messageDetails", method = RequestMethod.POST)
    public ModelAndView showMessageDetails(@RequestParam(value = "transactionId", required = true) Integer transactionId, @RequestParam(value = "fromPage", required = false) String fromPage, HttpSession session) throws Exception {
       
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/sentmessageDetails");
        
        if(fromPage != null) {
            mav.addObject("fromPage", fromPage);
        }
        
        try {
            transactionIn transactionInfo = transactionInManager.getTransactionDetails(transactionId);
          
            /* Get the configuration details */
            configuration configDetails = configurationManager.getConfigurationById(transactionInfo.getconfigId());

            /* Get the organization details for the source (Sender) organization */
            User userInfo = (User)session.getAttribute("userDetails");

            /* Get a list of form fields */
            /*configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transactionInfo.getconfigId(), 2);*/
            configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transactionInfo.getconfigId());
            List<configurationFormFields> senderInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),1);
            List<configurationFormFields> senderProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),2);
            List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),3);
            List<configurationFormFields> targetProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),4);
            List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),5);
            List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),6);

            Transaction transaction = new Transaction();
            transactionInRecords records = null;

            batchUploads batchInfo = transactionInManager.getBatchDetails(transactionInfo.getbatchId());
            transactionTarget transactionTarget = transactionInManager.getTransactionTarget(transactionInfo.getbatchId(), transactionId);

            transaction.setorgId(batchInfo.getOrgId());
            transaction.settransportMethodId(2);
            transaction.setmessageTypeId(configDetails.getMessageTypeId());
            transaction.setuserId(batchInfo.getuserId());
            transaction.setbatchName(batchInfo.getutBatchName());
            transaction.setoriginalFileName(batchInfo.getoriginalFileName());
            transaction.setstatusId(batchInfo.getstatusId());
            transaction.settransactionStatusId(transactionInfo.getstatusId());
            transaction.settargetOrgId(0);
            transaction.setconfigId(transactionInfo.getconfigId());
            transaction.setautoRelease(transportDetails.getautoRelease());
            transaction.setbatchId(batchInfo.getId());
            transaction.settransactionId(transactionTarget.getId());
            transaction.settransactionTargetId(transactionTarget.getId());
            transaction.setdateSubmitted(transactionInfo.getdateCreated());

            /* Check to see if the message is a feedback report */
            if(transactionInfo.gettransactionTargetId() > 0) {
                transaction.setsourceType(2); /* Feedback report */
                transaction.setorginialTransactionId(transactionInfo.gettransactionTargetId());
            }
            else {
                transaction.setsourceType(configDetails.getsourceType());
            }

            lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(transaction.getstatusId());
            transaction.setstatusValue(processStatus.getDisplayCode());

            /* get the message type name */
            transaction.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

            records = transactionInManager.getTransactionRecords(transactionId);
            transaction.settransactionRecordId(records.getId());


            /* Set all the transaction SOURCE ORG fields */
            List<transactionRecords> fromFields;
            if(!senderInfoFormFields.isEmpty()) {
                fromFields = setOutboundFormFields(senderInfoFormFields, records, transactionInfo.getconfigId(), true, 0);
            }
            else {
                fromFields = setOrgDetails(batchInfo.getOrgId());
            }
            transaction.setsourceOrgFields(fromFields);
            
            /* Set all the transaction SOURCE PROVIDER fields */
            List<transactionRecords> fromProviderFields = setOutboundFormFields(senderProviderFormFields, records, transactionInfo.getconfigId(), true, 0);
            transaction.setsourceProviderFields(fromProviderFields);

            /* Set all the transaction TARGET fields */
            List<transactionRecords> toFields;
            if(!targetInfoFormFields.isEmpty()) {
                toFields = setOutboundFormFields(targetInfoFormFields, records, transactionInfo.getconfigId(), true, 0);

                if("".equals(toFields.get(0).getFieldValue()) || toFields.get(0).getFieldValue() == null) {
                    toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transactionInfo.getId()).gettargetOrgId());
                }

            }
            else {
                toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transactionInfo.getId()).gettargetOrgId());
            }
            transaction.settargetOrgFields(toFields);

            /* Set all the transaction TARGET PROVIDER fields */
            List<transactionRecords> toProviderFields = setOutboundFormFields(targetProviderFormFields, records, transactionInfo.getconfigId(), true, 0);
            transaction.settargetProviderFields(toProviderFields);

            /* Set all the transaction PATIENT fields */
            List<transactionRecords> patientFields = setOutboundFormFields(patientInfoFormFields, records, transactionInfo.getconfigId(), true, 0);
            transaction.setpatientFields(patientFields);

            /* Set all the transaction DETAIL fields */
            List<transactionRecords> detailFields = setOutboundFormFields(detailFormFields, records, transactionInfo.getconfigId(), true, 0);
            transaction.setdetailFields(detailFields);

            mav.addObject("transactionDetails", transaction);

            /* Set the header totals */
            setTotals(session);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal);
            mav.addObject("fromPage","sent");
            mav.addObject("transactionInId", transactionTarget.gettransactionInId());

        }
        catch (Exception e) {
            throw new Exception("Error occurred in viewing the sent batch details. transactionId: "+transactionId,e);
        }
        
        return mav;
    }
    
    /**
     * The '/viewStatus{statusId}' function will return the details of the selected status. 
     * The results will be displayed in the overlay.
     *
     * @Param	statusId   This will hold the id of the selected status
     *
     * @Return	This function will return the status details view.
     */
    @RequestMapping(value = "/viewStatus{statusId}", method = RequestMethod.GET)
    public @ResponseBody ModelAndView viewStatus(@PathVariable int statusId) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/statusDetails");

        /* Get the details of the selected status */ 
        lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(statusId);
        mav.addObject("statusDetails", processStatus);

        return mav;
    }
    
    /**
     * The 'uploadMessageAttachment' will use jquery to upload a new attachment to the message
     * 
     */
    @RequestMapping(value = "/uploadMessageAttachment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer uploadMessageAttachment(@RequestParam(value = "fileUpload", required = true) MultipartFile fileUpload, @RequestParam(value = "title", required = false) String title, HttpSession session) throws Exception {
        
        Integer attachmentId = 0;
        
        /* Get the organization details for the source (Sender) organization */
        User userInfo = (User)session.getAttribute("userDetails");
        Organization sendingOrgDetails = organizationmanager.getOrganizationById(userInfo.getOrgId());
        
        try {
            /* Upload the attachment */
            String fileName = transactionInManager.uploadAttachment(fileUpload, sendingOrgDetails.getcleanURL());

            /* Create a new attachment */
            transactionAttachment attachment = new transactionAttachment();
            attachment.setTransactionInId(0);
            attachment.setfileLocation("/"+sendingOrgDetails.getcleanURL()+"/attachments/");
            attachment.setfileName(fileName);
            attachment.settitle(title);

            attachmentId = transactionInManager.submitAttachment(attachment);

        }
        catch (Exception e) {
            throw new Exception("Error occurred when uploading a message attachment.",e);
        }
        
        return attachmentId;
    }
    
    /**
     * The 'populateExistingAttachments' function will look to see if any attachments exist
     * for the transaction.
     * 
     * @param   transactionId   The id of the selected transaction (only passed when viewing an
     *                          existing transaction)
     * 
     * @param   newattachmentIdList the list of attachments added to a transaction (only passed
     *                              when a new attachment is added to a transaction)
     * 
     * @return  This function will return the transaction attachment list page.
     */
    @RequestMapping(value= "/populateExistingAttachments.do", method = RequestMethod.GET)
    public @ResponseBody ModelAndView getMessageAttachments(@RequestParam(value = "transactionId", required = false) Integer transactionId, @RequestParam(value = "newattachmentIdList", required = false) List<Integer> newattachmentIdList, @RequestParam(value= "pageFrom", required = false) String pageFrom) throws Exception {
        
        if(pageFrom == null) {
            pageFrom = "sent";
        }
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/existingAttachments");
        
        try {
            List<transactionAttachment> attachments = new ArrayList<transactionAttachment>();
        
            if(transactionId > 0) {
                List<transactionAttachment> existingAttachments = transactionInManager.getAttachmentsByTransactionId(transactionId);

                for(transactionAttachment attachment : existingAttachments) {
                    attachments.add(attachment);
                }
            }

            if(newattachmentIdList != null && !newattachmentIdList.isEmpty()) {
                for(Integer attachmentId : newattachmentIdList) {
                    transactionAttachment attachment = transactionInManager.getAttachmentById(attachmentId);
                    attachments.add(attachment);
                }
            }

            mav.addObject("attachments", attachments);
            mav.addObject("pageFrom", pageFrom);
            
        }
        catch (Exception e) {
            throw new Exception("Error occurred trying to get attachments. transactionid: "+ transactionId,e);
        }
        
        
        return mav;
        
    }
    
    /**
     * The 'removeAttachment.do' function will remove the selected attachment.
     * 
     * @param attachmentId  The id of the attachment to remove
     * 
     * @return This function will simply return a 1.
     */
    @RequestMapping(value= "/removeAttachment.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer removeMessageAttachment(@RequestParam(value = "attachmentId", required = false) Integer attachmentId) throws Exception {
        
        try {
            transactionInManager.removeAttachmentById(attachmentId);
        }
        catch (Exception e) {
            throw new Exception("Error occurred trying to delete an attachment. attachmentId: "+attachmentId,e);
        }
        
        return 1;
    }
    
    /**
     * The 'populateProvider.do' function will get the provider details for the provider id
     * passed in.
     * 
     * @param   providerId  The id of the provider to return details for.
     * 
     * @return This function will return the provider object.
     */
    @RequestMapping(value="/populateProvider.do", method = RequestMethod.GET)
    public @ResponseBody Provider populateProvider(@RequestParam(value = "providerId", required = true) int providerId) throws Exception {
        
        try {
            Provider providerDetails = providermanager.getProviderById(providerId);
        
            /* Get the list of addresses for the provider */
            List<providerAddress> providerAddresses = providermanager.getProviderAddresses(providerId);
            providerDetails.setProviderAddresses(providerAddresses);

            /* Get the list of ids for the provider */
            List<providerIdNum> providerIds = providermanager.getProviderIds(providerId);
            providerDetails.setProviderIds(providerIds);

            return providerDetails;
        }
        catch (Exception e) {
            throw new Exception("",e);
        }
        
    }
    
    
    /**
     * The 'batch/sendBatches' POST function will send the marked batches off to the processing
     * method.
     * 
     * @param batchIdList The list of marked batch Ids
     * 
     * @return  This function will redirect the user to the sent items page
     * 
     */
    @RequestMapping(value="/sendBatches", method = RequestMethod.POST)
    public ModelAndView sendBatches(@RequestParam(value = "batchIdList", required = true) List<Integer> batchIdList, RedirectAttributes redirectAttr , HttpSession session) throws Exception {
        
        try {
            /* 
            If the list of batch Ids is not empty loop through each marked batch
            and send off to the processBatch method.
            */
            if(!batchIdList.isEmpty()) {
                for(Integer batchId : batchIdList) {
                    
                    /* Update batch to released */
                    transactionInManager.updateBatchStatus(batchId, 6, "");

                    /* Process the batch */
                    boolean transactionSentToProcess = transactionInManager.processBatch(batchId);

                }
            }


            /*
                Send the user back to the pending items page
            */
            redirectAttr.addFlashAttribute("savedStatus", "sent");
            ModelAndView mav = new ModelAndView(new RedirectView("sent"));
            return mav;
        }
        catch (Exception e) {
            throw new Exception("Error occurred trying to send batches. batchIdList: "+batchIdList,e);
        }
        
    }
      
    
    /**
     * The 'submitMessageNote.do' function will get the provider details for the provider id
     * passed in.
     * 
     * @param   providerId  The id of the provider to return details for.
     * 
     * @return This function will return the provider object.
     */
    @RequestMapping(value="/submitMessageNote.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer submitMessageNote(HttpSession session, @RequestParam(value = "messageTransactionId", required = true) int messageTransactionId, @RequestParam(value = "internalStatusId", required = false) int internalStatusId, @RequestParam(value = "messageNotes", required = false) String messageNotes) throws Exception {
        
        try {
            /* Update the internal status code if exists */
            if(internalStatusId > 0) {
                transactionTarget transactionDetails = transactionOutManager.getTransactionDetails(messageTransactionId);
                transactionDetails.setinternalStatusId(internalStatusId);

                transactionOutManager.updateTransactionDetails(transactionDetails);
            }

            /* If the note is not emtpy need to insert it */
            if(!"".equals(messageNotes) && messageNotes != null) {
                /* Need to get all the message types set up for the user */
                User userInfo = (User)session.getAttribute("userDetails");

                transactionOutNotes newNote = new transactionOutNotes();
                newNote.settransactionTargetId(messageTransactionId);
                newNote.setuserId(userInfo.getId());
                newNote.setnote(messageNotes);

                transactionOutManager.saveNote(newNote);
            }

            return 1;
        }
        catch (Exception e) {
            throw new Exception("Error occcurred in creating a message note. transactionId: "+ messageTransactionId,e);
        }
        
    }
    
    
    /**
     * The 'populateExistingNotes.do' function will look to see if any notes exist
     * for the transaction.
     * 
     * @param   transactionId   The id of the selected transaction (only passed when viewing an
     *                          existing transaction)
     * 
     * 
     * @return  This function will return the transaction note list page.
     */
    @RequestMapping(value= "/populateExistingNotes.do", method = RequestMethod.GET)
    public @ResponseBody ModelAndView getMessageNotes(@RequestParam(value = "transactionId", required = true) Integer transactionId) throws Exception {
       
        try {
             ModelAndView mav = new ModelAndView();
            mav.setViewName("/Health-e-Web/existingNotes");
            
            List<transactionOutNotes> existingNotes = transactionOutManager.getNotesByTransactionId(transactionId);

            /* Set the name of the user who created it */
            for(transactionOutNotes note : existingNotes) {
                User userDetails = usermanager.getUserById(note.getuserId());
                String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                note.setuserName(usersName);
            }

            mav.addObject("notes", existingNotes);
            mav.addObject("pageFrom", "inbox");

            return mav;
        }
        catch (Exception e) {
            throw new Exception("Error occurred trying to get message notes. transactionId: "+transactionId,e);
        }
        
       
        
    }
    
    /**
     * The 'removeNote.do' function will remove the selected note.
     * 
     * @param noteId  The id of the note to remove
     * 
     * @return This function will simply return a 1.
     */
    @RequestMapping(value= "/removeNote.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer removeMessageNote(@RequestParam(value = "noteId", required = false) Integer noteId) throws Exception {
        
        try {
            transactionOutManager.removeNoteById(noteId);
            return 1;
        }
        catch (Exception e) {
            throw new Exception("Error occurred trying to remove a message note. noteId: "+noteId,e);
        }
        
    }
    
    /**
     * The 'setInboxFormFields' will create and populate the form field object
     * 
     * @param formfields  The list of form fields
     * @param records     The values of the form fields to populate with.
     * 
     * @return This function will return a list of transactionRecords fields with the correct data
     * 
     * @throws NoSuchMethodException 
     */
    public List<transactionRecords> setInboxFormFields(List<configurationFormFields> formfields, transactionOutRecords records, int configId, boolean readOnly, int transactionInId) throws NoSuchMethodException {
        
        List<transactionRecords> fields = new ArrayList<transactionRecords>();
        
        for(configurationFormFields formfield : formfields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(formfield.getFieldNo());
            field.setrequired(formfield.getRequired());
            field.setsaveToTable(formfield.getsaveToTableName());
            field.setsaveToTableCol(formfield.getsaveToTableCol());
            field.setfieldLabel(formfield.getFieldLabel());
            field.setreadOnly(readOnly);
            field.setfieldValue(null);
            
            /* Get the validation */
            if(formfield.getValidationType() > 1) {
                field.setvalidation(messagetypemanager.getValidationById(formfield.getValidationType()).toString());
            }
            
            if(records != null) {
                String colName = new StringBuilder().append("f").append(formfield.getFieldNo()).toString();
                try {
                    field.setfieldValue(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            /* 
                If records == null and an auto populate field is set for the field get the data from the
                table/col for the transaction
            */
            else if(records == null && formfield.getautoPopulateTableName() != null && transactionInId > 0) {
                
                /* Get the pre-populated values */
                String tableName = formfield.getautoPopulateTableName();
                String tableCol = formfield.getautoPopulateTableCol();
                
                if(!tableName.isEmpty() && !tableCol.isEmpty()) {
                    field.setfieldValue(transactionInManager.getFieldValue(tableName, tableCol, "transactionInId", transactionInId));
                    field.setreadOnly(true);
                }
                
            }
            
            if(configId > 0) {
                /* See if any fields have crosswalks associated to it */
                List<fieldSelectOptions> fieldSelectOptions = transactionInManager.getFieldSelectOptions(formfield.getId(),configId);
                field.setfieldSelectOptions(fieldSelectOptions);
            }
            
            fields.add(field);
        }
        
        return fields;
    }
    
    /**
     * The 'setOutboundFormFields' will create and populate the form field object
     * 
     * @param formfields  The list of form fields
     * @param records     The values of the form fields to populate with.
     * 
     * @return This function will return a list of transactionRecords fields with the correct data
     * 
     * @throws NoSuchMethodException 
     */ 
    public List<transactionRecords> setOutboundFormFields(List<configurationFormFields> formfields, transactionInRecords records, int configId, boolean readOnly, int orgId) throws NoSuchMethodException {
        
        List<transactionRecords> fields = new ArrayList<transactionRecords>();
        
        for(configurationFormFields formfield : formfields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(formfield.getFieldNo());
            field.setrequired(formfield.getRequired());
            field.setsaveToTable(formfield.getsaveToTableName());
            field.setsaveToTableCol(formfield.getsaveToTableCol());
            field.setfieldLabel(formfield.getFieldLabel());
            field.setreadOnly(readOnly);
            field.setfieldValue(null);
            
            /* Get the pre-populated values */
            String tableName = formfield.getautoPopulateTableName();
            String tableCol = formfield.getautoPopulateTableCol();
            
            /* Get the validation */
            if(formfield.getValidationType() > 1) {
                field.setvalidation(messagetypemanager.getValidationById(formfield.getValidationType()).toString());
            }
            
            if(records != null) {
                String colName = new StringBuilder().append("f").append(formfield.getFieldNo()).toString();
                try {
                    field.setfieldValue(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                /* If autopopulate field is set make it read only */
                if(!tableName.isEmpty() && !tableCol.isEmpty()) {
                    field.setreadOnly(true);
                }
            }
            else if(orgId > 0)  {
            
                if(!tableName.isEmpty() && !tableCol.isEmpty()) {
                    field.setfieldValue(transactionInManager.getFieldValue(tableName, tableCol, "id", orgId));
                }
            }
            
            if(configId > 0) {
                /* See if any fields have crosswalks associated to it */
                List<fieldSelectOptions> fieldSelectOptions = transactionInManager.getFieldSelectOptions(formfield.getId(),configId);
                field.setfieldSelectOptions(fieldSelectOptions);
            }
            
            fields.add(field);
        }
        
        return fields;
    }
    
    /**
     * The '/feedbackReports' POST request will display a list of feedback reports for the selected transaction.
     *
     * @param request
     * @param response
     * 
     * @return	the health-e-web feedback report message list view
     * @throws Exception
     */
    @RequestMapping(value = "/feedbackReports", method = RequestMethod.POST)
    public ModelAndView getFeedbackReports(@RequestParam(value = "transactionId", required = true) Integer transactionId, @RequestParam(value = "fromPage", required = false) String fromPage, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/feedbackReports");
        
        if(fromPage == null) {
            fromPage = "inbox";
        }
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            /* Get the transaction Details */
            transactionTarget transactionDetails = transactionOutManager.getTransactionDetails(transactionId);

            /* Get the details of the batch */
            int batchId = 0;

            /* Get all the feedback reports for the original batch */
            List<transactionIn> feedbackReports = transactionOutManager.getFeedbackReports(transactionId, fromPage);

            List<Transaction> transactionList = new ArrayList<Transaction>();

            /* if coming from the sent page then we need to look for feedback reports from the 
            originating message. Otherwise we need to look for feedback reports from the message in
            the inbox.
            */
            if("sent".equals(fromPage)) {
                batchId = transactionDetails.getbatchUploadId();
                batchUploads batchDetails = transactionInManager.getBatchDetails(batchId);
                mav.addObject("batchDetails", batchDetails);
                mav.addObject("OriginaltransactionId", transactionDetails.gettransactionInId());

                for(transactionIn feedbackReport : feedbackReports) {

                    /* Need to get the feedback reports in the inbox */
                    transactionTarget inboxFeedbackReport = transactionOutManager.getTransactionsByInId(feedbackReport.getId());

                    Transaction transaction = new Transaction();
                    transaction.settransactionRecordId(inboxFeedbackReport.getId());
                    transaction.setstatusId(inboxFeedbackReport.getstatusId());
                    transaction.setdateSubmitted(inboxFeedbackReport.getdateCreated());
                    transaction.setconfigId(inboxFeedbackReport.getconfigId());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(inboxFeedbackReport.getstatusId());
                    transaction.setstatusValue(processStatus.getDisplayCode());

                    transactionOutRecords records = transactionOutManager.getTransactionRecords(inboxFeedbackReport.getId());

                    /* Get a list of form fields */
                    configurationTransport transportDetails = configurationTransportManager.getTransportDetails(inboxFeedbackReport.getconfigId());
                    List<configurationFormFields> sourceInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(inboxFeedbackReport.getconfigId(),transportDetails.getId(),1);
                    List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(inboxFeedbackReport.getconfigId(),transportDetails.getId(),3);
                    List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(inboxFeedbackReport.getconfigId(),transportDetails.getId(),5);
                    List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(inboxFeedbackReport.getconfigId(),transportDetails.getId(),6);

                    /* Set all the transaction SOURCE ORG fields */
                    List<transactionRecords> fromFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transactionDetails.getId()).getsourceOrgId());
                    transaction.setsourceOrgFields(fromFields);

                    /* Set all the transaction TARGET fields */
                    List<transactionRecords> toFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transactionDetails.getId()).gettargetOrgId());
                    transaction.settargetOrgFields(toFields);

                    /* Set all the transaction PATIENT fields */
                    List<transactionRecords> patientFields = setInboxFormFields(patientInfoFormFields, records, 0, true, 0);
                    transaction.setpatientFields(patientFields);

                    /* Set all the transaction DETAIL fields */
                    List<transactionRecords> detailFields = setInboxFormFields(detailFormFields, records, 0, true, 0);
                    transaction.setdetailFields(detailFields);

                    /* get the message type name */
                    configuration configDetails = configurationManager.getConfigurationById(inboxFeedbackReport.getconfigId());
                    transaction.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                    transactionList.add(transaction);
                }
            }
            else {
                batchId = transactionDetails.getbatchDLId();
                batchDownloads batchDetails = transactionOutManager.getBatchDetails(batchId);
                mav.addObject("batchDetails", batchDetails);
                mav.addObject("OriginaltransactionId", transactionId);

                for(transactionIn feedbackReport : feedbackReports) {

                    Transaction transaction = new Transaction();
                    transaction.settransactionRecordId(feedbackReport.getId());
                    transaction.setstatusId(feedbackReport.getstatusId());
                    transaction.setdateSubmitted(feedbackReport.getdateCreated());
                    transaction.setconfigId(feedbackReport.getconfigId());


                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(feedbackReport.getstatusId());
                    transaction.setstatusValue(processStatus.getDisplayCode());

                    transactionInRecords records = transactionInManager.getTransactionRecords(feedbackReport.getId());

                    /* Get a list of form fields */
                    configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(feedbackReport.getconfigId(), 2);
                    List<configurationFormFields> sourceInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(feedbackReport.getconfigId(),transportDetails.getId(),1);
                    List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(feedbackReport.getconfigId(),transportDetails.getId(),3);
                    List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(feedbackReport.getconfigId(),transportDetails.getId(),5);
                    List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(feedbackReport.getconfigId(),transportDetails.getId(),6);

                    /* Set all the transaction SOURCE fields */
                    List<transactionRecords> fromFields = setOutboundFormFields(sourceInfoFormFields, records, 0, true, 0);
                    transaction.setsourceOrgFields(fromFields);

                    /* Set all the transaction TARGET fields */
                    List<transactionRecords> toFields = setOutboundFormFields(targetInfoFormFields, records, 0, true, 0);
                    transaction.settargetOrgFields(toFields);

                    /* Set all the transaction PATIENT fields */
                    List<transactionRecords> patientFields = setOutboundFormFields(patientInfoFormFields, records, 0, true, 0);
                    transaction.setpatientFields(patientFields);

                    /* Set all the transaction DETAIL fields */
                    List<transactionRecords> detailFields = setOutboundFormFields(detailFormFields, records, 0, true, 0);
                    transaction.setdetailFields(detailFields);

                    /* get the message type name */
                    configuration configDetails = configurationManager.getConfigurationById(feedbackReport.getconfigId());
                    transaction.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                    transactionList.add(transaction);
                }
            }

            mav.addObject("transactions", transactionList);
            mav.addObject("fromPage", fromPage);

            /* Set the header totals */
            setTotals(session);

            mav.addObject("pendingTotal", pendingTotal);
            mav.addObject("inboxTotal", inboxTotal);
        }
        catch (Exception e) {
            throw new Exception("Error occurred in finding associted feedback report messages. transactionId: "+transactionId,e);
        }
        
        
        return mav;
    }
    
    /**
    * @param filter 
    * START for start date of month e.g.  Nov 01, 2013
    * END for end date of month e.g.  Nov 30, 2013
    * @return
    */
   public Date getMonthDate(String filter){
       
       String MM_DD_YYYY = "yyyy-mm-dd";
       SimpleDateFormat sdf = new SimpleDateFormat(MM_DD_YYYY);
       sdf.setTimeZone(TimeZone.getTimeZone("EST"));
       sdf.format(GregorianCalendar.getInstance().getTime());

       Calendar cal =  GregorianCalendar.getInstance();
       int date = cal.getActualMinimum(Calendar.DATE);
       if("END".equalsIgnoreCase(filter)){
           date = cal.getActualMaximum(Calendar.DATE);
           cal.set(Calendar.DATE, date);
           cal.set(Calendar.HOUR_OF_DAY, 23);
           cal.set(Calendar.MINUTE, 59);
           cal.set(Calendar.SECOND, 59);
           cal.set(Calendar.MILLISECOND, 0);
       }
       else {
           cal.set(Calendar.DATE, date);
           cal.set(Calendar.HOUR_OF_DAY, 0);
           cal.set(Calendar.MINUTE, 0);
           cal.set(Calendar.SECOND, 0);
           cal.set(Calendar.MILLISECOND, 0);
       }
       
       
       return cal.getTime();
   }
   
   
   /**
     * The 'cancelMessage.do' function will cancel the selected message.
     * 
     * @param transactionId  The id of the selected transaction
     * 
     * @return This function will simply return a 1.
     */
    @RequestMapping(value= "/cancelMessage.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer cancelMessageTransaction(@RequestParam(value = "transactionId", required = true) Integer transactionId, @RequestParam(value = "sent", required = true) boolean sent) throws Exception {
        
        try {
            
            if(sent == true) {
                /* Need to get batch for the transaction */
                transactionTarget targetDetails = transactionOutManager.getTransactionDetails(transactionId);

                transactionOutManager.cancelMessageTransaction(transactionId, targetDetails.gettransactionInId());

                /* Need to update the batch status */
                transactionOutManager.updateTargetBatchStatus(targetDetails.getbatchDLId(), 32, "");
                transactionInManager.updateBatchStatus(targetDetails.getbatchUploadId(), 32, "");
            }
            else {
                transactionIn transactionDetails = transactionInManager.getTransactionDetails(transactionId);
                
                transactionInManager.cancelMessageTransaction(transactionId);
                
                transactionInManager.updateBatchStatus(transactionDetails.getbatchId(), 32, "");
            }
            
            return 1;
        }
        catch (Exception e) {
            throw new Exception("Error occurred trying to cancel a message transaction. transactionId: "+transactionId,e);
        }
        
    }
    
    /**
     * The 'deleteMessage.do' function will delete the selected message.
     * 
     * @param transactionId  The id of the selected transaction
     * 
     * @return This function will simply return a 1.
     */
    @RequestMapping(value= "/deleteMessage.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer deleteMessageTransaction(@RequestParam(value = "transactionId", required = false) Integer transactionId) throws Exception {
        
        try {
            int batchId = transactionInManager.getTransactionDetails(transactionId).getbatchId();
            transactionInManager.deleteMessage(batchId, transactionId);
            
            return 1;
        }
        catch (Exception e) {
            throw new Exception("Error occurred trying to delete a message transaction. transactionId: "+transactionId,e);
        }
        
    }
    
    
    /**
     * The 'setOrgDetails' function will set the field values to the passed in orgId if the
     * organization information wasn't collected with the file upload.
     * 
     * @param orgId The organization id to get the details for
     * 
     * @return 
     */
    public List<transactionRecords> setOrgDetails(int orgId) {
        
        List<transactionRecords> fields = new ArrayList<transactionRecords>();
        
        /* Get the organization Details */
        Organization orgDetails = organizationmanager.getOrganizationById(orgId);
        
        transactionRecords namefield = new transactionRecords();
        
        namefield.setFieldValue(orgDetails.getOrgName());
        fields.add(namefield);
        
        transactionRecords addressfield = new transactionRecords();
        
        addressfield.setFieldValue(orgDetails.getAddress());
        fields.add(addressfield);
        
        transactionRecords address2field = new transactionRecords();
        address2field.setFieldValue(orgDetails.getAddress2());
        fields.add(address2field);
        
        transactionRecords cityfield = new transactionRecords();
        cityfield.setFieldValue(orgDetails.getCity());
        fields.add(cityfield);
        
        transactionRecords statefield = new transactionRecords();
        statefield.setFieldValue(orgDetails.getState());
        fields.add(statefield);
        
        transactionRecords zipfield = new transactionRecords();
        zipfield.setFieldValue(orgDetails.getPostalCode());
        fields.add(zipfield);
        
        transactionRecords phonefield = new transactionRecords();
        phonefield.setFieldValue(orgDetails.getPhone());
        fields.add(phonefield);
        
        transactionRecords faxfield = new transactionRecords();
        faxfield.setFieldValue(orgDetails.getFax());
        fields.add(faxfield);
        
        return fields;
        
    } 
    
}
