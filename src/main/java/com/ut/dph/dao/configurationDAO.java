package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationDataTranslations;

public interface configurationDAO {
	
  Integer createConfiguration(configuration configuration);
  
  void updateConfiguration(configuration configuration);
  
  configuration getConfigurationById(int configId);
  
  List<configuration> getConfigurationsByOrgId(int orgId);
  
  List<configuration> findConfigurations(String searchTerm);
  
  List<configuration> getConfigurationByName(String configName);
  
  List<configuration> getConfigurations(int page, int maxResults);
  
  List<configuration> getLatestConfigurations(int maxResults);
  
  Long findTotalConfigs();
  
  Long getTotalConnections(int configId);
  
  void updateCompletedSteps(int configId, int stepCompleted);
  
  @SuppressWarnings("rawtypes")
  List getFileTypes();
  
  List<configurationDataTranslations> getDataTranslations(int configId, int transportMethod);
  
  String getFieldName(int fieldId);
  
  void deleteDataTranslations(int configId, int transportMethod);
}
