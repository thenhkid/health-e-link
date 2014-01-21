/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.dph.service.impl;

import com.ut.dph.dao.transactionInDAO;
import com.ut.dph.model.batchUploadSummary;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.transactionAttachment;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.model.custom.ConfigForInsert;
import com.ut.dph.reference.fileSystem;

import org.springframework.stereotype.Service;

import com.ut.dph.service.transactionInManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public void submitBatchUploadSummary(batchUploadSummary summary) {
        transactionInDAO.submitBatchUploadSummary(summary);
    }

    @Override
    @Transactional
    public void submitBatchUploadChanges(batchUploads batchUpload) {
        transactionInDAO.submitBatchUploadChanges(batchUpload);
    }

    @Override
    @Transactional
    public Integer submitTransactionIn(transactionIn transactionIn) {
        return transactionInDAO.submitTransactionIn(transactionIn);
    }

    @Override
    @Transactional
    public void submitTransactionInChanges(transactionIn transactionIn) {
        transactionInDAO.submitTransactionInChanges(transactionIn);
    }

    @Override
    @Transactional
    public Integer submitTransactionInRecords(transactionInRecords records) {
        return transactionInDAO.submitTransactionInRecords(records);
    }

    @Override
    @Transactional
    public void submitTransactionInRecordsUpdates(transactionInRecords records) {
        transactionInDAO.submitTransactionInRecordsUpdates(records);
    }

    @Override
    @Transactional
    public void submitTransactionTranslatedInRecords(int transactionId, int transactionRecordId, int configId) {
        transactionInDAO.submitTransactionTranslatedInRecords(transactionId, transactionRecordId, configId);
    }

    @Override
    @Transactional
    public List<batchUploads> getpendingBatches(int userId, int orgId) {
        return transactionInDAO.getpendingBatches(userId, orgId);
    }

    @Override
    @Transactional
    public List<transactionIn> getBatchTransactions(int batchId, int userId) {
        return transactionInDAO.getBatchTransactions(batchId, userId);
    }

    @Override
    @Transactional
    public List<transactionIn> getsentTransactions(int orgId) {
        return transactionInDAO.getsentTransactions(orgId);
    }

    @Override
    @Transactional
    public batchUploads getBatchDetails(int batchId) {
        return transactionInDAO.getBatchDetails(batchId);
    }

    @Override
    @Transactional
    public transactionIn getTransactionDetails(int transactionId) {
        return transactionInDAO.getTransactionDetails(transactionId);
    }

    @Override
    @Transactional
    public transactionInRecords getTransactionRecords(int transactionId) {
        return transactionInDAO.getTransactionRecords(transactionId);
    }

    @Override
    @Transactional
    public transactionInRecords getTransactionRecord(int recordId) {
        return transactionInDAO.getTransactionRecord(recordId);
    }

    @Override
    @Transactional
    public void submitTransactionTarget(transactionTarget transactionTarget) {
        transactionInDAO.submitTransactionTarget(transactionTarget);
    }

    @Override
    @Transactional
    public transactionTarget getTransactionTargetDetails(int transactionTargetId) {
        return transactionInDAO.getTransactionTargetDetails(transactionTargetId);
    }

    @Override
    @Transactional
    public void submitTransactionTargetChanges(transactionTarget transactionTarget) {
        transactionInDAO.submitTransactionTargetChanges(transactionTarget);
    }

    @Override
    @Transactional
    public transactionTarget getTransactionTarget(int batchUploadId, int transactionInId) {
        return transactionInDAO.getTransactionTarget(batchUploadId, transactionInId);
    }

    /**
     * The 'uploadAttachment' function will take in the file and orgName and upload the file to the appropriate file on the file system.
     *
     * @param fileUpload The file to be uploaded
     * @param orgName The organization name that is uploading the file. This will be the folder where to save the file to.
     */
    @Override
    public String uploadAttachment(MultipartFile fileUpload, String orgName) {

        MultipartFile file = fileUpload;
        String fileName = file.getOriginalFilename();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = file.getInputStream();
            File newFile = null;

            //Set the directory to save the brochures to
            fileSystem dir = new fileSystem();
            dir.setDir(orgName, "attachments");

            newFile = new File(dir.getDir() + fileName);

            if (newFile.exists()) {
                int i = 1;
                while (newFile.exists()) {
                    int iDot = fileName.lastIndexOf(".");
                    newFile = new File(dir.getDir() + fileName.substring(0, iDot) + "_(" + ++i + ")" + fileName.substring(iDot));
                }
                fileName = newFile.getName();
            } else {
                newFile.createNewFile();
            }

            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();

            //Save the attachment
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    @Override
    @Transactional
    public Integer submitAttachment(transactionAttachment attachment) {
        return transactionInDAO.submitAttachment(attachment);
    }

    @Override
    @Transactional
    public transactionAttachment getAttachmentById(int attachmentId) {
        return transactionInDAO.getAttachmentById(attachmentId);
    }

    @Override
    @Transactional
    public void submitAttachmentChanges(transactionAttachment attachment) {
        transactionInDAO.submitAttachmentChanges(attachment);
    }

    @Override
    @Transactional
    public List<transactionAttachment> getAttachmentsByTransactionId(int transactionInId) {
        return transactionInDAO.getAttachmentsByTransactionId(transactionInId);
    }

    @Override
    @Transactional
    public void removeAttachmentById(int attachmentId) {

        /* Need to get the file name of the attachment */
        transactionAttachment attachment = getAttachmentById(attachmentId);

        /* Need to remove the attachment */
        fileSystem currdir = new fileSystem();
        currdir.setDirByName(attachment.getfileLocation());
        File currFile = new File(currdir.getDir() + attachment.getfileName());
        currFile.delete();

        /* Now remove the attachment from the database */
        transactionInDAO.removeAttachmentById(attachmentId);

    }

    /**
     * This method needs to take in a batchUploadId and process the batch all the way to inserting into message tables If any steps fails, we revert and note the error
     *
     *
     * Last Step - insert all transactions with RP (10) status and batch status of SRP (5) for a batch
     *
     */
    @Override
    public boolean processTransactions(int batchUploadId) {

        /**
         * from here we get insert statements ready and insert*
         */
        List<Integer> configIds = getConfigIdsForBatch(batchUploadId);
        /**
         * loop each configId and prep sqls*
         */
        
        /* Grace: Here is a faster way to loop */
        for(Integer configId : configIds) {
             /**
             * separate out the transactionIds for the one that need to loop*
             */
            ConfigForInsert config = new ConfigForInsert();
            config.setBatchUploadId(batchUploadId);
            config.setConfigId(configId);
            /**
             * call sp to grab config info*
             */
        }
        
        /** Old way **/
        /*
        for (int i = 0; i <= configIds.size(); i++) {
            
             * separate out the transactionIds for the one that need to loop*
            
            ConfigForInsert config = new ConfigForInsert();
            config.setBatchUploadId(batchUploadId);
            config.setConfigId(i);
            
             * call sp to grab config info*
            
        }
       */

        return false;
    }

    /**
     * These are ready records We will insert by configId All the values for being inserted into the same field would have been appended to the first field Id 
	 * *
     */
    @Override
    public boolean insertToMessageTables(ConfigForInsert configForInsert) {
        // TODO Auto-generated method stub
        return false;
    }

	@Override
	public List <ConfigForInsert> setConfigForInsert(int configId, int batchUploadId) {
		// we call sp and set the parameters here
		return transactionInDAO.setConfigForInsert(configId, batchUploadId);	
	}

	@Override
	public List<Integer> getConfigIdsForBatch(int batchUploadId) {
		return transactionInDAO.getConfigIdsForBatch(batchUploadId);
	}

}
