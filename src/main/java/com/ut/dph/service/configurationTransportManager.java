package com.ut.dph.service;

import com.ut.dph.model.configurationFTPFields;

import java.util.List;

import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.configurationTransportMessageTypes;

public interface configurationTransportManager {

    configurationTransport getTransportDetails(int configId) throws Exception;

    configurationTransport getTransportDetailsByTransportMethod(int configId, int transportMethod);

    Integer updateTransportDetails(configurationTransport transportDetails);

    List<configurationFormFields> getConfigurationFields(int configId, int transportDetailId);
    
    List<configurationFormFields> getConfigurationFieldsByBucket(int configId, int transportDetailId, int bucket) throws Exception;

    void updateConfigurationFormFields(configurationFormFields formField);

    @SuppressWarnings("rawtypes")
    List getTransportMethods();

    void setupOnlineForm(int transportId, int configId, int messageTypeId);

    List<configurationFTPFields> getTransportFTPDetails(int transportDetailId);
    
    void saveTransportFTP(configurationFTPFields FTPFields);
    
    String getTransportMethodById(int Id);
    
    List<configurationTransportMessageTypes> getTransportMessageTypes(int configTransportId);
    
    void deleteTransportMessageTypes(int configTransportId);
    
    void saveTransportMessageTypes(configurationTransportMessageTypes messageType);
    
    void copyExistingTransportMethod(int configTransportId, int configId);
   
    List<configurationFormFields> getRequiredFieldsForConfig(Integer configId);
    
    List<configurationFormFields> getCffByValidationType(Integer configId, Integer validationTypeId);
}
