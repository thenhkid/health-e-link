package com.ut.dph.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.dao.configurationTransportDAO;
import com.ut.dph.model.configurationFTPFields;
import com.ut.dph.model.configurationTransportMessageTypes;
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
    public configurationTransport getTransportDetailsByTransportMethod(int configId, int transportMethod) {
        return configurationTransportDAO.getTransportDetailsByTransportMethod(configId, transportMethod);
    }

    @Override
    @Transactional
    public void setupOnlineForm(int transportId, int configId, int messageTypeId) {
        configurationTransportDAO.setupOnlineForm(transportId, configId, messageTypeId);
    }

    @Override
    @Transactional
    public Integer updateTransportDetails(configurationTransport transportDetails) {
        
        int transportDetailId;
        
        transportDetailId = (Integer) configurationTransportDAO.updateTransportDetails(transportDetails);
      
        return transportDetailId;
    }

    @SuppressWarnings("rawtypes")
    public List getTransportMethods() {
        return configurationTransportDAO.getTransportMethods();
    }

    @Override
    @Transactional
    public List<configurationFormFields> getConfigurationFields(int configId, int transportDetailId) {
        return configurationTransportDAO.getConfigurationFields(configId, transportDetailId);
    }
    
    @Override
    @Transactional
    public List<configurationFormFields> getConfigurationFieldsByBucket(int configId, int transportDetailId, int bucket) {
        return configurationTransportDAO.getConfigurationFieldsByBucket(configId, transportDetailId, bucket);
    }

    @Override
    @Transactional
    public void updateConfigurationFormFields(configurationFormFields formField) {
        configurationTransportDAO.updateConfigurationFormFields(formField);
    }
    
    @Override
    @Transactional
    public List<configurationFTPFields> getTransportFTPDetails(int transportDetailId) {
        return configurationTransportDAO.getTransportFTPDetails(transportDetailId);
    }
    
    @Override
    @Transactional
    public void saveTransportFTP(configurationFTPFields FTPFields) {
        configurationTransportDAO.saveTransportFTP(FTPFields);
    }
    
    @Override
    @Transactional
    public String getTransportMethodById(int Id) {
        return configurationTransportDAO.getTransportMethodById(Id);
    }
    
    @Override
    @Transactional
    public List<configurationTransportMessageTypes> getTransportMessageTypes(int configTransportId) {
        return configurationTransportDAO.getTransportMessageTypes(configTransportId);
    }
    
    @Override
    @Transactional
    public void deleteTransportMessageTypes(int configTransportId) {
        configurationTransportDAO.deleteTransportMessageTypes(configTransportId);
    }
    
    @Override
    @Transactional
    public void saveTransportMessageTypes(configurationTransportMessageTypes messageType) {
        configurationTransportDAO.saveTransportMessageTypes(messageType);
    }
    
    @Override
    @Transactional
    public void copyExistingTransportMethod(int configTransportId, int configId) {
        configurationTransportDAO.copyExistingTransportMethod(configTransportId, configId);
    }

	@Override
	public List<configurationFormFields> getRequiredFieldsForConfig(Integer configId) {
		 return configurationTransportDAO.getRequiredFieldsForConfig(configId);
	}
	
	@Override
	public List <configurationFormFields> getCffByValidationType(Integer configId, Integer validationTypeId) {
		 return configurationTransportDAO.getCffByValidationType(configId, validationTypeId );
	}
}
