package com.ut.dph.dao.impl;

import java.util.List;

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
 * @see com.ut.dph.dao.sysAdminDAO
 * @author gchan
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
	public List<LookUpTable> getLookUpTables(int page, int maxResults,
			String searchTerm) {
		/**
		 * all look up tables must begin with lu_
		 * **/
		if (!searchTerm.toLowerCase().startsWith("lu_")) {
			searchTerm = "lu_%" + searchTerm + "%";
		} else {
			searchTerm = searchTerm + '%';
		}

		Query query = sessionFactory
				.getCurrentSession()
				.createSQLQuery(
						"select  "
								+ "displayText as \"tableName\","
								+ "utTableName as \"utTableName\","
								+ "count(COLUMN_NAME) as \"columnNum\", "
								+ "urlId as \"urlId\", "
								+ "TABLE_ROWS as \"rowNum\", "
								+ "description as \"description\" "
								+ "from information_schema.tables infoT, "
								+ "information_schema.COLUMNS infoc, lookUpTables "
								+ "where infoc.TABLE_SCHEMA = :schemaName "
								+ "and lookUpTables.utTableName = infot.TABLE_NAME "
								+ "and lookUpTables.utTableName = infoc.TABLE_NAME "
								+ "and infoc.TABLE_SCHEMA = infot.TABLE_SCHEMA "
								+ "and infoc.TABLE_NAME = infot.TABLE_NAME "
								+ "and infoc.TABLE_NAME like :searchTerm "
								+ "group by infoc.TABLE_NAME order by infoc.TABLE_NAME")
				.addScalar("tableName", StandardBasicTypes.STRING)
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
		// TODO
		/** add codes for paging **/

		return tableList;

	}

	/** this method returns the number of look up tables in the system **/
	@Override
	@Transactional
	public Integer findTotalLookUpTable() {

		Query query = sessionFactory
				.getCurrentSession()
				.createSQLQuery(
						"select count(*) as totalLookUpTables from lookUpTables").addScalar("totalLookUpTables", StandardBasicTypes.INTEGER);
		Integer totalTables = (Integer) query.list().get(0);

		return totalTables;
	}

	/**
	 * this method takes the table name and searchTerm (if there is one) and
	 * return the data in the table
	 **/

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<TableData> getDataList(int page, int maxResults, String utTableName, String searchTerm) {
		
		searchTerm = "%" + searchTerm + "%";
		String sql = "select id, displayText, description, "
				+ " isCustom as custom, status as status, dateCreated as dateCreated from "
				+ utTableName +  " where (displayText like :searchTerm or description like :searchTerm) order by id";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
		.addScalar("id",StandardBasicTypes.INTEGER )
		.addScalar("displayText",StandardBasicTypes.STRING )
		.addScalar("description",StandardBasicTypes.STRING )
		.addScalar("custom",StandardBasicTypes.BOOLEAN)
		.addScalar("status",StandardBasicTypes.BOOLEAN)
		.addScalar("dateCreated",StandardBasicTypes.DATE)
		.setResultTransformer(Transformers.aliasToBean(TableData.class))
		.setParameter("searchTerm",searchTerm);
		
		List<TableData> dataList = query.list();
		// TODO
		/** add codes for paging **/

		return dataList;

	}

	@Override
	@Transactional
	public Integer findTotalDataRows(String tableName) {
		String sql = "select count(*) as rowCount from " + tableName;
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
		Query query = sessionFactory.getCurrentSession().createSQLQuery("select utTableName as \"utTableName\", "
				+ "displayText as \"tableName\", "
				+ "urlId as \"urlId\", description as \"description\", "
				+ "dateCreated as \"dateCreated\" from lookUpTables where urlId = :urlId")
				.addScalar("utTableName",StandardBasicTypes.STRING )
				.addScalar("tableName",StandardBasicTypes.STRING )
				.addScalar("urlId",StandardBasicTypes.STRING )
				.addScalar("description",StandardBasicTypes.STRING)
				.addScalar("dateCreated",StandardBasicTypes.DATE).setResultTransformer(
						Transformers.aliasToBean(LookUpTable.class)).setParameter("urlId", urlId);
		
		if (query.list().size() == 1) {
			lookUpTable = (LookUpTable) query.list().get(0);
		}
		
		return lookUpTable;

	}

	/** this method deletes the data item in the table**/ 
	@Override
	@Transactional
	public boolean deleteDataItem(String utTableName, int id) {
				String sql  = "delete from " + utTableName + " where id = :id";
				System.out.println(sql);
				Query deleteTable = sessionFactory.getCurrentSession().createSQLQuery(sql)
						.addScalar("id",StandardBasicTypes.INTEGER ).setParameter("id", id);
				try {
					deleteTable.executeUpdate();
					return true;
				} catch (Throwable ex) {
	                System.err.println("deleteDataItem failed." + ex);
	                return false;
	                	
				}
	}
}
