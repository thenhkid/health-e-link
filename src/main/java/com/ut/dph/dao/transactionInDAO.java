/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.dao;

import com.ut.dph.model.CrosswalkData;
import com.ut.dph.model.Macros;
import com.ut.dph.model.TransactionInError;
import com.ut.dph.model.User;
import com.ut.dph.model.batchUploadSummary;
import com.ut.dph.model.batchUploads;
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

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 *
 * @author chadmccue
 */
@Repository
public interface transactionInDAO {
    
    String getFieldValue(String tableName, String tableCol, String idCol, int idValue);
    
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
    
    batchUploads getBatchDetailsByBatchName(String batchName) throws Exception;
    
    transactionIn getTransactionDetails(int transactionId) throws Exception;
    
    transactionInRecords getTransactionRecords(int transactionId);
    
    transactionInRecords getTransactionRecord(int recordId);
     
    void submitTransactionTarget(transactionTarget transactionTarget);
    
    transactionTarget getTransactionTargetDetails(int transactionTargetId);
    
    void submitTransactionTargetChanges(transactionTarget transactionTarget) throws Exception;
    
    transactionTarget getTransactionTarget(int batchUploadId, int transactionInId);
    
    Integer submitAttachment(transactionAttachment attachment) throws Exception;
    
    transactionAttachment getAttachmentById(int attachmentId) throws Exception;
    
    void submitAttachmentChanges(transactionAttachment attachment) throws Exception;
    
    List<transactionAttachment> getAttachmentsByTransactionId(int transactionInId) throws Exception;
    
    void removeAttachmentById(int attachmentId) throws Exception;
    
    List <Integer> getConfigIdsForBatch (int batchUploadId, boolean getAll);
    
    List <ConfigForInsert> setConfigForInsert(int configId, int batchUploadId);
    
    List<Integer> getTransWithMultiValues(ConfigForInsert config);
    
    boolean insertSingleToMessageTables(ConfigForInsert configForInsert);
    
    boolean insertMultiValToMessageTables(ConfigForInsert config, Integer subStringCounter, Integer transId);
    
    Integer clearMessageTableForBatch(int batchId, String tableName);
    
    public List <String> getMessageTables();
    
    List<Integer> getBlankTransIds(ConfigForInsert config);
    
    Integer genericValidation(configurationFormFields cff, Integer validationTypeId, Integer batchUploadId, String regEx);
    
    Integer countSubString(ConfigForInsert config, Integer transId);
    
    List<batchUploads> getuploadedBatches(int userId, int orgId, Date fromDate, Date toDate, String searchTerm, int page, int maxResults) throws Exception;
    
    List<batchUploads> getuploadedBatches(int userId, int orgId, Date fromDate, Date toDate, String searchTerm, int page, int maxResults, List <Integer> excludedStatusIds) throws Exception;
    
    void updateBatchStatus (Integer batchUploadId, Integer statusId, String timeField) throws Exception;
    
    void updateTransactionStatus (Integer batchUploadId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception;
    
    void updateTransactionTargetStatus (Integer batchUploadId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception;
    
    boolean allowBatchClear (Integer batchUploadId);
    
    Integer clearTransactionInRecords(Integer batchId);
    
    Integer clearTransactionTranslatedIn(Integer batchUploadId);
    
    Integer clearTransactionTarget(Integer batchUploadId);

    Integer clearTransactionIn(Integer batchUploadId);
     
    Integer insertFailedRequiredFields(configurationFormFields cff, Integer batchUploadId);
    
    Integer clearTransactionInErrors(Integer batchUploadId, boolean leaveFinalStatusIds);
    
    void updateStatusForErrorTrans(Integer batchId, Integer statusId, boolean foroutboundProcessing);
    
    void updateBlanksToNull(configurationFormFields cff, Integer batchUploadId);
    
    List<transactionRecords> getFieldColAndValues (Integer batchUploadId, configurationFormFields cff);
    
    void updateFieldValue (transactionRecords tr, String newValue);
    
    void insertValidationError(transactionRecords tr, configurationFormFields cff, Integer batchUploadId);
    
    Integer getFeedbackReportConnection(int configId, int targetorgId);
    
    void nullForCWCol(Integer configId, Integer batchId, boolean foroutboundProcessing);
    
    void executeCWData(Integer configId, Integer batchId, Integer fieldNo, CrosswalkData cwd, boolean foroutboundProcessing, Integer fieldId);
    
    void updateFieldNoWithCWData (Integer configId, Integer batchId, Integer fieldNo, Integer passClear, boolean foroutboundProcessing);

    void flagCWErrors (Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing);
   
    void flagMacroErrors (Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing);

    void resetTransactionTranslatedIn (Integer batchId, boolean resetAll);
    
    Integer executeMacro (Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing, Macros macro);
    
    void insertProcessingError(Integer errorId, Integer configId, Integer batchId, Integer fieldNo, Integer macroId, Integer cwId, Integer validationTypeId, boolean required, boolean foroutboundProcessing, String errorCause);
    
    void insertProcessingError(Integer errorId, Integer configId, Integer batchId, Integer fieldNo, Integer macroId, Integer cwId, Integer validationTypeId, boolean required, boolean foroutboundProcessing, String errorCause, Integer transactionId);
    
    List<configurationTransport> getHandlingDetailsByBatch(int batchId) throws Exception; 
    
    void updateRecordCounts (Integer batchId, List <Integer> statusIds, boolean foroutboundProcessing, String colNameToUpdate);
    
    Integer getRecordCounts (Integer batchId, List <Integer> statusIds, boolean foroutboundProcessing);
    
    Integer getRecordCounts (Integer batchId, List <Integer> statusIds, boolean foroutboundProcessing, boolean inStatusIds);
    
    Integer copyTransactionInStatusToTarget(Integer batchId);
    
    Integer insertLoadData (Integer batchId, String delimChar, String fileWithPath, String tableName, boolean containsHeaderRow);
    
    Integer createLoadTable (String loadTableName);
    
    Integer dropLoadTable (String loadTableName);
   
    Integer updateLoadTable(String loadTableName, Integer batchId);
    
    Integer loadTransactionIn (String loadTableName, Integer batchId);
    
    Integer loadTransactionInRecords (Integer batchId);
    
    Integer loadTransactionInRecordsData (String loadTableName);
    
    Integer updateConfigIdForBatch(Integer batchId, Integer configId);
    
    Integer loadTransactionTranslatedIn (Integer batchId);
    
    Integer insertBatchUploadSummaryAll (batchUploads batchUpload, configurationConnection batchTargets);
    
    Integer insertBatchTargets (Integer batchId);
    
    List <configurationConnection> getBatchTargets (Integer batchId, boolean active);
    
    Integer clearBatchUploadSummary(Integer batchId);
    
    List<batchUploads> getBatchesByStatusIds (List <Integer> statusIds);
    
    void deleteMessage(int batchId, int transactionId) throws Exception;
    
    void cancelMessageTransaction(int transactionId) throws Exception;
    
    List <transactionInRecords> getTransactionInRecordsForBatch (Integer batchId);
    
    Integer updateConfigIdForCMS(Integer batchId, configurationMessageSpecs cms);
    
    Integer insertInvalidConfigError(Integer batchId);
    
    Integer updateInvalidConfigStatus(Integer batchId);

    Integer indexLoadTable(String loadTableName);
    
    batchUploadSummary getUploadSummaryDetails(int transactionInId);
    
    Integer clearBatchDownloadSummaryByUploadBatchId(Integer batchId);
    
    Integer clearTransactionTranslatedOutByUploadBatchId(Integer batchId);
    
    Integer clearTransactionOutRecordsByUploadBatchId(Integer batchId);
    
    Integer rejectInvalidTargetOrg (Integer batchId, configurationConnection batchTargets);
    
    Integer insertBatchUploadSumByOrg (batchUploads batchUpload, configurationConnection confConn);
    
    Integer setStatusForErrorCode(Integer batchId, Integer statusId, Integer errorId, boolean foroutboundProcessing);
    
    Integer rejectNoConnections (batchUploads batch);
    
    List <Integer> getDuplicatedIds (Integer batchId);
    
    List <batchUploadSummary> getBatchUploadSummary (Integer transactionInId);
    
    Integer insertTransactionInByTargetId(batchUploadSummary bus);
    
    Integer getTransactionInIdByTargetId(batchUploadSummary bus);
    
    Integer updateTInIdForTransactiontarget(batchUploadSummary bus, Integer newTInId);
    
    Integer updateTINIDForBatchUploadSummary(batchUploadSummary bus, Integer newTInId);
    
    Integer copyTransactionInRecord(Integer newTInId, Integer oldTInId);
    
    Integer insertTransactionTranslated(Integer newTInId, batchUploadSummary bus);
    
    List <batchUploads> getAllUploadedBatches(Date fromDate, Date toDate, String searchTerm, int page, int maxResults) throws Exception;
    
    boolean checkPermissionForBatch (User userInfo, batchUploads batchInfo);
    
    List <TransactionInError> getErrorList (Integer batchId);
    
    ConfigErrorInfo setConfigErrorInfo(Integer batchId, Integer errorCode, ConfigErrorInfo configErrorInfo);
    
    List <TransErrorDetail> getTransErrorDetailsForNoRptFields(Integer batchId, List<Integer> errorCodes);
    
    Integer getCountForErrorId (Integer batchId, Integer errorId);
    
    List <TransErrorDetail> getTransErrorDetailsForInvConfig(Integer batchId);
    
    List <ConfigErrorInfo> getErrorConfigForBatch(Integer batchId);
     
    ConfigErrorInfo getHeaderForConfigErrorInfo(Integer batchId, ConfigErrorInfo configErrorInfo, List<Integer> rptFieldArray );
    
    List <TransErrorDetail> getTransErrorDetails(batchUploads batchInfo, ConfigErrorInfo configErrorInfo);
    
    TransErrorDetail getTransErrorData(TransErrorDetail ted, String sqlStmt);
    
    Integer insertNoPermissionConfig(batchUploads batch);
    
    Integer updateStatusByErrorCode(Integer batchId, Integer errorId, Integer statusId);
    
    batchUploads getBatchDetailsByTInId(Integer transactionInId); 
    
    void updateTranStatusByTInId (Integer transactionInId, Integer statusId) throws Exception;
    
    List<TransErrorDetail> getTransactionErrorsByFieldNo(int transactionInId, int fieldNo) throws Exception;
}
