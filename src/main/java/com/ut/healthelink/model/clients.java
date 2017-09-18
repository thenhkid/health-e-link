/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author chadmccue
 */
@Entity
@Table(name = "CLIENTS")
public class clients {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    
    @Column(name = "FIRSTNAME", nullable = false)
    private String firstName = "";
    
    @Column(name = "MIDDLENAME", nullable = false)
    private String middleName = "";
    
    @Column(name = "LASTNAME", nullable = false)
    private String lastName = "";
    
    @Column(name = "SUFFIX", nullable = true)
    private String suffix = "";
    
    @Column(name = "DOB", nullable = false)
    private Date DOB;
    
    @Column(name = "LINE1", nullable = false)
    private String line1 = "";
    
    @Column(name = "LINE2", nullable = true)
    private String line2 = "";
    
    @Column(name = "CITY", nullable = false)
    private String city = "";
    
    @Column(name = "STATE", nullable = false)
    private String state = "";
    
    @Column(name = "postalCode", nullable = false)
    private String postalCode = "";
    
    @Column(name = "PHONE1", nullable = true)
    private String phone1 = "";
    
    @Column(name = "PHONE2", nullable = true)
    private String phone2 = "";
    
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATECREATED", nullable = false)
    private Date dateCreated = new Date();
    
    @Column(name = "CREATEDBYID", nullable = false)
    private int createdById = 0;
    
    @Column(name = "GENDERID", nullable = false)
    private int genderId = 0;
    
    @Column(name = "HISPANICID", nullable = false)
    private int hispanicId = 0;
    
    @Column(name = "RACEID", nullable = false)
    private int raceId = 0;
    
    @Column(name = "PRIMARYLANGUAGEID", nullable = false)
    private int primaryLanguageId = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getCreatedById() {
        return createdById;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public int getHispanicId() {
        return hispanicId;
    }

    public void setHispanicId(int hispanicId) {
        this.hispanicId = hispanicId;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
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

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public int getPrimaryLanguageId() {
        return primaryLanguageId;
    }

    public void setPrimaryLanguageId(int primaryLanguageId) {
        this.primaryLanguageId = primaryLanguageId;
    }
    
}
