package com.ut.healthelink.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.healthelink.dao.configurationDAO;
import com.ut.healthelink.dao.organizationDAO;
import com.ut.healthelink.model.CrosswalkData;
import com.ut.healthelink.model.HL7Details;
import com.ut.healthelink.model.HL7ElementComponents;
import com.ut.healthelink.model.HL7Elements;
import com.ut.healthelink.model.HL7Segments;
import com.ut.healthelink.model.Macros;
import com.ut.healthelink.model.Organization;
import com.ut.healthelink.model.configuration;
import com.ut.healthelink.model.configurationCCDElements;
import com.ut.healthelink.model.configurationConnection;
import com.ut.healthelink.model.configurationConnectionReceivers;
import com.ut.healthelink.model.configurationConnectionSenders;
import com.ut.healthelink.model.configurationDataTranslations;
import com.ut.healthelink.model.configurationExcelDetails;
import com.ut.healthelink.model.configurationMessageSpecs;
import com.ut.healthelink.model.configurationSchedules;
import com.ut.healthelink.reference.fileSystem;
import com.ut.healthelink.service.configurationManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
    public List<configuration> getConfigurations() {
        return configurationDAO.getConfigurations();
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
    public void deleteDataTranslations(int configId, int categoryId) {
        configurationDAO.deleteDataTranslations(configId, categoryId);
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
    public List<Macros> getMacrosByCategory(int categoryId) {
        return configurationDAO.getMacrosByCategory(categoryId);
    }

    @Override
    @Transactional
    public Macros getMacroById(int macroId) {
        return configurationDAO.getMacroById(macroId);
    }

    @Override
    @Transactional
    public List<configurationConnection> getAllConnections() {
        return configurationDAO.getAllConnections();
    }

    @Override
    @Transactional
    public List<configurationConnection> getLatestConnections(int maxResults) {
        return configurationDAO.getLatestConnections(maxResults);
    }

    @Override
    @Transactional
    public List<configurationConnection> getConnectionsByConfiguration(int configId, int userId) {
        return configurationDAO.getConnectionsByConfiguration(configId, userId);
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
    public void updateMessageSpecs(configurationMessageSpecs messageSpecs, int transportDetailId, int fileType) throws Exception {

        boolean processFile = false;
        String fileName = null;
        String cleanURL = null;
        int clearFields = 0;
        fileSystem dir = null;

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
                dir = new fileSystem();
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
                inputStream.close();

                //Set the filename to the file name
                messageSpecs.settemplateFile(fileName);

            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception (e);
            }
        }

        configurationDAO.updateMessageSpecs(messageSpecs, transportDetailId, clearFields);

        if (processFile == true) {
            try {
                loadExcelContents(messageSpecs.getconfigId(), transportDetailId, fileName, dir);
            } catch (Exception e1) {
                e1.printStackTrace();
                throw new Exception (e1);
            }

        }

    }

    /**
     * The 'loadExcelContents' will take the contents of the uploaded excel template file and populate the corresponding configuration form fields table. This function will split up the contents into the appropriate buckets. Buckets (1 - 4) will be separated by spacer rows with in the excel file.
     *
     * @param id value of the latest added configuration
     * @param fileName	file name of the uploaded excel file.
     * @param dir	the directory of the uploaded file
     *
     */
    public void loadExcelContents(int id, int transportDetailId, String fileName, fileSystem dir) throws Exception {
    	String errorMessage = "";
        try {
            //Set the initial value of the buckets (1);
            Integer bucketVal = new Integer(1);

            //Set the initial value of the field number (0);
            Integer fieldNo = new Integer(0);

            //Set the initial value of the display position for the field
            //within each bucket (0);
            Integer dspPos = new Integer(0);

            //Create Workbook instance holding reference to .xlsx file
            OPCPackage pkg = null;
            XSSFWorkbook workbook = null;

            try {
                pkg = OPCPackage.open(new File(dir.getDir() + fileName));

                workbook = new XSSFWorkbook(pkg);

            } catch (Exception e1) {
                e1.printStackTrace();
                errorMessage = errorMessage + "<br/>" + e1.getMessage();
            }

            //Get first/desired sheet from the workbook
            Sheet sheet = workbook.getSheetAt(0);

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
                        /*switch (cell.getCellType()) {
                         case Cell.CELL_TYPE_BOOLEAN:
                         required = cell.getBooleanCellValue();
                         break;

                         case Cell.CELL_TYPE_STRING:
                         fieldDesc = cell.getStringCellValue();
                         break;
                         }*/
                        //Check the cell type and format accordingly
                        if (cell.getColumnIndex() == 0) {
                            fieldDesc = cell.getStringCellValue();
                        } else if (cell.getColumnIndex() == 1) {
                            required = cell.getBooleanCellValue();
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
                pkg.close();
            } catch (IOException e) {
                e.printStackTrace();
                errorMessage = errorMessage + "<br/>" + e.getMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = errorMessage + "<br/>" + e.getMessage();
        }
        
        /** throw error message here because want to make sure file stream is closed **/
        if (!errorMessage.equalsIgnoreCase("")) {
        	throw new Exception(errorMessage);
        }
        
    }

    @Override
    public List<configurationDataTranslations> getDataTranslationsWithFieldNo(
            int configId, int categoryId) {
        return configurationDAO.getDataTranslationsWithFieldNo(configId, categoryId);
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
    public void updateHL7Details(HL7Details details) {
        configurationDAO.updateHL7Details(details);
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
    public int saveHL7Details(HL7Details details) {
        return configurationDAO.saveHL7Details(details);
    }

    @Override
    public int saveHL7Segment(HL7Segments newSegment) {
        return configurationDAO.saveHL7Segment(newSegment);
    }

    @Override
    public int saveHL7Element(HL7Elements newElement) {
        return configurationDAO.saveHL7Element(newElement);
    }

    @Override
    public void saveHL7Component(HL7ElementComponents newcomponent) {
        configurationDAO.saveHL7Component(newcomponent);
    }

    @Override
    public String getMessageTypeNameByConfigId(Integer configId) {
        return configurationDAO.getMessageTypeNameByConfigId(configId);
    }

    @SuppressWarnings("rawtypes")
    @Override
    @Transactional
    public List getEncodings() {
        return configurationDAO.getEncodings();
    }
    
    @Override
    public void removeHL7ElementComponent(Integer componentId) {
        configurationDAO.removeHL7ElementComponent(componentId);
    }
    
    @Override
    public void removeHL7Element(Integer elementId) {
        configurationDAO.removeHL7Element(elementId);
    }
    
    @Override
     public void removeHL7Segment(Integer segmentId) {
         configurationDAO.removeHL7Segment(segmentId);
     }
     
     @Override
     public List<configurationCCDElements> getCCDElements(Integer configId) throws Exception {
         return configurationDAO.getCCDElements(configId);
     }
     
     @Override
     public void saveCCDElement(configurationCCDElements ccdElement) throws Exception {
         configurationDAO.saveCCDElement(ccdElement);
     }
     
     @Override
     public configurationCCDElements getCCDElement(Integer elementId) throws Exception {
         return configurationDAO.getCCDElement(elementId);
     }

	@Override
	public configurationExcelDetails getExcelDetails(Integer configId, Integer orgId)
			throws Exception {
		return configurationDAO.getExcelDetails(configId, orgId);
	}

	@Override
	public List <String> getConfigHeaderCols(configurationMessageSpecs cms)
			throws Exception {
		String selectCols = "";
		String insertCols = "";
		
		if (cms.getrptField1() != 0) {
			selectCols += ("F" + cms.getrptField1() + ",");
			insertCols += "reportField1Data, ";
		}
		if (cms.getrptField1() != 0) {
			selectCols += ("F" + cms.getrptField2() + ",");
			insertCols += "reportField2Data, ";
		}
		if (cms.getrptField1() != 0) {
			selectCols += ("F" + cms.getrptField3() + ",");
			insertCols += "reportField3Data, ";
		}
		if (cms.getrptField1() != 0) {
			selectCols += ("F" + cms.getrptField4());
			insertCols += "reportField4Data";
		}
		if (insertCols.endsWith(",")) {
			insertCols += insertCols.substring(0, insertCols.length() - 1);
			selectCols += selectCols.substring(0, selectCols.length() - 1);
		}
		
		List<String> headerCols = Arrays.asList(selectCols, insertCols);
		
		return headerCols;
	}
}
