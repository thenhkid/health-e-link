/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.service;

import com.ut.healthelink.model.Organization;
import com.ut.healthelink.model.batchUploads;
import com.ut.healthelink.reference.fileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author gchan
 */
@Service
public class xlsxToTxt {

    @Autowired
    private organizationManager organizationmanager;
    
    @Autowired
    private configurationTransportManager configurationTransportManager;
    
    @Autowired
    private transactionInManager transactioninmanager;

    @SuppressWarnings("resource")
	public String TranslateXLSXtoTxt(String fileLocation, String excelFileName, batchUploads batch) throws Exception {

    	Organization orgDetails = organizationmanager.getOrganizationById(batch.getOrgId());
        
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

	        FileWriter fw = new FileWriter(newFile, true);
	        String text = "";
	        InputStream inp = new FileInputStream(inputFile);

	       XSSFWorkbook document = new XSSFWorkbook(inp);
	       XSSFExcelExtractor extractor = new XSSFExcelExtractor(document);
	       extractor.setFormulasNotResults(true);
		   extractor.setIncludeSheetNames(false);
		   text = extractor.getText();
		   //need to replace all nulls
		   String text1 = text.replaceAll("null\t", " \t");
		   text = "";
		   String text2 = text1.replaceAll("\tnull", "\t ");
	       text1 = "";
		   if (text2.equalsIgnoreCase("")) {
	        	newfileName = "FILE IS NOT xslx ERROR";
	        }
	        
	        fw.write(text2);
	        fw.close();

	        
        } catch (Exception ex) {
        	ex.printStackTrace();
        	newfileName = "ERRORERRORERROR";
        	PrintStream ps = new PrintStream(newFile);
        	ex.printStackTrace(ps);
        	ps.close();
        	transactioninmanager.insertProcessingError(5, null, batch.getId(), null, null, null, null,
                    false, false, ex.getStackTrace().toString());
        }
        return newfileName;

    }

}
