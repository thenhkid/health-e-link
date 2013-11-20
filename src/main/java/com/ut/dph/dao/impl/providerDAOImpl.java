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
import com.ut.dph.model.providerIdNum;

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
	 * @return			The function does not return anything
	 */
	@Override
	public void deleteProvider(int providerId) {
		//Need to remove the provider addresses
		//Find all the addresses associated to the provider to be deleted
		Query deleteAddresses = sessionFactory.getCurrentSession().createQuery("delete from providerAddress where providerId = :providerId");
		deleteAddresses.setParameter("providerId",providerId);
		deleteAddresses.executeUpdate();
		
		//delete provider ids
		Query deleteProviderIds = sessionFactory.getCurrentSession().createQuery("delete from providerIdNum where providerId = :providerId)");
		deleteProviderIds.setParameter("providerId",providerId);
		deleteProviderIds.executeUpdate();
		
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
	    			Restrictions.like("firstName", "%"+searchTerm+"%"),
	    			Restrictions.like("lastName", "%"+searchTerm+"%")
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
	 * #Return 	The function will return a list providerAddress objects
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
	
	/**
	 * The 'getAddressDetails' function will return the details of the clicked address
	 * 
	 * @param	addressId	This will be used to query addresses
	 * 
	 * #Return 	The function will return a providerAddress object
	 */
	@Override
	public providerAddress getAddressDetails(int addressId) {
		return (providerAddress) sessionFactory.getCurrentSession().get(providerAddress.class, addressId);
	}
	
	
	/**
	 * The 'updateAddress' function will update the selected address with the changes
	 * entered into the form.
	 * 
	 * @param	systemAddress	This will hold the address object from the address form
	 * 
	 * @return 			the function does not return anything
	 */
	@Override
    public void updateAddress(providerAddress providerAddress) {
		sessionFactory.getCurrentSession().update(providerAddress);
	}
	
	/**
	 * The 'createAddress' function will submit the new address
	 * entered into the form.
	 * 
	 * @param	systemAddress	This will hold the address object from the address form
	 * 
	 * @return 			the function does not return anything
	 */
	@Override
    public void createAddress(providerAddress providerAddress) {
		sessionFactory.getCurrentSession().save(providerAddress);
	}
	
	/**
	 * The 'deleteAddress' function will delete the provider address on the addressId
	 * passed in.
	 * 
	 * @param	addressid	This will be id to find the specific address
	 * 
	 * @return			The function does not return anything
	 */
	@Override
	public void deleteAddress(int addressid) {
		Query deleteAddress = sessionFactory.getCurrentSession().createQuery("delete from providerAddress where id = :addressid");
		deleteAddress.setParameter("addressid",addressid);
		deleteAddress.executeUpdate();
	}
	
	/**
	 * The 'getProviderIds' function will return a list of ids associated
	 * with the selected provider.
	 * 
	 * @param	providerIdNum	This will be used to query the ids
	 * 
	 * #Return 	The function will return a list providerIdNum objects
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<providerIdNum>  getProviderIds(int providerId) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(providerIdNum.class)
	    	.add(Restrictions.eq("providerId",providerId))
	    	.addOrder(Order.asc("dateCreated"));
	     
	     return criteria.list();  
	}
	
	/**
	 * The 'getIdDetails' function will return the details of the clicked id
	 * 
	 * @param	id	This will be used to query ids
	 * 
	 * #Return 	The function will return a providerIdNum object
	 */
	@Override
	public providerIdNum getIdDetails(int id) {
		return (providerIdNum) sessionFactory.getCurrentSession().get(providerIdNum.class, id);
	}
	
	
	/**
	 * The 'updateId' function will update the selected id with the changes
	 * entered into the form.
	 * 
	 * @param	providerIdNum	This will hold the id object from the provider id form
	 * 
	 * @return 			the function does not return anything
	 */
	@Override
    public void updateId(providerIdNum providerIdNum) {
		sessionFactory.getCurrentSession().update(providerIdNum);
	}
	
	/**
	 * The 'createId' function will submit the new id
	 * entered into the form.
	 * 
	 * @param	providerIdNum	This will hold the id object from the id form
	 * 
	 * @return 			the function does not return anything
	 */
	@Override
    public void createId(providerIdNum providerIdNum) {
		sessionFactory.getCurrentSession().save(providerIdNum);
	}
	
	/**
	 * The 'deleteAddress' function will delete the provider address on the addressId
	 * passed in.
	 * 
	 * @param	id	This will be id to find the specific provider id
	 * 
	 * @return			The function does not return anything
	 */
	@Override
	public void deleteId(int id) {
		Query deleteProviderId = sessionFactory.getCurrentSession().createQuery("delete from providerIdNum where id = :id");
		deleteProviderId.setParameter("id",id);
		deleteProviderId.executeUpdate();
	}

}
