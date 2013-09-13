package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.configuration;

public interface configurationDAO {

  void insertConfiguration(configuration configuration);
  
  configuration getConfigurationById(int configId);
  
  List<configuration> getConfigurationsByOrgId(int orgId);
  
  List<configuration> getConfigurations();
}
