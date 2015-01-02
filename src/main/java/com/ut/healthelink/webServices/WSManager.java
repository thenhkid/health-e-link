package com.ut.healthelink.webservices;

import javax.xml.soap.SOAPMessage;
import com.ut.healthelink.model.custom.ToWSSOAP;

public interface WSManager {

	String sendHIESoapMessage(String fileContent, String toEmail, String fromEmail, Integer batchId);
	
	String getXMLValue (String xmlTag, String xml) throws Exception;
	
	SOAPMessage createSOAPRequest(String fileContent, String email, ToWSSOAP toWSOAP) throws Exception;
	
	ToWSSOAP getWSSOAPInfo();
   
}
