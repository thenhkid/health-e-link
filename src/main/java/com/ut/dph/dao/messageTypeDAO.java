package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.Crosswalks;
import com.ut.dph.model.messageType;
import com.ut.dph.model.messageTypeDataTranslations;
import com.ut.dph.model.messageTypeFormFields;
import com.ut.dph.model.validationType;

public interface messageTypeDAO {
	
	Integer createMessageType(messageType messagetype);
	
	void updateMessageType(messageType messagetype);
	
	void deleteMessageType(int messageTypeId);
		  
	messageType getMessageTypeById(int messageTypeId);
	
	messageType getMessageTypeByName(String name);
	
	List<messageType> getMessageTypes(int page, int maxResults);
	
	List<messageType> getLatestMessageTypes(int maxResults);
	
	List<messageType> getActiveMessageTypes();
        
        List<messageType> getAvailableMessageTypes(int orgId);
	  
	List<messageType> findMessageTypes(String searchTerm);
	
	Long findTotalMessageTypes();
	
	List<messageTypeFormFields> getMessageTypeFields(int messageTypeId);
	
	void updateMessageTypeFields(messageTypeFormFields formField);
        
        void saveMessageTypeFields(messageTypeFormFields formField);
	
	@SuppressWarnings("rawtypes")
	List getInformationTables();
        
        @SuppressWarnings("rawtypes")
	List getAllTables();
       
	@SuppressWarnings("rawtypes")
	List getTableColumns(String tableName);
	
	@SuppressWarnings("rawtypes")
	List getValidationTypes();
        
        @SuppressWarnings("rawtypes")
	String getValidationById(int id);
	
	@SuppressWarnings("rawtypes")
	List getDelimiters();
	
	Long getTotalFields(int messageTypeId);
	
	List<Crosswalks> getCrosswalks(int page, int maxResults, int orgId);
	
	Integer createCrosswalk(Crosswalks crosswalkDetails);
	
	Long checkCrosswalkName(String name, int orgId);
	
	double findTotalCrosswalks(int orgId);
	
	Crosswalks getCrosswalk(int cwId);
	
	@SuppressWarnings("rawtypes")
	List getCrosswalkData(int cwId);
	
	void saveDataTranslations(messageTypeDataTranslations translations);
	
	List<messageTypeDataTranslations> getMessageTypeTranslations(int messageTypeId);
	
	String getFieldName(int fieldId);
	
	String getCrosswalkName(int cwId);
	
	String getDelimiterChar(int id);
	
	void deleteDataTranslations(int messageTypeId);
	
	List <validationType> getValidationTypes1();

}
