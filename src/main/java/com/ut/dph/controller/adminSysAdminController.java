package com.ut.dph.controller;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ut.dph.service.sysAdminManager;
import com.ut.dph.model.messageType;
import com.ut.dph.model.custom.LookUpTable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/administrator/sysadmin")
public class adminSysAdminController {
	
	@Autowired 
	private sysAdminManager sysAdminManager;
	
	/**
	 * The private maxResults variable will hold the number of results to show per
	 * list page.
	 */
	private static int maxResults = 10;
	
	
	/**
	 *  The '/list' GET request will serve up the existing list of lu_ tables in the system
	 *  
	 * @param page			The page parameter will hold the page to view when pagination 
	 * 						is built.
	 * @return				The list of look up tables
	 * 
	 * @Objects				(1) An object containing all the found look up tables
	 * 				
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView listLookUpTables(@RequestParam(value="page", required=false) Integer page) throws Exception {
		
		if(page == null){
	        page = 1;
	    }
 
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/list");
        
        /**
         * we query list of tables for display
         **/
         List <LookUpTable> tableList = sysAdminManager.getTableLits(maxResults, page);
        mav.addObject("tableList", tableList);
       
        //Return the total list of look up table
        int totalLookUpTables = sysAdminManager.findTotalLookUpTable();
        int totalPages = Math.round(totalLookUpTables/maxResults);
        mav.addObject("totalPages",totalPages);
        mav.addObject("currentPage",page);
       
        return mav;
 
	}

	
	/**
	 * The '/list' POST request will be used to search look up tables from the search form on the
	 * list page.
	 * 
	 * @param searchTerm	The searchTerm parameter will hold the string to search on
	 * @return				The look up table page list
	 * 
	 * @Objects				(1) An object will be returned holding the requested search term used to 
	 * 							populate the search box
	 * 						(3) An object containing the found look up tables
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	public ModelAndView searchForLookUpTable(@RequestParam String searchTerm) throws Exception {
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/list");
        
        List<LookUpTable> tableList = sysAdminManager.findLookUpTables(searchTerm);
        mav.addObject("searchTerm",searchTerm);
        mav.addObject("tableList", tableList);		
        
        return mav;
 
	}

}

