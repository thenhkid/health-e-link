package com.ut.dph.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.dao.organizationDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.service.organizationManager;
import com.ut.dph.model.User;

@Service
public class organizationManagerImpl implements organizationManager{
	
	@Autowired
	private organizationDAO organizationDAO;
	
	@Override
	@Transactional
	public Integer createOrganization(Organization organization) {
		Integer lastId = null;
		lastId = (Integer) organizationDAO.createOrganization(organization);
		
		//Need to create the directory structure for the new organization
		//Use the cleanURL (name without spaces) for the directory name
		//First get the operating system
		String os = System.getProperty("os.name").toLowerCase();
		
		try {
			//Windows
			if (os.indexOf("win") >= 0) {
				//C:/BowLink/
				String dir = "c:\\bowlink\\" + organization.getcleanURL();
				File directory = new File(dir);
				if (!directory.exists()) {
	                directory.mkdir();
	                new File("c:\\bowlink\\" + organization.getcleanURL() + "\\crosswalks").mkdirs();
	                new File("c:\\bowlink\\" + organization.getcleanURL() + "\\input files").mkdirs();
	                new File("c:\\bowlink\\" + organization.getcleanURL() + "\\output files").mkdirs();
	                new File("c:\\bowlink\\" + organization.getcleanURL() + "\\templates").mkdirs();
	                new File("c:\\bowlink\\" + organization.getcleanURL() + "\\brochures").mkdirs();
	            }
			} 
			//Mac
			else if (os.indexOf("mac") >= 0) {
				String dir = "/Users/chadmccue/bowlink/" + organization.getcleanURL();
				File directory = new File(dir);
				if (!directory.exists()) {
	                directory.mkdir();
	                new File("/Users/chadmccue/bowlink/" + organization.getcleanURL() + "/crosswalks").mkdirs();
	                new File("/Users/chadmccue/bowlink/" + organization.getcleanURL() + "/input files").mkdirs();
	                new File("/Users/chadmccue/bowlink/" + organization.getcleanURL() + "/output files").mkdirs();
	                new File("/Users/chadmccue/bowlink/" + organization.getcleanURL() + "/templates").mkdirs();
	                new File("/Users/chadmccue/bowlink/" + organization.getcleanURL() + "/brochures").mkdirs();
	            }
			} 
			//Unix or Linux or Solarix
			else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0 || os.indexOf("sunos") >= 0) {
				String dir = "/home/bowlink/" + organization.getcleanURL();
				File directory = new File(dir);
				if (!directory.exists()) {
	                directory.mkdir();
	                new File("/home/bowlink/" + organization.getcleanURL() + "/crosswalks").mkdirs();
	                new File("/home/bowlink/" + organization.getcleanURL() + "/input files").mkdirs();
	                new File("/home/bowlink/" + organization.getcleanURL() + "/output files").mkdirs();
	                new File("/home/bowlink/" + organization.getcleanURL() + "/templates").mkdirs();
	                new File("/home/bowlink/" + organization.getcleanURL() + "/brochures").mkdirs();
	            }
			} 
			else {
				System.out.println("Your OS is not support!!");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return lastId;
	}
	  
	@Override
	@Transactional
	public void updateOrganization(Organization organization) {
		organizationDAO.updateOrganization(organization);
	}

	@Override
	@Transactional
	public Organization getOrganizationById(int orgId) {
	  return organizationDAO.getOrganizationById(orgId);
	}
	
	@Override
	@Transactional
	public List<Organization> getOrganizationByName(String cleanURL) {
		return organizationDAO.getOrganizationByName(cleanURL);
	}
	
	@Override
	@Transactional
	public List<Organization> findOrganizations(String searchTerm) {
		return organizationDAO.findOrganizations(searchTerm);
	}
	  
	@Override
	@Transactional
	public List<Organization> getOrganizations(int page, int maxResults) {
	  return organizationDAO.getOrganizations(page, maxResults);
	}
	
	@Override
	@Transactional
	public Long findTotalOrgs() {
	  return organizationDAO.findTotalOrgs();
	}
	
	@Override
	@Transactional
	public Long findTotalUsers(int orgId) {
	  return organizationDAO.findTotalUsers(orgId);
	}
	
	@Override
	@Transactional
	public Long findTotalConfigurations(int orgId) {
	  return organizationDAO.findTotalConfigurations(orgId);
	}
	
	@Override
	@Transactional
	public List<User> getOrganizationUsers(int orgId, int page, int maxResults) {
		return organizationDAO.getOrganizationUsers(orgId, page, maxResults);
	}
	
}
