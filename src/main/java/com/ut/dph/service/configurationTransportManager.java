package com.ut.dph.service;

import java.util.List;

import com.ut.dph.model.configurationTransport;

public interface configurationTransportManager {
	
	configurationTransport getTransportDetails(int configId);
	 
	@SuppressWarnings("rawtypes")
	List getTransportMethods();

}
