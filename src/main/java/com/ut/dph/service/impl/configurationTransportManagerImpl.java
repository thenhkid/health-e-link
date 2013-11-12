package com.ut.dph.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.dao.configurationTransportDAO;
import com.ut.dph.service.configurationTransportManager;

import org.springframework.stereotype.Service;

@Service
public class configurationTransportManagerImpl implements configurationTransportManager {
	
	@Autowired
	private configurationTransportDAO configurationTransportDAO;

 	@Override
 	@Transactional
 	public configurationTransport getTransportDetails(int configId) {
 		return configurationTransportDAO.getTransportDetails(configId);
 	}
 	
 	@Override
 	@Transactional
 	public void updateTransportDetails(configurationTransport transportDetails) {
 		configurationTransportDAO.updateTransportDetails(transportDetails);
 	}
 	
 	@Override
 	@Transactional
 	public void copyMessageTypeFields(int configId, int messageTypeId) {
 		configurationTransportDAO.copyMessageTypeFields(configId, messageTypeId);
 	}
 	
 	@SuppressWarnings("rawtypes")
	public List getTransportMethods() {
 		return configurationTransportDAO.getTransportMethods();
 	}
 	
 	@Override
 	@Transactional
 	public List<configurationFormFields> getConfigurationFields(int configId) {
 		return configurationTransportDAO.getConfigurationFields(configId);
 	}
 	
 	@Override
 	@Transactional
 	public void updateConfigurationFormFields(configurationFormFields formField) {
 		configurationTransportDAO.updateConfigurationFormFields(formField);
 	}
 	
 	@Override
 	@Transactional
 	public void populateConfigurationFieldMappings(int configId) {
 		configurationTransportDAO.populateConfigurationFieldMappings(configId);
 	}

}
