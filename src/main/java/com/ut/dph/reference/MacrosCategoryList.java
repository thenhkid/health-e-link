package com.ut.dph.reference;

import java.util.LinkedHashMap;
import java.util.Map;


public class MacrosCategoryList {
	
	@SuppressWarnings("rawtypes")
	public Map getCategories() throws Exception {
		
		LinkedHashMap<String,String> categories = new LinkedHashMap<String,String>();
		
		categories.put("Compare Values", "Compare Values");
		categories.put("HL7 MESSAGE", "HL7 MESSAGE");
		categories.put("Maplink Specs", "Maplink Specs");
		categories.put("Stored Procedures", "Stored Procedures");
		
		
		return categories;
	}

}
