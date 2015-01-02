/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.model;

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
@Table(name = "BATCHDOWNLOADSUMMARY")
public class batchDownloadSummary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    
    @Column(name = "BATCHID", nullable = false)
    private int batchId;
    
    @Column(name = "TRANSACTIONTARGETID", nullable = false)
    private int transactionTargetId;
    
    @Column(name = "SOURCEORGID", nullable = false)
    private int sourceOrgId;
    
    @Column(name = "TARGETORGID", nullable = false)
    private int targetOrgId;
    
    @Column(name = "MESSAGETYPEID", nullable = false)
    private int messageTypeId;
    
    @Column(name = "TARGETCONFIGID", nullable = false)
    private int targetConfigId;
    
    @Column(name = "targetSubOrgId", nullable = false)
    private int targetSubOrgId = 0;
    
    @Column(name = "sourceSubOrgId", nullable = false)
    private int sourceSubOrgId = 0;
    
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
    
    public int gettransactionTargetId() {
        return transactionTargetId;
    }

    public void settransactionTargetId(int transactionTargetId) {
        this.transactionTargetId = transactionTargetId;
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
    
    public int gettargetConfigId() {
        return targetConfigId;
    }

    public void settargetConfigId(int targetConfigId) {
        this.targetConfigId = targetConfigId;
    }

	public int getTargetSubOrgId() {
		return targetSubOrgId;
	}

	public void setTargetSubOrgId(int targetSubOrgId) {
		this.targetSubOrgId = targetSubOrgId;
	}

	public int getSourceSubOrgId() {
		return sourceSubOrgId;
	}

	public void setSourceSubOrgId(int sourceSubOrgId) {
		this.sourceSubOrgId = sourceSubOrgId;
	}
    
    
    
}
