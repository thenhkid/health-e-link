package com.ut.dph.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ut.dph.dao.configurationDAO;
import com.ut.dph.model.configuration;

@Service
public class configurationDAOImpl implements configurationDAO {

	  @Autowired
	  private SessionFactory sessionFactory;
	  
	  @Override
	  public void saveConfiguration(configuration configuration) {
		sessionFactory.getCurrentSession().update(configuration);
	  }
	
	  @Override
	  public configuration getConfigurationById(int configId) {
	     return (configuration) sessionFactory.
	      getCurrentSession().
	      get(configuration.class, configId);
	  }
	  
	  @Override
	  @SuppressWarnings("unchecked")
	  public List<configuration> getConfigurationsByOrgId(int orgId) {
	    Query query = sessionFactory.getCurrentSession().createQuery("from configuration where orgId = :orgId order by configName asc");
	    query.setParameter("orgId", orgId);
	    return query.list();
	  }
	  
	  
	  @Override
	  @SuppressWarnings("unchecked")
	  public List<configuration> getConfigurations(int firstResults, int maxResults) {
	      Query query = sessionFactory.getCurrentSession().createQuery("from configuration order by configName asc");
	      query.setFirstResult(firstResults);
	      query.setMaxResults(maxResults);
	      List<configuration> configurationList = query.list(); 
	      return configurationList;	
  	}
	  
	 /**
	 * The 'findConfigurations' function will return a list of configurations based on a 
	 * search term passed in. The function will search configurations on the following fields
	 * configName, orgName and messageTypeName
	 * 
	 * @Table	configurations
	 * 
	 * @param	searchTerm		Will hold the term used search configurations on
	 * 
	 * @return	This function will return a list of configuration objects
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<configuration> findConfigurations(String searchTerm) {
	     Criteria criteria = sessionFactory.getCurrentSession().createCriteria(configuration.class)
	    	.add(Restrictions.or(
	    			Restrictions.like("configName", "%"+searchTerm+"%"),
	     			Restrictions.like("orgName", "%"+searchTerm+"%"),
	     			Restrictions.like("messageTypeName", "%"+searchTerm+"%")
	     		)
	     	);
	     
	     return criteria.list();  
	}
	  
	 /**
	 * The 'totalConfigs' function will return the total number of active configurations in the system. This
	 * will be used for pagination when viewing the list of configurations
	 * 
	 * @Table configurations
	 * 
	 * @return This function will return the total configurations
	 */
	@Override
	public Long findTotalConfigs() {
		Query query = sessionFactory.getCurrentSession().createQuery("select count(*) as totalConfigs from configuration where status = 1");
		
		Long totalConfigs = (Long) query.uniqueResult();
		
		return totalConfigs;
	}
  
  	/**
   	* The 'getLatestConfigurations' function will return a list of the latest configurations that have been
   	*  added to the system and activated.
	 * 
	 * @Table	organizations
	 * 
	 * @param	maxResults	This will hold the value of the maxium number of results we want
	 * 						to send back to the page
	 * 
	 * @Return	This function will return a list of organization objects
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<configuration> getLatestConfigurations(int maxResults) {
	    Query query = sessionFactory.getCurrentSession().createQuery("from configuration where status = 1 order by dateCreated desc");
	   
		//Set the max results to display
		query.setMaxResults(maxResults);
		
	    List<configuration> configurationList = query.list(); 
	    return configurationList;	
	}

}
