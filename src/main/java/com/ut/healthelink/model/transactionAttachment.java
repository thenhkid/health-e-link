/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.model;

import com.ut.healthelink.validator.NoHtml;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author chadmccue
 */
@Entity
@Table(name = "TRANSACTIONATTACHMENTS")
public class transactionAttachment {
    
    @Transient
    private String downloadLink;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    
    @Column(name = "TRANSACTIONINID", nullable = false)
    private int transactionInId;
    
    @NoHtml
    @Column(name = "FILENAME", nullable = false)
    private String fileName = null;
    
    @NoHtml
    @Column(name = "FILELOCATION", nullable = false)
    private String fileLocation = null;
    
    @NoHtml
    @Column(name = "TITLE", nullable = true)
    private String title = null;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransactionInId() {
        return transactionInId;
    }

    public void setTransactionInId(int transactionInId) {
        this.transactionInId = transactionInId;
    }

    public String getfileName() {
        return fileName;
    }

    public void setfileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getfileLocation() {
        return fileLocation;
    }

    public void setfileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
    
    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
    
    
}
