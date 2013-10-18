package com.ut.dph.reference;

import java.io.File;

public class fileSystem {
	
	//Get the operating system
	String os = System.getProperty("os.name").toLowerCase();
	
	String dir = null;
	
	public String getDir() {
		return dir;
	}
	
	public void setDir(String orgName, String folderName) {
	
	   //Windows
	   if (os.indexOf("win") >= 0) {
		  this.dir = "c:\\bowlink\\" + orgName + "\\" + folderName + "\\";
	   }
	   //Mac
	   else if (os.indexOf("mac") >= 0) {
			this.dir = "/Users/chadmccue/bowlink/" + orgName + "/" + folderName + "/";
	   }
	   //Unix or Linux or Solarix
	   else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0 || os.indexOf("sunos") >= 0) {
		  this.dir = "/home/bowlink/" + orgName + "/" + folderName + "/";
	   }
		
	}
	
	public void creatOrgDirectories(String orgName) {
		
		try {
			//Windows
			if (os.indexOf("win") >= 0) {
				//C:/BowLink/
				String dir = "c:\\bowlink\\" +orgName;
				File directory = new File(dir);
				if (!directory.exists()) {
	                directory.mkdir();
	                new File("c:\\bowlink\\" + orgName + "\\crosswalks").mkdirs();
	                new File("c:\\bowlink\\" + orgName + "\\input files").mkdirs();
	                new File("c:\\bowlink\\" + orgName + "\\output files").mkdirs();
	                new File("c:\\bowlink\\" + orgName + "\\templates").mkdirs();
	                new File("c:\\bowlink\\" + orgName + "\\brochures").mkdirs();
	            }
			} 
			//Mac
			else if (os.indexOf("mac") >= 0) {
				String dir = "/Users/chadmccue/bowlink/" + orgName;
				File directory = new File(dir);
				if (!directory.exists()) {
	                directory.mkdir();
	                new File("/Users/chadmccue/bowlink/" + orgName + "/crosswalks").mkdirs();
	                new File("/Users/chadmccue/bowlink/" + orgName + "/input files").mkdirs();
	                new File("/Users/chadmccue/bowlink/" + orgName + "/output files").mkdirs();
	                new File("/Users/chadmccue/bowlink/" + orgName + "/templates").mkdirs();
	                new File("/Users/chadmccue/bowlink/" + orgName + "/brochures").mkdirs();
	            }
			} 
			//Unix or Linux or Solarix
			else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0 || os.indexOf("sunos") >= 0) {
				String dir = "/home/bowlink/" + orgName;
				File directory = new File(dir);
				if (!directory.exists()) {
	                directory.mkdir();
	                new File("/home/bowlink/" + orgName + "/crosswalks").mkdirs();
	                new File("/home/bowlink/" + orgName + "/input files").mkdirs();
	                new File("/home/bowlink/" + orgName + "/output files").mkdirs();
	                new File("/home/bowlink/" + orgName + "/templates").mkdirs();
	                new File("/home/bowlink/" + orgName + "/brochures").mkdirs();
	            }
			} 
			else {
				System.out.println("Your OS is not support!!");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
