package com.ut.dph.dao.impl;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.dao.sysAdminDAO;
import com.ut.dph.model.custom.TableData;
import com.ut.dph.model.custom.LookUpTable;
/**

 *
 */


@Repository
public class sysAdminDAOImpl implements sysAdminDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	private String schemaName = "universalTranslator";
	
	/** this gets a list of Lookup tables **/
	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List <LookUpTable> getLookUpTables(int page, int maxResults) {
		
		 Query query= 
				sessionFactory.getCurrentSession().createSQLQuery( "select  "
						+ "displayText as \"tableName\","
						+ "utTableName as \"utTableName\","
						+ "count(COLUMN_NAME) as \"columnNum\", "
						+ "urlId as \"urlId\", "
						+ "TABLE_ROWS as \"rowNum\", "
						+ "description as \"description\" "
						+ "from information_schema.tables infoT, information_schema.COLUMNS infoc, lookUpTables "
						+ "where infoc.TABLE_SCHEMA = :schemaName "
						+ "and lookUpTables.utTableName = infot.TABLE_NAME "
						+ "and lookUpTables.utTableName = infoc.TABLE_NAME "
						+ "and infoc.TABLE_SCHEMA = infot.TABLE_SCHEMA "
						+ "and infoc.TABLE_NAME = infot.TABLE_NAME "
						+ "and infoc.TABLE_NAME like'lu_%' "
						+ "group by infoc.TABLE_NAME order by infoc.TABLE_NAME;")
		                .addScalar("tableName",StandardBasicTypes.STRING )
		                .addScalar("utTableName",StandardBasicTypes.STRING )
		                .addScalar("urlId",StandardBasicTypes.STRING )
		                .addScalar("columnNum",StandardBasicTypes.INTEGER )
		                .addScalar("rowNum",StandardBasicTypes.INTEGER )
		                .addScalar("description",StandardBasicTypes.STRING )
		                .setResultTransformer(Transformers.aliasToBean(LookUpTable.class))
		                .setParameter("schemaName", schemaName);
			
		List <LookUpTable> tableList = query.list();
		//TODO
		
		/**add codes for paging**/
		
		return tableList;
	
	}


	/** this method returns the number of look up tables in the system
	 **/
	@Override
	@Transactional
	public Integer findTotalLookUpTable() {
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery("select count(*) as totalLookUpTables "
				+ " from lookUpTables")
				.addScalar("totalLookUpTables",StandardBasicTypes.INTEGER);
		Integer totalTables = (Integer) query.list().get(0);
		
		return totalTables;
	}

	/**
	 * This returns the search for look up tables
	 * **/
	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<LookUpTable> findLookUpTables(String searchTerm) {
		/** 
		 * all look up tables must begin with lu_
		 * **/
		if (!searchTerm.toLowerCase().startsWith("lu_")){
			searchTerm = "lu_%" + searchTerm + "%";				 
		} else {
			searchTerm = searchTerm  + '%';
		}
		
		Query query= 
				sessionFactory.getCurrentSession().createSQLQuery( "select  "
						+ "displayText as \"tableName\","
						+ "utTableName as \"utTableName\","
						+ "count(COLUMN_NAME) as \"columnNum\", "
						+ "urlId as \"urlId\", "
						+ "TABLE_ROWS as \"rowNum\", "
						+ "description as \"description\" "
						+ "from information_schema.tables infoT, information_schema.COLUMNS infoc, lookUpTables "
						+ "where infoc.TABLE_SCHEMA = :schemaName "
						+ "and lookUpTables.utTableName = infot.TABLE_NAME "
						+ "and lookUpTables.utTableName = infoc.TABLE_NAME "
						+ "and infoc.TABLE_SCHEMA = infot.TABLE_SCHEMA "
						+ "and infoc.TABLE_NAME = infot.TABLE_NAME "
						+ "and infoc.TABLE_NAME like :searchTerm "
						+ "group by infoc.TABLE_NAME order by infoc.TABLE_NAME;")
		                .addScalar("tableName",StandardBasicTypes.STRING )
		                .addScalar("utTableName",StandardBasicTypes.STRING )
		                .addScalar("urlId",StandardBasicTypes.STRING )
		                .addScalar("columnNum",StandardBasicTypes.INTEGER )
		                .addScalar("rowNum",StandardBasicTypes.INTEGER )
		                .addScalar("description",StandardBasicTypes.STRING )
		                .setResultTransformer(Transformers.aliasToBean(LookUpTable.class))
		                .setParameter("schemaName", schemaName)
		                .setParameter("searchTerm", searchTerm);
			
		List <LookUpTable> tableList = query.list();
		return tableList;
		
		
	}
		
	
	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List <TableData> getDataList(int page, int maxResults, String tableName, String searchTerm) {
		tableName = "lu_genders";
		searchTerm = "male";
		 String sql = "select id, displayText, description, "
		 		+ " isCustom as custom, status as status, dateCreated as dateCreated from " + tableName;
		 Query query= 
				sessionFactory.getCurrentSession().createSQLQuery(sql);
		
						/**
						"select  "
						+ ":tableName as tableName, id, displayText, description,"
						+ "  from :tableName"
						
						+ " order by infoc.TABLE_NAME;")
		                .addScalar("tableName",StandardBasicTypes.STRING )
		                .addScalar("columnNum",StandardBasicTypes.INTEGER )
		                .addScalar("rowNum",StandardBasicTypes.INTEGER )
		                .addScalar("description",StandardBasicTypes.STRING )
		                .setResultTransformer(Transformers.aliasToBean(LookUpTable.class))
		                .setParameter("tableName", tableName)
		                .setParameter("searchTerm", searchTerm);
		                ;
						**/
		List <TableData> dataList = query.list();
		//TODO
		/**add codes for paging**/
		
		return dataList;
	
	}


	@Override
	public Integer findTotalDataRows(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}


	

}
