package com.ut.dph.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ut.dph.dao.messageTypeDAO;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.model.messageType;
import com.ut.dph.reference.fileSystem;

@Service
public class messageTypeManagerImpl implements messageTypeManager {
	
	@Autowired
	private messageTypeDAO messageTypeDAO;
	
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
		   
		   //Set the directory to save the brochures to
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
		   // TODO Auto-generated catch block  
		   e.printStackTrace();  
		}  
	
		//Submit the new brochure to the database
		lastId = (Integer) messageTypeDAO.createMessageType(messageType);	
		
		return lastId;
	}
	
	@Override
	@Transactional
	public void updateMessageType(messageType messageType) {
		
		//Update the brochure
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
		
		//First get the brochure details
		messageType messageType = messageTypeDAO.getMessageTypeById(messageTypeId);
		
		//Next delete the actual attachment
		fileSystem currdir = new fileSystem();
		currdir.setMessageTypeDir("libraryFiles");
		File currFile = new File(currdir.getDir() + messageType.getTemplateFile());
		currFile.delete();
		
		messageTypeDAO.deleteMessageType(messageTypeId);
	}

}
