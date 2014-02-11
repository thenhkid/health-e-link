package com.ut.dph.security;

import com.ut.dph.model.Organization;
import com.ut.dph.model.User;
import com.ut.dph.model.userAccess;
import com.ut.dph.service.organizationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;

import java.io.IOException;
import java.util.Set;

import com.ut.dph.service.userManager;
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

        usermanager.setLastLogin(authentication.getName());

        if (roles.contains("ROLE_ADMIN")) {
            getRedirectStrategy().sendRedirect(request, response, adminTargetUrl);
        } else if (roles.contains("ROLE_USER")) {
            /* Need to get the userId */
            User userDetails = usermanager.getUserByUserName(authentication.getName());
            
            Organization orgDetails = organizationManager.getOrganizationById(userDetails.getOrgId());
            
            userDetails.setdateOrgWasCreated(orgDetails.getDateCreated());
            
            /* Need to set some session variables to hold information about the user */
            List<userAccess> userAccess = usermanager.getuserSections(userDetails.getId());
            
            HttpSession session = request.getSession();
            session.setAttribute("userAccess" , userAccess);
            
            /* Need to store the user object in session */
            session.setAttribute("userDetails", userDetails);
            
            
            getRedirectStrategy().sendRedirect(request, response, userTargetUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
    }
}
