package com.ut.dph.model.lutables;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import com.ut.dph.validator.Phone;


@Entity
@Table(name="lu_Manufacturers")
public class lu_Manufacturers {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable = false)
	private int id;
	
	@NotEmpty
	@Column(name="displayText", nullable = false)
	private String displayText;
	
	@Column(name="description", nullable = false)
	private String description;
	
	@Column(name="DATECREATED", nullable = true)
	private Date dateCreated = new Date();
	
	@Column(name="isCustom", nullable = true)
	private boolean custom = false;
	
	@Column(name="status", nullable = true)
	private boolean status = true;

	@Column(name="contactName", nullable = false)
	private String contactName;
	
	@Column(name="addressType", nullable = false)
	private String addressType;
	
	@Column(name="line1", nullable = false)
	private String line1;
	
	@Column(name="line2", nullable = false)
	private String line2;
	
	@Column(name="city", nullable = false)
	private String city;
	
	@Column(name="state", nullable = false)
	private String state;
	
	@Column(name="zip", nullable = false)
	private String zip;
	
	@Column(name="country", nullable = false)
	private String country;
	
	@Column(name="phone1Type", nullable = false)
	private String phone1Type;
	
	@Phone
	@Column(name="phone1", nullable = false)
	private String phone1;
	
	@Column(name="phone2Type", nullable = false)
	private String phone2Type;
	
	@Phone
	@Column(name="phone2", nullable = false)
	private String phone2;
	
	@Phone
	@Column(name="fax", nullable = false)
	private String fax;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public boolean isCustom() {
		return custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone1Type() {
		return phone1Type;
	}

	public void setPhone1Type(String phone1Type) {
		this.phone1Type = phone1Type;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2Type() {
		return phone2Type;
	}

	public void setPhone2Type(String phone2Type) {
		this.phone2Type = phone2Type;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
}
