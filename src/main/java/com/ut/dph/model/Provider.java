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
@Table(name="PROVIDERS")
public class Provider {
	
	@Transient
	private List<providerAddress> providerAddresses = null;
	
	
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
	
	@NotEmpty
	@Column(name="PROVIDERID", nullable = true)
	private String providerId;
	
	public String getProviderId() {
		return providerId;
	}
	
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	@Email
	@Column(name="EMAIL", nullable = false)
	private String email;
	
	public String getEmail() {
		return email;
	}
	  
	public void setEmail(String email) {
		this.email = email;
	}
	
	@NotEmpty @Phone
	@Column(name="PHONE", nullable = false)
	private String phone;
	
	public String getPhone() {
		return phone;
	}
	  
	public void setPhone(String phone) {
		this.phone = phone;
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
	
	

}
