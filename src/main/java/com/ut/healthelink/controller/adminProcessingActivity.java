/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.controller;

import com.ut.healthelink.model.WSMessagesIn;
import com.ut.healthelink.model.activityReportList;
import com.ut.healthelink.model.Organization;
import com.ut.healthelink.model.Transaction;
import com.ut.healthelink.model.TransportMethod;
import com.ut.healthelink.model.User;
import com.ut.healthelink.model.UserActivity;
import com.ut.healthelink.model.batchDownloads;
import com.ut.healthelink.model.batchUploadSummary;
import com.ut.healthelink.model.batchUploads;
import com.ut.healthelink.model.configuration;
import com.ut.healthelink.model.configurationFormFields;
import com.ut.healthelink.model.configurationMessageSpecs;
import com.ut.healthelink.model.configurationTransport;
import com.ut.healthelink.model.wsMessagesOut;
import com.ut.healthelink.model.custom.TableData;
import com.ut.healthelink.model.custom.TransErrorDetail;
import com.ut.healthelink.model.custom.TransErrorDetailDisplay;
import com.ut.healthelink.model.custom.searchParameters;
import com.ut.healthelink.model.fieldSelectOptions;
import com.ut.healthelink.model.lutables.lu_ProcessStatus;
import com.ut.healthelink.model.pendingDeliveryTargets;
import com.ut.healthelink.model.referralActivityExports;
import com.ut.healthelink.model.systemSummary;
import com.ut.healthelink.model.transactionIn;
import com.ut.healthelink.model.transactionInRecords;
import com.ut.healthelink.model.transactionOutRecords;
import com.ut.healthelink.model.transactionRecords;
import com.ut.healthelink.model.transactionTarget;
import com.ut.healthelink.reference.fileSystem;
import com.ut.healthelink.security.decryptObject;
import com.ut.healthelink.security.encryptObject;
import com.ut.healthelink.service.configurationManager;
import com.ut.healthelink.service.configurationTransportManager;
import com.ut.healthelink.service.fileManager;
import com.ut.healthelink.service.messageTypeManager;
import com.ut.healthelink.service.organizationManager;
import com.ut.healthelink.service.sysAdminManager;
import com.ut.healthelink.service.transactionInManager;
import com.ut.healthelink.service.transactionOutManager;
import com.ut.healthelink.service.userManager;
import com.ut.healthelink.webServices.WSManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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

    @Autowired
    private fileManager filemanager;
    
    @Autowired
    private  WSManager wsmanager;
    
    private String topSecret = "Hello123JavaTomcatMysqlDPHSystem2016";

    /**
     * The private maxResults variable will hold the number of results to show per list page.
     */
    private static int maxResults = 10;

    private String archivePath = "/bowlink/archivesIn/";
    
    
    /**
     * 
     */
    @RequestMapping(value = "/activityReport", method = RequestMethod.GET)
    public ModelAndView activityReport(HttpSession session) throws Exception {

        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);
        
        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/activityReport");
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");
        
        if ("".equals(searchParameters.getsection()) || !"activityReport".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setsection("activityReport");
        } else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
        }
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);
        
        /* Get the list of batches for the passed in dates */
        List<Integer> batchIds = transactionInManager.getBatchesForReport(fromDate, toDate);
        
        /* Get totals */
        BigInteger totalReferrals = transactionInManager.getReferralCount(batchIds);
        mav.addObject("totalReferrals", totalReferrals);
        
        
        BigInteger totalFBReports = transactionInManager.getFeedbackReportCount(batchIds);
        mav.addObject("totalFBReports", totalFBReports);
        
        BigInteger totalRejected = transactionInManager.getRejectedCount(fromDate, toDate);
        mav.addObject("totalRejected", totalRejected);
        
        /* Get FB List */
        List<activityReportList> feedbackReportList = transactionInManager.getFeedbackReportList(batchIds);
        mav.addObject("feedbackReportList", feedbackReportList);
        
        Map<String, BigInteger> fbMade = new HashMap<String, BigInteger>();
        
        if(feedbackReportList != null && !feedbackReportList.isEmpty()) {
            for(activityReportList fb : feedbackReportList) {

                if(fbMade.containsKey(fb.getMessageType())) {
                    BigInteger currTotal = fbMade.get(fb.getMessageType());
                    currTotal = currTotal.add(fb.getTotal());
                    fbMade.put(fb.getMessageType(),currTotal);
                }
                else {
                    fbMade.put(fb.getMessageType(), fb.getTotal());
                }
            }
        }
        mav.addObject("fbTypesMade", fbMade);
        
        /* Get Referral List */
        List<activityReportList> referralList = transactionInManager.getReferralList(batchIds);
        mav.addObject("referralList", referralList);      
        
        Map<String, BigInteger> referralsMade = new HashMap<String, BigInteger>();
        
        if(referralList != null && !referralList.isEmpty()) {
            for(activityReportList referral : referralList) {

                if(referralsMade.containsKey(referral.getMessageType())) {
                    BigInteger currTotal = referralsMade.get(referral.getMessageType());
                    currTotal = currTotal.add(referral.getTotal());
                    referralsMade.put(referral.getMessageType(),currTotal);
                }
                else {
                    referralsMade.put(referral.getMessageType(), referral.getTotal());
                }
            }
        }
        mav.addObject("referralTypesMade", referralsMade);
        
         /* Get the activity status totals */
        List<Integer> activityStatusTotals = transactionInManager.getActivityStatusTotals(batchIds);
        if (activityStatusTotals.size() != 0) {
        	mav.addObject("totalCompleted", activityStatusTotals.get(0));
        	mav.addObject("totalEnrolled", activityStatusTotals.get(1));
        } else  {
        	mav.addObject("totalCompleted", 0);
        	mav.addObject("totalEnrolled", 0);
        }
        
        
        return mav;

    }
    
    /**
     * 
     */
    @RequestMapping(value = "/activityReport", method = RequestMethod.POST)
    public ModelAndView activityReport(@RequestParam Date fromDate, @RequestParam Date toDate, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/activityReport");
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);

        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setsection("activityReport");
        
         /* Get the list of batches for the passed in dates */
        List<Integer> batchIds = transactionInManager.getBatchesForReport(fromDate, toDate);
        
        /* Get totals */
        BigInteger totalReferrals = transactionInManager.getReferralCount(batchIds);
        mav.addObject("totalReferrals", totalReferrals);
        
        BigInteger totalFBReports = transactionInManager.getFeedbackReportCount(batchIds);
        mav.addObject("totalFBReports", totalFBReports);
        
        BigInteger totalRejected = transactionInManager.getRejectedCount(fromDate, toDate);
        mav.addObject("totalRejected", totalRejected);
        
        /* Get FB List */
        List<activityReportList> feedbackReportList = transactionInManager.getFeedbackReportList(batchIds);
        mav.addObject("feedbackReportList", feedbackReportList);
        
        Map<String, BigInteger> fbMade = new HashMap<String, BigInteger>();
        
        if(feedbackReportList != null && !feedbackReportList.isEmpty()) {
            for(activityReportList fb : feedbackReportList) {

                if(fbMade.containsKey(fb.getMessageType())) {
                    BigInteger currTotal = fbMade.get(fb.getMessageType());
                    currTotal = currTotal.add(fb.getTotal());
                    fbMade.put(fb.getMessageType(),currTotal);
                }
                else {
                    fbMade.put(fb.getMessageType(), fb.getTotal());
                }
            }
        }
        mav.addObject("fbTypesMade", fbMade);
        
        /* Get Referral List */
        List<activityReportList> referralList = transactionInManager.getReferralList(batchIds);
        mav.addObject("referralList", referralList);      
        
        Map<String, BigInteger> referralsMade = new HashMap<String, BigInteger>();
        
        if(referralList != null && !referralList.isEmpty()) {
            for(activityReportList referral : referralList) {

                if(referralsMade.containsKey(referral.getMessageType())) {
                    BigInteger currTotal = referralsMade.get(referral.getMessageType());
                    currTotal = currTotal.add(referral.getTotal());
                    referralsMade.put(referral.getMessageType(),currTotal);
                }
                else {
                    referralsMade.put(referral.getMessageType(), referral.getTotal());
                }
            }
        }
        mav.addObject("referralTypesMade", referralsMade);
        
        /* Get the activity status totals */
        List<Integer> activityStatusTotals = transactionInManager.getActivityStatusTotals(batchIds);
        if (activityStatusTotals.size() != 0) {
        	mav.addObject("totalCompleted", activityStatusTotals.get(0));
        	mav.addObject("totalEnrolled", activityStatusTotals.get(1));
        } else  {
        	mav.addObject("totalCompleted", 0);
        	mav.addObject("totalEnrolled", 0);
        }
        return mav;

    }

    /**
     * The '/inbound' GET request will serve up the existing list of generated referrals and feedback reports
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return The list of inbound batch list
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
        Date originalDate = new Date(year, month, day);

        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");

        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/inbound");

        if ("".equals(searchParameters.getsection()) || !"inbound".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setsection("inbound");
        } else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
        }

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);

        /* Get system inbound summary */
        systemSummary summaryDetails = transactionInManager.generateSystemInboundSummary();
        mav.addObject("summaryDetails", summaryDetails);

        /* Get all inbound transactions */
        try {

            Integer fetchCount = 0;
            List<batchUploads> uploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate, fetchCount);

            if (!uploadedBatches.isEmpty()) {
                
                //we can map the process status so we only have to query once
                List<lu_ProcessStatus> processStatusList = sysAdminManager.getAllProcessStatus();
                Map<Integer, String> psMap = new HashMap<Integer, String>();
                for (lu_ProcessStatus ps : processStatusList) {
                    psMap.put(ps.getId(), ps.getDisplayCode());
                }

                //same with transport method names
                List<TransportMethod> transporthMethods = configurationTransportManager.getTransportMethods(Arrays.asList(0, 1));
                Map<Integer, String> tmMap = new HashMap<Integer, String>();
                for (TransportMethod tms : transporthMethods) {
                    tmMap.put(tms.getId(), tms.getTransportMethod());
                }

                //if we have lots of organization in the future we can tweak this to narrow down to orgs with batches
                List<Organization> organizations = organizationmanager.getOrganizations();
                Map<Integer, String> orgMap = new HashMap<Integer, String>();
                for (Organization org : organizations) {
                    orgMap.put(org.getId(), org.getOrgName());
                }

                //same goes for users
                List<User> users = usermanager.getAllUsers();
                Map<Integer, String> userMap = new HashMap<Integer, String>();
                for (User user : users) {
                    userMap.put(user.getId(), (user.getFirstName() + " " + user.getLastName()));
                }

                for (batchUploads batch : uploadedBatches) {
                    
                    Integer totalOpen = 0;
                    Integer totalClosed = 0;
                    
                    //Get the upload type (Referral or Feedback Report
                    List<transactionIn> transactions = transactionInManager.getBatchTransactions(batch.getId(), 0);
                    
                    if(!transactions.isEmpty()) {
                        
                        transactionIn transactionDetails = transactionInManager.getTransactionDetails(transactions.get(0).getId());
                        if(transactionDetails.gettransactionTargetId() > 0) {
                            batch.setUploadType("Feedback Report");
                            
                            transactionTarget targetDetails = transactionInManager.getTransactionTargetDetails(transactionDetails.gettransactionTargetId());
                            
                            if(targetDetails != null) {
                                /* Get the originating referall batch ID */
                                batchUploads referringbatch = transactionInManager.getBatchDetails(targetDetails.getbatchUploadId());
                                batch.setReferringBatch(referringbatch.getutBatchName());
                            }
                            else {
                                batch.setReferringBatch("Not Found");
                            }
                            
                        }
                        else {
                            batch.setUploadType("Referral");
                            
                            for(transactionIn transaction : transactions) {
                                if(transaction.getmessageStatus() == 1) {
                                    totalOpen+=1;
                                }
                                else {
                                    totalClosed += 1;
                                }
                            }
                            
                            batch.setTotalOpen(totalOpen);
                            batch.setTotalClosed(totalClosed);
                        }
                    }
                    else {
                        batch.setUploadType("Referral");
                    }
                    
                    //the count is in totalRecordCount already, can skip re-count
                    // batch.settotalTransactions(transactionInManager.getRecordCounts(batch.getId(), statusIds, false, false));
                    batch.setstatusValue(psMap.get(batch.getstatusId()));

                    batch.setorgName(orgMap.get(batch.getOrgId()));

                    batch.settransportMethod(tmMap.get(batch.gettransportMethodId()));

                    batch.setusersName(userMap.get(batch.getuserId()));

                }
            }

            mav.addObject("batches", uploadedBatches);

        } catch (Exception e) {
            throw new Exception("Error occurred viewing the all uploaded batches.", e);
        }

        return mav;

    }

    /**
     * The '/inbound' POST request will serve up the existing list of generated referrals and feedback reports based on a search or date
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return The list of inbound batch list
     *
     * @Objects	(1) An object containing all the found batches
     *
     * @throws Exception
     */
    @RequestMapping(value = "/inbound", method = RequestMethod.POST)
    public ModelAndView listInBoundBatches(@RequestParam Date fromDate, @RequestParam Date toDate, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/inbound");

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);

        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setsection("inbound");

        /* Get system inbound summary */
        systemSummary summaryDetails = transactionInManager.generateSystemInboundSummary();
        mav.addObject("summaryDetails", summaryDetails);

        /* Get all inbound transactions */ 
        try {

            Integer fetchCount = 0;
            /* Need to get a list of all uploaded batches */
            List<batchUploads> uploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate, fetchCount);

            if (!uploadedBatches.isEmpty()) {
                //we can map the process status so we only have to query once
                List<lu_ProcessStatus> processStatusList = sysAdminManager.getAllProcessStatus();
                Map<Integer, String> psMap = new HashMap<Integer, String>();
                for (lu_ProcessStatus ps : processStatusList) {
                    psMap.put(ps.getId(), ps.getDisplayCode());
                }

                //same with transport method names
                List<TransportMethod> transporthMethods = configurationTransportManager.getTransportMethods(Arrays.asList(0, 1));
                Map<Integer, String> tmMap = new HashMap<Integer, String>();
                for (TransportMethod tms : transporthMethods) {
                    tmMap.put(tms.getId(), tms.getTransportMethod());
                }

                //if we have lots of organization in the future we can tweak this to narrow down to orgs with batches
                List<Organization> organizations = organizationmanager.getOrganizations();
                Map<Integer, String> orgMap = new HashMap<Integer, String>();
                for (Organization org : organizations) {
                    orgMap.put(org.getId(), org.getOrgName());
                }

                //same goes for users
                List<User> users = usermanager.getAllUsers();
                Map<Integer, String> userMap = new HashMap<Integer, String>();
                for (User user : users) {
                    userMap.put(user.getId(), (user.getFirstName() + " " + user.getLastName()));
                }

                for (batchUploads batch : uploadedBatches) {
                    
                    Integer totalOpen = 0;
                    Integer totalClosed = 0;
                    
                    //Get the upload type (Referral or Feedback Report
                    List<transactionIn> transactions = transactionInManager.getBatchTransactions(batch.getId(), 0);
                    
                    if(!transactions.isEmpty()) {
                        transactionIn transactionDetails = transactionInManager.getTransactionDetails(transactions.get(0).getId());
                        if(transactionDetails.gettransactionTargetId() > 0) {
                            batch.setUploadType("Feedback Report");
                            
                            transactionTarget targetDetails = transactionInManager.getTransactionTargetDetails(transactionDetails.gettransactionTargetId());
                            
                            if(targetDetails != null) {
                                /* Get the originating referall batch ID */
                                batchUploads referringbatch = transactionInManager.getBatchDetails(targetDetails.getbatchUploadId());
                                batch.setReferringBatch(referringbatch.getutBatchName());
                            }
                            else {
                                batch.setReferringBatch("Not Found");
                            }
                            
                        }
                        else {
                            batch.setUploadType("Referral");
                            
                            for(transactionIn transaction : transactions) {
                                if(transaction.getmessageStatus() == 1) {
                                    totalOpen+=1;
                                }
                                else {
                                    totalClosed += 1;
                                }
                            }
                            
                            batch.setTotalOpen(totalOpen);
                            batch.setTotalClosed(totalClosed);
                        }
                    }
                    else {
                        batch.setUploadType("Referral");
                        batch.setTotalOpen(totalOpen);
                        batch.setTotalClosed(totalClosed);
                    }
                    
                    batch.setstatusValue(psMap.get(batch.getstatusId()));

                    batch.setorgName(orgMap.get(batch.getOrgId()));

                    batch.settransportMethod(tmMap.get(batch.gettransportMethodId()));

                    batch.setusersName(userMap.get(batch.getuserId()));
                }
            }

            mav.addObject("batches", uploadedBatches);

        } catch (Exception e) {
            throw new Exception("Error occurred viewing the all uploaded batches.", e);
        }

        return mav;
    }

    /**
     * The '/outbound' GET request will serve up the existing list of generated referrals and feedback reports to for the target
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return The list of inbound batch list
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
        Date originalDate = new Date(year, month, day);

        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");

        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/outbound");

        if ("".equals(searchParameters.getsection()) || !"outbound".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setsection("outbound");
        } else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
        }

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);

        /* Get system oubound summary */
        systemSummary summaryDetails = transactionOutManager.generateSystemOutboundSummary();
        mav.addObject("summaryDetails", summaryDetails);

        /* Get all inbound transactions */
        try {
            /* Need to get a list of all uploaded batches */
            List<batchDownloads> Batches = transactionOutManager.getAllBatches(fromDate, toDate);

            List<Integer> statusIds = new ArrayList();

            if (!Batches.isEmpty()) {

                //we can map the process status so we only have to query once
                List<lu_ProcessStatus> processStatusList = sysAdminManager.getAllProcessStatus();
                Map<Integer, String> psMap = new HashMap<Integer, String>();
                for (lu_ProcessStatus ps : processStatusList) {
                    psMap.put(ps.getId(), ps.getDisplayCode());
                }

                //same with transport method names
                List<TransportMethod> transporthMethods = configurationTransportManager.getTransportMethods(Arrays.asList(0, 1));
                Map<Integer, String> tmMap = new HashMap<Integer, String>();
                for (TransportMethod tms : transporthMethods) {
                    tmMap.put(tms.getId(), tms.getTransportMethod());
                }

                //if we have lots of organization in the future we can tweak this to narrow down to orgs with batches
                List<Organization> organizations = organizationmanager.getOrganizations();
                Map<Integer, String> orgMap = new HashMap<Integer, String>();
                for (Organization org : organizations) {
                    orgMap.put(org.getId(), org.getOrgName());
                }

                //same goes for users
                List<User> users = usermanager.getAllUsers();
                Map<Integer, String> userMap = new HashMap<Integer, String>();
                for (User user : users) {
                    userMap.put(user.getId(), (user.getFirstName() + " " + user.getLastName()));
                }

                for (batchDownloads batch : Batches) {

                    if (batch.gettransportMethodId() == 1 || batch.gettransportMethodId() == 5 || batch.gettransportMethodId() == 6) {
                        String fileDownloadExt = batch.getoutputFIleName().substring(batch.getoutputFIleName().lastIndexOf(".") + 1);
                        String newfileName = new StringBuilder().append(batch.getutBatchName()).append(".").append(fileDownloadExt).toString();

                        batch.setoutputFIleName(newfileName);
                    }

                    //batch.settotalTransactions(transactionInManager.getRecordCounts(batch.getId(), statusIds, true, false));
                    batch.setstatusValue(psMap.get(batch.getstatusId()));

                    batch.setorgName(orgMap.get(batch.getOrgId()));

                    batch.settransportMethod(tmMap.get(batch.gettransportMethodId()));

                    batch.setusersName(userMap.get(batch.getuserId()));

                    /* Get from batch information */
                    List<transactionTarget> transactionTargets = transactionOutManager.getTransactionsByBatchDLId(batch.getId());

                    for (transactionTarget transactiontarget : transactionTargets) {
                        batchUploads batchUploadDetails = transactionInManager.getBatchDetails(transactiontarget.getbatchUploadId());

                        batch.setFromBatchName(batchUploadDetails.getutBatchName());
                        if (batchUploadDetails.gettransportMethodId() == 5 || batchUploadDetails.gettransportMethodId() == 1) {
                            String fileExt = batchUploadDetails.getoriginalFileName().substring(batchUploadDetails.getoriginalFileName().lastIndexOf(".") + 1);
                            String newsrcfileName = new StringBuilder().append(batchUploadDetails.getutBatchName()).append(".").append(fileExt).toString();
                            batch.setFromBatchFile(newsrcfileName);
                        }
                        batch.setFromOrgId(batchUploadDetails.getOrgId());

                    }

                }
            }

            mav.addObject("batches", Batches);

        } catch (Exception e) {
            throw new Exception("Error occurred viewing the all downloaded batches. Error:" + e.getMessage(), e);
        }

        return mav;

    }

    /**
     * The '/outbound' POST request will serve up the existing list of generated referrals and feedback reports for a target based on a search or date
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return The list of inbound batch list
     *
     * @Objects	(1) An object containing all the found batches
     *
     * @throws Exception
     */
    @RequestMapping(value = "/outbound", method = RequestMethod.POST)
    public ModelAndView listOutBoundBatches(@RequestParam Date fromDate, @RequestParam Date toDate, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/outbound");

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);

        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setsection("outbound");

        /* Get system oubound summary */
        systemSummary summaryDetails = transactionOutManager.generateSystemOutboundSummary();
        mav.addObject("summaryDetails", summaryDetails);

        /* Get all oubound transactions */
        try {
            /* Need to get a list of all uploaded batches */
            List<batchDownloads> Batches = transactionOutManager.getAllBatches(fromDate, toDate);

            List<Integer> statusIds = new ArrayList();

            //we can map the process status so we only have to query once
            List<lu_ProcessStatus> processStatusList = sysAdminManager.getAllProcessStatus();
            Map<Integer, String> psMap = new HashMap<Integer, String>();
            for (lu_ProcessStatus ps : processStatusList) {
                psMap.put(ps.getId(), ps.getDisplayCode());
            }

            //same with transport method names
            List<TransportMethod> transporthMethods = configurationTransportManager.getTransportMethods(Arrays.asList(0, 1));
            Map<Integer, String> tmMap = new HashMap<Integer, String>();
            for (TransportMethod tms : transporthMethods) {
                tmMap.put(tms.getId(), tms.getTransportMethod());
            }

            //if we have lots of organization in the future we can tweak this to narrow down to orgs with batches
            List<Organization> organizations = organizationmanager.getOrganizations();
            Map<Integer, String> orgMap = new HashMap<Integer, String>();
            for (Organization org : organizations) {
                orgMap.put(org.getId(), org.getOrgName());
            }

            //same goes for users
            List<User> users = usermanager.getAllUsers();
            Map<Integer, String> userMap = new HashMap<Integer, String>();
            for (User user : users) {
                userMap.put(user.getId(), (user.getFirstName() + " " + user.getLastName()));
            }

            if (!Batches.isEmpty()) {
                for (batchDownloads batch : Batches) {

                    if (batch.gettransportMethodId() == 1 || batch.gettransportMethodId() == 5 || batch.gettransportMethodId() == 6) {
                        String fileDownloadExt = batch.getoutputFIleName().substring(batch.getoutputFIleName().lastIndexOf(".") + 1);
                        String newfileName = new StringBuilder().append(batch.getutBatchName()).append(".").append(fileDownloadExt).toString();

                        batch.setoutputFIleName(newfileName);
                    }

                    //batch.settotalTransactions(transactionInManager.getRecordCounts(batch.getId(), statusIds, true, false));
                    batch.setstatusValue(psMap.get(batch.getstatusId()));

                    batch.setorgName(orgMap.get(batch.getOrgId()));

                    batch.settransportMethod(tmMap.get(batch.gettransportMethodId()));

                    batch.setusersName(userMap.get(batch.getuserId()));

                    /* Get from batch information */
                    List<transactionTarget> transactionTargets = transactionOutManager.getTransactionsByBatchDLId(batch.getId());

                    for (transactionTarget transactiontarget : transactionTargets) {
                        batchUploads batchUploadDetails = transactionInManager.getBatchDetails(transactiontarget.getbatchUploadId());

                        batch.setFromBatchName(batchUploadDetails.getutBatchName());
                        if (batchUploadDetails.gettransportMethodId() == 5 || batchUploadDetails.gettransportMethodId() == 1) {
                            String fileExt = batchUploadDetails.getoriginalFileName().substring(batchUploadDetails.getoriginalFileName().lastIndexOf(".") + 1);
                            String newsrcfileName = new StringBuilder().append(batchUploadDetails.getutBatchName()).append(".").append(fileExt).toString();
                            batch.setFromBatchFile(newsrcfileName);
                        }
                        batch.setFromOrgId(batchUploadDetails.getOrgId());

                    }

                }
            }

            mav.addObject("batches", Batches);

        } catch (Exception e) {
            throw new Exception("Error occurred viewing the all downloaded batches. Error:" + e.getMessage(), e);
        }

        return mav;
    }

    /**
     * The '/inbound/batch/{batchName}' GET request will retrieve a list of transactions that are associated to the clicked batch
     *
     * @param batchName	The name of the batch to retreive transactions for
     * @return The list of inbound batch transactions
     *
     * @Objects	(1) An object containing all the found batch transactions
     *
     * @throws Exception
     */
    @RequestMapping(value = "/{path}/batch/{batchName}", method = RequestMethod.GET)
    public ModelAndView listBatchTransactions(@PathVariable String path, @PathVariable String batchName) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/transactions");
        mav.addObject("page", path);

        /* Get the details of the batch */
        batchUploads batchDetails = transactionInManager.getBatchDetailsByBatchName(batchName);

        if (batchDetails != null) {

            Organization orgDetails = organizationmanager.getOrganizationById(batchDetails.getOrgId());
            batchDetails.setorgName(orgDetails.getOrgName());

            mav.addObject("batchDetails", batchDetails);

            try {
                /* Get all the transactions for the batch */
                List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batchDetails.getId(), 0);

                List<Transaction> transactionList = new ArrayList<Transaction>();

                //we can map the process status so we only have to query once
                List<lu_ProcessStatus> processStatusList = sysAdminManager.getAllProcessStatus();
                Map<Integer, String> psMap = new HashMap<Integer, String>();
                for (lu_ProcessStatus ps : processStatusList) {
                    psMap.put(ps.getId(), ps.getDisplayCode());
                }

                for (transactionIn transaction : batchTransactions) {

                    Transaction transactionDetails = new Transaction();
                    transactionDetails.settransactionRecordId(transaction.getId());
                    transactionDetails.setstatusId(transaction.getstatusId());
                    transactionDetails.setdateSubmitted(transaction.getdateCreated());
                    transactionDetails.setconfigId(transaction.getconfigId());
                    transactionDetails.setmessageStatus(transaction.getmessageStatus());
                    
                    transactionDetails.setstatusValue(psMap.get(transaction.getstatusId()));

                    transactionInRecords records = transactionInManager.getTransactionRecords(transaction.getId());

                    /* Get a list of form fields */
                    configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transaction.getconfigId());
                    List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(), transportDetails.getId(), 3);

                    /* Set all the transaction TARGET fields */
                    List<transactionRecords> toFields;
                    if (!targetInfoFormFields.isEmpty()) {
                        toFields = setOutboundFormFields(targetInfoFormFields, records, 0, 0, true, 0);

                        if ("".equals(toFields.get(0).getFieldValue()) || toFields.get(0).getFieldValue() == null) {
                            toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transaction.getId()).gettargetOrgId());
                        }
                    } else {
                        toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transaction.getId()).gettargetOrgId());
                    }
                    transactionDetails.settargetOrgFields(toFields);

                    /* get the message type name */
                    configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                    transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                    configurationMessageSpecs messageSpecs = configurationManager.getMessageSpecs(transaction.getconfigId());

                    if (messageSpecs.getrptField1() > 0) {

                        configurationFormFields formField1 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(), transportDetails.getId(), messageSpecs.getrptField1());

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

                    if (messageSpecs.getrptField2() > 0) {

                        configurationFormFields formField2 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(), transportDetails.getId(), messageSpecs.getrptField2());

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

                    if (messageSpecs.getrptField3() > 0) {

                        configurationFormFields formField3 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(), transportDetails.getId(), messageSpecs.getrptField3());

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

                    if (messageSpecs.getrptField4() > 0) {

                        configurationFormFields formField4 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(), transportDetails.getId(), messageSpecs.getrptField4());

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

                mav.addObject("transactions", transactionList);

            } catch (Exception e) {
                throw new Exception("(Admin) Error occurred in getting transactions for a sent batch. batchId: " + batchDetails.getId() + " ERROR: " + e.getMessage(), e);
            }
        }

        return mav;
    }

    /**
     * The '/outbound/batch/{batchName}' GET request will retrieve a list of transactions that are associated to the clicked batch
     *
     * @param batchName	The name of the batch to retreive transactions for
     * @return The list of outbound batch transactions
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

        if (batchDetails != null) {

            Organization orgDetails = organizationmanager.getOrganizationById(batchDetails.getOrgId());
            batchDetails.setorgName(orgDetails.getOrgName());

            mav.addObject("batchDetails", batchDetails);

            try {
                /* Get all the transactions for the batch */
                List<transactionTarget> batchTransactions = transactionOutManager.getInboxBatchTransactions(batchDetails.getId(), 0);

                List<Transaction> transactionList = new ArrayList<Transaction>();
                
                //we can map the process status so we only have to query once
                List<lu_ProcessStatus> processStatusList = sysAdminManager.getAllProcessStatus();
                Map<Integer, String> psMap = new HashMap<Integer, String>();
                for (lu_ProcessStatus ps : processStatusList) {
                    psMap.put(ps.getId(), ps.getDisplayCode());
                }

                for (transactionTarget transaction : batchTransactions) {

                    Transaction transactionDetails = new Transaction();
                    transactionDetails.settransactionRecordId(transaction.getId());
                    transactionDetails.setstatusId(transaction.getstatusId());
                    transactionDetails.setdateSubmitted(transaction.getdateCreated());
                    transactionDetails.setconfigId(transaction.getconfigId());

                    transactionDetails.setstatusValue(psMap.get(transaction.getstatusId()));

                    transactionOutRecords records = transactionOutManager.getTransactionRecords(transaction.getId());

                    /* Get a list of form fields */
                    configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transaction.getconfigId());
                    List<configurationFormFields> sourceInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(), transportDetails.getId(), 1);

                    /* Set all the transaction TARGET fields */
                    List<transactionRecords> fromFields;
                    if (!sourceInfoFormFields.isEmpty()) {
                        fromFields = setInboxFormFields(sourceInfoFormFields, records, 0, true, 0);
                        
                        int sourceOrgAsInt = 0;
                    
                        try {
                            sourceOrgAsInt = Integer.parseInt(fromFields.get(0).getFieldValue().trim());
                        } catch (Exception e) {
                            sourceOrgAsInt = 0;
                        }

                        if(sourceOrgAsInt > 0) {
                            /* Make sure the org exists */
                            Organization fromorgDetails = organizationmanager.getOrganizationById(sourceOrgAsInt);
                            if(fromorgDetails.getId() == sourceOrgAsInt) {
                                fromFields = setOrgDetails(sourceOrgAsInt);
                            }
                            else {
                                fromFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transaction.getId()).getsourceOrgId());
                            }
                        }
                        else if ("".equals(fromFields.get(0).getFieldValue()) || fromFields.get(0).getFieldValue() == null) {
                            fromFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transaction.getId()).getsourceOrgId());
                        }
                        
                    } else {
                        fromFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transaction.getId()).getsourceOrgId());
                    }
                    transactionDetails.setsourceOrgFields(fromFields);

                    /* get the message type name */
                    configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                    transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                    transactionList.add(transactionDetails);
                }

                mav.addObject("transactions", transactionList);

            } catch (Exception e) {
                throw new Exception("(Admin) Error occurred in getting transactions for a target batch. batchId: " + batchDetails.getId() + " ERROR: " + e.getMessage(), e);
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
    @RequestMapping(value = "/pending", method = RequestMethod.GET)
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

            if (!batchTransactions.isEmpty()) {
                for (ListIterator iter = batchTransactions.listIterator(); iter.hasNext();) {

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

        } catch (Exception e) {
            throw new Exception("(Admin) Error occurred viewing the all pending transactions. Error: " + e.getMessage(), e);
        }

        return mav;

    }

    /**
     * The '/pending/{orgId}' GET method will return all pending output transactions based on the the passed in organization Id. The will return pending transactions grouped by message types for the passed in organziation
     *
     *
     */
    @RequestMapping(value = "/pending/messageTypes", method = RequestMethod.POST)
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

            if (!batchTransactions.isEmpty()) {
                for (ListIterator iter = batchTransactions.listIterator(); iter.hasNext();) {

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

        } catch (Exception e) {
            throw new Exception("(Admin) Error occurred viewing the all pending transactions. Error: " + e.getMessage(), e);
        }

        return mav;

    }

    /**
     * The '/pending/{orgId}/{messageTypeId}' GET method will return all pending output transactions based on the the passed in organization Id and selected message type.
     *
     */
    @RequestMapping(value = "/pending/transactions", method = RequestMethod.POST)
    public ModelAndView viewOrgPendingMessages(@RequestParam(value = "orgId", required = true) int orgId, @RequestParam(value = "messageTypeId", required = true) int messageTypeId, @RequestParam(value = "fromDate", required = false) Date fromDate, @RequestParam(value = "toDate", required = false) Date toDate) throws Exception {

        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);

        if (fromDate == null) {
            fromDate = getMonthDate("START");
        }
        if (toDate == null) {
            toDate = getMonthDate("END");
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/pendingTransactions");

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);
        mav.addObject("orgId", orgId);
        mav.addObject("messageTypeId", messageTypeId);

        /* Get all waiting transactions */
        try {

            mav.addObject("messageType", messagetypemanager.getMessageTypeById(messageTypeId).getName());
            mav.addObject("targetOrg", organizationmanager.getOrganizationById(orgId).getOrgName());

            /* Need to get a list of all uploaded batches */
            List<transactionTarget> transactions = transactionOutManager.getPendingDeliveryTransactions(orgId, messageTypeId, fromDate, toDate);

            List<Transaction> transactionList = new ArrayList<Transaction>();

            if (!transactions.isEmpty()) {
                for (transactionTarget transaction : transactions) {

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
                    List<configurationFormFields> sourceInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(batchDetails.getsourceConfigId(), transportDetails.getId(), 1);
                    List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(batchDetails.getsourceConfigId(), transportDetails.getId(), 5);

                    /* Set all the transaction SOURCE fields */
                    List<transactionRecords> fromFields;
                    if (!sourceInfoFormFields.isEmpty()) {
                        fromFields = setOutboundFormFields(sourceInfoFormFields, records, 0, 0, true, 0);
                    } else {
                        fromFields = setOrgDetails(batchDetails.getsourceOrgId());
                    }
                    transactionDetails.setsourceOrgFields(fromFields);

                    /* Set all the transaction TARGET fields */
                    List<transactionRecords> patientFields;
                    patientFields = setOutboundFormFields(patientInfoFormFields, records, 0, 0, true, 0);
                    transactionDetails.setpatientFields(patientFields);

                    /* get the message type name */
                    configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                    transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                    transactionList.add(transactionDetails);
                }

                mav.addObject("transactions", transactionList);
            }

        } catch (Exception e) {
            throw new Exception("(Admin) Error occurred viewing the all pending transactions. Error: " + e.getMessage(), e);
        }

        return mav;

    }

    /**
     * The '/ViewMessageDetails' POST request will display the selected transaction details. This page is served up from inbox batch transaction list page. So the form will be readOnly.
     *
     * @param transactionId The id of the selected transaction
     * @param fromPage The page the request is coming from (inbox)
     *
     * @return this request will return the messageDetailsForm
     */
    @RequestMapping(value = "/ViewMessageDetails", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView showInboxMessageDetails(@RequestParam(value = "Type", required = true) Integer Type, @RequestParam(value = "transactionId", required = true) Integer transactionId, @RequestParam(value = "configId", required = true) Integer configId) throws Exception {

        ModelAndView mav = new ModelAndView();
        
        try {

            Transaction transaction = new Transaction();

            /* Type = 1 (Source) */
            if (Type == 1) {

                transactionIn transactionInfo = transactionInManager.getTransactionDetails(transactionId);
                if (transactionInfo == null) { // this will be null if batch is reset - the same transactions will not be available 
                    return mav;
                }
                /* Get the configuration details */
                configuration configDetails = configurationManager.getConfigurationById(transactionInfo.getconfigId());

                /* Get a list of form fields */
                /*configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transactionInfo.getconfigId(), 2);*/
                configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transactionInfo.getconfigId());
                List<configurationFormFields> senderInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 1);
                List<configurationFormFields> senderProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 2);
                List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 3);
                List<configurationFormFields> targetProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 4);
                List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 5);
                List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 6);

                transactionInRecords records = null;

                batchUploads batchInfo = transactionInManager.getBatchDetails(transactionInfo.getbatchId());

                records = transactionInManager.getTransactionRecords(transactionId);

                /* Set all the transaction SOURCE ORG fields */
                List<transactionRecords> fromFields;
                if (!senderInfoFormFields.isEmpty()) {
                    fromFields = setOutboundFormFields(senderInfoFormFields, records, transactionInfo.getconfigId(), 0, true, 0);
                } else {
                    fromFields = setOrgDetails(batchInfo.getOrgId());
                }
                transaction.setsourceOrgFields(fromFields);

                /* Set all the transaction SOURCE PROVIDER fields */
                List<transactionRecords> fromProviderFields = setOutboundFormFields(senderProviderFormFields, records, transactionInfo.getconfigId(), 0, true, 0);
                transaction.setsourceProviderFields(fromProviderFields);

                /* Set all the transaction TARGET fields */
                List<transactionRecords> toFields;
                if (!targetInfoFormFields.isEmpty()) {
                    toFields = setOutboundFormFields(targetInfoFormFields, records, transactionInfo.getconfigId(), 0, true, 0);
                    
                    int targetOrgAsInt;

                    try {
                        targetOrgAsInt = Integer.parseInt(toFields.get(0).getFieldValue().trim());
                    } catch (Exception e) {
                        targetOrgAsInt = 0;
                    }

                    if(targetOrgAsInt > 0) {
                        /* Make sure the org exists */
                        Organization orgDetails = organizationmanager.getOrganizationById(targetOrgAsInt);
                        if(orgDetails.getId() == targetOrgAsInt) {
                            toFields = setOrgDetails(targetOrgAsInt);
                        }
                        else {
                            toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transactionInfo.getId()).gettargetOrgId());
                        }
                    }
                    else if ("".equals(toFields.get(0).getFieldValue()) || toFields.get(0).getFieldValue() == null) {
                        toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transactionInfo.getId()).gettargetOrgId());
                    }
                    
                } else {
                    toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transactionInfo.getId()).gettargetOrgId());
                }
                transaction.settargetOrgFields(toFields);


                /* Set all the transaction TARGET PROVIDER fields */
                List<transactionRecords> toProviderFields = setOutboundFormFields(targetProviderFormFields, records, transactionInfo.getconfigId(), 0, true, 0);
                transaction.settargetProviderFields(toProviderFields);

                /* Set all the transaction PATIENT fields */
                List<transactionRecords> patientFields = setOutboundFormFields(patientInfoFormFields, records, transactionInfo.getconfigId(), 0, true, 0);
                transaction.setpatientFields(patientFields);

                /* Set all the transaction DETAIL fields */
                List<transactionRecords> detailFields = setOutboundFormFields(detailFormFields, records, transactionInfo.getconfigId(), 0, true, 0);
                transaction.setdetailFields(detailFields);
                
                if(transportDetails.getfileType() == 4) {
                    /* No longer need this */
                    //mav.setViewName("/administrator/processing-activities/HL7messageDetails");
                    mav.setViewName("/administrator/processing-activities/messageDetails");
                }
                else {
                    mav.setViewName("/administrator/processing-activities/messageDetails");
                }
                
            } else {

                transactionTarget transactionInfo = transactionOutManager.getTransactionDetails(transactionId);

                if (transactionInfo == null) { // this will be null if batch is reset - the same transactions will not be available 
                    return mav;
                }
                /* Get the configuration details */
                configuration configDetails = configurationManager.getConfigurationById(transactionInfo.getconfigId());
                
                /* Get a list of form fields */
                /*configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transactionInfo.getconfigId(), 2);*/
                configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transactionInfo.getconfigId());
                List<configurationFormFields> senderInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 1);
                List<configurationFormFields> senderProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 2);
                List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 3);
                List<configurationFormFields> targetProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 4);
                List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 5);
                List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 6);

                transactionOutRecords records = null;

                batchDownloads batchInfo = transactionOutManager.getBatchDetails(transactionInfo.getbatchDLId());

                records = transactionOutManager.getTransactionRecords(transactionId);

                /* Set all the transaction SOURCE ORG fields */
                List<transactionRecords> fromFields;
                if (!senderInfoFormFields.isEmpty()) {
                    fromFields = setInboxFormFields(senderInfoFormFields, records, transactionInfo.getconfigId(), true, 0);
                    
                    int sourceOrgAsInt = 0;
                    
                    try {
                        sourceOrgAsInt = Integer.parseInt(fromFields.get(0).getFieldValue().trim());
                    } catch (Exception e) {
                        sourceOrgAsInt = 0;
                    }
                    
                    if(sourceOrgAsInt > 0) {
                        /* Make sure the org exists */
                        Organization orgDetails = organizationmanager.getOrganizationById(sourceOrgAsInt);
                        if(orgDetails.getId() == sourceOrgAsInt) {
                            fromFields = setOrgDetails(sourceOrgAsInt);
                        }
                        else {
                            fromFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transactionInfo.getId()).getsourceOrgId());
                        }
                    }
                    else if ("".equals(fromFields.get(0).getFieldValue()) || fromFields.get(0).getFieldValue() == null) {
                        fromFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transactionInfo.getId()).getsourceOrgId());
                    }
                    
                    
                } else {
                    fromFields = setOrgDetails(batchInfo.getOrgId());
                }
                transaction.setsourceOrgFields(fromFields);

                /* Set all the transaction SOURCE PROVIDER fields */
                List<transactionRecords> fromProviderFields = setInboxFormFields(senderProviderFormFields, records, transactionInfo.getconfigId(), true, 0);
                transaction.setsourceProviderFields(fromProviderFields);

                /* Set all the transaction TARGET fields */
                List<transactionRecords> toFields;
                if (!targetInfoFormFields.isEmpty()) {
                    toFields = setInboxFormFields(targetInfoFormFields, records, transactionInfo.getconfigId(), true, 0);
                    
                    int targetOrgAsInt;

                    try {
                        targetOrgAsInt = Integer.parseInt(toFields.get(0).getFieldValue().trim());
                    } catch (Exception e) {
                        targetOrgAsInt = 0;
                    }

                    if(targetOrgAsInt > 0) {
                        /* Make sure the org exists */
                        Organization orgDetails = organizationmanager.getOrganizationById(targetOrgAsInt);
                        if(orgDetails.getId() == targetOrgAsInt) {
                            toFields = setOrgDetails(targetOrgAsInt);
                        }
                        else {
                            toFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transactionInfo.getId()).gettargetOrgId());
                        }
                    }
                    else if ("".equals(toFields.get(0).getFieldValue()) || toFields.get(0).getFieldValue() == null) {
                        toFields = setOrgDetails(transactionOutManager.getDownloadSummaryDetails(transactionInfo.getId()).gettargetOrgId());
                    }

                } else {
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
                
                mav.setViewName("/administrator/processing-activities/messageDetails");
                
            }
            
            mav.addObject("transactionDetails", transaction);

        } catch (Exception e) {
            throw new Exception("Error occurred in viewing the sent batch details. transactionId: " + transactionId, e);
        }
        
       

        return mav;

    }

    /**
     * The '/viewStatus{statusId}' function will return the details of the selected status. The results will be displayed in the overlay.
     *
     * @Param	statusId This will hold the id of the selected status
     *
     * @Return	This function will return the status details view.
     */
    @RequestMapping(value = "/viewStatus{statusId}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView viewStatus(@PathVariable int statusId) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activities/statusDetails");

        /* Get the details of the selected status */
        lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(statusId);
        mav.addObject("statusDetails", processStatus);

        return mav;
    }

    /**
     * @param filter START for start date of month e.g. Nov 01, 2013 END for end date of month e.g. Nov 30, 2013
     * @return
     */
    public Date getMonthDate(String filter) {

        String MM_DD_YYYY = "yyyy-mm-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(MM_DD_YYYY);
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        sdf.format(GregorianCalendar.getInstance().getTime());

        Calendar cal = GregorianCalendar.getInstance();
        int date = cal.getActualMinimum(Calendar.DATE);
        if ("END".equalsIgnoreCase(filter)) {
            date = cal.getActualMaximum(Calendar.DATE);
            cal.set(Calendar.DATE, date);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 0);
        } else {
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
     * @param formfields The list of form fields
     * @param records The values of the form fields to populate with.
     *
     * @return This function will return a list of transactionRecords fields with the correct data
     *
     * @throws NoSuchMethodException
     */
    public List<transactionRecords> setOutboundFormFields(List<configurationFormFields> formfields, transactionInRecords records, int configId, int transactionId, boolean readOnly, int orgId) throws NoSuchMethodException, Exception {

        List<transactionRecords> fields = new ArrayList<transactionRecords>();

        for (configurationFormFields formfield : formfields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(formfield.getFieldNo());
            field.setrequired(formfield.getRequired());
            field.setsaveToTable(formfield.getsaveToTableName());
            field.setsaveToTableCol(formfield.getsaveToTableCol());
            field.setfieldLabel(formfield.getFieldLabel());
            field.setreadOnly(readOnly);
            field.setfieldValue(null);

            if (transactionId > 0) {
                /* Get the error for each field */
                try {
                    List<TransErrorDetail> fieldErrors = transactionInManager.getTransactionErrorsByFieldNo(transactionId, formfield.getFieldNo());

                    if (fieldErrors.size() > 0) {
                        StringBuilder errorDetails = new StringBuilder();

                        for (TransErrorDetail error : fieldErrors) {
                            errorDetails.append(error.getErrorDisplayText());
                            if (!"".equals(error.getErrorInfo()) && error.getErrorInfo() != null) {
                                errorDetails.append(" - ").append(error.getErrorInfo());
                            }
                            errorDetails.append("<br />");
                        }
                        field.setErrorDesc(errorDetails.toString());
                    }
                } catch (Exception e) {
                    throw new Exception("Error at batch options.", e);
                }
            }

            /* Get the pre-populated values */
            String tableName = formfield.getautoPopulateTableName();
            String tableCol = formfield.getautoPopulateTableCol();

            /* Get the validation */
            if (formfield.getValidationType() > 1) {
                field.setvalidation(messagetypemanager.getValidationById(formfield.getValidationType()).toString());
            }

            if (records != null) {
                String colName = new StringBuilder().append("f").append(formfield.getFieldNo()).toString();
                try {
                    field.setfieldValue(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }

                /* If autopopulate field is set make it read only */
                if (tableName != null && tableCol != null && !tableName.isEmpty() && !tableCol.isEmpty()) {
                    field.setreadOnly(true);
                }
            } else if (orgId > 0) {

                if (tableName != null && tableCol != null && !tableName.isEmpty() && !tableCol.isEmpty()) {
                    field.setfieldValue(transactionInManager.getFieldValue(tableName, tableCol, "id", orgId));
                }
            }

            if (configId > 0) {
                /* See if any fields have crosswalks associated to it */
                List<fieldSelectOptions> fieldSelectOptions = transactionInManager.getFieldSelectOptions(formfield.getId(), configId);
                field.setfieldSelectOptions(fieldSelectOptions);
            }

            fields.add(field);
        }

        return fields;
    }

    /**
     * The 'setOrgDetails' function will set the field values to the passed in orgId if the organization information wasn't collected with the file upload.
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
     * @param formfields The list of form fields
     * @param records The values of the form fields to populate with.
     *
     * @return This function will return a list of transactionRecords fields with the correct data
     *
     * @throws NoSuchMethodException
     */
    public List<transactionRecords> setInboxFormFields(List<configurationFormFields> formfields, transactionOutRecords records, int configId, boolean readOnly, int transactionInId) throws NoSuchMethodException {

        List<transactionRecords> fields = new ArrayList<transactionRecords>();

        for (configurationFormFields formfield : formfields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(formfield.getFieldNo());
            field.setrequired(formfield.getRequired());
            field.setsaveToTable(formfield.getsaveToTableName());
            field.setsaveToTableCol(formfield.getsaveToTableCol());
            field.setfieldLabel(formfield.getFieldLabel());
            field.setreadOnly(readOnly);
            field.setfieldValue(null);

            /* Get the validation */
            if (formfield.getValidationType() > 1) {
                field.setvalidation(messagetypemanager.getValidationById(formfield.getValidationType()).toString());
            }

            if (records != null) {
                String colName = new StringBuilder().append("f").append(formfield.getFieldNo()).toString();
                try {
                    field.setfieldValue(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } /* 
             If records == null and an auto populate field is set for the field get the data from the
             table/col for the transaction
             */ else if (records == null && formfield.getautoPopulateTableName() != null && transactionInId > 0) {

                /* Get the pre-populated values */
                String tableName = formfield.getautoPopulateTableName();
                String tableCol = formfield.getautoPopulateTableCol();

                if (!tableName.isEmpty() && !tableCol.isEmpty()) {
                    field.setfieldValue(transactionInManager.getFieldValue(tableName, tableCol, "transactionInId", transactionInId));
                    field.setreadOnly(true);
                }

            }

            if (configId > 0) {
                /* See if any fields have crosswalks associated to it */
                List<fieldSelectOptions> fieldSelectOptions = transactionInManager.getFieldSelectOptions(formfield.getId(), configId);
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
    public @ResponseBody
    boolean processAllTransactions(@RequestParam(value = "orgId", required = true) Integer orgId, @RequestParam(value = "messageTypeId", required = false) Integer messageTypeId) throws Exception {

        if (messageTypeId == null || "".equals(messageTypeId)) {
            messageTypeId = 0;
        }

        /* Need to get all transactions for the passed in org and message type id (IF PASSED IN) */
        List transactions = transactionOutManager.getAllransactionsToProcessByMessageType(orgId, messageTypeId);

        for (ListIterator iter = transactions.listIterator(); iter.hasNext();) {

            Object[] row = (Object[]) iter.next();

            transactionTarget transaction = transactionOutManager.getTransactionDetails(Integer.valueOf(Integer.parseInt(String.valueOf(row[0]))));

            int batchId = transactionOutManager.processManualTransaction(transaction);

        }

        return true;
    }

    /**
     * The 'donotprocessAllTransactions' function will update all transactions based on the passed in organziation id and message type id to DO NOT PROCESS.
     */
    @RequestMapping(value = "/donotprocessAllTransactions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    boolean donotprocessAllTransactions(@RequestParam(value = "orgId", required = true) Integer orgId, @RequestParam(value = "messageTypeId", required = false) Integer messageTypeId) throws Exception {

        if (messageTypeId == null || "".equals(messageTypeId)) {
            messageTypeId = 0;
        }

        /* Need to get all transactions for the passed in org and message type id (IF PASSED IN) */
        List transactions = transactionOutManager.getAllransactionsToProcessByMessageType(orgId, messageTypeId);

        for (ListIterator iter = transactions.listIterator(); iter.hasNext();) {
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
    public @ResponseBody
    boolean processTransaction(@RequestParam(value = "transactionId", required = true) Integer transactionId) throws Exception {

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
    public @ResponseBody
    boolean donotprocessTransaction(@RequestParam(value = "transactionId", required = true) Integer transactionId) throws Exception {

        transactionOutManager.doNotProcessTransaction(transactionId);

        return true;
    }

    /**
     * The '/inbound/batchActivities/{batchName}' GET request will retrieve a list of user activities that are associated to the clicked batch
     *
     * @param batchName	The name of the batch to retrieve transactions for
     * @return The list of inbound batch user activities
     *
     * @Objects	(1) An object containing all the found user activities
     *
     * @throws Exception
     */
    @RequestMapping(value = "/{path}/batchActivities/{batchName}", method = RequestMethod.GET)
    public ModelAndView listBatchActivities(@PathVariable String path, @PathVariable String batchName) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/batchActivities");
        mav.addObject("page", path);

        /* Get the details of the batch */
        batchUploads batchDetails = transactionInManager.getBatchDetailsByBatchName(batchName);

        if (batchDetails != null) {

            Organization orgDetails = organizationmanager.getOrganizationById(batchDetails.getOrgId());
            batchDetails.setorgName(orgDetails.getOrgName());

            mav.addObject("batchDetails", batchDetails);

            try {
                /* Get all the user activities for the batch */
                List<UserActivity> uas = transactionInManager.getBatchActivities(batchDetails, true, false);
                mav.addObject("userActivities", uas);

            } catch (Exception e) {
                throw new Exception("(Admin) Error occurred in getting batch activities for an inbound batch. batchId: " + batchDetails.getId() + " ERROR: " + e.getMessage(), e);
            }
        }

        return mav;
    }

    /**
     * The '/ViewUATransactionList' function will return the list of transaction ids for a batch activity that was too long to display The results will be displayed in the overlay.
     *
     * @Param	uaId This will hold the id of the user activity
     * @Param	type 1 = inbound 2 = outbound
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
        if (type == 1) {
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
     * @return The audit report for the batch
     *
     * @Objects	(1) An object containing all the errored transactions
     *
     * @throws Exception
     */
    @RequestMapping(value = "/{path}/auditReport/{batchName}", method = RequestMethod.GET)
    public ModelAndView viewInboundAuditReport(@PathVariable String path, @PathVariable String batchName) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/auditReport");
        mav.addObject("page", path);
        boolean canCancel = false;
        boolean canReset = false;
        boolean canEdit = false;
        boolean canSend = false;

        /* Get the details of the batch */
        batchUploads batchDetails = transactionInManager.getBatchDetailsByBatchName(batchName);

        if (batchDetails != null) {

            Organization orgDetails = organizationmanager.getOrganizationById(batchDetails.getOrgId());
            batchDetails.setorgName(orgDetails.getOrgName());

            lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batchDetails.getstatusId());
            batchDetails.setstatusValue(processStatus.getDisplayCode());

            List<Integer> cancelStatusList = Arrays.asList(21, 22, 23, 1, 8, 35, 60);
            if (!cancelStatusList.contains(batchDetails.getstatusId())) {
                canCancel = true;
            }

            List<Integer> resetStatusList = Arrays.asList(2, 22, 23, 1, 8, 35, 60); //DNP (21) is not a final status for admin
            if (!resetStatusList.contains(batchDetails.getstatusId())) {
                canReset = true;
            }

            if (batchDetails.getstatusId() == 5) {
                // now we check so we don't have to make a db hit if batch status is not 5 
                if (transactionInManager.getRecordCounts(batchDetails.getId(), Arrays.asList(11,12,13,16,18,20), false, false) == 0) {
                    canSend = true;
                }
            }

            if (batchDetails.getstatusId() == 5 && transactionInManager.getRecordCounts(batchDetails.getId(), Arrays.asList(14), false, true) > 0) {
                canEdit = true;
            }

            /**
             * we need to check sbp (4), tbc (25) status, 38 SBL - if server is restarted and somehow the file hangs in SBP, we want to give them option to reset if sbp/tbc start time is about two hours, that should be sufficient indication that a file is stuck we don't want to reset or cancel in the middle of the processing
             */
            if (batchDetails.getstatusId() == 4 || batchDetails.getstatusId() == 25 || batchDetails.getstatusId() == 38) {
                Date d1 = batchDetails.getstartDateTime();
                Date d2 = new Date();
                //in milliseconds
                long diff = d2.getTime() - d1.getTime();

                long diffHours = diff / (60 * 60 * 1000) % 24;
                if (diffHours < 2) {
                    canReset = false;
                    canCancel = false;
                }
            }

            if (batchDetails.getConfigId() != 0) {
                batchDetails.setConfigName(configurationManager.getMessageTypeNameByConfigId(batchDetails.getConfigId()));
            } else {
                batchDetails.setConfigName("Multiple Message Types");
            }
            mav.addObject("batchDetails", batchDetails);
            if (batchDetails.geterrorRecordCount() <= 1000) {
                try {
                    List<TransErrorDetailDisplay> errorList = new LinkedList<TransErrorDetailDisplay>();
                    errorList = transactionInManager.populateErrorList(batchDetails);
                    mav.addObject("errorList", errorList);
                } catch (Exception e) {
                    throw new Exception("(Admin) Error occurred in getting the audit report for an inbound batch. batchId: " + batchDetails.getId() + " ERROR: " + e.getMessage(), e);
                }
            } else {
                mav.addObject("toomany", true);
            }
        } else {
            mav.addObject("doesNotExist", true);
        }

        mav.addObject("canCancel", canCancel);
        mav.addObject("canReset", canReset);
        mav.addObject("canEdit", canEdit);
        mav.addObject("canSend", canSend);

        return mav;
    }

    /**
     * The 'inboundBatchOptions' function will process the batch according to the option submitted by admin
     */
    @RequestMapping(value = "/inboundBatchOptions", method = RequestMethod.POST)
    public @ResponseBody
    boolean inboundBatchOptions(HttpSession session, @RequestParam(value = "tId", required = false) Integer transactionInId,
            @RequestParam(value = "batchId", required = true) Integer batchId, Authentication authentication,
            @RequestParam(value = "batchOption", required = true) String batchOption) throws Exception {

        String strBatchOption = "";
        User userInfo = usermanager.getUserByUserName(authentication.getName());
        batchUploads batchDetails = transactionInManager.getBatchDetails(batchId);

        if (userInfo != null && batchDetails != null) {
            if (batchOption.equalsIgnoreCase("processBatch")) {
                if (batchDetails.getstatusId() == 2 || batchDetails.getstatusId() == 42 ) {
                    strBatchOption = "Loaded Batch";
                    transactionInManager.loadBatch(batchId);
                } else if (batchDetails.getstatusId() == 3 || batchDetails.getstatusId() == 36
                		|| batchDetails.getstatusId() == 43 ) {
                    strBatchOption = "Processed Batch";
                    transactionInManager.processBatch(batchId, false, 0);
                }
            } else if (batchOption.equalsIgnoreCase("cancel")) {
                strBatchOption = "Cancelled Batch";
                transactionInManager.updateBatchStatus(batchId, 4, "startDateTime");
                transactionInManager.updateTransactionStatus(batchId, 0, 0, 31);
                transactionInManager.updateBatchStatus(batchId, 32, "endDateTime");
                //need to cancel targets also
                transactionInManager.updateTranTargetStatusByUploadBatchId(batchId, 0, 31);
                transactionInManager.updateBatchDLStatusByUploadBatchId(batchId, 0, 32 , "endDateTime");
                
            } else if (batchOption.equalsIgnoreCase("reset")) {
                strBatchOption = "Reset Batch";
                //1. Check
                boolean allowBatchClear = transactionInManager.allowBatchClear(batchId);
                if (allowBatchClear) {
                    //if ftp or rhapsody, we flag as DNP and move file back to input folder
                    if (batchDetails.gettransportMethodId() == 5 || batchDetails.gettransportMethodId() == 3) {
                        transactionInManager.updateBatchStatus(batchId, 4, "startDateTime");

                        strBatchOption = "Reset Batch  - FTP/Rhapsody Reset";
                   
                        //targets could be created already so we need to update the target status by upload batchId 
                        transactionInManager.updateTranTargetStatusByUploadBatchId(batchId, 0, 31);
                        transactionInManager.updateBatchDLStatusByUploadBatchId(batchId, 0, 35, "endDateTime");
                        transactionInManager.updateTransactionStatus(batchId, 0, 0, 31);
                        transactionInManager.updateBatchStatus(batchId, 35, "endDateTime");
                   
                        String fileExt = batchDetails.getoriginalFileName().substring(batchDetails.getoriginalFileName().lastIndexOf("."));
                        fileSystem fileSystem = new fileSystem();

                        File archiveFile = new File(fileSystem.setPath(archivePath) + batchDetails.getutBatchName() + fileExt);
                        String fileToPath = fileSystem.setPathFromRoot(batchDetails.getOriginalFolder());
                        //we name it ut batch name when move so we know
                        String newFileName = transactionInManager.newFileName(fileToPath, (batchDetails.getutBatchName() + fileExt));
                        File newFile = new File(fileToPath + newFileName);
                        Path source = archiveFile.toPath();
                        Path target = newFile.toPath();
                        Files.copy(source, target);
                        
                    } else if (batchDetails.gettransportMethodId() == 6) {
                    	//we reset ws message to 1 so it will get pick up again
                    	 strBatchOption = "Reset Batch  - Web Service Reset";
                    	 //we error out old batch
                    	 transactionInManager.updateBatchStatus(batchId, 4, "startDateTime");
                    	 //targets could be created already so we need to update the target status by upload batchId 
                         transactionInManager.updateTranTargetStatusByUploadBatchId(batchId, 0, 31);
                         transactionInManager.updateBatchDLStatusByUploadBatchId(batchId, 0, 35, "endDateTime");
                         transactionInManager.updateTransactionStatus(batchId, 0, 0, 31);
                         transactionInManager.updateBatchStatus(batchId, 35, "endDateTime");
                   
                    	 //we reinsert wsmessageIn
                         List<WSMessagesIn> wsMessagesList = wsmanager.getWSMessagesInByBatchId(batchDetails.getId());
                         WSMessagesIn newWSIn = new WSMessagesIn();
                         WSMessagesIn copyWSIn = wsMessagesList.get(0);
                         newWSIn.setOrgId(copyWSIn.getOrgId());
                         newWSIn.setStatusId(1);
                         newWSIn.setDomain(copyWSIn.getDomain());
                         newWSIn.setFromAddress(copyWSIn.getFromAddress());
                         newWSIn.setPayload(copyWSIn.getPayload());
                         newWSIn.setDateCreated(copyWSIn.getDateCreated());
                         wsmanager.saveWSMessagesIn(newWSIn);
                    	
                    } else{

                        transactionInManager.updateBatchStatus(batchId, 4, "");
                        //2. clear
                        boolean cleared = transactionInManager.clearBatch(batchId);

                        //copy archive file back to original folder
                        fileSystem dir = new fileSystem();

                        // we need to move unencoded file back from archive folder and replace current file
                        //we set archive path
                        try {

                            File archiveFile = new File(dir.setPath(archivePath) + batchDetails.getutBatchName() + batchDetails.getoriginalFileName().substring(batchDetails.getoriginalFileName().lastIndexOf(".")));
                            Path archive = archiveFile.toPath();
                            File toFile = new File(dir.setPath(batchDetails.getFileLocation()) + batchDetails.getoriginalFileName());
                            Path toPath = toFile.toPath();
                            //need to encode file first
                            if (batchDetails.getEncodingId() == 1) {
                                String strEncodedFile = filemanager.encodeFileToBase64Binary(archiveFile);
                                toFile.delete();
                                //we replace file with encoded
                                filemanager.writeFile(toFile.getAbsolutePath(), strEncodedFile);
                            } else { // already encoded
                                Files.copy(archive, toPath, StandardCopyOption.REPLACE_EXISTING);
                            }

                            cleared = true;

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            cleared = false;
                        }

                        if (cleared) {
                            transactionInManager.updateBatchStatus(batchId, 2, "startOver");
                        }
                    }
                }
            } else if (batchOption.equalsIgnoreCase("releaseBatch")) {
                strBatchOption = "Released Batch";
                if (batchDetails.getstatusId() == 5) {
                    transactionInManager.updateBatchStatus(batchId, 4, "startDateTime");
                    //check once again to make sure all transactions are in final status
                    if (transactionInManager.getRecordCounts(batchId, Arrays.asList(11, 12, 13, 16), false, false) == 0) {
                        transactionInManager.updateBatchStatus(batchId, 6, "endDateTime");
                    } else {
                        transactionInManager.updateBatchStatus(batchId, 5, "endDateTime");
                    }
                }
            } else if (batchOption.equalsIgnoreCase("rejectMessage")) {
                strBatchOption = "Rejected Transaction";
                if (batchDetails.getstatusId() == 5) {
                    transactionInManager.updateTranStatusByTInId(transactionInId, 13);
                }
            }
        }

        //log user activity
        UserActivity ua = new UserActivity();
        ua.setUserId(userInfo.getId());
        ua.setAccessMethod("POST");
        ua.setPageAccess("/inboundBatchOptions");
        ua.setActivity("Admin - " + strBatchOption);
        ua.setBatchUploadId(batchId);
        if (transactionInId != null) {
            ua.setTransactionInIds(transactionInId.toString());
        }
        usermanager.insertUserLog(ua);
        return true;
    }

    /**
     * The '/rejectMessages POST request will loop through idList, and reject transactions
     *
     * @param request - idList with transactionIds to be rejected
     * @param request - batchId with batch Id
     * @param response
     * @return	we redirect back to the audit report
     * @throws Exception
     */
    @RequestMapping(value = "/rejectMessages", method = RequestMethod.POST)
    public ModelAndView massRejectTransactions(@RequestParam(value = "idList", required = false) List<Integer> idList,
            @RequestParam(value = "batchId", required = false) Integer batchId, Authentication authentication,
            RedirectAttributes redirectAttr, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        try {

            User userInfo = usermanager.getUserByUserName(authentication.getName());
            batchUploads batchInfo = transactionInManager.getBatchDetails(batchId);
            if (userInfo != null && batchInfo != null) {
                if (batchInfo.getstatusId() == 5) {
                    if (idList.size() > 0) {
                        for (Integer transactionInId : idList) {
                            transactionInManager.updateTranStatusByTInId(transactionInId, 13);
                        }
                    }
                }
            }
            //log user activity
            UserActivity ua = new UserActivity();
            ua.setUserId(userInfo.getId());
            ua.setAccessMethod(request.getMethod());
            ua.setPageAccess("/rejectMessages");
            ua.setActivity("Admin Mass Rejected Messages");
            ua.setBatchUploadId(batchId);
            if (idList.size() > 0) {
                ua.setTransactionInIds(idList.toString().replace("]", "").replace("[", ""));
            }
            usermanager.insertUserLog(ua);

            ModelAndView mav = new ModelAndView(new RedirectView("inbound/auditReport/" + batchInfo.getutBatchName()));
            return mav;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Admin Error at mass rejected messages.", e);
        }

    }

    /**
     * The '/editTransaction POST will bring up the ERG form for user to fix
     *
     * @param request - transactionInId
     * @param response
     * @return	we redirect back to the audit report
     * @throws Exception
     */
    @RequestMapping(value = "/editTransaction", method = RequestMethod.POST)
    public ModelAndView editTransaction(@RequestParam(value = "transactionInId", required = false) Integer transactionInId, Authentication authentication,
            RedirectAttributes redirectAttr, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        try {
            ModelAndView mav = new ModelAndView();

            User userInfo = usermanager.getUserByUserName(authentication.getName());

            batchUploads batchDetails = transactionInManager.getBatchDetailsByTInId(transactionInId);

            if (batchDetails != null) {

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

            }

            /* If user has edit athoritity then show the edit page, otherwise redirect back to the auditReport */
            mav.setViewName("/administrator/processing-activity/ERG");

            try {
                transactionIn transactionInfo = transactionInManager.getTransactionDetails(transactionInId);

                /* Get the configuration details */
                configuration configDetails = configurationManager.getConfigurationById(transactionInfo.getconfigId());

                /* Get a list of form fields */
                /*configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transactionInfo.getconfigId(), 2);*/
                configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transactionInfo.getconfigId());
                List<configurationFormFields> senderInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 1);
                List<configurationFormFields> senderProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 2);
                List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 3);
                List<configurationFormFields> targetProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 4);
                List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 5);
                List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 6);

                Transaction transaction = new Transaction();
                transactionInRecords records = null;

                transactionTarget transactionTarget = transactionInManager.getTransactionTarget(transactionInfo.getbatchId(), transactionInId);

                transaction.settransactionId(transactionInId);
                transaction.setbatchId(batchDetails.getId());
                transaction.setbatchName(batchDetails.getutBatchName());
                transaction.setdateSubmitted(transactionInfo.getdateCreated());
                transaction.setstatusId(batchDetails.getstatusId());
                transaction.setconfigId(transactionInfo.getConfigId());

                /* Check to see if the message is a feedback report */
                if (transactionInfo.gettransactionTargetId() > 0) {
                    transaction.setsourceType(2); /* Feedback report */

                    transaction.setorginialTransactionId(transactionInfo.gettransactionTargetId());
                } else {
                    transaction.setsourceType(configDetails.getsourceType());
                }

                lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(transaction.getstatusId());
                transaction.setstatusValue(processStatus.getDisplayCode());

                /* get the message type name */
                transaction.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                records = transactionInManager.getTransactionRecords(transactionInId);
                transaction.settransactionRecordId(records.getId());


                /* Set all the transaction SOURCE ORG fields */
                List<transactionRecords> fromFields;
                if (!senderInfoFormFields.isEmpty()) {
                    fromFields = setOutboundFormFields(senderInfoFormFields, records, transactionInfo.getconfigId(), transactionInId, false, 0);
                } else {
                    fromFields = setOrgDetails(batchDetails.getOrgId());
                }
                transaction.setsourceOrgFields(fromFields);

                /* Set all the transaction SOURCE PROVIDER fields */
                List<transactionRecords> fromProviderFields = setOutboundFormFields(senderProviderFormFields, records, transactionInfo.getconfigId(), transactionInId, false, 0);
                transaction.setsourceProviderFields(fromProviderFields);

                /* Set all the transaction TARGET fields */
                List<transactionRecords> toFields;
                if (!targetInfoFormFields.isEmpty()) {
                    toFields = setOutboundFormFields(targetInfoFormFields, records, transactionInfo.getconfigId(), transactionInId, false, 0);

                    if ("".equals(toFields.get(0).getFieldValue()) || toFields.get(0).getFieldValue() == null) {
                        toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transactionInfo.getId()).gettargetOrgId());
                    }

                } else {
                    toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transactionInfo.getId()).gettargetOrgId());
                }
                transaction.settargetOrgFields(toFields);

                /* Set all the transaction TARGET PROVIDER fields */
                List<transactionRecords> toProviderFields = setOutboundFormFields(targetProviderFormFields, records, transactionInfo.getconfigId(), transactionInId, false, 0);
                transaction.settargetProviderFields(toProviderFields);

                /* Set all the transaction PATIENT fields */
                List<transactionRecords> patientFields = setOutboundFormFields(patientInfoFormFields, records, transactionInfo.getconfigId(), transactionInId, false, 0);
                transaction.setpatientFields(patientFields);

                /* Set all the transaction DETAIL fields */
                List<transactionRecords> detailFields = setOutboundFormFields(detailFormFields, records, transactionInfo.getconfigId(), transactionInId, false, 0);
                transaction.setdetailFields(detailFields);

                mav.addObject("transaction", transaction);

                mav.addObject("transactionInId", transactionInId);

            } catch (Exception e) {
                throw new Exception("Error occurred in viewing the sent batch details. transactionId: " + transactionInId, e);
            }

            /**
             * log user activity *
             */
            UserActivity ua = new UserActivity();
            ua.setUserId(userInfo.getId());
            ua.setAccessMethod(request.getMethod());
            ua.setPageAccess("/edit");
            ua.setActivity("Viewed Transaction with Error(s)");
            ua.setTransactionInIds(String.valueOf(transactionInId));
            ua.setBatchUploadId(batchDetails.getId());
            usermanager.insertUserLog(ua);

            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error occurred displaying upload ERG form.", e);
        }

    }

    /**
     * The '/editMessage' POST request will submit the changes to the passed in transaction. The transaction will be updated to a status of 10 (Pending Release) and the error records will be cleared
     *
     * @param transactionDetails The object to hold the transaction fields
     *
     */
    @RequestMapping(value = "/editMessage", method = RequestMethod.POST)
    public @ResponseBody
    Integer submitTransactionChanges(@ModelAttribute(value = "transactionDetails") Transaction transactionDetails, HttpServletRequest request, HttpServletResponse response, Authentication authentication, HttpSession session) throws Exception {

        /* Update the transactionInRecords */
        List<transactionRecords> sourceOrgFields = transactionDetails.getsourceOrgFields();
        List<transactionRecords> sourceProviderFields = transactionDetails.getsourceProviderFields();
        List<transactionRecords> targetOrgFields = transactionDetails.gettargetOrgFields();
        List<transactionRecords> targetProviderFields = transactionDetails.gettargetProviderFields();
        List<transactionRecords> patientFields = transactionDetails.getpatientFields();
        List<transactionRecords> detailFields = transactionDetails.getdetailFields();

        transactionInRecords records = transactionInManager.getTransactionRecords(transactionDetails.gettransactionId());

        String colName;
        for (transactionRecords field : sourceOrgFields) {
            colName = new StringBuilder().append("f").append(field.getfieldNo()).toString();
            try {
                BeanUtils.setProperty(records, colName, field.getfieldValue());
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (transactionRecords field : sourceProviderFields) {
            colName = new StringBuilder().append("f").append(field.getfieldNo()).toString();
            try {
                BeanUtils.setProperty(records, colName, field.getfieldValue());
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (transactionRecords field : targetOrgFields) {
            colName = new StringBuilder().append("f").append(field.getfieldNo()).toString();
            try {
                BeanUtils.setProperty(records, colName, field.getfieldValue());
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (transactionRecords field : targetProviderFields) {
            colName = new StringBuilder().append("f").append(field.getfieldNo()).toString();
            try {
                BeanUtils.setProperty(records, colName, field.getfieldValue());
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (transactionRecords field : patientFields) {
            colName = new StringBuilder().append("f").append(field.getfieldNo()).toString();
            try {
                BeanUtils.setProperty(records, colName, field.getfieldValue());
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (transactionRecords field : detailFields) {
            colName = new StringBuilder().append("f").append(field.getfieldNo()).toString();
            try {
                BeanUtils.setProperty(records, colName, field.getfieldValue());
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            records.setId(records.getId());
            transactionInManager.submitTransactionInRecordsUpdates(records);

            /* Update the transactionTranslatedIn records  */
            transactionInManager.submitTransactionTranslatedInRecords(transactionDetails.gettransactionId(), records.getId(), transactionDetails.getconfigId());

            /* Remove the transaction errors */
            transactionInManager.deleteTransactionInErrorsByTransactionId(transactionDetails.gettransactionId());

            /* Update the transaction status to 10 (PR Released) */
            transactionInManager.updateTransactionStatus(0, transactionDetails.gettransactionId(), 14, 10);

            /**
             * update status so it will re-process *
             */
            transactionInManager.updateBatchStatus(transactionDetails.getbatchId(), 3, "startDateTime");

            /**
             * re-process batch *
             */
            transactionInManager.processBatch(transactionDetails.getbatchId(), true, transactionDetails.gettransactionId());

            /**
             * add logging *
             */
            UserActivity ua = new UserActivity();
            User userInfo = usermanager.getUserByUserName(authentication.getName());
            ua.setUserId(userInfo.getId());
            ua.setAccessMethod(request.getMethod());
            ua.setPageAccess("/editMessage");
            ua.setActivity("Modified Transaction with Error(s)");
            ua.setTransactionInIds(String.valueOf(transactionDetails.gettransactionId()));
            ua.setBatchUploadId(transactionDetails.getbatchId());
            usermanager.insertUserLog(ua);
        } catch (Exception e) {
            throw new Exception("Error saving the transaction: error", e);
        }

        return 1;

    }
    
    
    /**
     * The '/referralActivityExport' GET request will return the latest export created
     * 
     * @param session
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/referralActivityExport", method = RequestMethod.GET)
    public ModelAndView referralActivityExport(HttpSession session) throws Exception {
        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);
        
        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/referralActivityExport");
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);
        
        List<referralActivityExports> exports = transactionInManager.getReferralActivityExportsWithUserNames(Arrays.asList(1,2,3,4,6));
        encryptObject encrypt = new encryptObject();
        Map<String, String> map;
        for (referralActivityExports export : exports) {
        	//Encrypt the use id to pass in the url
            map = new HashMap<String, String>();
            map.put("id", Integer.toString(export.getId()));
            map.put("topSecret", topSecret);

            String[] encrypted = encrypt.encryptObject(map);
            export.setEncryptedId(encrypted[0]);
            export.setEncryptedSecret(encrypted[1]);
        }
        mav.addObject("exports", exports);
        
        return mav;
    }
    
    /**
     * The '/referralActivityExport' POST method will generate add an entry into the existing table.
     * @param session
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/referralActivityExport", method = RequestMethod.POST)
    public ModelAndView referralActivityExport(@RequestParam Date fromDate, @RequestParam Date toDate, RedirectAttributes redirectAttr, HttpSession session) throws Exception {
        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);

        User userInfo = (User) session.getAttribute("userDetails");
        
        /** insert a new export **/
        referralActivityExports export  = new referralActivityExports();
        
        export.setCreatedBy(userInfo.getId());
        export.setToDate(toDate);
        export.setFromDate(fromDate);
        
        DateFormat selDateRangeFormat = new SimpleDateFormat("MM/dd/yyyy");
        export.setSelDateRange(selDateRangeFormat.format(fromDate) + " - " + selDateRangeFormat.format(toDate));
        export.setStatusId(1);
        transactionInManager.saveReferralActivityExport(export); 
        
        
        ModelAndView mav = new ModelAndView(new RedirectView("referralActivityExport"));
        return mav;
        
    }
    
    
    /**
     * The '/wsmessage' GET request will serve up the list of inbound web services messages
     *
     *
     * @Objects	(1) An object containing all the found wsMessagesIn
     *
     * @throws Exception
     */
    @RequestMapping(value = "/wsmessage", method = RequestMethod.GET)
    public ModelAndView listInBoundWSmessages(HttpSession session) throws Exception {
    	
    	int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);

        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");

        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/wsmessage");

        if ("".equals(searchParameters.getsection()) || !"inbound".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setsection("inbound");
        } else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
        }

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);

        /* Get all ws messages */
        try {

            Integer fetchCount = 0;
            List<WSMessagesIn> wsMessagesList = wsmanager.getWSMessagesInList(fromDate, toDate, fetchCount);

            if (!wsMessagesList.isEmpty()) {
                
                //we can map the process status so we only have to query once
            	List<TableData> errorCodeList = sysAdminManager.getDataList("lu_ErrorCodes", "");
                Map<Integer, String> errorMap = new HashMap<Integer, String>();
                for (TableData error : errorCodeList) {
                    errorMap.put(error.getId(), error.getDisplayText());
                }
                
                //ws status map
                Map<Integer, String> statusMap = new HashMap<Integer, String>();
                statusMap.put(1, "To be processed");
                statusMap.put(2, "Processed");
                statusMap.put(3, "Rejected");
                statusMap.put(4, "Being Process");
                
                
                //if we have lots of organization in the future we can tweak this to narrow down to orgs with batches
                List<Organization> organizations = organizationmanager.getOrganizations();
                Map<Integer, String> orgMap = new HashMap<Integer, String>();
                for (Organization org : organizations) {
                    orgMap.put(org.getId(), org.getOrgName());
                }

                for (WSMessagesIn wsIn : wsMessagesList) {
                	//set error text
                	wsIn.setErrorDisplayText(errorMap.get(wsIn.getErrorId()));
                	//set org name
                	if (wsIn.getOrgId() == 0) {
                		wsIn.setOrgName("No Org Match");
                	} else {
                		wsIn.setOrgName(orgMap.get(wsIn.getOrgId()));
                	}
                	//set status
                	wsIn.setStatusName(statusMap.get(wsIn.getStatusId()));
                	
                	if (wsIn.getBatchUploadId() != 0) {
                		wsIn.setBatchName(transactionInManager.getBatchDetails(wsIn.getBatchUploadId()).getutBatchName());
                	}
                }
            }

            mav.addObject("wsMessages", wsMessagesList);

        } catch (Exception e) {
            throw new Exception("Error occurred viewing the all web service messages.", e);
        }

        return mav;

    }

    /**
     * The '/wsMessage' POST request will serve up a list of 
     * WSMessages received by the system.
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return The list of wsMessages
     *
     * @Objects	(1) An object containing all the found wsMessages
     *
     * @throws Exception
     */
    @RequestMapping(value = "/wsmessage", method = RequestMethod.POST)
    public ModelAndView listWSMessages(@RequestParam Date fromDate, @RequestParam Date toDate, 
    		HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/wsmessage");

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);

        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setsection("inbound");

        /* Get all ws in  */ 
        try {
        	Integer fetchCount = 0;
            List<WSMessagesIn> wsMessagesList = wsmanager.getWSMessagesInList(fromDate, toDate, fetchCount);

            if (!wsMessagesList.isEmpty()) {
                
                //we can map the process status so we only have to query once
            	List<TableData> errorCodeList = sysAdminManager.getDataList("lu_ErrorCodes", "");
                Map<Integer, String> errorMap = new HashMap<Integer, String>();
                for (TableData error : errorCodeList) {
                    errorMap.put(error.getId(), error.getDisplayText());
                }
                
                //ws status map
                Map<Integer, String> statusMap = new HashMap<Integer, String>();
                statusMap.put(1, "To be processed");
                statusMap.put(2, "Processed");
                statusMap.put(3, "Rejected");
                
                //if we have lots of organization in the future we can tweak this to narrow down to orgs with batches
                List<Organization> organizations = organizationmanager.getOrganizations();
                Map<Integer, String> orgMap = new HashMap<Integer, String>();
                for (Organization org : organizations) {
                    orgMap.put(org.getId(), org.getOrgName());
                }

                for (WSMessagesIn wsIn : wsMessagesList) {
                	//set error text
                	wsIn.setErrorDisplayText(errorMap.get(wsIn.getErrorId()));
                	//set org name
                	if (wsIn.getOrgId() == 0) {
                		wsIn.setOrgName("No Org Match");
                	} else {
                		wsIn.setOrgName(orgMap.get(wsIn.getOrgId()));
                	}
                	//set status
                	wsIn.setStatusName(statusMap.get(wsIn.getStatusId()));
                	if (wsIn.getBatchUploadId() != 0) {
                		wsIn.setBatchName(transactionInManager.getBatchDetails(wsIn.getBatchUploadId()).getutBatchName());
                	}
                	
                }
            }

            mav.addObject("wsMessages", wsMessagesList);
            

        } catch (Exception e) {
            throw new Exception("Error occurred viewing the all uploaded batches.", e);
        }

        return mav;
    }

    /**this displays the payload**/
    @RequestMapping(value= "/wsmessage/viewPayload.do", method = RequestMethod.POST)
    public @ResponseBody ModelAndView viewPayload(
    		@RequestParam Integer wsId) 
    throws Exception {
    	
    	ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activities/viewPayload");
        WSMessagesIn wsMessage = wsmanager.getWSMessagesIn(wsId);   
        String payload = "";
        if (wsMessage != null) {
        	payload = wsMessage.getPayload();
        }
        
        mav.addObject("payload", payload);

        return mav;

    }
    
    /**
     * The '/rejected' GET request will serve up the existing list of referrals with at least one rejected
     * transaction.
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return The list of batches with rejected transactions
     *
     * @Objects	(1) An object containing all the found batches
     *
     * @throws Exception
     */
    @RequestMapping(value = "/rejected", method = RequestMethod.GET)
    public ModelAndView listRejectedBatches(HttpSession session) throws Exception {

        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);

        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");

        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/rejected");

        if ("".equals(searchParameters.getsection()) || !"rejected".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setsection("rejected");
        } else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
        }

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);

        
        /* Get all inbound transactions */
        try {

            Integer fetchCount = 0;
            List<batchUploads> rejectedBatches = transactionInManager.getAllRejectedBatches(fromDate, toDate, fetchCount);

            if (!rejectedBatches.isEmpty()) {
                
                //we can map the process status so we only have to query once
                List<lu_ProcessStatus> processStatusList = sysAdminManager.getAllProcessStatus();
                Map<Integer, String> psMap = new HashMap<Integer, String>();
                for (lu_ProcessStatus ps : processStatusList) {
                    psMap.put(ps.getId(), ps.getDisplayCode());
                }

                //same with transport method names
                List<TransportMethod> transporthMethods = configurationTransportManager.getTransportMethods(Arrays.asList(0, 1));
                Map<Integer, String> tmMap = new HashMap<Integer, String>();
                for (TransportMethod tms : transporthMethods) {
                    tmMap.put(tms.getId(), tms.getTransportMethod());
                }

                //if we have lots of organization in the future we can tweak this to narrow down to orgs with batches
                List<Organization> organizations = organizationmanager.getOrganizations();
                Map<Integer, String> orgMap = new HashMap<Integer, String>();
                for (Organization org : organizations) {
                    orgMap.put(org.getId(), org.getOrgName());
                }

                //same goes for users
                List<User> users = usermanager.getAllUsers();
                Map<Integer, String> userMap = new HashMap<Integer, String>();
                for (User user : users) {
                    userMap.put(user.getId(), (user.getFirstName() + " " + user.getLastName()));
                }

                for (batchUploads batch : rejectedBatches) {
                    
                    //Get the upload type (Referral or Feedback Report
                    List<transactionIn> transactions = transactionInManager.getBatchTransactions(batch.getId(), 0);
                    
                    if(!transactions.isEmpty()) {
                        
                        transactionIn transactionDetails = transactionInManager.getTransactionDetails(transactions.get(0).getId());
                        if(transactionDetails.gettransactionTargetId() > 0) {
                            batch.setUploadType("Feedback Report");
                            
                            transactionTarget targetDetails = transactionInManager.getTransactionTargetDetails(transactionDetails.gettransactionTargetId());
                            
                            if(targetDetails != null) {
                                /* Get the originating referall batch ID */
                                batchUploads referringbatch = transactionInManager.getBatchDetails(targetDetails.getbatchUploadId());
                                batch.setReferringBatch(referringbatch.getutBatchName());
                            }
                            else {
                                batch.setReferringBatch("Not Found");
                            }
                            
                        }
                        else {
                            batch.setUploadType("Referral");
                            
                        }
                    }
                    else {
                        batch.setUploadType("Referral");
                    }
                    
                    //the count is in totalRecordCount already, can skip re-count
                    // batch.settotalTransactions(transactionInManager.getRecordCounts(batch.getId(), statusIds, false, false));
                    batch.setstatusValue(psMap.get(batch.getstatusId()));

                    batch.setorgName(orgMap.get(batch.getOrgId()));

                    batch.settransportMethod(tmMap.get(batch.gettransportMethodId()));

                    batch.setusersName(userMap.get(batch.getuserId()));

                }
            }

            mav.addObject("batches", rejectedBatches);

        } catch (Exception e) {
            throw new Exception("Error occurred viewing the batches with rejected transactions", e);
        }

        return mav;

    }

    /**
     * The '/rejected' POST request will serve up the existing list of referrals with at least one rejected
     * transaction.
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return The list of batches with rejected transactions
     *
     * @Objects	(1) An object containing all the found batches
     *
     * @throws Exception
     */
    @RequestMapping(value = "/rejected", method = RequestMethod.POST)
    public ModelAndView listRejectedBatches(@RequestParam Date fromDate, @RequestParam Date toDate, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/rejected");

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);

        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setsection("rejected");

        /* Get all inbound transactions */ 
        try {

            Integer fetchCount = 0;
            /* Need to get a list of all uploaded batches */
            List<batchUploads> rejectedBatches = transactionInManager.getAllRejectedBatches(fromDate, toDate, fetchCount);

            if (!rejectedBatches.isEmpty()) {
                //we can map the process status so we only have to query once
                List<lu_ProcessStatus> processStatusList = sysAdminManager.getAllProcessStatus();
                Map<Integer, String> psMap = new HashMap<Integer, String>();
                for (lu_ProcessStatus ps : processStatusList) {
                    psMap.put(ps.getId(), ps.getDisplayCode());
                }

                //same with transport method names
                List<TransportMethod> transporthMethods = configurationTransportManager.getTransportMethods(Arrays.asList(0, 1));
                Map<Integer, String> tmMap = new HashMap<Integer, String>();
                for (TransportMethod tms : transporthMethods) {
                    tmMap.put(tms.getId(), tms.getTransportMethod());
                }

                //if we have lots of organization in the future we can tweak this to narrow down to orgs with batches
                List<Organization> organizations = organizationmanager.getOrganizations();
                Map<Integer, String> orgMap = new HashMap<Integer, String>();
                for (Organization org : organizations) {
                    orgMap.put(org.getId(), org.getOrgName());
                }

                //same goes for users
                List<User> users = usermanager.getAllUsers();
                Map<Integer, String> userMap = new HashMap<Integer, String>();
                for (User user : users) {
                    userMap.put(user.getId(), (user.getFirstName() + " " + user.getLastName()));
                }

                for (batchUploads batch : rejectedBatches) {
                    
                    //Get the upload type (Referral or Feedback Report
                    List<transactionIn> transactions = transactionInManager.getBatchTransactions(batch.getId(), 0);
                    
                    if(!transactions.isEmpty()) {
                        transactionIn transactionDetails = transactionInManager.getTransactionDetails(transactions.get(0).getId());
                        if(transactionDetails.gettransactionTargetId() > 0) {
                            batch.setUploadType("Feedback Report");
                            
                            transactionTarget targetDetails = transactionInManager.getTransactionTargetDetails(transactionDetails.gettransactionTargetId());
                            
                            if(targetDetails != null) {
                                /* Get the originating referall batch ID */
                                batchUploads referringbatch = transactionInManager.getBatchDetails(targetDetails.getbatchUploadId());
                                batch.setReferringBatch(referringbatch.getutBatchName());
                            }
                            else {
                                batch.setReferringBatch("Not Found");
                            }
                            
                        }
                        else {
                            batch.setUploadType("Referral");
                            
                        }
                    }
                    else {
                        batch.setUploadType("Referral");
                    }
                    
                    batch.setstatusValue(psMap.get(batch.getstatusId()));

                    batch.setorgName(orgMap.get(batch.getOrgId()));

                    batch.settransportMethod(tmMap.get(batch.gettransportMethodId()));

                    batch.setusersName(userMap.get(batch.getuserId()));
                }
            }

            mav.addObject("batches", rejectedBatches);

        } catch (Exception e) {
            throw new Exception("Error occurred viewing the batches with rejected transactions.", e);
        }

        return mav;
    }
    
    
    /**
     * The '/wsmessageOut' GET request will serve up the list of outbound web services messages
     *
     *
     * @Objects	(1) An object containing all the found wsMessagesOut
     *
     * @throws Exception
     */
    @RequestMapping(value = "/wsmessageOut", method = RequestMethod.GET)
    public ModelAndView listInBoundWSmessagesOut(HttpSession session) throws Exception {

        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);

        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");

        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/wsmessageOut");

        if ("".equals(searchParameters.getsection()) || !"inbound".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setsection("inbound");
        } else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
        }

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);

        /* Get all ws messages */
        try {

            Integer fetchCount = 0;
            List<wsMessagesOut> wsMessagesList = wsmanager.getWSMessagesOutList(fromDate, toDate, fetchCount);

            if (!wsMessagesList.isEmpty()) {
	                    //if we have lots of organization in the future we can tweak this to narrow down to orgs with batches
		                List<Organization> organizations = organizationmanager.getOrganizations();
		                Map<Integer, String> orgMap = new HashMap<Integer, String>();
		                for (Organization org : organizations) {
		                    orgMap.put(org.getId(), org.getOrgName());
		                }
	
		                for (wsMessagesOut wsOut : wsMessagesList) {
		                	//set org name
		                	if (wsOut.getOrgId() == 0) {
		                		wsOut.setOrgName("No Org Match");
		                	} else {
		                		wsOut.setOrgName(orgMap.get(wsOut.getOrgId()));
		                	}
		                	
		                	if (wsOut.getBatchDownloadId() != 0) {
		                		wsOut.setBatchName(transactionOutManager.getBatchDetails(wsOut.getBatchDownloadId()).getutBatchName());
		                	}
		                }
	                }
            
            mav.addObject("wsMessages", wsMessagesList);

        } catch (Exception e) {
            throw new Exception("Error occurred viewing the all outbound web service messages.", e);
        }

        return mav;

    }
    
    
    /**
     * The '/wsMessageOut' POST request will serve up a list of outbound
     * WSMessages received by the system.
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return The list of wsMessages
     *
     * @Objects	(1) An object containing all the found wsMessages
     *
     * @throws Exception
     */
    @RequestMapping(value = "/wsmessageOut", method = RequestMethod.POST)
    public ModelAndView listWSMessagesOut(@RequestParam Date fromDate, @RequestParam Date toDate, 
    		HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/wsmessageOut");

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);

        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setsection("inbound");

        /* Get all ws in  */ 
        try {
        	Integer fetchCount = 0;
            List<wsMessagesOut> wsMessagesList = wsmanager.getWSMessagesOutList(fromDate, toDate, fetchCount);

            if (!wsMessagesList.isEmpty()) {
                
                //if we have lots of organization in the future we can tweak this to narrow down to orgs with batches
                List<Organization> organizations = organizationmanager.getOrganizations();
                Map<Integer, String> orgMap = new HashMap<Integer, String>();
                for (Organization org : organizations) {
                    orgMap.put(org.getId(), org.getOrgName());
                }

                for (wsMessagesOut ws : wsMessagesList) {
                	
                	if (ws.getOrgId() == 0) {
                		ws.setOrgName("No Org Match");
                	} else {
                		ws.setOrgName(orgMap.get(ws.getOrgId()));
                	}
                }
            }

            mav.addObject("wsMessages", wsMessagesList);
            

        } catch (Exception e) {
            throw new Exception("Error occurred viewing the all outbound ws.", e);
        }

        return mav;
    }

    
    /**this displays the soap message**/
    @RequestMapping(value= "/wsmessage/viewSoapMessage.do", method = RequestMethod.POST)
    public @ResponseBody ModelAndView viewSoapMessage(
    		@RequestParam Integer wsId) 
    throws Exception {
    	
    	ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activities/viewSoapMessage");
        wsMessagesOut wsMessage = wsmanager.getWSMessagesOut(wsId);   
        mav.addObject("wsMessage", wsMessage);

        return mav;

    }
    
    /**this displays the soap response**/
    @RequestMapping(value= "/wsmessage/viewSoapResponse.do", method = RequestMethod.POST)
    public @ResponseBody ModelAndView viewSoapResponse(
    		@RequestParam Integer wsId) 
    throws Exception {
    	
    	ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activities/viewSoapResponse");
        wsMessagesOut wsMessage = wsmanager.getWSMessagesOut(wsId);   
        mav.addObject("wsMessage", wsMessage);

        return mav;

    }
    
    
    /**
     * The '/wsmessageOut' GET request will serve up the list of outbound web services messages
     *
     *
     * @Objects	(1) An object containing all the found wsMessagesOut
     *
     * @throws Exception
     */
    @RequestMapping(value = "/wsmessageOut/{batchName}", method = RequestMethod.GET)
    public ModelAndView listSingleWSmessagesOut(HttpSession session, @PathVariable String batchName) throws Exception {

        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);

        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");

        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/wsmessageOut");

        if ("".equals(searchParameters.getsection()) || !"inbound".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setsection("inbound");
        } else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
        }

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);

        /* Get all ws messages */
        try {

            /* Get the details of the batch */
            batchDownloads batchDetails = transactionOutManager.getBatchDetailsByBatchName(batchName);

            List<wsMessagesOut> wsMessagesList = wsmanager.getWSMessagesOutByBatchId(batchDetails.getId());

            if (!wsMessagesList.isEmpty()) {
	                    for (wsMessagesOut wsOut : wsMessagesList) {
		                	//set org name
		                	if (wsOut.getOrgId() == 0) {
		                		wsOut.setOrgName("No Org Match");
		                	} else {
		                		wsOut.setOrgName(organizationmanager.getOrganizationById(wsOut.getOrgId()).getOrgName());
		                	}
		                	
		                	if (wsOut.getBatchDownloadId() != 0) {
		                		wsOut.setBatchName(transactionOutManager.getBatchDetails(wsOut.getBatchDownloadId()).getutBatchName());
		                	}
		                }
	                }
            
            mav.addObject("wsMessages", wsMessagesList);

        } catch (Exception e) {
            throw new Exception("Error occurred viewing the all web service outbound messages.", e);
        }

        return mav;

    }
    
    /**
     * The '/wsmessage' GET request will serve up the list of inbound web services messages
     *
     *
     * @Objects	(1) An object containing all the found wsMessagesIn
     *
     * @throws Exception
     */
    @RequestMapping(value = "/wsmessage/{batchName}", method = RequestMethod.GET)
    public ModelAndView listInBoundOneWSmessages(HttpSession session, @PathVariable String batchName) throws Exception {

        int year = 114;
        int month = 0;
        int day = 1;
        Date originalDate = new Date(year, month, day);

        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");

        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters) session.getAttribute("searchParameters");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/wsmessage");

        if ("".equals(searchParameters.getsection()) || !"inbound".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setsection("inbound");
        } else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
        }

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("originalDate", originalDate);

        /* Get all ws messages */
        try {

        	batchUploads batchDetails = transactionInManager.getBatchDetailsByBatchName(batchName);
            List<WSMessagesIn> wsMessagesList = wsmanager.getWSMessagesInByBatchId(batchDetails.getId());

            if (!wsMessagesList.isEmpty()) {
                
                //we can map the process status so we only have to query once
            	List<TableData> errorCodeList = sysAdminManager.getDataList("lu_ErrorCodes", "");
                Map<Integer, String> errorMap = new HashMap<Integer, String>();
                for (TableData error : errorCodeList) {
                    errorMap.put(error.getId(), error.getDisplayText());
                }
                
                //ws status map
                Map<Integer, String> statusMap = new HashMap<Integer, String>();
                statusMap.put(1, "To be processed");
                statusMap.put(2, "Processed");
                statusMap.put(3, "Rejected");
                statusMap.put(4, "Being Process");

                for (WSMessagesIn wsIn : wsMessagesList) {
                	//set error text
                	wsIn.setErrorDisplayText(errorMap.get(wsIn.getErrorId()));
                	//set org name
                	if (wsIn.getOrgId() == 0) {
                		wsIn.setOrgName("No Org Match");
                	} else {
                		wsIn.setOrgName(organizationmanager.getOrganizationById(wsIn.getOrgId()).getOrgName());
                	}
                	//set status
                	wsIn.setStatusName(statusMap.get(wsIn.getStatusId()));
                	
                	if (wsIn.getBatchUploadId() != 0) {
                		wsIn.setBatchName(transactionInManager.getBatchDetails(wsIn.getBatchUploadId()).getutBatchName());
                	}
                }
            }

            mav.addObject("wsMessages", wsMessagesList);

        } catch (Exception e) {
            throw new Exception("Error occurred viewing the single inbound web service messages.", e);
        }

        return mav;

    }
    
    
    @RequestMapping(value = "/dlExport", method = {RequestMethod.GET})
    public void dlExport(@RequestParam String i, @RequestParam String v, 
    		HttpSession session, HttpServletResponse response) throws Exception {
    	
    	User userDetails = new User();
    	Integer exportId = 0;
    	
    	boolean canViewReport = false;
    	if (session.getAttribute("userDetails") != null) {
    		userDetails = (User) session.getAttribute("userDetails");
    		//1 decrpt and get the reportId
            decryptObject decrypt = new decryptObject();
            Object obj = decrypt.decryptObject(i, v);
            String[] result = obj.toString().split((","));
            exportId = Integer.parseInt(result[0].substring(4));
            
            //now we get the report details
            referralActivityExports export = transactionInManager.getReferralActivityExportById(exportId);
            
            if (export != null) {
            	//we check permission and program
            	if (userDetails.getRoleId() != 2)  {
            		canViewReport = true;
            	}
            } 
            //we log them, grab report for them to download
            //if report doesn't exist we send them back to list with a message
            UserActivity ua = new UserActivity();
            ua.setUserId(userDetails.getId());
            ua.setAccessMethod("POST");
            ua.setPageAccess("/dlReport");
           
            
            if (!canViewReport) {
            	//log user activity
            	ua.setActivity("Tried to View Export - " + exportId);
                usermanager.insertUserLog(ua);
            }   else {
            	ua.setActivity("Viewed Export - " + exportId);
                usermanager.insertUserLog(ua);
            	
                //generate the report for user to download
            	//need to get report path
                fileSystem dir = new fileSystem();
                dir.setDirByName("referralActivityExports/");
                String filePath = dir.getDir();
                String fileName = export.getFileName();
            	try {
            		File f = new File(dir.getDir() + export.getFileName());

	            	 if(!f.exists()){
	            		 throw new Exception("Error with File " + dir.getDir() + export.getFileName());
	            	 }
            	 } catch (Exception e) {
            		 try {
           	    	  //update file to error
           	    	  export.setStatusId(5);
           	    	  transactionInManager.updateReferralActivityExport(export);
           	    	  throw new Exception("File does not exists " + dir.getDir() + export.getFileName());
           	      } catch (Exception ex1) {
           	    	  throw new Exception("File does not exists " + dir.getDir() + export.getFileName() + ex1);
           	      }
       	    	  
       	      	}

          	  
            	try {
            		  // get your file as InputStream
            		  InputStream is = new FileInputStream(filePath+fileName);
            	      // copy it to response's OutputStream
            	      
            	      String mimeType = "application/octet-stream";
            		  response.setContentType(mimeType);
            		  response.setHeader("Content-Transfer-Encoding", "binary");
                      response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                      org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
                      response.flushBuffer();
            	      is.close();
            	      
            	      
            	      
                      //update status
                      if (export.getStatusId() == 3) {
                    	  export.setStatusId(4);
                    	  transactionInManager.updateReferralActivityExport(export);
                      }
            	    } catch (IOException ex) {
            	    	ex.printStackTrace();
            	    	System.out.println("Error writing file to output stream. Filename was '{}'"+ fileName + ex);
            	      try {
            	    	  //update file to error
            	    	  export.setStatusId(5);
            	    	  transactionInManager.updateReferralActivityExport(export);
            	    	  throw new Exception("Error with File " +filePath + fileName + ex);
            	      } catch (Exception e) {
            	    	  throw new Exception("Error with File " +filePath + fileName + ex);
            	      }
            	    }
            	}
            
        } else {
    		//someone somehow got to this link, we just log
    		//we log who is accessing 
            //now we have report id, we check to see which program it belongs to and if the user has permission
        	UserActivity ua = new UserActivity();
            ua.setUserId(userDetails.getId());
            ua.setAccessMethod("POST");
            ua.setPageAccess("/dlReport");
            ua.setActivity("Tried to view export - " + exportId);
            usermanager.insertUserLog(ua);
            throw new Exception("invalid export view - " + exportId);	
    	}
    	
    }  
    
    @RequestMapping(value = "/delExport", method = {RequestMethod.GET})
    public ModelAndView delExport(@RequestParam String i, @RequestParam String v, 
    		HttpSession session, HttpServletResponse response) throws Exception {
    	
    	User userDetails = new User();
    	Integer exportId = 0;
    	
    	boolean canDeleteReport = false;
    	if (session.getAttribute("userDetails") != null) {
    		userDetails = (User) session.getAttribute("userDetails");
    		//1 decrpt and get the reportId
            decryptObject decrypt = new decryptObject();
            Object obj = decrypt.decryptObject(i, v);
            String[] result = obj.toString().split((","));
            exportId = Integer.parseInt(result[0].substring(4));
            
            //now we get the report details
            referralActivityExports export = transactionInManager.getReferralActivityExportById(exportId);
            
            if (export != null) {
            	//we check permission and program
            	if (userDetails.getRoleId() != 2)  {
            		canDeleteReport = true;
            	}
            } 
            //we log them, grab report for them to download
            //if report doesn't exist we send them back to list with a message
            UserActivity ua = new UserActivity();
            ua.setUserId(userDetails.getId());
            ua.setAccessMethod("GET");
            ua.setPageAccess("/delReport");
           
            
            if (!canDeleteReport) {
            	//log user activity
            	ua.setActivity("Tried to Delete Export - " + exportId);
                usermanager.insertUserLog(ua);
            }   else {
            	ua.setActivity("Deleted Export - " + exportId);
                usermanager.insertUserLog(ua);
            	export.setStatusId(5);
            	transactionInManager.updateReferralActivityExport(export);
            }
            
        } else {
    		//someone somehow got to this link, we just log
    		//we log who is accessing 
            //now we have report id, we check to see which program it belongs to and if the user has permission
        	UserActivity ua = new UserActivity();
            ua.setUserId(userDetails.getId());
            ua.setAccessMethod("GET");
            ua.setPageAccess("/dlReport");
            ua.setActivity("Tried to delete export - " + exportId);
            usermanager.insertUserLog(ua);
            throw new Exception("invalid delete export view - " + exportId);	
    	}
    	
    	ModelAndView mav = new ModelAndView(new RedirectView("referralActivityExport"));
        return mav;
    	
    }  
}
