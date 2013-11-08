package com.ut.dph.service;

import java.util.List;

import com.ut.dph.model.Crosswalks;
import com.ut.dph.model.messageType;
import com.ut.dph.model.messageTypeDataTranslations;
import com.ut.dph.model.messageTypeFormFields;

public interface messageTypeManager {
	
	Integer createMessageType(messageType messagetype);
	
	void updateMessageType(messageType messagetype);
	
	void deleteMessageType(int messageTypeId);
		  
	messageType getMessageTypeById(int messageTypeId);
	
	messageType getMessageTypeByName(String name);
	
	List<messageType> getMessageTypes(int page, int maxResults);
	
	List<messageType> getLatestMessageTypes(int maxResults);
	  
	List<messageType> findMessageTypes(String searchTerm);
	
	Long findTotalMessageTypes();
	
	List<messageTypeFormFields> getMessageTypeFields(int messageTypeId);
	
	void updateMessageTypeFields(messageTypeFormFields formField);
	
	@SuppressWarnings("rawtypes")
	List getInformationTables();
	
	@SuppressWarnings("rawtypes")
	List getTableColumns(String tableName);
	
	@SuppressWarnings("rawtypes")
	List getValidationTypes();
	
	@SuppressWarnings("rawtypes")
	List getDelimiters();
	
	Long getTotalFields(int messageTypeId);
	
	List<Crosswalks> getCrosswalks(int page, int maxResults);
	
	Integer createCrosswalk(Crosswalks crosswalkDetails);
	
	Long findTotalCrosswalks();
	
	Crosswalks getCrosswalk(int cwId);
	
	@SuppressWarnings("rawtypes")
	List getCrosswalkData(int cwId);
	
	void saveDataTranslations(messageTypeDataTranslations translations);
	
	List<messageTypeDataTranslations> getMessageTypeTranslations(int messageTypeId);
	
	String getFieldName(int fieldId);
	
	String getCrosswalkName(int cwId);
	
	Long checkCrosswalkName(String name);
	
	void deleteDataTranslations(int messageTypeId);

}
