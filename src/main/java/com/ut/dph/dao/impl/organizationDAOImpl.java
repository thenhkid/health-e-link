package com.ut.dph.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Criteria; 
import org.hibernate.criterion.Restrictions; 
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ut.dph.dao.organizationDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.model.User;

@Service
public class organizationDAOImpl implements organizationDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Integer createOrganization(Organization organization) {
		Integer lastId = null;
				
		lastId = (Integer) sessionFactory.getCurrentSession().save(organization);
		
		return lastId;
	}
	
	@Override
    public void updateOrganization(Organization organization) {
		sessionFactory.getCurrentSession().update(organization);
	}
	
	@Override
	public Organization getOrganizationById(int orgId) {
      return (Organization) sessionFactory.
      getCurrentSession().
      get(Organization.class, orgId);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Organization> getOrganizationByName(String cleanURL) {
	     Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Organization.class);  
	     criteria.add(Restrictions.like("cleanURL", cleanURL));  
	     return criteria.list();  
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Organization> findOrganizations(String searchTerm) {
	     Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Organization.class)
	    	.add(Restrictions.not(Restrictions.eq("cleanURL","")))
	    	.add(Restrictions.or(
	    			Restrictions.like("orgName", searchTerm+"%"),
	     			Restrictions.like("city", searchTerm+"%"),
	     			Restrictions.like("address", searchTerm+"%")
	     		)
	     	);
	     
	     return criteria.list();  
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Organization> getOrganizations(int firstResults, int maxResults) {
	    Query query = sessionFactory.getCurrentSession().createQuery("from Organization where cleanURL is not '' order by orgName asc");
	    query.setFirstResult(firstResults);
	    //query.setMaxResults(maxResults);
	    List<Organization> organizationList = query.list(); 
	    return organizationList;	
	}
	
	@Override
	public Long findTotalUsers(int orgId) {
		
		Query query = sessionFactory.getCurrentSession().createQuery("select count(*) as totalUsers from User where orgId = :orgId");
		query.setParameter("orgId", orgId);
		
		Long totalUsers = (Long) query.uniqueResult();
		
		return totalUsers;
		
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<User>  getOrganizationUsers(int orgId) {
		
		Query query = sessionFactory.getCurrentSession().createQuery("from User where orgId = :orgId order by lastName asc");
		query.setParameter("orgId", orgId);
		
		return query.list();
		
	}

}
