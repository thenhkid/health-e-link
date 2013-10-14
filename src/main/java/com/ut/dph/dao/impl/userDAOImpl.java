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
import com.ut.dph.model.siteSections;
import com.ut.dph.model.userAccess;

/**
 * The userDAOImpl class will implement the DAO access layer to handle
 * updates for organization system users
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
	 * The 'createUser" function will create the new system user and save
	 * the user.
	 * 
	 * @Table	users
	 * 
	 * @param	user	This will hold the user object from the form
	 * 
	 * @return 			the function will return the id of the new user
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
		for(int i = 0;i<userSections.size();i++) {
			userAccess newusersections = new userAccess();
			int sectionid = userSections.get(i);
			newusersections.setUserId(lastId);
			newusersections.setFeatureId(sectionid);
			sessionFactory.getCurrentSession().save(newusersections);
		}
		
		return lastId;
	}
	
	
	/**
	 * The 'updateUser' function will update the selected user with the changes
	 * entered into the form.
	 * 
	 * @param	user	This will hold the user object from the user form
	 * 
	 * @return 			the function does not return anything
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
		for(int i = 0;i<userSections.size();i++) {
			userAccess newusersections = new userAccess();
			int sectionid = userSections.get(i);
			newusersections.setUserId(user.getId());
			newusersections.setFeatureId(sectionid);
			sessionFactory.getCurrentSession().save(newusersections);
		}
		
	}
	
	/**
	 * The 'getUserById' function will return a single user object based on the userId
	 * passed in.
	 * 
	 * @param	userId	This will be used to find the specifc user
	 * 
	 * @return			The function will return a user object 
	 */
	@Override
	public User getUserById(int userId) {
      return (User) sessionFactory.getCurrentSession().get(User.class, userId);
	}
	
	
	/**
	 * The 'getUserByUserName' function will return a single user object based on a username
	 * passed in.
	 * 
	 * @param	username	This will used to query the username field of the users table
	 * 
	 * @return				The function will return a user object
	 */
	@Override
	public User getUserByUserName(String username) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);  
	    criteria.add(Restrictions.like("username", username));  
	    return (User) criteria.uniqueResult();   
	}
	
	/**
	 * The 'findUsers' function will return a list of user objects based on a specific 
	 * organization Id and a search term. The search will look for users for a spcicif
	 * organization and whose first name or last name match the search term provided.
	 * 
	 * @param	orgId		This will be the orgId used to find users
	 * 			searchTerm	This will be used to query the firstname field and lastname field
	 * 
	 * @return		The function will return a list of user objects
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> findUsers(int orgId, String searchTerm) {
	     Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class)
	    	.add(Restrictions.eq("orgId",orgId))
	    	.add(Restrictions.or(
	    			Restrictions.like("firstName", searchTerm+"%"),
	    			Restrictions.like("lastName", searchTerm+"%")
	     		)
	     	);
	     
	     return criteria.list();  
	}
	
	/**
	 * The 'findTotalLogins' function will return the total number of logins for a user.
	 * 
	 * @param	userId	This will be the userid used to find logins
	 * 
	 * @return			The function will return a number of logins
	 */
	@Override
	public Long findTotalLogins(int userId) {
		
		Query query = sessionFactory.getCurrentSession().createQuery("select count(*) as totalLogins from userLogin where userId = :userId");
		query.setParameter("userId", userId);
		
		Long totalLogins = (Long) query.uniqueResult();
		
		return totalLogins;
		
	}
	
	/**
	 * The 'setLastLogin' function will be called upon a successful login. It will save the entry
	 * into the rel_userLogins table.
	 * 
	 * @param username		This will be the username of the person logging in.
	 * 
	 */
	@Override
	public void setLastLogin(String username) {
		Query q1 = sessionFactory.getCurrentSession().createQuery("insert into userLogin (userId)" + "select id from User where username = :username");
		q1.setParameter("username", username);
		q1.executeUpdate();
		
	};
	
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
	 * The 'getUserSections' function will return a list of sections a specific user has been granted
	 * access to.
	 * 
	 * @param	userId		This will hold the userId to query on.
	 * 
	 * @return 				The function will return a list of sections granted to the user.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<userAccess> getuserSections(int userId) {
		 Query query = sessionFactory.getCurrentSession().createQuery("from userAccess where userId = :userId");
		 query.setParameter("userId",userId);
	     List<userAccess> userSectionList = query.list(); 
	     return userSectionList;	
	}

}
