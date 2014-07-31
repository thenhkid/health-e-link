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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
    
}
