package com.ut.dph.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.Restrictions;

import com.ut.dph.dao.configurationTransportDAO;
import com.ut.dph.model.configurationTransport;

@Service
public class configurationTransportDAOImpl implements configurationTransportDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * The 'getTransportDetails' function will return the details of the transport
	 * method for the passed in configuration id
	 * 
	 * @param configId	Holds the id of the selected configuration
	 * 
	 * @Return	This function will return a configurationTransport object
	 */
	public configurationTransport getTransportDetails(int configId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(configurationTransport.class);  
	    criteria.add(Restrictions.like("configId", configId));  
	    return (configurationTransport) criteria.uniqueResult();  
 	}
	
	/**
	* The 'getTransportMethods' function will return a list of available transport methods
	* 
	*/
	@Override
	@SuppressWarnings("rawtypes")
	@Transactional
	public List getTransportMethods() {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("SELECT id, transportMethod FROM ref_transportMethods order by transportMethod asc");
		
		return query.list();
	}

	
}
