package com.ut.dph.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ut.dph.dao.userDAO;
import com.ut.dph.model.User;
import com.ut.dph.model.UserActivity;
import com.ut.dph.model.siteSections;
import com.ut.dph.model.userAccess;


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
        Query query = sessionFactory.getCurrentSession().createSQLQuery("SELECT id, firstName, lastName, userType FROM users where status = 1 and orgId = :orgId order by lastName asc, firstName asc");
              query.setParameter("orgId", orgId);
              
        return query.list();
        
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
     * The 'getOrganizationContact' function will return a user based on the organization id passed in
     * and the mainContact parameter;
     * 
     * @orgId         The id of the organization to search a user on
     * @mainContact   The value of the contact type to return (1 = Primary, 2 = Secondary)
     * 
     * @return The function will return a user object
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<User> getOrganizationContact(int orgId, int mainContact) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User where orgId = :orgId and mainContact = :mainContact and sendEmailAlert = 1");
        query.setParameter("orgId", orgId);
        query.setParameter("mainContact", mainContact);
        
        return query.list();
       
    }
    
    /**
     * The 'getUserByIdentifier' function will try to location a user based on the identifier
     * passed in.
     * 
     * @param identifier The value that will be used to find a user.
     * 
     * @return The function will return a user object
     */
    @Override
    public Integer getUserByIdentifier(String identifier) {
        
        String sql = ("select id from users where lower(email) = '" + identifier + "' or lower(username) = '" + identifier + "' or lower(concat(concat(firstName,' '),lastName)) = '" + identifier + "'");
        
        Query findUser = sessionFactory.getCurrentSession().createSQLQuery(sql);
         
        if(findUser.list().size() > 1) {
            return null;
        }
        else {
            if(findUser.uniqueResult() == null) {
                return null;
            }
            else {
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
        
        if(query.list().size() > 1) {
            return null;
        }
        else {
            if(query.uniqueResult() == null) {
                return null;
            }
            else {
                return (User) query.uniqueResult();
            }
        }
    }
   
    /**
     * The 'insertUserLog' function will take a userActivity and insert the information into the database
     * 
     * @userActivity       An activity of the user
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
}
