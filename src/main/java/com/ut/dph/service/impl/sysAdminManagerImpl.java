package com.ut.dph.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ut.dph.dao.sysAdminDAO;
import com.ut.dph.model.custom.LookUpTable;
import com.ut.dph.model.custom.TableData;
import com.ut.dph.service.sysAdminManager;

@Service
public class sysAdminManagerImpl implements sysAdminManager {

	/** 
	 * about 90% of our tables falls into the standard table category, which is
	 * id, displayText, description, status and isCustom) 
	 * 
	 * @param tableName
	 * @param tableId
	 */
	@Autowired
	private sysAdminDAO sysAdminDAO;
	
	

	@Override
	public List<LookUpTable> getTableList(int maxResults, int page, String searchTerm) {
		/** this calls DAO to get a list of tables**/ 
		return sysAdminDAO.getLookUpTables(page, maxResults, searchTerm);
	}

	@Override
	public Integer findTotalLookUpTable() {
		return sysAdminDAO.findTotalLookUpTable();		
	}


	@Override
	public List<TableData> getDataList(int maxResults, int page, String utTableName, String searchTerm) {
		return sysAdminDAO.getDataList(page, maxResults, utTableName, searchTerm);
	}

	@Override
	public Integer findTotalDataRows(String tableName) {
		return sysAdminDAO.findTotalDataRows(tableName);
	}

	@Override
	public LookUpTable getTableInfo(String urlId) {
		return sysAdminDAO.getTableInfo(urlId);
		
	}

	@Override
	public void deleteDataItem(String utTableName, int id) {
		sysAdminDAO.deleteDataItem (utTableName, id);
		
	}

}