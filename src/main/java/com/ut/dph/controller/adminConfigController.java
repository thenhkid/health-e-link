package com.ut.dph.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ut.dph.model.Crosswalks;
import com.ut.dph.model.HL7Details;
import com.ut.dph.model.HL7ElementComponents;
import com.ut.dph.model.HL7Elements;
import com.ut.dph.model.HL7Segments;
import com.ut.dph.model.Macros;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.messageTypeFormFields;
import com.ut.dph.service.configurationManager;
import com.ut.dph.model.Organization;
import com.ut.dph.model.User;
import com.ut.dph.model.configurationConnection;
import com.ut.dph.model.configurationConnectionReceivers;
import com.ut.dph.model.configurationConnectionSenders;
import com.ut.dph.model.configurationFTPFields;
import com.ut.dph.model.configurationMessageSpecs;
import com.ut.dph.model.configurationSchedules;
import com.ut.dph.service.organizationManager;
import com.ut.dph.model.messageType;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.configurationTransportMessageTypes;
import com.ut.dph.service.configurationTransportManager;
import com.ut.dph.service.userManager;
import java.io.PrintWriter;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

@Controller
@RequestMapping("/administrator/configurations")

public class adminConfigController {

    @Autowired
    private configurationManager configurationmanager;

    @Autowired
    private organizationManager organizationmanager;

    @Autowired
    private messageTypeManager messagetypemanager;
    
    @Autowired
    private userManager userManager;

    @Autowired
    private configurationTransportManager configurationTransportManager;

    /**
     * The private variable configId will hold the configurationId when viewing a configuration this will be used when on a configuration subsections like Field Mappings, Data Translations, etc. We will use this private variable so we don't have to go fetch the id
     */
    private static int configId = 0;

    /**
     * The private maxResults variable will hold the number of results to show per list page.
     */
    private static int maxResults = 20;
    
    /** 
     * The private mappings variable will hold the value to determine if the left menu
     * will activate the mappings link, ERG Customization link or both.
     * 
     * 0 = Both Mappings link and ERG Customization link are inactive
     * 1 = Mappings link only
     * 2 = ERG Customization link only
     * 3 = Both Mappings link and ERG Customization link are active
     */
    private static int mappings = 0;
    
    /**
     * The private HL7 variable will determine if the transport method is file download
     * of an HL7 message. This will display the HL7 Customization link in the left menu.
     */
    private static boolean HL7 = false;
    
    /** 
     * The stepsCompleted variable will hold the number of steps the configuration has gone 
     * through
     */
    private static int stepsCompleted = 0;

    private static List<configurationDataTranslations> translations = null;

    /**
     * The '/list' GET request will serve up the existing list of configurations in the system
     *
     * @param page	The page parameter will hold the page to view when pagination is built.
     * @return	The configuration page list
     *
     * @Objects	(1) An object containing all the found configurations
     *
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView listConfigurations(@RequestParam(value = "page", required = false) Integer page) throws Exception {

        if (page == null) {
            page = 1;
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/list");
        
        List<configuration> configurations = configurationmanager.getConfigurations(page, maxResults);
        
        Organization org;
        messageType messagetype;
        configurationTransport transportDetails;

        for (configuration config : configurations) {
            org = organizationmanager.getOrganizationById(config.getorgId());
            config.setOrgName(org.getOrgName());

            messagetype = messagetypemanager.getMessageTypeById(config.getMessageTypeId());
            config.setMessageTypeName(messagetype.getName());
            
            transportDetails = configurationTransportManager.getTransportDetails(config.getId());
            if(transportDetails != null) {
             config.settransportMethod(configurationTransportManager.getTransportMethodById(transportDetails.gettransportMethodId()));
            }
            
        }
        
        mav.addObject("configurationList", configurations);

        //Return the total list of configurations
        Long totalConfigs = configurationmanager.findTotalConfigs();

        Integer totalPages = (int) Math.ceil((double)totalConfigs / maxResults);
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", page);
        return mav;

    }

    /**
     * The '/list' POST request will be used to search configurations from the search form on the configuration list page.
     *
     * @param searchTerm	The searchTerm parameter will hold the string to search on
     * @return	The configuration page list
     *
     * @Objects	(1) An object will be returned holding the requested search term used to populate the search box (2) An object containing the found configurations
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView findConfigurations(@RequestParam String searchTerm, @RequestParam int configType) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/list");

        List<configuration> configurations = configurationmanager.findConfigurations(searchTerm, configType);
        mav.addObject("searchTerm", searchTerm);
        mav.addObject("searchConfigType", configType);
        mav.addObject("configurationList", configurations);

        Organization org;
        messageType messagetype;
        configurationTransport transportDetails;

        for (configuration config : configurations) {
            org = organizationmanager.getOrganizationById(config.getorgId());
            config.setOrgName(org.getOrgName());

            messagetype = messagetypemanager.getMessageTypeById(config.getMessageTypeId());
            config.setMessageTypeName(messagetype.getName());
            
            transportDetails = configurationTransportManager.getTransportDetails(config.getId());
            if(transportDetails != null) {
             config.settransportMethod(configurationTransportManager.getTransportMethodById(transportDetails.gettransportMethodId()));
            }
        }

        return mav;

    }

    /**
     * The '/create' GET request will serve up the create new configuration page
     *
     * @return	The create new configuration form
     *
     * @Objects	(1) An object with a new configuration
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createConfiguration() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/details");
        mav.addObject("configurationDetails", new configuration());

        //Need to get a list of active organizations.
        List<Organization> organizations = organizationmanager.getAllActiveOrganizations();
        mav.addObject("organizations", organizations);

        //Need to get a list of active message types
        List<messageType> messageTypes = messagetypemanager.getActiveMessageTypes();
        mav.addObject("messageTypes", messageTypes);

        return mav;

    }
    
    /**
     * The '/getAvailableUsers.do' function will return a list of users that are associated
     * to the selected organization.
     * 
     * @param orgId The organization selected in the drop down
     * 
     * @return users    The available users
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value= "/getAvailableUsers.do", method = RequestMethod.GET)
    public @ResponseBody List<User> getUsers(@RequestParam(value = "orgId", required = true) int orgId) {
        
        List<User> users = userManager.getUsersByOrganization(orgId);
        
        return users;
    } 
    
    /**
     * The '/getAvailableMessageTypes.do' function will return a list of message types
     * that have not been already set up for the passed in organization.
     * 
     * @param   orgId   The organization selected in the drop down
     * 
     * @return  messageTypes    The available message types
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value= "/getAvailableMessageTypes.do", method = RequestMethod.GET)
    public @ResponseBody List<messageType> getMessageTypes(@RequestParam(value = "orgId", required = true) int orgId) {
        
        List<messageType> messageTypes = messagetypemanager.getAvailableMessageTypes(orgId);
        
        return messageTypes;
    } 

    /**
     * The '/create' POST request will submit the new configuration once all required fields are checked, the system will also check to make sure the configuration name is not already in use.
     *
     * @param configurationDetails	The object holding the configuration details form fields
     * @param result	The validation result
     * @param redirectAttr	The variable that will hold values that can be read after the redirect
     * @param action	The variable that holds which button was pressed
     *
     * @return	Will return the configuration details page on "Save" Will return the configuration create page on error
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView saveNewConfiguration(@ModelAttribute(value = "configurationDetails") configuration configurationDetails, BindingResult result, RedirectAttributes redirectAttr, @RequestParam String action) throws Exception {

        /* Need to make sure the name isn't already taken for the org selected */
        configuration existing = configurationmanager.getConfigurationByName(configurationDetails.getconfigName(), configurationDetails.getorgId());
        
        if (existing != null) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/administrator/configurations/details");
            
            mav.addObject("configurationDetails", configurationDetails);
            
            //Need to get a list of active organizations.
            List<Organization> organizations = organizationmanager.getAllActiveOrganizations();
            mav.addObject("organizations", organizations);

            //Need to get a list of active message types
            List<messageType> messageTypes = messagetypemanager.getActiveMessageTypes();
            mav.addObject("messageTypes", messageTypes);
            
            mav.addObject("existingName", "The configuration name " + configurationDetails.getconfigName().trim() + " already exists.");
            return mav;
        }
        
        
        Integer id = (Integer) configurationmanager.createConfiguration(configurationDetails);

        configId = id;
        
        stepsCompleted = 1;
        
        //If the "Save" button was pressed 
        if (action.equals("save")) {
            redirectAttr.addFlashAttribute("savedStatus", "created");
            ModelAndView mav = new ModelAndView(new RedirectView("details"));
            return mav;
            
        } //If the "Next Step" button was pressed.
        else {
            redirectAttr.addFlashAttribute("savedStatus", "created");
            ModelAndView mav = new ModelAndView(new RedirectView("transport"));
            return mav;
        }
        
    }

    /**
     * The '/details' GET request will display the clicked configuration details page.
     *
     * @return	Will return the configuration details page.
     *
     * @Objects	(1) The object containing all the information for the clicked configuration (2) The 'id' of the clicked configuration that will be used in the menu and action bar
     *
     * @throws Exception
     *
     */
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public ModelAndView viewConfigurationDetails(@RequestParam(value = "i", required = false) Integer id) throws Exception {

        if (id != null) {
            configId = id;
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/details");

        configuration configurationDetails = configurationmanager.getConfigurationById(configId);
        mav.addObject("configurationDetails", configurationDetails);

        //Set the variable to hold the number of completed steps for this configuration;
        stepsCompleted = configurationDetails.getstepsCompleted();
        mav.addObject("stepsCompleted", stepsCompleted);

        //Need to get a list of active organizations.
        List<Organization> organizations = organizationmanager.getAllActiveOrganizations();
        mav.addObject("organizations", organizations);

        //Need to get a list of active message types
        List<messageType> messageTypes = messagetypemanager.getActiveMessageTypes();
        mav.addObject("messageTypes", messageTypes);
        
        //Need to get a list of organization users 
        List<User> users = userManager.getUsersByOrganization(configurationDetails.getorgId());
        mav.addObject("users", users);
        
        mav.addObject("id", configId);
        
        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configId);
        if(transportDetails != null) {
            //Need to set the mappings static variable
            if(transportDetails.gettransportMethodId() == 2) {
                mappings = 2;
            }
            else {
                if(transportDetails.geterrorHandling() == 1) {
                    mappings = 3;
                }
                else {
                    mappings = 1;
                }
            }
            
            if(transportDetails.getfileType() == 4) {
                HL7 = true;
            }
            else {
                HL7 = false;
            }
        }
        
        mav.addObject("mappings", mappings);
        mav.addObject("HL7", HL7);

        return mav;

    }

    /**
     * The '/details' POST request will display the clicked configuration details page.
     *
     * @return	Will return the configuration details page.
     *
     * @Objects	(1) The object containing all the information for the clicked configuration (2) The 'id' of the clicked configuration that will be used in the menu and action bar
     *
     * @throws Exception
     *
     */
    @RequestMapping(value = "/details", method = RequestMethod.POST)
    public ModelAndView updateConfigurationDetails(@ModelAttribute(value = "configurationDetails") configuration configurationDetails, BindingResult result, RedirectAttributes redirectAttr, @RequestParam String action) throws Exception {

        //Need to get a list of active organizations.
        List<Organization> organizations = organizationmanager.getAllActiveOrganizations();

        //Need to get a list of active message types
        List<messageType> messageTypes = messagetypemanager.getActiveMessageTypes();
        
        //Need to get a list of organization users 
        List<User> users = userManager.getUsersByOrganization(configurationDetails.getorgId());

        //submit the updates
        configurationmanager.updateConfiguration(configurationDetails);
        
        /**
         * Need to update the configuration completed step
         *
        */
        if (stepsCompleted < 1) {
            configurationmanager.updateCompletedSteps(configId, 1);
            stepsCompleted = 1;
        }
        
        //If the "Save" button was pressed 
        if (action.equals("save")) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/administrator/configurations/details");

            mav.addObject("organizations", organizations);
            mav.addObject("messageTypes", messageTypes);
            mav.addObject("users", users);
            mav.addObject("id", configId);
            mav.addObject("mappings", mappings);
            mav.addObject("savedStatus", "updated");
            mav.addObject("stepsCompleted", stepsCompleted);

            return mav;
        } //If the "Next Step" button was pressed.
        else {
            redirectAttr.addFlashAttribute("savedStatus", "updated");
            ModelAndView mav = new ModelAndView(new RedirectView("transport"));
            return mav;
        }

    }

    /**
     * The '/transport' GET request will display the clicked configuration transport details form.
     *
     * @return	Will return the configuration transport details form
     *
     * @Objects	transportDetails will hold a empty object or an object containing the existing transport details for the selected configuration
     *
     * @throws Exception
     *
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/transport", method = RequestMethod.GET)
    public ModelAndView viewTransportDetails() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/transport");
        
        //Get the configuration details for the selected config
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);
        
        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configId);
        if(transportDetails == null) {
            transportDetails = new configurationTransport();
            
            /* Get organization directory name */
            Organization orgDetails = organizationmanager.getOrganizationById(configurationDetails.getorgId());
            if(configurationDetails.getType() == 1) {
                transportDetails.setfileLocation("/bowlink/"+orgDetails.getcleanURL()+"/input files/");
            }
            else {
               transportDetails.setfileLocation("/bowlink/"+orgDetails.getcleanURL()+"/output files/"); 
            }
            
            List<Integer> assocMessageTypes = new ArrayList<Integer>();
            assocMessageTypes.add(configurationDetails.getId());
            transportDetails.setmessageTypes(assocMessageTypes);
            
        }
        else {
            /* Need to set the associated message types */
            List<configurationTransportMessageTypes> messageTypes = configurationTransportManager.getTransportMessageTypes(transportDetails.getId());
            List<Integer> assocMessageTypes = new ArrayList<Integer>();
            if(messageTypes != null) {
                for(configurationTransportMessageTypes messageType : messageTypes) {
                    assocMessageTypes.add(messageType.getconfigId());
                }
                transportDetails.setmessageTypes(assocMessageTypes);
            }
            else {
                assocMessageTypes.add(configurationDetails.getId());
                transportDetails.setmessageTypes(assocMessageTypes);
            }
            
        }
        
        /* Need to get any FTP fields */
        List<configurationFTPFields> ftpFields = configurationTransportManager.getTransportFTPDetails(transportDetails.getId());
        
        if(ftpFields.isEmpty()) {
            List<configurationFTPFields> emptyFTPFields = new ArrayList<configurationFTPFields>();
            configurationFTPFields pushFTPFields = new configurationFTPFields();
            pushFTPFields.setmethod(1);
            
            emptyFTPFields.add(pushFTPFields);
            
            configurationFTPFields getFTPFields = new configurationFTPFields();
            getFTPFields.setmethod(2);
            
            emptyFTPFields.add(getFTPFields);
            
            transportDetails.setFTPFields(emptyFTPFields);
        }
        else {
            transportDetails.setFTPFields(ftpFields);
        }
        
        
        //Need to get a list of all configurations for the current organization
        List<configuration> configurations = configurationmanager.getConfigurationsByOrgId(configurationDetails.getorgId(),"");

        for(configuration config : configurations) {
            configurationTransport transDetails = configurationTransportManager.getTransportDetails(config.getId());
            config.setMessageTypeName(messagetypemanager.getMessageTypeById(config.getMessageTypeId()).getName());
            
            if(transDetails != null) {
                config.settransportDetailId(transDetails.getId());
                config.settransportMethod(configurationTransportManager.getTransportMethodById(transDetails.gettransportMethodId()));
            }

        }
        mav.addObject("availConfigurations", configurations);
        
        transportDetails.setconfigId(configId);
        mav.addObject("transportDetails", transportDetails);
        
        
        //Set the variable id to hold the current configuration id
        mav.addObject("id", configId);
        mav.addObject("mappings", mappings);
        mav.addObject("HL7", HL7);

        configurationDetails.setOrgName(organizationmanager.getOrganizationById(configurationDetails.getorgId()).getOrgName());
        configurationDetails.setMessageTypeName(messagetypemanager.getMessageTypeById(configurationDetails.getMessageTypeId()).getName());
        configurationDetails.settransportMethod(configurationTransportManager.getTransportMethodById(transportDetails.gettransportMethodId()));
        
        //pass the configuration detail object back to the page.
        mav.addObject("configurationDetails", configurationDetails);

        //Set the variable to hold the number of completed steps for this configuration;
        mav.addObject("stepsCompleted", stepsCompleted);

        //Get the list of available transport methods
        List transportMethods = configurationTransportManager.getTransportMethods();
        mav.addObject("transportMethods", transportMethods);

        //Get the list of available file delimiters
        List delimiters = messagetypemanager.getDelimiters();
        mav.addObject("delimiters", delimiters);

        //Get the list of available file types
        List fileTypes = configurationmanager.getFileTypes();
        mav.addObject("fileTypes", fileTypes);

        return mav;
    }
    
    /**
     * The '/copyExistingTransportMethod.do' POST request will copy the existing transport
     * settings for the passed transport method to the new configuration passed in.
     * 
     */
    @RequestMapping(value = "/copyExistingTransportMethod.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer copyExistingTransportMethod(@RequestParam(value = "detailId", required = true) Integer detailId, @RequestParam(value = "configId", required = true) Integer configId) throws Exception {
        
        configurationTransportManager.copyExistingTransportMethod(detailId, configId);
        
        return 1;
    }
    
    /**
     * The '/transport' POST request will submit the transport details
     *
     * @param	transportDetails	Will contain the contents of the transport form
     *
     * @return	This function will either return to the transport details screen or redirect to the next step (Field Mappings)
     */
    @RequestMapping(value = "/transport", method = RequestMethod.POST)
    public ModelAndView updateTransportDetails(@Valid @ModelAttribute(value = "transportDetails") configurationTransport transportDetails, BindingResult result, RedirectAttributes redirectAttr, @RequestParam String action) throws Exception {
        
        Integer currTransportId = transportDetails.getId();
        
        /* submit the updates */
        Integer transportId = (Integer) configurationTransportManager.updateTransportDetails(transportDetails);
        
        /**
         * if transport method = ERG (2) then set up the online form
         * OR
         * if transport method is not ERG but the error handling is set
         * to fix errors via ERG set up the online form
         */
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);
        configurationTransportManager.setupOnlineForm(transportId, configId, configurationDetails.getMessageTypeId());
       
        /* Need to set the mappings static variable */
        if(transportDetails.gettransportMethodId() == 2) {
            mappings = 2;
        }
        else {
            if(transportDetails.geterrorHandling() == 1) {
                mappings = 3;
            }
            else {
                mappings = 1;
            }
        }
        
        if(transportDetails.getfileType() == 4) {
            HL7 = true;
        }
        else {
            HL7 = false;
        }
        
        /** 
         * Need to set up the FTP information if
         * any has been entered
         */
        if(transportDetails.gettransportMethodId() == 3 && !transportDetails.getFTPFields().isEmpty()) {
            for(configurationFTPFields ftpFields : transportDetails.getFTPFields()) {
                ftpFields.settransportId(transportId);
                configurationTransportManager.saveTransportFTP(ftpFields);
            }
        }
        
        
        /**
         * Need to set the associated messages types
         * 
         * step 1: Remove all associations
         * step 2: Loop through the selected message Types
         */
        
        /** Step 1: */ 
        configurationTransportManager.deleteTransportMessageTypes(transportId);
        
        
        /** Step 2: */
        if(transportDetails.getmessageTypes() != null) {
            configurationTransportMessageTypes messageType;
            for(Integer selconfigId : transportDetails.getmessageTypes()) {
                messageType = new configurationTransportMessageTypes();
                messageType.setconfigId(selconfigId);
                messageType.setconfigTransportId(transportId);
                configurationTransportManager.saveTransportMessageTypes(messageType);
            }
        }
        
        
        
        redirectAttr.addFlashAttribute("savedStatus", "updated");

        //If the "Save" button was pressed 
        if (action.equals("save")) {
           /**
            * Need to update the configuration completed step
            *
           */
           if (stepsCompleted < 2) {
               configurationmanager.updateCompletedSteps(configId, 2);
               stepsCompleted = 2;
           }
           ModelAndView mav = new ModelAndView(new RedirectView("transport"));
           return mav;
        } else {
            //If the type of configuration is for a source then send to message specs
            if(configurationDetails.getType() == 1) {
                /**
                * Need to update the configuration completed step
                *
                */
                if (stepsCompleted < 2) {
                   configurationmanager.updateCompletedSteps(configId, 2);
                   stepsCompleted = 2;
                }
                ModelAndView mav = new ModelAndView(new RedirectView("messagespecs"));
                return mav;
            }
            else {
                /** If transport method is ERG send to the ERG Customization page */
                if(transportDetails.gettransportMethodId() == 2) {
                    /**
                     * Need to update the configuration completed step
                     *
                    */
                    if (stepsCompleted < 2) {
                       configurationmanager.updateCompletedSteps(configId, 3);
                       stepsCompleted = 3;
                    }
                    ModelAndView mav = new ModelAndView(new RedirectView("ERGCustomize"));
                    return mav;
                }
                /** Otherwise send to the field mappings page */
                else {
                    /**
                     * Need to update the configuration completed step
                     *
                    */
                    if (stepsCompleted < 2) {
                       configurationmanager.updateCompletedSteps(configId, 3);
                       stepsCompleted = 3;
                    }
                    ModelAndView mav = new ModelAndView(new RedirectView("mappings"));
                    return mav;
                } 
            }
        }
        

    }

    /**
     * The '/messagespecs' GET request will display the configuration message specs form.
     *
     * @return	Will return the configuration message spec details form
     *
     * @Objects	transportDetails will hold a empty object or an object containing the existing transport details for the selected configuration
     *
     * @throws Exception
     *
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/messagespecs", method = RequestMethod.GET)
    public ModelAndView viewMessageSpecDetails() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/messagespecs");
        
        configurationMessageSpecs messageSpecs = configurationmanager.getMessageSpecs(configId);
        if(messageSpecs == null) {
            messageSpecs = new configurationMessageSpecs();
            messageSpecs.setconfigId(configId);
        }
        mav.addObject("messageSpecs", messageSpecs);
        
        //Need to pass the selected transport Type
        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configId);
        mav.addObject("transportType", transportDetails.gettransportMethodId());
        
        //Set the variable id to hold the current configuration id
        mav.addObject("id", configId);
        mav.addObject("mappings", mappings);
        mav.addObject("HL7", HL7);

        //Get the configuration details for the selected config
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);
       
        configurationDetails.setOrgName(organizationmanager.getOrganizationById(configurationDetails.getorgId()).getOrgName());
        configurationDetails.setMessageTypeName(messagetypemanager.getMessageTypeById(configurationDetails.getMessageTypeId()).getName());
        configurationDetails.settransportMethod(configurationTransportManager.getTransportMethodById(transportDetails.gettransportMethodId()));
        
        //pass the configuration detail object back to the page.
        mav.addObject("configurationDetails", configurationDetails);
        
        //Need to get all available fields that can be used for the reportable fields
        List<configurationFormFields> fields = configurationTransportManager.getConfigurationFields(configId, transportDetails.getId());
        mav.addObject("availableFields",fields);

        //Set the variable to hold the number of completed steps for this configuration;
        mav.addObject("stepsCompleted", stepsCompleted);
        
        return mav;
    }
    
    /**
     * The '/messagespecs' POST request submit all the configuration message specs.
     *
     * @param messageSpecs  Will contain the contents of the configuration message spec form.
     * 
     * @return	This function will either return to the message spec details screen or redirect to the next step (Field Mappings)
     *
     * @throws Exception
     *
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/messagespecs", method = RequestMethod.POST)
    public ModelAndView updateMessageSpecs(@Valid @ModelAttribute(value = "messageSpecs") configurationMessageSpecs messageSpecs, BindingResult result, RedirectAttributes redirectAttr, @RequestParam String action) throws Exception {

        /**
         * Need to update the configuration completed step
         *
        */
        if (stepsCompleted < 3) {
            configurationmanager.updateCompletedSteps(configId, 3);
            stepsCompleted = 3;
        }
        
        /** Need to pass the selected transport Type */
        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configId);
        
        /** Save/Update the configuration message specs */
        configurationmanager.updateMessageSpecs(messageSpecs, transportDetails.getId());
        
        redirectAttr.addFlashAttribute("savedStatus", "updated");

        /** If the "Save" button was pressed */
        if (action.equals("save")) {
            ModelAndView mav = new ModelAndView(new RedirectView("messagespecs"));
            return mav;
        } 
        else {
            /** If transport method is ERG send to the ERG Customization page */
            if(transportDetails.gettransportMethodId() == 2) {
                ModelAndView mav = new ModelAndView(new RedirectView("ERGCustomize"));
                return mav;
            }
            /** Otherwise send to the field mappings page */
            else {
                ModelAndView mav = new ModelAndView(new RedirectView("mappings"));
                return mav;
            }
            
        }
        
    }
    
    /** 
     * The '/ERGCustomize' GET request will display the configuration ERG Customization form.
     * 
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/ERGCustomize", method = RequestMethod.GET)
    public ModelAndView viewERGCustomization() throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/ERGCustomize");
        mav.addObject("id", configId);
        mav.addObject("mappings", mappings);
        mav.addObject("HL7", HL7);
        
        //Get the configuration details for the selected config
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);
        
        //Get the transport details by configid and selected transport method
        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configId);

        //Get the transport fields
        List<configurationFormFields> fields = configurationTransportManager.getConfigurationFields(configId, transportDetails.getId());
        transportDetails.setFields(fields);

        mav.addObject("transportDetails", transportDetails);
       
        configurationDetails.setOrgName(organizationmanager.getOrganizationById(configurationDetails.getorgId()).getOrgName());
        configurationDetails.setMessageTypeName(messagetypemanager.getMessageTypeById(configurationDetails.getMessageTypeId()).getName());
        configurationDetails.settransportMethod(configurationTransportManager.getTransportMethodById(transportDetails.gettransportMethodId()));
        
        //pass the configuration detail object back to the page.
        mav.addObject("configurationDetails", configurationDetails);

        //Set the variable to hold the number of completed steps for this configuration;
        mav.addObject("stepsCompleted", stepsCompleted);
        
         //Get the list of available field validation types
        List validationTypes = messagetypemanager.getValidationTypes();
        mav.addObject("validationTypes", validationTypes);

        
        return mav;
    }
    

    /**
     * The '/mappings' GET request will determine based on the selected transport method what page to display. 
     * Either the choose fields page if 'online form' is selected or 'mappings' if a custom file is being uploaded.
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/mappings", method = RequestMethod.GET)
    public ModelAndView getConfigurationMappings() throws Exception {
       
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/mappings");
        mav.addObject("id", configId);
        mav.addObject("mappings", mappings);
        mav.addObject("HL7", HL7);

        //Get the completed steps for the selected configuration;
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);
        
        //Get the transport details by configid and selected transport method
        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configId);
        
        configurationDetails.setOrgName(organizationmanager.getOrganizationById(configurationDetails.getorgId()).getOrgName());
        configurationDetails.setMessageTypeName(messagetypemanager.getMessageTypeById(configurationDetails.getMessageTypeId()).getName());
        configurationDetails.settransportMethod(configurationTransportManager.getTransportMethodById(transportDetails.gettransportMethodId()));
        
        //pass the configuration detail object back to the page.
        mav.addObject("configurationDetails", configurationDetails);

        //Get the transport fields
        List<configurationFormFields> fields = configurationTransportManager.getConfigurationFields(configId, transportDetails.getId());
        transportDetails.setFields(fields);

        mav.addObject("transportDetails", transportDetails);

        //Need to get the template fields
        List<messageTypeFormFields> templateFields = messagetypemanager.getMessageTypeFields(configurationDetails.getMessageTypeId());
        mav.addObject("templateFields", templateFields);
        
        mav.addObject("selTransportMethod", transportDetails.gettransportMethodId());
       
        
        //Set the variable to hold the number of completed steps for this configuration;
        mav.addObject("stepsCompleted", stepsCompleted);

        return mav;
    }

    /**
     * The 'saveFields' POST method will submit the changes to the form field settings for the selected configuration. This method is only for configurations set for 'Online Form' as the data transportation method.
     *
     * @param	transportDetails	The field details from the form action	The field that will hold which button was pressed "Save" or "Next Step"
     *
     * @return	This method will either redirect back to the Choose Fields page or redirect to the next step data translations page.
     */
    @RequestMapping(value = "/saveFields", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer saveFormFields(@Valid @ModelAttribute(value = "transportDetails") configurationTransport transportDetails, RedirectAttributes redirectAttr, @RequestParam String action, @RequestParam int transportMethod, @RequestParam int errorHandling) throws Exception {

        /**
         * Need to update the configuration completed step
         *
        */
        if (stepsCompleted < 4) {
            configurationmanager.updateCompletedSteps(configId, 4);
            stepsCompleted = 4;
        }

        //Get the list of fields
        List<configurationFormFields> fields = transportDetails.getFields();

        if (null != fields && fields.size() > 0) {
            for (configurationFormFields formfield : fields) {
                //If the message type field id is blank then set the use field to no
                if (formfield.getmessageTypeFieldId() == 0) {
                    formfield.setUseField(false);
                } else {
                    formfield.setUseField(true);
                }
                //Update each field
                configurationTransportManager.updateConfigurationFormFields(formfield);
            }
        }

        //If the "Save" button was pressed 
        if (action.equals("save")) {
            return 1;
            
        } else {
            if(errorHandling == 1) {
                return 2;
            }
            else  {
                return 1;
            }
            
        }

    }

    /**
     * The '/translations' GET request will display the data translations page for the selected transport Method
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/translations", method = RequestMethod.GET)
    public ModelAndView getConfigurationTranslations() throws Exception {
      
        //Set the data translations array to get ready to hold data
        translations = new CopyOnWriteArrayList<configurationDataTranslations>();

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/translations");
        mav.addObject("id", configId);
        mav.addObject("mappings", mappings);
        mav.addObject("HL7", HL7);
        
        //Get the completed steps for the selected configuration;
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);
        
        //Get the transport details by configid and selected transport method
        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configId);
        
        configurationDetails.setOrgName(organizationmanager.getOrganizationById(configurationDetails.getorgId()).getOrgName());
        configurationDetails.setMessageTypeName(messagetypemanager.getMessageTypeById(configurationDetails.getMessageTypeId()).getName());
        configurationDetails.settransportMethod(configurationTransportManager.getTransportMethodById(transportDetails.gettransportMethodId()));
        
        //pass the configuration detail object back to the page.
        mav.addObject("configurationDetails", configurationDetails);

        //Get the transport fields
        List<configurationFormFields> fields = configurationTransportManager.getConfigurationFields(configId, transportDetails.getId());
        transportDetails.setFields(fields);

        mav.addObject("fields", fields);

        //Return a list of available crosswalks
        List<Crosswalks> crosswalks = messagetypemanager.getCrosswalks(1, 0, configurationDetails.getorgId());
        mav.addObject("crosswalks", crosswalks);
        mav.addObject("orgId", configurationDetails.getorgId());

        //Return a list of available macros
        List<Macros> macros = configurationmanager.getMacros();
        mav.addObject("macros", macros);
        
        //Loop through list of macros to mark the ones that need
        //fields filled in
        List<Integer> macroLookUpList = new ArrayList<Integer>();
        for(Macros macro : macros) {
            if(macro.getfieldAQuestion() != null || macro.getfieldBQuestion() != null || macro.getcon1Question() != null || macro.getcon2Question() != null) {
                macroLookUpList.add(macro.getId());
            }
        }
        mav.addObject("macroLookUpList", macroLookUpList);
        
        //Set the variable to hold the number of completed steps for this configuration;
        mav.addObject("stepsCompleted", stepsCompleted);

        return mav;
    }
    
    /**
     * The '/getMacroDetails.do' function will display the modal window for the 
     * selected macro form.
     * 
     * @param   macroId     The id of the selected macro
     * 
     */
    @RequestMapping(value = "/getMacroDetails.do", method = RequestMethod.GET)
    public @ResponseBody ModelAndView getMacroDetails(@RequestParam(value = "macroId", required = true) Integer macroId) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/macroDetails");
        
        Macros macroDetails = configurationmanager.getMacroById(macroId);
        
        mav.addObject("fieldA_Question", macroDetails.getfieldAQuestion());
        mav.addObject("fieldB_Question", macroDetails.getfieldBQuestion());
        mav.addObject("Con1_Question", macroDetails.getcon1Question());
        mav.addObject("Con2_Question", macroDetails.getcon2Question());
        
        return mav;
    }

    /**
     * The '/translations' POST request will submit the selected data translations and save it to the data base.
     *
     */
    @RequestMapping(value = "/translations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer submitDataTranslations() throws Exception {

        /**
         * Need to update the configuration completed step
         *
        */
        if (stepsCompleted < 5) {
            configurationmanager.updateCompletedSteps(configId, 5);
            stepsCompleted = 5;
        }

	//Delete all the data translations before creating
        //This will help with the jquery removing translations
        configurationmanager.deleteDataTranslations(configId);

        //Loop through the list of translations
        for (configurationDataTranslations translation : translations) {
            configurationmanager.saveDataTranslations(translation);
        }

        return 1;
    }

    /**
     * The '/getTranslations.do' function will return the list of existing translations set up for the selected configuration/transportMethod.
     *
     * @Return list of translations
     */
    @RequestMapping(value = "/getTranslations.do", method = RequestMethod.GET)
    public @ResponseBody ModelAndView getTranslations(@RequestParam(value = "reload", required = true) boolean reload) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/existingTranslations");

		//only get the saved translations if reload == 0
        //We only want to retrieve the saved ones on initial load
        if (reload == false) {
            //Need to get a list of existing translations
            List<configurationDataTranslations> existingTranslations = configurationmanager.getDataTranslations(configId);
            
            String fieldName;
            String crosswalkName;
            String macroName;

            for (configurationDataTranslations translation : existingTranslations) {
                //Get the field name by id
                fieldName = configurationmanager.getFieldName(translation.getFieldId());
                translation.setfieldName(fieldName);

                //Get the crosswalk name by id
                if (translation.getCrosswalkId() != 0) {
                	crosswalkName = messagetypemanager.getCrosswalkName(translation.getCrosswalkId());
                	translation.setcrosswalkName(crosswalkName);
                }
                //Get the macro name by id
                if(translation.getMacroId() > 0) {
                    Macros macroDetails = configurationmanager.getMacroById(translation.getMacroId());
                    macroName = macroDetails.getmacroShortName();
                    if(macroName.contains("DATE")) {
                        macroName = macroDetails.getmacroShortName() + " " + macroDetails.getdateDisplay();
                    }
                    translation.setMacroName(macroName);
                }

                translations.add(translation);
            }
        }

        mav.addObject("dataTranslations", translations);

        return mav;

    }

    /**
     * The '/setTranslations{params}' function will handle taking in a selected field and a selected crosswalk and add it to an array of translations. This array will be used when the form is submitted to associate to the existing configuration / trasnort method combination.
     *
     * @param f	This will hold the id of the selected field cw	This will hold the id of the selected crosswalk fText	This will hold the text value of the selected field (used for display purposes) CWText	This will hold the text value of the selected crosswalk (used for display purposes)
     *
     * @Return	This function will return the existing translations view that will display the table of newly selected translations
     */
    @RequestMapping(value = "/setTranslations{params}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView setTranslations(
            @RequestParam(value = "f", required = true) Integer field, @RequestParam(value = "cw", required = true) Integer cwId, @RequestParam(value = "fText", required = true) String fieldText,
            @RequestParam(value = "CWText", required = true) String cwText, @RequestParam(value = "macroId", required = true) Integer macroId, 
            @RequestParam(value = "macroName", required = true) String macroName, @RequestParam(value = "fieldA", required = false) String fieldA, 
            @RequestParam(value = "fieldB") String fieldB, @RequestParam(value = "constant1") String constant1,
            @RequestParam(value = "constant2", required = false) String constant2, @RequestParam(value = "passClear") Integer passClear
    ) throws Exception {

        int processOrder = translations.size() + 1;

        if (macroId == null) {
            macroId = 0;
            macroName = null;
        }
        if (cwId == null) {
            cwId = 0;
            cwText = null;
        }

        configurationDataTranslations translation = new configurationDataTranslations();
        translation.setconfigId(configId);
        translation.setFieldId(field);
        translation.setfieldName(fieldText);
        translation.setMacroId(macroId);
        translation.setMacroName(macroName);
        translation.setCrosswalkId(cwId);
        translation.setcrosswalkName(cwText);
        translation.setFieldA(fieldA);
        translation.setFieldB(fieldB);
        translation.setConstant1(constant1);
        translation.setConstant2(constant2);
        translation.setProcessOrder(processOrder);
        translation.setPassClear(passClear);

        translations.add(translation);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/existingTranslations");
        mav.addObject("dataTranslations", translations);

        return mav;
    }

    /**
     * The 'removeTranslations{params}' function will handle removing a translation from translations array.
     *
     * @param	fieldId This will hold the field that is being removed
     * @param	processOrder	This will hold the process order of the field to be removed so we remove the correct field number as the same field could be in the list with different crosswalks
     *
     * @Return	1	The function will simply return a 1 back to the ajax call
     */
    @RequestMapping(value = "/removeTranslations{params}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Integer removeTranslation(@RequestParam(value = "fieldId", required = true) Integer fieldId, @RequestParam(value = "processOrder", required = true) Integer processOrder) throws Exception {

        Iterator<configurationDataTranslations> it = translations.iterator();
        int currProcessOrder;

        while (it.hasNext()) {
            configurationDataTranslations translation = it.next();
            if (translation.getFieldId() == fieldId && translation.getProcessOrder() == processOrder) {
                translations.remove(translation);
            } else if (translation.getProcessOrder() > processOrder) {
                currProcessOrder = translation.getProcessOrder();
                translation.setProcessOrder(currProcessOrder - 1);
            }
        }

        return 1;
    }

    /**
     * The 'updateTranslationProcessOrder{params}' function will handle removing a translation from translations array.
     *
     * @param	fieldId This will hold the field that is being removed
     * @param	processOrder	This will hold the process order of the field to be removed so we remove the correct field number as the same field could be in the list with different crosswalks
     *
     * @Return	1	The function will simply return a 1 back to the ajax call
     */
    @RequestMapping(value = "/updateTranslationProcessOrder{params}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Integer updateTranslationProcessOrder(@RequestParam(value = "currProcessOrder", required = true) Integer currProcessOrder, @RequestParam(value = "newProcessOrder", required = true) Integer newProcessOrder) throws Exception {

        Iterator<configurationDataTranslations> it = translations.iterator();

        while (it.hasNext()) {
            configurationDataTranslations translation = it.next();
            if (translation.getProcessOrder() == currProcessOrder) {
                translation.setProcessOrder(newProcessOrder);
            } else if (translation.getProcessOrder() == newProcessOrder) {
                translation.setProcessOrder(currProcessOrder);
            }
        }

        return 1;
    }
   
    /**
     * The '/connections' function will handle displaying the configuration connections screen.
     * The function will pass the existing connection objects for the selected configuration.
     * 
     * @Return the connection view and the following objects.
     * 
     *         organizations - list of available active organizations to connect to
     *                         this list will not contain any currently associated
     *                         organizations.
     *         
     *         connections  - list of currently associated organizations
     */
    @RequestMapping(value = "/connections", method = RequestMethod.GET)
    public ModelAndView getConnections(@RequestParam(value = "page", required = false) Integer page) throws Exception {
        
        if (page == null) {
            page = 1;
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/connections");
        mav.addObject("id", configId);
        mav.addObject("mappings", mappings);
        mav.addObject("HL7", HL7);
        
        /* get a list of all connections in the sysetm */
        List<configurationConnection> connections = configurationmanager.getAllConnections(page, maxResults);
        
        Long totalConnections = (long) 0;
        
        /* Loop over the connections to get the configuration details */
        if(connections != null) {
            for(configurationConnection connection : connections) {
                /* Array to holder the users */
                List<User> connectionSenders = new ArrayList<User>();
                List<User> connectonReceivers = new ArrayList<User>();
                
                configuration srcconfigDetails = configurationmanager.getConfigurationById(connection.getsourceConfigId());
                configurationTransport srctransportDetails = configurationTransportManager.getTransportDetails(srcconfigDetails.getId());
                
                srcconfigDetails.setOrgName(organizationmanager.getOrganizationById(srcconfigDetails.getorgId()).getOrgName());
                srcconfigDetails.setMessageTypeName(messagetypemanager.getMessageTypeById(srcconfigDetails.getMessageTypeId()).getName());
                srcconfigDetails.settransportMethod(configurationTransportManager.getTransportMethodById(srctransportDetails.gettransportMethodId()));
                if(srctransportDetails.gettransportMethodId() == 1 && srcconfigDetails.getType() == 2) {
                     srcconfigDetails.settransportMethod("File Download");
                }
                else {
                    srcconfigDetails.settransportMethod(configurationTransportManager.getTransportMethodById(srctransportDetails.gettransportMethodId()));
                }
                
                connection.setsrcConfigDetails(srcconfigDetails);
                
                configuration tgtconfigDetails = configurationmanager.getConfigurationById(connection.gettargetConfigId());
                configurationTransport tgttransportDetails = configurationTransportManager.getTransportDetails(tgtconfigDetails.getId());
                
                tgtconfigDetails.setOrgName(organizationmanager.getOrganizationById(tgtconfigDetails.getorgId()).getOrgName());
                tgtconfigDetails.setMessageTypeName(messagetypemanager.getMessageTypeById(tgtconfigDetails.getMessageTypeId()).getName());
                if(tgttransportDetails.gettransportMethodId() == 1 && tgtconfigDetails.getType() == 2) {
                     tgtconfigDetails.settransportMethod("File Download");
                }
                else {
                    tgtconfigDetails.settransportMethod(configurationTransportManager.getTransportMethodById(tgttransportDetails.gettransportMethodId()));
                }
                
                /* Get the list of connection senders */
                List<configurationConnectionSenders> senders = configurationmanager.getConnectionSenders(connection.getId());
                
                for(configurationConnectionSenders sender : senders) {
                    User userDetail = userManager.getUserById(sender.getuserId());
                    connectionSenders.add(userDetail);
                }
                connection.setconnectionSenders(connectionSenders);
                
                /* Get the list of connection receivers */
                List<configurationConnectionReceivers> receivers = configurationmanager.getConnectionReceivers(connection.getId());
                
                for(configurationConnectionReceivers receiver : receivers) {
                    User userDetail = userManager.getUserById(receiver.getuserId());
                    connectonReceivers.add(userDetail);
                }
                connection.setconnectionReceivers(connectonReceivers);
                
                
                connection.settgtConfigDetails(tgtconfigDetails);
            }
            
            /* Return the total list of connections */
            totalConnections = (long) connections.size();
        }
        
        mav.addObject("connections", connections);
        
        Integer totalPages = (int) Math.ceil((double)totalConnections / maxResults);
        
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", page);
       
        /* Set the variable to hold the number of completed steps for this configuration */
        mav.addObject("stepsCompleted", stepsCompleted);

        return mav;
    }
    
    /**
     * The '/connections' POST function will search the existing connection list by the entered search term.
     * 
     * @param searchTerm The term to search for.
     */
    @RequestMapping(value = "/connections", method = RequestMethod.POST)
    public ModelAndView getConnections(@RequestParam(value = "searchTerm", required = false) String searchTerm) throws Exception {
        

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/connections");
        mav.addObject("id", configId);
        mav.addObject("mappings", mappings);
        mav.addObject("searchTerm", searchTerm);
        mav.addObject("HL7", HL7);
        
        /* get a list of all connections in the sysetm */
        List<configurationConnection> connections = configurationmanager.findConnections(searchTerm);
        
        Long totalConnections = (long) 0;
        
        /* Loop over the connections to get the configuration details */
        if(connections != null) {
            for(configurationConnection connection : connections) {
                /* Array to holder the users */
                List<User> connectionSenders = new ArrayList<User>();
                List<User> connectonReceivers = new ArrayList<User>();
                
                configuration srcconfigDetails = configurationmanager.getConfigurationById(connection.getsourceConfigId());
                configurationTransport srctransportDetails = configurationTransportManager.getTransportDetails(srcconfigDetails.getId());
                
                srcconfigDetails.setOrgName(organizationmanager.getOrganizationById(srcconfigDetails.getorgId()).getOrgName());
                srcconfigDetails.setMessageTypeName(messagetypemanager.getMessageTypeById(srcconfigDetails.getMessageTypeId()).getName());
                srcconfigDetails.settransportMethod(configurationTransportManager.getTransportMethodById(srctransportDetails.gettransportMethodId()));
                if(srctransportDetails.gettransportMethodId() == 1 && srcconfigDetails.getType() == 2) {
                     srcconfigDetails.settransportMethod("File Download");
                }
                else {
                    srcconfigDetails.settransportMethod(configurationTransportManager.getTransportMethodById(srctransportDetails.gettransportMethodId()));
                }
                
                connection.setsrcConfigDetails(srcconfigDetails);
                
                configuration tgtconfigDetails = configurationmanager.getConfigurationById(connection.gettargetConfigId());
                configurationTransport tgttransportDetails = configurationTransportManager.getTransportDetails(tgtconfigDetails.getId());
                
                tgtconfigDetails.setOrgName(organizationmanager.getOrganizationById(tgtconfigDetails.getorgId()).getOrgName());
                tgtconfigDetails.setMessageTypeName(messagetypemanager.getMessageTypeById(tgtconfigDetails.getMessageTypeId()).getName());
                if(tgttransportDetails.gettransportMethodId() == 1 && tgtconfigDetails.getType() == 2) {
                     tgtconfigDetails.settransportMethod("File Download");
                }
                else {
                    tgtconfigDetails.settransportMethod(configurationTransportManager.getTransportMethodById(tgttransportDetails.gettransportMethodId()));
                }
                
                /* Get the list of connection senders */
                List<configurationConnectionSenders> senders = configurationmanager.getConnectionSenders(connection.getId());
                
                for(configurationConnectionSenders sender : senders) {
                    User userDetail = userManager.getUserById(sender.getuserId());
                    connectionSenders.add(userDetail);
                }
                connection.setconnectionSenders(connectionSenders);
                
                /* Get the list of connection receivers */
                List<configurationConnectionReceivers> receivers = configurationmanager.getConnectionReceivers(connection.getId());
                
                for(configurationConnectionReceivers receiver : receivers) {
                    User userDetail = userManager.getUserById(receiver.getuserId());
                    connectonReceivers.add(userDetail);
                }
                connection.setconnectionReceivers(connectonReceivers);
                
                
                connection.settgtConfigDetails(tgtconfigDetails);
            }
            
            /* Return the total list of connections */
            totalConnections = (long) connections.size();
        }
        
        mav.addObject("connections", connections);
        
        Integer totalPages = (int) Math.ceil((double)totalConnections / maxResults);
        
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", 1);
       
        /* Set the variable to hold the number of completed steps for this configuration */
        mav.addObject("stepsCompleted", stepsCompleted);

        return mav;
    }
    
    /**
     * The '/createConnection' function will handle displaying the create configuration connection screen.
     * 
     * @return This function will display the new connection overlay
     */
    @RequestMapping(value = "/createConnection", method = RequestMethod.GET)
    public @ResponseBody ModelAndView createNewConnectionForm() throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/connectionDetails");
        
        configurationConnection connectionDetails = new configurationConnection();
        mav.addObject("connectionDetails", connectionDetails);
        
        //Need to get a list of active organizations.
        List<Organization> organizations = organizationmanager.getAllActiveOrganizations();
        mav.addObject("organizations", organizations);
        
        return mav;
    }
    
    /**
     * The '/editConnection' funtion will handle displaying the edit configuration connection screen.
     * 
     * @param connectionId The id of the clicked configuration connection
     * 
     * @return This function will display the edit connection overlay
     */
    @RequestMapping(value = "/editConnection", method = RequestMethod.GET)
    public @ResponseBody ModelAndView editConnectionForm(@RequestParam(value = "connectionId", required = true) int connectionId) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/connectionDetails");
        
        configurationConnection connectionDetails = configurationmanager.getConnection(connectionId);
        
        configuration srcconfigDetails = configurationmanager.getConfigurationById(connectionDetails.getsourceConfigId());
        srcconfigDetails.setorgId(organizationmanager.getOrganizationById(srcconfigDetails.getorgId()).getId());
        connectionDetails.setsrcConfigDetails(srcconfigDetails);
        
        configuration tgtconfigDetails = configurationmanager.getConfigurationById(connectionDetails.gettargetConfigId());
        tgtconfigDetails.setorgId(organizationmanager.getOrganizationById(tgtconfigDetails.getorgId()).getId());
        connectionDetails.settgtConfigDetails(tgtconfigDetails);
        
        List<configurationConnectionSenders> senders = configurationmanager.getConnectionSenders(connectionId);
        
        /* Array to holder the users */
        List<User> connectionSenders = new ArrayList<User>();
        List<User> connectonReceivers = new ArrayList<User>();
                
        for(configurationConnectionSenders sender : senders) {
            User userDetail = userManager.getUserById(sender.getuserId());
            connectionSenders.add(userDetail);
        }
        connectionDetails.setconnectionSenders(connectionSenders);

        /* Get the list of connection receivers */
        List<configurationConnectionReceivers> receivers = configurationmanager.getConnectionReceivers(connectionId);

        for(configurationConnectionReceivers receiver : receivers) {
            User userDetail = userManager.getUserById(receiver.getuserId());
            connectonReceivers.add(userDetail);
        }
        connectionDetails.setconnectionReceivers(connectonReceivers);
        
        mav.addObject("connectionDetails", connectionDetails);
        
        //Need to get a list of active organizations.
        List<Organization> organizations = organizationmanager.getAllActiveOrganizations();
        mav.addObject("organizations", organizations);
        
        return mav;
    }
    
    /**
     * The '/getAvailableConfigurations.do' function will return a list of configuration that have been
     * set up for the passed in organization.
     * 
     * @param   orgId   The organization selected in the drop down
     * 
     * @return  configurations  The available configurations
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value= "/getAvailableConfigurations.do", method = RequestMethod.GET)
    public @ResponseBody List<configuration> getAvailableConfigurations(@RequestParam(value = "orgId", required = true) int orgId) throws Exception {
        
        List<configuration> configurations = configurationmanager.getActiveConfigurationsByOrgId(orgId);
        
        if(configurations != null) {
            for(configuration configuration : configurations) {
                configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configuration.getId());
                
                configuration.setOrgName(organizationmanager.getOrganizationById(configuration.getorgId()).getOrgName());
                configuration.setMessageTypeName(messagetypemanager.getMessageTypeById(configuration.getMessageTypeId()).getName());
                configuration.settransportMethod(configurationTransportManager.getTransportMethodById(transportDetails.gettransportMethodId()));
            }
        }
        
        return configurations;
    } 
    
     /**
     * The '/addConnection.do' POST request will create the connection between the passed in organization
     * and the configuration.
     *
     * @param	srcConfig     The selected source configuration
     * @param   tgtConfig     The selected target configuration
     *
     * @return	The method will return a 1 back to the calling ajax function which will handle the page load.
     */
    @RequestMapping(value = "/addConnection.do", method = RequestMethod.POST)
    public ModelAndView addConnection (@ModelAttribute(value = "connectionDetails") configurationConnection connectionDetails, @RequestParam List<Integer> srcUsers, @RequestParam List<Integer> tgtUsers, RedirectAttributes redirectAttr) throws Exception {
       
        connectionDetails.setStatus(true);
        Integer connectionId;
        
        if(connectionDetails.getId() == 0) {
            connectionId = configurationmanager.saveConnection(connectionDetails);
            redirectAttr.addFlashAttribute("savedStatus", "created");
        }
        else {
            connectionId = connectionDetails.getId();
            configurationmanager.updateConnection(connectionDetails);
            
            /* Delete existing senders and receivers */
            configurationmanager.removeConnectionSenders(connectionId);
            configurationmanager.removeConnectionReceivers(connectionId);
            redirectAttr.addFlashAttribute("savedStatus", "updated");
        }
        
        for(Integer sender : srcUsers) {
            configurationConnectionSenders senderInfo = new configurationConnectionSenders();
            senderInfo.setconnectionId(connectionId);
            senderInfo.setuserId(sender);
            configurationmanager.saveConnectionSenders(senderInfo);
        }
        
        for(Integer receiver : tgtUsers) {
            configurationConnectionReceivers receiverInfo = new configurationConnectionReceivers();
            receiverInfo.setconnectionId(connectionId);
            receiverInfo.setuserId(receiver);
            configurationmanager.saveConnectionReceivers(receiverInfo);
        }
        
       ModelAndView mav = new ModelAndView(new RedirectView("connections"));
       
       return mav;
        
    }
    
    /**
     * The '/changeConnectionStatus.do' POST request will update the passed in connection status.
     * 
     * @param connectionId  The id for the connection to update the status for
     * @param statusVal     The new status for the connection
     * 
     * @return  The method will return a 1 back to the calling ajax function.
     */
    @RequestMapping(value= "/changeConnectionStatus.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer changeConnectionStatus(@RequestParam boolean statusVal, @RequestParam int connectionId) throws Exception {
        
       configurationConnection connection = configurationmanager.getConnection(connectionId);
       connection.setStatus(statusVal);
       configurationmanager.updateConnection(connection);
        
       return 1;
    }
    
    
    /**
     * The '/scheduling' GET request will display the scheduling page for the selected transport Method
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/scheduling", method = RequestMethod.GET)
    public ModelAndView getConfigurationSchedules() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/schedule");
        mav.addObject("id", configId);
        mav.addObject("mappings", mappings);
        mav.addObject("HL7", HL7);
        
        //Get the completed steps for the selected configuration;
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);
        
        //Get the transport details by configid and selected transport method
        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configId);
        
        configurationDetails.setOrgName(organizationmanager.getOrganizationById(configurationDetails.getorgId()).getOrgName());
        configurationDetails.setMessageTypeName(messagetypemanager.getMessageTypeById(configurationDetails.getMessageTypeId()).getName());
        configurationDetails.settransportMethod(configurationTransportManager.getTransportMethodById(transportDetails.gettransportMethodId()));
        
        //pass the configuration detail object back to the page.
        mav.addObject("configurationDetails", configurationDetails);
        
        //Get the schedule for the configuration and selected transport method
        configurationSchedules scheduleDetails = configurationmanager.getScheduleDetails(configId);
        
        if(scheduleDetails == null) {
            scheduleDetails = new configurationSchedules();
            scheduleDetails.setconfigId(configId);
        }
        mav.addObject("scheduleDetails", scheduleDetails);  
        
        //Set the variable to hold the number of completed steps for this configuration;
        mav.addObject("stepsCompleted", stepsCompleted);

        return mav;
    }
    
    /**
     * The '/scheduling' POST request will submit the scheduling settings for the selected configuration.
     * 
     * @param scheduleDetails   The object that will hold the scheduling form fields
     * 
     * @return This method will redirect the user back to the scheduling form page.
     */
    @RequestMapping(value = "/scheduling", method = RequestMethod.POST)
    public ModelAndView submitConfigurationSchedules(@ModelAttribute(value = "scheduleDetails") configurationSchedules scheduleDetails, RedirectAttributes redirectAttr) throws Exception {
       
       //Set default values based on what schedule type is selected
       //This will help in case the user was switching around selecting
       //values before they saved
        
       //Manually
       if(scheduleDetails.gettype() == 1 || scheduleDetails.gettype() == 5) {
           scheduleDetails.setprocessingType(0);
           scheduleDetails.setnewfileCheck(0);
           scheduleDetails.setprocessingDay(0);
           scheduleDetails.setprocessingTime(0);
       }
       //Daily
       else if (scheduleDetails.gettype() == 2) {
           scheduleDetails.setprocessingDay(0);
           if(scheduleDetails.getprocessingType() == 1) {
               scheduleDetails.setnewfileCheck(0);
           }
           else {
               scheduleDetails.setprocessingTime(0);
           }
       }
       //Weekly
       else if(scheduleDetails.gettype() == 3) {
           scheduleDetails.setprocessingType(0);
           scheduleDetails.setnewfileCheck(0);
       }
       //Monthly
       else if(scheduleDetails.gettype() == 3) {
           scheduleDetails.setprocessingType(0);
           scheduleDetails.setnewfileCheck(0);
           scheduleDetails.setprocessingDay(0);
       }
        
       
       configurationmanager.saveSchedule(scheduleDetails);
       
       //Update the configuration completed step
       configuration configurationDetails = configurationmanager.getConfigurationById(configId);
        if (configurationDetails.getstepsCompleted() < 6) {
            configurationmanager.updateCompletedSteps(configId, 6);
        }
       
       redirectAttr.addFlashAttribute("savedStatus", "updated");

       ModelAndView mav = new ModelAndView(new RedirectView("scheduling"));
       return mav;
    
    }
    
    /**
     * The '/HL7' GET request will display the HL7 customization form.
     */
    @RequestMapping(value = "/HL7", method = RequestMethod.GET)
    public ModelAndView getHL7Form() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/HL7");
        mav.addObject("id", configId);
        mav.addObject("mappings", mappings);
        mav.addObject("HL7", HL7);
        
        //Get the completed steps for the selected configuration;
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);
        
        //Get the transport details by configid and selected transport method
        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configId);
        
        configurationDetails.setOrgName(organizationmanager.getOrganizationById(configurationDetails.getorgId()).getOrgName());
        configurationDetails.setMessageTypeName(messagetypemanager.getMessageTypeById(configurationDetails.getMessageTypeId()).getName());
        configurationDetails.settransportMethod(configurationTransportManager.getTransportMethodById(transportDetails.gettransportMethodId()));
        
        //pass the configuration detail object back to the page.
        mav.addObject("configurationDetails", configurationDetails);
        
        //Set the variable to hold the number of completed steps for this configuration;
        mav.addObject("stepsCompleted", stepsCompleted);
        
        HL7Details hl7Details = configurationmanager.getHL7Details(configId);
        int HL7Id = 0;
        
        /* If null then create an empty HL7 Detail object */
        if(hl7Details == null) {
            HL7Details hl7DetailsEmpty = new HL7Details();
            hl7DetailsEmpty.setconfigId(configId);
            mav.addObject("HL7Details", hl7DetailsEmpty);
        }
        else {
            HL7Id = hl7Details.getId();
            
            /* Get a list of HL7 Segments */
            List<HL7Segments> HL7Segments = configurationmanager.getHL7Segments(HL7Id);

            /* Get a list of HL7Elements */
            if(!HL7Segments.isEmpty()) {
                for(HL7Segments segment : HL7Segments) {

                    List<HL7Elements> HL7Elments = configurationmanager.getHL7Elements(HL7Id, segment.getId());

                    if(!HL7Elments.isEmpty()) {
                        
                        for(HL7Elements element : HL7Elments) {
                            List<HL7ElementComponents> components = configurationmanager.getHL7ElementComponents(element.getId());
                            element.setelementComponents(components);
                        }
                        
                        segment.setHL7Elements(HL7Elments);
                    }

                }
            }
            hl7Details.setHL7Segments(HL7Segments);
            
            mav.addObject("HL7Details", hl7Details);
            
        }
        
        //Get the transport fields
        List<configurationFormFields> fields = configurationTransportManager.getConfigurationFields(configId, transportDetails.getId());
        transportDetails.setFields(fields);

        mav.addObject("fields", fields);
        
        return mav;
    }
    
    
    /**
     * The '/HL7' POST request save all the hl7 custom settings
     */
    @RequestMapping(value = "/HL7", method = RequestMethod.POST)
    public ModelAndView saveHL7Customization(@ModelAttribute(value = "HL7Details") HL7Details HL7Details, RedirectAttributes redirectAttr) throws Exception {
        
        /* Update the details of the hl7 */
        configurationmanager.updateHL7Details(HL7Details);
        
        List<HL7Segments> segments = HL7Details.getHL7Segments();
        
        if (null != segments && segments.size() > 0) {
            
            for (HL7Segments segment : segments) {
                
                /* Update each segment */
                configurationmanager.updateHL7Segments(segment);
                
                /* Get the list of segment elements */
                List<HL7Elements> elements = segment.getHL7Elements();
                
                if (null != elements && elements.size() > 0) {
                    
                     for (HL7Elements element : elements) {
                         configurationmanager.updateHL7Elements(element);
                         
                         
                         /* Get the list of segment element components */
                        List<HL7ElementComponents> components = element.getelementComponents();
                        
                        if (null != components && components.size() > 0) {
                            for (HL7ElementComponents component : components) {
                                configurationmanager.updateHL7ElementComponent(component);
                            }
                        }
                         
                     }
                    
                }
                
            }
        }
        
        redirectAttr.addFlashAttribute("savedStatus", "updated");
        ModelAndView mav = new ModelAndView(new RedirectView("HL7"));
        return mav;
        
        
    }
    
    /**
     * The '/newHL7Segment' GET request will be used to display the blank new HL7 Segment screen (In a modal)
     *
     *
     * @return	The HL7 Segment blank form page
     *
     * @Objects	An object that will hold all the form fields of a new HL7 Segment 
     *
     */
    @RequestMapping(value = "/newHL7Segment", method = RequestMethod.GET)
    public @ResponseBody ModelAndView newHL7Segment(@RequestParam(value = "hl7Id", required = true) int hl7Id, @RequestParam(value = "nextPos", required = true) int nextPos) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/HL7Segment");
        
        HL7Segments segmentDetails = new HL7Segments();
        segmentDetails.sethl7Id(hl7Id);
        segmentDetails.setdisplayPos(nextPos);
        
        mav.addObject("HL7SegmentDetails", segmentDetails);

        return mav;
    }
    
    /**
     * The '/saveHL7Segment' POST request will handle submitting the new HL7 Segment
     *
     * @param HL7SegmentDetails	The object containing the HL7 Segment form fields
     * @param result	The validation result
     * @param redirectAttr	The variable that will hold values that can be read after the redirect
     *
     * @return	Will return the HL7 Customization page on "Save"
     *
     * @throws Exception
     */
    @RequestMapping(value = "/saveHL7Segment", method = RequestMethod.POST)
    public ModelAndView saveHL7Segment(@ModelAttribute(value = "HL7SegmentDetails") HL7Segments HL7SegmentDetails, RedirectAttributes redirectAttr) throws Exception {

        configurationmanager.saveHL7Segment(HL7SegmentDetails);

        redirectAttr.addFlashAttribute("savedStatus", "savedSegment");
        ModelAndView mav = new ModelAndView(new RedirectView("HL7"));
        return mav;
    }
    
    /**
     * The '/newHL7Element' GET request will be used to display the blank new HL7 Segment Element screen (In a modal)
     *
     *
     * @return	The HL7 Segment Element blank form page
     *
     * @Objects	An object that will hold all the form fields of a new HL7 Segment Element 
     *
     */
    @RequestMapping(value = "/newHL7Element", method = RequestMethod.GET)
    public @ResponseBody ModelAndView newHL7Element(@RequestParam(value = "hl7Id", required = true) int hl7Id, @RequestParam(value = "segmentId", required = true) int segmentId, @RequestParam(value = "nextPos", required = true) int nextPos) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/HL7Element");
        
        HL7Elements elementDetails = new HL7Elements();
        elementDetails.sethl7Id(hl7Id);
        elementDetails.setsegmentId(segmentId);
        elementDetails.setdisplayPos(nextPos);
        
        mav.addObject("HL7ElementDetails", elementDetails);

        return mav;
    }
    
    /**
     * The '/saveHL7Element' POST request will handle submitting the new HL7 Segment Element
     *
     * @param HL7ElementDetails	The object containing the HL7 Segment Element form fields
     * @param result	The validation result
     * @param redirectAttr	The variable that will hold values that can be read after the redirect
     *
     * @return	Will return the HL7 Customization page on "Save"
     *
     * @throws Exception
     */
    @RequestMapping(value = "/saveHL7Element", method = RequestMethod.POST)
    public ModelAndView saveHL7Element(@ModelAttribute(value = "HL7ElementDetails") HL7Elements HL7ElementDetails, RedirectAttributes redirectAttr) throws Exception {

        configurationmanager.saveHL7Element(HL7ElementDetails);

        redirectAttr.addFlashAttribute("savedStatus", "savedElement");
        ModelAndView mav = new ModelAndView(new RedirectView("HL7"));
        return mav;
    }
    
    /**
     * The '/newHL7Component' GET request will be used to display the blank new HL7 Element Component screen (In a modal)
     *
     *
     * @return	The HL7 Element Component blank form page
     *
     * @Objects	An object that will hold all the form fields of a new HL7 Element Component 
     *
     */
    @RequestMapping(value = "/newHL7Component", method = RequestMethod.GET)
    public @ResponseBody ModelAndView newHL7Component(@RequestParam(value = "elementId", required = true) int elementId, @RequestParam(value = "nextPos", required = true) int nextPos) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/HL7Component");
        
        HL7ElementComponents componentDetails = new HL7ElementComponents();
        componentDetails.setelementId(elementId);
        componentDetails.setdisplayPos(nextPos);
        
        mav.addObject("HL7ComponentDetails", componentDetails);
        
        //Get the transport fields
        //Get the transport details by configid and selected transport method
        configurationTransport transportDetails = configurationTransportManager.getTransportDetails(configId);
        List<configurationFormFields> fields = configurationTransportManager.getConfigurationFields(configId, transportDetails.getId());
        transportDetails.setFields(fields);

        mav.addObject("fields", fields);

        return mav;
    }
    
    /**
     * The '/saveHL7Component' POST request will handle submitting the new HL7 Segment Element
     *
     * @param HL7ComponentDetails  The object containing the HL7 Element Component form fields
     * @param result	The validation result
     * @param redirectAttr	The variable that will hold values that can be read after the redirect
     *
     * @return	Will return the HL7 Customization page on "Save"
     *
     * @throws Exception
     */
    @RequestMapping(value = "/saveHL7Component", method = RequestMethod.POST)
    public ModelAndView saveHL7Component(@ModelAttribute(value = "HL7ComponentDetails") HL7ElementComponents HL7ComponentDetails, RedirectAttributes redirectAttr) throws Exception {

        configurationmanager.saveHL7Component(HL7ComponentDetails);

        redirectAttr.addFlashAttribute("savedStatus", "savedComponent");
        ModelAndView mav = new ModelAndView(new RedirectView("HL7"));
        return mav;
    }
    
    /**
     * The 'testFTPConnection.do' method will test the FTP connection paramenters.
     */
    @RequestMapping(value = "/testFTPConnection.do", method = RequestMethod.GET)
    public @ResponseBody String testFTPPushConnection(@RequestParam String protocol, @RequestParam String ip, @RequestParam String username, @RequestParam String password, @RequestParam String directory, @RequestParam Integer port) {
        
        String connectionResponse = null;
        
        try {
        
            FTPClient ftp;

            if("FTP".equals(protocol)) {
                ftp = new FTPClient();
            }
            else {
                FTPSClient ftps;
                ftps = new FTPSClient(true);

                ftp = ftps;
                ftps.setTrustManager(null);
                
            }

            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            ftp.setDefaultTimeout(3000);
            ftp.setConnectTimeout(2000);
            
            if(port > 0) {
                ftp.connect(ip,port);
            }
            else {
                ftp.connect(ip);
            }


            int reply = ftp.getReplyCode();
            if(!FTPReply.isPositiveCompletion(reply)) {
                 connectionResponse = ftp.getReplyString();
                 ftp.disconnect();
            }
            else {
                 ftp.login(username, password);
                 ftp.enterLocalPassiveMode();
                 
                 if(!"".equals(directory)) {
                     ftp.changeWorkingDirectory(directory);
                 }
                 
                 connectionResponse = ftp.getReplyString();
                 
                 ftp.logout();
                 ftp.disconnect();

            } 
        }
        catch(Exception e) {
            connectionResponse = "Connecton not valid";
        }
        
        return connectionResponse;
        
    }
    
    
}
