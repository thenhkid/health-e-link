package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.Brochure;
import com.ut.dph.model.Organization;
import com.ut.dph.model.Provider;
import com.ut.dph.model.User;

public interface organizationDAO {
	
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
	
	List<Provider> getOrganizationProviders(int orgId, int page, int MaxResults);
	  
	Long findTotalProviders(int orgId);
	
	List<Brochure> getOrganizationBrochures(int orgId, int page, int MaxResults);
	  
	Long findTotalBrochures(int orgId);

}
