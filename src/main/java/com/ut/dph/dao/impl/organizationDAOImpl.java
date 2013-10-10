package com.ut.dph.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Criteria; 
import org.hibernate.criterion.Restrictions; 
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ut.dph.dao.organizationDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.model.User;

@Repository
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
	public Long findTotalOrgs() {
		Query query = sessionFactory.getCurrentSession().createQuery("select count(*) as totalOrgs from Organization where cleanURL is not ''");
		
		Long totalOrgs = (Long) query.uniqueResult();
		
		return totalOrgs;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Organization> getOrganizations(int page, int maxResults) {
	    Query query = sessionFactory.getCurrentSession().createQuery("from Organization where cleanURL is not '' order by orgName asc");
	    
	    int firstResult = 0;
		
		//Set the parameters for paging
		//Set the page to load
		if(page > 1) {
			firstResult = (maxResults*(page-1));
		}
		query.setFirstResult(firstResult);
		//Set the max results to display
		query.setMaxResults(maxResults);
		
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
	public List<User>  getOrganizationUsers(int orgId, int page, int maxResults) {
		
		Query query = sessionFactory.getCurrentSession().createQuery("from User where orgId = :orgId order by lastName asc, firstName asc");
		query.setParameter("orgId", orgId);
		
		int firstResult = 0;
		
		//Set the parameters for paging
		//Set the page to load
		if(page > 1) {
			firstResult = (maxResults*(page-1));
		}
		query.setFirstResult(firstResult);
		//Set the max results to display
		query.setMaxResults(maxResults);
		
		return query.list();
		
	}

}
