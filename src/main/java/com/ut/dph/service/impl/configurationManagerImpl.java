package com.ut.dph.service.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.dao.configurationDAO;
import com.ut.dph.model.Connections;
import com.ut.dph.model.Macros;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.service.configurationManager;

@Service
public class configurationManagerImpl implements configurationManager {

	  @Autowired
	  private configurationDAO configurationDAO;
	  
	  @Override
	  @Transactional
	  public Integer createConfiguration(configuration configuration) {
		 configuration.setstepsCompleted(1);
	     return configurationDAO.createConfiguration(configuration);
	  }
	  
	  @Override
	  @Transactional
	  public void updateConfiguration(configuration configuration) {
		  configurationDAO.updateConfiguration(configuration);
	  }
	 
	
	  @Override
	  @Transactional
	  public configuration getConfigurationById(int configId) {
	    return configurationDAO.getConfigurationById(configId);
	  }
	  
	  @Override
	  @Transactional
	  public List<configuration> getConfigurationsByOrgId(int configId) {
	    return configurationDAO.getConfigurationsByOrgId(configId);
	  }
	  
	  @Override
	  @Transactional
	  public List<configuration> getConfigurationByName(String configName) {
		  return configurationDAO.getConfigurationByName(configName);
	  }
	
	  @Override
	  @Transactional
	  public List<configuration> getConfigurations(int page, int maxResults) {
	    return configurationDAO.getConfigurations(page, maxResults);
	  }
  
	  @Override
	  @Transactional
	  public List<configuration> findConfigurations(String searchTerm) {
		 return configurationDAO.findConfigurations(searchTerm);
	  }
  
	  @Override
	  @Transactional
	  public List<configuration> getLatestConfigurations(int maxResults) {
		return configurationDAO.getLatestConfigurations(maxResults);
	  }
	
	  @Override
	  @Transactional
	  public Long findTotalConfigs() {
		  return configurationDAO.findTotalConfigs();
	  }
	  
	  @Override
	  @Transactional
	  public Long getTotalConnections(int configId) {
		  return configurationDAO.getTotalConnections(configId);
	  }
	  
	  @Override
	  @Transactional
	  public void updateCompletedSteps(int configId, int stepCompleted) {
		  configurationDAO.updateCompletedSteps(configId, stepCompleted);
	  }
	  
	  @SuppressWarnings("rawtypes")
	  @Override
	  @Transactional
	  public List getFileTypes() {
		  return configurationDAO.getFileTypes();
	  }
	  
	  @Override
	  @Transactional
	  public List<configurationDataTranslations> getDataTranslations(int configId, int transportMethod) {
		  return configurationDAO.getDataTranslations(configId, transportMethod);
	  }
	  
	 @Override
	 @Transactional
	 public String getFieldName(int fieldId) {
		return configurationDAO.getFieldName(fieldId);
	 }
	 
	@Override
	@Transactional
	public void deleteDataTranslations(int configId, int transportMethod) {
		configurationDAO.deleteDataTranslations(configId, transportMethod);
	}
	
	@Override
	@Transactional
	public void saveDataTranslations(configurationDataTranslations translations) {
		configurationDAO.saveDataTranslations(translations);
	}
	
	@Override
	@Transactional
	public  List<Macros> getMacros() {
		return configurationDAO.getMacros();
	}
	
	@Override
	@Transactional
	public List<Connections> getConnections(int configId) {
		return configurationDAO.getConnections(configId);
	}

}
