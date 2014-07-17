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
@Table(name = "MESSAGE_PATIENTS")
public class messagePatients {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    
    @Column(name = "TRANSACTIONINID", nullable = false)
    private int transactionInId;
    
    @Column(name = "SOURCEPATIENTID", nullable = true)
    private String sourcePatientId = null;
    
    @Column(name = "PREFIX", nullable = true)
    private String prefix = null;
    
    @Column(name = "FIRSTNAME", nullable = true)
    private String firstName = null;
    
    @Column(name = "LASTNAME", nullable = true)
    private String lastName = null;
    
    @Column(name = "SUFFIX", nullable = true)
    private String suffix = null;
    
    @Column(name = "DOB", nullable = true)
    private Date dob = null;
    
    @Column(name = "GENDERID", nullable = true)
    private Integer genderId = 0;
    
    @Column(name = "RACEID", nullable = true)
    private Integer raceId = 0;
   
    @Column(name = "HISPANICID", nullable = true)
    private Integer hispanicId = 0;
    
    @Column(name = "PRIMARYLANGUAGEID", nullable = true)
    private Integer primaryLanguageId = 0;
    
    @Column(name = "ENGLISHPROFICIENT", nullable = true)
    private String englishProficient = null;
    
    @Column(name = "MARITALSTATUSID", nullable = true)
    private Integer maritalStausId = 0;
    
    @Column(name = "INITIALCONTRACEPTIVEID", nullable = true)
    private Integer initialContraceptiveId = 0;
    
    @Column(name = "EMAIL", nullable = true)
    private String email = null;
    
    @Column(name = "ADDITONALCONTACTNUMBER", nullable = true)
    private String additonalContactNumber = null;
    
    @Column(name = "URL", nullable = true)
    private String url = null;
    
    @Column(name = "OCCUPATION", nullable = true)
    private String occupation = null;
    
    @Column(name = "GUARDIANNAME", nullable = true)
    private String guardianName = null;
    
    @Column(name = "EMERGENCYCONTACT", nullable = true)
    private String emergencyContact = null;
    
    @Column(name = "EMERGENCYTEL", nullable = true)
    private String emergencyTel = null;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATECREATED", nullable = false)
    private Date dateCreated = new Date();
    
    @Column(name = "RACE", nullable = true)
    private String race = null;

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getTransactionInId() {
        return transactionInId;
    }

    public void setTransactionInId(Integer transactionInId) {
        this.transactionInId = transactionInId;
    }

    public String getSourcePatientId() {
        return sourcePatientId;
    }

    public void setSourcePatientId(String sourcePatientId) {
        this.sourcePatientId = sourcePatientId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
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

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(Integer raceId) {
        this.raceId = raceId;
    }

    public int getHispanicId() {
        return hispanicId;
    }

    public void setHispanicId(Integer hispanicId) {
        this.hispanicId = hispanicId;
    }

    public int getPrimaryLanguageId() {
        return primaryLanguageId;
    }

    public void setPrimaryLanguageId(Integer primaryLanguageId) {
        this.primaryLanguageId = primaryLanguageId;
    }

    public String getEnglishProficient() {
        return englishProficient;
    }

    public void setEnglishProficient(String englishProficient) {
        this.englishProficient = englishProficient;
    }

    public int getMaritalStausId() {
        return maritalStausId;
    }

    public void setMaritalStausId(Integer maritalStausId) {
        this.maritalStausId = maritalStausId;
    }

    public int getInitialContraceptiveId() {
        return initialContraceptiveId;
    }

    public void setInitialContraceptiveId(Integer initialContraceptiveId) {
        this.initialContraceptiveId = initialContraceptiveId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getadditonalContactNumber() {
        return additonalContactNumber;
    }

    public void setadditonalContactNumber(String additonalContactNumber) {
        this.additonalContactNumber = additonalContactNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyTel() {
        return emergencyTel;
    }

    public void setEmergencyTel(String emergencyTel) {
        this.emergencyTel = emergencyTel;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }
    
}
