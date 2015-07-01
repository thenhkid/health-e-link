package com.ut.healthelink.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ut.healthelink.model.WSMessagesIn;
import com.ut.healthelink.model.wsMessagesOut;

@Repository
public interface WebServicesDAO {
	
	List<WSMessagesIn> getWSMessagesInList (Date fromDate, Date toDate, Integer fetchSize) throws Exception;
	
	WSMessagesIn getWSMessagesIn (Integer wsId) throws Exception;
  
	void saveWSMessagesOut (wsMessagesOut wsMessagesOut) throws Exception;
	
	List<wsMessagesOut> getWSMessagesOutList (Date fromDate, Date toDate, Integer fetchSize) throws Exception;
	
	wsMessagesOut getWSMessagesOut (Integer wsId) throws Exception;
	
	List<wsMessagesOut> getWSMessagesOutByBatchId (Integer batchId) throws Exception;
	
	List<WSMessagesIn> getWSMessagesInByBatchId (Integer batchId) throws Exception;
	
	void saveWSMessagesIn (WSMessagesIn wsIn) throws Exception;

}

