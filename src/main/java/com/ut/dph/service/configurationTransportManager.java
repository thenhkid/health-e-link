package com.ut.dph.service;

import java.util.List;

import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;

public interface configurationTransportManager {
	
	configurationTransport getTransportDetails(int configId);
	
	void updateTransportDetails(configurationTransport transportDetails);
	
	void copyMessageTypeFields(int configId, int messageTypeId);
	
	List<configurationFormFields> getConfigurationFields(int configId);
	
	void updateConfigurationFormFields(configurationFormFields formField);
	
	void populateConfigurationFieldMappings(int configId);
	 
	@SuppressWarnings("rawtypes")
	List getTransportMethods();

}
