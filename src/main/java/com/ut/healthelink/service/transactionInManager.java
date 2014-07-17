/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.service;

import com.ut.healthelink.model.MoveFilesLog;
import com.ut.healthelink.model.UserActivity;
import com.ut.healthelink.model.CrosswalkData;
import com.ut.healthelink.model.Macros;
import com.ut.healthelink.model.Transaction;
import com.ut.healthelink.model.TransactionInError;
import com.ut.healthelink.model.User;
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
import com.ut.healthelink.model.custom.TransErrorDetailDisplay;
import com.ut.healthelink.model.messagePatients;
import com.ut.healthelink.model.systemSummary;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author chadmccue
 */
public interface transactionInManager {

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

    String uploadAttachment(MultipartFile fileUpload, String orgName) throws Exception;

    Integer submitAttachment(transactionAttachment attachment) throws Exception;

    transactionAttachment getAttachmentById(int attachmentId) throws Exception;

    void submitAttachmentChanges(transactionAttachment attachment) throws Exception;

    List<transactionAttachment> getAttachmentsByTransactionId(int transactionInId) throws Exception;

    void removeAttachmentById(int attachmentId) throws Exception;

    boolean insertSingleToMessageTables(ConfigForInsert configForInsert);

    boolean insertMultiValToMessageTables(ConfigForInsert config, Integer subStringCounter, Integer transId);

    List<ConfigForInsert> setConfigForInsert(int configId, int batchUploadId);

    List<Integer> getConfigIdsForBatch(int batchUploadId, boolean getAll);

    List<Integer> getConfigIdsForBatch(int batchUploadId, boolean getAll, Integer transactionInId);

    List<Integer> getTransWithMultiValues(ConfigForInsert config);

    Integer clearMessageTables(int batchId);

    List<Integer> getBlankTransIds(ConfigForInsert config);

    Integer countSubString(ConfigForInsert config, Integer transId);

    List<batchUploads> getuploadedBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception;

    List<batchUploads> getuploadedBatches(int userId, int orgId, Date fromDate, Date toDate, List<Integer> excludedStatusIds) throws Exception;

    boolean processBatch(int batchUploadId, boolean doNotClearErrors, Integer transactionId) throws Exception;

    void processBatches();

    void updateBatchStatus(Integer batchUploadId, Integer statusId, String timeField) throws Exception;

    void updateTransactionStatus(Integer batchUploadId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception;

    void updateTransactionTargetStatus(Integer batchUploadId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception;

    boolean clearBatch(Integer batchUploadId) throws Exception;

    boolean setDoNotProcess(Integer batchUploadId);

    Integer clearTransactionInRecords(Integer batchUploadId);

    boolean insertTransactions(Integer batchUploadId);

    Integer clearTransactionIn(Integer batchUploadId);

    Integer clearTransactionTranslatedIn(Integer batchUploadId);

    Integer clearTransactionTables(Integer batchUploadId, boolean leaveFinalStatusIds);

    Integer clearTransactionTarget(Integer batchUploadId);

    void flagAndEmailAdmin(Integer batchUploadId);

    List<configurationFormFields> getRequiredFieldsForConfig(Integer configId);

    Integer insertFailedRequiredFields(configurationFormFields cff, Integer batchUploadId, Integer transactionInId);

    Integer clearTransactionInErrors(Integer batchUploadId, boolean leaveFinalStatus);

    Integer deleteTransactionInErrorsByTransactionId(Integer transactionInId);

    void updateStatusForErrorTrans(Integer batchId, Integer statusId, boolean foroutboundProcessing, Integer transactionId);

    Integer runValidations(Integer batchUploadId, Integer configId, Integer transactionId);

    Integer genericValidation(configurationFormFields cff, Integer validationTypeId, Integer batchUploadId, String regEx, Integer transactionId);

    Integer urlValidation(configurationFormFields cff, Integer validationTypeId, Integer batchUploadId, Integer transactionId);

    Integer dateValidation(configurationFormFields cff, Integer validationTypeId, Integer batchUploadId, Integer transactionId);

    void updateBlanksToNull(configurationFormFields cff, Integer batchUploadId);

    List<transactionRecords> getFieldColAndValues(Integer batchUploadId, configurationFormFields cff);

    Date convertLongDate(String dateValue);

    void updateFieldValue(transactionRecords tr, String newValue);

    void insertValidationError(transactionRecords tr, configurationFormFields cff, Integer batchUploadId);

    List<Integer> getFeedbackReportConnection(int configId, int targetorgId);

    String formatDateForDB(Date date);

    Date convertDate(String date);

    String chkMySQLDate(String date);

    boolean isValidURL(String url);

    Integer processCrosswalk(Integer configId, Integer batchId, configurationDataTranslations translation, boolean foroutboundProcessing, Integer transactionId);

    Integer processMacro(Integer configId, Integer batchId, configurationDataTranslations translation, boolean foroutboundProcessing, Integer transactionId);

    void nullForCWCol(Integer configId, Integer batchId, boolean foroutboundProcessing, Integer transactionId);

    void executeSingleValueCWData(Integer configId, Integer batchId, Integer fieldNo, CrosswalkData cwd, boolean foroutboundProcessing, Integer fieldId, Integer transactionId);

    void updateFieldNoWithCWData(Integer configId, Integer batchId, Integer fieldNo, Integer passClear, boolean foroutboundProcessing, Integer transactionId);

    Integer executeMacro(Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing, Macros macro, Integer transactionId);

    void flagCWErrors(Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing, Integer transactionId);

    void flagMacroErrors(Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing, Integer transactionId);

    List<configurationTransport> getHandlingDetailsByBatch(int batchId);

    void insertProcessingError(Integer errorId, Integer configId, Integer batchId, Integer fieldNo, Integer macroId, Integer cwId, Integer validationTypeId, boolean required, boolean foroutboundProcessing, String errorCause);

    void insertProcessingError(Integer errorId, Integer configId, Integer batchId, Integer fieldNo, Integer macroId, Integer cwId, Integer validationTypeId, boolean required, boolean foroutboundProcessing, String errorCause, Integer transactionId);

    void updateRecordCounts(Integer batchId, List<Integer> statusIds, boolean foroutboundProcessing, String colNameToUpdate);

    Integer getRecordCounts(Integer batchId, List<Integer> statusIds, boolean foroutboundProcessing);

    Integer getRecordCounts(Integer batchId, List<Integer> statusIds, boolean foroutboundProcessing, boolean inStatusIds);

    void resetTransactionTranslatedIn(Integer batchId, boolean resetAll);

    void resetTransactionTranslatedIn(Integer batchId, boolean resetAll, Integer transactionInId);

    Integer copyTransactionInStatusToTarget(Integer batchId, Integer transactionId);

    Integer insertLoadData(Integer batchId, String delimChar, String fileWithPath, String loadTableName, boolean containsHeaderRow);

    Integer createLoadTable(String loadTableName);

    Integer dropLoadTable(String loadTableName);

    Integer updateLoadTable(String loadTableName, Integer batchId);

    Integer loadTransactionIn(String loadTableName, Integer batchId);

    Integer loadTransactionInRecords(Integer batchId);

    Integer loadTransactionInRecordsData(String loadTableName);

    Integer updateConfigIdForBatch(Integer batchId, Integer configId);

    Integer loadTransactionTranslatedIn(Integer batchId);

    Integer insertBatchUploadSummary(batchUploads batchUpload, configurationConnection batchTargets);

    Integer insertBatchTargets(Integer batchId);

    List<configurationConnection> getBatchTargets(Integer batchId, boolean active);

    Integer clearBatchUploadSummary(Integer batchId);

    void loadBatches();

    boolean loadBatch(Integer batchId);

    List<batchUploads> getBatchesByStatusIds(List<Integer> statusIds);

    void setBatchToError(Integer batchId, String errorMessage) throws Exception;

    void deleteMessage(int batchId, int transactionId) throws Exception;

    void cancelMessageTransaction(int transactionId) throws Exception;

    List<transactionInRecords> getTransactionInRecordsForBatch(Integer batchId);

    Integer updateConfigIdForCMS(Integer batchId, configurationMessageSpecs cms);

    Integer flagInvalidConfig(Integer batchId);

    Integer insertInvalidConfigError(Integer batchId);

    Integer updateInvalidConfigStatus(Integer batchId);

    Integer indexLoadTable(String loadTableName);

    batchUploadSummary getUploadSummaryDetails(int transactionInId);

    Integer clearBatchDownloadSummaryByUploadBatchId(Integer batchId);

    Integer clearTransactionTranslatedOutByUploadBatchId(Integer batchId);

    Integer clearTransactionOutRecordsByUploadBatchId(Integer batchId);

    Integer rejectInvalidTargetOrg(Integer batchId, configurationConnection confConn);

    Integer insertBatchUploadSumByOrg(batchUploads batchUpload, configurationConnection confConn);

    Integer setStatusForErrorCode(Integer batchId, Integer statusId, Integer errorId, boolean foroutboundProcessing);

    Integer rejectNoConnections(batchUploads batch);

    Integer newEntryForMultiTargets(Integer batchId);

    List<Integer> getDuplicatedIds(Integer batchId);

    List<batchUploadSummary> getBatchUploadSummary(Integer transactionInId);

    Integer insertTransactionInByTargetId(batchUploadSummary bus);

    Integer getTransactionInIdByTargetId(batchUploadSummary bus);

    Integer updateTInIdForTransactiontarget(batchUploadSummary bus, Integer newTInId);

    Integer updateTINIDForBatchUploadSummary(batchUploadSummary bus, Integer newTInId);

    Integer copyTransactionInRecord(Integer newTInId, Integer oldTInId);

    Integer insertTransactionTranslated(Integer oldInId, Integer newInId, batchUploadSummary bus);

    List<batchUploads> getAllUploadedBatches(Date fromDate, Date toDate) throws Exception;

    List<batchUploads> getAllUploadedBatches(Date fromDate, Date toDate, Integer fetchSize) throws Exception;

    boolean searchTransactions(Transaction transaction, String searchTerm) throws Exception;

    systemSummary generateSystemInboundSummary();

    boolean checkPermissionForBatch(User userInfo, batchUploads batchInfo);

    List<TransactionInError> getErrorList(Integer batchId);

    List<TransErrorDetail> getTransErrorDetailsForNoRptFields(Integer batchId, List<Integer> errorCodes);

    Integer getCountForErrorId(Integer batchId, Integer errorId);

    List<TransErrorDetail> getTransErrorDetailsForInvConfig(Integer batchId);

    List<ConfigErrorInfo> getErrorConfigForBatch(Integer batchId);

    ConfigErrorInfo getHeaderForConfigErrorInfo(Integer batchId, ConfigErrorInfo configErrorInfo);

    List<TransErrorDetail> getTransErrorDetails(batchUploads batchInfo, ConfigErrorInfo configErrorInfo, String sqlStmt);

    TransErrorDetail getTransErrorData(TransErrorDetail ted, String sqlStmt);

    Integer flagNoPermissionConfig(batchUploads batch);

    boolean hasPermissionForBatch(batchUploads batchInfo, User userInfo, boolean hasConfigurations);

    batchUploads getBatchDetailsByTInId(Integer transactionInId);

    boolean allowBatchClear(Integer batchUploadId);

    void updateTranStatusByTInId(Integer transactionInId, Integer statusId) throws Exception;

    List<batchUploads> populateBatchInfo(List<batchUploads> uploadedBatches, User userInfo);

    List<TransErrorDetail> getTransactionErrorsByFieldNo(int transactionInId, int fieldNo) throws Exception;

    List<TransErrorDetailDisplay> populateErrorList(batchUploads batchInfo);

    List<UserActivity> getBatchActivities(batchUploads batchInfo, boolean forUsers, boolean foroutboundProcessing);

    List<transactionRecords> getFieldColAndValueByTransactionId(configurationFormFields cff, Integer transactionId);

    Integer insertSFTPRun(MoveFilesLog sftpJob);

    void updateSFTPRun(MoveFilesLog sftpJob) throws Exception;

    List<batchUploads> getsentBatchesHistory(int userId, int orgId, int toOrgId, int messageTypeId, Date fromDate, Date toDate) throws Exception;

    Integer moveSFTPFiles();

    Integer moveFilesByPath(String path, Integer transportMethodId, Integer orgId, Integer transportId);

    List<configurationFTPFields> getFTPInfoForJob(Integer method);

    String newFileName(String path, String fileName);

    List<batchUploadSummary> getBatchesToSentOrg(int srcorgId, int tgtOrgId, int messageTypeId) throws Exception;

    messagePatients getPatientTransactionDetails(int transactionInId);

    String copyUplaodedPath(configurationTransport transportDetails, MultipartFile fileUpload);

    Map<String, String> chkUploadBatchFile(configurationTransport transportDetails, File uploadedFile) throws Exception;

    Integer moveRhapsodyFiles();

    List<configurationRhapsodyFields> getRhapsodyInfoForJob(Integer method);

    Integer insertTransactionInError(Integer newTInId, Integer oldTInId);

    List<Integer> checkCWFieldForList(Integer configId, Integer batchId,
            configurationDataTranslations cdt, boolean foroutboundProcessing, Integer transactionId);

    Integer processMultiValueCWData(Integer configId, Integer batchId, configurationDataTranslations cdt, List<CrosswalkData> cdList, boolean foroutboundProcessing, Integer transactionId);

    List<IdAndFieldValue> getIdAndValuesForConfigField(Integer configId, Integer batchId,
            configurationDataTranslations cdt, boolean foroutboundProcessing, Integer transactionId);

    Integer updateFieldValue(String fieldValue, Integer fieldNo, Integer transactionId, boolean foroutboundProcessing);

    void trimFieldValues(Integer batchId, boolean foroutboundProcessing, Integer transactionId, boolean trimAll);
    
    void updateTransactionTargetListStatus(List<transactionTarget> transactions, Integer statusId);
    
    void submitTransactionMultipleTargets(batchMultipleTargets target);
    
    List<batchMultipleTargets> getBatchMultipleTargets(Integer batchId);
    
    Integer copyBatchDetails(Integer batchId, Integer tgtConfigId, Integer transactionId);
    
    void sendEmailToAdmin(String message, String subject) throws Exception;
    
    List<batchUploadSummary> getuploadBatchesByConfigAndTarget(Integer configId, Integer orgId, Integer tgtConfigId);
    
    boolean searchBatchForHistory(batchUploads batchDetails, String searchTerm, Date fromDate, Date toDate) throws Exception;
    
    Integer updateTranTargetStatusByUploadBatchId(Integer batchUploadId, Integer fromStatusId, Integer toStatusId);
    
    Integer updateBatchDLStatusByUploadBatchId(Integer batchUploadId, Integer fromStatusId, Integer toStatusId, String timeField);
    
    List<Integer> getBatchDownloadIdsFromUploadId(Integer batchUploadId);
    
    Integer clearBatchDownloads(List <Integer> batchDownloadIDs);
    
    boolean recheckLongDate(String longDateVal, String convertedDate);
    
    List<Integer> getTransactionInIdsFromBatch(Integer batchUploadId);
    
}
