package com.ut.dph.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.dao.configurationDAO;
import com.ut.dph.model.Organization;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.messageType;

@Service
public class configurationDAOImpl implements configurationDAO {

	  @Autowired
	  private SessionFactory sessionFactory;
	  
	 /**
	 * The 'createConfiguration' function will create a new configuration
	 * 
	 * @Table	configurations
	 * 
	 * @param	configuration	Will hold the configuration object from the form
	 * 
	 * @return 	The function will return the id of the created configuration
	 */
	  @Override
	  public Integer createConfiguration(configuration configuration) {
		  Integer lastId = null;
			
		  lastId = (Integer) sessionFactory.getCurrentSession().save(configuration);
			
		  return lastId;
	  }
	  
	 /**
	 * The 'updateConfiguration' function will update a selected configuration details
	 * 
	 * @Table	configurations
	 * 
	 * @param	configuration	Will hold the configuration object from the field
	 *	
	 */
	@Override
    public void updateConfiguration(configuration configuration) {
		sessionFactory.getCurrentSession().update(configuration);
	}
	
	
	/**
	 * The 'getConfigurationById' function will return a configuration based on the
	 * id passed in.
	 * 
	 * @Table 	configurations
	 * 
	 * @param	configId	This will hold the configuration id to find
	 * 
	 * @return	This function will return a single configuration object
	 */
	 @Override
	 public configuration getConfigurationById(int configId) {
	     return (configuration) sessionFactory.
	      getCurrentSession().
	      get(configuration.class, configId);
	  }
	 
	 /**
	 * The 'getConfigurationsByOrgId' function will return a list of configurations for the
	 * organization id passed in
	 * 
	 * @Table 	configurations
	 * 
	 * @param	orgId	This will hold the organization id to find
	 * 
	 * @return	This function will return a list of configuration object
	 */
	  @Override
	  @SuppressWarnings("unchecked")
	  public List<configuration> getConfigurationsByOrgId(int orgId) {
	    Query query = sessionFactory.getCurrentSession().createQuery("from configuration where orgId = :orgId order by dateCreated desc");
	    query.setParameter("orgId", orgId);
	    return query.list();
	  }
	  
	 /**
	 * The 'getOrganizationByName' function will return a single organization based on
	 * the name passed in.
	 * 
	 * @Table	organizations
	 * 
	 * @param	cleanURL	Will hold the 'clean' organization name from the url
	 * 
	 * @return	This function will return a single organization object
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<configuration> getConfigurationByName(String configName) {
	     Criteria criteria = sessionFactory.getCurrentSession().createCriteria(configuration.class);  
	     criteria.add(Restrictions.like("configName", configName));  
	     return criteria.list();  
	}
	  
	/**
	 * The 'getConfigurations' function will return a list of the configurations in the system
	 * 
	 * @Table	configurations
	 * 
	 * @param	page	This will hold the value of page being viewed (used for pagination)
	 * 			maxResults	This will hold the value of the maximum number of results we want
	 * 						to send back to the list page
	 * 
	 * @Return	This function will return a list of configuration objects
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<configuration> getConfigurations(int page, int maxResults) {
	      Query query = sessionFactory.getCurrentSession().createQuery("from configuration order by dateCreated desc");
	      
	      //By default we want to return the first result
		  int firstResult = 0;
		  
		 //If viewing a page other than the first we then need to figure out
	     //which result to start with
		 if(page > 1) {
			firstResult = (maxResults*(page-1));
		 }
		 query.setFirstResult(firstResult);
		 
		 //Set the max results to display
		 query.setMaxResults(maxResults);
		  
	      List<configuration> configurationList = query.list(); 
	      return configurationList;	
  	}
	
	/**
	* The 'getDataTranslations' function will return a list of data translations saved
	* for the passed in configuration/transport method.
	* 
	* @param	configId	The id of the configuration we want to return associated translations
	* 						for.
	* @param	transportMethod	The selected transport method for the configuration
	* 
	* @return	This function will return a list of translations
	*/
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<configurationDataTranslations> getDataTranslations(int configId, int transportMethod) {
		Query query = sessionFactory.getCurrentSession().createQuery("from configurationDataTranslations where configId = :configId and transportMethod = :transportMethod order by processOrder asc");
			  query.setParameter("configId",configId);
			  query.setParameter("transportMethod",transportMethod);
		
		return query.list();
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
		
		if(searchTerm != "") {
			//get a list of organization id's that match the term passed in
			List <Integer> orgIdList = new ArrayList<Integer>();
			Criteria findOrgs = sessionFactory.getCurrentSession().createCriteria(Organization.class);  
			findOrgs.add(Restrictions.like("orgName", "%"+searchTerm+"%"));  
		    List<Organization> orgs = findOrgs.list();
		    
		    for(Organization org : orgs) {
		    	orgIdList.add(org.getId());
			}
		    
		    //get a list of message type id's that match the term passed in
		    List <Integer> msgTypeIdList = new ArrayList<Integer>();
		    Criteria findMsgTypes = sessionFactory.getCurrentSession().createCriteria(messageType.class);
		    findMsgTypes.add(Restrictions.like("name", "%"+searchTerm+"%"));
		    List<messageType> msgTypes = findMsgTypes.list();
		    
		    for(messageType msgType : msgTypes) {
		    	msgTypeIdList.add(msgType.getId());
			}
		   
		    Criteria criteria = sessionFactory.getCurrentSession().createCriteria(configuration.class);
		    
		    if(orgIdList.size() == 0) {
		    	orgIdList.add(0);
		    }
		    if(msgTypeIdList.size() == 0) {
		    	msgTypeIdList.add(0);
		    }
		    
		    criteria.add(Restrictions.or(
		    		Restrictions.in("orgId", orgIdList),
		    		Restrictions.in("messageTypeId",msgTypeIdList)
	     		)
	     	)
	     	.addOrder(Order.desc("dateCreated"));
		    
		    return criteria.list();  
		}
		else {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(configuration.class);
			criteria.addOrder(Order.desc("dateCreated"));
			return criteria.list();  
		}
	   
	    
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
	 * @param	maxResults	This will hold the value of the maximum number of results we want
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
	
	/**
	 * The 'getTotalConnections' function will return the total number of active connections set up for a configuration.
	 * 
	 * @Table 	configurationCrosswalks
	 * 
	 * @param	configId 	The id of the configuration to find connections for.
	 * 
	 * @Return	The total number of active connections set up for a configurations
	 * 
	 */
	@Override
	public Long getTotalConnections(int configId) {
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery("SELECT count(*) FROM configurationConnections where configId = :configId and status = 1")
				.setParameter("configId", configId);
		
		BigInteger totalCount =  (BigInteger) query.uniqueResult();
			
		Long totalConnections = totalCount.longValue();
		
		return totalConnections;
	}
	
	/**
	 * The 'updateCompletedSteps' function will update the steps completed for a passe in
	 * configurations. This column will be used to determine when you can activate a 
	 * configuration and when you can access certain steps in the configuration creation
	 * process.
	 * 
	 * @param	configId		This will hold the id of the configuration to update
	 * 			stepCompleted	This will hold the completed step number
	 */
	@Override
	@Transactional
	public void updateCompletedSteps(int configId, int stepCompleted) {
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery("UPDATE configurations set stepsCompleted = :stepCompleted where id = :configId")
			 .setParameter("stepCompleted", stepCompleted)
			 .setParameter("configId", configId);
		
		query.executeUpdate();
	}
	
	/**
	* The 'getFileTypes' function will return a list of available file types
	* 
	*/
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public List getFileTypes() {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("SELECT id, fileType FROM ref_fileTypes order by fileType asc");
		
		return query.list();
	}
	
	/**
	 * The 'getFieldName' function will return the name of a field based on the fieldId passed in.
	 * This is used for display purposes to show the actual field lable instead of a field name.
	 * 
	 * @param fieldId	This will hold the id of the field to retrieve
	 * 
	 * @Return This function will return a string (field name)
	 */
	@Override
	@Transactional
	public String getFieldName(int fieldId) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("SELECT fieldDesc FROM configurationFormFields where id = :fieldId")
				.setParameter("fieldId", fieldId);
			
		String fieldName = (String) query.uniqueResult();
		
		return fieldName;
	}
	
	/**
	* The 'deleteDataTranslations' function will remove all data translations for the
	* passed in configuration / transport method.
	* 
	* @param	configId		The id of the configuration to remove associated translations
	* @param	transportMethod	The transport method for the configuration
	* 
	*/
	@Override
	@Transactional
	public void deleteDataTranslations(int configId, int transportMethod) {
		Query deleteTranslations = sessionFactory.getCurrentSession().createQuery("delete from configurationDataTranslations where configId = :configId and transportMethod = :transportMethod");
		deleteTranslations.setParameter("configId",configId);
		deleteTranslations.setParameter("transportMethod",transportMethod);
		deleteTranslations.executeUpdate();
	}

}
