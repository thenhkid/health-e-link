/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.dao.impl;

import com.ut.dph.dao.transactionOutDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.model.User;
import com.ut.dph.model.batchDownloadSummary;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationConnectionReceivers;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationSchedules;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.model.messageType;
import com.ut.dph.model.targetOutputRunLogs;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionOutNotes;
import com.ut.dph.model.transactionOutRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.service.sysAdminManager;
import com.ut.dph.service.userManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author chadmccue
 */
@Service
public class transactionOutDAOImpl implements transactionOutDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private sysAdminManager sysAdminManager;
    
    @Autowired
    private userManager usermanager;
    
    /**
     * The 'submitBatchDownload' function will submit the new batch.
     *
     * @param batchDownload The object that will hold the new batch info
     *
     * @table batchDownloadd
     *
     * @return This function returns the batchId for the newly inserted batch
     */
    @Override
    @Transactional
    public Integer submitBatchDownload(batchDownloads batchDownload) {

        Integer batchId = null;

        batchId = (Integer) sessionFactory.getCurrentSession().save(batchDownload);

        return batchId;

    }
    
    /**
     * The 'submitSummaryEntry' function will submit an entry that will contain specific information for transactions within the submitted batch. 
     * This will be used when trying to find out which batches a user has access to when logged into the ERG.
     *
     * @param summary The object that will hold the batch summary information
     *
     * @table batchDownloadSummary
     *
     * @return This function does not return anything.
     */
    @Override
    @Transactional
    public void submitSummaryEntry(batchDownloadSummary summary) throws Exception {
            
        /* Need to make sure no duplicates */
        Query query = sessionFactory.getCurrentSession().createQuery(""
                + "select id from batchDownloadSummary where batchId = :batchId "
                + "and transactionTargetId = :transactionTargetId "
                + "and sourceOrgId = :sourceOrgId "
                + "and targetOrgId = :targetOrgId "
                + "and messageTypeId = :messageTypeId "
                + "and targetConfigId = :targetConfigId"
                + "");
        
        query.setParameter("batchId", summary.getbatchId());
        query.setParameter("transactionTargetId", summary.gettransactionTargetId());
        query.setParameter("sourceOrgId", summary.getsourceOrgId());
        query.setParameter("targetOrgId", summary.gettargetOrgId());
        query.setParameter("messageTypeId", summary.getmessageTypeId());
        query.setParameter("targetConfigId", summary.gettargetConfigId());
        
        Integer summaryId = (Integer) query.uniqueResult();
        
        if(summaryId == null) {
             sessionFactory.getCurrentSession().save(summary);
        } 
    }
    
    /**
     * The 'findMergeableBatch' function will check for any batches created for the target org that are mergable and have not 
     * yet been picked up or viewed.
     * 
     * @param orgId The id of the organization to look for.
     * 
     * @return  This function will return the id of a mergeable batch or 0 if no batches are found.
     */
    @Override
    @Transactional
    public int findMergeableBatch(int orgId) {
        
         Query query = sessionFactory.getCurrentSession().createQuery("select id FROM batchDownloads where orgId = :orgId and mergeable = 1 and statusId = 28");
         query.setParameter("orgId", orgId);
        
         Integer batchId = (Integer) query.uniqueResult();
         
         if(batchId == null) {
             batchId = 0;
         }
         
         return batchId;
        
    }
    
    
    /**
     * The 'getInboxBatches' will return a list of received batches for the logged
     * in user.
     *
     * @param userId The id of the logged in user trying to view received batches
     * @param orgId The id of the organization the user belongs to
     *
     * @return The function will return a list of received batches
     */
    @Override
    @Transactional
    @SuppressWarnings("UnusedAssignment")
    public List<batchDownloads> getInboxBatches(int userId, int orgId, String searchTerm, Date fromDate, Date toDate, int page, int maxResults) throws Exception {
        int firstResult = 0;

        /* Get a list of connections the user has access to */
        Criteria connections = sessionFactory.getCurrentSession().createCriteria(configurationConnectionReceivers.class);
        connections.add(Restrictions.eq("userId", userId));
        List<configurationConnectionReceivers> userConnections = connections.list();

        List<Integer> messageTypeList = new ArrayList<Integer>();
        List<Integer> sourceOrgList = new ArrayList<Integer>();

        if (userConnections.isEmpty()) {
            messageTypeList.add(0);
            sourceOrgList.add(0);
        } else {

            for (configurationConnectionReceivers userConnection : userConnections) {
                Criteria connection = sessionFactory.getCurrentSession().createCriteria(configurationConnection.class);
                connection.add(Restrictions.eq("id", userConnection.getconnectionId()));

                configurationConnection connectionInfo = (configurationConnection) connection.uniqueResult();

                /* Get the message type for the configuration */
                Criteria targetconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                targetconfigurationQuery.add(Restrictions.eq("id", connectionInfo.gettargetConfigId()));

                configuration configDetails = (configuration) targetconfigurationQuery.uniqueResult();
                
                /* Add the message type to the message type list */
               messageTypeList.add(configDetails.getMessageTypeId());  

                /* Get the list of source orgs */
                Criteria sourceconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                sourceconfigurationQuery.add(Restrictions.eq("id", connectionInfo.getsourceConfigId()));
                configuration sourceconfigDetails = (configuration) sourceconfigurationQuery.uniqueResult();

                /* Add the target org to the target organization list */
                sourceOrgList.add(sourceconfigDetails.getorgId());
            }
        }
        
        if(messageTypeList.isEmpty()) {
            messageTypeList.add(0);
        }

        /* Get a list of available batches */
        Criteria batchSummaries = sessionFactory.getCurrentSession().createCriteria(batchDownloadSummary.class);
        batchSummaries.add(Restrictions.eq("targetOrgId", orgId));
        batchSummaries.add(Restrictions.in("messageTypeId", messageTypeList));
        batchSummaries.add(Restrictions.in("sourceOrgId", sourceOrgList));
        List<batchDownloadSummary> batchDownloadSummaryList = batchSummaries.list();

        List<Integer> batchIdList = new ArrayList<Integer>();

        if (batchDownloadSummaryList.isEmpty()) {
            batchIdList.add(0);
        } else {

            for (batchDownloadSummary summary : batchDownloadSummaryList) {
                batchIdList.add(summary.getbatchId());
            }

        }

        Criteria findBatches = sessionFactory.getCurrentSession().createCriteria(batchDownloads.class);
        findBatches.add(Restrictions.in("id", batchIdList));
        findBatches.add(Restrictions.and(
            Restrictions.ne("statusId", 29), /* Submission Processed Errored */
            Restrictions.ne("statusId", 30), /* Target Creation Errored */
            Restrictions.ne("statusId", 32) /* Submission Cancelled */
        ));
        
        if(!"".equals(fromDate) && fromDate != null) {
            findBatches.add(Restrictions.ge("dateCreated", fromDate));
        }  
        
        if(!"".equals(toDate)&& toDate != null) {
            findBatches.add(Restrictions.lt("dateCreated", toDate));
        } 
        
        findBatches.addOrder(Order.desc("dateCreated"));
        
        /* If a search term is entered conduct a search */
        if(!"".equals(searchTerm)) {
            
            List<batchDownloads> batches = findBatches.list();
            
            List<Integer> batchFoundIdList = findInboxBatches(batches, searchTerm);
            
            if (batchFoundIdList.isEmpty()) {
                batchFoundIdList.add(0);
            }
            
            Criteria foundBatches = sessionFactory.getCurrentSession().createCriteria(batchDownloads.class);
            foundBatches.add(Restrictions.in("id", batchFoundIdList));
            foundBatches.addOrder(Order.desc("dateCreated"));
            
            if (page > 1) {
                firstResult = (maxResults * (page - 1));
            }

            foundBatches.setFirstResult(firstResult);

            if (maxResults > 0) {
                //Set the max results to display
                foundBatches.setMaxResults(maxResults);
            }
            
            return foundBatches.list();
            
        }
        
        else {
            
            if (page > 1) {
                firstResult = (maxResults * (page - 1));
            }

            findBatches.setFirstResult(firstResult);

            if (maxResults > 0) {
                //Set the max results to display
                findBatches.setMaxResults(maxResults);
            }

            return findBatches.list();
        }
        
    }
    
    /**
     * The 'findInboxBatches' function will take a list of batches and apply the searchTerm to narrow down the results.
     *
     * @param batches The object containing the returned batches
     * @param searchTerm The term to search the batches on
     *
     * @return This function will return a list of batches that match the search term.
     */
    public List<Integer> findInboxBatches(List<batchDownloads> batches, String searchTerm) throws Exception {

        List<Integer> batchIdList = new ArrayList<Integer>();

        searchTerm = searchTerm.toLowerCase();
        searchTerm = searchTerm.replace(".", "\\.");

        for (batchDownloads batch : batches) {
            
            lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
            batch.setstatusValue(processStatus.getDisplayCode());

            User userDetails = usermanager.getUserById(batch.getuserId());
            String usersName = new StringBuilder().append(userDetails.getFirstName()).append(" ").append(userDetails.getLastName()).toString();
            batch.setusersName(usersName);

            /* Search the submitted by */
            if (batch.getusersName().toLowerCase().matches(".*" + searchTerm + ".*")) {
                if (!batchIdList.contains(batch.getId())) {
                    batchIdList.add(batch.getId());
                }
            }

            /* Search the batch name */
            if (batch.getutBatchName().toLowerCase().matches(".*" + searchTerm + ".*")) {
                if (!batchIdList.contains(batch.getId())) {
                    batchIdList.add(batch.getId());
                }
            }

            /* Search the batch date */
            String dateAsString = new SimpleDateFormat("MM/dd/yyyy").format(batch.getdateCreated());

            if (dateAsString.matches(".*" + searchTerm + ".*")) {
                if (!batchIdList.contains(batch.getId())) {
                    batchIdList.add(batch.getId());
                }
            }

            /* Search the status */
            if (batch.getstatusValue().toLowerCase().matches(".*" + searchTerm + ".*")) {
                if (!batchIdList.contains(batch.getId())) {
                    batchIdList.add(batch.getId());
                }
            }

            /* Search message types included in the batch */
            Criteria transactionQuery = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
            transactionQuery.add(Restrictions.eq("batchUploadId", batch.getId()));
            List<transactionIn> transactions = transactionQuery.list();

            if (!transactions.isEmpty()) {

                /* Loop through the transactions to get the config details */
                for (transactionIn transaction : transactions) {

                    Criteria configQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                    configQuery.add(Restrictions.eq("id", transaction.getconfigId()));
                    List<configuration> configs = configQuery.list();

                    if (!configs.isEmpty()) {

                        /* Loop through the configurations to get the config details */
                        for (configuration config : configs) {

                            messageType messageTypeDetails = (messageType) sessionFactory.getCurrentSession().get(messageType.class, config.getMessageTypeId());

                            /* Search the status */
                            if (messageTypeDetails.getName().toLowerCase().matches(".*" + searchTerm + ".*")) {
                                if (!batchIdList.contains(batch.getId())) {
                                    batchIdList.add(batch.getId());
                                }
                            }
                        }
                    }

                }

            }

            /* Search source data */
            Criteria sourceQuery = sessionFactory.getCurrentSession().createCriteria(batchDownloadSummary.class);
            sourceQuery.add(Restrictions.eq("batchId", batch.getId()));
            List<batchDownloadSummary> sources = sourceQuery.list();

            if (!sources.isEmpty()) {

                for (batchDownloadSummary source : sources) {
                    Organization orgDetails = (Organization) sessionFactory.getCurrentSession().get(Organization.class, source.getsourceOrgId());


                    /* Search the source organization name */
                    if (orgDetails.getOrgName().toLowerCase().matches(".*" + searchTerm + ".*")) {
                        if (!batchIdList.contains(batch.getId())) {
                            batchIdList.add(batch.getId());
                        }
                    }
                }
            }
        }

        if (batchIdList.isEmpty()) {
            batchIdList.add(0);
        }

        return batchIdList;
    }
    
    /**
     * The 'getBatchDetails' function will return the batch details for the passed in batch id.
     *
     * @param batchId The id of the batch to return.
     */
    @Override
    @Transactional
    public batchDownloads getBatchDetails(int batchId) throws Exception {
        return (batchDownloads) sessionFactory.getCurrentSession().get(batchDownloads.class, batchId);

    }
    
    /**
     * The 'getInboxBatchTransactions' function will return a list of transactions within a batch from the inbox. 
     * The list of transactions will only be the ones the passed in user has access to.
     *
     * @param batchId The id of the selected batch
     * @param userId The id of the logged in user
     *
     * @return The function will return a list of transactionIn objects
     */
    @Override
    @Transactional
    public List<transactionTarget> getInboxBatchTransactions(int batchId, int userId) throws Exception {
        /* Get a list of connections the user has access to */
        
        Criteria connections = sessionFactory.getCurrentSession().createCriteria(configurationConnectionReceivers.class);
        connections.add(Restrictions.eq("userId", userId));
        List<configurationConnectionReceivers> userConnections = connections.list();

        List<Integer> messageTypeList = new ArrayList<Integer>();
        List<Integer> OrgList = new ArrayList<Integer>();

        if (userConnections.isEmpty()) {
            messageTypeList.add(0);
            OrgList.add(0);
        } else {

            for (configurationConnectionReceivers userConnection : userConnections) {
                Criteria connection = sessionFactory.getCurrentSession().createCriteria(configurationConnection.class);
                connection.add(Restrictions.eq("id", userConnection.getconnectionId()));

                configurationConnection connectionInfo = (configurationConnection) connection.uniqueResult();

                /* Get the message type for the configuration */
                Criteria targetconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                targetconfigurationQuery.add(Restrictions.eq("id", connectionInfo.gettargetConfigId()));
                configuration configDetails = (configuration) targetconfigurationQuery.uniqueResult();

                /* Add the message type to the message type list */
                messageTypeList.add(configDetails.getMessageTypeId());

                /* Get the list of target orgs */
                Criteria sourceconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                sourceconfigurationQuery.add(Restrictions.eq("id", connectionInfo.getsourceConfigId()));
                configuration soourceconfigDetails = (configuration) sourceconfigurationQuery.uniqueResult();

                /* Add the target org to the target organization list */
                OrgList.add(soourceconfigDetails.getorgId());
            }
        }

        /* Get a list of available batches */
        Criteria batchSummaries = sessionFactory.getCurrentSession().createCriteria(batchDownloadSummary.class);
        batchSummaries.add(Restrictions.eq("batchId", batchId));
        batchSummaries.add(Restrictions.in("messageTypeId", messageTypeList));
        batchSummaries.add(Restrictions.in("sourceOrgId", OrgList));
        List<batchDownloadSummary> batchDownloadSummaryList = batchSummaries.list();

        List<Integer> transactionTargetList = new ArrayList<Integer>();

        if (batchDownloadSummaryList.isEmpty()) {
            transactionTargetList.add(0);
        } else {

            for (batchDownloadSummary summary : batchDownloadSummaryList) {
                transactionTargetList.add(summary.gettransactionTargetId());
            }

        }

        Criteria findTransactions = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
        findTransactions.add(Restrictions.in("id", transactionTargetList));
        findTransactions.addOrder(Order.desc("dateCreated"));

        return findTransactions.list();
    }
    
    /**
     * The 'getTransactionRecords' function will return the transaction TARGET records for the passed in transactionId.
     *
     * @param transactionTargetId The id of the transaction records to return
     *
     */
    @Override
    @Transactional
    public transactionOutRecords getTransactionRecords(int transactionTargetId) throws Exception {
        Query query = sessionFactory.getCurrentSession().createQuery("from transactionOutRecords where transactionTargetId = :transactionTargetId");
        query.setParameter("transactionTargetId", transactionTargetId);

        transactionOutRecords records = (transactionOutRecords) query.uniqueResult();

        return records;
    }

    /**
     * The 'getTransactionRecord' function will return the transaction TARGET record for the passed in recordId.
     *
     * @param recordId The id of the records to return
     *
     */
    @Override
    @Transactional
    public transactionOutRecords getTransactionRecord(int recordId) {
        return (transactionOutRecords) sessionFactory.getCurrentSession().get(transactionOutRecords.class, recordId);
    }
    
    /**
     * The 'getTransactionDetails' function will return the transaction TARGET details for the passed in transactionId.
     *
     * @param transactionId The id of the transaction to return
     *
     */
    @Override
    @Transactional
    public transactionTarget getTransactionDetails(int transactionId) throws Exception {
        return (transactionTarget) sessionFactory.getCurrentSession().get(transactionTarget.class, transactionId);
    }
    

    /**
     * The 'changeDeliveryStatus' function will modify the status of the viewed transaction and its related inbound
     * transaction. The function will also review the current status of all the transactions within the download batch
     * and upload batch to see if the batch is in a final delivered status.
     * 
     * @param   batchDLId           The id of the download batch for the viewed transaction
     * @param   batchUploadId       The id of the upload batch that is related to the viewed transaction
     * @param   transactionTargetId The id of the viewed transaction
     * @param   transactionInId     The id of the transaction that is related to the viewed transaction
     */
    @Override
    @Transactional
    public void changeDeliveryStatus(int batchDLId, int batchUploadId, int transactionTargetId, int transactionInId) {
        
        /* Update the current transaction status */
        Query transoutStatusUpdate = sessionFactory.getCurrentSession().createSQLQuery("UPDATE transactionTarget set statusId = 20 where id = :transactionTargetId");
        transoutStatusUpdate.setParameter("transactionTargetId", transactionTargetId);
        
        transoutStatusUpdate.executeUpdate();
        
        /* Need to update the transactionIn status Id */
        Query transInStatusUpdate = sessionFactory.getCurrentSession().createSQLQuery("UPDATE transactionIn set statusId = 20 where id = :transactionInId");
        transInStatusUpdate.setParameter("transactionInId", transactionInId);
        
        transInStatusUpdate.executeUpdate();
        
        /* 
           Need to check if all the transactions are in a Received state is so then 
           update the batchDownload status to Submission Delivery Complete (SDC, ID = 23)
           otherwise set the batchDownload status to Submission Delivery Locked (SDL, ID = 22)
        */
        Criteria findTargetTransactions = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
        findTargetTransactions.add(Restrictions.eq("batchDLId", batchDLId));
        List<transactionTarget> targetTransactions = findTargetTransactions.list();
        
        int totalReceivedTransactions = 0;
        int batchDLStatusId = 22;
        for(transactionTarget transaction : targetTransactions) {
            if(transaction.getstatusId() == 20) {
                totalReceivedTransactions+=1;
            }
        }
        
        if(totalReceivedTransactions == targetTransactions.size()) {
            batchDLStatusId = 23;
        }
        
        Query batchDownloadStatusUpdate = sessionFactory.getCurrentSession().createSQLQuery("UPDATE batchDownloads set statusId = :batchDLStatusId where id = :batchDLId");
        batchDownloadStatusUpdate.setParameter("batchDLStatusId", batchDLStatusId);
        batchDownloadStatusUpdate.setParameter("batchDLId", batchDLId);
        
        batchDownloadStatusUpdate.executeUpdate();
        
        /* 
           Need to check if all the transactions are in a Received state is so then 
           update the batchUpload status to Submission Delivery Complete (SDC, ID = 23)
           otherwise set the batchDownload status to Submission Delivery Locked (SDL, ID = 22)
        */
        Criteria findSourceTransactions = sessionFactory.getCurrentSession().createCriteria(transactionIn.class);
        findSourceTransactions.add(Restrictions.eq("batchId", batchUploadId));
        List<transactionIn> sourceTransactions = findSourceTransactions.list();
        
        int totalSentTransactions = 0;
        int batchULStatusId = 22;
        for(transactionIn transaction : sourceTransactions) {
            if(transaction.getstatusId() == 20) {
                totalSentTransactions+=1;
            }
        }
        
        if(totalSentTransactions == sourceTransactions.size()) {
            batchULStatusId = 23;
        }
        
        Query batchUploadStatusUpdate = sessionFactory.getCurrentSession().createSQLQuery("UPDATE batchUploads set statusId = :batchULStatusId where id = :batchUploadId");
        batchUploadStatusUpdate.setParameter("batchULStatusId", batchULStatusId);
        batchUploadStatusUpdate.setParameter("batchUploadId", batchUploadId);
        
        batchUploadStatusUpdate.executeUpdate();
    }
    
    /**
     * The 'getInternalStatusCodes' function will query and return the list of active internal status
     * codes that can be set to a message.
     * 
     * @return This function will return a list of internal status codes
     */
    @Override
    @Transactional
    public List getInternalStatusCodes() {
        Query query = sessionFactory.getCurrentSession().createSQLQuery("SELECT id, displaytext FROM lu_internalMessageStatus order by displayText asc");
        return query.list();
    }
    
    /**
     * The 'updateTransactionDetails' function will update the details of the transaction.
     * 
     * @param transactionDetails    The object containing the transaction to update
     * 
     * @return This function does not return anything
     */
    public void updateTransactionDetails(transactionTarget transactionDetails) throws Exception {
        sessionFactory.getCurrentSession().update(transactionDetails);
    }
    
    /**
     * The 'saveNote' function will save the new transaction note.
     * 
     * @table transactionOutNotes
     * 
     * @param note  The transactionOutNote object that will hold the new note
     * 
     * @return This function will not return anything
     */
    public void saveNote(transactionOutNotes note) throws Exception {
        sessionFactory.getCurrentSession().save(note);
    }
    
    /**
     * The 'getNotesByTransactionId' function will return a list of notes for the passed in transaction.
     *
     * @param transactionInId The id of the transaction to search on
     *
     * @return This function will return a list of transactionOutNotes objects
     */
    @Override
    @Transactional
    public List<transactionOutNotes> getNotesByTransactionId(int transactionId) throws Exception {
       Criteria criteria = sessionFactory.getCurrentSession().createCriteria(transactionOutNotes.class);
       criteria.add(Restrictions.eq("transactionTargetId", transactionId));
       criteria.addOrder(Order.desc("dateSubmitted"));
       

       return criteria.list();
        
    }
    
    /**
     * The 'removeNoteById' function will remove the note from the Database.
     *
     * @param noteId The id of the attachment to be removed
     *
     * @table transactionOutNotes
     *
     * @return This function will not return anything.
     */
    @Override
    @Transactional
    public void removeNoteById(int noteId) throws Exception {

        Query deletNote = sessionFactory.getCurrentSession().createQuery("delete from transactionOutNotes where id = :noteId");
        deletNote.setParameter("noteId", noteId);
        deletNote.executeUpdate();

    }
    
    /**
     * The 'getActiveFeedbacReportsByMessageType' function will return an associated feedback report
     * configuration for the organization passed in and for the message type of the viewed transaction.
     * 
     * @param messageTypeId     The messageType of the viewed transaction
     * @param orgId             The organization id of the user viewing the transaction
     * 
     * @return This function will return a 0 if no feedback reports are found or the id of the feedback
     *         report configuration found.
     */
    @Override
    @Transactional
    public Integer getActiveFeedbackReportsByMessageType(int messageTypeId, int orgId) throws Exception {
        
        /* Get the feedback report configurations for the passed in or and message type */
        Criteria feedbackReports = sessionFactory.getCurrentSession().createCriteria(configuration.class);
        feedbackReports.add(Restrictions.eq("orgId", orgId));
        feedbackReports.add(Restrictions.eq("associatedMessageTypeId", messageTypeId));
        feedbackReports.add(Restrictions.eq("status", true));
        feedbackReports.add(Restrictions.eq("sourceType", 2));
        feedbackReports.add(Restrictions.eq("type", 1));
        
        List<configuration> feedbackReportConfigs = feedbackReports.list();
        
        Integer feedbackConfigId = 0;
        
        if(!feedbackReportConfigs.isEmpty()) {
            /* Make sure the feedback report is of an ERG type */
            for(configuration config : feedbackReportConfigs) {

                Criteria configurationTransports = sessionFactory.getCurrentSession().createCriteria(configurationTransport.class);
                configurationTransports.add(Restrictions.eq("configId", config.getId()));

                configurationTransport transportDetails = (configurationTransport) configurationTransports.uniqueResult();

                if(transportDetails.gettransportMethodId() == 2) {
                    feedbackConfigId = config.getId();
                }

            }
        }
        
        return feedbackConfigId;
    }
    
    /**
     * The 'getFeedbackReports' function will return a list of feedback reports for the passed in transaction.
     * 
     * @param transactionId The id of the selected transaction
     * @param fromPage  The page the user is coming from, if from sent items page then we only want to show
     *                  feedback reports that are fully sent.
     * 
     * @return This function will return a list of feedback reports
     */
    @Override
    @Transactional
    public List<transactionIn> getFeedbackReports(int transactionId, String fromPage) throws Exception {
        
        Criteria feedbackReportQuery = sessionFactory.getCurrentSession().createCriteria(transactionIn.class);
        feedbackReportQuery.add(Restrictions.eq("transactionTargetId", transactionId));
        
        /* 
        If looking for feedback reports from sent batches the 
        feedback reports must be in a received state
        */
        if("sent".equals(fromPage)) {
            feedbackReportQuery.add(Restrictions.or(
                Restrictions.eq("statusId", 18),
                Restrictions.eq("statusId", 20)
            ));
        }
        
        feedbackReportQuery.addOrder(Order.desc("dateCreated"));
        
        List<transactionIn> feedbackReports = feedbackReportQuery.list();
        
        return feedbackReports;
    }
    
    
    /**
     * The 'getTransactionsByInId' will find inbox transactions based on an outbound transactionId.
     * 
     * @param transactionInId The id of the outbound transaction
     * 
     * @return This function will return a transactionTarget object
     */
    @Override
    @Transactional
    public transactionTarget getTransactionsByInId(int transactionInId) throws Exception {
        
        Criteria transactionQuery = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
        transactionQuery.add(Restrictions.eq("transactionInId", transactionInId));
        
         transactionTarget transaction = (transactionTarget) transactionQuery.uniqueResult();

        return transaction;
        
    }
    
    /**
     * The 'getpendingOutPutTransactions' function will return a list of transactions
     * that are in the 'Pending Output' status (id = 19) that are ready to start the output
     * process
     * 
     * @param   transactionTargetId This will hold a specific transaction Id to process will
     *                              default to 0 which will find all transactions.
     * 
     * @table transactionTarget;
     */
    @Override
    @Transactional
    public List<transactionTarget> getpendingOutPutTransactions(int transactionTargetId) throws Exception {
        
        Criteria transactionQuery = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
        transactionQuery.add(Restrictions.eq("statusId", 19));
        
        if(transactionTargetId > 0) {
            transactionQuery.add(Restrictions.eq("id", transactionTargetId));
        }
        
        List<transactionTarget> transactions = transactionQuery.list();

        return transactions;
    }
    
    /**
     * The 'processOutPutTransactions' method will find all the configuration form fields set up for
     * the target configuration and retrieve the data from the mapped tables and columns.
     * 
     * @transactionTargetId The id of the target transactions
     * 
     * @configId    The id of the target configuration to retrieve the form fields.
     * 
     * @transactionInId The id of the inbound transaction to get the actual data.
     */
    @Override
    @Transactional
    public boolean processOutPutTransactions(int transactionTargetId, int configId, int transactionInId) throws Exception {
        
        /* Need to pull all the data out of the appropriate message_ tables for the transaction */
        Criteria formFieldsQuery = sessionFactory.getCurrentSession().createCriteria(configurationFormFields.class);
        formFieldsQuery.add(Restrictions.eq("configId", configId));
        
        List<configurationFormFields> formFields = formFieldsQuery.list();
        
        if(!formFields.isEmpty()) {
            
            String sql;
            sql = "insert into transactionTranslatedOut (transactionTargetId, configId, ";
            Integer counter = 1;
            for(configurationFormFields formField : formFields) {
                
                sql += "f"+formField.getFieldNo();
                
                if(counter < formFields.size()) {
                    sql += ", ";
                    counter+=1;
                }
            }
            
            sql += ") ";
            
            sql += "VALUES( :transactionTargetId, :configId, ";
            
            String dataSQL;
            counter = 1;
            for(configurationFormFields formField : formFields) {
                
                dataSQL = "SELECT " + formField.getsaveToTableCol() + " from " + formField.getsaveToTableName()
                        + " WHERE transactionInId = :transactionInId";
                
                Query getData = sessionFactory.getCurrentSession().createSQLQuery(dataSQL)
                .setParameter("transactionInId", transactionInId);
                
                /* if no result is found then we need to look at the main transactionTranslatedIn table 
                for the value only if pass through errors is set
                */
                
                sql += "'"+getData.uniqueResult()+"'";
                
                if(counter < formFields.size()) {
                    sql += ", ";
                    counter+=1;
                }
            }
            
            sql += ") ";
            
            Query insertData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("transactionTargetId", transactionTargetId)
                .setParameter("configId", configId);
            
            insertData.executeUpdate();
            
            return true;
            
        }
        else {
            return false;
        }
    }
    
    /**
     * The 'updateTargetBatchStatus' function will update the status of the passed in batch downloadId
     * 
     * @param batchDLId The id of the batch download
     * @param statusId  The id of the new status
     * @param timeField 
     */
    @Override
    @Transactional
    public void updateTargetBatchStatus(Integer batchDLId, Integer statusId, String timeField) throws Exception {

        String sql = "update batchDownloads set statusId = :statusId ";
        if (!timeField.equalsIgnoreCase("")) {
            sql = sql + ", " + timeField + " = CURRENT_TIMESTAMP";
        } else {
            // we reset time
            sql = sql + ", startDateTime = null, endDateTime = null";
        }
        sql = sql + " where id = :id ";
        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("statusId", statusId)
                .setParameter("id", batchDLId);
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
            System.err.println("updateTargetBatchStatus failed." + ex);
        }
    }
    
    /**
     * 
     */
    @Override
    @Transactional
    public void updateTargetTransasctionStatus(int batchDLId, int statusId) {
        String sql = "update transactionTarget set statusId = :statusId where batchDLId = :batchDLId";
        
        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("statusId", statusId)
                .setParameter("batchDLId", batchDLId);
        
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
            System.err.println("updateTargetTransactionStatus failed." + ex);
        }
    }
    
    /**
     * The 'moveTranslatedRecords' function will copy the translated records found in
     * transactionTranslatedOut table to the transactionOutRecords table for the passed
     * in transactionId.
     * 
     * @param transactionTargetId   The id of the transaction to copy.
     * 
     * @return This function does not return anything.
     */
    @Override
    @Transactional
    public void moveTranslatedRecords(int transactionTargetId) throws Exception {
        
        /* Always clear this table out for the passed in transactionTargetId */
        Query clearRecords = sessionFactory.getCurrentSession().createSQLQuery("DELETE from transactionOutRecords where transactionTargetId = :transactionTargetId");
        clearRecords.setParameter("transactionTargetId", transactionTargetId);
        clearRecords.executeUpdate();

        Query query = sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO transactionOutRecords ("
                + "transactionTargetId, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23, f24, f25, f26, f27, f28, f29, f30, f31,"
                + "f32, f33, f34, f35, f36, f37, f38, f39, f40, f41, f42, f43, f44, f45, f46, f47, f48, f49, f50, f51, f52, f53, f54, f55, f56, f57, f58, f59, f60, f61, f62, f63, f64,"
                + "f65, f66, f67, f68, f69, f70, f71, f72, f73, f74, f75, f76, f77, f78, f79, f80, f81, f82, f83, f84, f85, f86, f87, f88, f89, f90, f91, f92, f93, f94, f95, f96, f97, f98,"
                + "f99, f100, f101, f102, f103, f104, f105, f106, f107, f108, f109, f110, f111, f112, f113, f114, f115, f116, f117, f118, f119, f120, f121, f122, f123, f124, f125, f126, f127, f128, f129,"
                + "f130, f131, f132, f133, f134, f135, f136, f137, f138, f139, f140, f141, f142, f143, f144, f145, f146, f147, f148, f149, f150, f151, f152, f153, f154, f155, f156, f157, f158, f159,"
                + "f160, f161, f162, f163, f164, f165, f166, f167, f168, f169, f170, f171, f172, f173, f174, f175, f176, f177, f178, f179, f180, f181, f182, f183, f184, f185, f186, f187, f188, f189,"
                + "f190, f191, f192, f193, f194, f195, f196, f197, f198, f199, f200, f201, f202, f203, f204, f205, f206, f207, f208, f209, f210, f211, f212, f213, f214, f215, f216, f217, f218, f219,"
                + "f220, f221, f222, f223, f224, f225, f226, f227, f228, f229, f230, f231, f232, f233, f234, f235, f236, f237, f238, f239, f240, f241, f242, f243, f244, f245, f246, f247, f248, f249,"
                + "f250, f251, f252, f253, f254, f255) "
                + "SELECT transactionTargetId, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23, f24, f25, f26, f27, f28, f29, f30, f31,"
                + "f32, f33, f34, f35, f36, f37, f38, f39, f40, f41, f42, f43, f44, f45, f46, f47, f48, f49, f50, f51, f52, f53, f54, f55, f56, f57, f58, f59, f60, f61, f62, f63, f64,"
                + "f65, f66, f67, f68, f69, f70, f71, f72, f73, f74, f75, f76, f77, f78, f79, f80, f81, f82, f83, f84, f85, f86, f87, f88, f89, f90, f91, f92, f93, f94, f95, f96, f97, f98,"
                + "f99, f100, f101, f102, f103, f104, f105, f106, f107, f108, f109, f110, f111, f112, f113, f114, f115, f116, f117, f118, f119, f120, f121, f122, f123, f124, f125, f126, f127, f128, f129,"
                + "f130, f131, f132, f133, f134, f135, f136, f137, f138, f139, f140, f141, f142, f143, f144, f145, f146, f147, f148, f149, f150, f151, f152, f153, f154, f155, f156, f157, f158, f159,"
                + "f160, f161, f162, f163, f164, f165, f166, f167, f168, f169, f170, f171, f172, f173, f174, f175, f176, f177, f178, f179, f180, f181, f182, f183, f184, f185, f186, f187, f188, f189,"
                + "f190, f191, f192, f193, f194, f195, f196, f197, f198, f199, f200, f201, f202, f203, f204, f205, f206, f207, f208, f209, f210, f211, f212, f213, f214, f215, f216, f217, f218, f219,"
                + "f220, f221, f222, f223, f224, f225, f226, f227, f228, f229, f230, f231, f232, f233, f234, f235, f236, f237, f238, f239, f240, f241, f242, f243, f244, f245, f246, f247, f248, f249,"
                + "f250, f251, f252, f253, f254, f255 "
                + "FROM transactionTranslatedOut where transactionTargetId = :transactionTargetId");
        query.setParameter("transactionTargetId", transactionTargetId);
        query.executeUpdate();
    }
    
    
    /**
     * The 'getLoadedOutBoundTransactions' function will look to see what translated transactions are loaded
     * and ready to be pulled out into a download batch
     * 
     * @param configId  The id of the configuration to check for loaded transactions for
     */
    @Override
    @Transactional
    public List<transactionTarget> getLoadedOutBoundTransactions(int configId) {
        
        Criteria transactionQuery = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
        transactionQuery.add(Restrictions.eq("statusId", 9));
        transactionQuery.add(Restrictions.eq("configId", configId));
        
        List<transactionTarget> transactions = transactionQuery.list();

        return transactions;
    }
    
    
    /**
     * The 'updateTransactionTargetBatchDLId' function will update the transactionTarget with the generated
     * batch downloadId.
     *
     * @param batchId The id of the created batch
     * @param transactionId The id of the specific transaction to update (default to 0)
     * 
     */
    @Override
    @Transactional
    public void updateTransactionTargetBatchDLId(Integer batchId, Integer transactionTargetId) {
        
        String sql = "update transactionTarget "
                + " set batchDLId = :batchId, "
                + "statusTime = CURRENT_TIMESTAMP"
                + " where id = :transactionTargetId";

        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchId", batchId)
                .setParameter("transactionTargetId", transactionTargetId);
          
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
            System.err.println("updateTransactionStatus failed." + ex);
        }

    }
    
    /**
     * The 'updateBatchOutputFileName' function will update the outputFileName with the finalized
     * generated file name. This will contain the appropriate extension.
     * 
     * @param batchId   The id of the batch to update
     * @param fileName  The new file name to update.
     */
    @Override
    @Transactional
    public void updateBatchOutputFileName(int batchId, String fileName) {
         String sql = "update BatchDownloads "
                + " set outputFileName = :fileName "
                + " where id = :batchId";

        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchId", batchId)
                .setParameter("fileName", fileName);
          
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
            System.err.println("update Batch outputfile name failed." + ex);
        }
    }
    
    
    /**
     * The 'getMaxFieldNo' function will return the max field number for the passed in configuration.
     * 
     * @param configId  The id of the configuration to find out how many fields it has
     * 
     * @return This function will return the max field number.
     */
    public int getMaxFieldNo(int configId) {
        
        
         /* Need to make sure no duplicates */
        Query query = sessionFactory.getCurrentSession().createQuery(""
                + "select max(fieldNo) as maxFieldNo from configurationFormFields where configId = :configId ");
      
        query.setParameter("configId", configId);
        
        int maxFieldNo = (Integer) query.uniqueResult();
        
        return maxFieldNo;
        
    }
    
    /**
     * The 'getdownloadableBatches' will return a list of received batches for the logged
     * in user that are ready to be downloaded
     *
     * @param userId The id of the logged in user trying to view downloadable batches
     * @param orgId The id of the organization the user belongs to
     *
     * @return The function will return a list of downloadable batches
     */
    @Override
    @Transactional
    @SuppressWarnings("UnusedAssignment")
    public List<batchDownloads> getdownloadableBatches(int userId, int orgId, Date fromDate, Date toDate, String searchTerm, int page, int maxResults) throws Exception {
        int firstResult = 0;
        
        /* Get a list of connections the user has access to */
        Criteria connections = sessionFactory.getCurrentSession().createCriteria(configurationConnectionReceivers.class);
        connections.add(Restrictions.eq("userId", userId));
        List<configurationConnectionReceivers> userConnections = connections.list();

        List<Integer> messageTypeList = new ArrayList<Integer>();
        List<Integer> sourceOrgList = new ArrayList<Integer>();

        if (userConnections.isEmpty()) {
            messageTypeList.add(0);
            sourceOrgList.add(0);
        } else {

            for (configurationConnectionReceivers userConnection : userConnections) {
                Criteria connection = sessionFactory.getCurrentSession().createCriteria(configurationConnection.class);
                connection.add(Restrictions.eq("id", userConnection.getconnectionId()));

                configurationConnection connectionInfo = (configurationConnection) connection.uniqueResult();

                /* Get the message type for the configuration */
                Criteria targetconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                targetconfigurationQuery.add(Restrictions.eq("id", connectionInfo.gettargetConfigId()));

                configuration configDetails = (configuration) targetconfigurationQuery.uniqueResult();
                
                /* Need to make sure only file download configurations are displayed */
                Criteria transportDetailsQuery = sessionFactory.getCurrentSession().createCriteria(configurationTransport.class);
                transportDetailsQuery.add(Restrictions.eq("configId", configDetails.getId()));
                
                configurationTransport transportDetails = (configurationTransport) transportDetailsQuery.uniqueResult();
                
                if(transportDetails.gettransportMethodId() == 1) {
                    /* Add the message type to the message type list */
                    messageTypeList.add(configDetails.getMessageTypeId()); 
                }

                /* Get the list of source orgs */
                Criteria sourceconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                sourceconfigurationQuery.add(Restrictions.eq("id", connectionInfo.getsourceConfigId()));
                configuration sourceconfigDetails = (configuration) sourceconfigurationQuery.uniqueResult();

                /* Add the target org to the target organization list */
                sourceOrgList.add(sourceconfigDetails.getorgId());
            }
        }
        
        if(messageTypeList.isEmpty()) {
            messageTypeList.add(0);
        }

        /* Get a list of available batches */
        Criteria batchSummaries = sessionFactory.getCurrentSession().createCriteria(batchDownloadSummary.class);
        batchSummaries.add(Restrictions.eq("targetOrgId", orgId));
        batchSummaries.add(Restrictions.in("messageTypeId", messageTypeList));
        batchSummaries.add(Restrictions.in("sourceOrgId", sourceOrgList));
        List<batchDownloadSummary> batchDownloadSummaryList = batchSummaries.list();

        List<Integer> batchIdList = new ArrayList<Integer>();

        if (batchDownloadSummaryList.isEmpty()) {
            batchIdList.add(0);
        } else {

            for (batchDownloadSummary summary : batchDownloadSummaryList) {
                batchIdList.add(summary.getbatchId());
            }

        }

        Criteria findBatches = sessionFactory.getCurrentSession().createCriteria(batchDownloads.class);
        findBatches.add(Restrictions.in("id", batchIdList));
        findBatches.add(Restrictions.or(
                Restrictions.eq("statusId", 22),
                Restrictions.eq("statusId", 23),
                Restrictions.eq("statusId", 28)
        ));
                
        if(!"".equals(fromDate)) {
            findBatches.add(Restrictions.ge("dateCreated", fromDate));
        }  
        
        if(!"".equals(toDate)) {
            findBatches.add(Restrictions.lt("dateCreated", toDate));
        } 
         
        findBatches.addOrder(Order.desc("dateCreated"));
        
        /* If a search term is entered conduct a search */
        if(!"".equals(searchTerm)) {
            
            List<batchDownloads> batches = findBatches.list();
            
            List<Integer> batchFoundIdList = finddownloadableBatches(batches, searchTerm);
            
            if (batchFoundIdList.isEmpty()) {
                batchFoundIdList.add(0);
            }
            
            Criteria foundBatches = sessionFactory.getCurrentSession().createCriteria(batchDownloads.class);
            foundBatches.add(Restrictions.in("id", batchFoundIdList));
            foundBatches.addOrder(Order.desc("dateCreated"));
            
            if (page > 1) {
                firstResult = (maxResults * (page - 1));
            }

            foundBatches.setFirstResult(firstResult);

            if (maxResults > 0) {
                //Set the max results to display
                foundBatches.setMaxResults(maxResults);
            }
            
            return foundBatches.list();
            
        }
        
        else {
            
            if (page > 1) {
                firstResult = (maxResults * (page - 1));
            }

            findBatches.setFirstResult(firstResult);

            if (maxResults > 0) {
                //Set the max results to display
                findBatches.setMaxResults(maxResults);
            }

            return findBatches.list();
        }

        
    }
    
    /**
     * The 'finddownloadableBatches' function will take a list of downloadable batches and apply the searchTerm to narrow down the results.
     *
     * @param batches The object containing the returned batches
     * @param searchTerm The term to search the batches on
     *
     * @return This function will return a list of batches that match the search term.
     */
    public List<Integer> finddownloadableBatches(List<batchDownloads> batches, String searchTerm) {

        List<Integer> batchIdList = new ArrayList<Integer>();

        searchTerm = searchTerm.toLowerCase();
        searchTerm = searchTerm.replace(".", "\\.");

        for (batchDownloads batch : batches) {
           
            /* Search the submitted by */
            if (batch.getoutputFIleName().toLowerCase().matches(".*" + searchTerm + ".*")) {
                if (!batchIdList.contains(batch.getId())) {
                    batchIdList.add(batch.getId());
                }
            }

            /* Search the batch name */
            if (batch.getutBatchName().toLowerCase().matches(".*" + searchTerm + ".*")) {
                if (!batchIdList.contains(batch.getId())) {
                    batchIdList.add(batch.getId());
                }
            }

            /* Search the batch date */
            String dateAsString = new SimpleDateFormat("MM/dd/yyyy").format(batch.getdateCreated());

            if (dateAsString.matches(".*" + searchTerm + ".*")) {
                if (!batchIdList.contains(batch.getId())) {
                    batchIdList.add(batch.getId());
                }
            }

            /* Search message types included in the batch */
            Criteria transactionQuery = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
            transactionQuery.add(Restrictions.eq("batchDLId", batch.getId()));
            List<transactionTarget> transactions = transactionQuery.list();

            if (!transactions.isEmpty()) {

                /* Loop through the transactions to get the config details */
                for (transactionTarget transaction : transactions) {

                    Criteria configQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                    configQuery.add(Restrictions.eq("id", transaction.getconfigId()));
                    List<configuration> configs = configQuery.list();

                    if (!configs.isEmpty()) {

                        /* Loop through the configurations to get the config details */
                        for (configuration config : configs) {

                            messageType messageTypeDetails = (messageType) sessionFactory.getCurrentSession().get(messageType.class, config.getMessageTypeId());

                            /* Search the status */
                            if (messageTypeDetails.getName().toLowerCase().matches(".*" + searchTerm + ".*")) {
                                if (!batchIdList.contains(batch.getId())) {
                                    batchIdList.add(batch.getId());
                                }
                            }
                        }
                    }

                }

            }

            /* Search target data */
            Criteria targetQuery = sessionFactory.getCurrentSession().createCriteria(batchDownloadSummary.class);
            targetQuery.add(Restrictions.eq("batchId", batch.getId()));
            List<batchDownloadSummary> targets = targetQuery.list();

            if (!targets.isEmpty()) {

                for (batchDownloadSummary target : targets) {
                    Organization orgDetails = (Organization) sessionFactory.getCurrentSession().get(Organization.class, target.gettargetOrgId());

                    /* Search the organization name */
                    if (orgDetails.getOrgName().toLowerCase().matches(".*" + searchTerm + ".*")) {
                        if (!batchIdList.contains(batch.getId())) {
                            batchIdList.add(batch.getId());
                        }
                    }
                }

            }
        }

        if (batchIdList.isEmpty()) {
            batchIdList.add(0);
        }

        return batchIdList;
    }
    
    
    /**
     * The 'updateLastDownloaded' function will update the last downloaded
     * field for the passed in batch
     * 
     * @param batchId   The id of the batch to update.
     * 
     * @return this function will not return anything.
     */
    @Override
    @Transactional
    public void updateLastDownloaded(int batchId) throws Exception {
        
        String sql = "update BatchDownloads "
                + " set lastDownloaded = CURRENT_TIMESTAMP "
                + " where id = :batchId";

        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchId", batchId);
          
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
            System.err.println("update Batch last downloaded date failed." + ex);
        }
        
    }
    
    /**
     * The 'getScheduledConfigurations' function will return a list of configurations
     * that have a Daily, Weekly or Monthly schedule setting
     */
    @Override
    public List<configurationSchedules> getScheduledConfigurations() {
        
        Query query = sessionFactory.getCurrentSession().createQuery("from configurationSchedules where type = 2 or type = 3 or type = 4");

        List<configurationSchedules> scheduledConfigList = query.list();
        return scheduledConfigList;
        
    }
    
    /**
     * The 'updateBatchStatus' function will update the status of the passed in batch
     * 
     * @param batchId   The id of the batch to update
     * @param statusId  The status to update the batch to
     * @param timeField 
     */
    @Override
    @Transactional
    public void updateBatchStatus(Integer batchId, Integer statusId) {

        String sql = "update batchDownloads set statusId = :statusId ";
        sql = sql + " where id = :batchId ";
        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("statusId", statusId)
                .setParameter("batchId", batchId);
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
            System.err.println("updateBatch download Status failed." + ex);
        }

    }
    
    /**
     * The 'saveOutputRunLog' function will insert the latest run log
     * for the batch.
     * 
     * @param log The output run log to save.
     */
    @Override
    public void saveOutputRunLog(targetOutputRunLogs log) throws Exception {
        sessionFactory.getCurrentSession().save(log);
    }
    
    /**
     * The 'targetOutputRunLogs' function will return the latest output run log for the
     * passed in configuration Id
     * 
     * @param configId = The configuration to find the latest log.
     * 
     * @return This function will return the latest log
     */
    @Override
    public List<targetOutputRunLogs> getLatestRunLog(int configId) throws Exception {
        
        Criteria latestLogQuery = sessionFactory.getCurrentSession().createCriteria(targetOutputRunLogs.class);
        latestLogQuery.add(Restrictions.eq("configId", configId));
        latestLogQuery.addOrder(Order.desc("lastRunTime"));
        
        return latestLogQuery.list();
        
    }
    
    /**
     * The 'getTransactionsByBatchDLId' will get a list of transactions by the batch download Id.
     * 
     * @param   batchDLId   The batch Download Id to search with.
     * 
     * @return This function will return a list of transaction targets.
     */
    @Override
    public List<transactionTarget> getTransactionsByBatchDLId(int batchDLId) {
        
        Criteria targets = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
        targets.add(Restrictions.eq("batchDLId", batchDLId));
        
        return targets.list();
    }
    
    /**
     * The 'cancelMessageTransaction' will cancel both the transactionIn and transactionTarget
     * entries.
     * 
     * @param transactionId The id of the transaction we want to cancel.
     * 
     * @return This function will not return anything.
     */
    @Override
    public void cancelMessageTransaction(int transactionId, int transactionInId) {
        
        /* Update the transactionTarget status */
        String targetSQL = "update transactionTarget set statusId = :statusId ";
        targetSQL = targetSQL + " where id = :transactionId ";
        Query updateTargetData = sessionFactory.getCurrentSession().createSQLQuery(targetSQL)
                .setParameter("statusId", 31)
                .setParameter("transactionId", transactionId);
        try {
            updateTargetData.executeUpdate();
        } catch (Exception ex) {
            System.err.println("cancel transaction failed." + ex);
        }
        
        /* Update the transactionIn status */
        String sql = "update transactionIn set statusId = :statusId ";
        sql = sql + " where id = :transactionInId ";
        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("statusId", 31)
                .setParameter("transactionInId", transactionInId);
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
            System.err.println("cancel transaction failed." + ex);
        }
        
        
        
        
    }
    
}
