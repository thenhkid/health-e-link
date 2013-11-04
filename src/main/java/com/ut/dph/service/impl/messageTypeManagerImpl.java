package com.ut.dph.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.ut.dph.dao.messageTypeDAO;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.model.messageType;
import com.ut.dph.model.messageTypeFormFields;
import com.ut.dph.reference.fileSystem;

@Service
public class messageTypeManagerImpl implements messageTypeManager {
	
	@Autowired
	private messageTypeDAO messageTypeDAO;
	
	@Autowired
	private SessionFactory sessionFactory;
	
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
		   messageType.setTemplateFile(fileName);
		   
		} 
		catch (IOException e) {  
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
	public List<messageType> getMessageTypes(int page, int maxResults) {
		return messageTypeDAO.getMessageTypes(page, maxResults);
	}
	
	@Override
	@Transactional
	public messageType getMessageTypeById(int messageTypeId) {
	  return messageTypeDAO.getMessageTypeById(messageTypeId);
	}
	
	@Override
	@Transactional
	public List<messageType> findMessageTypes(String searchTerm) {
		return messageTypeDAO.findMessageTypes(searchTerm);
	}
	
	@Override
	@Transactional
	public Long findTotalMessageTypes() {
	  return messageTypeDAO.findTotalMessageTypes();
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
	public List getTableColumns(String tableName) {
		return messageTypeDAO.getTableColumns(tableName);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public List getValidationTypes() {
		return messageTypeDAO.getValidationTypes();
	}
	
	
	/**
	 * The 'loadExcelContents' will take the contents of the uploaded excel template file and populate
	 * the corresponding message type form fields table. This function will split up the contents into
	 * the appropriate buckets. Buckets (1 - 4) will be separated by spacer rows with in the excel file.
	 * 
	 * @param id 			id: value of the latest added message type
	 * @param fileName		fileName: file name of the uploaded excel file.
	 * 
	 */
	public void loadExcelContents(int id, String fileName) {
		
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
			dir.setMessageTypeDir("libraryFiles");
			
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
                   Query query = sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO messageTypeFormFields (messageTypeId, fieldNo, fieldDesc, fieldLabel, validationType, required, bucketNo, bucketDspPos)" 
                		   +"VALUES (:messageTypeId, :fieldNo, :fieldDesc, :fieldLabel, 0, :required, :bucketNo, :bucketDspPos)")
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
			
			
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		 
	
	}

}
