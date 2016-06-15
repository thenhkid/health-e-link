package com.ut.healthelink.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.ut.healthelink.dao.userDAO;
import com.ut.healthelink.model.Organization;
import com.ut.healthelink.model.User;
import com.ut.healthelink.model.UserActivity;
import com.ut.healthelink.model.configuration;
import com.ut.healthelink.model.configurationConnection;
import com.ut.healthelink.model.configurationConnectionSenders;
import com.ut.healthelink.model.siteSections;
import com.ut.healthelink.model.userAccess;
import java.util.ArrayList;

/**
 * The userDAOImpl class will implement the DAO access layer to handle updates for organization system users
 *
 *
 * @author chadmccue
 *
 */
@Repository
public class userDAOImpl implements userDAO {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * The 'createUser" function will create the new system user and save the user.
     *
     * @Table	users
     *
     * @param	user	This will hold the user object from the form
     *
     * @return the function will return the id of the new user
     *
     */
    @Override
    public Integer createUser(User user) {
        Integer lastId = null;

        lastId = (Integer) sessionFactory.getCurrentSession().save(user);

        //Need to insert the updated sections
        List<Integer> userSections = user.getsectionList();

        //Loop through the selected sections and save them for the user
        //This will populate the 'rel_userSiteFeatures' table
        for (int i = 0; i < userSections.size(); i++) {
            userAccess newusersections = new userAccess();
            int sectionid = userSections.get(i);
            newusersections.setUserId(lastId);
            newusersections.setFeatureId(sectionid);
            sessionFactory.getCurrentSession().save(newusersections);
        }

        return lastId;
    }

    /**
     * The 'updateUser' function will update the selected user with the changes entered into the form.
     *
     * @param	user	This will hold the user object from the user form
     *
     * @return the function does not return anything
     */
    @Override
    public void updateUser(User user) {
        sessionFactory.getCurrentSession().update(user);

        //Delete the current sections the user has access to
        Query q1 = sessionFactory.getCurrentSession().createQuery("delete from userAccess where userId = :userId");
        q1.setParameter("userId", user.getId());
        q1.executeUpdate();

        //get the list of sections the user will have acess to
        List<Integer> userSections = user.getsectionList();

        //Loop through the selected sections and save them for the user
        for (int i = 0; i < userSections.size(); i++) {
            userAccess newusersections = new userAccess();
            int sectionid = userSections.get(i);
            newusersections.setUserId(user.getId());
            newusersections.setFeatureId(sectionid);
            sessionFactory.getCurrentSession().save(newusersections);
        }

    }

    /**
     * The 'getUserById' function will return a single user object based on the userId passed in.
     *
     * @param	userId	This will be used to find the specifc user
     *
     * @return	The function will return a user object
     */
    @Override
    public User getUserById(int userId) {
        return (User) sessionFactory.getCurrentSession().get(User.class, userId);
    }

    /**
     * The 'getUsersByOrganization' function will return users based on the orgId passed in
     *
     * @param orgId The organization id to find users for
     *
     * @return The function will return a list of user objects
     */
    @Override
    public List<User> getUsersByOrganization(int orgId) {

        List<Integer> OrgIds = new ArrayList<Integer>();
        OrgIds.add(orgId);

        Criteria subOrgQuery = sessionFactory.getCurrentSession().createCriteria(Organization.class);
        subOrgQuery.add(Restrictions.eq("status", true));
        subOrgQuery.add(Restrictions.eq("parentId", orgId));

        List<Organization> subOrgs = subOrgQuery.list();

        if (!subOrgs.isEmpty()) {
            for (Organization org : subOrgs) {
                OrgIds.add(org.getId());
            }
        }

        Criteria users = sessionFactory.getCurrentSession().createCriteria(User.class);
        users.add(Restrictions.eq("status", true));
        users.add(Restrictions.in("orgId", OrgIds));

        List<User> userList = users.list();

        return userList;

    }

    /**
     * The 'getUserByUserName' function will return a single user object based on a username passed in.
     *
     * @param	username	This will used to query the username field of the users table
     *
     * @return	The function will return a user object
     */
    @Override
    public User getUserByUserName(String username) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
        criteria.add(Restrictions.like("username", username));
        return (User) criteria.uniqueResult();
    }

    /**
     * The 'findTotalLogins' function will return the total number of logins for a user.
     *
     * @param	userId	This will be the userid used to find logins
     *
     * @return	The function will return a number of logins
     */
    @Override
    public Long findTotalLogins(int userId) {

        Query query = sessionFactory.getCurrentSession().createQuery("select count(id) as totalLogins from userLogin where userId = :userId");
        query.setParameter("userId", userId);

        Long totalLogins = (Long) query.uniqueResult();

        return totalLogins;

    }

    /**
     * The 'setLastLogin' function will be called upon a successful login. It will save the entry into the rel_userLogins table.
     *
     * @param username	This will be the username of the person logging in.
     *
     */
    @Override
    public void setLastLogin(String username) {
        Query q1 = sessionFactory.getCurrentSession().createQuery("insert into userLogin (userId)" + "select id from User where username = :username");
        q1.setParameter("username", username);
        q1.executeUpdate();

    }

    ;
	
	/**
	 * The 'getSections' function will return the list of all the available sections a user can have
	 * access to on the site.
	 * 
	 * @return		The function will return a list of sections
	 */
	@Override
    @SuppressWarnings("unchecked")
    public List<siteSections> getSections() {
        Query query = sessionFactory.getCurrentSession().createQuery("from siteSections order by featureName asc");
        List<siteSections> sectionList = query.list();
        return sectionList;
    }

    /**
     * The 'getUserSections' function will return a list of sections a specific user has been granted access to.
     *
     * @param	userId	This will hold the userId to query on.
     *
     * @return The function will return a list of sections granted to the user.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<userAccess> getuserSections(int userId) {
        Query query = sessionFactory.getCurrentSession().createQuery("from userAccess where userId = :userId");
        query.setParameter("userId", userId);
        List<userAccess> userSectionList = query.list();
        return userSectionList;
    }
    
    /**
     * 
     * @param userId
     * @return 
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<siteSections> getuserAllowedModules(int userId) {
        String sql = "select * from utsitefeatures where id in (select featureId from rel_usersitefeatures where userId = :userId) order by featureName asc";
        
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(
                    Transformers.aliasToBean(siteSections.class));
        query.setParameter("userId", userId);
        
        List<siteSections> siteSections = query.list();
        return siteSections;
    
    }

    /**
     * The 'getOrganizationContact' function will return a user based on the organization id passed in and the mainContact parameter;
     *
     * @orgId The id of the organization to search a user on
     * @mainContact The value of the contact type to return (1 = Primary, 2 = Secondary)
     *
     * @return The function will return a user object
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<User> getOrganizationContact(int orgId, int mainContact) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User where orgId = :orgId and mainContact = :mainContact");
        query.setParameter("orgId", orgId);
        query.setParameter("mainContact", mainContact);

        return query.list();

    }

    /**
     * The 'getUserByIdentifier' function will try to location a user based on the identifier passed in.
     *
     * @param identifier The value that will be used to find a user.
     *
     * @return The function will return a user object
     */
    @Override
    public Integer getUserByIdentifier(String identifier) {

        String sql = ("select id from users where lower(email) = '" + identifier + "' or lower(username) = '" + identifier + "' or lower(concat(concat(firstName,' '),lastName)) = '" + identifier + "'");

        Query findUser = sessionFactory.getCurrentSession().createSQLQuery(sql);

        if (findUser.list().size() > 1) {
            return null;
        } else {
            if (findUser.uniqueResult() == null) {
                return null;
            } else {
                return (Integer) findUser.uniqueResult();
            }
        }
    }

    /**
     * The 'getUserByResetCode' function will try to location a user based on the a reset code
     *
     * @param resetCode The value that will be used to find a user.
     *
     * @return The function will return a user object
     */
    @Override
    public User getUserByResetCode(String resetCode) {

        Query query = sessionFactory.getCurrentSession().createQuery("from User where resetCode = :resetCode");
        query.setParameter("resetCode", resetCode);

        if (query.list().size() > 1) {
            return null;
        } else {
            if (query.uniqueResult() == null) {
                return null;
            } else {
                return (User) query.uniqueResult();
            }
        }
    }

    /**
     * The 'insertUserLog' function will take a userActivity and insert the information into the database
     *
     * @userActivity An activity of the user
     * @return no return is expected
     */
    @Override
    public void insertUserLog(UserActivity userActivity) {
        try {
            sessionFactory.getCurrentSession().save(userActivity);
        } catch (Exception ex) {
            System.err.println("insertUserLog " + ex.getCause());
            ex.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserActivity getUAById(Integer uaId) {
        try {
            Query query = sessionFactory.getCurrentSession().createSQLQuery("select * from userActivity where id = :uaId").setResultTransformer(Transformers.aliasToBean(UserActivity.class));
            query.setParameter("uaId", uaId);
            List<UserActivity> uaList = query.list();
            if (uaList.size() > 0) {
                return uaList.get(0);
            }
        } catch (Exception ex) {
            System.err.println("getUAById " + ex.getCause());
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getUserByTypeByOrganization(int orgId) {
        try {
            Query query = sessionFactory.getCurrentSession().createQuery("from User where orgId = :orgId and status = 1 order by userType");
            query.setParameter("orgId", orgId);
            List<User> users = query.list();
            return users;
        } catch (Exception ex) {
            System.err.println("getUserByTypeByOrganization " + ex.getCause());
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getSendersForConfig(List<Integer> configIds) {
        try {
            String sql = ("select * from users where status = 1 and id in (select userId from configurationconnectionsenders where connectionId in "
                    + " (select id from configurationconnections "
                    + " where sourceConfigId in ( :configId))) order by userType;");

            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(
                    Transformers.aliasToBean(User.class));
            query.setParameterList("configId", configIds);

            List<User> users = query.list();

            return users;

        } catch (Exception ex) {
            System.err.println("getSendersForConfig  " + ex.getCause());
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getOrgUsersForConfig(List<Integer> configIds) {
        try {
            String sql = ("select * from users where status = 1 and orgId in (select orgId from configurations where id "
                    + " in ( :configId ) "
                    + " and status = 1) order by userType;");

            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(
                    Transformers.aliasToBean(User.class));
            query.setParameterList("configId", configIds);

            List<User> users = query.list();

            return users;

        } catch (Exception ex) {
            System.err.println("getOrgUsersForConfig  " + ex.getCause());
            ex.printStackTrace();
            return null;
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<User> getUserConnectionListSending(Integer configId) {
        try {
            String sql = ("select * from users where status = 1 and Id in (select userId from configurationconnectionsenders where sendEmailAlert = 1 and connectionId "
                    + " in (select id from configurationconnections where sourceConfigId = :configId))");

            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(
                    Transformers.aliasToBean(User.class));
            query.setParameter("configId", configId);

            List<User> users = query.list();

            return users;

        } catch (Exception ex) {
            System.err.println("getOrgUsersForConfig  " + ex.getCause());
            ex.printStackTrace();
            return null;
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<User> getUserConnectionListReceiving(Integer configId) {
        try {
            String sql = ("select * from users where status = 1 and Id in (select userId from configurationconnectionreceivers where sendEmailAlert = 1 and connectionId "
                    + " in (select id from configurationconnections where targetConfigId = :configId))");

            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(
                    Transformers.aliasToBean(User.class));
            query.setParameter("configId", configId);

            List<User> users = query.list();

            return users;

        } catch (Exception ex) {
            System.err.println("getOrgUsersForConfig  " + ex.getCause());
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAllUsers() {
        Query query = sessionFactory.getCurrentSession().createQuery("from User");

        List<User> userList = query.list();
        return userList;
    }

    @Override
    public void updateUserActivity(UserActivity userActivity) {
        try {
            sessionFactory.getCurrentSession().update(userActivity);
        } catch (Exception ex) {
            System.err.println("updateUserActivity  " + ex.getCause());
            ex.printStackTrace();
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public List<String> getUserRoles(User user) {
        try {
            String sql = ("select r.role from users u inner join userRoles r on u.roleId = r.id where u.status = 1 and u.username = :userName");

            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
            query.setParameter("userName", user.getUsername());
            List<String> roles = query.list();

            return roles;

        } catch (Exception ex) {
            System.err.println("getUserRoles  " + ex.getCause());
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public void updateUserOnly(User user) throws Exception {
        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getUsersByStatuRolesAndOrg(boolean status, List<Integer> rolesToExclude, List<Integer> orgs, boolean include) throws Exception {
        String sql = ("select users.*, orgName from users, organizations "
                + " where users.status = :status and users.orgId = organizations.id");

        if (!rolesToExclude.isEmpty()) {
            sql = sql + " and roleId not in (:rolesToExclude)";
        }
        if (!orgs.isEmpty()) {
            sql = sql + " and orgId ";
            if (!include) {
                sql = sql + " not ";
            }
            sql = sql + " in (:orgs)";
        }
        sql = sql + " order by orgName, username";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(
                Transformers.aliasToBean(User.class));
        query.setParameter("status", status);
        if (!rolesToExclude.isEmpty()) {
            query.setParameterList("rolesToExclude", rolesToExclude);
        }
        if (!orgs.isEmpty()) {
            query.setParameterList("orgs", orgs);
        }

        List<User> users = query.list();

        return users;
    }
    
    @Override
    public List<Integer> getUserAllowedTargets(int userId, List<configurationConnectionSenders> connections) throws Exception {
        List<Integer> orgList = new ArrayList<Integer>();
        
        if (connections == null || connections.isEmpty()) {
           orgList.add(0);
        }
        else {
            for (configurationConnectionSenders userConnection : connections) {
                Criteria connection = sessionFactory.getCurrentSession().createCriteria(configurationConnection.class);
                connection.add(Restrictions.eq("id", userConnection.getconnectionId()));

                configurationConnection connectionInfo = (configurationConnection) connection.uniqueResult();

                /* Get the list of target orgs */
                Criteria targetconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                targetconfigurationQuery.add(Restrictions.eq("id", connectionInfo.gettargetConfigId()));
                configuration targetconfigDetails = (configuration) targetconfigurationQuery.uniqueResult();

                /* Add the target org to the target organization list */
                orgList.add(targetconfigDetails.getorgId());
            }
        }
        
        return orgList;
        
    }
  
    @Override
    public List<Integer> getUserAllowedMessageTypes(int userId, List<configurationConnectionSenders> connections) throws Exception {
        List<Integer> messageTypeList = new ArrayList<Integer>();
         
        if (connections == null || connections.isEmpty()) {
           messageTypeList.add(0);
        }
        else {
           for (configurationConnectionSenders userConnection : connections) {
                Criteria connection = sessionFactory.getCurrentSession().createCriteria(configurationConnection.class);
                connection.add(Restrictions.eq("id", userConnection.getconnectionId()));

                configurationConnection connectionInfo = (configurationConnection) connection.uniqueResult();

                /* Get the message type for the configuration */
                Criteria sourceconfigurationQuery = sessionFactory.getCurrentSession().createCriteria(configuration.class);
                sourceconfigurationQuery.add(Restrictions.eq("id", connectionInfo.getsourceConfigId()));
                configuration configDetails = (configuration) sourceconfigurationQuery.uniqueResult();

                /* Add the message type to the message type list */
                messageTypeList.add(configDetails.getMessageTypeId());

            } 
        }
        
        return messageTypeList;
    }
    
    @Override
    public List<configurationConnectionSenders> configurationConnectionSendersByUserId(int userId) {
        /* Get a list of connections the user has access to */
        Criteria connections = sessionFactory.getCurrentSession().createCriteria(configurationConnectionSenders.class);
        connections.add(Restrictions.eq("userId", userId));
        List<configurationConnectionSenders> userConnections = connections.list();
        
        return userConnections;
    }
}
