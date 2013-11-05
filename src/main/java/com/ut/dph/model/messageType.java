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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Entity
@Table(name="MESSAGETYPES")
public class messageType {
	
	  @Transient
	  private CommonsMultipartFile file; 
	  
	  @Transient
	  private List<messageTypeFormFields> fields = null;
	  
	  @Transient
	  private List<messageTypeDataTranslations> dataTranslations = null;
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  @Column(name="ID", nullable = false)
	  private int id;
	  
	  public int getId() {
	    return id;
	  }
	  
	  public void setId(int id) {
	    this.id = id;
	  }
	  
	  @Column(name="STATUS", nullable = false)
	  private int status;
	  
	  public int getStatus() {
	    return status;
	  }
	  
	  public void setStatus(int status) {
	    this.status = status;
	  }
	  
	  @Column(name="DATECREATED", nullable = true)
	  private Date dateCreated = new Date();
	  public Date getdateCreated() {
	    return dateCreated;
	  }
	  
	  public void setdateCreated(Date dateCreated) {
	    this.dateCreated = dateCreated;
	  }
  
	  @NotEmpty
	  @Column(name="NAME", nullable = false)
	  private String name;
	  public String getName() {
		  return name;		  
	  }
	  
	  public void setName(String name) {
		  this.name = name;
	  }
	  
	  @Column(name="TEMPLATEFILE", nullable = true)
	  private String templateFile;
	  public String getTemplateFile() {
		  return templateFile;
	  }
	  
	  public void setTemplateFile(String templateFile) {
		  this.templateFile = templateFile;
	  }
	  
	  
	  public CommonsMultipartFile getFile() {  
		  return file;  
	  }  
		  
	  public void setFile(CommonsMultipartFile file) {  
		  this.file = file;  
	  }  
	  
	  public List<messageTypeFormFields> getFields() {
		  return fields;
	  }
	  
	  public void setFields(List<messageTypeFormFields> fields) {
		  this.fields = fields;
	  }
	  
	  public List<messageTypeDataTranslations> getTranslations() {
		  return dataTranslations;
	  }
	  
	  public void setTranslations(List<messageTypeDataTranslations> dataTranslations) {
		  this.dataTranslations = dataTranslations;
	  }

}
