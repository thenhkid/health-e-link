/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service.impl;

import com.ut.dph.dao.transactionOutDAO;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.transactionOutRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.service.transactionOutManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author chadmccue
 */
@Service
public class transactionOutManagerImpl implements transactionOutManager {
    
    @Autowired
    private transactionOutDAO transactionOutDAO;
    
    @Override
    @Transactional
    public List<batchDownloads> getInboxBatches(int userId, int orgId, int page, int maxResults) {
        return transactionOutDAO.getInboxBatches(userId, orgId, page, maxResults);
    }
    
    @Override
    @Transactional
    public List<batchDownloads> findInboxBatches(List<batchDownloads> batches, String searchTerm) {
        return transactionOutDAO.findInboxBatches(batches, searchTerm);
    }
    
    @Override
    @Transactional
    public batchDownloads getBatchDetails(int batchId) {
        return transactionOutDAO.getBatchDetails(batchId);
    }
    
    @Override
    @Transactional
    public List<transactionTarget> getInboxBatchTransactions(int batchId, int userId) {
        return transactionOutDAO.getInboxBatchTransactions(batchId, userId);
    }
    
    @Override
    @Transactional
    public transactionTarget getTransactionDetails(int transactionId) {
        return transactionOutDAO.getTransactionDetails(transactionId);
    }
    
    @Override
    @Transactional
    public transactionOutRecords getTransactionRecords(int transactionTargetId) {
        return transactionOutDAO.getTransactionRecords(transactionTargetId);
    }

    @Override
    @Transactional
    public transactionOutRecords getTransactionRecord(int recordId) {
        return transactionOutDAO.getTransactionRecord(recordId);
    }
    
<<<<<<< HEAD
    @Override
    @Transactional
    public void changeDeliveryStatus(int batchDLId, int batchUploadId, int transactionTargetId, int transactionInId) {
        transactionOutDAO.changeDeliveryStatus(batchDLId, batchUploadId, transactionTargetId, transactionInId);
    }

=======
>>>>>>> ac9942cb8ae159da81ca57e434e458ae58833c37
}
