package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;

public interface configurationTransportDAO {
	
	List<configurationTransport> getTransportDetails(int configId);
	
	Integer updateTransportDetails(configurationTransport transportDetails, int clearFields);
	
	List<configurationFormFields> getConfigurationFields(int configId);
	
	void updateConfigurationFormFields(configurationFormFields formField);
	
	@SuppressWarnings("rawtypes")
	List getTransportMethods();
	
	void setupOnlineForm(int configId, int messageTypeId);

}
