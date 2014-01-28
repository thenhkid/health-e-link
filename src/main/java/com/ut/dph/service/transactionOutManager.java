/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service;

import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.model.transactionOutRecords;

import java.util.List;

/**
 *
 * @author chadmccue
 */
public interface transactionOutManager {
    
    List<batchDownloads> getInboxBatches(int userId, int orgId, int page, int maxResults);
    
    List<batchDownloads> findInboxBatches(List<batchDownloads> batches, String searchTerm);
    
    batchDownloads getBatchDetails(int batchId);
    
    List<transactionTarget> getInboxBatchTransactions(int batchId, int userId);
    
    transactionTarget getTransactionDetails(int transactionId);
    
    transactionOutRecords getTransactionRecords(int transactionTargetId);
    
    transactionOutRecords getTransactionRecord(int recordId);
    
    void changeDeliveryStatus(int batchDLId, int batchUploadId, int transactionTargetId, int transactionInId);
    
}
