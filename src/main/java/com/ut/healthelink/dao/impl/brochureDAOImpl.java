package com.ut.healthelink.dao.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ut.healthelink.dao.brochureDAO;
import com.ut.healthelink.model.Brochure;

/**
 * The brochureDAOImpl class will implement the DAO access layer to handle updates for organization brochures
 *
 *
 * @author chadmccue
 *
 */
@Repository
public class brochureDAOImpl implements brochureDAO {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * The 'createBrochure" function will create the new brochure
     *
     * @Table	brochures
     *
     * @param	brocure	This will hold the brochure object from the form
     *
     * @return The function will return the id of the new brochure
     *
     */
    @Override
    public Integer createBrochure(Brochure brochure) {
        Integer lastId = null;

        lastId = (Integer) sessionFactory.getCurrentSession().save(brochure);

        return lastId;
    }

    /**
     * The 'updateBrochure' function will update the selected brochure with the changes entered into the form.
     *
     * @param	brochure	This will hold the brochure object from the brocure form
     *
     * @return the function does not return anything
     */
    @Override
    public void updateBrochure(Brochure brochure) {
        sessionFactory.getCurrentSession().update(brochure);
    }

    /**
     * The 'deleteBrochure' function will delete the selected brochure
     *
     * @param	brochureId	This will hold the id of the brochure to delete
     *
     * @return the function does not return anything
     */
    @Override
    public void deleteBrochure(int brochureId) {
        Query deleteBrochure = sessionFactory.getCurrentSession().createQuery("delete from Brochure where id = :brochureId");
        deleteBrochure.setParameter("brochureId", brochureId);
        deleteBrochure.executeUpdate();
    }

    /**
     * The 'getBrochureById' function will return a single brochure object based on the brochureId passed in.
     *
     * @param	brochureid	This will be id to find the specific brochure
     *
     * @return	The function will return a brochure object
     */
    @Override
    public Brochure getBrochureById(int brochureId) {
        return (Brochure) sessionFactory.getCurrentSession().get(Brochure.class, brochureId);
    }

}
