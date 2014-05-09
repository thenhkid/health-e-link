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
public class historyResults {
    
    private String orgName = null;
    private String messageType = null;
    private String msg = null;
    
    
    public String getorgName() {
        return orgName;
    }
    
    public void setorgName(String orgName) {
        this.orgName = orgName;
    }
    
    public String getmessageType() {
        return messageType;
    }
    
    public void setmessageType(String messageType) {
        this.messageType = messageType;
    }
    
    public String getmsg() {
        return msg;
    }
    
    public void setmsg(String msg) {
        this.msg = msg;
    }
    
}
