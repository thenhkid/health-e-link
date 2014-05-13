package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.User;
import com.ut.dph.model.UserActivity;
import com.ut.dph.model.siteSections;
import com.ut.dph.model.userAccess;

import org.springframework.stereotype.Repository;

@Repository
public interface userDAO {

    Integer createUser(User user);

    void updateUser(User user);

    User getUserById(int userId);

    List<User> getUsersByOrganization(int orgId);

    User getUserByUserName(String username);

    Long findTotalLogins(int orgId);

    void setLastLogin(String username);

    List<siteSections> getSections();

    List<userAccess> getuserSections(int userId);
    
    List<User> getOrganizationContact(int orgId, int mainContact);
    
    Integer getUserByIdentifier(String identifier);
    
    User getUserByResetCode(String resetCode);

    void insertUserLog (UserActivity userActivity);
    
    UserActivity getUAById (Integer uaId);
    
    List<User> getUserByTypeByOrganization(int orgId);
}
