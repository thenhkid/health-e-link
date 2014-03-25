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
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "INFO_PROVIDERADDRESSES")
public class providerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    
    @Column(name = "TYPE", nullable = true)
    private String type;
    
    @NotEmpty
    @NoHtml
    @Column(name = "LINE1", nullable = false)
    private String line1;
    
    @NoHtml
    @Column(name = "LINE2", nullable = true)
    private String line2;
    
    @NotEmpty
    @NoHtml
    @Column(name = "CITY", nullable = false)
    private String city;
    
    @NoHtml
    @Column(name = "COUNTY", nullable = true)
    private String county;
    
    @NotEmpty
    @NoHtml
    @Column(name = "STATE", nullable = false)
    private String state;
    
    @NotEmpty
    @NoHtml
    @Column(name = "POSTALCODE", nullable = false)
    private String postalCode;
    
    @NoHtml
    @Column(name = "PRIORITY", nullable = true)
    private String priority;
    
    @NoHtml
    @Column(name = "STATUS", nullable = true)
    private String status;
    
    @NotEmpty
    @Phone
    @NoHtml
    @Column(name = "PHONE1", nullable = false)
    private String phone1;
    
    @Phone
    @NoHtml
    @Column(name = "PHONE2", nullable = true)
    private String phone2;
    
    @Phone
    @NoHtml
    @Column(name = "FAX", nullable = true)
    private String fax;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATECREATED", nullable = true)
    private Date dateCreated = new Date();
    
    @Column(name = "PROVIDERID", nullable = false)
    private int providerId;

    
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getLine1() {
        return this.line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return this.line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return this.county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
    
    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone) {
        this.phone1 = phone;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone) {
        this.phone2 = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getProviderId() {
        return this.providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

}
