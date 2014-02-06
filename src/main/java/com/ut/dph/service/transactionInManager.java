/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service;

import com.ut.dph.model.CrosswalkData;
import com.ut.dph.model.batchUploadSummary;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.transactionAttachment;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import com.ut.dph.model.transactionRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.model.custom.ConfigForInsert;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author chadmccue
 */
public interface transactionInManager {
    
    String getFieldValue(String tableName, String tableCol, int idValue);
    
    List<fieldSelectOptions> getFieldSelectOptions(int fieldId, int configId);
    
    Integer submitBatchUpload(batchUploads batchUpload);
    
    void submitBatchUploadSummary(batchUploadSummary summary);
    
    void submitBatchUploadChanges(batchUploads batchUpload);
    
    Integer submitTransactionIn(transactionIn transactionIn);
    
    void submitTransactionInChanges(transactionIn transactionIn);
    
    Integer submitTransactionInRecords(transactionInRecords records);
    
    void submitTransactionInRecordsUpdates(transactionInRecords records);
    
    void submitTransactionTranslatedInRecords(int transactionId, int transactionRecordId, int configId);
    
    List<batchUploads> getpendingBatches(int userId, int orgId, int page, int maxResults);
    
    List<batchUploads> findBatches(List<batchUploads> batches, String searchTerm);
    
    List<transactionIn> getBatchTransactions(int batchId, int userId);
    
    List<batchUploads> getsentBatches(int userId, int orgId, int page, int maxResults);
    
    batchUploads getBatchDetails(int batchId);
    
    transactionIn getTransactionDetails(int transactionId);
    
    transactionInRecords getTransactionRecords(int transactionId);
    
    transactionInRecords getTransactionRecord(int recordId);
    
    void submitTransactionTarget(transactionTarget transactionTarget);
    
    transactionTarget getTransactionTargetDetails(int transactionTargetId);
    
    void submitTransactionTargetChanges(transactionTarget transactionTarget);
    
    transactionTarget getTransactionTarget(int batchUploadId, int transactionInId);
    
    String uploadAttachment(MultipartFile fileUpload, String orgName);
    
    Integer submitAttachment(transactionAttachment attachment);
    
    transactionAttachment getAttachmentById(int attachmentId);
    
    void submitAttachmentChanges(transactionAttachment attachment);
    
    List<transactionAttachment> getAttachmentsByTransactionId(int transactionInId);
    
    void removeAttachmentById(int attachmentId);
    
    boolean processTransactions(int batchUploadId);
    
    boolean insertSingleToMessageTables(ConfigForInsert configForInsert);
    
    boolean insertMultiValToMessageTables(ConfigForInsert config, Integer subStringCounter, Integer transId);
    
    List <ConfigForInsert> setConfigForInsert(int configId, int batchUploadId);
    
    List <Integer> getConfigIdsForBatch (int batchUploadId);
    
    List <Integer> getTransWithMultiValues (ConfigForInsert config);

    boolean clearMessageTables(int batchId);
    
     Map<String,String> uploadBatchFile(int configId, MultipartFile fileUpload);
    
    List <Integer> getBlankTransIds (ConfigForInsert config);
    
    Integer countSubString(ConfigForInsert config, Integer transId);
    
    List<batchUploads> getuploadedBatches(int userId, int orgId);
    
    boolean processBatch(int batchUploadId);
    
    boolean processBatches();
    
    void updateBatchStatus (Integer batchUploadId, Integer statusId, String timeField);
    
    void updateTransactionStatus (Integer batchUploadId, Integer fromStatusId, Integer toStatusId);
    
    void updateTransactionTargetStatus (Integer batchUploadId, Integer fromStatusId, Integer toStatusId);
    
    boolean clearBatch(Integer batchUploadId);
    
    boolean setDoNotProcess(Integer batchUploadId);
    
    boolean clearTransactionInRecords(Integer batchUploadId);
    
    boolean insertTransactions (Integer batchUploadId);
    
    boolean loadBatch (Integer batchUploadId);
    
    boolean clearTransactionIn(Integer batchUploadId);
    
    boolean clearTransactionTranslatedIn(Integer batchUploadId);
    
    boolean clearTransactionTables(Integer batchUploadId);
    
    boolean clearTransactionTarget(Integer batchUploadId);

    void flagAndEmailAdmin(Integer batchUploadId);
    
    List <configurationFormFields> getRequiredFieldsForConfig (Integer configId);
    
    boolean insertFailedRequiredFields(configurationFormFields cff, Integer batchUploadId);
    
    boolean clearTransactionInErrors(Integer batchUploadId);
    
    void updateStatusForErrorTrans(Integer batchUploadId, Integer statusId);
    
    boolean runValidations(Integer batchUploadId, Integer configId);
    
    void genericValidation(configurationFormFields cff, Integer validationTypeId, Integer batchUploadId, String regEx);
    
    void urlValidation(configurationFormFields cff, Integer validationTypeId, Integer batchUploadId);
    
    void dateValidation(configurationFormFields cff, Integer validationTypeId, Integer batchUploadId);
    
    void updateBlanksToNull(configurationFormFields cff, Integer batchUploadId);
    
    List<transactionRecords> getFieldColAndValues (Integer batchUploadId, configurationFormFields cff);
    
    Date convertLongDate (String dateValue);
    
    void updateFieldValue (transactionRecords tr, String newValue);
    
    void insertValidationError(transactionRecords tr, configurationFormFields cff, Integer batchUploadId);
    
    Integer getFeedbackReportConnection(int configId, int targetorgId);
       
    String formatDateForDB (Date date);
    
    Date convertDate(String date);
    
    boolean chkMySQLDate(String date);
	
    boolean isValidURL(String url);
    
    boolean processCrosswalk (Integer configId, Integer batchUploadId, configurationDataTranslations translation);
    
    boolean processMacro (Integer configId, Integer batchUploadId, configurationDataTranslations translation);
    
    void nullForSWCol(Integer configId, Integer batchUploadId);
    
    void executeCWData(Integer configId, Integer batchUploadId, Integer fieldNo, CrosswalkData cwd);

}
