/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.dph.service;

import com.ut.dph.model.Organization;
import com.ut.dph.reference.fileSystem;
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
    private organizationManager organiationmanager;
    
    @Autowired
    private configurationTransportManager configurationTransportManager;

    public String TranslateCCDtoTxt(String fileLocation, String ccdFileName, int orgId) throws Exception {

        Organization orgDetails = organiationmanager.getOrganizationById(orgId);
        fileSystem dir = new fileSystem();

        dir.setDir(orgDetails.getcleanURL(), "templates");
        
        String templatefileName = orgDetails.getCCDJarTemplate();
        
        URLClassLoader loader = new URLClassLoader(new URL[]{new URL("file://" + dir.getDir() + templatefileName)});

        // Remove the .class extension
        Class cls = loader.loadClass(templatefileName.substring(0, templatefileName.lastIndexOf('.')));
        Constructor constructor = cls.getConstructor();

        Object CCDObj = constructor.newInstance();

        Method myMethod = cls.getMethod("CCDtoTxt", new Class[]{File.class});

        /* Get the uploaded CCD File */
        fileLocation = fileLocation.replace("/bowlink/", "");
        dir.setDirByName(fileLocation);
        File ccdFile = new File(dir.getDir() + ccdFileName);
        
        /* Create the txt file that will hold the CCD fields */
        String newfileName = new StringBuilder().append(ccdFile.getName().substring(0, ccdFile.getName().lastIndexOf("."))).append(".").append("txt").toString();

        File newFile = new File(dir.getDir() + newfileName);

        if (newFile.exists()) {
            try {

                if (newFile.exists()) {
                    int i = 1;
                    while (newFile.exists()) {
                        int iDot = ccdFileName.lastIndexOf(".");
                        newFile = new File(dir.getDir() + ccdFileName.substring(0, iDot) + "_(" + ++i + ")" + ccdFileName.substring(iDot));
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
