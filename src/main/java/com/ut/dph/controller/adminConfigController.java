package com.ut.dph.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

import com.ut.dph.model.configuration;
import com.ut.dph.service.configurationManager;

@Controller
@RequestMapping("/administrator/configurations")

public class adminConfigController {
	
	@Autowired 
	private configurationManager configurationmanager;
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView listConfigurations(@RequestParam(value="page", required=false) Integer page) throws Exception {
		
		if(page == null){
	        page = 0;
	    }
 
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/listConfigurations");
        
        List<configuration> configurations = configurationmanager.getConfigurations(page,5);
        mav.addObject("configurationList", configurations);		
        
        return mav;
 
	}
	
	@RequestMapping(value="/detail/{id}", method = RequestMethod.GET)
	public ModelAndView viewConfigurationDetail(@PathVariable Integer id) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/configurations/configurationDetails");
		
		configuration configDetails = configurationmanager.getConfigurationById(id);
		mav.addObject("id",id);
		mav.addObject("configuration",configDetails);
		
		return mav;
	
	}
	
	@RequestMapping(value="/detail/{id}", method = RequestMethod.POST)
    public ModelAndView saveConfiguration(@Valid configuration configuration, BindingResult result, RedirectAttributes redirectAttr,@RequestParam String action, @PathVariable Integer id) throws Exception {
		
		
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.addObject("id",id);
			mav.setViewName("/administrator/configurations/configurationDetails");
			
			return mav;
		}
		
		configurationmanager.saveConfiguration(configuration);
		redirectAttr.addFlashAttribute("savedStatus", "success");
		
		if(action.equals("save")) {
			ModelAndView mav = new ModelAndView();
			mav.addObject("id",id);
			mav.setViewName("/administrator/configurations/configurationDetails");
			
			return mav;
		}
		else {
			ModelAndView mav = new ModelAndView(new RedirectView("../list"));
			return mav;			
		}
	    
		
    }

}
