package com.ut.dph.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.ut.dph.validator.Phone;

@Entity
@Table(name="PROVIDERS")
public class Provider {
	
  	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable = false)
	private int id;
	
	@Column(name="ORGID", nullable = false)
	private int orgId;
	
	@NotEmpty
	@Column(name="FIRSTNAME", nullable = false)
	private String firstName;
	  
	@NotEmpty
	@Column(name="LASTNAME", nullable = true)
	private String lastName;
	
	@NotEmpty
	@Column(name="PROVIDERID", nullable = true)
	private String providerId;
	
	@NotEmpty @Email
	@Column(name="EMAIL", nullable = false)
	private String email;
	
	@NotEmpty @Phone
	@Column(name="PHONE", nullable = false)
	private String phone;
	  
	@Phone
	@Column(name="FAX", nullable = true)
	private String fax;
	
	@Column(name="STATUS", nullable = false)
	private boolean status = false;
	
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
	
	public String getFirstName() {
		return firstName;
	}
	  
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	  
	public String getLastName() {
		return lastName;
	}
	  
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getProviderId() {
		return providerId;
	}
	
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	public String getEmail() {
		return email;
	}
	  
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	  
	public void setPhone(String phone) {
		this.phone = phone;
	}
	  
    public String getFax() {
		return fax;
	}
	 
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public boolean getStatus() {
		return status;
	}
	  
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	  
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

}
