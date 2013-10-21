package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.messageType;

public interface messageTypeDAO {
	
	Integer createMessageType(messageType messagetype);
	
	void updateMessageType(messageType messagetype);
	
	void deleteMessageType(int messageTypeId);
		  
	messageType getMessageTypeById(int messageTypeId);
	
	List<messageType> getMessageTypes(int page, int maxResults);
	  
	List<messageType> findMessageTypes(String searchTerm);
	
	Long findTotalMessageTypes();

}
