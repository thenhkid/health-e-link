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
    
    private int orgId;
    private String orgName = null;
    private int messageTypeId;
    private String messageType = null;
    private String msg = null;
    private int type;
    boolean showDetails = false;
    
    public int getorgId() {
        return orgId;
    }
    
    public void setorgId(int orgId) {
        this.orgId = orgId;
    }
    
    public String getorgName() {
        return orgName;
    }
    
    public void setorgName(String orgName) {
        this.orgName = orgName;
    }
    
    public int getmessageTypeId() {
        return messageTypeId;
    }
    
    public void setmessageTypeId(int messageTypeId) {
        this.messageTypeId = messageTypeId;
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
    
    public int gettype() {
        return type;
    }
    
    public void settype(int type) {
        this.type = type;
    }
    
    public boolean getshowDetails() {
        return showDetails;
    }
    
    public void setshowDetails(boolean showDetails) {
        this.showDetails = showDetails;
    }
    
}
