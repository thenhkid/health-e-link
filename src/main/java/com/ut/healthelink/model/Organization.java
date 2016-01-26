package com.ut.healthelink.model;

import com.ut.healthelink.validator.NoHtml;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.ut.healthelink.validator.Phone;
import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Entity
@Table(name = "ORGANIZATIONS")
public class Organization {
    
    @Transient
    private CommonsMultipartFile file = null, headerBackgroundFile = null, headerLogoFile = null;
    
    @Transient
    private String headerImageDirectory = "";
    
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
    
    @Column(name = "parentId", nullable = true)
    private Integer parentId = 0;
    
    @Column(name = "LONGITUDE", nullable = true) 
    private String longitude;
    
    @Column(name = "LATITUDE", nullable = true)
    private String latitude;
    
    @Column(name = "ORGTYPE", nullable = false)
    private Integer orgType = 1;
    
    @Column(name = "headerLogo", nullable = false)
    private String headerLogo;
    
    @Column(name = "headerBackground", nullable = false)
    private String headerBackground;

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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParsingTemplate() {
        return parsingTemplate;
    }

    public void setParsingTemplate(String parsingTemplate) {
        this.parsingTemplate = parsingTemplate;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getOrgType() {
        return orgType;
    }

    public void setOrgType(Integer orgType) {
        this.orgType = orgType;
    }

    public String getHeaderLogo() {
        return headerLogo;
    }

    public void setHeaderLogo(String headerLogo) {
        this.headerLogo = headerLogo;
    }

    public String getHeaderBackground() {
        return headerBackground;
    }

    public void setHeaderBackground(String headerBackground) {
        this.headerBackground = headerBackground;
    }

    public String getHeaderImageDirectory() {
        return headerImageDirectory;
    }

    public void setHeaderImageDirectory(String headerImageDirectory) {
        this.headerImageDirectory = headerImageDirectory;
    }

    public String getCleanURL() {
        return cleanURL;
    }

    public void setCleanURL(String cleanURL) {
        this.cleanURL = cleanURL;
    }

    public CommonsMultipartFile getHeaderBackgroundFile() {
        return headerBackgroundFile;
    }

    public void setHeaderBackgroundFile(CommonsMultipartFile headerBackgroundFile) {
        this.headerBackgroundFile = headerBackgroundFile;
    }

    public CommonsMultipartFile getHeaderLogoFile() {
        return headerLogoFile;
    }

    public void setHeaderLogoFile(CommonsMultipartFile headerLogoFile) {
        this.headerLogoFile = headerLogoFile;
    }
    
}
