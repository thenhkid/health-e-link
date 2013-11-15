package com.ut.dph.service;

import java.util.List;

import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;

public interface configurationTransportManager {
	
	List<configurationTransport> getTransportDetails(int configId);
	
	void updateTransportDetails(configurationTransport transportDetails);
	
	List<configurationFormFields> getConfigurationFields(int configId);
	
	void updateConfigurationFormFields(configurationFormFields formField);
	 
	@SuppressWarnings("rawtypes")
	List getTransportMethods();
	
	void setupOnlineForm(int configId, int messageTypeId);

}
