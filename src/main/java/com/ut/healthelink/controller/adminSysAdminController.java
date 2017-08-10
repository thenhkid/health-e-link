package com.ut.healthelink.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import com.ut.healthelink.model.UserActivity;
import com.ut.healthelink.reference.TestCategoryList;
import com.ut.healthelink.reference.USStateList;
import com.ut.healthelink.reference.ProcessCategoryList;
import com.ut.healthelink.service.configurationManager;
import com.ut.healthelink.service.sysAdminManager;
import com.ut.healthelink.service.userManager;
import com.ut.healthelink.model.Macros;
import com.ut.healthelink.model.User;
import com.ut.healthelink.model.custom.LogoInfo;
import com.ut.healthelink.model.custom.LookUpTable;
import com.ut.healthelink.model.custom.TableData;
import com.ut.healthelink.model.lutables.lu_Counties;
import com.ut.healthelink.model.lutables.lu_GeneralHealthStatuses;
import com.ut.healthelink.model.lutables.lu_GeneralHealths;
import com.ut.healthelink.model.lutables.lu_Immunizations;
import com.ut.healthelink.model.lutables.lu_Manufacturers;
import com.ut.healthelink.model.lutables.lu_MedicalConditions;
import com.ut.healthelink.model.lutables.lu_Medications;
import com.ut.healthelink.model.lutables.lu_Procedures;
import com.ut.healthelink.model.lutables.lu_ProcessStatus;
import com.ut.healthelink.model.lutables.lu_Tests;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/administrator/sysadmin")
public class adminSysAdminController {

    @Autowired
    private sysAdminManager sysAdminManager;
    
    @Autowired
    private userManager usermanager;


    @Autowired
    private configurationManager configurationmanager;

    @Autowired
    private ServletContext servletContext;

    /**
     * The private maxResults variable will hold the number of results to show per list page.
     */
    private static int maxResults = 20;
    private static int nonPagingMax = 999999;
    private static int startPage = 1;

    /**
     * This shows a dashboard with info for sysadmin components.
	 * *
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView dashboard(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/dashboard");
        /**
         * set totals*
         */
        int totalLookUpTables = sysAdminManager.findTotalLookUpTable();
        Long totalMacroRows = sysAdminManager.findTotalMacroRows();
        Long totalHL7Entries = sysAdminManager.findtotalHL7Entries();
        Long totalUsers = sysAdminManager.findTotalUsers();
        
        mav.addObject("totalLookUpTables", totalLookUpTables);
        mav.addObject("totalMacroRows", totalMacroRows);
        mav.addObject("totalHL7Entries", totalHL7Entries);
        mav.addObject("totalUsers", totalUsers);
        

        return mav;
    }

    /**
     * MACROS 
	 * *
     */
    @RequestMapping(value = "/macros", method = RequestMethod.GET)
    public ModelAndView listMacros() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/macros");

        //Return a list of available macros
        List<Macros> macroList = sysAdminManager.getMarcoList("%");
        mav.addObject("macroList", macroList);

        return mav;
    }

    @RequestMapping(value = "/macros/delete", method = RequestMethod.GET)
    public ModelAndView deleteMacro(@RequestParam(value = "i", required = true) int macroId,
            RedirectAttributes redirectAttr) throws Exception {

        boolean suceeded = sysAdminManager.deleteMacro(macroId);
        String returnMessage = "deleted";

        if (!suceeded) {
            returnMessage = "notDeleted";
        }
        //This variable will be used to display the message on the details form
        redirectAttr.addFlashAttribute("savedStatus", returnMessage);

        ModelAndView mav = new ModelAndView(new RedirectView("../macros?msg=" + returnMessage));
        return mav;
    }

    /**
     * The '/{urlId}/data.create' GET request will be used to create a new data for selected table
     *
     */
    @RequestMapping(value = "/macros/create", method = RequestMethod.GET)
    public ModelAndView newMacroForm() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/macro/details");

        //create a macro
        Macros macro = new Macros();
        macro.setId(0);
        mav.addObject("macroDetails", macro);
        mav.addObject("btnValue", "Create");
        return mav;
    }

    @RequestMapping(value = "/macros/create", method = RequestMethod.POST)
    public ModelAndView createMacro(
            @Valid @ModelAttribute(value = "macroDetails") Macros macroDetails,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/macro/details");
        /**
         * check for error *
         */
        if (result.hasErrors()) {
            mav.addObject("macroDetails", macroDetails);
            mav.addObject("btnValue", "Create");
            return mav;
        }
        //now we save
        sysAdminManager.createMacro(macroDetails);
        mav.addObject("success", "macroCreated");
        mav.addObject("btnValue", "Update");
        return mav;
    }

    /**
     * The '/macros/view' GET request will be used to create a new data for selected table
     *
     */
    @RequestMapping(value = "/macros/view", method = RequestMethod.GET)
    public ModelAndView viewMacroDetails(@RequestParam(value = "i", required = false) Integer i) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/macro/details");
        //get macro info here
        Macros macroDetails = configurationmanager.getMacroById(i);
        mav.addObject("macroDetails", macroDetails);
        mav.addObject("btnValue", "Update");
        return mav;
    }

    /**
     * UPDATE macros
	 * *
     */
    @RequestMapping(value = "/macros/update", method = RequestMethod.POST)
    public ModelAndView updateMacro(
            @Valid @ModelAttribute(value = "macroDetails") Macros macroDetails,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/macro/details");

        if (result.hasErrors()) {
            mav.addObject("macroDetails", macroDetails);
            mav.addObject("btnValue", "Update");
            return mav;
        }

        //get macro info here
        boolean updated = sysAdminManager.updateMacro(macroDetails);

        if (updated) {
            mav.addObject("success", "macroUpdated");
        } else {
            mav.addObject("success", "Error!");
        }
        mav.addObject("macroDetails", macroDetails);
        mav.addObject("btnValue", "Update");
        return mav;
    }

    /**
     * END MACROS
	 * *
     */
    /**
     * The '/list' GET request will serve up the existing list of lu_ tables in the system
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return	The list of look up tables
     *
     * @Objects	(1) An object containing all the found look up tables
     *
     * @throws Exception
     */
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public ModelAndView listLookUpTables() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/tableList");

        /**
         * we query list of tables for display
         *
         */
        List<LookUpTable> tableList = sysAdminManager.getTableList("%");
        mav.addObject("tableList", tableList);


        return mav;

    }

    /**
     * The '/data/std/{urlId}' GET request will serve up the data for a standardize look up table
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @param urlId	The urlId for the look up table - can't use dynamic table names in Hibernate, and don't want to worry about sql injection and plain view of table name, will do a look up.
     * @param searchTerm	if a user want to look for a particular term, will be useful when we have a lot of medical services
     *
     * @return	list of TableData
     *
     * @Objects	(1) An object containing all the found look up tables
     *
     * @throws Exception
     */
    @RequestMapping(value = "/data/std/{urlId}", method = RequestMethod.GET)
    public ModelAndView listTableData(@PathVariable String urlId) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std");

        /**
         * we query data for look up table, this view returns all data from table, hence the search term will be %
         *
         */
        LookUpTable tableInfo = sysAdminManager.getTableInfo(urlId);
        List<TableData> dataList = sysAdminManager.getDataList(tableInfo.getUtTableName(), "%");
        mav.addObject("dataList", dataList);
        mav.addObject("tableInfo", tableInfo);

        mav.addObject("goToURL", tableInfo.getUrlId());
        mav.addObject("urlIdInfo", tableInfo.getUrlId());
        return mav;

    }

    /**
     * The '/data/std/{urlId}/delete?i=' Post request will serve up the date for a look up table
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @param urlId	The urlId for the look up table - can't use dynamic table names in Hibernate, and don't want to worry about sql injection and plain view of table name, will do a look up.
     * @param i	if a user want to look for a particular term, will be useful when we have a lot of medical services
     *
     * @return	list of TableData
     *
     * @Objects	(1) An object containing all the found look up tables
     *
     * @throws Exception
     */
    @RequestMapping(value = "/data/std/{urlId}/delete", method = RequestMethod.GET)
    public ModelAndView deleteTableData(@RequestParam(value = "i", required = true) int dataId,
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
     * The '/{urlId}/data.create' GET request will be used to create a new data for selected table
     *
     */
    @RequestMapping(value = "/data/std/{urlId}/create", method = RequestMethod.GET)
    public ModelAndView newTableDataForm(@PathVariable String urlId) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        LookUpTable tableInfo = sysAdminManager.getTableInfo(urlId);
        //create a table data
        TableData tableData = new TableData();
        tableData.setId(0);
        tableData.setUrlId(urlId);
        mav.addObject("stdForm", "stdForm");
        mav.addObject("tableDataDetails", tableData);
        mav.addObject("tableInfo", tableInfo);
        mav.addObject("objectType", "tableData");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "Create");
        mav.addObject("submitBtnValue", "Create");
        return mav;
    }

    /**
     * The '/{urlId}/create' POST request will handle submitting the new provider.
     *
     * @param tableDataForm	The object containing the tableData form fields
     * @param result	The validation result
     * @param redirectAttr	The variable that will hold values that can be read after the redirect
     * @Objects	(1) The object containing all the information for the new data item (2) We will extract table from web address
     * @throws Exception
     */
    @RequestMapping(value = "/data/std/create", method = RequestMethod.POST)
    public ModelAndView createTableData(
            @Valid @ModelAttribute(value = "tableDataDetails") TableData tableData,
            BindingResult result) throws Exception {

        LookUpTable tableInfo = sysAdminManager.getTableInfo(tableData.getUrlId());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "tableData");
        mav.addObject("formId", "tabledataform");
        /**
         * check for error *
         */
        if (result.hasErrors()) {
            mav.addObject("stdForm", "stdForm");
            mav.addObject("tableInfo", tableInfo);
            mav.addObject("btnValue", "Create");
            mav.addObject("submitBtnValue", "Create");
            return mav;
        }

        //now we save
        sysAdminManager.createTableDataHibernate(tableData, tableInfo.getUtTableName());
        mav.addObject("success", "dataCreated");
        mav.addObject("btnValue", "Update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    /**
     * The '/data/std/{urlId}/tableData?i=' GET request will be used to create a new data for selected table
     *
     */
    @RequestMapping(value = "/data/std/{urlId}/tableData", method = RequestMethod.GET)
    public ModelAndView viewTableData(@PathVariable String urlId,
            @RequestParam(value = "i", required = false) Integer i) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        LookUpTable tableInfo = sysAdminManager.getTableInfo(urlId);
        TableData tableData = sysAdminManager.getTableData(i, tableInfo.getUtTableName());
        tableData.setUrlId(urlId);
        mav.addObject("tableDataDetails", tableData);
        mav.addObject("tableInfo", tableInfo);
        mav.addObject("objectType", "tableData");
        mav.addObject("stdForm", "stdForm");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "Update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    /**
     * The '/data/std/update' POST request will be used to update a look up data item
     *
     */
    @RequestMapping(value = "/data/std/update", method = RequestMethod.POST)
    public ModelAndView updateTableData(
            @Valid @ModelAttribute(value = "tableDataDetails") TableData tableData,
            BindingResult result)
            throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "tableData");
        mav.addObject("formId", "tabledataform");
        mav.addObject("submitBtnValue", "Update");
        mav.addObject("btnValue", "Update");

        LookUpTable tableInfo = sysAdminManager.getTableInfo(tableData.getUrlId());

        /**
         * check for error *
         */
        if (result.hasErrors()) {
            mav.addObject("tableInfo", tableInfo);
            mav.addObject("stdForm", "stdForm");
            return mav;
        }

        // now we update
        boolean updated = sysAdminManager.updateTableData(tableData, tableInfo.getUtTableName());

        // This variable will be used to display the message on the details
        if (updated) {
            mav.addObject("success", "dataUpdated");
        } else {
            mav.addObject("success", "- There is an error.");
        }

        return mav;
    }

    /**
     * here we have the views for the look up tables that do not have the standard 7 columns, these have models in UT
     *
     */
    @RequestMapping(value = "/data/nstd/{urlId}", method = RequestMethod.GET)
    public ModelAndView listTableDataNStd(@PathVariable String urlId) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std");

        /**
         * we query data for look up table, this view returns all data from table, hence the search term will be %
         *
         */
        LookUpTable tableInfo = sysAdminManager.getTableInfo(urlId);
        List<TableData> dataList = sysAdminManager.getDataList(tableInfo.getUtTableName(), "%");
        mav.addObject("dataList", dataList);
        mav.addObject("tableInfo", tableInfo);

        mav.addObject("goToURL", tableInfo.getUtTableName());
        mav.addObject("urlIdInfo", tableInfo.getUrlId());
        return mav;
    }

    @RequestMapping(value = "/data/nstd/{urlId}/delete", method = RequestMethod.GET)
    public ModelAndView deleteCountyData(@RequestParam(value = "i", required = true) int dataId,
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

    @RequestMapping(value = "/data/nstd/lu_Counties/create", method = RequestMethod.GET)
    public ModelAndView newCountyForm() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        //Get a list of states
        USStateList stateList = new USStateList();

        //create a lu_Counties
        lu_Counties luc = new lu_Counties();
        luc.setId(0);
        mav.addObject("tableDataDetails", luc);
        mav.addObject("objectType", "lu_Counties");
        mav.addObject("formId", "tabledataform");
        mav.addObject("stateList", stateList.getStates());
        mav.addObject("btnValue", "lu_counties/create");
        mav.addObject("submitBtnValue", "Create");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_counties/create", method = RequestMethod.POST)
    public ModelAndView createCounties(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_Counties luc,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_Counties");
        mav.addObject("formId", "tabledataform");
        /**
         * check for error *
         */
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_counties/create");
            //Get a list of states
            USStateList stateList = new USStateList();
            //Get the object that will hold the states
            mav.addObject("stateList", stateList.getStates());
            mav.addObject("submitBtnValue", "Create");
            return mav;
        }

        //now we save
        sysAdminManager.createCounty(luc);
        mav.addObject("success", "dataCreated");
        mav.addObject("btnValue", "lu_counties/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    /**
     * view a county's data	data/nstd/lu_Counties/tableData*
     */
    @RequestMapping(value = "/data/nstd/lu_Counties/tableData", method = RequestMethod.GET)
    public ModelAndView viewCountyData(@RequestParam(value = "i", required = false) Integer i)
            throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        USStateList stateList = new USStateList();

        lu_Counties luc = sysAdminManager.getCountyById(i);
        mav.addObject("tableDataDetails", luc);
        //Get the object that will hold the states
        mav.addObject("stateList", stateList.getStates());
        mav.addObject("objectType", "lu_Counties");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_counties/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_counties/update", method = RequestMethod.POST)
    public ModelAndView updateCounty(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_Counties luc,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_Counties");
        mav.addObject("formId", "tabledataform");
        /**
         * check for error *
         */
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_counties/update");
            //Get a list of states
            USStateList stateList = new USStateList();
            //Get the object that will hold the states
            mav.addObject("stateList", stateList.getStates());
            mav.addObject("submitBtnValue", "Update");
            return mav;
        }

        //now we save
        sysAdminManager.updateCounty(luc);
        mav.addObject("success", "dataUpdated");
        mav.addObject("btnValue", "lu_counties/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    /**
     * general health *
     */
    @RequestMapping(value = "/data/nstd/lu_GeneralHealths/create", method = RequestMethod.GET)
    public ModelAndView newGeneralHealthForm() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        //create the object
        lu_GeneralHealths lu = new lu_GeneralHealths();
        lu.setId(0);
        mav.addObject("tableDataDetails", lu);
        mav.addObject("objectType", "lu_GeneralHealths");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_generalhealths/create");
        mav.addObject("submitBtnValue", "Create");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_generalhealths/create", method = RequestMethod.POST)
    public ModelAndView createGeneralHealth(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_GeneralHealths lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_GeneralHealths");
        mav.addObject("formId", "tabledataform");
        // check for error
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_generalhealths/create");
            mav.addObject("submitBtnValue", "Create");
            return mav;
        }

        //now we save
        sysAdminManager.createGeneralHealth(lu);
        mav.addObject("success", "dataCreated");
        mav.addObject("btnValue", "lu_generalhealths/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_GeneralHealths/tableData", method = RequestMethod.GET)
    public ModelAndView viewGeneralHealth(@RequestParam(value = "i", required = false) Integer i)
            throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        lu_GeneralHealths lu = sysAdminManager.getGeneralHealthById(i);
        mav.addObject("tableDataDetails", lu);

        mav.addObject("objectType", "lu_GeneralHealths");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_generalhealths/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_generalhealths/update", method = RequestMethod.POST)
    public ModelAndView updateGeneralHealth(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_GeneralHealths lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_GeneralHealths");
        mav.addObject("formId", "tabledataform");
        /**
         * check for error *
         */
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_generalhealths/update");
            mav.addObject("submitBtnValue", "Update");
            return mav;
        }

        //now we save
        sysAdminManager.updateGeneralHealth(lu);
        mav.addObject("success", "dataUpdated");
        mav.addObject("btnValue", "lu_generalhealths/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    /**
     * end of general health *
     */

    /**
     * start of general health statuses *
     */
    // this method creates the form for the object
    @RequestMapping(value = "/data/nstd/lu_GeneralHealthStatuses/create", method = RequestMethod.GET)
    public ModelAndView newGeneralHealthStatusForm() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        //create the object
        lu_GeneralHealthStatuses lu = new lu_GeneralHealthStatuses();
        lu.setId(0);
        mav.addObject("tableDataDetails", lu);
        mav.addObject("objectType", "lu_GeneralHealthStatuses");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_GeneralHealthStatuses/create");
        mav.addObject("submitBtnValue", "Create");
        return mav;
    }

    //this method saves the object
    @RequestMapping(value = "/data/nstd/lu_generalhealthstatuses/create", method = RequestMethod.POST)
    public ModelAndView createGeneralHealthStatuses(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_GeneralHealthStatuses lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_GeneralHealthStatuses");
        mav.addObject("formId", "tabledataform");
        // check for error
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_GeneralHealthStatuses/create");
            mav.addObject("submitBtnValue", "Create");
            return mav;
        }

        //now we save
        sysAdminManager.createGeneralHealthStatus(lu);
        mav.addObject("success", "dataCreated");
        mav.addObject("btnValue", "lu_GeneralHealthStatuses/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    // this method queries and displays the object data
    @RequestMapping(value = "/data/nstd/lu_GeneralHealthStatuses/tableData", method = RequestMethod.GET)
    public ModelAndView viewGeneralHealthStatus(@RequestParam(value = "i", required = false) Integer i)
            throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        lu_GeneralHealthStatuses lu = sysAdminManager.getGeneralHealthStatusById(i);
        mav.addObject("tableDataDetails", lu);

        mav.addObject("objectType", "lu_GeneralHealthStatuses");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_GeneralHealthStatuses/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    //this method updates the object
    @RequestMapping(value = "/data/nstd/lu_generalhealthstatuses/update", method = RequestMethod.POST)
    public ModelAndView updateGeneralHealthStatus(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_GeneralHealthStatuses lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_GeneralHealthStatuses");
        mav.addObject("formId", "tabledataform");
        /**
         * check for error *
         */
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_GeneralHealthStatuses/update");
            mav.addObject("submitBtnValue", "Update");
            return mav;
        }

        //now we save
        sysAdminManager.updateGeneralHealthStatus(lu);
        mav.addObject("success", "dataUpdated");
        mav.addObject("btnValue", "lu_GeneralHealthStatuses/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    /**
     * end of general health status *
     */
    /**
     * start of Immunizations *
     */
    // this method creates the form for the object
    @RequestMapping(value = "/data/nstd/lu_Immunizations/create", method = RequestMethod.GET)
    public ModelAndView newImmunizationsForm() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        //create the object
        lu_Immunizations lu = new lu_Immunizations();
        lu.setId(0);
        mav.addObject("tableDataDetails", lu);
        mav.addObject("objectType", "lu_Immunizations");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_Immunizations/create");
        mav.addObject("submitBtnValue", "Create");
        return mav;
    }

    //this method saves the object
    @RequestMapping(value = "/data/nstd/lu_immunizations/create", method = RequestMethod.POST)
    public ModelAndView createImmunization(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_Immunizations lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_Immunizations");
        mav.addObject("formId", "tabledataform");
        // check for error
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_Immunizations/create");
            mav.addObject("submitBtnValue", "Create");
            return mav;
        }

        //now we save
        sysAdminManager.createImmunization(lu);
        mav.addObject("success", "dataCreated");
        mav.addObject("btnValue", "lu_Immunizations/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    // this method queries and displays the object data
    @RequestMapping(value = "/data/nstd/lu_Immunizations/tableData", method = RequestMethod.GET)
    public ModelAndView viewImmunization(@RequestParam(value = "i", required = false) Integer i)
            throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        lu_Immunizations lu = sysAdminManager.getImmunizationById(i);
        mav.addObject("tableDataDetails", lu);
        mav.addObject("objectType", "lu_Immunizations");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_Immunizations/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    //this method updates the object
    @RequestMapping(value = "/data/nstd/lu_immunizations/update", method = RequestMethod.POST)
    public ModelAndView updateImmunization(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_Immunizations lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_Immunizations");
        mav.addObject("formId", "tabledataform");
        /**
         * check for error *
         */
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_Immunizations/update");
            mav.addObject("submitBtnValue", "Update");
            return mav;
        }

        //now we save
        sysAdminManager.updateImmunization(lu);
        mav.addObject("success", "dataUpdated");
        mav.addObject("btnValue", "lu_immunizations/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    /**
     * end of Immunizations *
     */
    /**
     * start of Manufacturers *
     */
    // this method creates the form for the object
    @RequestMapping(value = "/data/nstd/lu_Manufacturers/create", method = RequestMethod.GET)
    public ModelAndView newManufacturerForm() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        //create the object
        lu_Manufacturers lu = new lu_Manufacturers();
        lu.setId(0);
        //Get a list of states
        USStateList stateList = new USStateList();
        //Get the object that will hold the states
        mav.addObject("stateList", stateList.getStates());
        mav.addObject("tableDataDetails", lu);
        mav.addObject("objectType", "lu_Manufacturers");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_Manufacturers/create");
        mav.addObject("submitBtnValue", "Create");
        return mav;
    }

    //this method saves the object
    @RequestMapping(value = "/data/nstd/lu_manufacturers/create", method = RequestMethod.POST)
    public ModelAndView createManufacturer(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_Manufacturers lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_Manufacturers");
        mav.addObject("formId", "tabledataform");
        // check for error
        if (result.hasErrors()) {
            //Get a list of states
            USStateList stateList = new USStateList();
            //Get the object that will hold the states
            mav.addObject("stateList", stateList.getStates());
            mav.addObject("btnValue", "lu_Manufacturers/create");
            mav.addObject("submitBtnValue", "Create");
            return mav;
        }

        //now we save
        sysAdminManager.createManufacturer(lu);
        mav.addObject("success", "dataCreated");
        mav.addObject("btnValue", "lu_Manufacturers/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    // this method queries and displays the object data
    @RequestMapping(value = "/data/nstd/lu_Manufacturers/tableData", method = RequestMethod.GET)
    public ModelAndView viewManufacturer(@RequestParam(value = "i", required = false) Integer i)
            throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        lu_Manufacturers lu = sysAdminManager.getManufacturerById(i);
        mav.addObject("tableDataDetails", lu);
        mav.addObject("objectType", "lu_Manufacturers");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_Manufacturers/update");
        mav.addObject("submitBtnValue", "Update");
        //Get a list of states
        USStateList stateList = new USStateList();
        //Get the object that will hold the states
        mav.addObject("stateList", stateList.getStates());
        return mav;
    }

    //this method updates the object
    @RequestMapping(value = "/data/nstd/lu_manufacturers/update", method = RequestMethod.POST)
    public ModelAndView updateManufacturer(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_Manufacturers lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_Manufacturers");
        mav.addObject("formId", "tabledataform");
        /**
         * check for error *
         */
        if (result.hasErrors()) {
            //Get a list of states
            USStateList stateList = new USStateList();
            //Get the object that will hold the states
            mav.addObject("stateList", stateList.getStates());
            mav.addObject("btnValue", "lu_Manufacturers/update");
            mav.addObject("submitBtnValue", "Update");
            return mav;
        }

        //now we save
        sysAdminManager.updateManufacturer(lu);
        mav.addObject("success", "dataUpdated");
        mav.addObject("btnValue", "lu_Manufacturers/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    /**
     * end of Manufacturers *
     */
    /**
     * medical conditions *
     */
    @RequestMapping(value = "/data/nstd/lu_MedicalConditions/create", method = RequestMethod.GET)
    public ModelAndView newMedicalConditionForm() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        //create the object
        lu_MedicalConditions lu = new lu_MedicalConditions();
        lu.setId(0);
        mav.addObject("tableDataDetails", lu);
        mav.addObject("objectType", "lu_MedicalConditions");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_MedicalConditions/create");
        mav.addObject("submitBtnValue", "Create");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_medicalconditions/create", method = RequestMethod.POST)
    public ModelAndView createMedicalConditon(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_MedicalConditions lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_MedicalConditions");
        mav.addObject("formId", "tabledataform");
        // check for error
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_MedicalConditions/create");
            mav.addObject("submitBtnValue", "Create");
            return mav;
        }

        //now we save
        sysAdminManager.createMedicalCondition(lu);
        mav.addObject("success", "dataCreated");
        mav.addObject("btnValue", "lu_MedicalConditions/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_MedicalConditions/tableData", method = RequestMethod.GET)
    public ModelAndView viewMedicalCondition(@RequestParam(value = "i", required = false) Integer i)
            throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        lu_MedicalConditions lu = sysAdminManager.getMedicalConditionById(i);
        mav.addObject("tableDataDetails", lu);

        mav.addObject("objectType", "lu_MedicalConditions");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_MedicalConditions/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_medicalconditions/update", method = RequestMethod.POST)
    public ModelAndView updateMedicalConditon(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_MedicalConditions lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_MedicalConditions");
        mav.addObject("formId", "tabledataform");
        /**
         * check for error *
         */
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_MedicalConditions/update");
            mav.addObject("submitBtnValue", "Update");
            return mav;
        }

        //now we save
        sysAdminManager.updateMedicalCondition(lu);
        mav.addObject("success", "dataUpdated");
        mav.addObject("btnValue", "lu_MedicalConditions/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    /**
     * end of medical conditions *
     */
    /**
     * start of medication *
     */
    @RequestMapping(value = "/data/nstd/lu_Medications/create", method = RequestMethod.GET)
    public ModelAndView newMedicationForm() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        //create the object
        lu_Medications lu = new lu_Medications();
        lu.setId(0);
        mav.addObject("tableDataDetails", lu);
        mav.addObject("objectType", "lu_Medications");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_Medications/create");
        mav.addObject("submitBtnValue", "Create");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_medications/create", method = RequestMethod.POST)
    public ModelAndView createMedication(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_Medications lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_Medications");
        mav.addObject("formId", "tabledataform");
        // check for error
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_Medications/create");
            mav.addObject("submitBtnValue", "Create");
            return mav;
        }

        //now we save
        sysAdminManager.createMedication(lu);
        mav.addObject("success", "dataCreated");
        mav.addObject("btnValue", "lu_Medications/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_Medications/tableData", method = RequestMethod.GET)
    public ModelAndView viewMedication(@RequestParam(value = "i", required = false) Integer i)
            throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        lu_Medications lu = sysAdminManager.getMedicationById(i);
        mav.addObject("tableDataDetails", lu);

        mav.addObject("objectType", "lu_Medications");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_Medications/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_medications/update", method = RequestMethod.POST)
    public ModelAndView updateMedication(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_Medications lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_Medications");
        mav.addObject("formId", "tabledataform");
        /**
         * check for error *
         */
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_Medications/update");
            mav.addObject("submitBtnValue", "Update");
            return mav;
        }

        //now we save
        sysAdminManager.updateMedication(lu);
        mav.addObject("success", "dataUpdated");
        mav.addObject("btnValue", "lu_Medications/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    /**
     * end of medication *
     */
    /**
     * Procedures *
     */
    @RequestMapping(value = "/data/nstd/lu_Procedures/create", method = RequestMethod.GET)
    public ModelAndView newProcedureForm() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        //create the object
        lu_Procedures lu = new lu_Procedures();
        lu.setId(0);
        mav.addObject("tableDataDetails", lu);
        mav.addObject("objectType", "lu_Procedures");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_Procedures/create");
        mav.addObject("submitBtnValue", "Create");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_procedures/create", method = RequestMethod.POST)
    public ModelAndView createProcedure(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_Procedures lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_Procedures");
        mav.addObject("formId", "tabledataform");
        // check for error
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_Procedures/create");
            mav.addObject("submitBtnValue", "Create");
            return mav;
        }

        //now we save
        sysAdminManager.createProcedure(lu);
        mav.addObject("success", "dataCreated");
        mav.addObject("btnValue", "lu_Procedures/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_Procedures/tableData", method = RequestMethod.GET)
    public ModelAndView viewProcedure(@RequestParam(value = "i", required = false) Integer i)
            throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        lu_Procedures lu = sysAdminManager.getProcedureById(i);
        mav.addObject("tableDataDetails", lu);

        mav.addObject("objectType", "lu_Procedures");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_Procedures/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_procedures/update", method = RequestMethod.POST)
    public ModelAndView updateProcedure(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_Procedures lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_Procedures");
        mav.addObject("formId", "tabledataform");
        /**
         * check for error *
         */
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_Procedures/update");
            mav.addObject("submitBtnValue", "Update");
            return mav;
        }

        //now we save
        sysAdminManager.updateProcedure(lu);
        mav.addObject("success", "dataUpdated");
        mav.addObject("btnValue", "lu_Procedures/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    /**
     * End of procedures*
     */
    /**
     * Start of Tests *
     */
    @RequestMapping(value = "/data/nstd/lu_Tests/create", method = RequestMethod.GET)
    public ModelAndView newTestForm() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        //Get a list of process status categories
        TestCategoryList categoryList = new TestCategoryList();
        mav.addObject("categoryList", categoryList.getCategories());
        
        //create the object
        lu_Tests lu = new lu_Tests();
        lu.setId(0);
        mav.addObject("tableDataDetails", lu);
        mav.addObject("objectType", "lu_Tests");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_Tests/create");
        mav.addObject("submitBtnValue", "Create");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_tests/create", method = RequestMethod.POST)
    public ModelAndView createTest(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_Tests lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        
        //Get a list of process status categories
        TestCategoryList categoryList = new TestCategoryList();
        mav.addObject("categoryList", categoryList.getCategories());
        
        mav.addObject("objectType", "lu_Tests");
        mav.addObject("formId", "tabledataform");
        // check for error
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_Tests/create");
            mav.addObject("submitBtnValue", "Create");
            return mav;
        }

        //now we save
        sysAdminManager.createTest(lu);
        mav.addObject("success", "dataCreated");
        mav.addObject("btnValue", "lu_Tests/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_Tests/tableData", method = RequestMethod.GET)
    public ModelAndView viewTest(@RequestParam(value = "i", required = false) Integer i)
            throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
       
        //Get a list of process status categories
        TestCategoryList categoryList = new TestCategoryList();
        mav.addObject("categoryList", categoryList.getCategories());
        
        lu_Tests lu = sysAdminManager.getTestById(i);
        mav.addObject("tableDataDetails", lu);

        mav.addObject("objectType", "lu_Tests");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_Tests/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_tests/update", method = RequestMethod.POST)
    public ModelAndView updateTest(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_Tests lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        
        //Get a list of process status categories
        TestCategoryList categoryList = new TestCategoryList();
        mav.addObject("categoryList", categoryList.getCategories());
        
        mav.addObject("objectType", "lu_Tests");
        mav.addObject("formId", "tabledataform");
        /**
         * check for error *
         */
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_Tests/update");
            mav.addObject("submitBtnValue", "Update");
            return mav;
        }

        //now we save
        sysAdminManager.updateTest(lu);
        mav.addObject("success", "dataUpdated");
        mav.addObject("btnValue", "lu_Tests/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    /**
     * End of Tests*
     */
    /**
     * Start of ProcessStatus *
     */
    @RequestMapping(value = "/data/nstd/lu_ProcessStatus/create", method = RequestMethod.GET)
    public ModelAndView newProcessStatusForm() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        //create the object
        lu_ProcessStatus lu = new lu_ProcessStatus();
        lu.setId(0);
        mav.addObject("tableDataDetails", lu);
        //Get a list of process status categories
        ProcessCategoryList categoryList = new ProcessCategoryList();
        mav.addObject("categoryList", categoryList.getCategories());
        mav.addObject("objectType", "lu_ProcessStatus");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_ProcessStatus/create");
        mav.addObject("submitBtnValue", "Create");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_processstatus/create", method = RequestMethod.POST)
    public ModelAndView createProcessStatus(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_ProcessStatus lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_ProcessStatus");
        //Get a list of process status categories
        ProcessCategoryList categoryList = new ProcessCategoryList();
        mav.addObject("categoryList", categoryList.getCategories());
        mav.addObject("formId", "tabledataform");
        // check for error
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_ProcessStatus/create");
            mav.addObject("submitBtnValue", "Create");
            return mav;
        }

        //now we save
        sysAdminManager.createProcessStatus(lu);
        mav.addObject("success", "dataCreated");
        mav.addObject("btnValue", "lu_ProcessStatus/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_ProcessStatus/tableData", method = RequestMethod.GET)
    public ModelAndView viewProcessStatus(@RequestParam(value = "i", required = false) Integer i)
            throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");

        lu_ProcessStatus lu = sysAdminManager.getProcessStatusById(i);
        mav.addObject("tableDataDetails", lu);
        ProcessCategoryList categoryList = new ProcessCategoryList();
        mav.addObject("categoryList", categoryList.getCategories());
        mav.addObject("objectType", "lu_ProcessStatus");
        mav.addObject("formId", "tabledataform");
        mav.addObject("btnValue", "lu_ProcessStatus/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    @RequestMapping(value = "/data/nstd/lu_processstatus/update", method = RequestMethod.POST)
    public ModelAndView updateProcessStatus(
            @Valid @ModelAttribute(value = "tableDataDetails") lu_ProcessStatus lu,
            BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/data/std/details");
        mav.addObject("objectType", "lu_ProcessStatus");
        mav.addObject("formId", "tabledataform");
        ProcessCategoryList categoryList = new ProcessCategoryList();
        mav.addObject("categoryList", categoryList.getCategories());
        /**
         * check for error *
         */
        if (result.hasErrors()) {
            mav.addObject("btnValue", "lu_ProcessStatus/update");
            mav.addObject("submitBtnValue", "Update");
            return mav;
        }

        //now we save
        sysAdminManager.updateProcessStatus(lu);
        mav.addObject("success", "dataUpdated");
        mav.addObject("btnValue", "lu_ProcessStatus/update");
        mav.addObject("submitBtnValue", "Update");
        return mav;
    }

    /**
     * End of ProcessStatus*
     */
    /**
     * Logos
     */
    @RequestMapping(value = "/logos", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView updateLogosForm(HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/logos");
        LogoInfo logoDetails = sysAdminManager.getLogoInfo();
        /**
         * we had a logo from before *
         */
        if (logoDetails.getBackEndLogoName().indexOf("backEndLogo") == 0) {
            sysAdminManager.copyBELogo(request, logoDetails);
        }
        if (logoDetails.getFrontEndLogoName().indexOf("frontEndLogo") == 0) {
            sysAdminManager.copyFELogo(request, logoDetails);
        }
        mav.addObject("btnValue", "Update");
        mav.addObject("logoDetails", logoDetails);
        return mav;
    }

    @RequestMapping(value = "/logos", method = RequestMethod.POST)
    public ModelAndView updateLogos(@ModelAttribute(value = "logoDetails") LogoInfo logoDetails,
            RedirectAttributes redirectAttr) throws Exception {

        sysAdminManager.updateLogoInfo(logoDetails);
        ModelAndView mav = new ModelAndView(new RedirectView("logos?msg=updated"));
        redirectAttr.addFlashAttribute("savedStatus", "updated");
        return mav;
    }
    
    /** modify admin profile **/
    @RequestMapping(value = "/adminInfo", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView displayAdminInfo(HttpServletRequest request, Authentication authentication) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/adminInfo/profile");
        User userDetails = usermanager.getUserByUserName(authentication.getName());
        
        mav.addObject("btnValue", "Update");
        mav.addObject("userdetails", userDetails);
        return mav;
    }
    
    @RequestMapping(value = "/adminInfo", method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView updateAdminInfo(HttpServletRequest request, @ModelAttribute(value = "userdetails") User userdetails, 
    		Authentication authentication, BindingResult result) throws Exception {

        ModelAndView mav = new ModelAndView();
        User user = usermanager.getUserByUserName(authentication.getName());
        
        mav.setViewName("/administrator/sysadmin/adminInfo/profile");
        
        
        /** we verify existing password **/
        
        boolean okToChange = false;
      
        try  {
        	okToChange = usermanager.authenticate(request.getParameter("existingPassword"), user.getEncryptedPw(), user.getRandomSalt());
        } catch(Exception ex) {
        	okToChange = false;
        }
        
        if (okToChange) {
        	/** we update user **/
            user.setFirstName(userdetails.getFirstName());
            user.setLastName(userdetails.getLastName());
            user.setEmail(userdetails.getEmail());
            if (!request.getParameter("newPassword").trim().equalsIgnoreCase("")) {
          		user.setRandomSalt(usermanager.generateSalt());
           		user.setEncryptedPw(usermanager.getEncryptedPassword(request.getParameter("newPassword"), user.getRandomSalt()));
            }
            try {
            	usermanager.updateUserOnly(user);
            } catch (Exception ex) {
            	ex.printStackTrace();
            	okToChange = false;
            }

        }
        
        if (!okToChange) {
        	mav.addObject("failed", "Please check your existing password.  Profile is not updated.");
        	mav.addObject("btnValue", "Update");
        }  else {
	        mav.addObject("userdetails", user);
	        mav.addObject("success", "Profile is updated");
	        mav.addObject("btnValue", "Update");
        }
        
        
        return mav;
    }  
    
 
    /** login as portion  **/
    @RequestMapping(value = "/loginAs", method = RequestMethod.GET)
    public ModelAndView loginAs() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/loginAs");
        
        //get all active users
        List <User> usersList = usermanager.getUsersByStatuRolesAndOrg(true, Arrays.asList(1), Arrays.asList(1), false);
        mav.addObject("usersList", usersList);

        return mav;
    }
    
    @RequestMapping(value = "/loginAs", method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView checkAdminPW(HttpServletRequest request,  Authentication authentication) throws Exception {

        ModelAndView mav = new ModelAndView();
        User user = usermanager.getUserByUserName(authentication.getName());
        
        mav.setViewName("/administrator/sysadmin/loginAs");
        
        boolean okToLoginAs = false;
        
        /** we verify existing password **/
        if (user.getRoleId() == 1 || user.getRoleId() == 4) {
	        try  {
	        	okToLoginAs = usermanager.authenticate(request.getParameter("j_password"), user.getEncryptedPw(), user.getRandomSalt());
	        } catch(Exception ex) {
	        	okToLoginAs = false;
	        }
        }
        
        
        if (!okToLoginAs) {
        	mav.addObject("msg", "Your credentials are invalid.");
        }  else {
	        mav.addObject("msg", "pwmatched");
	    }
        
        
        return mav;
    }  
    
    @RequestMapping(value = "/getLog", method = {RequestMethod.GET})
    public void getLog(HttpSession session, HttpServletResponse response, Authentication authentication) throws Exception {
    	
    	User userInfo = usermanager.getUserByUserName(authentication.getName());
    	//log user activity
 	   UserActivity ua = new UserActivity();
 	   ua.setUserId(userInfo.getId());
 	   ua.setAccessMethod("GET");
 	   ua.setPageAccess("/getLog");
 	   ua.setActivity("Download Tomcat Log");
 	   usermanager.insertUserLog(ua);
 	   
    	File logFileDir = new File(System.getProperty("catalina.home"), "logs");
        File logFile = new File(logFileDir, "catalina.out");
        // get your file as InputStream
	   InputStream is = new FileInputStream(logFile);
	   String mimeType = "application/octet-stream";
	            		  response.setContentType(mimeType);
	            		  response.setHeader("Content-Transfer-Encoding", "binary");
	                      response.setHeader("Content-Disposition", "attachment;filename=catalina.out");
	                      org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
	                      response.flushBuffer();
	            	      is.close();
	   
} 
}
