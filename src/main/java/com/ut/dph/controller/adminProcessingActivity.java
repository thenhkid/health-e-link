/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.controller;

import com.ut.dph.model.Organization;
import com.ut.dph.model.batchUploads;
import com.ut.dph.model.configuration;
import com.ut.dph.model.custom.searchParameters;
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.organizationManager;
import com.ut.dph.service.sysAdminManager;
import com.ut.dph.service.transactionInManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author chadmccue
 */

@Controller
@RequestMapping("/administrator/processing-activity")
public class adminProcessingActivity {
    
    @Autowired
    private transactionInManager transactionInManager;
    
    @Autowired
    private sysAdminManager sysAdminManager;
    
    @Autowired
    private organizationManager organizationmanager;
    
    @Autowired
    private configurationTransportManager configurationTransportManager;
    
    /**
     * The private maxResults variable will hold the number of results to show per list page.
     */
    private static int maxResults = 30;
    
    /**
     * The '/inbound' GET request will serve up the existing list of generated referrals and feedback reports
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return	The configuration page list
     *
     * @Objects	(1) An object containing all the found batches
     *
     * @throws Exception
     */
    @RequestMapping(value = "/inbound", method = RequestMethod.GET)
    public ModelAndView listInBoundBatches(HttpSession session) throws Exception {
        
        int page = 1;
        
        Date fromDate = getMonthDate("START");
        Date toDate = getMonthDate("END");
        String searchTerm = "";
        
        /* Retrieve search parameters from session */
        searchParameters searchParameters = (searchParameters)session.getAttribute("searchParameters");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/processing-activity/inbound");
        
        if("".equals(searchParameters.getsection()) || !"inbound".equals(searchParameters.getsection())) {
            searchParameters.setfromDate(fromDate);
            searchParameters.settoDate(toDate);
            searchParameters.setpage(1);
            searchParameters.setsection("inbound");
            searchParameters.setsearchTerm("");
        }
        else {
            fromDate = searchParameters.getfromDate();
            toDate = searchParameters.gettoDate();
            page = searchParameters.getpage();
            searchTerm = searchParameters.getsearchTerm();
        }
            
        mav.addObject("fromDate", fromDate);
        mav.addObject("toDate", toDate);
        
        
        /* Get all inbound transactions */
        try {
            /* Need to get a list of all uploaded batches */
            Integer totaluploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate, "", 1, 0).size();
            List<batchUploads> uploadedBatches = transactionInManager.getAllUploadedBatches(fromDate, toDate, "", 1, maxResults);
            
            List<Integer> statusIds = new ArrayList();

            if(!uploadedBatches.isEmpty()) {
                for(batchUploads batch : uploadedBatches) {
                    batch.settotalTransactions(transactionInManager.getRecordCounts(batch.getId(), statusIds, false, false));

                    lu_ProcessStatus processStatus = sysAdminManager.getProcessStatusById(batch.getstatusId());
                    batch.setstatusValue(processStatus.getDisplayCode());

                    Organization orgDetails = organizationmanager.getOrganizationById(batch.getOrgId());
                    batch.setorgName(orgDetails.getOrgName());
                    
                    batch.settransportMethod(configurationTransportManager.getTransportMethodById(batch.gettransportMethodId()));

                }
            }
            

           mav.addObject("batches", uploadedBatches);
           
           Integer totalPages = (int)Math.ceil((double)totaluploadedBatches / maxResults);
           mav.addObject("totalPages", totalPages);
        }
        catch (Exception e) {
            throw new Exception("Error occurred viewing the all uploaded batches.",e);
        }
        
        return mav;
        
    }
    
    /**
    * @param filter 
    * START for start date of month e.g.  Nov 01, 2013
    * END for end date of month e.g.  Nov 30, 2013
    * @return
    */
   public Date getMonthDate(String filter){
       
       String MM_DD_YYYY = "yyyy-mm-dd";
       SimpleDateFormat sdf = new SimpleDateFormat(MM_DD_YYYY);
       sdf.setTimeZone(TimeZone.getTimeZone("EST"));
       sdf.format(GregorianCalendar.getInstance().getTime());

       Calendar cal =  GregorianCalendar.getInstance();
       int date = cal.getActualMinimum(Calendar.DATE);
       if("END".equalsIgnoreCase(filter)){
           date = cal.getActualMaximum(Calendar.DATE);
           cal.set(Calendar.DATE, date);
           cal.set(Calendar.HOUR_OF_DAY, 23);
           cal.set(Calendar.MINUTE, 59);
           cal.set(Calendar.SECOND, 59);
           cal.set(Calendar.MILLISECOND, 0);
       }
       else {
           cal.set(Calendar.DATE, date);
           cal.set(Calendar.HOUR_OF_DAY, 0);
           cal.set(Calendar.MINUTE, 0);
           cal.set(Calendar.SECOND, 0);
           cal.set(Calendar.MILLISECOND, 0);
       }
       
       
       return cal.getTime();
   }
    
}
