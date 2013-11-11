package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.configurationTransport;

public interface configurationTransportDAO {
	
	configurationTransport getTransportDetails(int configId);
	
	@SuppressWarnings("rawtypes")
	List getTransportMethods();

}
