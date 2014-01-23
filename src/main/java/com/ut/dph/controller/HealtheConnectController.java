/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

import com.ut.dph.model.Organization;
import com.ut.dph.model.User;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.model.transactionIn;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.service.organizationManager;
import com.ut.dph.service.sysAdminManager;
import com.ut.dph.service.transactionInManager;
import com.ut.dph.service.userManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
        
        /* Need to get a list of uploaded files */
        User userInfo = (User)session.getAttribute("userDetails");
        
        /* Need to get a list of all uploaded batches */
        List<batchUploads> uploadedBatches = transactionInManager.getuploadedBatches(userInfo.getId(), userInfo.getOrgId());
        
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
        
        mav.addObject("uploadedBatches", uploadedBatches);
        
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
        
        mav.addObject("configurations", configMessageTypes);
        
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
    public @ResponseBody ModelAndView submitFileUpload(RedirectAttributes redirectAttr, HttpSession session, @RequestParam(value = "configIds", required = true) List<Integer> configIds, @RequestParam(value = "uploadedFile", required = true) MultipartFile uploadedFile) throws Exception {
        
        /* Get the organization details for the source (Sender) organization */
        User userInfo = (User)session.getAttribute("userDetails");
        Organization sendingOrgDetails = organizationmanager.getOrganizationById(userInfo.getOrgId());
        
        /* Need to get the first configId */
        Integer configId = configIds.get(0);
        
        /* Need to get the message type for the first config */
        configuration configDetails = configurationManager.getConfigurationById(configId);
        
        /* Upload the attachment */
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
    
}
