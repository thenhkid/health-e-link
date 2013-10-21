package com.ut.dph.dao.impl;

import java.util.List;

import org.hibernate.Criteria; 
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions; 
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ut.dph.dao.messageTypeDAO;
import com.ut.dph.model.messageType;

/**
 * The brochureDAOImpl class will implement the DAO access layer to handle
 * updates for organization brochures
 * 
 * 
 * @author chadmccue
 *
 */


@Repository
public class messageTypeDAOImpl implements messageTypeDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * The 'createMessageType" function will create the new brochure 
	 * 
	 * @Table	messageTypes
	 * 
	 * @param	messageType	This will hold the messageType object from the form
	 * 
	 * @return 	The function will return the id of the new messageType
	 * 
	 */
	@Override
	public Integer createMessageType(messageType messageType) {
		Integer lastId = null;
			
		lastId = (Integer) sessionFactory.getCurrentSession().save(messageType);
		
		return lastId;
	}
	
	
	/**
	 * The 'updateMessageType' function will update the selected message type with the changes
	 * entered into the form.
	 * 
	 * @param	messageType	This will hold the messagetype object from the message type form
	 * 
	 * @return 			the function does not return anything
	 */
	@Override
    public void updateMessageType(messageType messageType) {
		sessionFactory.getCurrentSession().update(messageType);
	}
	

	/**
	 * The 'deleteMessageType' function will delete the selected brochure
	 * 
	 * @param	messageTypeId	This will hold the id of the message type to delete
	 * 
	 * @return 			the function does not return anything
	 */
	@Override
    public void deleteMessageType(int messageTypeId) {
		Query deleteMessageType = sessionFactory.getCurrentSession().createQuery("delete from messageType where id = :messageTypeId");
		deleteMessageType.setParameter("messageTypeId",messageTypeId);
		deleteMessageType.executeUpdate();
	}
	
	
	/**
	 * The 'getMessageTypeById' function will return a single message type object based on the messageTypeId
	 * passed in.
	 * 
	 * @param	messageTypeId	This will be id to find the specific message type
	 * 
	 * @return			The function will return a messageType object 
	 */
	@Override
	public messageType getMessageTypeById(int messageTypeId) {
      return (messageType) sessionFactory.getCurrentSession().get(messageType.class, messageTypeId);
	}
	
	/**
	 * The 'getMessageTypes' function will return the list of message types in the system.
	 * 
	 * @Table	messageTypes
	 * 
	 * @Param	page		This will hold the current page to view
	 * 			maxResults	This will hold the total number of results to return back to the list page
	 * 
	 * @Return	This function will return a list of message type objects
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<messageType>  getMessageTypes(int page, int maxResults) {
		
		Query query = sessionFactory.getCurrentSession().createQuery("from messageType order by name asc");
		
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
	 * The 'findMessageTypes' function will return a list of message type objects based on a search term. 
	 * The search will look for message types whose title or file name match the search term provided.
	 * 
	 * @param	searchTerm	This will be used to query the title and file name field
	 * 
	 * @return		The function will return a list of message type objects
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<messageType> findMessageTypes(String searchTerm) {
	    //Order by title
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(messageType.class)
	    	.add(Restrictions.or(
	    			Restrictions.like("name", searchTerm+"%"),
	    			Restrictions.like("templateFile", "%"+searchTerm+"%")
	     		)
	     	)
	     	.addOrder(Order.asc("name"));
	     
	     return criteria.list();  
	}
	
	/**
	 * The 'findTotalMessageTypes' function will return the total number of message types in the system
	 * 
	 * @Table	messageTypes
	 * 
	 * 
	 * @Return	This function will return the total number of message types set up in the system
	 */
	@Override
	public Long findTotalMessageTypes() {
		
		Query query = sessionFactory.getCurrentSession().createQuery("select count(*) as totalMessageTypes from messageType");
		
		Long totalMessageTypes = (Long) query.uniqueResult();
		
		return totalMessageTypes;
	}
	

}
