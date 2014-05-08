package com.ut.dph.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.ut.dph.dao.messageTypeDAO;
import com.ut.dph.dao.organizationDAO;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.model.Crosswalks;
import com.ut.dph.model.Organization;
import com.ut.dph.model.messageType;
import com.ut.dph.model.messageTypeDataTranslations;
import com.ut.dph.model.messageTypeFormFields;
import com.ut.dph.model.validationType;
import com.ut.dph.reference.fileSystem;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Sheet;

@Service
public class messageTypeManagerImpl implements messageTypeManager {

    @Autowired
    private messageTypeDAO messageTypeDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private organizationDAO organizationDAO;

    @Override
    @Transactional
    public Integer createMessageType(messageType messageType) {
        Integer lastId = null;

        MultipartFile file = messageType.getFile();
        String fileName = file.getOriginalFilename();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = file.getInputStream();
            File newFile = null;

            //Set the directory to save the uploaded message type template to
            fileSystem dir = new fileSystem();
            dir.setMessageTypeDir("libraryFiles");

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
            messageType.setTemplateFile(fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Submit the new message type to the database
        lastId = (Integer) messageTypeDAO.createMessageType(messageType);

        //Call the function that will load the content of the message type excel file
        //into the messageTypeFormFields table
        loadExcelContents(lastId, fileName);

        return lastId;
    }

    @Override
    @Transactional
    public void updateMessageType(messageType messageType) {
        //Update the selected message type
        messageTypeDAO.updateMessageType(messageType);
    }

    @Override
    @Transactional
    public void saveMessageTypeFields(messageTypeFormFields formField) {
        messageTypeDAO.saveMessageTypeFields(formField);
    }

    @Override
    @Transactional
    public List<messageType> getMessageTypes() {
        return messageTypeDAO.getMessageTypes();
    }

    @Override
    @Transactional
    public List<messageType> getAvailableMessageTypes(int orgId) {
        return messageTypeDAO.getAvailableMessageTypes(orgId);
    }

    @Override
    @Transactional
    public List<messageType> getLatestMessageTypes(int maxResults) {
        return messageTypeDAO.getLatestMessageTypes(maxResults);
    }

    @Override
    @Transactional
    public List<messageType> getActiveMessageTypes() {
        return messageTypeDAO.getActiveMessageTypes();
    }

    @Override
    @Transactional
    public messageType getMessageTypeById(int messageTypeId) {
        return messageTypeDAO.getMessageTypeById(messageTypeId);
    }

    @Override
    @Transactional
    public messageType getMessageTypeByName(String name) {
        return messageTypeDAO.getMessageTypeByName(name);
    }

    @Override
    @Transactional
    public Long findTotalMessageTypes() {
        return messageTypeDAO.findTotalMessageTypes();
    }

    @Override
    @Transactional
    public double findTotalCrosswalks(int orgId) {
        return messageTypeDAO.findTotalCrosswalks(orgId);
    }

    @Override
    @Transactional
    public void deleteMessageType(int messageTypeId) {

        //First get the message type details
        messageType messageType = messageTypeDAO.getMessageTypeById(messageTypeId);

        //Next delete the actual attachment
        fileSystem currdir = new fileSystem();
        currdir.setMessageTypeDir("libraryFiles");
        File currFile = new File(currdir.getDir() + messageType.getTemplateFile());
        currFile.delete();

        messageTypeDAO.deleteMessageType(messageTypeId);
    }

    @Override
    @Transactional
    public List<messageTypeFormFields> getMessageTypeFields(int messageTypeId) {
        return messageTypeDAO.getMessageTypeFields(messageTypeId);
    }

    @Override
    @Transactional
    public void updateMessageTypeFields(messageTypeFormFields formField) {

        //Update the message type form field mappings
        messageTypeDAO.updateMessageTypeFields(formField);
    }

    @SuppressWarnings("rawtypes")
    @Override
    @Transactional
    public List getInformationTables() {
        return messageTypeDAO.getInformationTables();
    }

    @SuppressWarnings("rawtypes")
    @Override
    @Transactional
    public List getAllTables() {
        return messageTypeDAO.getAllTables();
    }

    @SuppressWarnings("rawtypes")
    @Override
    @Transactional
    public List getTableColumns(String tableName) {
        return messageTypeDAO.getTableColumns(tableName);
    }

    @SuppressWarnings("rawtypes")
    @Override
    @Transactional
    public List getValidationTypes() {
        return messageTypeDAO.getValidationTypes();
    }

    @SuppressWarnings("rawtypes")
    @Override
    @Transactional
    public String getValidationById(int id) {
        return messageTypeDAO.getValidationById(id);
    }

    @SuppressWarnings("rawtypes")
    @Override
    @Transactional
    public List getDelimiters() {
        return messageTypeDAO.getDelimiters();
    }

    @Override
    @Transactional
    public Long getTotalFields(int messageTypeId) {
        return messageTypeDAO.getTotalFields(messageTypeId);
    }

    @Override
    @Transactional
    public List<Crosswalks> getCrosswalks(int page, int maxResults, int orgId) {
        return messageTypeDAO.getCrosswalks(page, maxResults, orgId);
    }

    @SuppressWarnings("rawtypes")
    @Override
    @Transactional
    public List getCrosswalkData(int cwId) {
        return messageTypeDAO.getCrosswalkData(cwId);
    }

    @Override
    @Transactional
    public String getFieldName(int fieldId) {
        return messageTypeDAO.getFieldName(fieldId);
    }

    @Override
    @Transactional
    public String getCrosswalkName(int cwId) {
        return messageTypeDAO.getCrosswalkName(cwId);
    }

    @Override
    @Transactional
    public Long checkCrosswalkName(String name, int orgId) {
        return messageTypeDAO.checkCrosswalkName(name, orgId);
    }

    @Override
    @Transactional
    public Integer createCrosswalk(Crosswalks crosswalkDetails) {
        Integer lastId = null;
        String cleanURL = null;

        MultipartFile file = crosswalkDetails.getFile();
        String fileName = file.getOriginalFilename();

        InputStream inputStream = null;
        OutputStream outputStream = null;
        fileSystem dir = new fileSystem();

        if (crosswalkDetails.getOrgId() > 0) {
            Organization orgDetails = organizationDAO.getOrganizationById(crosswalkDetails.getOrgId());
            cleanURL = orgDetails.getcleanURL();
            dir.setDir(cleanURL, "crosswalks");
        } else {
            //Set the directory to save the uploaded message type template to
            dir.setMessageTypeCrosswalksDir("libraryFiles");
        }

        File newFile = null;
        newFile = new File(dir.getDir() + fileName);

        try {
            inputStream = file.getInputStream();

            if (!newFile.exists()) {
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
            crosswalkDetails.setfileName(fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Need to get the actual delimiter character
        String delimChar = (String) messageTypeDAO.getDelimiterChar(crosswalkDetails.getFileDelimiter());

        //Check to make sure the file contains the selected delimiter
        //Set the directory that holds the crosswalk files
        int delimCount = (Integer) dir.checkFileDelimiter(dir, fileName, delimChar);

        if (delimCount > 0) {
            //Submit the new message type to the database
            lastId = (Integer) messageTypeDAO.createCrosswalk(crosswalkDetails);

            //Call the function that will load the content of the crosswalk text file
            //into the rel_crosswalkData table
            loadCrosswalkContents(lastId, fileName, delimChar, cleanURL);

            return lastId;
        } else {
            //Need to delete the file
            newFile.delete();

            //Need to return an error
            return 0;
        }
    }

    @Override
    @Transactional
    public Crosswalks getCrosswalk(int cwId) {
        return messageTypeDAO.getCrosswalk(cwId);
    }

    @Override
    @Transactional
    public void saveDataTranslations(messageTypeDataTranslations translations) {
        messageTypeDAO.saveDataTranslations(translations);
    }

    @Override
    @Transactional
    public void deleteDataTranslations(int messageTypeId) {
        messageTypeDAO.deleteDataTranslations(messageTypeId);
    }

    @Override
    @Transactional
    public List<messageTypeDataTranslations> getMessageTypeTranslations(int messageTypeId) {
        return messageTypeDAO.getMessageTypeTranslations(messageTypeId);
    }

    /**
     * The 'loadCrosswalkContents' will take the contents of the uploaded text template file and populate the rel_crosswalkData table.
     *
     * @param id id: value of the latest added crosswalk
     * @param fileName	fileName: file name of the uploaded text file.
     * @param delim	delim: the delimiter used in the file
     *
     */
    public void loadCrosswalkContents(int id, String fileName, String delim, String cleanURL) {

        //Set the directory that holds the crosswalk files
        fileSystem dir = new fileSystem();

        if (cleanURL == null) {
            dir.setMessageTypeCrosswalksDir("libraryFiles");
        } else {
            dir.setDir(cleanURL, "crosswalks");
        }

        FileInputStream file = null;
        String[] lineValue = null;
        try {
            file = new FileInputStream(new File(dir.getDir() + fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(file));

        try {
            String line = null;
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (line != null) {

                //Need to parse each line via passed in delimiter
                if (delim == "t") {
                    lineValue = line.split("\t");
                } else {
                    lineValue = line.split("\\" + delim);
                }
                String sourceValue = lineValue[0];
                String targetValue = lineValue[1];
                String descVal = lineValue[2];

                //Need to insert all the fields into the crosswalk data Fields table
                Query query = sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO rel_crosswalkData (crosswalkId, sourceValue, targetValue, descValue)"
                        + "VALUES (:crosswalkid, :sourceValue, :targetValue, :descVal)")
                        .setParameter("crosswalkid", id)
                        .setParameter("sourceValue", sourceValue)
                        .setParameter("targetValue", targetValue)
                        .setParameter("descVal", descVal);

                query.executeUpdate();

                try {
                    line = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The 'loadExcelContents' will take the contents of the uploaded excel template file and populate the corresponding message type form fields table. This function will split up the contents into the appropriate buckets. Buckets (1 - 4) will be separated by spacer rows with in the excel file.
     *
     * @param id id: value of the latest added message type
     * @param fileName	fileName: file name of the uploaded excel file.
     *
     */
    public void loadExcelContents(int id, String fileName) {

        try {
            //Set the initial value of the buckets (1);
            Integer bucketVal = 1;

            //Set the initial value of the field number (0);
            Integer fieldNo = 0;

            //Set the initial value of the display position for the field
            //within each bucket (0);
            Integer dspPos = 0;

            //Set the directory that will hold the message type library excel files
            fileSystem dir = new fileSystem();
            dir.setMessageTypeDir("libraryFiles");

            //Create Workbook instance holding reference to .xlsx file
            OPCPackage pkg = null;
            XSSFWorkbook workbook = null;

            FileInputStream file = new FileInputStream(new File(dir.getDir() + fileName));

            try {
                pkg = OPCPackage.open(new File(dir.getDir() + fileName));

                workbook = new XSSFWorkbook(pkg);

            } catch (Exception e1) {
                e1.printStackTrace();
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
                    dspPos = 0;
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
                    Query query = sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO messageTypeFormFields (messageTypeId, fieldNo, fieldDesc, fieldLabel, validationType, required, bucketNo, bucketDspPos)"
                            + "VALUES (:messageTypeId, :fieldNo, :fieldDesc, :fieldLabel, 0, :required, :bucketNo, :bucketDspPos)")
                            .setParameter("messageTypeId", id)
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

    // this does the same thing as getValidationTypes except putting result in an object
    //TODO need to combine and test and replace getValidationTypes
    @Override
    public List<validationType> getValidationTypes1() {
        return messageTypeDAO.getValidationTypes1();
    }
    
    @Override
    @Transactional
    public List<messageType> getAssociatedMessageTypes(int orgId) {
        return messageTypeDAO.getAssociatedMessageTypes(orgId);
    }

}
