/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.errorHandling;

import java.io.IOException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author chadmccue
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
    
    private MailSender mailSender;
 
    @ExceptionHandler(IOException.class)
    public ModelAndView exception(IOException e) {
         
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/exception");
        
        SimpleMailMessage msg = new SimpleMailMessage();
       
        msg.setTo("dphuniversaltranslator@gmail.com");
        
        msg.setFrom("dphuniversaltranslator@gmail.com");
        msg.setSubject("Exception Error");
        
        StringBuilder sb = new StringBuilder();
        sb.append("Name: "+e.getClass().getSimpleName());
        sb.append(System.getProperty("line.separator"));
        sb.append("Message: " + e.getMessage());
        sb.append(System.getProperty("line.separator"));
        sb.append("Stack Trace: " + e.getStackTrace());
        
        msg.setText(sb.toString());
        

        mailSender.send(msg);
 
        return mav;
    }
}
