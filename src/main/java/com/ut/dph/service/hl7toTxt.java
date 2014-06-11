/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service;

import com.ut.dph.reference.fileSystem;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.stereotype.Service;

/**
 *
 * @author chadmccue
 */
@Service
public class hl7toTxt {
    
    public String TranslateHl7toTxt(String fileLocation, String fileName) throws Exception {
        
        FileInputStream fileInput = null;
        
        fileSystem dir = new fileSystem();
        fileLocation = fileLocation.replace("/Applications/bowlink/", "").replace("/home/bowlink/","").replace("/bowlink/", "");
        dir.setDirByName(fileLocation);
        
        File origfile = null;
        
        try {
            origfile = new File(dir.getDir() + fileName+".hr");
            fileInput = new FileInputStream(origfile);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        /* Create the output file */
        String newfileName = new StringBuilder().append(origfile.getName().substring(0, origfile.getName().lastIndexOf("."))).append(".").append("txt").toString();
        
        File newFile = new File(dir.getDir() + newfileName);
        
        if(newFile.exists()) {
             try {

                if (newFile.exists()) {
                    int i = 1;
                    while (newFile.exists()) {
                        int iDot = fileName.lastIndexOf(".");
                        newFile = new File(dir.getDir() + fileName.substring(0, iDot) + "_(" + ++i + ")" + fileName.substring(iDot));
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
        
        /* Parse through the original HL7 message to create the content for the new txt file */
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
        String line;
        int counter = 0;
        String recordRow = "";
        FileWriter fw = new FileWriter(newFile, true);
        
        while ((line = br.readLine()) != null) {
            
            String[] lineItemsArray = line.split("\\|",-1);
            
            if("MSH".equals(lineItemsArray[0])) {
                counter++;
                //Create a new line
                if(counter > 1) {
                    recordRow = new StringBuilder().append(recordRow).append(System.getProperty("line.separator")).toString();
                    fw.write(recordRow); 
                    recordRow = "";
                }
            } 
            else {
                recordRow = new StringBuilder().append(recordRow).append("|").toString();
            }
            if(!"MSH".equals(lineItemsArray[0])) {
                recordRow = new StringBuilder().append(recordRow).append(line.replace("^","|")).toString();
            }
            else {
                recordRow = new StringBuilder().append(recordRow).append(line).toString();  
            }
            
        }
        fw.write(recordRow);  
        fw.close();
        
        return newfileName;
        
    }
    
    
}
