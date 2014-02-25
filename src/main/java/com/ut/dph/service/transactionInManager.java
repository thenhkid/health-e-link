/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service;

import com.ut.dph.model.CrosswalkData;
import com.ut.dph.model.Macros;
import com.ut.dph.model.batchUploadSummary;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;
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
    
    Integer submitBatchUpload(batchUploads batchUpload) throws Exception;
    
    void submitBatchUploadSummary(batchUploadSummary summary) throws Exception;
    
    void submitBatchUploadChanges(batchUploads batchUpload) throws Exception;
    
    Integer submitTransactionIn(transactionIn transactionIn) throws Exception;
    
    void submitTransactionInChanges(transactionIn transactionIn) throws Exception;
    
    Integer submitTransactionInRecords(transactionInRecords records) throws Exception;
    
    void submitTransactionInRecordsUpdates(transactionInRecords records) throws Exception;
    
    void submitTransactionTranslatedInRecords(int transactionId, int transactionRecordId, int configId) throws Exception;
    
    List<batchUploads> getpendingBatches(int userId, int orgId, String searchTerm, Date fromDate, Date toDate, int page, int maxResults) throws Exception;
    
    List<transactionIn> getBatchTransactions(int batchId, int userId) throws Exception;
    
    List<batchUploads> getsentBatches(int userId, int orgId, String searchTerm, Date fromDate, Date toDate, int page, int maxResults) throws Exception;
    
    batchUploads getBatchDetails(int batchId) throws Exception;
    
    transactionIn getTransactionDetails(int transactionId) throws Exception;
    
    transactionInRecords getTransactionRecords(int transactionId);
    
    transactionInRecords getTransactionRecord(int recordId);
    
    void submitTransactionTarget(transactionTarget transactionTarget);
    
    transactionTarget getTransactionTargetDetails(int transactionTargetId);
    
    void submitTransactionTargetChanges(transactionTarget transactionTarget) throws Exception;
    
    transactionTarget getTransactionTarget(int batchUploadId, int transactionInId);
    
    String uploadAttachment(MultipartFile fileUpload, String orgName) throws Exception;
    
    Integer submitAttachment(transactionAttachment attachment) throws Exception;
    
    transactionAttachment getAttachmentById(int attachmentId) throws Exception;
    
    void submitAttachmentChanges(transactionAttachment attachment) throws Exception;
    
    List<transactionAttachment> getAttachmentsByTransactionId(int transactionInId) throws Exception;
    
    void removeAttachmentById(int attachmentId) throws Exception;
    
    boolean processTransactions(int batchUploadId);
    
    boolean insertSingleToMessageTables(ConfigForInsert configForInsert);
    
    boolean insertMultiValToMessageTables(ConfigForInsert config, Integer subStringCounter, Integer transId);
    
    List <ConfigForInsert> setConfigForInsert(int configId, int batchUploadId);
    
    List <Integer> getConfigIdsForBatch (int batchUploadId, boolean getAll);
    
    List <Integer> getTransWithMultiValues (ConfigForInsert config);

    Integer clearMessageTables(int batchId);
    
     Map<String,String> uploadBatchFile(int configId, MultipartFile fileUpload) throws Exception;
    
    List <Integer> getBlankTransIds (ConfigForInsert config);
    
    Integer countSubString(ConfigForInsert config, Integer transId);
    
    List<batchUploads> getuploadedBatches(int userId, int orgId) throws Exception;
    
    boolean processBatch(int batchUploadId) throws Exception;
    
    boolean processBatches();
    
    void updateBatchStatus (Integer batchUploadId, Integer statusId, String timeField) throws Exception;
    
    void updateTransactionStatus (Integer batchUploadId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception;
    
    void updateTransactionTargetStatus (Integer batchUploadId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception;
    
    boolean clearBatch(Integer batchUploadId) throws Exception;
    
    boolean setDoNotProcess(Integer batchUploadId);
    
    Integer clearTransactionInRecords(Integer batchUploadId);
    
    boolean insertTransactions (Integer batchUploadId);
    
    Integer loadTextBatch (batchUploads batchUpload);
    
    Integer clearTransactionIn(Integer batchUploadId);
    
    Integer clearTransactionTranslatedIn(Integer batchUploadId);
    
    Integer clearTransactionTables(Integer batchUploadId);
    
    Integer clearTransactionTarget(Integer batchUploadId);

    void flagAndEmailAdmin(Integer batchUploadId);
    
    List <configurationFormFields> getRequiredFieldsForConfig (Integer configId);
    
    Integer insertFailedRequiredFields(configurationFormFields cff, Integer batchUploadId);
    
    Integer clearTransactionInErrors(Integer batchUploadId);
    
    void updateStatusForErrorTrans(Integer batchId, Integer statusId, boolean foroutboundProcessing);
    
    Integer runValidations(Integer batchUploadId, Integer configId);
    
    Integer genericValidation(configurationFormFields cff, Integer validationTypeId, Integer batchUploadId, String regEx);
    
    Integer urlValidation(configurationFormFields cff, Integer validationTypeId, Integer batchUploadId);
    
    Integer dateValidation(configurationFormFields cff, Integer validationTypeId, Integer batchUploadId);
    
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
    
    Integer processCrosswalk (Integer configId, Integer batchId, configurationDataTranslations translation, boolean foroutboundProcessing);
    
    Integer processMacro (Integer configId, Integer batchId, configurationDataTranslations translation, boolean foroutboundProcessing);
    
    void nullForCWCol(Integer configId, Integer batchId, boolean foroutboundProcessing);
    
    void executeCWData(Integer configId, Integer batchId, Integer fieldNo, CrosswalkData cwd, boolean foroutboundProcessing, Integer fieldId);

    void updateFieldNoWithCWData (Integer configId, Integer batchId, Integer fieldNo, Integer passClear, boolean foroutboundProcessing);
    
    Integer executeMacro (Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing, Macros macro);
    
    void flagCWErrors (Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing);
    
    void flagMacroErrors (Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing);
    
    List<configurationTransport> getHandlingDetailsByBatch(int batchId);
    
    void insertProcessingError(Integer errorId, Integer configId, Integer batchId, Integer fieldId, Integer macroId, Integer cwId, Integer validationTypeId, boolean required, boolean foroutboundProcessing, String errorCause);
    
    void updateRecordCounts (Integer batchId, List <Integer> statusIds, boolean foroutboundProcessing, String colNameToUpdate);
    
    Integer getRecordCounts (Integer batchId, List <Integer> statusIds, boolean foroutboundProcessing);
    
    void resetTransactionTranslatedIn(Integer batchId, boolean resetAll);
    
    Integer copyTransactionInStatusToTarget(Integer batchId);
    
    Integer insertLoadData(Integer batchId, String delmiter, String fileWithPath);
   
    Integer createLoadTable (String tableName);
    
    Integer dropLoadTable (String tableName);
    
    Integer updateLoadTableId(String tableName);
    
}
