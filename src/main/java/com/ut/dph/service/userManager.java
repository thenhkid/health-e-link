package com.ut.dph.service;

import java.util.List;

import com.ut.dph.model.User;
import com.ut.dph.model.siteSections;
import com.ut.dph.model.userAccess;

public interface userManager {
	
  Integer createUser(User user);
	
  void updateUser(User user);
	  
  User getUserById(int userId);
  
  List<User> findUsers(int orgId, String searchTerm);
  
  List<User> getUsersByOrganization(int orgId);
  
  User getUserByUserName(String username);
   
  Long findTotalLogins(int userId);
  
  void setLastLogin(String username);
  
  List<siteSections> getSections();
  
  List<userAccess> getuserSections(int userId);
  
  List<User> getOrganizationContact(int orgId, int mainContact);
  

}

