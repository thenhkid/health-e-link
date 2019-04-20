package com.ut.healthelink.security;

import com.ut.healthelink.model.Organization;
import com.ut.healthelink.model.User;
import com.ut.healthelink.model.UserActivity;
import com.ut.healthelink.model.custom.searchParameters;
import com.ut.healthelink.model.siteSections;
import com.ut.healthelink.model.userAccess;
import com.ut.healthelink.service.organizationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;

import java.io.IOException;
import java.util.Set;

import com.ut.healthelink.service.userManager;
import java.util.List;
import javax.servlet.http.HttpSession;

public class CustomAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private userManager usermanager;
    
    @Autowired
    private organizationManager organizationManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String userTargetUrl = "/profile";
        String adminTargetUrl = "/administrator";
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        
        //we do not log
        if (request.getParameter("username").equalsIgnoreCase(authentication.getName())) {
        	usermanager.setLastLogin(authentication.getName());
        }
        /** log the admin who logged in as user**/
        // System.out.println(request.getParameter("username"));
      //we log here 
        /* Need to get the userId */
        User userDetails = usermanager.getUserByUserName(authentication.getName());
        
        if (!request.getParameter("username").equalsIgnoreCase(authentication.getName())) {
	        try {
	            //log user activity
                    User userLogDetails = usermanager.getUserByUserName(request.getParameter("username"));
	            UserActivity ua = new UserActivity();
	            ua.setUserId(userLogDetails.getId());
	            ua.setFeatureId(0);
	            ua.setAccessMethod("POST");
	            ua.setPageAccess("/login");
	            ua.setActivity("Login As User");
	            ua.setActivityDesc("Login as user - " +  userDetails.getUsername() + ".  Id - " + userDetails.getId());
	            usermanager.insertUserLog(ua);
	        } catch (Exception ex) {
	            System.err.println("Login Handler = error logging user " + ex.getCause());
	            ex.printStackTrace();
	        }
        }
        if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_PROCESSINGADMIN") || roles.contains("ROLE_SYSTEMADMIN")) {
            
            HttpSession session = request.getSession();
             
            searchParameters searchParameters = new searchParameters();
            
            /* Need to store the search session object */
            session.setAttribute("searchParameters", searchParameters);
            
            /* Need to store the user object in session */
            session.setAttribute("userDetails", userDetails);
            
            getRedirectStrategy().sendRedirect(request, response, adminTargetUrl);
        } 
        
        else if (roles.contains("ROLE_USER")) {
            
            Organization orgDetails = organizationManager.getOrganizationById(userDetails.getOrgId());
            
            searchParameters searchParameters = new searchParameters();
            
            userDetails.setdateOrgWasCreated(orgDetails.getDateCreated());
            
            /* Need to set some session variables to hold information about the user */
            List<siteSections> userAllowedModules = usermanager.getuserAllowedModules(userDetails.getId());
            userDetails.setUserAllowedModules(userAllowedModules);
            
            HttpSession session = request.getSession();
            
            /* Need to store the user object in session */
            session.setAttribute("userDetails", userDetails);
            
            /* Check to see if header image is empty and if parent Id > 0 get the parent header image and logo */
            if((orgDetails.getHeaderLogo() == null || "".equals(orgDetails.getHeaderLogo())) && orgDetails.getParentId() > 0) {
                Organization parentOrgDetails = organizationManager.getOrganizationById(orgDetails.getParentId());
                
                if(!"".equals(parentOrgDetails.getHeaderLogo()) && parentOrgDetails.getHeaderLogo() != null) {
                    orgDetails.setHeaderLogo(parentOrgDetails.getHeaderLogo());
                    orgDetails.setHeaderImageDirectory(parentOrgDetails.getcleanURL());
                }
                if(!"".equals(parentOrgDetails.getHeaderBackground()) && parentOrgDetails.getHeaderBackground() != null) {
                    orgDetails.setHeaderBackground(parentOrgDetails.getHeaderBackground());
                    orgDetails.setHeaderImageDirectory(parentOrgDetails.getcleanURL());
                }
            }
            else {
                orgDetails.setHeaderImageDirectory(orgDetails.getcleanURL());
            }
            
            /* Need to store the user company information in session */
            session.setAttribute("organizationDetails", orgDetails);
            
            /* Need to store the search session object */
            session.setAttribute("searchParameters", searchParameters);
            
            getRedirectStrategy().sendRedirect(request, response, userTargetUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
    }
}
