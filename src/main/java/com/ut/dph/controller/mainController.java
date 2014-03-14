package com.ut.dph.controller;

import com.ut.dph.model.User;
import com.ut.dph.model.mailMessage;
import com.ut.dph.service.emailMessageManager;
import com.ut.dph.service.userManager;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
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
    public ModelAndView logout(HttpServletRequest request,  HttpServletResponse response) throws Exception {
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
    public ModelAndView welcome(HttpServletRequest request,  HttpServletResponse response) throws Exception {
        
        return new ModelAndView("/home");
    }
    
    /**
     * The '/product-suite' request will display the product suite information page.
     */
    @RequestMapping(value="/product-suite", method = RequestMethod.GET)
    public ModelAndView productSuite() throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/productSuite");
        mav.addObject("pageTitle","Product Suite");
        return mav;
    }
    
    /**
     * The '/about' GET request will display the about page.
     */
    @RequestMapping(value="/about", method = RequestMethod.GET)
    public ModelAndView aboutPage() throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/about");
        mav.addObject("pageTitle","About Us");
        return mav;
    }
    
    /**
     * The '/contact' GEt request will display the contact page.
     */
    @RequestMapping(value="/contact", method = RequestMethod.GET)
    public ModelAndView contactPage() throws Exception {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/contact");
        mav.addObject("pageTitle","Contact Us");
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
     * The '/forgotPassword' POST request will be used to find the account information for the user
     * and send an email.
     *
     *
     */
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
    public ModelAndView findPassword(@RequestParam String identifier, RedirectAttributes redirectAttr) throws Exception {
        
        User userDetails = usermanager.getUserByIdentifier(identifier);
        
        if(userDetails == null) {
            ModelAndView mav = new ModelAndView(new RedirectView("/forgotPassword?msg=notfound"));
            return mav;
        }
        else {
            
            StringBuilder code = new StringBuilder();
            
            /* Generate a random 6 digit number for a confirmation code */
            for(int i=1;i<=7;i++) {
                Random rand = new Random();
                int r = rand.nextInt(8) + 1;
                code.append(r);
            }
            
            /* Sent Reset Email */
            mailMessage messageDetails = new mailMessage();
            
            messageDetails.settoEmailAddress(userDetails.getEmail());
            messageDetails.setmessageSubject("Universal Translator Reset Password");
            
            StringBuilder sb = new StringBuilder();
            
            sb.append("Dear "+userDetails.getFirstName()+",<br />");
            sb.append("You have recently asked to reset your Universal Translator password.<br /><br />");
            sb.append("<a href='http://localhost:8085/resetPassword?b="+code+"'>Click here to reset your password.</a>");
            
            messageDetails.setmessageBody(sb.toString());
            messageDetails.setfromEmailAddress("dphuniversaltranslator@gmail.com");
            
            emailMessageManager.sendEmail(messageDetails);
            
            ModelAndView mav = new ModelAndView(new RedirectView("/forgotPassword?msg=sent"));
            return mav;
        }

        
    }
}
