package com.ut.dph.service;

import java.util.List;

import com.ut.healthelink.model.Provider;
import com.ut.healthelink.model.providerAddress;
import com.ut.healthelink.model.providerIdNum;

public interface providerManager {
	
  Integer createProvider(Provider provider);
	
  void updateProvider(Provider provider);
  
  void deleteProvider(int providerId);
	  
  Provider getProviderById(int providerId) throws Exception;
  
  List<Provider> findProviders(int orgId, String searchTerm);
  
  List<providerAddress> getProviderAddresses(int providerId) throws Exception;
  
  providerAddress getAddressDetails(int addressId);
  
  void updateAddress(providerAddress providerAddress);
  
  void createAddress(providerAddress providerAddress);
  
  void deleteAddress(int addressId);
  
  List<providerIdNum> getProviderIds(int providerId);
  
  providerIdNum getIdDetails(int id);
  
  void updateId(providerIdNum providerIdNum);
  
  void createId(providerIdNum providerIdNum);
  
  void deleteId(int id);

}

