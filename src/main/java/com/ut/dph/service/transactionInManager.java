/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service;

import com.ut.dph.model.fieldSelectOptions;
import java.util.List;

/**
 *
 * @author chadmccue
 */
public interface transactionInManager {
    
    String getFieldValue(String tableName, String tableCol, int idValue);
    
    List<fieldSelectOptions> getFieldSelectOptions(int fieldId, int configId, int transportMethod);
    
    
}
