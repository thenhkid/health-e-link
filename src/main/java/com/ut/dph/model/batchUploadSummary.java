/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author chadmccue
 */
@Entity
@Table(name = "BATCHUPLOADSUMMARY")
public class batchUploadSummary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    
    @Column(name = "BATCHID", nullable = false)
    private int batchId;
    
    @Column(name = "TRANSACTIONINID", nullable = false)
    private int transactionInId;
    
    @Column(name = "SOURCEORGID", nullable = false)
    private int sourceOrgId;
    
    @Column(name = "TARGETORGID", nullable = false)
    private int targetOrgId;
    
    @Column(name = "MESSAGETYPEID", nullable = false)
    private int messageTypeId;
    
    @Column(name = "SOURCECONFIGID", nullable = false)
    private int sourceConfigId;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getbatchId() {
        return batchId;
    }

    public void setbatchId(int batchId) {
        this.batchId = batchId;
    }
    
    public int gettransactionInId() {
        return transactionInId;
    }

    public void settransactionInId(int transactionInId) {
        this.transactionInId = transactionInId;
    }
    
    public int getsourceOrgId() {
        return sourceOrgId;
    }

    public void setsourceOrgId(int sourceOrgId) {
        this.sourceOrgId = sourceOrgId;
    }
    
    public int gettargetOrgId() {
        return targetOrgId;
    }

    public void settargetOrgId(int targetOrgId) {
        this.targetOrgId = targetOrgId;
    }
    
    public int getmessageTypeId() {
        return messageTypeId;
    }

    public void setmessageTypeId(int messageTypeId) {
        this.messageTypeId = messageTypeId;
    }
    
    public int getsourceConfigId() {
        return sourceConfigId;
    }

    public void setsourceConfigId(int sourceConfigId) {
        this.sourceConfigId = sourceConfigId;
    }
    
}