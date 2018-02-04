package com.ut.healthelink.service;

import com.ut.healthelink.model.configurationFTPFields;

import java.util.List;

import com.ut.healthelink.model.TransportMethod;
import com.ut.healthelink.model.configurationFormFields;
import com.ut.healthelink.model.configurationMessageSpecs;
import com.ut.healthelink.model.configurationRhapsodyFields;
import com.ut.healthelink.model.configurationTransport;
import com.ut.healthelink.model.configurationTransportMessageTypes;
import com.ut.healthelink.model.configurationWebServiceFields;
import com.ut.healthelink.model.configurationWebServiceSenders;

public interface configurationTransportManager {

    configurationTransport getTransportDetails(int configId) throws Exception;

    configurationTransport getTransportDetailsByTransportMethod(int configId, int transportMethod);

    Integer updateTransportDetails(configurationTransport transportDetails) throws Exception;

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
    
    void saveTransportFTP(int orgId, configurationFTPFields FTPFields) throws Exception;
    
    String getTransportMethodById(int Id);
    
    List<configurationTransportMessageTypes> getTransportMessageTypes(int configTransportId);
    
    void deleteTransportMessageTypes(int configTransportId);
    
    void saveTransportMessageTypes(configurationTransportMessageTypes messageType);
    
    void copyExistingTransportMethod(int configTransportId, int configId);
   
    List<configurationFormFields> getRequiredFieldsForConfig(Integer configId);
    
    List<configurationFormFields> getCffByValidationType(Integer configId, Integer validationTypeId);
    
    List <configurationTransport> getDistinctConfigTransportForOrg(Integer orgId, Integer transportMethodId);
    
    List<configurationMessageSpecs> getConfigurationMessageSpecsForUserTransport(Integer userId, Integer transportMethodId, boolean getZeroMessageTypeCol);
    
    configurationFormFields getCFFByFieldNo(int configId, int fieldNo) throws Exception;
    
    List <configurationMessageSpecs> getConfigurationMessageSpecsForOrgTransport(Integer orgId, Integer transportMethodId, boolean getZeroMessageTypeCol);
    
    List<configurationTransport> getConfigTransportForFileExtAndPath(String fileExt, Integer transportMethodId, Integer status, String inputPath);
    
    List<configurationTransport>  getTransportListForFileExtAndPath(String fileExt, Integer transportMethodId, Integer status, String inputPath);
    
    configurationTransport getTransportDetailsByTransportId(Integer transportId);
    
    Integer getOrgIdForFTPPath (configurationFTPFields ftpInfo) throws Exception;
    
    Integer getMinMaxFileSize(String fileExt, Integer transportMethodId);
    
    List <configurationTransport> getCountContainsHeaderRow(String fileExt, Integer transportMethodId);
    
    List <Integer> getConfigCount(String fileExt, Integer transportMethodId, Integer fileDelimiter);
    
    List<configurationTransport> getDistinctDelimCharForFileExt(String fileExt, Integer transportMethodId);
    
    void saveTransportRhapsody(configurationRhapsodyFields rhapsodyFields) throws Exception;
    
    List<configurationRhapsodyFields> getTransRhapsodyDetails(int transportDetailId) throws Exception;
    
    configurationRhapsodyFields getTransRhapsodyDetailsPush(int transportDetailId) throws Exception;
    
    configurationRhapsodyFields getTransRhapsodyDetailsPull(int transportDetailId) throws Exception;
    
    List <configurationTransport>  getTransportEncoding(String fileExt, Integer transportMethodId);
    
    Integer getOrgIdForRhapsodyPath (configurationRhapsodyFields rhapsodyInfo) throws Exception;
    
    List <TransportMethod> getTransportMethods(List <Integer> statusIds);
    
    List <configurationTransport> getConfigurationTransportFileExtByFileType(Integer orgId, Integer transportMethodId, List<Integer> fileTypeIds, List <Integer> statusIds, boolean distinctOnly, boolean foroutboundProcessing);

    List<configurationWebServiceFields> getTransWSDetails(int transportDetailId) throws Exception;
    
    void saveTransportWebService(configurationWebServiceFields wsFields) throws Exception;
    
    List<configurationTransport> getDistinctTransportDetailsForOrgByTransportMethodId (Integer transportMethodId, Integer status, Integer orgId);
    
    List<configurationTransport> getCTForOrgByTransportMethodId(Integer transportMethodId, Integer status, Integer orgId);

    configurationWebServiceFields getTransWSDetailsPush(int transportDetailId) throws Exception;
    
    configurationWebServiceFields getTransWSDetailsPull(int transportDetailId) throws Exception;
    
    List <configurationWebServiceSenders> getWSSenderList(int transportDetailId) throws Exception;
    
    void saveWSSender(configurationWebServiceSenders wsSender) throws Exception;
    
    void deleteWSSender(configurationWebServiceSenders wsSender) throws Exception;
    
    boolean hasConfigsWithMasstranslations(Integer orgId, Integer transportMethodId) throws Exception;
      
    void checkAndCreateDirectory(configurationRhapsodyFields rhapsodyInfo) throws Exception;
}
