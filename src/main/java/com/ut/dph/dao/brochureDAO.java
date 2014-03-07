package com.ut.dph.dao;

import java.util.List;

import com.ut.dph.model.Brochure;
import org.springframework.stereotype.Repository;

@Repository
public interface brochureDAO {
	
	Integer createBrochure(Brochure brochure);
	
	void updateBrochure(Brochure brochure);
	
	void deleteBrochure(int brochureId);
		  
	Brochure getBrochureById(int brochureId);
	  
	List<Brochure> findBrochures(int orgId, String searchTerm);

}
