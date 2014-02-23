/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author chadmccue
 */
@Entity
@Table(name = "CONFIGURATIONHL7ELEMENTVALUES")
public class HL7ElementComponents {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    
    @Column(name = "ELEMENTID", nullable = false)
    private int elementId;
    
    @Column(name = "fieldDescriptor", nullable = true)
    private String fieldDescriptor = "";
    
    @Column(name = "fieldValue", nullable = true)
    private String fieldValue = "";
    
    @Column(name = "displayPos", nullable = true)
    private int displayPos = 1;
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getelementId() {
        return elementId;
    }
    
    public void setelementId(int elementId) {
        this.elementId = elementId;
    }
    
    public String getfieldDescriptor() {
        return fieldDescriptor;
    }
    
    public void setfieldDescriptor(String fieldDescriptor) {
        this.fieldDescriptor = fieldDescriptor;
    }
    
    public String getfieldValue() {
        return fieldValue;
    }
    
    public void setfieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
    
    public int getdisplayPos() {
        return displayPos;
    }
    
    public void setdisplayPos(int displayPos) {
        this.displayPos = displayPos;
    }
    
}
