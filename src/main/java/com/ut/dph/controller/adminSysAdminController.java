package com.ut.dph.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import com.ut.dph.service.sysAdminManager;
import com.ut.dph.model.custom.LookUpTable;
import com.ut.dph.model.custom.TableData;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        mav.setViewName("/administrator/sysadmin/tableList");
        
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
        
        boolean suceeded = sysAdminManager.deleteDataItem(tableInfo.getUtTableName(), dataId);
        String returnMessage = "deleted";
        
        if (!suceeded) {
        	returnMessage = "notDeleted";
        }
        //This variable will be used to display the message on the details form
		redirectAttr.addFlashAttribute("savedStatus", returnMessage);	
		
		ModelAndView mav = new ModelAndView(new RedirectView("../" + urlId));
		return mav;	
     
	}	
	
	
	/**
	 *  The '/{urlId}/data.create' GET request will be used to create a new data for selected table
	 *  
	 */
	@RequestMapping(value="/std/data/{urlId}/dataItem.create", method = RequestMethod.GET)
	public ModelAndView newDataForm(@PathVariable String urlId) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/sysadmin/std/details");	
		
		LookUpTable tableInfo = sysAdminManager.getTableInfo(urlId);
		//create a table data
		TableData tableData = new TableData();
		tableData.setUrlId(tableInfo.getUrlId());
		tableData.setId(0);
		mav.addObject("tableDataDetails",tableData);
		mav.addObject("tableInfo",tableInfo);
		return mav;
	}
	
	/**
	 * The '/{urlId}/data.create' POST request will handle submitting the new provider.
	 * 
	 * @param tableDataForm		The object containing the tableData form fields
	 * @param result			The validation result
	 * @param redirectAttr		The variable that will hold values that can be read after the redirect
	 * @param action			The variable that holds which button was pressed
	 * 
	 * @return					Will return the table's data page on "Save & Close"
	 * 							Will return the data details page on "Save"
	 * 							Will return the data create page on error
	 * 
	 * @Objects					(1) The object containing all the information for the new data item
	 * 							(2) We will extract table from web address
	 * @throws Exception
	 */
	@RequestMapping(value="/std/data/{urlId}/dataItem.create", method = RequestMethod.POST)
	public ModelAndView createProvider(@Valid @ModelAttribute(value="tableDataDetails") TableData tableData, 
			BindingResult result, RedirectAttributes redirectAttr,
			@RequestParam String action, @PathVariable String urlId) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/administrator/sysadmin/std/details");
		return mav;
		
		/** check for error 
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/administrator/sysadmin/std/details");
			return mav;
		}
		
		//Create the provider
		Integer dataId = sysAdminManager.createTableDate(tableData);
	
		//If the "Save" button was pressed 
		if(action.equals("save")) {
			//This variable will be used to display the message on the details form
			redirectAttr.addFlashAttribute("savedStatus", "created");
			ModelAndView mav = new ModelAndView(new RedirectView("provider."+providerdetails.getFirstName()+providerdetails.getLastName()+"?i="+providerId));
			return mav;
		}
		//If the "Save & Close" button was pressed.
		else {
			//This variable will be used to display the message on the details form
			redirectAttr.addFlashAttribute("savedStatus", "created");
			
			ModelAndView mav = new ModelAndView(new RedirectView("providers"));
			return mav;			
		}
		**/
	
	}
		
	
}

