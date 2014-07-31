/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.service.impl;

import com.ut.healthelink.dao.newsArticleDAO;
import com.ut.healthelink.model.newsArticle;
import com.ut.healthelink.service.newsArticleManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author chadmccue
 */
@Service
public class newsArticleManagerImpl implements newsArticleManager {
    
    @Autowired
    private newsArticleDAO newsArticleDAO;
    
    @Override
    @Transactional
    public void createNewsArticle(newsArticle article) throws Exception {
        newsArticleDAO.createNewsArticle(article);
    }
    
    
    @Override
    @Transactional
    public void updateNewsArticle(newsArticle article) throws Exception {
        newsArticleDAO.updateNewsArticle(article);
    }
    
    @Override
    @Transactional
    public void deleteNewsArticle(int id) throws Exception {
        newsArticleDAO.deleteNewsArticle(id);
    }
    
    @Override
    @Transactional
    public List<newsArticle> listAllNewsArticles() throws Exception {
        return newsArticleDAO.listAllNewsArticles();
    }
    
    @Override
    @Transactional
    public newsArticle getNewsArticleById(int id) throws Exception {
        return newsArticleDAO.getNewsArticleById(id);
    }
    
}
