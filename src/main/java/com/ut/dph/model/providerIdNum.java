package com.ut.dph.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "INFO_PROVIDERIDS")
public class providerIdNum {

    private int id;
    private int providerId;
    private String idNum;
    private String type;
    private String issuedBy;
    private Date dateCreated = new Date();

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
    @Column(name = "IDNUM", nullable = false)
    public String getIdNum() {
        return this.idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    @NotEmpty
    @Column(name = "ISSUEDBY", nullable = false)
    public String getIssuedBy() {
        return this.issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
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
