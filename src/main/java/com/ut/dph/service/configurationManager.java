package com.ut.dph.service;

import java.util.List;

import com.ut.dph.model.Macros;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationConnectionReceivers;
import com.ut.dph.model.configurationConnectionSenders;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.configurationMessageSpecs;
import com.ut.dph.model.configurationSchedules;

public interface configurationManager {
	
  Integer createConfiguration(configuration configuration);
  
  void updateConfiguration(configuration configuration);
  
  configuration getConfigurationById(int configId);
  
  List<configuration> getConfigurationsByOrgId(int orgId, String searchTerm);
  
  List<configuration> getActiveConfigurationsByOrgId(int orgId);
  
  configuration getConfigurationByName(String configName, int orgId);
  
  List<configuration> findConfigurations(String searchTerm);
  
  List<configuration> getConfigurations(int page, int maxResults);
  
  List<configuration> getLatestConfigurations(int maxResults);
  
  Long findTotalConfigs();
  
  Long getTotalConnections(int configId);
  
  void updateCompletedSteps(int configId, int stepCompleted);
  
  @SuppressWarnings("rawtypes")
  List getFileTypes();
  
  String getFileTypesById(int id);
  
  List<configurationDataTranslations> getDataTranslations(int configId);
  
  String getFieldName(int fieldId);
  
  void deleteDataTranslations(int configId);
  
  void saveDataTranslations(configurationDataTranslations translations);
  
  List<Macros> getMacros();
  
  Macros getMacroById(int macroId);
  
  List<configurationConnection> getAllConnections(int page, int maxResults);
  
  List<configurationConnection> getConnectionsByConfiguration(int configId);
  
  Integer saveConnection(configurationConnection connection);
  
  configurationConnection getConnection(int connectionId);
  
  void updateConnection(configurationConnection connection);
 
  configurationSchedules getScheduleDetails(int configId);
  
  void saveSchedule(configurationSchedules scheduleDetails);
  
  configurationMessageSpecs getMessageSpecs(int configId);
  
  void updateMessageSpecs(configurationMessageSpecs messageSpecs, int transportDetailId);
  
  List<configuration> getActiveConfigurationsByUserId(int userId, int transportMethod);
  
  List<configurationConnectionSenders> getConnectionSenders(int connectionId);
  
  List<configurationConnectionReceivers> getConnectionReceivers(int connectionId);
  
  void saveConnectionSenders(configurationConnectionSenders senders);
  
  void saveConnectionReceivers(configurationConnectionReceivers receivers);
  
  void removeConnectionSenders(int connectionId);
  
  void removeConnectionReceivers(int connectionId);
 
}
