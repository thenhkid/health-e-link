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
@Table(name = "BATCHMULTIPLETARGETS")
public class batchMultipleTargets {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;

    @Column(name = "BATCHID", nullable = false)
    private int batchId;

    @Column(name = "TGTCONFIGID", nullable = false)
    private int tgtConfigId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public int getTgtConfigId() {
        return tgtConfigId;
    }

    public void setTgtConfigId(int tgtConfigId) {
        this.tgtConfigId = tgtConfigId;
    }
    
    
}
