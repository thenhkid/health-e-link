package com.ut.dph.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="REL_USERLOGINS")
public class userLogin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable = false)
	private int id;
	
	@NotEmpty
	@Column(name="USERID", nullable = false)
	private int userId;	 
	
	@Column(name="DATECREATED", nullable = true)
	private Date dateCreated = new Date();
	
	public int getId() {
		return id;
	}
  
	public void setId(int id) {
		this.id = id;
	}

	public int getUserId(){
		return userId;
	}
  
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	

}
