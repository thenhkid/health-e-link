package com.ut.dph.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.dao.organizationDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.service.organizationManager;

@Service
public class organizationManagerImpl implements organizationManager{
	
	@Autowired
	private organizationDAO organizationDAO;
	
	@Override
	@Transactional
	public Integer createOrganization(Organization organization) {
		Integer lastId = null;
		lastId = (Integer) organizationDAO.createOrganization(organization);	
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
	public List<Organization> getOrganizations(int firstResults, int maxResults) {
	  return organizationDAO.getOrganizations(firstResults, maxResults);
	}
	
	@Override
	@Transactional
	public Integer findTotalUsers(int orgId) {
	  return organizationDAO.findTotalUsers(orgId);
	}

}
