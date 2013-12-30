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
import javax.persistence.Temporal;

/**
 *
 * @author chadmccue
 */
@Entity
@Table(name = "BATCHUPLOADS")
public class batchUploads {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    
    @Column(name = "ORGID", nullable = false)
    private int orgId;
    
    @Column(name = "PROVIDERID", nullable = true)
    private int providerId = 0;
    
    @Column(name = "USERID", nullable = false)
    private int userId;
    
    @Column(name = "UTBATCHCONFNAME", nullable = true)
    private String utBatchConfName = null;
    
    @Column(name = "UTBATCHNAME", nullable = false)
    private String utBatchName;
    
    @Column(name = "TRANSPORTMETHODID", nullable = false)
    private int transportMethodId;
    
    @Column(name = "ORIGINALFILENAME", nullable = false)
    private String originalFileName;
    
    @Column(name = "STATUSID", nullable = false)
    private int statusId;
    
    @Column(name = "STARTDATETIME", nullable = true)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDateTime = null;
    
    @Column(name = "ENDDATETIME", nullable = true)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDateTime = null;
    
    @Column(name = "TOTALRECORDCOUNT", nullable = false)
    private int totalRecordCount = 0;
    
    @Column(name = "DELETED", nullable = false)
    private boolean deleted = false;
    
    @Column(name = "ERRORRECORDCOUNT", nullable = false)
    private int errorRecordCount = 0;
    
    @Column(name = "DATESUBMITTED", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateSubmitted = new Date();
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }
    
    public int getproviderId() {
        return providerId;
    }

    public void setproviderId(int providerId) {
        this.providerId = providerId;
    }
    
    public int getuserId() {
        return userId;
    }

    public void setuserId(int userId) {
        this.userId = userId;
    }
    
    public String getutBatchConfName() {
        return utBatchConfName;
    }
    
    public void setutBatchConfName(String utBatchConfName) {
        this.utBatchConfName = utBatchConfName;
    }
    
    public String getutBatchName() {
        return utBatchName;
    }
    
    public void setutBatchName(String utBatchName) {
        this.utBatchName = utBatchName;
    }
    
    public int gettransportMethodId() {
        return transportMethodId;
    }
    
    public void settransportMethodId(int transportMethodId) {
        this.transportMethodId = transportMethodId;
    }
    
    public String getoriginalFileName() {
        return originalFileName;
    }
    
    public void setoriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
    
    public int getstatusId() {
        return statusId;
    }
    
    public void setstatusId(int statusId) {
        this.statusId = statusId;
    }
    
    public Date getstartDateTime() {
        return startDateTime;
    }
    
    public void setstartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }
    
    public Date getendDateTime() {
        return endDateTime;
    }
    
    public void setendDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }
    
    public int gettotalRecordCount() {
        return totalRecordCount;
    }
    
    public void settotalRecordCount(int totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }
    
    public boolean getdeleted() {
        return deleted;
    }
    
    public void setdeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    public int geterrorRecordCount() {
        return errorRecordCount;
    }
    
    public void seterrorRecordCount(int errorRecordCount) {
        this.errorRecordCount = errorRecordCount;
    }
    
    public Date getdateSubmitted() {
       return dateSubmitted;
   }
  
   
}
