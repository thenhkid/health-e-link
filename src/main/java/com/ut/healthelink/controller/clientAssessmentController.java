/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.controller;

import com.ut.healthelink.model.Organization;
import com.ut.healthelink.model.Transaction;
import com.ut.healthelink.model.User;
import com.ut.healthelink.model.UserActivity;
import com.ut.healthelink.model.batchMultipleTargets;
import com.ut.healthelink.model.batchUploadSummary;
import com.ut.healthelink.model.batchUploads;
import com.ut.healthelink.model.clients;
import com.ut.healthelink.model.configuration;
import com.ut.healthelink.model.configurationFormFields;
import com.ut.healthelink.model.configurationTransport;
import com.ut.healthelink.model.transactionIn;
import com.ut.healthelink.model.transactionInRecords;
import com.ut.healthelink.model.transactionRecords;
import com.ut.healthelink.model.transactionTarget;
import com.ut.healthelink.reference.USStateList;
import com.ut.healthelink.service.clientManager;
import com.ut.healthelink.service.configurationManager;
import com.ut.healthelink.service.configurationTransportManager;
import com.ut.healthelink.service.messageTypeManager;
import com.ut.healthelink.service.organizationManager;
import com.ut.healthelink.service.transactionInManager;
import com.ut.healthelink.service.userManager;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author chadmccue
 */
@Controller
@RequestMapping("/clients/assessments")
public class clientAssessmentController {
    
    @Autowired
    private userManager usermanager;
    
    @Autowired
    private clientManager clientmanager;
    
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
    
    /**
     * The '/assessmentForm' GET request will get the assessmentForm details and present the user
     * with the selected assessment form.
     *
     * @param request
     * @return	the client assessment page
     * @throws Exception
     */
    @RequestMapping(value = "/assessmentForm", method = RequestMethod.GET)
    public ModelAndView searchClients(HttpServletRequest request, HttpSession session,
         @RequestParam(value = "clientId", required = true) Integer clientId,
         @RequestParam(value = "assessmentId", required = true) Integer assessmentId) throws Exception {
        
        /** THE CLIENTID AND ASSESSMENTID SHOULD BE ENCRYPTED IN THE URL **/
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/clients/assessments/assessmentForm");
        
         /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        clients clientDetails = clientmanager.getClientById(clientId);
        
        mav.addObject("clientDetails", clientDetails);
        
         //Get a list of states
        USStateList stateList = new USStateList();
        
        //Get the object that will hold the states
        mav.addObject("stateList", stateList.getStates());
        
        return mav;
    }
    
    /**
     * The '/assessmentForm' POST request will submit the assessment form and calculate the conclusion
     * and display the conclusion page.
     *
     * @param request
     * @return	the assessment conclusion page
     * @throws Exception
     */
    @RequestMapping(value = "/assessmentForm", method = RequestMethod.POST)
    public ModelAndView searchClients(HttpServletRequest request, HttpSession session,
         @RequestParam(value = "clientId", required = true) Integer clientId) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/clients/assessments/assessmentConclusion");
        
         /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        mav.addObject("clientId", clientId);
        
        return mav;
    }
    
    
    /**
     * The 'enrollClientIntoPrograms' POST request will enroll the passed in patient for the selected
     * programs. These referrals will be created and set to a saved state.
     * 
     * @param request
     * @param session
     * @param selectedPrograms
     * @param clientId
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/enrollClientIntoPrograms", method = RequestMethod.POST)
    public ModelAndView enrollClientIntoPrograms(HttpServletRequest request, HttpSession session,
         @RequestParam(value = "selectedPrograms", required = true) List<String> selectedPrograms, 
         @RequestParam(value = "clientId", required = true) Integer clientId, RedirectAttributes redirectAttr) throws Exception {
        
        if(selectedPrograms != null && !"".equals(selectedPrograms) && clientId > 0) {
            
            /* Get the organization details for the source (Sender) organization */
            User userInfo = (User) session.getAttribute("userDetails");

            for(String program : selectedPrograms) {
                int srcConfigId = Integer.parseInt(program.split("-")[0]);
                int tgtConfigId = Integer.parseInt(program.split("-")[1]);
                int tgtOrgId = Integer.parseInt(program.split("-")[2]);
                
                /* Get the configuration details */
                configuration configDetails = configurationManager.getConfigurationById(srcConfigId);

                Organization sendingOrgDetails = organizationmanager.getOrganizationById(userInfo.getOrgId());

                /* Get a list of form fields */
                configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(srcConfigId, 2);

                Transaction transaction = new Transaction();
                transactionInRecords records = null;
                
                /* Create a new transaction */
                if (organizationmanager.getOrganizationById(userInfo.getOrgId()).getParentId() == 0) {
                    transaction.setorgId(userInfo.getOrgId());
                    transaction.setSourceSubOrgId(0);
                } else {
                    transaction.setorgId(organizationmanager.getOrganizationById(userInfo.getOrgId()).getParentId());
                    transaction.setSourceSubOrgId(userInfo.getOrgId());
                }
                
                if(organizationmanager.getOrganizationById(tgtOrgId).getParentId() > 0) {
                    transaction.settargetOrgId(organizationmanager.getOrganizationById(tgtOrgId).getParentId());
                    transaction.setTargetSubOrgId(tgtOrgId);
                }
                else {
                    transaction.settargetOrgId(tgtOrgId);
                }

                transaction.settransportMethodId(2);
                transaction.setmessageTypeId(configDetails.getMessageTypeId());
                transaction.setuserId(userInfo.getId());
                transaction.setbatchName(null);
                transaction.setoriginalFileName(null);
                transaction.setstatusId(0);
                transaction.settransactionStatusId(0);
                transaction.setconfigId(srcConfigId);
                transaction.setautoRelease(transportDetails.getautoRelease());
                
                transaction.setAttachmentNote(transportDetails.getAttachmentNote());
                transaction.setAttachmentRequired(transportDetails.getAttachmentRequired());

                if (transportDetails.getAttachmentLimit() != null && !"".equals(transportDetails.getAttachmentLimit())) {
                    transaction.setAttachmentLimit(Integer.parseInt(transportDetails.getAttachmentLimit()));
                }

                List<Integer> targetConfigId = new ArrayList<Integer>();
                targetConfigId.add(tgtConfigId);
                transaction.settargetConfigId(targetConfigId);

                transaction.setsourceType(configDetails.getsourceType());

                /* Get the organization details for the target (Receiving) organization */
                Organization receivingOrgDetails = organizationmanager.getOrganizationById(tgtOrgId);

                boolean readOnly = false;

                try {
                    List<configurationFormFields> senderInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(srcConfigId, transportDetails.getId(), 1);

                    /* Set all the transaction SOURCE ORG fields */
                    List<transactionRecords> fromFields = transactionInManager.setOutboundFormFields(senderInfoFormFields, records, srcConfigId, readOnly, sendingOrgDetails.getId(), 0);
                    transaction.setsourceOrgFields(fromFields);

                } catch (Exception e) {
                    throw new Exception("Error retrieving sender fields for configuration id: " + srcConfigId, e);
                }

                try {
                    List<configurationFormFields> senderProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(srcConfigId, transportDetails.getId(), 2);

                    /* Set all the transaction SOURCE PROVIDER fields */
                    List<transactionRecords> fromProviderFields = transactionInManager.setOutboundFormFields(senderProviderFormFields, records, srcConfigId, readOnly, sendingOrgDetails.getId(), 0);
                    transaction.setsourceProviderFields(fromProviderFields);

                } catch (Exception e) {
                    throw new Exception("Error retrieving sender provider fields for configuration id: " + srcConfigId, e);
                }

                try {
                    List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(srcConfigId, transportDetails.getId(), 3);

                    /* Set all the transaction TARGET fields */
                    List<transactionRecords> toFields = transactionInManager.setOutboundFormFields(targetInfoFormFields, records, srcConfigId, readOnly, receivingOrgDetails.getId(), 0);
                    transaction.settargetOrgFields(toFields);

                } catch (Exception e) {
                    throw new Exception("Error retrieving target fields for configuration id: " + srcConfigId, e);
                }

                try {
                    List<configurationFormFields> targetProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(srcConfigId, transportDetails.getId(), 4);

                    /* Set all the transaction TARGET PROVIDER fields */
                    List<transactionRecords> toProviderFields = transactionInManager.setOutboundFormFields(targetProviderFormFields, records, srcConfigId, readOnly, receivingOrgDetails.getId(), 0);
                    transaction.settargetProviderFields(toProviderFields);

                } catch (Exception e) {
                    throw new Exception("Error retrieving target provider fields for configuration id: " + srcConfigId, e);
                }

                try {
                    List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(srcConfigId, transportDetails.getId(), 5);

                    List<transactionRecords> patientFields = transactionInManager.setOutboundFormFields(patientInfoFormFields, records, srcConfigId, true, 0, clientId);
                    transaction.setpatientFields(patientFields);

                } catch (Exception e) {
                    throw new Exception("Error retrieving patient fields for configuration id: " + srcConfigId, e);
                }

                try {
                    List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(srcConfigId, transportDetails.getId(), 6);

                    /* Set all the transaction DETAIL fields */
                    readOnly = false;
                    List<transactionRecords> detailFields = transactionInManager.setOutboundFormFields(detailFormFields, records, srcConfigId, readOnly, 0, 0);
                    transaction.setdetailFields(detailFields);
                } catch (Exception e) {
                    throw new Exception("Error retrieving patient fields for configuration id: " + srcConfigId, e);
                }
                
                saveReferral(transaction, userInfo);
                
            }
            
             /*
             Sent the user to the "Pending" items page
             */
            redirectAttr.addFlashAttribute("savedStatus", "saved");
            ModelAndView mav = new ModelAndView(new RedirectView("/Health-e-Web/pending"));
            return mav;
        }
        
        else {
             /*
             Sent the user to the "Pending" items page
             */
            ModelAndView mav = new ModelAndView(new RedirectView("/clients/search"));
            return mav;
        }
        
    }
    
    /**
     * 
     * @param transactionDetails
     * @param userInfo
     * @throws Exception 
     */
    private void saveReferral(Transaction transactionDetails, User userInfo) throws Exception {
       
        Integer currBatchId = transactionDetails.getbatchId();
        Integer currTransactionId = transactionDetails.gettransactionId();
        Integer currRecordId = transactionDetails.gettransactionRecordId();
        Integer currTransactionTargetId = transactionDetails.gettransactionTargetId();
        Integer batchId;
        Integer transactionId;
        Integer transactionRecordId;
        Integer transactionTargetId;
        
         /* Create the batch name (TransportMethodId+OrgId+MessageTypeId+Date/Time/Seconds) */
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
        Date date = new Date();
        String batchName = new StringBuilder().append("2").append(transactionDetails.getorgId()).append(transactionDetails.getmessageTypeId()).append(dateFormat.format(date)).toString();

        /* Submit a new batch */
        try {
            batchUploads batchUpload = new batchUploads();
            batchUpload.setOrgId(transactionDetails.getorgId());
            batchUpload.setuserId(userInfo.getId());
            batchUpload.setutBatchName(batchName);
            batchUpload.settransportMethodId(2);
            batchUpload.setoriginalFileName(batchName);
            batchUpload.setstatusId(8);

            batchUpload.settotalRecordCount(1);
            batchId = (Integer) transactionInManager.submitBatchUpload(batchUpload);
            
        } catch (Exception e) {
            throw new Exception("Error occurred in creating new batch", e);
        }
        
        try {
            /* Submit a new Transaction In record */
            transactionIn transactionIn = new transactionIn();
            transactionIn.setbatchId(batchId);
            transactionIn.setconfigId(transactionDetails.getconfigId());
            transactionIn.settransactionTargetId(transactionDetails.getorginialTransactionId());
            transactionIn.setSourceSubOrgId(transactionDetails.getSourceSubOrgId());
            transactionIn.setstatusId(15);
           
            transactionId = (Integer) transactionInManager.submitTransactionIn(transactionIn);

            /* Need to populate the batchUploadSummary table */
            batchUploadSummary summary = new batchUploadSummary();
            summary.setbatchId(batchId);
            summary.settransactionInId(transactionId);
            summary.setsourceOrgId(transactionDetails.getorgId());
            summary.settargetOrgId(transactionDetails.gettargetOrgId());
            summary.setmessageTypeId(transactionDetails.getmessageTypeId());
            summary.setsourceConfigId(transactionDetails.getconfigId());
            summary.setTargetSubOrgId(transactionDetails.getTargetSubOrgId());
            summary.setSourceSubOrgId(transactionDetails.getSourceSubOrgId());

            transactionInManager.submitBatchUploadSummary(summary);
        } catch (Exception e) {
            throw new Exception("Error occurred in creating the new transaction for batch " + batchId, e);
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
            if (currRecordId == 0) {
                records = new transactionInRecords();
                records.setTransactionInId(transactionId);
            } else {
                records = transactionInManager.getTransactionRecord(currRecordId);
            }

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

            if (currRecordId == 0) {
                transactionRecordId = transactionInManager.submitTransactionInRecords(records);
            } else {
                transactionRecordId = currRecordId;
                records.setId(transactionRecordId);
                transactionInManager.submitTransactionInRecordsUpdates(records);
            }
            
        } catch (Exception e) {
            throw new Exception("Error occurred in saving transaction field values", e);
        }
        
        try {
            transactionInManager.submitTransactionTranslatedInRecords(transactionId, transactionRecordId, transactionDetails.getconfigId());
            /* Submit a new Transaction Target record */
            transactionTarget transactiontarget = new transactionTarget();
            transactiontarget.setbatchUploadId(batchId);
            transactiontarget.settransactionInId(transactionId);
            transactiontarget.setTargetSubOrgId(transactionDetails.getTargetSubOrgId());
            transactiontarget.setSourceSubOrgId(transactionDetails.getSourceSubOrgId());

            if (transactionDetails.gettargetConfigId().size() > 1) {
                transactiontarget.setconfigId(transactionDetails.gettargetConfigId().get(0));

                /* If the message is going to multiple targets only save one and store the 
                 other targets in the batchMultipleTargets table. The ones that are stored 
                 here will be sent out when the message is actually sent. A batch will be 
                 created for each of them. */
                for (int i = 1; i < transactionDetails.gettargetConfigId().size(); i++) {
                    batchMultipleTargets target = new batchMultipleTargets();
                    target.setBatchId(batchId);
                    target.setTgtConfigId(transactionDetails.gettargetConfigId().get(i));

                    transactionInManager.submitTransactionMultipleTargets(target);
                }

            } else {
                transactiontarget.setconfigId(transactionDetails.gettargetConfigId().get(0));
            }
            transactiontarget.setstatusId(15);
            
            transactionInManager.submitTransactionTarget(transactiontarget);
         } catch (Exception e) {
            throw new Exception("Error occurred in submitting the translated records id:" + transactionId, e);
        }
        
    }
}
