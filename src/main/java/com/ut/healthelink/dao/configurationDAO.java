package com.ut.healthelink.dao;

import java.util.List;

import com.ut.healthelink.model.CrosswalkData;
import com.ut.healthelink.model.HL7Details;
import com.ut.healthelink.model.HL7ElementComponents;
import com.ut.healthelink.model.HL7Elements;
import com.ut.healthelink.model.HL7Segments;
import com.ut.healthelink.model.Macros;
import com.ut.healthelink.model.configuration;
import com.ut.healthelink.model.configurationCCDElements;
import com.ut.healthelink.model.configurationConnection;
import com.ut.healthelink.model.configurationConnectionReceivers;
import com.ut.healthelink.model.configurationConnectionSenders;
import com.ut.healthelink.model.configurationDataTranslations;
import com.ut.healthelink.model.configurationExcelDetails;
import com.ut.healthelink.model.configurationMessageSpecs;
import com.ut.healthelink.model.configurationSchedules;

import org.springframework.stereotype.Repository;

@Repository
public interface configurationDAO {
	
  Integer createConfiguration(configuration configuration);
  
  void updateConfiguration(configuration configuration);
  
  configuration getConfigurationById(int configId);
  
  List<configuration> getConfigurationsByOrgId(int orgId, String searchTerm);
  
  List<configuration> getActiveConfigurationsByOrgId(int orgId);
  
  configuration getConfigurationByName(String configName, int orgId);
  
  List<configuration> getConfigurations();
  
  List<configuration> getLatestConfigurations(int maxResults);
  
  Long findTotalConfigs();
  
  Long getTotalConnections(int configId);
  
  void updateCompletedSteps(int configId, int stepCompleted);
  
  @SuppressWarnings("rawtypes")
  List getFileTypes();
  
  String getFileTypesById(int id);
 
  List<configurationDataTranslations> getDataTranslations(int configId);
  
  List<configurationDataTranslations> getDataTranslationsWithFieldNo(int configId, int categoryId);
  
  String getFieldName(int fieldId);
  
  void deleteDataTranslations(int configId, int categoryId);
  
  void saveDataTranslations(configurationDataTranslations translations);

  List<Macros> getMacros();
  
  List<Macros> getMacrosByCategory(int categoryId);
  
  Macros getMacroById(int macroId);
  
  List<configurationConnection> getAllConnections();
  
  List<configurationConnection> getLatestConnections(int maxResults);
  
  List<configurationConnection> getConnectionsByConfiguration(int configId, int userId);
  
  List<configurationConnection> getConnectionsByTargetConfiguration(int configId);
  
  Integer saveConnection(configurationConnection connection);
  
  configurationConnection getConnection(int connectionId);
  
  void updateConnection(configurationConnection connection);
  
  configurationSchedules getScheduleDetails(int configId);
  
  void saveSchedule(configurationSchedules scheduleDetails);
 
  configurationMessageSpecs getMessageSpecs(int configId);
  
  void updateMessageSpecs(configurationMessageSpecs messageSpecs, int transportDetailId, int clearFields);
  
  List<configuration> getActiveConfigurationsByUserId(int userId, int transportMethod)  throws Exception;
  
  List<configurationConnectionSenders> getConnectionSenders(int connectionId);
  
  List<configurationConnectionReceivers> getConnectionReceivers(int connectionId);
  
  void saveConnectionSenders(configurationConnectionSenders senders);
  
  void saveConnectionReceivers(configurationConnectionReceivers receivers);
  
  void removeConnectionSenders(int connectionId);
  
  void removeConnectionReceivers(int connectionId);
  
  List<CrosswalkData> getCrosswalkData(int cwId);
  
  HL7Details getHL7Details(int configId);
  
  List<HL7Segments> getHL7Segments(int hl7Id);
  
  List<HL7Elements> getHL7Elements(int hl7Id, int segmentId);
  
  List<HL7ElementComponents> getHL7ElementComponents(int elementId);
  
  void updateHL7Details(HL7Details details);
  
  void updateHL7Segments(HL7Segments segment);
  
  void updateHL7Elements(HL7Elements element);
  
  void updateHL7ElementComponent(HL7ElementComponents component);
  
  int saveHL7Details(HL7Details details);
  
  int saveHL7Segment(HL7Segments newSegment);
  
  int saveHL7Element(HL7Elements newElement);
  
  void saveHL7Component(HL7ElementComponents newcomponent);
  
  String getMessageTypeNameByConfigId (Integer configId);
  
  @SuppressWarnings("rawtypes")
  List getEncodings();
  
  void removeHL7ElementComponent(Integer componentId);
  
  void removeHL7Element(Integer elementId);
  
  void removeHL7Segment(Integer segmentId);
  
  List<configurationCCDElements> getCCDElements(Integer configId) throws Exception;
  
  void saveCCDElement(configurationCCDElements ccdElement) throws Exception;
  
  configurationCCDElements getCCDElement(Integer elementId) throws Exception;
  
  configurationExcelDetails getExcelDetails (Integer configId, Integer orgId) throws Exception;
}
