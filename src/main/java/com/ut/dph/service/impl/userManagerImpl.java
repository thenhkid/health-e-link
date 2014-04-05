package com.ut.dph.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.dao.userDAO;
import com.ut.dph.model.User;
import com.ut.dph.service.userManager;
import com.ut.dph.model.UserActivity;
import com.ut.dph.model.siteSections;
import com.ut.dph.model.userAccess;

@Service
public class userManagerImpl implements userManager {

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
    public List<User> getUsersByOrganization(int orgId) {
        return userDAO.getUsersByOrganization(orgId);
    }

    @Override
    @Transactional
    public User getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    @Override
    @Transactional
    public Long findTotalLogins(int userId) {
        return userDAO.findTotalLogins(userId);
    }

    @Override
    @Transactional
    public void setLastLogin(String username) {
        userDAO.setLastLogin(username);
    }

    @Override
    @Transactional
    public List<siteSections> getSections() {
        return userDAO.getSections();
    }

    @Override
    @Transactional
    public List<userAccess> getuserSections(int userId) {
        return userDAO.getuserSections(userId);
    }
    
    @Override
    @Transactional
    public List<User> getOrganizationContact(int orgId, int mainContact){
        return userDAO.getOrganizationContact(orgId, mainContact);
    }
    
    @Override
    @Transactional
    public Integer getUserByIdentifier(String identifier) {
        return userDAO.getUserByIdentifier(identifier);
    }

    @Override
    @Transactional
    public User getUserByResetCode(String resetCode) {
        return userDAO.getUserByResetCode(resetCode);
    }
    
    @Override
    @Transactional
    public void insertUserLog (UserActivity userActivity) {
        userDAO.insertUserLog(userActivity);
    }
}
