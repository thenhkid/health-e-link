package com.ut.healthelink.service;

import java.io.File;
import java.io.IOException;


public interface fileManager {
	
	String encodeFileToBase64Binary(File file) throws IOException;
	String decodeFileToBase64Binary(File file) throws IOException;
	byte[] fileToBytes(File file) throws IOException;
	String readTextFile(String fileName);
	void writeFile(String strFileName, String strFile);
}
