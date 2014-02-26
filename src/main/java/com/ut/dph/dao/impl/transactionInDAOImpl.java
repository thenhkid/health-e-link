/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.dph.dao.impl;

import com.ut.dph.dao.transactionInDAO;
import com.ut.dph.model.CrosswalkData;
import com.ut.dph.model.Macros;
import com.ut.dph.model.Organization;
import com.ut.dph.model.User;
import com.ut.dph.model.batchUploadSummary;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationConnectionSenders;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.transactionAttachment;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import com.ut.dph.model.transactionRecords;
import com.ut.dph.model.transactionTarget;
import com.ut.dph.model.custom.ConfigForInsert;
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.model.messageType;
import com.ut.dph.service.sysAdminManager;
import com.ut.dph.service.userManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author chadmccue
 */
@Service
public class transactionInDAOImpl implements transactionInDAO {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private sysAdminManager sysAdminManager;
    
    @Autowired
    private userManager usermanager;
   	
    private String schemaName = "universalTranslator";

    private int transRELId = 12;

    /**
     * The 'getFieldValue' function will return the value saved for the passed in tableName, tableCol and idValue.
     *
     * @param	tableName	The name of the table to query
     * @param tableCol The column name of to return
     * @param idValue The id value of the row to search
     *
     * @return The function will return a String
     */
    @Override
    public String getFieldValue(String tableName, String tableCol, int idValue) {

        String sql = ("select " + tableCol + " from " + tableName + " where id = :id");

        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        query.setParameter("id", idValue);

        String tableValue = (String) query.uniqueResult();

        return tableValue;

    }

    /**
     * The 'getFieldSelectOptions' function will return a list of values to populate the field select box.
     *
     * @param fieldId The fieldId to search on
     * @param configId The configuration Id to search on
     *
     * @return The function will return a list of select box options
     */
    @Override
    public List<fieldSelectOptions> getFieldSelectOptions(int fieldId, int configId) {

        List<fieldSelectOptions> fieldSelectOptions = new ArrayList<fieldSelectOptions>();

        Query findCrosswalks = sessionFactory.getCurrentSession().createSQLQuery("SELECT crosswalkId, id FROM configurationDataTranslations where configId = :configId and fieldId = :fieldId");
        findCrosswalks.setParameter("configId", configId);
        findCrosswalks.setParameter("fieldId", fieldId);
        List crosswalks = findCrosswalks.list();

        Iterator it = crosswalks.iterator();
        int crosswalkId;
        String optionDesc;
        String optionValue;
        fieldSelectOptions fieldOptions = null;

        while (it.hasNext()) {
            Object row[] = (Object[]) it.next();
            crosswalkId = (Integer) row[0];

            Query crosswalkData = sessionFactory.getCurrentSession().createSQLQuery("SELECT sourceValue, descValue FROM rel_crosswalkData where crosswalkId = :crosswalkId");
            crosswalkData.setParameter("crosswalkId", crosswalkId);
            List crosswalkDataList = crosswalkData.list();

            Iterator cwDataIt = crosswalkDataList.iterator();
            while (cwDataIt.hasNext()) {
                Object cwDatarow[] = (Object[]) cwDataIt.next();
                optionDesc = (String) cwDatarow[1];
                optionValue = (String) cwDatarow[0];

                fieldOptions = new fieldSelectOptions();
                fieldOptions.setoptionDesc(optionDesc);
                fieldOptions.setoptionValue(optionValue);
                fieldSelectOptions.add(fieldOptions);
            }

        }

        return fieldSelectOptions;
    }

    /**
     * The 'submitBatchUpload' function will submit the new batch.
     *
     * @param batchUpload The object that will hold the new batch info
     *
     * @table batchUploads
     *
     * @return This function returns the batchId for the newly inserted batch
     */
    @Override
    @Transactional
    public Integer submitBatchUpload(batchUploads batchUpload) throws Exception {

        Integer batchId = null;

        batchId = (Integer) sessionFactory.getCurrentSession().save(batchUpload);

        return batchId;

    }

    /**
     * The 'submitBatchUploadSummary' function will submit an entry that will contain specific information for transactions within the submitted batch. This will be used when trying to find out which batches a user has access to when logged into the ERG.
     *
     * @param summary The object that will hold the batch summary information
     *
     * @table batchUploadSummary
     *
     * @return This function does not return anything.
     */
    @Override
    @Transactional
    public void submitBatchUploadSummary(batchUploadSummary summary) throws Exception {
        sessionFactory.getCurrentSession().save(summary);
    }

    /**
     * The 'submitBatchUploadChanges' function will submit the batch changes.
     *
     * @param batchUpload The object that will hold the new batch info
     *
     * @table batchUploads
     *
     * @return This function does not return anything
     */
    @Override
    @Transactional
    public void submitBatchUploadChanges(batchUploads batchUpload) throws Exception {
        sessionFactory.getCurrentSession().update(batchUpload);
    }

    /**
     * The 'submitTransactionIn' function will submit the new transaction for the batch.
     *
     * @param transactionIn The object that will hold the new transaction info
     *
     * @table transactionIn
     *
     * @return This function returns the transactioInId for the newly inserted transaction
     */
    @Override
    @Transactional
    public Integer submitTransactionIn(transactionIn transactionIn) throws Exception {
        Integer transactioInId = null;

        transactioInId = (Integer) sessionFactory.getCurrentSession().save(transactionIn);

        return transactioInId;
    }

    /**
     * The 'submitTransactionInChanges' function will submit the transaction changes for the batch.
     *
     * @param transactionIn The object that will hold the new transaction info
     *
     * @table transactionIn
     *
     * @return This function does not return anything
     */
    @Override
    @Transactional
    public void submitTransactionInChanges(transactionIn transactionIn) throws Exception {
        sessionFactory.getCurrentSession().update(transactionIn);
    }

    /**
     * The 'submitTransactionInRecords' function will submit the transaction records
     *
     * @param records The object that will hold the transaction record info
     *
     * @table transactionInRecords
     *
     * @return This function will return the generated transaction record id
     */
    @Override
    @Transactional
    public Integer submitTransactionInRecords(transactionInRecords records) throws Exception {
        Integer transactioInRecordId = null;

        transactioInRecordId = (Integer) sessionFactory.getCurrentSession().save(records);

        return transactioInRecordId;
    }

    /**
     * The 'submitTransactionInRecordsUpdates' function will submit the transaction record changes for the batch
     *
     * @param records The object that will hold the transaction record info
     *
     * @table transactionInRecords
     *
     * @return This function does not return anything
     */
    @Override
    @Transactional
    public void submitTransactionInRecordsUpdates(transactionInRecords records) throws Exception {
        sessionFactory.getCurrentSession().update(records);
    }

    /**
     * The 'submitTransactionTranslatedInRecords' function will copy the records from the transactionInRecords table to the transactionTranslatedIn table so the processing can begin on the field values.
     *
     * @param transactionRecordId The id of the inserted record in the transactionInRecords table
     *
     * @return This function does not return anything.
     */
    @Override
    @Transactional
    public void submitTransactionTranslatedInRecords(int transactionInId, int transactionRecordId, int configId) throws Exception {

        /* Always clear this table out for the passed in transactionId */
        Query clearRecords = sessionFactory.getCurrentSession().createSQLQuery("DELETE from transactionTranslatedIn where transactionInId = :transactionInId");
        clearRecords.setParameter("transactionInId", transactionInId);
        clearRecords.executeUpdate();

        Query query = sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO transactionTranslatedIn ("
                + "transactionInId, configId, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23, f24, f25, f26, f27, f28, f29, f30, f31,"
                + "f32, f33, f34, f35, f36, f37, f38, f39, f40, f41, f42, f43, f44, f45, f46, f47, f48, f49, f50, f51, f52, f53, f54, f55, f56, f57, f58, f59, f60, f61, f62, f63, f64,"
                + "f65, f66, f67, f68, f69, f70, f71, f72, f73, f74, f75, f76, f77, f78, f79, f80, f81, f82, f83, f84, f85, f86, f87, f88, f89, f90, f91, f92, f93, f94, f95, f96, f97, f98,"
                + "f99, f100, f101, f102, f103, f104, f105, f106, f107, f108, f109, f110, f111, f112, f113, f114, f115, f116, f117, f118, f119, f120, f121, f122, f123, f124, f125, f126, f127, f128, f129,"
                + "f130, f131, f132, f133, f134, f135, f136, f137, f138, f139, f140, f141, f142, f143, f144, f145, f146, f147, f148, f149, f150, f151, f152, f153, f154, f155, f156, f157, f158, f159,"
                + "f160, f161, f162, f163, f164, f165, f166, f167, f168, f169, f170, f171, f172, f173, f174, f175, f176, f177, f178, f179, f180, f181, f182, f183, f184, f185, f186, f187, f188, f189,"
                + "f190, f191, f192, f193, f194, f195, f196, f197, f198, f199, f200, f201, f202, f203, f204, f205, f206, f207, f208, f209, f210, f211, f212, f213, f214, f215, f216, f217, f218, f219,"
                + "f220, f221, f222, f223, f224, f225, f226, f227, f228, f229, f230, f231, f232, f233, f234, f235, f236, f237, f238, f239, f240, f241, f242, f243, f244, f245, f246, f247, f248, f249,"
                + "f250, f251, f252, f253, f254, f255) "
                + "SELECT transactionInId, :configId, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23, f24, f25, f26, f27, f28, f29, f30, f31,"
                + "f32, f33, f34, f35, f36, f37, f38, f39, f40, f41, f42, f43, f44, f45, f46, f47, f48, f49, f50, f51, f52, f53, f54, f55, f56, f57, f58, f59, f60, f61, f62, f63, f64,"
                + "f65, f66, f67, f68, f69, f70, f71, f72, f73, f74, f75, f76, f77, f78, f79, f80, f81, f82, f83, f84, f85, f86, f87, f88, f89, f90, f91, f92, f93, f94, f95, f96, f97, f98,"
                + "f99, f100, f101, f102, f103, f104, f105, f106, f107, f108, f109, f110, f111, f112, f113, f114, f115, f116, f117, f118, f119, f120, f121, f122, f123, f124, f125, f126, f127, f128, f129,"
                + "f130, f131, f132, f133, f134, f135, f136, f137, f138, f139, f140, f141, f142, f143, f144, f145, f146, f147, f148, f149, f150, f151, f152, f153, f154, f155, f156, f157, f158, f159,"
                + "f160, f161, f162, f163, f164, f165, f166, f167, f168, f169, f170, f171, f172, f173, f174, f175, f176, f177, f178, f179, f180, f181, f182, f183, f184, f185, f186, f187, f188, f189,"
                + "f190, f191, f192, f193, f194, f195, f196, f197, f198, f199, f200, f201, f202, f203, f204, f205, f206, f207, f208, f209, f210, f211, f212, f213, f214, f215, f216, f217, f218, f219,"
                + "f220, f221, f222, f223, f224, f225, f226, f227, f228, f229, f230, f231, f232, f233, f234, f235, f236, f237, f238, f239, f240, f241, f242, f243, f244, f245, f246, f247, f248, f249,"
                + "f250, f251, f252, f253, f254, f255 "
                + "FROM transactionInRecords where id = :id");
        query.setParameter("id", transactionRecordId);
        query.setParameter("configId", configId);
        query.executeUpdate();
    }

    /**
     * The 'getpendingBatches' function will return a list of batches that have a status of "Error" or "Release Pending". The batches returned will only be the ones the current user has access to viewing.
     *
     * @param userId The id of the logged in user trying to view pending batches
     * @param orgId The id of the organization the user belongs to
     *
     * @return The function will return a list of pending batches
     */
    @Override
    @Transactional
    @SuppressWarnings("UnusedAssignment")
    public List<batchUploads> getpendingBatches(int userId, int orgId, String searchTerm, Date fromDate, Date toDate, int page, int maxResults) throws Exception {

        int firstResult = 0;

        /* Get a list of connections the user has access to */
        Criteria connections = sessionFactory.getCurrentSession().createCriteria(configurationConnectionSenders.class);
        connections.add(Restrictions.eq("userId", userId));
        List<configurationConnectionSenders> userConnections = connections.list();

        List<Integer> messageTypeList = new ArrayList<Integer>();
        List<Integer> targetOrgList = new ArrayList<Integer>();

        if (userConnections.isEmpty()) {
            messageTypeList.add(0);
            targetOrgList.add(0);
        } else {

            for (configurationConnectionSenders userConnection : userConnections) {
                Criteria connection = sessionFactory.getCurrentSession().createCriteria(configurationConnection.class);
                connection.add(Restrictions.eq("id", userConnection.getconnectionId()));

                configurationConnection connectionInfo = (configurationConnection) connection.uniqueResult();

                /* Get the message type for the configuration */
                Criteria sourceconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                sourceconfigurationQuery.add(Restrictions.eq("id", connectionInfo.getsourceConfigId()));

                configuration configDetails = (configuration) sourceconfigurationQuery.uniqueResult();

                /* Add the message type to the message type list */
                messageTypeList.add(configDetails.getMessageTypeId());

                /* Get the list of target orgs */
                Criteria targetconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                targetconfigurationQuery.add(Restrictions.eq("id", connectionInfo.gettargetConfigId()));
                configuration targetconfigDetails = (configuration) targetconfigurationQuery.uniqueResult();

                /* Add the target org to the target organization list */
                targetOrgList.add(targetconfigDetails.getorgId());
            }
        }

        /* Get a list of available batches */
        Criteria batchSummaries = sessionFactory.getCurrentSession().createCriteria(batchUploadSummary.class);
        batchSummaries.add(Restrictions.eq("sourceOrgId", orgId));
        batchSummaries.add(Restrictions.in("messageTypeId", messageTypeList));
        batchSummaries.add(Restrictions.in("targetOrgId", targetOrgList));
        List<batchUploadSummary> batchUploadSummaryList = batchSummaries.list();

        List<Integer> batchIdList = new ArrayList<Integer>();

        if (batchUploadSummaryList.isEmpty()) {
            batchIdList.add(0);
        } else {

            for (batchUploadSummary summary : batchUploadSummaryList) {
                batchIdList.add(summary.getbatchId());
            }

        }

        Criteria findBatches = sessionFactory.getCurrentSession().createCriteria(batchUploads.class);
        findBatches.add(Restrictions.in("id", batchIdList));
        findBatches.add(Restrictions.or(
                Restrictions.eq("statusId", 5),
                Restrictions.eq("statusId", 6),
                Restrictions.eq("statusId", 7),
                Restrictions.eq("statusId", 8)
        )
        );
        
         if(!"".equals(fromDate) && fromDate != null) {
            findBatches.add(Restrictions.ge("dateSubmitted", fromDate));
        }  
        
        if(!"".equals(toDate) && toDate != null) {
            findBatches.add(Restrictions.lt("dateSubmitted", toDate));
        } 
        
        findBatches.addOrder(Order.desc("dateSubmitted"));
        
         /* If a search term is entered conduct a search */
        if(!"".equals(searchTerm)) {
            
            List<batchUploads> batches = findBatches.list();
            
            List<Integer> batchFoundIdList = findBatches(batches, searchTerm);
            
            if (batchFoundIdList.isEmpty()) {
                batchFoundIdList.add(0);
            }
            
            Criteria foundBatches = sessionFactory.getCurrentSession().createCriteria(batchUploads.class);
            foundBatches.add(Restrictions.in("id", batchFoundIdList));
            foundBatches.addOrder(Order.desc("dateSubmitted"));
            
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
     * The 'findBatches' function will take a list of batches and apply the searchTerm to narrow down the results.
     *
     * @param batches The object containing the returned batches
     * @param searchTerm The term to search the batches on
     *
     * @return This function will return a list of batches that match the search term.
     */
    public List<Integer> findBatches(List<batchUploads> batches, String searchTerm) throws Exception {

        List<Integer> batchIdList = new ArrayList<Integer>();

        searchTerm = searchTerm.toLowerCase();
        searchTerm = searchTerm.replace(".", "\\.");

        for (batchUploads batch : batches) {
            
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
            String dateAsString = new SimpleDateFormat("MM/dd/yyyy").format(batch.getdateSubmitted());

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
            Criteria transactionQuery = sessionFactory.getCurrentSession().createCriteria(transactionIn.class);
            transactionQuery.add(Restrictions.eq("batchId", batch.getId()));
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

            /* Search target data */
            Criteria targetQuery = sessionFactory.getCurrentSession().createCriteria(batchUploadSummary.class);
            targetQuery.add(Restrictions.eq("batchId", batch.getId()));
            List<batchUploadSummary> targets = targetQuery.list();

            if (!targets.isEmpty()) {

                for (batchUploadSummary target : targets) {
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
     * The 'getBatchTransactions' function will return a list of transactions within a batch. The list of transactions will only be the ones the passed in user has access to.
     *
     * @param batchId The id of the selected batch
     * @param userId The id of the logged in user
     *
     * @return The function will return a list of transactionIn objects
     */
    @Override
    @Transactional
    public List<transactionIn> getBatchTransactions(int batchId, int userId) throws Exception {
        /* Get a list of connections the user has access to */

        Criteria connections = sessionFactory.getCurrentSession().createCriteria(configurationConnectionSenders.class);
        connections.add(Restrictions.eq("userId", userId));
        List<configurationConnectionSenders> userConnections = connections.list();

        List<Integer> messageTypeList = new ArrayList<Integer>();
        List<Integer> OrgList = new ArrayList<Integer>();

        if (userConnections.isEmpty()) {
            messageTypeList.add(0);
            OrgList.add(0);
        } else {

            for (configurationConnectionSenders userConnection : userConnections) {
                Criteria connection = sessionFactory.getCurrentSession().createCriteria(configurationConnection.class);
                connection.add(Restrictions.eq("id", userConnection.getconnectionId()));

                configurationConnection connectionInfo = (configurationConnection) connection.uniqueResult();

                /* Get the message type for the configuration */
                Criteria sourceconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                sourceconfigurationQuery.add(Restrictions.eq("id", connectionInfo.getsourceConfigId()));
                configuration configDetails = (configuration) sourceconfigurationQuery.uniqueResult();

                /* Add the message type to the message type list */
                messageTypeList.add(configDetails.getMessageTypeId());

                /* Get the list of target orgs */
                Criteria targetconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                targetconfigurationQuery.add(Restrictions.eq("id", connectionInfo.gettargetConfigId()));
                configuration targetconfigDetails = (configuration) targetconfigurationQuery.uniqueResult();

                /* Add the target org to the target organization list */
                OrgList.add(targetconfigDetails.getorgId());
            }
        }

        /* Get a list of available batches */
        Criteria batchSummaries = sessionFactory.getCurrentSession().createCriteria(batchUploadSummary.class);
        batchSummaries.add(Restrictions.eq("batchId", batchId));
        batchSummaries.add(Restrictions.in("messageTypeId", messageTypeList));
        batchSummaries.add(Restrictions.in("targetOrgId", OrgList));
        List<batchUploadSummary> batchUploadSummaryList = batchSummaries.list();

        List<Integer> transactionInIdList = new ArrayList<Integer>();

        if (batchUploadSummaryList.isEmpty()) {
            transactionInIdList.add(0);
        } else {

            for (batchUploadSummary summary : batchUploadSummaryList) {
                transactionInIdList.add(summary.gettransactionInId());
            }

        }

        Criteria findTransactions = sessionFactory.getCurrentSession().createCriteria(transactionIn.class);
        findTransactions.add(Restrictions.in("id", transactionInIdList));
        findTransactions.addOrder(Order.desc("dateCreated"));

        return findTransactions.list();
    }

    /**
     * The 'getsentBatches' function will return a list of sent batches for the organization passed in.
     *
     * @param orgId The organization Id to find pending transactions for.
     *
     * @return The function will return a list of sent transactions
     */
    @Override
    @Transactional
    public List<batchUploads> getsentBatches(int userId, int orgId, String searchTerm, Date fromDate, Date toDate, int page, int maxResults) throws Exception {

        int firstResult = 0;

        /* Get a list of connections the user has access to */
        Criteria connections = sessionFactory.getCurrentSession().createCriteria(configurationConnectionSenders.class);
        connections.add(Restrictions.eq("userId", userId));
        List<configurationConnectionSenders> userConnections = connections.list();

        List<Integer> messageTypeList = new ArrayList<Integer>();
        List<Integer> targetOrgList = new ArrayList<Integer>();

        if (userConnections.isEmpty()) {
            messageTypeList.add(0);
            targetOrgList.add(0);
        } else {

            for (configurationConnectionSenders userConnection : userConnections) {
                Criteria connection = sessionFactory.getCurrentSession().createCriteria(configurationConnection.class);
                connection.add(Restrictions.eq("id", userConnection.getconnectionId()));

                configurationConnection connectionInfo = (configurationConnection) connection.uniqueResult();

                /* Get the message type for the configuration */
                Criteria sourceconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                sourceconfigurationQuery.add(Restrictions.eq("id", connectionInfo.getsourceConfigId()));

                configuration configDetails = (configuration) sourceconfigurationQuery.uniqueResult();

                /* Add the message type to the message type list */
                messageTypeList.add(configDetails.getMessageTypeId());

                /* Get the list of target orgs */
                Criteria targetconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                targetconfigurationQuery.add(Restrictions.eq("id", connectionInfo.gettargetConfigId()));
                configuration targetconfigDetails = (configuration) targetconfigurationQuery.uniqueResult();

                /* Add the target org to the target organization list */
                targetOrgList.add(targetconfigDetails.getorgId());
            }
        }

        /* Get a list of available batches */
        Criteria batchSummaries = sessionFactory.getCurrentSession().createCriteria(batchUploadSummary.class);
        batchSummaries.add(Restrictions.eq("sourceOrgId", orgId));
        batchSummaries.add(Restrictions.in("messageTypeId", messageTypeList));
        batchSummaries.add(Restrictions.in("targetOrgId", targetOrgList));
        List<batchUploadSummary> batchUploadSummaryList = batchSummaries.list();

        List<Integer> batchIdList = new ArrayList<Integer>();

        if (batchUploadSummaryList.isEmpty()) {
            batchIdList.add(0);
        } else {

            for (batchUploadSummary summary : batchUploadSummaryList) {
                batchIdList.add(summary.getbatchId());
            }

        }
        
        Criteria findBatches = sessionFactory.getCurrentSession().createCriteria(batchUploads.class);
        findBatches.add(Restrictions.in("id", batchIdList));
        findBatches.add(Restrictions.or(
                Restrictions.eq("statusId", 4), /* Submission Being Processed */
                Restrictions.eq("statusId", 22), /* Submission Delivery Locked */
                Restrictions.eq("statusId", 23), /* Submission Delivery Completed */
                Restrictions.eq("statusId", 24), /* Submission Processing Completed */
                Restrictions.eq("statusId", 25), /* Target Batch Creation in process */
                Restrictions.eq("statusId", 28), /* Target Batch Creation in process */
                Restrictions.eq("statusId", 29), /* Submission Processed Errored */
                Restrictions.eq("statusId", 30) /* Target Creation Errored */
                
        )
        );
        
        if(!"".equals(fromDate)) {
            findBatches.add(Restrictions.ge("dateSubmitted", fromDate));
        }  
        
        if(!"".equals(toDate)) {
            findBatches.add(Restrictions.lt("dateSubmitted", toDate));
        } 
        
        
        findBatches.addOrder(Order.desc("dateSubmitted"));
        
        /* If a search term is entered conduct a search */
        if(!"".equals(searchTerm)) {
            
            List<batchUploads> batches = findBatches.list();
            
            List<Integer> batchFoundIdList = findBatches(batches, searchTerm);
            
            if (batchFoundIdList.isEmpty()) {
                batchFoundIdList.add(0);
            }
            
            Criteria foundBatches = sessionFactory.getCurrentSession().createCriteria(batchUploads.class);
            foundBatches.add(Restrictions.in("id", batchFoundIdList));
            foundBatches.addOrder(Order.desc("dateSubmitted"));
            
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
     * The 'getBatchDetails' function will return the batch details for the passed in batch id.
     *
     * @param batchId The id of the batch to return.
     */
    @Override
    @Transactional
    public batchUploads getBatchDetails(int batchId) throws Exception {
        return (batchUploads) sessionFactory.getCurrentSession().get(batchUploads.class, batchId);

    }

    /**
     * The 'getTransactionDetails' function will return the transaction IN details for the passed in transactionId.
     *
     * @param transactionId The id of the transaction to return
     *
     */
    @Override
    @Transactional
    public transactionIn getTransactionDetails(int transactionId) throws Exception {
        return (transactionIn) sessionFactory.getCurrentSession().get(transactionIn.class, transactionId);
    }

    /**
     * The 'getTransactionRecords' function will return the transaction IN records for the passed in transactionId.
     *
     * @param transactionId The id of the transaction records to return
     *
     */
    @Override
    @Transactional
    public transactionInRecords getTransactionRecords(int transactionId) {
        Query query = sessionFactory.getCurrentSession().createQuery("from transactionInRecords where transactionInId = :transactionId");
        query.setParameter("transactionId", transactionId);

        transactionInRecords records = (transactionInRecords) query.uniqueResult();

        return records;
    }

    /**
     * The 'getTransactionRecord' function will return the transaction IN record for the passed in recordId.
     *
     * @param recordId The id of the records to return
     *
     */
    @Override
    @Transactional
    public transactionInRecords getTransactionRecord(int recordId) {
        return (transactionInRecords) sessionFactory.getCurrentSession().get(transactionInRecords.class, recordId);
    }

    /**
     * The 'submitTransactionTarget' function will submit the transaction target
     *
     * @param transactionTarget The object that will hold the transaction target info
     *
     * @table transactionTarget
     *
     * @return This function will return the generated transaction target id
     */
    @Override
    @Transactional
    public void submitTransactionTarget(transactionTarget transactionTarget) {
        sessionFactory.getCurrentSession().save(transactionTarget);
    }

    /**
     * The 'getTransactionTargetDetails' function will return the transaction TARGET details for the passed in transactionTargetId.
     *
     * @param transactionTargetId The id of the transaction target to return
     *
     * @table transactionTarget
     *
     */
    @Override
    @Transactional
    public transactionTarget getTransactionTargetDetails(int transactionTargetId) {
        return (transactionTarget) sessionFactory.getCurrentSession().get(transactionTarget.class, transactionTargetId);
    }

    /**
     * The 'submitTransactionTargetChanges' function will submit the transaction target changes
     *
     * @param transactionTarget The object that will hold the transaction target info
     *
     * @table transactionTarget
     *
     * @return This function will not return anything
     */
    @Override
    @Transactional
    public void submitTransactionTargetChanges(transactionTarget transactionTarget) throws Exception {
        sessionFactory.getCurrentSession().update(transactionTarget);
    }

    /**
     * The 'getTransactionTarget' function will return the transaction TARGET records for the passed in transactionId and batchId.
     *
     * @param batchUploadId The id of the transaction batch to return
     * @param transactionId The id of the transaction to return
     *
     * @table transactionTarget
     *
     */
    @Override
    @Transactional
    public transactionTarget getTransactionTarget(int batchUploadId, int transactionInId) {
        Query query = sessionFactory.getCurrentSession().createQuery("from transactionTarget where batchUploadId = :batchUploadId and transactionInId = :transactionId");
        query.setParameter("batchUploadId", batchUploadId);
        query.setParameter("transactionId", transactionInId);

        transactionTarget target = (transactionTarget) query.uniqueResult();

        return target;
    }

    /**
     * The 'submitAttachment' will save the attachment to the database
     *
     * @param attachment The object that will hold the attachment details
     *
     * @return THis function will return the id of the saved attachment.
     */
    @Override
    @Transactional
    public Integer submitAttachment(transactionAttachment attachment) throws Exception {
        Integer attachmentId = null;

        attachmentId = (Integer) sessionFactory.getCurrentSession().save(attachment);

        return attachmentId;
    }

    /**
     * The 'getAttachmentById' function will return the details of an attachment for the passed in Id.
     *
     * @param attachmentId The id of the attachment to get the details for
     *
     * @return This function will return a transactionAttachment object
     */
    @Override
    @Transactional
    public transactionAttachment getAttachmentById(int attachmentId) throws Exception {
        return (transactionAttachment) sessionFactory.getCurrentSession().get(transactionAttachment.class, attachmentId);
    }

    /**
     * The 'submitAttachmentChanges' function will update the attachment details for the passed in attachment.
     *
     * @param attachment The attachment object that contains the details
     *
     * @return This function does not return anything.
     */
    @Override
    @Transactional
    public void submitAttachmentChanges(transactionAttachment attachment) throws Exception {
        sessionFactory.getCurrentSession().update(attachment);
    }

    /**
     * The 'getAttachmentsByTransactionId' function will return a list of attachments for the passed in transaction.
     *
     * @param transactionInId The id of the transaction to search on
     *
     * @return This function will return a list of transactionAttachment objects
     */
    @Override
    @Transactional
    public List<transactionAttachment> getAttachmentsByTransactionId(int transactionInId) throws Exception {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(transactionAttachment.class);
        criteria.add(Restrictions.eq("transactionInId", transactionInId));

        return criteria.list();
    }

    /**
     * The 'removeAttachmentById' function will remove the attachment from the Database.
     *
     * @param attachmentId The id of the attachment to be removed
     *
     * @table transactionAttachments
     *
     * @return This function will not return anything.
     */
    @Override
    @Transactional
    public void removeAttachmentById(int attachmentId) throws Exception {

        Query deletAttachment = sessionFactory.getCurrentSession().createQuery("delete from transactionAttachment where id = :attachmentId");
        deletAttachment.setParameter("attachmentId", attachmentId);
        deletAttachment.executeUpdate();

    }

    /**
     * The 'getConfigIdsForBatch' function will return a list of configurations for a batch
     *
     * @param batchUploadId The id of the attachment to be removed
     *
     * @table transactionTranslatedIn
     *
     * @return This function will return a list of configIds (Integer)
     */
    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Integer> getConfigIdsForBatch(int batchUploadId, boolean getAll) {

        String sql = "select distinct configId from transactionTranslatedIn "
                + " where transactionInId in (select id from transactionIn where batchId = :id ";
                
                if (!getAll) {
                	sql = sql + " and statusId != :transRELId";
                }
                sql = sql + ");";

        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        query.setParameter("id", batchUploadId);
        if (!getAll) {
        	query.setParameter("transRELId", transRELId);
        }
        List<Integer> configIds = query.list();

        return configIds;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<ConfigForInsert> setConfigForInsert(int configId, int batchUploadId) {
        String sql = ("call setSqlForConfig(:id, :batchUploadId);");
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .addScalar("saveToTableName", StandardBasicTypes.STRING)
                .addScalar("saveToTableCol", StandardBasicTypes.STRING)
                .addScalar("batchUploadId", StandardBasicTypes.INTEGER)
                .addScalar("configId", StandardBasicTypes.INTEGER)
                .addScalar("singleValueFields", StandardBasicTypes.STRING)
                .addScalar("splitFields", StandardBasicTypes.STRING)
                .addScalar("checkForDelim", StandardBasicTypes.STRING)
                .setResultTransformer(
                        Transformers.aliasToBean(ConfigForInsert.class))
                .setParameter("id", configId)
                .setParameter("batchUploadId", batchUploadId);

        List<ConfigForInsert> configListForInsert = query.list();

        return configListForInsert;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Integer> getTransWithMultiValues(ConfigForInsert config) {

        String sql = ("select transactionInId from "
                + " transactionTranslatedIn where (" + config.getCheckForDelim()
                + ") and transactionInId in (select id from transactionIn where statusId = :relId "
                + " and batchId = :batchUploadId"
                + " and configId = :configId); ");

        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        query.setParameter("configId", config.getConfigId());
        query.setParameter("batchUploadId", config.getBatchUploadId());
        query.setParameter("relId", transRELId);

        List<Integer> transId = query.list();

        return transId;

    }

    @Override
    @Transactional
    public boolean insertSingleToMessageTables(ConfigForInsert config) {
        boolean insertSuccess = false;
        String sql = "insert into " + config.getSaveToTableName()
                + " (transactionInId, " + config.getSaveToTableCol()
                + ") select transactionInId, "
                + config.getSingleValueFields()
                + " from transactionTranslatedIn where "
                + " transactionInId in (select id from transactionIn where batchId = :batchId"
                + " and configId = :configId and statusId = :relId ";
        if (config.getLoopTransIds() != null && config.getLoopTransIds().size() > 0) {
            sql = sql + " and id not in (" + config.getLoopTransIds().toString().substring(1, config.getLoopTransIds().toString().length() - 1) + ")";
        }
        if (config.getBlankValueTransId() != null && config.getBlankValueTransId().size() > 0) {
            sql = sql + " and id not in (" + config.getBlankValueTransId().toString().substring(1, config.getBlankValueTransId().toString().length() - 1) + ")";
        }

        sql = sql + ");";

        Query insertData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchId", config.getBatchUploadId())
                .setParameter("configId", config.getConfigId())
                .setParameter("relId", transRELId);

        try {
            insertData.executeUpdate();
            insertSuccess = true;
        } catch (Exception ex) {
            System.err.println("insertSingleToMessageTables." + ex);

        }
        return insertSuccess;
    }

    @Override
    @Transactional
    public Integer clearMessageTableForBatch(int batchId, String mt) {
        String sql = "delete from " + mt + " where transactionInId in "
                + " (select id from transactionIn where batchId = :id)"
                + ";";
        Query deleteTable = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .addScalar("id", StandardBasicTypes.INTEGER).setParameter("id", batchId);
        try {
            deleteTable.executeUpdate();
            return 0;
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
            return 1;

        }
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<String> getMessageTables() {
        String sql = ("select table_name from information_schema.COLUMNS "
                + " where TABLE_SCHEMA = :schemaName "
                + " and table_name like concat('message_%')"
                + " and column_name like 'transactionInId';");
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        query.setParameter("schemaName", schemaName);
        List<String> mt = query.list();
        return mt;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Integer> getBlankTransIds(ConfigForInsert config) {

        String sql = ("select transactionInId from "
                + " transactionTranslatedIn where (length(CONCAT_WS(''," + config.getSingleValueFields()
                + ")) = 0 or length(CONCAT_WS(''," + config.getSingleValueFields()
                + ")) is null) and transactionInId in (select id from transactionIn where statusId = :relId "
                + " and batchId = :batchUploadId"
                + " and configId = :configId); ");

        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        query.setParameter("configId", config.getConfigId());
        query.setParameter("batchUploadId", config.getBatchUploadId());
        query.setParameter("relId", transRELId);

        List<Integer> transId = query.list();

        return transId;
    }

    /**
     * This method will pass in the subString position and insert value *
     */
    @Override
    @Transactional
    public boolean insertMultiValToMessageTables(ConfigForInsert config,
            Integer subStringCounter, Integer transId) {

        String replaceSplitField = config.getSplitFields().replaceAll("@valPos", subStringCounter.toString());
        String sql = "insert into " + config.getSaveToTableName()
                + " (transactionInId, " + config.getSaveToTableCol()
                + ") select transactionInId, "
                + replaceSplitField
                + " from transactionTranslatedIn where "
                + " transactionInId in (select id from transactionIn where batchId = :batchId"
                + " and configId = :configId and statusId = :relId and id = :id";

        sql = sql + ");";

        Query insertData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchId", config.getBatchUploadId())
                .setParameter("configId", config.getConfigId())
                .setParameter("id", transId)
                .setParameter("relId", transRELId);
        try {
            insertData.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.err.println("insertMultiValToMessageTables." + ex);
            return false;
        }
    }

    @Override
    @Transactional
    public Integer countSubString(ConfigForInsert config, Integer transId) {
        String col = config.getSingleValueFields().substring(0, config.getSingleValueFields().indexOf(","));
        String sql
                = "(SELECT ROUND(((LENGTH(" + col
                + ") - LENGTH(REPLACE(LCASE(" + col
                + "), '||^||', '')))/LENGTH('||^||')),0) as stringCount from transactionTranslatedin "
                + " where transactionInId = :id);";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("stringCount", StandardBasicTypes.INTEGER);
        query.setParameter("id", transId);
        Integer stringCount = (Integer) query.list().get(0);

        return stringCount;
    }

    /**
     * The 'getuploadedBatches' function will return a list of batches that were uploaded by the logged in user.
     *
     * @param userId The id of the logged in user
     * @param orgId The id of the organization the logged in user belongs to
     *
     * @return This function will return a list of batches.
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<batchUploads> getuploadedBatches(int userId, int orgId) throws Exception {

        /* Get a list of uploaded batches for the organization */
        Criteria findBatches = sessionFactory.getCurrentSession().createCriteria(batchUploads.class);
        findBatches.add(Restrictions.eq("orgId", orgId));
        findBatches.add(Restrictions.eq("transportMethodId", 1));
        findBatches.add(Restrictions.ne("statusId", 1));
        findBatches.addOrder(Order.desc("dateSubmitted"));

        return findBatches.list();
    }

    @Override
    @Transactional
    public void updateBatchStatus(Integer batchUploadId, Integer statusId, String timeField) throws Exception {

        String sql = "update batchUploads set statusId = :statusId ";
        if (!timeField.equalsIgnoreCase("")) {
            sql = sql + ", " + timeField + " = CURRENT_TIMESTAMP";
        } else {
            // we reset time
            sql = sql + ", startDateTime = null, endDateTime = null";
        }
        sql = sql + " where id = :id ";
        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("statusId", statusId)
                .setParameter("id", batchUploadId);
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
        }

    }

    @Override
    @Transactional
    public void updateTransactionStatus(Integer batchUploadId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception {
        String sql = "update transactionIn "
                + " set statusId = :toStatusId, "
                + "dateCreated = CURRENT_TIMESTAMP";

        if (transactionId > 0) {
            sql += " where id = :transactionId ";
        } else {
            sql += " where batchId = :batchUploadId ";
        }

        if (fromStatusId != 0) {
            sql = sql + " and statusId = :fromStatusId";
        }
        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("toStatusId", toStatusId);
                if (transactionId > 0) {
                     updateData.setParameter("transactionId", transactionId);
                }
                else {
                     updateData.setParameter("batchUploadId", batchUploadId);
                }
               

        if (fromStatusId != 0) {
            updateData.setParameter("fromStatusId", fromStatusId);
        }

        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
        }

    }

    /**
     * The 'updateTransactionTargetStatus' function will update the transactionTarget entries when the created batch has been sent.
     *
     * @param batchUploadId The id of the created batch
     * @param transactionId The id of the specific transaction to update (default to 0)
     * @param fromStatusId
     * @param toStatusId The status we want to change to
     */
    @Override
    @Transactional
    public void updateTransactionTargetStatus(Integer batchUploadId, Integer transactionId, Integer fromStatusId, Integer toStatusId) throws Exception {
        
        String sql = "update transactionTarget "
                + " set statusId = :toStatusId, "
                + "statusTime = CURRENT_TIMESTAMP";

        if (transactionId > 0) {
            sql += " where id = :transactionId ";
        } else {
            sql += " where batchUploadId = :batchUploadId ";
        }

        if (fromStatusId != 0) {
            sql = sql + " and statusId = :fromStatusId";
        }
        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("toStatusId", toStatusId);
                if (transactionId > 0) {
                    updateData.setParameter("transactionId", transactionId);
                }
                else {
                    updateData.setParameter("batchUploadId", batchUploadId);
                }

        if (fromStatusId != 0) {
            updateData.setParameter("fromStatusId", fromStatusId);
        }

        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
        }

    }

    @Override
    @Transactional
    public boolean allowBatchClear(Integer batchUploadId) {
        String sql
                = "select count(*) as rowCount from batchUploads where id = :id and statusId in (22,23,1);";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("rowCount", StandardBasicTypes.INTEGER);
        query.setParameter("id", batchUploadId);
        Integer rowCount = (Integer) query.list().get(0);
        if (rowCount == 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Integer clearTransactionInRecords(Integer batchUploadId) {
        String sql = "delete from transactionInRecords where transactionInId in"
                + "(select id from transactionIn where batchId = :batchUploadId )";

        Query deleteData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchUploadId", batchUploadId);

        try {
            deleteData.executeUpdate();
            return 0;
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
            return 1;
        }
    }

    @Override
    @Transactional
    public Integer clearTransactionTranslatedIn(Integer batchUploadId) {
        String sql = "delete from transactionTranslatedIn where transactionInId in"
                + "(select id from transactionIn where batchId = :batchUploadId )";

        Query deleteData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchUploadId", batchUploadId);

        try {
            deleteData.executeUpdate();
            return 0;
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
            return 1;
        }
    }

    @Override
    @Transactional
    public Integer clearTransactionTarget(Integer batchUploadId) {
        String sql = "delete from TransactionTarget where batchUploadId = :batchUploadId";

        Query deleteData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchUploadId", batchUploadId);

        try {
            deleteData.executeUpdate();
            return 0;
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
            return 1;
        }
    }

    @Override
    @Transactional
    public Integer clearTransactionIn(Integer batchUploadId) {
        String sql = "delete from transactionIn where batchId = :batchUploadId";
        Query deleteData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchUploadId", batchUploadId);
        try {
            deleteData.executeUpdate();
            return 0;
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
            return 1;
        }
    }

    /**
     * errorId = 1 is required field missing*
     * we do not re-check REL records
     */
    @Override
    @Transactional
    public Integer insertFailedRequiredFields(configurationFormFields cff, Integer batchUploadId) {
        String sql = "insert into transactionInerrors (batchUploadId, configId, transactionInId, configurationFormFieldsId, errorid)"
                + "(select " + batchUploadId + ", "+ cff.getconfigId() +", transactionInId, " + cff.getId()
                + ", 1 from transactionTranslatedIn where configId = :configId "
                + " and (F" + cff.getFieldNo()
                + " is  null  or length(trim(F" + cff.getFieldNo() + ")) = 0)"
                + "and transactionInId in (select id from transactionIn where batchId = :batchUploadId"
                + " and configId = :configId and statusId != :transRELId));";
        Query insertData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchUploadId", batchUploadId)
                .setParameter("configId", cff.getconfigId())
                .setParameter("transRELId", transRELId);

        try {
            insertData.executeUpdate();
            return 0;
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
            
            return 1;
        }

    }

    @Override
    @Transactional
    public Integer clearTransactionInErrors(Integer batchUploadId) {
        String sql = "delete from transactionInErrors where batchUploadId = :batchUploadId";

        Query deleteData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchUploadId", batchUploadId);

        try {
            deleteData.executeUpdate();
            return 0;
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
            return 1;
        }
    }

    @Override
    @Transactional
    public void updateStatusForErrorTrans(Integer batchId, Integer statusId, boolean foroutboundProcessing) {

        String sql;

        if (foroutboundProcessing == false) {
            sql = "update transactionIn set statusId = :statusId where"
                    + " id in (select distinct transactionInId"
                    + " from transactionInErrors where batchUploadId = :batchId); ";
        } else {
            sql = "update transactionTarget set statusId = :statusId where"
                    + " id in (select distinct transactionTargetId"
                    + " from transactionOutErrors where batchDownLoadId = :batchId); ";

        }

        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchId", batchId)
                .setParameter("statusId", statusId);

        try {
            updateData.executeUpdate();

        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
        }
    }

    @Override
    @Transactional
    public Integer genericValidation(configurationFormFields cff,
            Integer validationTypeId, Integer batchUploadId, String regEx) {

        String sql = "call insertValidationErrors(:vtType, :fieldNo, :batchUploadId, :configId, :cffId)";

        Query insertError = sessionFactory.getCurrentSession().createSQLQuery(sql);
        insertError.setParameter("vtType", cff.getValidationType());
        insertError.setParameter("fieldNo", cff.getFieldNo());
        insertError.setParameter("batchUploadId", batchUploadId);
        insertError.setParameter("configId", cff.getconfigId());
        insertError.setParameter("cffId", cff.getId());
        try {
            insertError.executeUpdate();
            return 0;
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
            insertProcessingError(5, cff.getconfigId(), batchUploadId, cff.getId(),
		    		null, null, validationTypeId, false, false, (ex.getClass() + "-" + ex.getCause().toString()));
            return 1; //we return error count of 1 when error
        }
    }

    @Override
    @Transactional
    public void updateBlanksToNull(configurationFormFields cff,
            Integer batchUploadId) {
        String sql = "update transactiontranslatedIn set F" + cff.getFieldNo() + " = null where length(F"
                + cff.getFieldNo() + ") = 0 "
                + "and transactionInId in (select id from transactionIn where batchId = :batchUploadId "
                + "and configId = :configId and statusId = :relId);";

        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql);
        updateData.setParameter("batchUploadId", batchUploadId);
        updateData.setParameter("configId", cff.getconfigId());
        updateData.setParameter("relId", transRELId);
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
        }

    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<transactionRecords> getFieldColAndValues(Integer batchUploadId,
            configurationFormFields cff) {
        String sql = ("select transactionInId as transactionId, F" + cff.getFieldNo()
                + "  as fieldValue, " + cff.getFieldNo() + " as fieldNo from transactiontranslatedIn "
                + " where configId = :configId "
                + " and F" + cff.getFieldNo() + " is not null "
                + " and transactionInId in (select id from transactionIn where"
                + " batchId = :batchUploadId"
                + " and configId = :configId and statusId != :transRELId order by transactionInId); ");

        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .addScalar("transactionId", StandardBasicTypes.INTEGER)
                .addScalar("fieldValue", StandardBasicTypes.STRING)
                .addScalar("fieldNo", StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(transactionRecords.class))
                .setParameter("configId", cff.getconfigId())
                .setParameter("batchUploadId", batchUploadId)
                .setParameter("transRELId", transRELId);

        List<transactionRecords> trs = query.list();

        return trs;
    }

    @Override
    @Transactional
    public void updateFieldValue(transactionRecords tr, String newValue) {
        String sql = "update transactiontranslatedIn set F" + tr.getfieldNo() + " = :newValue where"
                + " transactionInId = :ttiId";
        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql);
        updateData.setParameter("ttiId", tr.getTransactionId());
        updateData.setParameter("newValue", newValue);

        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
        }

    }

    @Override
    @Transactional
    public void insertValidationError(transactionRecords tr, configurationFormFields cff, Integer batchUploadId) {
        String sql = "insert into transactionInerrors "
                + "(batchUploadId, configId, transactionInId, configurationFormFieldsId, errorid, validationTypeId)"
                + " values (:batchUploadId, :configId, :ttiId, :cffId, 2, :validationId);";
        Query insertData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchUploadId", batchUploadId)
                .setParameter("cffId", cff.getId())
                .setParameter("configId", cff.getconfigId())
                .setParameter("ttiId", tr.getTransactionId())
                .setParameter("validationId", cff.getValidationType());

        try {
            insertData.executeUpdate();

        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());

        }
    }

    /**
     *
     */
    @Override
    @Transactional
    public Integer getFeedbackReportConnection(int configId, int targetorgId) {

        Criteria configurationConnections = sessionFactory.getCurrentSession().createCriteria(configurationConnection.class);
        configurationConnections.add(Restrictions.eq("sourceConfigId", configId));
        List<configurationConnection> connections = configurationConnections.list();

        Integer connectionId = 0;

        if (!connections.isEmpty()) {

            for (configurationConnection connection : connections) {
                Criteria configurations = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                configurations.add(Restrictions.eq("id", connection.gettargetConfigId()));

                configuration configDetails = (configuration) configurations.uniqueResult();

                if (configDetails.getorgId() == targetorgId) {
                    connectionId = connection.getId();
                }

            }

        }

        return connectionId;

    }

    @Override
    @Transactional
    public void nullForCWCol(Integer configId, Integer batchId, boolean foroutboundProcessing) {

        String sql;

        if (foroutboundProcessing == false) {

            sql = "update transactionTranslatedIn set forcw = null where "
                    + "transactionInId in (select id from transactionIn where configId = :configId "
                    + " and batchId = :batchId);";

        } else {
            sql = "update transactionTranslatedOut set forcw = null where "
                    + "transactionTargetId in (select id from transactionTarget where configId = :configId "
                    + " and batchDLId = :batchId);";
        }

        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchId", batchId)
                .setParameter("configId", configId);
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
        }
    }

    @Override
    @Transactional
    public void executeCWData(Integer configId, Integer batchId, Integer fieldNo, 
    		CrosswalkData cwd, boolean foroutboundProcessing, Integer fieldId) {

        String sql;

        if (foroutboundProcessing == false) {
            sql = "update transactionTranslatedIn set forcw = :targetValue where "
                    + "F" + fieldNo + " = :sourceValue and transactionInId in "
                    + "(select id from transactionIn where configId = :configId "
                    + " and batchId = :batchId and statusId != :transRELId);";
        } else {
            sql = "update transactionTranslatedOut set forcw = :targetValue where "
                    + "F" + fieldNo + " = :sourceValue and transactionTargetId in "
                    + "(select id from transactionTarget where configId = :configId "
                    + " and batchDLId = :batchId and statusId != :transRELId);";
        }

        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("targetValue", cwd.getTargetValue())
                .setParameter("sourceValue", cwd.getSourceValue())
                .setParameter("batchId", batchId)
                .setParameter("configId", configId)
                 .setParameter("transRELId", transRELId);
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
            insertProcessingError(5, configId, batchId, fieldId,
		    		null, cwd.getCrosswalkId(), null,
		    		false, foroutboundProcessing, (ex.getClass() + " " + ex.getCause().toString()));
        }

    }

    @Override
    @Transactional
    public void updateFieldNoWithCWData(Integer configId, Integer batchId, Integer fieldNo, Integer passClear, boolean foroutboundProcessing) {

        String sql;

        if (foroutboundProcessing == false) {
            sql = "update transactionTranslatedIn "
                    + " JOIN (SELECT id from transactionIn WHERE configId = :configId"
                    + " and batchId = :batchId) as ti ON transactionTranslatedIn.transactionInId = ti.id "
                    + " SET transactionTranslatedIn.F" + fieldNo + " = forcw ";
        } else {
            sql = "update transactionTranslatedOut "
                    + " JOIN (SELECT id from transactionTarget WHERE configId = :configId"
                    + " and batchDLId = :batchId) as ti ON transactionTranslatedOut.transactionTargetId = ti.id "
                    + " SET transactionTranslatedOut.F" + fieldNo + " = forcw ";
        }

        if (passClear == 1) {
            // 1 is pass, we leave original values in fieldNo alone
            sql = sql + "where forcw is not null;";
        }
        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchId", batchId)
                .setParameter("configId", configId);
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
        }
    }

    @Override
    @Transactional
    public void flagCWErrors(Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing) {

        String sql;

        if (foroutboundProcessing == false) {
            sql = "insert into transactionInerrors (batchUploadId, configId, "
                    + "transactionInId, configurationFormFieldsId, errorid, cwId)"
                    + " select " + batchId + ", " +configId +",transactionInId, " + cdt.getFieldId()
                    + ", 3,  " + cdt.getCrosswalkId() + " from transactionTranslatedIn where "
                    + "configId = :configId "
                    + " and (F" + cdt.getFieldNo()
                    + " is not null and length(F"+cdt.getFieldNo()+") != 0  and forcw is null)"
                    + "and transactionInId in (select id from transactionIn "
                    + "where batchId = :batchId"
                    + " and configId = :configId);";

        } else {
            sql = "insert into transactionOutErrors (batchDownloadId, configId, "
                    + "transactionTargetId, configurationFormFieldsId, errorid, cwId)"
                    + " select " + batchId + ", "+ configId+",  transactionTargetId, " + cdt.getFieldId()
                    + ", 3,  " + cdt.getCrosswalkId() + " from transactionTranslatedOut where "
                    + " configId = :configId "
                    + " and (F" + cdt.getFieldNo()
                    + " is not null and length(F"+cdt.getFieldNo()+") != 0 and forcw is null)"
                    + " and transactionTargetId in (select id from transactionTarget "
                    + " where batchDLId = :batchId"
                    + " and configId = :configId);";
        }

        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchId", batchId)
                .setParameter("configId", configId);
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
        }
    }
    
    
    @Override
    @Transactional
    public void flagMacroErrors(Integer configId, Integer batchId, configurationDataTranslations cdt, boolean foroutboundProcessing) {

        String sql;

        if (foroutboundProcessing == false) {
            sql = "insert into transactionInerrors (batchUploadId, configId, "
                    + "transactionInId, configurationFormFieldsId, errorid, macroId)"
                    + " select " + batchId + ", "+ configId +", transactionInId, " + cdt.getFieldId()
                    + ", 4,  " + cdt.getMacroId() + " from transactionTranslatedIn where "
                    + "configId = :configId "
                    + " and (F" + cdt.getFieldNo()
                    + " is not null and forcw is null)"
                    + "and transactionInId in (select id from transactionIn "
                    + "where batchId = :batchId"
                    + " and configId = :configId);";

        } else {
            sql = "insert into transactionOutErrors (batchDownloadId, configId, "
                    + "transactionTargetId, configurationFormFieldsId, errorid, macroId)"
                    + " select " + batchId + ", " + configId +",transactionTargetId, " + cdt.getFieldId()
                    + ", 4,  " + cdt.getMacroId() + " from transactionTranslatedOut where "
                    + "configId = :configId "
                    + " and (F" + cdt.getFieldNo()
                    + " is not null and forcw is null)"
                    + " and transactionTargetId in (select id from transactionTarget "
                    + "where batchDLId = :batchId"
                    + " and configId = :configId);";
        }

        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchId", batchId)
                .setParameter("configId", configId);
        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
        }
    }

    /** this method will repalce transactionTranslatedIn data with
     * 	data from transactionInRecords
     * 
     *  @param batchId - the batch to reset
     *  @param resetAll - if true, we reset all records.  If false, we skip the REL records
     *  
     */
    @Override
    @Transactional
    public void resetTransactionTranslatedIn(Integer batchId, boolean resetAll) {
        String sql = "UPDATE transactionTranslatedIn INNER JOIN transactionInRecords ON "
                + " (transactionTranslatedIn.transactionInId = transactionInRecords.transactionInId) and transactionTranslatedIn.transactionInId "
                + " in (select id from transactionIn where batchId = :batchId ";
                if (!resetAll) {
                	sql = sql + " and statusId != :transRELId";
                }
                sql = sql + ") SET transactionTranslatedIn.F1 = transactionInRecords.F1,"
                + " transactionTranslatedIn.F2 = transactionInRecords.F2, transactionTranslatedIn.F3 = transactionInRecords.F3,"
                + " transactionTranslatedIn.F4 = transactionInRecords.F4, transactionTranslatedIn.F5 = transactionInRecords.F5,"
                + "transactionTranslatedIn.F6 = transactionInRecords.F6,transactionTranslatedIn.F7 = transactionInRecords.F7,"
                + "transactionTranslatedIn.F8 = transactionInRecords.F8,transactionTranslatedIn.F9 = transactionInRecords.F9,"
                + "transactionTranslatedIn.F10 = transactionInRecords.F10,transactionTranslatedIn.F11 = transactionInRecords.F11,"
                + "transactionTranslatedIn.F12 = transactionInRecords.F12,transactionTranslatedIn.F13 = transactionInRecords.F13,"
                + "transactionTranslatedIn.F14 = transactionInRecords.F14,transactionTranslatedIn.F15 = transactionInRecords.F15,"
                + "transactionTranslatedIn.F16 = transactionInRecords.F16,transactionTranslatedIn.F17 = transactionInRecords.F17,"
                + "transactiontranslatedIn.F18 = transactionInRecords.F18,"
                + "transactiontranslatedIn.F19 = transactionInRecords.F19,"
                + "transactiontranslatedIn.F20 = transactionInRecords.F20,"
                + "transactiontranslatedIn.F21 = transactionInRecords.F21,"
                + "transactiontranslatedIn.F22 = transactionInRecords.F22,"
                + "transactiontranslatedIn.F23 = transactionInRecords.F23,"
                + "transactiontranslatedIn.F24 = transactionInRecords.F24,"
                + "transactiontranslatedIn.F25 = transactionInRecords.F25,"
                + "transactiontranslatedIn.F26 = transactionInRecords.F26,"
                + "transactiontranslatedIn.F27 = transactionInRecords.F27,"
                + "transactiontranslatedIn.F28 = transactionInRecords.F28,"
                + "transactiontranslatedIn.F29 = transactionInRecords.F29,"
                + "transactiontranslatedIn.F30 = transactionInRecords.F30,"
                + "transactiontranslatedIn.F31 = transactionInRecords.F31,"
                + "transactiontranslatedIn.F32 = transactionInRecords.F32,"
                + "transactiontranslatedIn.F33 = transactionInRecords.F33,"
                + "transactiontranslatedIn.F34 = transactionInRecords.F34,"
                + "transactiontranslatedIn.F35 = transactionInRecords.F35,"
                + "transactiontranslatedIn.F36 = transactionInRecords.F36,"
                + "transactiontranslatedIn.F37 = transactionInRecords.F37,"
                + "transactiontranslatedIn.F38 = transactionInRecords.F38,"
                + "transactiontranslatedIn.F39 = transactionInRecords.F39,"
                + "transactiontranslatedIn.F40 = transactionInRecords.F40,"
                + "transactiontranslatedIn.F41 = transactionInRecords.F41,"
                + "transactiontranslatedIn.F42 = transactionInRecords.F42,"
                + "transactiontranslatedIn.F43 = transactionInRecords.F43,"
                + "transactiontranslatedIn.F44 = transactionInRecords.F44,"
                + "transactiontranslatedIn.F45 = transactionInRecords.F45,"
                + "transactiontranslatedIn.F46 = transactionInRecords.F46,"
                + "transactiontranslatedIn.F47 = transactionInRecords.F47,"
                + "transactiontranslatedIn.F48 = transactionInRecords.F48,"
                + "transactiontranslatedIn.F49 = transactionInRecords.F49,"
                + "transactiontranslatedIn.F50 = transactionInRecords.F50,"
                + "transactiontranslatedIn.F51 = transactionInRecords.F51,"
                + "transactiontranslatedIn.F52 = transactionInRecords.F52,"
                + "transactiontranslatedIn.F53 = transactionInRecords.F53,"
                + "transactiontranslatedIn.F54 = transactionInRecords.F54,"
                + "transactiontranslatedIn.F55 = transactionInRecords.F55,"
                + "transactiontranslatedIn.F56 = transactionInRecords.F56,"
                + "transactiontranslatedIn.F57 = transactionInRecords.F57,"
                + "transactiontranslatedIn.F58 = transactionInRecords.F58,"
                + "transactiontranslatedIn.F59 = transactionInRecords.F59,"
                + "transactiontranslatedIn.F60 = transactionInRecords.F60,"
                + "transactiontranslatedIn.F61 = transactionInRecords.F61,"
                + "transactiontranslatedIn.F62 = transactionInRecords.F62,"
                + "transactiontranslatedIn.F63 = transactionInRecords.F63,"
                + "transactiontranslatedIn.F64 = transactionInRecords.F64,"
                + "transactiontranslatedIn.F65 = transactionInRecords.F65,"
                + "transactiontranslatedIn.F66 = transactionInRecords.F66,"
                + "transactiontranslatedIn.F67 = transactionInRecords.F67,"
                + "transactiontranslatedIn.F68 = transactionInRecords.F68,"
                + "transactiontranslatedIn.F69 = transactionInRecords.F69,"
                + "transactiontranslatedIn.F70 = transactionInRecords.F70,"
                + "transactiontranslatedIn.F71 = transactionInRecords.F71,"
                + "transactiontranslatedIn.F72 = transactionInRecords.F72,"
                + "transactiontranslatedIn.F73 = transactionInRecords.F73,"
                + "transactiontranslatedIn.F74 = transactionInRecords.F74,"
                + "transactiontranslatedIn.F75 = transactionInRecords.F75,"
                + "transactiontranslatedIn.F76 = transactionInRecords.F76,"
                + "transactiontranslatedIn.F77 = transactionInRecords.F77,"
                + "transactiontranslatedIn.F78 = transactionInRecords.F78,"
                + "transactiontranslatedIn.F79 = transactionInRecords.F79,"
                + "transactiontranslatedIn.F80 = transactionInRecords.F80,"
                + "transactiontranslatedIn.F81 = transactionInRecords.F81,"
                + "transactiontranslatedIn.F82 = transactionInRecords.F82,"
                + "transactiontranslatedIn.F83 = transactionInRecords.F83,"
                + "transactiontranslatedIn.F84 = transactionInRecords.F84,"
                + "transactiontranslatedIn.F85 = transactionInRecords.F85,"
                + "transactiontranslatedIn.F86 = transactionInRecords.F86,"
                + "transactiontranslatedIn.F87 = transactionInRecords.F87,"
                + "transactiontranslatedIn.F88 = transactionInRecords.F88,"
                + "transactiontranslatedIn.F89 = transactionInRecords.F89,"
                + "transactiontranslatedIn.F90 = transactionInRecords.F90,"
                + "transactiontranslatedIn.F91 = transactionInRecords.F91,"
                + "transactiontranslatedIn.F92 = transactionInRecords.F92,"
                + "transactiontranslatedIn.F93 = transactionInRecords.F93,"
                + "transactiontranslatedIn.F94 = transactionInRecords.F94,"
                + "transactiontranslatedIn.F95 = transactionInRecords.F95,"
                + "transactiontranslatedIn.F96 = transactionInRecords.F96,"
                + "transactiontranslatedIn.F97 = transactionInRecords.F97,"
                + "transactiontranslatedIn.F98 = transactionInRecords.F98,"
                + "transactiontranslatedIn.F99 = transactionInRecords.F99,"
                + "transactiontranslatedIn.F100 = transactionInRecords.F100,"
                + "transactiontranslatedIn.F101 = transactionInRecords.F101,"
                + "transactiontranslatedIn.F102 = transactionInRecords.F102,"
                + "transactiontranslatedIn.F103 = transactionInRecords.F103,"
                + "transactiontranslatedIn.F104 = transactionInRecords.F104,"
                + "transactiontranslatedIn.F105 = transactionInRecords.F105,"
                + "transactiontranslatedIn.F106 = transactionInRecords.F106,"
                + "transactiontranslatedIn.F107 = transactionInRecords.F107,"
                + "transactiontranslatedIn.F108 = transactionInRecords.F108,"
                + "transactiontranslatedIn.F109 = transactionInRecords.F109,"
                + "transactiontranslatedIn.F110 = transactionInRecords.F110,"
                + "transactiontranslatedIn.F111 = transactionInRecords.F111,"
                + "transactiontranslatedIn.F112 = transactionInRecords.F112,"
                + "transactiontranslatedIn.F113 = transactionInRecords.F113,"
                + "transactiontranslatedIn.F114 = transactionInRecords.F114,"
                + "transactiontranslatedIn.F115 = transactionInRecords.F115,"
                + "transactiontranslatedIn.F116 = transactionInRecords.F116,"
                + "transactiontranslatedIn.F117 = transactionInRecords.F117,"
                + "transactiontranslatedIn.F118 = transactionInRecords.F118,"
                + "transactiontranslatedIn.F119 = transactionInRecords.F119,"
                + "transactiontranslatedIn.F120 = transactionInRecords.F120,"
                + "transactiontranslatedIn.F121 = transactionInRecords.F121,"
                + "transactiontranslatedIn.F122 = transactionInRecords.F122,"
                + "transactiontranslatedIn.F123 = transactionInRecords.F123,"
                + "transactiontranslatedIn.F124 = transactionInRecords.F124,"
                + "transactiontranslatedIn.F125 = transactionInRecords.F125,"
                + "transactiontranslatedIn.F126 = transactionInRecords.F126,"
                + "transactiontranslatedIn.F127 = transactionInRecords.F127,"
                + "transactiontranslatedIn.F128 = transactionInRecords.F128,"
                + "transactiontranslatedIn.F129 = transactionInRecords.F129,"
                + "transactiontranslatedIn.F130 = transactionInRecords.F130,"
                + "transactiontranslatedIn.F131 = transactionInRecords.F131,"
                + "transactiontranslatedIn.F132 = transactionInRecords.F132,"
                + "transactiontranslatedIn.F133 = transactionInRecords.F133,"
                + "transactiontranslatedIn.F134 = transactionInRecords.F134,"
                + "transactiontranslatedIn.F135 = transactionInRecords.F135,"
                + "transactiontranslatedIn.F136 = transactionInRecords.F136,"
                + "transactiontranslatedIn.F137 = transactionInRecords.F137,"
                + "transactiontranslatedIn.F138 = transactionInRecords.F138,"
                + "transactiontranslatedIn.F139 = transactionInRecords.F139,"
                + "transactiontranslatedIn.F140 = transactionInRecords.F140,"
                + "transactiontranslatedIn.F141 = transactionInRecords.F141,"
                + "transactiontranslatedIn.F142 = transactionInRecords.F142,"
                + "transactiontranslatedIn.F143 = transactionInRecords.F143,"
                + "transactiontranslatedIn.F144 = transactionInRecords.F144,"
                + "transactiontranslatedIn.F145 = transactionInRecords.F145,"
                + "transactiontranslatedIn.F146 = transactionInRecords.F146,"
                + "transactiontranslatedIn.F147 = transactionInRecords.F147,"
                + "transactiontranslatedIn.F148 = transactionInRecords.F148,"
                + "transactiontranslatedIn.F149 = transactionInRecords.F149,"
                + "transactiontranslatedIn.F150 = transactionInRecords.F150,"
                + "transactiontranslatedIn.F151 = transactionInRecords.F151,"
                + "transactiontranslatedIn.F152 = transactionInRecords.F152,"
                + "transactiontranslatedIn.F153 = transactionInRecords.F153,"
                + "transactiontranslatedIn.F154 = transactionInRecords.F154,"
                + "transactiontranslatedIn.F155 = transactionInRecords.F155,"
                + "transactiontranslatedIn.F156 = transactionInRecords.F156,"
                + "transactiontranslatedIn.F157 = transactionInRecords.F157,"
                + "transactiontranslatedIn.F158 = transactionInRecords.F158,"
                + "transactiontranslatedIn.F159 = transactionInRecords.F159,"
                + "transactiontranslatedIn.F160 = transactionInRecords.F160,"
                + "transactiontranslatedIn.F161 = transactionInRecords.F161,"
                + "transactiontranslatedIn.F162 = transactionInRecords.F162,"
                + "transactiontranslatedIn.F163 = transactionInRecords.F163,"
                + "transactiontranslatedIn.F164 = transactionInRecords.F164,"
                + "transactiontranslatedIn.F165 = transactionInRecords.F165,"
                + "transactiontranslatedIn.F166 = transactionInRecords.F166,"
                + "transactiontranslatedIn.F167 = transactionInRecords.F167,"
                + "transactiontranslatedIn.F168 = transactionInRecords.F168,"
                + "transactiontranslatedIn.F169 = transactionInRecords.F169,"
                + "transactiontranslatedIn.F170 = transactionInRecords.F170,"
                + "transactiontranslatedIn.F171 = transactionInRecords.F171,"
                + "transactiontranslatedIn.F172 = transactionInRecords.F172,"
                + "transactiontranslatedIn.F173 = transactionInRecords.F173,"
                + "transactiontranslatedIn.F174 = transactionInRecords.F174,"
                + "transactiontranslatedIn.F175 = transactionInRecords.F175,"
                + "transactiontranslatedIn.F176 = transactionInRecords.F176,"
                + "transactiontranslatedIn.F177 = transactionInRecords.F177,"
                + "transactiontranslatedIn.F178 = transactionInRecords.F178,"
                + "transactiontranslatedIn.F179 = transactionInRecords.F179,"
                + "transactiontranslatedIn.F180 = transactionInRecords.F180,"
                + "transactiontranslatedIn.F181 = transactionInRecords.F181,"
                + "transactiontranslatedIn.F182 = transactionInRecords.F182,"
                + "transactiontranslatedIn.F183 = transactionInRecords.F183,"
                + "transactiontranslatedIn.F184 = transactionInRecords.F184,"
                + "transactiontranslatedIn.F185 = transactionInRecords.F185,"
                + "transactiontranslatedIn.F186 = transactionInRecords.F186,"
                + "transactiontranslatedIn.F187 = transactionInRecords.F187,"
                + "transactiontranslatedIn.F188 = transactionInRecords.F188,"
                + "transactiontranslatedIn.F189 = transactionInRecords.F189,"
                + "transactiontranslatedIn.F190 = transactionInRecords.F190,"
                + "transactiontranslatedIn.F191 = transactionInRecords.F191,"
                + "transactiontranslatedIn.F192 = transactionInRecords.F192,"
                + "transactiontranslatedIn.F193 = transactionInRecords.F193,"
                + "transactiontranslatedIn.F194 = transactionInRecords.F194,"
                + "transactiontranslatedIn.F195 = transactionInRecords.F195,"
                + "transactiontranslatedIn.F196 = transactionInRecords.F196,"
                + "transactiontranslatedIn.F197 = transactionInRecords.F197,"
                + "transactiontranslatedIn.F198 = transactionInRecords.F198,"
                + "transactiontranslatedIn.F199 = transactionInRecords.F199,"
                + "transactiontranslatedIn.F200 = transactionInRecords.F200,"
                + "transactiontranslatedIn.F201 = transactionInRecords.F201,"
                + "transactiontranslatedIn.F202 = transactionInRecords.F202,"
                + "transactiontranslatedIn.F203 = transactionInRecords.F203,"
                + "transactiontranslatedIn.F204 = transactionInRecords.F204,"
                + "transactiontranslatedIn.F205 = transactionInRecords.F205,"
                + "transactiontranslatedIn.F206 = transactionInRecords.F206,"
                + "transactiontranslatedIn.F207 = transactionInRecords.F207,"
                + "transactiontranslatedIn.F208 = transactionInRecords.F208,"
                + "transactiontranslatedIn.F209 = transactionInRecords.F209,"
                + "transactiontranslatedIn.F210 = transactionInRecords.F210,"
                + "transactiontranslatedIn.F211 = transactionInRecords.F211,"
                + "transactiontranslatedIn.F212 = transactionInRecords.F212,"
                + "transactiontranslatedIn.F213 = transactionInRecords.F213,"
                + "transactiontranslatedIn.F214 = transactionInRecords.F214,"
                + "transactiontranslatedIn.F215 = transactionInRecords.F215,"
                + "transactiontranslatedIn.F216 = transactionInRecords.F216,"
                + "transactiontranslatedIn.F217 = transactionInRecords.F217,"
                + "transactiontranslatedIn.F218 = transactionInRecords.F218,"
                + "transactiontranslatedIn.F219 = transactionInRecords.F219,"
                + "transactiontranslatedIn.F220 = transactionInRecords.F220,"
                + "transactiontranslatedIn.F221 = transactionInRecords.F221,"
                + "transactiontranslatedIn.F222 = transactionInRecords.F222,"
                + "transactiontranslatedIn.F223 = transactionInRecords.F223,"
                + "transactiontranslatedIn.F224 = transactionInRecords.F224,"
                + "transactiontranslatedIn.F225 = transactionInRecords.F225,"
                + "transactiontranslatedIn.F226 = transactionInRecords.F226,"
                + "transactiontranslatedIn.F227 = transactionInRecords.F227,"
                + "transactiontranslatedIn.F228 = transactionInRecords.F228,"
                + "transactiontranslatedIn.F229 = transactionInRecords.F229,"
                + "transactiontranslatedIn.F230 = transactionInRecords.F230,"
                + "transactiontranslatedIn.F231 = transactionInRecords.F231,"
                + "transactiontranslatedIn.F232 = transactionInRecords.F232,"
                + "transactiontranslatedIn.F233 = transactionInRecords.F233,"
                + "transactiontranslatedIn.F234 = transactionInRecords.F234,"
                + "transactiontranslatedIn.F235 = transactionInRecords.F235,"
                + "transactiontranslatedIn.F236 = transactionInRecords.F236,"
                + "transactiontranslatedIn.F237 = transactionInRecords.F237,"
                + "transactiontranslatedIn.F238 = transactionInRecords.F238,"
                + "transactiontranslatedIn.F239 = transactionInRecords.F239,"
                + "transactiontranslatedIn.F240 = transactionInRecords.F240,"
                + "transactiontranslatedIn.F241 = transactionInRecords.F241,"
                + "transactiontranslatedIn.F242 = transactionInRecords.F242,"
                + "transactiontranslatedIn.F243 = transactionInRecords.F243,"
                + "transactiontranslatedIn.F244 = transactionInRecords.F244,"
                + "transactiontranslatedIn.F245 = transactionInRecords.F245,"
                + "transactiontranslatedIn.F246 = transactionInRecords.F246,"
                + "transactiontranslatedIn.F247 = transactionInRecords.F247,"
                + "transactiontranslatedIn.F248 = transactionInRecords.F248,"
                + "transactiontranslatedIn.F249 = transactionInRecords.F249,"
                + "transactiontranslatedIn.F250 = transactionInRecords.F250,"
                + "transactiontranslatedIn.F251 = transactionInRecords.F251,"
                + "transactiontranslatedIn.F252 = transactionInRecords.F252,"
                + "transactiontranslatedIn.F253 = transactionInRecords.F253,"
                + "transactiontranslatedIn.F254 = transactionInRecords.F254,"
                + "transactiontranslatedIn.F255 = transactionInRecords.F255";

        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setParameter("batchId", batchId);
        if (!resetAll) {
        	updateData.setParameter("transRELId",transRELId);
        }

        try {
            updateData.executeUpdate();
        } catch (Exception ex) {
        	System.err.println(ex.getClass() + " " + ex.getCause());
        }

    }
    
    /**
     * This method looks for fieldA, fieldB, con1 and con2, fieldNo in the configurationDataTranslations
     * and passes it to the formula (SP) stored in Macros
     * 
     * All macros will take the following parameter -
     * configId, batchId, srcField, fieldA, fieldB, con1, con2, macroId, foroutboundProcessing,
     * errorId
     * 
     * **/
	@Override
	@Transactional
	public Integer executeMacro(Integer configId, Integer batchId,
			configurationDataTranslations cdt, boolean foroutboundProcessing,
			Macros macro) {
		try {
				String sql = ("CALL " + macro.getFormula() + " (:configId, :batchId, :srcField, "
						+ ":fieldA, :fieldB, :con1, :con2, :macroId, :foroutboundProcessing, :passClear);");	
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		        query.setParameter("configId", configId);
		        query.setParameter("batchId", batchId);
		        query.setParameter("srcField", ("F" + cdt.getFieldNo()));
		        query.setParameter("fieldA", ("F" + cdt.getFieldA()));
		        query.setParameter("fieldB", ("F" + cdt.getFieldB()));
		        query.setParameter("con1", ("F" + cdt.getFieldNo()));
		        query.setParameter("con2", cdt.getConstant2());
		        query.setParameter("macroId", cdt.getMacroId());
		        query.setParameter("foroutboundProcessing", foroutboundProcessing);
		        query.setParameter("passClear", cdt.getPassClear());
		       
		        query.list();
		        return 0; 
		   } catch (Exception ex) {
			   //insert system error
			   insertProcessingError(5, configId, batchId, cdt.getFieldId(),
			    		cdt.getMacroId(), null, null,
			    		false, foroutboundProcessing, (ex.getClass() + " " + ex.getCause().toString()));
			   System.err.println(ex.getClass() + " " + ex.getCause());
			   return 1;
		   }

	}


		@Override
	    @Transactional
	    public void insertProcessingError(Integer errorId, Integer configId, Integer batchId,
	    		Integer fieldId,Integer macroId, Integer cwId, Integer validationTypeId,
	    		boolean required, boolean foroutboundProcessing, String stackTrace) {

	        String tableName = "transactionInErrors";
	        String batchType = "batchUploadId";
	        if (foroutboundProcessing) {
	        	tableName = "transactionOutErrors";
	        	batchType = "batchDownloadId";
	        }
	        	String sql = " INSERT INTO " + tableName +" (errorId, " + batchType + ", configId, "
	        			+ "configurationFormFieldsId, required,  "
	        			+ "cwId, macroId, validationTypeId, stackTrace) "
	        			+ "VALUES (:errorId, :batchId, :configId, "
	        			+ " :configurationFormFieldsId, :required, "
	        			+ ":cwId,:macroId,:validationTypeId,:stackTrace);";
	        
	        	Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
	        		.setParameter("errorId", errorId)
	        		.setParameter("batchId", batchId)
	                .setParameter("configId", configId)
	                .setParameter("configurationFormFieldsId", fieldId)
	        		.setParameter("required", required)
	                .setParameter("validationTypeId", validationTypeId)
	                .setParameter("cwId", cwId)
	        		.setParameter("macroId", macroId)
	                .setParameter("stackTrace", stackTrace.toString());
	        try {
	            updateData.executeUpdate();
	        } catch (Exception ex) {
	        	System.err.println(ex.getClass() + " " + ex.getCause());
	        }
	    }

		@Override
		@Transactional
		@SuppressWarnings("unchecked")
		public List<configurationTransport> getHandlingDetailsByBatch(
				int batchId) throws Exception {
			try {
				String sql = ("select distinct clearRecords, autoRelease, errorHandling "
						+ " from configurationtransportdetails where configId in "
						+ "(select distinct configId from transactionIn where batchId = :batchId);");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
		                .setResultTransformer(
		                        Transformers.aliasToBean(configurationTransport.class))
		                .setParameter("batchId", batchId);
		       
		        	List<configurationTransport> ct = query.list();
		        	return ct;
	        } catch (Exception ex) {
	        	System.err.println(ex.getClass() + " " + ex.getCause());
	        	return null;
	        }
	       
	    }

		@Override
		@Transactional
		public void updateRecordCounts (Integer batchId, List <Integer> statusIds, boolean foroutboundProcessing, String colNameToUpdate) {
			String sql = "";
	        if (!foroutboundProcessing) {
	        	sql = " update batchUploads set "+ colNameToUpdate  +" = "
	        			+ "(select count(*) as total from transactionIn where batchId = :batchId ";
	        } else {
	        	sql = "update batchUploads set "+colNameToUpdate+" = (select count(*) as total "
	        			+ " from transactionTarget where"
						+ " batchDLId = :batchId ";
	        }
	        if (statusIds.size() > 0) {
	        	sql = sql +  "and statusId in (:statusIds)";
	        }
	        sql = sql + ") where id = :batchId";
	        
	        try {
	        	Query query = sessionFactory
	                .getCurrentSession()
	                .createSQLQuery(sql)
	                .setParameter("batchId", batchId);
	        	if (statusIds.size() > 0) {
	                query.setParameterList("statusIds", statusIds);
	        	}
	        	query.executeUpdate();
	        } catch (Exception ex) {
	        	System.err.println(ex.getClass() + " " + ex.getCause());
	        }
		}

		@Override
		@Transactional
		public Integer getRecordCounts(Integer batchId, List<Integer> statusIds, boolean foroutboundProcessing) {
			String tableName = "transactionIn";
			String batchType = "batchId";
	        if (foroutboundProcessing) {
	        	tableName = "transactionTarget";
	        	batchType = "batchDLId";
	        }
	        	String sql = "select count(*) as total from " + tableName+ " where " + batchType +" = :batchId ";
	        if (statusIds.size() > 0) {
	        	sql = sql +  " and statusId in (:statusIds)";
	        }
	        
	        
	        
	        try {
	        	Query query = sessionFactory
	                .getCurrentSession()
	                .createSQLQuery(sql).addScalar("total", StandardBasicTypes.INTEGER);
	            
	        	query.setParameter("batchId", batchId);
	        	
	            if (statusIds.size() > 0) {
	                query.setParameterList("statusIds", statusIds);
	        	}
	        	
	        	return (Integer) query.list().get(0);
	        } catch (Exception ex) {
	        	System.err.println(ex.getClass() + " " + ex.getCause());
	        	return null;
	        }
		}

		@Override
		@Transactional
		public Integer copyTransactionInStatusToTarget(Integer batchId) {
			try {
				String sql = ("UPDATE transactionTarget INNER JOIN transactionIn  ON  transactionIn.id = transactionTarget.transactionInId "
						+ " and transactionTarget.batchUploadId = :batchId set transactionTarget.statusId = transactionIn.statusId;");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
		                .setParameter("batchId", batchId);
		        query.executeUpdate();
				return 0;
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return 1;
			}
		}

		@Override
		@Transactional
		public Integer insertLoadData(Integer batchId, String delimChar, String fileWithPath, String loadTableName) {
			try {
				String sql = ("LOAD DATA LOCAL INFILE '" + fileWithPath +"' INTO TABLE "
						+ loadTableName +" fields terminated by '" + delimChar +"' LINES TERMINATED BY '\\n';");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);           
		        query.executeUpdate();
				return 0;
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return 1;
			}
		}

		@Override
		@Transactional
		public Integer createLoadTable(String loadTableName) {
			try {
				String sql = ("create TABLE " + loadTableName + "(f1 varchar(255) DEFAULT NULL,  f2 varchar(255) DEFAULT NULL,  f3 varchar(255) DEFAULT NULL,  f4 varchar(255) DEFAULT NULL,  f5 varchar(255) DEFAULT NULL,  f6 varchar(255) DEFAULT NULL,  f7 varchar(255) DEFAULT NULL,  f8 varchar(255) DEFAULT NULL,  f9 varchar(255) DEFAULT NULL,  f10 varchar(255) DEFAULT NULL,  f11 varchar(255) DEFAULT NULL,  f12 varchar(255) DEFAULT NULL,  f13 varchar(255) DEFAULT NULL,  f14 varchar(255) DEFAULT NULL,  f15 varchar(255) DEFAULT NULL,  f16 varchar(255) DEFAULT NULL,  f17 varchar(255) DEFAULT NULL,  f18 varchar(255) DEFAULT NULL,  f19 varchar(255) DEFAULT NULL,  f20 varchar(255) DEFAULT NULL,  f21 varchar(255) DEFAULT NULL,  f22 varchar(255) DEFAULT NULL,  f23 varchar(255) DEFAULT NULL,  f24 varchar(255) DEFAULT NULL,  f25 varchar(255) DEFAULT NULL,  f26 varchar(255) DEFAULT NULL,  f27 varchar(255) DEFAULT NULL,  f28 varchar(255) DEFAULT NULL,  f29 varchar(255) DEFAULT NULL,  f30 varchar(255) DEFAULT NULL,  f31 varchar(255) DEFAULT NULL,  f32 varchar(255) DEFAULT NULL,  f33 varchar(255) DEFAULT NULL,  f34 varchar(255) DEFAULT NULL,  f35 varchar(255) DEFAULT NULL,  f36 varchar(255) DEFAULT NULL,  f37 varchar(255) DEFAULT NULL,  f38 varchar(255) DEFAULT NULL,  f39 varchar(255) DEFAULT NULL,  f40 varchar(255) DEFAULT NULL,  f41 varchar(255) DEFAULT NULL,  f42 varchar(255) DEFAULT NULL,  f43 varchar(255) DEFAULT NULL,  f44 varchar(255) DEFAULT NULL,  f45 varchar(255) DEFAULT NULL,  f46 varchar(255) DEFAULT NULL,  f47 varchar(255) DEFAULT NULL,  f48 varchar(255) DEFAULT NULL,  f49 varchar(255) DEFAULT NULL,  f50 varchar(255) DEFAULT NULL,  f51 varchar(255) DEFAULT NULL,  f52 varchar(255) DEFAULT NULL,  f53 varchar(255) DEFAULT NULL,  f54 varchar(255) DEFAULT NULL,  f55 varchar(255) DEFAULT NULL,  f56 varchar(255) DEFAULT NULL,  f57 varchar(255) DEFAULT NULL,  f58 varchar(255) DEFAULT NULL,  f59 varchar(255) DEFAULT NULL,  f60 varchar(255) DEFAULT NULL,  f61 varchar(255) DEFAULT NULL,  f62 varchar(255) DEFAULT NULL,  f63 varchar(255) DEFAULT NULL,  f64 varchar(255) DEFAULT NULL,  f65 varchar(255) DEFAULT NULL,  f66 varchar(255) DEFAULT NULL,  f67 varchar(255) DEFAULT NULL,  f68 varchar(255) DEFAULT NULL,  f69 varchar(255) DEFAULT NULL,  f70 varchar(255) DEFAULT NULL,  f71 varchar(255) DEFAULT NULL,  f72 varchar(255) DEFAULT NULL,  f73 varchar(255) DEFAULT NULL,  f74 varchar(255) DEFAULT NULL,  f75 varchar(255) DEFAULT NULL,  f76 varchar(255) DEFAULT NULL,  f77 varchar(255) DEFAULT NULL,  f78 varchar(255) DEFAULT NULL,  f79 varchar(255) DEFAULT NULL,  f80 varchar(255) DEFAULT NULL,  f81 varchar(255) DEFAULT NULL,  f82 varchar(255) DEFAULT NULL,  f83 varchar(255) DEFAULT NULL,  f84 varchar(255) DEFAULT NULL,  f85 varchar(255) DEFAULT NULL,  f86 varchar(255) DEFAULT NULL,  f87 varchar(255) DEFAULT NULL,  f88 varchar(255) DEFAULT NULL,  f89 varchar(255) DEFAULT NULL,  f90 varchar(255) DEFAULT NULL,  f91 varchar(255) DEFAULT NULL,  f92 varchar(255) DEFAULT NULL,  f93 varchar(255) DEFAULT NULL,  f94 varchar(255) DEFAULT NULL,  f95 varchar(255) DEFAULT NULL,  f96 varchar(255) DEFAULT NULL,  f97 varchar(255) DEFAULT NULL,  f98 varchar(255) DEFAULT NULL,  f99 varchar(255) DEFAULT NULL,  f100 varchar(255) DEFAULT NULL,  f101 varchar(255) DEFAULT NULL,  f102 varchar(255) DEFAULT NULL,  f103 varchar(255) DEFAULT NULL,  f104 varchar(255) DEFAULT NULL,  f105 varchar(255) DEFAULT NULL,  f106 varchar(255) DEFAULT NULL,  f107 varchar(255) DEFAULT NULL,  f108 varchar(255) DEFAULT NULL,  f109 varchar(255) DEFAULT NULL,  f110 varchar(255) DEFAULT NULL,  f111 varchar(255) DEFAULT NULL,  f112 varchar(255) DEFAULT NULL,  f113 varchar(255) DEFAULT NULL,  f114 varchar(255) DEFAULT NULL,  f115 varchar(255) DEFAULT NULL,  f116 varchar(255) DEFAULT NULL,  f117 varchar(255) DEFAULT NULL,  f118 varchar(255) DEFAULT NULL,  f119 varchar(255) DEFAULT NULL,  f120 varchar(255) DEFAULT NULL,  f121 varchar(255) DEFAULT NULL,  f122 varchar(255) DEFAULT NULL,  f123 varchar(255) DEFAULT NULL,  f124 varchar(255) DEFAULT NULL,  f125 varchar(255) DEFAULT NULL,  f126 varchar(255) DEFAULT NULL,  f127 varchar(255) DEFAULT NULL,  f128 varchar(255) DEFAULT NULL,  f129 varchar(255) DEFAULT NULL,  f130 varchar(255) DEFAULT NULL,  f131 varchar(255) DEFAULT NULL,  f132 varchar(255) DEFAULT NULL,  f133 varchar(255) DEFAULT NULL,  f134 varchar(255) DEFAULT NULL,  f135 varchar(255) DEFAULT NULL,  f136 varchar(255) DEFAULT NULL,  f137 varchar(255) DEFAULT NULL,  f138 varchar(255) DEFAULT NULL,  f139 varchar(255) DEFAULT NULL,  f140 varchar(255) DEFAULT NULL,  f141 varchar(255) DEFAULT NULL,  f142 varchar(255) DEFAULT NULL,  f143 varchar(255) DEFAULT NULL,  f144 varchar(255) DEFAULT NULL,  f145 varchar(255) DEFAULT NULL,  f146 varchar(255) DEFAULT NULL,  f147 varchar(255) DEFAULT NULL,  f148 varchar(255) DEFAULT NULL,  f149 varchar(255) DEFAULT NULL,  f150 varchar(255) DEFAULT NULL,  f151 varchar(255) DEFAULT NULL,  f152 varchar(255) DEFAULT NULL,  f153 varchar(255) DEFAULT NULL,  f154 varchar(255) DEFAULT NULL,  f155 varchar(255) DEFAULT NULL,  f156 varchar(255) DEFAULT NULL,  f157 varchar(255) DEFAULT NULL,  f158 varchar(255) DEFAULT NULL,  f159 varchar(255) DEFAULT NULL,  f160 varchar(255) DEFAULT NULL,  f161 varchar(255) DEFAULT NULL,  f162 varchar(255) DEFAULT NULL,  f163 varchar(255) DEFAULT NULL,  f164 varchar(255) DEFAULT NULL,  f165 varchar(255) DEFAULT NULL,  f166 varchar(255) DEFAULT NULL,  f167 varchar(255) DEFAULT NULL,  f168 varchar(255) DEFAULT NULL,  f169 varchar(255) DEFAULT NULL,  f170 varchar(255) DEFAULT NULL,  f171 varchar(255) DEFAULT NULL,  f172 varchar(255) DEFAULT NULL,  f173 varchar(255) DEFAULT NULL,  f174 varchar(255) DEFAULT NULL,  f175 varchar(255) DEFAULT NULL,  f176 varchar(255) DEFAULT NULL,  f177 varchar(255) DEFAULT NULL,  f178 varchar(255) DEFAULT NULL,  f179 varchar(255) DEFAULT NULL,  f180 varchar(255) DEFAULT NULL,  f181 varchar(255) DEFAULT NULL,  f182 varchar(255) DEFAULT NULL,  f183 varchar(255) DEFAULT NULL,  f184 varchar(255) DEFAULT NULL,  f185 varchar(255) DEFAULT NULL,  f186 varchar(255) DEFAULT NULL,  f187 varchar(255) DEFAULT NULL,  f188 varchar(255) DEFAULT NULL,  f189 varchar(255) DEFAULT NULL,  f190 varchar(255) DEFAULT NULL,  f191 varchar(255) DEFAULT NULL,  f192 varchar(255) DEFAULT NULL,  f193 varchar(255) DEFAULT NULL,  f194 varchar(255) DEFAULT NULL,  f195 varchar(255) DEFAULT NULL,  f196 varchar(255) DEFAULT NULL,  f197 varchar(255) DEFAULT NULL,  f198 varchar(255) DEFAULT NULL,  f199 varchar(255) DEFAULT NULL,  f200 varchar(255) DEFAULT NULL,  f201 varchar(255) DEFAULT NULL,  f202 varchar(255) DEFAULT NULL,  f203 varchar(255) DEFAULT NULL,  f204 varchar(255) DEFAULT NULL,  f205 varchar(255) DEFAULT NULL,  f206 varchar(255) DEFAULT NULL,  f207 varchar(255) DEFAULT NULL,  f208 varchar(255) DEFAULT NULL,  f209 varchar(255) DEFAULT NULL,  f210 varchar(255) DEFAULT NULL,  f211 varchar(255) DEFAULT NULL,  f212 varchar(255) DEFAULT NULL,  f213 varchar(255) DEFAULT NULL,  f214 varchar(255) DEFAULT NULL,  f215 varchar(255) DEFAULT NULL,  f216 varchar(255) DEFAULT NULL,  f217 varchar(255) DEFAULT NULL,  f218 varchar(255) DEFAULT NULL,  f219 varchar(255) DEFAULT NULL,  f220 varchar(255) DEFAULT NULL,  f221 varchar(255) DEFAULT NULL,  f222 varchar(255) DEFAULT NULL,  f223 varchar(255) DEFAULT NULL,  f224 varchar(255) DEFAULT NULL,  f225 varchar(255) DEFAULT NULL,  f226 varchar(255) DEFAULT NULL,  f227 varchar(255) DEFAULT NULL,  f228 varchar(255) DEFAULT NULL,  f229 varchar(255) DEFAULT NULL,  f230 varchar(255) DEFAULT NULL,  f231 varchar(255) DEFAULT NULL,  f232 varchar(255) DEFAULT NULL,  f233 varchar(255) DEFAULT NULL,  f234 varchar(255) DEFAULT NULL,  f235 varchar(255) DEFAULT NULL,  f236 varchar(255) DEFAULT NULL,  f237 varchar(255) DEFAULT NULL,  f238 varchar(255) DEFAULT NULL,  f239 varchar(255) DEFAULT NULL,  f240 varchar(255) DEFAULT NULL,  f241 varchar(255) DEFAULT NULL,  f242 varchar(255) DEFAULT NULL,  f243 varchar(255) DEFAULT NULL,  f244 varchar(255) DEFAULT NULL,  f245 varchar(255) DEFAULT NULL,  f246 varchar(255) DEFAULT NULL,  f247 varchar(255) DEFAULT NULL,  f248 varchar(255) DEFAULT NULL,  f249 varchar(255) DEFAULT NULL,  f250 varchar(255) DEFAULT NULL,  f251 varchar(255) DEFAULT NULL,  f252 varchar(255) DEFAULT NULL,  f253 varchar(255) DEFAULT NULL,  f254 varchar(255) DEFAULT NULL,  f255 varchar(255) DEFAULT NULL,  batchId int(11),  loadTableId varchar(45) ,  id int(11) NOT NULL AUTO_INCREMENT,  PRIMARY KEY (id)) ENGINE=InnoDB AUTO_INCREMENT=1038 DEFAULT CHARSET=latin1;");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);           
		        query.executeUpdate();
				return 0;
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return 1;
			}
		}

		@Override
		@Transactional
		public Integer dropLoadTable(String loadTableName) {
			try {
				String sql = ("drop TABLE if exists " + loadTableName + ";");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);           
		        query.executeUpdate();
				return 0;
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return 1;
			}
		}
		
		@Override
		@Transactional
		public Integer updateLoadTable(String loadTableName, Integer batchId) {
			try {
				String sql = ("update " + loadTableName +" set batchId = :batchId, loadTableId = concat('" + loadTableName + "_', id);");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql); 
		        query.setParameter("batchId", batchId);
		        query.executeUpdate();
				return 0;
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return 1;
			}
		}

		@Override
		@Transactional
		@SuppressWarnings("unchecked")
		public List <configurationTransport> getConfigurationTransportForBatch(Integer batchId) {
			try {
				
				String sql = ("select delimChar, errorHandling, autoRelease, fileLocation, fileType, transportMethodId"
						+ " from configurationTransportDetails, ref_delimiters "
						+ "	where ref_delimiters.id = configurationTransportDetails.fileDelimiter "
						+ " and configId in ( select configId from batchUploadConfirgurations where batchId = :batchId) ;");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(
                        Transformers.aliasToBean(configurationTransport.class));           
		        query.setParameter("batchId", batchId);
		        
		        List <configurationTransport> configurationTransports = query.list();
				
		        return configurationTransports;
		        
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return null;
			}
		}

		@Override
		@Transactional
		public Integer loadTransactionIn(String loadTableName, Integer batchId) {
			try {
				String sql = ("insert into transactionIn (batchId, statusId, loadTableId) "
						+ " select :batchId, 11, loadTableId from "+ loadTableName +";");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql); 
		        query.setParameter("batchId", batchId);
		        query.executeUpdate();
				return 0;
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return 1;
			}
		}
		
		@Override
		@Transactional
		public Integer loadTransactionInRecords(Integer batchId) {
			try {
				String sql = ("insert into transactionInRecords (transactionInId, loadTableId) select id, loadTableId from transactionIn where batchId = :batchId");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql); 
		        query.setParameter("batchId", batchId);
		        query.executeUpdate();
				return 0;
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return 1;
			}
		}
		
		@Override
		@Transactional
		public Integer loadTransactionInRecordsData (String loadTableName) {
			try {
				String sql = ("UPDATE transactionInRecords "
					+ "	INNER JOIN " + loadTableName + " loadTableTemp ON " 
					+ "transactionInRecords.loadTableId = loadTableTemp.loadTableId "
					+ "set  transactionInRecords.F1 = loadTableTemp.F1, " 
	                + "transactionInRecords.F2 = loadTableTemp.F2, "  
	                + "transactionInRecords.F3 = loadTableTemp.F3, " 
	                + "transactionInRecords.F4 = loadTableTemp.F4, "  
	                + "transactionInRecords.F5 = loadTableTemp.F5, " 
	                + "transactionInRecords.F6 = loadTableTemp.F6, " 
	                + "transactionInRecords.F7 = loadTableTemp.F7, " 
	                + "transactionInRecords.F8 = loadTableTemp.F8, " 
	                + "transactionInRecords.F9 = loadTableTemp.F9, " 
	                + "transactionInRecords.F10 = loadTableTemp.F10, " 
	                + "transactionInRecords.F11 = loadTableTemp.F11, " 
	                + "transactionInRecords.F12 = loadTableTemp.F12, " 
	                + "transactionInRecords.F13 = loadTableTemp.F13, " 
	                + "transactionInRecords.F14 = loadTableTemp.F14, " 
	                + "transactionInRecords.F15 = loadTableTemp.F15, " 
	                + "transactionInRecords.F16 = loadTableTemp.F16, " 
	                + "transactionInRecords.F17 = loadTableTemp.F17, " 
	                + "transactionInRecords.F18 = loadTableTemp.F18, " 
	                + "transactionInRecords.F19 = loadTableTemp.F19, " 
	                + "transactionInRecords.F20 = loadTableTemp.F20, " 
	                + "transactionInRecords.F21 = loadTableTemp.F21, " 
	                + "transactionInRecords.F22 = loadTableTemp.F22, " 
	                + "transactionInRecords.F23 = loadTableTemp.F23, " 
	                + "transactionInRecords.F24 = loadTableTemp.F24, " 
	                + "transactionInRecords.F25 = loadTableTemp.F25, " 
	                + "transactionInRecords.F26 = loadTableTemp.F26, " 
	                + "transactionInRecords.F27 = loadTableTemp.F27, " 
	                + "transactionInRecords.F28 = loadTableTemp.F28, " 
	                + "transactionInRecords.F29 = loadTableTemp.F29, " 
	                + "transactionInRecords.F30 = loadTableTemp.F30, " 
	                + "transactionInRecords.F31 = loadTableTemp.F31, " 
	                + "transactionInRecords.F32 = loadTableTemp.F32, " 
	                + "transactionInRecords.F33 = loadTableTemp.F33, " 
	                + "transactionInRecords.F34 = loadTableTemp.F34, " 
	                + "transactionInRecords.F35 = loadTableTemp.F35, " 
	                + "transactionInRecords.F36 = loadTableTemp.F36, " 
	                + "transactionInRecords.F37 = loadTableTemp.F37, " 
	                + "transactionInRecords.F38 = loadTableTemp.F38, " 
	                + "transactionInRecords.F39 = loadTableTemp.F39, " 
	                + "transactionInRecords.F40 = loadTableTemp.F40, " 
	                + "transactionInRecords.F41 = loadTableTemp.F41, " 
	                + "transactionInRecords.F42 = loadTableTemp.F42, " 
	                + "transactionInRecords.F43 = loadTableTemp.F43, " 
	                + "transactionInRecords.F44 = loadTableTemp.F44, " 
	                + "transactionInRecords.F45 = loadTableTemp.F45, " 
	                + "transactionInRecords.F46 = loadTableTemp.F46, " 
	                + "transactionInRecords.F47 = loadTableTemp.F47, " 
	                + "transactionInRecords.F48 = loadTableTemp.F48, " 
	                + "transactionInRecords.F49 = loadTableTemp.F49, " 
	                + "transactionInRecords.F50 = loadTableTemp.F50, " 
	                + "transactionInRecords.F51 = loadTableTemp.F51, " 
	                + "transactionInRecords.F52 = loadTableTemp.F52, " 
	                + "transactionInRecords.F53 = loadTableTemp.F53, " 
	                + "transactionInRecords.F54 = loadTableTemp.F54, " 
	                + "transactionInRecords.F55 = loadTableTemp.F55, " 
	                + "transactionInRecords.F56 = loadTableTemp.F56, " 
	                + "transactionInRecords.F57 = loadTableTemp.F57, " 
	                + "transactionInRecords.F58 = loadTableTemp.F58, " 
	                + "transactionInRecords.F59 = loadTableTemp.F59, " 
	                + "transactionInRecords.F60 = loadTableTemp.F60, " 
	                + "transactionInRecords.F61 = loadTableTemp.F61, " 
	                + "transactionInRecords.F62 = loadTableTemp.F62, " 
	                + "transactionInRecords.F63 = loadTableTemp.F63, " 
	                + "transactionInRecords.F64 = loadTableTemp.F64, " 
	                + "transactionInRecords.F65 = loadTableTemp.F65, " 
	                + "transactionInRecords.F66 = loadTableTemp.F66, " 
	                + "transactionInRecords.F67 = loadTableTemp.F67, " 
	                + "transactionInRecords.F68 = loadTableTemp.F68, " 
	                + "transactionInRecords.F69 = loadTableTemp.F69, " 
	                + "transactionInRecords.F70 = loadTableTemp.F70, " 
	                + "transactionInRecords.F71 = loadTableTemp.F71, " 
	                + "transactionInRecords.F72 = loadTableTemp.F72, " 
	                + "transactionInRecords.F73 = loadTableTemp.F73, " 
	                + "transactionInRecords.F74 = loadTableTemp.F74, " 
	                + "transactionInRecords.F75 = loadTableTemp.F75, " 
	                + "transactionInRecords.F76 = loadTableTemp.F76, " 
	                + "transactionInRecords.F77 = loadTableTemp.F77, " 
	                + "transactionInRecords.F78 = loadTableTemp.F78, " 
	                + "transactionInRecords.F79 = loadTableTemp.F79, " 
	                + "transactionInRecords.F80 = loadTableTemp.F80, " 
	                + "transactionInRecords.F81 = loadTableTemp.F81, " 
	                + "transactionInRecords.F82 = loadTableTemp.F82, " 
	                + "transactionInRecords.F83 = loadTableTemp.F83, " 
	                + "transactionInRecords.F84 = loadTableTemp.F84, " 
	                + "transactionInRecords.F85 = loadTableTemp.F85, " 
	                + "transactionInRecords.F86 = loadTableTemp.F86, " 
	                + "transactionInRecords.F87 = loadTableTemp.F87, " 
	                + "transactionInRecords.F88 = loadTableTemp.F88, " 
	                + "transactionInRecords.F89 = loadTableTemp.F89, " 
	                + "transactionInRecords.F90 = loadTableTemp.F90, " 
	                + "transactionInRecords.F91 = loadTableTemp.F91, " 
	                + "transactionInRecords.F92 = loadTableTemp.F92, " 
	                + "transactionInRecords.F93 = loadTableTemp.F93, " 
	                + "transactionInRecords.F94 = loadTableTemp.F94, " 
	                + "transactionInRecords.F95 = loadTableTemp.F95, " 
	                + "transactionInRecords.F96 = loadTableTemp.F96, " 
	                + "transactionInRecords.F97 = loadTableTemp.F97, " 
	                + "transactionInRecords.F98 = loadTableTemp.F98, " 
	                + "transactionInRecords.F99 = loadTableTemp.F99, " 
	                + "transactionInRecords.F100 = loadTableTemp.F100, " 
	                + "transactionInRecords.F101 = loadTableTemp.F101, " 
	                + "transactionInRecords.F102 = loadTableTemp.F102, " 
	                + "transactionInRecords.F103 = loadTableTemp.F103, " 
	                + "transactionInRecords.F104 = loadTableTemp.F104, " 
	                + "transactionInRecords.F105 = loadTableTemp.F105, " 
	                + "transactionInRecords.F106 = loadTableTemp.F106, " 
	                + "transactionInRecords.F107 = loadTableTemp.F107, " 
	                + "transactionInRecords.F108 = loadTableTemp.F108, " 
	                + "transactionInRecords.F109 = loadTableTemp.F109, " 
	                + "transactionInRecords.F110 = loadTableTemp.F110, " 
	                + "transactionInRecords.F111 = loadTableTemp.F111, " 
	                + "transactionInRecords.F112 = loadTableTemp.F112, " 
	                + "transactionInRecords.F113 = loadTableTemp.F113, " 
	                + "transactionInRecords.F114 = loadTableTemp.F114, " 
	                + "transactionInRecords.F115 = loadTableTemp.F115, " 
	                + "transactionInRecords.F116 = loadTableTemp.F116, " 
	                + "transactionInRecords.F117 = loadTableTemp.F117, " 
	                + "transactionInRecords.F118 = loadTableTemp.F118, " 
	                + "transactionInRecords.F119 = loadTableTemp.F119, " 
	                + "transactionInRecords.F120 = loadTableTemp.F120, " 
	                + "transactionInRecords.F121 = loadTableTemp.F121, " 
	                + "transactionInRecords.F122 = loadTableTemp.F122, " 
	                + "transactionInRecords.F123 = loadTableTemp.F123, " 
	                + "transactionInRecords.F124 = loadTableTemp.F124, " 
	                + "transactionInRecords.F125 = loadTableTemp.F125, " 
	                + "transactionInRecords.F126 = loadTableTemp.F126, " 
	                + "transactionInRecords.F127 = loadTableTemp.F127, " 
	                + "transactionInRecords.F128 = loadTableTemp.F128, " 
	                + "transactionInRecords.F129 = loadTableTemp.F129, " 
	                + "transactionInRecords.F130 = loadTableTemp.F130, " 
	                + "transactionInRecords.F131 = loadTableTemp.F131, " 
	                + "transactionInRecords.F132 = loadTableTemp.F132, " 
	                + "transactionInRecords.F133 = loadTableTemp.F133, " 
	                + "transactionInRecords.F134 = loadTableTemp.F134, " 
	                + "transactionInRecords.F135 = loadTableTemp.F135, " 
	                + "transactionInRecords.F136 = loadTableTemp.F136, " 
	                + "transactionInRecords.F137 = loadTableTemp.F137, " 
	                + "transactionInRecords.F138 = loadTableTemp.F138, " 
	                + "transactionInRecords.F139 = loadTableTemp.F139, " 
	                + "transactionInRecords.F140 = loadTableTemp.F140, " 
	                + "transactionInRecords.F141 = loadTableTemp.F141, " 
	                + "transactionInRecords.F142 = loadTableTemp.F142, " 
	                + "transactionInRecords.F143 = loadTableTemp.F143, " 
	                + "transactionInRecords.F144 = loadTableTemp.F144, " 
	                + "transactionInRecords.F145 = loadTableTemp.F145, " 
	                + "transactionInRecords.F146 = loadTableTemp.F146, " 
	                + "transactionInRecords.F147 = loadTableTemp.F147, " 
	                + "transactionInRecords.F148 = loadTableTemp.F148, " 
	                + "transactionInRecords.F149 = loadTableTemp.F149, " 
	                + "transactionInRecords.F150 = loadTableTemp.F150, " 
	                + "transactionInRecords.F151 = loadTableTemp.F151, " 
	                + "transactionInRecords.F152 = loadTableTemp.F152, " 
	                + "transactionInRecords.F153 = loadTableTemp.F153, " 
	                + "transactionInRecords.F154 = loadTableTemp.F154, " 
	                + "transactionInRecords.F155 = loadTableTemp.F155, " 
	                + "transactionInRecords.F156 = loadTableTemp.F156, " 
	                + "transactionInRecords.F157 = loadTableTemp.F157, " 
	                + "transactionInRecords.F158 = loadTableTemp.F158, " 
	                + "transactionInRecords.F159 = loadTableTemp.F159, " 
	                + "transactionInRecords.F160 = loadTableTemp.F160, " 
	                + "transactionInRecords.F161 = loadTableTemp.F161, " 
	                + "transactionInRecords.F162 = loadTableTemp.F162, " 
	                + "transactionInRecords.F163 = loadTableTemp.F163, " 
	                + "transactionInRecords.F164 = loadTableTemp.F164, " 
	                + "transactionInRecords.F165 = loadTableTemp.F165, " 
	                + "transactionInRecords.F166 = loadTableTemp.F166, " 
	                + "transactionInRecords.F167 = loadTableTemp.F167, " 
	                + "transactionInRecords.F168 = loadTableTemp.F168, " 
	                + "transactionInRecords.F169 = loadTableTemp.F169, " 
	                + "transactionInRecords.F170 = loadTableTemp.F170, " 
	                + "transactionInRecords.F171 = loadTableTemp.F171, " 
	                + "transactionInRecords.F172 = loadTableTemp.F172, " 
	                + "transactionInRecords.F173 = loadTableTemp.F173, " 
	                + "transactionInRecords.F174 = loadTableTemp.F174, " 
	                + "transactionInRecords.F175 = loadTableTemp.F175, " 
	                + "transactionInRecords.F176 = loadTableTemp.F176, " 
	                + "transactionInRecords.F177 = loadTableTemp.F177, " 
	                + "transactionInRecords.F178 = loadTableTemp.F178, " 
	                + "transactionInRecords.F179 = loadTableTemp.F179, " 
	                + "transactionInRecords.F180 = loadTableTemp.F180, " 
	                + "transactionInRecords.F181 = loadTableTemp.F181, " 
	                + "transactionInRecords.F182 = loadTableTemp.F182, " 
	                + "transactionInRecords.F183 = loadTableTemp.F183, " 
	                + "transactionInRecords.F184 = loadTableTemp.F184, " 
	                + "transactionInRecords.F185 = loadTableTemp.F185, " 
	                + "transactionInRecords.F186 = loadTableTemp.F186, " 
	                + "transactionInRecords.F187 = loadTableTemp.F187, " 
	                + "transactionInRecords.F188 = loadTableTemp.F188, " 
	                + "transactionInRecords.F189 = loadTableTemp.F189, " 
	                + "transactionInRecords.F190 = loadTableTemp.F190, " 
	                + "transactionInRecords.F191 = loadTableTemp.F191, " 
	                + "transactionInRecords.F192 = loadTableTemp.F192, " 
	                + "transactionInRecords.F193 = loadTableTemp.F193, " 
	                + "transactionInRecords.F194 = loadTableTemp.F194, " 
	                + "transactionInRecords.F195 = loadTableTemp.F195, " 
	                + "transactionInRecords.F196 = loadTableTemp.F196, " 
	                + "transactionInRecords.F197 = loadTableTemp.F197, " 
	                + "transactionInRecords.F198 = loadTableTemp.F198, " 
	                + "transactionInRecords.F199 = loadTableTemp.F199, " 
	                + "transactionInRecords.F200 = loadTableTemp.F200, " 
	                + "transactionInRecords.F201 = loadTableTemp.F201, " 
	                + "transactionInRecords.F202 = loadTableTemp.F202, " 
	                + "transactionInRecords.F203 = loadTableTemp.F203, " 
	                + "transactionInRecords.F204 = loadTableTemp.F204, " 
	                + "transactionInRecords.F205 = loadTableTemp.F205, " 
	                + "transactionInRecords.F206 = loadTableTemp.F206, " 
	                + "transactionInRecords.F207 = loadTableTemp.F207, " 
	                + "transactionInRecords.F208 = loadTableTemp.F208, " 
	                + "transactionInRecords.F209 = loadTableTemp.F209, " 
	                + "transactionInRecords.F210 = loadTableTemp.F210, " 
	                + "transactionInRecords.F211 = loadTableTemp.F211, " 
	                + "transactionInRecords.F212 = loadTableTemp.F212, " 
	                + "transactionInRecords.F213 = loadTableTemp.F213, " 
	                + "transactionInRecords.F214 = loadTableTemp.F214, " 
	                + "transactionInRecords.F215 = loadTableTemp.F215, " 
	                + "transactionInRecords.F216 = loadTableTemp.F216, " 
	                + "transactionInRecords.F217 = loadTableTemp.F217, " 
	                + "transactionInRecords.F218 = loadTableTemp.F218, " 
	                + "transactionInRecords.F219 = loadTableTemp.F219, " 
	                + "transactionInRecords.F220 = loadTableTemp.F220, " 
	                + "transactionInRecords.F221 = loadTableTemp.F221, " 
	                + "transactionInRecords.F222 = loadTableTemp.F222, " 
	                + "transactionInRecords.F223 = loadTableTemp.F223, " 
	                + "transactionInRecords.F224 = loadTableTemp.F224, " 
	                + "transactionInRecords.F225 = loadTableTemp.F225, " 
	                + "transactionInRecords.F226 = loadTableTemp.F226, " 
	                + "transactionInRecords.F227 = loadTableTemp.F227, " 
	                + "transactionInRecords.F228 = loadTableTemp.F228, " 
	                + "transactionInRecords.F229 = loadTableTemp.F229, " 
	                + "transactionInRecords.F230 = loadTableTemp.F230, " 
	                + "transactionInRecords.F231 = loadTableTemp.F231, " 
	                + "transactionInRecords.F232 = loadTableTemp.F232, " 
	                + "transactionInRecords.F233 = loadTableTemp.F233, " 
	                + "transactionInRecords.F234 = loadTableTemp.F234, " 
	                + "transactionInRecords.F235 = loadTableTemp.F235, " 
	                + "transactionInRecords.F236 = loadTableTemp.F236, " 
	                + "transactionInRecords.F237 = loadTableTemp.F237, " 
	                + "transactionInRecords.F238 = loadTableTemp.F238, " 
	                + "transactionInRecords.F239 = loadTableTemp.F239, " 
	                + "transactionInRecords.F240 = loadTableTemp.F240, " 
	                + "transactionInRecords.F241 = loadTableTemp.F241, " 
	                + "transactionInRecords.F242 = loadTableTemp.F242, " 
	                + "transactionInRecords.F243 = loadTableTemp.F243, " 
	                + "transactionInRecords.F244 = loadTableTemp.F244, " 
	                + "transactionInRecords.F245 = loadTableTemp.F245, " 
	                + "transactionInRecords.F246 = loadTableTemp.F246, " 
	                + "transactionInRecords.F247 = loadTableTemp.F247, " 
	                + "transactionInRecords.F248 = loadTableTemp.F248, " 
	                + "transactionInRecords.F249 = loadTableTemp.F249, " 
	                + "transactionInRecords.F250 = loadTableTemp.F250, " 
	                + "transactionInRecords.F251 = loadTableTemp.F251, " 
	                + "transactionInRecords.F252 = loadTableTemp.F252, " 
	                + "transactionInRecords.F253 = loadTableTemp.F253, " 
	                + "transactionInRecords.F254 = loadTableTemp.F254, " 
	                + "transactionInRecords.F255 = loadTableTemp.F255;");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql); 
		        query.executeUpdate();
				return 0;
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return 1;
			}
		}

		@Override
		@Transactional
		@SuppressWarnings("unchecked")
		public List <Integer> getConfigsForUploadBatch(Integer batchId){
			try {
				
				String sql = ("select distinct configId "
						+ " from batchUploadConfirgurations where batchId = :batchId"
						+ " order by configId;");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("batchId", batchId);
		        
		        List <Integer> configIds = query.list();
		        return configIds;
		        
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return null;
			}
		}

		@Override
		@Transactional
		public Integer updateConfigIdForBatch(Integer batchId, Integer configId) {
			try {
				
				String sql = ("update transactionIn set configId = :configId where batchId = :batchId");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
		        		.setParameter("batchId", batchId).setParameter("configId", configId);
		        query.executeUpdate();
		        return 0;
		        
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return 1;
			}
		}
		
		@Override
		@Transactional
		public Integer loadTransactionTranslatedIn(Integer batchId) {
				try {
					String sql = ("insert into transactionTranslatedIn (configId, transactionInId) select configId, id from transactionIn where batchId = :batchId and configId is not null;");
			        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql); 
			        query.setParameter("batchId", batchId);
			        query.executeUpdate();
					return 0;
				} catch (Exception ex) {
					System.err.println(ex.getClass() + " " + ex.getCause());
					return 1;
				}
		}

		@Override
		@Transactional
		public Integer insertBatchUploadSummary(batchUploads batchUpload, configurationConnection batchTargets) {
			// TODO Auto-generated method stub
			try {
				String sql = ("");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql); 
		        //query.setParameter("batchId", batchId);
		        //query.executeUpdate();
				return 0;
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return 1;
			}
		}

		@Override
		@Transactional
		public Integer insertBatchTargets (Integer batchId, configurationConnection batchTargets){
			// TODO Auto-generated method stub
			try {
				String sql = ("INSERT INTO transactionTarget (batchUploadId, transactionInId, "
						+ " configId, statusId) select " + batchId + ", id, " 
						+ batchTargets.gettargetConfigId()+ ",9 from transactionIn where configId = :configId and "
						+ "batchId = :batchId");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql); 
		        query.setParameter("batchId", batchId);
		        query.setParameter("configId", batchTargets.getsourceConfigId());
		        query.executeUpdate();
				return 0;
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return 1;
			}
		}

		@Override
		@Transactional
		@SuppressWarnings("unchecked")
		public List<configurationConnection> getBatchTargets(Integer batchId) {
			try {
				String sql = ("select sourceConfigId, targetConfigId, id from configurationconnections "
						+ " where sourceconfigId in (select configId from transactionIn where "
						+ " batchId = :batchId);");
		        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(
                        Transformers.aliasToBean(configurationConnection.class)); 
		        query.setParameter("batchId", batchId);
		       
				List<configurationConnection> cc = query.list();
				return cc;
			} catch (Exception ex) {
				System.err.println(ex.getClass() + " " + ex.getCause());
				return null;
			}
		}

		@Override
		public Integer clearBatchUploadSummary(Integer batchId) {
			String sql = "delete from BatchUploadSummary where batchId = :id)"
	                + ";";
	        Query deleteTable = sessionFactory.getCurrentSession().createSQLQuery(sql)
	                .addScalar("id", StandardBasicTypes.INTEGER).setParameter("id", batchId);
	        try {
	            deleteTable.executeUpdate();
	            return 0;
	        } catch (Exception ex) {
	        	System.err.println(ex.getClass() + " " + ex.getCause());
	        	return 1;

	        }
		}
		
		

		
}
