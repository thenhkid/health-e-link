/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

import com.ut.dph.model.Organization;
import com.ut.dph.model.Transaction;
import com.ut.dph.model.User;
import com.ut.dph.model.UserActivity;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.batchUploadSummary;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationMessageSpecs;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.custom.TransErrorDetailDisplay;
import com.ut.dph.model.custom.searchParameters;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.model.pendingDeliveryTargets;
import com.ut.dph.model.systemSummary;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import com.ut.dph.model.transactionOutRecords;
import com.ut.dph.model.transactionRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.service.organizationManager;
import com.ut.dph.service.sysAdminManager;
import com.ut.dph.service.transactionInManager;
import com.ut.dph.service.transactionOutManager;
import com.ut.dph.service.userManager;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author chadmccue
 */

@Controller
@RequestMapping("/administrator/processing-activity")
public class adminProcessingActivity {
    
    @Autowired
    private transactionInManager transactionInManager;
    
    @Autowired
    private transactionOutManager transactionOutManager;
    
    @Autowired
    private sysAdminManager sysAdminManager;
    
    @Autowired
    private organizationManager organizationmanager;
    
    @Autowired
    private configurationTransportManager configurationTransportManager;
    
    @Autowired
    private messageTypeManager messagetypemanager;
    
    @Autowired
    private configurationManager configurationManager;
    
    @Autowired
    private userManager usermanager;
    
    /**
     * The private maxResults variable will hold the number of results to show per list page.
     */
    private static int maxResults = 10;
    
    /**
     * The '/inbound' GET request will serve up the existing list of generated referrals and feedback reports
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return          The list of inbound batch list
     *
     * @Objects	(1) An object containing all the found batches
     *
     * @throws Exception
     */
    @RequestMapping(value = "/inbound", method = RequestMethod.GET)
    public ModelAndView listInBoundBatches(HttpSession session) throws Exception {
        
        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year,month,day);
        
        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/inbound");
        
        if("".equals(searchParameters.getsection()) || !"inbound".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setsection("inbound");
        }
        else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
        }
            
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate",originalDate);
        
        /* Get system inbound summary */
        systemSummary summaryDetails = transactionInManager.generateSystemInboundSummary();
        mav.addObject("summaryDetails", summaryDetails);
        
        
        /* Get all inbound transactions */
        try {
            /* Need to get a list of all uploaded batches */
            List<batchUploads> uploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate);
            
            List<Integer> statusIds = new ArrayList();

            if(!uploadedBatches.isEmpty()) {
                for(batchUploads batch : uploadedBatches) {
                    batch.settotalTransactions(transactionInManager.getRecordCounts(batch.getId(), statusIds, false, false));

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    Organization orgDetails = organizationmanager.getOrganizationById(batch.getOrgId());
                    batch.setorgName(orgDetails.getOrgName());
                    
                    batch.settransportMethod(configurationTransportManager.getTransportMethodById(batch.gettransportMethodId()));
                    
                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }
            
           mav.addObject("batches", uploadedBatches);
           
        }
        catch (Exception e) {
            throw new Exception("Error occurred viewing the all uploaded batches.",e);
        }
        
        return mav;
        
    }
    
    /**
     * The '/inbound' POST request will serve up the existing list of generated referrals and feedback reports
     * based on a search or date
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return          The list of inbound batch list
     *
     * @Objects	(1) An object containing all the found batches
     *
     * @throws Exception
     */
    @RequestMapping(value = "/inbound", method = RequestMethod.POST)
    public ModelAndView listInBoundBatches(@RequestParam Date fromDate, @RequestParam Date toDate,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year,month,day);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/inbound");
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate",originalDate);
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setsection("inbound");
        
        /* Get system inbound summary */
        systemSummary summaryDetails = transactionInManager.generateSystemInboundSummary();
        mav.addObject("summaryDetails", summaryDetails);
        
        
        /* Get all inbound transactions */
        try {
            /* Need to get a list of all uploaded batches */
            List<batchUploads> uploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate);
            
            List<Integer> statusIds = new ArrayList();

            if(!uploadedBatches.isEmpty()) {
                for(batchUploads batch : uploadedBatches) {
                    batch.settotalTransactions(transactionInManager.getRecordCounts(batch.getId(), statusIds, false, false));

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    Organization orgDetails = organizationmanager.getOrganizationById(batch.getOrgId());
                    batch.setorgName(orgDetails.getOrgName());
                    
                    batch.settransportMethod(configurationTransportManager.getTransportMethodById(batch.gettransportMethodId()));
                    
                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }
            
           mav.addObject("batches", uploadedBatches);
           
        }
        catch (Exception e) {
            throw new Exception("Error occurred viewing the all uploaded batches.",e);
        }
        
        return mav;
    }
    
   
    /**
     * The '/outbound' GET request will serve up the existing list of generated referrals and feedback reports to for the
     * target
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return          The list of inbound batch list
     *
     * @Objects	(1) An object containing all the found batches
     *
     * @throws Exception
     */
    @RequestMapping(value = "/outbound", method = RequestMethod.GET)
    public ModelAndView listOutBoundBatches(HttpSession session) throws Exception {
        
        
        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year,month,day);
        
        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/outbound");
        
        if("".equals(searchParameters.getsection()) || !"outbound".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setsection("outbound");
        }
        else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
        }
            
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate",originalDate);
        
        /* Get system inbound summary */
        systemSummary summaryDetails = transactionOutManager.generateSystemOutboundSummary();
        mav.addObject("summaryDetails", summaryDetails);
        
        
        /* Get all inbound transactions */
        try {
            /* Need to get a list of all uploaded batches */
            List<batchDownloads> Batches = transactionOutManager.getAllBatches(fromDate, toDate);
            
            List<Integer> statusIds = new ArrayList();

            if(!Batches.isEmpty()) {
                for(batchDownloads batch : Batches) {
                    batch.settotalTransactions(transactionInManager.getRecordCounts(batch.getId(), statusIds, true, false));

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    Organization orgDetails = organizationmanager.getOrganizationById(batch.getOrgId());
                    batch.setorgName(orgDetails.getOrgName());
                    
                    batch.settransportMethod(configurationTransportManager.getTransportMethodById(batch.gettransportMethodId()));
                    
                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }

           mav.addObject("batches", Batches);
           
        }
        catch (Exception e) {
            throw new Exception("Error occurred viewing the all downloaded batches. Error:"+e.getMessage(),e);
        }
        
        return mav;
        
    }
    
    
    /**
     * The '/outbound' POST request will serve up the existing list of generated referrals and feedback reports
     * for a target based on a search or date
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return          The list of inbound batch list
     *
     * @Objects	(1) An object containing all the found batches
     *
     * @throws Exception
     */
    @RequestMapping(value = "/outbound", method = RequestMethod.POST)
    public ModelAndView listOutBoundBatches(@RequestParam Date fromDate, @RequestParam Date toDate,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
      
        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year,month,day);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/outbound");
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate",originalDate);
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setsection("outbound");
        
        /* Get system inbound summary */
        systemSummary summaryDetails = transactionOutManager.generateSystemOutboundSummary();
        mav.addObject("summaryDetails", summaryDetails);
        
        
        /* Get all inbound transactions */
        try {
            /* Need to get a list of all uploaded batches */
            List<batchDownloads> Batches = transactionOutManager.getAllBatches(fromDate, toDate);
            
            List<Integer> statusIds = new ArrayList();

            if(!Batches.isEmpty()) {
                for(batchDownloads batch : Batches) {
                    batch.settotalTransactions(transactionInManager.getRecordCounts(batch.getId(), statusIds, true, false));

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    Organization orgDetails = organizationmanager.getOrganizationById(batch.getOrgId());
                    batch.setorgName(orgDetails.getOrgName());
                    
                    batch.settransportMethod(configurationTransportManager.getTransportMethodById(batch.gettransportMethodId()));
                    
                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }
            

           mav.addObject("batches", Batches);
          
        }
        catch (Exception e) {
            throw new Exception("Error occurred viewing the all downloaded batches. Error:"+e.getMessage(),e);
        }
        
        return mav;
    }
    
    
    /**
     * The '/inbound/batch/{batchName}' GET request will retrieve a list of transactions that are associated to the clicked batch 
     *
     * @param batchName	The name of the batch to retreive transactions for
     * @return          The list of inbound batch transactions
     *
     * @Objects	(1) An object containing all the found batch transactions
     *
     * @throws Exception
     */
    @RequestMapping(value = "/inbound/batch/{batchName}", method = RequestMethod.GET)
    public ModelAndView listBatchTransactions(@PathVariable String batchName) throws Exception {
  
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/transactions");
        
        /* Get the details of the batch */
        batchUploads batchDetails = transactionInManager.getBatchDetailsByBatchName(batchName);
        
        if(batchDetails != null) {
            
            Organization orgDetails = organizationmanager.getOrganizationById(batchDetails.getOrgId());
            batchDetails.setorgName(orgDetails.getOrgName());
        
            mav.addObject("batchDetails", batchDetails);

            try {
                /* Get all the transactions for the batch */
                List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batchDetails.getId(), 0);

                List<Transaction> transactionList = new ArrayList<Transaction>();
                
                if(batchTransactions.size() > 100) {
                    mav.addObject("toomany","toomany");
                    mav.addObject("size", batchTransactions.size());
                }
                else {

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
                        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transaction.getconfigId());
                        List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),3);

                        /* Set all the transaction TARGET fields */
                        List<transactionRecords> toFields;
                        if(!targetInfoFormFields.isEmpty()) {
                            toFields = setOutboundFormFields(targetInfoFormFields, records, 0, true, 0);

                            if("".equals(toFields.get(0).getFieldValue()) || toFields.get(0).getFieldValue() == null) {
                                toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transaction.getId()).gettargetOrgId());
                            }
                        }
                        else {
                            toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transaction.getId()).gettargetOrgId());
                        }
                        transactionDetails.settargetOrgFields(toFields);

                        /* get the message type name */
                        configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                        transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                        configurationMessageSpecs messageSpecs  = configurationManager.getMessageSpecs(transaction.getconfigId());

                        if(messageSpecs.getrptField1() > 0) {

                            configurationFormFields formField1 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(),transportDetails.getId(),messageSpecs.getrptField1());

                            transactionDetails.setreportableFieldHeading1(formField1.getFieldLabel());

                            String rptFieldCol1 = new StringBuilder().append("f").append(messageSpecs.getrptField1()).toString();
                            try {
                                transactionDetails.setreportableField1(BeanUtils.getProperty(records, rptFieldCol1));
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvocationTargetException ex) {
                                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        if(messageSpecs.getrptField2() > 0) {

                            configurationFormFields formField2 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(),transportDetails.getId(),messageSpecs.getrptField2());

                            transactionDetails.setreportableFieldHeading2(formField2.getFieldLabel());

                            String rptFieldCol2 = new StringBuilder().append("f").append(messageSpecs.getrptField2()).toString();
                            try {
                                transactionDetails.setreportableField2(BeanUtils.getProperty(records, rptFieldCol2));
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvocationTargetException ex) {
                                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        if(messageSpecs.getrptField3() > 0) {

                            configurationFormFields formField3 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(),transportDetails.getId(),messageSpecs.getrptField3());

                            transactionDetails.setreportableFieldHeading3(formField3.getFieldLabel());

                            String rptFieldCol3 = new StringBuilder().append("f").append(messageSpecs.getrptField3()).toString();
                            try {
                                transactionDetails.setreportableField3(BeanUtils.getProperty(records, rptFieldCol3));
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvocationTargetException ex) {
                                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        if(messageSpecs.getrptField4() > 0) {

                            configurationFormFields formField4 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(),transportDetails.getId(),messageSpecs.getrptField4());

                            transactionDetails.setreportableFieldHeading4(formField4.getFieldLabel());

                            String rptFieldCol4 = new StringBuilder().append("f").append(messageSpecs.getrptField4()).toString();
                            try {
                                transactionDetails.setreportableField4(BeanUtils.getProperty(records, rptFieldCol4));
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvocationTargetException ex) {
                                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        transactionList.add(transactionDetails);
                    }
                 }

                mav.addObject("transactions", transactionList);

            }
            catch (Exception e) {
                throw new Exception("(Admin) Error occurred in getting transactions for a sent batch. batchId: "+ batchDetails.getId()+" ERROR: "+e.getMessage(),e);
            }
        }
        
        return mav;
    }
    
   
    /**
     * The '/outbound/batch/{batchName}' GET request will retrieve a list of transactions that are associated to the clicked batch 
     *
     * @param batchName	The name of the batch to retreive transactions for
     * @return          The list of outbound batch transactions
     *
     * @Objects	(1) An object containing all the found batch transactions
     *
     * @throws Exception
     */
    @RequestMapping(value = "/outbound/batch/{batchName}", method = RequestMethod.GET)
    public ModelAndView listoutboundBatchTransactions(@PathVariable String batchName) throws Exception {
  
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/outboundtransactions");
        
        /* Get the details of the batch */
        batchDownloads batchDetails = transactionOutManager.getBatchDetailsByBatchName(batchName);
        
        if(batchDetails != null) {
            
            Organization orgDetails = organizationmanager.getOrganizationById(batchDetails.getOrgId());
            batchDetails.setorgName(orgDetails.getOrgName());
        
            mav.addObject("batchDetails", batchDetails);

            try {
                /* Get all the transactions for the batch */
                List<transactionTarget> batchTransactions = transactionOutManager.getInboxBatchTransactions(batchDetails.getId(), 0);

                List<Transaction> transactionList = new ArrayList<Transaction>();
                
                if(batchTransactions.size() > 100) {
                    mav.addObject("toomany","toomany");
                    mav.addObject("size", batchTransactions.size());
                }
                else {

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
                        List<configurationFormFields> sourceInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),1);

                        /* Set all the transaction TARGET fields */
                        List<transactionRecords> fromFields;
                        if(!sourceInfoFormFields.isEmpty()) {
                            fromFields = setInboxFormFields(sourceInfoFormFields, records, 0, true, 0);
                        }
                        else {
                            fromFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transaction.getId()).getsourceOrgId());
                        }
                        transactionDetails.setsourceOrgFields(fromFields);

                        /* get the message type name */
                        configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                        transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                        transactionList.add(transactionDetails);
                    }
                 }

                mav.addObject("transactions", transactionList);

            }
            catch (Exception e) {
                throw new Exception("(Admin) Error occurred in getting transactions for a target batch. batchId: "+ batchDetails.getId()+" ERROR: "+e.getMessage(),e);
            }
        }
        
        return mav;
    }
    
    /**
     * The '/pending' GET request will retrieve a list of pendind transactions grouped by organization.
     * 
     * @return The list of pending transactions to be processed
     * 
     * @throws Exception
     */
    @RequestMapping(value="/pending", method = RequestMethod.GET)
    public ModelAndView showWaitingMessages(HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/pending");
       
        /* Get system inbound summary */
        systemSummary summaryDetails = transactionOutManager.generateSystemWaitingSummary();
        mav.addObject("summaryDetails", summaryDetails);
        
        /* Get all waiting transactions */
        try {
            /* Need to get a list of all uploaded batches */
            List batchTransactions = transactionOutManager.getTransactionsToProcess();
            
            List<pendingDeliveryTargets> transactionList = new ArrayList<pendingDeliveryTargets>();
            
            if(!batchTransactions.isEmpty()) {
                 for(ListIterator iter = batchTransactions.listIterator(); iter.hasNext(); ) {
                     
                    Object[] row = (Object[]) iter.next();
                     
                    
                    /* Get the target Org */
                    Organization tgtOrgDetails = organizationmanager.getOrganizationById(Integer.valueOf(Integer.parseInt(String.valueOf(row[0]))));
                            
                    pendingDeliveryTargets targetDetails = new pendingDeliveryTargets();
                    targetDetails.setOrgId(tgtOrgDetails.getId());
                    targetDetails.setTotalPending(Integer.valueOf(Integer.parseInt(String.valueOf(row[1]))));
                        
                    String OrgDetails = new StringBuilder()
                      .append(tgtOrgDetails.getOrgName())
                      .append("<br />")
                      .append(tgtOrgDetails.getAddress()).append(" ").append(tgtOrgDetails.getAddress2())
                      .append("<br />")
                      .append(tgtOrgDetails.getCity()).append(" ").append(tgtOrgDetails.getState()).append(",").append(tgtOrgDetails.getPostalCode()).toString();
                       
                    targetDetails.setOrgDetails(OrgDetails);
                     
                    transactionList.add(targetDetails);
                }
            }
            

           mav.addObject("transactions", transactionList);
           
       }
        catch (Exception e) {
            throw new Exception("(Admin) Error occurred viewing the all pending transactions. Error: "+e.getMessage(),e);
        }
        
        return mav;
        
    }
    
    
    /**
     * The '/pending/{orgId}' GET method will return all pending output transactions based on the the
     * passed in organization Id. The will return pending transactions grouped by message types for the passed in
     * organziation
     * 
     * 
     */
    @RequestMapping(value="/pending/messageTypes", method = RequestMethod.POST)
    public ModelAndView viewOrgPendingMessages(@RequestParam(value = "orgId", required = true) int orgId) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/pendingByMessageType");
        
        mav.addObject("orgId", orgId);
        
        
        /* Get all waiting transactions */
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaltransactions = 0;
            List batchTransactions = transactionOutManager.getTransactionsToProcessByMessageType(orgId);
            
            List<pendingDeliveryTargets> transactionList = new ArrayList<pendingDeliveryTargets>();
            
            if(!batchTransactions.isEmpty()) {
                 for(ListIterator iter = batchTransactions.listIterator(); iter.hasNext(); ) {
                     
                    Object[] row = (Object[]) iter.next();
                     
                    
                    /* Get the target Org */
                    Organization tgtOrgDetails = organizationmanager.getOrganizationById(Integer.valueOf(Integer.parseInt(String.valueOf(row[0]))));
                            
                    pendingDeliveryTargets targetDetails = new pendingDeliveryTargets();
                    targetDetails.setOrgId(tgtOrgDetails.getId());
                    targetDetails.setTotalPending(Integer.valueOf(Integer.parseInt(String.valueOf(row[3]))));
                        
                    String OrgDetails = new StringBuilder()
                      .append(tgtOrgDetails.getOrgName())
                      .append("<br />")
                      .append(tgtOrgDetails.getAddress()).append(" ").append(tgtOrgDetails.getAddress2())
                      .append("<br />")
                      .append(tgtOrgDetails.getCity()).append(" ").append(tgtOrgDetails.getState()).append(",").append(tgtOrgDetails.getPostalCode()).toString();
                       
                    targetDetails.setOrgDetails(OrgDetails);
                    
                    targetDetails.setMessageType(String.valueOf(row[1]));
                    targetDetails.setMessageTypeId(Integer.valueOf(Integer.parseInt(String.valueOf(row[2]))));
                     
                    transactionList.add(targetDetails);
                }
                 
                mav.addObject("transactions", transactionList);
            }
          
       }
        catch (Exception e) {
            throw new Exception("(Admin) Error occurred viewing the all pending transactions. Error: "+e.getMessage(),e);
        }
        
        return mav;
        
        
    }
    
    /**
     * The '/pending/{orgId}/{messageTypeId}' GET method will return all pending output transactions based on the the
     * passed in organization Id and selected message type. 
     * 
     */
    @RequestMapping(value="/pending/transactions", method = RequestMethod.POST)
    public ModelAndView viewOrgPendingMessages(@RequestParam(value = "orgId", required = true) int orgId, @RequestParam(value = "messageTypeId", required = true) int messageTypeId, @RequestParam(value = "fromDate", required = false) Date fromDate, @RequestParam(value= "toDate", required = false) Date toDate) throws Exception {
        
        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year,month,day);
        
        if(fromDate == null) {
            fromDate = getMonthDate("START");
        }
        if(toDate == null) {
            toDate = getMonthDate("END");
        }
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/pendingTransactions");
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate",originalDate);
        mav.addObject("orgId", orgId);
        mav.addObject("messageTypeId", messageTypeId);
        
        
        /* Get all waiting transactions */
        try {
            
            mav.addObject("messageType", messagetypemanager.getMessageTypeById(messageTypeId).getName());
            mav.addObject("targetOrg", organizationmanager.getOrganizationById(orgId).getOrgName());
            
            /* Need to get a list of all uploaded batches */
            List<transactionTarget> transactions = transactionOutManager.getPendingDeliveryTransactions(orgId, messageTypeId, fromDate, toDate);
            
            List<Transaction> transactionList = new ArrayList<Transaction>();

            if(!transactions.isEmpty()) {
                for(transactionTarget transaction : transactions) {
                    
                    /* Need to get uploaded Config */
                    batchUploadSummary batchDetails = transactionInManager.getUploadSummaryDetails(transaction.gettransactionInId());
                    batchUploads batchUploadDetails = transactionInManager.getBatchDetails(batchDetails.getbatchId());
                    
                    Transaction transactionDetails = new Transaction();
                    transactionDetails.settransactionRecordId(transaction.getId());
                    transactionDetails.setstatusId(transaction.getstatusId());
                    transactionDetails.setdateSubmitted(transaction.getdateCreated());
                    transactionDetails.setconfigId(transaction.getconfigId());
                    transactionDetails.setbatchName(batchUploadDetails.getutBatchName());

                    transactionInRecords records = transactionInManager.getTransactionRecords(transaction.gettransactionInId());

                    /* Get a list of form fields */
                    configurationTransport transportDetails = configurationTransportManager.getTransportDetails(batchDetails.getsourceConfigId());
                    List<configurationFormFields> sourceInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(batchDetails.getsourceConfigId(),transportDetails.getId(),1);
                    List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(batchDetails.getsourceConfigId(),transportDetails.getId(),5);

                    /* Set all the transaction SOURCE fields */
                    List<transactionRecords> fromFields;
                    if(!sourceInfoFormFields.isEmpty()) {
                        fromFields = setOutboundFormFields(sourceInfoFormFields, records, 0, true, 0);
                    }
                    else {
                        fromFields = setOrgDetails(batchDetails.getsourceOrgId());
                    }
                    transactionDetails.setsourceOrgFields(fromFields);
                    
                    /* Set all the transaction TARGET fields */
                    List<transactionRecords> patientFields;
                    patientFields = setOutboundFormFields(patientInfoFormFields, records, 0, true, 0);
                    transactionDetails.setpatientFields(patientFields);

                    /* get the message type name */
                    configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                    transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                    transactionList.add(transactionDetails);
                }
                
                mav.addObject("transactions", transactionList);
            }
            
       }
        catch (Exception e) {
            throw new Exception("(Admin) Error occurred viewing the all pending transactions. Error: "+e.getMessage(),e);
        }
        
        return mav;
        
        
    }
    
    /**
     * The '/ViewMessageDetails' POST request will display the selected transaction details. This page is 
     * served up from inbox batch transaction list page. So the form will be readOnly.
     * 
     * @param transactionId  The id of the selected transaction
     * @param fromPage       The page the request is coming from (inbox) 
     * 
     * @return this request will return the messageDetailsForm
     */
    @RequestMapping(value="/ViewMessageDetails", method = RequestMethod.GET)
    public @ResponseBody ModelAndView showInboxMessageDetails(@RequestParam(value="Type", required = true) Integer Type, @RequestParam(value = "transactionId", required = true) Integer transactionId, @RequestParam(value = "configId", required = true) Integer configId) throws Exception {
       
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activities/messageDetails");
        
        try {
            
            Transaction transaction = new Transaction();
            
            /* Type = 1 (Source) */
            if(Type == 1) {
                
                 transactionIn transactionInfo = transactionInManager.getTransactionDetails(transactionId);
                 if (transactionInfo == null)  { // this will be null if batch is reset - the same transactions will not be available 
                	 return mav;
                 }
                 /* Get the configuration details */
                configuration configDetails = configurationManager.getConfigurationById(transactionInfo.getconfigId());
                
                /* Get a list of form fields */
                /*configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transactionInfo.getconfigId(), 2);*/
                configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transactionInfo.getconfigId());
                List<configurationFormFields> senderInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),1);
                List<configurationFormFields> senderProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),2);
                List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),3);
                List<configurationFormFields> targetProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),4);
                List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),5);
                List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),6);

                transactionInRecords records = null;

                batchUploads batchInfo = transactionInManager.getBatchDetails(transactionInfo.getbatchId());
                
                records = transactionInManager.getTransactionRecords(transactionId);
                
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
                
            }
            else {
                
                transactionTarget transactionInfo = transactionOutManager.getTransactionDetails(transactionId);
                
                if (transactionInfo == null)  { // this will be null if batch is reset - the same transactions will not be available 
               	 return mav;
                }
                /* Get the configuration details */
                configuration configDetails = configurationManager.getConfigurationById(transactionInfo.getconfigId());
                
                /* Get a list of form fields */
                /*configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transactionInfo.getconfigId(), 2);*/
                configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transactionInfo.getconfigId());
                List<configurationFormFields> senderInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),1);
                List<configurationFormFields> senderProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),2);
                List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),3);
                List<configurationFormFields> targetProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),4);
                List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),5);
                List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),6);

                transactionOutRecords records = null;
                
                batchDownloads batchInfo = transactionOutManager.getBatchDetails(transactionInfo.getbatchDLId());
                
                records = transactionOutManager.getTransactionRecords(transactionId);
                
                /* Set all the transaction SOURCE ORG fields */
                List<transactionRecords> fromFields;
                if(!senderInfoFormFields.isEmpty()) {
                    fromFields = setInboxFormFields(senderInfoFormFields, records, transactionInfo.getconfigId(), true, 0);
                }
                else {
                    fromFields = setOrgDetails(batchInfo.getOrgId());
                }
                transaction.setsourceOrgFields(fromFields);

                /* Set all the transaction SOURCE PROVIDER fields */
                List<transactionRecords> fromProviderFields = setInboxFormFields(senderProviderFormFields, records, transactionInfo.getconfigId(), true, 0);
                transaction.setsourceProviderFields(fromProviderFields);

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
                

                /* Set all the transaction TARGET PROVIDER fields */
                List<transactionRecords> toProviderFields = setInboxFormFields(targetProviderFormFields, records, transactionInfo.getconfigId(), true, 0);
                transaction.settargetProviderFields(toProviderFields);

                /* Set all the transaction PATIENT fields */
                List<transactionRecords> patientFields = setInboxFormFields(patientInfoFormFields, records, transactionInfo.getconfigId(), true, 0);
                transaction.setpatientFields(patientFields);

                /* Set all the transaction DETAIL fields */
                List<transactionRecords> detailFields = setInboxFormFields(detailFormFields, records, transactionInfo.getconfigId(), true, 0);
                transaction.setdetailFields(detailFields);
                
            }
            
            mav.addObject("transactionDetails", transaction);

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
        mav.setViewName("/administrator/processing-activities/statusDetails");

        /* Get the details of the selected status */ 
        lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(statusId);
        mav.addObject("statusDetails", processStatus);

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
     * The 'processAllTransactions' function will process all transactions based on the passed in organziation id and message type id.
     */
    @RequestMapping(value = "/processAllTransactions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean processAllTransactions(@RequestParam(value = "orgId", required = true) Integer orgId, @RequestParam(value = "messageTypeId", required = false) Integer messageTypeId) throws Exception {
        
       
        if(messageTypeId == null || "".equals(messageTypeId)) {
            messageTypeId = 0;
        }
        
        /* Need to get all transactions for the passed in org and message type id (IF PASSED IN) */
        List transactions = transactionOutManager.getAllransactionsToProcessByMessageType(orgId, messageTypeId);
        
        for(ListIterator iter = transactions.listIterator(); iter.hasNext(); ) {
            
            Object[] row = (Object[]) iter.next();
            
            transactionTarget transaction = transactionOutManager.getTransactionDetails(Integer.valueOf(Integer.parseInt(String.valueOf(row[0]))));
            
            int batchId = transactionOutManager.processManualTransaction(transaction);
            
        }
        
        return true;
    }
    
    
    /**
     * The 'donotprocessAllTransactions' function will update all transactions based on the passed in organziation id and message type id to
     * DO NOT PROCESS.
     */
    @RequestMapping(value = "/donotprocessAllTransactions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean donotprocessAllTransactions(@RequestParam(value = "orgId", required = true) Integer orgId, @RequestParam(value = "messageTypeId", required = false) Integer messageTypeId) throws Exception {
        
       
        if(messageTypeId == null || "".equals(messageTypeId)) {
            messageTypeId = 0;
        }
        
        /* Need to get all transactions for the passed in org and message type id (IF PASSED IN) */
        List transactions = transactionOutManager.getAllransactionsToProcessByMessageType(orgId, messageTypeId);
        
        for(ListIterator iter = transactions.listIterator(); iter.hasNext(); ) {
            Object[] row = (Object[]) iter.next();
            transactionOutManager.doNotProcessTransaction(Integer.valueOf(Integer.parseInt(String.valueOf(row[0]))));
            
        }
        
        return true;
    }
    
    
    /**
     * The 'processTransaction' function will take an outbound transaction and start the processing.
     * 
     * @param transactionId The id of the transaction that needs to be processed.
     */
    @RequestMapping(value = "/processTransaction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean processTransaction(@RequestParam(value = "transactionId", required = true) Integer transactionId) throws Exception {
        
        transactionTarget transaction = transactionOutManager.getTransactionDetails(transactionId);
        
        int batchId = transactionOutManager.processManualTransaction(transaction);
        
        return true;
    }
    
    /**
     * The 'donotprocessTransaction' function will update the transaction to Do Not Process.
     * 
     * @param transactionId The id of the transaction that needs to be processed.
     */
    @RequestMapping(value = "/donotprocessTransaction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean donotprocessTransaction(@RequestParam(value = "transactionId", required = true) Integer transactionId) throws Exception {
        
        transactionOutManager.doNotProcessTransaction(transactionId);
        
        return true;
    }
    
    
    /**
     * The '/inbound/batchActivities/{batchName}' GET request will retrieve a list of user activities that are associated to the clicked batch 
     *
     * @param batchName	The name of the batch to retrieve transactions for
     * @return          The list of inbound batch user activities
     *
     * @Objects	(1) An object containing all the found user activities
     *
     * @throws Exception
     */
    @RequestMapping(value = "/inbound/batchActivities/{batchName}", method = RequestMethod.GET)
    public ModelAndView listBatchActivities(@PathVariable String batchName) throws Exception {
  
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/batchActivities");
        
        /* Get the details of the batch */
        batchUploads batchDetails = transactionInManager.getBatchDetailsByBatchName(batchName);
        
        if(batchDetails != null) {
            
            Organization orgDetails = organizationmanager.getOrganizationById(batchDetails.getOrgId());
            batchDetails.setorgName(orgDetails.getOrgName());
        
            mav.addObject("batchDetails", batchDetails);

            try {
                /* Get all the user activities for the batch */
                List<UserActivity> uas = transactionInManager.getBatchActivities(batchDetails, true, false);
                mav.addObject("userActivities", uas);

            }
            catch (Exception e) {
                throw new Exception("(Admin) Error occurred in getting batch activities for an inbound batch. batchId: "+ batchDetails.getId()+" ERROR: "+e.getMessage(),e);
            }
        }
        
        return mav;
    }
    
    
    /**
     * The '/ViewUATransactionList' function will return the list of transaction ids for a batch activity that was 
     * too long to display
     * The results will be displayed in the overlay.
     *
     * @Param	uaId   This will hold the id of the user activity
     * @Param	type   1 = inbound 2 = outbound
     *
     * @Return	This function will return the transactionList for that user activity.
     */
    @RequestMapping(value = "/ViewUATransactionList", method = RequestMethod.GET)
    public ModelAndView viewUATransactionList(@RequestParam(value = "uaId", required = true) Integer uaId,
    		@RequestParam(value = "Type", required = true) Integer type) 
    		throws Exception {
   
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activities/transactionList");

        /* Get the details of the selected status */ 
        UserActivity userActivity = usermanager.getUAById(uaId);
        
        
        /* Get the details of the batch */
        batchUploads batchDetails = new batchUploads();
        if (type==1) {
        	batchDetails = transactionInManager.getBatchDetails(userActivity.getBatchUploadId());
        } else {
        	batchDetails = transactionInManager.getBatchDetails(userActivity.getBatchDownloadId());
        }
        
        mav.addObject("userActivity", userActivity);
        mav.addObject("batchDetails", batchDetails);
        	
        return mav;
    }

    /**
     * The '/inbound/auditReport/{batchName}' GET request will retrieve the audit report that is associated to the clicked batch 
     *
     * @param batchName	The name of the batch to retrieve transactions for
     * @return          The audit report for the batch
     *
     * @Objects	(1) An object containing all the errored transactions
     *
     * @throws Exception
     */
    @RequestMapping(value = "/inbound/auditReport/{batchName}", method = RequestMethod.GET)
    public ModelAndView viewAuditReport(@PathVariable String batchName) throws Exception {
  
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/auditReport");
        
        /* Get the details of the batch */
        batchUploads batchDetails = transactionInManager.getBatchDetailsByBatchName(batchName);
        
        if(batchDetails != null) {
            
            Organization orgDetails = organizationmanager.getOrganizationById(batchDetails.getOrgId());
            batchDetails.setorgName(orgDetails.getOrgName());
            lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batchDetails.getstatusId());
            batchDetails.setstatusValue(processStatus.getDisplayCode());

            if (batchDetails.getConfigId() != 0) {
            	batchDetails.setConfigName(configurationManager.getMessageTypeNameByConfigId(batchDetails.getConfigId()));
            } else {
            	batchDetails.setConfigName("Multiple Message Types");
            }
            mav.addObject("batchDetails", batchDetails);

            try {
            	List<TransErrorDetailDisplay> errorList = new LinkedList<TransErrorDetailDisplay>();
                errorList = transactionInManager.populateErrorList(batchDetails);
                mav.addObject("errorList", errorList);
            }
            catch (Exception e) {
                throw new Exception("(Admin) Error occurred in getting the audit report for an inbound batch. batchId: "+ batchDetails.getId()+" ERROR: "+e.getMessage(),e);
            }
        }
        
        return mav;
    }
    
   
}
