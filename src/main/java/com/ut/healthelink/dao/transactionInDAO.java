/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.dao;

import com.ut.healthelink.model.Transaction;
import com.ut.healthelink.model.activityReportList;
import com.ut.healthelink.model.CrosswalkData;
import com.ut.healthelink.model.Macros;
import com.ut.healthelink.model.MoveFilesLog;
import com.ut.healthelink.model.TransactionInError;
import com.ut.healthelink.model.User;
import com.ut.healthelink.model.UserActivity;
import com.ut.healthelink.model.WSMessagesIn;
import com.ut.healthelink.model.batchClearAfterDelivery;
import com.ut.healthelink.model.batchMultipleTargets;
import com.ut.healthelink.model.batchUploadSummary;
import com.ut.healthelink.model.batchUploads;
import com.ut.healthelink.model.configurationConnection;
import com.ut.healthelink.model.configurationDataTranslations;
import com.ut.healthelink.model.configurationFTPFields;
import com.ut.healthelink.model.configurationFormFields;
import com.ut.healthelink.model.configurationMessageSpecs;
import com.ut.healthelink.model.configurationRhapsodyFields;
import com.ut.healthelink.model.configurationTransport;
import com.ut.healthelink.model.fieldSelectOptions;
import com.ut.healthelink.model.transactionAttachment;
import com.ut.healthelink.model.transactionIn;
import com.ut.healthelink.model.transactionInRecords;
import com.ut.healthelink.model.transactionRecords;
import com.ut.healthelink.model.transactionTarget;
import com.ut.healthelink.model.custom.ConfigErrorInfo;
import com.ut.healthelink.model.custom.ConfigForInsert;
import com.ut.healthelink.model.custom.IdAndFieldValue;
import com.ut.healthelink.model.custom.TransErrorDetail;
import com.ut.healthelink.model.messagePatients;
import com.ut.healthelink.model.referralActivityExports;

import java.math.BigInteger;
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
    
    List<batchUploads> getpendingBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception;
    
    List<transactionIn> getBatchTransactions(int batchId, int userId) throws Exception;
    
    List<batchUploads> getsentBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception;
    
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
    
    List <Integer> getConfigIdsForBatch (int batchUploadId, boolean getAll, Integer transactionInId);
    
    List <ConfigForInsert> setConfigForInsert(int configId, int batchUploadId);
    
    List<Integer> getTransWithMultiValues(ConfigForInsert config);
    
    boolean insertSingleToMessageTables(ConfigForInsert configForInsert);
    
    boolean insertMultiValToMessageTables(ConfigForInsert config, Integer subStringCounter, Integer transId);
    
    Integer clearMessageTable(int batchId, String tableName, int transactionInId);
    
    public List <String> getMessageTables();
    
    List<Integer> getBlankTransIds(ConfigForInsert config);
    
    Integer genericValidation(configurationFormFields cff, Integer validationTypeId, Integer batchUploadId, String regEx, Integer transactionId);
    
    Integer countSubString(ConfigForInsert config, Integer transId);
    
    List<batchUploads> getuploadedBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception;
    
    List<batchUploads> getuploadedBatches(int userId, int orgId, Date fromDate, Date toDate, List <Integer> excludedStatusIds) throws Exception;
    
    void updateBatchStatus (Integer batchUploadId, Integer statusId, String timeField) throws Exception;
    
    void updateTransactionStatus (Integer batchUploadId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception;
    
    void updateTransactionTargetStatus (Integer batchUploadId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception;
    
    boolean allowBatchClear (Integer batchUploadId);
    
    Integer clearTransactionInRecords(Integer batchId, Integer transactionInId);
    
    Integer clearTransactionTranslatedIn(Integer batchUploadId, Integer transactionInId);
    
    Integer clearTransactionTarget(Integer batchUploadId);

    Integer clearTransactionIn(Integer batchUploadId);
     
    Integer insertFailedRequiredFields(configurationFormFields cff, Integer batchUploadId, Integer transactionInId);
    
    Integer clearTransactionInErrors(Integer batchUploadId, boolean leaveFinalStatusIds);
    
    Integer deleteTransactionInErrorsByTransactionId(Integer transactionInId);
    
    void updateStatusForErrorTrans(Integer batchId, Integer statusId, boolean foroutboundProcessing, Integer transactionId);
    
    void updateBlanksToNull(configurationFormFields cff, Integer batchUploadId);
    
    List<transactionRecords> getFieldColAndValues (Integer batchUploadId, configurationFormFields cff);
    
    void updateFieldValue (transactionRecords tr, String newValue);
    
    void insertValidationError(transactionRecords tr, configurationFormFields cff, Integer batchUploadId);
    
    List<Integer> getFeedbackReportConnection(int configId, int targetorgId);
    
    void nullForCWCol(Integer configId, Integer batchId, boolean foroutboundProcessing, Integer transactionId);
    
    void executeSingleValueCWData(Integer configId, Integer batchId, Integer fieldNo, CrosswalkData cwd, boolean foroutboundProcessing, Integer fieldId, Integer transactionId);
    
    void updateFieldNoWithCWData (Integer configId, Integer batchId, Integer fieldNo, Integer passClear, boolean foroutboundProcessing, Integer transactionId);

    void flagCWErrors (Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing, Integer transactionId);
   
    void flagMacroErrors (Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing, Integer transactionId);

    void resetTransactionTranslatedIn (Integer batchId, boolean resetAll, Integer transactionInId);
    
    Integer executeMacro (Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing, Macros macro, Integer transactionId);
    
    void insertProcessingError(Integer errorId, Integer configId, Integer batchId, Integer fieldNo, Integer macroId, Integer cwId, Integer validationTypeId, boolean required, boolean foroutboundProcessing, String errorCause);
    
    void insertProcessingError(Integer errorId, Integer configId, Integer batchId, Integer fieldNo, Integer macroId, Integer cwId, Integer validationTypeId, boolean required, boolean foroutboundProcessing, String errorCause, Integer transactionId);
    
    List<configurationTransport> getHandlingDetailsByBatch(int batchId) throws Exception; 
    
    void updateRecordCounts (Integer batchId, List <Integer> statusIds, boolean foroutboundProcessing, String colNameToUpdate);
    
    Integer getRecordCounts (Integer batchId, List <Integer> statusIds, boolean foroutboundProcessing);
    
    Integer getRecordCounts (Integer batchId, List <Integer> statusIds, boolean foroutboundProcessing, boolean inStatusIds);
    
    Integer copyTransactionInStatusToTarget(Integer batchId, Integer transactionId);
    
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
    
    Integer clearTransactionOutErrorsByUploadBatchId(Integer batchId);
    
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
    
    Integer insertTransactionTranslated(Integer oldInId ,Integer newInId, batchUploadSummary bus);
    
    List <batchUploads> getAllUploadedBatches(Date fromDate, Date toDate) throws Exception;
    
    List <batchUploads> getAllUploadedBatches(Date fromDate, Date toDate, Integer fetchSize) throws Exception;
    
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
    
    List<UserActivity> getBatchUserActivities(batchUploads batchInfo, boolean foroutboundProcessing);
    
    List <transactionRecords> getFieldColAndValueByTransactionId (configurationFormFields cff, Integer transactionId);
   
    Integer insertSFTPRun(MoveFilesLog sftpJob);
    
    void updateSFTPRun(MoveFilesLog sftpJob) throws Exception;
    
    List<batchUploads> getsentBatchesHistory(int userId, int orgId, int toOrgId, int messageTypeId, Date fromDate, Date toDate) throws Exception;
    
    List <configurationFTPFields> getFTPInfoForJob (Integer method);
    
    List<batchUploadSummary> getBatchesToSentOrg(int srcorgId, int tgtOrgId, int messageTypeId) throws Exception;
    
    messagePatients getPatientTransactionDetails(int transactionInId);
    
    messagePatients getPatientTransactionDetailsForExport(int transactionInId);
    
    List <configurationRhapsodyFields> getRhapsodyInfoForJob (Integer method);
    
    Integer insertTransactionInError(Integer newTInId, Integer oldTInId);
    
    List <Integer> checkCWFieldForList(Integer configId, Integer batchId,
            configurationDataTranslations cdt, boolean foroutboundProcessing, Integer transactionId);
    
    List <IdAndFieldValue> getIdAndValuesForConfigField(Integer configId, Integer batchId,
            configurationDataTranslations cdt, boolean foroutboundProcessing, Integer transactionId);
    
    Integer updateFieldValue (String fieldValue, Integer fieldNo, Integer transactionId,boolean foroutboundProcessing);
   
    void trimFieldValues (Integer batchId, boolean foroutboundProcessing, Integer transactionId, boolean trimAll);
    
    void updateTransactionTargetListStatus(List<transactionTarget> transactions, Integer statusId);
    
    void submitTransactionMultipleTargets(batchMultipleTargets target);
    
    List<batchMultipleTargets> getBatchMultipleTargets(Integer batchId);
    
    Integer copyBatchDetails(Integer batchId, Integer tgtConfigId, Integer transactionId);
    
    List<batchUploadSummary> getuploadBatchesByConfigAndTarget(Integer configId, Integer orgId, Integer tgtConfigId, Integer userOrgId);
    
    boolean searchBatchForHistory(batchUploads batchDetails, String searchTerm, Date fromDate, Date toDate) throws Exception;
    
    Integer updateTranTargetStatusByUploadBatchId(Integer batchUploadId, Integer fromStatusId, Integer toStatusId);
    
    Integer updateBatchDLStatusByUploadBatchId(Integer batchUploadId, Integer fromStatusId, Integer toStatusId, String timeField);
    
    List<Integer> getBatchDownloadIdsFromUploadId(Integer batchUploadId);
    
    Integer clearBatchDownloads(List <Integer> batchDownloadIDs);
    
    String getTransactionInIdsFromBatch(Integer batchUploadId);
    
    List<WSMessagesIn> getWSMessagesByStatusId(List<Integer> statusIds);
    
    WSMessagesIn getWSMessagesById(Integer wsMessageId);
    
    Integer updateWSMessage(WSMessagesIn wsMessage);
    
    List <Integer> getErrorCodes (List <Integer> codesToIgnore);
    
    Integer rejectInvalidSourceSubOrg(batchUploads batch, configurationConnection confConn, boolean nofinalStatus);
    
    Integer updateSSOrgIdTransactionIn(batchUploads batchUpload, configurationConnection batchTargets);
    
    Integer updateSSOrgIdUploadSummary(batchUploads batchUpload);

    Integer updateSSOrgIdTransactionTarget(batchUploads batchUpload);
    
    List<Integer> geBatchesIdsForReport(String fromDate, String toDate, boolean erg) throws Exception;
    
    BigInteger getReferralCount(String batchIds) throws Exception;
    
    BigInteger getFeedbackReportCount(String batchIds) throws Exception;
    
    List<activityReportList> getFeedbackReportList(String batchIds) throws Exception;
    
    List<activityReportList> getReferralList(String batchIds) throws Exception;
    
    List<Integer> getFeedbackTransactions(String ids, Integer configId) throws Exception;
    
    Integer getStatusFieldNo(Integer configId) throws Exception;
    
    BigInteger getTotalOpenFeedbackReports(String transIds, String fieldNo) throws Exception;
    
    BigInteger getTotalClosedFeedbackReports(String transIds, String fieldNo) throws Exception;
    
    Integer getActivityStatusFieldNo(Integer configId) throws Exception;
    
    Integer getReferralIdFieldNo(Integer configId) throws Exception;
    
    BigInteger getTotalCompletedActivityStatus(String transIds, String fieldNo) throws Exception;
    
    BigInteger getTotalEnrolledActivityStatus(String transIds, String fieldNo) throws Exception;
    
    BigInteger getRejectedCount(String fromDate, String toDate) throws Exception;
    
    List<referralActivityExports> getReferralActivityExports() throws Exception;
    
    void saveReferralActivityExport(referralActivityExports activityExport) throws Exception;
    
    String getTransactionFieldValue(Integer transactionInId, String fieldNo) throws Exception;
    
    String getActivityStatusValueById(Integer activityStatusId) throws Exception;
    
    String getReportActivityStatusValueById(Integer activityStatusId) throws Exception;
    
    List<batchUploads> getAllRejectedBatches(Date fromDate, Date toDate, Integer fetchSize) throws Exception;
    
    void clearMultipleTargets(Integer batchId) throws Exception;
    
    List getConfigFieldNumbers(Integer configId) throws Exception;
    
    List <Transaction> setTransactionInInfoByStatusId (Integer batchId, List<Integer> statusIds, Integer howMany) throws Exception;
    
    Transaction setTransactionTargetInfoByStatusId (Transaction transaction) throws Exception;
    
    List<batchUploads> getMassTranslateBatchForOutput (Integer howMany) throws Exception;
    
    void saveClearAfterDelivery (batchClearAfterDelivery cad) throws Exception;
    
    void updateClearAfterDelivery (batchClearAfterDelivery cad) throws Exception;
    
    List <batchClearAfterDelivery> getClearAfterDeliveryBatches (List<Integer> statusIds) throws Exception;
    
    batchClearAfterDelivery getClearAfterDeliveryById (Integer cadId) throws Exception;
}
