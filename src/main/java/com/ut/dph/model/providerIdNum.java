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
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "INFO_PROVIDERIDS")
public class providerIdNum {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    
    @Column(name = "TYPE", nullable = true)
    private int providerId;
    
    @NotEmpty
    @NoHtml
    @Column(name = "IDNUM", nullable = false)
    private String idNum;
    
    @NoHtml
    @Column(name = "PROVIDERID", nullable = false)
    private String type;
    
    @NotEmpty
    @NoHtml
    @Column(name = "ISSUEDBY", nullable = false)
    private String issuedBy;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATECREATED", nullable = true)
    private Date dateCreated = new Date();

    
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

    public String getIdNum() {
        return this.idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getIssuedBy() {
        return this.issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
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
