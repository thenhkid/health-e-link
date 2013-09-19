package com.ut.dph.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.dao.configurationDAO;
import com.ut.dph.model.configuration;
import com.ut.dph.service.configurationManager;

@Service
public class configurationManagerImpl implements configurationManager {

  @Autowired
  private configurationDAO configurationDAO;
  
  @Override
  @Transactional
  public void saveConfiguration(configuration configuration) {
    configurationDAO.saveConfiguration(configuration);
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
  public List<configuration> getConfigurations(int firstResults, int maxResults) {
    return configurationDAO.getConfigurations(firstResults, maxResults);
  }

}
