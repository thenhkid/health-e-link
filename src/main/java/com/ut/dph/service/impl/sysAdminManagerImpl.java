package com.ut.dph.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ut.dph.dao.sysAdminDAO;
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
import com.ut.dph.reference.fileSystem;
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

	@Override
	public void createGeneralHealth(lu_GeneralHealths lu) {
		sysAdminDAO.createGeneralHealth(lu);
		
	}

	@Override
	public lu_GeneralHealths getGeneralHealthById(int id) {
		return sysAdminDAO.getGeneralHealthById(id);
	}

	@Override
	public void updateGeneralHealth(lu_GeneralHealths lu) {
		sysAdminDAO.updateGeneralHealth(lu);
		
	}
	
	@Override
	public void createGeneralHealthStatus(lu_GeneralHealthStatuses lu) {
		sysAdminDAO.createGeneralHealthStatus(lu);
		
	}

	@Override
	public lu_GeneralHealthStatuses getGeneralHealthStatusById(int id) {
		return sysAdminDAO.getGeneralHealthStatusById(id);
	}

	@Override
	public void updateGeneralHealthStatus(lu_GeneralHealthStatuses lu) {
		sysAdminDAO.updateGeneralHealthStatus(lu);
		
	}

	@Override
	public void createImmunization(lu_Immunizations lu) {
		sysAdminDAO.createImmunization(lu);
	}

	@Override
	public lu_Immunizations getImmunizationById(int id) {
		return sysAdminDAO.getImmunizationById(id);
	}

	@Override
	public void updateImmunization(lu_Immunizations lu) {
		sysAdminDAO.updateImmunization(lu);
	}

	@Override
	public void createManufacturer(lu_Manufacturers lu) {
		sysAdminDAO.createManufacturer(lu);
		
	}

	@Override
	public lu_Manufacturers getManufacturerById(int id) {
		return sysAdminDAO.getManufacturerById(id);
	}

	@Override
	public void updateManufacturer(lu_Manufacturers lu) {
		sysAdminDAO.updateManufacturer(lu);
	}

	@Override
	public void createMedicalCondition(lu_MedicalConditions lu) {
		sysAdminDAO.createMedicalCondition(lu);
	}

	@Override
	public lu_MedicalConditions getMedicalConditionById(int id) {
		return sysAdminDAO.getMedicalConditionById(id);
	}

	@Override
	public void updateMedicalCondition(lu_MedicalConditions lu) {
		sysAdminDAO.updateMedicalCondition(lu);
	}

	@Override
	public void createMedication(lu_Medications lu) {
		sysAdminDAO.createMedication(lu);
		
	}

	@Override
	public lu_Medications getMedicationById(int id) {
		return sysAdminDAO.getMedicationById(id);
	}

	@Override
	public void updateMedication(lu_Medications lu) {
		sysAdminDAO.updateMedication(lu);
	}
	
	@Override
	public void createProcedure(lu_Procedures lu) {
		sysAdminDAO.createProcedure(lu);
		
	}

	@Override
	public lu_Procedures getProcedureById(int id) {
		return sysAdminDAO.getProcedureById(id);
	}

	@Override
	public void updateProcedure(lu_Procedures lu) {
		sysAdminDAO.updateProcedure(lu);
	}

	@Override
	public void createTest(lu_Tests lu) {
		sysAdminDAO.createTest(lu);
		
	}

	@Override
	public lu_Tests getTestById(int id) {
		return sysAdminDAO.getTestById(id);
	}

	@Override
	public void updateTest(lu_Tests lu) {
		sysAdminDAO.updateTest(lu);
	}

	@Override
	public void createProcessStatus(lu_ProcessStatus lu) {
		sysAdminDAO.createProcessStatus(lu);
		
	}

	@Override
	public lu_ProcessStatus getProcessStatusById(int id) throws Exception {
		return sysAdminDAO.getProcessStatusById(id);
	}

	@Override
	public void updateProcessStatus(lu_ProcessStatus lu) {
		sysAdminDAO.updateProcessStatus(lu);
	}

	@Override
	public boolean logoExists(String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LogoInfo getLogoInfo() {
		return sysAdminDAO.getLogoInfo();
	}

	@Override
	public boolean updateLogoInfo(LogoInfo logoDetails) {
		/** first write logo to Bowink folder so we can copy it in the event that a new war is deployed**/
		boolean errors = false;
		
		String bowlinkPath = getBowlinkLogoPath();
		
		/** work with Front End Logo First **/
		if (logoDetails.getFrontEndFile().getSize() > 0) {
			MultipartFile feFile = logoDetails.getFrontEndFile();
	        String oldFileName = feFile.getOriginalFilename();
	        
			/**we rename all files to frontEndLogo and backEndLogo**/
	        int lastIndex = oldFileName.lastIndexOf(".");
	        String extension =  oldFileName.substring(lastIndex, oldFileName.length());
	        String feFileName = "frontEndLogo" + extension;
	        logoDetails.setFrontEndLogoName(feFileName);
	        
	        /** we write fe logo to bowlink **/
			try {
				 writeFile(feFileName, feFile.getInputStream(), bowlinkPath);
			} catch (Exception e) {
				 e.printStackTrace();
				 errors = true;
			}
			
			
		}
		
		/** back end logo **/
		if (logoDetails.getBackEndFile().getSize() > 0) {
			
			MultipartFile beFile = logoDetails.getBackEndFile();
	        String oldBEFileName = beFile.getOriginalFilename();
	        
			/**we rename all files to frontEndLogo and backEndLogo**/
	        int lastBEIndex = oldBEFileName.lastIndexOf(".");
	        String beExtension =  oldBEFileName.substring(lastBEIndex, oldBEFileName.length());
	        String beFileName = "backEndLogo" + beExtension;
	        logoDetails.setBackEndLogoName(beFileName);
			
			
			/** we write fe logo to bowlink **/
			try {
				 writeFile(beFileName, beFile.getInputStream(), bowlinkPath);
			} catch (Exception e) {
				 e.printStackTrace();
				 errors = true;
			}
			
			
	}
		/**we change date modified**/
		java.util.Date today = new java.util.Date();
		java.sql.Timestamp now = new java.sql.Timestamp(today.getTime());
		logoDetails.setDateModified(now);
		
		/** now we save the logo info**/
		sysAdminDAO.updateLogoInfo(logoDetails);
		return errors;
	}

	@Override
	public boolean writeFile(String fileName, InputStream inputStream, String directory) {
		boolean writeError = false;
		 OutputStream outputStream = null;

        try {
            
            File newFile = null;
            
            newFile = new File(directory + fileName);

            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            writeError = true;
        }
        return writeError;
	}

	@Override
	public void copyFELogo(HttpServletRequest request, LogoInfo logoInfo) {
		try {
			File feLogo = new File(getBowlinkLogoPath() + logoInfo.getFrontEndLogoName());
			InputStream inputStream = new FileInputStream(feLogo);
			writeFile(logoInfo.getFrontEndLogoName(), inputStream, getDeployedPath(request) +getFrontEndLogoPath());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public String getBowlinkLogoPath() {
		fileSystem dir = new fileSystem();
		dir.setDir("libraryFiles", "logo");
		return dir.getDir();
	}


	@Override
	public String getDeployedPath(HttpServletRequest request) {
		ServletContext servletContext = request.getSession().getServletContext();
		String relativeWebPath = "../../../img/admin/health-e-link/sp-health-e-link.png";
		String absoluteDiskPath = servletContext.getRealPath(relativeWebPath);
		int firstIndex = absoluteDiskPath.indexOf("../");
		return absoluteDiskPath.substring(0, firstIndex);
		
	}

	@Override
	public String getFrontEndLogoPath() {
		String feWebPath = "dspResources/img/front-end/health-e-link/";
		if (System.getProperty("os.name").indexOf("win") >= 0) {
			feWebPath = "dspResources\\img\\front-end\\health-e-link\\";
		}
		return feWebPath;
	}

	@Override
	public String getBackEndLogoPath() {
		String beWebPath = "dspResources/img/admin/health-e-link/";
		if (System.getProperty("os.name").indexOf("win") >= 0) {
			beWebPath = "dspResources\\img\\admin\\health-e-link\\";
		}
		return beWebPath;
	}

	@Override
	public void copyBELogo(HttpServletRequest request, LogoInfo logoInfo) {
		/**back end **/
		try {
			File beLogo = new File(getBowlinkLogoPath() + logoInfo.getBackEndLogoName());
			InputStream inputStreamBE = new FileInputStream(beLogo);
			writeFile(logoInfo.getBackEndLogoName(), inputStreamBE, getDeployedPath(request) +getBackEndLogoPath());
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	
		
}
