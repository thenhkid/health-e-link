/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.dao;

import com.ut.healthelink.model.batchDownloadSummary;
import com.ut.healthelink.model.batchDownloads;
import com.ut.healthelink.model.batchClearAfterDelivery;
import com.ut.healthelink.model.batchUploads;
import com.ut.healthelink.model.configuration;
import com.ut.healthelink.model.configurationFormFields;
import com.ut.healthelink.model.configurationSchedules;
import com.ut.healthelink.model.configurationTransport;
import com.ut.healthelink.model.targetOutputRunLogs;
import com.ut.healthelink.model.transactionIn;
import com.ut.healthelink.model.transactionOutNotes;
import com.ut.healthelink.model.transactionOutRecords;
import com.ut.healthelink.model.transactionRecords;
import com.ut.healthelink.model.transactionTarget;
import com.ut.healthelink.model.custom.ConfigOutboundForInsert;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 *
 * @author chadmccue
 */
@Repository
public interface transactionOutDAO {
    
    List<batchDownloads> getInboxBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception;
    
    batchDownloads getBatchDetails(int batchId) throws Exception;
    
    batchDownloads getBatchDetailsByBatchName(String batchName) throws Exception;
    
    List<transactionTarget> getInboxBatchTransactions(int batchId, int userId) throws Exception;
    
    transactionTarget getTransactionDetails(int transactionId) throws Exception;
    
    transactionOutRecords getTransactionRecords(int transactionTargetId) throws Exception;
    
    transactionOutRecords getTransactionRecord(int recordId);

    void changeDeliveryStatus(int batchDLId, int batchUploadId, int transactionTargetId, int transactionInId);
    
    @SuppressWarnings("rawtypes")
	List getInternalStatusCodes();
    
    void updateTransactionDetails(transactionTarget transactionDetails) throws Exception;
    
    void saveNote(transactionOutNotes note) throws Exception;
    
    List<transactionOutNotes> getNotesByTransactionId(int transactionId) throws Exception;
    
    void removeNoteById(int noteId) throws Exception;
    
    Integer getActiveFeedbackReportsByMessageType(int messageTypeId, int orgId) throws Exception;
    
    List<transactionIn> getFeedbackReports(int transactionId, String fromPage) throws Exception;
    
    transactionTarget getTransactionsByInId(int transactionInId) throws Exception;
    
    List<transactionTarget> getpendingOutPutTransactions(int transactionTargetId) throws Exception;
    
    boolean processOutPutTransactions(int transactionTargetId, int configId, int transactionInId);
    
    void updateTargetBatchStatus(Integer batchDLId, Integer statusId, String timeField) throws Exception;
    
    void updateTargetTransasctionStatus(int batchDLId, int statusId);

    void moveTranslatedRecords(int transactionTargetId) throws Exception;
    
    List<transactionTarget> getLoadedOutBoundTransactions(int configId);
    
    Integer submitBatchDownload(batchDownloads batchDownload);
    
    int findMergeableBatch(int orgId);
    
    void submitSummaryEntry(batchDownloadSummary summary) throws Exception;
    
    void updateTransactionTargetBatchDLId(Integer batchId, Integer transactionTargetId);
    
    void updateBatchOutputFileName(int batchId, String fileName);
    
    int getMaxFieldNo(int configId) throws Exception;
    
    List<batchDownloads> getdownloadableBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception;
    
    void updateLastDownloaded(int batchId) throws Exception;
    
    List<configurationSchedules> getScheduledConfigurations();
    
    void updateBatchStatus (Integer batchId, Integer statusId);
    
    void saveOutputRunLog(targetOutputRunLogs log) throws Exception;
    
    List<targetOutputRunLogs> getLatestRunLog(int configId) throws Exception;
    
    List<transactionTarget> getTransactionsByBatchDLId(int batchDLId);
    
    void cancelMessageTransaction(int transactionId, int transactionInId);
    
    void clearTransactionTranslatedOut(Integer transactionTargetId);
    
    void clearTransactionOutRecords(Integer transactionTargetId);
    
    void clearTransactionOutErrors(Integer transactionTargetId);
    
    batchDownloadSummary getDownloadSummaryDetails(int transactionTargetId);
    
    List <batchDownloads> getAllBatches(Date fromDate, Date toDate) throws Exception;
    
    List getTransactionsToProcess() throws Exception;
    
    List getTransactionsToProcessByMessageType(int orgId) throws Exception;
    
    List getAllransactionsToProcessByMessageType(int orgId, int messageTypeId) throws Exception;
    
    List<transactionTarget> getPendingDeliveryTransactions(int orgId, int messageType, Date fromDate, Date toDate) throws Exception;
    
    void doNotProcessTransaction(int transactionId) throws Exception;
    
    List<batchDownloads> getInboxBatchesHistory(int userId, int orgId, int fromOrgId, int messageTypeId, Date fromDate, Date toDate) throws Exception;
    
    boolean searchBatchForHistory(batchDownloads batchDetails, String searchTerm, Date fromDate, Date toDate) throws Exception;
    
    List<batchDownloadSummary> getBatchesBySentOrg(int srcorgId, int tgtOrgId, int messageTypeId) throws Exception;
    
    List<batchDownloadSummary> getuploadBatchesByConfigAndSource(Integer configId, Integer orgId, Integer userOrgId);
    
    void updateTransactionTargetStatusOutBound(Integer batchDLId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception;
    
    List <String> getWSSenderFromBatchDLId(List<Integer> batchDLIds) throws Exception;
    
    List<transactionTarget> getSentTransactions(Date fromDate, Date toDate) throws Exception;
    
    List<transactionTarget> getTTByStatusId(int batchId, List <Integer> statusIds) throws Exception;
    
    List<Integer> getTargetConfigsForBatch (int batchId, List <Integer> statusIds) throws Exception;
    
    Integer writeOutputToTextFile(configurationTransport transportDetails, Integer batchDLId, String filePathAndName, String configFields);
   
    void updateTTBatchIdByUploadBatch(Integer batchUploadId, Integer batchDownloadId) throws Exception;
    
    void  massInsertSummaryEntry(batchDownloads batchDowloadDetails, configuration configDetails, batchUploads batchUploadDetails) throws Exception;
    
    void setUpTransactionTranslatedOut(batchDownloads batchDowloadDetails, configuration configDetails, Integer statusId);
    
    List <ConfigOutboundForInsert> setConfigOutboundForInsert(int configId, int batchDownloadId) throws Exception;
    
    void massUpdateTTO (ConfigOutboundForInsert configOutboundForInsertList, Integer statusId ) throws Exception;
    
    String getConfigFieldsForOutput (Integer configId) throws Exception;
    
    void insertFailedRequiredFields(configurationFormFields cff, Integer batchDownloadId, Integer transactionTargetId) throws Exception;
    
    void genericValidation(configurationFormFields cff, Integer validationTypeId, Integer batchDownloadId, String regEx, Integer transactionTargetId) throws Exception;
    
    void moveTranslatedRecordsByBatch(int batchDownloadId) throws Exception;
    
    List<transactionRecords> getFieldColAndValues(Integer batchDownloadId, configurationFormFields cff) throws Exception;
    
    List<transactionRecords> getFieldColAndValueByTransactionId(configurationFormFields cff, Integer transactionId) throws Exception;

    void updateFieldValue (transactionRecords tr, String newValue) throws Exception;
    
    void insertValidationError(transactionRecords tr, configurationFormFields cff, Integer batchDownloadId) throws Exception;
    
    void updateErrorStatusForTT (Integer batchDownloadId, Integer newStatusId, Integer transactionTargetId) throws Exception;
   
    void clearTransactionTranslatedOutByBatchId(Integer batchDownloadId) throws Exception;
    
    void insertDroppedValues (configurationFormFields cff, List <String> reportHeaderCols, Integer batchUploadId, Integer entityIdFCol) throws Exception;


    
}
