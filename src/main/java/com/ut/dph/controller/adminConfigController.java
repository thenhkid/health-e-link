package com.ut.dph.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import com.ut.dph.model.configuration;
import com.ut.dph.service.configurationManager;

@Controller
@RequestMapping("/administrator/configurations")

public class adminConfigController {
	
	@Autowired 
	private configurationManager configurationmanager;
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView listConfigurations(@RequestParam(value="page", required=false) Integer page,HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		
		if(page == null){
	        page = 0;
	    }
 
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/listConfigurations");
        
        List<configuration> configurations = configurationmanager.getConfigurations(page,5);
        mav.addObject("configurationList", configurations);		
        
        return mav;
 
	}

}
