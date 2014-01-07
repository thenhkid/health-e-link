package com.ut.dph.model;

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

    private int id;
    private String type;
    private String line1;
    private String line2;
    private String city;
    private String county;
    private String state;
    private String postalCode;
    private String priority;
    private String status;
    private String phone1;
    private String phone2;
    private String fax;
    private Date dateCreated = new Date();
    private int providerId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "TYPE", nullable = true)
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NotEmpty
    @Column(name = "LINE1", nullable = false)
    public String getLine1() {
        return this.line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    @Column(name = "LINE2", nullable = true)
    public String getLine2() {
        return this.line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    @NotEmpty
    @Column(name = "CITY", nullable = false)
    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "COUNTY", nullable = true)
    public String getCounty() {
        return this.county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @NotEmpty
    @Column(name = "STATE", nullable = false)
    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @NotEmpty
    @Phone
    @Column(name = "PHONE1", nullable = false)

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone) {
        this.phone1 = phone;
    }

    @Phone
    @Column(name = "PHONE2", nullable = true)

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone) {
        this.phone2 = phone;
    }

    @Phone
    @Column(name = "FAX", nullable = true)

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @NotEmpty
    @Column(name = "POSTALCODE", nullable = false)
    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Column(name = "PRIORITY", nullable = true)
    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Column(name = "STATUS", nullable = true)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATECREATED", nullable = true)
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Column(name = "PROVIDERID", nullable = false)
    public int getProviderId() {
        return this.providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

}
