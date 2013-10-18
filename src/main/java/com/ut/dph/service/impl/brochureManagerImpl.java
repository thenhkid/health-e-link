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

import com.ut.dph.dao.brochureDAO;
import com.ut.dph.model.Brochure;
import com.ut.dph.model.Organization;
import com.ut.dph.service.brochureManager;
import com.ut.dph.service.organizationManager;
import com.ut.dph.reference.fileSystem;

@Service
public class brochureManagerImpl implements brochureManager{
	
	@Autowired
	private brochureDAO brochureDAO;
	
	@Autowired
	private organizationManager organizationManager;
	
	@Override
	@Transactional
	public Integer createBrochure(Brochure brochure) {
		Integer lastId = null;
		
		//Need to get the cleanURL of the organization for the brochure
		Organization orgDetails = organizationManager.getOrganizationById(brochure.getOrgId());
		
		MultipartFile file = brochure.getFile(); 
		String fileName = file.getOriginalFilename();
		
		InputStream inputStream = null;  
		OutputStream outputStream = null;  
		
		try {  
		   inputStream = file.getInputStream(); 
		   File newFile = null;
		   
		   //Set the directory to save the brochures to
		   fileSystem dir = new fileSystem();
		   dir.setDir(orgDetails.getcleanURL(), "brochures");
		  
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
		   brochure.setfileName(fileName);
		   
		} 
		catch (IOException e) {  
		   // TODO Auto-generated catch block  
		   e.printStackTrace();  
		}  
	
		//Submit the new brochure to the database
		lastId = (Integer) brochureDAO.createBrochure(brochure);	
		
		return lastId;
	}
	  
	@Override
	@Transactional
	public void updateBrochure(Brochure brochure) {
		
		//Need to get the cleanURL of the organization for the brochure
		Organization orgDetails = organizationManager.getOrganizationById(brochure.getOrgId());
		
		//Check to see if a new file is being uplaoded for the selected brochure
		//If so, upload the new file and remove the previous file from the
		//file system.
		MultipartFile file = brochure.getFile(); 
		
		if(!file.isEmpty()) {
			
			fileSystem currdir = new fileSystem();
			currdir.setDir(orgDetails.getcleanURL(), "brochures");
			File currFile = new File(currdir.getDir() + brochure.getfileName());
			currFile.delete();
			
			String fileName = file.getOriginalFilename();
			
			InputStream inputStream = null;  
			OutputStream outputStream = null;  
			
			try {  
			   inputStream = file.getInputStream(); 
			   File newFile = null;
			   
			   //Set the directory to save the brochures to
			   fileSystem dir = new fileSystem();
			   dir.setDir(orgDetails.getcleanURL(), "brochures");
			  
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
			   brochure.setfileName(fileName);
			   
			} 
			catch (IOException e) {  
			   // TODO Auto-generated catch block  
			   e.printStackTrace();  
			}  
		}
		
		//Update the brochure
		brochureDAO.updateBrochure(brochure);
		
	}
	
	@Override
	@Transactional
	public void deleteBrochure(int brochureId) {
		
		//First get the brochure details
		Brochure brochure = brochureDAO.getBrochureById(brochureId);
		
		//Need to get the cleanURL of the organization for the brochure
		Organization orgDetails = organizationManager.getOrganizationById(brochure.getOrgId());
		
		//Next delete the actual attachment
		fileSystem currdir = new fileSystem();
		currdir.setDir(orgDetails.getcleanURL(), "brochures");
		File currFile = new File(currdir.getDir() + brochure.getfileName());
		currFile.delete();
		
		brochureDAO.deleteBrochure(brochureId);
	}

	@Override
	@Transactional
	public Brochure getBrochureById(int brochureId) {
	  return brochureDAO.getBrochureById(brochureId);
	}

	@Override
	@Transactional
	public List<Brochure> findBrochures(int orgId, String searchTerm) {
		return brochureDAO.findBrochures(orgId, searchTerm);
	}
	
}
