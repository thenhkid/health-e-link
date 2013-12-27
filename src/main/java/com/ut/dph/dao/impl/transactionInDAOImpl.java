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
import org.hibernate.Query;
import org.hibernate.SessionFactory;
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
    
}
