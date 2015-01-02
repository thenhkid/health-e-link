package com.ut.healthelink.dao;

import java.util.List;

import com.ut.healthelink.model.User;
import com.ut.healthelink.model.UserActivity;
import com.ut.healthelink.model.siteSections;
import com.ut.healthelink.model.userAccess;

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
    
    List <User> getSendersForConfig (List <Integer> configIds);
    
    List<User> getOrgUsersForConfig(List <Integer> configIds);

    List<User> getAllUsers();
    
    void updateUserActivity (UserActivity userActivity);
    
    List<String> getUserRoles (User user) throws Exception;
    
    void updateUserOnly(User user) throws Exception;
    
    List<User> getUsersByStatuRolesAndOrg(boolean status, List <Integer> rolesToExclude,  List <Integer> orgs, boolean include) throws Exception;
}
