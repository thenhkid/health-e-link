/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

import com.ut.dph.model.Organization;
import com.ut.dph.model.Provider;
import com.ut.dph.model.Transaction;
import com.ut.dph.model.User;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.batchUploadSummary;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.custom.TableData;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.model.providerAddress;
import com.ut.dph.model.providerIdNum;
import com.ut.dph.model.transactionAttachment;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import com.ut.dph.model.transactionOutNotes;
import com.ut.dph.model.transactionOutRecords;
import com.ut.dph.model.transactionRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.service.organizationManager;
import com.ut.dph.service.providerManager;
import com.ut.dph.service.sysAdminManager;
import com.ut.dph.service.transactionInManager;
import com.ut.dph.service.transactionOutManager;
import com.ut.dph.service.userManager;
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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    private transactionOutManager transactionOutManager;
    
    @Autowired
    private sysAdminManager sysAdminManager;
    
    @Autowired
    private userManager usermanager;
    
    @Autowired
    private providerManager providermanager;
    
    private int inboxTotal = 0;
    private int pendingTotal = 0;
    
    /**
     * The private maxResults variable will hold the number of results to show per list page.
     */
    private static int maxResults = 20;
    
    /**
     * The 'findTotals' function will set the total number of
     * inbox messages and total number of pending messages 
     */
    public void setTotals(int totalInbox, int totalPending, HttpSession session) {
        
        User userInfo = (User)session.getAttribute("userDetails");
        
        /* Need to get a list of all pending batches */
        if(totalPending == 0) {
            pendingTotal = transactionInManager.getpendingBatches(userInfo.getId(), userInfo.getOrgId(), 1, 0).size();
         }
        else {
            pendingTotal = totalPending;
        }
        
        /* Need to get a list of all inbox batches */
        if(totalInbox == 0) {
            inboxTotal = transactionOutManager.getInboxBatches(userInfo.getId(), userInfo.getOrgId(), 1, 0).size();
        }
        else {
            inboxTotal = totalInbox;
        }
        
    }
    
    
    /**
     * The '/inbox' request will serve up the Health-e-Web (ERG) inbox.
     *
     * @param request
     * @param response
     * @return	the health-e-web inbox  view
     * @throws Exception
     */
    @RequestMapping(value = "/inbox", method = RequestMethod.GET)
    public ModelAndView viewinbox(HttpServletRequest request, HttpServletResponse response, HttpSession session,@RequestParam(value = "page", required = false) Integer page) throws Exception {
        
        if (page == null) {
            page = 1;
        }
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/inbox");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        /* Need to get a list of all inbox batches */
        Integer totalInboxBatches = transactionOutManager.getInboxBatches(userInfo.getId(), userInfo.getOrgId(), page, 0).size();
        List<batchDownloads> inboxBatches = transactionOutManager.getInboxBatches(userInfo.getId(), userInfo.getOrgId(), page, maxResults);
        
        if(!inboxBatches.isEmpty()) {
            for(batchDownloads batch : inboxBatches) {
                List<transactionTarget> batchTransactions = transactionOutManager.getInboxBatchTransactions(batch.getId(), userInfo.getId());
                batch.settotalTransactions(batchTransactions.size());
                
                lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                batch.setstatusValue(processStatus.getDisplayCode());
                
                User userDetails = usermanager.getUserById(batch.getuserId());
                String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                batch.setusersName(usersName);
            }
        }
        
        mav.addObject("inboxBatches", inboxBatches);
       
        /* Set the header totals */
        setTotals(totalInboxBatches,0,session);
        
        mav.addObject("pendingTotal", pendingTotal);
        mav.addObject("inboxTotal", inboxTotal);
        
        Integer totalPages = (int)Math.ceil((double)inboxTotal / maxResults);
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", page);
        
        return mav;
    }
    
    
   /**
     * The '/inbox' POST request will serve up the Health-e-Web (ERG) page that will list all inbox
     * messages based on the term searched for.
     *
     * @param request
     * @param response
     * * @param searchTerm The term to search pending messages
     * @return	the health-e-web inbox message list view
     * @throws Exception
     */
    @RequestMapping(value = "/inbox", method = RequestMethod.POST)
    public ModelAndView findInboxBatches(@RequestParam String searchTerm, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/inbox");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        List<batchDownloads> inboxBatches;
        Integer totalInboxBatches = 0;
        
        /* Need to get a list of all pending batches */
        if(searchTerm != null && !"".equals(searchTerm)) {
            inboxBatches = transactionOutManager.getInboxBatches(userInfo.getId(), userInfo.getOrgId(), 1, 0);
            totalInboxBatches = inboxBatches.size();
        }
        else {
           inboxBatches = transactionOutManager.getInboxBatches(userInfo.getId(), userInfo.getOrgId(), 1, maxResults);
           totalInboxBatches = transactionOutManager.getInboxBatches(userInfo.getId(), userInfo.getOrgId(), 1, 0).size();
        }
        
        if(!inboxBatches.isEmpty()) {
            for(batchDownloads batch : inboxBatches) {
                List<transactionTarget> batchTransactions = transactionOutManager.getInboxBatchTransactions(batch.getId(), userInfo.getId());
                batch.settotalTransactions(batchTransactions.size());
                
                lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                batch.setstatusValue(processStatus.getDisplayCode());
                
                User userDetails = usermanager.getUserById(batch.getuserId());
                String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                batch.setusersName(usersName);
            }
        }
        
        if(searchTerm != null && !"".equals(searchTerm)) {
            /* Pass the returned pending batches to the filter method */
            List<batchDownloads> batchResults = transactionOutManager.findInboxBatches(inboxBatches, searchTerm);

            if(!batchResults.isEmpty()) {
                for(batchDownloads batch : inboxBatches) {
                    List<transactionTarget> batchTransactions = transactionOutManager.getInboxBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);
                }
            }

            mav.addObject("inboxBatches", batchResults);
            mav.addObject("totalPages", 0);
        }
        else {
            mav.addObject("inboxBatches", inboxBatches);    
            Integer totalPages = (int)Math.ceil((double)totalInboxBatches / maxResults);
            mav.addObject("totalPages", totalPages);
        }
        
        /* Set the header totals */
        setTotals(totalInboxBatches,0,session);
       
        mav.addObject("searchTerm", searchTerm);
        mav.addObject("pendingTotal", pendingTotal);
        mav.addObject("inboxTotal", inboxTotal);
        mav.addObject("currentPage", 1);
        
        return mav;
    }
    
    
    /**
     * The 'batch/inboxTransactions' request will display a page that will show all transactions
     * associated with the clicked inbox batch.
     * 
     * @param batchId   The id of the clicked batch
     * @param session   The session scope variables
     * 
     * @return This method will return the page to display all transactions for the batch.
     */
    @RequestMapping(value = "inbox/batch/Transactions", method = RequestMethod.POST)
    public ModelAndView showInboxBatchTransactions(@RequestParam Integer batchId, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/batchTransactions");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        /* Get the details of the batch */
        batchDownloads batchDetails = transactionOutManager.getBatchDetails(batchId);
        mav.addObject("batchDetails", batchDetails);
        
        /* Get all the transactions for the batch */
        List<transactionTarget> batchTransactions = transactionOutManager.getInboxBatchTransactions(batchId, userInfo.getId());
        
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
            configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transaction.getconfigId(), 2);
            List<configurationFormFields> sourceInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),1);
            List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),3);
            List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),5);
            List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),6);
            
            /* Set all the transaction SOURCE fields */
            List<transactionRecords> fromFields = new ArrayList<transactionRecords>();
            for(configurationFormFields fields : sourceInfoFormFields) {
                transactionRecords field = new transactionRecords();

                String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                try {
                    field.setfieldValue(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }

                /* Get the pre-populated values */
                fromFields.add(field);
            }
            transactionDetails.setsourceOrgFields(fromFields);

            /* Set all the transaction TARGET fields */
            List<transactionRecords> toFields = new ArrayList<transactionRecords>();
            for(configurationFormFields fields : targetInfoFormFields) {
                transactionRecords field = new transactionRecords();

                String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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

                String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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

                String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                String fieldValue = BeanUtils.getProperty(records, colName);

                if(fields.getFieldDesc().equals("urgency") && !fieldValue.equals("")) {
                    int id = Integer.parseInt(fieldValue);
                    TableData  tableData = sysAdminManager.getTableData(id, "lu_Urgency");
                    fieldValue = tableData.getDisplayText();
                }

               field.setfieldValue(fieldValue);

               detailFields.add(field);
            }
            transactionDetails.setdetailFields(detailFields);


            /* get the message type name */
            configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
            transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

            transactionList.add(transactionDetails);
        }
        
        
        mav.addObject("transactions", transactionList);
        mav.addObject("fromPage", "inbox");
        
        /* Set the header totals */
        setTotals(0,0,session);
        
        mav.addObject("pendingTotal", pendingTotal);
        mav.addObject("inboxTotal", inboxTotal);
        
        return mav;
        
    }
    
    
    /**
     * The '/inbox/messageDetails' POST request will display the selected transaction details. This page is 
     * served up from inbox batch transaction list page. So the form will be readOnly.
     * 
     * @param transactionId  The id of the selected transaction
     * @param fromPage       The page the request is coming from (inbox) 
     * 
     * @return this request will return the messageDetailsForm
     */
    @RequestMapping(value="/inbox/messageDetails", method = RequestMethod.POST)
    public ModelAndView showInboxMessageDetails(@RequestParam(value = "transactionId", required = true) Integer transactionId, HttpSession session) throws NoSuchMethodException {
       
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/sentmessageDetails");
        
        /* Need to update the status of the transaction to Recieved (id=20) */
        
        
        transactionTarget transactionInfo = transactionOutManager.getTransactionDetails(transactionId);
          
        /* Get the configuration details */
        configuration configDetails = configurationManager.getConfigurationById(transactionInfo.getconfigId());
        
        /* Get the organization details for the source (Sender) organization */
        User userInfo = (User)session.getAttribute("userDetails");
        
        /* Get a list of form fields */
        configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transactionInfo.getconfigId(), 2);
        List<configurationFormFields> senderInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),1);
        List<configurationFormFields> senderProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),2);
        List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),3);
        List<configurationFormFields> targetProviderFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),4);
        List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),5);
        List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transactionInfo.getconfigId(),transportDetails.getId(),6);
        
        Transaction transaction = new Transaction();
        transactionOutRecords records = null;
            
        batchDownloads batchInfo = transactionOutManager.getBatchDetails(transactionInfo.getbatchDLId());
        
        transaction.setorgId(batchInfo.getOrgId());
        transaction.settransportMethodId(2);
        transaction.setmessageTypeId(configDetails.getMessageTypeId());
        transaction.setuserId(batchInfo.getuserId());
        transaction.setbatchName(batchInfo.getutBatchName());
        transaction.setstatusId(batchInfo.getstatusId());
        transaction.settransactionStatusId(transactionInfo.getstatusId());
        transaction.setconfigId(transactionInfo.getconfigId());
        transaction.setautoRelease(transportDetails.getautoRelease());
        transaction.setbatchId(batchInfo.getId());
        transaction.settransactionId(transactionId);
        transaction.settransactionTargetId(transactionInfo.getId());
        transaction.setdateSubmitted(transactionInfo.getdateCreated());
        transaction.setsourceType(configDetails.getsourceType());
        transaction.setinternalStatusId(transactionInfo.getinternalStatusId());
        transaction.settransactionInId(transactionInfo.gettransactionInId());
        
        lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(transaction.getstatusId());
        transaction.setstatusValue(processStatus.getDisplayCode());
            
        /* get the message type name */
        transaction.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

        records = transactionOutManager.getTransactionRecords(transactionId);
        transaction.settransactionRecordId(records.getId());
        
        /* Set all the transaction SOURCE ORG fields */
        List<transactionRecords> fromFields = new ArrayList<transactionRecords>();
        
        for(configurationFormFields fields : senderInfoFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setfieldLabel(fields.getFieldLabel());
            
            String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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
            
            String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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
            
            String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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
            
            String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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
            
            String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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
            
            String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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
        
        /* Get the list of internal message status */
        List internalStatusCodes = transactionOutManager.getInternalStatusCodes();
        mav.addObject("internalStatusCodes",internalStatusCodes);
        
        /* Set the header totals */
        setTotals(0,0,session);
        
        mav.addObject("pendingTotal", pendingTotal);
        mav.addObject("inboxTotal", inboxTotal);
        mav.addObject("fromPage", "inbox");
        
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
        User userInfo = (User)session.getAttribute("userDetails");
        List<configuration> configurations = configurationManager.getActiveConfigurationsByUserId(userInfo.getId(), 2);
        
        
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
        
        /* Set the header totals */
        setTotals(0,0,session);
        
        mav.addObject("pendingTotal", pendingTotal);
        mav.addObject("inboxTotal", inboxTotal);
        
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
        User userInfo = (User)session.getAttribute("userDetails");
        Organization sendingOrgDetails = organizationmanager.getOrganizationById(userInfo.getOrgId());
        
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
            batchUploads batchInfo = transactionInManager.getBatchDetails(transactionInfo.getbatchId());
            transactionTarget transactionTarget = transactionInManager.getTransactionTarget(transactionInfo.getbatchId(), transactionId);
            
            transaction.setorgId(batchInfo.getOrgId());
            transaction.settransportMethodId(transportDetails.gettransportMethodId());
            transaction.setmessageTypeId(configDetails.getMessageTypeId());
            transaction.setuserId(batchInfo.getuserId());
            transaction.setbatchName(batchInfo.getutBatchName());
            transaction.setoriginalFileName(batchInfo.getoriginalFileName());
            transaction.setstatusId(transactionInfo.getstatusId());
            transaction.settransactionStatusId(transactionInfo.getstatusId());
            transaction.settargetOrgId(targetOrg);
            transaction.setconfigId(transactionInfo.getconfigId());
            transaction.setautoRelease(transportDetails.getautoRelease());
            transaction.setbatchId(batchInfo.getId());
            transaction.settransactionId(transactionId);
            transaction.settransactionTargetId(transactionTarget.getId());
            transaction.setdateSubmitted(transactionInfo.getdateCreated());
            
            lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(transaction.getstatusId());
            transaction.setstatusValue(processStatus.getDisplayCode());
            
            /* get the message type name */
            transaction.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());
            
            records = transactionInManager.getTransactionRecords(transactionId);
            transaction.settransactionRecordId(records.getId());
        }
        else {
            /* Create a new transaction */
            transaction.setorgId(userInfo.getOrgId());
            transaction.settransportMethodId(2);
            transaction.setmessageTypeId(configDetails.getMessageTypeId());
            transaction.setuserId(userInfo.getId());
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
        
        /* Get a list of organization providers */
        List<Provider> providers = organizationmanager.getOrganizationProviders(configDetails.getorgId(), 1, 0);
        mav.addObject("providers", providers);
        
        /* Set the header totals */
        setTotals(0,0,session);
        
        mav.addObject("pendingTotal", pendingTotal);
        mav.addObject("inboxTotal", inboxTotal);
        
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
    public ModelAndView submitMessage(@ModelAttribute(value = "transactionDetails") Transaction transactionDetails, HttpSession session, @RequestParam String action, RedirectAttributes redirectAttr, @RequestParam List<Integer> attachmentIds) {
        
        User userInfo = (User)session.getAttribute("userDetails");
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
            batchUpload.setuserId(userInfo.getId());
            batchUpload.setutBatchName(batchName);
            batchUpload.settransportMethodId(2);
            batchUpload.setoriginalFileName(batchName);
            
            /* 
            If the "Save" button was pressed set the
            status to "Submission Saved"
            */
            if(action.equals("save")) {
                batchUpload.setstatusId(8);
            }
            /* 
            If the "Release" button was pressed 
            set the status to "Submission Release Pending"
            */
            else if(action.equals("release")) {
                batchUpload.setstatusId(5);
            }
            /* 
            If the "Send" button was pressed 
            set the status to "Submission Released"
            */
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
            batchUploads batchUpload = transactionInManager.getBatchDetails(batchId);
            
            /* 
            If the "Save" button was pressed set the
            status to "Submission Saved"
            */
            if(action.equals("save")) {
                batchUpload.setstatusId(8);
            }
            /* 
            If the "Release" button was pressed 
            set the status to "Submission Release Pending"
            */
            else if(action.equals("release")) {
                batchUpload.setstatusId(5);
            }
            /* 
            If the "Send" button was pressed 
            set the status to "Submission Released"
            */
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

            /* 
            If the "Save" button was pressed set the
            status to "Saved"
            */
            if(action.equals("save")) {
                transactionIn.setstatusId(15);
            }
            /* 
            If the "Release" button was pressed 
            set the status to "Release Pending"
            */
            else if(action.equals("release")) {
                transactionIn.setstatusId(10);
            }
            /* 
            If the "Send" button was pressed 
            set the status to "Released"
            */
            else {
                transactionIn.setstatusId(12);
            }

            transactionId = (Integer) transactionInManager.submitTransactionIn(transactionIn);
            
            /* Need to populate the batchUploadSummary table */
            batchUploadSummary summary = new batchUploadSummary();
            summary.setbatchId(batchId);
            summary.settransactionInId(transactionId);
            summary.setsourceOrgId(transactionDetails.getorgId());
            summary.settargetOrgId(transactionDetails.gettargetOrgId());
            summary.setmessageTypeId(transactionDetails.getmessageTypeId());
            summary.setsourceConfigId(transactionDetails.getconfigId());
            
            transactionInManager.submitBatchUploadSummary(summary);
            
        }
        
        /* Otherwise update existing batch */
        else {
            transactionId =  currTransactionId;  
            
            transactionIn transactionIn = transactionInManager.getTransactionDetails(transactionId);
            
            /* 
            If the "Save" button was pressed set the
            status to "Saved"
            */
            if(action.equals("save")) {
                transactionIn.setstatusId(15);
            }
            /*
            If the "Release" button was pressed 
            set the status to "Release Pending"
            */
            else if(action.equals("release")) {
                transactionIn.setstatusId(10);
            }
            /* 
            If the "Send" button was pressed 
            set the status to "Released"
            */
            else {
                transactionIn.setstatusId(12);
            }
            
            transactionInManager.submitTransactionInChanges(transactionIn);
        }
        
        /* See if any attachments were uploaded */
        if(!attachmentIds.isEmpty()) {
            
            for(Integer attachmentId: attachmentIds) {
                transactionAttachment attachment = transactionInManager.getAttachmentById(attachmentId);
                attachment.setTransactionInId(transactionId);
                
                transactionInManager.submitAttachmentChanges(attachment);
            }
            
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
        
        transactionInManager.submitTransactionTranslatedInRecords(transactionId, transactionRecordId, transactionDetails.getconfigId());
        
        /* Need to populate the transaction Target */
        if(currTransactionTargetId == 0) {
        
            /* Submit a new Transaction Target record */
            transactionTarget transactiontarget = new transactionTarget();
            transactiontarget.setbatchUploadId(batchId);
            transactiontarget.settransactionInId(transactionId);
            transactiontarget.setconfigId(transactionDetails.gettargetConfigId());
            
            /* 
            If the "Save" button was pressed set the
            status to "Saved"
            */
            if(action.equals("save")) {
                transactiontarget.setstatusId(8);
            }
            /* 
            If the "Send" or "Release" button was pressed 
            set the status to "Release Pending"
            */
            else {
                transactiontarget.setstatusId(6);
            }

            transactionInManager.submitTransactionTarget(transactiontarget);
        }
        
        /* Otherwise update existing batch */
        else {
            transactionTargetId =  currTransactionTargetId;  
            
            transactionTarget transactiontarget = transactionInManager.getTransactionTargetDetails(transactionTargetId);
            
            /* 
            If the "Save" button was pressed set the
            status to "Saved"
            */
            if(action.equals("save")) {
                transactiontarget.setstatusId(8);
            }
            /* 
            If the "Send" or "Release" button was pressed 
            set the status to "Release Pending"
            */
            else {
                transactiontarget.setstatusId(6);
            }
            
            transactionInManager.submitTransactionTargetChanges(transactiontarget);
        }
        
        if (action.equals("send")) {
            
            /*
            Once the "Send" button is pressed we will send the batch off to the processTransactions
            method to handle all the processing and saving to the final messages_ tables. This method
            will return true if successfully received and false otherwise.
            */
            boolean transactionSentToProcess = transactionInManager.processBatch(batchId);
            
            
            /*
                Send the user to the "Sent" items page
            */
            redirectAttr.addFlashAttribute("savedStatus", "sent");
            ModelAndView mav = new ModelAndView(new RedirectView("sent"));
            return mav;
        }
        else {
            /*
                Sent the user to the "Pending" items page
            */
            redirectAttr.addFlashAttribute("savedStatus", "saved");
            ModelAndView mav = new ModelAndView(new RedirectView("pending"));
            return mav;
        }
    
    }
    
    /**
     * The '/pending' GET request will serve up the Health-e-Web (ERG) page that will list all pending
     * messages.
     *
     * @param request
     * @param response
     * @return	the health-e-web pending message list view
     * @throws Exception
     */
    @RequestMapping(value = "/pending", method = RequestMethod.GET)
    public ModelAndView pendingBatches(HttpServletRequest request, HttpServletResponse response, HttpSession session,@RequestParam(value = "page", required = false) Integer page) throws Exception {
        
        if (page == null) {
            page = 1;
        }
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/pending");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        /* Need to get a list of all pending batches */
        Integer totalpendingBatches = transactionInManager.getpendingBatches(userInfo.getId(), userInfo.getOrgId(), page, 0).size();
        List<batchUploads> pendingBatches = transactionInManager.getpendingBatches(userInfo.getId(), userInfo.getOrgId(), page, maxResults);
        
        if(!pendingBatches.isEmpty()) {
            for(batchUploads batch : pendingBatches) {
                List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                batch.settotalTransactions(batchTransactions.size());
                
                lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                batch.setstatusValue(processStatus.getDisplayCode());
                
                User userDetails = usermanager.getUserById(batch.getuserId());
                String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                batch.setusersName(usersName);
            }
        }
        
        mav.addObject("pendingBatches", pendingBatches);
       
        /* Set the header totals */
        setTotals(0,totalpendingBatches,session);
        
        mav.addObject("pendingTotal", pendingTotal);
        mav.addObject("inboxTotal", inboxTotal);
        
        Integer totalPages = (int)Math.ceil((double)pendingTotal / maxResults);
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", page);
        
        return mav;
    }
    
    /**
     * The '/pending' POST request will serve up the Health-e-Web (ERG) page that will list all pending
     * messages based on the term searched for.
     *
     * @param request
     * @param response
     * * @param searchTerm The term to search pending messages
     * @return	the health-e-web pending message list view
     * @throws Exception
     */
    @RequestMapping(value = "/pending", method = RequestMethod.POST)
    public ModelAndView findpendingBatches(@RequestParam String searchTerm, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/pending");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        List<batchUploads> pendingBatches;
        Integer totalpendingBatches = 0;
        
        /* Need to get a list of all pending batches */
        if(searchTerm != null && !"".equals(searchTerm)) {
            pendingBatches = transactionInManager.getpendingBatches(userInfo.getId(), userInfo.getOrgId(), 1, 0);
            totalpendingBatches = pendingBatches.size();
        }
        else {
           pendingBatches = transactionInManager.getpendingBatches(userInfo.getId(), userInfo.getOrgId(), 1, maxResults);
           totalpendingBatches = transactionInManager.getpendingBatches(userInfo.getId(), userInfo.getOrgId(), 1, 0).size();
        }
        
        if(!pendingBatches.isEmpty()) {
            for(batchUploads batch : pendingBatches) {
                List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                batch.settotalTransactions(batchTransactions.size());
                
                lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                batch.setstatusValue(processStatus.getDisplayCode());
                
                User userDetails = usermanager.getUserById(batch.getuserId());
                String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                batch.setusersName(usersName);
            }
        }
        
        if(searchTerm != null && !"".equals(searchTerm)) {
            /* Pass the returned pending batches to the filter method */
            List<batchUploads> batchResults = transactionInManager.findBatches(pendingBatches, searchTerm);

            if(!batchResults.isEmpty()) {
                for(batchUploads batch : batchResults) {
                    List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);
                }
            }

            mav.addObject("pendingBatches", batchResults);
            mav.addObject("totalPages", 0);
        }
        else {
            mav.addObject("pendingBatches", pendingBatches);    
            Integer totalPages = (int)Math.ceil((double)totalpendingBatches / maxResults);
            mav.addObject("totalPages", totalPages);
        }
        
        /* Set the header totals */
        setTotals(0,totalpendingBatches,session);
       
        mav.addObject("searchTerm", searchTerm);
        mav.addObject("pendingTotal", pendingTotal);
        mav.addObject("inboxTotal", inboxTotal);
        mav.addObject("currentPage", 1);
        
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
    public ModelAndView sentMessages(HttpServletRequest request, HttpServletResponse response, HttpSession session,@RequestParam(value = "page", required = false) Integer page) throws Exception {
        
        if (page == null) {
            page = 1;
        }
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/sent");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        /* Need to get a list of all pending transactions */
        Integer totalSentBatches = transactionInManager.getsentBatches(userInfo.getId(), userInfo.getOrgId(), page, 0).size();
        
        List<batchUploads> sentBatches = transactionInManager.getsentBatches(userInfo.getId(), userInfo.getOrgId(), page, maxResults);
       
        if(!sentBatches.isEmpty()) {
            for(batchUploads batch : sentBatches) {
                List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                batch.settotalTransactions(batchTransactions.size());
                
                lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                batch.setstatusValue(processStatus.getDisplayCode());
                
                User userDetails = usermanager.getUserById(batch.getuserId());
                String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                batch.setusersName(usersName);
            }
        }
        
        mav.addObject("sentBatches", sentBatches);
        
        /* Set the header totals */
        setTotals(0,0,session);
        
        mav.addObject("pendingTotal", pendingTotal);
        mav.addObject("inboxTotal", inboxTotal);
        
        Integer totalPages = (int)Math.ceil((double)totalSentBatches / maxResults);
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", page);
        
        return mav;
    }
    
    
    /**
     * The '/sent' POST request will serve up the Health-e-Web (ERG) page that will list all sent
     * messages based on the term searched for.
     *
     * @param request
     * @param response
     * @param searchTerm The term to search sent messages 
     * @return	the health-e-web sent message list view
     * @throws Exception
     */
    @RequestMapping(value = "/sent", method = RequestMethod.POST)
    public ModelAndView findsentBatches(@RequestParam String searchTerm, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/sent");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        List<batchUploads> sentBatches;
        Integer totalSentBatches = 0;
        
        /* Need to get a list of all pending batches */
        if(searchTerm != null && !"".equals(searchTerm)) {
            sentBatches = transactionInManager.getsentBatches(userInfo.getId(), userInfo.getOrgId(), 1, 0);
        }
        else {
            sentBatches = transactionInManager.getsentBatches(userInfo.getId(), userInfo.getOrgId(), 1, maxResults);
            totalSentBatches = transactionInManager.getsentBatches(userInfo.getId(), userInfo.getOrgId(), 1, 0).size();
        }
        
        if(!sentBatches.isEmpty()) {
            for(batchUploads batch : sentBatches) {
                List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                batch.settotalTransactions(batchTransactions.size());
                
                lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                batch.setstatusValue(processStatus.getDisplayCode());
                
                User userDetails = usermanager.getUserById(batch.getuserId());
                String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                batch.setusersName(usersName);
            }
        }
        
        if(searchTerm != null && !"".equals(searchTerm)) {
            /* Pass the returned pending batches to the filter method */
            List<batchUploads> batchResults = transactionInManager.findBatches(sentBatches, searchTerm);
               
            if(!batchResults.isEmpty()) {
                for(batchUploads batch : batchResults) {
                    List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batch.getId(), userInfo.getId());
                    batch.settotalTransactions(batchTransactions.size());

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    User userDetails = usermanager.getUserById(batch.getuserId());
                    String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                    batch.setusersName(usersName);
                }
            }

            mav.addObject("sentBatches", batchResults);
            mav.addObject("totalPages", 0);
        }
        else {
            mav.addObject("sentBatches", sentBatches); 
            Integer totalPages = (int)Math.ceil((double)totalSentBatches / maxResults);
            mav.addObject("totalPages", totalPages);
        }
        
        /* Set the header totals */
        setTotals(0,0,session);
       
        mav.addObject("searchTerm", searchTerm);
        mav.addObject("pendingTotal", pendingTotal);
        mav.addObject("inboxTotal", inboxTotal);
        mav.addObject("currentPage", 1);
        
        return mav;
    }
    
    /**
     * The 'batch/transactions' request will display a page that will show all transactions
     * associated with the clicked batch.
     * 
     * @param batchId   The id of the clicked batch
     * @param fromPage  The page the user was on when viewing batches (Pending or Sent)
     * @param session   The session scope variables
     * 
     * @return This method will return the page to display all transactions for the batch.
     */
    @RequestMapping(value = "batch/transactions", method = RequestMethod.POST)
    public ModelAndView showBatchTransactions(@RequestParam Integer batchId, @RequestParam String fromPage, HttpSession session) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/batchTransactions");
        
        /* Need to get all the message types set up for the user */
        User userInfo = (User)session.getAttribute("userDetails");
        
        /* Get the details of the batch */
        batchUploads batchDetails = transactionInManager.getBatchDetails(batchId);
        mav.addObject("batchDetails", batchDetails);
        
        /* Get all the transactions for the batch */
        List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batchId, userInfo.getId());
        
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
            configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(transaction.getconfigId(), 2);
            List<configurationFormFields> sourceInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),1);
            List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),3);
            List<configurationFormFields> patientInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),5);
            List<configurationFormFields> detailFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),6);
            
            /* Set all the transaction SOURCE fields */
            List<transactionRecords> fromFields = new ArrayList<transactionRecords>();
            for(configurationFormFields fields : sourceInfoFormFields) {
                transactionRecords field = new transactionRecords();

                String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                try {
                    field.setfieldValue(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }

                /* Get the pre-populated values */
                fromFields.add(field);
            }
            transactionDetails.setsourceOrgFields(fromFields);

            /* Set all the transaction TARGET fields */
            List<transactionRecords> toFields = new ArrayList<transactionRecords>();
            for(configurationFormFields fields : targetInfoFormFields) {
                transactionRecords field = new transactionRecords();

                String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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

                String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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

                String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
                String fieldValue = BeanUtils.getProperty(records, colName);

                if(fields.getFieldDesc().equals("urgency") && !fieldValue.equals("")) {
                    int id = Integer.parseInt(fieldValue);
                    TableData  tableData = sysAdminManager.getTableData(id, "lu_Urgency");
                    fieldValue = tableData.getDisplayText();
                }

               field.setfieldValue(fieldValue);

               detailFields.add(field);
            }
            transactionDetails.setdetailFields(detailFields);


            /* get the message type name */
            configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
            transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

            transactionList.add(transactionDetails);
        }
        
        
        mav.addObject("transactions", transactionList);
        mav.addObject("fromPage", fromPage);
        
        /* Set the header totals */
        setTotals(0,0,session);
        
        mav.addObject("pendingTotal", pendingTotal);
        mav.addObject("inboxTotal", inboxTotal);
        
        return mav;
        
    }
    
    
    
    /**
     * The '/sent/details' POST request will display the selected transaction details. This page is 
     * served up from inbox or sent Items batch transaction list page. So the form will be readOnly.
     * 
     * @param transactionId  The id of the selected transaction
     * @param fromPage       The page the request is coming from (inbox) or (sent)
     * 
     * @return this request will return the messageDetailsForm
     */
    @RequestMapping(value="/sent/messageDetails", method = RequestMethod.POST)
    public ModelAndView showMessageDetails(@RequestParam(value = "transactionId", required = true) Integer transactionId, @RequestParam(value = "fromPage", required = false) String fromPage, HttpSession session) throws NoSuchMethodException {
       
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/sentmessageDetails");
        
        if(fromPage != null) {
            mav.addObject("fromPage", fromPage);
        }
        
        transactionIn transactionInfo = transactionInManager.getTransactionDetails(transactionId);
          
        /* Get the configuration details */
        configuration configDetails = configurationManager.getConfigurationById(transactionInfo.getconfigId());
        
        /* Get the organization details for the source (Sender) organization */
        User userInfo = (User)session.getAttribute("userDetails");
        
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
            
        batchUploads batchInfo = transactionInManager.getBatchDetails(transactionInfo.getbatchId());
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
        transaction.setdateSubmitted(transactionInfo.getdateCreated());
        
        lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(transaction.getstatusId());
        transaction.setstatusValue(processStatus.getDisplayCode());
            
        /* get the message type name */
        transaction.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

        records = transactionInManager.getTransactionRecords(transactionId);
        transaction.settransactionRecordId(records.getId());
        
        /* Set all the transaction SOURCE ORG fields */
        List<transactionRecords> fromFields = new ArrayList<transactionRecords>();
        
        for(configurationFormFields fields : senderInfoFormFields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(fields.getFieldNo());
            field.setfieldLabel(fields.getFieldLabel());
            
            String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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
            
            String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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
            
            String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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
            
            String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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
            
            String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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
            
            String colName = new StringBuilder().append("f").append(fields.getFieldNo()).toString();
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
        
        /* Set the header totals */
        setTotals(0,0,session);
        
        mav.addObject("pendingTotal", pendingTotal);
        mav.addObject("inboxTotal", inboxTotal);
        mav.addObject("fromPage","sent");
        
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
        mav.setViewName("/Health-e-Web/statusDetails");

        /* Get the details of the selected status */ 
        lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(statusId);
        mav.addObject("statusDetails", processStatus);

        return mav;
    }
    
    /**
     * The 'uploadMessageAttachment' will use jquery to upload a new attachment to the message
     * 
     */
    @RequestMapping(value = "/uploadMessageAttachment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer uploadMessageAttachment(@RequestParam(value = "fileUpload", required = true) MultipartFile fileUpload, @RequestParam(value = "title", required = false) String title, HttpSession session) throws Exception {
        
        /* Get the organization details for the source (Sender) organization */
        User userInfo = (User)session.getAttribute("userDetails");
        Organization sendingOrgDetails = organizationmanager.getOrganizationById(userInfo.getOrgId());
        
        /* Upload the attachment */
        String fileName = transactionInManager.uploadAttachment(fileUpload, sendingOrgDetails.getcleanURL());
        
        /* Create a new attachment */
        transactionAttachment attachment = new transactionAttachment();
        attachment.setTransactionInId(0);
        attachment.setfileLocation("/"+sendingOrgDetails.getcleanURL()+"/attachments/");
        attachment.setfileName(fileName);
        attachment.settitle(title);
        
        Integer attachmentId = transactionInManager.submitAttachment(attachment);
        
        return attachmentId;
    }
    
    /**
     * The 'populateExistingAttachments' function will look to see if any attachments exist
     * for the transaction.
     * 
     * @param   transactionId   The id of the selected transaction (only passed when viewing an
     *                          existing transaction)
     * 
     * @param   newattachmentIdList the list of attachments added to a transaction (only passed
     *                              when a new attachment is added to a transaction)
     * 
     * @return  This function will return the transaction attachment list page.
     */
    @RequestMapping(value= "/populateExistingAttachments.do", method = RequestMethod.GET)
    public @ResponseBody ModelAndView getMessageAttachments(@RequestParam(value = "transactionId", required = false) Integer transactionId, @RequestParam(value = "newattachmentIdList", required = false) List<Integer> newattachmentIdList, @RequestParam(value= "pageFrom", required = false) String pageFrom) {
        
        if(pageFrom == null) {
            pageFrom = "sent";
        }
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/existingAttachments");
        
        List<transactionAttachment> attachments = new ArrayList<transactionAttachment>();
        
        if(transactionId > 0) {
            List<transactionAttachment> existingAttachments = transactionInManager.getAttachmentsByTransactionId(transactionId);
            
            for(transactionAttachment attachment : existingAttachments) {
                attachments.add(attachment);
            }
        }
        
        if(newattachmentIdList != null && !newattachmentIdList.isEmpty()) {
            for(Integer attachmentId : newattachmentIdList) {
                transactionAttachment attachment = transactionInManager.getAttachmentById(attachmentId);
                attachments.add(attachment);
            }
        }
        
        mav.addObject("attachments", attachments);
        mav.addObject("pageFrom", pageFrom);
        
        return mav;
        
    }
    
    /**
     * The 'removeAttachment.do' function will remove the selected attachment.
     * 
     * @param attachmentId  The id of the attachment to remove
     * 
     * @return This function will simply return a 1.
     */
    @RequestMapping(value= "/removeAttachment.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer removeMessageAttachment(@RequestParam(value = "attachmentId", required = false) Integer attachmentId) {
        
        transactionInManager.removeAttachmentById(attachmentId);
       
        return 1;
    }
    
    /**
     * The 'populateProvider.do' function will get the provider details for the provider id
     * passed in.
     * 
     * @param   providerId  The id of the provider to return details for.
     * 
     * @return This function will return the provider object.
     */
    @RequestMapping(value="/populateProvider.do", method = RequestMethod.GET)
    public @ResponseBody Provider populateProvider(@RequestParam(value = "providerId", required = true) int providerId) {
        
        Provider providerDetails = providermanager.getProviderById(providerId);
        
        /* Get the list of addresses for the provider */
        List<providerAddress> providerAddresses = providermanager.getProviderAddresses(providerId);
        providerDetails.setProviderAddresses(providerAddresses);
        
        /* Get the list of ids for the provider */
        List<providerIdNum> providerIds = providermanager.getProviderIds(providerId);
        providerDetails.setProviderIds(providerIds);
        
        return providerDetails;
    }
    
    
    /**
     * The 'batch/sendBatches' POST function will send the marked batches off to the processing
     * method.
     * 
     * @param batchIdList The list of marked batch Ids
     * 
     * @return  This function will redirect the user to the sent items page
     * 
     */
    @RequestMapping(value="/sendBatches", method = RequestMethod.POST)
    public ModelAndView sendBatches(@RequestParam(value = "batchIdList", required = true) List<Integer> batchIdList, RedirectAttributes redirectAttr , HttpSession session) throws NoSuchMethodException {
        
        /* 
        If the list of batch Ids is not empty loop through each marked batch
        and send off to the processBatch method.
        */
        if(!batchIdList.isEmpty()) {
            for(Integer batchId : batchIdList) {
                
                /* Update the transactionIn status to 12 (REL) */
                transactionInManager.updateTransactionStatus(batchId,0,12);
                
                /* Process the batch */
                boolean transactionSentToProcess = transactionInManager.processBatch(batchId);
            }
        }
        
            
        /*
            Send the user back to the pending items page
        */
        redirectAttr.addFlashAttribute("savedStatus", "sent");
        ModelAndView mav = new ModelAndView(new RedirectView("sent"));
        return mav;
    }
      
    
    /**
     * The 'submitMessageNote.do' function will get the provider details for the provider id
     * passed in.
     * 
     * @param   providerId  The id of the provider to return details for.
     * 
     * @return This function will return the provider object.
     */
    @RequestMapping(value="/submitMessageNote.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer submitMessageNote(HttpSession session, @RequestParam(value = "messageTransactionId", required = true) int messageTransactionId, @RequestParam(value = "internalStatusId", required = false) int internalStatusId, @RequestParam(value = "messageNotes", required = false) String messageNotes) {
        
        /* Update the internal status code if exists */
        if(internalStatusId > 0) {
            transactionTarget transactionDetails = transactionOutManager.getTransactionDetails(messageTransactionId);
            transactionDetails.setinternalStatusId(internalStatusId);
            
            transactionOutManager.updateTransactionDetails(transactionDetails);
        }
        
        /* If the note is not emtpy need to insert it */
        if(!"".equals(messageNotes) && messageNotes != null) {
            /* Need to get all the message types set up for the user */
            User userInfo = (User)session.getAttribute("userDetails");
            
            transactionOutNotes newNote = new transactionOutNotes();
            newNote.settransactionTargetId(messageTransactionId);
            newNote.setuserId(userInfo.getId());
            newNote.setnote(messageNotes);
            
            transactionOutManager.saveNote(newNote);
        }
        
        return 1;
    }
    
    
    /**
     * The 'populateExistingNotes.do' function will look to see if any notes exist
     * for the transaction.
     * 
     * @param   transactionId   The id of the selected transaction (only passed when viewing an
     *                          existing transaction)
     * 
     * 
     * @return  This function will return the transaction note list page.
     */
    @RequestMapping(value= "/populateExistingNotes.do", method = RequestMethod.GET)
    public @ResponseBody ModelAndView getMessageNotes(@RequestParam(value = "transactionId", required = true) Integer transactionId) {
       
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Health-e-Web/existingNotes");
        
        List<transactionOutNotes> existingNotes = transactionOutManager.getNotesByTransactionId(transactionId);
        
        /* Set the name of the user who created it */
        for(transactionOutNotes note : existingNotes) {
            User userDetails = usermanager.getUserById(note.getuserId());
            String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
            note.setuserName(usersName);
        }
         
        mav.addObject("notes", existingNotes);
        mav.addObject("pageFrom", "inbox");
        
        return mav;
        
    }
    
    /**
     * The 'removeNote.do' function will remove the selected note.
     * 
     * @param noteId  The id of the note to remove
     * 
     * @return This function will simply return a 1.
     */
    @RequestMapping(value= "/removeNote.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer removeMessageNote(@RequestParam(value = "noteId", required = false) Integer noteId) {
        
        transactionOutManager.removeNoteById(noteId);
       
        return 1;
        
    }
    
}
