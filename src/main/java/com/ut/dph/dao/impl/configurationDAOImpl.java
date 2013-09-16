package com.ut.dph.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ut.dph.dao.configurationDAO;
import com.ut.dph.model.configuration;

@Service
public class configurationDAOImpl implements configurationDAO {

  @Autowired
  private SessionFactory sessionFactory;
  
  @Override
  public void insertConfiguration(configuration configuration) {
    sessionFactory.getCurrentSession().save(configuration);
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
    Query query = sessionFactory.
      getCurrentSession().
      createQuery("from configuration where orgId = :orgId");
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

}
