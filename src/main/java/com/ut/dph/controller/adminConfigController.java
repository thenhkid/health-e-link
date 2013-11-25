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

import com.ut.dph.model.Connections;
import com.ut.dph.model.Crosswalks;
import com.ut.dph.model.Macros;
import com.ut.dph.model.configuration;
import com.ut.dph.model.configurationDataTranslations;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.messageTypeFormFields;
import com.ut.dph.service.configurationManager;
import com.ut.dph.model.Organization;
import com.ut.dph.model.configurationSchedules;
import com.ut.dph.service.organizationManager;
import com.ut.dph.model.messageType;
import com.ut.dph.service.messageTypeManager;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.service.configurationTransportManager;

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
    private configurationTransportManager configurationTransportManager;

    /**
     * The private variable configId will hold the configurationId when viewing a configuration this will be used when on a configuration subsections like Field Mappings, Data Translations, etc. We will use this private variable so we don't have to go fetch the id
     */
    private static int configId = 0;

    /**
     * The private maxResults variable will hold the number of results to show per list page.
     */
    private static final int maxResults = 20;

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
        mav.addObject("configurationList", configurations);

        Organization org;
        messageType messagetype;

        for (configuration config : configurations) {
            org = organizationmanager.getOrganizationById(config.getorgId());
            config.setOrgName(org.getOrgName());

            messagetype = messagetypemanager.getMessageTypeById(config.getMessageTypeId());
            config.setMessageTypeName(messagetype.getName());

            Long totalConnections = (Long) configurationmanager.getTotalConnections(configId);
            config.setTotalConnections(totalConnections);

        }

        //Return the total list of configurations
        Long totalConfigs = configurationmanager.findTotalConfigs();

        Integer totalPages = Math.round(totalConfigs / maxResults);
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
    public ModelAndView findConfigurations(@RequestParam String searchTerm) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/list");

        List<configuration> configurations = configurationmanager.findConfigurations(searchTerm);
        mav.addObject("searchTerm", searchTerm);
        mav.addObject("configurationList", configurations);

        Organization org;
        messageType messagetype;

        for (configuration config : configurations) {
            org = organizationmanager.getOrganizationById(config.getorgId());
            config.setOrgName(org.getOrgName());

            messagetype = messagetypemanager.getMessageTypeById(config.getMessageTypeId());
            config.setMessageTypeName(messagetype.getName());

            Long totalConnections = (Long) configurationmanager.getTotalConnections(configId);
            config.setTotalConnections(totalConnections);
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
    public ModelAndView saveNewConfiguration(@Valid @ModelAttribute(value = "configurationDetails") configuration configurationDetails, BindingResult result, RedirectAttributes redirectAttr, @RequestParam String action) throws Exception {

        //Need to get a list of active organizations.
        List<Organization> organizations = organizationmanager.getAllActiveOrganizations();

        //Need to get a list of active message types
        List<messageType> messageTypes = messagetypemanager.getActiveMessageTypes();

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/administrator/configurations/details");

            //Need to get a list of active organizations.
            mav.addObject("organizations", organizations);

            //Need to get a list of active message types
            mav.addObject("messageTypes", messageTypes);

            return mav;
        }

        Integer id = (Integer) configurationmanager.createConfiguration(configurationDetails);

        configId = id;

	//Need to set up the online form transport method
        //setupOnlineForm(configId,messageTypeId)
        configurationTransportManager.setupOnlineForm(configId, configurationDetails.getMessageTypeId());

        redirectAttr.addFlashAttribute("savedStatus", "created");
        ModelAndView mav = new ModelAndView(new RedirectView("details"));
        return mav;

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
        mav.addObject("completedSteps", configurationDetails.getstepsCompleted());

        //Need to get a list of active organizations.
        List<Organization> organizations = organizationmanager.getAllActiveOrganizations();
        mav.addObject("organizations", organizations);

        //Need to get a list of active message types
        List<messageType> messageTypes = messagetypemanager.getActiveMessageTypes();
        mav.addObject("messageTypes", messageTypes);
        mav.addObject("id", configId);

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
    public ModelAndView updateConfigurationDetails(@Valid @ModelAttribute(value = "configurationDetails") configuration configurationDetails, BindingResult result, RedirectAttributes redirectAttr, @RequestParam String action) throws Exception {

        //Need to get a list of active organizations.
        List<Organization> organizations = organizationmanager.getAllActiveOrganizations();

        //Need to get a list of active message types
        List<messageType> messageTypes = messagetypemanager.getActiveMessageTypes();

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/administrator/configurations/details");

            //Need to get a list of active organizations.
            mav.addObject("organizations", organizations);
            //Need to get a list of active message types
            mav.addObject("messageTypes", messageTypes);
            mav.addObject("id", configId);

            return mav;
        }

        //submit the updates
        configurationmanager.updateConfiguration(configurationDetails);

        //If the "Save" button was pressed 
        if (action.equals("save")) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/administrator/configurations/details");

            //Need to get a list of active organizations.
            mav.addObject("organizations", organizations);
            //Need to get a list of active message types
            mav.addObject("messageTypes", messageTypes);
            mav.addObject("id", configId);
            mav.addObject("savedStatus", "updated");

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

        //Get the transport details
        List<configurationTransport> transportDetails = configurationTransportManager.getTransportDetails(configId);

        //Set the variable id to hold the current configuration id
        mav.addObject("id", configId);

        //Get the configuration details for the selected config
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);
        configurationDetails.setTransportDetails(transportDetails);

        //pass the configuration detail object back to the page.
        mav.addObject("configurationDetails", configurationDetails);

        //Set the variable to hold the number of completed steps for this configuration;
        mav.addObject("completedSteps", configurationDetails.getstepsCompleted());

        //Get the list of available transport methods
        List transportMethods = configurationTransportManager.getTransportMethods();
        mav.addObject("transportMethods", transportMethods);

        //Set a list of transport methods already set up for the configuration
        List<Integer> transportList = new ArrayList<Integer>();

        for (configurationTransport details : transportDetails) {
            transportList.add(details.gettransportMethod());
        }
        mav.addObject("usedTransportMethods", transportList);

        //Get the list of available file delimiters
        List delimiters = messagetypemanager.getDelimiters();
        mav.addObject("delimiters", delimiters);

        //Get the list of available file types
        List fileTypes = configurationmanager.getFileTypes();
        mav.addObject("fileTypes", fileTypes);

        return mav;

    }

    /**
     * The 'addTransportMethod.do' POST request will associate the configuration to the selected transport Method.
     *
     * @param	configId	The configId will hold the id of the current configuration
     * @param	transportMethod	The transportMethod will hold the selected transport method
     *
     * @return	The method will return a 1 back to the calling ajax function which will handle the page load.
     */
    @RequestMapping(value = "/addTransportMethod.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Integer addNewTransportMethod(@RequestParam int configId, @RequestParam int transportMethod, RedirectAttributes redirectAttr) throws Exception {

        configurationTransport transportDetails = new configurationTransport();
        transportDetails.setconfigId(configId);
        transportDetails.settransportMethod(transportMethod);
        transportDetails.setFile(null);

        configurationTransportManager.updateTransportDetails(transportDetails);

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
    public ModelAndView updateTransportDetails(@Valid @ModelAttribute(value = "transportDetails") configuration configurationDetails, BindingResult result, RedirectAttributes redirectAttr, @RequestParam String action) throws Exception {

        //Need to update the configuration completed step
        if (configurationDetails.getstepsCompleted() < 2) {
            configurationmanager.updateCompletedSteps(configId, 2);
        }

        List<configurationTransport> details = configurationDetails.getTransportDetails();

        for (configurationTransport transport : details) {
            //submit the updates
            configurationTransportManager.updateTransportDetails(transport);
        }

        redirectAttr.addFlashAttribute("savedStatus", "updated");

        //If the "Save" button was pressed 
        if (action.equals("save")) {
            ModelAndView mav = new ModelAndView(new RedirectView("transport"));
            return mav;
        } else {
            ModelAndView mav = new ModelAndView(new RedirectView("mappings"));
            return mav;
        }

    }

    /**
     * The '/mappings' GET request will determine based on the selected transport method what page to display. Either the choose fields page if 'online form' is selected or 'mappings' if a custom file is being uploaded.
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/mappings", method = RequestMethod.GET)
    public ModelAndView getConfigurationMappings(@RequestParam(value = "i", required = false) Integer transportMethod) throws Exception {
        int selTransportMethod = 2;

        if (transportMethod != null) {
            selTransportMethod = transportMethod;
        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("id", configId);

        //Get the completed steps for the selected configuration;
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);
        mav.addObject("completedSteps", configurationDetails.getstepsCompleted());

        //Get a list of all transport details
        List<configurationTransport> allTransportDetails = configurationTransportManager.getTransportDetails(configId);

        //Get the transport details by configid and selected transport method
        configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(configId, selTransportMethod);

        //Get the transport fields
        List<configurationFormFields> fields = configurationTransportManager.getConfigurationFields(configId, transportDetails.getId());
        transportDetails.setFields(fields);

        mav.addObject("transportDetails", transportDetails);

        //If online form is selected then show the choose fields page
        if (selTransportMethod == 2) {
            mav.setViewName("/administrator/configurations/chooseFields");

            //Get the list of available field validation types
            List validationTypes = messagetypemanager.getValidationTypes();
            mav.addObject("validationTypes", validationTypes);

        } //for everything else we need to show the mappings page.
        else {

            //Need to get the template fields
            List<messageTypeFormFields> templateFields = messagetypemanager.getMessageTypeFields(configurationDetails.getMessageTypeId());
            mav.addObject("templateFields", templateFields);

            mav.setViewName("/administrator/configurations/mappings");
        }
        mav.addObject("selTransportMethod", selTransportMethod);

        //Get the list of available transport methods
        List transportMethods = configurationTransportManager.getTransportMethods();
        mav.addObject("transportMethods", transportMethods);

        //Set a list of transport methods already set up for the configuration
        List<Integer> transportList = new ArrayList<Integer>();

        for (configurationTransport details : allTransportDetails) {
            transportList.add(details.gettransportMethod());
        }
        mav.addObject("availTransportMethods", transportList);

        return mav;
    }

    /**
     * The 'saveFields' POST method will submit the changes to the form field settings for the selected configuration. This method is only for configurations set for 'Online Form' as the data transportation method.
     *
     * @param	transportDetails	The field details from the form action	The field that will hold which button was pressed "Save" or "Next Step"
     *
     * @return	This method will either redirect back to the Choose Fields page or redirect to the next step data translations page.
     */
    @RequestMapping(value = "/saveFields", method = RequestMethod.POST)
    public ModelAndView saveFormFields(@Valid @ModelAttribute(value = "transportDetails") configurationTransport transportDetails, RedirectAttributes redirectAttr, @RequestParam String action, @RequestParam int transportMethod) throws Exception {

        //Get the configuration details for the selected config
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);

        //Update the configuration completed step
        if (configurationDetails.getstepsCompleted() < 3) {
            configurationmanager.updateCompletedSteps(configId, 3);
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

        redirectAttr.addFlashAttribute("savedStatus", "updated");

        //If the "Save" button was pressed 
        if (action.equals("save")) {
            ModelAndView mav = new ModelAndView(new RedirectView("mappings?i=" + transportMethod));
            return mav;
        } else {
            ModelAndView mav = new ModelAndView(new RedirectView("translations"));
            return mav;
        }

    }

    /**
     * The '/translations' GET request will display the data translations page for the selected transport Method
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/translations", method = RequestMethod.GET)
    public ModelAndView getConfigurationTranslations(@RequestParam(value = "i", required = false) Integer transportMethod) throws Exception {
        int selTransportMethod = 2;

        if (transportMethod != null) {
            selTransportMethod = transportMethod;
        }

        //Set the data translations array to get ready to hold data
        translations = new CopyOnWriteArrayList<configurationDataTranslations>();

        //Get the completed steps for the selected configuration;
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/translations");
        mav.addObject("id", configId);
        mav.addObject("completedSteps", configurationDetails.getstepsCompleted());

        //Get a list of all transport details
        List<configurationTransport> allTransportDetails = configurationTransportManager.getTransportDetails(configId);

        //Get the transport details by configid and selected transport method
        configurationTransport transportDetails = configurationTransportManager.getTransportDetailsByTransportMethod(configId, selTransportMethod);

        //Get the transport fields
        List<configurationFormFields> fields = configurationTransportManager.getConfigurationFields(configId, transportDetails.getId());
        transportDetails.setFields(fields);

        mav.addObject("fields", fields);
        mav.addObject("selTransportMethod", selTransportMethod);

        //Return a list of available crosswalks
        List<Crosswalks> crosswalks = messagetypemanager.getCrosswalks(1, 0, configurationDetails.getorgId());
        mav.addObject("crosswalks", crosswalks);
        mav.addObject("orgId", configurationDetails.getorgId());

        //Return a list of available macros
        List<Macros> macros = configurationmanager.getMacros();
        mav.addObject("macros", macros);

        //Get the list of available transport methods
        List transportMethods = configurationTransportManager.getTransportMethods();
        mav.addObject("transportMethods", transportMethods);

        //Set a list of transport methods already set up for the configuration
        List<Integer> transportList = new ArrayList<Integer>();

        for (configurationTransport details : allTransportDetails) {
            transportList.add(details.gettransportMethod());
        }
        mav.addObject("availTransportMethods", transportList);

        return mav;

    }

    /**
     * The '/translations' POST request will submit the selected data translations and save it to the data base.
     *
     */
    @RequestMapping(value = "/translations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Integer submitDataTranslations(@RequestParam(value = "transportMethod", required = true) Integer transportMethod) throws Exception {

        //Update the configuration completed step
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);
        if (configurationDetails.getstepsCompleted() < 4) {
            configurationmanager.updateCompletedSteps(configId, 4);
        }

		 //Delete all the data translations before creating
        //This will help with the jquery removing translations
        configurationmanager.deleteDataTranslations(configId, transportMethod);

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
    public @ResponseBody
    ModelAndView getTranslations(@RequestParam(value = "reload", required = true) boolean reload, @RequestParam(value = "transportMethod", required = true) int transportMethod) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/existingTranslations");

		//only get the saved translations if reload == 0
        //We only want to retrieve the saved ones on initial load
        if (reload == false) {
            //Need to get a list of existing translations
            List<configurationDataTranslations> existingTranslations = configurationmanager.getDataTranslations(configId, transportMethod);

            for (configurationDataTranslations translation : existingTranslations) {
                //Get the field name by id
                String fieldName = configurationmanager.getFieldName(translation.getFieldId());
                translation.setfieldName(fieldName);

                //Get the crosswalk name by id
                String crosswalkName = messagetypemanager.getCrosswalkName(translation.getCrosswalkId());
                translation.setcrosswalkName(crosswalkName);

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
            @RequestParam(value = "CWText", required = true) String cwText, @RequestParam(value = "transportMethod", required = true) int transportMethod,
            @RequestParam(value = "macroId", required = true) Integer macroId, @RequestParam(value = "macroName", required = true) String macroName,
            @RequestParam(value = "fieldA", required = false) String fieldA, @RequestParam(value = "fieldB") String fieldB, @RequestParam(value = "constant1") String constant1,
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
        translation.settransportMethod(transportMethod);
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
    public ModelAndView getConnections() throws Exception {

        //Get the completed steps for the selected configuration;
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/connections");
        mav.addObject("id", configId);
        mav.addObject("completedSteps", configurationDetails.getstepsCompleted());

        //Return a list of all active organizations
        List<Organization> organizations = organizationmanager.getAllActiveOrganizations();
        mav.addObject("organizations", organizations);

        //Return a list of associated connections
        List<Connections> connections = configurationmanager.getConnections(configId);
        

        //Set a list of organizations already used
        List<Integer> usedOrgs = new ArrayList<Integer>();

        for (Connections connection : connections) {
            usedOrgs.add(connection.getorgId());
            
            //Need to get the org name;
            Organization orgDetails = organizationmanager.getOrganizationById(connection.getorgId());
            connection.setorgName(orgDetails.getOrgName());
        }
        
        //Set the object to hold the existing connections
        mav.addObject("connections", connections);

        //Add the organization id the configuration is being set up for.
        usedOrgs.add(configurationDetails.getorgId());

        mav.addObject("usedOrgs", usedOrgs);

        return mav;
    }
    
     /**
     * The '/addConnection.do' POST request will create the connection between the passed in organization
     * and the configuration.
     *
     * @param	org	The organization that to connect to
     *
     * @return	The method will return a 1 back to the calling ajax function which will handle the page load.
     */
    @RequestMapping(value = "/addConnection.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Integer addConnection(@RequestParam int org) throws Exception {
        
        //Update the configuration completed step
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);
        if (configurationDetails.getstepsCompleted() < 5) {
            configurationmanager.updateCompletedSteps(configId, 5);
        }

        Connections newConnection = new Connections();
        newConnection.setorgId(org);
        newConnection.setconfigId(configId);

        configurationmanager.saveConnection(newConnection);

        return 1;
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
        
        Connections connection = configurationmanager.getConnection(connectionId);
        connection.setstatus(statusVal);
        configurationmanager.updateConnection(connection);
        
        return 1;
    }
    
    
    /**
     * The '/scheduling' GET request will display the scheduling page for the selected transport Method
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/scheduling", method = RequestMethod.GET)
    public ModelAndView getConfigurationSchedules(@RequestParam(value = "i", required = false) Integer transportMethod) throws Exception {
        //Default the transport method to online form
        int selTransportMethod = 2;

        if (transportMethod != null) {
            selTransportMethod = transportMethod;
        }

        //Get the completed steps for the selected configuration;
        configuration configurationDetails = configurationmanager.getConfigurationById(configId);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/configurations/schedule");
        mav.addObject("id", configId);
        mav.addObject("completedSteps", configurationDetails.getstepsCompleted());
        
        //Get the schedule for the configuration and selected transport method
        configurationSchedules scheduleDetails = configurationmanager.getScheduleDetails(configId, selTransportMethod);
        
        if(scheduleDetails == null) {
            configurationSchedules emptySchedule = new configurationSchedules();
            emptySchedule.setconfigId(configId);
            emptySchedule.settransportMethod(selTransportMethod);
            emptySchedule.settype(1);
            
            //set the selected type
            mav.addObject("type", "1");
            mav.addObject("scheduleDetails", emptySchedule);  
        }
        else {
           //set the selected type
           mav.addObject("type", scheduleDetails.gettype());

           mav.addObject("scheduleDetails", scheduleDetails);  
        }
        
        //Get the list of available transport methods
        List transportMethods = configurationTransportManager.getTransportMethods();
        mav.addObject("transportMethods", transportMethods);
        
        //pass back the selected transport method
        mav.addObject("selTransportMethod", selTransportMethod);
        
        //Set a list of transport methods already set up for the configuration
        List<Integer> transportList = new ArrayList<Integer>();
        
        //Get a list of all transport details
        List<configurationTransport> allTransportDetails = configurationTransportManager.getTransportDetails(configId);

        for (configurationTransport details : allTransportDetails) {
            transportList.add(details.gettransportMethod());
        }
        mav.addObject("availTransportMethods", transportList);

        return mav;
    }
    
    /**
     * 
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
}
