package com.ut.dph.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.dao.providerDAO;
import com.ut.dph.model.Provider;
import com.ut.dph.model.providerAddress;
import com.ut.dph.service.providerManager;

@Service
public class providerManagerImpl implements providerManager{
	
	@Autowired
	private providerDAO providerDAO;
	
	@Override
	@Transactional
	public Integer createProvider(Provider provider) {
		Integer lastId = null;
		lastId = (Integer) providerDAO.createProvider(provider);	
		return lastId;
	}
	  
	@Override
	@Transactional
	public void updateProvider(Provider provider) {
		providerDAO.updateProvider(provider);
	}
	
	@Override
	@Transactional
	public void deleteProvider(int providerId) {
		providerDAO.deleteProvider(providerId);
	}

	@Override
	@Transactional
	public Provider getProviderById(int providerId) {
	  return providerDAO.getProviderById(providerId);
	}

	@Override
	@Transactional
	public List<Provider> findProviders(int orgId, String searchTerm) {
		return providerDAO.findProviders(orgId, searchTerm);
	}
	
	@Override
	@Transactional
	public List<providerAddress>  getProviderAddresses(int providerId) {
		return providerDAO.getProviderAddresses(providerId);
	}
	
	@Override
	@Transactional
	public providerAddress getAddressDetails(int addressId) {
		return providerDAO.getAddressDetails(addressId);
	}
	
	@Override
	@Transactional
	public void updateAddress(providerAddress providerAddress) {
		providerDAO.updateAddress(providerAddress);
	}
	
	@Override
	@Transactional
	public void createAddress(providerAddress providerAddress) {
		providerDAO.createAddress(providerAddress);
	}
	
	@Override
	@Transactional
	public void deleteAddress(int addressId) {
		providerDAO.deleteAddress(addressId);
	}
	
}
