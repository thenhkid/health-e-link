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
import com.ut.dph.model.messageType;
import com.ut.dph.model.transactionIn;
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
    
}
