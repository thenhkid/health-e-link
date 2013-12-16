/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service.impl;

import com.ut.dph.dao.transactionInDAO;
import com.ut.dph.model.fieldSelectOptions;
import org.springframework.stereotype.Service;
import com.ut.dph.service.transactionInManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
    public List<fieldSelectOptions> getFieldSelectOptions(int fieldId, int configId, int transportMethod) {
        return transactionInDAO.getFieldSelectOptions(fieldId, configId, transportMethod);
    }
    
}
