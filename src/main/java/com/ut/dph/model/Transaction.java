/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chadmccue
 */
public class Transaction {
    
   private int orgId;
   private int userId;
   private String batchName = null;
   private int transportMethodId;
   private String originalFileName = null;
   private int statusId;
   private int messageTypeId;
   private int transactionStatusId;
   private int targetOrgId;
   
   private ArrayList transactionRecords = null;
   
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
   
   public ArrayList gettransactionRecords() {
       return transactionRecords;
   }
   
   public void settransactionRecords(ArrayList transactionRecords) {
       this.transactionRecords = transactionRecords;
   }
   
   public int gettargetOrgId() {
       return targetOrgId;
   }

   public void settargetOrgId(int targetOrgId) {
       this.targetOrgId = targetOrgId;
   }
}
