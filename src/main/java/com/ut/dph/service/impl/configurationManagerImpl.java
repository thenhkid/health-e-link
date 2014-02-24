package com.ut.dph.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.dao.configurationDAO;
import com.ut.dph.dao.organizationDAO;
import com.ut.dph.model.CrosswalkData;
import com.ut.dph.model.HL7Details;
import com.ut.dph.model.HL7ElementComponents;
import com.ut.dph.model.HL7Elements;
import com.ut.dph.model.HL7Segments;
import com.ut.dph.model.Macros;
import com.ut.dph.model.Organization;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationConnectionReceivers;
import com.ut.dph.model.configurationConnectionSenders;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.configurationMessageSpecs;
import com.ut.dph.model.configurationSchedules;
import com.ut.dph.reference.fileSystem;
import com.ut.dph.service.configurationManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.web.multipart.MultipartFile;

@Service
public class configurationManagerImpl implements configurationManager {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private configurationDAO configurationDAO;

    @Autowired
    private organizationDAO organizationDAO;

    @Override
    @Transactional
    public Integer createConfiguration(configuration configuration) {
        configuration.setstepsCompleted(1);
        return configurationDAO.createConfiguration(configuration);
    }

    @Override
    @Transactional
    public void updateConfiguration(configuration configuration) {
        configurationDAO.updateConfiguration(configuration);
    }

    @Override
    @Transactional
    public configuration getConfigurationById(int configId) {
        return configurationDAO.getConfigurationById(configId);
    }

    @Override
    @Transactional
    public List<configuration> getConfigurationsByOrgId(int configId, String searchTerm) {
        return configurationDAO.getConfigurationsByOrgId(configId, searchTerm);
    }

    @Override
    @Transactional
    public List<configuration> getActiveConfigurationsByOrgId(int configId) {
        return configurationDAO.getActiveConfigurationsByOrgId(configId);
    }

    @Override
    @Transactional
    public configuration getConfigurationByName(String configName, int orgId) {
        return configurationDAO.getConfigurationByName(configName, orgId);
    }

    @Override
    @Transactional
    public List<configuration> getConfigurations(int page, int maxResults) {
        return configurationDAO.getConfigurations(page, maxResults);
    }

    @Override
    @Transactional
    public List<configuration> findConfigurations(String searchTerm, int configType) {
        return configurationDAO.findConfigurations(searchTerm, configType);
    }

    @Override
    @Transactional
    public List<configuration> getLatestConfigurations(int maxResults) {
        return configurationDAO.getLatestConfigurations(maxResults);
    }

    @Override
    @Transactional
    public Long findTotalConfigs() {
        return configurationDAO.findTotalConfigs();
    }

    @Override
    @Transactional
    public Long getTotalConnections(int configId) {
        return configurationDAO.getTotalConnections(configId);
    }

    @Override
    @Transactional
    public void updateCompletedSteps(int configId, int stepCompleted) {
        configurationDAO.updateCompletedSteps(configId, stepCompleted);
    }

    @SuppressWarnings("rawtypes")
    @Override
    @Transactional
    public List getFileTypes() {
        return configurationDAO.getFileTypes();
    }

    @Override
    @Transactional
    public String getFileTypesById(int id) {
        return configurationDAO.getFileTypesById(id);
    }

    @Override
    @Transactional
    public List<configurationDataTranslations> getDataTranslations(int configId) {
        return configurationDAO.getDataTranslations(configId);
    }

    @Override
    @Transactional
    public String getFieldName(int fieldId) {
        return configurationDAO.getFieldName(fieldId);
    }

    @Override
    @Transactional
    public void deleteDataTranslations(int configId) {
        configurationDAO.deleteDataTranslations(configId);
    }

    @Override
    @Transactional
    public void saveDataTranslations(configurationDataTranslations translations) {
        configurationDAO.saveDataTranslations(translations);
    }

    @Override
    @Transactional
    public List<Macros> getMacros() {
        return configurationDAO.getMacros();
    }

    @Override
    @Transactional
    public Macros getMacroById(int macroId) {
        return configurationDAO.getMacroById(macroId);
    }

    @Override
    @Transactional
    public List<configurationConnection> getAllConnections(int page, int maxResults) {
        return configurationDAO.getAllConnections(page, maxResults);
    }

    @Override
    @Transactional
    public List<configurationConnection> findConnections(String searchTerm) {
        return configurationDAO.findConnections(searchTerm);
    }

    @Override
    @Transactional
    public List<configurationConnection> getConnectionsByConfiguration(int configId) {
        return configurationDAO.getConnectionsByConfiguration(configId);
    }

    @Override
    @Transactional
    public List<configurationConnection> getConnectionsByTargetConfiguration(int configId) {
        return configurationDAO.getConnectionsByTargetConfiguration(configId);
    }

    @Override
    @Transactional
    public Integer saveConnection(configurationConnection connection) {
        return configurationDAO.saveConnection(connection);
    }

    @Override
    @Transactional
    public configurationConnection getConnection(int connectionId) {
        return configurationDAO.getConnection(connectionId);
    }

    @Override
    @Transactional
    public void updateConnection(configurationConnection connection) {
        configurationDAO.updateConnection(connection);
    }

    @Override
    @Transactional
    public configurationSchedules getScheduleDetails(int configId) {
        return configurationDAO.getScheduleDetails(configId);
    }

    @Override
    @Transactional
    public void saveSchedule(configurationSchedules scheduleDetails) {
        configurationDAO.saveSchedule(scheduleDetails);
    }

    @Override
    @Transactional
    public configurationMessageSpecs getMessageSpecs(int configId) {
        return configurationDAO.getMessageSpecs(configId);
    }

    @Override
    @Transactional
    public List<configuration> getActiveConfigurationsByUserId(int userId, int transportMethod) throws Exception {
        return configurationDAO.getActiveConfigurationsByUserId(userId, transportMethod);
    }

    @Override
    @Transactional
    public List<configurationConnectionSenders> getConnectionSenders(int connectionId) {
        return configurationDAO.getConnectionSenders(connectionId);
    }

    @Override
    @Transactional
    public List<configurationConnectionReceivers> getConnectionReceivers(int connectionId) {
        return configurationDAO.getConnectionReceivers(connectionId);
    }

    @Override
    @Transactional
    public void saveConnectionSenders(configurationConnectionSenders senders) {
        configurationDAO.saveConnectionSenders(senders);
    }

    @Override
    @Transactional
    public void saveConnectionReceivers(configurationConnectionReceivers receivers) {
        configurationDAO.saveConnectionReceivers(receivers);
    }

    @Override
    @Transactional
    public void removeConnectionSenders(int connectionId) {
        configurationDAO.removeConnectionSenders(connectionId);
    }

    @Override
    @Transactional
    public void removeConnectionReceivers(int connectionId) {
        configurationDAO.removeConnectionReceivers(connectionId);
    }

    @Override
    @Transactional
    public void updateMessageSpecs(configurationMessageSpecs messageSpecs, int transportDetailId) {

        boolean processFile = false;
        String fileName = null;
        String cleanURL = null;
        int clearFields = 0;

        MultipartFile file = messageSpecs.getFile();
        //If a file is uploaded
        if (file != null && !file.isEmpty()) {
            processFile = true;
            clearFields = 1;

            fileName = file.getOriginalFilename();

            InputStream inputStream = null;
            OutputStream outputStream = null;

            //Need to get the selected organization clean url
            configuration configDetails = configurationDAO.getConfigurationById(messageSpecs.getconfigId());
            Organization orgDetails = organizationDAO.getOrganizationById(configDetails.getorgId());
            cleanURL = orgDetails.getcleanURL();

            try {
                inputStream = file.getInputStream();
                File newFile = null;

                //Set the directory to save the uploaded message type template to
                fileSystem dir = new fileSystem();
                dir.setDir(cleanURL, "templates");

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

                //Set the filename to the file name
                messageSpecs.settemplateFile(fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configurationDAO.updateMessageSpecs(messageSpecs, transportDetailId, clearFields);

        if (processFile == true) {
            loadExcelContents(messageSpecs.getconfigId(), transportDetailId, fileName, cleanURL);
        }

    }

    /**
     * The 'loadExcelContents' will take the contents of the uploaded excel template file and populate the corresponding configuration form fields table. This function will split up the contents into the appropriate buckets. Buckets (1 - 4) will be separated by spacer rows with in the excel file.
     *
     * @param id id: value of the latest added configuration
     * @param fileName	fileName: file name of the uploaded excel file.
     * @param cleanURL	cleanURL: the cleanURL of the selected organization
     *
     */
    public void loadExcelContents(int id, int transportDetailId, String fileName, String cleanURL) {

        try {
            //Set the initial value of the buckets (1);
            Integer bucketVal = new Integer(1);

            //Set the initial value of the field number (0);
            Integer fieldNo = new Integer(0);

            //Set the initial value of the display position for the field
            //within each bucket (0);
            Integer dspPos = new Integer(0);

            //Set the directory that will hold the message type library excel files
            fileSystem dir = new fileSystem();
            dir.setDir(cleanURL, "templates");

            FileInputStream file = new FileInputStream(new File(dir.getDir() + fileName));

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = null;
            try {
                workbook = new XSSFWorkbook(file);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                //Check to see if empty spacer row
                Cell firstcell = row.getCell(1);

                if (firstcell.getCellType() == firstcell.CELL_TYPE_BLANK) {
                    //Found a spacer row change the bucket value
                    bucketVal++;

                    //When a spacer row is found need to reset the dspPos variable
                    dspPos = new Integer(0);
                } else {
                    //For each row, iterate through all the columns
                    Iterator<Cell> cellIterator = row.cellIterator();
                    boolean required = false;
                    String fieldDesc = "";

                    //Increase the field number by 1
                    fieldNo++;

                    //Increase the display position by 1
                    dspPos++;

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        //Check the cell type and format accordingly
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_BOOLEAN:
                                required = cell.getBooleanCellValue();
                                break;

                            case Cell.CELL_TYPE_STRING:
                                fieldDesc = cell.getStringCellValue();
                                break;
                        }
                    }

                    //Need to insert all the fields into the message type Form Fields table
                    Query query = sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO configurationFormFields (configId, transportDetailId, fieldNo, fieldDesc, fieldLabel, validationType, required, bucketNo, bucketDspPos, useField)"
                            + "VALUES (:configId, :transportDetailId, :fieldNo, :fieldDesc, :fieldLabel, 1, :required, :bucketNo, :bucketDspPos, 1)")
                            .setParameter("configId", id)
                            .setParameter("transportDetailId", transportDetailId)
                            .setParameter("fieldNo", fieldNo)
                            .setParameter("fieldDesc", fieldDesc)
                            .setParameter("fieldLabel", fieldDesc)
                            .setParameter("required", required)
                            .setParameter("bucketNo", bucketVal)
                            .setParameter("bucketDspPos", dspPos);

                    query.executeUpdate();

                }
            }
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<configurationDataTranslations> getDataTranslationsWithFieldNo(
            int configId) {
        return configurationDAO.getDataTranslationsWithFieldNo(configId);
    }

    @Override
    public List<CrosswalkData> getCrosswalkData(int cwId) {
        return configurationDAO.getCrosswalkData(cwId);
    }
    
    
    @Override
    public HL7Details getHL7Details(int configId) {
        return configurationDAO.getHL7Details(configId);
    }
    
    @Override
    public List<HL7Segments> getHL7Segments(int hl7Id) {
        return configurationDAO.getHL7Segments(hl7Id);
    }
    
    @Override
    @Transactional
    public List<HL7Elements> getHL7Elements(int hl7Id, int segmentId) {
        return configurationDAO.getHL7Elements(hl7Id, segmentId);
    }
    
    @Override
    public List<HL7ElementComponents> getHL7ElementComponents(int elementId) {
        return configurationDAO.getHL7ElementComponents(elementId);
    }

    @Override
    public void updateHL7Segments(HL7Segments segment) {
        configurationDAO.updateHL7Segments(segment);
    }
    
    @Override
    public void updateHL7Elements(HL7Elements element) {
        configurationDAO.updateHL7Elements(element);
    }
    
    @Override
    public void updateHL7ElementComponent(HL7ElementComponents component) {
        configurationDAO.updateHL7ElementComponent(component);
    }
    
    @Override
    public void saveHL7Segment(HL7Segments newSegment) {
        configurationDAO.saveHL7Segment(newSegment);
    }
    
    @Override
    public void saveHL7Element(HL7Elements newElement) {
        configurationDAO.saveHL7Element(newElement);
    }
    
    @Override
    public void saveHL7Component(HL7ElementComponents newcomponent) {
        configurationDAO.saveHL7Component(newcomponent);
    }
}
