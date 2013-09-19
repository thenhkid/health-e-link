package com.ut.dph.service;

import java.util.List;

import com.ut.dph.model.configuration;

public interface configurationManager {
	
  void saveConfiguration(configuration configuration);
  
  configuration getConfigurationById(int configId);
  
  List<configuration> getConfigurationsByOrgId(int orgId);
  
  List<configuration> getConfigurations(int firstResults, int maxResults);
}
