package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.messageType;
import com.ut.dph.model.messageTypeFormFields;

public interface messageTypeDAO {
	
	Integer createMessageType(messageType messagetype);
	
	void updateMessageType(messageType messagetype);
	
	void deleteMessageType(int messageTypeId);
		  
	messageType getMessageTypeById(int messageTypeId);
	
	List<messageType> getMessageTypes(int page, int maxResults);
	  
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

}
