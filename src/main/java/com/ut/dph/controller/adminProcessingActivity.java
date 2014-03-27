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
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationMessageSpecs;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.custom.searchParameters;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.model.systemSummary;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import com.ut.dph.model.transactionRecords;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.service.organizationManager;
import com.ut.dph.service.sysAdminManager;
import com.ut.dph.service.transactionInManager;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
    private sysAdminManager sysAdminManager;
    
    @Autowired
    private organizationManager organizationmanager;
    
    @Autowired
    private configurationTransportManager configurationTransportManager;
    
    @Autowired
    private messageTypeManager messagetypemanager;
    
    @Autowired
    private configurationManager configurationManager;
    
    /**
     * The private maxResults variable will hold the number of results to show per list page.
     */
    private static int maxResults = 30;
    
    /**
     * The '/inbound' GET request will serve up the existing list of generated referrals and feedback reports
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return          The list of inbound batch list
     *
     * @Objects	(1) An object containing all the found batches
     *
     * @throws Exception
     */
    @RequestMapping(value = "/inbound", method = RequestMethod.GET)
    public ModelAndView listInBoundBatches(HttpSession session) throws Exception {
        
        int page = 1;
        
        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");
        String searchTerm = "";
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/inbound");
        
        if("".equals(searchParameters.getsection()) || !"inbound".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setpage(1);
            searchParameters.setsection("inbound");
            searchParameters.setsearchTerm("");
        }
        else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
            page = searchParameters.getpage();
            searchTerm = searchParameters.getsearchTerm();
        }
            
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("searchTerm", searchTerm);
        
        /* Get system inbound summary */
        systemSummary summaryDetails = transactionInManager.generateSystemInboundSummary();
        mav.addObject("summaryDetails", summaryDetails);
        
        
        /* Get all inbound transactions */
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaluploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate, searchTerm, 1, 0).size();
            List<batchUploads> uploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate, searchTerm, 1, maxResults);
            
            List<Integer> statusIds = new ArrayList();

            if(!uploadedBatches.isEmpty()) {
                for(batchUploads batch : uploadedBatches) {
                    batch.settotalTransactions(transactionInManager.getRecordCounts(batch.getId(), statusIds, false, false));

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    Organization orgDetails = organizationmanager.getOrganizationById(batch.getOrgId());
                    batch.setorgName(orgDetails.getOrgName());
                    
                    batch.settransportMethod(configurationTransportManager.getTransportMethodById(batch.gettransportMethodId()));

                }
            }
            

           mav.addObject("batches", uploadedBatches);
           
           Integer totalPages = (int)Math.ceil((double)totaluploadedBatches / maxResults);
           mav.addObject("totalPages", totalPages);
        }
        catch (Exception e) {
            throw new Exception("Error occurred viewing the all uploaded batches.",e);
        }
        
        return mav;
        
    }
    
    /**
     * The '/inbound' POST request will serve up the existing list of generated referrals and feedback reports
     * based on a search or date
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return          The list of inbound batch list
     *
     * @Objects	(1) An object containing all the found batches
     *
     * @throws Exception
     */
    @RequestMapping(value = "/inbound", method = RequestMethod.POST)
    public ModelAndView listInBoundBatches(@RequestParam(value = "page", required = false) Integer page, @RequestParam String searchTerm, @RequestParam Date fromDate, @RequestParam Date toDate,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
        if(page == null || page < 1) {
            page = 1;
        }
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/inbound");
        
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        mav.addObject("searchTerm", searchTerm);
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");
        searchParameters.setfromDate(fromDate);
        searchParameters.settoDate(toDate);
        searchParameters.setpage(page);
        searchParameters.setsection("inbound");
        searchParameters.setsearchTerm(searchTerm);
        
        
        /* Get all inbound transactions */
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaluploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate, searchTerm, 1, 0).size();
            List<batchUploads> uploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate, searchTerm, page, maxResults);
            
            List<Integer> statusIds = new ArrayList();

            if(!uploadedBatches.isEmpty()) {
                for(batchUploads batch : uploadedBatches) {
                    batch.settotalTransactions(transactionInManager.getRecordCounts(batch.getId(), statusIds, false, false));

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    Organization orgDetails = organizationmanager.getOrganizationById(batch.getOrgId());
                    batch.setorgName(orgDetails.getOrgName());
                    
                    batch.settransportMethod(configurationTransportManager.getTransportMethodById(batch.gettransportMethodId()));

                }
            }
            

           mav.addObject("batches", uploadedBatches);
           
           Integer totalPages = (int)Math.ceil((double)totaluploadedBatches / maxResults);
           mav.addObject("totalPages", totalPages);
        }
        catch (Exception e) {
            throw new Exception("Error occurred viewing the all uploaded batches.",e);
        }
        
        return mav;
    }
    
   /**
     * The '/batch/{batchName}' POST request will retrieve a list of transactions that are associated to the clicked batch 
     *
     * @param batchName	The name of the batch to retreive transactions for
     * @return          The list of inbound batch transactions
     *
     * @Objects	(1) An object containing all the found batch transactions
     *
     * @throws Exception
     */
    @RequestMapping(value = "/batch/{batchName}", method = RequestMethod.GET)
    public ModelAndView listBatchTransactions(@PathVariable String batchName) throws Exception {
  
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/transactions");
        
        /* Get the details of the batch */
        batchUploads batchDetails = transactionInManager.getBatchDetailsByBatchName(batchName);
        
        if(batchDetails != null) {
            
            Organization orgDetails = organizationmanager.getOrganizationById(batchDetails.getOrgId());
            batchDetails.setorgName(orgDetails.getOrgName());
        
            mav.addObject("batchDetails", batchDetails);

            try {
                /* Get all the transactions for the batch */
                List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batchDetails.getId(), 0);

                List<Transaction> transactionList = new ArrayList<Transaction>();
                
                if(batchTransactions.size() > 100) {
                    mav.addObject("toomany","toomany");
                    mav.addObject("size", batchTransactions.size());
                }
                else {

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
                        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transaction.getconfigId());
                        List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),3);

                        /* Set all the transaction TARGET fields */
                        List<transactionRecords> toFields;
                        if(!targetInfoFormFields.isEmpty()) {
                            toFields = setOutboundFormFields(targetInfoFormFields, records, 0, true, 0);
                        }
                        else {
                            toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transaction.getId()).gettargetOrgId());
                        }
                        transactionDetails.settargetOrgFields(toFields);

                        /* get the message type name */
                        configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                        transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());

                        configurationMessageSpecs messageSpecs  = configurationManager.getMessageSpecs(transaction.getconfigId());

                        if(messageSpecs.getrptField1() > 0) {

                            configurationFormFields formField1 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(),transportDetails.getId(),messageSpecs.getrptField1());

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

                        if(messageSpecs.getrptField2() > 0) {

                            configurationFormFields formField2 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(),transportDetails.getId(),messageSpecs.getrptField2());

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

                        if(messageSpecs.getrptField3() > 0) {

                            configurationFormFields formField3 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(),transportDetails.getId(),messageSpecs.getrptField3());

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

                        if(messageSpecs.getrptField4() > 0) {

                            configurationFormFields formField4 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(),transportDetails.getId(),messageSpecs.getrptField4());

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
                 }

                mav.addObject("transactions", transactionList);

            }
            catch (Exception e) {
                throw new Exception("(Admin) Error occurred in getting transactions for a sent batch. batchId: "+ batchDetails.getId()+" ERROR: "+e.getMessage(),e);
            }
        }
        
        return mav;
    }
    
    /**
     * The '/batch/{batchName}' POST request will retrieve a list of transactions that are associated to the clicked batch and
     * match the entered search term.
     *
     * @param batchName	 The name of the batch to retreive transactions for
     * @param searchTerm The term to narrow down the results
     * 
     * @return          The list of inbound batch transactions
     *
     * @Objects	(1) An object containing all the found batch transactions
     *
     * @throws Exception
     */
    @RequestMapping(value = "/batch/{batchName}", method = RequestMethod.POST)
    public ModelAndView listBatchTransactions(@PathVariable String batchName, @RequestParam String searchTerm) throws Exception {
  
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/transactions");
        
        /* Get the details of the batch */
        batchUploads batchDetails = transactionInManager.getBatchDetailsByBatchName(batchName);
        
        if(batchDetails != null) {
            
            Organization orgDetails = organizationmanager.getOrganizationById(batchDetails.getOrgId());
            batchDetails.setorgName(orgDetails.getOrgName());
        
            mav.addObject("batchDetails", batchDetails);

            try {
                /* Get all the transactions for the batch */
                List<transactionIn> batchTransactions = transactionInManager.getBatchTransactions(batchDetails.getId(), 0);

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
                    configurationTransport transportDetails = configurationTransportManager.getTransportDetails(transaction.getconfigId());
                    List<configurationFormFields> targetInfoFormFields = configurationTransportManager.getConfigurationFieldsByBucket(transaction.getconfigId(),transportDetails.getId(),3);
                    
                    /* Set all the transaction TARGET fields */
                    List<transactionRecords> toFields;
                    if(!targetInfoFormFields.isEmpty()) {
                        toFields = setOutboundFormFields(targetInfoFormFields, records, 0, true, 0);
                    }
                    else {
                        toFields = setOrgDetails(transactionInManager.getUploadSummaryDetails(transaction.getId()).gettargetOrgId());
                    }
                    transactionDetails.settargetOrgFields(toFields);

                    /* get the message type name */
                    configuration configDetails = configurationManager.getConfigurationById(transaction.getconfigId());
                    transactionDetails.setmessageTypeName(messagetypemanager.getMessageTypeById(configDetails.getMessageTypeId()).getName());
                    
                    configurationMessageSpecs messageSpecs  = configurationManager.getMessageSpecs(transaction.getconfigId());
                    
                    if(messageSpecs.getrptField1() > 0) {
                        
                        configurationFormFields formField1 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(),transportDetails.getId(),messageSpecs.getrptField1());
                        
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
                    
                    if(messageSpecs.getrptField2() > 0) {
                        
                        configurationFormFields formField2 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(),transportDetails.getId(),messageSpecs.getrptField2());
                        
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
                    
                    if(messageSpecs.getrptField3() > 0) {
                        
                        configurationFormFields formField3 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(),transportDetails.getId(),messageSpecs.getrptField3());
                        
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
                    
                    if(messageSpecs.getrptField4() > 0) {
                        
                        configurationFormFields formField4 = configurationTransportManager.getConfigurationFieldsByFieldNo(transaction.getconfigId(),transportDetails.getId(),messageSpecs.getrptField4());
                        
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
                
                if(!"".equals(searchTerm)) {
                    List<Transaction> matchedtransactionList = new ArrayList<Transaction>();
                    
                    if(transactionList.size() > 0) {
                        
                        for(Transaction transaction : transactionList) {
                           
                            boolean matchFound = transactionInManager.searchTransactions(transaction, searchTerm);
                            if(matchFound == true) {
                                matchedtransactionList.add(transaction);
                            }
                        }
                        
                    }
                    
                    if(matchedtransactionList.size() > 100) {
                        mav.addObject("transactions", "");
                        mav.addObject("stilltoomany","stilltoomany");
                        mav.addObject("size", matchedtransactionList.size());
                    }
                    else {
                        mav.addObject("transactions", matchedtransactionList);
                    }
                    
                }
                else {
                    
                    if(transactionList.size() > 100) {
                        mav.addObject("transactions", "");
                        mav.addObject("toomany","toomany");
                        mav.addObject("size", transactionList.size());
                    }
                    else {
                        mav.addObject("transactions", transactionList);
                    }
                }

            }
            catch (Exception e) {
                throw new Exception("(Admin) Error occurred in searching transactions for a sent batch. batchId: "+ batchDetails.getId()+" ERROR: "+e.getMessage(),e);
            }
        }
        
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
        mav.setViewName("/administrator/processing-activities/statusDetails");

        /* Get the details of the selected status */ 
        lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(statusId);
        mav.addObject("statusDetails", processStatus);

        return mav;
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
    
    /**
     * The 'setOutboundFormFields' will create and populate the form field object
     * 
     * @param formfields  The list of form fields
     * @param records     The values of the form fields to populate with.
     * 
     * @return This function will return a list of transactionRecords fields with the correct data
     * 
     * @throws NoSuchMethodException 
     */ 
    public List<transactionRecords> setOutboundFormFields(List<configurationFormFields> formfields, transactionInRecords records, int configId, boolean readOnly, int orgId) throws NoSuchMethodException {
        
        List<transactionRecords> fields = new ArrayList<transactionRecords>();
        
        for(configurationFormFields formfield : formfields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(formfield.getFieldNo());
            field.setrequired(formfield.getRequired());
            field.setsaveToTable(formfield.getsaveToTableName());
            field.setsaveToTableCol(formfield.getsaveToTableCol());
            field.setfieldLabel(formfield.getFieldLabel());
            field.setreadOnly(readOnly);
            field.setfieldValue(null);
            
            /* Get the pre-populated values */
            String tableName = formfield.getautoPopulateTableName();
            String tableCol = formfield.getautoPopulateTableCol();
            
            /* Get the validation */
            if(formfield.getValidationType() > 1) {
                field.setvalidation(messagetypemanager.getValidationById(formfield.getValidationType()).toString());
            }
            
            if(records != null) {
                String colName = new StringBuilder().append("f").append(formfield.getFieldNo()).toString();
                try {
                    field.setfieldValue(BeanUtils.getProperty(records, colName));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(HealtheWebController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                /* If autopopulate field is set make it read only */
                if(!tableName.isEmpty() && !tableCol.isEmpty()) {
                    field.setreadOnly(true);
                }
            }
            else if(orgId > 0)  {
            
                if(!tableName.isEmpty() && !tableCol.isEmpty()) {
                    field.setfieldValue(transactionInManager.getFieldValue(tableName, tableCol, "id", orgId));
                }
            }
            
            if(configId > 0) {
                /* See if any fields have crosswalks associated to it */
                List<fieldSelectOptions> fieldSelectOptions = transactionInManager.getFieldSelectOptions(formfield.getId(),configId);
                field.setfieldSelectOptions(fieldSelectOptions);
            }
            
            fields.add(field);
        }
        
        return fields;
    }
    
    /**
     * The 'setOrgDetails' function will set the field values to the passed in orgId if the
     * organization information wasn't collected with the file upload.
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
    
}
