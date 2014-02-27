/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.dao;

import com.ut.dph.model.batchDownloadSummary;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.configurationSchedules;
import com.ut.dph.model.targetOutputRunLogs;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionOutNotes;
import com.ut.dph.model.transactionOutRecords;
import com.ut.dph.model.transactionTarget;
import java.util.Date;
import java.util.List;

/**
 *
 * @author chadmccue
 */
public interface transactionOutDAO {
    
    List<batchDownloads> getInboxBatches(int userId, int orgId, String searchTerm, Date fromDate, Date toDate, int page, int maxResults) throws Exception;
    
    batchDownloads getBatchDetails(int batchId) throws Exception;
    
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

    void moveTranslatedRecords(int transactionTargetId) throws Exception;
    
    List<transactionTarget> getLoadedOutBoundTransactions(int configId);
    
    Integer submitBatchDownload(batchDownloads batchDownload);
    
    int findMergeableBatch(int orgId);
    
    void submitSummaryEntry(batchDownloadSummary summary) throws Exception;
    
    void updateTransactionTargetBatchDLId(Integer batchId, Integer transactionTargetId);
    
    void updateBatchOutputFileName(int batchId, String fileName);
    
    int getMaxFieldNo(int configId);
    
    List<batchDownloads> getdownloadableBatches(int userId, int orgId, Date fromDate, Date toDate, String searchTerm, int page, int maxResults) throws Exception;
    
    void updateLastDownloaded(int batchId) throws Exception;
    
    List<configurationSchedules> getScheduledConfigurations();
    
    void updateBatchStatus (Integer batchId, Integer statusId);
    
    void saveOutputRunLog(targetOutputRunLogs log) throws Exception;
    
    List<targetOutputRunLogs> getLatestRunLog(int configId) throws Exception;
    
    List<transactionTarget> getTransactionsByBatchDLId(int batchDLId);
    
    void cancelMessageTransaction(int transactionId, int transactionInId);
    
}
