/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service;

import com.ut.dph.model.mailMessage;

/**
 *
 * @author chad
 */
public interface emailMessageManager {
    
    void sendEmail(mailMessage messageDetails) throws Exception;
    
}
