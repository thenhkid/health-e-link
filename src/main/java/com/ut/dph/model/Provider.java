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

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.ut.dph.validator.Phone;

@Entity
@Table(name="INFO_PROVIDERS")
public class Provider {
	
	@Transient
	private List<providerAddress> providerAddresses = null;
	
	@Transient
	private List<providerIdNum> providerIds = null;
	
	
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
	
	@Column(name="ORGID", nullable = false)
	private int orgId;
	
	public int getOrgId(){
		return orgId;
	}
	  
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	
	@NotEmpty
	@Column(name="FIRSTNAME", nullable = false)
	private String firstName;
	
	public String getFirstName() {
		return firstName;
	}
	  
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	  
	@NotEmpty
	@Column(name="LASTNAME", nullable = true)
	private String lastName;
	
	public String getLastName() {
		return lastName;
	}
	  
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Email
	@Column(name="EMAIL", nullable = true)
	private String email;
	
	public String getEmail() {
		return email;
	}
	  
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name="URL", nullable = true)
	private String website;
	
	public String getWebsite() {
		return website;
	}
	  
	public void setWebsite(String website) {
		this.website = website;
	}
	
	@NotEmpty
	@Column(name="GENDER", nullable = true)
	private String gender;
	
	public String getGender() {
		return gender;
	}
	  
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Column(name="SPECIALTY", nullable = true)
	private String specialty;
	
	public String getSpecialty() {
		return specialty;
	}
	  
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	
	@NotEmpty @Phone
	@Column(name="PHONE1", nullable = true)
	private String phone1;
	
	public String getPhone1() {
		return phone1;
	}
	  
	public void setPhone1(String phone) {
		this.phone1 = phone;
	}
	
	@Phone
	@Column(name="PHONE2", nullable = true)
	private String phone2;
	
	public String getPhone2() {
		return phone2;
	}
	  
	public void setPhone2(String phone) {
		this.phone2 = phone;
	}
	  
	@Phone
	@Column(name="FAX", nullable = true)
	private String fax;
	
	public String getFax() {
		return fax;
	}
	 
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	@Column(name="STATUS", nullable = false)
	private boolean status = false;
	
	public boolean getStatus() {
		return status;
	}
	  
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	@Column(name="DATECREATED", nullable = true)
	private Date dateCreated = new Date();
	
	public Date getDateCreated() {
		return dateCreated;
	}
	  
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	
	public List<providerAddress> getProviderAddresses() {
		return providerAddresses;
	}
 
	public void setProviderAddresses(List<providerAddress> providerAddresses) {
		this.providerAddresses = providerAddresses;
	}
	
	public List<providerIdNum> getProviderIds() {
		return providerIds;
	}
 
	public void setProviderIds(List<providerIdNum> providerIds) {
		this.providerIds = providerIds;
	}

}
