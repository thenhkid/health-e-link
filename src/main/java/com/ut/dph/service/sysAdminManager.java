package com.ut.dph.service;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ut.dph.model.custom.LogoInfo;
import com.ut.dph.model.custom.LookUpTable;
import com.ut.dph.model.custom.TableData;
import com.ut.dph.model.lutables.lu_Counties;
import com.ut.dph.model.lutables.lu_GeneralHealthStatuses;
import com.ut.dph.model.lutables.lu_GeneralHealths;
import com.ut.dph.model.lutables.lu_Immunizations;
import com.ut.dph.model.lutables.lu_Manufacturers;
import com.ut.dph.model.lutables.lu_MedicalConditions;
import com.ut.dph.model.lutables.lu_Medications;
import com.ut.dph.model.lutables.lu_Procedures;
import com.ut.dph.model.lutables.lu_ProcessStatus;
import com.ut.dph.model.lutables.lu_Tests;
import com.ut.dph.model.Macros;
import com.ut.dph.model.mainHL7Details;
import com.ut.dph.model.mainHL7Elements;
import com.ut.dph.model.mainHL7Segments;

/**
 * 1. sysAdminManager should handle the adding, deleting and modifying lu_ table items 2. It should
 *
 * @author gchan
 *
 */
public interface sysAdminManager {

    /**
     * about 90% of our tables fall into the standard table category, which is id, displayText, description, status and isCustom)      *
     */
    List<LookUpTable> getTableList(String searchTerm);

    Integer findTotalLookUpTable();

    LookUpTable getTableInfo(String urlId);

    List<TableData> getDataList(String utTableName, String searchTerm);

    Integer findTotalDataRows(String utTableName);

    boolean deleteDataItem(String utTableName, int id);

    TableData getTableData(Integer id, String utTableName);

    void createTableDataHibernate(TableData tableData, String utTableName);

    boolean updateTableData(TableData tableData, String utTableName);

    List<Macros> getMarcoList(String searchTerm);

    Long findTotalMacroRows();
    
    Long findtotalHL7Entries();

    String addWildCardSearch(String searchTerm);

    String addWildCardLUSearch(String searchTerm);

    boolean deleteMacro(int id);

    void createMacro(Macros macro);

    boolean updateMacro(Macros macro);

    void createCounty(lu_Counties luc);

    lu_Counties getCountyById(int id);

    void updateCounty(lu_Counties luc);

    void createGeneralHealth(lu_GeneralHealths lu);

    lu_GeneralHealths getGeneralHealthById(int id);

    void updateGeneralHealth(lu_GeneralHealths lu);

    void createGeneralHealthStatus(lu_GeneralHealthStatuses lu);

    lu_GeneralHealthStatuses getGeneralHealthStatusById(int id);

    void updateGeneralHealthStatus(lu_GeneralHealthStatuses lu);

    void createImmunization(lu_Immunizations lu);

    lu_Immunizations getImmunizationById(int id);

    void updateImmunization(lu_Immunizations lu);

    void createManufacturer(lu_Manufacturers lu);

    lu_Manufacturers getManufacturerById(int id);

    void updateManufacturer(lu_Manufacturers lu);

    void createMedicalCondition(lu_MedicalConditions lu);

    lu_MedicalConditions getMedicalConditionById(int id);

    void updateMedicalCondition(lu_MedicalConditions lu);

    void createMedication(lu_Medications lu);

    lu_Medications getMedicationById(int id);

    void updateMedication(lu_Medications lu);

    void createProcedure(lu_Procedures lu);

    lu_Procedures getProcedureById(int id);

    void updateProcedure(lu_Procedures lu);

    void createTest(lu_Tests lu);

    lu_Tests getTestById(int id);

    void updateTest(lu_Tests lu);

    void createProcessStatus(lu_ProcessStatus lu);

    lu_ProcessStatus getProcessStatusById(int id) throws Exception;

    void updateProcessStatus(lu_ProcessStatus lu);

    boolean logoExists(String fileName);

    LogoInfo getLogoInfo();

    boolean updateLogoInfo(LogoInfo logoDetails);

    boolean writeFile(String path, InputStream inputStream, String directory);

    void copyFELogo(HttpServletRequest request, LogoInfo logoInfo);

    void copyBELogo(HttpServletRequest request, LogoInfo logoInfo);

    String getBowlinkLogoPath();

    String getDeployedPath(HttpServletRequest request);

    String getFrontEndLogoPath();

    String getBackEndLogoPath();
    
    List<mainHL7Details> getHL7List() throws Exception;
    
    mainHL7Details getHL7Details(int hl7Id) throws Exception;
    
    List<mainHL7Segments> getHL7Segments(int hl7Id);
  
    List<mainHL7Elements> getHL7Elements(int hl7Id, int segmentId);
    
    int createHL7(mainHL7Details details);

    void updateHL7Details(mainHL7Details details);

    void updateHL7Segments(mainHL7Segments segment);

    void updateHL7Elements(mainHL7Elements element);

    int saveHL7Segment(mainHL7Segments newSegment);

    int saveHL7Element(mainHL7Elements newElement);
    
    List<lu_ProcessStatus> getAllProcessStatus() throws Exception;
    
    List<lu_ProcessStatus> getAllHistoryFormProcessStatus() throws Exception;
}
