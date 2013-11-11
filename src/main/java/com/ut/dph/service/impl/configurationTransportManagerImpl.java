package com.ut.dph.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.model.configurationTransport;
import com.ut.dph.dao.configurationTransportDAO;
import com.ut.dph.service.configurationTransportManager;

import org.springframework.stereotype.Service;

@Service
public class configurationTransportManagerImpl implements configurationTransportManager {
	
		@Autowired
		private configurationTransportDAO configurationTransportDAO;
	
	 	@Override
	 	@Transactional
	 	public configurationTransport getTransportDetails(int configId) {
	 		return configurationTransportDAO.getTransportDetails(configId);
	 	}
	 	
	 	@SuppressWarnings("rawtypes")
		public List getTransportMethods() {
	 		return configurationTransportDAO.getTransportMethods();
	 	}

}
