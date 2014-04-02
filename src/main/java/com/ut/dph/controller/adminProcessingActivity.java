/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

import com.ut.dph.model.Organization;
import com.ut.dph.model.Transaction;
import com.ut.dph.model.User;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.batchUploadSummary;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationMessageSpecs;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.custom.searchParameters;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.lutables.lu_ProcessStatus;
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
        
        int page = 1;
        
        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year,month,day);
        
        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");
        String searchTerm = "";
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/inbound");
        
        if("".equals(searchParameters.getsection()) || !"inbound".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setpage(1);
            searchParameters.setsection("inbound");
            searchParameters.setsearchTerm("");
        }
        else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
            page = searchParameters.getpage();
            searchTerm = searchParameters.getsearchTerm();
        }
            
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("searchTerm", searchTerm);
        mav.addObject("originalDate",originalDate);
        
        /* Get system inbound summary */
        systemSummary summaryDetails = transactionInManager.generateSystemInboundSummary();
        mav.addObject("summaryDetails", summaryDetails);
        
        
        /* Get all inbound transactions */
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaluploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate, searchTerm, 1, 0).size();
            List<batchUploads> uploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate, searchTerm, 1, maxResults);
            
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
           
           Integer totalPages = (int)Math.ceil((double)totaluploadedBatches / maxResults);
           mav.addObject("totalPages", totalPages);
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
    public ModelAndView listInBoundBatches(@RequestParam(value = "page", required = false) Integer page, @RequestParam String searchTerm, @RequestParam Date fromDate, @RequestParam Date toDate,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        if(page == null || page < 1) {
            page = 1;
        }
        
        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year,month,day);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/inbound");
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("searchTerm", searchTerm);
        mav.addObject("originalDate",originalDate);
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setpage(page);
        searchParameters.setsection("inbound");
        searchParameters.setsearchTerm(searchTerm);
        
        /* Get system inbound summary */
        systemSummary summaryDetails = transactionInManager.generateSystemInboundSummary();
        mav.addObject("summaryDetails", summaryDetails);
        
        
        /* Get all inbound transactions */
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaluploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate, searchTerm, 1, 0).size();
            List<batchUploads> uploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate, searchTerm, page, maxResults);
            
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
           
           Integer totalPages = (int)Math.ceil((double)totaluploadedBatches / maxResults);
           mav.addObject("totalPages", totalPages);
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
        
        int page = 1;
        
        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year,month,day);
        
        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");
        String searchTerm = "";
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/outbound");
        
        if("".equals(searchParameters.getsection()) || !"outbound".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setpage(1);
            searchParameters.setsection("outbound");
            searchParameters.setsearchTerm("");
        }
        else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
            page = searchParameters.getpage();
            searchTerm = searchParameters.getsearchTerm();
        }
            
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("searchTerm", searchTerm);
        mav.addObject("originalDate",originalDate);
        
        /* Get system inbound summary */
        systemSummary summaryDetails = transactionOutManager.generateSystemOutboundSummary();
        mav.addObject("summaryDetails", summaryDetails);
        
        
        /* Get all inbound transactions */
        try {
            /* Need to get a list of all uploaded batches */
            Integer totalBatches = transactionOutManager.getAllBatches(fromDate, toDate, searchTerm, 1, 0).size();
            List<batchDownloads> Batches = transactionOutManager.getAllBatches(fromDate, toDate, searchTerm, 1, maxResults);
            
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
           
           Integer totalPages = (int)Math.ceil((double)totalBatches / maxResults);
           mav.addObject("totalPages", totalPages);
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
    public ModelAndView listOutBoundBatches(@RequestParam(value = "page", required = false) Integer page, @RequestParam String searchTerm, @RequestParam Date fromDate, @RequestParam Date toDate,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        if(page == null || page < 1) {
            page = 1;
        }
        
        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year,month,day);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/outbound");
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("searchTerm", searchTerm);
        mav.addObject("originalDate",originalDate);
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setpage(page);
        searchParameters.setsection("outbound");
        searchParameters.setsearchTerm(searchTerm);
        
        /* Get system inbound summary */
        systemSummary summaryDetails = transactionOutManager.generateSystemOutboundSummary();
        mav.addObject("summaryDetails", summaryDetails);
        
        
        /* Get all inbound transactions */
        try {
            /* Need to get a list of all uploaded batches */
            Integer totalBatches = transactionOutManager.getAllBatches(fromDate, toDate, searchTerm, 1, 0).size();
            List<batchDownloads> Batches = transactionOutManager.getAllBatches(fromDate, toDate, searchTerm, page, maxResults);
            
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
           
           Integer totalPages = (int)Math.ceil((double)totalBatches / maxResults);
           mav.addObject("totalPages", totalPages);
        }
        catch (Exception e) {
            throw new Exception("Error occurred viewing the all downloaded batches. Error:"+e.getMessage(),e);
        }
        
        return mav;
    }
    
    
    /**
     * The '/inbound/batch/{batchName}' POST request will retrieve a list of transactions that are associated to the clicked batch 
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
     * The '/inbound/batch/{batchName}' POST request will retrieve a list of transactions that are associated to the clicked batch and
     * match the entered search term.
     *
     * @param batchName	 The name of the batch to retreive transactions for
     * @param searchTerm The term to narrow down the results
     * 
     * @return          The list of inbound batch transactions
     *
     * @Objects	(1) An object containing all the found batch transactions
     *
     * @throws Exception
     */
    @RequestMapping(value = "/inbound/batch/{batchName}", method = RequestMethod.POST)
    public ModelAndView listBatchTransactions(@PathVariable String batchName, @RequestParam String searchTerm) throws Exception {
  
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
                
                if(!"".equals(searchTerm)) {
                    List<Transaction> matchedtransactionList = new ArrayList<Transaction>();
                    
                    if(transactionList.size() > 0) {
                        
                        for(Transaction transaction : transactionList) {
                           
                            boolean matchFound = transactionInManager.searchTransactions(transaction, searchTerm);
                            if(matchFound == true) {
                                matchedtransactionList.add(transaction);
                            }
                        }
                        
                    }
                    
                    if(matchedtransactionList.size() > 100) {
                        mav.addObject("transactions", "");
                        mav.addObject("stilltoomany","stilltoomany");
                        mav.addObject("size", matchedtransactionList.size());
                    }
                    else {
                        mav.addObject("transactions", matchedtransactionList);
                    }
                    
                }
                else {
                    
                    if(transactionList.size() > 100) {
                        mav.addObject("transactions", "");
                        mav.addObject("toomany","toomany");
                        mav.addObject("size", transactionList.size());
                    }
                    else {
                        mav.addObject("transactions", transactionList);
                    }
                }

            }
            catch (Exception e) {
                throw new Exception("(Admin) Error occurred in searching transactions for a sent batch. batchId: "+ batchDetails.getId()+" ERROR: "+e.getMessage(),e);
            }
        }
        
        return mav;
    }
   
    /**
     * The '/outbound/batch/{batchName}' POST request will retrieve a list of transactions that are associated to the clicked batch 
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
     * The '/outbound/batch/{batchName}' POST request will retrieve a list of transactions that are associated to the clicked batch and
     * match the entered search term.
     *
     * @param batchName	 The name of the batch to retreive transactions for
     * @param searchTerm The term to narrow down the results
     * 
     * @return          The list of inbound batch transactions
     *
     * @Objects	(1) An object containing all the found batch transactions
     *
     * @throws Exception
     */
    @RequestMapping(value = "/outbound/batch/{batchName}", method = RequestMethod.POST)
    public ModelAndView listoutboundBatchTransactions(@PathVariable String batchName, @RequestParam String searchTerm) throws Exception {
  
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
                
                if(!"".equals(searchTerm)) {
                    List<Transaction> matchedtransactionList = new ArrayList<Transaction>();
                    
                    if(transactionList.size() > 0) {
                        
                        for(Transaction transaction : transactionList) {
                           
                            boolean matchFound = transactionOutManager.searchTransactions(transaction, searchTerm);
                            if(matchFound == true) {
                                matchedtransactionList.add(transaction);
                            }
                        }
                        
                    }
                    
                    if(matchedtransactionList.size() > 100) {
                        mav.addObject("transactions", "");
                        mav.addObject("stilltoomany","stilltoomany");
                        mav.addObject("size", matchedtransactionList.size());
                    }
                    else {
                        mav.addObject("transactions", matchedtransactionList);
                    }
                    
                }
                else {
                    
                    if(transactionList.size() > 100) {
                        mav.addObject("transactions", "");
                        mav.addObject("toomany","toomany");
                        mav.addObject("size", transactionList.size());
                    }
                    else {
                        mav.addObject("transactions", transactionList);
                    }
                }

           }
            catch (Exception e) {
                throw new Exception("(Admin) Error occurred in searching transactions for a target batch. batchId: "+ batchDetails.getId()+" ERROR: "+e.getMessage(),e);
            }
        }
        
        return mav;
    }
    
    
    /**
     * The '/pending' GET request will retrieve a list of transactions that are waiting to be processed.
     * 
     * @return The list of pending transactions to be processed
     * 
     * @throws Exception
     */
    @RequestMapping(value="/pending", method = RequestMethod.GET)
    public ModelAndView showWaitingMessages(HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/pending");
        
        int page = 1;
        
        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year,month,day);
        
        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");
        String searchTerm = "";
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");

        if("".equals(searchParameters.getsection()) || !"pending".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setpage(1);
            searchParameters.setsection("pending");
            searchParameters.setsearchTerm("");
        }
        else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
            page = searchParameters.getpage();
            searchTerm = searchParameters.getsearchTerm();
        }
            
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("searchTerm", searchTerm);
        mav.addObject("originalDate",originalDate);
        
        /* Get system inbound summary */
        systemSummary summaryDetails = transactionOutManager.generateSystemWaitingSummary();
        mav.addObject("summaryDetails", summaryDetails);
        
        /* Get all waiting transactions */
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaltransactions = transactionOutManager.getTransactionsToProcess(fromDate, toDate, searchTerm, 1, 0).size();
            List<transactionTarget> batchTransactions = transactionOutManager.getTransactionsToProcess(fromDate, toDate, searchTerm, 1, maxResults);
            
            List<Transaction> transactionList = new ArrayList<Transaction>();

            if(!batchTransactions.isEmpty()) {
                for(transactionTarget transaction : batchTransactions) {
                    
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
                    List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(batchDetails.getsourceConfigId(),transportDetails.getId(),3);

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
                    List<transactionRecords> toFields;
                    if(!sourceInfoFormFields.isEmpty()) {
                        toFields = setOutboundFormFields(targetInfoFormFields, records, 0, true, 0);
                    }
                    else {
                        toFields = setOrgDetails(batchDetails.gettargetOrgId());
                    }
                    transactionDetails.settargetOrgFields(toFields);

                    /* get the message type name */
                    configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                    transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                    transactionList.add(transactionDetails);
                }
            }
            

           mav.addObject("transactions", transactionList);
           
           Integer totalPages = (int)Math.ceil((double)totaltransactions / maxResults);
           mav.addObject("totalPages", totalPages);
       }
        catch (Exception e) {
            throw new Exception("(Admin) Error occurred viewing the all pending transactions. Error: "+e.getMessage(),e);
        }
        
        return mav;
        
    }
    
    /**
     * The '/pending' POST request will retrieve a list of transactions that are awaiting to be processed based on a 
     * search term or date range.
     * 
     * @return The list of awaiting transactions to be processed
     * 
     * @throws Exception
     */
    @RequestMapping(value="/pending", method = RequestMethod.POST)
    public ModelAndView searchWaitingMessages(@RequestParam(value = "page", required = false) Integer page, @RequestParam String searchTerm, @RequestParam Date fromDate, @RequestParam Date toDate, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/pending");
        
        if(page == null || page < 1) {
            page = 1;
        }
        
        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year,month,day);
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("searchTerm", searchTerm);
        mav.addObject("originalDate",originalDate);
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setpage(page);
        searchParameters.setsection("waiting");
        searchParameters.setsearchTerm(searchTerm);
            
        
        /* Get system inbound summary */
        systemSummary summaryDetails = transactionOutManager.generateSystemWaitingSummary();
        mav.addObject("summaryDetails", summaryDetails);
        
        /* Get all waiting transactions */
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaltransactions = transactionOutManager.getTransactionsToProcess(fromDate, toDate, searchTerm, 1, 0).size();
            List<transactionTarget> batchTransactions = transactionOutManager.getTransactionsToProcess(fromDate, toDate, searchTerm, page, maxResults);
            
            List<Transaction> transactionList = new ArrayList<Transaction>();

            if(!batchTransactions.isEmpty()) {
                for(transactionTarget transaction : batchTransactions) {
                    
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
                    List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(batchDetails.getsourceConfigId(),transportDetails.getId(),3);

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
                    List<transactionRecords> toFields;
                    if(!sourceInfoFormFields.isEmpty()) {
                        toFields = setOutboundFormFields(targetInfoFormFields, records, 0, true, 0);
                    }
                    else {
                        toFields = setOrgDetails(batchDetails.gettargetOrgId());
                    }
                    transactionDetails.settargetOrgFields(toFields);

                    /* get the message type name */
                    configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                    transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                    transactionList.add(transactionDetails);
                }
                
                if(!"".equals(searchTerm)) {
                    List<Transaction> matchedtransactionList = new ArrayList<Transaction>();
                    
                    if(transactionList.size() > 0) {
                        
                        for(Transaction transaction : transactionList) {
                           
                            boolean matchFound = transactionOutManager.searchTransactions(transaction, searchTerm);
                            if(matchFound == true) {
                                matchedtransactionList.add(transaction);
                            }
                        }
                        
                    }
                    
                    mav.addObject("transactions", matchedtransactionList);
                    
                }
                else {
                    
                    mav.addObject("transactions", transactionList);
                }
            }
           
           
           Integer totalPages = (int)Math.ceil((double)totaltransactions / maxResults);
           mav.addObject("totalPages", totalPages);
        }
        catch (Exception e) {
            throw new Exception("(Admin) Error occurred viewing the all waiting transactions. Error: "+e.getMessage(),e);
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
}
