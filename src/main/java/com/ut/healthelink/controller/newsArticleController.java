/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.controller;

import com.ut.healthelink.model.newsArticle;
import com.ut.healthelink.service.newsArticleManager;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author chadmccue
 */
@Controller
@RequestMapping(value={"/administrator/sysadmin/news", "/news"})
public class newsArticleController {
    
    @Autowired
    private newsArticleManager newsarticlemanager;
    
    /**
     The '' request will return the list of news articles that are currently in the
     * system.
	 
    */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView newsArticleList(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/news");
        
        List<newsArticle> newsArticles = newsarticlemanager.listAllNewsArticles();
        
        mav.addObject("newsArticles", newsArticles);

        return mav;
    }
    
    /**
     The '/create' request will return a blank news article form.
     * 
    */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createnewsArticle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/news/details");
        mav.addObject("newsArticle", new newsArticle());
        
        return mav;
    }
    
    
    /**
     * The '/create' POST request will submit the new article once all required fields are checked.
     *
     * @param newsArticle	The object holding the news article form fields
     * @param result	The validation result
     * @param redirectAttr	The variable that will hold values that can be read after the redirect
     * @param action	The variable that holds which button was pressed
     *
     * @return	Will return the news article list page on "Save"
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView saveNewArticle(@Valid newsArticle newsArticle, BindingResult result, RedirectAttributes redirectAttr, @RequestParam String action) throws Exception {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/administrator/sysadmin/news/details");
            return mav;
        }

        newsarticlemanager.createNewsArticle(newsArticle);

        redirectAttr.addFlashAttribute("savedStatus", "created");

        ModelAndView mav = new ModelAndView(new RedirectView("/administrator/sysadmin/news"));
        return mav;

    }

    /**
     The '/edit' request will return a blank news article form.
     * 
    */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView editnewsArticle(@RequestParam(value = "i", required = true) Integer articleId, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/administrator/sysadmin/news/details");
        
        newsArticle newsArticle = newsarticlemanager.getNewsArticleById(articleId);
        
        mav.addObject("newsArticle", newsArticle);
        
        return mav;
    }
    
    /**
     * The '/edit' POST request will submit the new article once all required fields are checked.
     *
     * @param newsArticle	The object holding the news article form fields
     * @param result	The validation result
     * @param redirectAttr	The variable that will hold values that can be read after the redirect
     * @param action	The variable that holds which button was pressed
     *
     * @return	Will return the news article list page on "Save"
     * @throws Exception
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView saveArticleUpdates(@Valid newsArticle newsArticle, BindingResult result, RedirectAttributes redirectAttr, @RequestParam String action) throws Exception {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/administrator/sysadmin/news/details");
            return mav;
        }

        newsarticlemanager.updateNewsArticle(newsArticle);

        redirectAttr.addFlashAttribute("savedStatus", "updated");

        ModelAndView mav = new ModelAndView(new RedirectView("/administrator/sysadmin/news"));
        return mav;

    }
    
    
    /** Front-end News Article Methods **/
    
    /**
     * The '/articles' GET request will search for a clicked article by the passed in article name.
     * 
     * @param articleTitle The title of the selected article.
     * 
     * @return Will return the selected news article.
     */
    @RequestMapping(value = "/articles", method = RequestMethod.GET)
    public ModelAndView viewArticles() throws Exception {
        
        List<newsArticle> articles = newsarticlemanager.listAllActiveNewsArticles();
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/news/list");
        mav.addObject("articles", articles);
        mav.addObject("pageTitle", "News");
        return mav;
        
    }
    
    /**
     * The '/news/article/{articleTitle}' GET request will search for a clicked article by the passed in article name.
     * 
     * @param articleTitle The title of the selected article.
     * 
     * @return Will return the selected news article.
     */
    @RequestMapping(value = "/article/{articleTitle}", method = RequestMethod.GET)
    public ModelAndView viewArticleDetails(@PathVariable String articleTitle) throws Exception {
        
        String articleTitleStripped = articleTitle.replace("-", " ");
        
        List<newsArticle> article = newsarticlemanager.getNewsArticleByTitle(articleTitleStripped);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/news/articleDetails");
        mav.addObject("article", article.get(0));
        mav.addObject("pageTitle", "News");
        return mav;
        
    }
    
}
