/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.dao.impl;

import com.ut.healthelink.dao.newsletterDAO;
import com.ut.healthelink.model.newsletterSignup;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author chadmccue
 */
@Repository
public class newsletterDAOImpl implements newsletterDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    /**
     * The 'emailExists' function will search to see if the email address is already in the system.
     *
     * @param emailAddress The email address to see if it exists
     *
     * @return This function will return a list of email addresses;
     */
    @Override
    public List<newsletterSignup> emailExists(String emailAddress) throws Exception {

        Criteria findEmail = sessionFactory.getCurrentSession().createCriteria(newsletterSignup.class);
        findEmail.add(Restrictions.eq("emailAddress", emailAddress));

        return findEmail.list();

    }

    /**
     * The 'saveEmailAddress' will save the email sign up
     *
     * @param emailSignup The object holding the new email
     *
     */
    @Override
    public void saveEmailAddress(newsletterSignup emailSignup) throws Exception {
        sessionFactory.getCurrentSession().save(emailSignup);
    }
    
    
    /**
     * The 'removeEmailAddress' function will remove the provided email address from
     * the email newsletter list.
     * 
     * @param emailAddress  The provided email address
     */
    @Override
    public void removeEmailAddress(String emailAddress) throws Exception {
        
        Query deleteemailAddress = sessionFactory.getCurrentSession().createQuery("delete from newsletterSignup where emailAddress = :emailAddress");
        deleteemailAddress.setParameter("emailAddress", emailAddress);
        deleteemailAddress.executeUpdate();
        
    }

}
