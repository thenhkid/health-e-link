package com.ut.dph.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class adminController {
	
	@RequestMapping(value="/administrator", method = RequestMethod.GET)
	public ModelAndView listConfigurations(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

			ModelAndView mav = new ModelAndView();
	        mav.setViewName("/administrator/menu");
	        return mav;
	}
    
	

}
