package com.ut.dph.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Criteria; 
import org.hibernate.criterion.Restrictions; 
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.dao.organizationDAO;
import com.ut.dph.model.Brochure;
import com.ut.dph.model.Organization;
import com.ut.dph.model.Provider;
import com.ut.dph.model.User;
import com.ut.dph.reference.fileSystem;
import com.ut.dph.service.brochureManager;

/**
 * The organizationDAOImpl class will implement the DAO access layer to handle
 * queries for an organization
 * 
 * 
 * @author chadmccue
 *
 */

@Repository
public class organizationDAOImpl implements organizationDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private brochureManager brochureManager;
	
	/**
	 * The 'createOrganziation' function will create a new organization
	 * 
	 * @Table	organizations
	 * 
	 * @param	organization	Will hold the organization object from the form
	 * 
	 * @return 	The function will return the id of the created organization
	 */
	@Override
	public Integer createOrganization(Organization organization) {
		Integer lastId = null;
				
		lastId = (Integer) sessionFactory.getCurrentSession().save(organization);
		
		return lastId;
	}
	
	
	/**
	 * The 'updateOrganization' function will update a selected organization
	 * 
	 * @Table	organizations
	 * 
	 * @param	organization	Will hold the organization object from the field
	 *	
	 */
	@Override
    public void updateOrganization(Organization organization) {
		sessionFactory.getCurrentSession().update(organization);
	}
	
	
	/**
	 * The 'getOrganizationById' function will return an organization based on
	 * organization id passed in.
	 * 
	 * @Table 	organizations
	 * 
	 * @param	orgId	This will hold the organization id to find
	 * 
	 * @return	This function will return a single organization object
	 */
	@Override
	public Organization getOrganizationById(int orgId) {
      return (Organization) sessionFactory.
      getCurrentSession().
      get(Organization.class, orgId);
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
	public List<Organization> getOrganizationByName(String cleanURL) {
	     Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Organization.class);  
	     criteria.add(Restrictions.like("cleanURL", cleanURL));  
	     return criteria.list();  
	}
	
	/**
	 * The 'findOrganizations' function will return a list of organizations based on a 
	 * search term passed in. The function will search organizations on the following fields
	 * cleanURL, orgName, city, address
	 * 
	 * The cleanURL field will contain the organization name with spaces removed. This will be
	 * used in the url when you select an organization.
	 * 
	 * @Table	organizations
	 * 
	 * @param	searchTerm		Will hold the term used search organizations on
	 * 
	 * @return	This function will return a list of organization objects
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Organization> findOrganizations(String searchTerm) {
	     Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Organization.class)
	    	.add(Restrictions.not(Restrictions.eq("cleanURL","")))
	    	.add(Restrictions.or(
	    			Restrictions.like("orgName", "%"+searchTerm+"%"),
	     			Restrictions.like("city", "%"+searchTerm+"%"),
	     			Restrictions.like("address", "%"+searchTerm+"%")
	     		)
	     	);
	     
	     return criteria.list();  
	}
	
	/**
	 * The 'findTotalOrgs' function will return the total number of organizations in the system. This
	 * will be used for pagination when viewing the list of organizations
	 * 
	 * @Table organizations
	 * 
	 * @return This function will return the total organizations
	 */
	@Override
	public Long findTotalOrgs() {
		Query query = sessionFactory.getCurrentSession().createQuery("select count(*) as totalOrgs from Organization where cleanURL is not ''");
		
		Long totalOrgs = (Long) query.uniqueResult();
		
		return totalOrgs;
	}
	
	
	/**
	 * The 'getOrganizations' function will return a list of the organizations in the system
	 * 
	 * @Table	organizations
	 * 
	 * @param	page	This will hold the value of page being viewed (used for pagination)
	 * 			maxResults	This will hold the value of the maxium number of results we want
	 * 						to send back to the list page
	 * 
	 * @Return	This function will return a list of organization objects
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Organization> getOrganizations(int page, int maxResults) {
	    Query query = sessionFactory.getCurrentSession().createQuery("from Organization where cleanURL is not '' order by orgName asc");
	    
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
		
	    List<Organization> organizationList = query.list(); 
	    return organizationList;	
	}
	
	/**
	 * The 'getLatestOrganizations' function will return a list of the latest organizations added to the system.
	 * This function will only return active organizations.
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
	public List<Organization> getLatestOrganizations(int maxResults) {
	    Query query = sessionFactory.getCurrentSession().createQuery("from Organization where cleanURL is not '' and status = 1 order by dateCreated desc");
	   
		//Set the max results to display
	    if(maxResults > 0) {
	    	query.setMaxResults(maxResults);
	    }
		
	    List<Organization> organizationList = query.list(); 
	    return organizationList;	
	}
	
	/**
	* The 'getAllActiveOrganizations' function will return all the active organizations in the system. The function
	* will sort by organization name
	* 
	* @Table	Organizations
	* 
	* @Return	This function will return a list of organization objects
	*/
	@Override
	@SuppressWarnings("unchecked")
	public List<Organization> getAllActiveOrganizations() {
	    Query query = sessionFactory.getCurrentSession().createQuery("from Organization where cleanURL is not '' and status = 1 order by orgName desc");
	   
	    List<Organization> organizationList = query.list(); 
	    return organizationList;	
	}
	
	/**
	 * The 'findTotalUsers' function will return the total number of system users set up for a 
	 * specific organization.
	 * 
	 * @Table	users
	 * 
	 * @Param	orgId	This will hold the organization id we want to search on
	 * 
	 * @Return	This function will return the total number of users for the organization
	 */
	@Override
	public Long findTotalUsers(int orgId) {
		
		Query query = sessionFactory.getCurrentSession().createQuery("select count(*) as totalUsers from User where orgId = :orgId");
		query.setParameter("orgId", orgId);
		
		Long totalUsers = (Long) query.uniqueResult();
		
		return totalUsers;
		
	}
	
	/**
	 * The 'findTotalConfigurations' function will return the total number of configurations set up for a 
	 * specific organization.
	 * 
	 * @Table	configurations
	 * 
	 * @Param	orgId	This will hold the organization id we want to search on
	 * 
	 * @Return	This function will return the total number of configurations for the organization
	 */
	@Override
	public Long findTotalConfigurations(int orgId) {
		
		Query query = sessionFactory.getCurrentSession().createQuery("select count(*) as totalConfigs from configuration where orgId = :orgId");
		query.setParameter("orgId", orgId);
		
		Long totalConfigs = (Long) query.uniqueResult();
		
		return totalConfigs;
		
	}
	
	
	/**
	 * The 'getOrganizationUsers' function will return the list of users for a specific organization.
	 * 
	 * @Table	users
	 * 
	 * @Param	orgId		This will hold the organization id to search on
	 * 			page		This will hold the current page to view
	 * 			maxResults	This will hold the total number of results to return back to the list page
	 * 
	 * @Return	This function will return a list of user objects
	 */
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
	
	/**
	 * The 'findTotalProviders' function will return the total number of providers set up for a 
	 * specific organization.
	 * 
	 * @Table	providers
	 * 
	 * @Param	orgId	This will hold the organization id we want to search on
	 * 
	 * @Return	This function will return the total number of providers for the organization
	 */
	@Override
	public Long findTotalProviders(int orgId) {
		
		Query query = sessionFactory.getCurrentSession().createQuery("select count(*) as totalProviders from Provider where orgId = :orgId");
		query.setParameter("orgId", orgId);
		
		Long totalProviders = (Long) query.uniqueResult();
		
		return totalProviders;
	}
	
	/**
	 * The 'getOrganizationProviders' function will return the list of providers for a specific organization.
	 * 
	 * @Table	providers
	 * 
	 * @Param	orgId		This will hold the organization id to search on
	 * 			page		This will hold the current page to view
	 * 			maxResults	This will hold the total number of results to return back to the list page
	 * 
	 * @Return	This function will return a list of provider objects
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Provider>  getOrganizationProviders(int orgId, int page, int maxResults) {
		
		Query query = sessionFactory.getCurrentSession().createQuery("from Provider where orgId = :orgId order by lastName asc, firstName asc");
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
	
	/**
	 * The 'findTotalBrochures' function will return the total number of brochures set up for a 
	 * specific organization.
	 * 
	 * @Table	brochures
	 * 
	 * @Param	orgId	This will hold the organization id we want to search on
	 * 
	 * @Return	This function will return the total number of brochures for the organization
	 */
	@Override
	public Long findTotalBrochures(int orgId) {
		
		Query query = sessionFactory.getCurrentSession().createQuery("select count(*) as totalBrochures from Brochure where orgId = :orgId");
		query.setParameter("orgId", orgId);
		
		Long totalBrochures = (Long) query.uniqueResult();
		
		return totalBrochures;
	}
	
	/**
	 * The 'getOrganizationBrochures' function will return the list of brochures for a specific organization.
	 * 
	 * @Table	brochures
	 * 
	 * @Param	orgId		This will hold the organization id to search on
	 * 			page		This will hold the current page to view
	 * 			maxResults	This will hold the total number of results to return back to the list page
	 * 
	 * @Return	This function will return a list of brochure objects
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Brochure>  getOrganizationBrochures(int orgId, int page, int maxResults) {
		
		Query query = sessionFactory.getCurrentSession().createQuery("from Brochure where orgId = :orgId order by title asc");
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
	
	/**
	 * The 'deleteOrganization' function will remove the organization and all other entities associated to the 
	 * organization. (Users, Providers, Brochures, Configurations, etc). When deleting users the function will
	 * also remove anything associated to the users (Logins, Access, etc).
	 * 
	 * @Param	orgId	This will hold the organization that will be deleted
	 * 
	 * @Return This function will not return any values.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void deleteOrganization(int orgId) {
		
		//Delete the providers
		Query deleteProvider = sessionFactory.getCurrentSession().createQuery("delete from Provider where orgId = :orgId");
		deleteProvider.setParameter("orgId", orgId);
		deleteProvider.executeUpdate();
		
		//Delete Brochures and the actual uploaded file
		//Find all the brochures associated to the organization to be deleted
		Query findBrochures = sessionFactory.getCurrentSession().createQuery("from Brochure where orgId = :orgId");
		findBrochures.setParameter("orgId",orgId);
		
		List<Brochure> brochures = findBrochures.list();
		
		//Loop through all the brochures.
		for(int i = 0; i<brochures.size();i++) {
			int brochureId = brochures.get(i).getId();
			brochureManager.deleteBrochure(brochureId);
		}
		
		//Find all the users associated to the organization to be deleted
		Query findUsers = sessionFactory.getCurrentSession().createQuery("from User where orgId = :orgId");
		findUsers.setParameter("orgId",orgId);
		
		List<User> users = findUsers.list();
		
		//Loop through all the users.
		for(int i = 0; i<users.size();i++) {
			int userId = users.get(i).getId();
			
			//Delete the logins for the users associated to the organization to be deleted.
			Query deleteLogins = sessionFactory.getCurrentSession().createQuery("delete from userLogin where userId = :userId");
			deleteLogins.setParameter("userId",userId);
			deleteLogins.executeUpdate();
			
			//Delete the user access entries for the users associated to the organization to be deleted.
			Query deleteuserFeatures = sessionFactory.getCurrentSession().createQuery("delete from userAccess where userId = :userId");
			deleteuserFeatures.setParameter("userId",userId);
			deleteuserFeatures.executeUpdate();
		}
		
		//Delete all users associated to the organization
		Query deleteUser = sessionFactory.getCurrentSession().createQuery("delete from User where orgId = :orgId");
		deleteUser.setParameter("orgId", orgId);
		deleteUser.executeUpdate();
		
		//Get the organization cleanURL
		Organization orgDetails = getOrganizationById(orgId);
		
		//Delete the organization folder within bowlink
		fileSystem dir = new fileSystem();
		dir.deleteOrgDirectories(orgDetails.getcleanURL());
		
		//Delete the organization
		Query deleteOrg = sessionFactory.getCurrentSession().createQuery("delete from Organization where id = :orgId");
		deleteOrg.setParameter("orgId", orgId);
		deleteOrg.executeUpdate();
		
	}

}
