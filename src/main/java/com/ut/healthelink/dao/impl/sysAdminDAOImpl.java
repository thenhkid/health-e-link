package com.ut.healthelink.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.ut.healthelink.dao.sysAdminDAO;
import com.ut.healthelink.dao.UtilitiesDAO;
import com.ut.healthelink.model.Macros;
import com.ut.healthelink.model.custom.LogoInfo;
import com.ut.healthelink.model.custom.LookUpTable;
import com.ut.healthelink.model.custom.TableData;
import com.ut.healthelink.model.lutables.lu_Counties;
import com.ut.healthelink.model.lutables.lu_GeneralHealthStatuses;
import com.ut.healthelink.model.lutables.lu_GeneralHealths;
import com.ut.healthelink.model.lutables.lu_Immunizations;
import com.ut.healthelink.model.lutables.lu_Manufacturers;
import com.ut.healthelink.model.lutables.lu_MedicalConditions;
import com.ut.healthelink.model.lutables.lu_Medications;
import com.ut.healthelink.model.lutables.lu_Procedures;
import com.ut.healthelink.model.lutables.lu_ProcessStatus;
import com.ut.healthelink.model.lutables.lu_Tests;
import com.ut.healthelink.model.mainHL7Details;
import com.ut.healthelink.model.mainHL7Elements;
import com.ut.healthelink.model.mainHL7Segments;
import java.util.ArrayList;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @see com.ut.healthelink.dao.sysAdminDAO
 * @author gchan
 */
@Repository
public class sysAdminDAOImpl implements sysAdminDAO {

    @Autowired
    private UtilitiesDAO udao;

    @Autowired
    private SessionFactory sessionFactory;

    private String schemaName = "healthelink";

    /**
     * this gets a list of Lookup tables *
     */
    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<LookUpTable> getLookUpTables(String searchTerm) {
        Query query = sessionFactory
                .getCurrentSession()
                .createSQLQuery(
                        "select  "
                        + "displayText as displayName,"
                        + "utTableName,"
                        + "count(COLUMN_NAME) as columnNum, "
                        + "urlId as urlId, "
                        + "TABLE_ROWS as rowNum, "
                        + "description "
                        + "from information_schema.tables infoT, "
                        + "information_schema.COLUMNS infoc, lookUpTables "
                        + "where infoc.TABLE_SCHEMA = :schemaName "
                        + "and lookUpTables.utTableName = infot.TABLE_NAME "
                        + "and lookUpTables.utTableName = infoc.TABLE_NAME "
                        + "and infoc.TABLE_SCHEMA = infot.TABLE_SCHEMA "
                        + "and infoc.TABLE_NAME = infot.TABLE_NAME "
                        + "and displayText like :searchTerm "
                        + "group by infoc.TABLE_NAME order by infoc.TABLE_NAME")
                .addScalar("displayName", StandardBasicTypes.STRING)
                .addScalar("utTableName", StandardBasicTypes.STRING)
                .addScalar("urlId", StandardBasicTypes.STRING)
                .addScalar("columnNum", StandardBasicTypes.INTEGER)
                .addScalar("rowNum", StandardBasicTypes.INTEGER)
                .addScalar("description", StandardBasicTypes.STRING)
                .setResultTransformer(
                        Transformers.aliasToBean(LookUpTable.class))
                .setParameter("schemaName", schemaName)
                .setParameter("searchTerm", searchTerm);

       

        List<LookUpTable> tableList = query.list();

        return tableList;

    }

    /**
     * this method returns the number of look up tables in the system *
     */
    @Override
    @Transactional
    public Integer findTotalLookUpTable() {

        Query query = sessionFactory
                .getCurrentSession()
                .createSQLQuery(
                        "select count(id) as totalLookUpTables from lookUpTables").addScalar("totalLookUpTables", StandardBasicTypes.INTEGER);
        Integer totalTables = (Integer) query.list().get(0);

        return totalTables;
    }

    /**
     * this method takes the table name and searchTerm (if there is one) and return the data in the table
	 *
     */
    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<TableData> getDataList(String utTableName, String searchTerm) {

        String sql = "select id, displayText, description, "
                + " isCustom as custom, status as status, dateCreated as dateCreated from "
                + utTableName + " where (displayText like :searchTerm or description like :searchTerm) order by id";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("displayText", StandardBasicTypes.STRING)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("custom", StandardBasicTypes.BOOLEAN)
                .addScalar("status", StandardBasicTypes.BOOLEAN)
                .addScalar("dateCreated", StandardBasicTypes.DATE)
                .setResultTransformer(Transformers.aliasToBean(TableData.class))
                .setParameter("searchTerm", searchTerm);


        List<TableData> dataList = query.list();
        // TODO
        /**
         * add codes for paging *
         */

        return dataList;

    }

    @Override
    @Transactional
    public Integer findTotalDataRows(String utTableName) {
        String sql = "select count(id) as rowCount from " + utTableName;
        Query query = sessionFactory
                .getCurrentSession()
                .createSQLQuery(sql).addScalar("rowCount", StandardBasicTypes.INTEGER);
        Integer rowCount = (Integer) query.list().get(0);

        return rowCount;
    }

    @Override
    @Transactional
    public LookUpTable getTableInfo(String urlId) {

        LookUpTable lookUpTable = new LookUpTable();
        Query query = sessionFactory.getCurrentSession().createSQLQuery(""
                + "select utTableName, "
                + "displayText as displayName, "
                + "urlId, description, "
                + "dateCreated from lookUpTables where urlId = :urlId")
                .addScalar("utTableName", StandardBasicTypes.STRING)
                .addScalar("displayName", StandardBasicTypes.STRING)
                .addScalar("urlId", StandardBasicTypes.STRING)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("dateCreated", StandardBasicTypes.DATE).setResultTransformer(
                        Transformers.aliasToBean(LookUpTable.class)).setParameter("urlId", urlId);

        if (query.list().size() == 1) {
            lookUpTable = (LookUpTable) query.list().get(0);
        }

        return lookUpTable;

    }

    /**
     * this method deletes the data item in the table*
     */
    @Override
    @Transactional
    public boolean deleteDataItem(String utTableName, int id) {
        String sql = "delete from " + utTableName + " where id = :id";
        Query deleteTable = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .addScalar("id", StandardBasicTypes.INTEGER).setParameter("id", id);
        try {
            deleteTable.executeUpdate();
            return true;
        } catch (Throwable ex) {
            System.err.println("deleteDataItem failed." + ex);
            return false;

        }
    }

    @Override
    @Transactional
    public TableData getTableData(Integer id, String utTableName) {
        //we create sql, we transform
        TableData tableData = new TableData();
        String sql = ("select id, displayText, description, isCustom as custom, "
                + "status "
                + " from " + utTableName + " where id = :id");
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("displayText", StandardBasicTypes.STRING)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("custom", StandardBasicTypes.BOOLEAN)
                .addScalar("status", StandardBasicTypes.BOOLEAN).setResultTransformer(
                        Transformers.aliasToBean(TableData.class)).setParameter("id", id);

        if (query.list().size() == 1) {
            tableData = (TableData) query.list().get(0);
        }
        return tableData;

    }

    
    @Override
    @Transactional
    public boolean updateTableData(TableData tableData, String utTableName) {
        boolean updated = false;
        String sql = "update " + utTableName
                + " set displayText = :displayText, "
                + "description = :description, "
                + "status = :status, "
                + "isCustom = :isCustom "
                + "where id = :id ";
        Query updateData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .addScalar("displayText", StandardBasicTypes.STRING)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("isCustom", StandardBasicTypes.BOOLEAN)
                .addScalar("status", StandardBasicTypes.BOOLEAN)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .setParameter("displayText", tableData.getDisplayText())
                .setParameter("description", tableData.getDescription())
                .setParameter("isCustom", tableData.isCustom())
                .setParameter("status", tableData.isStatus())
                .setParameter("id", tableData.getId());
        try {
            updateData.executeUpdate();
            updated = true;
        } catch (Throwable ex) {
            System.err.println("update table data failed." + ex);
        }
        return updated;

    }

    @Override
    @Transactional
    public void createTableDataHibernate(TableData tableData, String utTableName) {

        String sql = "insert into " + utTableName + " (displayText, description, isCustom, status) "
                + "values (:displayText, :description, :isCustom, :status)";
        Query insertData = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .addScalar("displayText", StandardBasicTypes.STRING)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("isCustom", StandardBasicTypes.BOOLEAN)
                .addScalar("status", StandardBasicTypes.BOOLEAN)
                .setParameter("displayText", tableData.getDisplayText())
                .setParameter("description", tableData.getDescription())
                .setParameter("isCustom", tableData.isCustom())
                .setParameter("status", tableData.isStatus());
        try {
            insertData.executeUpdate();
        } catch (Throwable ex) {
            System.err.println("insert table data failed." + ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<Macros> getMarcoList(String searchTerm) {

        Query query = sessionFactory.getCurrentSession().createQuery("from Macros where "
                + "macro_short_name like :searchTerm "
                + "order by categoryId, macro_short_name asc");
        query.setParameter("searchTerm", searchTerm);

        return query.list();
    }

    @Override
    @Transactional
    public Long findTotalMacroRows() {
        Query query = sessionFactory.getCurrentSession().createQuery("select count(id) as totalMacros from Macros");
        Long totalMacros = (Long) query.uniqueResult();
        return totalMacros;
    }
    
    @Override
    @Transactional
    public Long findtotalHL7Entries() {
        Query query = sessionFactory.getCurrentSession().createQuery("select count(id) as totalHL7 from mainHL7Details");
        Long totalHL7Entries = (Long) query.uniqueResult();
        return totalHL7Entries;
    }
    
    @Override
    @Transactional
    public Long findtotalNewsArticles() {
        Query query = sessionFactory.getCurrentSession().createQuery("select count(id) as totalArticles from newsArticle");
        Long totalNewsArticles = (Long) query.uniqueResult();
        return totalNewsArticles;
    }

    /**
     * this method deletes the macro in the table*
     */
    @Override
    @Transactional
    public boolean deleteMacro(int id) {
        Query deletMarco = sessionFactory.getCurrentSession().createQuery("delete from Macros where id = :macroId)");
        deletMarco.setParameter("macroId", id);
        deletMarco.executeUpdate();
        try {
            deletMarco.executeUpdate();
            return true;
        } catch (Throwable ex) {
            System.err.println("delete macro failed." + ex);
            return false;
        }
    }

    /**
     * this method adds a macro*
     */
    @Override
    @Transactional
    public void createMacro(Macros macro) {
        try {
            sessionFactory.getCurrentSession().save(macro);
        } catch (Throwable ex) {
            System.err.println("create macro failed." + ex);

        }
    }

    @Override
    @Transactional
    public boolean updateMacro(Macros macro) {
        try {
            sessionFactory.getCurrentSession().update(macro);
            return true;
        } catch (Throwable ex) {
            System.err.println("update macro failed." + ex);
            return false;
        }
    }

    @Override
    @Transactional
    public void createCounty(lu_Counties luc) {
        try {
            sessionFactory.getCurrentSession().save(luc);
        } catch (Throwable ex) {
            System.err.println("create county failed." + ex);

        }

    }

    @Override
    @Transactional
    public lu_Counties getCountyById(int id) {
        return (lu_Counties) sessionFactory.getCurrentSession().get(lu_Counties.class, id);
    }

    @Override
    @Transactional
    public void updateCounty(lu_Counties luc) {
        sessionFactory.getCurrentSession().update(luc);
    }

    @Override
    @Transactional
    public void createGeneralHealth(lu_GeneralHealths lu) {
        try {
            sessionFactory.getCurrentSession().save(lu);
        } catch (Throwable ex) {
            System.err.println("create general health failed." + ex);
        }
    }

    @Override
    @Transactional
    public lu_GeneralHealths getGeneralHealthById(int id) {
        try {
            return (lu_GeneralHealths) sessionFactory.getCurrentSession().get(lu_GeneralHealths.class, id);
        } catch (Throwable ex) {
            System.err.println("get general health failed." + ex);
            return null;
        }
    }

    @Override
    @Transactional
    public void updateGeneralHealth(lu_GeneralHealths lu) {
        try {
            sessionFactory.getCurrentSession().update(lu);
        } catch (Throwable ex) {
            System.err.println("update general health failed." + ex);
        }
    }

    @Override
    @Transactional
    public void createGeneralHealthStatus(lu_GeneralHealthStatuses lu) {
        try {
            sessionFactory.getCurrentSession().save(lu);
        } catch (Throwable ex) {
            System.err.println("create general health status failed." + ex);
        }
    }

    @Override
    @Transactional
    public lu_GeneralHealthStatuses getGeneralHealthStatusById(int id) {
        try {
            return (lu_GeneralHealthStatuses) sessionFactory.getCurrentSession().get(lu_GeneralHealthStatuses.class, id);
        } catch (Throwable ex) {
            System.err.println("get general health status failed." + ex);
            return null;
        }
    }

    @Override
    @Transactional
    public void updateGeneralHealthStatus(lu_GeneralHealthStatuses lu) {
        try {
            sessionFactory.getCurrentSession().update(lu);
        } catch (Throwable ex) {
            System.err.println("update general health status failed." + ex);
        }
    }

    @Override
    @Transactional
    public void createImmunization(lu_Immunizations lu) {
        try {
            sessionFactory.getCurrentSession().save(lu);
        } catch (Throwable ex) {
            System.err.println("create Immunization failed." + ex);
        }
    }

    @Override
    @Transactional
    public lu_Immunizations getImmunizationById(int id) {
        try {
            return (lu_Immunizations) sessionFactory.getCurrentSession().get(lu_Immunizations.class, id);
        } catch (Throwable ex) {
            System.err.println("get Immunization failed." + ex);
            return null;
        }
    }

    @Override
    @Transactional
    public void updateImmunization(lu_Immunizations lu) {
        try {
            sessionFactory.getCurrentSession().update(lu);
        } catch (Throwable ex) {
            System.err.println("update Immunization failed." + ex);
        }
    }

    @Override
    @Transactional
    public void createManufacturer(lu_Manufacturers lu) {
        try {
            sessionFactory.getCurrentSession().save(lu);
        } catch (Throwable ex) {
            System.err.println("create Manufacturer failed." + ex);
        }
    }

    @Override
    @Transactional
    public lu_Manufacturers getManufacturerById(int id) {
        try {
            return (lu_Manufacturers) sessionFactory.getCurrentSession().get(lu_Manufacturers.class, id);
        } catch (Throwable ex) {
            System.err.println("get Manufacturers failed." + ex);
            return null;
        }
    }

    @Override
    @Transactional
    public void updateManufacturer(lu_Manufacturers lu) {
        try {
            sessionFactory.getCurrentSession().update(lu);
        } catch (Throwable ex) {
            System.err.println("update Manufacturers failed." + ex);
        }
    }

    @Override
    @Transactional
    public void createMedicalCondition(lu_MedicalConditions lu) {
        try {
            sessionFactory.getCurrentSession().save(lu);
        } catch (Throwable ex) {
            System.err.println("create medical condition failed." + ex);
        }
    }

    @Override
    @Transactional
    public lu_MedicalConditions getMedicalConditionById(int id) {
        try {
            return (lu_MedicalConditions) sessionFactory.getCurrentSession().get(lu_MedicalConditions.class, id);
        } catch (Throwable ex) {
            System.err.println("get Medical Condition failed." + ex);
            return null;
        }
    }

    @Override
    @Transactional
    public void updateMedicalCondition(lu_MedicalConditions lu) {
        try {
            sessionFactory.getCurrentSession().update(lu);
        } catch (Throwable ex) {
            System.err.println("update Medical Condition failed." + ex);
        }
    }

    @Override
    @Transactional
    public void createMedication(lu_Medications lu) {
        try {
            sessionFactory.getCurrentSession().save(lu);
        } catch (Throwable ex) {
            System.err.println("create Medication failed." + ex);
        }
    }

    @Override
    @Transactional
    public lu_Medications getMedicationById(int id) {
        try {
            return (lu_Medications) sessionFactory.getCurrentSession().get(lu_Medications.class, id);
        } catch (Throwable ex) {
            System.err.println("get Medication failed." + ex);
            return null;
        }
    }

    @Override
    @Transactional
    public void updateMedication(lu_Medications lu) {
        try {
            sessionFactory.getCurrentSession().update(lu);
        } catch (Throwable ex) {
            System.err.println("update Medication failed." + ex);
        }
    }

    @Override
    @Transactional
    public void createProcedure(lu_Procedures lu) {
        try {
            sessionFactory.getCurrentSession().save(lu);
        } catch (Throwable ex) {
            System.err.println("create Procedure failed." + ex);
        }
    }

    @Override
    @Transactional
    public lu_Procedures getProcedureById(int id) {
        try {
            return (lu_Procedures) sessionFactory.getCurrentSession().get(lu_Procedures.class, id);
        } catch (Throwable ex) {
            System.err.println("get Procedure failed." + ex);
            return null;
        }
    }

    @Override
    @Transactional
    public void updateProcedure(lu_Procedures lu) {
        try {
            sessionFactory.getCurrentSession().update(lu);
        } catch (Throwable ex) {
            System.err.println("update Procedure failed." + ex);
        }
    }

    @Override
    @Transactional
    public void createTest(lu_Tests lu) {
        try {
            sessionFactory.getCurrentSession().save(lu);
        } catch (Throwable ex) {
            System.err.println("create Test failed." + ex);
        }
    }

    @Override
    @Transactional
    public lu_Tests getTestById(int id) {
        try {
            return (lu_Tests) sessionFactory.getCurrentSession().get(lu_Tests.class, id);
        } catch (Throwable ex) {
            System.err.println("get Test failed." + ex);
            return null;
        }
    }

    @Override
    @Transactional
    public void updateTest(lu_Tests lu) {
        try {
            sessionFactory.getCurrentSession().update(lu);
        } catch (Throwable ex) {
            System.err.println("update Test failed." + ex);
        }
    }

    @Override
    @Transactional
    public void createProcessStatus(lu_ProcessStatus lu) {
        try {
            sessionFactory.getCurrentSession().save(lu);
        } catch (Throwable ex) {
            System.err.println("create ProcessStatus failed." + ex);
        }
    }

    @Override
    @Transactional
    public lu_ProcessStatus getProcessStatusById(int id) throws Exception {
        try {
            return (lu_ProcessStatus) sessionFactory.getCurrentSession().get(lu_ProcessStatus.class, id);
        } catch (Throwable ex) {
            System.err.println("get ProcessStatus failed." + ex);
            return null;
        }
    }

    @Override
    @Transactional
    public void updateProcessStatus(lu_ProcessStatus lu) {
        try {
            sessionFactory.getCurrentSession().update(lu);
        } catch (Throwable ex) {
            System.err.println("update ProcessStatus failed." + ex);
        }
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public LogoInfo getLogoInfo() {
        LogoInfo logoInfo = new LogoInfo();
        try {
            Query logos = sessionFactory.getCurrentSession().createQuery("from LogoInfo order by id desc ").setMaxResults(1);
            List<LogoInfo> li = logos.list();
            if (li.size() != 0) {
                logoInfo = li.get(0);
            }
        } catch (Throwable ex) {
            System.err.println("get LogoInfo failed." + ex);
        }
        return logoInfo;
    }

    @Override
    @Transactional
    public void updateLogoInfo(LogoInfo logoDetails) {
        try {
            sessionFactory.getCurrentSession().update(logoDetails);
        } catch (Throwable ex) {
            System.err.println("update LogoInfo failed." + ex);
        }
    }
    
    /**
     * The 'getHL7List' function will return the list of saved hl7 standard versions.
     */
    @Override
    @Transactional
    public List<mainHL7Details> getHL7List() throws Exception {
        
       Query query = sessionFactory.getCurrentSession().createQuery("from mainHL7Details order by id desc");

        List<mainHL7Details> HL7List = query.list();
        return HL7List;
    }
    
    /**
     * The 'getHL7Details' function will the HL7 details for the passed in hl7 version.
     *
     * @Table HL7Specs
     *
     * @param	hl7Id    This will hold the id to find
     *
     * @return	This function will return a HL7Details object
     */
    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public mainHL7Details getHL7Details(int hl7Id) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(mainHL7Details.class);
        criteria.add(Restrictions.eq("id", hl7Id));
        
        if(criteria.uniqueResult() == null) {
            return null;
        }
        else {
            return (mainHL7Details) criteria.uniqueResult();
        }

    }
    
    /**
     * The 'getHL7Segments' function will return the list of segments for a specific HL7 Message.
     * 
     * @Table configurationHL7Segments
     * 
     * @return This function will return a list of HL7Segment objects
     */
    @Override
    @Transactional
    public List<mainHL7Segments> getHL7Segments(int hl7Id) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(mainHL7Segments.class);
        criteria.add(Restrictions.eq("hl7Id", hl7Id));
        criteria.addOrder(Order.asc("displayPos"));
        
        return criteria.list();
    }
    
    /**
     * The 'getHL7Elements' function will return the list of elements for a specific HL7 Message segment.
     * 
     * @Table configurationHL7Elements
     * 
     * @return This function will return a list of HL7Elements objects
     */
    @Override
    @Transactional
    public List<mainHL7Elements> getHL7Elements(int hl7Id, int segmentId) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(mainHL7Elements.class);
        criteria.add(Restrictions.eq("hl7Id", hl7Id));
        criteria.add(Restrictions.eq("segmentId", segmentId));
        criteria.addOrder(Order.asc("displayPos"));
        
        return criteria.list();
    }
   
    /**
     * the 'updateHL7Details' funciton will update/save the details of the HL7 message
     * 
     * @param details The Hl7 details object
     */
    @Override
    @Transactional
    public void updateHL7Details(mainHL7Details details) {
        sessionFactory.getCurrentSession().saveOrUpdate(details);
    }
    
    
    /**
     * The 'updateHL7Segments' function will update the segment passed to the function.
     * 
     * @param segment The segment object to update
     */
    @Override
    @Transactional
    public void updateHL7Segments(mainHL7Segments segment) {
        sessionFactory.getCurrentSession().update(segment);
    }
    
    /**
     * The 'updateHL7Elements' function will update the segment element
     * passed to the function.
     * 
     * @param element  The segment element object to update.
     */
    @Override
    @Transactional
    public void updateHL7Elements(mainHL7Elements element) {
         sessionFactory.getCurrentSession().update(element);
    }
    
    /**
     * The 'createHL7' function will save the new HL7 Segment
     * 
     * @param HL7Details The object holding the new HL7 Object
     */
    @Override
    @Transactional
    public int createHL7(mainHL7Details HL7Details) {
        Integer lastId;

        lastId = (Integer) sessionFactory.getCurrentSession().save(HL7Details);
        
        return lastId;
    }
    
    /**
     * The 'saveHL7Segment' function will save the new HL7 Segment
     * 
     * @param newSegment The object holding the new HL7 Object
     */
    @Override
    @Transactional
    public int saveHL7Segment(mainHL7Segments newSegment) {
        Integer lastId;

        lastId = (Integer) sessionFactory.getCurrentSession().save(newSegment);
        
        return lastId;
    }
    
    /**
     * The 'saveHL7Element' function will save the new HL7 Segment Element
     * 
     * @param newElement The object holding the new HL7 Element Object
     */
    @Override
    @Transactional
    public int saveHL7Element(mainHL7Elements newElement) {
        Integer lastId;

        lastId = (Integer) sessionFactory.getCurrentSession().save(newElement);
        
        return lastId;
    }
    
    @Override
    @Transactional
    public List<lu_ProcessStatus> getAllProcessStatus() throws Exception {
        
         
        Criteria statusList = sessionFactory.getCurrentSession().createCriteria(lu_ProcessStatus.class);
        statusList.add(Restrictions.eq("status",true));
        statusList.addOrder(Order.asc("category"));
        statusList.addOrder(Order.asc("displayText"));
        
        return statusList.list();
        
    }
    
    @Override
    @Transactional
    public List<lu_ProcessStatus> getAllHistoryFormProcessStatus() throws Exception {
        
        Criteria statusList = sessionFactory.getCurrentSession().createCriteria(lu_ProcessStatus.class);
        statusList.add(Restrictions.eq("status",true));
        statusList.add(Restrictions.in("id", new Integer [] {17,31,21,14,11,9,16,20,15,25,29,8,3,23,37,33,19,12,10}));
        statusList.addOrder(Order.asc("category"));
        statusList.addOrder(Order.asc("displayText"));
        
        return statusList.list();
        
    }

	@Override
	@Transactional
	public Long findTotalUsers() throws Exception {
		Query query = sessionFactory.getCurrentSession().createQuery("select count(id) as totalUsers from User");
        Long totalUsers = (Long) query.uniqueResult();
        return totalUsers;
	}

}
