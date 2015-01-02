package com.ut.healthelink.webServices.impl;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ut.healthelink.dao.transactionOutDAO;
import com.ut.healthelink.model.custom.ToWSSOAP;
import com.ut.healthelink.service.transactionInManager;
import com.ut.healthelink.service.transactionOutManager;
import com.ut.healthelink.webservices.WSManager;


@Service
public class WSManagerImpl implements WSManager {

	@Autowired
    private transactionInManager transactionInManager;
	
	@Autowired
    private transactionOutDAO transactionOutDAO;

	@Autowired
    private transactionOutManager transactionoutmanager;

	
	@Override
	public String sendHIESoapMessage(String fileContent, String toEmail, String fromEmail, Integer batchId){
		String messageResult = "failed";
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
	        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
	
	        // Send SOAP Message to SOAP Server
	        //end point changes, need to put into config file of some sort

			ToWSSOAP toWSOAP = getWSSOAPInfo();
			if (!fromEmail.equalsIgnoreCase("")) {
				toWSOAP.setFromEmail(fromEmail);
			}
			
	        SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(fileContent, toEmail, toWSOAP), toWSOAP.getEndPoint());
	
	        // print SOAP Response
	        System.out.println("************start of soap response *****************");
	        System.out.println("End point is - " + toWSOAP.getEndPoint());
	        System.out.println("Response SOAP Message:");
	        soapResponse.writeTo(System.out);
        
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        soapResponse.writeTo(os);
	        String responseXML = new String(os.toByteArray());
	        System.out.println("response xml is " + responseXML);
	        
	        try {
	        		messageResult = getXMLValue ("rhap:payload", responseXML);
	        		System.out.println("message result is " + messageResult);
	        } catch(Exception ex) {
	        	transactionOutDAO.updateBatchStatus(batchId, 30);
            	/**insert error**/
            	transactionInManager.insertProcessingError(20, null, batchId, null, null, null, null, false, true, ex.toString());
            	transactionoutmanager.updateTargetTransasctionStatus(batchId, 33);
	        	ex.printStackTrace();
	        }
	        soapConnection.close();
	        
			} catch (Exception ex) {
				transactionOutDAO.updateBatchStatus(batchId, 30);
            	/**insert error**/
            	transactionInManager.insertProcessingError(20, null, batchId, null, null, null, null, false, true, ex.toString());
            	transactionoutmanager.updateTargetTransasctionStatus(batchId, 33);
				ex.printStackTrace();
				
			}
		return messageResult;

	}

	@Override
	public String getXMLValue(String xmlTag, String xml) {
		String xmlValue = "";
		//first we find where the tag is
		
		/****/
		xml = xml.toLowerCase();
		xmlTag = xmlTag.toLowerCase();
		
		String startTag = "<" + xmlTag + ">";
		String endTag = "</" + xmlTag + ">";
		
		int startLoc = xml.indexOf(startTag) + startTag.length();
		int endLoc = xml.indexOf(endTag);
		try {
			xmlValue = xml.substring(startLoc, endLoc);
		} catch (Exception e)	{				
			System.out.println(e.getMessage() );
			System.out.println("Utilities:getXMLValue - xmlTag is " + xmlTag + " error is:" + e);
		}
		return xmlValue;
	
	}

	@Override
	public SOAPMessage createSOAPRequest(String fileContent, String toEmail, ToWSSOAP toWSOAP) {
		try {
			
			MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
	    	SOAPMessage soapMessage = messageFactory.createMessage();
	        SOAPPart soapPart = soapMessage.getSOAPPart();
	        String rhapURI = "http://www.orionhealth.com/rhapsodyconnect";
	        String iheURI = "http://www.orionhealth.com/2011/08/ihe";
	        
	
	        // SOAP Envelope
	        SOAPEnvelope envelope = soapPart.getEnvelope();
	        envelope.addNamespaceDeclaration("ihe", iheURI);
	        envelope.addNamespaceDeclaration("rhap", rhapURI);
	        
        
	        // SOAP Body
	        SOAPBody soapBody = envelope.getBody();
	        SOAPElement soapBodyElem = soapBody.addChildElement("SendDocuments", "rhap");
	        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("From", "rhap");
	        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("To", "rhap");
	        soapBodyElem1.addTextNode(toWSOAP.getFromEmail());
	        soapBodyElem2.addTextNode(toEmail);
	        
	        SOAPElement soapBodyElemIHE = soapBodyElem.addChildElement("ProvideAndRegisterDocumentSet", "ihe");
	        SOAPElement soapBodyElemIHEDocument = soapBodyElemIHE.addChildElement("Document", "ihe");
	        soapBodyElemIHEDocument.addChildElement("ClassCode", "ihe");
	        soapBodyElemIHEDocument.addChildElement("ConfidentialityCode", "ihe");
	        soapBodyElemIHEDocument.addChildElement("FormatCode", "ihe");
	        soapBodyElemIHEDocument.addChildElement("HealthcareFacilityTypeCode", "ihe");
	        soapBodyElemIHEDocument.addChildElement("PracticeSettingCode", "ihe");
	        soapBodyElemIHEDocument.addChildElement("TypeCode", "ihe");
	        SOAPElement soapBodyElemIHEChild8 = soapBodyElemIHEDocument.addChildElement("DocumentContents", "ihe");
	        soapBodyElemIHEChild8.addTextNode(fileContent);
	      
	        soapMessage.saveChanges();
	
	        /* Print the request message */
	        System.out.println("***************Request SOAP Message:*****************");
	        soapMessage.writeTo(System.out);
	        System.out.println();
	        
	        return soapMessage;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
        
	}

	@Override
	public ToWSSOAP getWSSOAPInfo() {
		String hostname = null;
		ToWSSOAP toWSOAP = new ToWSSOAP();
		//default to test for now
		toWSOAP.setEndPoint("http://localhost:8080");
		toWSOAP.setFromEmail("e-Referral@state.ma.us");
		try {
            hostname = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
        	System.out.print( "Error - " + e + " " +
                    e.getStackTrace()[1].getClassName() + 
                    "." + e.getStackTrace()[1].getMethodName() +  " "  +  e.getStackTrace());
        }
        
        if("10.202.52.54".equals(hostname)) {
        	toWSOAP.setEndPoint("http://orion-mao-testrhapgw11v:9013/services/DGL_eReferral_Interface.DGL_eReferral_InterfaceHttpSoap12Endpoint");
        	toWSOAP.setFromEmail("ereferral@direct.ereferral.test.masshiwaystage.com");
        } else if("10.64.61.129".equals(hostname)) {
        	toWSOAP.setEndPoint("http://orion-mao-devrhapgw11v:9013/services/DGL_eReferral_Interface.DGL_eReferral_InterfaceHttpSoap12Endpoint");
        	toWSOAP.setFromEmail("ereferral@direct.ereferral.dev.masshiwaystage.com");
        } else if("10.202.52.152".equals(hostname)) {
        	toWSOAP.setEndPoint("http://orion-mao-prodrhapgw11v:9013/services/DGL_eReferral_Interface.DGL_eReferral_InterfaceHttpSoap12Endpoint"); 
        	toWSOAP.setFromEmail("ereferral@direct.ereferral.masshiway.net");
        } else if("10.202.52.22".equals(hostname)) {
        	toWSOAP.setEndPoint("http://orion-mao-prtstrhapgw11v:9013/services/DGL_eReferral_Interface.DGL_eReferral_InterfaceHttpSoap12Endpoint"); 
        	toWSOAP.setFromEmail("ereferral@direct.ereferral.prtst.masshiwaystage.com");
        }
		
		return toWSOAP;
	}

   
}
