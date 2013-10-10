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

@Repository
public class userDAOImpl implements userDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Integer createUser(User user) {
		Integer lastId = null;
				
		lastId = (Integer) sessionFactory.getCurrentSession().save(user);
		
		//Need to insert the updated sections
		List<Integer> userSections = user.getsectionList();
		
		//Loop through the selected sections and save them for the user
		for(int i = 0;i<userSections.size();i++) {
			userAccess newusersections = new userAccess();
			int sectionid = userSections.get(i);
			newusersections.setUserId(lastId);
			newusersections.setFeatureId(sectionid);
			sessionFactory.getCurrentSession().save(newusersections);
		}
		
		return lastId;
	}
	
	@Override
    public void updateUser(User user) {
		sessionFactory.getCurrentSession().update(user);
		
		//Need to update the available sections the user has access to
		Query q1 = sessionFactory.getCurrentSession().createQuery("delete from userAccess where userId = :userId");
		q1.setParameter("userId", user.getId());
		q1.executeUpdate();
		
		//Need to insert the updated sections
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
	
	@Override
	public User getUserById(int userId) {
      return (User) sessionFactory.getCurrentSession().get(User.class, userId);
	}
	
	@Override
	public User getUserByUserName(String username) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);  
	    criteria.add(Restrictions.like("username", username));  
	    return (User) criteria.uniqueResult();   
	}
	
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
	
	@Override
	@SuppressWarnings("unchecked")
	public List<siteSections> getSections() {
		 Query query = sessionFactory.getCurrentSession().createQuery("from siteSections order by featureName asc");
	     List<siteSections> sectionList = query.list(); 
	     return sectionList;	
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<userAccess> getuserSections(int userId) {
		 Query query = sessionFactory.getCurrentSession().createQuery("from userAccess where userId = :userId");
		 query.setParameter("userId",userId);
	     List<userAccess> userSectionList = query.list(); 
	     return userSectionList;	
	}

}
