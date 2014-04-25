package com.ut.dph.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationMessageSpecs;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.dao.configurationTransportDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.model.configurationFTPFields;
import com.ut.dph.model.configurationTransportMessageTypes;
import com.ut.dph.reference.fileSystem;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.organizationManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class configurationTransportManagerImpl implements configurationTransportManager {

    @Autowired
    private configurationTransportDAO configurationTransportDAO;
    
    @Autowired
    private organizationManager organizationManager;

    @Override
    @Transactional
    public configurationTransport getTransportDetails(int configId) throws Exception {
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
    public List<configurationFormFields> getConfigurationFieldsByBucket(int configId, int transportDetailId, int bucket) throws Exception {
        return configurationTransportDAO.getConfigurationFieldsByBucket(configId, transportDetailId, bucket);
    }

    @Override
    @Transactional
    public configurationFormFields getConfigurationFieldsByFieldNo(int configId, int transportDetailId, int fieldNo) throws Exception {
        return configurationTransportDAO.getConfigurationFieldsByFieldNo(configId, transportDetailId, fieldNo);
    }

    @Override
    @Transactional
    public void updateConfigurationFormFields(configurationFormFields formField) {
        configurationTransportDAO.updateConfigurationFormFields(formField);
    }

    @Override
    @Transactional
    public List<configurationFTPFields> getTransportFTPDetails(int transportDetailId) throws Exception {
        return configurationTransportDAO.getTransportFTPDetails(transportDetailId);
    }

    @Override
    @Transactional
    public configurationFTPFields getTransportFTPDetailsPush(int transportDetailId) throws Exception {
        return configurationTransportDAO.getTransportFTPDetailsPush(transportDetailId);
    }

    @Override
    @Transactional
    public configurationFTPFields getTransportFTPDetailsPull(int transportDetailId) throws Exception {
        return configurationTransportDAO.getTransportFTPDetailsPull(transportDetailId);
    }

    @Override
    @Transactional
    public void saveTransportFTP(int orgId, configurationFTPFields FTPFields) {
        
        /* Need to upload the certificate if uploaded */
        if (FTPFields.getfile() != null && FTPFields.getfile().getSize() > 0) {
            
            //Need to get the cleanURL of the organization for the brochure
            Organization orgDetails = organizationManager.getOrganizationById(orgId);

            MultipartFile file = FTPFields.getfile();
            String fileName = file.getOriginalFilename();

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                inputStream = file.getInputStream();
                File newFile = null;

                //Set the directory to save the brochures to
                fileSystem dir = new fileSystem();
                dir.setDir(orgDetails.getcleanURL(), "certificates");

                newFile = new File(dir.getDir() + fileName);
                
                if (newFile.exists()) {
                    int i = 1;
                    while (newFile.exists()) {
                        int iDot = fileName.lastIndexOf(".");
                        newFile = new File(dir.getDir() + fileName.substring(0, iDot) + "_(" + ++i + ")" + fileName.substring(iDot));
                    }
                    fileName = newFile.getName();
                } else {
                    newFile.createNewFile();
                }

                outputStream = new FileOutputStream(newFile);
                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                outputStream.close();

                //Set the filename to the original file name
                FTPFields.setcertification(fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

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
    public List<configurationFormFields> getCffByValidationType(Integer configId, Integer validationTypeId) {
        return configurationTransportDAO.getCffByValidationType(configId, validationTypeId);
    }

    @Override
    public List<configurationTransport> getDistinctConfigTransportForOrg(Integer orgId, Integer transportMethodId) {
        return configurationTransportDAO.getDistinctConfigTransportForOrg(orgId, transportMethodId);
    }

    @Override
    public List<configurationMessageSpecs> getConfigurationMessageSpecsForUserTransport(Integer userId, Integer transportMethodId, boolean getZeroMessageTypeCol) {
        return configurationTransportDAO.getConfigurationMessageSpecsForUserTransport(userId, transportMethodId, getZeroMessageTypeCol);
    }

    @Override
    public configurationFormFields getCFFByFieldNo(int configId, int fieldNo)
            throws Exception {
        return configurationTransportDAO.getCFFByFieldNo(configId, fieldNo);
    }

    @Override
    public List<configurationMessageSpecs> getConfigurationMessageSpecsForOrgTransport(Integer orgId, Integer transportMethodId, boolean getZeroMessageTypeCol) {
        return configurationTransportDAO.getConfigurationMessageSpecsForOrgTransport(orgId, transportMethodId, getZeroMessageTypeCol);
    }

}
