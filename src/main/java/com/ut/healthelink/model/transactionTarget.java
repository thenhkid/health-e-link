/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author chadmccue
 */
@Entity
@Table(name = "TRANSACTIONTARGET")
public class transactionTarget {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    
    @Column(name = "BATCHUPLOADID", nullable = false)
    private int batchUploadId;
    
    @Column(name = "TRANSACTIONINID", nullable = false)
    private int transactionInId;
    
    @Column(name = "BATCHDLID", nullable = true)
    private int batchDLId = 0;
    
    @Column(name = "CONFIGID", nullable = false)
    private int configId;
    
    @Column(name = "STATUSID", nullable = false)
    private int statusId;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATECREATED", nullable = false)
    private Date dateCreated = new Date();
    
    @Column(name = "INTERNALSTATUSID", nullable = true)
    private int internalStatusId = 0;
    
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

    public int getbatchUploadId() {
        return batchUploadId;
    }

    public void setbatchUploadId(int batchUploadId) {
        this.batchUploadId = batchUploadId;
    }
    
    public int gettransactionInId() {
        return transactionInId;
    }

    public void settransactionInId(int transactionInId) {
        this.transactionInId = transactionInId;
    }
    
    public int getbatchDLId() {
        return batchDLId;
    }

    public void setbatchDLId(int batchDLId) {
        this.batchDLId = batchDLId;
    }
    
    public int getconfigId() {
        return configId;
    }

    public void setconfigId(int configId) {
        this.configId = configId;
    }
    
    public int getstatusId() {
        return statusId;
    }

    public void setstatusId(int statusId) {
        this.statusId = statusId;
    }
    
    public Date getdateCreated() {
        return dateCreated;
    }
    
    public void setdateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public int getinternalStatusId() {
        return internalStatusId;
    }
    
    public void setinternalStatusId(int internalStatusId) {
        this.internalStatusId = internalStatusId;
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
