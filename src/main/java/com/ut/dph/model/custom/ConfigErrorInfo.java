/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.model.custom;

import java.util.List;

/**
 * 
 * @author gchan
 */
public class ConfigErrorInfo {

	private Integer batchId;
	private Integer configId = 0;
	private String configName;
	private String messageTypeName;
	
	
	/** labels stays the same for each config **/
	private List<String> rptFieldHeadings;
	private List<Integer> rptFieldNos;
	private List <TransErrorDetail> transErrorDetails;
	
	
	public Integer getBatchId() {
		return batchId;
	}
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	public Integer getConfigId() {
		return configId;
	}
	public void setConfigId(Integer configId) {
		this.configId = configId;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public String getMessageTypeName() {
		return messageTypeName;
	}
	public void setMessageTypeName(String messageTypeName) {
		this.messageTypeName = messageTypeName;
	}
	public List<String> getRptFieldHeadings() {
		return rptFieldHeadings;
	}
	public void setRptFieldHeadings(List<String> rptFieldHeadings) {
		this.rptFieldHeadings = rptFieldHeadings;
	}

	public List<Integer> getRptFieldNos() {
		return rptFieldNos;
	}
	public void setRptFieldNos(List<Integer> rptFieldNos) {
		this.rptFieldNos = rptFieldNos;
	}
	public List<TransErrorDetail> getTransErrorDetails() {
		return transErrorDetails;
	}
	public void setTransErrorDetails(List<TransErrorDetail> transErrorDetails) {
		this.transErrorDetails = transErrorDetails;
	}
	
}
