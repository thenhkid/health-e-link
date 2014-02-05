/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.dao.impl;

import com.ut.dph.dao.transactionOutDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.model.batchDownloadSummary;
import com.ut.dph.model.batchDownloads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationConnectionReceivers;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.messageType;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionOutNotes;
import com.ut.dph.model.transactionOutRecords;
import com.ut.dph.model.transactionTarget;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public List<batchDownloads> getInboxBatches(int userId, int orgId, int page, int maxResults) {
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
        findBatches.addOrder(Order.desc("dateCreated"));

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
    
    /**
     * The 'findInboxBatches' function will take a list of batches and apply the searchTerm to narrow down the results.
     *
     * @param batches The object containing the returned batches
     * @param searchTerm The term to search the batches on
     *
     * @return This function will return a list of batches that match the search term.
     */
    @Override
    @Transactional
    public List<batchDownloads> findInboxBatches(List<batchDownloads> batches, String searchTerm) {

        List<Integer> batchIdList = new ArrayList<Integer>();

        searchTerm = searchTerm.toLowerCase();
        searchTerm = searchTerm.replace(".", "\\.");

        for (batchDownloads batch : batches) {

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

        Criteria findBatches = sessionFactory.getCurrentSession().createCriteria(batchDownloads.class);
        findBatches.add(Restrictions.in("id", batchIdList));
        findBatches.addOrder(Order.desc("dateCreated"));

        return findBatches.list();
    }
    
    /**
     * The 'getBatchDetails' function will return the batch details for the passed in batch id.
     *
     * @param batchId The id of the batch to return.
     */
    @Override
    @Transactional
    public batchDownloads getBatchDetails(int batchId) {
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
    public List<transactionTarget> getInboxBatchTransactions(int batchId, int userId) {
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
    public transactionOutRecords getTransactionRecords(int transactionTargetId) {
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
    public transactionTarget getTransactionDetails(int transactionId) {
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
    public void updateTransactionDetails(transactionTarget transactionDetails) {
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
    public void saveNote(transactionOutNotes note) {
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
    public List<transactionOutNotes> getNotesByTransactionId(int transactionId) {
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
    public void removeNoteById(int noteId) {

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
    public Integer getActiveFeedbackReportsByMessageType(int messageTypeId, int orgId) {
        
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
    public List<transactionIn> getFeedbackReports(int transactionId, String fromPage) {
        
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
    public transactionTarget getTransactionsByInId(int transactionInId) {
        
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
    public List<transactionTarget> getpendingOutPutTransactions(int transactionTargetId) {
        
        Criteria transactionQuery = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
        transactionQuery.add(Restrictions.eq("statusId", 19));
        
        if(transactionTargetId > 0) {
            transactionQuery.add(Restrictions.eq("id", transactionTargetId));
        }
        
        List<transactionTarget> transactions = transactionQuery.list();

        return transactions;
    }
    
    /**
     * 
     */
    @Override
    @Transactional
    public boolean processOutPutTransactions(int transactionTargetId, int configId, int transactionInId) {
        
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
            
        }
        
        return true;
        
    }
}
