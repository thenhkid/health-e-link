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
import java.io.IOException;
import java.io.PrintStream;
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
public class hl7toTxt {
    
    @Autowired
    private organizationManager organizationmanager;
    
    @Autowired
    private configurationTransportManager configurationtransportmanager;
    
    public String TranslateHl7toTxt(String fileLocation, String fileName, int orgId) throws Exception {
        
        Organization orgDetails = organizationmanager.getOrganizationById(orgId);
        fileSystem dir = new fileSystem();

        dir.setDir(orgDetails.getcleanURL(), "templates");
        
        String templatefileName = orgDetails.getparsingTemplate();
        
        URLClassLoader loader = new URLClassLoader(new URL[]{new URL("file://" + dir.getDir() + templatefileName)});
        
        // Remove the .class extension
        Class cls = loader.loadClass(templatefileName.substring(0, templatefileName.lastIndexOf('.')));
        
        Constructor constructor = cls.getConstructor();
        
        Object HL7Obj = constructor.newInstance();

        Method myMethod = cls.getMethod("HL7toTxt", new Class[]{File.class});
        
        /* Get the uploaded HL7 File */
        fileLocation = fileLocation.replace("/Applications/bowlink/", "").replace("/home/bowlink/","").replace("/bowlink/", "");
        dir.setDirByName(fileLocation);
        
        File hl7File = new File(dir.getDir() + fileName + ".hr");
        
        /* Create the output file */
        String newfileName = new StringBuilder().append(hl7File.getName().substring(0, hl7File.getName().lastIndexOf("."))).append(".").append("txt").toString();
        
        File newFile = new File(dir.getDir() + newfileName);
        
        if(newFile.exists()) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
           
        }
        else {
            newFile.createNewFile();
            newfileName = newFile.getName();
           
        }
        try {
        
	        FileWriter fw = new FileWriter(newFile, true);
	
	        /* END */
	        String fileRecords = (String) myMethod.invoke(HL7Obj, new Object[]{hl7File});
	        
	        fw.write(fileRecords);
	
	        fw.close();
        } catch (Exception ex) {
        	ex.printStackTrace();
        	newfileName = "ERRORERRORERROR";
        	PrintStream ps = new PrintStream(newFile);
        	ex.printStackTrace(ps);
        	ps.close();
        }
        
        return newfileName;
        
    }
    
    
}
