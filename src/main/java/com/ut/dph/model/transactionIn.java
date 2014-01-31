/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.model;

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
@Table(name = "TRANSACTIONIN")
public class transactionIn {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    
    @Column(name = "BATCHID", nullable = false)
    private int batchId;
    
    @Column(name = "CONFIGID", nullable = false)
    private int configId;
    
    @Column(name = "STATUSID", nullable = false)
    private int statusId;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATECREATED", nullable = false)
    private Date dateCreated = new Date();
    
    @Column(name = "ORIGTRANSACTIONINID", nullable = false)
    private int origtransactionInId = 0;
    
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
    
    public int getorigtransactionInId() {
        return origtransactionInId;
    }
    
    public void setorigtransactionInId(int origtransactionInId) {
        this.origtransactionInId = origtransactionInId;
    }
  
}
