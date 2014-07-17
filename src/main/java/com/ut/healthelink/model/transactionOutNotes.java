/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.model;

import com.ut.healthelink.validator.NoHtml;
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
@Table(name = "TRANSACTIONOUTNOTES")
public class transactionOutNotes {
    
    @Transient
    private String userName = null;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    
    @Column(name = "TRANSACTIONTARGETID", nullable = false)
    private int transactionTargetId;
    
    @NoHtml
    @Column(name = "NOTE", nullable = true)
    private String note;
    
    @Column(name = "USERID", nullable = false)
    private int userId;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATESUBMITTED", nullable = false)
    private Date dateSubmitted = new Date();
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int gettransactionTargetId() {
        return transactionTargetId;
    }

    public void settransactionTargetId(int transactionTargetId) {
        this.transactionTargetId = transactionTargetId;
    }
    
    public String getnote() {
        return note;
    }
    
    public void setnote(String note) {
        this.note = note;
    }
    
    public int getuserId() {
        return userId;
    }

    public void setuserId(int userId) {
        this.userId = userId;
    }
    
    public Date getdateSubmitted() {
        return dateSubmitted;
    }
    
    public void setdateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }
    
    public String getuserName() {
        return userName;
    }
    
    public void setuserName(String userName) {
        this.userName = userName;
    }
    
}
