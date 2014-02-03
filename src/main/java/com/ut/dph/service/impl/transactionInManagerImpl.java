/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.dph.service.impl;

import com.ut.dph.dao.messageTypeDAO;
import com.ut.dph.dao.transactionInDAO;
import com.ut.dph.model.batchUploadSummary;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.transactionAttachment;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import com.ut.dph.model.transactionRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.model.custom.ConfigForInsert;
import com.ut.dph.reference.fileSystem;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.messageTypeManager;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.ut.dph.service.transactionInManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
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

    @Override
    @Transactional
    public String getFieldValue(String tableName, String tableCol, int idValue) {
        return transactionInDAO.getFieldValue(tableName, tableCol, idValue);
    }

    @Override
    @Transactional
    public List<fieldSelectOptions> getFieldSelectOptions(int fieldId, int configId) {
        return transactionInDAO.getFieldSelectOptions(fieldId, configId);
    }

    @Override
    @Transactional
    public Integer submitBatchUpload(batchUploads batchUpload) {
        return transactionInDAO.submitBatchUpload(batchUpload);
    }

    @Override
    @Transactional
    public void submitBatchUploadSummary(batchUploadSummary summary) {
        transactionInDAO.submitBatchUploadSummary(summary);
    }

    @Override
    @Transactional
    public void submitBatchUploadChanges(batchUploads batchUpload) {
        transactionInDAO.submitBatchUploadChanges(batchUpload);
    }

    @Override
    @Transactional
    public Integer submitTransactionIn(transactionIn transactionIn) {
        return transactionInDAO.submitTransactionIn(transactionIn);
    }

    @Override
    @Transactional
    public void submitTransactionInChanges(transactionIn transactionIn) {
        transactionInDAO.submitTransactionInChanges(transactionIn);
    }

    @Override
    @Transactional
    public Integer submitTransactionInRecords(transactionInRecords records) {
        return transactionInDAO.submitTransactionInRecords(records);
    }

    @Override
    @Transactional
    public void submitTransactionInRecordsUpdates(transactionInRecords records) {
        transactionInDAO.submitTransactionInRecordsUpdates(records);
    }

    @Override
    @Transactional
    public void submitTransactionTranslatedInRecords(int transactionId, int transactionRecordId, int configId) {
        transactionInDAO.submitTransactionTranslatedInRecords(transactionId, transactionRecordId, configId);
    }

    @Override
    @Transactional
    public List<batchUploads> getpendingBatches(int userId, int orgId, int page, int maxResults) {
        return transactionInDAO.getpendingBatches(userId, orgId, page, maxResults);
    }

    @Override
    @Transactional
    public List<batchUploads> findBatches(List<batchUploads> batches, String searchTerm) {
        return transactionInDAO.findBatches(batches, searchTerm);
    }

    @Override
    @Transactional
    public List<transactionIn> getBatchTransactions(int batchId, int userId) {
        return transactionInDAO.getBatchTransactions(batchId, userId);
    }

    @Override
    @Transactional
    public List<batchUploads> getsentBatches(int userId, int orgId, int page, int maxResults) {
        return transactionInDAO.getsentBatches(userId, orgId, page, maxResults);
    }

    @Override
    @Transactional
    public batchUploads getBatchDetails(int batchId) {
        return transactionInDAO.getBatchDetails(batchId);
    }

    @Override
    @Transactional
    public transactionIn getTransactionDetails(int transactionId) {
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
    public void submitTransactionTargetChanges(transactionTarget transactionTarget) {
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
    public String uploadAttachment(MultipartFile fileUpload, String orgName) {

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
    public Integer submitAttachment(transactionAttachment attachment) {
        return transactionInDAO.submitAttachment(attachment);
    }

    @Override
    @Transactional
    public transactionAttachment getAttachmentById(int attachmentId) {
        return transactionInDAO.getAttachmentById(attachmentId);
    }

    @Override
    @Transactional
    public void submitAttachmentChanges(transactionAttachment attachment) {
        transactionInDAO.submitAttachmentChanges(attachment);
    }

    @Override
    @Transactional
    public List<transactionAttachment> getAttachmentsByTransactionId(int transactionInId) {
        return transactionInDAO.getAttachmentsByTransactionId(transactionInId);
    }

    @Override
    @Transactional
    public void removeAttachmentById(int attachmentId) {

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
    public List<Integer> getConfigIdsForBatch(int batchUploadId) {
        return transactionInDAO.getConfigIdsForBatch(batchUploadId);
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
    public boolean clearMessageTables(int batchId) {
        List<String> mts = transactionInDAO.getMessageTables();
        try {
            for (String mt : mts) {
                transactionInDAO.clearMessageTableForBatch(batchId, mt);
            }
            return true;
        } catch (Exception e) {
            System.out.println("clearMessageTables " + e.getStackTrace());
            return false;

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
    public Map<String, String> uploadBatchFile(int configId, MultipartFile fileUpload) {

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

            if (delimCount < 10) {
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

    @Override
    public List<batchUploads> getuploadedBatches(int userId, int orgId) {
        return transactionInDAO.getuploadedBatches(userId, orgId);
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
    public boolean processBatch(int batchUploadId) {

        boolean successfulBatch = false;
        /**
         * get batch details *
         */
        batchUploads batch = getBatchDetails(batchUploadId);

        /**
         * ERG are loaded already, we load all other files maybe move loading? *
         */
        if (batch.gettransportMethodId() != 2) { // 2 is ERG

            //set batch to SBP - 4
            updateBatchStatus(batchUploadId, 4, "startDateTime");

            //let's clear all tables first as we are starting over
            successfulBatch = clearTransactionTables(batchUploadId);

            //loading batch will take it all the way to loaded (9) status for transactions and SSL (3) for batch 
            successfulBatch = loadBatch(batchUploadId);

            //after loading is successful we update to SSL
            updateBatchStatus(batchUploadId, 3, "startDateTime");

            //get batch details again for next round
            batch = getBatchDetails(batchUploadId);

        }

        /**
         * this should be the same point of both ERG and Uploaded File *
         */
        //Check to make sure the file is valid for processing, valid file is a batch with SSL (3) or SR*
        successfulBatch = true;

        if ((batch.getstatusId() == 3 || batch.getstatusId() == 6) && successfulBatch) {

            //set batch to SBP - 4*
            updateBatchStatus(batchUploadId, 4, "startDateTime");

            /**
             * we get all the configurations *
             */
            List<Integer> configIds = getConfigIdsForBatch(batchUploadId);

            for (Integer configId : configIds) {

                /**
                 * we need to run all checks before insert regardless *
                 */
                /**
                 * check R/O *
                 */
                List<configurationFormFields> reqFields = getRequiredFieldsForConfig(configId);
                /**
                 * we loop each field and flag errors *
                 */
                for (configurationFormFields cff : reqFields) {
                    insertFailedRequiredFields(cff, batchUploadId);
                }
                //update status of the failed records to ERR - 14
                updateStatusForErrorTrans(batchUploadId, 14);

                /**
                 * run validation*
                 */
                runValidations(batchUploadId, configId);
                //update status of the failed records to ERR - 14
                updateStatusForErrorTrans(batchUploadId, 14);

                /**
                 * run cw/macros *
                 */
                /**
                 * we check configuration details, remove rejected records from transactionTranslatedIn, pass records stays * *
                 */
                //TODO we figure out if we pass a batch, reject the batch, etc here
                //we have REJ - 13, Error - 14, Passed 16 for transactions
            }

        }

        /**
         * somewhere in here we need to make sure all configs are set to handle files the same way, if not, we will go with manual release *
         */
        boolean autoRelease = true;
        if (autoRelease) {
            /**
             * inserts for batches that passes *
             */
            if (!insertTransactions(batchUploadId)) {
                successfulBatch = false;
                /**
                 * something went wrong, we removed all inserted entries *
                 */
                clearMessageTables(batchUploadId);
                /**
                 * we leave transaction status alone and flag batch as error during processing -SPE*
                 */
                updateBatchStatus(batchUploadId, 28, "endDateTime");
            }

            /**
             * set batch to SPC 24*
             */
            if (successfulBatch) {
                updateBatchStatus(batchUploadId, 24, "endDateTime");
                /**
                 * set all REL - 10 records to 19 *
                 */
                updateTransactionStatus(batchUploadId, 12, 19);
                /**
                 * we update total record counts for batch *
                 */
            }
        }

        return successfulBatch;
    }

    /**
     * this is called by the scheduler It selects all batch with a status of SSA Loads them SSL - Trans - L Starts the Process - SBP - Parses
     *
     * 1. Validate R/O - ERG records will just pass we select batches that are 2. Validate Fields - ERG records will just pass 3. Apply CW / Macros - ERG records should just pass as what is being inserted is our internal standard 4. We insert
     *
     * In between don't forget to change status Might need wrapper to align upload file process
     */
    @Override
    public boolean processBatches() {
		//0. grab all batches with SSA - 2

        //2. validate type
        //3. apply cw / macros to check for valid data
        //4. check auto release / manual status 
        //5. assuming all is well, call processTransactions(batchUploadId);
        /**
         * status at this point *
         */
        return false;
    }

    @Override
    public void updateBatchStatus(Integer batchUploadId, Integer statusId, String timeField) {
        transactionInDAO.updateBatchStatus(batchUploadId, statusId, timeField);
    }

    @Override
    public void updateTransactionStatus(Integer batchUploadId, Integer fromStatusId, Integer toStatusId) {
        transactionInDAO.updateTransactionStatus(batchUploadId, fromStatusId, toStatusId);
    }

    /**
     * provided the batch status is not one of the delivery status (22 SDL, 23 SDC) what would we like clear batch to do 1. Change batch process to SBP to nothing can be touch 2. remove records from message tables 3. figure out what status to set batch *
     */
    @Override
    public boolean clearBatch(Integer batchUploadId) {
        boolean canDelete = transactionInDAO.allowBatchClear(batchUploadId);
        boolean cleared = false;
        if (canDelete) {
            /**
             * we remove from message tables*
             */
            //TODO how much should we clear? Is it different for ERG and Upload?

            cleared = clearMessageTables(batchUploadId);
            if (cleared) {
                int toBatchStatusId = 3; //SSA
                if (getBatchDetails(batchUploadId).gettransportMethodId() == 2) {
                    cleared = clearTransactionInErrors(batchUploadId);
                    toBatchStatusId = 5;
                    transactionInDAO.updateTransactionStatus(batchUploadId, 0, 15);
                } else {
                    //we clear transactionInRecords here as for batch upload we start over
                    cleared = clearTransactionTables(batchUploadId);
                }
                transactionInDAO.updateBatchStatus(batchUploadId, toBatchStatusId, "");
            }
        }
        return cleared;
    }

    @Override
    public boolean setDoNotProcess(Integer batchId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean clearTransactionInRecords(Integer batchUploadId) {
        return transactionInDAO.clearTransactionInRecords(batchUploadId);
    }

    /**
     * This method assumes that all records are validated and ready for insert We loop through each configuration and insert Transaction status will remain unchanged. *
     */
    @Override
    public boolean insertTransactions(Integer batchUploadId) {
        List<Integer> configIds = getConfigIdsForBatch(batchUploadId);
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

    /**
     * this process will load an upload file with status of SSA and take it all the way to SSL
     *
     * 1. read file 2. parse row by row and a. figure out config b. insert into transactionIn c. insert into transacitonTarget d. flag transactions as Loaded or Invalid *
     */
    @Override
    public boolean loadBatch(Integer batchUploadId) {
        /**
         * we read uploaded file, line by line*
         */
        /**
         * we look for config id*
         */
        /**
         * if we find we insert into transactionIn, transactionInRecords, transactionTranslatedIn, transactionTarget *
         */
        /**
         * only transactions with valid configId will make it into transactionTranslatedIn We will not find INVALID records in transactionTranslatedIn
         */
        /**
         * when we are done, the batch will be SSL and records will either be INVALID or Loaded *
         */
        return true;
    }

    @Override
    public boolean clearTransactionTranslatedIn(Integer batchUploadId) {
        return transactionInDAO.clearTransactionTranslatedIn(batchUploadId);
    }

    @Override
    public boolean clearTransactionTables(Integer batchUploadId) {
        boolean cleared = false;
        //we clear transactionTranslatedIn
        cleared = clearTransactionTranslatedIn(batchUploadId);
        //we clear transactionInRecords
        if (cleared) {
            cleared = clearTransactionInRecords(batchUploadId);
        }
        //we clear transactionTarget
        if (cleared) {
            cleared = clearTransactionTarget(batchUploadId);
        }
        if (cleared) {
            cleared = clearTransactionInErrors(batchUploadId);
        }
        //we clear transactionIn
        if (cleared) {
            cleared = clearTransactionTarget(batchUploadId);
        }
        if (!cleared) {
            flagAndEmailAdmin(batchUploadId);
        }
        return cleared;
    }

    @Override
    public boolean clearTransactionTarget(Integer batchUploadId) {
        return transactionInDAO.clearTransactionTarget(batchUploadId);
    }

    @Override
    public boolean clearTransactionIn(Integer batchUploadId) {
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
    public boolean insertFailedRequiredFields(configurationFormFields cff, Integer batchUploadId) {
        return transactionInDAO.insertFailedRequiredFields(cff, batchUploadId);
    }

    @Override
    public boolean clearTransactionInErrors(Integer batchUploadId) {
        return transactionInDAO.clearTransactionInErrors(batchUploadId);
    }

    /**
     * This method finds all error transactionInId in TransactionInErrors and update transactionIn with the appropriate error status It can be passed, reject and error
     *
     */
    @Override
    public void updateStatusForErrorTrans(Integer batchUploadId, Integer statusId) {
        transactionInDAO.updateStatusForErrorTrans(batchUploadId, statusId);
    }

    @Override
    public boolean runValidations(Integer batchUploadId, Integer configId) {
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
                    genericValidation(cff, validationTypeId, batchUploadId, regEx);
                    break;
                //phone  calling SP to validation and insert - one statement 
                case 3:
                    genericValidation(cff, validationTypeId, batchUploadId, regEx);
                    break;
                // need to loop through each record / each field
                case 4:
                    dateValidation(cff, validationTypeId, batchUploadId);
                    break;
                //numeric   calling SQL to validation and insert - one statement      
                case 5:
                    genericValidation(cff, validationTypeId, batchUploadId, regEx);
                    break;
                //url - need to rethink as regExp is not validating correctly
                case 6:
                    urlValidation(cff, validationTypeId, batchUploadId);
                    break;
                //anything new we hope to only have to modify sp
                default:
                    genericValidation(cff, validationTypeId, batchUploadId, regEx);
                    break;
            }

        }
        return true;
    }

    @Override
    public void genericValidation(configurationFormFields cff,
            Integer validationTypeId, Integer batchUploadId, String regEx) {
        transactionInDAO.genericValidation(cff, validationTypeId, batchUploadId, regEx);
    }

    @Override
    public void urlValidation(configurationFormFields cff,
            Integer validationTypeId, Integer batchUploadId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void dateValidation(configurationFormFields cff, Integer validationTypeId, Integer batchUploadId) {
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
        			insertDateError(tr, cff, batchUploadId);
	            }
	    		
        	}
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
        return transactionInDAO.getDateColAndValues(batchUploadId, cff);
    }

    /** this method checks the potential day formats that a user can send in.  
     * We will check for long days such as February 2, 2014 Wednesday etc.
     * Only accepting US format of month - day - year
     * February 2, 2014 Sunday 2:00:02 PM
     * February 2, 2014
     * 02-02-2014
     * 02/02/2014
     * 02/02/14
     * 02/2/14 12:02:00 PM
     * etc.
     */
    /** this method returns the pattern date is in so we can convert it properly and 
     * translate into mysql datetime insert format
     */
    public Date convertDate(String input) {
    	 
        // some regular expression
        String time = "(\\s(([01]?\\d)|(2[0123]))[:](([012345]\\d)|(60))"
            + "[:](([012345]\\d)|(60)))?"; // with a space before, zero or one time
     
        // no check for leap years (Schaltjahr)
        // and 31.02.2006 will also be correct
        String day = "(([12]\\d)|(3[01])|(0?[1-9]))"; // 01 up to 31
        //String month = "((1[012])|(0\\d))"; // 01 up to 12
        String month = "((1[012])|(\\d))"; // 01 up to 12
        String year = "\\d{4}";
     
        // define here all date format
        String date = input.replace("/", "-");
        date = date.replace(".", "-");
        //ArrayList<Pattern> patterns = new ArrayList<Pattern>();
        Pattern pattern1 = Pattern.compile(day + "-" + month + "-" + year + time);
        Pattern pattern2 = Pattern.compile(year + "-" + month + "-" + day + time);
        Pattern pattern3 = Pattern.compile(month + "-" + day + "-" + year + time);
        // check dates
        
          if (pattern1.matcher(date).matches()) {
        	  //we have d-m-y format, we transform and return date
        	  try {
        		  SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-YYYY");
            	  dateformat.setLenient(false);
            	  Date dateValue = dateformat.parse(date);
            	  return dateValue;
        	  } catch (Exception e) {
        		  e.printStackTrace();
        		  return null;
        	  } 
          } else if (pattern2.matcher(date).matches()) {
            	//we have d-m-y format, we transform and return date
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
              	//we have d-m-y format, we transform and return date
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
    public void insertDateError(transactionRecords tr,
            configurationFormFields cff, Integer batchUploadId) {
    	transactionInDAO.insertDateError(tr, cff, batchUploadId);
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
          } else  {
        	  return false;
          }
      }
}
