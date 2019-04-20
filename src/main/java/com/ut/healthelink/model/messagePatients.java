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
import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author chadmccue
 */
@Entity
@Table(name = "MESSAGE_PATIENTS")
public class messagePatients {
    
    @Transient
    private String genderVal = "";
    
    @Transient
    private String raceVal = "";
     
    @Transient
    private String ethnicityVal = "";
    
    @Transient
    private String languageVal = "";
    
    @Transient
    private String zip = "";
    
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
    
    @Column(name = "MIDDLENAME", nullable = true)
    private String middleName = null;
    
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
    private Integer maritalStatusId = 0;
    
    @Column(name = "INITIALCONTRACEPTIVEID", nullable = true)
    private Integer initialContraceptiveId = 0;
    
    @Column(name = "INITIALNOCONTRACEPTIVEID", nullable = true)
    private Integer initialNoContraceptiveId = 0;
    
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
    
    @Column(name = "PATIENTSTATUSID", nullable = true)
    private Integer patientStatusId = 0;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATECREATED", nullable = false)
    private Date dateCreated = new Date();
    
    @Column(name = "RACE", nullable = true)
    private String race = null;
  
    @Column(name = "primaryGuardianFirstName", nullable = true)
    private String primaryGuardianFirstName = null;
    
    @Column(name = "primaryGuardianLastName", nullable = true)
    private String primaryGuardianLastName = null;
    
    @Column(name = "primaryGuardianPhone", nullable = true)
    private String primaryGuardianPhone = null;
    
    @Column(name = "primaryGuardianPhone2", nullable = true)
    private String primaryGuardianPhone2 = null;
    
    @Column(name = "primaryGuardianEmail", nullable = true)
    private String primaryGuardianEmail = null;
    
    @Column(name = "additionalGuardianFirstName", nullable = true)
    private String additionalGuardianFirstName = null;
    
    @Column(name = "additionalGuardianLastName", nullable = true)
    private String additionalGuardianLastName = null;
    
    @Column(name = "additionalGuardianPhone", nullable = true)
    private String additionalGuardianPhone = null;
    
    @Column(name = "additionalGuardianPhone2", nullable = true)
    private String additionalGuardianPhone2 = null;
    
    @Column(name = "additionalGuardianEmail", nullable = true)
    private String additionalGuardianEmail = null;
    
    @Column(name = "miscField1", nullable = true)
    private String miscField1 = null;
    
    @Column(name = "miscField2", nullable = true)
    private String miscField2 = null;
    
    @Column(name = "miscField3", nullable = true)
    private String miscField3 = null;
    
    @Column(name = "miscField4", nullable = true)
    private String miscField4 = null;
    
    @Column(name = "miscField5", nullable = true)
    private String miscField5 = null;
    
    @Column(name = "miscField6", nullable = true)
    private String miscField6 = null;
    
    @Column(name = "miscField7", nullable = true)
    private String miscField7 = null;
    
    @Column(name = "miscField8", nullable = true)
    private String miscField8 = null;
    
    @Column(name = "miscField9", nullable = true)
    private String miscField9 = null;
    
    @Column(name = "miscField10", nullable = true)
    private String miscField10 = null;
    
    
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

    public int getMaritalStatusId() {
        return maritalStatusId;
    }

    public void setMaritalStausId(Integer maritalStatusId) {
        this.maritalStatusId = maritalStatusId;
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

    public String getGenderVal() {
        return genderVal;
    }

    public void setGenderVal(String genderVal) {
        this.genderVal = genderVal;
    }

    public String getRaceVal() {
        return raceVal;
    }

    public void setRaceVal(String raceVal) {
        this.raceVal = raceVal;
    }

    public String getEthnicityVal() {
        return ethnicityVal;
    }

    public void setEthnicityVal(String ethnicityVal) {
        this.ethnicityVal = ethnicityVal;
    }

    public String getLanguageVal() {
        return languageVal;
    }

    public void setLanguageVal(String languageVal) {
        this.languageVal = languageVal;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Integer getInitialNoContraceptiveId() {
        return initialNoContraceptiveId;
    }

    public void setInitialNoContraceptiveId(Integer initialNoContraceptiveId) {
        this.initialNoContraceptiveId = initialNoContraceptiveId;
    }

    public String getAdditonalContactNumber() {
        return additonalContactNumber;
    }

    public void setAdditonalContactNumber(String additonalContactNumber) {
        this.additonalContactNumber = additonalContactNumber;
    }

    public Integer getPatientStatusId() {
        return patientStatusId;
    }

    public void setPatientStatusId(Integer patientStatusId) {
        this.patientStatusId = patientStatusId;
    }

	public String getPrimaryGuardianFirstName() {
		return primaryGuardianFirstName;
	}

	public void setPrimaryGuardianFirstName(String primaryGuardianFirstName) {
		this.primaryGuardianFirstName = primaryGuardianFirstName;
	}

	public String getPrimaryGuardianLastName() {
		return primaryGuardianLastName;
	}

	public void setPrimaryGuardianLastName(String primaryGuardianLastName) {
		this.primaryGuardianLastName = primaryGuardianLastName;
	}

	public String getPrimaryGuardianPhone() {
		return primaryGuardianPhone;
	}

	public void setPrimaryGuardianPhone(String primaryGuardianPhone) {
		this.primaryGuardianPhone = primaryGuardianPhone;
	}

	public String getPrimaryGuardianPhone2() {
		return primaryGuardianPhone2;
	}

	public void setPrimaryGuardianPhone2(String primaryGuardianPhone2) {
		this.primaryGuardianPhone2 = primaryGuardianPhone2;
	}

	public String getPrimaryGuardianEmail() {
		return primaryGuardianEmail;
	}

	public void setPrimaryGuardianEmail(String primaryGuardianEmail) {
		this.primaryGuardianEmail = primaryGuardianEmail;
	}

	public String getAdditionalGuardianFirstName() {
		return additionalGuardianFirstName;
	}

	public void setAdditionalGuardianFirstName(String additionalGuardianFirstName) {
		this.additionalGuardianFirstName = additionalGuardianFirstName;
	}

	public String getAdditionalGuardianLastName() {
		return additionalGuardianLastName;
	}

	public void setAdditionalGuardianLastName(String additionalGuardianLastName) {
		this.additionalGuardianLastName = additionalGuardianLastName;
	}

	public String getAdditionalGuardianPhone() {
		return additionalGuardianPhone;
	}

	public void setAdditionalGuardianPhone(String additionalGuardianPhone) {
		this.additionalGuardianPhone = additionalGuardianPhone;
	}

	public String getAdditionalGuardianPhone2() {
		return additionalGuardianPhone2;
	}

	public void setAdditionalGuardianPhone2(String additionalGuardianPhone2) {
		this.additionalGuardianPhone2 = additionalGuardianPhone2;
	}

	public String getAdditionalGuardianEmail() {
		return additionalGuardianEmail;
	}

	public void setAdditionalGuardianEmail(String additionalGuardianEmail) {
		this.additionalGuardianEmail = additionalGuardianEmail;
	}

	public String getMiscField1() {
		return miscField1;
	}

	public void setMiscField1(String miscField1) {
		this.miscField1 = miscField1;
	}

	public String getMiscField2() {
		return miscField2;
	}

	public void setMiscField2(String miscField2) {
		this.miscField2 = miscField2;
	}

	public String getMiscField3() {
		return miscField3;
	}

	public void setMiscField3(String miscField3) {
		this.miscField3 = miscField3;
	}

	public String getMiscField4() {
		return miscField4;
	}

	public void setMiscField4(String miscField4) {
		this.miscField4 = miscField4;
	}

	public String getMiscField5() {
		return miscField5;
	}

	public void setMiscField5(String miscField5) {
		this.miscField5 = miscField5;
	}

	public String getMiscField6() {
		return miscField6;
	}

	public void setMiscField6(String miscField6) {
		this.miscField6 = miscField6;
	}

	public String getMiscField7() {
		return miscField7;
	}

	public void setMiscField7(String miscField7) {
		this.miscField7 = miscField7;
	}

	public String getMiscField8() {
		return miscField8;
	}

	public void setMiscField8(String miscField8) {
		this.miscField8 = miscField8;
	}

	public String getMiscField9() {
		return miscField9;
	}

	public void setMiscField9(String miscField9) {
		this.miscField9 = miscField9;
	}

	public String getMiscField10() {
		return miscField10;
	}

	public void setMiscField10(String miscField10) {
		this.miscField10 = miscField10;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTransactionInId(int transactionInId) {
		this.transactionInId = transactionInId;
	}

	public void setMaritalStatusId(Integer maritalStatusId) {
		this.maritalStatusId = maritalStatusId;
	}

}
