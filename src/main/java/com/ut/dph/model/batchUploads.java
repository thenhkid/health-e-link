/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.dph.model;

import com.ut.dph.validator.NoHtml;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author chadmccue
 */
@Entity
@Table(name = "BATCHUPLOADS")
public class batchUploads {

    @Transient
    private Integer totalTransactions = 0;

    @Transient
    private String statusValue;

    @Transient
    private String usersName;
    
    @Transient
    private String orgName;
    
    @Transient
    private String transportMethod;
    
    @Transient
    private String configName;
    
    @Transient
    private Integer transTotalNotFinal = 10; // set to random number that is not 0
    
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

    @NoHtml
    @Column(name = "ORIGINALFILENAME", nullable = false)
    private String originalFileName;

    @Column(name = "STATUSID", nullable = false)
    private int statusId;

    @Column(name = "STARTDATETIME", nullable = true)
    private Date startDateTime = null;

    @Column(name = "ENDDATETIME", nullable = true)
    private Date endDateTime = null;

    @Column(name = "TOTALRECORDCOUNT", nullable = false)
    private int totalRecordCount = 0;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted = false;

    @Column(name = "ERRORRECORDCOUNT", nullable = false)
    private int errorRecordCount = 0;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATESUBMITTED", nullable = false)
    private Date dateSubmitted = new Date();

    @Column(name = "configId", nullable = true)
    private Integer configId;

    @Column(name = "CONTAINSHEADERROW", nullable = false)
    private boolean containsHeaderRow = false;

    @Column(name = "delimChar", nullable = true)
    private String delimChar;

    @Column(name = "fileLocation", nullable = true)
    private String fileLocation;
    
    @NoHtml
    @Column(name = "originalFolder", nullable = false)
    private String originalFolder;

    public boolean isContainsHeaderRow() {
        return containsHeaderRow;
    }

    public void setContainsHeaderRow(boolean containsHeaderRow) {
        this.containsHeaderRow = containsHeaderRow;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

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

    public void settotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public int gettotalTransactions() {
        return totalTransactions;
    }

    public String getstatusValue() {
        return statusValue;
    }

    public void setstatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public String getusersName() {
        return usersName;
    }

    public void setusersName(String usersName) {
        this.usersName = usersName;
    }

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }
    
    public String getDelimChar() {
        return delimChar;
    }

    public void setDelimChar(String delimChar) {
        this.delimChar = delimChar;
    }
    
    public String getorgName() {
        return orgName;
    }

    public void setorgName(String orgName) {
        this.orgName = orgName;
    }
    
    public String gettransportMethod() {
        return transportMethod;
    }

    public void settransportMethod(String transportMethod) {
        this.transportMethod = transportMethod;
    }
    
    public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public Integer getTransTotalNotFinal() {
		return transTotalNotFinal;
	}

	public void setTransTotalNotFinal(Integer transTotalNotFinal) {
		this.transTotalNotFinal = transTotalNotFinal;
	}

	public String getOriginalFolder() {
		return originalFolder;
	}

	public void setOriginalFolder(String originalFolder) {
		this.originalFolder = originalFolder;
	}

}
