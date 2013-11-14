package com.ut.dph.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Entity
@Table(name="CONFIGURATIONTRANSPORTDETAILS")
public class configurationTransport {
	
	  @Transient
	  private List<configurationFormFields> fields = null;
	
	  @Transient
	  private CommonsMultipartFile file = null; 
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  @Column(name="ID", nullable = false)
	  private int id;
	  
	  @Column(name="CONFIGID", nullable = false)
	  private int configId;
	  
	  @Column(name="TRANSPORTMETHOD", nullable = false)
	  private int transportMethod;
	  
	  @Column(name="FILENAME", nullable = true)
	  private String fileName;
	  
	  @Digits(integer=4, fraction=0, message="Wrong Number")
	  @Column(name="MESSAGETYPECOLNO", nullable = true)
	  private int messageTypeColNo;
	  
	  @Column(name="MESSAGETYPECUSTOMVAL", nullable= true)
	  private String messageTypeCustomVal = null;
	  
	  @Digits(integer=4, fraction=0, message="Wrong Number")
	  @Column(name="TARGETORGCOLNO", nullable = true)
	  private int targetOrgColNo;
	  
	  @Column(name="FILETYPE", nullable = false)
	  private int fileType;
	  
	  @Column(name="FILEDELIMITER", nullable = false)
	  private int fileDelimiter;
	  
	  @Column(name="CONTAINSHEADER", nullable = false)
	  private boolean containsHeader = false;
	  
	  public int getId() {
		  return id;
	  }
		  
	  public void setId(int id) {
		  this.id = id;
	  }
	  
	  public int getconfigId() {
		  return configId;
	  }
		  
	  public void setconfigId(int configId) {
		  this.configId = configId;
	  }
	  
	  public int gettransportMethod() {
		  return transportMethod;
	  }
	  
	  public void settransportMethod(int transportMethod) {
		  this.transportMethod= transportMethod;
	  }
	  
	  public int getmessageTypeColNo() {
		  return messageTypeColNo;
	  }
	  
	  public void setmessageTypeColNo(int messageTypeColNo) {
		  this.messageTypeColNo = messageTypeColNo;
	  }
	  
	  public String getmessageTypeCustomVal() {
		  return messageTypeCustomVal;
	  }
	  
	  public void setmessageTypeCustomVal(String messageTypeCustomVal) {
		  this.messageTypeCustomVal = messageTypeCustomVal;
	  }
	  
	  public int gettargetOrgColNo() {
		  return targetOrgColNo;
	  }
	  
	  public void settargetOrgColNo(int targetOrgColNo) {
		  this.targetOrgColNo = targetOrgColNo;
	  }
	  
	  public int getfileType() {
		  return fileType;
	  }
	  
	  public void setfileType(int fileType) {
		  this.fileType = fileType;
	  }
	  
	  public int getfileDelimiter() {
		  return fileDelimiter;
	  }
	  
	  public void setfileDelimiter(int fileDelimiter) {
		  this.fileDelimiter = fileDelimiter;
	  }
	  
	  public String getfileName() {
		  return fileName;
	  }
		  
	  public void setfileName(String fileName) {
		  this.fileName = fileName;
	  }
	  
	  public CommonsMultipartFile getFile() {  
		  return file;  
	  }  
	  
	  public void setFile(CommonsMultipartFile file) {  
	 	this.file = file;  
	  }  
	  
	  public List<configurationFormFields> getFields() {
		  return fields;
	  }
	  
	  public void setFields(List<configurationFormFields> fields) {
		  this.fields = fields;
	  }

}
