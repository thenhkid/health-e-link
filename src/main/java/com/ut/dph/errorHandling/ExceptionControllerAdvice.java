/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.errorHandling;

import com.ut.dph.model.User;
import com.ut.dph.model.mailMessage;
import com.ut.dph.service.emailMessageManager;

import java.net.InetAddress;
import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author chadmccue
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
   
    @Autowired
    private emailMessageManager emailMessageManager;
 
    @ExceptionHandler(Exception.class)
    public ModelAndView exception(HttpSession session, Exception e) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/exception");
        
        mailMessage messageDetails = new mailMessage();
        
        messageDetails.settoEmailAddress("gchan123@yahoo.com");
        messageDetails.setfromEmailAddress("dphuniversaltranslator@gmail.com");
        messageDetails.setmessageSubject("Exception Error " + InetAddress.getLocalHost().getHostAddress());
        
        StringBuilder sb = new StringBuilder();
        
        /* If a user is logged in then send along the user details */
        if(session.getAttribute("userDetails") != null) {
            User userInfo = (User)session.getAttribute("userDetails");
            if(userInfo != null) {
                sb.append("Logged in User: " + userInfo.getFirstName() + " " + userInfo.getLastName() + " (ID: "+ userInfo.getId() + ")");
                sb.append(System.getProperty("line.separator"));
                sb.append("User OrgId: " + userInfo.getOrgId());
                sb.append(System.getProperty("line.separator"));
            }
        }
       
        sb.append("Error: "+ e);
        sb.append("<br /><br />");
        sb.append("Message: " + e.getMessage());
        sb.append("<br /><br />");
        sb.append("Stack Trace: " + Arrays.toString(e.getStackTrace()));
        
        messageDetails.setmessageBody(sb.toString());
        
        emailMessageManager.sendEmail(messageDetails); 
        /*mav.addObject("messageBody",sb.toString());*/
        
        return mav;
    }
}
