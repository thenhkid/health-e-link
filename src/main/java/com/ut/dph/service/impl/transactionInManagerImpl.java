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
import com.ut.dph.model.Transaction;
import com.ut.dph.model.TransactionInError;
import com.ut.dph.model.User;
import com.ut.dph.model.batchUploadSummary;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationMessageSpecs;
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
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.model.systemSummary;
import com.ut.dph.reference.fileSystem;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import org.apache.commons.io.FilenameUtils;
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

    private int processingSysErrorId = 5;

    //final status Ids
    private List<Integer> finalStatusIds = Arrays.asList(11, 12, 13, 16);

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
    public List<batchUploads> getpendingBatches(int userId, int orgId, String searchTerm, Date fromDate, Date toDate, int page, int maxResults) throws Exception {
        return transactionInDAO.getpendingBatches(userId, orgId, searchTerm, fromDate, toDate, page, maxResults);
    }

    @Override
    @Transactional
    public List<transactionIn> getBatchTransactions(int batchId, int userId) throws Exception {
        return transactionInDAO.getBatchTransactions(batchId, userId);
    }

    @Override
    @Transactional
    public List<batchUploads> getsentBatches(int userId, int orgId, String searchTerm, Date fromDate, Date toDate, int page, int maxResults) throws Exception {
        return transactionInDAO.getsentBatches(userId, orgId, searchTerm, fromDate, toDate, page, maxResults);
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

    /**
     * 1.27.14 not in use
     *
     * Last Step - insert all transactions with RP (10) status and batch status of SRP (5) for a batch
     *
     */
    @Override
    public boolean processTransactions(int batchUploadId) {
        return true;
    }

    @Override
    public List<ConfigForInsert> setConfigForInsert(int configId, int batchUploadId) {
        // we call sp and set the parameters here
        return transactionInDAO.setConfigForInsert(configId, batchUploadId);
    }

    @Override
    public List<Integer> getConfigIdsForBatch(int batchUploadId, boolean getAll) {
        return transactionInDAO.getConfigIdsForBatch(batchUploadId, getAll);
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
     * this method takes in the transId, the insert fields, the insert tables*
     */
    @Override
    public boolean insertMultiValToMessageTables(ConfigForInsert config, Integer subStringCounter, Integer transId) {
        return transactionInDAO.insertMultiValToMessageTables(config, subStringCounter, transId);
    }

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
            System.out.println("clearMessageTables " + e.getStackTrace());
            return 1;

        }
    }

    /**
     * The 'uploadBatchFile' function will take in the file and orgName and upload the file to the appropriate file on the file system. The function will run the file through various validations. If a single validation fails the batch will be put in a error validation status and the file will be removed from the system. The user will receive an error message on the screen letting them know which validations have failed and be asked to upload a new file.
     *
     * The following validations will be taken place. - File is not empty - Proper file type (as determined in the configuration set up) - Proper delimiter (as determined in the configuration set up) - Does not exceed file size (as determined in the configuration set up)
     *
     * @param configId The configuration Id to get some validation parameters
     * @param fileUpload The file to be uploaded
     *
     */
    @Override
    public Map<String, String> uploadBatchFile(int configId, MultipartFile fileUpload) throws Exception {

        configuration configDetails = configurationManager.getConfigurationById(configId);
        configurationTransport transportDetails = configurationtransportmanager.getTransportDetails(configId);

        MultipartFile file = fileUpload;
        String fileName = file.getOriginalFilename();

        long fileSize = file.getSize();
        long fileSizeMB = (fileSize / (1024L * 1024L));

        /* 
         1 = File is empty
         2 = Too large
         3 = Wrong file type
         4 = Wrong delimiter
         */
        Map<String, String> batchFileResults = new HashMap<String, String>();

        /* Make sure the file is not empty : ERROR CODE 1 */
        if (fileSize == 0) {
            batchFileResults.put("emptyFile", "1");
        }

        /* Make sure file is the correct size : ERROR CODE 2 */
        double maxFileSize = (double) transportDetails.getmaxFileSize();

        if (fileSizeMB > maxFileSize) {
            batchFileResults.put("wrongSize", "2");
        }

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

            batchFileResults.put("fileName", fileName);

            /* Make sure file is the correct file type : ERROR CODE 3 */
            String ext = FilenameUtils.getExtension(dir.getDir() + fileName);

            String fileType = (String) configurationManager.getFileTypesById(transportDetails.getfileType());

            if (ext == null ? fileType != null : !ext.equals(fileType)) {
                batchFileResults.put("wrongFileType", "3");
            }

            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();

            /* Make sure the file has the correct delimiter : ERROR CODE 5 */
            String delimChar = (String) messageTypeDAO.getDelimiterChar(transportDetails.getfileDelimiter());

            //Check to make sure the file contains the selected delimiter
            //Set the directory that holds the crosswalk files
            int delimCount = (Integer) dir.checkFileDelimiter(dir, fileName, delimChar);

            if (delimCount < 3) {
                batchFileResults.put("wrongDelim", "4");
            }

            //Save the attachment
        } catch (IOException e) {
            e.printStackTrace();
        }

        return batchFileResults;

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
     * orginal method only excludes 1, need the ability to exclude different statusIds *
     */
    @Override
    public List<batchUploads> getuploadedBatches(int userId, int orgId, Date fromDate, Date toDate, String searchTerm, int page, int maxResults) throws Exception {
        return getuploadedBatches(userId, orgId, fromDate, toDate, searchTerm, page, maxResults, Arrays.asList(1));
    }

    @Override
    public List<batchUploads> getuploadedBatches(int userId, int orgId, Date fromDate, Date toDate, String searchTerm, int page, int maxResults, List<Integer> excludedStatusIds) throws Exception {
        return transactionInDAO.getuploadedBatches(userId, orgId, fromDate, toDate, searchTerm, page, maxResults, excludedStatusIds);
    }

    /**
     * We will take a batch and then from its status etc we will decide if we want to process transactions or not. This allow the admin to run just one batch
     *
     * This assumes batches SR - 6, Trans status REL We still run through entire process but these records should pass... (check to make sure it aligns with file upload) just be applying Macros / CW and inserting into our message tables
     */
    /**
     * This assumes batch being passed in is SSA or SR 1. We set it to SBP, start date time
     *
     * *
     */
    @Override
    public boolean processBatch(int batchUploadId) throws Exception {

        Integer batchStausId = 29;
        List<Integer> errorStatusIds = Arrays.asList(11, 13, 14, 16);
        //get batch details
        batchUploads batch = getBatchDetails(batchUploadId);
        //this should be the same point of both ERG and Uploaded File *
        Integer systemErrorCount = 0;
        // Check to make sure the file is valid for processing, valid file is a batch with SSL (3) or SR (6)*

        if ((batch.getstatusId() == 3 || batch.getstatusId() == 6)) {
            // set batch to SBP - 4*
            updateBatchStatus(batchUploadId, 4, "startDateTime");

            /**
             * we should only process the ones that are not REL status, to be safe, we copy over data from transactionInRecords*
             */
            resetTransactionTranslatedIn(batchUploadId, false);

            //clear transactionInError table for batch
            systemErrorCount = systemErrorCount + clearTransactionInErrors(batchUploadId, true);

            List<Integer> configIds = getConfigIdsForBatch(batchUploadId, false);
            for (Integer configId : configIds) {
				//we need to run all checks before insert regardless *

                //check R/O
                List<configurationFormFields> reqFields = getRequiredFieldsForConfig(configId);

                for (configurationFormFields cff : reqFields) {
                    systemErrorCount = systemErrorCount + insertFailedRequiredFields(cff, batchUploadId);
                }
                // update status of the failed records to ERR - 14
                updateStatusForErrorTrans(batchUploadId, 14, false);
                //run validation
                systemErrorCount = systemErrorCount + runValidations(batchUploadId, configId);
                // update status of the failed records to ERR - 14
                updateStatusForErrorTrans(batchUploadId, 14, false);

                // 1. grab the configurationDataTranslations and run cw/macros
                List<configurationDataTranslations> dataTranslations = configurationManager
                        .getDataTranslationsWithFieldNo(configId);
                for (configurationDataTranslations cdt : dataTranslations) {
                    if (cdt.getCrosswalkId() != 0) {
                        systemErrorCount = systemErrorCount + processCrosswalk(configId, batchUploadId, cdt, false);
                    } else if (cdt.getMacroId() != 0) {
                        systemErrorCount = systemErrorCount + processMacro(configId, batchUploadId, cdt, false);
                    }
                }
                /**
                 * if there are errors, those are system errors, they will be logged we get errorId 5 and email to admin, update batch to 29 *
                 */
                if (systemErrorCount > 0) {
                    setBatchToError(batchUploadId, "System error occurred during processBatch, please review errors in audit report");
                    return false;
                }
            } //end of configs

            updateTransactionStatus(batchUploadId, 0, 10, 12);
            //transactionIn and transactionTarget status should be the same 
            copyTransactionInStatusToTarget(batchUploadId);

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
                        updateTransactionStatus(batchUploadId, 0, 14, 16);
                        //target should still be pending output
                        updateTransactionTargetStatus(batchUploadId, 0, 14, 19);
                    }
                    if (handlingDetails.get(0).geterrorHandling() == 2) {
                        //reject errors
                        updateTransactionStatus(batchUploadId, 0, 14, 13);
                        copyTransactionInStatusToTarget(batchUploadId);
                    }
                    updateTransactionStatus(batchUploadId, 0, 12, 19);
                    updateTransactionTargetStatus(batchUploadId, 0, 12, 19);
                    batchStausId = 24;

                } else if (handlingDetails.get(0).getautoRelease() && handlingDetails.get(0).geterrorHandling() == 3) {
                    //auto-release, 3 = Reject submission on error 
                    batchStausId = 7;
                    //updating entire batch to reject since error transactionIds are in error tables
                    updateTransactionTargetStatus(batchUploadId, 0, 14, 13);
                    updateTransactionStatus(batchUploadId, 0, 14, 13);

                } else if (!handlingDetails.get(0).getautoRelease() && handlingDetails.get(0).geterrorHandling() == 1) { //manual release
                    //transaction will be set to saved, batch will be set to RP
                    batchStausId = 5;
                    //we leave status alone as we already set them
                } else if (!handlingDetails.get(0).getautoRelease() && handlingDetails.get(0).geterrorHandling() == 2) {
                    //reject records
                    batchStausId = 5;
                    updateTransactionStatus(batchUploadId, 0, 14, 13);
                    updateTransactionTargetStatus(batchUploadId, 0, 14, 13);
                } else if (!handlingDetails.get(0).getautoRelease() && handlingDetails.get(0).geterrorHandling() == 3) {
                    batchStausId = 7;
                    updateTransactionStatus(batchUploadId, 0, 14, 13);
                    updateTransactionTargetStatus(batchUploadId, 0, 14, 13);
                } else if (!handlingDetails.get(0).getautoRelease() && handlingDetails.get(0).geterrorHandling() == 4) {
                    batchStausId = 5;
                    // pass
                    updateTransactionStatus(batchUploadId, 0, 14, 16);
                    updateTransactionTargetStatus(batchUploadId, 0, 14, 19);
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
                        processBatch(batch.getId());
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
    public Integer insertFailedRequiredFields(configurationFormFields cff, Integer batchUploadId) {
        return transactionInDAO.insertFailedRequiredFields(cff, batchUploadId);
    }

    @Override
    public Integer clearTransactionInErrors(Integer batchUploadId, boolean leaveFinalStatusIds) {
        return transactionInDAO.clearTransactionInErrors(batchUploadId, leaveFinalStatusIds);
    }

    /**
     * This method finds all error transactionInId in TransactionInErrors / TransactionOutErrors and update transactionIn with the appropriate error status It can be passed, reject and error
     *
     */
    @Override
    public void updateStatusForErrorTrans(Integer batchId,
            Integer statusId, boolean foroutboundProcessing) {
        transactionInDAO.updateStatusForErrorTrans(batchId, statusId, foroutboundProcessing);
    }

    @Override
    public Integer runValidations(Integer batchUploadId, Integer configId) {
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
                    errorCount = errorCount + genericValidation(cff, validationTypeId, batchUploadId, regEx);
                    break;
                //phone  calling SP to validation and insert - one statement 
                case 3:
                    errorCount = errorCount + genericValidation(cff, validationTypeId, batchUploadId, regEx);
                    break;
                // need to loop through each record / each field
                case 4:
                    errorCount = errorCount + dateValidation(cff, validationTypeId, batchUploadId);
                    break;
                //numeric   calling SQL to validation and insert - one statement      
                case 5:
                    errorCount = errorCount + genericValidation(cff, validationTypeId, batchUploadId, regEx);
                    break;
                //url - need to rethink as regExp is not validating correctly
                case 6:
                    errorCount = errorCount + urlValidation(cff, validationTypeId, batchUploadId);
                    break;
                //anything new we hope to only have to modify sp
                default:
                    errorCount = errorCount + genericValidation(cff, validationTypeId, batchUploadId, regEx);
                    break;
            }

        }
        return errorCount;
    }

    @Override
    public Integer genericValidation(configurationFormFields cff,
            Integer validationTypeId, Integer batchUploadId, String regEx) {
        return transactionInDAO.genericValidation(cff, validationTypeId, batchUploadId, regEx);
    }

    @Override
    public Integer urlValidation(configurationFormFields cff,
            Integer validationTypeId, Integer batchUploadId) {
        try {
            //1. we grab all transactionInIds for messages that are not length of 0 and not null 
            List<transactionRecords> trs = getFieldColAndValues(batchUploadId, cff);
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
            Integer validationTypeId, Integer batchUploadId) {
        try {
            //1. we grab all transactionInIds for messages that are not length of 0 and not null 
            List<transactionRecords> trs = getFieldColAndValues(batchUploadId, cff);

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
        System.out.print(date.indexOf("-"));
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
            configurationDataTranslations cdt, boolean foroutboundProcessing) {
        try {
            // 1. we get the info for that cw (fieldNo, sourceVal, targetVal rel_crosswalkData)
            List<CrosswalkData> cdList = configurationManager.getCrosswalkData(cdt.getCrosswalkId());
            //we null forcw column, we translate and insert there, we then replace
            nullForCWCol(configId, batchId, foroutboundProcessing);
            for (CrosswalkData cwd : cdList) {
                executeCWData(configId, batchId, cdt.getFieldNo(), cwd, foroutboundProcessing, cdt.getFieldId());
            }

            //we replace original F[FieldNo] column with data in forcw
            updateFieldNoWithCWData(configId, batchId, cdt.getFieldNo(), cdt.getPassClear(), foroutboundProcessing);

            //flag errors, anything row that is not null in F[FieldNo] but null in forCW
            flagCWErrors(configId, batchId, cdt, foroutboundProcessing);

            //flag as error in transactionIn or transactionOut table
            updateStatusForErrorTrans(batchId, 14, foroutboundProcessing);

            //we replace original F[FieldNo] column with data in forcw
            updateFieldNoWithCWData(configId, batchId, cdt.getFieldNo(), cdt.getPassClear(), foroutboundProcessing);

            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

    }

    @Override
    public Integer processMacro(Integer configId, Integer batchId, configurationDataTranslations cdt,
            boolean foroutboundProcessing) {
        // we clear forCW column for before we begin any translation
        nullForCWCol(configId, batchId, foroutboundProcessing);
        try {
            Macros macro = configurationManager.getMacroById(cdt.getMacroId());
            int sysError = 0;
            try {
                // we expect the target field back so we can figure out clear pass option
                sysError = sysError + executeMacro(configId, batchId, cdt, foroutboundProcessing, macro);
                // insert macro errors
                flagMacroErrors(configId, batchId, cdt, foroutboundProcessing);
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
    public void nullForCWCol(Integer configId, Integer batchId, boolean foroutboundProcessing) {
        transactionInDAO.nullForCWCol(configId, batchId, foroutboundProcessing);
    }

    @Override
    public void executeCWData(Integer configId, Integer batchId, Integer fieldNo, CrosswalkData cwd, boolean foroutboundProcessing, Integer fieldId) {
        transactionInDAO.executeCWData(configId, batchId, fieldNo, cwd, foroutboundProcessing, fieldId);
    }

    @Override
    public void updateFieldNoWithCWData(Integer configId, Integer batchId, Integer fieldNo, Integer passClear, boolean foroutboundProcessing) {
        transactionInDAO.updateFieldNoWithCWData(configId, batchId, fieldNo, passClear, foroutboundProcessing);
    }

    @Override
    public void flagCWErrors(Integer configId, Integer batchId,
            configurationDataTranslations cdt, boolean foroutboundProcessing) {
        transactionInDAO.flagCWErrors(configId, batchId, cdt, foroutboundProcessing);
    }

    @Override
    public void flagMacroErrors(Integer configId, Integer batchId,
            configurationDataTranslations cdt, boolean foroutboundProcessing) {
        transactionInDAO.flagMacroErrors(configId, batchId, cdt, foroutboundProcessing);
    }

    @Override
    public Integer executeMacro(Integer configId, Integer batchId,
            configurationDataTranslations cdt, boolean foroutboundProcessing, Macros macro) {
        return transactionInDAO.executeMacro(configId, batchId, cdt, foroutboundProcessing, macro);

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
        transactionInDAO.resetTransactionTranslatedIn(batchId, resetAll);
    }

    @Override
    public Integer copyTransactionInStatusToTarget(Integer batchId) {
        return transactionInDAO.copyTransactionInStatusToTarget(batchId);
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
            String fileWithPath = dir.getDir() + batch.getFileLocation() + batch.getoriginalFileName();
            fileWithPath = fileWithPath.replace("bowlink///", "");

            //2. we load data with my sql
            //get delimiter, get fileWithPath etc
            if (batch.getoriginalFileName().endsWith(".txt") || batch.getoriginalFileName().endsWith(".csv")) {
                sysError = sysError + insertLoadData(batch.getId(), batch.getDelimChar(), fileWithPath, loadTableName, batch.isContainsHeaderRow());

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

            //load targets - we need to loadTarget only if field for target is blank, otherwise we load what user sent
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
                     * if targetOrgCol has value, we make sure value is value
                     */
                    sysErrors = sysErrors + insertBatchUploadSummary(batch, bt);
                    if (sourceConfigId != bt.getsourceConfigId()) {
                        if (bt.getTargetOrgCol() != 0) {
                            sysErrors = sysErrors + rejectInvalidTargetOrg(batchId, bt);
                        }
                        sourceConfigId = bt.getsourceConfigId();
                    }
                }
                sysErrors = sysErrors + setStatusForErrorCode(batchId, 11, 9, false);

                //reject transactions with config that do not connections
                sysErrors = sysErrors + rejectNoConnections(batch);
                sysErrors = sysErrors + setStatusForErrorCode(batchId, 11, 10, false);

                sysErrors = sysErrors + insertBatchTargets(batchId);

                //handle duplicates, need to insert again and let it be its own row
                sysErrors = sysErrors + newEntryForMultiTargets(batchId);

                //we reset transactionTranslatedIn
                resetTransactionTranslatedIn(batchId, true);

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
                        //update loaded to rejected
                        updateTransactionStatus(batchId, 0, 9, 13);
                        return false;
                    }
                }
            }

            //at the end of loaded, we update to PR
            updateTransactionStatus(batchId, 0, 9, 10);
            updateTransactionTargetStatus(batchId, 0, 9, 10);
            batchStatusId = 3;

        } catch (Exception ex) {
            insertProcessingError(processingSysErrorId, null, batchId, null, null, null, null, false, false, ("loadBatch error" + ex.getCause()));
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
    public List<batchUploads> getAllUploadedBatches(Date fromDate, Date toDate, String searchTerm, int page, int maxResults) throws Exception {
        return transactionInDAO.getAllUploadedBatches(fromDate, toDate, searchTerm, page, maxResults);
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

            System.out.println("This Hour: " + thishour.getTime() + " Next Hour: " + nexthour.getTime());

            Integer batchesThisHour = transactionInDAO.getAllUploadedBatches(thishour.getTime(), nexthour.getTime(), "", 1, 0).size();

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

            System.out.println("Today: " + starttoday.getTime() + " Tomorrow: " + starttomorrow.getTime());

            Integer batchesToday = transactionInDAO.getAllUploadedBatches(starttoday.getTime(), starttomorrow.getTime(), "", 1, 0).size();

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

            System.out.println("This Week: " + thisweek.getTime() + " Next Week: " + nextweek.getTime());

            Integer batchesThisWeek = transactionInDAO.getAllUploadedBatches(thisweek.getTime(), nextweek.getTime(), "", 1, 0).size();

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

    /**
     * *
     */
    @Override
    public List<ConfigErrorInfo> populateErrorListByErrorCode(batchUploads batchInfo) {

        List<ConfigErrorInfo> confErrorList = new LinkedList<ConfigErrorInfo>();
        try {
            ConfigErrorInfo configErrorInfo = new ConfigErrorInfo();
            configErrorInfo.setBatchId(batchInfo.getId());

            List<TransErrorDetail> tedList = getTransErrorDetailsForNoRptFields(batchInfo.getId(), Arrays.asList(5));
            if (tedList.size() > 0) {
                configErrorInfo.setErrorViewId(1);
                configErrorInfo.setTransErrorDetails(tedList);
                configErrorInfo.setMessageTypeName("Submission Process Errored");
                confErrorList.add(configErrorInfo);
            }
            configErrorInfo = new ConfigErrorInfo();
            tedList = getTransErrorDetailsForNoRptFields(batchInfo.getId(), Arrays.asList(7, 8, 10));
            if (tedList.size() > 0) {
                configErrorInfo.setErrorViewId(1);
                configErrorInfo.setTransErrorDetails(tedList);
                configErrorInfo.setMessageTypeName("Configuration Errors");
                confErrorList.add(configErrorInfo);
            }
            /**
             * now get invalid configIds, errorId 6 - these are tied to transaction but not reportable fields since we don't know what configId it is. we don't know column that holds the field either since it didn't match with any for org, we display the first 4 columns
             */
            configErrorInfo = new ConfigErrorInfo();
            configErrorInfo.setBatchId(batchInfo.getId());
            tedList = getTransErrorDetailsForInvConfig(batchInfo.getId());
            if (tedList.size() > 0) {
                //we grab f1-f4 and report off those
                configErrorInfo.setErrorViewId(2);
                configErrorInfo.setMessageTypeName("Configurations Unknown");
                configErrorInfo.setRptFieldHeading1("Field 1");
                configErrorInfo.setRptFieldHeading2("Field 2");
                configErrorInfo.setRptFieldHeading3("Field 3");
                configErrorInfo.setRptFieldHeading4("Field 4");
                configErrorInfo.setTransErrorDetails(tedList);
                confErrorList.add(configErrorInfo);
            }
            //now get the rest by configId
            List<ConfigErrorInfo> confErrorListByConfig = getErrorConfigForBatch(batchInfo.getId());
            for (ConfigErrorInfo cei : confErrorListByConfig) {
                //we populate the rest of the info 1. headers
                configErrorInfo.setBatchId(batchInfo.getId());
                configErrorInfo = getHeaderForConfigErrorInfo(batchInfo.getId(), cei);
                //add error details
                configErrorInfo.setTransErrorDetails(getTransErrorDetails(batchInfo, configErrorInfo));
            }
            confErrorList.addAll(confErrorListByConfig);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("populateErrorListByErrorCode " + ex.getCause());
        }
        return confErrorList;
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
    public List<TransErrorDetail> getTransErrorDetails(batchUploads batchInfo, ConfigErrorInfo configErrorInfo) {
        //get field values
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
                    batch.setTransTotalNotFinal(getRecordCounts(batch.getId(), finalStatusIds, true, false));
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

}
