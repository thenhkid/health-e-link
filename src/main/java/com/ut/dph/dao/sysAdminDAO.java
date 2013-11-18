package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.custom.LookUpTableItems;
import com.ut.dph.model.custom.LookUpTable;


public interface sysAdminDAO {
	
	List <LookUpTable> getLookUpTables(int page, int maxResults);
	
	Integer findTotalLookUpTable();
	
	List <LookUpTable> findLookUpTables(String searchTerm);
	
}
