package com.ut.dph.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class mainController {
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		
		return new ModelAndView("login");
 
	}

	
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public ModelAndView loginerror(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
 
		ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        mav.addObject("error", "true");
        return mav;
 
	}
	
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		
		return new ModelAndView("login");
 
	}

	
	 @RequestMapping(value = "/", method = RequestMethod.GET)
	 public ModelAndView welcome(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		
		return new ModelAndView("home");
 
	}
	 

}
