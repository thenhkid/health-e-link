/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.dph.controller;

import com.ut.dph.dao.messageTypeDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.model.User;
import com.ut.dph.model.UserActivity;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationMessageSpecs;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.custom.ConfigErrorInfo;
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.service.organizationManager;
import com.ut.dph.service.sysAdminManager;
import com.ut.dph.service.transactionInManager;
import com.ut.dph.service.transactionOutManager;
import com.ut.dph.service.userManager;

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
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/Health-e-Connect")
public class HealtheConnectController {

    @Autowired
    private configurationManager configurationManager;

    @Autowired
    private messageTypeManager messagetypemanager;

    @Autowired
    private organizationManager organizationmanager;

    @Autowired
    private transactionInManager transactionInManager;

    @Autowired
    private sysAdminManager sysAdminManager;

    @Autowired
    private userManager usermanager;

    @Autowired
    private transactionOutManager transactionOutManager;

    @Autowired
    private configurationTransportManager configurationtransportmanager;

    @Autowired
    private messageTypeDAO messageTypeDAO;

    /**
     * The private maxResults variable will hold the number of results to show per list page.
     */
    private static int maxResults = 20;

    /**
     * this list holds the status that we do not want the audit reports to show *
     */
    private static List<Integer> excludedStatusIds = Arrays.asList(1, 2);

    //final status Ids
    private List<Integer> finalStatusIds = Arrays.asList(11, 12, 13, 16);

    /**
     * The '/upload' request will serve up the Health-e-Connect upload page.
     *
     * @param request
     * @param response
     * @return	the health-e-Connect upload view
     * @throws Exception
     */
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public ModelAndView viewUploadForm(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        if (searchTerm == null) {
            searchTerm = "";
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/upload");

        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("currentPage", 1);

        /* Need to get a list of uploaded files */
        User userInfo = (User) session.getAttribute("userDetails");

        try {
            /* Need to get a list of all uploaded batches */
            Integer totaluploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, 1, 0).size();
            List<batchUploads> uploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, 1, maxResults);

            if (!uploadedBatches.isEmpty()) {
                uploadedBatches = transactionInManager.populateBatchInfo(uploadedBatches, userInfo);
            }

            List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);
            boolean hasConfigurations = false;
            if (configurations.size() >= 1) {
                hasConfigurations = true;
            }

            mav.addObject("hasConfigurations", hasConfigurations);
            mav.addObject("uploadedBatches", uploadedBatches);
            mav.addObject("searchTerm", searchTerm);

            Integer totalPages = (int) Math.ceil((double) totaluploadedBatches / maxResults);
            mav.addObject("totalPages", totalPages);

            /**
             * log user activity *
             */
            UserActivity ua = new UserActivity();
            ua.setUserId(userInfo.getId());
            ua.setAccessMethod(request.getMethod());
            ua.setPageAccess("/upload");
            ua.setActivity("View Uploads Page");
            if (!searchTerm.equalsIgnoreCase("")) {
                String logString = "Search Term - " + searchTerm;
                if (uploadedBatches.size() < 1) {
                    logString = " without permission - " + logString + " returned zero results.";
                }
                ua.setActivityDesc(logString);
            }
            usermanager.insertUserLog(ua);
        } catch (Exception e) {
            throw new Exception("Error occurred viewing the uploaded batches. userId: " + userInfo.getId(), e);
        }

        return mav;
    }

    /**
     * The '/upload' POST request will serve up the Health-e-Connect upload batch page.
     *
     * @param request
     * @param response
     * @return	the health-e-Connect upload view
     * @throws Exception
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView findUploads(@RequestParam(value = "page", required = false) Integer page, @RequestParam String searchTerm, @RequestParam Date fromDate, @RequestParam Date toDate, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/upload");

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);

        /* Need to get a list of uploaded files */
        User userInfo = (User) session.getAttribute("userDetails");

        try {
            /* Need to get a list of all uploaded batches */
            Integer totaluploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, 1, 0).size();
            List<batchUploads> uploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, page, maxResults);

            if (!uploadedBatches.isEmpty()) {
                uploadedBatches = transactionInManager.populateBatchInfo(uploadedBatches, userInfo);
            }

            List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);
            boolean hasConfigurations = false;
            if (configurations.size() >= 1) {
                hasConfigurations = true;
            }

            mav.addObject("hasConfigurations", hasConfigurations);

            mav.addObject("uploadedBatches", uploadedBatches);

            Integer totalPages = (int) Math.ceil((double) totaluploadedBatches / maxResults);
            mav.addObject("totalPages", totalPages);
            mav.addObject("searchTerm", searchTerm);
            mav.addObject("currentPage", page);

            /**
             * log user activity *
             */
            UserActivity ua = new UserActivity();
            ua.setUserId(userInfo.getId());
            ua.setAccessMethod(request.getMethod());
            ua.setPageAccess("/upload");
            ua.setActivity("View Uploads Page");
            ua.setActivityDesc("Page - " + page + " Search Term - " + searchTerm + " From Date -" + fromDate + " To Date - " + toDate);
            usermanager.insertUserLog(ua);

            return mav;
        } catch (Exception e) {
            throw new Exception("Error occurred searching uploaded batches.", e);
        }

    }

    /**
     * The '/fileUploadForm' GET request will be used to display the blank file upload screen (In a modal)
     *
     *
     * @return	The file upload blank form page
     *
     *
     */
    @RequestMapping(value = "/fileUploadForm", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView fileUploadForm(HttpSession session) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/uploadForm");

        /* Need to get all available message types for this user */
        Map<Integer, String> configMessageTypes = new HashMap<Integer, String>();

        User userInfo = (User) session.getAttribute("userDetails");
        List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);

        for (configuration config : configurations) {
            configMessageTypes.put(config.getId(), messagetypemanager.getMessageTypeById(config.getMessageTypeId()).getName());
        }

        boolean allowmultipleMessageTypes = false;
        List<configurationTransport> transportTypes = configurationtransportmanager.getDistinctConfigTransportForOrg(userInfo.getOrgId(), 1);

        if (transportTypes.size() == 1) {
            allowmultipleMessageTypes = true;
        }

        mav.addObject("configurations", configMessageTypes);
        mav.addObject("allowmultipleMessageTypes", allowmultipleMessageTypes);

        return mav;
    }

    /**
     * The '/submitFileUpload' POST request will submit the new file and run the file through various validations. If a single validation fails the batch will be put in a error validation status and the file will be removed from the system. The user will receive an error message on the screen letting them know which validations have failed and be asked to upload a new file.
     *
     * The following validations will be taken place. - File is not empty - Proper file type (as determined in the configuration set up) - Proper delimiter (as determined in the configuration set up) - Does not exceed file size (as determined in the configuration set up)
     */
    @RequestMapping(value = "/submitFileUpload", method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView submitFileUpload(RedirectAttributes redirectAttr, HttpSession session, @RequestParam(value = "configId", required = true) Integer configId, @RequestParam(value = "uploadedFile", required = true) MultipartFile uploadedFile) throws Exception {

        /* Get the organization details for the source (Sender) organization */
        User userInfo = (User) session.getAttribute("userDetails");
        Organization sendingOrgDetails = organizationmanager.getOrganizationById(userInfo.getOrgId());

        configuration configDetails = null;
        configurationTransport transportDetails = null;
        configurationMessageSpecs messageSpecs = null;
        String delimChar = null;
        boolean multipleMessageTypes = false;

        /* 
         When Multiple Message Types is selected we need to find one config Id attached to the user in order
         to pull the information to process the file.
         */
        if (configId == 0) {

            /* Need to get list of available configurations for the user */
            List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);

            /* Pull the first configujration in the list */
            configId = configurations.get(0).getId();

            /* Need to get the details of the configuration */
            configDetails = configurationManager.getConfigurationById(configId);
            transportDetails = configurationtransportmanager.getTransportDetails(configId);
            messageSpecs = configurationManager.getMessageSpecs(configId);

            delimChar = (String) messageTypeDAO.getDelimiterChar(transportDetails.getfileDelimiter());

            multipleMessageTypes = true;
        } else {
            /* Need to get the details of the configuration */
            configDetails = configurationManager.getConfigurationById(configId);
            transportDetails = configurationtransportmanager.getTransportDetails(configId);
            messageSpecs = configurationManager.getMessageSpecs(configId);

            delimChar = (String) messageTypeDAO.getDelimiterChar(transportDetails.getfileDelimiter());

        }

        try {
            /* Upload the file */
            Map<String, String> batchResults = transactionInManager.uploadBatchFile(configId, uploadedFile);

            /* Need to add the file to the batchUploads table */
            /* Create the batch name (OrgId+MessageTypeId+Date/Time) */
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            String batchName = new StringBuilder().append(userInfo.getOrgId()).append(configDetails.getMessageTypeId()).append(dateFormat.format(date)).toString();

            /* Submit a new batch */
            batchUploads batchUpload = new batchUploads();
            batchUpload.setOrgId(userInfo.getOrgId());
            batchUpload.setuserId(userInfo.getId());
            batchUpload.setutBatchName(batchName);
            batchUpload.settransportMethodId(1);
            batchUpload.setoriginalFileName(batchName);
            batchUpload.setoriginalFileName(batchResults.get("fileName"));
            batchUpload.setFileLocation(transportDetails.getfileLocation());
            batchUpload.setContainsHeaderRow(messageSpecs.getcontainsHeaderRow());
            batchUpload.setDelimChar(delimChar);

            if (multipleMessageTypes == true) {
                batchUpload.setConfigId(0);
            } else {
                batchUpload.setConfigId(configId);
            }


            /* Set the status to the batch as SFV (Source Failed Validation) */
            batchUpload.setstatusId(1);

            Integer batchId = (Integer) transactionInManager.submitBatchUpload(batchUpload);

            List<Integer> errorCodes = new ArrayList<Integer>();

            Object emptyFileVal = batchResults.get("emptyFile");
            if (emptyFileVal != null) {
                errorCodes.add(1);
            }

            Object wrongSizeVal = batchResults.get("wrongSize");
            if (wrongSizeVal != null) {
                errorCodes.add(2);
            }

            Object wrongFileTypeVal = batchResults.get("wrongFileType");
            if (wrongFileTypeVal != null) {
                errorCodes.add(3);
            }

            Object wrongDelimVal = batchResults.get("wrongDelim");
            if (wrongDelimVal != null) {
                errorCodes.add(4);
            }

            /* Make sure the org doesn't have multiple configurations with different delims, headers, etc */
            if (multipleMessageTypes == true) {
                List<configurationTransport> transportTypes = configurationtransportmanager.getDistinctConfigTransportForOrg(userInfo.getOrgId(), 1);

                if (transportTypes.size() != 1) {
                    errorCodes.add(5);
                }
            }


            /* If Passed validation update the status to Source Submission Accepted */
            if (0 == errorCodes.size()) {
                /* Get the details of the batch */
                batchUploads batch = transactionInManager.getBatchDetails(batchId);
                batch.setstatusId(2);

                transactionInManager.submitBatchUploadChanges(batch);

                /* Redirect to the list of uploaded batches */
                redirectAttr.addFlashAttribute("savedStatus", "uploaded");

            } else {
                redirectAttr.addFlashAttribute("savedStatus", "error");
                redirectAttr.addFlashAttribute("errorCodes", errorCodes);
            }

            ModelAndView mav = new ModelAndView(new RedirectView("upload"));
            return mav;

        } catch (Exception e) {
            throw new Exception("Error occurred uploading a new file. configId: " + configId, e);
        }

    }

    /**
     * The '/download' GET request will serve up the Health-e-Connect download batch page.
     *
     * @param request
     * @param response
     * @return	the health-e-Connect download view
     * @throws Exception
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ModelAndView viewDownloads(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/download");

        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("currentPage", 1);

        /* Need to get a list of uploaded files */
        User userInfo = (User) session.getAttribute("userDetails");

        try {
            /* Need to get a list of all uploaded batches */
            Integer totaldownloadableBatches = transactionOutManager.getdownloadableBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, "", 1, 0).size();
            List<batchDownloads> downloadableBatches = transactionOutManager.getdownloadableBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, "", 1, maxResults);

            if (!downloadableBatches.isEmpty()) {
                for (batchDownloads batch : downloadableBatches) {

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }

            mav.addObject("downloadableBatches", downloadableBatches);

            Integer totalPages = (int) Math.ceil((double) totaldownloadableBatches / maxResults);
            mav.addObject("totalPages", totalPages);

            return mav;
        } catch (Exception e) {
            throw new Exception("Error occurred trying to view the download screen. userId: " + userInfo.getId(), e);
        }

    }

    /**
     * The '/download' POST request will serve up the Health-e-Connect download batch page.
     *
     * @param request
     * @param response
     * @return	the health-e-Connect download view
     * @throws Exception
     */
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public ModelAndView findDownloads(@RequestParam(value = "page", required = false) Integer page, @RequestParam String searchTerm, @RequestParam Date fromDate, @RequestParam Date toDate, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/download");

        System.out.println(fromDate);

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);

        /* Need to get a list of uploaded files */
        User userInfo = (User) session.getAttribute("userDetails");

        try {
            /* Need to get a list of all uploaded batches */
            Integer totaldownloadableBatches = transactionOutManager.getdownloadableBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, 1, 0).size();
            List<batchDownloads> downloadableBatches = transactionOutManager.getdownloadableBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, page, maxResults);

            if (!downloadableBatches.isEmpty()) {
                for (batchDownloads batch : downloadableBatches) {

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }
            mav.addObject("downloadableBatches", downloadableBatches);

            Integer totalPages = (int) Math.ceil((double) totaldownloadableBatches / maxResults);
            mav.addObject("totalPages", totalPages);
            mav.addObject("searchTerm", searchTerm);
            mav.addObject("currentPage", page);

            return mav;
        } catch (Exception e) {
            throw new Exception("Error occurred searching downloadable batches.", e);
        }

    }

    /**
     * The 'downloadBatch.do' function will update the status of the batch and all the transactions associated to the batch when the download link is clicked.
     *
     * @param batchId The id of the clicked batch to download
     *
     * @return This function will simply return a 1.
     */
    @RequestMapping(value = "/downloadBatch.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Integer downloadBatch(@RequestParam(value = "batchId", required = false) Integer batchId) throws Exception {

        try {
            transactionOutManager.updateTargetBatchStatus(batchId, 22, "");

            transactionOutManager.updateTargetTransasctionStatus(batchId, 20);

            /* Need to find the batch Upload Id */
            List<transactionTarget> targets = transactionOutManager.getTransactionsByBatchDLId(batchId);

            if (!targets.isEmpty()) {
                for (transactionTarget target : targets) {
                    transactionInManager.updateBatchStatus(target.getbatchUploadId(), 22, "");

                    transactionInManager.updateTransactionStatus(target.getbatchUploadId(), 0, 0, 20);

                }
            }

            /* Update the last Downloaded field */
            transactionOutManager.updateLastDownloaded(batchId);

            return 1;
        } catch (Exception e) {
            throw new Exception("Error occurred downloading the file. batchId: " + batchId, e);
        }

    }

    /**
     * The '/auditReports' request will serve up the Health-e-Connect audit reports page.
     *
     * @param request
     * @param response
     * @return	the health-e-Connect report view
     * @throws Exception
     */
    @RequestMapping(value = "/auditReports", method = RequestMethod.GET)
    public ModelAndView viewAuditRpts(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws Exception {

        User userInfo = (User) session.getAttribute("userDetails");

        if (searchTerm == null) {
            searchTerm = "";
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/auditReports");

        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("currentPage", 1);

        try {
            /* Need to get a list of all uploaded batches */
            Integer totaluploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, 1, 0, excludedStatusIds).size();
            List<batchUploads> uploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, 1, maxResults, excludedStatusIds);

            if (!uploadedBatches.isEmpty()) {
                uploadedBatches = transactionInManager.populateBatchInfo(uploadedBatches, userInfo);
            }

            List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);
            boolean hasConfigurations = false;
            if (configurations.size() >= 1) {
                hasConfigurations = true;
            }

            mav.addObject("hasConfigurations", hasConfigurations);
            mav.addObject("uploadedBatches", uploadedBatches);
            mav.addObject("searchTerm", searchTerm);
            mav.addObject("user", userInfo);

            Integer totalPages = (int) Math.ceil((double) totaluploadedBatches / maxResults);
            mav.addObject("totalPages", totalPages);

            /**
             * log user activity *
             */
            UserActivity ua = new UserActivity();
            ua.setUserId(userInfo.getId());
            ua.setAccessMethod(request.getMethod());
            ua.setPageAccess("/auditReports");
            ua.setActivity("View Audit Reports Page");
            if (!searchTerm.equalsIgnoreCase("")) {
                String logString = "Search Term - " + searchTerm;
                if (uploadedBatches.size() < 1) {
                    logString = " without permission - " + logString + " returned zero results.";
                }
                ua.setActivityDesc(logString);
            }
            usermanager.insertUserLog(ua);
        } catch (Exception e) {
            throw new Exception("Error occurred viewing the audit reports. userId: " + userInfo.getId(), e);
        }

        return mav;
    }

    /**
     * The '/auditReports POST request will serve up the Health-e-Connect audit reports page.
     *
     * @param request
     * @param response
     * @return	the health-e-Connect audit reports search view
     * @throws Exception
     */
    @RequestMapping(value = "/auditReports", method = RequestMethod.POST)
    public ModelAndView findAuditRpts(@RequestParam(value = "page", required = false) Integer page, @RequestParam String searchTerm,
            @RequestParam Date fromDate, @RequestParam Date toDate, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        /* Need to get a list of uploaded files */
        User userInfo = (User) session.getAttribute("userDetails");

        /**
         * log user activity *
         */
        UserActivity ua = new UserActivity();
        ua.setUserId(userInfo.getId());
        ua.setAccessMethod(request.getMethod());
        ua.setPageAccess("/auditReports");
        ua.setActivity("Audit Reports Page");
        usermanager.insertUserLog(ua);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/auditReports");

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);

        try {
            /* Need to get a list of all uploaded batches */
            Integer totaluploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, 1, 0, excludedStatusIds).size();
            List<batchUploads> uploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, page, maxResults, excludedStatusIds);

            if (!uploadedBatches.isEmpty()) {
                uploadedBatches = transactionInManager.populateBatchInfo(uploadedBatches, userInfo);
            }

            List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);
            boolean hasConfigurations = false;
            if (configurations.size() >= 1) {
                hasConfigurations = true;
            }

            mav.addObject("hasConfigurations", hasConfigurations);

            mav.addObject("uploadedBatches", uploadedBatches);

            Integer totalPages = (int) Math.ceil((double) totaluploadedBatches / maxResults);
            mav.addObject("totalPages", totalPages);
            mav.addObject("searchTerm", searchTerm);
            mav.addObject("currentPage", page);
            return mav;
        } catch (Exception e) {
            throw new Exception("Error occurred searching audit report.", e);
        }

    }

    /**
     * The '/auditReport POST request will serve up the requested Health-e-Connect audit report .
     *
     * @param request
     * @param response
     * @return	the health-e-Connect audit reports search view
     * @throws Exception
     */
    @RequestMapping(value = "/auditReport", method = RequestMethod.POST)
    public ModelAndView viewAuditRpt(@RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "batchId", required = false) Integer batchId,
            HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        try {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/Health-e-Connect/auditReport");

            /* Need to get a list of uploaded files */
            User userInfo = (User) session.getAttribute("userDetails");
            batchUploads batchInfo = transactionInManager.getBatchDetails(batchId);

            if (batchInfo.getConfigId() != 0) {
                batchInfo.setConfigName(configurationManager.getMessageTypeNameByConfigId(batchInfo.getConfigId()));
            } else {
                batchInfo.setConfigName("Multiple Message Types");
            }
            /**
             * make sure user has permission to batch 1. if user uploaded the batch 2. if user has permission to the configs in the batch 3. sometimes entire batch is errored and have no configIds to go by, we let user see it if they have configurations
         *
             */

            List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);
            boolean hasConfigurations = false;

            if (configurations.size() >= 1) {
                hasConfigurations = true;
            }

            boolean hasPermission = transactionInManager.hasPermissionForBatch(batchInfo, userInfo, hasConfigurations);

            if (hasPermission) {
                /**
                 * grab org info*
                 */
                Organization org = organizationmanager.getOrganizationById(batchInfo.getOrgId());
                mav.addObject("org", org);

                /**
                 * grab error info - need to filter this by error type *
                 */
                List<ConfigErrorInfo> confErrorList = new LinkedList<ConfigErrorInfo>();
                confErrorList = transactionInManager.populateErrorListByErrorCode(batchInfo);
                mav.addObject("confErrorList", confErrorList);

            }

            /**
             * buttons *
             */
            /**
             * check final status - a batch should all be 11,12,13 or 16 to get released & batch status is PR *
             */
            boolean canSend = false;
            if (userInfo.getdeliverAuthority() && batchInfo.getstatusId() == 5) {
                // now we check so we don't have to make a db hit if batch status is not 5 
                if (transactionInManager.getRecordCounts(batchId, finalStatusIds, false, false) == 0) {
                    canSend = true;
                }
            }
            // check to see if it can be cancelled - 
            boolean canCancel = false;
            List<Integer> cancelStatusList = Arrays.asList(21, 22, 23, 1, 8);
            if (userInfo.getcancelAuthority() && !cancelStatusList.contains(batchInfo.getstatusId())) {
                canCancel = true;
            }

            boolean canEdit = false;
            if (userInfo.geteditAuthority() && batchInfo.getstatusId() == 5 && transactionInManager.getRecordCounts(batchId, Arrays.asList(14), false, true) > 0) {
                canEdit = true;
            }

            /**
             * log user activity *
             */
            UserActivity ua = new UserActivity();
            ua.setUserId(userInfo.getId());
            ua.setAccessMethod(request.getMethod());
            ua.setPageAccess("/auditReport");
            ua.setActivity("Audit Report Request");
            ua.setBatchIds(String.valueOf(batchInfo.getId()));
            if (!hasPermission) {
                ua.setActivityDesc("without permission");
            }
            usermanager.insertUserLog(ua);

            //buttons
            mav.addObject("canSend", canSend);
            mav.addObject("canCancel", canCancel);
            mav.addObject("canEdit", canEdit);
            mav.addObject("batch", batchInfo);
            mav.addObject("hasPermission", hasPermission);
            mav.addObject("hasConfigurations", hasConfigurations);

            Integer totalPages = 0;

            //(int)Math.ceil((double)totalErrorPages / maxResults);
            mav.addObject("totalPages", totalPages);
            //for errors
            mav.addObject("currentPage", page);

            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error occurred displaying audit report.", e);
        }

    }

    /**
     * The '/edit POST request will serve up the requested Health-e-Connect audit report edit page. This page will be used to fix errors within a transaction.
     *
     * @param request
     * @param response
     * @return	the ERG form
     * @throws Exception
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView editTransaction(@RequestParam(value = "transactionInId", required = true) Integer transactionInId,
            @RequestParam(value = "batchIdERG", required = true) Integer batchId,
            RedirectAttributes redirectAttr,
            HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        try {
            boolean hasPermission = false;
            boolean canEdit = false;
            boolean hasConfigurations = false;

            /**
             * check for permission*
             */
            User userInfo = (User) session.getAttribute("userDetails");
            batchUploads batchInfo = transactionInManager.getBatchDetailsByTInId(transactionInId);
            if (batchInfo != null) {
                List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);

                if (configurations.size() >= 1) {
                    hasConfigurations = true;
                }

                hasPermission = transactionInManager.hasPermissionForBatch(batchInfo, userInfo, hasConfigurations);

                /**
                 * get transaction details *
                 */
                /**
                 * for button to save/release*
                 */
                if (batchInfo.getstatusId() == 5 && userInfo.geteditAuthority()) {
                    canEdit = true;
                }
            }
            
            /* If user has edit athoritity then show the edit page, otherwise redirect back to the auditReport */
            if(canEdit == true) {
                
                ModelAndView mav = new ModelAndView();
                mav.setViewName("/Health-e-Connect/ERG");
                mav.addObject("canEdit", canEdit);
                mav.addObject("hasConfigurations", hasConfigurations);
                mav.addObject("hasPermission", hasPermission);
                mav.addObject("transactionInId", transactionInId);

                /**
                 * log user activity *
                 */
                UserActivity ua = new UserActivity();
                ua.setUserId(userInfo.getId());
                ua.setAccessMethod(request.getMethod());
                ua.setPageAccess("/Health-e-Connect/ERG");
                ua.setActivity("ERG View");
                ua.setTransactionInIds(String.valueOf(transactionInId));
                if (batchInfo != null) {
                    ua.setBatchIds(String.valueOf(batchInfo.getId()));
                }
                if (!hasPermission) {
                    ua.setActivityDesc("without permission");
                }
                usermanager.insertUserLog(ua);
                
                return mav;
                
            }
            else {
                ModelAndView mav = new ModelAndView(new RedirectView("/Health-e-Connect/upload"));
                return mav;
            }

            
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error occurred displaying upload ERG form.", e);
        }

    }

    /**
     * The '/batchOptions POST request will serve up the requested Health-e-Connect audit report .
     *
     * @param request
     * @param response
     * @return	we redirect back to /auditReports
     * @throws Exception
     */
    @RequestMapping(value = "/batchOptions", method = RequestMethod.POST)
    public ModelAndView batchOptions(@RequestParam(value = "batchId", required = false) Integer batchId,
            @RequestParam(value = "idList", required = false) List<Integer> idList,
            @RequestParam(value = "batchOption", required = false) String batchOption,
            RedirectAttributes redirectAttr,
            HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        try {

            /**
             * Four options - rejectMessages - canEdit - need to POST form and refresh audit report releaseBatch - canSend resetBatch, cancelBatch - canCancel
    	 * *
             */
            System.out.println(batchOption);
            System.out.println("idList is " + idList);

            /**
             * check for permission*
             */
            User userInfo = (User) session.getAttribute("userDetails");
            batchUploads batchInfo = transactionInManager.getBatchDetails(batchId);
            boolean hasConfigurations = false;
            boolean hasPermission = false;
            String redirectPage = "auditReports?searchTerm=" + batchInfo.getutBatchName();
            String systemMessage = "";

            if (batchInfo != null) {
                List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);

                if (configurations.size() >= 1) {
                    hasConfigurations = true;
                }

                hasPermission = transactionInManager.hasPermissionForBatch(batchInfo, userInfo, hasConfigurations);
            }
            if (hasPermission) {

                boolean allowBatchClear = transactionInManager.allowBatchClear(batchId);
                /**
                 * make sure user has the appropriate permission to this batch *
                 */
                if (batchOption.equalsIgnoreCase("resetBatch")) { // canCancel
                    redirectPage = "upload?searchTerm=" + batchInfo.getutBatchName();
                    //check to make sure we can clear batch and then delete info and reset
                    if (allowBatchClear && userInfo.getcancelAuthority()) {
    				//reset batch takes batch back to statusId of 2
                        //1. set batch process to 4
                        transactionInManager.updateBatchStatus(batchId, 4, "");
                        //2. clear
                        boolean cleared = transactionInManager.clearBatch(batchId);
                        if (cleared) {
                            transactionInManager.updateBatchStatus(batchId, 2, "startOver");
                            systemMessage = "Batch is reset.";
                        } else {
                            transactionInManager.updateBatchStatus(batchId, 29, "endDateTime");
                            systemMessage = "An error occurred while resetting batch.  Please review logs.";
                        }
                    } else {
                        systemMessage = "You do not have permission to reset a batch.";
                        hasPermission = false;

                    }

                } else if (batchOption.equalsIgnoreCase("cancelBatch")) {
                    //check authority
                    if (allowBatchClear && userInfo.getcancelAuthority()) {
                        transactionInManager.updateBatchStatus(batchId, 4, "startDateTime");

                        boolean cleared = transactionInManager.clearBatch(batchId);
                        if (cleared) {
                            transactionInManager.updateBatchStatus(batchId, 21, "endDateTime");
                            systemMessage = "Batch is set to 'Do Not Process'.";
                        } else {
                            transactionInManager.updateBatchStatus(batchId, 29, "endDateTime");
                            systemMessage = "An error occurred while resetting batch.  Please review logs.";
                        }
                    } else {
                        systemMessage = "You do not have permission to cancel a batch.";
                        hasPermission = false;
                    }
                } else if (batchOption.equalsIgnoreCase("releaseBatch")) {
                    if (allowBatchClear && userInfo.getdeliverAuthority()) {
                        transactionInManager.updateBatchStatus(batchId, 4, "startDateTime");
                        //check once again to make sure all transactions are in final status
                        if (transactionInManager.getRecordCounts(batchId, Arrays.asList(11, 12, 13, 16), false, false) == 0) {
                            transactionInManager.updateBatchStatus(batchId, 6, "endDateTime");
                        } else {
                            transactionInManager.updateBatchStatus(batchId, 5, "endDateTime");
                            systemMessage = "All transactions must be in final status before it can be release.  Please review audit report";
                        }
                    } else {
                        transactionInManager.updateBatchStatus(batchId, 5, "endDateTime");
                        systemMessage = "You do not have permission to release a batch.";
                        hasPermission = false;
                    }
                } else if (batchOption.equalsIgnoreCase("rejectMessages")) {
                    if (batchInfo.getstatusId() == 5 && userInfo.geteditAuthority()) {
                        if (idList.size() > 0) {
                            for (Integer transactionInId : idList) {
                                transactionInManager.updateTranStatusByTInId(transactionInId, 13);
                            }
                            systemMessage = "Transactions are marked as rejected.";
                        }

                    } else {
                        systemMessage = "You do not have permission to reject these transactions.";
                        hasPermission = false;
                    }

                }
            } // end of permission

            //log user activity
            UserActivity ua = new UserActivity();
            ua.setUserId(userInfo.getId());
            ua.setAccessMethod(request.getMethod());
            ua.setPageAccess("/batchOptions");
            ua.setActivity("Batch Options -" + batchOption);
            ua.setBatchIds(String.valueOf(batchInfo.getId()));
            ua.setTransactionInIds(idList.toString());
            if (!hasPermission) {
                ua.setActivityDesc("without permission" + systemMessage);
            }
            usermanager.insertUserLog(ua);

            redirectAttr.addFlashAttribute("batchOptionStatus", systemMessage);
            ModelAndView mav = new ModelAndView(new RedirectView(redirectPage));
            //add messages
            return mav;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error at batch options.", e);
        }

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
}
