package com.ut.dph.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CONFIGURATIONS")
public class configuration {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="ID", nullable = false)
  private int id;
  
  @Column(name="orgId", nullable = false)
  private int orgId;
  
  @Column(name="configName", nullable = false)
  private String configName;
  
  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }

  public int getOrganizationId() {
    return orgId;
  }
  
  public void setOrganizationId(int orgId) {
    this.orgId = orgId;
  }
  
  public String getConfigName() {
    return configName;
  }
  
  public void setConfigName(String configName) {
    this.configName = configName;
  }
  
}
