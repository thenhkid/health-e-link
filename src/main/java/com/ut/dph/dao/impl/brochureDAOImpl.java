package com.ut.dph.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ut.dph.dao.brochureDAO;
import com.ut.dph.model.Brochure;

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

    /**
     * The 'findBrochures' function will return a list of brochure objects based on a specific organization Id and a search term. The search will look for brochures for a specific organization and whose title or file name match the search term provided.
     *
     * @param	orgId	This will be the orgId used to find providers searchTerm	This will be used to query the title and file name field
     *
     * @return	The function will return a list of brochure objects
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Brochure> findBrochures(int orgId, String searchTerm) {
        //Order by title
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Brochure.class)
                .add(Restrictions.eq("orgId", orgId))
                .add(Restrictions.or(
                                Restrictions.like("title", "%" + searchTerm + "%"),
                                Restrictions.like("fileName", "%" + searchTerm + "%")
                        )
                )
                .addOrder(Order.asc("title"));

        return criteria.list();
    }

}
