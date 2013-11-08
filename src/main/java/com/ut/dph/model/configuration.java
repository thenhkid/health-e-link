package com.ut.dph.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="CONFIGURATIONS")
public class configuration {
	
	  @Transient
	  private String orgName = null;
	  
	  @Transient
	  private String messageTypeName = null;
	  
	  @Transient
	  private List<Organization> connections = null;

	  @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  @Column(name="ID", nullable = false)
	  private int id;
	  
	  @Column(name="orgId", nullable = false)
	  private int orgId;
	  
	  @NotEmpty
	  @Column(name="configName", nullable = false)
	  private String configName;
	  
	  @Column(name="DATECREATED", nullable = true)
	  private Date dateCreated = new Date();
	  
	  @Column(name="STATUS", nullable = false)
	  private boolean status = false;
	  
	  @Column(name="TYPE", nullable = false)
	  private int type = 1;
	  
	  @Column(name="MESSAGETYPEID", nullable = false)
	  private int messageTypeId = 0;
	  
	  public int getId() {
	    return id;
	  }
	  
	  public void setId(int id) {
	    this.id = id;
	  }
	
	  public int getorgId() {
	    return orgId;
	  }
	  
	  public void setorgId(int orgId) {
	    this.orgId = orgId;
	  }
	  
	  public String getConfigName() {
	    return configName;
	  }
	  
	  public void setConfigName(String configName) {
	    this.configName = configName;
	  }
	  
	  public Date getDateCreated() {
		  return dateCreated;
	  }
	  
	  public void setDateCreated(Date dateCreated) {
		  this.dateCreated = dateCreated;
	  }
	  
	  public boolean getStatus() {
		  return status;
	  }
	  
	  public void setStatus(boolean status) {
		  this.status = status;
	  }
	  
	  public int getType() {
	    return type;
	  }
	  
	  public void setType(int type) {
	    this.type = type;
	  }
	  
	  public int getMessageTypeId() {
	    return messageTypeId;
	  }
	  
	  public void setMessageTypeId(int messageTypeId) {
	    this.messageTypeId = messageTypeId;
	  }
	  
	  public List<Organization> getConnections() {
		  return connections;
	  }
	  
	  public void setConnections(List<Organization> connections) {
		  this.connections = connections;
	  }
	  
	  public String getOrgName() {
	    return orgName;
	  }
	  
	  public void setOrgName(String orgName) {
	    this.orgName = orgName;
	  }
	  
	  public String getMessageTypeName() {
	    return messageTypeName;
	  }
	  
	  public void setMessageTypeName(String messageTypeName) {
	    this.messageTypeName = messageTypeName;
	  }
}
