package com.ut.healthelink.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.healthelink.dao.organizationDAO;
import com.ut.healthelink.model.Organization;
import com.ut.healthelink.model.Provider;
import com.ut.healthelink.service.organizationManager;
import com.ut.healthelink.model.User;
import com.ut.healthelink.model.Brochure;
import com.ut.healthelink.reference.fileSystem;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ListIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

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
    public void updateOrganization(Organization organization) throws Exception {
       
	//Need to make sure all folders are created for
        //the organization
        fileSystem dir = new fileSystem();
        
        dir.creatOrgDirectories(organization.getcleanURL());
        
        MultipartFile file = organization.getFile();
        //If a file is uploaded
        if (file != null && !file.isEmpty()) {
        
            String fileName = file.getOriginalFilename();
            
            InputStream inputStream = null;
            OutputStream outputStream = null;
            
            try {
                inputStream = file.getInputStream();
                File newFile = null;

                //Set the directory to save the uploaded message type template to
                fileSystem orgdir = new fileSystem();

                orgdir.setDir(organization.getcleanURL(), "templates");

                newFile = new File(orgdir.getDir() + fileName);

                if (newFile.exists()) {
                    newFile.delete();
                }
                newFile.createNewFile();
                
                outputStream = new FileOutputStream(newFile);
                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                outputStream.close();

                //Set the filename to the file name
                organization.setparsingTemplate(fileName);

            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception (e);
            }
            
        }
        
        organizationDAO.updateOrganization(organization);
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
    
    @Override
    @Transactional
    public Integer getTotalPartners() {
        List partners = organizationDAO.getPartnerEntriesForMap();
        
        Integer totalPartners = 0;
        
        if(partners.size() > 0) {
            for(ListIterator iter = partners.listIterator(); iter.hasNext(); ) {
                
                Object[] row = (Object[]) iter.next();
                
                if(row[9] != null) {
                    totalPartners+=1;
                }
            }
        }
        
        return totalPartners;
    }
    
    @Override
    @Transactional
    public JSONObject getPartnerEntriesForMap() {
        
        List partners = organizationDAO.getPartnerEntriesForMap();
        
        JSONObject mapObject = new JSONObject();
        
        JSONArray partnerMapArray = new JSONArray();
        
        if(partners.size() > 0) {
            for(ListIterator iter = partners.listIterator(); iter.hasNext(); ) {
                
                Object[] row = (Object[]) iter.next();
                
                if(row[9] != null) {
                    
                    /* Get any Brochures */
                    List<Brochure> orgBrochures = organizationDAO.getOrganizationBrochures(Integer.valueOf(String.valueOf(row[0])));
                
                    JSONObject mapItemObject = new JSONObject();

                    mapItemObject.put("latitude", String.valueOf(row[9]));
                    mapItemObject.put("longitude", String.valueOf(row[8]));

                    if(Integer.valueOf(String.valueOf(row[10])) == 1) {
                        mapItemObject.put("icon", "/dspResources/img/front-end/location-pin-provider.png");
                    }
                    else {
                        mapItemObject.put("icon", "/dspResources/img/front-end/location-pin-cbo.png");
                    }
                    
                    if(orgBrochures != null && orgBrochures.size() > 0) {
                        String brochureTitle = orgBrochures.get(0).getTitle();
                        String brochureURL = "<a href=\"/FileDownload/downloadFile.do?filename="+orgBrochures.get(0).getfileName()+"&foldername=brochures&orgId="+Integer.valueOf(String.valueOf(row[0]))+"\">"+brochureTitle+"</a>";
                        mapItemObject.put("baloon_text", "<div style=\"width:250px;\"> <strong>"+row[1]+"</strong><br />"+row[2]+"&nbsp;"+row[3]+"<br />"+row[4]+"&nbsp;"+row[5]+",&nbsp;"+row[6]+"<br />Phone:&nbsp;"+row[7]+"<br />Brochure:&nbsp"+brochureURL+"</div>");
                    }
                    else {
                       mapItemObject.put("baloon_text", "<div style=\"width:250px;\"> <strong>"+row[1]+"</strong><br />"+row[2]+"&nbsp;"+row[3]+"<br />"+row[4]+"&nbsp;"+row[5]+",&nbsp;"+row[6]+"<br />Phone:&nbsp;"+row[7]+"</div>");
                    }

                    partnerMapArray.add(mapItemObject);
                }
            }
            
        }
        
        mapObject.put("markers", partnerMapArray);
        
        return mapObject;
        
    }
}
