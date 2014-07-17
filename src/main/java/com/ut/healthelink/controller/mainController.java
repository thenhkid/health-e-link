package com.ut.healthelink.controller;

import com.ut.healthelink.model.User;
import com.ut.healthelink.model.mailMessage;
import com.ut.healthelink.model.userAccess;
import com.ut.dph.service.emailMessageManager;
import com.ut.dph.service.userManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


/**
 * The mainController class will handle all URL requests that fall outside of specific user or admin controllers
 *
 * eg. login, logout, about, etc
 *
 * @author chadmccue
 *
 */
@Controller
public class mainController {

    @Autowired
    private userManager usermanager;

    @Autowired
    private emailMessageManager emailMessageManager;
    
    /**
     * The '/login' request will serve up the login page.
     *
     * @param request
     * @param response
     * @return	the login page view
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() throws Exception {
        

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/login");

        return mav;

    }

    /**
     * The '/loginfailed' request will serve up the login page displaying the login failed error message
     *
     * @param request
     * @param response
     * @return	the error object and the login page view
     * @throws Exception
     */
    @RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
    public ModelAndView loginerror(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/login");
        mav.addObject("error", "true");
        return mav;

    }

    /**
     * The '/logout' request will handle a user logging out of the system. The request will handle front-end users or administrators logging out.
     *
     * @param request
     * @param response
     * @return	the login page view
     * @throws Exception
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("/login");
    }

    /**
     * The '/' request will be the default request of the translator. The request will serve up the home page of the translator.
     *
     * @param request
     * @param response
     * @return	the home page view
     * @throws Exception
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttr, HttpSession session) throws Exception {
        
        /** If the user is logged in and clicks to go home send to the account home page, otherwise the login page**/
        User userInfo = (User)session.getAttribute("userDetails");
        
        if(userInfo == null) {
           ModelAndView mav = new ModelAndView(new RedirectView("/login"));
            return mav; 
        }
        else {
            ModelAndView mav = new ModelAndView(new RedirectView("/profile"));
            return mav;
        }
        
        /*return new ModelAndView("/home");*/
    }

    /**
     * The '/product-suite' request will display the product suite information page.
     */
    @RequestMapping(value = "/product-suite", method = RequestMethod.GET)
    public ModelAndView productSuite() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/productSuite");
        mav.addObject("pageTitle", "Product Suite");
        return mav;
    }

    /**
     * The '/about' GET request will display the about page.
     */
    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public ModelAndView aboutPage() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/about");
        mav.addObject("pageTitle", "About Us");
        return mav;
    }

    /**
     * The '/contact' GEt request will display the contact page.
     */
    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public ModelAndView contactPage() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/contact");
        mav.addObject("pageTitle", "Contact Us");
        return mav;
    }
    
    /**
     * The '/contact' POST request will display the contact page.
     */
    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public ModelAndView contactPageSend(@RequestParam String name, @RequestParam String company, @RequestParam String address, @RequestParam String city, 
            @RequestParam String state, @RequestParam String zip, @RequestParam String phone, @RequestParam String ext, @RequestParam String fax, @RequestParam String email, 
            @RequestParam String comments) throws Exception {
        
       StringBuilder sb = new StringBuilder();
       
       mailMessage messageDetails = new mailMessage();
        
       messageDetails.settoEmailAddress("e-Referral@state.ma.us");
       messageDetails.setfromEmailAddress("dphuniversaltranslator@gmail.com");
       messageDetails.setmessageSubject("e-Referral Contact Form Submission");
       
       
        sb.append("Name: "+ name);
        sb.append("<br /><br />");
        sb.append("Company / Organization: " + company);
        sb.append("<br /><br />");
        sb.append("Address: " + address);
        sb.append("<br /><br />");
        sb.append("City: " + city);
        sb.append("<br /><br />");
        sb.append("State: " + state);
        sb.append("<br /><br />");
        sb.append("Zip: " + zip);
        sb.append("<br /><br />");
        sb.append("Phone: " + phone);
        sb.append("<br /><br />");
        sb.append("Ext: " + ext);
        sb.append("<br /><br />");
        sb.append("Fax: " + fax);
        sb.append("<br /><br />");
        sb.append("Email: " + email);
        sb.append("<br /><br />");
        sb.append("Comments: " + comments);
        sb.append("<br /><br />");
        
        messageDetails.setmessageBody(sb.toString());
        
        emailMessageManager.sendEmail(messageDetails); 

        ModelAndView mav = new ModelAndView();
        
        mav.setViewName("/contact");
        mav.addObject("pageTitle", "Contact Us");
        mav.addObject("sent","sent");
        
        
        return mav;
    }

    /**
     * The '/forgotPassword' GET request will be used to display the forget password form (In a modal)
     *
     *
     * @return	The forget password form page
     *
     *
     */
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    public ModelAndView forgotPassword(HttpSession session) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/forgotPassword");

        return mav;
    }

    /**
     * The '/forgotPassword.do' POST request will be used to find the account information for the user and send an email.
     *
     *
     */
    @RequestMapping(value = "/forgotPassword.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Integer findPassword(@RequestParam String identifier) throws Exception {

        Integer userId = usermanager.getUserByIdentifier(identifier);

        if (userId == null) {
            return 0;
        } else {

            return userId;
        }

    }

    /**
     * The '/sendPassword.do' POST request will be used to send the reset email to the user.
     *
     * @param userId The id of the return user.
     */
    @RequestMapping(value = "/sendPassword.do", method = RequestMethod.POST)
    public void sendPassword(@RequestParam Integer userId, HttpServletRequest request) throws Exception {
        
        String randomCode = generateRandomCode();

        User userDetails = usermanager.getUserById(userId);
        userDetails.setresetCode(randomCode);

        //Return the sections for the clicked user
        List<userAccess> userSections = usermanager.getuserSections(userId);
        List<Integer> userSectionList = new ArrayList<Integer>();

        for (int i = 0; i < userSections.size(); i++) {
            userSectionList.add(userSections.get(i).getFeatureId());
        }

        userDetails.setsectionList(userSectionList);

        usermanager.updateUser(userDetails);

        /* Sent Reset Email */
        mailMessage messageDetails = new mailMessage();

        messageDetails.settoEmailAddress(userDetails.getEmail());
        messageDetails.setmessageSubject("Universal Translator Reset Password");
        
        String resetURL = request.getRequestURL().toString().replace("sendPassword.do", "resetPassword?b=");
        
        StringBuilder sb = new StringBuilder();

        sb.append("Dear " + userDetails.getFirstName() + ",<br />");
        sb.append("You have recently asked to reset your Universal Translator password.<br /><br />");
        sb.append("<a href='" + resetURL + randomCode + "'>Click here to reset your password.</a>");

        messageDetails.setmessageBody(sb.toString());
        messageDetails.setfromEmailAddress("dphuniversaltranslator@gmail.com");

        emailMessageManager.sendEmail(messageDetails);

    }

    /**
     * The '/resetPassword' GET request will be used to display the reset password form
     *
     *
     * @return	The forget password form page
     *
     *
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public ModelAndView resetPassword(@RequestParam(value = "b", required = false) String resetCode, HttpSession session) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/resetPassword");
        mav.addObject("resetCode", resetCode);

        return mav;
    }

    /**
     * The '/resetPassword' POST request will be used to display update the users password
     *
     * @param resetCode The code that was set to reset a user for.
     * @param newPassword The password to update the user to
     *
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ModelAndView resetPassword(@RequestParam String resetCode, @RequestParam String newPassword, HttpSession session, RedirectAttributes redirectAttr) throws Exception {

        User userDetails = usermanager.getUserByResetCode(resetCode);

        if (userDetails == null) {
            redirectAttr.addFlashAttribute("msg", "notfound");

            ModelAndView mav = new ModelAndView(new RedirectView("/login"));
            return mav;
        } else {
            userDetails.setresetCode(null);
            userDetails.setPassword(newPassword);

            //Return the sections for the clicked user
            List<userAccess> userSections = usermanager.getuserSections(userDetails.getId());
            List<Integer> userSectionList = new ArrayList<Integer>();

            for (int i = 0; i < userSections.size(); i++) {
                userSectionList.add(userSections.get(i).getFeatureId());
            }

            userDetails.setsectionList(userSectionList);

            usermanager.updateUser(userDetails);

            redirectAttr.addFlashAttribute("msg", "updated");

            ModelAndView mav = new ModelAndView(new RedirectView("/login"));
            return mav;
        }

    }

    /**
     * The 'generateRandomCode' function will be used to generate a random access code to reset a users password. The function will call itself until it gets a unique code.
     *
     * @return This function returns a randomcode as a string
     */
    public String generateRandomCode() {

        StringBuilder code = new StringBuilder();

        /* Generate a random 6 digit number for a confirmation code */
        for (int i = 1; i <= 7; i++) {
            Random rand = new Random();
            int r = rand.nextInt(8) + 1;
            code.append(r);
        }

        /* Check to make sure there is not reset code already generated */
        User usedCode = usermanager.getUserByResetCode(code.toString());

        if (usedCode == null) {
            return code.toString();
        } else {

            return generateRandomCode();

        }

    }
}
