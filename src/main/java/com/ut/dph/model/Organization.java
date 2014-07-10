package com.ut.dph.model;

import com.ut.dph.validator.NoHtml;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.ut.dph.validator.Phone;
import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Entity
@Table(name = "ORGANIZATIONS")
public class Organization {
    
    @Transient
    private CommonsMultipartFile file = null;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;

    @NotEmpty
    @NoHtml
    @Column(name = "ORGNAME", nullable = false)
    private String orgName;

    @NotEmpty
    @NoHtml
    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @NoHtml
    @Column(name = "ADDRESS2", nullable = true)
    private String address2;

    @NotEmpty
    @NoHtml
    @Column(name = "CITY", nullable = false)
    private String city;

    @NotEmpty
    @NoHtml
    @Column(name = "STATE", nullable = false)
    private String state;

    @NotEmpty
    @NoHtml
    @Column(name = "POSTALCODE", nullable = false)
    private String postalCode;

    @NotEmpty
    @Phone
    @Column(name = "PHONE", nullable = false)
    private String phone;
    
    @NoHtml
    @Column(name = "FAX", nullable = true)
    private String fax;

    @Column(name = "PUBLIC", nullable = false)
    private boolean publicOrg = true;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATECREATED", nullable = true)
    private Date dateCreated = new Date();

    @Column(name = "STATUS", nullable = false)
    private boolean status = true;

    @NoHtml
    @Column(name = "CLEANURL", nullable = false)
    private String cleanURL;
    
    @Column(name = "parsingTemplate", nullable = true)
    private String parsingTemplate = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
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

    public boolean getPublicOrg() {
        return publicOrg;
    }

    public void setPublicOrg(boolean publicOrg) {
        this.publicOrg = publicOrg;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getcleanURL() {
        return cleanURL;
    }

    public void setcleanURL(String cleanURL) {
        this.cleanURL = cleanURL;
    }
    
    public String getparsingTemplate() {
        return parsingTemplate;
    }

    public void setparsingTemplate(String parsingTemplate) {
        this.parsingTemplate = parsingTemplate;
    }
    
    public CommonsMultipartFile getFile() {
        return file;
    }

    public void setFile(CommonsMultipartFile file) {
        this.file = file;
    }

}
