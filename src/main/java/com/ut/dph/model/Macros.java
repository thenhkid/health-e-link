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

@Entity
@Table(name="REF_MACROS")
public class Macros {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable = false)
	private int id;
	
	@NotEmpty
	@Column(name="NAME", nullable = false)
	private String name;
	
	@Column(name="SCRIPTNAME", nullable = false)
	private int scriptName = 0;
	
	public int getId() {
	    return id;
	}
	  
	public void setId(int id) {
	    this.id = id;
	}
	
	public String getName() {
		return name;
	}
	  
	public void setName(String name) {
		this.name = name;
	}
	
	public int getScriptName() {
		return scriptName;
	}
	
	public void setScriptName(int scriptName) {
		this.scriptName = scriptName;
	}

}
