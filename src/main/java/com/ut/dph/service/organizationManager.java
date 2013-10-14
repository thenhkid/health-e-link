package com.ut.dph.service;

import java.util.List;

import com.ut.dph.model.Organization;
import com.ut.dph.model.User;

public interface organizationManager {
	
  Integer createOrganization(Organization organization);
	
  void updateOrganization(Organization organization);
	  
  Organization getOrganizationById(int orgId);
  
  List<Organization> getOrganizationByName(String cleanURL);
  
  List<Organization> findOrganizations(String searchTerm);
  
  List<Organization> getOrganizations(int page, int maxResults);
  
  Long findTotalOrgs();
  
  Long findTotalUsers(int orgId);
  
  Long findTotalConfigurations(int orgId);
  
  List<User> getOrganizationUsers(int orgId, int page, int maxResults);
  
  void deleteOrganization(int orgId);
  
}

