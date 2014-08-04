/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.dao;

import com.ut.healthelink.model.newsletterSignup;
import java.util.List;

/**
 *
 * @author chadmccue
 */
public interface newsletterDAO {
    
    List<newsletterSignup> emailExists(String emailAddress) throws Exception;
    
    void saveEmailAddress(newsletterSignup emailSignup) throws Exception;
    
    void removeEmailAddress(String emailAddress) throws Exception;
    
}
