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
import java.io.FileWriter;
import java.io.PrintStream;



import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author gchan
 */
@Service
public class excelToTxt {

    @Autowired
    private organizationManager organizationmanager;
    
    @Autowired
    private configurationTransportManager configurationTransportManager;
    
    @Autowired
    private transactionInManager transactioninmanager;

    @SuppressWarnings({"deprecation"})
	public String TranslateXLSXtoTxt(String fileLocation, String excelFileName, batchUploads batch) throws Exception {

    	Organization orgDetails = organizationmanager.getOrganizationById(batch.getOrgId());
        
        fileSystem dir = new fileSystem();
        
        dir.setDir(orgDetails.getcleanURL(), "loadFiles");
        /* Get the uploaded xlsx File */
        fileLocation = fileLocation.replace("/Applications/bowlink/", "").replace("/home/bowlink/","").replace("/bowlink/", "");
        dir.setDirByName(fileLocation);
        
        String excelFile = (excelFileName + ".xlsx");
        if (batch.getoriginalFileName().endsWith(".xls")) {
        	excelFile = (excelFileName + ".xls");
        }
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
     	    Workbook workbook = WorkbookFactory.create(inputFile);
        	Sheet datatypeSheet = workbook.getSheetAt(0);
            StringBuffer sb = new StringBuffer();
            DataFormatter formatter = new DataFormatter();
            for(Row row : datatypeSheet) {
            	for(int cn=0; cn<row.getLastCellNum(); cn++) {
         		   // If the cell is missing from the file, generate a blank one
         	       // (Works by specifying a MissingCellPolicy)
         	       Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);
         	       String text = formatter.formatCellValue(cell);
         	       sb.append(text + batch.getDelimChar());
         	   }
            	if (row.getRowNum() != datatypeSheet.getLastRowNum()) {
         	   				sb.append(System.getProperty("line.separator"));
           		}
         	}
     	   
             if (sb.toString().equalsIgnoreCase("")) {
     	    	newfileName = "FILE IS NOT xslx ERROR";
     	    }
            String stringRemoveEmptyRows = sb.toString().replaceAll("(?m)^[ \t]*\r?\n", "");
            fw.write(stringRemoveEmptyRows);
        	fw.close();
     	    workbook.close();
     	    
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
