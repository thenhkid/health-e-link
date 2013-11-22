package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.custom.TableData;
import com.ut.dph.model.custom.LookUpTable;


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
	
	Integer findTotalDataRows(String tableName);
	
	LookUpTable  getTableInfo(String urlId);
	
	boolean deleteDataItem (String utTableName, int id);
}
