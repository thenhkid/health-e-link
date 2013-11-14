package com.ut.dph.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.dao.configurationTransportDAO;
import com.ut.dph.dao.configurationDAO;
import com.ut.dph.dao.organizationDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.model.configuration;
import com.ut.dph.reference.fileSystem;
import com.ut.dph.service.configurationTransportManager;

import org.springframework.stereotype.Service;

@Service
public class configurationTransportManagerImpl implements configurationTransportManager {
	
	@Autowired
	private configurationTransportDAO configurationTransportDAO;
	
	@Autowired
	private organizationDAO organizationDAO;
	
	@Autowired
	private configurationDAO configurationDAO;
	
	@Autowired
	private SessionFactory sessionFactory;

 	@Override
 	@Transactional
 	public configurationTransport getTransportDetails(int configId) {
 		return configurationTransportDAO.getTransportDetails(configId);
 	}
 	
 	@Override
 	@Transactional
 	public void updateTransportDetails(configurationTransport transportDetails, int clearFields) {
 		boolean processFile = false;
 		String fileName = null;
 		String cleanURL = null;
 		
 		MultipartFile file = transportDetails.getFile(); 
 		
 		//If a file is uploaded
 		if(!file.isEmpty()) {
 			processFile = true;
 			
			fileName = file.getOriginalFilename();
		
			InputStream inputStream = null;  
			OutputStream outputStream = null;  
		
			//Need to get the selected organization clean url
			configuration configDetails = configurationDAO.getConfigurationById(transportDetails.getconfigId());
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
				   while(newFile.exists()) {
					   int iDot = fileName.lastIndexOf(".");
					   newFile = new File(dir.getDir() + fileName.substring(0,iDot)+"_("+ ++i + ")" + fileName.substring(iDot));
				   }
				   fileName = newFile.getName();
			   }  
			   else {
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
			   transportDetails.setfileName(fileName);
			   
			} 
			catch (IOException e) {  
			   e.printStackTrace();  
			}  
 		}
 		
		configurationTransportDAO.updateTransportDetails(transportDetails, clearFields);
		
		if(processFile == true) {
			loadExcelContents(transportDetails.getconfigId(), fileName, cleanURL);
		}
		
 	}
 	
 	@Override
 	@Transactional
 	public void copyMessageTypeFields(int configId, int messageTypeId) {
 		configurationTransportDAO.copyMessageTypeFields(configId, messageTypeId);
 	}
 	
 	@SuppressWarnings("rawtypes")
	public List getTransportMethods() {
 		return configurationTransportDAO.getTransportMethods();
 	}
 	
 	@Override
 	@Transactional
 	public List<configurationFormFields> getConfigurationFields(int configId) {
 		return configurationTransportDAO.getConfigurationFields(configId);
 	}
 	
 	@Override
 	@Transactional
 	public void updateConfigurationFormFields(configurationFormFields formField) {
 		configurationTransportDAO.updateConfigurationFormFields(formField);
 	}
 	
 	
 	/**
	 * The 'loadExcelContents' will take the contents of the uploaded excel template file and populate
	 * the corresponding configuration form fields table. This function will split up the contents into
	 * the appropriate buckets. Buckets (1 - 4) will be separated by spacer rows with in the excel file.
	 * 
	 * @param id 			id: value of the latest added configuration
	 * @param fileName		fileName: file name of the uploaded excel file.
	 * @param cleanURL		cleanURL: the cleanURL of the selected organization
	 * 
	 */
	public void loadExcelContents(int id, String fileName, String cleanURL) {
		
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
            
            while (rowIterator.hasNext()) 
            {
                Row row = rowIterator.next();
                
                //Check to see if empty spacer row
                Cell firstcell = row.getCell(1);
                
                if(firstcell.getCellType() == firstcell.CELL_TYPE_BLANK) {
                	//Found a spacer row change the bucket value
                	bucketVal++;
                	
                	//When a spacer row is found need to reset the dspPos variable
                	dspPos = new Integer(0);
                }
                else {
                	//For each row, iterate through all the columns
                    Iterator<Cell> cellIterator = row.cellIterator();
                    boolean required = false;
                    String fieldDesc = "";
                    
                    //Increase the field number by 1
                    fieldNo++;
                    
                    //Increase the display position by 1
                    dspPos++;
                    
                    while (cellIterator.hasNext()) 
                    {
                        Cell cell = cellIterator.next();
                        
                        //Check the cell type and format accordingly
                        switch (cell.getCellType()) 
                        {
                        	case Cell.CELL_TYPE_BOOLEAN:
                        		required = cell.getBooleanCellValue();
                        		break;
                        		
                        	case Cell.CELL_TYPE_STRING:
                            	fieldDesc = cell.getStringCellValue();
                                break;
                        }
                    }
                    
                   //Need to insert all the fields into the message type Form Fields table
                   Query query = sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO configurationFormFields (configId, fieldNo, fieldDesc, fieldLabel, validationType, required, bucketNo, bucketDspPos, useField)" 
                		   +"VALUES (:configId, :fieldNo, :fieldDesc, :fieldLabel, 0, :required, :bucketNo, :bucketDspPos, 1)")
                   		.setParameter("configId", id)
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
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
