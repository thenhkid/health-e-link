package com.ut.dph.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.dao.userDAO;
import com.ut.dph.model.User;
import com.ut.dph.service.userManager;

@Service
public class userManagerImpl implements userManager{
	
	@Autowired
	private userDAO userDAO;
	
	@Override
	@Transactional
	public Integer createUser(User user) {
		Integer lastId = null;
		lastId = (Integer) userDAO.createUser(user);	
		return lastId;
	}
	  
	@Override
	@Transactional
	public void updateUser(User user) {
		userDAO.updateUser(user);
	}

	@Override
	@Transactional
	public User getUserById(int userId) {
	  return userDAO.getUserById(userId);
	}

	@Override
	@Transactional
	public List<User> findUsers(int orgId, String searchTerm) {
		return userDAO.findUsers(orgId, searchTerm);
	}
	  
	
	@Override
	@Transactional
	public Long findTotalLogins(int userId) {
	  return userDAO.findTotalLogins(userId);
	}
	

}
