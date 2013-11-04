package com.ut.dph.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="MESSAGETYPEFORMFIELDS")
public class messageTypeFormFields {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable = false)
	private int id;
	
	@Column(name="MESSAGETYPEID", nullable = false) 
	private int messageTypeId;
	
	@Column(name="FIELDNO", nullable = false)
	private int fieldNo;
	
	@Column(name="FIELDDESC", nullable = true)
	private String fieldDesc;
	
	@NotEmpty
	@Column(name="FIELDLABEL", nullable = false)
	private String fieldLabel;
	
	@Column(name="VALIDATIONTYPE", nullable = true)
	private int validationType = 0;
	
	@Column(name="REQUIRED", nullable = false)
	private boolean required = false;
	
	@Column(name="SAVETOTABLENAME", nullable = true)
	private String saveToTableName;
	
	@Column(name="SAVETOTABLECOL", nullable = true)
	private String saveToTableCol;
	
	@Column(name="BUCKETNO", nullable = false)
	private int bucketNo;
	
	@Column(name="BUCKETDSPPOS", nullable = false)
	private int bucketDspPos;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getMessageTypeId() {
		return messageTypeId;
	}
	public void setMessageTypeId(int messageTypeId) {
		this.messageTypeId = messageTypeId;
	}
	
	public int getFieldNo() {
		return fieldNo;
	}
	public void setFieldNo(int fieldNo) {
		this.fieldNo = fieldNo;
	}
	
	public String getFieldDesc() {
		return fieldDesc;
	}
	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	
	public int getValidationType() {
		return validationType;
	}
	public void setValidationType(int validationType) {
		this.validationType = validationType;
	}

	public boolean getRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public String getSaveToTableName() {
		return saveToTableName;
	}
	public void setSaveToTableName(String saveToTableName) {
		this.saveToTableName = saveToTableName;
	}

	public String getSaveToTableCol() {
		return saveToTableCol;
	}
	public void setSaveToTableCol(String saveToTableCol) {
		this.saveToTableCol = saveToTableCol;
	}
	
	public int getBucketNo() {
		return bucketNo;
	}
	public void setBucketNo(int bucketNo) {
		this.bucketNo = bucketNo;
	}

	public int getBucketDspPos() {
		return bucketDspPos;
	}
	public void setBucketDspPos(int bucketDspPos) {
		this.bucketDspPos = bucketDspPos;
	}

}
