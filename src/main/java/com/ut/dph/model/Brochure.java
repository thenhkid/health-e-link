package com.ut.dph.model;

import java.util.Date;

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
@Table(name="BROCHURES")
public class Brochure {
	
	@Transient
	private CommonsMultipartFile file; 
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable = false)
	private int id;
	
	@Column(name="ORGID", nullable = false)
	private int orgId;
	
	@NotEmpty
	@Column(name="TITLE", nullable = false)
	private String title;
	
	@Column(name="FILENAME", nullable = false)
	private String fileName;
	
	@Column(name="DATECREATED", nullable = true)
	private Date dateCreated = new Date();
	
	public int getId() {
	    return id;
	}
	  
	public void setId(int id) {
	    this.id = id;
	}
	
	public int getOrgId(){
		return orgId;
	}
	  
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	
	public String getTitle() {
		return title;
	}
	  
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getfileName() {
		return fileName;
	}
	  
	public void setfileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	  
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public CommonsMultipartFile getFile() {  
		return file;  
	}  
	  
	public void setFile(CommonsMultipartFile file) {  
	  this.file = file;  
	}  

}
