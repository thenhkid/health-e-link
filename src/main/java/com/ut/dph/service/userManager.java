package com.ut.dph.service;

import java.util.List;


import com.ut.dph.model.User;

public interface userManager {
	
  Integer createUser(User user);
	
  void updateUser(User user);
	  
  User getUserById(int userId);
  
  List<User> findUsers(int orgId, String searchTerm);
   
  Long findTotalLogins(int userId);
  

}

