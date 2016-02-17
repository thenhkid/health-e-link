/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.dao.impl;

import com.ut.healthelink.dao.transactionOutDAO;
import com.ut.healthelink.model.batchDownloadSummary;
import com.ut.healthelink.model.batchDownloads;
import com.ut.healthelink.model.configuration;
import com.ut.healthelink.model.configurationConnection;
import com.ut.healthelink.model.configurationConnectionReceivers;
import com.ut.healthelink.model.configurationFormFields;
import com.ut.healthelink.model.configurationSchedules;
import com.ut.healthelink.model.configurationTransport;
import com.ut.healthelink.model.lutables.lu_ProcessStatus;
import com.ut.healthelink.model.messagePatients;
import com.ut.healthelink.model.targetOutputRunLogs;
import com.ut.healthelink.model.transactionIn;
import com.ut.healthelink.model.transactionOutNotes;
import com.ut.healthelink.model.transactionOutRecords;
import com.ut.healthelink.model.transactionTarget;
import com.ut.healthelink.service.sysAdminManager;
import com.ut.healthelink.service.transactionInManager;
import com.ut.healthelink.service.userManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author chadmccue
 */
@Repository
public class transactionOutDAOImpl implements transactionOutDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private sysAdminManager sysAdminManager;

    @Autowired
    private userManager usermanager;
    
    @Autowired
    private transactionInManager transactionInManager;

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
     * The 'submitSummaryEntry' function will submit an entry that will contain specific information for transactions within the submitted batch. This will be used when trying to find out which batches a user has access to when logged into the ERG.
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
                + "and sourceSubOrgId = :sourceSubOrgId "
                + "and targetOrgId = :targetOrgId "
                + "and messageTypeId = :messageTypeId "
                + "and targetConfigId = :targetConfigId"
                + "");

        query.setParameter("batchId", summary.getbatchId());
        query.setParameter("transactionTargetId", summary.gettransactionTargetId());
        query.setParameter("sourceOrgId", summary.getsourceOrgId());
        query.setParameter("sourceSubOrgId", summary.getSourceSubOrgId());
        query.setParameter("targetOrgId", summary.gettargetOrgId());
        query.setParameter("messageTypeId", summary.getmessageTypeId());
        query.setParameter("targetConfigId", summary.gettargetConfigId());

        Integer summaryId = (Integer) query.uniqueResult();

        if (summaryId == null) {
            sessionFactory.getCurrentSession().save(summary);
        }
    }

    /**
     * The 'findMergeableBatch' function will check for any batches created for the target org that are mergable and have not yet been picked up or viewed.
     *
     * @param orgId The id of the organization to look for.
     *
     * @return This function will return the id of a mergeable batch or 0 if no batches are found.
     */
    @Override
    @Transactional
    public int findMergeableBatch(int orgId) {

        Query query = sessionFactory.getCurrentSession().createQuery("select id FROM batchDownloads where orgId = :orgId and mergeable = 1 and (statusId = 23 OR statusId = 28)");
        query.setParameter("orgId", orgId);

        Integer batchId = (Integer) query.uniqueResult();

        if (batchId == null) {
            batchId = 0;
        }

        return batchId;

    }

    /**
     * The 'getInboxBatches' will return a list of received batches for the logged in user.
     *
     * @param userId The id of the logged in user trying to view received batches
     * @param orgId The id of the organization the user belongs to
     *
     * @return The function will return a list of received batches
     */
    @Override
    @Transactional
    @SuppressWarnings("UnusedAssignment")
    public List<batchDownloads> getInboxBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception {

        return findInboxBatches(userId, orgId, 0, 0, fromDate, toDate);

    }

    /**
     * The 'getInboxBatchesHistory' will return a list of received batches for the logged in user.
     *
     * @param userId The id of the logged in user trying to view received batches
     * @param orgId The id of the organization the user belongs to
     *
     * @return The function will return a list of received batches
     */
    @Override
    @Transactional
    @SuppressWarnings("UnusedAssignment")
    public List<batchDownloads> getInboxBatchesHistory(int userId, int orgId, int fromOrgId, int messageTypeId, Date fromDate, Date toDate) throws Exception {

        return findInboxBatches(userId, orgId, fromOrgId, messageTypeId, fromDate, toDate);

    }

    /**
     * The 'findInboxBatches' will return a list of received batches for the logged in user.
     *
     * @param userId The id of the logged in user trying to view received batches
     * @param orgId The id of the organization the user belongs to
     *
     * @return The function will return a list of received batches
     */
    @Transactional
    @SuppressWarnings("UnusedAssignment")
    public List<batchDownloads> findInboxBatches(int userId, int orgId, int fromOrgId, int messageTypeId, Date fromDate, Date toDate) throws Exception {
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
                if (messageTypeId == 0) {
                    messageTypeList.add(configDetails.getMessageTypeId());
                } else {
                    if (messageTypeId == configDetails.getMessageTypeId()) {
                        messageTypeList.add(configDetails.getMessageTypeId());
                    }
                }


                /* Get the list of source orgs */
                Criteria sourceconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                sourceconfigurationQuery.add(Restrictions.eq("id", connectionInfo.getsourceConfigId()));
                configuration sourceconfigDetails = (configuration) sourceconfigurationQuery.uniqueResult();

                /* Add the target org to the target organization list */
                if (fromOrgId == 0) {
                    sourceOrgList.add(sourceconfigDetails.getorgId());
                } else {
                    if (fromOrgId == sourceconfigDetails.getorgId()) {
                        sourceOrgList.add(sourceconfigDetails.getorgId());
                    }
                }
            }
        }

        if (messageTypeList.isEmpty()) {
            messageTypeList.add(0);
        }

        /* Get a list of available batches */
        Criteria batchSummaries = sessionFactory.getCurrentSession().createCriteria(batchDownloadSummary.class);
        batchSummaries.add(Restrictions.or(
                Restrictions.eq("targetOrgId", orgId),
                Restrictions.eq("targetSubOrgId", orgId)
        ));
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
        findBatches.add(Restrictions.eq("transportMethodId", 2));
        findBatches.add(Restrictions.and(
                Restrictions.ne("statusId", 29), /* Submission Processed Errored */
                Restrictions.ne("statusId", 30), /* Target Creation Errored */
                Restrictions.ne("statusId", 32) /* Submission Cancelled */
        ));

        if (!"".equals(fromDate) && fromDate != null) {
            findBatches.add(Restrictions.ge("dateCreated", fromDate));
        }

        if (!"".equals(toDate) && toDate != null) {
            findBatches.add(Restrictions.lt("dateCreated", toDate));
        }

        findBatches.addOrder(Order.desc("dateCreated"));

        return findBatches.list();

    }

    /**
     * The 'getAllBatches' function will return a list of batches for the admin in the processing activities section.
     *
     * @param fromDate
     * @param toDate
     * @return This function will return a list of batch uploads
     * @throws Exception
     */
    @Override
    @Transactional
    public List<batchDownloads> getAllBatches(Date fromDate, Date toDate) throws Exception {

        int firstResult = 0;

        Criteria findBatches = sessionFactory.getCurrentSession().createCriteria(batchDownloads.class);

        if (!"".equals(fromDate)) {
            findBatches.add(Restrictions.ge("dateCreated", fromDate));
        }

        if (!"".equals(toDate)) {
            findBatches.add(Restrictions.lt("dateCreated", toDate));
        }

        findBatches.addOrder(Order.desc("dateCreated"));

        return findBatches.list();

    }

    @Override
    @Transactional
    public boolean searchBatchForHistory(batchDownloads batchDetails, String searchTerm, Date fromDate, Date toDate) throws Exception {

        boolean matched = true;

        String[] terms = searchTerm.split("\\|", -1);
        String[] systemStatus = terms[0].split("\\-", -1);

        String statusCode = systemStatus[0];
        String statusCategory = systemStatus[1];

        String status = terms[1];
        String batchName = terms[2];
        String firstName = terms[3];
        String lastName = terms[4];
        String utBatchName = terms[5];
        String patientId = terms[6];
        String providerId = terms[7];

        if (!"".equals(batchName) && !batchName.equals(batchDetails.getoutputFIleName())) {
            matched = false;
        }

        if (!"".equals(utBatchName) && !utBatchName.equals(batchDetails.getutBatchName())) {
            matched = false;
        }

        if (!batchDetails.getdateCreated().after(fromDate)) {
            matched = false;
        }

        if (!batchDetails.getdateCreated().before(toDate)) {
            matched = false;
        }

        lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batchDetails.getstatusId());

        if (!"".equals(statusCategory) && "batch".equals(statusCategory) && !statusCode.equals(processStatus.getEndUserDisplayCode())) {
            matched = false;
        }


        /* Search message types included in the batch */
        Criteria transactionQuery = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
        transactionQuery.add(Restrictions.eq("batchDLId", batchDetails.getId()));
        List<transactionTarget> transactions = transactionQuery.list();

        /* Loop through the transactions to match patient information */
        for (transactionTarget transaction : transactions) {

            /* Get a the transaction in entry */
            Criteria transactionIn = sessionFactory.getCurrentSession().createCriteria(transactionIn.class);
            transactionIn.add(Restrictions.eq("id", transaction.gettransactionInId()));

            transactionIn transactionInDetails = (transactionIn) transactionIn.uniqueResult();

            lu_ProcessStatus transprocessStatus = sysAdminManager.getProcessStatusById(transactionInDetails.getstatusId());

            if (!"".equals(statusCategory) && "transaction".equals(statusCategory) && !statusCode.equals(transprocessStatus.getEndUserDisplayCode())) {
                matched = false;
            }

            if (!"0".equals(status) && !status.equals(String.valueOf(transactionInDetails.getmessageStatus()))) {
                matched = false;
            }

            Criteria patientQuery = sessionFactory.getCurrentSession().createCriteria(messagePatients.class);
            patientQuery.add(Restrictions.eq("transactionInId", transaction.gettransactionInId()));

            if (!"".equals(firstName)) {
                patientQuery.add(Restrictions.like("firstName", firstName));
            }
            if (!"".equals(lastName)) {
                patientQuery.add(Restrictions.like("lastName", lastName));
            }
            if (!"".equals(patientId)) {
                patientQuery.add(Restrictions.like("sourcePatientId", patientId));
            }

            if (patientQuery.list().isEmpty()) {
                matched = false;
            }

        }

        return matched;
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
     * The 'getBatchDetailsByBatchName' will return a batch by name
     *
     * @param batchName The name of the batch to search form.
     *
     * @return This function will return a batchUpload object
     */
    @Override
    @Transactional
    public batchDownloads getBatchDetailsByBatchName(String batchName) throws Exception {
        Query query = sessionFactory.getCurrentSession().createQuery("from batchDownloads where utBatchName = :batchName");
        query.setParameter("batchName", batchName);

        if (query.list().size() > 1) {
            return null;
        } else {
            return (batchDownloads) query.uniqueResult();
        }

    }

    /**
     * The 'getInboxBatchTransactions' function will return a list of transactions within a batch from the inbox. The list of transactions will only be the ones the passed in user has access to.
     *
     * @param batchId The id of the selected batch
     * @param userId The id of the logged in user
     *
     * @return The function will return a list of transactionIn objects
     */
    @Override
    @Transactional
    public List<transactionTarget> getInboxBatchTransactions(int batchId, int userId) throws Exception {

        List<Integer> transactionTargetList = new ArrayList<Integer>();

        if (userId > 0) {
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

            if (batchDownloadSummaryList.isEmpty()) {
                transactionTargetList.add(0);
            } else {

                for (batchDownloadSummary summary : batchDownloadSummaryList) {
                    transactionTargetList.add(summary.gettransactionTargetId());
                }

            }
        } else {
            /* Get a list of available batches */
            Criteria batchSummaries = sessionFactory.getCurrentSession().createCriteria(batchDownloadSummary.class);
            batchSummaries.add(Restrictions.eq("batchId", batchId));
            List<batchDownloadSummary> batchDownloadSummaryList = batchSummaries.list();

            if (batchDownloadSummaryList.isEmpty()) {
                transactionTargetList.add(0);
            } else {

                for (batchDownloadSummary summary : batchDownloadSummaryList) {
                    transactionTargetList.add(summary.gettransactionTargetId());
                }

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
        try {
            return (transactionTarget) sessionFactory.getCurrentSession().get(transactionTarget.class, transactionId);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("errored at getTransactionDetails " + ex.getMessage());
            return null;
        }
    }

    /**
     * The 'changeDeliveryStatus' function will modify the status of the viewed transaction and its related inbound transaction. The function will also review the current status of all the transactions within the download batch and upload batch to see if the batch is in a final delivered status.
     *
     * @param batchDLId The id of the download batch for the viewed transaction
     * @param batchUploadId The id of the upload batch that is related to the viewed transaction
     * @param transactionTargetId The id of the viewed transaction
     * @param transactionInId The id of the transaction that is related to the viewed transaction
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
        for (transactionTarget transaction : targetTransactions) {
            if (transaction.getstatusId() == 20) {
                totalReceivedTransactions += 1;
            }
        }

        if (totalReceivedTransactions == targetTransactions.size()) {
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
        for (transactionIn transaction : sourceTransactions) {
            if (transaction.getstatusId() == 20) {
                totalSentTransactions += 1;
            }
        }

        if (totalSentTransactions == sourceTransactions.size()) {
            batchULStatusId = 23;
        }

        Query batchUploadStatusUpdate = sessionFactory.getCurrentSession().createSQLQuery("UPDATE batchUploads set statusId = :batchULStatusId where id = :batchUploadId");
        batchUploadStatusUpdate.setParameter("batchULStatusId", batchULStatusId);
        batchUploadStatusUpdate.setParameter("batchUploadId", batchUploadId);

        batchUploadStatusUpdate.executeUpdate();
    }

    /**
     * The 'getInternalStatusCodes' function will query and return the list of active internal status codes that can be set to a message.
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
     * @param transactionDetails The object containing the transaction to update
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
     * @param note The transactionOutNote object that will hold the new note
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
     * The 'getActiveFeedbacReportsByMessageType' function will return an associated feedback report configuration for the organization passed in and for the message type of the viewed transaction.
     *
     * @param messageTypeId The messageType of the viewed transaction
     * @param orgId The organization id of the user viewing the transaction
     *
     * @return This function will return a 0 if no feedback reports are found or the id of the feedback report configuration found.
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

        if (!feedbackReportConfigs.isEmpty()) {
            /* Make sure the feedback report is of an ERG type */
            for (configuration config : feedbackReportConfigs) {

                Criteria configurationTransports = sessionFactory.getCurrentSession().createCriteria(configurationTransport.class);
                configurationTransports.add(Restrictions.eq("configId", config.getId()));

                configurationTransport transportDetails = (configurationTransport) configurationTransports.uniqueResult();

                if (transportDetails.gettransportMethodId() == 2) {
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
     * @param fromPage The page the user is coming from, if from sent items page then we only want to show feedback reports that are fully sent.
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
        if ("sent".equals(fromPage)) {
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
     * The 'getpendingOutPutTransactions' function will return a list of transactions that are in the 'Pending Output' status (id = 19) that are ready to start the output process
     *
     * @param transactionTargetId This will hold a specific transaction Id to process will default to 0 which will find all transactions.
     *
     * @table transactionTarget;
     */
    @Override
    @Transactional
    public List<transactionTarget> getpendingOutPutTransactions(int transactionTargetId) throws Exception {

        Criteria transactionQuery = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
        transactionQuery.add(Restrictions.eq("statusId", 19));

        if (transactionTargetId > 0) {
            transactionQuery.add(Restrictions.eq("id", transactionTargetId));
        }

        List<transactionTarget> transactions = transactionQuery.list();

        return transactions;
    }

    /**
     * The 'processOutPutTransactions' method will find all the configuration form fields set up for the target configuration and retrieve the data from the mapped tables and columns.
     *
     * @transactionTargetId The id of the target transactions
     *
     * @configId The id of the target configuration to retrieve the form fields.
     *
     * @transactionInId The id of the inbound transaction to get the actual data.
     */
    @Override
    @Transactional
    public boolean processOutPutTransactions(int transactionTargetId, int configId, int transactionInId) {

        /* Need to pull all the data out of the appropriate message_ tables for the transaction */
        Criteria formFieldsQuery = sessionFactory.getCurrentSession().createCriteria(configurationFormFields.class);
        formFieldsQuery.add(Restrictions.eq("configId", configId));

        List<configurationFormFields> formFields = formFieldsQuery.list();

        if (!formFields.isEmpty()) {

            String sql;
            sql = "insert into transactionTranslatedOut (transactionTargetId, configId,";
            for (configurationFormFields formField : formFields) {
                if (!formField.getsaveToTableName().equalsIgnoreCase("")) {
                    sql += "f" + formField.getFieldNo() + ",";
                }
            }

            sql = sql.substring(0, sql.length() - 1);

            sql += ") ";

            sql += "VALUES( :transactionTargetId, :configId,";

            String dataSQL;
            for (configurationFormFields formField : formFields) {
                if (!formField.getsaveToTableName().equalsIgnoreCase("")) {

                    int rowNum;
                    int idot = formField.getFieldDesc().indexOf(".");

                    if (idot > 0) {
                        idot += 1;
                        rowNum = 0;
                        try {
                        	rowNum = Integer.parseInt(formField.getFieldDesc().substring(idot)) - 1;
                        } catch (Exception ex) {
                        	transactionInManager.insertProcessingError(28, null, 0, null, null, null, null, false, true, ("Expection number idot form desc, formField " + formField.getFieldDesc() + " " + ex.getLocalizedMessage()), transactionTargetId);
                        	ex.printStackTrace();
                        	return false;
                        }
                    } else {
                        rowNum = 0;
                    }

                    dataSQL = "SELECT " + formField.getsaveToTableCol() + " from " + formField.getsaveToTableName()
                            + " WHERE transactionInId = :transactionInId LIMIT " + rowNum + ",1"; //LIMIT 0,1
                    Query getData = sessionFactory.getCurrentSession().createSQLQuery(dataSQL)
                            .setParameter("transactionInId", transactionInId);

                    /* if no result is found then we need to look at the main transactionTranslatedIn table 
                     for the value only if pass through errors is set
                     */
                    if (getData.uniqueResult() == null) {
                        sql += null + ",";
                    } else {
                        sql += "'" + getData.uniqueResult().toString().replace("'", "''") + "',";
                    }
                }
            }

            //remove last comma
            sql = sql.substring(0, sql.length() - 1);
            sql += ") ";

            Query insertData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                    .setParameter("transactionTargetId", transactionTargetId)
                    .setParameter("configId", configId);
            try {
                insertData.executeUpdate();
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                transactionInManager.insertProcessingError(29, null, 0, null, null, null, null, false, true, ("Expection at insertData - sql " +  sql + " " + ex.getLocalizedMessage()), transactionTargetId);
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * The 'updateTargetBatchStatus' function will update the status of the passed in batch downloadId
     *
     * @param batchDLId The id of the batch download
     * @param statusId The id of the new status
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
     * The 'moveTranslatedRecords' function will copy the translated records found in transactionTranslatedOut table to the transactionOutRecords table for the passed in transactionId.
     *
     * @param transactionTargetId The id of the transaction to copy.
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
                + "f250, f251, f252, f253, f254, f255, F256, F257,F258,F259,F260,F261,F262,F263,F264,F265,F266,F267,F268,F269,F270,F271,F272,F273,F274,F275,F276,F277,F278,F279,F280,F281,F282,F283,F284,F285,F286,F287,F288,F289,F290,F291,F292,F293,F294,F295,F296,F297,F298,F299,F300,F301,F302,F303,F304,F305,F306,F307,F308,F309,F310,F311,F312,F313,F314,F315,F316,F317,F318,F319,F320,F321,F322,F323,F324,F325,F326,F327,F328,F329,F330,F331,F332,F333,F334,F335,F336,F337,F338,F339,F340,F341,F342,F343,F344,F345,F346,F347,F348,F349,F350,F351,F352,F353,F354,F355,F356,F357,F358,F359,F360,F361,F362,F363,F364,F365,F366,F367,F368,F369,F370,F371,F372,F373,F374,F375,F376,F377,F378,F379,F380,F381,F382,F383,F384,F385,F386,F387,F388,F389,F390,F391,F392,F393,F394,F395,F396,F397,F398,F399,F400,F401,F402,F403,F404,F405,F406,F407,F408,F409,F410,F411,F412,F413,F414,F415,F416,F417,F418,F419,F420,F421,F422,F423,F424,F425,F426,F427,F428,F429,F430,F431,F432,F433,F434,F435,F436,F437,F438,F439,F440,F441,F442,F443,F444,F445,F446,F447,F448,F449,F450,F451,F452,F453,F454,F455,F456,F457,F458,F459,F460,F461,F462,F463,F464,F465,F466,F467,F468,F469,F470,F471,F472,F473,F474,F475,F476,F477,F478,F479,F480,F481,F482,F483,F484,F485,F486,F487,F488,F489,F490,F491,F492,F493,F494,F495,F496,F497,F498,F499,F500) "
                + "SELECT transactionTargetId, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23, f24, f25, f26, f27, f28, f29, f30, f31,"
                + "f32, f33, f34, f35, f36, f37, f38, f39, f40, f41, f42, f43, f44, f45, f46, f47, f48, f49, f50, f51, f52, f53, f54, f55, f56, f57, f58, f59, f60, f61, f62, f63, f64,"
                + "f65, f66, f67, f68, f69, f70, f71, f72, f73, f74, f75, f76, f77, f78, f79, f80, f81, f82, f83, f84, f85, f86, f87, f88, f89, f90, f91, f92, f93, f94, f95, f96, f97, f98,"
                + "f99, f100, f101, f102, f103, f104, f105, f106, f107, f108, f109, f110, f111, f112, f113, f114, f115, f116, f117, f118, f119, f120, f121, f122, f123, f124, f125, f126, f127, f128, f129,"
                + "f130, f131, f132, f133, f134, f135, f136, f137, f138, f139, f140, f141, f142, f143, f144, f145, f146, f147, f148, f149, f150, f151, f152, f153, f154, f155, f156, f157, f158, f159,"
                + "f160, f161, f162, f163, f164, f165, f166, f167, f168, f169, f170, f171, f172, f173, f174, f175, f176, f177, f178, f179, f180, f181, f182, f183, f184, f185, f186, f187, f188, f189,"
                + "f190, f191, f192, f193, f194, f195, f196, f197, f198, f199, f200, f201, f202, f203, f204, f205, f206, f207, f208, f209, f210, f211, f212, f213, f214, f215, f216, f217, f218, f219,"
                + "f220, f221, f222, f223, f224, f225, f226, f227, f228, f229, f230, f231, f232, f233, f234, f235, f236, f237, f238, f239, f240, f241, f242, f243, f244, f245, f246, f247, f248, f249,"
                + "f250, f251, f252, f253, f254, f255, F256, F257,F258,F259,F260,F261,F262,F263,F264,F265,F266,F267,F268,F269,F270,F271,F272,F273,F274,F275,F276,F277,F278,F279,F280,F281,F282,F283,F284,F285,F286,F287,F288,F289,F290,F291,F292,F293,F294,F295,F296,F297,F298,F299,F300,F301,F302,F303,F304,F305,F306,F307,F308,F309,F310,F311,F312,F313,F314,F315,F316,F317,F318,F319,F320,F321,F322,F323,F324,F325,F326,F327,F328,F329,F330,F331,F332,F333,F334,F335,F336,F337,F338,F339,F340,F341,F342,F343,F344,F345,F346,F347,F348,F349,F350,F351,F352,F353,F354,F355,F356,F357,F358,F359,F360,F361,F362,F363,F364,F365,F366,F367,F368,F369,F370,F371,F372,F373,F374,F375,F376,F377,F378,F379,F380,F381,F382,F383,F384,F385,F386,F387,F388,F389,F390,F391,F392,F393,F394,F395,F396,F397,F398,F399,F400,F401,F402,F403,F404,F405,F406,F407,F408,F409,F410,F411,F412,F413,F414,F415,F416,F417,F418,F419,F420,F421,F422,F423,F424,F425,F426,F427,F428,F429,F430,F431,F432,F433,F434,F435,F436,F437,F438,F439,F440,F441,F442,F443,F444,F445,F446,F447,F448,F449,F450,F451,F452,F453,F454,F455,F456,F457,F458,F459,F460,F461,F462,F463,F464,F465,F466,F467,F468,F469,F470,F471,F472,F473,F474,F475,F476,F477,F478,F479,F480,F481,F482,F483,F484,F485,F486,F487,F488,F489,F490,F491,F492,F493,F494,F495,F496,F497,F498,F499,F500 "
                + "FROM transactionTranslatedOut where transactionTargetId = :transactionTargetId");
        query.setParameter("transactionTargetId", transactionTargetId);
        query.executeUpdate();
    }

    /**
     * The 'getLoadedOutBoundTransactions' function will look to see what translated transactions are loaded and ready to be pulled out into a download batch
     *
     * @param configId The id of the configuration to check for loaded transactions for
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
     * The 'updateTransactionTargetBatchDLId' function will update the transactionTarget with the generated batch downloadId.
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
     * The 'updateBatchOutputFileName' function will update the outputFileName with the finalized generated file name. This will contain the appropriate extension.
     *
     * @param batchId The id of the batch to update
     * @param fileName The new file name to update.
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
     * @param configId The id of the configuration to find out how many fields it has
     *
     * @return This function will return the max field number.
     */
    @Override
    @Transactional
    public int getMaxFieldNo(int configId) throws Exception {

        String sql = "select max(fieldNo) as maxFieldNo from configurationFormFields where configId = :configId";

        /* Need to make sure no duplicates */
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("maxFieldNo", StandardBasicTypes.INTEGER);
        query.setParameter("configId", configId);

        return (Integer) query.list().get(0);

    }

    /**
     * The 'getdownloadableBatches' will return a list of received batches for the logged in user that are ready to be downloaded
     *
     * @param userId The id of the logged in user trying to view downloadable batches
     * @param orgId The id of the organization the user belongs to
     *
     * @return The function will return a list of downloadable batches
     */
    @Override
    @Transactional
    @SuppressWarnings("UnusedAssignment")
    public List<batchDownloads> getdownloadableBatches(int userId, int orgId, Date fromDate, Date toDate) throws Exception {
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

                if (transportDetails.gettransportMethodId() == 1) {
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

        if (messageTypeList.isEmpty()) {
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
        findBatches.add(Restrictions.eq("transportMethodId", 1));
        findBatches.add(Restrictions.or(
                Restrictions.eq("statusId", 22),
                Restrictions.eq("statusId", 23),
                Restrictions.eq("statusId", 28)
        ));

        if (!"".equals(fromDate)) {
            findBatches.add(Restrictions.ge("dateCreated", fromDate));
        }

        if (!"".equals(toDate)) {
            findBatches.add(Restrictions.lt("dateCreated", toDate));
        }

        findBatches.addOrder(Order.desc("dateCreated"));

        return findBatches.list();

    }

    /**
     * The 'updateLastDownloaded' function will update the last downloaded field for the passed in batch
     *
     * @param batchId The id of the batch to update.
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
     * The 'getScheduledConfigurations' function will return a list of configurations that have a Daily, Weekly or Monthly schedule setting
     */
    @Override
    @Transactional
    public List<configurationSchedules> getScheduledConfigurations() {

        Query query = sessionFactory.getCurrentSession().createQuery("from configurationSchedules where type = 2 or type = 3 or type = 4");

        List<configurationSchedules> scheduledConfigList = query.list();
        return scheduledConfigList;

    }

    /**
     * The 'updateBatchStatus' function will update the status of the passed in batch
     *
     * @param batchId The id of the batch to update
     * @param statusId The status to update the batch to
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
     * The 'saveOutputRunLog' function will insert the latest run log for the batch.
     *
     * @param log The output run log to save.
     */
    @Override
    @Transactional
    public void saveOutputRunLog(targetOutputRunLogs log) throws Exception {
        sessionFactory.getCurrentSession().save(log);
    }

    /**
     * The 'targetOutputRunLogs' function will return the latest output run log for the passed in configuration Id
     *
     * @param configId = The configuration to find the latest log.
     *
     * @return This function will return the latest log
     */
    @Override
    @Transactional
    public List<targetOutputRunLogs> getLatestRunLog(int configId) throws Exception {

        Criteria latestLogQuery = sessionFactory.getCurrentSession().createCriteria(targetOutputRunLogs.class);
        latestLogQuery.add(Restrictions.eq("configId", configId));
        latestLogQuery.addOrder(Order.desc("lastRunTime"));

        return latestLogQuery.list();

    }

    /**
     * The 'getTransactionsByBatchDLId' will get a list of transactions by the batch download Id.
     *
     * @param batchDLId The batch Download Id to search with.
     *
     * @return This function will return a list of transaction targets.
     */
    @Override
    @Transactional
    public List<transactionTarget> getTransactionsByBatchDLId(int batchDLId) {

        Criteria targets = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
        targets.add(Restrictions.eq("batchDLId", batchDLId));

        return targets.list();
    }

    /**
     * The 'cancelMessageTransaction' will cancel both the transactionIn and transactionTarget entries.
     *
     * @param transactionId The id of the transaction we want to cancel.
     *
     * @return This function will not return anything.
     */
    @Override
    @Transactional
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

    @Override
    @Transactional
    public void clearTransactionTranslatedOut(Integer transactionTargetId) {
        String sql = "delete from transactionTranslatedOut where transactionTargetId = :transactionTargetId";

        Query deleteData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("transactionTargetId", transactionTargetId);

        try {
            deleteData.executeUpdate();
        } catch (Exception ex) {
            System.err.println("clearTransactionTranslatedOut " + ex.getCause());
        }

    }

    @Override
    @Transactional
    public void clearTransactionOutRecords(Integer transactionTargetId) {
        String sql = "delete from transactionOutRecords where transactionTargetId = :transactionTargetId";

        Query deleteData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("transactionTargetId", transactionTargetId);

        try {
            deleteData.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("clearTransactionOutRecords " + ex.getCause());
        }

    }

    @Override
    @Transactional
    public void clearTransactionOutErrors(Integer transactionTargetId) {
        String sql = "delete from transactionOutErrors where transactionTargetId = :transactionTargetId";

        Query deleteData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("transactionTargetId", transactionTargetId);

        try {
            deleteData.executeUpdate();
        } catch (Exception ex) {
            System.err.println("clearTransactionOutErrors " + ex.getCause());
        }

    }

    /**
     * The 'getDownloadSummaryDetails' method will return the details for the batch summary for the passed in transactionTargetId.
     *
     * @param transactionTargetId The id of the transaction to search the summary for
     *
     * @return This method will return the batchDownloadSummary object
     */
    @Override
    @Transactional
    public batchDownloadSummary getDownloadSummaryDetails(int transactionTargetId) {

        /* Get a list of available batches */
        Criteria batchSummaries = sessionFactory.getCurrentSession().createCriteria(batchDownloadSummary.class);
        batchSummaries.add(Restrictions.eq("transactionTargetId", transactionTargetId));

        if (batchSummaries.list().size() > 1) {
            return (batchDownloadSummary) batchSummaries.list().get(0);
        } else {
            return (batchDownloadSummary) batchSummaries.uniqueResult();
        }

    }

    /**
     * The 'getTransactionsToProcess' will return a llist of transactions that need to be processed.
     *
     * @return This methid will return a list
     */
    @Override
    @Transactional
    public List getTransactionsToProcess() throws Exception {

        int firstResult = 0;

        String SQL = "SELECT c.orgId, count(tt.id) as total\n"
                + "FROM transactionTarget tt inner join configurations c on c.id = tt.configId\n"
                + "where tt.batchDLId = 0 and tt.statusID = 9\n"
                + "Group by c.orgId\n"
                + "Order by total desc";

        Query transactions = sessionFactory.getCurrentSession().createSQLQuery(SQL);

        return transactions.list();

    }

    /**
     * The 'getTransactionsToProcessByMessageType' will return a list of transactions that need to be processed grouped by message type.
     *
     * @return This method will return a list
     */
    @Override
    @Transactional
    public List getTransactionsToProcessByMessageType(int orgId) throws Exception {

        int firstResult = 0;

        String SQL = "SELECT c.orgId, m.name, c.messageTypeId, count(tt.id) as total\n"
                + "FROM transactionTarget tt inner join configurations c on c.id = tt.configId inner join messagetypes m on m.id = c.messageTypeId\n"
                + "where tt.batchDLId = 0 and tt.statusID = 9 and c.orgId = " + orgId + "\n"
                + "Group by c.messageTypeId\n"
                + "Order by total desc";

        Query transactions = sessionFactory.getCurrentSession().createSQLQuery(SQL);

        return transactions.list();

    }

    /**
     * The 'getPendingTransactions' method will return all pending target transactions based on the org and message type passed in.
     *
     * @param orgId The id of the organzition to return pending transactions
     * @param messageType The id of the message type to return pending transactions
     *
     * @return This function will return a list of transactionTargets
     */
    @Override
    @Transactional
    public List<transactionTarget> getPendingDeliveryTransactions(int orgId, int messageType, Date fromDate, Date toDate) throws Exception {

        List<Integer> configIds = new ArrayList<Integer>();

        Criteria listConfigs = sessionFactory.getCurrentSession().createCriteria(configuration.class);
        listConfigs.add(Restrictions.eq("orgId", orgId));
        listConfigs.add(Restrictions.eq("messageTypeId", messageType));

        List<configuration> configs = listConfigs.list();

        for (configuration config : configs) {

            if (!configIds.contains(config.getId())) {
                configIds.add(config.getId());
            }
        }

        Criteria transactions = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
        transactions.add(Restrictions.in("configId", configIds));
        transactions.add(Restrictions.eq("statusId", 9));
        transactions.add(Restrictions.eq("batchDLId", 0));

        if (!"".equals(fromDate)) {
            transactions.add(Restrictions.ge("dateCreated", fromDate));
        }

        if (!"".equals(toDate)) {
            transactions.add(Restrictions.lt("dateCreated", toDate));
        }

        return transactions.list();
    }

    @Override
    @Transactional
    public void doNotProcessTransaction(int transactionId) throws Exception {

        Criteria transactionDetails = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
        transactionDetails.add(Restrictions.eq("id", transactionId));

        transactionTarget targetDetails = (transactionTarget) transactionDetails.uniqueResult();

        /* Update the transaction Target status to DNP (Do Not Process) */
        targetDetails.setstatusId(34);
        sessionFactory.getCurrentSession().update(targetDetails);

        /* Update the transaction in status to DNP (Do Not Process) */
        String sql = "update transactionIn set statusId = 34 where id = :transactionInId";

        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("transactionInId", targetDetails.gettransactionInId());
        updateData.executeUpdate();

        /* Need to check to see if this is the only transaction for the uploaded batch */
        String updateBatchSQL = "update batchUploads set statusId = 21 where id = :batchId and 0 in (select count(id) as total from transactionIn where batchId = :batchId and statusId != 34)";

        Query updateBatchStatus = sessionFactory.getCurrentSession().createSQLQuery(updateBatchSQL);
        updateBatchStatus.setParameter("batchId", targetDetails.getbatchUploadId());
        updateBatchStatus.executeUpdate();

    }

    /**
     * The 'getAllransactionsToProcessByMessageType' will return a list of transactions that need to be processed by the passed in organizationId and message type id.
     *
     * @return This method will return a list
     */
    @Override
    @Transactional
    public List getAllransactionsToProcessByMessageType(int orgId, int messageTypeId) throws Exception {

        int firstResult = 0;

        String SQL = "SELECT tt.id, c.id as configId\n"
                + "FROM transactionTarget tt inner join configurations c on c.id = tt.configId \n"
                + "where tt.batchDLId = 0 and tt.statusID = 9 and c.orgId = " + orgId;

        if (messageTypeId > 0) {
            SQL += " and c.messageTypeId = " + messageTypeId;
        }

        Query transactions = sessionFactory.getCurrentSession().createSQLQuery(SQL);

        return transactions.list();

    }

    /**
     * The 'getBatchesBySentOrg' will search the batchDownloadSummary table for batches sent by the passed in orgId to the passed in orgId for the passed in messagetypeId
     *
     * @param srcOrgId The orgId who sent the batch
     * @param tgtOrgId The orgId for the user who is logged in
     * @param messageTypeId The id of the message Type that was selected
     *
     * @return This function will return a list of batches found matching the criteria passed in.
     */
    @Override
    @Transactional
    public List<batchDownloadSummary> getBatchesBySentOrg(int srcorgId, int tgtOrgId, int messageTypeId) throws Exception {

        Criteria batchSummaries = sessionFactory.getCurrentSession().createCriteria(batchDownloadSummary.class);
        batchSummaries.add(Restrictions.eq("sourceOrgId", srcorgId));
        batchSummaries.add(Restrictions.eq("targetOrgId", tgtOrgId));
        batchSummaries.add(Restrictions.eq("messageTypeId", messageTypeId));

        return batchSummaries.list();

    }

    /**
     * The 'getuploadBatchesByConfigAndSource' method will return the list of uploaded messages for the passed in configId and passed in target orgId
     *
     * @param configId The configuration Id to find uploaded messages
     *
     * @param orgId The organization Id that the message was sent to
     *
     * @return This method will return a list of uploaded batches
     */
    @Override
    @Transactional
    public List<batchDownloadSummary> getuploadBatchesByConfigAndSource(Integer configId, Integer orgId, Integer userOrgId) {

        Criteria batchSummaries = sessionFactory.getCurrentSession().createCriteria(batchDownloadSummary.class);
        batchSummaries.add(Restrictions.eq("targetConfigId", configId));
        batchSummaries.add(Restrictions.eq("sourceOrgId", orgId));
        batchSummaries.add(Restrictions.or(
                Restrictions.eq("targetOrgId", userOrgId),
                Restrictions.eq("targetSubOrgId", userOrgId)
        ));

        return batchSummaries.list();

    }

    /**
     * The 'updateTransactionTargetStatusOutBound' function will update the transactionTarget entries when the created batch has been sent.
     *
     * @param batchId The id of the download batch - default to 0
     * @param transactionId The id of the specific transaction to update
     * @param fromStatusId
     * @param toStatusId The status we want to change to
     */
    @Override
    @Transactional
    public void updateTransactionTargetStatusOutBound(Integer batchDLId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception {
        try {
            String sql = "update transactionTarget "
                    + " set statusId = :toStatusId, "
                    + "statusTime = CURRENT_TIMESTAMP";

            if (transactionId > 0) {
                sql += " where id = :transactionId ";
            } else {
                sql += " where batchDLId = :batchDLId ";
            }

            if (fromStatusId != 0) {
                sql = sql + " and statusId = :fromStatusId";
            }
            Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                    .setParameter("toStatusId", toStatusId);
            if (transactionId > 0) {
                updateData.setParameter("transactionId", transactionId);
            } else {
                updateData.setParameter("batchDLId", batchDLId);
            }

            if (fromStatusId != 0) {
                updateData.setParameter("fromStatusId", fromStatusId);
            }

            updateData.executeUpdate();
        } catch (Exception ex) {
            System.err.println("updateTransactionTargetStatusOutBound " + ex.getCause());
        }

    }

    @Override
    @Transactional
    public List<String> getWSSenderFromBatchDLId(List<Integer> batchDLIds) throws Exception {
        String SQL = "select senderEmail from batchUploads where id in "
                + " (select batchuploadId from transactiontarget where id in "
                + " (select transactionTargetId from transactionIN where id in "
                + " (select transactionInId from transactiontarget where batchDLId = :batchDLIds)))";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(SQL).setParameterList("batchDLIds", batchDLIds);

        List<String> emails = query.list();

        return emails;

    }

    /**
     * The 'getSentTransactions' method will return all pending target transactions based on the org and message type passed in.
     *
     *
     * @return This function will return a list of transactionTargets
     */
    @Override
    @Transactional
    public List<transactionTarget> getSentTransactions(Date fromDate, Date toDate) throws Exception {
        
        /* Get a list of available batches */
       /* Criteria transactionIn = sessionFactory.getCurrentSession().createCriteria(transactionIn.class);
        transactionIn.add(Restrictions.eq("transactionTargetId", 0));
        List<transactionIn> transactionInList = transactionIn.list();

        List<Integer> transactionInIds = new ArrayList<Integer>();

        if (transactionInList.isEmpty()) {
            transactionInIds.add(0);
        } else {

            for (transactionIn transaction : transactionInList) {
                transactionInIds.add(transaction.getId());
            }
        }*/
        
        
        
        Criteria transactions = sessionFactory.getCurrentSession().createCriteria(transactionTarget.class);
        transactions.add(
                Restrictions.or(
                        Restrictions.eq("statusId", 20),
                        Restrictions.eq("statusId", 18)
                )
        );
        transactions.add(Restrictions.ge("batchDLId", 0));
        //transactions.add(Restrictions.in("transactionInId", transactionInIds));

        transactions.add(Restrictions.ge("dateCreated", fromDate));
        transactions.add(Restrictions.lt("dateCreated", toDate));
       

        return transactions.list();
    }
    
}
