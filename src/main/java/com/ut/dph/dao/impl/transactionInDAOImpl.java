/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.dao.impl;

import com.ut.dph.dao.transactionInDAO;
import com.ut.dph.model.fieldSelectOptions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<fieldSelectOptions> getFieldSelectOptions(int fieldId, int configId, int transportMethod) {
        
        List<fieldSelectOptions> fieldSelectOptions = new ArrayList<fieldSelectOptions>();
       
        Query findCrosswalks = sessionFactory.getCurrentSession().createSQLQuery("SELECT crosswalkId, id FROM rel_configurationDataTranslations where configId = :configId and fieldId = :fieldId and transportMethod = :transportMethod");
        findCrosswalks.setParameter("configId", configId);
        findCrosswalks.setParameter("fieldId", fieldId);
        findCrosswalks.setParameter("transportMethod", transportMethod);
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
    
}
