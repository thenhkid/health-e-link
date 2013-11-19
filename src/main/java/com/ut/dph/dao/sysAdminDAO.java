package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.custom.TableData;
import com.ut.dph.model.custom.LookUpTable;


public interface sysAdminDAO {
	
	List <LookUpTable> getLookUpTables(int page, int maxResults);
	
	Integer findTotalLookUpTable();
	
	List <LookUpTable> findLookUpTables(String searchTerm);
	
	List <TableData> getDataList(int page, int maxResults, String tableName, String searchTerm);
	
	Integer findTotalDataRows(String tableName);
}
