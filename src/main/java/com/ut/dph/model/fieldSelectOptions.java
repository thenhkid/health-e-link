/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.model;

/**
 *
 * @author chadmccue
 */
public class fieldSelectOptions {
    
    private int optionValue;
    private String optionDesc = null;
    
    public int getoptionValue() {
        return optionValue;
    }

    public void setoptionValue(int optionValue) {
        this.optionValue = optionValue;
    }
    
    public String getoptionDesc() {
        return optionDesc;
    }
    
    public void setoptionDesc(String optionDesc) {
        this.optionDesc = optionDesc;
    }
}
