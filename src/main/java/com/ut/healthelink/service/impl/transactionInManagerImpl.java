/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.service.impl;

import com.ut.healthelink.model.activityReportList;
import com.ut.healthelink.dao.messageTypeDAO;
import com.ut.healthelink.dao.transactionInDAO;
import com.ut.healthelink.dao.transactionOutDAO;
import com.ut.healthelink.model.CrosswalkData;
import com.ut.healthelink.model.Macros;
import com.ut.healthelink.model.Organization;
import com.ut.healthelink.model.MoveFilesLog;
import com.ut.healthelink.model.Transaction;
import com.ut.healthelink.model.TransactionInError;
import com.ut.healthelink.model.User;
import com.ut.healthelink.model.UserActivity;
import com.ut.healthelink.model.WSMessagesIn;
import com.ut.healthelink.model.batchDownloads;
import com.ut.healthelink.model.batchClearAfterDelivery;
import com.ut.healthelink.model.batchMultipleTargets;
import com.ut.healthelink.model.batchUploadSummary;
import com.ut.healthelink.model.batchUploads;
import com.ut.healthelink.model.configurationConnection;
import com.ut.healthelink.model.configurationDataTranslations;
import com.ut.healthelink.model.configurationExcelDetails;
import com.ut.healthelink.model.configurationFTPFields;
import com.ut.healthelink.model.configurationFormFields;
import com.ut.healthelink.model.configurationMessageSpecs;
import com.ut.healthelink.model.configurationRhapsodyFields;
import com.ut.healthelink.model.configurationTransport;
import com.ut.healthelink.model.configurationWebServiceFields;
import com.ut.healthelink.model.fieldSelectOptions;
import com.ut.healthelink.model.mailMessage;
import com.ut.healthelink.model.transactionAttachment;
import com.ut.healthelink.model.transactionIn;
import com.ut.healthelink.model.transactionInRecords;
import com.ut.healthelink.model.transactionRecords;
import com.ut.healthelink.model.transactionTarget;
import com.ut.healthelink.model.custom.ConfigErrorInfo;
import com.ut.healthelink.model.custom.ConfigForInsert;
import com.ut.healthelink.model.custom.IdAndFieldValue;
import com.ut.healthelink.model.custom.TransErrorDetail;
import com.ut.healthelink.model.custom.TransErrorDetailDisplay;
import com.ut.healthelink.model.lutables.lu_ProcessStatus;
import com.ut.healthelink.model.messagePatients;
import com.ut.healthelink.model.referralActivityExports;
import com.ut.healthelink.model.systemSummary;
import com.ut.healthelink.model.transactionOutNotes;
import com.ut.healthelink.reference.fileSystem;
import com.ut.healthelink.service.CCDtoTxt;
import com.ut.healthelink.service.configurationManager;
import com.ut.healthelink.service.configurationTransportManager;
import com.ut.healthelink.service.emailMessageManager;
import com.ut.healthelink.service.fileManager;
import com.ut.healthelink.service.hl7toTxt;
import com.ut.healthelink.service.messageTypeManager;
import com.ut.healthelink.service.organizationManager;
import com.ut.healthelink.service.sysAdminManager;
import com.ut.healthelink.service.userManager;
import com.ut.healthelink.service.utilManager;
import com.ut.healthelink.service.xlsToTxt;
import com.ut.healthelink.service.xlsxToTxt;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.ut.healthelink.service.transactionInManager;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author chadmccue
 */
@Service
public class transactionInManagerImpl implements transactionInManager {

    @Resource(name = "myProps")
    private Properties myProps;

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
    
    @Autowired
    private xlsxToTxt xlsxtotxt;
    
    @Autowired
    private xlsToTxt xlstotxt;
    
    @Autowired
    private emailMessageManager emailManager;

    @Autowired
    private utilManager utilmanager;

    private int processingSysErrorId = 5;

    //final status Ids
    private List<Integer> finalStatusIds = Arrays.asList(11, 12, 13, 16);

    //reject Ids
    private List<Integer> rejectIds = Arrays.asList(13, 14);

    private String directoryPath = System.getProperty("directory.rootDir");

    private String archivePath = (directoryPath + "archivesIn/");

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
    public List<transactionIn> getBatchTransactions(int batchId, int userId, List<Integer> messageTypeList, List<Integer> OrgList) throws Exception {
        return transactionInDAO.getBatchTransactions(batchId, userId, messageTypeList, OrgList);
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
                sysErrorCount = sysErrorCount + transactionInDAO.clearMessageTable(batchId, mt, 0);
            }
            return sysErrorCount;
        } catch (Exception e) {
            System.err.println("clearMessageTables " + e.getStackTrace());
            return 1;

        }
    }

    @Override
    public Integer clearMessageTablesByTransactionInId(int transactionInId) {
        List<String> mts = transactionInDAO.getMessageTables();
        Integer sysErrorCount = 0;
        try {
            for (String mt : mts) {
                sysErrorCount = sysErrorCount + transactionInDAO.clearMessageTable(0, mt, transactionInId);
            }
            return sysErrorCount;
        } catch (Exception e) {
            System.err.println("clearMessageTablesByTransactionInId " + e.getStackTrace());
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
        UserActivity ua = new UserActivity();
        Integer batchStausId = 29;
        List<Integer> errorStatusIds = Arrays.asList(11, 13, 14, 16);
        //get batch details
        batchUploads batch = getBatchDetails(batchUploadId);
        //this should be the same point of both ERG and Uploaded File *
        Integer systemErrorCount = 0;
        // Check to make sure the file is valid for processing, valid file is a batch with SSL (3) or SR (6)*

        boolean insertTargets = false;
        // we should only insert for batches that are just loaded
        if (batch.getstatusId() == 36 || batch.getstatusId() == 43) {
            insertTargets = true;
        }
        if ((batch.getstatusId() == 3 || batch.getstatusId() == 6
                || batch.getstatusId() == 36 || batch.getstatusId() == 43)) {
            /**
             * insert log*
             */
            try {
                //log user activity
                ua.setUserId(0);
                ua.setFeatureId(0);
                ua.setAccessMethod("System");
                ua.setActivity("System Processed File");
                ua.setBatchUploadId(batchUploadId);
                usermanager.insertUserLog(ua);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println("transactionId - insert user log" + ex.toString());
            }

            // set batch to SBP - 4*
            updateBatchStatus(batchUploadId, 4, "startDateTime");

            //clear transactionInError table for batch, if do not clear errors is true, we skip this.
            if (!doNotClearErrors) {
                systemErrorCount = systemErrorCount + clearTransactionInErrors(batchUploadId, true);
                cleanAuditErrorTable(batchUploadId);
            }

            List<Integer> configIds = getConfigIdsForBatch(batchUploadId, false, transactionId);

            for (Integer configId : configIds) {
                //we need to run all checks before insert regardless *
                /**
                 * we are reordering 1. cw/macro, 2. required and 3. validate *
                 */
                // 1. grab the configurationDataTranslations and run cw/macros
                List<configurationDataTranslations> dataTranslations = configurationManager
                        .getDataTranslationsWithFieldNo(configId, 1); //while processing
                for (configurationDataTranslations cdt : dataTranslations) {
                    if (cdt.getCrosswalkId() != 0 && cdt.getFieldType() != 6) {
                        systemErrorCount = systemErrorCount + processCrosswalk(configId, batchUploadId, cdt, false, transactionId);
                    } else if (cdt.getMacroId() != 0 && cdt.getFieldType() != 6) {
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

                /**
                 * targets should only be inserted if it hasn't gone through this loop already *
                 */
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
                                /**
                                 * need to check if there is a sourceSubOrgId, if so, we need to check to make sure the sourceSubOrgId belong to the sending org
                                 *
                                 */
                                if (bt.getSourceSubOrgCol() != 0) {
                                    //reject
                                    systemErrorCount = systemErrorCount + rejectInvalidSourceSubOrg(batch, bt, true);
                                    systemErrorCount = systemErrorCount + setStatusForErrorCode(batchId, 11, 23, false);
                                    /**
                                     * update transactionIn's SourceSubOrgId *
                                     */
                                    systemErrorCount = systemErrorCount + updateSSOrgIdTransactionIn(batch, bt);
                                }

                            }
                        }
                        systemErrorCount = systemErrorCount + setStatusForErrorCode(batchId, 11, 9, false);

                        //reject transactions with config that do not connections
                        systemErrorCount = systemErrorCount + rejectNoConnections(batch);
                        systemErrorCount = systemErrorCount + setStatusForErrorCode(batchId, 11, 10, false);

                        systemErrorCount = systemErrorCount + insertBatchTargets(batchId);

                        //handle duplicates, need to insert again and let it be its own row
                        systemErrorCount = systemErrorCount + newEntryForMultiTargets(batchId);

                        /**
                         * update tt, batchuploadsummary *
                         */
                        systemErrorCount = systemErrorCount + updateSSOrgIdUploadSummary(batch);

                        systemErrorCount = systemErrorCount + updateSSOrgIdTransactionTarget(batch);

                    }
                }
                /**
                 * end of inserting target *
                 */

                /**
                 * we apply post processing rules here - categoryId 3 *
                 */
                //1. we loop it by config
                for (Integer postConfigId : configIds) {
                    List<configurationDataTranslations> postDataTranslations = configurationManager
                            .getDataTranslationsWithFieldNo(configId, 3); //while processing
                    for (configurationDataTranslations cdt : postDataTranslations) {
                        systemErrorCount = systemErrorCount + processMacro(postConfigId, batchUploadId, cdt, false, transactionId);
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

                if (getRecordCounts(batchUploadId, finalStatusIds, false, false) > 0 && batch.getstatusId() == 6) {
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
                            && getRecordCounts(batchUploadId, finalStatusIds, false, false) > 0) {
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
                    if (getRecordCounts(batchUploadId, finalStatusIds, false, false) > 0) {
                        //there are records not in final status
                        batchStausId = 7;
                        //updating entire batch to reject since error transactionIds are in error tables
                        updateTransactionTargetStatus(batchUploadId, transactionId, 14, 13);
                        updateTransactionStatus(batchUploadId, transactionId, 14, 13);
                    } else {
                        batchStausId = 6;
                    }
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

            //we finish processing, we need to alert admin if there are any records there are rejected
            /**
             * we check batch to see if the batch has any rejected records. if it does, we send an email to notify reject.email in properties file
             *
             */
            Integer rejectedCount = getRecordCounts(batch.getId(), rejectIds, false, true);
            
            if (rejectedCount == batch.gettotalRecordCount()) {
             	sendEmailToAdmin((new Date() + "<br/>Please login and review. Entire batch failed.  <br/>Batch Id -  " + batch.getId() + "<br/> UT Batch Name " + batch.getutBatchName() + " <br/>Original batch file name - " + batch.getoriginalFileName()), "Entire Batch Failed");
            } else if (rejectedCount > 0) {
                sendRejectNotification(batch, rejectedCount);
            }

        } // end of single batch insert 

        /**
         * update log with transactionInIds *
         */
        try {
            if (ua != null) {
                if (getTransactionInIdsFromBatch(batchUploadId).toString().replace("[", "").replace("]", "").length() > 100) {
                    ua.setTransactionInIds(getTransactionInIdsFromBatch(batchUploadId).toString().replace("[", "").replace("]", ""));
                } else {
                    ua.setTransactionInIds(getTransactionInIdsFromBatch(batchUploadId).toString().replace("[", "").replace("]", ""));
                }
                usermanager.updateUserActivity(ua);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("process batch - update user log" + ex.toString());
        }
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
            List<batchUploads> batches = getBatchesByStatusIds(Arrays.asList(3, 6, 36));
            if (batches != null && batches.size() != 0) {
                //we loop and process
                for (batchUploads batch : batches) {
                    try {
                        processBatch(batch.getId(), false, 0);
                    } catch (Exception ex) {
                        setBatchToError(batch.getId(), ("Errored at processBatches  " + ex.toString()));
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
                if (getBatchDetails(batchUploadId).gettransportMethodId() == 2 || getBatchDetails(batchUploadId).gettransportMethodId() == 42) {
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
    public Integer clearTransactionInRecords(Integer batchUploadId, Integer transactionInId) {
        return transactionInDAO.clearTransactionInRecords(batchUploadId, transactionInId);
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
    public Integer clearTransactionTranslatedIn(Integer batchUploadId, Integer transactionInId) {
        return transactionInDAO.clearTransactionTranslatedIn(batchUploadId, transactionInId);
    }

    @Override
    public Integer clearTransactionTables(Integer batchUploadId, boolean leaveFinalStatusIds) {
        //TODO have in transaction block for roll back?
        //we clear transactionTranslatedIn
        Integer cleared = clearTransactionTranslatedIn(batchUploadId, 0);
        //we clear transactionInRecords
        cleared = cleared + clearTransactionInRecords(batchUploadId, 0);
        //clear batchDownloadSummary
        cleared = cleared + clearBatchDownloadSummaryByUploadBatchId(batchUploadId);
        //clear transactionoutrecords
        cleared = cleared + clearTransactionOutRecordsByUploadBatchId(batchUploadId);
        cleared = cleared + clearTransactionTranslatedOutByUploadBatchId(batchUploadId);
        cleared = cleared + clearTransactionOutErrorsByUploadBatchId(batchUploadId);
        //we clear transactionTarget
        List<Integer> batchDLIds = getBatchDownloadIdsFromUploadId(batchUploadId);
        if (batchDLIds.size() > 0) {
            //need to get batchdownload Ids for transactiontargets
            cleared = cleared + clearTransactionTarget(batchUploadId);
            cleared = cleared + clearBatchDownloads(batchDLIds);
        }
        cleared = cleared + clearTransactionInErrors(batchUploadId, leaveFinalStatusIds);
        cleared = cleared + clearBatchUploadSummary(batchUploadId);
        cleared = cleared + clearMessageTables(batchUploadId);
        cleared = cleared + clearTransactionTranslatedListIn(batchUploadId);
        
        //we clear transactionIn
        cleared = cleared + clearTransactionIn(batchUploadId);
        //at SDC status, we have download batches, we need to clear those too

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
                    if (tr.getfieldValue().length() != 0) {
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
            }
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            insertProcessingError(processingSysErrorId, cff.getconfigId(), batchUploadId, cff.getFieldNo(), null, null, validationTypeId, false, false, (ex.getClass() + " " + ex.toString()));
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
                    if (tr.getfieldValue().length() != 0) {
                        //sql is picking up dates in mysql format and it is not massive inserting, running this check to avoid unnecessary sql call
                        //System.out.println(tr.getFieldValue());
                        //we check long dates
                        Date dateValue = null;
                        String mySQLDate = chkMySQLDate(tr.getFieldValue());

                        if (dateValue == null && mySQLDate.equalsIgnoreCase("")) {
                            dateValue = convertLongDate(tr.getFieldValue());
                        }
                        if (dateValue == null && mySQLDate.equalsIgnoreCase("")) {
                            dateValue = convertDate(tr.getfieldValue());
                        }

                        String formattedDate = null;
                        if (dateValue != null && mySQLDate.equalsIgnoreCase("")) {
                            formattedDate = formatDateForDB(dateValue);
                            //3. if it converts, we update the column value
                            updateFieldValue(tr, formattedDate);
                        }

                        if (formattedDate == null && (mySQLDate.equalsIgnoreCase("") || mySQLDate.equalsIgnoreCase("ERROR"))) {
                            insertValidationError(tr, cff, batchUploadId);
                        }
                    }
                }
            }
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            insertProcessingError(processingSysErrorId, cff.getconfigId(), batchUploadId, cff.getFieldNo(), null, null, validationTypeId, false, false, (ex.getClass() + " " + ex.toString()));
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
        String date = input.replaceAll("/", "-");
        date = date.replaceAll("\\.", "-");
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
    public List<Integer> getFeedbackReportConnection(int configId, int targetorgId) {
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
            /**
             * this method converts February 29 to March 1, we need to run through check two to make sure it is valid *
             */
            if (!recheckLongDate(dateValue, date.toString())) {
                return null;
            }
        } catch (Exception e) {
        }
        return date;
    }

    public String chkMySQLDate(String dateValue) {

        // some regular expression
        String time = "(\\s(([01]?\\d)|(2[0123]))[:](([012345]\\d)|(60))"
                + "[:](([012345]\\d)|(60)))?"; // with a space before, zero or one time

        // no check for leap years (Schaltjahr)
        // and 31.02.2006 will also be correct
        String day = "(([12]\\d)|(3[01])|(0?[1-9]))"; // 01 up to 31
        String month = "((1[012])|(0\\d))"; // 01 up to 12
        String year = "\\d{4}";

        // define here all date format
        String date = dateValue.replaceAll("/", "-");
        date = date.replaceAll("\\.", "-");
        Pattern pattern = Pattern.compile(year + "-" + month + "-" + day + time);

        // check dates
        if (pattern.matcher(date).matches()) {
            try {
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                dateformat.setLenient(false);
                dateformat.parse(date);
                return "Valid";
            } catch (Exception e) {
                //e.printStackTrace();
                return "Error";
            }
        } else {
            return "";
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
            Integer errors = 0;
            //we null forcw column, we translate and insert there, we then replace
            nullForCWCol(configId, batchId, foroutboundProcessing, transactionId);
            //we check to see if field value contains a list defined by UT delimiter
            List<Integer> cwMultiList = checkCWFieldForList(configId, batchId, cdt, foroutboundProcessing, transactionId);
            if (cwMultiList.size() > 0) {
            	List<CrosswalkData> cdList = getCrosswalkDataForBatch(cdt, batchId, foroutboundProcessing, transactionId);
                // we loop through each field value in the list and apply cw
                errors = processMultiValueCWData(configId, batchId, cdt, cdList, foroutboundProcessing, transactionId);
            } else {
            	/**
                for (CrosswalkData cwd : cdList) {
                    executeSingleValueCWData(configId, batchId, cdt.getFieldNo(), cwd, foroutboundProcessing, cdt.getFieldId(), transactionId);
                }
                **/
            	Integer returnCW = executeCWDataForSingleFieldValue(configId, batchId, cdt, foroutboundProcessing, transactionId);
            	//replacing with single query that will update entire cwlist
                if (cdt.getPassClear() == 1) {
                    //flag errors, anything row that is not null in F[FieldNo] but null in forCW
                    flagCWErrors(configId, batchId, cdt, foroutboundProcessing, transactionId);
                    //flag as error in transactionIn or transactionOut table
                    updateStatusForErrorTrans(batchId, 14, foroutboundProcessing, transactionId);

                }
                //we replace original F[FieldNo] column with data in forcw
                updateFieldNoWithCWData(configId, batchId, cdt.getFieldNo(), cdt.getPassClear(), foroutboundProcessing, transactionId);
            }
            return errors;
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
                Integer intMacroReturn = flagMacroErrors(configId, batchId, cdt, foroutboundProcessing, transactionId);
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
    public Integer executeCWDataForSingleFieldValue(Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing,  Integer transactionId) {
       return transactionInDAO.executeCWDataForSingleFieldValue(configId, batchId,  cdt, foroutboundProcessing, transactionId);
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
    public Integer flagMacroErrors(Integer configId, Integer batchId,
            configurationDataTranslations cdt, boolean foroutboundProcessing, Integer transactionId) {
        return transactionInDAO.flagMacroErrors(configId, batchId, cdt, foroutboundProcessing, transactionId);
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
    public void insertProcessingError(Integer errorId, Integer configId, Integer batchId, Integer fieldNo,
            Integer macroId, Integer cwId, Integer validationTypeId, boolean required,
            boolean foroutboundProcessing, String errorCause) {
        insertProcessingError(errorId, configId, batchId, fieldNo, macroId, cwId, validationTypeId, required, foroutboundProcessing, errorCause, null);

    }

    @Override
    public void insertProcessingError(Integer errorId, Integer configId, Integer batchId, Integer fieldNo,
            Integer macroId, Integer cwId, Integer validationTypeId, boolean required,
            boolean foroutboundProcessing, String errorCause, Integer transactionId) {
        transactionInDAO.insertProcessingError(errorId, configId, batchId, fieldNo, macroId, cwId, validationTypeId, required, foroutboundProcessing, errorCause, transactionId);

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
    public void loadBatches() throws Exception {
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
    public boolean loadBatch(Integer batchId) throws Exception {
        //first thing we do is get details, then we set it to  38
        batchUploads batch = getBatchDetails(batchId);
        //we recheck status in case it was picked up in a loop
        if (batch.getstatusId() == 2 || batch.getstatusId() == 42) {
            Integer batchStatusId = 38;
            List<Integer> errorStatusIds = Arrays.asList(11, 13, 14, 16);
            String processFolderPath = "/bowlink/loadFiles/";
            try {

                try {
                    //log user activity
                    UserActivity ua = new UserActivity();
                    ua.setUserId(0);
                    ua.setFeatureId(0);
                    ua.setAccessMethod("System");
                    ua.setActivity("System Loaded Batch");
                    ua.setBatchUploadId(batchId);
                    usermanager.insertUserLog(ua);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.err.println("loadBatch - insert user log" + ex.toString());
                }

                // set batch to SBL - 38
                //if could be that the process has picked this up
                updateBatchStatus(batchId, batchStatusId, "startDateTime");

                // let's clear all tables first as we are starting over
                Integer sysErrors = clearTransactionTables(batchId, false);
                String errorMessage = "Load errors, please contact admin to review logs";
                // loading batch will take it all the way to loaded (9) status for
                if (sysErrors > 0) {
                    insertProcessingError(5, null, batchId, null, null, null, null, false, false, "Error cleaning out transaction tables.  Batch cannot be loaded.");
                    updateBatchStatus(batchId, 39, "endDateTime");
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

                /**
                 * decoded files will always be in loadFiles folder with UTBatchName *
                 */
                // all files are Base64 encoded at this point
                String encodedFilePath = dir.setPath(batch.getFileLocation());
                String encodedFileName = batch.getoriginalFileName();
                File encodedFile = new File(encodedFilePath + encodedFileName);
                String decodedFilePath = dir.setPath(processFolderPath);
                String decodedFileName = batch.getutBatchName();
                String decodedFileExt = batch.getoriginalFileName().substring(batch.getoriginalFileName().lastIndexOf("."));
                String decodedFile = decodedFilePath + decodedFileName + decodedFileExt;
                
                boolean fileDecoded = false;
                try {
                		filemanager.decode((encodedFilePath + encodedFileName), decodedFile);
                		fileDecoded = true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    sysErrors = 1;
                    processingSysErrorId = 17;
                }

                
                   
                if (fileDecoded) {
                	
                	actualFileName = (decodedFilePath + decodedFileName + decodedFileExt);
                
                /*
	                 If batch is set up for CCD input then we need to translate it
	                 to a pipe-delimited text file.
                     */
                    /**
                     * here we need to check if we should change file to xml or hr for org sometimes org will send hl7 files over or .out or some other extension, they all need to be .hr all ccd file will need to end in xml
                     *
                     */
                    //so we check decodedFileName and change it to the proper extension if need be
                    String chagneToExtension = "";
                    String processFileName = batch.getoriginalFileName();
                    /**
                     * For configId of 0, we need to check to see if org has hr or ccd if configId is not 0, we pull up the extension type and rename file if we find more than one file extension set up for org we reject them them file extension will be 4 (hr) or 9 (ccd) info we have from batchUpload - transportMethodId, configId, orgId
                     *
                     */
                    if (batch.getConfigId() != 0) {
                        configurationTransport ct = configurationtransportmanager.getTransportDetails(batch.getConfigId());
                        if (ct.getfileType() == 9) {
                            chagneToExtension = "xml";
                        } else if (ct.getfileType() == 4) {
                            chagneToExtension = "hr";
                        }
                    } else if (batch.getConfigId() == 0) {
                        //should restrict this to only 4/9
                        //see if the users has any 4/9 fileType, we don't need to worry about changing extension if org doesn't

                        List<configurationTransport> ctList = configurationtransportmanager.getConfigurationTransportFileExtByFileType(batch.getOrgId(), batch.gettransportMethodId(), null, Arrays.asList(1), true, false);
                        if (ctList.size() > 1) {
                            //it is ok to have multiple if they are not using file type 4/9, so we check again
                            List<configurationTransport> ctList2 = configurationtransportmanager.getConfigurationTransportFileExtByFileType(batch.getOrgId(), batch.gettransportMethodId(), Arrays.asList(4, 9), Arrays.asList(1), true, false);
                            if (ctList2.size() != 0) { //they have multiple file types defined along with hr or ccd, we fail them
                                //clean up
                                File tempLoadFile = new File(actualFileName);
                                if (tempLoadFile.exists()) {
                                    tempLoadFile.delete();
                                }
                                //log
                                updateBatchStatus(batchId, 7, "endDateTime");
                                insertProcessingError(18, null, batchId, null, null, null, null,
                                        false, false, "Multiple file types were found for transport method.");
                                //get out of loop
                                return false;

                            }
                        } else if (ctList.size() == 1) {
                            if (ctList.get(0).getfileType() == 9) {
                                chagneToExtension = "xml";
                            } else if (ctList.get(0).getfileType() == 4) {
                                chagneToExtension = "hr";
                            }
                        }
                    }

                    if (chagneToExtension != "") {
                        processFileName = batch.getutBatchName() + "." + chagneToExtension;
                        //we overwrite file 
                        //old file is here actualFileName;
                        //new file is the same name with diff extension
                        File actualFile = new File(actualFileName);
                        File fileWithNewExtension = new File(decodedFilePath + processFileName);
                        Path fileWithOldExtension = actualFile.toPath();
                        Path renamedFile = fileWithNewExtension.toPath();
                        Files.move(fileWithOldExtension, renamedFile, REPLACE_EXISTING);
                    }

                    if (processFileName.endsWith(".xml")) {
                        newfilename = ccdtotxt.TranslateCCDtoTxt(decodedFilePath, decodedFileName, batch.getOrgId());
                        if (newfilename.equals("ERRORERRORERROR")) {
                            //clean up and break
                            //need to remove load table, will leave load file with error
                            sysError = sysError + dropLoadTable(loadTableName);
                            updateBatchStatus(batchId, 39, "endDateTime");
                            insertProcessingError(5, null, batchId, null, null, null, null,
                                    false, false, "Error at applying jar template");
                            return false;
                        } else if (newfilename.equals("FILE IS NOT XML ERROR")) {
                            //clean up and break
                            //need to remove load table, will leave load file with error
                            sysError = sysError + dropLoadTable(loadTableName);
                            updateBatchStatus(batchId, 7, "endDateTime");
                            insertProcessingError(22, null, batchId, null, null, null, null,
                                    false, false, "XML format is invalid.");
                            return false;
                        }

                        actualFileName = (decodedFilePath + newfilename);
                        //we remove temp load file 
                        File tempLoadFile = new File(decodedFilePath + processFileName);
                        if (tempLoadFile.exists()) {
                            tempLoadFile.delete();
                        }
                        /* 
	                     if the original file name is a HL7 file (".hr") then we are going to translate it to
	                     a pipe-delimited text file.
                         */
                    } else if (processFileName.endsWith(".hr")) {

                        newfilename = hl7toTxt.TranslateHl7toTxt(decodedFilePath, decodedFileName, batch.getOrgId());
                        if (newfilename.equals("ERRORERRORERROR")) {
                            //clean up and break
                            //need to remove load table, will leave load file with error
                            sysError = sysError + dropLoadTable(loadTableName);
                            updateBatchStatus(batchId, 39, "endDateTime");
                            insertProcessingError(5, null, batchId, null, null, null, null,
                                    false, false, "Error at applying jar template");
                            return false;
                        }
                        actualFileName = (decodedFilePath + newfilename);
                        //we remove temp load file 
                        File tempLoadFile = new File(decodedFilePath + processFileName);
                        if (tempLoadFile.exists()) {
                            tempLoadFile.delete();
                        }
                    } else if (processFileName.endsWith(".xlsx")) {
                    	newfilename = xlsxtotxt.TranslateXLSXtoTxt( decodedFilePath, decodedFileName, batch);
                        if (newfilename.equals("ERRORERRORERROR")) {
                            //clean up and break
                            //need to remove load table, will leave load file with error
                            sysError = sysError + dropLoadTable(loadTableName);
                            updateBatchStatus(batchId, 39, "endDateTime");
                            insertProcessingError(5, null, batchId, null, null, null, null,
                                    false, false, "Error translating xlsx file");
                            return false;
                        } else if (newfilename.equals("FILE IS NOT xslx ERROR")) {
                            //clean up and break
                            //need to remove load table, will leave load file with error
                            sysError = sysError + dropLoadTable(loadTableName);
                            updateBatchStatus(batchId, 7, "endDateTime");
                            insertProcessingError(22, null, batchId, null, null, null, null,
                                    false, false, "XLSX format is invalid.");
                            return false;
                        }
                        actualFileName = (decodedFilePath + newfilename);
                        //we remove temp load file 
                        File tempLoadFile = new File(decodedFilePath + processFileName);
                        if (tempLoadFile.exists()) {
                            tempLoadFile.delete();
                        }
                    } else if (processFileName.endsWith(".xls")) {
                    	newfilename = xlstotxt.TranslateXLStoTxt( decodedFilePath, decodedFileName, batch);
                        if (newfilename.equals("ERRORERRORERROR")) {
                            //clean up and break
                            //need to remove load table, will leave load file with error
                            sysError = sysError + dropLoadTable(loadTableName);
                            updateBatchStatus(batchId, 39, "endDateTime");
                            insertProcessingError(5, null, batchId, null, null, null, null,
                                    false, false, "Error translating xls file");
                            return false;
                        } else if (newfilename.equals("FILE IS NOT xsl ERROR")) {
                            //clean up and break
                            //need to remove load table, will leave load file with error
                            sysError = sysError + dropLoadTable(loadTableName);
                            updateBatchStatus(batchId, 7, "endDateTime");
                            insertProcessingError(22, null, batchId, null, null, null, null,
                                    false, false, "XSL format is invalid.");
                            return false;
                        }
                        actualFileName = (decodedFilePath + newfilename);
                        //we remove temp load file 
                        File tempLoadFile = new File(decodedFilePath + processFileName);
                        if (tempLoadFile.exists()) {
                            tempLoadFile.delete();
                        }
                    } 
                    
                    //at this point, hl7 and hr are in unencoded plain text
                    if (actualFileName.endsWith(".txt") || actualFileName.endsWith(".csv")) {
                        sysError = sysError + insertLoadData(batch.getId(), batch.getDelimChar(), actualFileName, loadTableName, batch.isContainsHeaderRow());
                        File actualFile = new File(actualFileName);
                        //we are archiving it
                        File archiveFile = new File(dir.setPathFromRoot(archivePath) + batch.getutBatchName() + "_dec" + actualFileName.substring(actualFileName.lastIndexOf(".")));
                        Path archive = archiveFile.toPath();
                        Path actual = actualFile.toPath();
                        //we keep original file in archive folder
                        Files.move(actual, archive, REPLACE_EXISTING);
                    }

                    /** if excel files some files comes with random lines at the beginning and end, need to remove **/
                    if (processFileName.endsWith(".xlsx") || processFileName.endsWith(".xls")) {
                    	configurationExcelDetails excelDetails = configurationManager.getExcelDetails(batch.getConfigId(), batch.getOrgId());
                    	if (excelDetails.getStartRow() > 1) {
                    		deleteLoadTableRows(excelDetails.getStartRow() - 1, "asc", loadTableName);
                    	}
                    	if (excelDetails.getDiscardLastRows() > 0) {
                    		deleteLoadTableRows(excelDetails.getDiscardLastRows(), " desc ", loadTableName);
                    	}
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
                        List<configurationMessageSpecs> checkOnlyConfigForOrg = configurationtransportmanager.getConfigurationMessageSpecsForOrgTransport(batch.getOrgId(), batch.gettransportMethodId(), true);

                        //2. we get all rows for batch
                        List<transactionInRecords> tInRecords = getTransactionInRecordsForBatch(batch.getId());
                        if (tInRecords == null || tInRecords.size() == 0) {
                            updateBatchStatus(batchId, 7, "endDateTime");
                            insertProcessingError(7, null, batchId, null, null, null, null,
                                    false, false, "No valid transactions were found for batch.");
                            return false;
                        }
                        if (configurationMessageSpecs == null || (configurationMessageSpecs.size() == 0 && checkOnlyConfigForOrg.size() == 0)) {
                            insertProcessingError(6, null, batchId, null, null, null, null,
                                    false, false, "No valid configurations were found for loading batch.");
                            // update all transactions to invalid
                            updateBatchStatus(batchId, 7, "endDateTime");
                            updateTransactionStatus(batchId, 0, 0, 11);
                            return false;
                        }
                        //if we only have one and it is set to 0,we can default, else we loop through
                        if ((configurationMessageSpecs.size() == 1 && configurationMessageSpecs.get(0).getmessageTypeCol() == 0) || checkOnlyConfigForOrg.size() == 1) {
                            int configId = 0;
                            if (configurationMessageSpecs.size() == 0) {
                                configId = checkOnlyConfigForOrg.get(0).getconfigId();
                            } else {
                                configId = configurationMessageSpecs.get(0).getconfigId();
                            }
                            sysError = sysError + updateConfigIdForBatch(batch.getId(), configId);
                        } else {
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
                    }

                    //we populate transactionTranslatedIn
                    sysError = sysError + loadTransactionTranslatedIn(batchId);

                    //update data in transactionTranslatedIn
                    resetTransactionTranslatedIn(batchId, true);
                    int transactionId = 0;
                    // we trim all values
                    trimFieldValues(batchId, false, transactionId, true);

                    //now that we have our config, we will apply pre-processing cw and macros to manipulate our data
                    //1. find all configs for batch, loop and process
                    List<Integer> configIds = getConfigIdsForBatch(batchId, false, transactionId);
                    for (Integer configId : configIds) {
                        //we need to run all checks before insert regardless *
                        /**
                         * we are reordering 1. cw/macro, 2. required and 3. validate *
                         */
                        // 1. grab the configurationDataTranslations and run cw/macros
                        List<configurationDataTranslations> dataTranslations = configurationManager
                                .getDataTranslationsWithFieldNo(configId, 2); //pre processing
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
                    updateBatchStatus(batchId, 39, "endDateTime");
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
                    updateBatchStatus(batchId, 39, "endDateTime");
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
                batchStatusId = 43; //loaded without targets               
            } catch (Exception ex) {
                insertProcessingError(processingSysErrorId, null, batchId, null, null, null, null, false, false, ("loadBatch error " + ex.getLocalizedMessage()));
                batchStatusId = 39;
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
            
            //if status is not 43 or 36, we email admin
            if (batchStatusId != 43 && batchStatusId != 36) {
            	sendEmailToAdmin((new Date() + "<br/>Please login and review. Load batch failed.  <br/>Batch Id -  " + batch.getId() + "<br/> UT Batch Name " + batch.getutBatchName() + " <br/>Original batch file name - " + batch.getoriginalFileName()), "Load Batch Failed");
            }
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
            System.err.println("flagInvalidConfig " + ex.toString());
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
    public Integer clearTransactionOutErrorsByUploadBatchId(Integer batchId) {
        return transactionInDAO.clearTransactionOutErrorsByUploadBatchId(batchId);
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
                    sysError = sysError + insertTransactionTranslated(bus.gettransactionInId(), newTInId, bus);
                    sysError = sysError + insertTransactionInError(newTInId, bus.gettransactionInId());
                    //dup errors
                }

            }

            return sysError;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("newEntryForMultiTargets " + ex.toString());
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
    public Integer insertTransactionTranslated(Integer oldInId, Integer newInId, batchUploadSummary bus) {
        return transactionInDAO.insertTransactionTranslated(oldInId, newInId, bus);
    }

    @Override
    @Transactional
    public List<batchUploads> getAllUploadedBatches(Date fromDate, Date toDate) throws Exception {
        return transactionInDAO.getAllUploadedBatches(fromDate, toDate, 0);
    }

    @Override
    @Transactional
    public List<batchUploads> getAllUploadedBatches(Date fromDate, Date toDate, Integer fetchSize) throws Exception {
        return transactionInDAO.getAllUploadedBatches(fromDate, toDate, fetchSize);
    }

    @Override
    @Transactional
    public List<batchUploads> getAllRejectedBatches(Date fromDate, Date toDate, Integer fetchSize) throws Exception {
        return transactionInDAO.getAllRejectedBatches(fromDate, toDate, fetchSize);
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
            //these errors are not tied to transactions, tied to batch - need to write method to query them out so we don't keep adding to list
            List<TransErrorDetail> tedList = getTransErrorDetailsForNoRptFields(batchInfo.getId(), getErrorCodes(Arrays.asList(1, 2, 3, 4, 6, 9, 23)));
            if (tedList.size() > 0) {
                masterTedList.addAll(tedList);
            }
            /**
             * now get invalid configIds, errorId 6 - these are tied to transaction but not reportable fields since we don't know what configId it is. we don't know column that holds the field either since it didn't match with any for org, we display the first 4 columns
             */
            configErrorInfo = new ConfigErrorInfo();
            configErrorInfo.setBatchId(batchInfo.getId());
            tedList = getTransErrorDetailsForInvConfig(batchInfo.getId()); //error id 6
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
            System.err.println("populateErrorList " + ex.toString());
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
                    } else if (trd.getErrorCode() == 23) {
                        trd.setErrorFieldLabel("Source Site");
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
            System.err.println("getTransErrorDetails " + ex.toString());
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
            System.err.println("flagNoPermissionConfig " + ex.toString());
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
            System.err.println("checkPermissionForBatch " + ex.toString());
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
            System.err.println("populateBatchInfo " + ex.toString());
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
     * The sftp move files will grab all unique active SFTP pull paths and check folders for file. It will check path to see how many configurations it is associated with. It will also get the delimiter, check if there is a headerRow, how the file is being release. *
     */
    @Override
    public Integer moveSFTPFiles() {
        Integer sysErrors = 0;

        try {
            //1 . get distinct ftp paths
            List<configurationFTPFields> inputPaths = getFTPInfoForJob(1);
            
            //2 we clean up
            deleteMoveFileLogsByStatus(2);
            
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
                String inPath = fileSystem.setPathFromRoot(ftpInfo.getdirectory());
                File f = new File(inPath);
                if (!f.exists()) {
                    sftpJob.setNotes("Directory " + ftpInfo.getdirectory() + " does not exist");
                    updateSFTPRun(sftpJob);
                    //need to get out of loop since set up was not done properly, but should not throw exception as rest of job should go on
                    sendEmailToAdmin(ftpInfo.getdirectory() + " does not exist", "SFTP Job Error");
                    break;
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
            System.err.println("moveSFTPFilesJob " + ex.toString());
            try {
                sendEmailToAdmin((ex.toString() + "<br/>" + Arrays.toString(ex.getStackTrace())), "SFTP Job Error - main method errored");
            } catch (Exception ex1) {
                System.err.println("moveSFTPFilesJob " + Arrays.toString(ex1.getStackTrace()));
            }
            return 1;
        }
        return sysErrors;
    }

    @Override
    public Integer moveFilesByPath(String inPath, Integer transportMethodId, Integer orgId, Integer transportId) {
        Integer sysErrors = 0;

        try {
            fileSystem fileSystem = new fileSystem();
            String fileInPath = fileSystem.setPathFromRoot(inPath);
            File folder = new File(fileInPath);

            //list files
            //we only list visible files
            File[] listOfFiles = folder.listFiles((FileFilter) HiddenFileFilter.VISIBLE);

            Organization orgDetails = organizationmanager.getOrganizationById(orgId);
            String defPath = "/bowlink/" + orgDetails.getcleanURL() + "/input files/";
            String outPath = fileSystem.setPath(defPath);

            //too many variables that could come into play regarding file types, will check files with one method
            //loop files 
            for (File file : listOfFiles) {
                String fileName = file.getName();
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
                Date date = new Date();
                /* Create the batch name (TransportMethodId+OrgId+Date/Time/Seconds) */
                String batchName = new StringBuilder().append(transportMethodId).append(orgId).append(dateFormat.format(date)).toString();

                if (!fileName.endsWith("_error")) {

                    try {

                        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);

                        //figure out how many active transports are using fileExt method for this particular path
                        List<configurationTransport> transportList = configurationtransportmanager.getTransportListForFileExtAndPath(fileExt, transportMethodId, 1, inPath.replace(directoryPath, ""));

                        //figure out if files has distinct delimiters
                        List<configurationTransport> transports = configurationtransportmanager.getConfigTransportForFileExtAndPath(fileExt, transportMethodId, 1, inPath.replace(directoryPath, ""));

                        batchUploads batchInfo = new batchUploads();
                        batchInfo.setOrgId(orgId);
                        batchInfo.settransportMethodId(transportMethodId);
                        batchInfo.setstatusId(4);
                        batchInfo.setstartDateTime(date);
                        batchInfo.setutBatchName(batchName);
                        batchInfo.setOriginalFolder(inPath);

                        Integer batchId = 0;
                        String newFileName = "";
                        Integer statusId = 4;
                        Integer configId = 0;
                        Integer fileSize = 0;
                        Integer encodingId = 1;
                        Integer errorId = 0;

                        if (transportList.size() == 0 || transports.size() == 0) { //neither of them should be 0
                            //no source transport is associated with this method / file
                            batchInfo.setuserId(usermanager.getUserByTypeByOrganization(orgId).get(0).getId());
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
                            configurationTransport ct = configurationtransportmanager.getTransportDetailsByTransportId(transportId);
                            fileSize = ct.getmaxFileSize();
                            if (transportList.size() > 1) {
                                configId = 0;
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

                            /**
                             * need to log and error out file if there are no users set up for config *
                             */
                            if (users.size() == 0) {
                                if (usermanager.getUserByTypeByOrganization(orgId).size() != 0) {
                                    batchInfo.setuserId(usermanager.getUserByTypeByOrganization(orgId).get(0).getId());
                                } else {
                                    batchInfo.setuserId(1); //no users, we set to admin
                                }
                                batchId = (Integer) submitBatchUpload(batchInfo);
                                statusId = 7;
                                errorId = 27;

                            } else {
                                batchInfo.setuserId(users.get(0).getId());
                            	batchId = (Integer) submitBatchUpload(batchInfo);
                                if (batchInfo.getConfigId() != 0) {
	                                /** check for mass translation **/
	                                if (configurationtransportmanager.getTransportDetails(batchInfo.getConfigId()).isMassTranslation()) {
	                                	statusId = 42;
	                                } else {
	                                	statusId = 2;
	                                }
                                }
                                

                            }

                        } else if (transportList.size() > 1 && transports.size() > 1) {
                            //we loop though our delimiters for this type of fileExt
                            String delimiter = "";
                            Integer fileDelimiter = 0;
                            String fileLocation = "";
                            Integer userId = 0;
                            //get distinct delimiters
                            List<configurationTransport> delimList = configurationtransportmanager.getDistinctDelimCharForFileExt(fileExt, transportMethodId);
                            List<configurationTransport> encodings = configurationtransportmanager.getTransportEncoding(fileExt, transportMethodId);
                            //we reject file is multiple encodings/delimiters are found for extension type as we won't know how to decode it and read delimiter
                            if (encodings.size() != 1) {
                                batchInfo.setuserId(usermanager.getUserByTypeByOrganization(orgId).get(0).getId());
                                statusId = 7;
                                errorId = 16;
                            } else {
                                encodingId = encodings.get(0).getEncodingId();
                                for (configurationTransport ctdelim : delimList) {
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
                            // we don't have an error yet

                            if (errorId > 0) {
                                // some error detected from previous checks
                                userId = usermanager.getUserByTypeByOrganization(orgId).get(0).getId();
                                batchInfo.setConfigId(configId);
                                batchInfo.setFileLocation(defPath);
                                batchInfo.setOrgId(orgId);
                                newFileName = newFileName(outPath, fileName);
                                batchInfo.setoriginalFileName(newFileName);
                                batchInfo.setuserId(userId);
                                if (batchInfo.getConfigId() != 0 && batchInfo.getstatusId() == 2) {
	                                /** check for mass translation **/
	                                if (configurationtransportmanager.getTransportDetails(batchInfo.getConfigId()).isMassTranslation()) {
	                                	batchInfo.setstatusId(42);
	                                }
                                }
                                batchId = (Integer) submitBatchUpload(batchInfo);
                                batchInfo.setEncodingId(encodingId);
                            } else if (statusId != 2) {
                                //no vaild delimiter detected
                                statusId = 7;
                                userId = usermanager.getUserByTypeByOrganization(orgId).get(0).getId();
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
                                List<configurationTransport> containsHeaderRowCount = configurationtransportmanager.getCountContainsHeaderRow(fileExt, transportMethodId);

                                if (containsHeaderRowCount.size() != 1) {
                                    batchInfo.setuserId(usermanager.getUserByTypeByOrganization(orgId).get(0).getId());
                                    statusId = 7;
                                    errorId = 14;
                                } else {
                                    List<Integer> totalConfigs = configurationtransportmanager.getConfigCount(fileExt, transportMethodId, fileDelimiter);

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
                                    if (batchInfo.getConfigId() != 0 && batchInfo.getstatusId() == 2) {
    	                                /** check for mass translation **/
    	                                if (configurationtransportmanager.getTransportDetails(batchInfo.getConfigId()).isMassTranslation()) {
    	                                	batchInfo.setstatusId(42);
    	                                }
                                    }
                                    batchId = (Integer) submitBatchUpload(batchInfo);
                                }
                            }
                        }
                        /**
                         * insert log*
                         */
                        try {
                            //log user activity
                            UserActivity ua = new UserActivity();
                            ua.setUserId(0);
                            ua.setFeatureId(0);
                            ua.setAccessMethod("System");
                            ua.setActivity("System Uploaded File");
                            ua.setBatchUploadId(batchInfo.getId());
                            usermanager.insertUserLog(ua);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.err.println("moveFilesByPath - insert user log" + ex.toString());
                        }
                        //we encoded user's file if it is not
                        File newFile = new File(outPath + newFileName);
                        // now we move file
                        Path source = file.toPath();
                        Path target = newFile.toPath();

                        File archiveFile = new File(fileSystem.setPathFromRoot(archivePath) + batchName + newFileName.substring(newFileName.lastIndexOf(".")));
                        Path archive = archiveFile.toPath();
                        //we keep original file in archive folder
                        Files.copy(source, archive);

                        /**
                         * we check encoding here *
                         */
                        if (encodingId < 2) { //file is not encoded
                            String encodedOldFile = filemanager.encodeFileToBase64Binary(file);
                            filemanager.writeFile(newFile.getAbsolutePath(), encodedOldFile);
                            Files.delete(source);
                        } else {
                            Files.move(source, target);
                        }

                        if (statusId == 2 || statusId == 42) {
                            /**
                             * check file size if configId is 0 we go with the smallest file size *
                             */
                            long maxFileSize = fileSize * 1000000;
                            if (Files.size(target) > maxFileSize) {
                                statusId = 7;
                                errorId = 12;
                            }
                        }

                        if (statusId != 2 && statusId != 42) {
                            insertProcessingError(errorId, 0, batchId, null, null, null, null, false, false, "");
                        }

                        updateBatchStatus(batchId, statusId, "endDateTime");

                    } catch (Exception exAtFile) {
                        exAtFile.printStackTrace();
                        System.err.println("moveFilesByPath " + exAtFile.toString());
                        try {
                            sendEmailToAdmin((exAtFile.toString() + "<br/>" + Arrays.toString(exAtFile.getStackTrace())), "moveFilesByPath - at rename file to error ");
                            //we need to move that file out of the way
                            file.renameTo((new File(file.getAbsolutePath() + batchName + "_error")));
                        } catch (Exception ex1) {
                            ex1.printStackTrace();
                            System.err.println("moveFilesByPath " + ex1.getMessage());

                        }
                    }
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                sendEmailToAdmin((ex.toString() + "<br/>" + Arrays.toString(ex.getStackTrace())), "moveFilesByPath - issue with looping folder files");
            } catch (Exception ex1) {
                ex1.printStackTrace();
                System.err.println("moveFilesByPath " + ex1.getMessage());
            }
            return 1;
        }
        return sysErrors;
    }

    /**
     * this method grabs all distinct ftp path that need to be check for files *
     */
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
            System.err.println("newBatchName " + ex.toString());
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
    public Integer moveRRFiles() {
        Integer sysErrors = 0;

        try {
            //1 . get distinct ftp paths
            List<configurationRhapsodyFields> inputPaths = getRhapsodyInfoForJob(1);
         	
            //2 we clean up
            deleteMoveFileLogsByStatus(2);
            
            //loop ftp paths and check
            for (configurationRhapsodyFields rhapsodyInfo : inputPaths) {
                //we insert job so if anything goes wrong or the scheduler overlaps, we won't be checking the same folder over and over
                MoveFilesLog moveJob = new MoveFilesLog();
                moveJob.setStatusId(1);
                //modifying to append rootDir
                moveJob.setFolderPath(rhapsodyInfo.getDirectory());
                moveJob.setTransportMethodId(5);
                moveJob.setMethod(1);
                Integer lastId = insertSFTPRun(moveJob);
                moveJob.setId(lastId);

                // check if directory exists, if not create
                fileSystem fileSystem = new fileSystem();
                //paths are from root instead of /home
                String inPath = fileSystem.setPathFromRoot(directoryPath + rhapsodyInfo.getDirectory());
                File f = new File(inPath);
                if (!f.exists()) {
                    moveJob.setNotes(("Directory "+ directoryPath +   rhapsodyInfo.getDirectory() + " does not exist"));
                    updateSFTPRun(moveJob);
                    //need to get out of loop since set up was not done properly, will try to throw error
                    sendEmailToAdmin((directoryPath + rhapsodyInfo.getDirectory() + " does not exist"), "Rhapsody Job Error");
                    break;
                }
                //we look up org for this path
                Integer orgId = configurationtransportmanager.getOrgIdForRhapsodyPath(rhapsodyInfo);

                sysErrors = sysErrors + moveFilesByPath(directoryPath + rhapsodyInfo.getDirectory(), 5, orgId, rhapsodyInfo.getTransportId());

                if (sysErrors == 0) {
                    moveJob.setStatusId(2);
                    moveJob.setEndDateTime(new Date());
                    updateSFTPRun(moveJob);
                }
            }

            // if there are no errors, we release the folder path
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("moveRRFiles " + ex.toString());
            try {
                sendEmailToAdmin((ex.toString() + "<br/>" + Arrays.toString(ex.getStackTrace())), "Rhapsody Job Error - main method errored");
            } catch (Exception ex1) {
                ex1.printStackTrace();
                System.err.println("moveRRFiles " + ex1.toString());
            }
            return 1;
        }
        return sysErrors;
    }

    /**
     * this method grabs all distinct ftp path that need to be check for files *
     */
    @Override
    public List<configurationRhapsodyFields> getRhapsodyInfoForJob(Integer method) {
        return transactionInDAO.getRhapsodyInfoForJob(method);
    }

    @Override
    public Integer insertTransactionInError(Integer newTInId, Integer oldTInId) {
        return transactionInDAO.insertTransactionInError(newTInId, oldTInId);
    }

    @Override
    public List<Integer> checkCWFieldForList(Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing, Integer transactionId) {
        return transactionInDAO.checkCWFieldForList(configId, batchId, cdt, foroutboundProcessing, transactionId);
    }

    @Override
    public Integer processMultiValueCWData(Integer configId, Integer batchId,
            configurationDataTranslations cdt, List<CrosswalkData> cwdList,
            boolean foroutboundProcessing, Integer transactionId) {
        try {
            Integer error = 0;
            List<IdAndFieldValue> idAndValues = getIdAndValuesForConfigField(configId, batchId, cdt, foroutboundProcessing, transactionId);
            //we turn cwdList into a map
            Map<String, String> cwMap = new HashMap<String, String>();
            for (CrosswalkData cw : cwdList) {
                cwMap.put(cw.getSourceValue(), cw.getTargetValue());
            }

            //1. we get list of ids for field
            for (IdAndFieldValue idAndValue : idAndValues) {
                Integer invalidCount = 0;
                Integer blankListLength = 0;
                List<String> values = new ArrayList<String>();

                List<String> fieldValues = Arrays.asList(idAndValue.getFieldValue().split("\\^\\^\\^\\^\\^", -1));
                //we loop through value and compare to cw
                for (String fieldValue : fieldValues) {
                    //sometimes user need to pass blank list, should not be count as an error
                    if (cwMap.containsKey(fieldValue.trim())) {
                        values.add(cwMap.get(fieldValue.trim()));
                    } else //we pass value
                    if (cdt.getPassClear() == 1) {
                        values.add(fieldValue.trim());
                        invalidCount = invalidCount + 1;
                        if (fieldValue.trim().length() != 0) {
                            blankListLength = blankListLength + 1;
                        }
                    }
                }

                String newValue = StringUtils.collectionToDelimitedString(values, "^^^^^");
                error = updateFieldValue(newValue, cdt.getFieldNo(), idAndValue.getTransactionId(), foroutboundProcessing);

                //we insert error if no valid values were replaced
                if (invalidCount > 0 && blankListLength > 0) {
                    insertProcessingError(3, cdt.getconfigId(), batchId, cdt.getFieldNo(), null, cdt.getCrosswalkId(), null, false, foroutboundProcessing, "", idAndValue.getTransactionId());
                }
            }

            return error;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("processMultiValueCWData " + ex.toString());
            return 1;

        }

    }

    @Override
    public List<IdAndFieldValue> getIdAndValuesForConfigField(Integer configId,
            Integer batchId, configurationDataTranslations cdt,
            boolean foroutboundProcessing, Integer transactionId) {
        return transactionInDAO.getIdAndValuesForConfigField(configId, batchId, cdt, foroutboundProcessing, transactionId);
    }

    @Override
    public Integer updateFieldValue(String fieldValue, Integer fieldNo, Integer transactionId, boolean foroutboundProcessing) {
        return transactionInDAO.updateFieldValue(fieldValue, fieldNo, transactionId, foroutboundProcessing);
    }

    @Override
    public void trimFieldValues(Integer batchId, boolean foroutboundProcessing, Integer transactionId, boolean trimAll) {
        transactionInDAO.trimFieldValues(batchId, foroutboundProcessing, transactionId, trimAll);
    }

    @Override
    public void updateTransactionTargetListStatus(List<transactionTarget> transactions, Integer statusId) {
        transactionInDAO.updateTransactionTargetListStatus(transactions, statusId);
    }

    @Override
    public void submitTransactionMultipleTargets(batchMultipleTargets target) {
        transactionInDAO.submitTransactionMultipleTargets(target);
    }

    @Override
    public List<batchMultipleTargets> getBatchMultipleTargets(Integer batchId) {
        return transactionInDAO.getBatchMultipleTargets(batchId);
    }

    @Override
    public Integer copyBatchDetails(Integer batchId, Integer tgtConfigId, Integer transactionId) {
        return transactionInDAO.copyBatchDetails(batchId, tgtConfigId, transactionId);
    }

    @Override
    public void sendEmailToAdmin(String message, String subject) throws Exception {
        try {
            mailMessage mail = new mailMessage();
            mail.setfromEmailAddress("support@health-e-link.net");
            mail.setmessageBody(message);
            mail.setmessageSubject(subject + " " + myProps.getProperty("server.identity"));
            mail.settoEmailAddress(myProps.getProperty("admin.email"));
            emailManager.sendEmail(mail);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex);
        }
    }

    @Override
    public List<batchUploadSummary> getuploadBatchesByConfigAndTarget(Integer configId, Integer orgId, Integer tgtConfigId, Integer userOrgId) {
        return transactionInDAO.getuploadBatchesByConfigAndTarget(configId, orgId, tgtConfigId, userOrgId);
    }

    @Override
    public boolean searchBatchForHistory(batchUploads batchDetails, String searchTerm, Date fromDate, Date toDate) throws Exception {
        return transactionInDAO.searchBatchForHistory(batchDetails, searchTerm, fromDate, toDate);
    }

    @Override
    public Integer updateTranTargetStatusByUploadBatchId(Integer batchUploadId,
            Integer fromStatusId, Integer toStatusId) {
        return transactionInDAO.updateTranTargetStatusByUploadBatchId(batchUploadId, fromStatusId, toStatusId);
    }

    @Override
    public Integer updateBatchDLStatusByUploadBatchId(Integer batchUploadId, Integer fromStatusId, Integer toStatusId, String timeField) {
        return transactionInDAO.updateBatchDLStatusByUploadBatchId(batchUploadId, fromStatusId, toStatusId, timeField);
    }

    @Override
    public List<Integer> getBatchDownloadIdsFromUploadId(Integer batchUploadId) {
        return transactionInDAO.getBatchDownloadIdsFromUploadId(batchUploadId);
    }

    @Override
    public Integer clearBatchDownloads(List<Integer> batchDownloadIDs) {
        return transactionInDAO.clearBatchDownloads(batchDownloadIDs);
    }

    @Override
    public boolean recheckLongDate(String longDateVal, String convertedDate) {
        try {
            longDateVal = longDateVal.toLowerCase();
            convertedDate = convertedDate.toLowerCase();
            if (longDateVal.contains("jan") && convertedDate.contains("jan")) {
                return true;
            } else if (longDateVal.contains("feb") && convertedDate.contains("feb")) {
                return true;
            } else if (longDateVal.contains("mar") && convertedDate.contains("mar")) {
                return true;
            } else if (longDateVal.contains("apr") && convertedDate.contains("apr")) {
                return true;
            } else if (longDateVal.contains("may") && convertedDate.contains("may")) {
                return true;
            } else if (longDateVal.contains("jun") && convertedDate.contains("jun")) {
                return true;
            } else if (longDateVal.contains("jul") && convertedDate.contains("jul")) {
                return true;
            } else if (longDateVal.contains("aug") && convertedDate.contains("aug")) {
                return true;
            } else if (longDateVal.contains("sep") && convertedDate.contains("sep")) {
                return true;
            } else if (longDateVal.contains("oct") && convertedDate.contains("oct")) {
                return true;
            } else if (longDateVal.contains("nov") && convertedDate.contains("nov")) {
                return true;
            } else if (longDateVal.contains("dec") && convertedDate.contains("dec")) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public String getTransactionInIdsFromBatch(Integer batchUploadId) {
        return transactionInDAO.getTransactionInIdsFromBatch(batchUploadId);
    }

    /**
     * This method process soap messages received. These are messages that has valid senders. 1 for not processed 2 processed - written as text file 3 for rejected 4 for while writing to text ifle processing
     *
     * It will gather the list and write to to proper folder as a text file then moveFile process will pick them up and process them using the same codes
     *
     **
     */
    @Override
    public Integer processWebServiceMessages() {

        Integer sysErrors = 0;
        try {
            //1 . get unprocessed web messages
            List<WSMessagesIn> wsMessageList = getWSMessagesByStatusId(Arrays.asList(1));
            //loop messages and write them to file
            for (WSMessagesIn wsMessage : wsMessageList) {
                //we check to make sure again as time could have pass and it could have been processed already
                WSMessagesIn wsRecheck = getWSMessagesById(wsMessage.getId());
                if (wsRecheck.getStatusId() != 1) {
                    //this means some other scheduler probably started while this one was going and processed the wsMessages, we get out
                    return 1;
                }
                sysErrors = processWebServiceMessage(wsMessage);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("processWebServiceMessages " + ex.toString());
            try {
                sendEmailToAdmin((ex.toString() + "<br/>" + Arrays.toString(ex.getStackTrace())), "Web Services Job Error - main method errored");
            } catch (Exception ex1) {
                ex1.printStackTrace();
                System.err.println("processWebServiceMessages - can't send email for web service job error " + ex1.toString());
            }
            return 1;
        }
        return sysErrors;

    }

    @Override
    public List<WSMessagesIn> getWSMessagesByStatusId(List<Integer> statusIds) {
        return transactionInDAO.getWSMessagesByStatusId(statusIds);
    }

    @Override
    public WSMessagesIn getWSMessagesById(Integer wsMessageId) {
        return transactionInDAO.getWSMessagesById(wsMessageId);
    }

    /**
     * this will process each web service message, it should be less intensive if we treat it like a file upload instead of Rhapsody At the end of this, file should be written to input folder, it should be SSA or SRJ and logged *
     */
    @Override
    public Integer processWebServiceMessage(WSMessagesIn ws) {
        Integer sysErrors = 0;

        try {
            ws.setStatusId(4);
            sysErrors = updateWSMessage(ws);

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
            Date date = new Date();
            /* Create the batch name (TransportMethodId+OrgId+Date/Time/Seconds) */
            String batchName = new StringBuilder().append(6).append("_").append(ws.getId()).append("_").append(ws.getOrgId()).append(dateFormat.format(date)).toString();

            //1 we look up org's transport details - we need to figure out fileExt, delimiter, file size, see if we can get one configId
            List<configurationTransport> confTransports = configurationtransportmanager.getDistinctTransportDetailsForOrgByTransportMethodId(6, 1, ws.getOrgId());

            Integer encodingId = 2;
            Integer batchId = 0;
            Integer errorId = 0;
            Integer fileSize = 0;
            Integer statusId = 0;
            Integer configId = 0;

            batchUploads batchInfo = new batchUploads();
            batchInfo.setOrgId(ws.getOrgId());
            batchInfo.settransportMethodId(6);
            batchInfo.setstatusId(4);
            batchInfo.setstartDateTime(date);
            batchInfo.setutBatchName(batchName);
            batchInfo.setSenderEmail(ws.getFromAddress());

            Organization orgDetails = organizationmanager.getOrganizationById(ws.getOrgId());
            String writeToFolder = "/bowlink/" + orgDetails.getcleanURL() + "/input files/";
            String fileExt = ".txt";
            String fileNamePath = writeToFolder + batchName + fileExt;
            //set folder path
            fileSystem dir = new fileSystem();
            String writeToFile = dir.setPath(fileNamePath);

            //we reject
            if (confTransports.size() != 1) { //should only be one since we don't have file ext
                //no source transport is associated with this method / file
                batchInfo.setuserId(usermanager.getUserByTypeByOrganization(ws.getOrgId()).get(0).getId());
                batchInfo.setConfigId(0);
                batchInfo.setoriginalFileName(batchName + fileExt);
                batchInfo.setFileLocation(writeToFolder);
                batchInfo.setEncodingId(encodingId);
                //write the file
                filemanager.writeFile(writeToFile, ws.getPayload());
                if (batchInfo.getConfigId() != 0 && batchInfo.getstatusId() == 2) {
                    /** check for mass translation **/
                    if (configurationtransportmanager.getTransportDetails(batchInfo.getConfigId()).isMassTranslation()) {
                    	batchInfo.setstatusId(42);
                    }
                }
                batchId = (Integer) submitBatchUpload(batchInfo);
                //insert error
                errorId = 13;
                statusId = 7;

            } else {
                configurationTransport ct = confTransports.get(0);
                List<configurationTransport> cts = configurationtransportmanager.getCTForOrgByTransportMethodId(6, 1, ws.getOrgId());

                //we loop through cts and set its wsFields 
                for (configurationTransport confTran : cts) {
                    // need to set web service fields
                    List<configurationWebServiceFields> wsFields = configurationtransportmanager.getTransWSDetails(confTran.getId());
                    //set the web services fields so we can grab document tag later
                    confTran.setWebServiceFields(wsFields);
                }
                encodingId = ct.getEncodingId();
                fileExt = "." + ct.getfileExt();
                writeToFolder = ct.getfileLocation();
                fileNamePath = writeToFolder + batchName + fileExt;
                fileSize = cts.get(0).getmaxFileSize();
                //determine configId here
                if (cts.size() > 1) {
                    configId = 0;
                    fileSize = configurationtransportmanager.getMinMaxFileSize(ct.getfileExt(), 6);
                } else {
                    configId = cts.get(0).getconfigId();
                }
                batchInfo.setConfigId(configId);
                batchInfo.setContainsHeaderRow(ct.getContainsHeaderRow());
                batchInfo.setDelimChar(ct.getDelimChar());
                batchInfo.setFileLocation(ct.getfileLocation());
                batchInfo.setOrgId(ws.getOrgId());
                batchInfo.setoriginalFileName(batchName + fileExt);
                batchInfo.setEncodingId(encodingId);

                //find user 
                List<User> users = new ArrayList<User>();
                if (configId != 0) {
                    users = usermanager.getSendersForConfig(Arrays.asList(configId));
                }
                if (users.size() == 0) {
                    users = usermanager.getOrganizationContact(ws.getOrgId(), 1);
                }
                if (users.size() == 0) {
                    users = usermanager.getOrganizationContact(ws.getOrgId(), 2);
                }
                if (users.size() == 0) {
                    users = usermanager.getOrganizationContact(ws.getOrgId(), 0);
                }
                //if we can't find a user to assign the batch to - we have to error the file
                if (users.size() == 0) {
                    //we assign to admin
                    batchInfo.setuserId(1);
                    statusId = 7;
                    errorId = 27;
                } else {
                    batchInfo.setuserId(users.get(0).getId());
                }
                //write payload to file
                writeToFile = dir.setPath(fileNamePath);

                String messageContent = ws.getPayload();

                //need to loop through cts list's tags in the payload to see which tag we should be looking for
                String tagToCheck = "";
                List<String> payloadArray = new ArrayList<String>(Arrays.asList(messageContent.split(" ")));
                int countTag = 0;
                boolean attachmentFound = false;

                for (configurationTransport confTran : cts) {
                    int tagPosition = 0;
                    countTag = 0;
                    configurationWebServiceFields wsFields = confTran.getWebServiceFields().get(0);
                    tagToCheck = "<" + wsFields.getTagName() + ">";
                    String tagToSplit = "</" + wsFields.getTagName() + ">";
                    countTag = messageContent.split(tagToCheck, -1).length - 1;
                    if (countTag > 0) {
                        tagPosition = wsFields.getTagPosition();
                        messageContent = messageContent.replaceAll(tagToCheck, "");
                        payloadArray = new ArrayList<String>(Arrays.asList(messageContent.split(tagToSplit, -1)));
                        payloadArray.remove(payloadArray.size() - 1);
                        /**
                         * we start looking for attachment at position with either expected content or delimiter
                         */
                        boolean atExpectedPosition = false;
                        int expectedPosition = wsFields.getTagPosition();
                        if (expectedPosition <= countTag) {
                            //we do a search for attachment at expected position
                            messageContent = payloadArray.get(expectedPosition - 1);
                            String decodedString = messageContent;
                            if (confTran.getEncodingId() == 2) {
                                decodedString = utilmanager.decodeStringToBase64Binary(messageContent);
                            }
                            //now we have string, we check for either expected content or delimiter
                            if (wsFields.getTextInAttachment().length() == 0) {
                                //we look for delimiter
                                if (decodedString.split(("\\" + ct.getDelimChar()), -1).length - 1 > 3) {
                                    atExpectedPosition = true;
                                }
                            } else {
                                atExpectedPosition = decodedString.contains(wsFields.getTextInAttachment());
                            }
                        }
                        if (atExpectedPosition) {
                            //we have attachment
                            filemanager.writeFile(writeToFile, messageContent);
                            ws.setFoundPosition(expectedPosition);
                            ws.setPositionMatched(true);
                            attachmentFound = true;
                            break;
                        } else {
                            //we have to loop through all document tag
                            int position = 1;
                            for (String content : payloadArray) {
                                String decodedString = content;
                                if (confTran.getEncodingId() == 2) {
                                    decodedString = utilmanager.decodeStringToBase64Binary(content);
                                }
                                //now we have decodedString, we check for text match or delimiter match
                                if (wsFields.getTextInAttachment().length() == 0) {
                                    //we look for delimiter
                                    if (decodedString.split(("\\" + ct.getDelimChar()), -1).length - 1 > 3) {
                                        ws.setFoundPosition(position);
                                        ws.setPositionMatched(false);
                                        filemanager.writeFile(writeToFile, content);
                                        attachmentFound = true;
                                        break;
                                    }
                                } else if (decodedString.contains(wsFields.getTextInAttachment())) {
                                    ws.setFoundPosition(position);
                                    ws.setPositionMatched(false);
                                    filemanager.writeFile(writeToFile, content);
                                    attachmentFound = true;
                                    break;
                                }
                                position++;
                                attachmentFound = false;
                            }

                        }

                    }
                }

                if (countTag == 0) {
                    //insert error
                    errorId = 25;
                    statusId = 7;
                } else if (attachmentFound == false) {
                    //insert error
                    errorId = 24;
                    statusId = 7;
                }

                if (statusId != 7) {
                    //decode and check delimiter
                    File file = new File(writeToFile);

                    if (ct.getEncodingId() == 2) {
                        //write to temp file
                        String strDecode = filemanager.decodeFileToBase64Binary(file);
                        file = new File(dir.setPathFromRoot(archivePath) + batchName + "_dec" + fileExt);
                        String decodeFilePath = dir.setPathFromRoot(archivePath) + batchName + "_dec" + fileExt;
                        filemanager.writeFile(decodeFilePath, strDecode);
                        String encodeFilePath = dir.setPathFromRoot(archivePath) + batchName + "_org" + fileExt;
                        filemanager.writeFile(encodeFilePath, ws.getPayload());
                        String encodeArchivePath = dir.setPathFromRoot(archivePath) + batchName + fileExt;
                        Files.copy(new File(writeToFile).toPath(), new File(encodeArchivePath).toPath(), REPLACE_EXISTING);
                    }

                    statusId = 2;
                    /**
                     * can't check delimiter for certain files, xml, hr etc *
                     */
                    if (fileExt.equalsIgnoreCase(".txt")) {
                        int delimCount = (Integer) dir.checkFileDelimiter(file, ct.getDelimChar());
                        if (delimCount < 3) {
                            statusId = 7;
                            errorId = 15;
                        }
                    }
                    //check file size
                    if (statusId == 2) {
                        /**
                         * check file size if configId is 0 we go with the smallest file size *
                         */

                        long maxFileSize = fileSize * 1000000;
                        if (Files.size(file.toPath()) > maxFileSize) {
                            statusId = 7;
                            errorId = 12;
                        }
                    }
                }
                batchInfo.setstatusId(statusId);
                batchInfo.setendDateTime(new Date());
                batchInfo.settotalRecordCount(1); // need to be at least one to show up in activites
                if (batchInfo.getConfigId() != 0 && batchInfo.getstatusId() == 2) {
                    /** check for mass translation **/
                    if (configurationtransportmanager.getTransportDetails(batchInfo.getConfigId()).isMassTranslation()) {
                    	batchInfo.setstatusId(42);
                    }
                }
                batchId = (Integer) submitBatchUpload(batchInfo);
                if (statusId != 2 || statusId != 42) {
                    insertProcessingError(errorId, 0, batchId, null, null, null, null, false, false, "");
                }

                //update message status to done
                ws.setStatusId(2);
                ws.setBatchUploadId(batchId);
                sysErrors = updateWSMessage(ws);
            }

            /**
             * insert log*
             */
            try {
                //log user activity
                UserActivity ua = new UserActivity();
                ua.setUserId(0);
                ua.setFeatureId(0);
                ua.setAccessMethod("System");
                ua.setActivity("System Processed Web Service Message " + ws.getId());
                ua.setBatchUploadId(batchInfo.getId());
                usermanager.insertUserLog(ua);

            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println("moveFilesByPath - insert user log" + ex.toString());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("processWebServiceMessage " + ex.toString());
            try {
                sendEmailToAdmin((ex.toString() + "<br/>" + Arrays.toString(ex.getStackTrace())), "processWebServiceMessage");
            } catch (Exception ex1) {
                ex1.printStackTrace();
                System.err.println("processWebServiceMessage " + ex1.toString());
            }
            return 1;
        }

        return sysErrors;
    }

    @Override
    public Integer updateWSMessage(WSMessagesIn wsMessage) {
        return transactionInDAO.updateWSMessage(wsMessage);
    }

    @Override
    public List<Integer> getErrorCodes(List<Integer> codesToIgnore) {
        return transactionInDAO.getErrorCodes(codesToIgnore);
    }

    @Override
    public Integer rejectInvalidSourceSubOrg(batchUploads batch,
            configurationConnection confConn, boolean nofinalStatus) {
        return transactionInDAO.rejectInvalidSourceSubOrg(batch, confConn, nofinalStatus);
    }

    @Override
    public Integer updateSSOrgIdTransactionIn(batchUploads batchUpload,
            configurationConnection batchTargets) {
        return transactionInDAO.updateSSOrgIdTransactionIn(batchUpload, batchTargets);
    }

    @Override
    public Integer updateSSOrgIdUploadSummary(batchUploads batchUpload) {
        return transactionInDAO.updateSSOrgIdUploadSummary(batchUpload);
    }

    @Override
    public Integer updateSSOrgIdTransactionTarget(batchUploads batchUpload) {
        return transactionInDAO.updateSSOrgIdTransactionTarget(batchUpload);
    }

    @Override
    public List<Integer> getBatchesForReport(Date fromDate, Date toDate) throws Exception {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateFrom = df.format(fromDate);
        String dateTo = df.format(toDate);

        List<Integer> batchIds = new ArrayList<Integer>();

        List<Integer> ergBatches = transactionInDAO.geBatchesIdsForReport(dateFrom, dateTo, true);

        List<Integer> nonErgBatches = transactionInDAO.geBatchesIdsForReport(dateFrom, dateTo, false);

        if (!ergBatches.isEmpty()) {
            for (Integer batch : ergBatches) {
                batchIds.add(batch);
            }
        }

        if (!nonErgBatches.isEmpty()) {
            for (Integer batch : nonErgBatches) {
                batchIds.add(batch);
            }
        }

        return batchIds;
    }

    @Override
    public BigInteger getReferralCount(List<Integer> batchIds) throws Exception {

        String ids = "";

        for (int i = 0; i < batchIds.size(); i++) {
            ids += batchIds.get(i);
            if (i < batchIds.size() - 1) {
                ids += ",";
            }
        }

        if (!batchIds.isEmpty()) {
            return transactionInDAO.getReferralCount(ids);
        } else {
            return null;
        }

    }

    @Override
    public BigInteger getFeedbackReportCount(List<Integer> batchIds) throws Exception {

        String ids = "";

        for (int i = 0; i < batchIds.size(); i++) {
            ids += batchIds.get(i);
            if (i < batchIds.size() - 1) {
                ids += ",";
            }
        }

        if (!batchIds.isEmpty()) {
            return transactionInDAO.getFeedbackReportCount(ids);
        } else {
            return null;
        }
    }

    @Override
    public BigInteger getRejectedCount(Date fromDate, Date toDate) throws Exception {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateFrom = df.format(fromDate);
        String dateTo = df.format(toDate);

        return transactionInDAO.getRejectedCount(dateFrom, dateTo);

    }

    @Override
    public List<activityReportList> getFeedbackReportList(List<Integer> batchIds) throws Exception {
        String ids = "";

        for (int i = 0; i < batchIds.size(); i++) {
            ids += batchIds.get(i);
            if (i < batchIds.size() - 1) {
                ids += ",";
            }
        }

        if (!batchIds.isEmpty()) {
            List<activityReportList> reportList = transactionInDAO.getFeedbackReportList(ids);

            for (activityReportList item : reportList) {

                List<Integer> transIds = transactionInDAO.getFeedbackTransactions(ids, item.getConfigId());

                String transIdList = "";
                for (int p = 0; p < transIds.size(); p++) {
                    transIdList += transIds.get(p);
                    if (p < transIds.size() - 1) {
                        transIdList += ",";
                    }
                }

                String fieldNo;
                Integer statusFieldNo = transactionInDAO.getStatusFieldNo(item.getConfigId());
                fieldNo = "f" + statusFieldNo;

                BigInteger totalOpen = transactionInDAO.getTotalOpenFeedbackReports(transIdList, fieldNo);
                BigInteger totalClosed = transactionInDAO.getTotalClosedFeedbackReports(transIdList, fieldNo);

                item.setOpenTotal(totalOpen);
                item.setClosedTotal(totalClosed);
            }

            return reportList;
        } else {
            return null;
        }

    }

    @Override
    public List<activityReportList> getReferralList(List<Integer> batchIds) throws Exception {
        String ids = "";

        for (int i = 0; i < batchIds.size(); i++) {
            ids += batchIds.get(i);
            if (i < batchIds.size() - 1) {
                ids += ",";
            }
        }

        if (!batchIds.isEmpty()) {
            List<activityReportList> reportList = transactionInDAO.getReferralList(ids);
            return reportList;
        } else {
            return null;
        }
    }

    @Override
    public List<Integer> getActivityStatusTotals(List<Integer> batchIds) throws Exception {

        String ids = "";

        for (int i = 0; i < batchIds.size(); i++) {
            ids += batchIds.get(i);
            if (i < batchIds.size() - 1) {
                ids += ",";
            }
        }

        List<Integer> activityStatusTotals = new ArrayList<Integer>();
        Integer totalCompleted = 0;
        Integer totalEnrolled = 0;

        if (!batchIds.isEmpty()) {
            List<activityReportList> reportList = transactionInDAO.getFeedbackReportList(ids);

            for (activityReportList item : reportList) {

                List<Integer> transIds = transactionInDAO.getFeedbackTransactions(ids, item.getConfigId());

                String transIdList = "";
                for (int p = 0; p < transIds.size(); p++) {
                    transIdList += transIds.get(p);
                    if (p < transIds.size() - 1) {
                        transIdList += ",";
                    }
                }

                String fieldNo;
                Integer statusFieldNo = transactionInDAO.getActivityStatusFieldNo(item.getConfigId());
                fieldNo = "f" + statusFieldNo;

                BigInteger totalCompletedStatus = transactionInDAO.getTotalCompletedActivityStatus(transIdList, fieldNo);
                BigInteger totalEnrolledStatus = transactionInDAO.getTotalEnrolledActivityStatus(transIdList, fieldNo);

                totalCompleted += totalCompletedStatus.intValue();
                totalEnrolled += totalEnrolledStatus.intValue();

            }

            activityStatusTotals.add(totalCompleted);
            activityStatusTotals.add(totalEnrolled);

        }

        return activityStatusTotals;

    }

    @Override
    public List<referralActivityExports> getReferralActivityExports() throws Exception {
        return transactionInDAO.getReferralActivityExports();
    }

    /**
     *
     * @param fromDate
     * @param toDate
     * @throws Exception
     */
    @Override
    public void createNewReferralActivityExport(referralActivityExports newExport) throws Exception {

        /**
         * Get every referral sent based on the dates provided *
         */
        Date fromDate = newExport.getFromDate();
        Date toDate = newExport.getToDate();

        List<transactionTarget> transactions = transactionOutDAO.getSentTransactions(fromDate, toDate);

        if (transactions != null && transactions.size() > 0) {

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
            Date date = new Date();
            String fileName = new StringBuilder().append("referralactivityExport_").append(dateFormat.format(date)).append(".txt").toString();

            newExport.setFileName(fileName);

            updateReferralActivityExport(newExport);

            /* Create new export file */
            InputStream inputStream = null;
            OutputStream outputStream = null;

            fileSystem dir = new fileSystem();
            dir.setDirByName("referralActivityExports/");

            File newFile = new File(dir.getDir() + fileName);

            /* Create the empty file in the correct location */
            try {

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
            } catch (IOException e) {
                e.printStackTrace();
            }

            /* Read in the file */
            FileInputStream fileInput = null;
            File file = new File(dir.getDir() + fileName);
            fileInput = new FileInputStream(file);

            FileWriter fw = null;

            try {
                fw = new FileWriter(file, true);
            } catch (IOException ex) {
                Logger.getLogger(transactionOutManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

            StringBuilder exportRow = new StringBuilder();

            /**
             * Need to pull referral, CBO Updates and FB reports *
             */
            /**
             * IF CBO Updates a referral there should be 2 entries ,the original referral and the CBO Update *
             */
            exportRow.append("Transaction Type")
                    .append("|").append("Sending Org Name")
                    .append("|").append("Sending Site Name")
                    .append("|").append("Receiving Org Name")
                    .append("|").append("Receiving Site Name")
                    .append("|").append("Referral Date / Time")
                    .append("|").append("eRef Internal Referral Id") /**
                     * For feedback report it must be the original referral ID *
                     */
                    .append("|").append("Provider Referral Id")
                    .append("|").append("D.O.B")
                    .append("|").append("Gender")
                    .append("|").append("Race")
                    .append("|").append("Ethnicity")
                    .append("|").append("Preferred Language")
                    .append("|").append("Zip Code")
                    .append("|").append("Referral Type")
                    .append("|").append("Program Option 1") /**
                     * ?? - CDSMP, MOB, ETC *
                     */
                    .append("|").append("Program Option 2") /**
                     * ?? - CDSMP, MOB, ETC *
                     */
                    .append("|").append("Program Option 3") /**
                     * ?? - CDSMP, MOB, ETC *
                     */
                    .append("|").append("Program Option 4") /**
                     * ?? - CDSMP, MOB, ETC *
                     */
                    .append("|").append("Program Option 5") /**
                     * ?? - CDSMP, MOB, ETC *
                     */
                    .append("|").append("Program Option 6") /**
                     * ?? - CDSMP, MOB, ETC *
                     */
                    .append("|").append("Program activity status 1") /**
                     * ?? - CDSMP, MOB, ETC *
                     */
                    .append("|").append("Program activity status 2") /**
                     * ?? - CDSMP, MOB, ETC *
                     */
                    .append("|").append("Program activity status 3") /**
                     * ?? - CDSMP, MOB, ETC *
                     */
                    .append("|").append("Program activity status 4") /**
                     * ?? - CDSMP, MOB, ETC *
                     */
                    .append("|").append("Program activity status 5") /**
                     * ?? - CDSMP, MOB, ETC *
                     */
                    .append("|").append("Program activity status 6") /**
                     * ?? - CDSMP, MOB, ETC *
                     */
                    .append("|").append("Referral Status")
                    .append("|").append("Feedback Report Activity Status")
                    .append("|").append("Feedback Report Date/Time")
                    .append("|").append("Feedback Report Notes")
                    .append("|").append("CBO Activity Type Status")
                    .append("|").append("CBO Activity Type Status Date/Time")
                    .append("|").append("CBO Activity Notes")
                    .append("|").append("Transaction Created By");

            exportRow.append(System.getProperty("line.separator"));

            fw.write(exportRow.toString());

            for (transactionTarget transaction : transactions) {

                transactionIn transactionInDetails = transactionInDAO.getTransactionDetails(transaction.gettransactionInId());

                batchUploadSummary batchUploadSummary = transactionInDAO.getUploadSummaryDetails(transaction.gettransactionInId());

                String type = "R";
                String referralStatus = "";
                String FBActivityStatus = "";
                String FBDateTime = "";
                Integer eRefId = 0;
                String referralId = "";
                String referralDate = "";
                String feedbackNotes = "";

                List fieldNumbers = transactionInDAO.getConfigFieldNumbers(transactionInDetails.getConfigId());

                Integer referralIdFieldNo = 0;
                Integer activityStatusFieldNo = 0;
                Integer notesFieldNo = 0;
                Integer statusFieldNo = 0;

                for (ListIterator iter = fieldNumbers.listIterator(); iter.hasNext();) {

                    Object[] row = (Object[]) iter.next();

                    if (row[0] != null && Integer.valueOf(String.valueOf(row[0])) > 0) {
                        referralIdFieldNo = Integer.valueOf(String.valueOf(row[0]));
                    }

                    if (row[1] != null && Integer.valueOf(String.valueOf(row[1])) > 0) {
                        activityStatusFieldNo = Integer.valueOf(String.valueOf(row[1]));
                    }

                    if (row[2] != null && Integer.valueOf(String.valueOf(row[2])) > 0) {
                        notesFieldNo = Integer.valueOf(String.valueOf(row[2]));
                    }

                    if (row[3] != null && Integer.valueOf(String.valueOf(row[3])) > 0) {
                        statusFieldNo = Integer.valueOf(String.valueOf(row[3]));
                    }

                }

                if (transactionInDetails.gettransactionTargetId() > 0) {
                    type = "F";
                    FBDateTime = batchUploadSummary.getDateSubmitted().toString();

                    transactionTarget origReferralTransaction = transactionOutDAO.getTransactionDetails(transactionInDetails.gettransactionTargetId());
                    batchUploads origbatchUploadDetails = transactionInDAO.getBatchDetails(origReferralTransaction.getbatchUploadId());

                    referralDate = origbatchUploadDetails.getdateSubmitted().toString();
                    eRefId = origbatchUploadDetails.getId();

                    //Integer statusFieldNo = transactionInDAO.getStatusFieldNo(transactionInDetails.getConfigId());
                    if (statusFieldNo > 0) {
                        String fieldNo = "f" + statusFieldNo;
                        referralStatus = transactionInDAO.getTransactionFieldValue(transaction.gettransactionInId(), fieldNo);
                    }

                    if ("1".equals(referralStatus)) {
                        referralStatus = "Open";
                    } else if ("2".equals(referralStatus)) {
                        referralStatus = "Closed";
                    } else {
                        referralStatus = "Open";
                    }

                    if (notesFieldNo > 0) {
                        String notefieldNo = "f" + notesFieldNo;
                        feedbackNotes = transactionInDAO.getTransactionFieldValue(transaction.gettransactionInId(), notefieldNo);
                    }
                    //Integer referralIdFieldNo = transactionInDAO.getReferralIdFieldNo(transactionInDetails.getConfigId());

                    if (referralIdFieldNo != null && referralIdFieldNo > 0) {
                        String referralIdfieldNo = "f" + referralIdFieldNo;

                        referralId = transactionInDAO.getTransactionFieldValue(transaction.gettransactionInId(), referralIdfieldNo);
                    }

                    //Integer activityStatusFieldNo = transactionInDAO.getActivityStatusFieldNo(transactionInDetails.getConfigId());
                    if (activityStatusFieldNo != null && activityStatusFieldNo > 0) {
                        String activityfieldNo = "f" + activityStatusFieldNo;

                        FBActivityStatus = transactionInDAO.getTransactionFieldValue(transaction.gettransactionInId(), activityfieldNo);

                        try {
                            if (FBActivityStatus != null && Integer.parseInt(FBActivityStatus) > 0) {
                                FBActivityStatus = transactionInDAO.getReportActivityStatusValueById(Integer.parseInt(FBActivityStatus));

                                if (FBActivityStatus == null || "".equals(FBActivityStatus) || "null".equals(FBActivityStatus)) {
                                    FBActivityStatus = "Service Update";
                                }
                            } else {
                                FBActivityStatus = "Service Update";
                            }
                        } catch (Exception e) {

                        }
                    }

                } else {
                    referralDate = batchUploadSummary.getDateSubmitted().toString();
                    eRefId = batchUploadSummary.getbatchId();

                    //Integer referralIdFieldNo = transactionInDAO.getReferralIdFieldNo(transactionInDetails.getConfigId());
                    if (referralIdFieldNo != null && referralIdFieldNo > 0) {
                        String referralIdfieldNo = "f" + referralIdFieldNo;

                        referralId = transactionInDAO.getTransactionFieldValue(transaction.gettransactionInId(), referralIdfieldNo);
                    }
                }

                String messageTypeName = configurationManager.getMessageTypeNameByConfigId(batchUploadSummary.getsourceConfigId());

                batchDownloads batchDownloadDetails = transactionOutDAO.getBatchDetails(transaction.getbatchDLId());

                /**
                 * Get patient information *
                 */
                messagePatients patientDetails = transactionInDAO.getPatientTransactionDetailsForExport(transaction.gettransactionInId());

                Organization sourceOrg = organizationmanager.getOrganizationById(batchUploadSummary.getsourceOrgId());
                Organization sourceSiteOrg = null;
                if (transaction.getSourceSubOrgId() > 0) {
                    sourceSiteOrg = organizationmanager.getOrganizationById(transaction.getSourceSubOrgId());
                }

                Organization targetOrg = organizationmanager.getOrganizationById(batchDownloadDetails.getOrgId());
                Organization targetSiteOrg = null;
                if (transaction.getTargetSubOrgId() > 0) {
                    targetSiteOrg = organizationmanager.getOrganizationById(transaction.getTargetSubOrgId());
                }

                /**
                 * R - (REFRRAL), C - (CBO Update) OR F - (FB REPORT) Sending Org Sending Site Receiving Org Receiving Site Referral Date / Time eRef Referral Id DOB Gender Race Ethnicity Preferred Language Zip Code Referral Type Program Option 1 Program Option 2 Program Option 3 Program Option 4 Program Option 5 Program Option 6 Program activity status 1 Program activity status 2 Program activity status 3 Program activity status 4 Program activity status 5 Program activity status 6 Referral Status Feedback Report Activity Status Feedback Report Date / Time Feedback Report Notes CBO Activity Type Status CBO Activity Type Status Date / Time CBO Notes Transaction Created By
                 */
                exportRow = new StringBuilder();

                exportRow.append(type).append("|");
                exportRow.append(sourceOrg.getOrgName()).append("|");

                if (sourceSiteOrg != null) {
                    exportRow.append(sourceSiteOrg.getOrgName()).append("|");
                } else {
                    exportRow.append(sourceOrg.getOrgName()).append("|");
                }

                exportRow.append(targetOrg.getOrgName()).append("|");

                if (targetSiteOrg != null) {
                    exportRow.append(targetSiteOrg.getOrgName()).append("|");
                } else {
                    exportRow.append(targetOrg.getOrgName()).append("|");
                }

                exportRow.append(referralDate).append("|")
                        .append(eRefId).append("|")
                        .append(referralId).append("|");

                if (patientDetails != null) {
                    exportRow.append(patientDetails.getDob()).append("|")
                            .append(patientDetails.getGenderVal()).append("|")
                            .append(patientDetails.getRaceVal()).append("|")
                            .append(patientDetails.getEthnicityVal()).append("|")
                            .append(patientDetails.getLanguageVal()).append("|")
                            .append(patientDetails.getZip()).append("|");
                } else {
                    exportRow.append("").append("|")
                            .append("").append("|")
                            .append("").append("|")
                            .append("").append("|")
                            .append("").append("|")
                            .append("").append("|");
                }

                exportRow.append(messageTypeName).append("|")
                        .append("").append("|")
                        .append("").append("|")
                        .append("").append("|")
                        .append("").append("|")
                        .append("").append("|")
                        .append("").append("|")
                        .append("").append("|")
                        .append("").append("|")
                        .append("").append("|")
                        .append("").append("|")
                        .append("").append("|")
                        .append("").append("|")
                        .append(referralStatus).append("|")
                        .append(FBActivityStatus).append("|")
                        .append(FBDateTime).append("|")
                        .append(feedbackNotes).append("|")
                        .append("").append("|")
                        .append("").append("|")
                        .append("").append("|")
                        .append("");

                exportRow.append(System.getProperty("line.separator"));

                fw.write(exportRow.toString());

                /* Get all notes */
                List<transactionOutNotes> transactionNotes = transactionOutDAO.getNotesByTransactionId(transaction.getId());

                if (transactionNotes != null && transactionNotes.size() > 0) {

                    for (transactionOutNotes note : transactionNotes) {

                        exportRow = new StringBuilder();

                        User noteUser = usermanager.getUserById(note.getuserId());

                        exportRow.append("C").append("|");
                        exportRow.append(sourceOrg.getOrgName()).append("|");

                        if (sourceSiteOrg != null) {
                            exportRow.append(sourceSiteOrg.getOrgName()).append("|");
                        } else {
                            exportRow.append(sourceOrg.getOrgName()).append("|");
                        }

                        exportRow.append(targetOrg.getOrgName()).append("|");

                        if (targetSiteOrg != null) {
                            exportRow.append(targetSiteOrg.getOrgName()).append("|");
                        } else {
                            exportRow.append(targetOrg.getOrgName()).append("|");
                        }

                        exportRow.append(batchUploadSummary.getDateSubmitted()).append("|")
                                .append(batchUploadSummary.getbatchId()).append("|")
                                .append(referralId).append("|")
                                .append(patientDetails.getDob()).append("|")
                                .append(patientDetails.getGenderVal()).append("|")
                                .append(patientDetails.getRaceVal()).append("|")
                                .append(patientDetails.getEthnicityVal()).append("|")
                                .append(patientDetails.getLanguageVal()).append("|")
                                .append(patientDetails.getZip()).append("|")
                                .append(messageTypeName).append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append("").append("|")
                                .append(note.getMessageStatus()).append("|")
                                .append(note.getdateSubmitted()).append("|")
                                .append(note.getnote().trim()).append("|")
                                .append(noteUser.getFirstName()).append(" ")
                                .append(noteUser.getLastName());

                        exportRow.append(System.getProperty("line.separator"));

                        fw.write(exportRow.toString());

                    }

                }

            }

            fw.close();
        }

    }

    @Override
    public void clearMultipleTargets(Integer batchId) throws Exception {
        transactionInDAO.clearMultipleTargets(batchId);
    }

    @Override
    public void sendRejectNotification(batchUploads batch, Integer rejectCount) throws Exception {

        mailMessage mail = new mailMessage();
        mail.setfromEmailAddress("support@health-e-link.net");

        //String[] ccAddresses = new String[2];
        List<String> ccAddresses = new ArrayList<String>();
        if (!myProps.getProperty("reject.ccemail").equalsIgnoreCase("")) {
        	ccAddresses.add(myProps.getProperty("reject.ccemail"));
        }

        //build message
        String message = "Batch" + batch.getutBatchName() + " contains " + rejectCount + " rejected transaction(s).";
        message = message + "<br/><br/>Environment: " + myProps.getProperty("server.identity");

        message = message + "<br/><br/>Please see details below.";

        message = message + "<br/><br/>Batch Id: " + batch.getutBatchName();

        List<Transaction> transactions = getTransactionsByStatusId(batch.getId(), rejectIds, 5);
        for (Transaction transaction : transactions) {

            message += "<br /><br />Sending Organization: " + transaction.getSrcOrgName();

            if (!"".equals(transaction.getSrcSiteName())) {
                message += "<br /><br />Sending Site: " + transaction.getSrcSiteName();
            }
            message += "<br /><br />Target Organization: " + transaction.getTargetOrgName();
            message += "<br /><br />Referral Type: " + transaction.getSrcConfigName();
            //message += "<br /><br />Date/Time Received: " + transaction.getdateSubmitted();

            /* If Holyoke Health Center send to their primary contact */
            List<User> orgUsers = null;
            if (transaction.getOrgId() == 68 || transaction.getParentOrgId() == 68) {
                if (transaction.getParentOrgId() > 0) {
                    orgUsers = usermanager.getOrganizationContact(transaction.getParentOrgId(), 1);
                } else {
                    orgUsers = usermanager.getOrganizationContact(transaction.getOrgId(), 1);
                }

                if (orgUsers != null && orgUsers.size() > 0) {
                    if (orgUsers.get(0).getStatus() == true && !"".equals(orgUsers.get(0).getEmail())) {
                        ccAddresses.add(orgUsers.get(0).getEmail());
                    }
                }
            }
        }

        mail.setmessageBody(message);
        mail.setmessageSubject("Rejected Referral submitted on " + myProps.getProperty("server.identity") + " environment");
        mail.settoEmailAddress(myProps.getProperty("reject.email"));

        if (ccAddresses.size() > 0) {
            String[] ccEmailAddresses = new String[ccAddresses.size()];
            ccEmailAddresses = ccAddresses.toArray(ccEmailAddresses);
            mail.setccEmailAddress(ccEmailAddresses);
        }

       emailManager.sendEmail(mail);
    }

    @Override
    public List<Transaction> getTransactionsByStatusId(Integer batchId,
            List<Integer> statusIds, Integer howMany) throws Exception {

        List<Transaction> transactions = setTransactionInInfoByStatusId(batchId, statusIds, howMany);

        for (Transaction transaction : transactions) {
            transaction.setSrcOrgName(organizationmanager.getOrganizationById(transaction.getorgId()).getOrgName());
            Transaction transactionOut = setTransactionTargetInfoByStatusId(transaction);
            if (transactionOut != null) {
                transaction.setTargetOrgName(organizationmanager.getOrganizationById(transactionOut.gettargetOrgId()).getOrgName());
                transaction.setTargetConfigId1(transactionOut.getTargetConfigId1());
                transaction.setTargetConfigName(transactionOut.getTargetConfigName());
                transaction.settransactionTargetId(transactionOut.gettransactionTargetId());
                transaction.settargetOrgId(transactionOut.gettargetOrgId());
            }
        }
        return transactions;

    }

    @Override
    public List<Transaction> setTransactionInInfoByStatusId(Integer batchId, List<Integer> statusIds, Integer howMany) throws Exception {
        return transactionInDAO.setTransactionInInfoByStatusId(batchId, statusIds, howMany);
    }

    @Override
    public Transaction setTransactionTargetInfoByStatusId(Transaction transaction) throws Exception {
        return transactionInDAO.setTransactionTargetInfoByStatusId(transaction);
    }

    @Override
    public List<batchUploads> getMassTranslateBatchForOutput(Integer howMany)
            throws Exception {
        return transactionInDAO.getMassTranslateBatchForOutput(howMany);
    }

    @Override
    public void loadMassBatches() throws Exception {
        //we check to see if anything is running first
    	boolean run = true; 
    	List<batchUploads> batchInProcess = getBatchesByStatusIds(Arrays.asList(38));
    	//we check time stamp to see how long that file has been processing
		//get the details
    	if (batchInProcess.size() != 0) {
	    	batchUploads batchDetails = getBatchDetails(batchInProcess.get(0).getId());
			//check how long first batch is going
	        //if more than 2 hours need to email 
	        LocalDateTime d1 = LocalDateTime.ofInstant(batchDetails.getstartDateTime().toInstant(), ZoneId.systemDefault());
	        LocalDateTime d2 = LocalDateTime.now();
	        long diffHours = java.time.Duration.between(d1, d2).toHours();
	        run = false;
	        if (diffHours > 2) {
	        	 mailMessage mail = new mailMessage();
	        	 mail.settoEmailAddress(myProps.getProperty("admin.email"));
	        	 mail.setfromEmailAddress("support@health-e-link.net");
	        	 mail.setmessageSubject("HEL Loading Mass Batch running for " + diffHours + " hours - " + myProps.getProperty("server.identity"));
	        	 StringBuilder emailBody = new StringBuilder();
	             emailBody.append("<br/>Current Time " + d2.toString());
	             emailBody.append("<br/>There are " + batchInProcess.size() + " with status 38 in queue.<br/>");
	             emailBody.append("First stuck batch is id " + batchDetails.getId() + " - " + batchDetails.getoriginalFileName() + ".");
	             mail.setmessageBody(emailBody.toString());
	             emailManager.sendEmail(mail);
	            //files should not take more than 4 hours to run, we check the last 48 on the list in case another one is running already
	             d1 = LocalDateTime.ofInstant(batchInProcess.get(batchInProcess.size() - 1).getstartDateTime().toInstant(), ZoneId.systemDefault());
	             long diffHours2 = java.time.Duration.between(d1, d2).toHours();
	             if (diffHours2 < 4) {
	                 System.out.println(d2 + " load mass batches not running - " + diffHours2);
	             }	else {
	            	 run = true;
	             }
	        }
    	}
        if (run) {
        	List<batchUploads> batches = getBatchesByStatusIds(Arrays.asList(42));
        	if (batches != null && batches.size() != 0) {
        		loadBatch(batches.get(0).getId());
        	}
        }
    }

    /**
     * We should only be loading up mass batches every 10 mins
     */
    @Override
    public void processMassBatches() throws Exception{
    	// we only want to process one mass batches at a time but don't want to set the scheduler to be 1.5 hours each time a file runs
    	//we check to see if anything is running first
    	boolean run = true; 
    	List<batchUploads> batchInProcess = getBatchesByStatusIds(Arrays.asList(4));
    	//we check time stamp to see how long that file has been processing
		//get the details
    	if (batchInProcess.size() != 0) {
			batchUploads batchDetails = getBatchDetails(batchInProcess.get(0).getId());
			//check how long first batch is going
	        //if more than 2 hours need to email 
	        LocalDateTime d1 = LocalDateTime.ofInstant(batchDetails.getstartDateTime().toInstant(), ZoneId.systemDefault());
	        LocalDateTime d2 = LocalDateTime.now();
	        long diffHours = java.time.Duration.between(d1, d2).toHours();
	        run = false;
	        if (diffHours > 2) {
	        	 mailMessage mail = new mailMessage();
	        	 mail.settoEmailAddress(myProps.getProperty("admin.email"));
	        	 mail.setfromEmailAddress("support@health-e-link.net");
	        	 mail.setmessageSubject("HEL Loading Mass Batch running for " + diffHours + " hours - " + myProps.getProperty("server.identity"));
	        	 StringBuilder emailBody = new StringBuilder();
             emailBody.append("<br/>Current Time " + d2.toString());
             emailBody.append("<br/>There are " + batchInProcess.size() + " with status 4 in queue.<br/>");
             emailBody.append("First stuck batch is id " + batchDetails.getId() + " - " + batchDetails.getoriginalFileName() + ".");
             mail.setmessageBody(emailBody.toString());
             emailManager.sendEmail(mail);
            //files should not take more than 4 hours to run, we check the last 4 on the list in case another one is running already
             d1 = LocalDateTime.ofInstant(batchInProcess.get(batchInProcess.size() - 1).getstartDateTime().toInstant(), ZoneId.systemDefault());
             long diffHours2 = java.time.Duration.between(d1, d2).toHours();
             if (diffHours2 < 4) {
                 System.out.println(d2 + " process mass batches not running - " + diffHours2);
             }	else if (diffHours2 >4) {
            	 run = true;
             } 
	        }
	    }
    	if (run) {
        	//0. grab all mass batches with MSL (43)
            try {
                List<batchUploads> batches = getBatchesByStatusIds(Arrays.asList(43));
                if (batches != null && batches.size() != 0) {
                    //we process one file at a time
                    processBatch(batches.get(0).getId(), false, 0);
                }
            } catch (Exception ex1) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ("processMassBatches error - " + ex1));
            }
        } 
   }

    @Override
    public void saveBatchClearAfterDelivery(batchClearAfterDelivery bmt) throws Exception {
        transactionInDAO.saveClearAfterDelivery(bmt);
    }

    @Override
    public void updateBatchClearAfterDelivery(batchClearAfterDelivery bmt) throws Exception {
        transactionInDAO.updateClearAfterDelivery(bmt);
    }

    @Override
    public void clearAfterDeliveryBatches() throws Exception {
        //we get all bmt with statusId of 0
        List<batchClearAfterDelivery> bmts = getBatchClearAfterDelivery(Arrays.asList(0));
        //we clear one batch at a time
        if (bmts.size() != 0) {
            clearAfterDeliveryBatch(bmts.get(0));
        }
    }

    @Override
    public void clearAfterDeliveryBatch(batchClearAfterDelivery cad)
            throws Exception {
        //first we recheck status
        batchClearAfterDelivery cadToClear = getClearAfterDeliverById(cad.getId());
        //then we update status
        if (cadToClear.getStatusId() == 0) {
            cadToClear.setStatusId(1);
            updateBatchClearAfterDelivery(cadToClear);
            if (cadToClear.getTransactionInId() != 0) {
                //we clear transaction
                clearMessageTablesByTransactionInId(cadToClear.getTransactionInId());
                clearTransactionInRecords(cadToClear.getBatchUploadId(), cadToClear.getTransactionInId());
                clearTransactionTranslatedIn(cadToClear.getBatchUploadId(), cadToClear.getTransactionInId());
                //clear the out tables
                transactionOutDAO.clearTransactionOutRecords(cadToClear.getTransactionTargetId());
                transactionOutDAO.clearTransactionTranslatedOut(cadToClear.getTransactionTargetId());

            } else {
                //we clear tables by batchId, everything else should be cleared already
                clearMessageTables(cad.getBatchUploadId());
            }
            //when done, set status to 2
            cadToClear.setStatusId(2);
            cadToClear.setDateCompleted(new Date());
            updateBatchClearAfterDelivery(cadToClear);
        }
    }

    @Override
    public List<batchClearAfterDelivery> getBatchClearAfterDelivery(
            List<Integer> statusIds) throws Exception {
        return transactionInDAO.getClearAfterDeliveryBatches(statusIds);
    }

    @Override
    public batchClearAfterDelivery getClearAfterDeliverById(Integer bmtId)
            throws Exception {
        return transactionInDAO.getClearAfterDeliveryById(bmtId);
    }

    @Override
    public List<CrosswalkData> getCrosswalkDataForBatch(
            configurationDataTranslations cdt, Integer batchId,
            boolean foroutboundProcessing, Integer transactionId)
            throws Exception {
        return transactionInDAO.getCrosswalkDataForBatch(cdt, batchId, foroutboundProcessing, transactionId);
    }

    @Override
    public void translateCWForBatch(configurationDataTranslations cdt,
            Integer batchId, boolean foroutboundProcessing,
            Integer transactionId) throws Exception {
        transactionInDAO.translateCWForBatch(cdt, batchId, foroutboundProcessing, transactionId);
    }

    /**
     * this method selects out all the referralActivityExport that are status of 1. It locks the status to 2 so it won't get selected again. It processes them one by one.
     */
    @Override
    public void processReferralActivityExportJob() throws Exception {
        // TODO Auto-generated method stub
        List<referralActivityExports> exports = getReferralActivityExportsByStatus(Arrays.asList(1), 1);
        for (referralActivityExports export : exports) {
            User userDetails = usermanager.getUserById(export.getCreatedBy());
            export.setStatusId(2);
            export.setRunStart(new Date());
            updateReferralActivityExport(export);
            //now we create the report
            createNewReferralActivityExport(export);
            //after creating the report
            if (export.getFileName().length() == 0) {
                export.setStatusId(6);
            } else {
                export.setStatusId(3);
            }
            export.setRunEnd(new Date());
            updateReferralActivityExport(export);
            //now we send email
            try {
                sendExportEmail(userDetails);
            } catch (Exception ex) {
                System.out.println("Export Email is not sent for export id - " + export.getId());
                ex.printStackTrace();
            }
        }
    }

    @Override
    public List<referralActivityExports> getReferralActivityExportsByStatus(
            List<Integer> statusIds, Integer howMany) throws Exception {
        return transactionInDAO.getReferralActivityExportsByStatus(statusIds, howMany);
    }

    @Override
    public void updateReferralActivityExport(referralActivityExports activityExport) throws Exception {
        transactionInDAO.updateReferralActivityExport(activityExport);
    }

    @Override
    public void sendExportEmail(User userDetails) throws Exception {
        String exportMessage = "Dear " + userDetails.getFirstName() + ", <br/>Please login to download your referral activity export.  Thank you.";
        mailMessage mail = new mailMessage();
        mail.setfromEmailAddress("support@health-e-link.net");
        mail.setmessageBody(exportMessage);
        mail.setmessageSubject("Referral activity export is ready to be downloaded.");
        mail.settoEmailAddress(userDetails.getEmail());
        emailManager.sendEmail(mail);
    }

    @Override
    public void saveReferralActivityExport(referralActivityExports activityExport) throws Exception {
        transactionInDAO.saveReferralActivityExport(activityExport);
    }

    @Override
    public List<referralActivityExports> getReferralActivityExportsWithUserNames(List<Integer> statusIds) throws Exception {
        return transactionInDAO.getReferralActivityExportsWithUserNames(statusIds);
    }

    @Override
    public referralActivityExports getReferralActivityExportById(
            Integer exportId) throws Exception {
        return transactionInDAO.getReferralActivityExportById(exportId);
    }
    
    /**
     * The 'setOutboundFormFields' will create and populate the form field object
     *
     * @param formfields The list of form fields
     * @param records The values of the form fields to populate with.
     *
     * @return This function will return a list of transactionRecords fields with the correct data
     *
     * @throws NoSuchMethodException
     */
    @Override
    @Transactional
    public List<transactionRecords> setOutboundFormFields(List<configurationFormFields> formfields, transactionInRecords records, int configId, boolean readOnly, int orgId, int clientId) throws NoSuchMethodException, ParseException {

        List<transactionRecords> fields = new ArrayList<transactionRecords>();

        //we can map the process status so we only have to query once
        List validationTypeList = messagetypemanager.getValidationTypes();
        Map<Integer, String> vtMap = new HashMap<Integer, String>();

        for (ListIterator iter = validationTypeList.listIterator(); iter.hasNext();) {

            Object[] row = (Object[]) iter.next();

            vtMap.put(Integer.valueOf(String.valueOf(row[0])), String.valueOf(row[1]));

        }
        
        for (configurationFormFields formfield : formfields) {
            transactionRecords field = new transactionRecords();
            field.setfieldNo(formfield.getFieldNo());
            field.setrequired(formfield.getRequired());
            field.setsaveToTable(formfield.getsaveToTableName());
            field.setsaveToTableCol(formfield.getsaveToTableCol());
            field.setfieldLabel(formfield.getFieldLabel());
            field.setreadOnly(readOnly);
            field.setfieldValue(null);
            field.setFieldType(formfield.getFieldType());
            field.setFieldHelp(formfield.getFieldHelp());
            field.setUseField(formfield.getUseField());

            /* Get the pre-populated values */
            String tableName = "";
            String tableCol = "";
            if(clientId > 0 && formfield.getUseField() == true) {
              tableName = "clients";
              tableCol = formfield.getsaveToTableCol();
            }
            else {
              tableName = formfield.getautoPopulateTableName();  
              tableCol = formfield.getautoPopulateTableCol();
            }
            
            /* Get the validation */
            if (formfield.getValidationType() > 1) {
                field.setvalidation(vtMap.get(formfield.getValidationType()));
            }
            
            if (records != null) {
                String colName = new StringBuilder().append("f").append(formfield.getFieldNo()).toString();
                try {
                    String fieldValue = BeanUtils.getProperty(records, colName);

                    if (fieldValue == null || "null".equals(fieldValue)) {
                        fieldValue = "";
                    } /* Check if date */ else if (fieldValue.length() == 10 && fieldValue.contains("-") && StringUtils.countOccurrencesOf(fieldValue, "-") == 2) {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fieldValue);
                        fieldValue = new SimpleDateFormat("MM/dd/yyyy").format(date);
                    }

                    field.setfieldValue(fieldValue);

                } catch (IllegalAccessException ex) {
                    Logger.getLogger(transactionInManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(transactionInManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }

                /* If autopopulate field is set make it read only */
                if (!tableName.isEmpty() && !tableCol.isEmpty()) {
                    field.setreadOnly(true);
                }
            } 
            else if (clientId > 0) {
                field.setreadOnly(true);
                if (!tableName.isEmpty() && !tableCol.isEmpty()) {
                    try {
                        field.setfieldValue(getFieldValue(tableName, tableCol, "id", clientId));
                    }
                    catch (Exception e) {
                        field.setFieldValue("");
                    }
                }
            }
            else if (orgId > 0) {

                if (!tableName.isEmpty() && !tableCol.isEmpty()) {
                    field.setfieldValue(getFieldValue(tableName, tableCol, "id", orgId));
                }
            } 

            if (configId > 0) {
                /* See if any fields have crosswalks associated to it */
                List<fieldSelectOptions> fieldSelectOptions = getFieldSelectOptions(formfield.getId(), configId);
                field.setfieldSelectOptions(fieldSelectOptions);
            }

            fields.add(field);
        }

        return fields;
    }

	@Override
	public void populateAuditReport(Integer batchUploadId, configurationMessageSpecs cms)
			throws Exception {
		
		//first we run store procedure
		transactionInDAO.populateAuditReport(batchUploadId, cms.getconfigId());
		// get distinct fieldNo involved
		List <Integer> fieldNoList = getErrorFieldNos(batchUploadId);
		//update field data
		for (Integer fieldNo : fieldNoList) {
			populateFieldError(batchUploadId, fieldNo, cms);
		}
		
	}

	@Override
	public List<Integer> getErrorFieldNos(Integer batchUploadId)
			throws Exception {
		return transactionInDAO.getErrorFieldNos(batchUploadId);
	}

	@Override
	public void populateFieldError(Integer batchUploadId, Integer fieldNo,
			configurationMessageSpecs cms) throws Exception {
		transactionInDAO.populateFieldError(batchUploadId, fieldNo, cms);
	}

	@Override
	public void cleanAuditErrorTable(Integer batchUploadId) throws Exception {
		transactionInDAO.cleanAuditErrorTable(batchUploadId);
	}
    
	@Override
	public void deleteMoveFileLogsByStatus(Integer statusId)  throws Exception{
		transactionInDAO.deleteMoveFileLogsByStatus(statusId);		
	}

	@Override
	public void deleteLoadTableRows(Integer howMany, String ascOrDesc,
			String laodTableName) throws Exception{
		transactionInDAO.deleteLoadTableRows(howMany, ascOrDesc, laodTableName);	
		
	}

	@Override
	public Integer clearTransactionTranslatedListIn(Integer batchUploadId) {
		return transactionInDAO.clearTransactionTranslatedListIn(batchUploadId);
	}

}
