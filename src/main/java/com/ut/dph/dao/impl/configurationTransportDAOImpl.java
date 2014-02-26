package com.ut.dph.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.ut.dph.dao.configurationTransportDAO;
import com.ut.dph.model.configurationFTPFields;
import com.ut.dph.model.configurationFormFields;
import com.ut.dph.model.configurationTransport;
import com.ut.dph.model.configurationTransportMessageTypes;

import java.util.Iterator;

@Service
public class configurationTransportDAOImpl implements configurationTransportDAO {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * The 'getTransportDetails' function will return the details of the transport method for the passed in configuration id
     *
     * @param configId	Holds the id of the selected configuration
     *
     * @Return	This function will return a list of configurationTransport objects
     */
    @SuppressWarnings("unchecked")
    public configurationTransport getTransportDetails(int configId) throws Exception {
        Query query = sessionFactory.getCurrentSession().createQuery("from configurationTransport where configId = :configId");
        query.setParameter("configId", configId);
        
         return (configurationTransport) query.uniqueResult();
    }

    /**
     * The 'getTransportDetailsByTransportMethod' function will return the details of the transport method for the passed in configuration id and passed in transport method
     *
     * @param configId	Holds the id of the selected configuration
     * @param transportMethod	Holds the selected transport method
     *
     * @Return	This function will return a configurationTransport object
     */
    public configurationTransport getTransportDetailsByTransportMethod(int configId, int transportMethod) {
        Query query = sessionFactory.getCurrentSession().createQuery("from configurationTransport where configId = :configId and transportMethodId = :transportMethod");
        query.setParameter("configId", configId);
        query.setParameter("transportMethod", transportMethod);

        return (configurationTransport) query.uniqueResult();
    }

    /**
     * The 'setupOnlineForm' function will complete the set up for the online form for the new configuration. Every configuration will have an associated online form.
     *
     * @param	configId	Holds the id of the new configuration
     * @param	messageTypeid	Holds the id of the selected message type
     *
     * @Return	This function does not return anything
     */
    @Override
    public void setupOnlineForm(int transportId, int configId, int messageTypeId) {
        copyMessageTypeFields(transportId, configId, messageTypeId);
    }

    /**
     * The 'updateTransportDetails' function will update the configuration transport details
     *
     * @param	transportDetails	The details of the transport form
     * @param	clearFields	This will hold a variable that will determine if we clear out existing configuration details.
     *
     * @return	this function does not return anything
     */
    public Integer updateTransportDetails(configurationTransport transportDetails) {

        if (transportDetails.getId() > 0) {
            sessionFactory.getCurrentSession().update(transportDetails);
            return transportDetails.getId();
        } else {
            int detailId = (Integer) sessionFactory.getCurrentSession().save(transportDetails);
            return detailId;
        }

    }

    /**
     * The 'getTransportMethods' function will return a list of available transport methods
     *
     */
    @Override
    @SuppressWarnings("rawtypes")
    @Transactional
    public List getTransportMethods() {
        Query query = sessionFactory.getCurrentSession().createSQLQuery("SELECT id, transportMethod FROM ref_transportMethods order by transportMethod asc");

        return query.list();
    }

    /**
     * The 'copyMessageTypeFields' function will copy the form fields for the selected message type for the selected configuration.
     *
     * @param   transportId     The id of the configured transport method
     * @param	configId	The id of the selected configuration 
     * @param   messageTypeId	The id of the selected message type to copy the form fields
     *
     * @return	This function does not return anything
     */
    @Transactional
    public void copyMessageTypeFields(int transportId, int configId, int messageTypeId) {
       
        /* Check to see if there are any data translations for the passed in message type */
        Query translationQuery = sessionFactory.getCurrentSession().createSQLQuery("SELECT id FROM rel_messageTypeDataTranslations where messageTypeId = :messageTypeId");
        translationQuery.setParameter("messageTypeId", messageTypeId);
       
        if(translationQuery.list().size() > 0) {
            /* Get all the message type fields */
            Query messageTypeFields = sessionFactory.getCurrentSession().createSQLQuery("SELECT id, messageTypeId FROM messageTypeFormFields where messageTypeId = :messageTypeId");
            messageTypeFields.setParameter("messageTypeId", messageTypeId);
            List fieldList = messageTypeFields.list();
            
            Iterator it = fieldList.iterator();
            int id;
            int max;
            while(it.hasNext()) {
                Object row[] = (Object[]) it.next();
                id = (Integer) row[0];
                Query query = sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO configurationFormFields (messageTypeFieldId, configId, transportDetailId, fieldNo, fieldDesc, fieldLabel, validationType, required, bucketNo, bucketDspPos, useField, saveToTableName, saveToTableCol, autoPopulateTableName, autoPopulateTableCol) SELECT id, :configId, :transportDetailId, fieldNo,  fieldDesc, fieldLabel, validationType, required, bucketNo, bucketDspPos, 1, saveToTableName, saveToTableCol, autoPopulateTableName, autoPopulateTableCol FROM messageTypeFormFields where messageTypeId = :messageTypeId and id = :id");
                query.setParameter("configId", configId);
                query.setParameter("messageTypeId", messageTypeId);
                query.setParameter("transportDetailId", transportId);
                query.setParameter("id", id);
                query.executeUpdate();
                
                /*Get the max id */
                Query maxId = sessionFactory.getCurrentSession().createSQLQuery("SELECT max(id), configId FROM configurationFormFields");
                List queryList = maxId.list();
                Iterator maxIt = queryList.iterator();
                while(maxIt.hasNext()) {
                    Object maxrow[] = (Object[]) maxIt.next();
                    max = (Integer) maxrow[0]; 
                    /* Check to see if there is a data translation for the current row */
                    Query copyTranslations = sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO configurationDataTranslations (configId, fieldId, crosswalkId, macroId, processOrder) SELECT :configId, :fieldId, crosswalkId, 0, processOrder FROM rel_messageTypeDataTranslations where fieldId = :fieldId2");
                    copyTranslations.setParameter("configId",configId);
                    copyTranslations.setParameter("fieldId",max);
                    copyTranslations.setParameter("fieldId2",id);
                    copyTranslations.executeUpdate();
                }   
            }
        }
        else {
            Query query = sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO configurationFormFields (messageTypeFieldId, configId, transportDetailId, fieldNo, fieldDesc, fieldLabel, validationType, required, bucketNo, bucketDspPos, useField, saveToTableName, saveToTableCol, autoPopulateTableName, autoPopulateTableCol) SELECT id, :configId, :transportDetailId, fieldNo,  fieldDesc, fieldLabel, validationType, required, bucketNo, bucketDspPos, 1, saveToTableName, saveToTableCol, autoPopulateTableName, autoPopulateTableCol FROM messageTypeFormFields where messageTypeId = :messageTypeId");
            query.setParameter("configId", configId);
            query.setParameter("messageTypeId", messageTypeId);
            query.setParameter("transportDetailId", transportId);

            query.executeUpdate();
        }
    }

    /**
     * The 'getConfigurationFields' function will return a list of saved form fields for the selected configuration.
     *
     * @param	configId	Will hold the id of the configuration we want to return fields for
     * @param transporetDetailId    The id of the selected transport method
     *
     * @return	This function will return a list of configuration form fields
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<configurationFormFields> getConfigurationFields(int configId, int transportDetailId) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(configurationFormFields.class)
                .add(Restrictions.eq("configId", configId))
                .add(Restrictions.eq("transportDetailId", transportDetailId))
                .addOrder(Order.asc("bucketNo"))
                .addOrder(Order.asc("bucketDspPos"));

        return criteria.list();
    }
    
    /**
     * The 'getConfigurationFieldsByBucket' function will return a list of form fields for the selected configuration
     * and selected Bucket (Section 1-4)
     * 
     * @param configId              The id of the selected configuration
     * @param transporetDetailId    The id of the selected transport method
     * @param buckt                 The integer value of the bucket (Section) you want to return fields for (must be 1-4)
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<configurationFormFields> getConfigurationFieldsByBucket(int configId, int transportDetailId, int bucket) throws Exception {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(configurationFormFields.class)
                .add(Restrictions.eq("configId", configId))
                .add(Restrictions.eq("transportDetailId", transportDetailId))
                .add(Restrictions.eq("bucketNo", bucket))
                .add(Restrictions.eq("useField", true))
                .addOrder(Order.asc("bucketDspPos"));

        return criteria.list();
    }

    /**
     * The 'updateConfigurationFormFields' function will update the configuration form field settings
     *
     * @param formField	object that will hold the form field settings
     *
     * @return This function will not return anything
     */
    public void updateConfigurationFormFields(configurationFormFields formField) {
        sessionFactory.getCurrentSession().update(formField);
    }
    
    /**
    * The 'getTransportFTPDetails' function will return the FTP information for the 
    * passed in transportDetailId.
    * 
    * @param transportDetailsId     the id of the selected transport method
    * 
    * @return This function will return a list of FTP details
    */
    @Override
    @Transactional
    public List<configurationFTPFields> getTransportFTPDetails(int transportDetailId) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(configurationFTPFields.class)
                .add(Restrictions.eq("transportId", transportDetailId));

        return criteria.list();
    }
    
    
    /**
     * The 'saveTransportFTP' function will save the transport FTP
     * information into the DB.
     * 
     * @param   FTPFields   The FTP form fields
     * 
     * @return this function will not return anything.
     */
    @Override
    @Transactional
    public void saveTransportFTP(configurationFTPFields FTPFields) {
        sessionFactory.getCurrentSession().saveOrUpdate(FTPFields);
    }
    
    /**
     * The 'getTransportMethodById' function will return the name of a transport method based on the id passed in.
     *
     * @param Id	This will hold the id of the transport method to retrieve
     *
     * @Return This function will return a string (transport Method).
     */
    @Override
    @Transactional
    public String getTransportMethodById(int Id) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery("SELECT transportMethod FROM ref_transportMethods where id = :Id")
                .setParameter("Id", Id);

        String transportMethod = (String) query.uniqueResult();

        return transportMethod;
    }
    
    /**
     * The 'getTransportMessageTypes' function will return a list of configurations the current transport is configured
     * to accept message types for.
     * 
     * @param configTransportId The current transport id to search on
     * 
     * @return  This function will return a list of configurationTransportMessageType objects.
     */
    @Override
    @Transactional
    public List<configurationTransportMessageTypes> getTransportMessageTypes(int configTransportId) {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM configurationTransportMessageTypes where configTransportId = :configTransportId");
        query.setParameter("configTransportId", configTransportId);

        return query.list();
    }
    
    /**
     * The 'deleteTransportMessageTypes' function will remove all associated message types for the passed in
     * transport method;
     * 
     * @param   configTransportId   The id for the selected config transport
     * 
     * @retuern This function does not return anything.
     */
    @Override
    @Transactional
    public void deleteTransportMessageTypes(int configTransportId) {
        Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM configurationTransportMessageTypes where configTransportId = :configTransportId");
        query.setParameter("configTransportId", configTransportId);
        
        query.executeUpdate();
    }
    
    /**
     * The 'saveTransportMessageTypes' function will save the association between configuration
     * transport and message types.
     * 
     * @param   messageType The configurationTransportMessageTypes object
     * 
     * @return  This function does not return anything.
     */
    public void saveTransportMessageTypes(configurationTransportMessageTypes messageType) {
        sessionFactory.getCurrentSession().save(messageType);
    }
    
    /**
     * The 'copyExistingTransportMethod' function will copy the existing transport settings
     * from the passed in transportId to the new configuration.
     * 
     * @param   configTransportId   The id for the existing transport method to copy from
     * @param   configId            The id for the new configuration to copy to
     * 
     * @return This function does not return anything.
     */
    public void copyExistingTransportMethod(int configTransportId, int configId) {
        
        Query query = sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO configurationTransportDetails (configId, transportMethodId, fileType, fileDelimiter, status, targetFileName, appendDateTime, maxFileSize, clearRecords, fileLocation, autoRelease, errorHandling, mergeBatches, copiedTransportId) select :configId, transportMethodId, fileType, fileDelimiter, status, targetFileName, appendDateTime, maxFileSize, clearRecords, fileLocation, autoRelease, errorHandling, mergeBatches, :configTransportId FROM configurationTransportDetails where id = :configTransportId");
        query.setParameter("configId", configId);
        query.setParameter("configTransportId", configTransportId);
        
        query.executeUpdate();
        
    }

    /**this method will returns a list of required form field for a configuration**/
    
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<configurationFormFields> getRequiredFieldsForConfig(Integer configId) {
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(configurationFormFields.class)
	                .add(Restrictions.eq("configId", configId))
	                .add(Restrictions.eq("required", true))
	                .addOrder(Order.asc("fieldNo"));
	                
	        return criteria.list();
	}

	/** this method returns a list of cff by validation type
	 * if 0 is passed in, we get them all **/
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<configurationFormFields> getCffByValidationType(
			Integer configId, Integer validationTypeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(configurationFormFields.class)
                .add(Restrictions.eq("configId", configId));
		if (validationTypeId != 0){
			criteria.add(Restrictions.eq("validationTypeId", validationTypeId));
		}     
                criteria.addOrder(Order.asc("fieldNo"));
                
        return criteria.list();
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<configurationTransport> getDistinctConfigTransportForOrg(Integer orgId, Integer transportMethodId) {
		try {
			
			String sql = ("select distinct delimChar, errorHandling, autoRelease, fileLocation, fileType, containsHeaderRow, "
					+ " transportMethodId from configurationTransportDetails, ref_delimiters , configurationMessageSpecs "
					+ " where ref_delimiters.id = configurationTransportDetails.fileDelimiter "
					+ " and configurationMessageSpecs.configId = configurationTransportDetails.configId "
					+ " and transportMethodId = :transportMethodId and configurationTransportDetails.configId in "
					+ "(select id from configurations where orgId = :orgId);");
	        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(
                    Transformers.aliasToBean(configurationTransport.class));           
	        query.setParameter("orgId", orgId);
	        query.setParameter("transportMethodId", transportMethodId);
	        
	        List <configurationTransport> configurationTransports = query.list();
			
	        return configurationTransports;
	        
		} catch (Exception ex) {
			System.err.println(ex.getClass() + " " + ex.getCause());
			ex.printStackTrace();
			
			return null;
		}
	}

}
