package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;

public interface configurationTransportDAO {
	
	configurationTransport getTransportDetails(int configId);
	
	void updateTransportDetails(configurationTransport transportDetails, int clearFields);
	
	void copyMessageTypeFields(int configId, int messageTypeId);
	
	List<configurationFormFields> getConfigurationFields(int configId);
	
	void updateConfigurationFormFields(configurationFormFields formField);
	
	@SuppressWarnings("rawtypes")
	List getTransportMethods();

}
