/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.healthelink.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author chadmccue
 */
public class Transaction {
    
   private int orgId;
   private int sourceSubOrgId;
   private int userId;
   private int configId;
   private String batchName = null;
   private int transportMethodId;
   private String originalFileName = null;
   private int statusId;
   private String statusValue;
   private int messageTypeId;
   private int transactionStatusId;
   private int targetOrgId;
   private int targetSubOrgId = 0;
   private List<Integer> targetConfigId;
   private boolean autoRelease = true;
   private Date dateSubmitted = null;
   private String messageTypeName = null;
   private int batchId = 0;
   private int transactionId = 0;
   private int transactionRecordId = 0;
   private int transactionTargetId = 0;
   private int sourceType = 1;
   private int internalStatusId = 0;
   private int orginialTransactionId = 0;
   private String reportableField1 = null;
   private String reportableField2 = null;
   private String reportableField3 = null;
   private String reportableField4 = null;
   private String reportableFieldHeading1 = null;
   private String reportableFieldHeading2 = null;
   private String reportableFieldHeading3 = null;
   private String reportableFieldHeading4 = null;
   
   private List<transactionRecords> sourceOrgFields = null;
   private List<transactionRecords> sourceProviderFields = null;
   private List<transactionRecords> targetOrgFields = null;
   private List<transactionRecords> targetProviderFields = null;
   private List<transactionRecords> patientFields = null;
   private List<transactionRecords> detailFields = null;
   private Integer attachmentLimit;
   
   private int messageStatus = 1;
   
   
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
   
   public String getstatusValue() {
       return statusValue;
   }
   
   public void setstatusValue(String statusValue) {
       this.statusValue = statusValue;
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
   
   public List<Integer> gettargetConfigId() {
       return targetConfigId;
   }
   
   public void settargetConfigId(List<Integer> targetConfigId) {
       this.targetConfigId = targetConfigId;
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
   
   public int gettransactionTargetId() {
       return transactionTargetId;
   }
   
   public void settransactionTargetId(int transactionTargetId) {
       this.transactionTargetId = transactionTargetId;
   }
   
   public int getsourceType() {
       return sourceType;
   }
   
   public void setsourceType(int sourceType) {
       this.sourceType = sourceType;
   }
   
   public int getinternalStatusId() {
       return internalStatusId;
   }
   
   public void setinternalStatusId(int internalStatusId) {
       this.internalStatusId = internalStatusId;
   }
   
   public int getorginialTransactionId() {
       return orginialTransactionId;
   }
   
   public void setorginialTransactionId(int orginialTransactionId) {
       this.orginialTransactionId = orginialTransactionId;
   }
   
   public String getreportableField1() {
       return reportableField1;
   }
   
   public void setreportableField1(String reportableField1) {
       this.reportableField1 = reportableField1;
   }
   
   public String getreportableField2() {
       return reportableField2;
   }
   
   public void setreportableField2(String reportableField2) {
       this.reportableField2 = reportableField2;
   }
   
   public String getreportableField3() {
       return reportableField3;
   }
   
   public void setreportableField3(String reportableField3) {
       this.reportableField3 = reportableField3;
   }
   
   public String getreportableField4() {
       return reportableField4;
   }
   
   public void setreportableField4(String reportableField4) {
       this.reportableField4 = reportableField4;
   }
   
   public String getreportableFieldHeading1() {
       return reportableFieldHeading1;
   }
   
   public void setreportableFieldHeading1(String reportableFieldHeading1) {
       this.reportableFieldHeading1 = reportableFieldHeading1;
   }
   
   public String getreportableFieldHeading2() {
       return reportableFieldHeading2;
   }
   
   public void setreportableFieldHeading2(String reportableFieldHeading2) {
       this.reportableFieldHeading2 = reportableFieldHeading2;
   }
   
   public String getreportableFieldHeading3() {
       return reportableFieldHeading3;
   }
   
   public void setreportableFieldHeading3(String reportableFieldHeading3) {
       this.reportableFieldHeading3 = reportableFieldHeading3;
   }
   
   public String getreportableFieldHeading4() {
       return reportableFieldHeading4;
   }
   
   public void setreportableFieldHeading4(String reportableFieldHeading4) {
       this.reportableFieldHeading4 = reportableFieldHeading4;
   }
   
   public int getmessageStatus() {
        return messageStatus;
    }
    
    public void setmessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public int getTargetSubOrgId() {
        return targetSubOrgId;
    }

    public void setTargetSubOrgId(int targetSubOrgId) {
        this.targetSubOrgId = targetSubOrgId;
    }
    
    public int getSourceSubOrgId() {
        return sourceSubOrgId;
    }

    public void setSourceSubOrgId(int sourceSubOrgId) {
        this.sourceSubOrgId = sourceSubOrgId;
    }

    public Integer getAttachmentLimit() {
        return attachmentLimit;
    }

    public void setAttachmentLimit(Integer attachmentLimit) {
        this.attachmentLimit = attachmentLimit;
    }
    
    

}
