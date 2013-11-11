package com.ut.dph.service;

import java.util.List;

import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationTransport;

public interface configurationManager {
	
  Integer createConfiguration(configuration configuration);
  
  void updateConfiguration(configuration configuration);
  
  configuration getConfigurationById(int configId);
  
  List<configuration> getConfigurationsByOrgId(int orgId);
  
  List<configuration> getConfigurationByName(String configName);
  
  List<configuration> findConfigurations(String searchTerm);
  
  List<configuration> getConfigurations(int page, int maxResults);
  
  List<configuration> getLatestConfigurations(int maxResults);
  
  Long findTotalConfigs();
  
  Long getTotalConnections(int configId);
  
 
}
