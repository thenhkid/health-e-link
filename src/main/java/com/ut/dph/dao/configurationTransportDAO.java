package com.ut.dph.dao;

import com.ut.dph.model.configurationFTPFields;
import java.util.List;

import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;

public interface configurationTransportDAO {

    configurationTransport getTransportDetails(int configId);

    Integer updateTransportDetails(configurationTransport transportDetails, int clearFields);

    configurationTransport getTransportDetailsByTransportMethod(int configId, int transportMethod);

    List<configurationFormFields> getConfigurationFields(int configId, int transportDetailId);
    
    List<configurationFormFields> getConfigurationFieldsByBucket(int configId, int transportDetailId, int bucket);

    void updateConfigurationFormFields(configurationFormFields formField);

    @SuppressWarnings("rawtypes")
    List getTransportMethods();

    void setupOnlineForm(int transportId, int configId, int messageTypeId);

    List<configurationFTPFields> getTransportFTPDetails(int transportDetailId);
    
    void saveTransportFTP(configurationFTPFields FTPFields);

}
