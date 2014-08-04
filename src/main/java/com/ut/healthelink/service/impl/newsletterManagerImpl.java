/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.service.impl;

import com.ut.healthelink.dao.newsletterDAO;
import com.ut.healthelink.model.newsletterSignup;
import com.ut.healthelink.service.newsletterManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author chadmccue
 */
@Service
public class newsletterManagerImpl implements newsletterManager {
    
    @Autowired
    newsletterDAO newsletterDAO;
    
    @Override
    @Transactional
    public List<newsletterSignup> emailExists(String emailAddress) throws Exception {
        return newsletterDAO.emailExists(emailAddress);
    }
    
    @Override
    @Transactional
    public void saveEmailAddress(newsletterSignup emailSignup) throws Exception {
        newsletterDAO.saveEmailAddress(emailSignup);
    }
    
    @Override
    @Transactional
    public void removeEmailAddress(String emailAddress) throws Exception {
        newsletterDAO.removeEmailAddress(emailAddress);
    }
    
}
