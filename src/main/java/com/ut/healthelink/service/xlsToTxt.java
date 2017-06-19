/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.service;

import com.ut.healthelink.model.Organization;
import com.ut.healthelink.reference.fileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author gchan
 */
@Service
public class xlsToTxt {

    @Autowired
    private organizationManager organizationmanager;
    
    @Autowired
    private configurationTransportManager configurationTransportManager;

    @SuppressWarnings("resource")
	public String TranslateXLStoTxt(Integer orgId, String fileLocation, String excelFileName) throws Exception {

    	Organization orgDetails = organizationmanager.getOrganizationById(orgId);
        
        fileSystem dir = new fileSystem();
        
        dir.setDir(orgDetails.getcleanURL(), "loadFiles");
        /* Get the uploaded xlsx File */
        fileLocation = fileLocation.replace("/Applications/bowlink/", "").replace("/home/bowlink/","").replace("/bowlink/", "");
        dir.setDirByName(fileLocation);
        
        String excelFile = (excelFileName + ".xlsx");
        /* Create the txt file that will hold the excel fields */
        String newfileName = (excelFileName + ".txt");

        File newFile = new File(dir.getDir() + newfileName);
        File inputFile = new File(dir.getDir() + excelFile);
        
        if (newFile.exists()) {
            try {

                if (newFile.exists()) {
                    int i = 1;
                    while (newFile.exists()) {
                        int iDot = newfileName.lastIndexOf(".");
                        newFile = new File(dir.getDir() + newfileName.substring(0, iDot) + "(" + ++i + ")" + newfileName.substring(iDot));
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

        try {
        	System.out.println(new Date());
        	FileWriter fw = new FileWriter(newFile, true);
	        String text = "";
	        InputStream inp = new FileInputStream(inputFile);
		    HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
		    ExcelExtractor extractor = new ExcelExtractor(wb);
		    
		    extractor.setFormulasNotResults(true);
		    extractor.setIncludeSheetNames(false);
		    text = extractor.getText();
		    //need to replace all nulls
			   String text1 = text.replaceAll("null\t", " \t");
			   text = "";
			   String text2 = text1.replaceAll("\tnull", "\t ");
		       text1 = "";
			   if (text2.equalsIgnoreCase("")) {
		        	newfileName = "FILE IS NOT xsl ERROR";
		        }
		        
		        fw.write(text2);
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
