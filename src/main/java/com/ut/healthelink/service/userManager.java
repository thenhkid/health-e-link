package com.ut.healthelink.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import com.ut.healthelink.model.User;
import com.ut.healthelink.model.UserActivity;
import com.ut.healthelink.model.siteSections;
import com.ut.healthelink.model.userAccess;

public interface userManager {
	
  Integer createUser(User user);
	
  void updateUser(User user);
	  
  User getUserById(int userId);
  
  List<User> getUsersByOrganization(int orgId);
  
  User getUserByUserName(String username);
   
  Long findTotalLogins(int userId);
  
  void setLastLogin(String username);
  
  List<siteSections> getSections();
  
  List<userAccess> getuserSections(int userId);
  
  List<User> getOrganizationContact(int orgId, int mainContact);
  
  Integer getUserByIdentifier(String identifier);
  
  User getUserByResetCode(String resetCode);
  
  void insertUserLog (UserActivity userActivity);
  
  List<User> getUserByTypeByOrganization(int orgId);
  
  UserActivity getUAById (Integer uaId);
  
  List <User> getSendersForConfig (List <Integer> configId);
  
  List<User> getOrgUsersForConfig(List <Integer> configId);
  
  List<User> getAllUsers();
  
  void updateUserActivity (UserActivity userActivity);
  
  byte[] generateSalt() throws NoSuchAlgorithmException;
  
  
  byte[] getEncryptedPassword(String password, byte[] salt)
		   throws NoSuchAlgorithmException, InvalidKeySpecException;
  
  boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt)
		   throws NoSuchAlgorithmException, InvalidKeySpecException;
  
  User encryptPW (User user) throws Exception;
  
  List<String> getUserRoles (User user) throws Exception;
  
  void updateUserOnly(User user) throws Exception;
  
  List<User> getUsersByStatuRolesAndOrg(boolean status, List <Integer> rolesToExclude,  List <Integer> orgs, boolean include) throws Exception;

  
}

