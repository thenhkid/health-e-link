/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.dao.impl;

import com.ut.dph.dao.transactionInDAO;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.fieldSelectOptions;
import com.ut.dph.model.transactionIn;
import com.ut.dph.model.transactionInRecords;
import java.util.ArrayList;
import java.util.Iterator;
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
public class transactionInDAOImpl implements transactionInDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    /**
     * The 'getFieldValue' function will return the value saved for the passed in tableName, 
     * tableCol and idValue.
     * 
     * @param	tableName	The name of the table to query
     * @param   tableCol        The column name of to return
     * @param   idValue         The id value of the row to search
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
     * The 'getFieldSelectOptions' function will return a list of values to populate the 
     * field select box.
     * 
     * @param  fieldId   The fieldId to search on
     * @param  configId  The configuration Id to search on
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
        
        while(it.hasNext()) {
            Object row[] = (Object[]) it.next();
            crosswalkId = (Integer) row[0];
            
            Query crosswalkData = sessionFactory.getCurrentSession().createSQLQuery("SELECT sourceValue, descValue FROM rel_crosswalkData where crosswalkId = :crosswalkId");
            crosswalkData.setParameter("crosswalkId", crosswalkId);
            List crosswalkDataList = crosswalkData.list();
            
            Iterator cwDataIt = crosswalkDataList.iterator();
            while(cwDataIt.hasNext()) {
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
     * @param   batchUpload     The object that will hold the new batch info
     * 
     * @table   batchUploads
     * 
     * @return  This function returns the batchId for the newly inserted batch
     */
    @Override
    @Transactional
    public Integer submitBatchUpload(batchUploads batchUpload) {
        
        Integer batchId = null;

        batchId = (Integer) sessionFactory.getCurrentSession().save(batchUpload);

        return batchId;
        
    }
    
    /**
     * The 'submitTransactionIn' function will submit the new transaction for the batch.
     * 
     * @param   transactionIn     The object that will hold the new transaction info
     * 
     * @table   transactionIn
     * 
     * @return  This function returns the transactioInId for the newly inserted transaction
     */
    @Override
    @Transactional
    public Integer submitTransactionIn(transactionIn transactionIn) {
        Integer transactioInId = null;

        transactioInId = (Integer) sessionFactory.getCurrentSession().save(transactionIn);

        return transactioInId;
    }
    
    /**
     * The 'submitTransactionInRecords'
     */
    @Override
    @Transactional
    public Integer submitTransactionInRecords(transactionInRecords records) {
        Integer transactioInRecordId = null;

        transactioInRecordId = (Integer) sessionFactory.getCurrentSession().save(records);

        return transactioInRecordId;
    }
    
    /**
     * The 'submitTransactionTranslatedInRecords' function will copy the records from the 
     * transactionInRecords table to the transactionTranslatedIn table so the processing
     * can begin on the field values.
     * 
     * @param transactionRecordId The id of the inserted record in the transactionInRecords table
     * 
     * @return This function does not return anything.
     */
    @Override
    @Transactional
    public void submitTransactionTranslatedInRecords(int transactionRecordId) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO transactionTranslatedIn (" +
                "transactionInId, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23, f24, f25, f26, f27, f28, f29, f30, f31," +
                "f32, f33, f34, f35, f36, f37, f38, f39, f40, f41, f42, f43, f44, f45, f46, f47, f48, f49, f50, f51, f52, f53, f54, f55, f56, f57, f58, f59, f60, f61, f62, f63, f64," +
                "f65, f66, f67, f68, f69, f70, f71, f72, f73, f74, f75, f76, f77, f78, f79, f80, f81, f82, f83, f84, f85, f86, f87, f88, f89, f90, f91, f92, f93, f94, f95, f96, f97, f98," +
                "f99, f100, f101, f102, f103, f104, f105, f106, f107, f108, f109, f110, f111, f112, f113, f114, f115, f116, f117, f118, f119, f120, f121, f122, f123, f124, f125, f126, f127, f128, f129, " +
                "f130, f131, f132, f133, f134, f135, f136, f137, f138, f139, f140, f141, f142, f143, f144, f145, f146, f147, f148, f149, f150, f151, f152, f153, f154, f155, f156, f157, f158, f159," +
                "f160, f161, f162, f163, f164, f165, f166, f167, f168, f169, f170, f171, f172, f173, f174, f175, f176, f177, f178, f179, f180, f181, f182, f183, f184, f185, f186, f187, f188, f189," +
                "f190, f191, f192, f193, f194, f195, f196, f197, f198, f199, f200, f201, f202, f203, f204, f205, f206, f207, f208, f209, f210, f211, f212, f213, f214, f215, f216, f217, f218, f219," +
                "f220, f221, f222, f223, f224, f225, f226, f227, f228, f229, f230, f231, f232, f233, f234, f235, f236, f237, f238, f239, f240, f241, f242, f243, f244, f245, f246, f247, f248, f249," +
                "f250, f251, f252, f253, f254, f255" +
                "SELECT transactionInId, f1 f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23, f24, f25, f26, f27, f28, f29, f30, f31," +
                "f32, f33, f34, f35, f36, f37, f38, f39, f40, f41, f42, f43, f44, f45, f46, f47, f48, f49, f50, f51, f52, f53, f54, f55, f56, f57, f58, f59, f60, f61, f62, f63, f64," +
                "f65, f66, f67, f68, f69, f70, f71, f72, f73, f74, f75, f76, f77, f78, f79, f80, f81, f82, f83, f84, f85, f86, f87, f88, f89, f90, f91, f92, f93, f94, f95, f96, f97, f98," +
                "f99, f100, f101, f102, f103, f104, f105, f106, f107, f108, f109, f110, f111, f112, f113, f114, f115, f116, f117, f118, f119, f120, f121, f122, f123, f124, f125, f126, f127, f128, f129, " +
                "f130, f131, f132, f133, f134, f135, f136, f137, f138, f139, f140, f141, f142, f143, f144, f145, f146, f147, f148, f149, f150, f151, f152, f153, f154, f155, f156, f157, f158, f159," +
                "f160, f161, f162, f163, f164, f165, f166, f167, f168, f169, f170, f171, f172, f173, f174, f175, f176, f177, f178, f179, f180, f181, f182, f183, f184, f185, f186, f187, f188, f189," +
                "f190, f191, f192, f193, f194, f195, f196, f197, f198, f199, f200, f201, f202, f203, f204, f205, f206, f207, f208, f209, f210, f211, f212, f213, f214, f215, f216, f217, f218, f219," +
                "f220, f221, f222, f223, f224, f225, f226, f227, f228, f229, f230, f231, f232, f233, f234, f235, f236, f237, f238, f239, f240, f241, f242, f243, f244, f245, f246, f247, f248, f249," +
                "f250, f251, f252, f253, f254, f255" +
                "FROM transactionInRecords where id = :id");
        query.setParameter("id", transactionRecordId);
        query.executeUpdate();
    }
    
    /**
     * The 'getpendingTransactions' function will return a list of pending
     * transactions for the organization passed in.
     * 
     * @param orgId The organization Id to find pending transactions for.
     * 
     * @return The function will return a list of pending transactions
     */
    @Override
    @Transactional
    public List<transactionIn> getpendingTransactions(int orgId) {
        
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(transactionIn.class);
        criteria.add(Restrictions.eq("statusId", 9));
        
        List<Integer> batchList = new ArrayList<Integer>();
        Criteria findBatches = sessionFactory.getCurrentSession().createCriteria(batchUploads.class);
        findBatches.add(Restrictions.eq("orgId", orgId));
        findBatches.add(Restrictions.eq("statusId", 6));
        
        List<batchUploads> batches = findBatches.list();

        for (batchUploads batch : batches) {
            batchList.add(batch.getId());
        }

        if (batchList.isEmpty()) {
            batchList.add(0);
        }
        
        criteria.add(Restrictions.in("batchId", batchList));
        
        criteria.addOrder(Order.desc("dateCreated"));
        
        return criteria.list();
    }
    
    /**
     * The 'getUploadBatch' function will return the batch details for the
     * passed in batch id.
     * 
     * @param batchId   The id of the batch to return.  
     */
    @Override
    @Transactional
    public batchUploads getUploadBatch(int batchId) {
         return (batchUploads) sessionFactory.getCurrentSession().get(batchUploads.class, batchId);
        
    }
    
}
