package com.ut.dph.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ut.dph.service.sysAdminManager;
import com.ut.dph.model.custom.LookUpTable;
import com.ut.dph.model.custom.TableData;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@RequestMapping("/administrator/sysadmin")
public class adminSysAdminController {
	
	@Autowired 
	private sysAdminManager sysAdminManager;
	
	/**
	 * The private maxResults variable will hold the number of results to show per
	 * list page.
	 */
	private static int maxResults = 20;
	
	
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
         List <LookUpTable> tableList = sysAdminManager.getTableList(maxResults, page, "");
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
	 * 						(2) An object containing the found look up tables
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	public ModelAndView searchForLookUpTable(@RequestParam String searchTerm, @RequestParam(value="page", required=false) Integer page) 
			throws Exception {
		
		if(page == null){
	        page = 1;
	    }
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/list");
        List<LookUpTable> tableList = sysAdminManager.getTableList(maxResults, page, searchTerm);;
        mav.addObject("searchTerm",searchTerm);
        mav.addObject("tableList", tableList);		
        
        return mav;
 
	}

	/**
	 *  The '/std/data/{urlId}' GET request will serve up the date for a look up table
	 *  
	 * @param page			The page parameter will hold the page to view when pagination 
	 * 						is built.
	 * @param urlId			The urlId for the look up table - can't use dynamic table names in Hibernate, 
	 * 						and don't want to worry about sql injection and plain view of table name, 
	 * 						will do a look up.
	 * @param searchTerm	if a user want to look for a particular term, will be useful when we have a lot of medical services						
	 * 
	 * @return				list of TableData
	 * 
	 * @Objects				(1) An object containing all the found look up tables
	 * 				
	 * @throws Exception
	 */
	@RequestMapping(value="/std/data/{urlId}", method = RequestMethod.GET)
	public ModelAndView listDataInTable(@RequestParam(value="page", required=false) Integer page,  @PathVariable String urlId) throws Exception {
		
		if(page == null){
	        page = 1;
	    }
 
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/std/data");
        
        /**
         * we query data for look up table, this view returns all data from table, hence the search term will be %
         **/
         LookUpTable tableInfo = sysAdminManager.getTableInfo(urlId);
         List <TableData> dataList = sysAdminManager.getDataList(maxResults, page, tableInfo.getUtTableName(), "%");
         mav.addObject("dataList", dataList);
         mav.addObject("tableInfo", tableInfo);
               
        int totalDataRows = sysAdminManager.findTotalDataRows(tableInfo.getUtTableName());
        int totalPages = Math.round(totalDataRows/maxResults);
        mav.addObject("totalPages",totalPages);
        mav.addObject("currentPage",page);
     
        return mav;
 
	}
	
	/**
	 *  The '/std/data/{urlId}' Post request will serve up the date for a look up table
	 *  
	 * @param page			The page parameter will hold the page to view when pagination 
	 * 						is built.
	 * @param urlId			The urlId for the look up table - can't use dynamic table names in Hibernate, 
	 * 						and don't want to worry about sql injection and plain view of table name, 
	 * 						will do a look up.
	 * @param searchTerm	if a user want to look for a particular term, will be useful when we have a lot of medical services						
	 * 
	 * @return				list of TableData
	 * 
	 * @Objects				(1) An object containing all the found look up tables
	 * 				
	 * @throws Exception
	 */
	@RequestMapping(value="/std/data/{urlId}", method = RequestMethod.POST)
	public ModelAndView listSearchDataInTable(@RequestParam String searchTerm,@RequestParam(value="page", required=false) Integer page,  @PathVariable String urlId) throws Exception {
		
		if(page == null){
	        page = 1;
	    }
 
		ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/std/data");
        
        /**
         * we query data for look up table, this view returns all data from table, hence the search term will be %
         **/
         LookUpTable tableInfo = sysAdminManager.getTableInfo(urlId);
         List <TableData> dataList = sysAdminManager.getDataList(maxResults, page, tableInfo.getUtTableName(), searchTerm);
         mav.addObject("dataList", dataList);
         mav.addObject("tableInfo", tableInfo);
               
        int totalDataRows = sysAdminManager.findTotalDataRows(tableInfo.getUtTableName());
        int totalPages = Math.round(totalDataRows/maxResults);
        mav.addObject("totalPages",totalPages);
        mav.addObject("currentPage",page);
     
        return mav;
 
	}
	
	/**
	 *  The '/std/data/{urlId}/delete?i=' Post request will serve up the date for a look up table
	 *  
	 * @param page			The page parameter will hold the page to view when pagination 
	 * 						is built.
	 * @param urlId			The urlId for the look up table - can't use dynamic table names in Hibernate, 
	 * 						and don't want to worry about sql injection and plain view of table name, 
	 * 						will do a look up.
	 * @param i	if a user want to look for a particular term, will be useful when we have a lot of medical services						
	 * 
	 * @return				list of TableData
	 * 
	 * @Objects				(1) An object containing all the found look up tables
	 * 				
	 * @throws Exception
	 */
	@RequestMapping(value="/std/data/{urlId}/delete", method = RequestMethod.GET)
	public ModelAndView deleteDataInTable(@RequestParam(value="i", required=true) int dataId, 
			@PathVariable String urlId, RedirectAttributes redirectAttr) throws Exception {
		 
		LookUpTable tableInfo = sysAdminManager.getTableInfo(urlId);
        sysAdminManager.deleteDataItem(tableInfo.getUtTableName(), dataId);
        
        //This variable will be used to display the message on the details form
		redirectAttr.addFlashAttribute("savedStatus", "deleted");	
		//e.g. administrator/sysadmin/std/data/3540Gen6563ers/delete?i=1
		ModelAndView mav = new ModelAndView(new RedirectView("../" + urlId));
		return mav;	
     
	}	
	
	
	
}

