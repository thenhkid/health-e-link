/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service.impl;

import com.ut.dph.dao.transactionOutDAO;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionOutNotes;
import com.ut.dph.model.transactionOutRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.transactionInManager;
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
    
    @Autowired
    private configurationManager configurationManager;
    
    @Autowired
    private transactionInManager transactionInManager;
    
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

    @Override
    @Transactional
    public void changeDeliveryStatus(int batchDLId, int batchUploadId, int transactionTargetId, int transactionInId) {
        transactionOutDAO.changeDeliveryStatus(batchDLId, batchUploadId, transactionTargetId, transactionInId);
    }
    
    @Override
    @Transactional
    public List getInternalStatusCodes() {
        return transactionOutDAO.getInternalStatusCodes();
    }
    
    @Override
    @Transactional
    public void updateTransactionDetails(transactionTarget transactionDetails) {
        transactionOutDAO.updateTransactionDetails(transactionDetails);
    }
    
    @Override
    @Transactional
    public void saveNote(transactionOutNotes note) {
        transactionOutDAO.saveNote(note);
    }
    
    @Override
    @Transactional
    public List<transactionOutNotes> getNotesByTransactionId(int transactionId) {
        return transactionOutDAO.getNotesByTransactionId(transactionId);
    }
    
    @Override
    @Transactional
    public void removeNoteById(int noteId) {
        transactionOutDAO.removeNoteById(noteId);
    }
    
    @Override
    @Transactional
    public Integer getActiveFeedbackReportsByMessageType(int messageTypeId, int orgId) {
        return transactionOutDAO.getActiveFeedbackReportsByMessageType(messageTypeId, orgId);
    }
    
    @Override
    @Transactional
    public List<transactionIn> getFeedbackReports(int transactionId, String fromPage) {
        return transactionOutDAO.getFeedbackReports(transactionId, fromPage);
    }
    
    @Override
    @Transactional
    public transactionTarget getTransactionsByInId(int transactionInId) {
        return transactionOutDAO.getTransactionsByInId(transactionInId);
    }
    
    @Override
    @Transactional
    public List<transactionTarget> getpendingOutPutTransactions(int transactionTargetId) {
        return transactionOutDAO.getpendingOutPutTransactions(transactionTargetId);
    }
    
    @Override
    @Transactional
    public boolean processOutPutTransactions(int transactionTargetId, int configId, int transactionInId) {
        return transactionOutDAO.processOutPutTransactions(transactionTargetId, configId, transactionInId);
    }
    
    @Override
    @Transactional
    public void updateTargetBatchStatus(Integer batchDLId, Integer statusId, String timeField) {
        transactionOutDAO.updateTargetBatchStatus(batchDLId, statusId, timeField);
    }

    /**
     * The 'translateTargetRecords' function will attempt to translate the target records based on the
     * translation details set up in the target configuration.
     * 
     * @param transactionTargetId   The id of the target transaction to be translated
     * @param batchId               The id of the batch the target transaction belongs to
     * @param configId              The id of the target configuration.
     * 
     * @return This function will return either TRUE (If translation completed with no errors)
     *         OR FALSE (If translation failed for any reason)
     */
    @Override
    @Transactional
    public boolean translateTargetRecords(int transactionTargetId, int configId, int batchId) {
        
        boolean translated = false;
        
        /* Need to get the configured data translations */
        List<configurationDataTranslations> dataTranslations = configurationManager.getDataTranslationsWithFieldNo(configId);
        
        for (configurationDataTranslations cdt : dataTranslations) {
            if (cdt.getCrosswalkId() != 0) {
                   translated = transactionInManager.processCrosswalk (configId, batchId, cdt, true);
            } 
            else if (cdt.getMacroId()!= 0)  {
                   translated = transactionInManager.processMacro (configId, batchId, cdt, true);
            }
        }
        
        return translated;
    }
    
}
