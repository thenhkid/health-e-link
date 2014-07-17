package com.ut.healthelink.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import com.ut.dph.service.fileManager;


@Service
public class fileManagerImpl implements fileManager {

    
    public String encodeFileToBase64Binary(File file) throws IOException {
    	try {
			byte[] bytes = fileToBytes(file);
			byte[] encoded = Base64.encodeBase64(bytes);
			String encodedString = new String(encoded);
	 
			return encodedString;
	    } catch (Exception ex) {
			System.err.println("encodeFileToBase64Binary -" + ex.getLocalizedMessage());
			ex.printStackTrace();
			return null;
		}
	}
    
    
    public String decodeFileToBase64Binary(File file) throws IOException {
    	try {
			byte[] bytes = fileToBytes(file);
			byte[] decoded = Base64.decodeBase64(bytes);
			String decodedString = new String(decoded);
			return decodedString;
    	} catch (Exception ex) {
    		System.err.println("decodeFileToBase64Binary -" + ex.getLocalizedMessage());
    		ex.printStackTrace();
    		return null;
    	}
	}
    
    @SuppressWarnings("resource")
	public byte[] fileToBytes(File file) throws IOException {
	    try {
	    	InputStream is = new FileInputStream(file);
	 
		    long length = file.length();
		    byte[] bytes = new byte[(int)length];
		    
		    int offset = 0;
		    int numRead = 0;
		    while (offset < bytes.length
		           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		        offset += numRead;
		    }
	 
		    if (offset < bytes.length) {
		        throw new IOException("Could not completely read file "+file.getName());
		    }
	 
		    is.close();
		    return bytes;
		    
		} catch (Exception ex) {
			System.err.println("decodeFileToBase64Binary -" + ex.getLocalizedMessage());
			ex.printStackTrace();
			return null;
		}
	}
    
    
    @SuppressWarnings("resource")
    public String readTextFile(String fileName) {
    	try {
        String returnValue = "";
        FileReader file = null;
        String line = "";
        try {
	          file = new FileReader(fileName);
	          BufferedReader reader = new BufferedReader(file);
	          while ((line = reader.readLine()) != null) {
	            returnValue += line + "\n";
	          }
	        } catch (FileNotFoundException e) {
	          throw new RuntimeException("File not found");
	        } catch (IOException e) {
	          throw new RuntimeException("IO Error occured");
	        } finally {
	          if (file != null) {
	            try {
	              file.close();
	            } catch (IOException e) {
	              e.printStackTrace();
	            }
	          }
	        }
	        return returnValue;
    	} catch (Exception ex) {
    		System.err.println("decodeFileToBase64Binary -" + ex.getLocalizedMessage());
    		ex.printStackTrace();
    		return null;
    	}
      }

      public void writeFile(String strFileName, String strFile) {
    	  try (BufferedWriter out = new BufferedWriter(new FileWriter(strFileName))) {
             out.write(strFile);
             out.flush();
          } catch (IOException ex) {
        	 System.err.println(ex.getMessage());
             ex.printStackTrace();
          }         
       }
}
