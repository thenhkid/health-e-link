package com.ut.dph.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Criteria; 
import org.hibernate.criterion.Restrictions; 
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ut.dph.dao.userDAO;
import com.ut.dph.model.User;

@Service
public class userDAOImpl implements userDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Integer createUser(User user) {
		Integer lastId = null;
				
		lastId = (Integer) sessionFactory.getCurrentSession().save(user);
		
		return lastId;
	}
	
	@Override
    public void updateUser(User user) {
		sessionFactory.getCurrentSession().update(user);
	}
	
	@Override
	public User getUserById(int userId) {
      return (User) sessionFactory.
      getCurrentSession().
      get(User.class, userId);
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

}
