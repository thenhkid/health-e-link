package com.ut.dph.model.custom;

import org.hibernate.validator.constraints.NotEmpty;


/**
 * This is a custom object that defines what a look up table is.
 * A look up table has a name
 * it has a certain number of columns (from this we can send it to a custom object since
 * medical services, procedure look up tables have more information. 
 * 
 * **/

public class LookUpTable {
	
	//item belong to a table Table_Name
	@NotEmpty
	private String tableName;
	
	@NotEmpty
	private int columnNum;
	
	/** this will be good to have but there is no field in MySQL for collecting this once **/
	@NotEmpty
	private String description;  

	/** this is good to have, # of items in that table **/
	@NotEmpty
	private int rowNum;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	
	
}
