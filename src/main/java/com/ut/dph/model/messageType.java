package com.ut.dph.model;

import java.sql.Date;

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
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  @Column(name="ID", nullable = false)
	  private int id;
	  
	  @Column(name="STATUS", nullable = false)
	  private boolean status;
	  
	  @Column(name="DATECREATED", nullable = false)
	  private Date dateCreated;
	  
	  @NotEmpty
	  @Column(name="NAME", nullable = false)
	  private String name;
	  
	  @NotEmpty
	  @Column(name="TEMPLATEFILE", nullable = false)
	  private String templateFile;
	  
	  public int getId() {
	    return id;
	  }
	  
	  public void setId(int id) {
	    this.id = id;
	  }
	
	  public boolean getStatus() {
	    return status;
	  }
	  
	  public void setStatus(boolean status) {
	    this.status = status;
	  }
	  
	  public Date getdateCreated() {
	    return dateCreated;
	  }
	  
	  public void setdateCreated(Date dateCreated) {
	    this.dateCreated = dateCreated;
	  }
	  
	  public String getName() {
		  return name;		  
	  }
	  
	  public void setName(String name) {
		  this.name = name;
	  }
	  
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

}
