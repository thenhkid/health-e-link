package com.ut.dph.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ut.dph.dao.sysAdminDAO;
import com.ut.dph.model.custom.LookUpTable;
import com.ut.dph.model.custom.TableData;
import com.ut.dph.model.lutables.lu_Counties;
import com.ut.dph.model.Macros;
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
		return sysAdminDAO.getLookUpTables(page, maxResults, addWildCardSearch(searchTerm));
	}

	@Override
	public Integer findTotalLookUpTable() {
		return sysAdminDAO.findTotalLookUpTable();		
	}


	@Override
	public List<TableData> getDataList(int maxResults, int page, String utTableName, String searchTerm) {
		return sysAdminDAO.getDataList(page, maxResults, utTableName, addWildCardSearch(searchTerm));
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

	@Override
	public void createTableDataHibernate(TableData tableData, String utTableName) {
		sysAdminDAO.createTableDataHibernate (tableData, utTableName);
	}

	@Override
	public List<Macros> getMarcoList(int maxResults, int page, String searchTerm) {
		return sysAdminDAO.getMarcoList(maxResults, page, addWildCardSearch(searchTerm));
	}

	@Override
	public Long findTotalMacroRows() {
		return sysAdminDAO.findTotalMacroRows();
	}

	@Override
	public String addWildCardSearch(String searchTerm) {
		
		if (!searchTerm.startsWith("%")) {
			searchTerm = "%" + searchTerm ;
		}
		if (!searchTerm.endsWith("%")) {
			searchTerm =  searchTerm + "%";
		}
		
		return searchTerm;
	}

	@Override
	public String addWildCardLUSearch(String searchTerm) {
		/**
		 * all look up tables must begin with lu_
		 * **/
		if (searchTerm.toLowerCase().startsWith("%")) {
			searchTerm = "lu_" + searchTerm;
		} else if (!searchTerm.toLowerCase().startsWith("lu_")) {
			searchTerm = "lu_%" + searchTerm;
		}
		if (!searchTerm.endsWith("%")) {
			searchTerm = searchTerm+ "%";
		}
		return searchTerm;
	}

	@Override
	public boolean deleteMacro(int id) {
		return sysAdminDAO.deleteMacro (id);
	}

	@Override
	public void createMacro(Macros macro) {
		sysAdminDAO.createMacro(macro);
	}
	
	@Override
	public boolean updateMacro(Macros macro) {
		return sysAdminDAO.updateMacro(macro);
		
	}

	@Override
	public void createCounty(lu_Counties luc) {
		sysAdminDAO.createCounty(luc);	
	}

	@Override
	public lu_Counties getCountyById(int id) {
		return sysAdminDAO.getCountyById(id);	
	}

	@Override
	public void updateCounty(lu_Counties luc) {
		 sysAdminDAO.updateCounty(luc);
	}


}