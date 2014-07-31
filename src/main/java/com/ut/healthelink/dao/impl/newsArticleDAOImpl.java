/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.dao.impl;

import com.ut.healthelink.dao.newsArticleDAO;
import com.ut.healthelink.model.newsArticle;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author chadmccue
 */
@Repository
public class newsArticleDAOImpl implements newsArticleDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    /**
     * The 'createNewsArticle' function will create a new news article
     *
     * @Table	newsarticles
     *
     * @param	article	Will hold the news article object from the form
     *
     * @return The function will not return anything
     */
    @Override
    public void createNewsArticle(newsArticle article) throws Exception {
        sessionFactory.getCurrentSession().save(article);
    }
    
    
    /**
     * The 'updateNewsArticle' function will update a passed in news article
     *
     * @Table	newsarticles
     *
     * @param	article	Will hold the news article object from the form
     *
     * @return The function will not return anything
     */
    @Override
    public void updateNewsArticle(newsArticle article) throws Exception {
        sessionFactory.getCurrentSession().update(article);
    }
    
    /**
     * The 'deleteNewsArticle' function will delete the selected news article
     *
     * @Table	newsarticles
     *
     * @param	id	Will hold the id of the news article to remove
     *
     * @return The function will not return anything
     */
    @Override
    @Transactional
    public void deleteNewsArticle(int id) throws Exception {
         //delete provider addresses
        try {
            Query deleteProviderAddresses = sessionFactory.getCurrentSession().createQuery("delete from newsarticles where id = :id)");
            deleteProviderAddresses.setParameter("id", id);
            deleteProviderAddresses.executeUpdate();
        }
        catch(SQLGrammarException ex){
            throw ex;
        };
    }
    
    /**
     * The 'listAllNewsArticles' function will return all the news articles in the system
     *
     * @Table	newsarticles
     *
     * @return The function will return the news articles
     */
    @Override
    @Transactional
    public List<newsArticle> listAllNewsArticles() throws Exception {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(newsArticle.class);

        return criteria.list();
    }
    
    /**
     * The 'getNewsArticleById' function will return a news article based on the passed Id
     *
     * @Table	newsarticles
     *
     * @param	id	Will hold the id of the selected news article
     *
     * @return The function will return the news article
     */
    @Override
    @Transactional
    public newsArticle getNewsArticleById(int id) throws Exception {
         return (newsArticle) sessionFactory.getCurrentSession().get(newsArticle.class, id);
    }
    
}
