package com.ut.dph.dao;

import com.ut.dph.model.configurationFTPFields;

import java.util.List;

import com.ut.dph.model.User;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationMessageSpecs;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.configurationTransportMessageTypes;
import org.springframework.stereotype.Repository;

@Repository
public interface configurationTransportDAO {

    configurationTransport getTransportDetails(int configId) throws Exception;

    Integer updateTransportDetails(configurationTransport transportDetails);

    configurationTransport getTransportDetailsByTransportMethod(int configId, int transportMethod);

    List<configurationFormFields> getConfigurationFields(int configId, int transportDetailId);
    
    List<configurationFormFields> getConfigurationFieldsByBucket(int configId, int transportDetailId, int bucket) throws Exception;
    
    configurationFormFields getConfigurationFieldsByFieldNo(int configId, int transportDetailId, int fieldNo) throws Exception;

    void updateConfigurationFormFields(configurationFormFields formField);

    @SuppressWarnings("rawtypes")
    List getTransportMethods();

    void setupOnlineForm(int transportId, int configId, int messageTypeId);

    List<configurationFTPFields> getTransportFTPDetails(int transportDetailId) throws Exception;
    
    configurationFTPFields getTransportFTPDetailsPush(int transportDetailId) throws Exception;
    
    configurationFTPFields getTransportFTPDetailsPull(int transportDetailId) throws Exception;
    
    void saveTransportFTP(configurationFTPFields FTPFields);
    
    String getTransportMethodById(int Id);
    
    List<configurationTransportMessageTypes> getTransportMessageTypes(int configTransportId);
    
    void deleteTransportMessageTypes(int configTransportId);
     
    void saveTransportMessageTypes(configurationTransportMessageTypes messageType);
    
    void copyExistingTransportMethod(int configTransportId, int configId);

    List <configurationFormFields> getRequiredFieldsForConfig (Integer configId);
    
    List <configurationFormFields> getCffByValidationType(Integer configId, Integer validationTypeId);
    
    List <configurationTransport> getDistinctConfigTransportForOrg(Integer orgId, Integer transportMethodId);

    List <configurationMessageSpecs> getConfigurationMessageSpecsForUserTransport(Integer userId, Integer transportMethodId, boolean getZeroMessageTypeCol);
    
    configurationFormFields getCFFByFieldNo(int configId, int fieldNo) throws Exception;
    
    List <configurationMessageSpecs> getConfigurationMessageSpecsForOrgTransport(Integer orgId, Integer transportMethodId, boolean getZeroMessageTypeCol);
   
    List <configurationTransport> getTransportsByMethodId (boolean notInJob, Integer status, Integer transportMethodId);
    
    List <User> getUserIdFromConnForTransport (Integer configurationTransportId);
    
    List<User> getOrgUserIdForTransport (Integer configurationTransportId);
    
    List<configurationTransport> getConfigTransportForFileExt(String fileExt, Integer transportMethodId);
    
    List <configurationTransport> getTransportListForFileExt(String fileExt, Integer transportMethodId);
    
    configurationTransport getTransportDetailsByTransportId(Integer transportId);
    
    Integer getOrgIdForFTPPath (configurationFTPFields ftpInfo) throws Exception;
    
    Integer getMinMaxFileSize(String fileExt, Integer transportMethodId);
}
