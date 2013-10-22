package com.ut.dph.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="INFO_PROVIDERADDRESSES")
public class providerAddress {
	
	private int id;
	private String type;
	private String line1;
	private String line2;
	private String city;
	private String county;
	private String postalCode;
	private String priority;
	private String status;
	private Date dateAdded = new Date();
	private Date dateModified = new Date();
	private int providerId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable = false)
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	@Column(name="TYPE", nullable = true)
	public String getType() {
		return this.type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	@Column(name="LINE1", nullable = true)
	public String getLine1() {
		return this.line1;
	}
	
	public void setLine1(String line1){
		this.line1 = line1;
	}
	
	@Column(name="LINE2", nullable = true)
	public String getLine2() {
		return this.line2;
	}
	
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	
	@Column(name="CITY", nullable = true)
	public String getCity() {
		return this.city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	@Column(name="COUNTY", nullable = true)
	public String getCounty() {
		return this.county;
	}
	
	public void setCounty(String county) {
		this.county = county;
	}
	
	@Column(name="POSTALCODE", nullable = true)
	public String getPostalCode() {
		return this.postalCode;
	}
	
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	@Column(name="PRIORITY", nullable = true)
	public String getPriority() {
		return this.priority;
	}
	
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	@Column(name="STATUS", nullable = true)
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="DATEADDED", nullable = true)
	public Date getDateAdded() {
		 return dateAdded;
	}
	  
	public void setDateAdded(Date dateAdded) {
		 this.dateAdded = dateAdded;
	}
	
	@Column(name="DATEMODIFIED", nullable = true)
	public Date getDateModified() {
		 return dateModified;
	}
	  
	public void setDateModified(Date dateModified) {
		 this.dateModified = dateModified;
	}
	
	@Column(name="PROVIDERID", nullable = false)
	public int getProviderId() {
		return this.providerId;
	}
	
	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}
	
}
