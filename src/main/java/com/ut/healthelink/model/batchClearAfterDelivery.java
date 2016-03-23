package com.ut.healthelink.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "batchClearAfterDelivery")
public class batchClearAfterDelivery {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;

    @Column(name = "batchUploadId", nullable = false)
    private int batchUploadId = 0;

    /** 0 new 1 start 2 done **/
    @Column(name = "statusId", nullable = false)
    private int statusId = 0;

    @Column(name = "batchDLId", nullable = false)
    private int batchDLId = 0;
    
    @Column(name = "transactionInId", nullable = false)
    private int transactionInId = 0;
    
    @Column(name = "transactionTargetId", nullable = false)
    private int transactionTargetId = 0;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATECREATED", nullable = false)
    private Date dateCreated = new Date();
    
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "dateCompleted", nullable = true)
    private Date dateCompleted;
    
    

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBatchUploadId() {
		return batchUploadId;
	}

	public void setBatchUploadId(int batchUploadId) {
		this.batchUploadId = batchUploadId;
	}

	public int getBatchDLId() {
		return batchDLId;
	}

	public void setBatchDLId(int batchDLId) {
		this.batchDLId = batchDLId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public int getTransactionInId() {
		return transactionInId;
	}

	public void setTransactionInId(int transactionInId) {
		this.transactionInId = transactionInId;
	}

	public int getTransactionTargetId() {
		return transactionTargetId;
	}

	public void setTransactionTargetId(int transactionTargetId) {
		this.transactionTargetId = transactionTargetId;
	}

	public Date getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

}
