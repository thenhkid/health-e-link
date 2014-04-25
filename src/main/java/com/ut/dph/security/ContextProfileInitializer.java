/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.dph.security;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class ContextProfileInitializer implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {

    public void initialize(ConfigurableWebApplicationContext ctx) {
        ConfigurableEnvironment environment = ctx.getEnvironment();
        
        String hostname = null;
        String profiles = "local";
        
        try {
            hostname = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ContextProfileInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if("10.202.52.54".equals(hostname)) {
            profiles = "orion-test";
        } else if("10.64.61.129".equals(hostname)) {
            profiles = "orion-dev";
        }
                
        environment.setActiveProfiles(profiles);
    }
}
