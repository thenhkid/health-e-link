package com.ut.dph.security;

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


public class CustomAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
    private userManager usermanager;
	 
	 @Override
	 public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
	      String userTargetUrl = "/profile";
	      String adminTargetUrl = "/administrator";
	      Set <String>roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
	      
	      usermanager.setLastLogin(authentication.getName());
	      
	      if (roles.contains("ROLE_ADMIN")) {
	         getRedirectStrategy().sendRedirect(request, response, adminTargetUrl);
	      } else if (roles.contains("ROLE_USER")) {
	         getRedirectStrategy().sendRedirect(request, response, userTargetUrl);
	      } else {
	         super.onAuthenticationSuccess(request, response, authentication);
	         return;
	      }
	   }
	}

