package com.ut.dph.dao.impl;

import java.util.List;

import org.hibernate.Criteria; 
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions; 
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ut.dph.dao.providerDAO;
import com.ut.dph.model.Provider;
import com.ut.dph.model.providerAddress;

/**
 * The providerDAOImpl class will implement the DAO access layer to handle
 * updates for organization providers
 * 
 * 
 * @author chadmccue
 *
 */


@Repository
public class providerDAOImpl implements providerDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * The 'createProvider" function will create the new provider 
	 * 
	 * @Table	providers
	 * 
	 * @param	provider	This will hold the provider object from the form
	 * 
	 * @return 			the function will return the id of the new provider
	 * 
	 */
	@Override
	public Integer createProvider(Provider provider) {
		Integer lastId = null;
				
		lastId = (Integer) sessionFactory.getCurrentSession().save(provider);
		
		List<providerAddress> addresses = provider.getProviderAddresses();
		
		//Loop through the selected provider address information and save to the provider
		//This will populate the 'info_ProviderAddresses' table
		for(int i = 0;i<addresses.size();i++) {
			addresses.get(i).setProviderId(lastId);
			sessionFactory.getCurrentSession().save(addresses.get(i));
		}
		
		
		return lastId;
	}
	
	
	/**
	 * The 'updateProvider' function will update the selected provider with the changes
	 * entered into the form.
	 * 
	 * @param	provider	This will hold the provider object from the provider form
	 * 
	 * @return 			the function does not return anything
	 */
	@Override
    public void updateProvider(Provider provider) {
		sessionFactory.getCurrentSession().update(provider);
	}
	
	/**
	 * The 'getProviderById' function will return a single provider object based on the providerId
	 * passed in.
	 * 
	 * @param	providerId	This will be id to find the specific provider
	 * 
	 * @return			The function will return a provider object 
	 */
	@Override
	public Provider getProviderById(int providerId) {
      return (Provider) sessionFactory.getCurrentSession().get(Provider.class, providerId);
	}
	
	/**
	 * The 'deleteProvider' function will delete the provider based on the providerId
	 * passed in.
	 * 
	 * @param	providerId	This will be id to find the specific provider
	 * 
	 * @return			The function will return a provider object 
	 */
	@Override
	public void deleteProvider(int providerId) {
		Query deletProvider = sessionFactory.getCurrentSession().createQuery("delete from Provider where id = :providerId");
		deletProvider.setParameter("providerId",providerId);
		deletProvider.executeUpdate();
	}
	
	/**
	 * The 'findProviders' function will return a list of provider objects based on a specific 
	 * organization Id and a search term. The search will look for providers for a specific
	 * organization and whose first name or last name match the search term provided.
	 * 
	 * @param	orgId		This will be the orgId used to find providers
	 * 			searchTerm	This will be used to query the firstname field and lastname field
	 * 
	 * @return		The function will return a list of provider objects
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Provider> findProviders(int orgId, String searchTerm) {
	    //Order by lastname then firstname 
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Provider.class)
	    	.add(Restrictions.eq("orgId",orgId))
	    	.add(Restrictions.or(
	    			Restrictions.like("firstName", searchTerm+"%"),
	    			Restrictions.like("lastName", searchTerm+"%")
	     		)
	     	)
	     	.addOrder(Order.asc("lastName"))
	     	.addOrder(Order.asc("firstName"));
	     
	     return criteria.list();  
	}
	
	/**
	 * The 'getProviderAddresses' function will return a list of addresses associated
	 * with the selected provider.
	 * 
	 * @param	providerId	This will be used to query addresses
	 * 
	 * #Return 	The function will return a lost providerAddress objects
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<providerAddress>  getProviderAddresses(int providerId) {
		 //Order by city
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(providerAddress.class)
	    	.add(Restrictions.eq("providerId",providerId))
	    	.addOrder(Order.asc("city"));
	     
	     return criteria.list();  
	}

}
