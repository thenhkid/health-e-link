/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service;

import com.ut.dph.model.Transaction;
import com.ut.dph.model.batchDownloadSummary;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.pendingDeliveryTargets;
import com.ut.dph.model.systemSummary;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionOutNotes;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.model.transactionOutRecords;
import java.util.Date;
import java.util.List;

/**
 *
 * @author chadmccue
 */
public interface transactionOutManager {
    
    List<batchDownloads> getInboxBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception;
    
    batchDownloads getBatchDetails(int batchId) throws Exception;
    
    batchDownloads getBatchDetailsByBatchName(String batchName) throws Exception;
    
    List<transactionTarget> getInboxBatchTransactions(int batchId, int userId) throws Exception;
    
    transactionTarget getTransactionDetails(int transactionId) throws Exception;
    
    transactionOutRecords getTransactionRecords(int transactionTargetId) throws Exception;
    
    transactionOutRecords getTransactionRecord(int recordId);

    void changeDeliveryStatus(int batchDLId, int batchUploadId, int transactionTargetId, int transactionInId);
    
    List getInternalStatusCodes();
    
    void updateTransactionDetails(transactionTarget transactionDetails) throws Exception;
    
    void saveNote(transactionOutNotes note) throws Exception;
    
    List<transactionOutNotes> getNotesByTransactionId(int transactionId) throws Exception;
    
    void removeNoteById(int noteId) throws Exception;
    
    Integer getActiveFeedbackReportsByMessageType(int messageTypeId, int orgId) throws Exception;
    
    List<transactionIn> getFeedbackReports(int transactionId, String fromPage) throws Exception;
    
    transactionTarget getTransactionsByInId(int transactionInId) throws Exception;
    
    List<transactionTarget> getpendingOutPutTransactions(int transactionTargetId) throws Exception;
    
    boolean processOutPutTransactions(int transactionTargetId, int configId, int transactionInId) throws Exception;
    
    void updateTargetBatchStatus(Integer batchDLId, Integer statusId, String timeField) throws Exception;
    
    void updateTargetTransasctionStatus(int batchDLId, int statusId);
    
    Integer translateTargetRecords(int transactionTargetId, int configId, int batchId, int categoryId);
    
    void moveTranslatedRecords(int transactionTargetId) throws Exception;
    
    void processOutputRecords(int transactionTargetId) throws Exception;
    
    void generateOutputFiles() throws Exception;
    
    List<batchDownloads> getdownloadableBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception;
    
    void updateLastDownloaded(int batchId) throws Exception;
    
    List<transactionTarget> getTransactionsByBatchDLId(int batchDLId);
    
    void cancelMessageTransaction(int transactionId, int transactionInId);
    
    void clearTransactionTranslatedOut(Integer transactionTargetId);
    
    void clearTransactionOutRecords(Integer transactionTargetId);
    
    void clearTransactionOutErrors(Integer transactionTargetId);
  
    Integer clearOutTables (Integer transactionTargetId);
    
    batchDownloadSummary getDownloadSummaryDetails(int transactionTargetId);
    
    systemSummary generateSystemOutboundSummary();
    
    List <batchDownloads> getAllBatches(Date fromDate, Date toDate) throws Exception;
    
    boolean searchTransactions(Transaction transaction, String searchTerm) throws Exception;
    
    systemSummary generateSystemWaitingSummary();
    
    List getTransactionsToProcess() throws Exception;
    
    int processManualTransaction(transactionTarget transaction) throws Exception;
    
    List getTransactionsToProcessByMessageType(int orgId) throws Exception;
    
    List getAllransactionsToProcessByMessageType(int orgId, int messageTypeId) throws Exception;
    
    List<transactionTarget> getPendingDeliveryTransactions(int orgId, int messageType, Date fromDate, Date toDate) throws Exception;
    
    boolean searchTransactionsByMessageType(pendingDeliveryTargets transaction, String searchTerm) throws Exception;
    
    boolean searchPendingTransactions(Transaction tran, String searchTerm) throws Exception;
    
    void doNotProcessTransaction(int transactionId) throws Exception;
    
    List<batchDownloads> getInboxBatchesHistory(int userId, int orgId, int fromOrgId, int messageTypeId, Date fromDate, Date toDate) throws Exception;
    
    boolean searchBatchForHistory(batchDownloads batchDetails, String searchTerm, Date fromDate, Date toDate) throws Exception;
    
    List<batchDownloadSummary> getBatchesBySentOrg(int srcorgId, int tgtOrgId, int messageTypeId) throws Exception;
    
    void selectOutputRecordsForProcess(Integer transactionTargetId) throws Exception;
    
    List<batchDownloadSummary> getuploadBatchesByConfigAndSource(Integer configId, Integer orgId);
}
