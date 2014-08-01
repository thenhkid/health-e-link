/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.service;

import com.ut.healthelink.model.newsArticle;
import java.util.List;

/**
 *
 * @author chadmccue
 */
public interface newsArticleManager {
    
    void createNewsArticle(newsArticle article) throws Exception;
    
    void updateNewsArticle(newsArticle article) throws Exception;
    
    void deleteNewsArticle(int id) throws Exception;
    
    List<newsArticle> listAllNewsArticles() throws Exception;
    
    List<newsArticle> listAllActiveNewsArticles() throws Exception;
    
    newsArticle getNewsArticleById(int id) throws Exception;
    
    List<newsArticle> getNewsArticleByTitle(String articleTitle) throws Exception;
    
}
