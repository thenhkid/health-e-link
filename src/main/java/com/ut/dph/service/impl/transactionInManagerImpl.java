/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.dph.service.impl;

import com.ut.dph.dao.messageTypeDAO;
import com.ut.dph.dao.transactionInDAO;
import com.ut.dph.dao.transactionOutDAO;
import com.ut.dph.model.CrosswalkData;
import com.ut.dph.model.Macros;
import com.ut.dph.model.Organization;
import com.ut.dph.model.MoveFilesLog;
import com.ut.dph.model.Transaction;
import com.ut.dph.model.TransactionInError;
import com.ut.dph.model.User;
import com.ut.dph.model.UserActivity;
import com.ut.dph.model.batchUploadSummary;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.configurationFTPFields;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationMessageSpecs;
import com.ut.dph.model.configurationRhapsodyFields;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.transactionAttachment;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import com.ut.dph.model.transactionRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.model.custom.ConfigErrorInfo;
import com.ut.dph.model.custom.ConfigForInsert;
import com.ut.dph.model.custom.TransErrorDetail;
import com.ut.dph.model.custom.TransErrorDetailDisplay;
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.model.messagePatients;
import com.ut.dph.model.systemSummary;
import com.ut.dph.reference.fileSystem;
import com.ut.dph.service.CCDtoTxt;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.fileManager;
import com.ut.dph.service.hl7toTxt;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.service.organizationManager;
import com.ut.dph.service.sysAdminManager;
import com.ut.dph.service.userManager;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.ut.dph.service.transactionInManager;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author chadmccue
 */
@Service
public class transactionInManagerImpl implements transactionInManager {

    @Autowired
    private transactionInDAO transactionInDAO;

    @Autowired
    private messageTypeDAO messageTypeDAO;

    @Autowired
    private configurationManager configurationManager;

    @Autowired
    private configurationTransportManager configurationtransportmanager;

    @Autowired
    private messageTypeManager messagetypemanager;

    @Autowired
    private organizationManager organizationmanager;

    @Autowired
    private transactionOutDAO transactionOutDAO;

    @Autowired
    private sysAdminManager sysAdminManager;

    @Autowired
    private userManager usermanager;
    
    @Autowired
    private fileManager filemanager;


    @Autowired
    private hl7toTxt hl7toTxt;
    
    @Autowired
    private CCDtoTxt ccdtotxt;

    private int processingSysErrorId = 5;

    //final status Ids
    private List<Integer> finalStatusIds = Arrays.asList(11, 12, 13, 16);
    
    private String archivePath = "/bowlink/archives/";
    
    @Override
    @Transactional
    public String getFieldValue(String tableName, String tableCol, String idCol, int idValue) {
        return transactionInDAO.getFieldValue(tableName, tableCol, idCol, idValue);
    }

    @Override
    @Transactional
    public List<fieldSelectOptions> getFieldSelectOptions(int fieldId, int configId) {
        return transactionInDAO.getFieldSelectOptions(fieldId, configId);
    }

    @Override
    @Transactional
    public Integer submitBatchUpload(batchUploads batchUpload) throws Exception {
        return transactionInDAO.submitBatchUpload(batchUpload);
    }

    @Override
    @Transactional
    public void submitBatchUploadSummary(batchUploadSummary summary) throws Exception {
        transactionInDAO.submitBatchUploadSummary(summary);
    }

    @Override
    @Transactional
    public void submitBatchUploadChanges(batchUploads batchUpload) throws Exception {
        transactionInDAO.submitBatchUploadChanges(batchUpload);
    }

    @Override
    @Transactional
    public Integer submitTransactionIn(transactionIn transactionIn) throws Exception {
        return transactionInDAO.submitTransactionIn(transactionIn);
    }

    @Override
    @Transactional
    public void submitTransactionInChanges(transactionIn transactionIn) throws Exception {
        transactionInDAO.submitTransactionInChanges(transactionIn);
    }

    @Override
    @Transactional
    public Integer submitTransactionInRecords(transactionInRecords records) throws Exception {
        return transactionInDAO.submitTransactionInRecords(records);
    }

    @Override
    @Transactional
    public void submitTransactionInRecordsUpdates(transactionInRecords records) throws Exception {
        transactionInDAO.submitTransactionInRecordsUpdates(records);
    }

    @Override
    @Transactional
    public void submitTransactionTranslatedInRecords(int transactionId, int transactionRecordId, int configId) throws Exception {
        transactionInDAO.submitTransactionTranslatedInRecords(transactionId, transactionRecordId, configId);
    }

    @Override
    @Transactional
    public List<batchUploads> getpendingBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception {
        return transactionInDAO.getpendingBatches(userId, orgId, fromDate, toDate);
    }

    @Override
    @Transactional
    public List<transactionIn> getBatchTransactions(int batchId, int userId) throws Exception {
        return transactionInDAO.getBatchTransactions(batchId, userId);
    }

    @Override
    @Transactional
    public List<batchUploads> getsentBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception {
        return transactionInDAO.getsentBatches(userId, orgId, fromDate, toDate);
    }

    @Override
    @Transactional
    public batchUploads getBatchDetails(int batchId) throws Exception {
        return transactionInDAO.getBatchDetails(batchId);
    }

    @Override
    @Transactional
    public batchUploads getBatchDetailsByBatchName(String batchName) throws Exception {
        return transactionInDAO.getBatchDetailsByBatchName(batchName);
    }

    @Override
    @Transactional
    public transactionIn getTransactionDetails(int transactionId) throws Exception {
        return transactionInDAO.getTransactionDetails(transactionId);
    }

    @Override
    @Transactional
    public transactionInRecords getTransactionRecords(int transactionId) {
        return transactionInDAO.getTransactionRecords(transactionId);
    }

    @Override
    @Transactional
    public transactionInRecords getTransactionRecord(int recordId) {
        return transactionInDAO.getTransactionRecord(recordId);
    }

    @Override
    @Transactional
    public void submitTransactionTarget(transactionTarget transactionTarget) {
        transactionInDAO.submitTransactionTarget(transactionTarget);
    }

    @Override
    @Transactional
    public transactionTarget getTransactionTargetDetails(int transactionTargetId) {
        return transactionInDAO.getTransactionTargetDetails(transactionTargetId);
    }

    @Override
    @Transactional
    public void submitTransactionTargetChanges(transactionTarget transactionTarget) throws Exception {
        transactionInDAO.submitTransactionTargetChanges(transactionTarget);
    }

    @Override
    @Transactional
    public transactionTarget getTransactionTarget(int batchUploadId, int transactionInId) {
        return transactionInDAO.getTransactionTarget(batchUploadId, transactionInId);
    }

    /**
     * The 'uploadAttachment' function will take in the file and orgName and upload the file to the appropriate file on the file system.
     *
     * @param fileUpload The file to be uploaded
     * @param orgName The organization name that is uploading the file. This will be the folder where to save the file to.
     */
    @Override
    public String uploadAttachment(MultipartFile fileUpload, String orgName) throws Exception {

        MultipartFile file = fileUpload;
        String fileName = file.getOriginalFilename();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = file.getInputStream();
            File newFile = null;

            //Set the directory to save the brochures to
            fileSystem dir = new fileSystem();
            dir.setDir(orgName, "attachments");

            newFile = new File(dir.getDir() + fileName);

            if (newFile.exists()) {
                int i = 1;
                while (newFile.exists()) {
                    int iDot = fileName.lastIndexOf(".");
                    newFile = new File(dir.getDir() + fileName.substring(0, iDot) + "_(" + ++i + ")" + fileName.substring(iDot));
                }
                fileName = newFile.getName();
            } else {
                newFile.createNewFile();
            }

            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();

            //Save the attachment
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    @Override
    @Transactional
    public Integer submitAttachment(transactionAttachment attachment) throws Exception {
        return transactionInDAO.submitAttachment(attachment);
    }

    @Override
    @Transactional
    public transactionAttachment getAttachmentById(int attachmentId) throws Exception {
        return transactionInDAO.getAttachmentById(attachmentId);
    }

    @Override
    @Transactional
    public void submitAttachmentChanges(transactionAttachment attachment) throws Exception {
        transactionInDAO.submitAttachmentChanges(attachment);
    }

    @Override
    @Transactional
    public List<transactionAttachment> getAttachmentsByTransactionId(int transactionInId) throws Exception {
        return transactionInDAO.getAttachmentsByTransactionId(transactionInId);
    }

    @Override
    @Transactional
    public void removeAttachmentById(int attachmentId) throws Exception {

        /* Need to get the file name of the attachment */
        transactionAttachment attachment = getAttachmentById(attachmentId);

        /* Need to remove the attachment */
        fileSystem currdir = new fileSystem();
        currdir.setDirByName(attachment.getfileLocation());
        File currFile = new File(currdir.getDir() + attachment.getfileName());
        currFile.delete();

        /* Now remove the attachment from the database */
        transactionInDAO.removeAttachmentById(attachmentId);

    }

    @Override
    public List<ConfigForInsert> setConfigForInsert(int configId, int batchUploadId) {
        // we call sp and set the parameters here
        return transactionInDAO.setConfigForInsert(configId, batchUploadId);
    }

    @Override
    public List<Integer> getConfigIdsForBatch(int batchUploadId, boolean getAll) {
        return transactionInDAO.getConfigIdsForBatch(batchUploadId, getAll, 0);
    }

    @Override
    public List<Integer> getConfigIdsForBatch(int batchUploadId, boolean getAll, Integer transactionInId) {
        return transactionInDAO.getConfigIdsForBatch(batchUploadId, getAll, transactionInId);
    }

    @Override
    public List<Integer> getTransWithMultiValues(ConfigForInsert config) {
        return transactionInDAO.getTransWithMultiValues(config);
    }

    /**
     * These are ready records We will insert by configId All the values for being inserted into the same field would have been appended to the first field Id *
     */
    @Override
    public boolean insertSingleToMessageTables(ConfigForInsert configForInsert) {
        return transactionInDAO.insertSingleToMessageTables(configForInsert);
    }

    /**
     * this method takes in the transId, the delimited fields, loops them and insert them into the message table by string pairs. UT delimiter is ^^^^^
     */
    @Override
    public boolean insertMultiValToMessageTables(ConfigForInsert config, Integer subStringCounter, Integer transId) {
        return transactionInDAO.insertMultiValToMessageTables(config, subStringCounter, transId);
    }

    /**
     * The 'clearMessageTables' function will loop through all each message table and remove any rows matching transactionInIds belonging to a batch.
     *
     * @param batchId of the batch to be cleared.
     *
     * It will return 0 as in no errors.
     */
    @Override
    public Integer clearMessageTables(int batchId) {
        List<String> mts = transactionInDAO.getMessageTables();
        Integer sysErrorCount = 0;
        try {
            for (String mt : mts) {
                sysErrorCount = sysErrorCount + transactionInDAO.clearMessageTableForBatch(batchId, mt);
            }
            return sysErrorCount;
        } catch (Exception e) {
            System.err.println("clearMessageTables " + e.getStackTrace());
            return 1;

        }
    }

    

    @Override
    public List<Integer> getBlankTransIds(ConfigForInsert config) {
        return transactionInDAO.getBlankTransIds(config);
    }

    @Override
    public Integer countSubString(ConfigForInsert config, Integer transId) {
        return transactionInDAO.countSubString(config, transId);
    }

    /**
     * The 'getuploadedBatches' function calls getuploadedBatches(int userId, int orgId, Date fromDate, Date toDate, List<Integer> excludedStatusIds)
     *
     * It defaults excludedStatusIds to 1 as that is how the original fn is written. We wrote new method to pass in 1 as excludedStatusIds so we don't have to go back and modify every single method.
     *
     * @param userId
     * @param orgId
     * @param fromDate
     * @param todate
     *
     * It will return a list of batchUploads.
     */
    @Override
    public List<batchUploads> getuploadedBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception {
        return getuploadedBatches(userId, orgId, fromDate, toDate, Arrays.asList(1));
    }

    /**
     * The 'getuploadedBatches' function gets a list of batchUploads according to parameters being queried.
     *
     * @param userId
     * @param orgId
     * @param fromDate
     * @param todate
     * @param excludedStatusIds - statusIds for batches to exclude
     *
     * It will return a list of batchUploads.
     */
    @Override
    public List<batchUploads> getuploadedBatches(int userId, int orgId, Date fromDate, Date toDate, List<Integer> excludedStatusIds) throws Exception {
        return transactionInDAO.getuploadedBatches(userId, orgId, fromDate, toDate, excludedStatusIds);
    }

    /**
     * We will take a batch and then from its status etc we will decide if we want to process transactions or not. This method allowa admin to run just one batch This assumes batches SR - 6, Trans status REL We still run through entire process but these records should pass... (check to make sure it aligns with file upload) just be applying Macros / CW and inserting into our message tables This method will only process a batch that is RP or SSL
     *
     * We added to this method as if a batch is being call from fixErrors (ERG Form), we do not clear errors in transactionInErrors table. We default the flag to false as when it is call from old methods, we
     *
     * *
     */
    @Override
    public boolean processBatch(int batchUploadId, boolean doNotClearErrors, Integer transactionId) throws Exception {

        Integer batchStausId = 29;
        List<Integer> errorStatusIds = Arrays.asList(11, 13, 14, 16);
        //get batch details
        batchUploads batch = getBatchDetails(batchUploadId);
        //this should be the same point of both ERG and Uploaded File *
        Integer systemErrorCount = 0;
        // Check to make sure the file is valid for processing, valid file is a batch with SSL (3) or SR (6)*

        boolean insertTargets = false;
        // we should only insert for batches that are just loaded
        if (batch.getstatusId() == 3) {
        	insertTargets = true;
        }
        if ((batch.getstatusId() == 3 || batch.getstatusId() == 6)) {
            // set batch to SBP - 4*
            updateBatchStatus(batchUploadId, 4, "startDateTime");
      
            //clear transactionInError table for batch, if do not clear errors is true, we skip this.
            if (!doNotClearErrors) {
                systemErrorCount = systemErrorCount + clearTransactionInErrors(batchUploadId, true);
            }

            List<Integer> configIds = getConfigIdsForBatch(batchUploadId, false, transactionId);

            for (Integer configId : configIds) {
				//we need to run all checks before insert regardless *
            	/** we are reordering 1. cw/macro, 2. required and 3. validate **/
            	// 1. grab the configurationDataTranslations and run cw/macros
                List<configurationDataTranslations> dataTranslations = configurationManager
                        .getDataTranslationsWithFieldNo(configId, 3); //while processing
                for (configurationDataTranslations cdt : dataTranslations) {
                    if (cdt.getCrosswalkId() != 0) {
                        systemErrorCount = systemErrorCount + processCrosswalk(configId, batchUploadId, cdt, false, transactionId);
                    } else if (cdt.getMacroId() != 0) {
                        systemErrorCount = systemErrorCount + processMacro(configId, batchUploadId, cdt, false, transactionId);
                    }
                }
                
                //check R/O
                List<configurationFormFields> reqFields = getRequiredFieldsForConfig(configId);

                for (configurationFormFields cff : reqFields) {
                    systemErrorCount = systemErrorCount + insertFailedRequiredFields(cff, batchUploadId, transactionId);
                }
                // update status of the failed records to ERR - 14
                updateStatusForErrorTrans(batchUploadId, 14, false, transactionId);

                //run validation
                systemErrorCount = systemErrorCount + runValidations(batchUploadId, configId, transactionId);
                // update status of the failed records to ERR - 14
                updateStatusForErrorTrans(batchUploadId, 14, false, transactionId);

               
                /** targets should only be inserted if it hasn't gone through this loop already **/
                if (insertTargets) {
	                //load our targets here
	                //load targets - we need to loadTarget only if field for target is blank, otherwise we load what user sent
		            Integer batchId = batchUploadId;
		            List<configurationConnection> batchTargetList = getBatchTargets(batchId, true);
		            int sourceConfigId = 0;
		            if (batchTargetList.size() <= 0) {
		                insertProcessingError(10, null, batchId, null, null, null, null, false, false, "No valid connections were found for loading batch.");
		                updateTransactionStatus(batchId, 0, 0, 13);
		                updateRecordCounts(batchId, new ArrayList<Integer>(), false, "errorRecordCount");
		                updateRecordCounts(batchId, new ArrayList<Integer>(), false, "totalRecordCount");
		                updateBatchStatus(batchId, 7, "endDateTime");
		                return false;
		            } else {
		                for (configurationConnection bt : batchTargetList) {
		                    /* populate batchUploadSummary need batchId, transactionInId,  configId, 
		                     * sourceOrgId, messageTypeId - in configurations - missing targetOrgId, 
		                     * if targetOrgCol has value, we populate - cms's target col could be 0, if spec has no target column,
		                     * we insert all connections
		                     * if targetOrgCol has value, we make sure value is valid
		                     */
		                	systemErrorCount = systemErrorCount + insertBatchUploadSummary(batch, bt);
		                    if (sourceConfigId != bt.getsourceConfigId()) {
		                        if (bt.getTargetOrgCol() != 0) {
		                        	systemErrorCount = systemErrorCount + rejectInvalidTargetOrg(batchId, bt);
		                        }
		                        sourceConfigId = bt.getsourceConfigId();
		                    }
		                }
		                systemErrorCount = systemErrorCount + setStatusForErrorCode(batchId, 11, 9, false);
		
		                //reject transactions with config that do not connections
		                systemErrorCount = systemErrorCount + rejectNoConnections(batch);
		                systemErrorCount = systemErrorCount + setStatusForErrorCode(batchId, 11, 10, false);
		
		                systemErrorCount = systemErrorCount + insertBatchTargets(batchId);
		
		                //handle duplicates, need to insert again and let it be its own row
		                systemErrorCount = systemErrorCount + newEntryForMultiTargets(batchId);
		
		            }
                }
                /** end of inserting target **/
                
                
                
                
                
                /**
                 * if there are errors, those are system errors, they will be logged we get errorId 5 and email to admin, update batch to 29 *
                 */
                if (systemErrorCount > 0) {
                    setBatchToError(batchUploadId, "System error occurred during processBatch, please review errors in audit report");
                    return false;
                }
            } //end of configs

            updateTransactionStatus(batchUploadId, transactionId, 10, 12);
            //transactionIn and transactionTarget status should be the same 
            copyTransactionInStatusToTarget(batchUploadId, transactionId);

            /**
             * batches gets process again when user hits release button, maybe have separate method call for those that are just going from pending release to release, have to think about scenario when upload file is huge *
             */
            List<configurationTransport> handlingDetails = getHandlingDetailsByBatch(batchUploadId);
            // multiple config should be set to handle the batch the same way - we email admin if we don't have one way of handling a batch
            if (handlingDetails.size() != 1) {
                //TODO email admin to fix problem
                updateRecordCounts(batchUploadId, new ArrayList<Integer>(), false, "totalRecordCount");
                // do we count pass records as errors?
                updateRecordCounts(batchUploadId, errorStatusIds, false, "errorRecordCount");
                setBatchToError(batchUploadId, "Multiple or no file handling found, please check auto-release and error handling configurations");
                return false;
            }

            if (handlingDetails.size() == 1) {
                /**
                 * 1 = Post errors to ERG 2 = Reject record on error 3 = Reject submission on error 4 = Pass through errors
                 *
                 */

                if (getRecordCounts(batchUploadId, Arrays.asList(11, 12, 13, 16), false, false) > 0 && batch.getstatusId() == 6) {
                    //we stop here as batch is not in final status and release batch was triggered
                    batch.setstatusId(5);
                    batchStausId = 5;
                    updateRecordCounts(batchUploadId, new ArrayList<Integer>(), false, "totalRecordCount");
                    updateRecordCounts(batchUploadId, errorStatusIds, false, "errorRecordCount");
                    updateBatchStatus(batchUploadId, batchStausId, "endDateTime");
                    return true;
                }
                // if auto and batch contains transactions that are not final status
                if (batch.getstatusId() == 6 || (handlingDetails.get(0).getautoRelease()
                        && (handlingDetails.get(0).geterrorHandling() == 2
                        || handlingDetails.get(0).geterrorHandling() == 4
                        || handlingDetails.get(0).geterrorHandling() == 1))) {

                    if (handlingDetails.get(0).getautoRelease() && handlingDetails.get(0).geterrorHandling() == 1
                            && getRecordCounts(batchUploadId, Arrays.asList(11, 12, 13, 16), false, false) > 0) {
                        //post records to ERG
                        batch.setstatusId(5);
                        batchStausId = 5;
                        updateRecordCounts(batchUploadId, new ArrayList<Integer>(), false, "totalRecordCount");
                        updateRecordCounts(batchUploadId, errorStatusIds, false, "errorRecordCount");
                        updateBatchStatus(batchUploadId, batchStausId, "endDateTime");
                        return true;
                    }

                    //run check to make sure we have records 
                    if (getRecordCounts(batchUploadId, Arrays.asList(12), false, true) > 0) {
                        //we insert here
                        if (!insertTransactions(batchUploadId)) {
                            //something went wrong, we removed all inserted entries
                            clearMessageTables(batchUploadId);
                            updateRecordCounts(batchUploadId, new ArrayList<Integer>(), false, "totalRecordCount");
                            // do we count pass records as errors?
                            updateRecordCounts(batchUploadId, errorStatusIds, false, "errorRecordCount");
                            updateBatchStatus(batchUploadId, 29, "endDateTime");
                            return false;
                        }
                    }
                    // all went well
                    if (handlingDetails.get(0).geterrorHandling() == 4) {
                        //update to pass - 16
                        updateTransactionStatus(batchUploadId, transactionId, 14, 16);
                        //auto release, records 
                        updateTransactionTargetStatus(batchUploadId, transactionId, 14, 19);
                        updateTransactionTargetStatus(batchUploadId, transactionId, 16, 19);

                    }
                    if (handlingDetails.get(0).geterrorHandling() == 2) {
                        //reject errors
                        updateTransactionStatus(batchUploadId, transactionId, 14, 13);
                        copyTransactionInStatusToTarget(batchUploadId, transactionId);
                    }
                    updateTransactionStatus(batchUploadId, transactionId, 12, 19);
                    updateTransactionTargetStatus(batchUploadId, transactionId, 12, 19);
                    batchStausId = 24;

                } else if (handlingDetails.get(0).getautoRelease() && handlingDetails.get(0).geterrorHandling() == 3) {
                    //auto-release, 3 = Reject submission on error 
                    batchStausId = 7;
                    //updating entire batch to reject since error transactionIds are in error tables
                    updateTransactionTargetStatus(batchUploadId, transactionId, 14, 13);
                    updateTransactionStatus(batchUploadId, transactionId, 14, 13);

                } else if (!handlingDetails.get(0).getautoRelease() && handlingDetails.get(0).geterrorHandling() == 1) { //manual release
                    //transaction will be set to saved, batch will be set to RP
                    batchStausId = 5;
                    //we leave status alone as we already set them
                } else if (!handlingDetails.get(0).getautoRelease() && handlingDetails.get(0).geterrorHandling() == 2) {
                    //reject records
                    batchStausId = 5;
                    updateTransactionStatus(batchUploadId, transactionId, 14, 13);
                    updateTransactionTargetStatus(batchUploadId, transactionId, 14, 13);
                } else if (!handlingDetails.get(0).getautoRelease() && handlingDetails.get(0).geterrorHandling() == 3) {
                    batchStausId = 7;
                    updateTransactionStatus(batchUploadId, transactionId, 14, 13);
                    updateTransactionTargetStatus(batchUploadId, transactionId, 14, 13);
                } else if (!handlingDetails.get(0).getautoRelease() && handlingDetails.get(0).geterrorHandling() == 4) {
                    batchStausId = 5;
                    // pass
                    updateTransactionStatus(batchUploadId, transactionId, 14, 16);
                    updateTransactionTargetStatus(batchUploadId, transactionId, 14, 16);
                } //end of checking auto/error handling

                updateRecordCounts(batchUploadId, new ArrayList<Integer>(), false, "totalRecordCount");
                // do we count pass records as errors?
                updateRecordCounts(batchUploadId, errorStatusIds, false, "errorRecordCount");
                updateBatchStatus(batchUploadId, batchStausId, "endDateTime");

            } //end of making sure there is one handling details for batch

        } // end of single batch insert 

        return true;
    }

    /**
     * This
     */
    @Override
    public void processBatches() {
        //0. grab all batches with SSL (3) - Loaded or ready for Release SR (6)
        //1. get all batches with SSA
        try {
            List<batchUploads> batches = getBatchesByStatusIds(Arrays.asList(3, 6));
            if (batches != null && batches.size() != 0) {
                //we loop and process
                for (batchUploads batch : batches) {
                    try {
                        processBatch(batch.getId(), false, 0);
                    } catch (Exception ex) {
                        setBatchToError(batch.getId(), ("Errored at processBatches  " + ex.getCause()));
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception ex1) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ("processBatches error - " + ex1));
        }

    }

    @Override
    public void updateBatchStatus(Integer batchUploadId, Integer statusId, String timeField) throws Exception {
        transactionInDAO.updateBatchStatus(batchUploadId, statusId, timeField);
    }

    @Override
    public void updateTransactionStatus(Integer batchUploadId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception {
        transactionInDAO.updateTransactionStatus(batchUploadId, transactionId, fromStatusId, toStatusId);
    }

    @Override
    public void updateTransactionTargetStatus(Integer batchUploadId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception {
        transactionInDAO.updateTransactionTargetStatus(batchUploadId, transactionId, fromStatusId, toStatusId);
    }

    /**
     * provided the batch status is not one of the delivery status (22 SDL, 23 SDC) what would we like clear batch to do 1. Change batch process to SBP to nothing can be touch 2. remove records from message tables 3. figure out what status to set batch *
     */
    @Override
    public boolean clearBatch(Integer batchUploadId) throws Exception {
        boolean canDelete = allowBatchClear(batchUploadId);
        Integer sysError = 0;
        if (canDelete) {
            //TODO how much should we clear? Is it different for ERG and Upload?
            sysError = clearMessageTables(batchUploadId);
            if (sysError == 0) {
                int toBatchStatusId = 3; //SSA
                if (getBatchDetails(batchUploadId).gettransportMethodId() == 2) {
                    sysError = clearTransactionInErrors(batchUploadId, false);
                    toBatchStatusId = 5;
                    resetTransactionTranslatedIn(batchUploadId, true);
                    transactionInDAO.updateTransactionStatus(batchUploadId, 0, 0, 15);
                    transactionInDAO.updateBatchStatus(batchUploadId, toBatchStatusId, "startOver");
                } else {
                    toBatchStatusId = 2;
                    sysError = clearTransactionTables(batchUploadId, false);
                    updateRecordCounts(batchUploadId, new ArrayList<Integer>(), false, "errorRecordCount");
                    updateRecordCounts(batchUploadId, new ArrayList<Integer>(), false, "totalRecordCount");
                }
            }
        }
        if (sysError == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setDoNotProcess(Integer batchId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Integer clearTransactionInRecords(Integer batchUploadId) {
        return transactionInDAO.clearTransactionInRecords(batchUploadId);
    }

    /**
     * This method assumes that all records are validated and ready for insert We loop through each configuration and insert Transaction status will remain unchanged. *
     */
    @Override
    public boolean insertTransactions(Integer batchUploadId) {
        List<Integer> configIds = getConfigIdsForBatch(batchUploadId, true);
        boolean processTransactions = true;

        for (Integer configId : configIds) {

            //blank values are seen as space and will cause errors when insert if field is not use
            List<configurationFormFields> configurationFormFields
                    = configurationtransportmanager.getCffByValidationType(configId, 0);
            for (configurationFormFields cff : configurationFormFields) {
                updateBlanksToNull(cff, batchUploadId);
            }

            /**
             * this list have the insert /check statements for each message table *
             */
            List<ConfigForInsert> configforConfigIds = setConfigForInsert(configId, batchUploadId);
            /**
             * we loop though each table and grab the transactions that has multiple values for that table, we set it to a list *
             */
            for (ConfigForInsert config : configforConfigIds) {
                /**
                 * we grab list of ids with multiple for this config we use the checkDelim string to look for those transactions *
                 */
                List<Integer> transIds = getTransWithMultiValues(config);
                config.setLoopTransIds(transIds);
                /**
                 * we need to check if we need to insert in case the whole table is mapped but doesn't contain values *
                 */
                List<Integer> skipTheseIds = getBlankTransIds(config);
                config.setBlankValueTransId(skipTheseIds);

                //we insert single values
                if (!insertSingleToMessageTables(config)) {
                    return false;
                }

                //we loop through transactions with multiple values and use SP to loop values with delimiters
                for (Integer transId : transIds) {
                    //we check how long field is
                    Integer subStringTotal = countSubString(config, transId);
                    for (int i = 0; i <= subStringTotal; i++) {
                        if (!insertMultiValToMessageTables(config, i + 1, transId)) {
                            return false;
                        }
                    }
                }
            }
        }

        return processTransactions;
    }

    @Override
    public Integer clearTransactionTranslatedIn(Integer batchUploadId) {
        return transactionInDAO.clearTransactionTranslatedIn(batchUploadId);
    }

    @Override
    public Integer clearTransactionTables(Integer batchUploadId, boolean leaveFinalStatusIds) {
        //TODO have in transaction block for roll back?
        //we clear transactionTranslatedIn
        Integer cleared = clearTransactionTranslatedIn(batchUploadId);
        //we clear transactionInRecords
        cleared = cleared + clearTransactionInRecords(batchUploadId);
        //clear batchDownloadSummary
        cleared = cleared + clearBatchDownloadSummaryByUploadBatchId(batchUploadId);
        //clear transactionoutrecords
        cleared = cleared + clearTransactionOutRecordsByUploadBatchId(batchUploadId);
        //clear tto
        cleared = cleared + clearTransactionTranslatedOutByUploadBatchId(batchUploadId);
        //we clear transactionTarget
        cleared = cleared + clearTransactionTarget(batchUploadId);
        cleared = cleared + clearTransactionInErrors(batchUploadId, leaveFinalStatusIds);
        cleared = cleared + clearBatchUploadSummary(batchUploadId);
        cleared = cleared + clearMessageTables(batchUploadId);
        //we clear transactionIn
        cleared = cleared + clearTransactionIn(batchUploadId);

        if (cleared > 0) {
            flagAndEmailAdmin(batchUploadId);
        }
        return cleared;
    }

    @Override
    public Integer clearTransactionTarget(Integer batchUploadId) {
        return transactionInDAO.clearTransactionTarget(batchUploadId);
    }

    @Override
    public Integer clearTransactionIn(Integer batchUploadId) {
        return transactionInDAO.clearTransactionIn(batchUploadId);
    }

    @Override
    public void flagAndEmailAdmin(Integer batchUploadId) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<configurationFormFields> getRequiredFieldsForConfig(Integer configId) {
        return configurationtransportmanager.getRequiredFieldsForConfig(configId);
    }

    @Override
    public Integer insertFailedRequiredFields(configurationFormFields cff, Integer batchUploadId, Integer transactionInId) {
        return transactionInDAO.insertFailedRequiredFields(cff, batchUploadId, transactionInId);
    }

    @Override
    public Integer clearTransactionInErrors(Integer batchUploadId, boolean leaveFinalStatusIds) {
        return transactionInDAO.clearTransactionInErrors(batchUploadId, leaveFinalStatusIds);
    }

    @Override
    public Integer deleteTransactionInErrorsByTransactionId(Integer transactionInId) {
        return transactionInDAO.deleteTransactionInErrorsByTransactionId(transactionInId);
    }

    /**
     * This method finds all error transactionInId in TransactionInErrors / TransactionOutErrors and update transactionIn with the appropriate error status It can be passed, reject and error
     *
     */
    @Override
    public void updateStatusForErrorTrans(Integer batchId,
            Integer statusId, boolean foroutboundProcessing, Integer transactionId) {
        transactionInDAO.updateStatusForErrorTrans(batchId, statusId, foroutboundProcessing, transactionId);
    }

    @Override
    public Integer runValidations(Integer batchUploadId, Integer configId, Integer transactionId) {
        Integer errorCount = 0;
        //1. we get validation types
        //2. we skip 1 as that is not necessary
        //3. we skip date (4) as there is no isDate function in MySQL
        //4. we skip the ids that are not null as Mysql will bomb out checking character placement
        //5. back to date, we grab transaction info and we loop (errId 7)

        /**
         * MySql RegEXP validate numeric - ^-?[0-9]+[.]?[0-9]*$|^-?[.][0-9]+$ validate email - ^[a-z0-9\._%+!$&*=^|~#%\'`?{}/\-]+@[a-z0-9\.-]+\.[a-z]{2,6}$ or ^[A-Z0-9._%-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$ validate url - ^(https?://)?([\da-z.-]+).([a-z0-9])([0-9a-z]*)*[/]?$ - need to fix not correct - might have to run in java as mysql is not catching all. validate phone - should be no longer than 11 digits ^[0-9]{7,11}$ validate date - doing this in java
         *
         */
        //TODO was hoping to have one SP but concat in SP not setting and not catching errors correctly. Need to recheck
        List<configurationFormFields> configurationFormFields
                = configurationtransportmanager.getCffByValidationType(configId, 0);

        for (configurationFormFields cff : configurationFormFields) {
            String regEx = "";
            Integer validationTypeId = cff.getValidationType();
            switch (cff.getValidationType()) {
                case 1:
                    break; // no validation
                //email calling SQL to validation and insert - one statement
                case 2:
                    errorCount = errorCount + genericValidation(cff, validationTypeId, batchUploadId, regEx, transactionId);
                    break;
                //phone  calling SP to validation and insert - one statement 
                case 3:
                    errorCount = errorCount + genericValidation(cff, validationTypeId, batchUploadId, regEx, transactionId);
                    break;
                // need to loop through each record / each field
                case 4:
                    errorCount = errorCount + dateValidation(cff, validationTypeId, batchUploadId, transactionId);
                    break;
                //numeric   calling SQL to validation and insert - one statement      
                case 5:
                    errorCount = errorCount + genericValidation(cff, validationTypeId, batchUploadId, regEx, transactionId);
                    break;
                //url - need to rethink as regExp is not validating correctly
                case 6:
                    errorCount = errorCount + urlValidation(cff, validationTypeId, batchUploadId, transactionId);
                    break;
                //anything new we hope to only have to modify sp
                default:
                    errorCount = errorCount + genericValidation(cff, validationTypeId, batchUploadId, regEx, transactionId);
                    break;
            }

        }
        return errorCount;
    }

    @Override
    public Integer genericValidation(configurationFormFields cff,
            Integer validationTypeId, Integer batchUploadId, String regEx, Integer transactionId) {
        return transactionInDAO.genericValidation(cff, validationTypeId, batchUploadId, regEx, transactionId);
    }

    @Override
    public Integer urlValidation(configurationFormFields cff,
            Integer validationTypeId, Integer batchUploadId, Integer transactionId) {
        try {
            //1. we grab all transactionInIds for messages that are not length of 0 and not null 
            List<transactionRecords> trs = null;
            //1. we grab all transactionInIds for messages that are not length of 0 and not null 
            if (transactionId == 0) {
                trs = getFieldColAndValues(batchUploadId, cff);
            } else {
                trs = getFieldColAndValueByTransactionId(cff, transactionId);
            }
            //2. we look at each column and check each value to make sure it is a valid url
            for (transactionRecords tr : trs) {
                //System.out.println(tr.getfieldValue());
                if (tr.getfieldValue() != null) {
                    //we append http:// if url doesn't start with it
                    String urlToValidate = tr.getfieldValue();
                    if (!urlToValidate.startsWith("http")) {
                        urlToValidate = "http://" + urlToValidate;
                    }
                    if (!isValidURL(urlToValidate)) {
                        insertValidationError(tr, cff, batchUploadId);
                    }

                }
            }
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            insertProcessingError(processingSysErrorId, cff.getconfigId(), batchUploadId, cff.getId(), null, null, validationTypeId, false, false, (ex.getClass() + " " + ex.getCause()));
            return 1;
        }

    }

    @Override
    public Integer dateValidation(configurationFormFields cff,
            Integer validationTypeId, Integer batchUploadId, Integer transactionId) {
        try {
            List<transactionRecords> trs = null;
            //1. we grab all transactionInIds for messages that are not length of 0 and not null 
            if (transactionId == 0) {
                trs = getFieldColAndValues(batchUploadId, cff);
            } else {
                trs = getFieldColAndValueByTransactionId(cff, transactionId);
            }
            //2. we look at each column and check each value by trying to convert it to a date
            for (transactionRecords tr : trs) {
                if (tr.getfieldValue() != null) {
                    //sql is picking up dates in mysql format and it is not massive inserting, running this check to avoid unnecessary sql call
                    //System.out.println(tr.getFieldValue());
                    //we check long dates
                    Date dateValue = null;
                    boolean mySQLDate = chkMySQLDate(tr.getFieldValue());

                    if (dateValue == null && !mySQLDate) {
                        dateValue = convertLongDate(tr.getFieldValue());
                    }
                    if (dateValue == null && !mySQLDate) {
                        dateValue = convertDate(tr.getfieldValue());
                    }

                    String formattedDate = null;
                    if (dateValue != null && !mySQLDate) {
                        formattedDate = formatDateForDB(dateValue);
                        //3. if it converts, we update the column value
                        updateFieldValue(tr, formattedDate);
                    }

                    if (formattedDate == null && !mySQLDate) {
                        insertValidationError(tr, cff, batchUploadId);
                    }

                }
            }
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            insertProcessingError(processingSysErrorId, cff.getconfigId(), batchUploadId, cff.getId(), null, null, validationTypeId, false, false, (ex.getClass() + " " + ex.getCause()));
            return 1;
        }

    }

    /**
     * This method updates all the length of 0 values for a particular column for a batch and configuration to null.
     *
     */
    @Override
    public void updateBlanksToNull(configurationFormFields cff, Integer batchUploadId) {
        transactionInDAO.updateBlanksToNull(cff, batchUploadId);
    }

    @Override
    public List<transactionRecords> getFieldColAndValues(Integer batchUploadId, configurationFormFields cff) {
        return transactionInDAO.getFieldColAndValues(batchUploadId, cff);
    }

    /**
     * this method checks the potential day formats that a user can send in. We will check for long days such as February 2, 2014 Wednesday etc. Only accepting US format of month - day - year February 2, 2014 Sunday 2:00:02 PM February 2, 2014 02-02-2014 02/02/2014 02/02/14 02/2/14 12:02:00 PM etc.
     */
    /**
     * this method returns the pattern date is in so we can convert it properly and translate into mysql datetime insert format
     */
    public Date convertDate(String input) {

        // some regular expression
        String time = "(\\s(([01]?\\d)|(2[0123]))[:](([012345]\\d)|(60))"
                + "[:](([012345]\\d)|(60)))?"; // with a space before, zero or one time

        // no check for leap years (Schaltjahr)
        // and 31.02.2006 will also be correct
        String day = "(([12]\\d)|(3[01])|(0?[1-9]))"; // 01 up to 31
        String month = "((1[012])|(0\\d))"; // 01 up to 12
        String year = "\\d{4}";

        // define here all date format
        String date = input.replace("/", "-");
        date = date.replace(".", "-");
        //ArrayList<Pattern> patterns = new ArrayList<Pattern>();
        //Pattern pattern1 = Pattern.compile(day + "-" + month + "-" + year + time); //not matching, doesn't work for 01-02-2014 is it jan or feb, will only accept us dates
        Pattern pattern2 = Pattern.compile(year + "-" + month + "-" + day + time);
        Pattern pattern3 = Pattern.compile(month + "-" + day + "-" + year + time);
        // check dates
        //month needs to have leading 0
        if (date.indexOf("-") == 1) {
            date = "0" + date;
        }

        if (pattern2.matcher(date).matches()) {
            //we have y-m-d format, we transform and return date
            try {
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd");
                dateformat.setLenient(false);
                Date dateValue = dateformat.parse(date);
                return dateValue;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        } else if (pattern3.matcher(date).matches()) {
            //we have m-d-y format, we transform and return date
            try {
                SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd-yyyy");
                dateformat.setLenient(false);
                Date dateValue = dateformat.parse(date);
                return dateValue;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        } else {
            return null;
        }

    }

    @Override
    public void updateFieldValue(transactionRecords tr, String newValue) {
        transactionInDAO.updateFieldValue(tr, newValue);
    }

    @Override
    public void insertValidationError(transactionRecords tr,
            configurationFormFields cff, Integer batchUploadId) {
        transactionInDAO.insertValidationError(tr, cff, batchUploadId);
    }

    @Override
    @Transactional
    public Integer getFeedbackReportConnection(int configId, int targetorgId) {
        return transactionInDAO.getFeedbackReportConnection(configId, targetorgId);
    }

    @Override
    public String formatDateForDB(Date date) {
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            return dateformat.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Date convertLongDate(String dateValue) {

        Date date = null;
        //this checks convert long date such February 2, 2014
        try {
            date = java.text.DateFormat.getDateInstance().parse(dateValue);
        } catch (Exception e) {
        }
        return date;
    }

    public boolean chkMySQLDate(String date) {

        // some regular expression
        String time = "(\\s(([01]?\\d)|(2[0123]))[:](([012345]\\d)|(60))"
                + "[:](([012345]\\d)|(60)))?"; // with a space before, zero or one time

        // no check for leap years (Schaltjahr)
        // and 31.02.2006 will also be correct
        String day = "(([12]\\d)|(3[01])|(0?[1-9]))"; // 01 up to 31
        String month = "((1[012])|(0\\d))"; // 01 up to 12
        String year = "\\d{4}";

        // define here all date format
        date.replace("/", "-");
        date.replace(".", "-");
        Pattern pattern = Pattern.compile(year + "-" + month + "-" + day + time);

        // check dates
        if (pattern.matcher(date).matches()) {
            try {
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                dateformat.setLenient(false);
                dateformat.parse(date);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean isValidURL(String url) {
        UrlValidator urlValidator = new UrlValidator();
        if (urlValidator.isValid(url)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer processCrosswalk(Integer configId, Integer batchId,
            configurationDataTranslations cdt, boolean foroutboundProcessing, Integer transactionId) {
        try {
            // 1. we get the info for that cw (fieldNo, sourceVal, targetVal rel_crosswalkData)
            List<CrosswalkData> cdList = configurationManager.getCrosswalkData(cdt.getCrosswalkId());
            //we null forcw column, we translate and insert there, we then replace
            nullForCWCol(configId, batchId, foroutboundProcessing, transactionId);
            for (CrosswalkData cwd : cdList) {
                executeSingleValueCWData(configId, batchId, cdt.getFieldNo(), cwd, foroutboundProcessing, cdt.getFieldId(), transactionId);
            }
            if (cdt.getPassClear() == 1) {
            	//flag errors, anything row that is not null in F[FieldNo] but null in forCW
            	flagCWErrors(configId, batchId, cdt, foroutboundProcessing, transactionId);
            	//flag as error in transactionIn or transactionOut table
                updateStatusForErrorTrans(batchId, 14, foroutboundProcessing, transactionId);

            } 
            //we replace original F[FieldNo] column with data in forcw
            updateFieldNoWithCWData(configId, batchId, cdt.getFieldNo(), cdt.getPassClear(), foroutboundProcessing, transactionId);

            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

    }

    @Override
    public Integer processMacro(Integer configId, Integer batchId, configurationDataTranslations cdt,
            boolean foroutboundProcessing, Integer transactionId) {
        // we clear forCW column for before we begin any translation
        nullForCWCol(configId, batchId, foroutboundProcessing, transactionId);
        try {
            Macros macro = configurationManager.getMacroById(cdt.getMacroId());
            int sysError = 0;
            try {
                // we expect the target field back so we can figure out clear pass option
                sysError = sysError + executeMacro(configId, batchId, cdt, foroutboundProcessing, macro, transactionId);
                // insert macro errors
                flagMacroErrors(configId, batchId, cdt, foroutboundProcessing, transactionId);
                return sysError;
            } catch (Exception e) {
                e.printStackTrace();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

    }

    @Override
    public void nullForCWCol(Integer configId, Integer batchId, boolean foroutboundProcessing, Integer transactionId) {
        transactionInDAO.nullForCWCol(configId, batchId, foroutboundProcessing, transactionId);
    }

    @Override
    public void executeSingleValueCWData(Integer configId, Integer batchId, Integer fieldNo, CrosswalkData cwd, boolean foroutboundProcessing, Integer fieldId, Integer transactionId) {
        transactionInDAO.executeSingleValueCWData(configId, batchId, fieldNo, cwd, foroutboundProcessing, fieldId, transactionId);
    }

    @Override
    public void updateFieldNoWithCWData(Integer configId, Integer batchId, Integer fieldNo, Integer passClear, boolean foroutboundProcessing, Integer transactionId) {
        transactionInDAO.updateFieldNoWithCWData(configId, batchId, fieldNo, passClear, foroutboundProcessing, transactionId);
    }

    @Override
    public void flagCWErrors(Integer configId, Integer batchId,
            configurationDataTranslations cdt, boolean foroutboundProcessing, Integer transactionId) {
        transactionInDAO.flagCWErrors(configId, batchId, cdt, foroutboundProcessing, transactionId);
    }

    @Override
    public void flagMacroErrors(Integer configId, Integer batchId,
            configurationDataTranslations cdt, boolean foroutboundProcessing, Integer transactionId) {
        transactionInDAO.flagMacroErrors(configId, batchId, cdt, foroutboundProcessing, transactionId);
    }

    @Override
    public Integer executeMacro(Integer configId, Integer batchId,
            configurationDataTranslations cdt, boolean foroutboundProcessing, Macros macro, Integer transactionId) {
        return transactionInDAO.executeMacro(configId, batchId, cdt, foroutboundProcessing, macro, transactionId);

    }

    @Override
    public List<configurationTransport> getHandlingDetailsByBatch(int batchId) {
        try {
            return transactionInDAO.getHandlingDetailsByBatch(batchId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void insertProcessingError(Integer errorId, Integer configId, Integer batchId, Integer fieldId,
            Integer macroId, Integer cwId, Integer validationTypeId, boolean required,
            boolean foroutboundProcessing, String errorCause) {
        insertProcessingError(errorId, configId, batchId, fieldId, macroId, cwId, validationTypeId, required, foroutboundProcessing, errorCause, null);

    }

    @Override
    public void insertProcessingError(Integer errorId, Integer configId, Integer batchId, Integer fieldId,
            Integer macroId, Integer cwId, Integer validationTypeId, boolean required,
            boolean foroutboundProcessing, String errorCause, Integer transactionId) {
        transactionInDAO.insertProcessingError(errorId, configId, batchId, fieldId, macroId, cwId, validationTypeId, required, foroutboundProcessing, errorCause, transactionId);

    }

    @Override
    public void updateRecordCounts(Integer batchId, List<Integer> statusIds,
            boolean foroutboundProcessing, String colNameToUpdate) {
        transactionInDAO.updateRecordCounts(batchId, statusIds, foroutboundProcessing, colNameToUpdate);
    }

    @Override
    public Integer getRecordCounts(Integer batchId, List<Integer> statusIds, boolean foroutboundProcessing) {
        return transactionInDAO.getRecordCounts(batchId, statusIds, foroutboundProcessing, true);
    }

    @Override
    public Integer getRecordCounts(Integer batchId, List<Integer> statusIds, boolean foroutboundProcessing, boolean inStatusIds) {
        return transactionInDAO.getRecordCounts(batchId, statusIds, foroutboundProcessing, inStatusIds);
    }

    @Override
    public void resetTransactionTranslatedIn(Integer batchId, boolean resetAll) {
        resetTransactionTranslatedIn(batchId, resetAll, 0);
    }

    @Override
    public void resetTransactionTranslatedIn(Integer batchId, boolean resetAll, Integer transactionInId) {
        transactionInDAO.resetTransactionTranslatedIn(batchId, resetAll, transactionInId);
    }

    @Override
    public Integer copyTransactionInStatusToTarget(Integer batchId, Integer transactionId) {
        return transactionInDAO.copyTransactionInStatusToTarget(batchId, transactionId);
    }

    @Override
    public Integer insertLoadData(Integer batchId, String delimChar, String fileWithPath, String loadTableName, boolean containsHeaderRow) {
        return transactionInDAO.insertLoadData(batchId, delimChar, fileWithPath, loadTableName, containsHeaderRow);
    }

    @Override
    public Integer createLoadTable(String loadTableName) {
        return transactionInDAO.createLoadTable(loadTableName);
    }

    @Override
    public Integer dropLoadTable(String loadTableName) {
        return transactionInDAO.dropLoadTable(loadTableName);
    }

    @Override
    public Integer updateLoadTable(String loadTableName, Integer batchId) {
        return transactionInDAO.updateLoadTable(loadTableName, batchId);
    }

    @Override
    public Integer loadTransactionIn(String loadTableName, Integer batchId) {
        return transactionInDAO.loadTransactionIn(loadTableName, batchId);
    }

    @Override
    public Integer loadTransactionInRecords(Integer batchId) {
        return transactionInDAO.loadTransactionInRecords(batchId);
    }

    @Override
    public Integer loadTransactionInRecordsData(String loadTableName) {
        return transactionInDAO.loadTransactionInRecordsData(loadTableName);
    }

    @Override
    public Integer updateConfigIdForBatch(Integer batchId, Integer configId) {
        return transactionInDAO.updateConfigIdForBatch(batchId, configId);
    }

    @Override
    public Integer loadTransactionTranslatedIn(Integer batchId) {
        return transactionInDAO.loadTransactionTranslatedIn(batchId);
    }

    @Override
    public Integer insertBatchUploadSummary(batchUploads batchUpload, configurationConnection batchTargets) {
        int sysErrors = 0;
        //1. if no targetCol is given, we mass insert
        if (batchTargets.getTargetOrgCol() == 0) {
            sysErrors = sysErrors + transactionInDAO.insertBatchUploadSummaryAll(batchUpload, batchTargets);
        } else {
            sysErrors = sysErrors + insertBatchUploadSumByOrg(batchUpload, batchTargets);
        }
        return sysErrors;
    }

    @Override
    public Integer insertBatchTargets(Integer batchId) {
        return transactionInDAO.insertBatchTargets(batchId);
    }

    @Override
    public List<configurationConnection> getBatchTargets(Integer batchId, boolean active) {
        return transactionInDAO.getBatchTargets(batchId, active);
    }

    @Override
    public Integer clearBatchUploadSummary(Integer batchId) {
        return transactionInDAO.clearBatchUploadSummary(batchId);
    }

    /**
     * this method is called by scheduler. It will take all batches with status of SSA and load them. Batch will end with a status of LOADED.
     */
    @Override
    public void loadBatches() {
        //1. get all batches with SSA
        List<batchUploads> batches = getBatchesByStatusIds(Arrays.asList(2));
        if (batches != null && batches.size() != 0) {
            //we loop and process
            for (batchUploads batch : batches) {
                loadBatch(batch.getId());
            }

        }
    }

    @Override
    public boolean loadBatch(Integer batchId) {
        Integer batchStatusId = 4;
        List<Integer> errorStatusIds = Arrays.asList(11, 13, 14, 16);
        String processFolderPath = "/bowlink/loadFiles/";
        try {
            //first thing we do is get details, then we set it to  4
            batchUploads batch = getBatchDetails(batchId);
            // set batch to SBP - 4
            updateBatchStatus(batchId, batchStatusId, "startDateTime");

            // let's clear all tables first as we are starting over
            Integer sysErrors = clearTransactionTables(batchId, false);
            String errorMessage = "Load errors, please contact admin to review logs";
            // loading batch will take it all the way to loaded (9) status for
            if (sysErrors > 0) {
                insertProcessingError(5, null, batchId, null, null, null, null, false, false, "Error cleaning out transaction tables.  Batch cannot be processed.");
                updateBatchStatus(batchId, 29, "endDateTime");
                return false;
            }

            String loadTableName = "uploadTable_" + batch.getId();
            //make sure old table is dropped if exists
            Integer sysError = dropLoadTable(loadTableName);
            sysError = sysError + createLoadTable(loadTableName);

            //we need to index loadTable
            sysError = sysError + indexLoadTable(loadTableName);

            fileSystem dir = new fileSystem();
            dir.setDirByName("/");

            //2. we load data with my sql
            String actualFileName = null;
            String newfilename = null;
            
            /** decoded files will always be in loadFiles folder with UTBatchName **/
            // all files are Base64 encoded at this point
            String encodedFilePath = dir.setPath(batch.getFileLocation());
            String encodedFileName = batch.getoriginalFileName();
            File encodedFile = new File (encodedFilePath + encodedFileName);
            String decodedFilePath = dir.setPath(processFolderPath);
            String decodedFileName = batch.getutBatchName();
            String decodedFileExt = batch.getoriginalFileName().substring(batch.getoriginalFileName().lastIndexOf("."));
            String strDecode = "";           
            try {
            	strDecode = filemanager.decodeFileToBase64Binary(encodedFile);
            } catch (Exception ex) {
            	ex.printStackTrace();
            	strDecode = "";
            	sysErrors = 1;
            	processingSysErrorId = 17;
            }
            
            if (!strDecode.equalsIgnoreCase("")) {
	            	//we write and decode file
	            filemanager.writeFile((decodedFilePath + decodedFileName + decodedFileExt) , strDecode);
	            actualFileName = (decodedFilePath + decodedFileName + decodedFileExt);
	            /*
	                If batch is set up for CCD input then we need to translate it
	                to a pipe-delimited text file.
	            */
	            if (batch.getoriginalFileName().endsWith(".xml")) {
	                newfilename = ccdtotxt.TranslateCCDtoTxt(decodedFilePath, decodedFileName, batch.getOrgId());
	                actualFileName = newfilename;
	            /* 
	             if the original file name is a HL7 file (".hr") then we are going to translate it to
	             a pipe-delimited text file.
	             */
	             } else if (batch.getoriginalFileName().endsWith(".hr")) {
	                newfilename = hl7toTxt.TranslateHl7toTxt(decodedFilePath, decodedFileName); 
	                actualFileName = newfilename;
	            }  
	                
	            //at this point, hl7 and hr are in unencoded plain text
	            if (actualFileName.endsWith(".txt") || actualFileName.endsWith(".csv")) {
	            	sysError = sysError + insertLoadData(batch.getId(), batch.getDelimChar(), actualFileName, loadTableName, batch.isContainsHeaderRow());
	            	//we delete the loadFile here as it is not encrypted
	                File actualFile = new File(actualFileName);
	            	actualFile.delete();
	            }
	            
	            
	            //3. we update batchId, loadRecordId
	            sysError = sysError + updateLoadTable(loadTableName, batch.getId());
	
	            // 4. we insert into transactionIn - status of invalid (11), batchId, loadRecordId
	            sysError = sysError + loadTransactionIn(loadTableName, batch.getId());
	
	            //5. we insert into transactionInRecords - we select transactionIn batchId, transactionInId
	            sysError = sysError + loadTransactionInRecords(batch.getId());
	
	            //6. we match loadRecordId and update transactionInRecords's F1-F255 data
	            sysError = sysError + loadTransactionInRecordsData(loadTableName);
	
	            //7. we delete loadTable
	            sysError = sysError + dropLoadTable(loadTableName);
	
	            //8. we see how if the file only has one upload type so we don't need to parse every line
	            // if we only have one, we update the entire table 
	            if (batch.getConfigId() != null && batch.getConfigId() != 0) {
	                // we update entire transactionIN with configId
	                sysError = sysError + updateConfigIdForBatch(batch.getId(), batch.getConfigId());
	            } else {
	                //1. we get all configs for user - user might not have permission to submit but someone else in org does
	
	                List<configurationMessageSpecs> configurationMessageSpecs = configurationtransportmanager.getConfigurationMessageSpecsForOrgTransport(batch.getOrgId(), batch.gettransportMethodId(), false);
	                //2. we get all rows for batch
	                List<transactionInRecords> tInRecords = getTransactionInRecordsForBatch(batch.getId());
	                if (tInRecords == null || tInRecords.size() == 0) {
	                    updateBatchStatus(batchId, 7, "endDateTime");
	                    insertProcessingError(7, null, batchId, null, null, null, null,
	                            false, false, "No valid transactions were found for batch.");
	                    return false;
	                }
	                if (configurationMessageSpecs == null || configurationMessageSpecs.size() == 0) {
	                    insertProcessingError(6, null, batchId, null, null, null, null,
	                            false, false, "No valid configurations were found for loading batch.");
	                    // update all transactions to invalid
	                    updateBatchStatus(batchId, 7, "endDateTime");
	                    updateTransactionStatus(batchId, 0, 0, 11);
	                    return false;
	                }
	
	                //3 loop through each config and mass update by config
	                for (configurationMessageSpecs cms : configurationMessageSpecs) {
	                    //we update by config
	                    if (updateConfigIdForCMS(batchId, cms) != 0) {
	                        sysError++;
	                        insertProcessingError(processingSysErrorId, null, batch.getId(), null, null, null, null,
	                                false, false, "System error while checking configuration");
	                        //system error - break
	                        break;
	                    }
	                }
	
	                // now we looped through config, we flag the invalid records.
	                sysError = flagInvalidConfig(batchId);
	                //we also need to flag and error the ones that a user is not supposed to upload for
	                sysError = flagNoPermissionConfig(batch);
	            }
	
	            //we populate transactionTranslatedIn
	            sysError = sysError + loadTransactionTranslatedIn(batchId);
	
	            //update data in transactionTranslatedIn
	            resetTransactionTranslatedIn(batchId, true);
      
	            //now that we have our config, we will apply pre-processing cw and macros to manipulate our data
	            //1. find all configs for batch, loop and process
	            int transactionId = 0;
                List<Integer> configIds = getConfigIdsForBatch(batchId, false, transactionId);
                for (Integer configId : configIds) {
					//we need to run all checks before insert regardless *
	            	/** we are reordering 1. cw/macro, 2. required and 3. validate **/
	            	// 1. grab the configurationDataTranslations and run cw/macros
	            	List<configurationDataTranslations> dataTranslations = configurationManager
	                        .getDataTranslationsWithFieldNo(configId, 1); //pre processing
	                for (configurationDataTranslations cdt : dataTranslations) {
	                    if (cdt.getCrosswalkId() != 0) {
	                    	sysError = sysError + processCrosswalk(configId, batchId, cdt, false, transactionId);
	                    } else if (cdt.getMacroId() != 0) {
	                    	sysError = sysError + processMacro(configId, batchId, cdt, false, transactionId);
	                    }
	                }
	            
                }	            
            }
            if (sysErrors > 0) {
                insertProcessingError(processingSysErrorId, null, batchId, null, null, null, null, false, false, errorMessage);
                updateBatchStatus(batchId, 29, "endDateTime");
                return false;
            }

            //we check handling here for rejecting entire batch
            List<configurationTransport> batchHandling = getHandlingDetailsByBatch(batchId);
            // if entire batch failed and have no configIds, there will be no error handling found
            if (getRecordCounts(batchId, Arrays.asList(11), false) == getRecordCounts(batchId, new ArrayList<Integer>(), false)) {
                //entire batch failed, we reject entire batch
                updateRecordCounts(batchId, errorStatusIds, false, "errorRecordCount");
                updateRecordCounts(batchId, new ArrayList<Integer>(), false, "totalRecordCount");
                updateBatchStatus(batchId, 7, "endDateTime");
                return false;
            } else if (batchHandling.size() != 1) {
                //TODO email admin to fix problem
                insertProcessingError(8, null, batchId, null, null, null, null, false, false, "Multiple or no file handling found, please check auto-release and error handling configurations");
                updateRecordCounts(batchId, new ArrayList<Integer>(), false, "totalRecordCount");
                // do we count pass records as errors?
                updateRecordCounts(batchId, errorStatusIds, false, "errorRecordCount");
                updateBatchStatus(batchId, 29, "endDateTime");
                return false;
            }
            if (batchHandling.size() == 1) {
                //reject submission on error
                if (batchHandling.get(0).geterrorHandling() == 3) {
                    // at this point we will only have invalid records
                    if (getRecordCounts(batchId, errorStatusIds, false) > 0) {
                        updateBatchStatus(batchId, 7, "endDateTime");
                        updateRecordCounts(batchId, errorStatusIds, false, "errorRecordCount");
                        //update loaded to rejected
                        updateTransactionStatus(batchId, 0, 9, 13);
                        return false;
                    }
                }
            }

            updateRecordCounts(batchId, errorStatusIds, false, "errorRecordCount");
            updateRecordCounts(batchId, new ArrayList<Integer>(), false, "totalRecordCount");
            //at the end of loaded, we update to PR
            updateTransactionStatus(batchId, 0, 9, 10);
            updateTransactionTargetStatus(batchId, 0, 9, 10);
            batchStatusId = 3;

        } catch (Exception ex) {
            insertProcessingError(processingSysErrorId, null, batchId, null, null, null, null, false, false, ("loadBatch error " + ex.getLocalizedMessage()));
            batchStatusId = 29;
        }

        try {
            updateBatchStatus(batchId, batchStatusId, "endDateTime");
            updateRecordCounts(batchId, new ArrayList<Integer>(), false, "totalRecordCount");
            // do we count pass records as errors?
            updateRecordCounts(batchId, errorStatusIds, false, "errorRecordCount");
        } catch (Exception ex1) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ("loadBatch error at updating batch status - " + ex1));
            return false;
        }

        return true;
    }

    @Override
    public List<batchUploads> getBatchesByStatusIds(List<Integer> statusIds) {
        return transactionInDAO.getBatchesByStatusIds(statusIds);
    }

    @Override
    public void setBatchToError(Integer batchId, String errorMessage) throws Exception {
        try {
            //TODO send email here
            insertProcessingError(processingSysErrorId, null, batchId, null, null, null, null,
                    false, false, errorMessage);
            updateBatchStatus(batchId, 29, "endDateTime");
        } catch (Exception ex1) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ("loadBatch error at updating batch status - " + ex1));

        }

    }

    @Override
    public void deleteMessage(int batchId, int transactionId) throws Exception {
        transactionInDAO.deleteMessage(batchId, transactionId);
    }

    @Override
    public void cancelMessageTransaction(int transactionId) throws Exception {
        transactionInDAO.cancelMessageTransaction(transactionId);
    }

    @Override
    public List<transactionInRecords> getTransactionInRecordsForBatch(Integer batchId) {
        return transactionInDAO.getTransactionInRecordsForBatch(batchId);
    }

    @Override
    public Integer updateConfigIdForCMS(Integer batchId, configurationMessageSpecs cms) {
        return transactionInDAO.updateConfigIdForCMS(batchId, cms);
    }

    @Override
    public Integer flagInvalidConfig(Integer batchId) {
        Integer sysErrors = 0;
        try {
            sysErrors = insertInvalidConfigError(batchId);
            sysErrors = sysErrors + updateInvalidConfigStatus(batchId);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("flagInvalidConfig " + ex.getCause());
            sysErrors++;
        }
        return sysErrors;
    }

    @Override
    public Integer insertInvalidConfigError(Integer batchId) {
        return transactionInDAO.insertInvalidConfigError(batchId);
    }

    @Override
    public Integer updateInvalidConfigStatus(Integer batchId) {
        return transactionInDAO.updateInvalidConfigStatus(batchId);
    }

    @Override
    public Integer indexLoadTable(String loadTableName) {
        return transactionInDAO.indexLoadTable(loadTableName);
    }

    @Override
    @Transactional
    public batchUploadSummary getUploadSummaryDetails(int transactionInId) {
        return transactionInDAO.getUploadSummaryDetails(transactionInId);
    }

    @Override
    public Integer clearBatchDownloadSummaryByUploadBatchId(Integer batchId) {
        return transactionInDAO.clearBatchDownloadSummaryByUploadBatchId(batchId);
    }

    @Override
    public Integer clearTransactionOutRecordsByUploadBatchId(Integer batchId) {
        return transactionInDAO.clearTransactionOutRecordsByUploadBatchId(batchId);
    }

    @Override
    public Integer clearTransactionTranslatedOutByUploadBatchId(Integer batchId) {
        return transactionInDAO.clearTransactionTranslatedOutByUploadBatchId(batchId);
    }

    @Override
    public Integer rejectInvalidTargetOrg(Integer batchId, configurationConnection batchTargets) {
        return transactionInDAO.rejectInvalidTargetOrg(batchId, batchTargets);
    }

    @Override
    public Integer insertBatchUploadSumByOrg(batchUploads batchUpload, configurationConnection batchTargets) {
        return transactionInDAO.insertBatchUploadSumByOrg(batchUpload, batchTargets);
    }

    @Override
    public Integer setStatusForErrorCode(Integer batchId, Integer statusId,
            Integer errorId, boolean foroutboundProcessing) {
        return transactionInDAO.setStatusForErrorCode(batchId, statusId, errorId, foroutboundProcessing);
    }

    @Override
    public Integer rejectNoConnections(batchUploads batch) {
        return transactionInDAO.rejectNoConnections(batch);
    }

    /**
     * we need to have one transactionIn entry for each transaction/target pair - there is no easy way to do it except to insert each duplicate one by one
     */
    @Override
    public Integer newEntryForMultiTargets(Integer batchId) {
        Integer sysError = 0;
        try {
            //1. we get duplicated transactionInIds
            List<Integer> transactionInIds = getDuplicatedIds(batchId);
            for (Integer transactionInId : transactionInIds) {
                //2. we get BATCHUPLOADSUMMARY
                List<batchUploadSummary> buses = getBatchUploadSummary(transactionInId);
                for (batchUploadSummary bus : buses) {
                    //we take each target and insert a new transaction into the transactionIn table
                    sysError = sysError + insertTransactionInByTargetId(bus);
                    //we get new tInId
                    Integer newTInId = getTransactionInIdByTargetId(bus);
                    //with new id, we update transactiontarget, batchUploadSummary
                    sysError = sysError + updateTInIdForTransactiontarget(bus, newTInId);
                    sysError = sysError + updateTINIDForBatchUploadSummary(bus, newTInId);
                    //we insert new entry into transactionInRecords and transactionTranslated In
                    sysError = sysError + copyTransactionInRecord(newTInId, bus.gettransactionInId());
                    sysError = sysError + insertTransactionTranslated(newTInId, bus);
                    sysError = sysError + insertTransactionInError(newTInId, bus.gettransactionInId());
                    //dup errors
                }

            }

            return sysError;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("newEntryForMultiTargets " + ex.getCause());
            return 1;
        }
    }

    @Override
    public List<Integer> getDuplicatedIds(Integer batchId) {
        return transactionInDAO.getDuplicatedIds(batchId);
    }

    @Override
    public List<batchUploadSummary> getBatchUploadSummary(Integer transactionInId) {
        return transactionInDAO.getBatchUploadSummary(transactionInId);
    }

    @Override
    public Integer insertTransactionInByTargetId(batchUploadSummary batchUploadSummary) {
        return transactionInDAO.insertTransactionInByTargetId(batchUploadSummary);
    }

    @Override
    public Integer getTransactionInIdByTargetId(batchUploadSummary bus) {
        return transactionInDAO.getTransactionInIdByTargetId(bus);
    }

    @Override
    public Integer updateTInIdForTransactiontarget(batchUploadSummary bus, Integer newTInId) {
        return transactionInDAO.updateTInIdForTransactiontarget(bus, newTInId);
    }

    @Override
    public Integer updateTINIDForBatchUploadSummary(batchUploadSummary bus, Integer newTInId) {
        return transactionInDAO.updateTINIDForBatchUploadSummary(bus, newTInId);
    }

    @Override
    public Integer copyTransactionInRecord(Integer newTInId, Integer oldTInId) {
        return transactionInDAO.copyTransactionInRecord(newTInId, oldTInId);
    }

    @Override
    public Integer insertTransactionTranslated(Integer newTInId,
            batchUploadSummary bus) {
        return transactionInDAO.insertTransactionTranslated(newTInId, bus);
    }

    @Override
    @Transactional
    public List<batchUploads> getAllUploadedBatches(Date fromDate, Date toDate) throws Exception {
        return transactionInDAO.getAllUploadedBatches(fromDate, toDate);
    }

    @Override
    public boolean searchTransactions(Transaction transaction, String searchTerm) throws Exception {

        boolean matchFound = false;

        if (transaction.getmessageTypeName().toLowerCase().matches(".*" + searchTerm + ".*")) {
            matchFound = true;
        }

        if (transaction.getreportableField1() != null && transaction.getreportableField1().toLowerCase().matches(".*" + searchTerm + ".*")) {
            matchFound = true;
        }

        if (transaction.getreportableField2() != null && transaction.getreportableField2().toLowerCase().matches(".*" + searchTerm + ".*")) {
            matchFound = true;
        }

        if (transaction.getreportableField3() != null && transaction.getreportableField3().toLowerCase().matches(".*" + searchTerm + ".*")) {
            matchFound = true;
        }

        if (transaction.getreportableField4() != null && transaction.getreportableField4().toLowerCase().matches(".*" + searchTerm + ".*")) {
            matchFound = true;
        }

        if (transaction.getstatusValue().toLowerCase().matches(".*" + searchTerm + ".*")) {
            matchFound = true;
        }

        if (transaction.gettargetOrgFields().size() > 0) {

            for (int i = 0; i < transaction.gettargetOrgFields().size(); i++) {
                if (transaction.gettargetOrgFields().get(i).getFieldValue() != null && transaction.gettargetOrgFields().get(i).getFieldValue().toLowerCase().matches(".*" + searchTerm + ".*")) {
                    matchFound = true;
                }
            }

        }

        return matchFound;

    }

    @Override
    public systemSummary generateSystemInboundSummary() {

        systemSummary systemSummary = new systemSummary();

        try {

            /* Get batches submitted this hour */
            Calendar thishour = new GregorianCalendar();
            thishour.set(Calendar.MINUTE, 0);
            thishour.set(Calendar.SECOND, 0);
            thishour.set(Calendar.MILLISECOND, 0);

            Calendar nexthour = new GregorianCalendar();
            nexthour.set(Calendar.MINUTE, 0);
            nexthour.set(Calendar.SECOND, 0);
            nexthour.set(Calendar.MILLISECOND, 0);
            nexthour.add(Calendar.HOUR_OF_DAY, 1);

            //System.out.println("This Hour: " + thishour.getTime() + " Next Hour: " + nexthour.getTime());
            Integer batchesThisHour = transactionInDAO.getAllUploadedBatches(thishour.getTime(), nexthour.getTime()).size();

            /* Get batches submitted today */
            Calendar starttoday = new GregorianCalendar();
            starttoday.set(Calendar.HOUR_OF_DAY, 0);
            starttoday.set(Calendar.MINUTE, 0);
            starttoday.set(Calendar.SECOND, 0);
            starttoday.set(Calendar.MILLISECOND, 0);

            Calendar starttomorrow = new GregorianCalendar();
            starttomorrow.set(Calendar.HOUR_OF_DAY, 0);
            starttomorrow.set(Calendar.MINUTE, 0);
            starttomorrow.set(Calendar.SECOND, 0);
            starttomorrow.set(Calendar.MILLISECOND, 0);
            starttomorrow.add(Calendar.DAY_OF_MONTH, 1);

            //System.out.println("Today: " + starttoday.getTime() + " Tomorrow: " + starttomorrow.getTime());
            Integer batchesToday = transactionInDAO.getAllUploadedBatches(starttoday.getTime(), starttomorrow.getTime()).size();

            /* Get batches submitted this week */
            Calendar thisweek = new GregorianCalendar();
            thisweek.set(Calendar.HOUR_OF_DAY, 0);
            thisweek.set(Calendar.MINUTE, 0);
            thisweek.set(Calendar.SECOND, 0);
            thisweek.set(Calendar.MILLISECOND, 0);
            thisweek.set(Calendar.DAY_OF_WEEK, thisweek.getFirstDayOfWeek());

            Calendar nextweek = new GregorianCalendar();
            nextweek.set(Calendar.HOUR_OF_DAY, 0);
            nextweek.set(Calendar.MINUTE, 0);
            nextweek.set(Calendar.SECOND, 0);
            nextweek.set(Calendar.MILLISECOND, 0);
            nextweek.set(Calendar.DAY_OF_WEEK, thisweek.getFirstDayOfWeek());
            nextweek.add(Calendar.WEEK_OF_YEAR, 1);

            //System.out.println("This Week: " + thisweek.getTime() + " Next Week: " + nextweek.getTime());
            Integer batchesThisWeek = transactionInDAO.getAllUploadedBatches(thisweek.getTime(), nextweek.getTime()).size();

            systemSummary.setBatchesPastHour(batchesThisHour);
            systemSummary.setBatchesToday(batchesToday);
            systemSummary.setBatchesThisWeek(batchesThisWeek);

            /* Get batches submitted yesterday */
        } catch (Exception ex) {
            Logger.getLogger(transactionInManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return systemSummary;

    }

    @Override
    public boolean checkPermissionForBatch(User userInfo, batchUploads batchInfo) {
        return transactionInDAO.checkPermissionForBatch(userInfo, batchInfo);
    }

    @Override
    public List<TransactionInError> getErrorList(Integer batchId) {
        return transactionInDAO.getErrorList(batchId);
    }

    //TODO need to write this better
    @Override
    public List<TransErrorDetailDisplay> populateErrorList(batchUploads batchInfo) {
        //we want to group by transaction
        List<TransErrorDetail> masterTedList = new ArrayList<TransErrorDetail>();
        List<TransErrorDetailDisplay> tedDisplayList = new ArrayList<TransErrorDetailDisplay>();
        try {
            ConfigErrorInfo configErrorInfo = new ConfigErrorInfo();
            configErrorInfo.setBatchId(batchInfo.getId());

            List<TransErrorDetail> tedList = getTransErrorDetailsForNoRptFields(batchInfo.getId(), Arrays.asList(5, 7, 8, 10, 11, 12, 13));
            if (tedList.size() > 0) {
                masterTedList.addAll(tedList);
            }
            /**
             * now get invalid configIds, errorId 6 - these are tied to transaction but not reportable fields since we don't know what configId it is. we don't know column that holds the field either since it didn't match with any for org, we display the first 4 columns
             */
            configErrorInfo = new ConfigErrorInfo();
            configErrorInfo.setBatchId(batchInfo.getId());
            tedList = getTransErrorDetailsForInvConfig(batchInfo.getId());
            if (tedList.size() > 0) {
                masterTedList.addAll(tedList);
            }
            //now get the rest by configId
            List<ConfigErrorInfo> confErrorListByConfig = getErrorConfigForBatch(batchInfo.getId());
            for (ConfigErrorInfo cei : confErrorListByConfig) {
            	cei.setBatchId(batchInfo.getId());
                configErrorInfo = getHeaderForConfigErrorInfo(batchInfo.getId(), cei);
                //add error details
                String sqlStmt = "";
                if (configErrorInfo.getRptField1() != 0) {
                    sqlStmt = sqlStmt + "F" + configErrorInfo.getRptField1() + " as rptField1Value, ";
                } else {
                    sqlStmt = sqlStmt + "'' as rptField1Value, ";
                }
                if (configErrorInfo.getRptField2() != 0) {
                    sqlStmt = sqlStmt + "F" + configErrorInfo.getRptField2() + " as rptField2Value, ";
                } else {
                    sqlStmt = sqlStmt + "'' as rptField2Value, ";
                }
                if (configErrorInfo.getRptField3() != 0) {
                    sqlStmt = sqlStmt + "F" + configErrorInfo.getRptField3() + " as rptField3Value, ";
                } else {
                    sqlStmt = sqlStmt + "'' as rptField3Value, ";
                }
                if (configErrorInfo.getRptField4() != 0) {
                    sqlStmt = sqlStmt + "F" + configErrorInfo.getRptField4() + " as rptField4Value ";
                } else {
                    sqlStmt = sqlStmt + "'' as rptField4Value ";
                }

                tedList = getTransErrorDetails(batchInfo, configErrorInfo, sqlStmt);
                if (tedList.size() > 0) {
                    masterTedList.addAll(tedList);
                }
            }

            /**
             * custom group by - need to check if it is faster to just hit the db *
             */
            if (masterTedList.size() > 0) {
                Integer transactionInId = -1;
                TransErrorDetailDisplay tedd = new TransErrorDetailDisplay();
                List<TransErrorDetail> transErrorDetailList = new ArrayList<TransErrorDetail>();
                Integer counter = 0;

                /**
                 * we need to group them by transactionInId *
                 */
                for (TransErrorDetail ted : masterTedList) {
                    counter++;
                    if (!ted.getTransactionInId().equals(transactionInId)) {
                        if (transactionInId != -1) {
                            tedd.setTedList(transErrorDetailList);
                            transErrorDetailList = new ArrayList<TransErrorDetail>();
                            tedDisplayList.add(tedd);
                        }

                        transactionInId = ted.getTransactionInId();
                        tedd = new TransErrorDetailDisplay();
                        tedd.setTransactionInId(transactionInId);
                        tedd.setTransactionStatus(ted.getTransactionStatus());
                        tedd.setRptField1Label(ted.getRptField1Label());
                        tedd.setRptField2Label(ted.getRptField2Label());
                        tedd.setRptField3Label(ted.getRptField3Label());
                        tedd.setRptField4Label(ted.getRptField4Label());
                        tedd.setTransactionStatusValue(ted.getTransactionStatusValue());
                    }
                    transErrorDetailList.add(ted);
                    if (counter == masterTedList.size()) {
                        tedd.setTedList(transErrorDetailList);
                        transErrorDetailList = new ArrayList<TransErrorDetail>();
                        tedDisplayList.add(tedd);

                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("populateErrorList " + ex.getCause());
        }

        return tedDisplayList;
    }

    @Override
    public List<TransErrorDetail> getTransErrorDetailsForNoRptFields(Integer batchId, List<Integer> errorCodes) {
        return transactionInDAO.getTransErrorDetailsForNoRptFields(batchId, errorCodes);
    }

    @Override
    public Integer getCountForErrorId(Integer batchId, Integer errorId) {
        return transactionInDAO.getCountForErrorId(batchId, errorId);
    }

    @Override
    public List<TransErrorDetail> getTransErrorDetailsForInvConfig(Integer batchId) {
        return transactionInDAO.getTransErrorDetailsForInvConfig(batchId);
    }

    @Override
    public List<ConfigErrorInfo> getErrorConfigForBatch(Integer batchId) {
        return transactionInDAO.getErrorConfigForBatch(batchId);
    }

    @Override
    public ConfigErrorInfo getHeaderForConfigErrorInfo(Integer batchId, ConfigErrorInfo configErrorInfo) {
        //we create header string
        List<Integer> rptFieldArray = Arrays.asList(configErrorInfo.getRptField1(), configErrorInfo.getRptField2(), configErrorInfo.getRptField3(), configErrorInfo.getRptField4());
        return transactionInDAO.getHeaderForConfigErrorInfo(batchId, configErrorInfo, rptFieldArray);
    }

    @Override
    public List<TransErrorDetail> getTransErrorDetails(batchUploads batchInfo, ConfigErrorInfo configErrorInfo, String sqlStmt) {
        List<TransErrorDetail> transErrorDetails;
        try {
            transErrorDetails = transactionInDAO.getTransErrorDetails(batchInfo, configErrorInfo);

            for (TransErrorDetail trd : transErrorDetails) {
                String newSQL = "";
                switch (trd.getErrorCode()) {
                    case 2:
                        trd.setErrorInfo(messageTypeDAO.getValidationById(trd.getValidationTypeId()));
                        break;
                    case 3:
                        trd.setErrorInfo(messagetypemanager.getCrosswalk(trd.getCwId()).getName());
                        break;
                    case 4:
                        trd.setErrorInfo(configurationManager.getMacroById(trd.getMacroId()).getMacroName());
                        break;
                    default:
                        break;
                }
                if (trd.getErrorFieldNo() != null) {
                    if (trd.getErrorCode() == 9) {
                        trd.setErrorFieldLabel("Target Org");
                    } else {
                        trd.setErrorFieldLabel(configurationtransportmanager.getCFFByFieldNo(configErrorInfo.getConfigId(), trd.getErrorFieldNo()).getFieldLabel());
                    }
                    newSQL = ", F" + trd.getErrorFieldNo() + " as errorData ";
                }
                trd = getTransErrorData(trd, sqlStmt + newSQL);
                trd.setRptField1Label(configErrorInfo.getRptFieldHeading1());
                trd.setRptField2Label(configErrorInfo.getRptFieldHeading2());
                trd.setRptField3Label(configErrorInfo.getRptFieldHeading3());
                trd.setRptField4Label(configErrorInfo.getRptFieldHeading4());
            }

            return transErrorDetails;

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("getTransErrorDetails " + ex.getCause());
            return null;
        }
    }

    @Override
    public TransErrorDetail getTransErrorData(TransErrorDetail ted, String sqlStmt) {
        //we create header string
        return transactionInDAO.getTransErrorData(ted, sqlStmt);
    }

    @Override
    public Integer flagNoPermissionConfig(batchUploads batch) {
        Integer sysErrors = 0;
        try {
            sysErrors = transactionInDAO.insertNoPermissionConfig(batch);
            sysErrors = sysErrors + transactionInDAO.updateStatusByErrorCode(batch.getId(), 11, 11);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("flagNoPermissionConfig " + ex.getCause());
            sysErrors++;
        }
        return sysErrors;
    }

    @Override
    public boolean hasPermissionForBatch(batchUploads batchInfo, User userInfo, boolean hasConfigurations) {
        boolean hasPermission = false;
        /**
         * user can view audit report if 1. uploaded by user 2. file type uploaded was for multiple types and user has configurations 3. user is in connection sender list for batch's configId
         */
        try {
            if (batchInfo.getuserId() == userInfo.getId()) {
                hasPermission = true;
            } else if (batchInfo.getConfigId() == 0 && hasConfigurations) {
                hasPermission = true;
            } else if (checkPermissionForBatch(userInfo, batchInfo)) {
                hasPermission = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("checkPermissionForBatch " + ex.getCause());
        }
        return hasPermission;
    }

    @Override
    public batchUploads getBatchDetailsByTInId(Integer transactionInId) {
        return transactionInDAO.getBatchDetailsByTInId(transactionInId);
    }

    @Override
    public boolean allowBatchClear(Integer batchUploadId) {
        return transactionInDAO.allowBatchClear(batchUploadId);
    }

    @Override
    public void updateTranStatusByTInId(Integer transactionInId, Integer statusId) throws Exception {
        transactionInDAO.updateTranStatusByTInId(transactionInId, statusId);

    }

    @Override
    public List<batchUploads> populateBatchInfo(
            List<batchUploads> uploadedBatches, User userInfo) {
        try {
            for (batchUploads batch : uploadedBatches) {
                List<transactionIn> batchTransactions = getBatchTransactions(batch.getId(), userInfo.getId());
                batch.settotalTransactions(batchTransactions.size());

                lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                batch.setstatusValue(processStatus.getDisplayCode());
                if (batch.getstatusId() == 5) {
                    batch.setTransTotalNotFinal(getRecordCounts(batch.getId(), finalStatusIds, false, false));
                }

                User userDetails = usermanager.getUserById(batch.getuserId());
                String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
                batch.setusersName(usersName);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("populateBatchInfo " + ex.getCause());
        }
        return uploadedBatches;
    }

    @Override
    public List<TransErrorDetail> getTransactionErrorsByFieldNo(int transactionInId, int fieldNo) throws Exception {
        return transactionInDAO.getTransactionErrorsByFieldNo(transactionInId, fieldNo);
    }

    @Override
    public List<UserActivity> getBatchActivities(batchUploads batchInfo, boolean forUsers, boolean foroutboundProcessing) {
        if (!forUsers) {
            //we have autolog that tracks the date/time each time the status change on a batch, not in use right now
            return null;
        } else {
            return transactionInDAO.getBatchUserActivities(batchInfo, foroutboundProcessing);
        }
    }

    @Override
    public List<transactionRecords> getFieldColAndValueByTransactionId(configurationFormFields cff,
            Integer transactionId) {
        return transactionInDAO.getFieldColAndValueByTransactionId(cff, transactionId);
    }


    @Override
    public Integer insertSFTPRun(MoveFilesLog sftpJob) {
        return transactionInDAO.insertSFTPRun(sftpJob);
    }

    @Override
    public void updateSFTPRun(MoveFilesLog sftpJob) throws Exception {
        transactionInDAO.updateSFTPRun(sftpJob);
    }

    @Override
    public List<batchUploads> getsentBatchesHistory(int userId, int orgId, int toOrgId, int messageTypeId, Date fromDate, Date toDate) throws Exception {
        return transactionInDAO.getsentBatchesHistory(userId, orgId, toOrgId, messageTypeId, fromDate, toDate);
    }

    /**
     * The sftp move files will grab all unique active SFTP pull paths and check folders for file. It will check path to see how many configurations it is associated with. It will also get the delimiter, check if there is a headerRow, how the file is being release.
	 * *
     */
    @Override
    public Integer moveSFTPFiles() {
        Integer sysErrors = 0;

        try {
            //1 . get distinct ftp paths
            List<configurationFTPFields> inputPaths = getFTPInfoForJob(1);

            //loop ftp paths and check
            for (configurationFTPFields ftpInfo : inputPaths) {
                //we insert job so if anything goes wrong or the scheduler overlaps, we won't be checking the same folder over and over
                MoveFilesLog sftpJob = new MoveFilesLog();
                sftpJob.setStatusId(1);
                sftpJob.setFolderPath(ftpInfo.getdirectory());
                sftpJob.setTransportMethodId(3);
                sftpJob.setMethod(1);
                Integer lastId = insertSFTPRun(sftpJob);
                sftpJob.setId(lastId);

                // check if directory exists, if not create
                fileSystem fileSystem = new fileSystem();
                String inPath = fileSystem.setPath(ftpInfo.getdirectory());
                File f = new File(inPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
              //we look up org for this path
        		Integer orgId = configurationtransportmanager.getOrgIdForFTPPath(ftpInfo);
        		
                sysErrors = sysErrors + moveFilesByPath(ftpInfo.getdirectory(), 3, orgId, ftpInfo.gettransportId());

                if (sysErrors == 0) {
                    sftpJob.setStatusId(2);
                    sftpJob.setEndDateTime(new Date());
                    updateSFTPRun(sftpJob);
                }
            }

			// if there are no errors, we release the folder path
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("moveSFTPFilesJob " + ex.getCause());
            return 1;
        }
        return sysErrors;
    }

    @Override
	public Integer moveFilesByPath(String inPath, Integer transportMethodId, Integer orgId, Integer transportId) {
		Integer sysErrors = 0;
		
		try {
		
		fileSystem fileSystem = new fileSystem();
		String fileInPath = fileSystem.setPath(inPath);
		File folder = new File(fileInPath);
		
			
		//list files
		//we only list visible files
		File[] listOfFiles = folder.listFiles((FileFilter) HiddenFileFilter.VISIBLE); 
		
		//too many variables that could come into play regarding file types, will check files with one method
		//loop files 
		 for (File file : listOfFiles) {
			 // first get file extension
			 String fileName = file.getName();
			 String fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
			 
			 //figure out how many transports are using fileExt method
			 List<configurationTransport> transportList = configurationtransportmanager.getTransportListForFileExt(fileExt, transportMethodId);
			 
			 //figure out if files has distinct delimiters
			 List<configurationTransport> transports =  configurationtransportmanager.getConfigTransportForFileExt(fileExt, transportMethodId);
			 
			 DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
             Date date = new Date();
             /* Create the batch name (TransportMethodId+OrgId+Date/Time/Seconds) */
             String batchName = new StringBuilder().append(transportMethodId).append(orgId).append(dateFormat.format(date)).toString();
             
             batchUploads batchInfo = new batchUploads();
			 batchInfo.setOrgId(orgId);
			 batchInfo.settransportMethodId(transportMethodId);
			 batchInfo.setstatusId(4);
			 batchInfo.setstartDateTime(date);
			 batchInfo.setutBatchName(batchName);
             batchInfo.setOriginalFolder(inPath);
             
			 String outPath = "";
             Integer batchId = 0;
             String newFileName = "";
             Integer statusId = 4;
             Integer configId = 0;
             Integer fileSize = 0;
             Integer encodingId = 1;
             Integer errorId = 0;
             
			 if (transportList.size() == 0) {
				 //no transport is using this method - we find the mgr user and reject this file
				 batchInfo.setuserId(usermanager.getUserByTypeByOrganization(orgId).get(0).getId());
				 //since this file is not associated with config, we put in main folder for org
				 Organization orgDetails = organizationmanager.getOrganizationById(orgId);
				 String defPath = "/bowlink/"+orgDetails.getcleanURL()+"/input files/";
				 outPath = fileSystem.setPath(defPath);
                 batchInfo.setConfigId(0);
                 newFileName = newFileName(outPath, fileName);
                 batchInfo.setoriginalFileName(newFileName);
                 batchInfo.setFileLocation(defPath);
                 batchInfo.setEncodingId(encodingId);
                 batchId = (Integer) submitBatchUpload(batchInfo);
				 //insert error
                 errorId = 13;
                 statusId = 7;
			 } else if (transports.size() == 1) {
				 encodingId = transports.get(0).getEncodingId();
				 configurationTransport  ct = configurationtransportmanager.getTransportDetailsByTransportId(transportId);
				 fileSize = ct.getmaxFileSize();
				 if (transportList.size() > 1) {
					 configId =0;
					 fileSize = configurationtransportmanager.getMinMaxFileSize(fileExt, transportMethodId);
				 } else {
					 configId = ct.getconfigId();
				 }
				 batchInfo.setConfigId(configId);
				 batchInfo.setContainsHeaderRow(transports.get(0).getContainsHeaderRow());
				 batchInfo.setDelimChar(transports.get(0).getDelimChar());
				 batchInfo.setFileLocation(ct.getfileLocation());
				 outPath = fileSystem.setPath(ct.getfileLocation());
				 batchInfo.setOrgId(orgId);
				 newFileName = newFileName(outPath, fileName);
                 batchInfo.setoriginalFileName(newFileName);
                 batchInfo.setEncodingId(encodingId);
                 
                 //find user 
                 List<User> users = usermanager.getSendersForConfig(Arrays.asList(ct.getconfigId()));
                 if (users.size() == 0) {
                     users = usermanager.getOrgUsersForConfig(Arrays.asList(ct.getconfigId()));
                 }
                 
                 batchInfo.setuserId(users.get(0).getId());
                 batchId = (Integer) submitBatchUpload(batchInfo);                
				 statusId = 2;
				 
			 } else if  (transportList.size() > 1 && transports.size() >1)  {
				 //we loop though our delimiters for this type of fileExt
				 String delimiter = "";
				 Integer fileDelimiter = 0;
				 String fileLocation = "";
				 Integer userId = 0;
				 //get distinct delimiters
				 List <configurationTransport>  delimList = configurationtransportmanager.getDistinctDelimCharForFileExt(fileExt, transportMethodId);
				 List <configurationTransport>  encodings = configurationtransportmanager.getTransportEncoding(fileExt, transportMethodId);
				 //we reject file is multiple encodings/delimiters are found for extension type as we won't know how to decode it and read delimiter
				 if (encodings.size() != 1) {
					 batchInfo.setuserId(usermanager.getUserByTypeByOrganization(orgId).get(0).getId());
					 statusId = 7;
					 errorId = 16;
				 } else {
					 	 encodingId = encodings.get(0).getEncodingId();
						 for (configurationTransport ctdelim: delimList) {
							 fileSystem dir = new fileSystem();
							 int delimCount = (Integer) dir.checkFileDelimiter(file, ctdelim.getDelimChar());
					         if (delimCount > 3) {
				        	 	   delimiter = ctdelim.getDelimChar();
				        	 	   fileDelimiter = ctdelim.getfileDelimiter();
					               statusId = 2;
					               fileLocation = ctdelim.getfileLocation();
					               break;
					     }	 
					 }
				 }
				 if (statusId !=2) {
					 //no vaild delimiter detected
					 statusId = 7;
					 userId = usermanager.getUserByTypeByOrganization(orgId).get(0).getId();
					 Organization orgDetails = organizationmanager.getOrganizationById(orgId);
					 String defPath = "/bowlink/"+orgDetails.getcleanURL()+"/input files/";
					 outPath = fileSystem.setPath(defPath);
	                 batchInfo.setConfigId(configId);
					 batchInfo.setFileLocation(defPath);
					 batchInfo.setOrgId(orgId);
					 newFileName = newFileName(outPath, fileName);
	                 batchInfo.setoriginalFileName(newFileName);            
	                 batchInfo.setuserId(userId);
	                 batchId = (Integer) submitBatchUpload(batchInfo);
	                 batchInfo.setEncodingId(encodingId);
	                 errorId = 15;
				 } else if (statusId == 2) {
					 encodingId = encodings.get(0).getEncodingId();
					 //we check to see if there is multi header row, if so, we reject because we don't know what header rows value to look for
					 List <configurationTransport>  containsHeaderRowCount = configurationtransportmanager.getCountContainsHeaderRow(fileExt, transportMethodId);
					 
					 if (containsHeaderRowCount.size() != 1) {
						 batchInfo.setuserId(usermanager.getUserByTypeByOrganization(orgId).get(0).getId());
						 statusId = 7;
						 errorId = 14;
					 } else {
						 List <Integer> totalConfigs = configurationtransportmanager.getConfigCount(fileExt, transportMethodId, fileDelimiter);
						 
						 //set how many configs we have
						 if (totalConfigs.size() > 1) {
							 configId = 0;
						 } else {
							 configId = totalConfigs.get(0);
						 } 
						 
						 //get path
						 fileLocation = configurationtransportmanager.getTransportDetails(totalConfigs.get(0)).getfileLocation();
						 fileSize = configurationtransportmanager.getTransportDetails(totalConfigs.get(0)).getmaxFileSize();
						 List<User> users = usermanager.getSendersForConfig(totalConfigs);
		                 if (users.size() == 0) {
		                     users = usermanager.getOrgUsersForConfig(totalConfigs);
		                 }
		                 userId = users.get(0).getId();
		                 batchInfo.setContainsHeaderRow(containsHeaderRowCount.get(0).getContainsHeaderRow());
						 batchInfo.setDelimChar(delimiter);
						 batchInfo.setConfigId(configId);
						 batchInfo.setFileLocation(fileLocation);
						 outPath = fileSystem.setPath(fileLocation);
						 batchInfo.setOrgId(orgId);
						 newFileName = newFileName(outPath, fileName);
		                 batchInfo.setoriginalFileName(newFileName);  		                
		                 batchInfo.setuserId(userId);
		                 batchInfo.setEncodingId(encodingId);
		                 batchId = (Integer) submitBatchUpload(batchInfo);
					 }
				 } 
				  
			 }
			 
			 
			 //we encoded user's file if it is not
			 File newFile = new File(outPath + newFileName);
			 // now we move file
             Path source = file.toPath();
             Path target = newFile.toPath();
             
             File archiveFile = new File(fileSystem.setPath(archivePath) + batchName + newFileName.substring(newFileName.lastIndexOf(".")));
             Path archive = archiveFile.toPath();
             //we keep original file in archive folder
             Files.copy(source, archive);
             /** we check encoding here **/
             if (encodingId < 2) { //file is not encoded
	             String encodedOldFile = filemanager.encodeFileToBase64Binary(file);
	             filemanager.writeFile(newFile.getAbsolutePath(), encodedOldFile);
	             Files.delete(source);
             } else {
            	 Files.move(source, target);
             }
             
             if (statusId == 2) {
            	 /** check file size
            	  * if configId is 0 we go with the smallest file size
            	  ***/
            	 long maxFileSize =  fileSize * 1000000;
            	 if (Files.size(target) > maxFileSize) {
                     statusId = 7;
                     errorId = 12;
                 }
             }
             
             if (statusId != 2) {
				 insertProcessingError(errorId, 0, batchId, null, null, null, null, false, false, "");
			 }
             
			 updateBatchStatus(batchId,statusId, "endDateTime");
         	 
		 }
		 
		} catch (Exception ex) {
			ex.printStackTrace();
	        System.err.println("moveFilesByPath " + ex.getCause());
	        return 1;
		}
		return sysErrors;
	}

	/** this method grabs all distinct ftp path that need to be check for files **/
    @Override
    public List<configurationFTPFields> getFTPInfoForJob(Integer method) {
        return transactionInDAO.getFTPInfoForJob(method);
    }

    @Override
    public String newFileName(String path, String fileName) {
        try {
            File newFile = new File(path + fileName);
            if (newFile.exists()) {
                int i = 1;
                while (newFile.exists()) {
                    int iDot = fileName.lastIndexOf(".");
                    newFile = new File(path + fileName.substring(0, iDot) + "_(" + ++i + ")" + fileName.substring(iDot));
                }
                fileName = newFile.getName();
            }
            return fileName;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("newBatchName " + ex.getCause());
            return null;
        }
    }
    
    @Override
    public List<batchUploadSummary> getBatchesToSentOrg(int srcorgId, int tgtOrgId, int messageTypeId) throws Exception {
        return transactionInDAO.getBatchesToSentOrg(srcorgId, tgtOrgId, messageTypeId);
    }
    
    
    @Override
    public messagePatients getPatientTransactionDetails(int transactionInId) {
        return transactionInDAO.getPatientTransactionDetails(transactionInId);
    }
    
    @Override
	public String copyUplaodedPath(configurationTransport transportDetails, MultipartFile fileUpload) {
	    	
			//save the file as is to input folder
			MultipartFile file = fileUpload;
	        String fileName = file.getOriginalFilename();

	        InputStream inputStream;
	        OutputStream outputStream;

	        try {
	            inputStream = file.getInputStream();
	            File newFile = null;

	            //Set the directory to save the brochures to
	            fileSystem dir = new fileSystem();

	            String filelocation = transportDetails.getfileLocation();
	            filelocation = filelocation.replace("/bowlink/", "");
	            dir.setDirByName(filelocation);

	            newFile = new File(dir.getDir() + fileName);

	            if (newFile.exists()) {
	                int i = 1;
	                while (newFile.exists()) {
	                    int iDot = fileName.lastIndexOf(".");
	                    newFile = new File(dir.getDir() + fileName.substring(0, iDot) + "_(" + ++i + ")" + fileName.substring(iDot));
	                }
	                fileName = newFile.getName();
	                newFile.createNewFile();
	            } else {
	                newFile.createNewFile();
	            }

	            //Save the attachment
	            outputStream = new FileOutputStream(newFile);
	            int read = 0;
	            byte[] bytes = new byte[1024];

	            while ((read = inputStream.read(bytes)) != -1) {
	                outputStream.write(bytes, 0, read);
	            }
	            outputStream.close();

	           return fileName;	          
	        } catch (IOException e) {
	        	System.err.println("copyUplaodedPath " + e.getCause());
	            e.printStackTrace();
	            return null;
	        }
		}
	
	/**
     * The 'chkUploadBatchFile' function will take in the file and orgName and upload the file to the appropriate file on the file system. The function will run the file through various validations. If a single validation fails the batch will be put in a error validation status and the file will be removed from the system. The user will receive an error message on the screen letting them know which validations have failed and be asked to upload a new file.
     *
     * The following validations will be taken place. - File is not empty - Proper file type (as determined in the configuration set up) - Proper delimiter (as determined in the configuration set up) - Does not exceed file size (as determined in the configuration set up)
     *
     * @param configId The configuration Id to get some validation parameters
     * @param fileUpload The file to be uploaded
     *
     */
    @Override
    public Map<String, String> chkUploadBatchFile(configurationTransport transportDetails, File uploadedFile) throws Exception {
    	
    	Map<String, String> batchFileResults = new HashMap<String, String>();
    	
    	try {
	    	long fileSize = uploadedFile.length();
	        long fileSizeMB = (fileSize / (1024L * 1024L));
	
	        /* 
	         1 = File is empty
	         2 = Too large
	         3 = Wrong file type
	         4 = Wrong delimiter
	         */
	       
	        /* Make sure the file is not empty : ERROR CODE 1 */
	        if (fileSize == 0) {
	            batchFileResults.put("emptyFile", "1");
	        }
	
	        /* Make sure file is the correct size : ERROR CODE 2 */
	        double maxFileSize = (double) transportDetails.getmaxFileSize();

	        if (fileSizeMB > maxFileSize) {
	            batchFileResults.put("wrongSize", "2");
	        }
	
	        	String fileName = uploadedFile.getName();
	        
	
	            batchFileResults.put("fileName", fileName);
	
	            /* Make sure file is the correct file type : ERROR CODE 3 */
	            String ext = FilenameUtils.getExtension(uploadedFile.getAbsolutePath());
	
	            String fileType = (String) configurationManager.getFileTypesById(transportDetails.getfileType());
	
	            if ("hl7".equals(fileType)) {
	                fileType = "hr";
	            }
	
	            if (ext == null ? fileType != null : !ext.equals(transportDetails.getfileExt())) {
	                batchFileResults.put("wrongFileType", "3");
	            }
	
	            fileSystem dir = new fileSystem();
	            	            
	            /* Make sure the file has the correct delimiter : ERROR CODE 5 */
	            String delimChar = (String) messageTypeDAO.getDelimiterChar(transportDetails.getfileDelimiter());
	
	            //Check to make sure the file contains the selected delimiter
	            //Set the directory that holds the crosswalk files
	            int delimCount = (Integer) dir.checkFileDelimiter(uploadedFile, delimChar);
	
	            if (delimCount < 3 && !"xml".equals(transportDetails.getfileExt())) {
	                batchFileResults.put("wrongDelim", "4");
	            }
	
	            //Save the attachment
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

        return batchFileResults;

    }

	@Override
	public Integer moveRhapsodyFiles() {
		Integer sysErrors = 0;

        try {
            //1 . get distinct ftp paths
            List<configurationRhapsodyFields> inputPaths = getRhapsodyInfoForJob(1);

            //loop ftp paths and check
            for (configurationRhapsodyFields rhapsodyInfo : inputPaths) {
                //we insert job so if anything goes wrong or the scheduler overlaps, we won't be checking the same folder over and over
                MoveFilesLog moveJob = new MoveFilesLog();
                moveJob.setStatusId(1);
                moveJob.setFolderPath(rhapsodyInfo.getDirectory());
                moveJob.setTransportMethodId(5);
                moveJob.setMethod(1);
                Integer lastId = insertSFTPRun(moveJob);
                moveJob.setId(lastId);

                // check if directory exists, if not create
                fileSystem fileSystem = new fileSystem();
                String inPath = fileSystem.setPath(rhapsodyInfo.getDirectory());
                File f = new File(inPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
              //we look up org for this path
        		Integer orgId = configurationtransportmanager.getOrgIdForRhapsodyPath(rhapsodyInfo);
        		
                sysErrors = sysErrors + moveFilesByPath(rhapsodyInfo.getDirectory(), 5, orgId, rhapsodyInfo.getTransportId());

                if (sysErrors == 0) {
                	moveJob.setStatusId(2);
                	moveJob.setEndDateTime(new Date());
                    updateSFTPRun(moveJob);
                }
            }

			// if there are no errors, we release the folder path
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("moveRhapsodyFiles " + ex.getCause());
            return 1;
        }
        return sysErrors;
	}

	/** this method grabs all distinct ftp path that need to be check for files **/
    @Override
    public List<configurationRhapsodyFields> getRhapsodyInfoForJob(Integer method) {
        return transactionInDAO.getRhapsodyInfoForJob(method);
    }
	

	@Override
	public Integer insertTransactionInError(Integer newTInId, Integer oldTInId) {
		return transactionInDAO.insertTransactionInError(newTInId, oldTInId);
	}
	
	@Override
	public boolean insertTargets (Integer batchId) {
		return transactionInDAO.insertTargets(batchId);
	}
}
