/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.dph.controller;

import com.ut.dph.dao.messageTypeDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.model.Transaction;
import com.ut.dph.model.User;
import com.ut.dph.model.UserActivity;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationMessageSpecs;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.custom.TransErrorDetail;
import com.ut.dph.model.custom.TransErrorDetailDisplay;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
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
    public ModelAndView viewUploadForm(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/upload");

        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);

        /* Need to get a list of uploaded files */
        User userInfo = (User) session.getAttribute("userDetails");

        try {
            List<batchUploads> uploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate);

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
           
            /**
             * log user activity *
             */
            UserActivity ua = new UserActivity();
            ua.setUserId(userInfo.getId());
            ua.setAccessMethod(request.getMethod());
            ua.setPageAccess("/upload");
            ua.setActivity("View Uploads Page");
            usermanager.insertUserLog(ua);
        } catch (Exception e) {
            throw new Exception("Error occurred viewing the uploaded batches. userId: " + userInfo.getId(), e);
        }

        return mav;
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

        /* Need to get a list of uploaded files */
        User userInfo = (User) session.getAttribute("userDetails");

        try {
            List<batchDownloads> downloadableBatches = transactionOutManager.getdownloadableBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate);

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
    public ModelAndView findDownloads(@RequestParam Date fromDate, @RequestParam Date toDate, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/download");

        System.out.println(fromDate);

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);

        /* Need to get a list of uploaded files */
        User userInfo = (User) session.getAttribute("userDetails");

        try {
            List<batchDownloads> downloadableBatches = transactionOutManager.getdownloadableBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate);

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
     * @return	the health-e-Connect audit report list
     * @throws Exception
     */
    @RequestMapping(value = "/auditReports", method = RequestMethod.GET)
    public ModelAndView viewAuditRpts(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        User userInfo = (User) session.getAttribute("userDetails");
        boolean showRelButton = false;
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/auditReports");

        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");

        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);

        try {
            List<batchUploads> uploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, excludedStatusIds);

            if (!uploadedBatches.isEmpty()) {
            	for (batchUploads batch : uploadedBatches) {
                    List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());
                    if (batch.getstatusId() == 5) {
                    	Integer transTotalNotFinal = transactionInManager.getRecordCounts(batch.getId(), finalStatusIds, false, false);
                    	batch.setTransTotalNotFinal(transTotalNotFinal); 
                    	if (transTotalNotFinal == 0) {
                    		showRelButton = true;
                    	}
                    }

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }

            List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);
            boolean hasConfigurations = false;
            if (configurations.size() >= 1) {
                hasConfigurations = true;
            }

            mav.addObject("showRelButton", showRelButton);
            mav.addObject("hasConfigurations", hasConfigurations);
            mav.addObject("uploadedBatches", uploadedBatches);
            mav.addObject("user", userInfo);

            /**
             * log user activity *
             */
            UserActivity ua = new UserActivity();
            ua.setUserId(userInfo.getId());
            ua.setAccessMethod(request.getMethod());
            ua.setPageAccess("/auditReports");
            ua.setActivity("View Audit Reports Page");
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
    public ModelAndView findAuditRpts(@RequestParam Date fromDate, @RequestParam Date toDate, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

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

        boolean showRelButton = false;
        try {
            List<batchUploads> uploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, excludedStatusIds);

            if (!uploadedBatches.isEmpty()) {
            	for (batchUploads batch : uploadedBatches) {
                    List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());
                    if (batch.getstatusId() == 5) {
                    	Integer transTotalNotFinal = transactionInManager.getRecordCounts(batch.getId(), finalStatusIds, false, false);
                    	batch.setTransTotalNotFinal(transTotalNotFinal); 
                    	if (transTotalNotFinal == 0) {
                    		showRelButton = true;
                    	}
                    }

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }

            List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);
            boolean hasConfigurations = false;
            if (configurations.size() >= 1) {
                hasConfigurations = true;
            }

            mav.addObject("hasConfigurations", hasConfigurations);
            mav.addObject("showRelButton", showRelButton);
            mav.addObject("uploadedBatches", uploadedBatches);

            return mav;
        } catch (Exception e) {
            throw new Exception("Error occurred searching audit report.", e);
        }

    }

    /**
     * The '/auditReport POST request will serve up the requested Health-e-Connect audit report .
     *
     * @param request - batchId
     * @param response
     * @return	the health-e-Connect detailed audit report view
     * @throws Exception
     */
    @RequestMapping(value = "/auditReport", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView viewAuditRpt(@RequestParam(value = "batchId", required = false) Integer batchId, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        try {
        
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/Health-e-Connect/auditReport");

            /* Need to get a list of uploaded files */
            User userInfo = (User) session.getAttribute("userDetails");
            batchUploads batchInfo = transactionInManager.getBatchDetails(batchId);
            lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batchInfo.getstatusId());
            batchInfo.setstatusValue(processStatus.getDisplayCode());


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
                
                //grab error info
                List<TransErrorDetailDisplay> errorList = new LinkedList<TransErrorDetailDisplay>();
                errorList = transactionInManager.populateErrorList(batchInfo);
                mav.addObject("errorList", errorList);
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
            ua.setBatchUploadId(batchInfo.getId());
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
    public ModelAndView editTransaction(@RequestParam(value = "transactionInId", required = true) Integer transactionId,
            @RequestParam(value = "batchIdERG", required = true) Integer batchId,
            RedirectAttributes redirectAttr,
            HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        try {
            boolean hasPermission = false;
            boolean canEdit = false;
            boolean hasConfigurations = false;
            ModelAndView mav = new ModelAndView();
          
            //check for permission
            User userInfo = (User) session.getAttribute("userDetails");
            batchUploads batchInfo = transactionInManager.getBatchDetailsByTInId(transactionId);
            if (batchInfo != null) {
                List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);

                if (configurations.size() >= 1) {
                    hasConfigurations = true;
                }

                hasPermission = transactionInManager.hasPermissionForBatch(batchInfo, userInfo, hasConfigurations);

                if (batchInfo.getstatusId() == 5 && userInfo.geteditAuthority()) {
                    canEdit = true;
                }
            }

            /* If user has edit athoritity then show the edit page, otherwise redirect back to the auditReport */
            if (canEdit == true) {
            	mav.setViewName("/Health-e-Connect/ERG");
                mav.addObject("canEdit", canEdit);
                mav.addObject("hasConfigurations", hasConfigurations);
                mav.addObject("hasPermission", hasPermission);

                try {
                    transactionIn transactionInfo = transactionInManager.getTransactionDetails(transactionId);

                    /* Get the configuration details */
                    configuration configDetails = configurationManager.getConfigurationById(transactionInfo.getconfigId());

                    /* Get a list of form fields */
                    /*configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transactionInfo.getconfigId(), 2);*/
                    configurationTransport transportDetails = configurationtransportmanager.getTransportDetails(transactionInfo.getconfigId());
                    List<configurationFormFields> senderInfoFormFields = configurationtransportmanager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 1);
                    List<configurationFormFields> senderProviderFormFields = configurationtransportmanager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 2);
                    List<configurationFormFields> targetInfoFormFields = configurationtransportmanager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 3);
                    List<configurationFormFields> targetProviderFormFields = configurationtransportmanager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 4);
                    List<configurationFormFields> patientInfoFormFields = configurationtransportmanager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 5);
                    List<configurationFormFields> detailFormFields = configurationtransportmanager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(), transportDetails.getId(), 6);

                    Transaction transaction = new Transaction();
                    transactionInRecords records = null;

                    transactionTarget transactionTarget = transactionInManager.getTransactionTarget(transactionInfo.getbatchId(), transactionId);

                    transaction.settransactionId(transactionId);
                    transaction.setbatchId(batchInfo.getId());
                    transaction.setbatchName(batchInfo.getutBatchName());
                    transaction.setdateSubmitted(transactionInfo.getdateCreated());
                    transaction.setstatusId(batchInfo.getstatusId());
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

                    records = transactionInManager.getTransactionRecords(transactionId);
                    transaction.settransactionRecordId(records.getId());


                    /* Set all the transaction SOURCE ORG fields */
                    List<transactionRecords> fromFields;
                    if (!senderInfoFormFields.isEmpty()) {
                        fromFields = setOutboundFormFields(senderInfoFormFields, records, transactionInfo.getconfigId(), transactionId, 0);
                    } else {
                        fromFields = setOrgDetails(batchInfo.getOrgId());
                    }
                    transaction.setsourceOrgFields(fromFields);

                    /* Set all the transaction SOURCE PROVIDER fields */
                    List<transactionRecords> fromProviderFields = setOutboundFormFields(senderProviderFormFields, records, transactionInfo.getconfigId(), transactionId, 0);
                    transaction.setsourceProviderFields(fromProviderFields);

                    /* Set all the transaction TARGET fields */
                    List<transactionRecords> toFields;
                    if (!targetInfoFormFields.isEmpty()) {
                        toFields = setOutboundFormFields(targetInfoFormFields, records, transactionInfo.getconfigId(), transactionId, 0);

                        if ("".equals(toFields.get(0).getFieldValue()) || toFields.get(0).getFieldValue() == null) {
                            toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transactionInfo.getId()).gettargetOrgId());
                        }

                    } else {
                        toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transactionInfo.getId()).gettargetOrgId());
                    }
                    transaction.settargetOrgFields(toFields);

                    /* Set all the transaction TARGET PROVIDER fields */
                    List<transactionRecords> toProviderFields = setOutboundFormFields(targetProviderFormFields, records, transactionInfo.getconfigId(), transactionId, 0);
                    transaction.settargetProviderFields(toProviderFields);

                    /* Set all the transaction PATIENT fields */
                    List<transactionRecords> patientFields = setOutboundFormFields(patientInfoFormFields, records, transactionInfo.getconfigId(), transactionId, 0);
                    transaction.setpatientFields(patientFields);

                    /* Set all the transaction DETAIL fields */
                    List<transactionRecords> detailFields = setOutboundFormFields(detailFormFields, records, transactionInfo.getconfigId(), transactionId, 0);
                    transaction.setdetailFields(detailFields);
                    
                    
                    mav.addObject("transaction", transaction);

                    mav.addObject("transactionInId", transactionId);

                } catch (Exception e) {
                    throw new Exception("Error occurred in viewing the sent batch details. transactionId: " + transactionId, e);
                }

                
            } else {
                mav = new ModelAndView(new RedirectView("/Health-e-Connect/upload"));
                
            }
            
            /**
             * log user activity *
             */
            UserActivity ua = new UserActivity();
            ua.setUserId(userInfo.getId());
            ua.setAccessMethod(request.getMethod());
            ua.setPageAccess("/edit");
            ua.setActivity("Viewed Transaction with Error(s)");
            ua.setTransactionInIds(String.valueOf(transactionId));
            ua.setBatchUploadId(batchInfo.getId());
            if (!hasPermission) {
                ua.setActivityDesc("without permission");
            }
            usermanager.insertUserLog(ua);

            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error occurred displaying upload ERG form.", e);
        }

    }
    
    /**
     * The '/editMessage' POST request will submit the changes to the passed in transaction. The
     * transaction will be updated to a status of 10 (Pending Release) and the error records will be cleared
     * 
     * @param transactionDetails The object to hold the transaction fields
     * 
     */
    @RequestMapping(value = "/editMessage", method = RequestMethod.POST)
    public @ResponseBody Integer submitTransactionChanges(@ModelAttribute(value = "transactionDetails") Transaction transactionDetails,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
       
        /* Update the transactionInRecords */
        List<transactionRecords> sourceOrgFields = transactionDetails.getsourceOrgFields();
        List<transactionRecords> sourceProviderFields = transactionDetails.getsourceProviderFields();
        List<transactionRecords> targetOrgFields = transactionDetails.gettargetOrgFields();
        List<transactionRecords> targetProviderFields = transactionDetails.gettargetProviderFields();
        List<transactionRecords> patientFields = transactionDetails.getpatientFields();
        List<transactionRecords> detailFields = transactionDetails.getdetailFields(); 
        
        transactionInRecords records = transactionInManager.getTransactionRecords(transactionDetails.gettransactionId());
        
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
        
        records.setId(records.getId());
        transactionInManager.submitTransactionInRecordsUpdates(records);
        
        /* Update the transactionTranslatedIn records  */
        transactionInManager.submitTransactionTranslatedInRecords(transactionDetails.gettransactionId(), records.getId(), transactionDetails.getconfigId());
        
        /* Remove the transaction errors */
        transactionInManager.deleteTransactionInErrorsByTransactionId(transactionDetails.gettransactionId());
       
        /* Update the transaction status to 10 (PR Released) */
        transactionInManager.updateTransactionStatus(0, transactionDetails.gettransactionId(), 14, 10);
        
        /** update status so it will re-process **/
        transactionInManager.updateBatchStatus(transactionDetails.getbatchId(), 3, "startDateTime");
        
        /** re-process batch **/
        transactionInManager.processBatch(transactionDetails.getbatchId(), true);
        
        /** add logging **/
        UserActivity ua = new UserActivity();
        User userInfo = (User) session.getAttribute("userDetails");
        ua.setUserId(userInfo.getId());
        ua.setAccessMethod(request.getMethod());
        ua.setPageAccess("/editMessage");
        ua.setActivity("Modified Transaction with Error(s)");
        ua.setTransactionInIds(String.valueOf(transactionDetails.gettransactionId()));
        ua.setBatchUploadId(transactionDetails.getbatchId());
        usermanager.insertUserLog(ua);
        
        return 1;
        
    }

    /**
     * The '/batchOptions POST request will perform option for batch as selected by the user .
     * Four options - 
     * rejectMessages - canEdit - need to POST form and refresh audit report 
     * releaseBatch - canSend resetBatch 
     * cancelBatch - canEdit - need to POST form and refresh audit report - reject one transaction
     * resetBatch - canCancel
     * @param request
     * @param response
     * @return	we redirect all back to /auditReports except for resetBatch. resetBatch sends user back to 
     * 			uploads.
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
             * check for permission*
             */
            User userInfo = (User) session.getAttribute("userDetails");
            batchUploads batchInfo = transactionInManager.getBatchDetails(batchId);
            boolean hasConfigurations = false;
            boolean hasPermission = false;
            String redirectPage = "auditReports?searchTerm=" + batchInfo.getutBatchName();
            String systemMessage = "";
            String batchOptionSubmitted = "";

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
                	batchOptionSubmitted = "Reset Batch";
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
                	batchOptionSubmitted = "Cancelled Batch";
                    //check authority
                    if (allowBatchClear && userInfo.getcancelAuthority()) {
                        transactionInManager.updateBatchStatus(batchId, 4, "startDateTime");
                        transactionInManager.updateTransactionStatus(batchId, 0, 0, 34);
                        transactionInManager.updateTransactionTargetStatus(batchId, 0, 0, 34);
                        transactionInManager.updateBatchStatus(batchId, 21, "endDateTime");
                        systemMessage = "Batch is set to 'Do Not Process'.";

                    } else {
                        systemMessage = "You do not have permission to cancel a batch.";
                        hasPermission = false;
                    }
                } else if (batchOption.equalsIgnoreCase("releaseBatch")) {
                	batchOptionSubmitted = "Released Batch";
                	boolean canReleaseBatch = false;
                	if (batchInfo.getstatusId() == 5 && userInfo.getdeliverAuthority()) { // do the check that doesn't require a hit to db first
	   	        		 if (batchInfo.getConfigId() != 0) {
	   	        			 canReleaseBatch = transactionInManager.checkPermissionForBatch(userInfo, batchInfo);
	   	        		 } else if (configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), batchInfo.gettransportMethodId()).size() > 0) {
	   	        			 canReleaseBatch = true;
	   	        		 }
                	}
                	if (canReleaseBatch) {
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
                        systemMessage = "You do not have permission to release this batch.";
                        hasPermission = false;
                    }
                } else if (batchOption.equalsIgnoreCase("rejectMessages")) {
                	batchOptionSubmitted = "Rejected Messages";
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

                } else if (batchOption.equalsIgnoreCase("rejectMessage")) {
                	batchOptionSubmitted = "Rejected Message";
                    if (batchInfo.getstatusId() == 5 && userInfo.geteditAuthority()) {
                        if (idList.size() > 0) {
                            for (Integer transactionInId : idList) {
                                transactionInManager.updateTranStatusByTInId(transactionInId, 13);
                            }
                            systemMessage = "Transaction is marked as rejected.";
                        }

                    } else {
                        systemMessage = "You do not have permission to reject this transaction.";
                        hasPermission = false;
                    }

                }
            } // end of permission
            
            //log user activity
            UserActivity ua = new UserActivity();
            ua.setUserId(userInfo.getId());
            ua.setAccessMethod(request.getMethod());
            ua.setPageAccess("/batchOptions");
            ua.setActivity("Batch Options - " + batchOptionSubmitted);
            ua.setBatchUploadId(batchInfo.getId());
            if (idList.size() > 0) {
            	ua.setTransactionInIds(idList.toString().replace("]", "").replace("[", ""));
            }
            if (!hasPermission) {
                ua.setActivityDesc("without permission" + systemMessage);
            }
            usermanager.insertUserLog(ua);
            
            
            redirectAttr.addFlashAttribute("noErrors", hasPermission);
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
    public List<transactionRecords> setOutboundFormFields(List<configurationFormFields> formfields, transactionInRecords records, int configId, int transactionId, int orgId) throws NoSuchMethodException, Exception {

        List<transactionRecords> fields = new ArrayList<transactionRecords>();

        for (configurationFormFields formfield : formfields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(formfield.getFieldNo());
            field.setrequired(formfield.getRequired());
            field.setsaveToTable(formfield.getsaveToTableName());
            field.setsaveToTableCol(formfield.getsaveToTableCol());
            field.setfieldLabel(formfield.getFieldLabel());
            field.setreadOnly(false);
            field.setfieldValue(null);
            
            /* Get the error for each field */
            try {
                 List<TransErrorDetail> fieldErrors = transactionInManager.getTransactionErrorsByFieldNo(transactionId, formfield.getFieldNo());
                 
                 if(fieldErrors.size() > 0) {
                     StringBuilder errorDetails = new StringBuilder();
                     
                     for(TransErrorDetail error : fieldErrors) {
                           errorDetails.append(error.getErrorDisplayText());
                           if(!"".equals(error.getErrorInfo()) && error.getErrorInfo() != null) {
                               errorDetails.append(" - ").append(error.getErrorInfo());
                           }
                           errorDetails.append("<br />");
                     }
                     field.setErrorDesc(errorDetails.toString());
                 }
            }
            catch(Exception e) {
                 throw new Exception("Error at batch options.", e);
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
                    field.setErrorData(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }

                /* If autopopulate field is set make it read only */
                if (!tableName.isEmpty() && !tableCol.isEmpty()) {
                    field.setreadOnly(true);
                }
            } else if (orgId > 0) {

                if (!tableName.isEmpty() && !tableCol.isEmpty()) {
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
     * The '/releaseBatches POST request will loop through batches, check permission and set batches
     * to release status. .
     *
     * @param request - idList with batch Ids to be release
     * @param response 
     * @return	we redirect back to /auditReports views
     * @throws Exception
     */
    @RequestMapping(value = "/releaseBatches", method = RequestMethod.POST)
    public ModelAndView massReleaseBatches(@RequestParam(value = "idList", required = false) List<Integer> idList,
            RedirectAttributes redirectAttr, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

    	boolean canReleaseBatch = false;
        String redirectPage = "auditReports";
        String systemMessage = "";
        boolean noErrors = true;
        
        try {
        	User userInfo = (User) session.getAttribute("userDetails");
        	/**
            * we loop batch and check for all final status, permission and then we release
            * **/
        	for (Integer batchId : idList) {
        		 String forInsert = "";
        		 batchUploads batchInfo = transactionInManager.getBatchDetails(batchId);
        		 //check to see if user is a valid sender for config, if 0, we check to see if user belong to batch's org and user need to have deliver authority
        		 if (batchInfo.getstatusId() == 5 && userInfo.getdeliverAuthority()) { // do the check that doesn't require a hit to db first
	        		 if (batchInfo.getConfigId() != 0) {
	        			 canReleaseBatch = transactionInManager.checkPermissionForBatch(userInfo, batchInfo);
	        		 } else if (configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), batchInfo.gettransportMethodId()).size() > 0) {
	        			 canReleaseBatch = true;
	        		 }
        		 }
                 if (canReleaseBatch) {
                         transactionInManager.updateBatchStatus(batchId, 4, "startDateTime");
                         //check once again to make sure all transactions are in final status
                         if (transactionInManager.getRecordCounts(batchId, Arrays.asList(11, 12, 13, 16), false, false) == 0) {
                             transactionInManager.updateBatchStatus(batchId, 6, "endDateTime");
                             forInsert = "Batch "+ batchInfo.getutBatchName() + " is released.";
                             systemMessage = systemMessage + forInsert + "<br/>";                           		 
                         } else {
                             transactionInManager.updateBatchStatus(batchId, 5, "endDateTime");
                             forInsert= "Batch "+ batchInfo.getutBatchName() + " has transactions that are not in final status. It is not released.";
                             systemMessage = systemMessage + forInsert + "<br/>";  
                         }
                   } else {
                         transactionInManager.updateBatchStatus(batchId, 5, "endDateTime");
                         forInsert = "You do not have permission to release Batch " + batchInfo.getutBatchName();
                         systemMessage = systemMessage + forInsert + "<br/>";
                         canReleaseBatch = false;
                         noErrors = false;
                   }
             		//log user activity
                     UserActivity ua = new UserActivity();
                     ua.setUserId(userInfo.getId());
                     ua.setAccessMethod(request.getMethod());
                     ua.setPageAccess("/releaseBatches");
                     ua.setActivity("Released Batch");
                     ua.setBatchUploadId(batchId);
                     if (!canReleaseBatch) {
                         ua.setActivityDesc("without permission" + forInsert);
                     }
                     	usermanager.insertUserLog(ua);
                     	
                 }
               
        	redirectAttr.addFlashAttribute("noErrors", noErrors);
            redirectAttr.addFlashAttribute("batchOptionStatus", systemMessage);
            ModelAndView mav = new ModelAndView(new RedirectView(redirectPage));
            return mav;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error at release batches.", e);
        }

    }
    
    /**
     * The '/rejectMessage' POST request will submit batchId and transactionId 
     * method will flag the transaction as rejected
     * 
     * @param batchId - holds the batch
     * @transactionId - hold the transactionId
     * 
     */
    @RequestMapping(value = "/rejectMessage", method = RequestMethod.POST)
    public @ResponseBody Integer rejectMessage(
    		@RequestParam(value = "batchId", required = false) Integer batchId,
            @RequestParam(value = "transactionId", required = false) Integer transactionId,
            RedirectAttributes redirectAttr,
            HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	User userInfo = (User) session.getAttribute("userDetails");
        batchUploads batchInfo = transactionInManager.getBatchDetails(batchId);
        boolean hasConfigurations = false;
        boolean hasPermission = false;
        
        if (batchInfo != null) {
            List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);
            if (configurations.size() >= 1) {
                hasConfigurations = true;
            }

            hasPermission = transactionInManager.hasPermissionForBatch(batchInfo, userInfo, hasConfigurations);
        }
        
        if (hasPermission) {
            if (batchInfo.getstatusId() == 5 && userInfo.geteditAuthority()) {
                	transactionInManager.updateTranStatusByTInId(transactionId, 13);
            } else {
                hasPermission = false;
            }
        } 
            
          //log user activity
            UserActivity ua = new UserActivity();
            ua.setUserId(userInfo.getId());
            ua.setAccessMethod(request.getMethod());
            ua.setPageAccess("/rejectMessage");
            ua.setActivity("Rejected Message");
            ua.setBatchUploadId(batchId);
            ua.setTransactionInIds(String.valueOf(transactionId));
            if (!hasPermission) {
                ua.setActivityDesc("without permission");
            }
            	usermanager.insertUserLog(ua);
            	
      return 1;
        
    }
    
    

}
