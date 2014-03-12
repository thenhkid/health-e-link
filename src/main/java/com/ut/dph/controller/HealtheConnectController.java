/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

import com.ut.dph.dao.messageTypeDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.model.User;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationMessageSpecs;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.model.transactionIn;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
     * The '/upload' request will serve up the Health-e-Connect upload page.
     *
     * @param request
     * @param response
     * @return	the health-e-Connect upload  view
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
        mav.addObject("currentPage", 1);
        
        /* Need to get a list of uploaded files */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaluploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, "", 1, 0).size();
            List<batchUploads> uploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, "", 1, maxResults);

            if(!uploadedBatches.isEmpty()) {
                for(batchUploads batch : uploadedBatches) {
                    List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }
            
           List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);
           boolean hasConfigurations = false;
           if(configurations.size() >=1 ) {
               hasConfigurations = true;
           }
           
           mav.addObject("hasConfigurations", hasConfigurations);

           mav.addObject("uploadedBatches", uploadedBatches);
           
           Integer totalPages = (int)Math.ceil((double)totaluploadedBatches / maxResults);
           mav.addObject("totalPages", totalPages);
        }
        catch (Exception e) {
            throw new Exception("Error occurred viewing the uploaded batches. userId: "+ userInfo.getId(),e);
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
    public ModelAndView findUploads(@RequestParam(value = "page", required = false) Integer page, @RequestParam String searchTerm, @RequestParam Date fromDate, @RequestParam Date toDate,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
       
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/upload");
        
        System.out.println(fromDate);
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        
        /* Need to get a list of uploaded files */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaluploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, 1, 0).size();
            List<batchUploads> uploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, page, maxResults);

            if(!uploadedBatches.isEmpty()) {
                for(batchUploads batch : uploadedBatches) {
                    List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }
            
            List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);
            boolean hasConfigurations = false;
            if(configurations.size() >=1 ) {
               hasConfigurations = true;
            }
           
            mav.addObject("hasConfigurations", hasConfigurations);
            
            mav.addObject("uploadedBatches", uploadedBatches); 

            Integer totalPages = (int)Math.ceil((double)totaluploadedBatches / maxResults);
            mav.addObject("totalPages", totalPages);
            mav.addObject("searchTerm", searchTerm);
            mav.addObject("currentPage", page);


            return mav;
        }
        catch (Exception e) {
            throw new Exception("Error occurred searching uploaded batches.",e);
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
    public @ResponseBody ModelAndView fileUploadForm(HttpSession session) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/uploadForm");
        
        /* Need to get all available message types for this user */
        Map<Integer,String> configMessageTypes = new HashMap<Integer,String>();
        
        User userInfo = (User)session.getAttribute("userDetails");
        List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);
        
        for (configuration config : configurations) {
            configMessageTypes.put(config.getId(),messagetypemanager.getMessageTypeById(config.getMessageTypeId()).getName());
        }
        
        boolean allowmultipleMessageTypes = false;
        List<configurationTransport> transportTypes = configurationtransportmanager.getDistinctConfigTransportForOrg(userInfo.getOrgId(), 1);
        
        if(transportTypes.size() == 1) {
            allowmultipleMessageTypes = true;
        }
        
        mav.addObject("configurations", configMessageTypes);
        mav.addObject("allowmultipleMessageTypes", allowmultipleMessageTypes);
        
        return mav;
    }
    
    
    
    
    /**
     * The '/submitFileUpload' POST request will submit the new file and run the file through
     * various validations. If a single validation fails the batch will be put in a error
     * validation status and the file will be removed from the system. The user will receive an 
     * error message on the screen letting them know which validations have failed and be asked
     * to upload a new file.
     * 
     * The following validations will be taken place.
     *  - File is not empty
     *  - Proper file type (as determined in the configuration set up)
     *  - Proper delimiter (as determined in the configuration set up)
     *  - Does not exceed file size (as determined in the configuration set up)
     */
    @RequestMapping(value = "/submitFileUpload", method = RequestMethod.POST)
    public @ResponseBody ModelAndView submitFileUpload(RedirectAttributes redirectAttr, HttpSession session, @RequestParam(value = "configId", required = true) Integer configId, @RequestParam(value = "uploadedFile", required = true) MultipartFile uploadedFile) throws Exception {
        
        /* Get the organization details for the source (Sender) organization */
        User userInfo = (User)session.getAttribute("userDetails");
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
        if(configId == 0) {
            
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
        }
        else {
            /* Need to get the details of the configuration */
            configDetails = configurationManager.getConfigurationById(configId);
            transportDetails = configurationtransportmanager.getTransportDetails(configId);
            messageSpecs = configurationManager.getMessageSpecs(configId);
            
            delimChar = (String) messageTypeDAO.getDelimiterChar(transportDetails.getfileDelimiter());

        }
        
        
        try {
            /* Upload the file */
            Map<String,String> batchResults = transactionInManager.uploadBatchFile(configId, uploadedFile);

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
            
            if(multipleMessageTypes == true) {
                batchUpload.setConfigId(0);
            }
            else {
                batchUpload.setConfigId(configId);
            }
            

            /* Set the status to the batch as SFV (Source Failed Validation) */
            batchUpload.setstatusId(1);

            Integer batchId = (Integer) transactionInManager.submitBatchUpload(batchUpload); 

            List <Integer> errorCodes = new ArrayList<Integer>();

            Object emptyFileVal = batchResults.get("emptyFile");
            if(emptyFileVal != null) {
                errorCodes.add(1);
            }

            Object wrongSizeVal = batchResults.get("wrongSize");
            if(wrongSizeVal != null) {
                errorCodes.add(2);
            }

            Object wrongFileTypeVal = batchResults.get("wrongFileType");
            if(wrongFileTypeVal != null) {
                errorCodes.add(3);
            }

            Object wrongDelimVal = batchResults.get("wrongDelim");
            if(wrongDelimVal != null) {
                errorCodes.add(4);
            }
            
            /* Make sure the org doesn't have multiple configurations with different delims, headers, etc */
            if(multipleMessageTypes == true) {
                 List<configurationTransport> transportTypes = configurationtransportmanager.getDistinctConfigTransportForOrg(userInfo.getOrgId(), 1);
                 
                 if(transportTypes.size() != 1) {
                     errorCodes.add(5);
                 }
            }
            

            /* If Passed validation update the status to Source Submission Accepted */
            if(0 == errorCodes.size()) {
                /* Get the details of the batch */
                batchUploads batch = transactionInManager.getBatchDetails(batchId);
                batch.setstatusId(2);

                transactionInManager.submitBatchUploadChanges(batch);

                /* Redirect to the list of uploaded batches */
                redirectAttr.addFlashAttribute("savedStatus", "uploaded");

            }

            else {
                redirectAttr.addFlashAttribute("savedStatus", "error");
                redirectAttr.addFlashAttribute("errorCodes", errorCodes);
            }


            ModelAndView mav = new ModelAndView(new RedirectView("upload"));
            return mav;

        }
        catch (Exception e) {
            throw new Exception("Error occurred uploading a new file. configId: "+ configId,e);
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
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaldownloadableBatches = transactionOutManager.getdownloadableBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, "", 1, 0).size();
            List<batchDownloads> downloadableBatches = transactionOutManager.getdownloadableBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, "", 1, maxResults);

            if(!downloadableBatches.isEmpty()) {
                for(batchDownloads batch : downloadableBatches) {

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }

            mav.addObject("downloadableBatches", downloadableBatches);

            Integer totalPages = (int)Math.ceil((double)totaldownloadableBatches / maxResults);
            mav.addObject("totalPages", totalPages);

            return mav;
        }
        catch (Exception e) {
            throw new Exception("Error occurred trying to view the download screen. userId: "+ userInfo.getId(),e);
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
    public ModelAndView findDownloads(@RequestParam(value = "page", required = false) Integer page, @RequestParam String searchTerm, @RequestParam Date fromDate, @RequestParam Date toDate,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
       
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/download");
        
        System.out.println(fromDate);
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        
        /* Need to get a list of uploaded files */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaldownloadableBatches = transactionOutManager.getdownloadableBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, 1, 0).size();
            List<batchDownloads> downloadableBatches = transactionOutManager.getdownloadableBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, page, maxResults);

            if(!downloadableBatches.isEmpty()) {
                for(batchDownloads batch : downloadableBatches) {

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }
            mav.addObject("downloadableBatches", downloadableBatches); 

            Integer totalPages = (int)Math.ceil((double)totaldownloadableBatches / maxResults);
            mav.addObject("totalPages", totalPages);
            mav.addObject("searchTerm", searchTerm);
            mav.addObject("currentPage", page);


            return mav;
        }
        catch (Exception e) {
            throw new Exception("Error occurred searching downloadable batches.",e);
        }
        
    }
    
    /**
     * The 'downloadBatch.do' function will update the status of the batch and all the transactions
     * associated to the batch when the download link is clicked.
     * 
     * @param batchId  The id of the clicked batch to download
     * 
     * @return This function will simply return a 1.
     */
    @RequestMapping(value= "/downloadBatch.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer downloadBatch(@RequestParam(value = "batchId", required = false) Integer batchId) throws Exception {
        
        try {
            transactionOutManager.updateTargetBatchStatus(batchId, 22 , "");
        
            transactionOutManager.updateTargetTransasctionStatus(batchId, 20);
            
            /* Need to find the batch Upload Id */
            List<transactionTarget> targets = transactionOutManager.getTransactionsByBatchDLId(batchId);

            if(!targets.isEmpty()) {
                for(transactionTarget target : targets) {
                    transactionInManager.updateBatchStatus(target.getbatchUploadId(), 22, "");
                    
                    transactionInManager.updateTransactionStatus(target.getbatchUploadId(), 0, 0, 20);
                    
                }
            }

            /* Update the last Downloaded field */
            transactionOutManager.updateLastDownloaded(batchId);

            return 1;
        }
        catch (Exception e) {
            throw new Exception("Error occurred downloading the file. batchId: "+batchId,e);
        }
        
    }
    
    
    
    /**
     * The '/auditReports' request will serve up the Health-e-Connect audit reports page.
     *
     * @param request
     * @param response
     * @return	the health-e-Connect report  view
     * @throws Exception
     */
    @RequestMapping(value = "/auditReports", method = RequestMethod.GET)
    public ModelAndView viewAuditRpts(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/auditReports");
        
        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("currentPage", 1);
        
        /* Need to get a list of uploaded files with status of 5 (PR), 6 (REL), 29 (Sys Error)*/
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaluploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, "", 1, 0).size();
            List<batchUploads> uploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, "", 1, maxResults);

            if(!uploadedBatches.isEmpty()) {
                for(batchUploads batch : uploadedBatches) {
                    List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }
            
           List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);
           boolean hasConfigurations = false;
           if(configurations.size() >=1 ) {
               hasConfigurations = true;
           }
           
           mav.addObject("hasConfigurations", hasConfigurations);

           mav.addObject("uploadedBatches", uploadedBatches);
           
           Integer totalPages = (int)Math.ceil((double)totaluploadedBatches / maxResults);
           mav.addObject("totalPages", totalPages);
        }
        catch (Exception e) {
            throw new Exception("Error occurred viewing the audit reports. userId: "+ userInfo.getId(),e);
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
    public ModelAndView findAuditRpts(@RequestParam(value = "page", required = false) Integer page, @RequestParam String searchTerm, @RequestParam Date fromDate, @RequestParam Date toDate,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
       
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Connect/auditReports");
        
        System.out.println(fromDate);
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        
        /* Need to get a list of uploaded files */
        User userInfo = (User)session.getAttribute("userDetails");
        
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaluploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, 1, 0).size();
            List<batchUploads> uploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId(), fromDate, toDate, searchTerm, page, maxResults);

            if(!uploadedBatches.isEmpty()) {
                for(batchUploads batch : uploadedBatches) {
                    List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);

                }
            }
            
            List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 1);
            boolean hasConfigurations = false;
            if(configurations.size() >=1 ) {
               hasConfigurations = true;
            }
           
            mav.addObject("hasConfigurations", hasConfigurations);
            
            mav.addObject("uploadedBatches", uploadedBatches); 

            Integer totalPages = (int)Math.ceil((double)totaluploadedBatches / maxResults);
            mav.addObject("totalPages", totalPages);
            mav.addObject("searchTerm", searchTerm);
            mav.addObject("currentPage", page);


            return mav;
        }
        catch (Exception e) {
            throw new Exception("Error occurred searching audit report.",e);
        }
        
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
}
