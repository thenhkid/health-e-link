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
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import com.ut.dph.model.transactionRecords;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.service.organizationManager;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
     * The '/sent' request will serve up the Health-e-Web (ERG) sent items page.
     *
     * @param request
     * @param response
     * @return	the health-e-web sent items  view
     * @throws Exception
     */
    @RequestMapping(value = "/sent", method = RequestMethod.GET)
    public ModelAndView viewSentItems(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/sent");
        
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
    @RequestMapping(value= "/create/details", method = RequestMethod.POST)
    public ModelAndView showMessageDetailsForm(@RequestParam(value = "configId", required = true) int configId, @RequestParam(value = "targetOrg", required = true) int targetOrg, HttpSession session) {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/messageDetailsForm");
        
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
        
        /* Create a new transaction */
        Transaction transaction = new Transaction();
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
        
       /* Set all the transaction SOURCE ORG fields */
       List<transactionRecords> fromFields = new ArrayList<transactionRecords>();
       String tableName;
       String tableCol;
        
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
            
            /* Get the pre-populated values */
            tableName = fields.getautoPopulateTableName();
            tableCol = fields.getautoPopulateTableCol();
            
            if(!tableName.isEmpty() && !tableName.isEmpty()) {
                field.setfieldValue(transactionInManager.getFieldValue(tableName, tableCol, sendingOrgDetails.getId()));
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
            
            /* Get the pre-populated values */
            tableName = fields.getautoPopulateTableName();
            tableCol = fields.getautoPopulateTableCol();
            
            if(!tableName.isEmpty() && !tableName.isEmpty()) {
                field.setfieldValue(transactionInManager.getFieldValue(tableName, tableCol, sendingOrgDetails.getId()));
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
            
            /* Get the pre-populated values */
            tableName = fields.getautoPopulateTableName();
            tableCol = fields.getautoPopulateTableCol();
            
            if(!tableName.isEmpty() && !tableName.isEmpty()) {
                field.setfieldValue(transactionInManager.getFieldValue(tableName, tableCol, receivingOrgDetails.getId()));
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
            
            /* Get the pre-populated values */
            tableName = fields.getautoPopulateTableName();
            tableCol = fields.getautoPopulateTableCol();
            
            if(!tableName.isEmpty() && !tableName.isEmpty()) {
                field.setfieldValue(transactionInManager.getFieldValue(tableName, tableCol, receivingOrgDetails.getId()));
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
     * @param configId  The selected configuration
     * @param targetOrg The selected target organization to receive the new message
     * 
     * @return this request will return the messageDetailsForm
     */
    @RequestMapping(value= "/submitMessage", method = RequestMethod.POST)
    public void submitMessage(@ModelAttribute(value = "transactionDetails") Transaction transactionDetails, HttpSession session) {
        
        int[] userInfo = (int[])session.getAttribute("userInfo");
        
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
        batchUpload.setstatusId(2);
        batchUpload.settotalRecordCount(1);
        
        Integer batchId = (Integer) transactionInManager.submitBatchUpload(batchUpload); 
        
        /* Submit a new Transaction In record */
        transactionIn transactionIn = new transactionIn();
        transactionIn.setbatchId(batchId);
        transactionIn.setconfigId(transactionDetails.getconfigId());
        transactionIn.setstatusId(2);
        
        Integer transactionId = (Integer) transactionInManager.submitTransactionIn(transactionIn);
        
        
        /* Get the 6 Bucket (Source Org, Source Provider, Target Org, Target Provider, Patient, Details) fields */
        List<transactionRecords> sourceOrgFields = transactionDetails.getsourceOrgFields();
        List<transactionRecords> sourceProviderFields = transactionDetails.getsourceProviderFields();
        List<transactionRecords> targetOrgFields = transactionDetails.gettargetOrgFields();
        List<transactionRecords> targetProviderFields = transactionDetails.gettargetProviderFields();
        List<transactionRecords> patientFields = transactionDetails.getpatientFields();
        List<transactionRecords> detailFields = transactionDetails.getdetailFields();
        
        transactionInRecords records = new transactionInRecords();
        records.setTransactionInId(transactionId);
        
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
        
        Integer transactionRecordId = (Integer) transactionInManager.submitTransactionInRecords(records);
    
    }
    
}
