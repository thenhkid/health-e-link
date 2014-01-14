/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service;

import com.ut.dph.model.batchUploads;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.transactionAttachment;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import com.ut.dph.model.transactionTarget;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author chadmccue
 */
public interface transactionInManager {
    
    String getFieldValue(String tableName, String tableCol, int idValue);
    
    List<fieldSelectOptions> getFieldSelectOptions(int fieldId, int configId);
    
    Integer submitBatchUpload(batchUploads batchUpload);
    
    void submitBatchUploadChanges(batchUploads batchUpload);
    
    Integer submitTransactionIn(transactionIn transactionIn);
    
    void submitTransactionInChanges(transactionIn transactionIn);
    
    Integer submitTransactionInRecords(transactionInRecords records);
    
    void submitTransactionInRecordsUpdates(transactionInRecords records);
    
    void submitTransactionTranslatedInRecords(int transactionId, int transactionRecordId, int configId);
    
    List<transactionIn> getpendingTransactions(int orgId);
    
    List<transactionIn> getsentTransactions(int orgId);
    
    batchUploads getUploadBatch(int batchId);
    
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
    
}
