package com.ut.healthelink.model.lutables;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="lu_Medications")
public class lu_Medications {

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

	@Column(name="form", nullable = true)
	private String form;

	@Column(name="concentration", nullable = true)
	private float concentration;
	
	@Column(name="concentrationUnit", nullable = true)
	private String concentrationUnit;
	
	@Column(name="medSize", nullable = true)
	private String medSize;

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

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public float getConcentration() {
		return concentration;
	}

	public void setConcentration(float concentration) {
		this.concentration = concentration;
	}

	public String getConcentrationUnit() {
		return concentrationUnit;
	}

	public void setConcentrationUnit(String concentrationUnit) {
		this.concentrationUnit = concentrationUnit;
	}

	public String getMedSize() {
		return medSize;
	}

	public void setMedSize(String medSize) {
		this.medSize = medSize;
	}

	
}
