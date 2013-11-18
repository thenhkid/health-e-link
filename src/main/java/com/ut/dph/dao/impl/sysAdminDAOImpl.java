package com.ut.dph.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ut.dph.dao.sysAdminDAO;
import com.ut.dph.model.custom.LookUpTableItems;
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
						+ "infoc.TABLE_NAME as \"tableName\","
						+ "count(COLUMN_NAME) as \"columnNum\", "
						+ "TABLE_ROWS as \"rowNum\", "
						+ "'' as \"description\" "
						+ "from information_schema.tables infoT, information_schema.COLUMNS infoc "
						+ "where infoc.TABLE_SCHEMA = :schemaName "
						+ "and infoc.TABLE_SCHEMA = infot.TABLE_SCHEMA "
						+ "and infoc.TABLE_NAME = infot.TABLE_NAME "
						+ "and infoc.TABLE_NAME like'lu_%' "
						+ "group by infoc.TABLE_NAME order by infoc.TABLE_NAME;")
		                .addScalar("tableName",StandardBasicTypes.STRING )
		                .addScalar("columnNum",StandardBasicTypes.INTEGER )
		                .addScalar("rowNum",StandardBasicTypes.INTEGER )
		                .addScalar("description",StandardBasicTypes.STRING )
		                .setResultTransformer(Transformers.aliasToBean(LookUpTable.class))
		                .setParameter("schemaName", schemaName);
			
		List <LookUpTable> tableList = query.list();
		return tableList;
	
	}


	/** this method returns the number of look up tables in the system
	 **/
	@Override
	@Transactional
	public Integer findTotalLookUpTable() {
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery("select count(*) as totalLookUpTables "
				+ " from information_schema.tables"
				+ " where TABLE_SCHEMA = :schemaName"
				+ " and TABLE_NAME like'lu_%'"
				)
				.addScalar("totalLookUpTables",StandardBasicTypes.INTEGER )
				.setParameter("schemaName", schemaName);
		
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
						+ "infoc.TABLE_NAME as \"tableName\","
						+ "count(COLUMN_NAME) as \"columnNum\", "
						+ "TABLE_ROWS as \"rowNum\", "
						+ "'' as \"description\" "
						+ "from information_schema.tables infoT, information_schema.COLUMNS infoc "
						+ "where infoc.TABLE_SCHEMA = :schemaName "
						+ "and infoc.TABLE_SCHEMA = infot.TABLE_SCHEMA "
						+ "and infoc.TABLE_NAME = infot.TABLE_NAME "
						+ "and infoc.TABLE_NAME like :searchTerm "
						+ "group by infoc.TABLE_NAME order by infoc.TABLE_NAME;")
		                .addScalar("tableName",StandardBasicTypes.STRING )
		                .addScalar("columnNum",StandardBasicTypes.INTEGER )
		                .addScalar("rowNum",StandardBasicTypes.INTEGER )
		                .addScalar("description",StandardBasicTypes.STRING )
		                .setResultTransformer(Transformers.aliasToBean(LookUpTable.class))
		                .setParameter("schemaName", schemaName)
						.setParameter("searchTerm", searchTerm);
			
		List <LookUpTable> tableList = query.list();
		return tableList;
		
		
	}
		
	



	

}
