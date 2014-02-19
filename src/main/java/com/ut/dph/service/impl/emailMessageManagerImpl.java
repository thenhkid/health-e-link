/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.service.impl;

import com.ut.dph.model.mailMessage;
import com.ut.dph.service.emailMessageManager;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 *
 * @author chad
 */
@Service
public class emailMessageManagerImpl implements emailMessageManager {
    
    private MailSender mailSender;
    
    public void setMailSender(MailSender mailSender) {
            this.mailSender = mailSender;
    }
    
    public void sendEmail(mailMessage messageDetails) throws Exception {
        
        SimpleMailMessage msg = new SimpleMailMessage();
       
        msg.setTo(messageDetails.gettoEmailAddress());
        
        if(messageDetails.getccEmailAddress() != null) {
            msg.setCc(messageDetails.getccEmailAddress());
        }
        
        msg.setFrom(messageDetails.getfromEmailAddress());
        msg.setSubject(messageDetails.getmessageSubject());
        msg.setReplyTo(messageDetails.getfromEmailAddress());
        msg.setText(messageDetails.getmessageBody());
        
        mailSender.send(msg);
        
    }
}
