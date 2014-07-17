/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.service;

import com.ut.healthelink.model.Organization;
import com.ut.healthelink.reference.fileSystem;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author chadmccue
 */
@Service
public class CCDtoTxt {

    @Autowired
    private organizationManager organizationmanager;
    
    @Autowired
    private configurationTransportManager configurationTransportManager;

    public String TranslateCCDtoTxt(String fileLocation, String ccdFileName, int orgId) throws Exception {

        Organization orgDetails = organizationmanager.getOrganizationById(orgId);
        fileSystem dir = new fileSystem();

        dir.setDir(orgDetails.getcleanURL(), "templates");
        
        String templatefileName = orgDetails.getparsingTemplate();
        
        URLClassLoader loader = new URLClassLoader(new URL[]{new URL("file://" + dir.getDir() + templatefileName)});

        // Remove the .class extension
        Class cls = loader.loadClass(templatefileName.substring(0, templatefileName.lastIndexOf('.')));
        Constructor constructor = cls.getConstructor();

        Object CCDObj = constructor.newInstance();

        Method myMethod = cls.getMethod("CCDtoTxt", new Class[]{File.class});

        /* Get the uploaded CCD File */
        fileLocation = fileLocation.replace("/Applications/bowlink/", "").replace("/home/bowlink/","").replace("/bowlink/", "");
        dir.setDirByName(fileLocation);
        
        File ccdFile = new File(dir.getDir() + ccdFileName + ".xml");
        
        /* Create the txt file that will hold the CCD fields */
        String newfileName = new StringBuilder().append(ccdFile.getName().substring(0, ccdFile.getName().lastIndexOf("."))).append(".").append("txt").toString();

        File newFile = new File(dir.getDir() + newfileName);
        
        if (newFile.exists()) {
            try {

                if (newFile.exists()) {
                    int i = 1;
                    while (newFile.exists()) {
                        int iDot = newfileName.lastIndexOf(".");
                        newFile = new File(dir.getDir() + newfileName.substring(0, iDot) + "_(" + ++i + ")" + newfileName.substring(iDot));
                    }
                    newfileName = newFile.getName();
                    newFile.createNewFile();
                } else {
                    newFile.createNewFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            newFile.createNewFile();
            newfileName = newFile.getName();

        }

        FileWriter fw = new FileWriter(newFile, true);

        /* END */
        String fileRecords = (String) myMethod.invoke(CCDObj, new Object[]{ccdFile});
        
        fw.write(fileRecords);

        fw.close();

        return newfileName;

    }

}
