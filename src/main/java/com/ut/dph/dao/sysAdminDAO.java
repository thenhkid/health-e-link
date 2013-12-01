package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.custom.LookUpTable;
import com.ut.dph.model.custom.TableData;


public interface sysAdminDAO {
	
	/**
	 * @param page
	 * @param maxResults
	 * @param searchTerm
	 * @return
	 */
	List <LookUpTable> getLookUpTables(int page, int maxResults, String searchTerm);
	
	Integer findTotalLookUpTable();
	
	List <TableData> getDataList(int page, int maxResults, String utTableName, String searchTerm);
	
	Integer findTotalDataRows(String utTableName);
	
	LookUpTable  getTableInfo(String urlId);
	
	boolean deleteDataItem (String utTableName, int id);
	
	TableData getTableData (Integer id, String utTableName);
	
	Integer createTableData (TableData tableData, String utTableName);
	
	boolean updateTableData (TableData tableData, String utTableName);

}
