package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.User;

public interface userDAO {
	
	Integer createUser(User user);
	
	void updateUser(User user);
	  
	User getUserById(int userId);
	
	List<User> findUsers(int orgId, String searchTerm);
	
	Long findTotalLogins(int orgId);

}
