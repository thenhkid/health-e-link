package com.ut.dph.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.dao.organizationDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.model.Provider;
import com.ut.dph.service.organizationManager;
import com.ut.dph.model.User;
import com.ut.dph.model.Brochure;
import com.ut.dph.reference.fileSystem;

@Service
public class organizationManagerImpl implements organizationManager {

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
        fileSystem dir = new fileSystem();

        dir.creatOrgDirectories(organization.getcleanURL());

        return lastId;
    }

    @Override
    @Transactional
    public void updateOrganization(Organization organization) {
        organizationDAO.updateOrganization(organization);

	//Need to make sure all folders are created for
        //the organization
        fileSystem dir = new fileSystem();

        dir.creatOrgDirectories(organization.getcleanURL());
    }

    @Override
    @Transactional
    public void deleteOrganization(int orgId) {
        organizationDAO.deleteOrganization(orgId);
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
    public List<Organization> getOrganizations() {
        return organizationDAO.getOrganizations();
    }

    @Override
    @Transactional
    public List<Organization> getLatestOrganizations(int maxResults) {
        return organizationDAO.getLatestOrganizations(maxResults);
    }

    @Override
    @Transactional
    public List<Organization> getAllActiveOrganizations() {
        return organizationDAO.getAllActiveOrganizations();
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
    public List<User> getOrganizationUsers(int orgId) {
        return organizationDAO.getOrganizationUsers(orgId);
    }

    @Override
    @Transactional
    public List<Provider> getOrganizationProviders(int orgId) {
        return organizationDAO.getOrganizationProviders(orgId);
    }

    @Override
    @Transactional
    public List<Brochure> getOrganizationBrochures(int orgId) {
        return organizationDAO.getOrganizationBrochures(orgId);
    }

    @Override
    @Transactional
    public List<Organization> getAssociatedOrgs(int orgId) {
        return organizationDAO.getAssociatedOrgs(orgId);
    }
    
    @Override
    @Transactional
    public List<Provider> getOrganizationActiveProviders(int orgId) {
        return organizationDAO.getOrganizationActiveProviders(orgId);
    }

}
