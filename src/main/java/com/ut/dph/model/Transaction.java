/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author chadmccue
 */
public class Transaction {
    
   private int orgId;
   private int userId;
   private int configId;
   private String batchName = null;
   private int transportMethodId;
   private String originalFileName = null;
   private int statusId;
   private int messageTypeId;
   private int transactionStatusId;
   private int targetOrgId;
   private boolean autoRelease = true;
   private Date dateSubmitted = null;
   private String messageTypeName = null;
   private int batchId = 0;
   private int transactionId = 0;
   private int transactionRecordId = 0;
   
   private List<transactionRecords> sourceOrgFields = null;
   private List<transactionRecords> sourceProviderFields = null;
   private List<transactionRecords> targetOrgFields = null;
   private List<transactionRecords> targetProviderFields = null;
   private List<transactionRecords> patientFields = null;
   private List<transactionRecords> detailFields = null;
   
   
   public int getorgId() {
       return orgId;
   }

   public void setorgId(int orgId) {
       this.orgId = orgId;
   }
   
   public int getuserId() {
       return userId;
   }

   public void setuserId(int userId) {
       this.userId = userId;
   }
   
   public int getconfigId() {
       return configId;
   }

   public void setconfigId(int configId) {
       this.configId = configId;
   }
   
   public String getbatchName() {
       return batchName;
   }

   public void setbatchName(String batchName) {
       this.batchName = batchName;
   }
   
   public int gettransportMethodId() {
       return transportMethodId;
   }

   public void settransportMethodId(int transportMethodId) {
       this.transportMethodId = transportMethodId;
   }
   
   public String getoriginalFileName() {
       return originalFileName;
   }

   public void setoriginalFileName(String originalFileName) {
       this.originalFileName = originalFileName;
   }
   
   public int getstatusId() {
       return statusId;
   }

   public void setstatusId(int statusId) {
       this.statusId = statusId;
   }
   
   public int getmessageTypeId() {
       return messageTypeId;
   }

   public void setmessageTypeId(int messageTypeId) {
       this.messageTypeId = messageTypeId;
   }
   
   public int gettransactionStatusId() {
       return transactionStatusId;
   }

   public void settransactionStatusId(int transactionStatusId) {
       this.transactionStatusId = transactionStatusId;
   }
  
   
   public int gettargetOrgId() {
       return targetOrgId;
   }

   public void settargetOrgId(int targetOrgId) {
       this.targetOrgId = targetOrgId;
   }
   
   public List<transactionRecords> getsourceOrgFields() {
       return sourceOrgFields;
   }
   
   public void setsourceOrgFields(List<transactionRecords> sourceOrgFields) {
       this.sourceOrgFields = sourceOrgFields;
   }
   
   public List<transactionRecords> getsourceProviderFields() {
       return sourceProviderFields;
   }
   
   public void setsourceProviderFields(List<transactionRecords> sourceProviderFields) {
       this.sourceProviderFields = sourceProviderFields;
   }
   
   public List<transactionRecords> gettargetOrgFields() {
       return targetOrgFields;
   }
   
   public void settargetOrgFields(List<transactionRecords> targetOrgFields) {
       this.targetOrgFields = targetOrgFields;
   }
   
   public List<transactionRecords> gettargetProviderFields() {
       return targetProviderFields;
   }
   
   public void settargetProviderFields(List<transactionRecords> targetProviderFields) {
       this.targetProviderFields = targetProviderFields;
   }
   
   public List<transactionRecords> getpatientFields() {
       return patientFields;
   }
   
   public void setpatientFields(List<transactionRecords> patientFields) {
       this.patientFields = patientFields;
   }
   
   public List<transactionRecords> getdetailFields() {
       return detailFields;
   }
   
   public void setdetailFields(List<transactionRecords> detailFields) {
       this.detailFields = detailFields;
   }
   
   public boolean getautoRelease() {
       return autoRelease;
   }
   
   public void setautoRelease(boolean autoRelease) {
       this.autoRelease = autoRelease;
   }
   
   public Date getdateSubmitted() {
       return dateSubmitted;
   }
   
   public void setdateSubmitted(Date dateSubmitted) {
       this.dateSubmitted = dateSubmitted;
   }
   
   public String getmessageTypeName() {
       return messageTypeName;
   }
   
   public void setmessageTypeName(String messageTypeName) {
       this.messageTypeName = messageTypeName;
   }
   
   public int gettransactionRecordId() {
       return transactionRecordId;
   }
   
   public void settransactionRecordId(int transactionRecordId) {
       this.transactionRecordId = transactionRecordId;
   }
   
   public int getbatchId() {
       return batchId;
   }
   
   public void setbatchId(int batchId) {
       this.batchId = batchId;
   }
   
   public int gettransactionId() {
       return transactionId;
   }
   
   public void settransactionId(int transactionId) {
       this.transactionId = transactionId;
   }
}
