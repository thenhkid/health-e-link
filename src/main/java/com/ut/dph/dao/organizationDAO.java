package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.Brochure;
import com.ut.dph.model.Organization;
import com.ut.dph.model.Provider;
import com.ut.dph.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface organizationDAO {

    Integer createOrganization(Organization organization);

    void updateOrganization(Organization organization);

    Organization getOrganizationById(int orgId);

    List<Organization> getOrganizationByName(String cleanURL);

    List<Organization> getOrganizations();

    List<Organization> getLatestOrganizations(int maxResults);

    List<Organization> getAllActiveOrganizations();

    Long findTotalOrgs();

    Long findTotalUsers(int orgId);

    Long findTotalConfigurations(int orgId);

    List<User> getOrganizationUsers(int orgId);

    void deleteOrganization(int orgId);

    List<Provider> getOrganizationProviders(int orgId);
    
    List<Provider> getOrganizationActiveProviders(int orgId);

    List<Brochure> getOrganizationBrochures(int orgId);

    List<Organization> getAssociatedOrgs(int orgId);

}
