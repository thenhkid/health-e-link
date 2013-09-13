package com.ut.dph.configuration;

import java.util.List;

import com.ut.dph.model.configuration;

public interface configurationManager {

  void insertConfiguration(configuration configuration);
  
  configuration getConfigurationById(int configId);
  
  List<configuration> getConfigurationsByOrgId(int orgId);
  
  List<configuration> getConfigurations();
}
