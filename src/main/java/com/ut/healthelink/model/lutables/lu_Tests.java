package com.ut.healthelink.model.lutables;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.ut.healthelink.validator.NoHtml;

@Entity
@Table(name="lu_Tests")
public class lu_Tests {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable = false)
	private int id;
	
	@NotEmpty
	@Column(name="displayText", nullable = false)
	private String displayText;
	
	@Column(name="description", nullable = true)
	private String description;
	
	@Column(name="DATECREATED", nullable = true)
	private Date dateCreated = new Date();
	
	@Column(name="isCustom", nullable = true)
	private boolean custom = false;
	
	@Column(name="status", nullable = true)
	private boolean status = true;
	
	@Column(name="vitalSign", nullable = true)
	private boolean vitalSign = false;

	@Column(name="normalRange", nullable = true)
	private String normalRange;

	@Column(name="normalRangeUnit", nullable = true)
	private String normalRangeUnit;
	
	@Column(name="codeSystem", nullable = true)
	private String codeSystem;

	@Column(name="codeValue", nullable = true)
	private String codeValue;
	
	@Column(name="codeVersion", nullable = true)
	private String codeVersion;
	
	@NotEmpty
	@NoHtml
	@Column(name="category", nullable = true)
	private String category;  


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

	public boolean isVitalSign() {
		return vitalSign;
	}

	public void setVitalSign(boolean vitalSign) {
		this.vitalSign = vitalSign;
	}

	public String getNormalRange() {
		return normalRange;
	}

	public void setNormalRange(String normalRange) {
		this.normalRange = normalRange;
	}

	public String getNormalRangeUnit() {
		return normalRangeUnit;
	}

	public void setNormalRangeUnit(String normalRangeUnit) {
		this.normalRangeUnit = normalRangeUnit;
	}

	public String getCodeSystem() {
		return codeSystem;
	}

	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getCodeVersion() {
		return codeVersion;
	}

	public void setCodeVersion(String codeVersion) {
		this.codeVersion = codeVersion;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
}
