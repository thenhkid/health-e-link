/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

import com.ut.dph.model.Organization;
import com.ut.dph.model.Transaction;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.custom.TableData;
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
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    private sysAdminManager sysAdminManager;
    
    
    /**
     * The '/inbox' request will serve up the Health-e-Web (ERG) inbox.
     *
     * @param request
     * @param response
     * @return	the health-e-web inbox  view
     * @throws Exception
     */
    @RequestMapping(value = "/inbox", method = RequestMethod.GET)
    public ModelAndView viewinbox(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/inbox");
        
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
        int[] userInfo = (int[])session.getAttribute("userInfo");
        List<configuration> configurations = configurationManager.getActiveERGConfigurationsByUserId(userInfo[0]);
        
        
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
    public ModelAndView showMessageDetailsForm(@PathVariable String pathVariable, @RequestParam(value = "configId", required = true) int configId, @RequestParam(value = "targetOrg", required = false) Integer targetOrg, @RequestParam(value = "targetConfig", required = false) Integer targetConfig, @RequestParam(value = "transactionId", required = false) Integer transactionId, HttpSession session) throws NoSuchMethodException {
        
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
        
        /* Get the configuration details */
        configuration configDetails = configurationManager.getConfigurationById(configId);
        
        /* Get the organization details for the source (Sender) organization */
        int[] userInfo = (int[])session.getAttribute("userInfo");
        Organization sendingOrgDetails = organizationmanager.getOrganizationById(userInfo[1]);
        
        /* Get the organization details for the target (Receiving) organization */
        Organization receivingOrgDetails = organizationmanager.getOrganizationById(targetOrg);
        
        /* Get a list of form fields */
        configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(configId, 2);
        List<configurationFormFields> senderInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),1);
        List<configurationFormFields> senderProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),2);
        List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),3);
        List<configurationFormFields> targetProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),4);
        List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),5);
        List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),6);
        
        Transaction transaction = new Transaction();
        transactionInRecords records = null;
        
        if(transactionId > 0) {
            transactionIn transactionInfo = transactionInManager.getTransactionDetails(transactionId);
            batchUploads batchInfo = transactionInManager.getUploadBatch(transactionInfo.getbatchId());
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
            transaction.settransactionId(transactionId);
            transaction.settransactionTargetId(transactionTarget.getId());
            
            records = transactionInManager.getTransactionRecords(transactionId);
            transaction.settransactionRecordId(records.getId());
        }
        else {
            /* Create a new transaction */
            transaction.setorgId(userInfo[1]);
            transaction.settransportMethodId(2);
            transaction.setmessageTypeId(configDetails.getMessageTypeId());
            transaction.setuserId(userInfo[0]);
            transaction.setbatchName(null);
            transaction.setoriginalFileName(null);
            transaction.setstatusId(0);
            transaction.settransactionStatusId(0);
            transaction.settargetOrgId(targetOrg);
            transaction.setconfigId(configId);
            transaction.setautoRelease(transportDetails.getautoRelease());
            transaction.settargetConfigId(targetConfig);
        }
        
       /* Set all the transaction SOURCE ORG fields */
       List<transactionRecords> fromFields = new ArrayList<transactionRecords>();
       String tableName;
       String tableCol;
       String colName;
       
       for(configurationFormFields fields : senderInfoFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setrequired(fields.getRequired());
            field.setsaveToTable(fields.getsaveToTableName());
            field.setsaveToTableCol(fields.getsaveToTableCol());
            field.setfieldLabel(fields.getFieldLabel());
            
            /* Get the validation */
            if(fields.getValidationType() > 1) {
                field.setvalidation(messagetypemanager.getValidationById(fields.getValidationType()).toString());
            }
            
            /* If editing an existing transaction pull the already entered values */
            if(transactionId > 0) {
                colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                try {
                    field.setfieldValue(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
            
                /* Get the pre-populated values */
                tableName = fields.getautoPopulateTableName();
                tableCol = fields.getautoPopulateTableCol();

                if(!tableName.isEmpty() && !tableName.isEmpty()) {
                    field.setfieldValue(transactionInManager.getFieldValue(tableName, tableCol, sendingOrgDetails.getId()));
                }
            }
            
            fromFields.add(field);
        }
        transaction.setsourceOrgFields(fromFields);
        
        /* Set all the transaction SOURCE PROVIDER fields */
        List<transactionRecords> fromProviderFields = new ArrayList<transactionRecords>();
        
        for(configurationFormFields fields : senderProviderFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setrequired(fields.getRequired());
            field.setsaveToTable(fields.getsaveToTableName());
            field.setsaveToTableCol(fields.getsaveToTableCol());
            field.setfieldLabel(fields.getFieldLabel());
            
            /* Get the validation */
            if(fields.getValidationType() > 1) {
                field.setvalidation(messagetypemanager.getValidationById(fields.getValidationType()).toString());
            }
            
            /* If editing an existing transaction pull the already entered values */
            if(transactionId > 0) {
                colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                try {
                    field.setfieldValue(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                /* Get the pre-populated values */
                tableName = fields.getautoPopulateTableName();
                tableCol = fields.getautoPopulateTableCol();

                if(!tableName.isEmpty() && !tableName.isEmpty()) {
                    field.setfieldValue(transactionInManager.getFieldValue(tableName, tableCol, sendingOrgDetails.getId()));
                }
            }
            
            fromProviderFields.add(field);
        }
       transaction.setsourceProviderFields(fromProviderFields);
        
        
        /* Set all the transaction TARGET fields */
        List<transactionRecords> toFields = new ArrayList<transactionRecords>();
        for(configurationFormFields fields : targetInfoFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setrequired(fields.getRequired());
            field.setsaveToTable(fields.getsaveToTableName());
            field.setsaveToTableCol(fields.getsaveToTableCol());
            field.setfieldLabel(fields.getFieldLabel());
            
            /* Get the validation */
            if(fields.getValidationType() > 1) {
                field.setvalidation(messagetypemanager.getValidationById(fields.getValidationType()).toString());
            }
            
            /* If editing an existing transaction pull the already entered values */
            if(transactionId > 0) {
                colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                try {
                    field.setfieldValue(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                /* Get the pre-populated values */
                tableName = fields.getautoPopulateTableName();
                tableCol = fields.getautoPopulateTableCol();

                if(!tableName.isEmpty() && !tableName.isEmpty()) {
                    field.setfieldValue(transactionInManager.getFieldValue(tableName, tableCol, receivingOrgDetails.getId()));
                }
            }
            
            /* Get the pre-populated values */
            toFields.add(field);
        }
        transaction.settargetOrgFields(toFields);
        
        /* Set all the transaction TARGET PROVIDER fields */
        List<transactionRecords> toProviderFields = new ArrayList<transactionRecords>();
        for(configurationFormFields fields : targetProviderFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setrequired(fields.getRequired());
            field.setsaveToTable(fields.getsaveToTableName());
            field.setsaveToTableCol(fields.getsaveToTableCol());
            field.setfieldLabel(fields.getFieldLabel());
            
            /* Get the validation */
            if(fields.getValidationType() > 1) {
                field.setvalidation(messagetypemanager.getValidationById(fields.getValidationType()).toString());
            }
            
            /* If editing an existing transaction pull the already entered values */
            if(transactionId > 0) {
                colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                try {
                    field.setfieldValue(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                /* Get the pre-populated values */
                tableName = fields.getautoPopulateTableName();
                tableCol = fields.getautoPopulateTableCol();

                if(!tableName.isEmpty() && !tableName.isEmpty()) {
                    field.setfieldValue(transactionInManager.getFieldValue(tableName, tableCol, receivingOrgDetails.getId()));
                }
            }
            
            toProviderFields.add(field);
        }
        transaction.settargetProviderFields(toProviderFields);
        
        /* Set all the transaction PATIENT fields */
        List<transactionRecords> patientFields = new ArrayList<transactionRecords>();
        for(configurationFormFields fields : patientInfoFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setrequired(fields.getRequired());
            field.setsaveToTable(fields.getsaveToTableName());
            field.setsaveToTableCol(fields.getsaveToTableCol());
            field.setfieldLabel(fields.getFieldLabel());
            field.setfieldValue(null);
            
            /* Get the validation */
            if(fields.getValidationType() > 1) {
                field.setvalidation(messagetypemanager.getValidationById(fields.getValidationType()).toString());
            }
            
            /* If editing an existing transaction pull the already entered values */
            if(transactionId > 0) {
                colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                try {
                    field.setfieldValue(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            /* See if any fields have crosswalks associated to it */
            List<fieldSelectOptions> fieldSelectOptions = transactionInManager.getFieldSelectOptions(fields.getId(),configId);
            field.setfieldSelectOptions(fieldSelectOptions);
            
            patientFields.add(field);
        }
        transaction.setpatientFields(patientFields);
        
        /* Set all the transaction DETAIL fields */
        List<transactionRecords> detailFields = new ArrayList<transactionRecords>();
        for(configurationFormFields fields : detailFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setrequired(fields.getRequired());
            field.setsaveToTable(fields.getsaveToTableName());
            field.setsaveToTableCol(fields.getsaveToTableCol());
            field.setfieldLabel(fields.getFieldLabel());
            field.setfieldValue(null);
            
            /* Get the validation */
            if(fields.getValidationType() > 1) {
                field.setvalidation(messagetypemanager.getValidationById(fields.getValidationType()).toString());
            }
            
            /* If editing an existing transaction pull the already entered values */
            if(transactionId > 0) {
                colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                try {
                    field.setfieldValue(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            /* See if any fields have crosswalks associated to it */
            List<fieldSelectOptions> fieldSelectOptions = transactionInManager.getFieldSelectOptions(fields.getId(),configId);
            field.setfieldSelectOptions(fieldSelectOptions);
            
            detailFields.add(field);
        }
        transaction.setdetailFields(detailFields);
        
        mav.addObject(transaction);
        
        
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
    public ModelAndView submitMessage(@ModelAttribute(value = "transactionDetails") Transaction transactionDetails, HttpSession session, @RequestParam String action, RedirectAttributes redirectAttr) {
        
        int[] userInfo = (int[])session.getAttribute("userInfo");
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
        
            /* Create the batch name (OrgId+MessageTypeId+Date/Time) */
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            String batchName = new StringBuilder().append(transactionDetails.getorgId()).append(transactionDetails.getmessageTypeId()).append(dateFormat.format(date)).toString();

            /* Submit a new batch */
            batchUploads batchUpload = new batchUploads();
            batchUpload.setOrgId(transactionDetails.getorgId());
            batchUpload.setuserId(userInfo[0]);
            batchUpload.setutBatchName(batchName);
            batchUpload.settransportMethodId(2);
            batchUpload.setoriginalFileName(batchName);
        
            /* If the "Send" button was pressed */
            if (action.equals("send")) {
                batchUpload.setstatusId(2);
            }
            /* If the "Save" or "Release" button was pressed */
            else {
                batchUpload.setstatusId(6);
            }
            batchUpload.settotalRecordCount(1);

            batchId = (Integer) transactionInManager.submitBatchUpload(batchUpload); 
        }
        
        /* Otherwise update existing batch */
        else {
            batchId = currBatchId;
            
            /* Get the details of the batch */
            batchUploads batchUpload = transactionInManager.getUploadBatch(batchId);
            
            /* If the "Send" button was pressed */
            if (action.equals("send")) {
                batchUpload.setstatusId(2);
            }
            /* If the "Save" or "Release" button was pressed */
            else {
                batchUpload.setstatusId(6);
            }
            
            transactionInManager.submitBatchUploadChanges(batchUpload);
        }
        
        /* If currTransactionId == 0 then create a new transaction */
        if(currTransactionId == 0) {
        
            /* Submit a new Transaction In record */
            transactionIn transactionIn = new transactionIn();
            transactionIn.setbatchId(batchId);
            transactionIn.setconfigId(transactionDetails.getconfigId());

            /* If the "Send" button was pressed */
            if (action.equals("send")) {
                transactionIn.setstatusId(17);
            }
            /* If the "Save" or "Release" button was pressed */
            else {
                transactionIn.setstatusId(9);
            }

            transactionId = (Integer) transactionInManager.submitTransactionIn(transactionIn);
        }
        
        /* Otherwise update existing batch */
        else {
            transactionId =  currTransactionId;  
            
            transactionIn transactionIn = transactionInManager.getTransactionDetails(transactionId);
            
            /* If the "Send" button was pressed */
            if (action.equals("send")) {
                transactionIn.setstatusId(17);
            }
            /* If the "Save" or "Release" button was pressed */
            else {
                transactionIn.setstatusId(9);
            }
            
            transactionInManager.submitTransactionInChanges(transactionIn);
        }
        
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
        
        transactionInManager.submitTransactionTranslatedInRecords(transactionId, transactionRecordId);
        
        /* Need to populate the transaction Target */
        if(currTransactionTargetId == 0) {
        
            /* Submit a new Transaction Target record */
            transactionTarget transactiontarget = new transactionTarget();
            transactiontarget.setbatchUploadId(batchId);
            transactiontarget.settransactionInId(transactionId);
            transactiontarget.setconfigId(transactionDetails.gettargetConfigId());
            
            /* If the "Send" button was pressed */
            if (action.equals("send")) {
                transactiontarget.setstatusId(16);
            }
            /* If the "Save" or "Release" button was pressed */
            else {
                transactiontarget.setstatusId(9);
            }

            transactionInManager.submitTransactionTarget(transactiontarget);
        }
        
        /* Otherwise update existing batch */
        else {
            transactionTargetId =  currTransactionTargetId;  
            
            transactionTarget transactionTarget = transactionInManager.getTransactionTargetDetails(transactionTargetId);
            
            /* If the "Send" button was pressed */
            if (action.equals("send")) {
                transactionTarget.setstatusId(16);
            }
            /* If the "Save" or "Release" button was pressed */
            else {
                transactionTarget.setstatusId(9);
            }
            
            transactionInManager.submitTransactionTargetChanges(transactionTarget);
        }
        
        
        if (action.equals("send")) {
            /*
                Send the user to the "Sent" items page
            */
            redirectAttr.addFlashAttribute("savedStatus", "sent");
            ModelAndView mav = new ModelAndView(new RedirectView("/Health-e-Web/sent"));
            return mav;
        }
        else {
            /*
                Sent the user to the "Pending" items page
            */
            redirectAttr.addFlashAttribute("savedStatus", "saved");
            ModelAndView mav = new ModelAndView(new RedirectView("/Health-e-Web/pending"));
            return mav;
        }
    
    }
    
    /**
     * The '/pending' request will serve up the Health-e-Web (ERG) page that will list all pending
     * messages.
     *
     * @param request
     * @param response
     * @return	the health-e-web pending message list view
     * @throws Exception
     */
    @RequestMapping(value = "/pending", method = RequestMethod.GET)
    public ModelAndView pendingMessages(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/pending");
        
        /* Need to get all the message types set up for the user */
        int[] userInfo = (int[])session.getAttribute("userInfo");
        
        /* Need to get a list of all pending transactions */
        List<transactionIn> pendingTransactions = transactionInManager.getpendingTransactions(userInfo[1]);
        
        List<Transaction> transactionList = new ArrayList<Transaction>();
        
        configuration configDetails;
        String colName;
        transactionInRecords records = null;
        
        if(pendingTransactions != null) {
           
            for(transactionIn transactionRecord : pendingTransactions) {
                batchUploads batchInfo = transactionInManager.getUploadBatch(transactionRecord.getbatchId());
                
                Transaction transactionDetails = new Transaction();
                transactionDetails.setbatchName(batchInfo.getutBatchName());
                transactionDetails.setconfigId(transactionRecord.getconfigId());
                transactionDetails.setdateSubmitted(batchInfo.getdateSubmitted());
                transactionDetails.settransactionRecordId(transactionRecord.getId());
                transactionDetails.setstatusId(transactionRecord.getstatusId());
                
                lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(transactionRecord.getstatusId());
                transactionDetails.setstatusValue(processStatus.getDisplayCode());
                
                records = transactionInManager.getTransactionRecords(transactionRecord.getId());
                
                /* Get a list of form fields */
                configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transactionRecord.getconfigId(), 2);
                List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionRecord.getconfigId(),transportDetails.getId(),3);
                List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionRecord.getconfigId(),transportDetails.getId(),5);
                List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionRecord.getconfigId(),transportDetails.getId(),6);
                
                /* Set all the transaction TARGET fields */
                List<transactionRecords> toFields = new ArrayList<transactionRecords>();
                for(configurationFormFields fields : targetInfoFormFields) {
                    transactionRecords field = new transactionRecords();
                    
                    colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                    try {
                        field.setfieldValue(BeanUtils.getProperty(records, colName));
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    /* Get the pre-populated values */
                    toFields.add(field);
                }
                transactionDetails.settargetOrgFields(toFields);
                
                /* Set all the transaction PATIENT fields */
                List<transactionRecords> patientFields = new ArrayList<transactionRecords>();
                for(configurationFormFields fields : patientInfoFormFields) {
                    transactionRecords field = new transactionRecords();
                    
                    colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                    try {
                        field.setfieldValue(BeanUtils.getProperty(records, colName));
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    patientFields.add(field);
                }
                transactionDetails.setpatientFields(patientFields);
                
                /* Set all the transaction DETAIL fields */
                List<transactionRecords> detailFields = new ArrayList<transactionRecords>();
                for(configurationFormFields fields : detailFormFields) {
                    transactionRecords field = new transactionRecords();
                    field.setfieldLabel(fields.getFieldDesc()); 
                   
                    colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                    String fieldValue = BeanUtils.getProperty(records, colName);
                    
                    if(fields.getFieldDesc().equals("urgency")) {
                        int id = Integer.parseInt(fieldValue);
                        TableData  tableData = sysAdminManager.getTableData(id, "lu_Urgency");
                        fieldValue = tableData.getDisplayText();
                    }
                    
                   field.setfieldValue(fieldValue);
                   
                   detailFields.add(field);
                }
                transactionDetails.setdetailFields(detailFields);
                
                
                /* get the message type name */
                configDetails = configurationManager.getConfigurationById(transactionRecord.getconfigId());
                transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());
                
                transactionList.add(transactionDetails);
            }
        }
        
        mav.addObject("pendingTransactions", transactionList);
        
        
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
        
        /* Need to get all the message types set up for the user */
        int[] userInfo = (int[])session.getAttribute("userInfo");
        
        /* Need to get a list of all pending transactions */
        List<transactionIn> sentTransactions = transactionInManager.getsentTransactions(userInfo[1]);
        
        List<Transaction> transactionList = new ArrayList<Transaction>();
        
        configuration configDetails;
        String colName;
        transactionInRecords records = null;
        
        if(sentTransactions != null) {
           
            for(transactionIn transactionRecord : sentTransactions) {
                batchUploads batchInfo = transactionInManager.getUploadBatch(transactionRecord.getbatchId());
                
                Transaction transactionDetails = new Transaction();
                transactionDetails.setbatchName(batchInfo.getutBatchName());
                transactionDetails.setconfigId(transactionRecord.getconfigId());
                transactionDetails.settransactionRecordId(transactionRecord.getId());
                transactionDetails.setstatusId(transactionRecord.getstatusId());
                
                lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(transactionRecord.getstatusId());
                transactionDetails.setstatusValue(processStatus.getDisplayCode());
                
                records = transactionInManager.getTransactionRecords(transactionRecord.getId());
                
                /* Get a list of form fields */
                configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transactionRecord.getconfigId(), 2);
                List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionRecord.getconfigId(),transportDetails.getId(),3);
                List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionRecord.getconfigId(),transportDetails.getId(),5);
                List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionRecord.getconfigId(),transportDetails.getId(),6);
                
                /* Set all the transaction TARGET fields */
                List<transactionRecords> toFields = new ArrayList<transactionRecords>();
                for(configurationFormFields fields : targetInfoFormFields) {
                    transactionRecords field = new transactionRecords();
                    
                    colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                    try {
                        field.setfieldValue(BeanUtils.getProperty(records, colName));
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    /* Get the pre-populated values */
                    toFields.add(field);
                }
                transactionDetails.settargetOrgFields(toFields);
                
                /* Set all the transaction PATIENT fields */
                List<transactionRecords> patientFields = new ArrayList<transactionRecords>();
                for(configurationFormFields fields : patientInfoFormFields) {
                    transactionRecords field = new transactionRecords();
                    
                    colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                    try {
                        field.setfieldValue(BeanUtils.getProperty(records, colName));
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    patientFields.add(field);
                }
                transactionDetails.setpatientFields(patientFields);
                
                /* Set all the transaction DETAIL fields */
                List<transactionRecords> detailFields = new ArrayList<transactionRecords>();
                for(configurationFormFields fields : detailFormFields) {
                    transactionRecords field = new transactionRecords();
                    field.setfieldLabel(fields.getFieldDesc()); 
                   
                    colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                    String fieldValue = BeanUtils.getProperty(records, colName);
                    
                    if(fields.getFieldDesc().equals("urgency")) {
                        int id = Integer.parseInt(fieldValue);
                        TableData  tableData = sysAdminManager.getTableData(id, "lu_Urgency");
                        fieldValue = tableData.getDisplayText();
                    }
                    
                   field.setfieldValue(fieldValue);
                   
                   detailFields.add(field);
                }
                transactionDetails.setdetailFields(detailFields);
                
                
                /* get the message type name */
                configDetails = configurationManager.getConfigurationById(transactionRecord.getconfigId());
                transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());
                
                transactionList.add(transactionDetails);
            }
        }
        
        mav.addObject("sentTransactions", transactionList);
        
        
        return mav;
    }
    
    /**
     * The '/sent/details' POST request will take the selected message type and target org and display
     * the new message form.
     * 
     * @param configId  The selected configuration
     * @param targetOrg The selected target organization to receive the new message
     * 
     * @return this request will return the messageDetailsForm
     */
    @RequestMapping(value="/sent/messageDetails", method = RequestMethod.POST)
    public ModelAndView showMessageDetails(@RequestParam(value = "transactionId", required = true) Integer transactionId, HttpSession session) throws NoSuchMethodException {
       
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/sentmessageDetails");
        
        transactionIn transactionInfo = transactionInManager.getTransactionDetails(transactionId);
          
        
        /* Get the configuration details */
        configuration configDetails = configurationManager.getConfigurationById(transactionInfo.getconfigId());
        
        /* Get the organization details for the source (Sender) organization */
        int[] userInfo = (int[])session.getAttribute("userInfo");
        Organization sendingOrgDetails = organizationmanager.getOrganizationById(userInfo[1]);
        
        /* Get the organization details for the target (Receiving) organization */
        transactionTarget targetInfo = transactionInManager.getTransactionTarget(transactionInfo.getbatchId(), transactionInfo.getId());
       
        Organization receivingOrgDetails = organizationmanager.getOrganizationById(configurationManager.getConfigurationById(targetInfo.getconfigId()).getorgId());
        
        /* Get a list of form fields */
        configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transactionInfo.getconfigId(), 2);
        List<configurationFormFields> senderInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),1);
        List<configurationFormFields> senderProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),2);
        List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),3);
        List<configurationFormFields> targetProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),4);
        List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),5);
        List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),6);
        
        Transaction transaction = new Transaction();
        transactionInRecords records = null;
            
        batchUploads batchInfo = transactionInManager.getUploadBatch(transactionInfo.getbatchId());
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
        transaction.settransactionId(transactionId);
        transaction.settransactionTargetId(transactionTarget.getId());

        records = transactionInManager.getTransactionRecords(transactionId);
        transaction.settransactionRecordId(records.getId());
        
        
        /* Set all the transaction SOURCE ORG fields */
        List<transactionRecords> fromFields = new ArrayList<transactionRecords>();
        String tableName;
        String tableCol;
        String colName;
       
        for(configurationFormFields fields : senderInfoFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setfieldLabel(fields.getFieldLabel());
            
            colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
            try {
                field.setfieldValue(BeanUtils.getProperty(records, colName));
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            fromFields.add(field);
        }
        transaction.setsourceOrgFields(fromFields);
        
        /* Set all the transaction SOURCE PROVIDER fields */
        List<transactionRecords> fromProviderFields = new ArrayList<transactionRecords>();
        
        for(configurationFormFields fields : senderProviderFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setfieldLabel(fields.getFieldLabel());
            
            colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
            try {
                field.setfieldValue(BeanUtils.getProperty(records, colName));
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            fromProviderFields.add(field);
        }
       transaction.setsourceProviderFields(fromProviderFields);
        
        
        /* Set all the transaction TARGET fields */
        List<transactionRecords> toFields = new ArrayList<transactionRecords>();
        for(configurationFormFields fields : targetInfoFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setfieldLabel(fields.getFieldLabel());
            
            colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
            try {
                field.setfieldValue(BeanUtils.getProperty(records, colName));
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            /* Get the pre-populated values */
            toFields.add(field);
        }
        transaction.settargetOrgFields(toFields);
        
        /* Set all the transaction TARGET PROVIDER fields */
        List<transactionRecords> toProviderFields = new ArrayList<transactionRecords>();
        for(configurationFormFields fields : targetProviderFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setfieldLabel(fields.getFieldLabel());
            
            colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
            try {
                field.setfieldValue(BeanUtils.getProperty(records, colName));
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            toProviderFields.add(field);
        }
        transaction.settargetProviderFields(toProviderFields);
        
        /* Set all the transaction PATIENT fields */
        List<transactionRecords> patientFields = new ArrayList<transactionRecords>();
        for(configurationFormFields fields : patientInfoFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setfieldLabel(fields.getFieldLabel());
            
            colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
            try {
                field.setfieldValue(BeanUtils.getProperty(records, colName));
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            /* See if any fields have crosswalks associated to it */
            List<fieldSelectOptions> fieldSelectOptions = transactionInManager.getFieldSelectOptions(fields.getId(),transactionInfo.getconfigId());
            field.setfieldSelectOptions(fieldSelectOptions);
            
            patientFields.add(field);
        }
        transaction.setpatientFields(patientFields);
        
        /* Set all the transaction DETAIL fields */
        List<transactionRecords> detailFields = new ArrayList<transactionRecords>();
        for(configurationFormFields fields : detailFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setfieldLabel(fields.getFieldLabel());
            
            colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
            try {
                field.setfieldValue(BeanUtils.getProperty(records, colName));
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            /* See if any fields have crosswalks associated to it */
            List<fieldSelectOptions> fieldSelectOptions = transactionInManager.getFieldSelectOptions(fields.getId(),transactionInfo.getconfigId());
            field.setfieldSelectOptions(fieldSelectOptions);
            
            detailFields.add(field);
        }
        transaction.setdetailFields(detailFields);
        
        mav.addObject("transactionDetails", transaction);
        
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
    public @ResponseBody
    ModelAndView viewStatus(@PathVariable int statusId) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/statusDetails");

        /* Get the details of the selected status */ 
        lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(statusId);
        mav.addObject("statusDetails", processStatus);

        return mav;
    }
    
}
