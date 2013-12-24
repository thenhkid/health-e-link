package com.ut.dph.service;

import java.util.List;

import com.ut.dph.model.custom.LookUpTable;
import com.ut.dph.model.custom.TableData;
import com.ut.dph.model.lutables.lu_Counties;
import com.ut.dph.model.lutables.lu_GeneralHealthStatuses;
import com.ut.dph.model.lutables.lu_GeneralHealths;
import com.ut.dph.model.lutables.lu_Immunizations;
import com.ut.dph.model.lutables.lu_Manufacturers;
import com.ut.dph.model.lutables.lu_MedicalConditions;
import com.ut.dph.model.Macros;

/** 
 * 1. sysAdminManager should handle the adding, deleting and modifying lu_ table items
 * 2. It should 
 * 
 * @author gchan
 *
 */

public interface sysAdminManager {

	/** 
	 * about 90% of our tables fall into the standard table category, which is
	 * id, displayText, description, status and isCustom) 

	 */
	
	List <LookUpTable> getTableList(int maxResults, int page, String searchTerm);
	
	Integer findTotalLookUpTable();
	
	LookUpTable getTableInfo(String urlId);
	
	List <TableData> getDataList(int maxResults, int page, String utTableName, String searchTerm);
	
	Integer findTotalDataRows(String utTableName);
	
	boolean deleteDataItem (String utTableName, int id);
	
	TableData getTableData(Integer id, String utTableName);
	
	Integer createTableData (TableData tableData, String utTableName);
	void createTableDataHibernate (TableData tableData, String utTableName);
	boolean updateTableData (TableData tableData, String utTableName);
	
	List <Macros> getMarcoList(int maxResults, int page, String searchTerm);
	
	Long findTotalMacroRows();
	
	String addWildCardSearch(String searchTerm);
	
	String addWildCardLUSearch(String searchTerm);
	
	boolean deleteMacro (int id);
	
	void createMacro (Macros macro);
	
	boolean updateMacro (Macros macro);
	
	void createCounty (lu_Counties luc);
	
	lu_Counties getCountyById (int id);
	
	void updateCounty (lu_Counties luc);
	
	void createGeneralHealth (lu_GeneralHealths lu);
	
	lu_GeneralHealths getGeneralHealthById (int id);
	
	void updateGeneralHealth (lu_GeneralHealths lu);
	
	void createGeneralHealthStatus (lu_GeneralHealthStatuses lu);
	
	lu_GeneralHealthStatuses getGeneralHealthStatusById (int id);
	
	void updateGeneralHealthStatus (lu_GeneralHealthStatuses lu);
	
	void createImmunization (lu_Immunizations lu);
	
	lu_Immunizations getImmunizationById (int id);
	
	void updateImmunization (lu_Immunizations lu);
	
	void createManufacturer (lu_Manufacturers lu);
	
	lu_Manufacturers getManufacturerById (int id);
	
	void updateManufacturer (lu_Manufacturers lu);
	
	void createMedicalCondition (lu_MedicalConditions lu);
	
	lu_MedicalConditions getMedicalConditionById (int id);
	
	void updateMedicalCondition(lu_MedicalConditions lu);
}

