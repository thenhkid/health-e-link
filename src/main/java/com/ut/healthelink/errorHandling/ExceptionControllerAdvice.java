/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.errorHandling;

import com.ut.healthelink.model.User;
import com.ut.healthelink.model.mailMessage;
import com.ut.healthelink.service.emailMessageManager;
import com.ut.healthelink.service.userManager;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author chadmccue
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
   
    @Autowired
    private emailMessageManager emailMessageManager;
    
    @Autowired
    private userManager usermanager;
 
    @ExceptionHandler(Exception.class)
    public ModelAndView exception(HttpSession session, Exception e, Authentication authentication) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/exception");
        
        mailMessage messageDetails = new mailMessage();
        
        messageDetails.settoEmailAddress("dphuniversaltranslator@gmail.com");
        messageDetails.setfromEmailAddress("dphuniversaltranslator@gmail.com");
        messageDetails.setmessageSubject("Exception Error " + InetAddress.getLocalHost().getHostAddress());
        
        StringBuilder sb = new StringBuilder();
        
        /* If a user is logged in then send along the user details */
        if(session.getAttribute("userDetails") != null || authentication != null) {
            User userInfo = (User)session.getAttribute("userDetails");
            
            if(userInfo == null && authentication != null) {
            // see if it is an admin that is logged in
            	userInfo = usermanager.getUserByUserName(authentication.getName());          	
            }
            if (userInfo != null) {	
            	sb.append("Logged in User: " + userInfo.getFirstName() + " " + userInfo.getLastName() + " (ID: "+ userInfo.getId() + ")");
                sb.append(System.getProperty("line.separator"));
                sb.append("User OrgId: " + userInfo.getOrgId());
                sb.append(System.getProperty("line.separator"));
            }
        }
       
        sb.append("Error: "+ e);
        sb.append("<br /><br />");
        sb.append("Time: " + new Date());
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
