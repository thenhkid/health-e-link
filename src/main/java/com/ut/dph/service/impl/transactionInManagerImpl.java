/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service.impl;

import com.ut.dph.dao.transactionInDAO;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import org.springframework.stereotype.Service;
import com.ut.dph.service.transactionInManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author chadmccue
 */
@Service
public class transactionInManagerImpl implements transactionInManager {
    
    @Autowired
    private transactionInDAO transactionInDAO;
    
    @Override
    @Transactional
    public String getFieldValue(String tableName, String tableCol, int idValue) {
        return transactionInDAO.getFieldValue(tableName, tableCol, idValue);
    }
    
    @Override
    @Transactional
    public List<fieldSelectOptions> getFieldSelectOptions(int fieldId, int configId) {
        return transactionInDAO.getFieldSelectOptions(fieldId, configId);
    }
    
    @Override
    @Transactional
    public Integer submitBatchUpload(batchUploads batchUpload) {
        return transactionInDAO.submitBatchUpload(batchUpload);
    }
    
    @Override
    @Transactional
    public Integer submitTransactionIn(transactionIn transactionIn) {
        return transactionInDAO.submitTransactionIn(transactionIn);
    }
    
    @Override
    @Transactional
    public Integer submitTransactionInRecords(transactionInRecords records) {
        return transactionInDAO.submitTransactionInRecords(records);
    }
    
    @Override
    @Transactional
    public void submitTransactionTranslatedInRecords(int transactionRecordId) {
        transactionInDAO.submitTransactionTranslatedInRecords(transactionRecordId);
    }
    
    @Override
    @Transactional
    public List<transactionIn> getpendingTransactions(int orgId) {
        return transactionInDAO.getpendingTransactions(orgId);
    }
    
    @Override
    @Transactional
    public batchUploads getUploadBatch(int batchId) {
        return transactionInDAO.getUploadBatch(batchId);
    }
    
}
