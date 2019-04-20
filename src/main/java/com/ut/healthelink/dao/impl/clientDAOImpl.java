/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.dao.impl;

import com.ut.healthelink.dao.clientDAO;
import com.ut.healthelink.model.clients;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author chadmccue
 */
@Repository
public class clientDAOImpl implements clientDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    @Transactional
    public List<clients> searchClients(int orgId, String firstName, String lastName, String DOB, String postalCode) throws Exception {
       
        String DOBSearch = "";
        
        
        if("".equals(firstName) && "".equals(lastName) && "".equals(DOB) && "".equals(postalCode)) {
            return null;
        }
        else {
            
            if(!"".equals(DOB)) {
                Date dateOfBirth = new SimpleDateFormat("MM/dd/yyyy").parse(DOB);
                DOBSearch = new SimpleDateFormat("yyyy-MM-dd").format(dateOfBirth);
            }
            
            String clientSQL = "select * "
                    + "from clients "
                    + "where id in (select clientId from organizationclients where orgId = :orgId)";
            
            if(!"".equals(firstName)) {
                clientSQL += " and firstName like '%" + firstName + "%'";
            }
            if(!"".equals(lastName)) {
               clientSQL += " and lastName like '%" + lastName + "%'";
            }
            if(!"".equals(DOBSearch)) {
                 clientSQL += " and DOB = '" + DOBSearch + "'";
            }
            if(!"".equals(postalCode)) {
                clientSQL += " and postalCode like '%" + postalCode + "%'";
            }
            
            Query query = sessionFactory.getCurrentSession().createSQLQuery(clientSQL)
                     .setResultTransformer(
                        Transformers.aliasToBean(clients.class));
            query.setParameter("orgId", orgId);

            List<clients> clients = query.list();

            return clients;
            
        }
        
    }
    
    @Override
    @Transactional
    public clients getClientById(int clientId) throws Exception {
        return (clients) sessionFactory.getCurrentSession().get(clients.class, clientId);
    }
}
