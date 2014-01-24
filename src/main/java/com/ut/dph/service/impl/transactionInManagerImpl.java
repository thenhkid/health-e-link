/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.dph.service.impl;

import com.ut.dph.dao.messageTypeDAO;
import com.ut.dph.dao.transactionInDAO;
import com.ut.dph.model.batchUploadSummary;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.transactionAttachment;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.model.custom.ConfigForInsert;
import com.ut.dph.reference.fileSystem;
import com.ut.dph.service.configurationManager;
import com.ut.dph.service.configurationTransportManager;

import org.springframework.stereotype.Service;

import com.ut.dph.service.transactionInManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
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

    @Autowired
    private messageTypeDAO messageTypeDAO;

    @Autowired
    private configurationManager configurationManager;

    @Autowired
    private configurationTransportManager configurationtransportmanager;

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
    public List<batchUploads> getpendingBatches(int userId, int orgId, int page, int maxResults) {
        return transactionInDAO.getpendingBatches(userId, orgId, page, maxResults);
    }
    
    @Override
    @Transactional
    public List<batchUploads> findBatches(List<batchUploads> batches, String searchTerm) {
        return transactionInDAO.findBatches(batches, searchTerm);
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
     * This takes a batch of 
     *
     * Last Step - insert all transactions with RP (10) status and batch status of SRP (5) for a batch
     *
     */
    @Override
    
    public boolean processTransactions(int batchUploadId) {
        /** check batch status **/
    	
    	/**
         * Check for R/O Apply CW/Macros Apply method to concatenate values for the same saveToTableCol using delimiter ||^||
         */
        /**
         * from here we prepare sql statement and insert we assume all transactions are validated and that multiple values /rows being inserted in the the same field are separated by delimiter ||^||
         */
        List<Integer> configIds = getConfigIdsForBatch(batchUploadId);
        for (Integer configId : configIds) {
            /**
             * this list have the insert /check statements for each message table *
             */
            List<ConfigForInsert> configforConfigIds = setConfigForInsert(configId, batchUploadId);
            /**
             * we loop though each table and grab the transactions that has multiple values for that table, we set it to a list *
             */
            for (ConfigForInsert config : configforConfigIds) {
                /**
                 * we grab list of ids with multiple for this config we use the checkDelim string to look for those transactions *
                 */
                List<Integer> transIds = getTransWithMultiValues(config);
                config.setLoopTransIds(transIds);

                /**
                 * we need to check if we need to insert in case the whole table is mapped but doesn't contain values *
                 */
                List<Integer> skipTheseIds = getBlankTransIds(config);
                config.setBlankValueTransId(skipTheseIds);

                /**
                 * we insert single values *
                 */
                insertSingleToMessageTables(config);

                /**
                 * we loop through transactions with multiple values and use SP to loop values with delimiters
                 *
                 */
                for (Integer transId : transIds) {
                    /**
                     * we check how long field is*
                     */
                    Integer subStringTotal = countSubString(config, transId);
                    //TODO look up loop counter - how to write counter in new syntax?
                    for (int i = 0; i <= subStringTotal; i++) {
                        insertMultiValToMessageTables(config, i + 1, transId);
                    }

                }
            }
        }
        /** now that we are done with inserting we check batch settings to see if we can release the batch 
         * we have Auto Release
         * **/
        
        
        return false;
    }

    @Override
    public List<ConfigForInsert> setConfigForInsert(int configId, int batchUploadId) {
        // we call sp and set the parameters here
        return transactionInDAO.setConfigForInsert(configId, batchUploadId);
    }

    @Override
    public List<Integer> getConfigIdsForBatch(int batchUploadId) {
        return transactionInDAO.getConfigIdsForBatch(batchUploadId);
    }

    @Override
    public List<Integer> getTransWithMultiValues(ConfigForInsert config) {
        return transactionInDAO.getTransWithMultiValues(config);
    }

    /**
     * These are ready records We will insert by configId All the values for being inserted into the same field would have been appended to the first field Id *
     */
    @Override
    public boolean insertSingleToMessageTables(ConfigForInsert configForInsert) {
        return transactionInDAO.insertSingleToMessageTables(configForInsert);
    }

    /**
     * this method takes in the transId, the insert fields, the insert tables*
     */
    @Override
    public boolean insertMultiValToMessageTables(ConfigForInsert config, Integer subStringCounter, Integer transId) {
        return transactionInDAO.insertMultiValToMessageTables(config, subStringCounter, transId);
    }

    @Override
    public boolean clearMessageTables(int batchId) {
        List<String> mts = transactionInDAO.getMessageTables();
        try {
            for (String mt : mts) {
                transactionInDAO.clearMessageTableForBatch(batchId, mt);
            }
            return true;
        } catch (Exception e) {
            System.out.println("clearMessageTables " + e.getStackTrace());
            return false;

        }
    }

    /**
     * The 'uploadBatchFile' function will take in the file and orgName and upload the file to the appropriate file on the file system. The function will run the file through various validations. If a single validation fails the batch will be put in a error validation status and the file will be removed from the system. The user will receive an error message on the screen letting them know which validations have failed and be asked to upload a new file.
     *
     * The following validations will be taken place. - File is not empty - Proper file type (as determined in the configuration set up) - Proper delimiter (as determined in the configuration set up) - Does not exceed file size (as determined in the configuration set up)
     *
     * @param configId The configuration Id to get some validation parameters
     * @param fileUpload The file to be uploaded
     *
     */
    @Override
    public Map<String, String> uploadBatchFile(int configId, MultipartFile fileUpload) {

        configuration configDetails = configurationManager.getConfigurationById(configId);
        configurationTransport transportDetails = configurationtransportmanager.getTransportDetails(configId);

        MultipartFile file = fileUpload;
        String fileName = file.getOriginalFilename();

        long fileSize = file.getSize();
        long fileSizeMB = (fileSize / (1024L * 1024L));

        /* 
         1 = File is empty
         2 = Too large
         3 = Wrong file type
         4 = Wrong delimiter
         */
        Map<String, String> batchFileResults = new HashMap<String, String>();

        /* Make sure the file is not empty : ERROR CODE 1 */
        if (fileSize == 0) {
            batchFileResults.put("emptyFile", "1");
        }

        /* Make sure file is the correct size : ERROR CODE 2 */
        double maxFileSize = (double) transportDetails.getmaxFileSize();

        if (fileSizeMB > maxFileSize) {
            batchFileResults.put("wrongSize", "2");
        }

        InputStream inputStream;
        OutputStream outputStream;

        try {
            inputStream = file.getInputStream();
            File newFile = null;

            //Set the directory to save the brochures to
            fileSystem dir = new fileSystem();

            String filelocation = transportDetails.getfileLocation();
            filelocation = filelocation.replace("/bowlink/", "");
            dir.setDirByName(filelocation);

            newFile = new File(dir.getDir() + fileName);

            if (newFile.exists()) {
                int i = 1;
                while (newFile.exists()) {
                    int iDot = fileName.lastIndexOf(".");
                    newFile = new File(dir.getDir() + fileName.substring(0, iDot) + "_(" + ++i + ")" + fileName.substring(iDot));
                }
                fileName = newFile.getName();
                newFile.createNewFile();
            } else {
                newFile.createNewFile();
            }

            batchFileResults.put("fileName", fileName);

            /* Make sure file is the correct file type : ERROR CODE 3 */
            String ext = FilenameUtils.getExtension(dir.getDir() + fileName);

            String fileType = (String) configurationManager.getFileTypesById(transportDetails.getfileType());

            if (ext == null ? fileType != null : !ext.equals(fileType)) {
                batchFileResults.put("wrongFileType", "3");
            }

            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();

            /* Make sure the file has the correct delimiter : ERROR CODE 5 */
            String delimChar = (String) messageTypeDAO.getDelimiterChar(transportDetails.getfileDelimiter());

            //Check to make sure the file contains the selected delimiter
            //Set the directory that holds the crosswalk files
            int delimCount = (Integer) dir.checkFileDelimiter(dir, fileName, delimChar);

            if (delimCount < 10) {
                batchFileResults.put("wrongDelim", "4");
            }

            //Save the attachment
        } catch (IOException e) {
            e.printStackTrace();
        }

        return batchFileResults;

    }

    @Override
    public List<Integer> getBlankTransIds(ConfigForInsert config) {
        return transactionInDAO.getBlankTransIds(config);
    }

    @Override
    public Integer countSubString(ConfigForInsert config, Integer transId) {
        return transactionInDAO.countSubString(config, transId);
    }
    
    @Override
    public List<batchUploads> getuploadedBatches(int userId, int orgId) {
        return transactionInDAO.getuploadedBatches(userId, orgId);
    }

    /** We will take a batch and 
     * then from its status etc we will decide if we want to process transactions or not.
     * This allow the admin to run just one batch
     * 
     * This assumes batches SR - 6, Trans status REL
     * We still run through entire proces but
     * these records should pass... (check to make sure it aligns with file upload)
     * just be applying Macros / CW and inserting into our message tables
     */
    /**
     * This assumes batch being passed in is SSA or SR
     * 1. We set it to SBP, start date time
     * 
     * **/
	@Override
	public boolean processBatch(int batchUploadId) {
		/** set batch to SBP - 4**/
		updateBatchStatus (batchUploadId, 4, "startDateTime");
		
		/** for non erg, this do a bunch of batch checks **/
		
		/** inserts targets, transactionIn etc **/
		
		/** run validation**/
		
		/** run cw/macros **/
		
		/** inserts for batches that passes **/
		processTransactions(batchUploadId);
		
		/** set batch to SPC 24**/
		updateBatchStatus (batchUploadId, 24, "endDateTime");
		/** set all REL - 10 records to 19 **/
		updateTransactionStatus (batchUploadId, 12, 19);
		
		return false;
	}
	

	/** 
	 * 	this is called by the scheduler
	 *  It selects all batch with a status of SSA 
	 *  Loads them SSL - Trans - L
	 *  Starts the Process - SBP - Parses 
	 *  
	 *  1. Validate R/O - ERG records will just pass
     * 		we select batches that are 
     * 2. Validate Fields - ERG records will just pass
     * 3. Apply CW / Macros - ERG records should just pass as what is being inserted is our internal
     * standard
     * 4. We insert
     * 
     * In between don't forget to change status
     * Might need wrapper to align upload file process
	 */
	@Override
	public boolean processBatches() {
		//0. grab all batches with SSA - 2
		
		//1. validate r/o
		
		//2. validate type
		
		//3. apply cw / macros to check for valid data
		
		//4. check auto release / manual status 
		
		//5. assuming all is well, call processTransactions(batchUploadId);
			/**status at this point **/
		return false;
	}

	@Override
	public void updateBatchStatus(Integer batchUploadId, Integer statusId, String timeField) {
		transactionInDAO.updateBatchStatus (batchUploadId, statusId, timeField);
	}

	@Override
	public void updateTransactionStatus (Integer batchUploadId, Integer fromStatusId, Integer toStatusId)
	{
		transactionInDAO.updateTransactionStatus (batchUploadId, fromStatusId, toStatusId);
	}

	/**
	 * provided the batch status is not one of the delivery status
	 * (22 SDL, 23 SDC)
	 * what would we like clear batch to do
	 * 1. Change batch process to SBP to nothing can be touch 
	 * 2. remove records from message tables
	 * 3. figure out what status to set batch
	 * **/
	@Override
	public boolean clearBatch(Integer batchUploadId) {
		boolean canDelete = transactionInDAO.allowBatchClear(batchUploadId);
		boolean cleared = false;
		if (canDelete) {
			/**we remove from message tables**/
			cleared = clearMessageTables(batchUploadId);
			if (cleared) {
				int toBatchStatusId = 3; //SSA
				if (getBatchDetails(batchUploadId).gettransportMethodId() == 2) {
					toBatchStatusId = 5;
					transactionInDAO.updateTransactionStatus (batchUploadId, 0, 15); 
				} else  {
					//we clear transactionInRecords here as for batch upload we start over
					cleared = clearTransactionInRecords(batchUploadId);
				}
				transactionInDAO.updateBatchStatus(batchUploadId, toBatchStatusId, "");
			}
		}
		return cleared;
	}

	@Override
	public boolean setDoNotProcess(Integer batchId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean clearTransactionInRecords(Integer batchUploadId) {
		return transactionInDAO.clearTransactionInRecords(batchUploadId);
	}
	
	
	
	

}
