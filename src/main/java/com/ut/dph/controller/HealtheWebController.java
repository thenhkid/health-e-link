/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

import com.ut.dph.model.Connections;
import com.ut.dph.model.Organization;
import com.ut.dph.model.Transaction;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.messageType;
import com.ut.dph.model.transactionRecords;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.service.organizationManager;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
        List<configuration> configurations = configurationManager.getActiveConfigurationsByOrgId(userInfo[1]);
        
        messageType messagetype;
        for (configuration config : configurations) {
            messagetype = messagetypemanager.getMessageTypeById(config.getMessageTypeId());
            config.setMessageTypeName(messagetype.getName());
            
            List<Connections> connections = configurationManager.getConnections(config.getId());
            
            for (Connections connection : connections) {
                //Need to get the org name;
                Organization orgDetails = organizationmanager.getOrganizationById(connection.getorgId());
                connection.setorgName(orgDetails.getOrgName());
            }
            
            config.setorgConnections(connections);
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
        List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),2);
        List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),3);
        List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(configId,transportDetails.getId(),4);
        
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
        
        /* Create new transaction Records */
        ArrayList transactionFields = new ArrayList();
        
         /* Set all the transaction SOURCE fields */
        List<transactionRecords> fromFields = new ArrayList<transactionRecords>();
        
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
            
            fromFields.add(field);
        }
        transactionFields.add(fromFields);
        
        
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
            toFields.add(field);
        }
        transactionFields.add(toFields);
        
        /* Set all the transaction PATIENT fields */
        List<transactionRecords> patientFields = new ArrayList<transactionRecords>();
        for(configurationFormFields fields : patientInfoFormFields) {
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
            
            patientFields.add(field);
        }
        transactionFields.add(patientFields);
        
        /* Set all the transaction DETAIL fields */
        List<transactionRecords> detailFields = new ArrayList<transactionRecords>();
        for(configurationFormFields fields : detailFormFields) {
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
            
            detailFields.add(field);
        }
        transactionFields.add(detailFields);
        
        
        /* Save the transaction Fields */
        transaction.settransactionRecords(transactionFields);
        
        mav.addObject(transaction);
        
        
        return mav;
    }
    
}
