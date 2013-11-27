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
	 * @param utTableName
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
	public Integer findTotalDataRows(String utTableName) {
		return sysAdminDAO.findTotalDataRows(utTableName);
	}

	@Override
	public LookUpTable getTableInfo(String urlId) {
		return sysAdminDAO.getTableInfo(urlId);
		
	}

	@Override
	public boolean deleteDataItem(String utTableName, int id) {
		return sysAdminDAO.deleteDataItem (utTableName, id);
	}

	@Override
	public TableData getTableData(Integer id, String utTableName) {
		return sysAdminDAO.getTableData(id, utTableName);
	}

	@Override
	public Integer createTableData(TableData tableData, String utTableName) {
		return sysAdminDAO.createTableData (tableData, utTableName);
	}

	@Override
	public boolean updateTableData(TableData tableData, String utTableName) {
		return sysAdminDAO.updateTableData (tableData, utTableName);	
	}


}