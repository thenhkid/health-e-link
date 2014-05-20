package com.ut.dph.reference;
import java.io.BufferedReader;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class fileSystem {

    //Get the operating system
    String os = System.getProperty("os.name").toLowerCase();

    String dir = null;

    String macDirectoryPath = "/Applications/bowlink/";
    String winDirectoryPath = "c:\\bowlink\\";
    String unixDirectoryPath = "/home/bowlink/";

    public String getDir() {
        return dir;
    }

    public void setMessageTypeDir(String folderName) {
        //Windows
        if (os.indexOf("win") >= 0) {
            this.dir = winDirectoryPath + folderName + "\\";
        } //Mac
        else if (os.indexOf("mac") >= 0) {
            this.dir = macDirectoryPath + folderName + "/";
        } //Unix or Linux or Solarix
        else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0 || os.indexOf("sunos") >= 0) {
            this.dir = unixDirectoryPath + folderName + "/";
        }
    }

    public void setMessageTypeCrosswalksDir(String folderName) {
        //Windows
        if (os.indexOf("win") >= 0) {
            this.dir = winDirectoryPath + folderName + "\\crosswalks\\";
        } //Mac
        else if (os.indexOf("mac") >= 0) {
            this.dir = macDirectoryPath + folderName + "/crosswalks/";
        } //Unix or Linux or Solarix
        else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0 || os.indexOf("sunos") >= 0) {
            this.dir = unixDirectoryPath + folderName + "/crosswalks/";
        }
    }

    public void setDir(String orgName, String folderName) {

        //Windows
        if (os.indexOf("win") >= 0) {
            this.dir = winDirectoryPath + orgName + "\\" + folderName + "\\";
        } //Mac
        else if (os.indexOf("mac") >= 0) {
            this.dir = macDirectoryPath + orgName + "/" + folderName + "/";
        } //Unix or Linux or Solarix
        else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0 || os.indexOf("sunos") >= 0) {
            this.dir = unixDirectoryPath + orgName + "/" + folderName + "/";
        }
    }
    
    public void setDirByName(String dirName) {
        //Windows
        if (os.indexOf("win") >= 0) {
            this.dir = winDirectoryPath + dirName;
        } //Mac
        else if (os.indexOf("mac") >= 0) {
            this.dir = macDirectoryPath + dirName;
        } //Unix or Linux or Solarix
        else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0 || os.indexOf("sunos") >= 0) {
            this.dir = unixDirectoryPath + dirName;
        }
    }

    public void deleteOrgDirectories(String orgName) {

        try {
            //Windows
            if (os.indexOf("win") >= 0) {
                //C:/BowLink/
                String dir = winDirectoryPath + orgName;
                File directory = new File(dir);
                delete(directory);
            } //Mac
            else if (os.indexOf("mac") >= 0) {
                String dir = macDirectoryPath + orgName;
                File directory = new File(dir);
                delete(directory);
            } //Unix or Linux or Solarix
            else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0 || os.indexOf("sunos") >= 0) {
                String dir = unixDirectoryPath + orgName;
                File directory = new File(dir);
                delete(directory);
            } else {
                System.out.println("Your OS is not support!!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void createOrgDirectory(String orgName, String dirName) {
         try {
            //Windows
            if (os.indexOf("win") >= 0) {
                //C:/BowLink/
                String dir = winDirectoryPath + orgName + "\\" + dirName.replace("/", "\\");
                File directory = new File(dir);
                if (!directory.exists()) {
                    directory.mkdir();
                }
            } //Mac
            else if (os.indexOf("mac") >= 0) {
                String dir = macDirectoryPath + orgName + "/" + dirName;
                File directory = new File(dir);
                if (!directory.exists()) {
                    directory.mkdir();
                }
            } //Unix or Linux or Solarix
            else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0 || os.indexOf("sunos") >= 0) {
                String dir = unixDirectoryPath + orgName + "/" + dirName;
                File directory = new File(dir);
                if (!directory.exists()) {
                    directory.mkdir();
                }
            } else {
                System.out.println("Your OS is not support!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void creatOrgDirectories(String orgName) {

        try {
            //Windows
            if (os.indexOf("win") >= 0) {
                //C:/BowLink/
                String dir = winDirectoryPath + orgName;
                File directory = new File(dir);
                if (!directory.exists()) {
                    directory.mkdir();
                    new File(winDirectoryPath + orgName + "\\crosswalks").mkdirs();
                    new File(winDirectoryPath + orgName + "\\input files").mkdirs();
                    new File(winDirectoryPath + orgName + "\\output files").mkdirs();
                    new File(winDirectoryPath + orgName + "\\templates").mkdirs();
                    new File(winDirectoryPath + orgName + "\\brochures").mkdirs();
                    new File(winDirectoryPath + orgName + "\\attachments").mkdirs();
                    new File(winDirectoryPath + orgName + "\\certificates").mkdirs();
                }
            } //Mac
            else if (os.indexOf("mac") >= 0) {
                String dir = macDirectoryPath + orgName;
                File directory = new File(dir);
                if (!directory.exists()) {
                    directory.mkdir();
                    new File(macDirectoryPath + orgName + "/crosswalks").mkdirs();
                    new File(macDirectoryPath + orgName + "/input files").mkdirs();
                    new File(macDirectoryPath + orgName + "/output files").mkdirs();
                    new File(macDirectoryPath + orgName + "/templates").mkdirs();
                    new File(macDirectoryPath + orgName + "/brochures").mkdirs();
                    new File(macDirectoryPath + orgName + "/attachments").mkdirs();
                    new File(macDirectoryPath + orgName + "/certificates").mkdirs();
                }
            } //Unix or Linux or Solarix
            else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0 || os.indexOf("sunos") >= 0) {
                String dir = unixDirectoryPath + orgName;
                File directory = new File(dir);
                if (!directory.exists()) {
                    directory.mkdir();
                    new File(unixDirectoryPath + orgName + "/crosswalks").mkdirs();
                    new File(unixDirectoryPath + orgName + "/input files").mkdirs();
                    new File(unixDirectoryPath + orgName + "/output files").mkdirs();
                    new File(unixDirectoryPath + orgName + "/templates").mkdirs();
                    new File(unixDirectoryPath + orgName + "/brochures").mkdirs();
                    new File(unixDirectoryPath + orgName + "/attachments").mkdirs();
                    new File(unixDirectoryPath + orgName + "/certificates").mkdirs();
                }
            } else {
                System.out.println("Your OS is not support!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void delete(File file) throws IOException {

        if (file.isDirectory()) {

            //directory is empty, then delete it
            if (file.list().length == 0) {

                file.delete();

            } else {

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                }
            }

        } else {
            //if file, then delete it
            file.delete();
        }
    }

    /**
     * The checkFileDelimiters function will check to make sure the file contains data separated by the delimiter chosen when uploading the file.
     *
     * @param fileName	The name of the file uploaded
     * @param delim	The delimiter chosen when uploading the file
     *
     * @returns The function will return 0 if no delimiter was found or the count of the delimiter
     */
    public Integer checkFileDelimiter(fileSystem dir, String fileName, String delim) {

        int delimCount = 0;

        FileInputStream fileInput = null;
        try {
            File file = new File(dir.getDir() + fileName);
            fileInput = new FileInputStream(file);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
        
        try {
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    if (delim == "t") {
                        delimCount = line.split("\t", -1).length - 1;
                    } else {
                        delimCount = line.split("\\" + delim, -1).length - 1;
                    }
                    break;
                }
            } catch (IOException ex) {
                Logger.getLogger(fileSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return delimCount;
    }
    
    public boolean isWinMachine() {
    	
    	boolean winMachine = false;
        //Windows
        if (os.indexOf("win") >= 0) {
        	winMachine =  true;
        } 
        return winMachine;
    }
    
    public String setPath(String addOnPath) {
    	String path = "";
    	//Windows
        if (os.indexOf("win") >= 0) {
        	path = winDirectoryPath.replace("\\bowlink\\", "") + addOnPath.replace("", "").replace("/", "\\");  
        } //Mac
        else if (os.indexOf("mac") >= 0) {
        	path = macDirectoryPath.replace("/bowlink/", "") + addOnPath;
        } //Unix or Linux or Solarix
        else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0 || os.indexOf("sunos") >= 0) {
        	path = unixDirectoryPath.replace("/bowlink/", "") + addOnPath;        
        }
        return path;
    }

    
    public Integer checkFileDelimiter(File file, String delim) {

        int delimCount = 0;

        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
        
        try {
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    if (delim == "t") {
                        delimCount = line.split("\t", -1).length - 1;
                    } else {
                        delimCount = line.split("\\" + delim, -1).length - 1;
                    }
                    break;
                }
            } catch (IOException ex) {
                Logger.getLogger(fileSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return delimCount;
    }
    
    public String encodeFileToBase64Binary(File file) throws IOException {
 
		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);
 
		return encodedString;
	}
    
    
    public String decodeFileToBase64Binary(File file) throws IOException {
    	 
		byte[] bytes = loadFile(file);
		byte[] decoded = Base64.decodeBase64(bytes);
		String decodedString = new String(decoded);
 
		return decodedString;
	}
    
    @SuppressWarnings("resource")
	public static byte[] loadFile(File file) throws IOException {
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
	}
    
    public String readTextFile(String fileName) {
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
