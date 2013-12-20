package com.ut.dph.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "CONFIGURATIONFORMFIELDS")
public class configurationFormFields {
    
    @Transient
    private String fieldValue = null;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;

    @Column(name = "MESSAGETYPEFIELDID", nullable = false)
    private int messageTypeFieldId = 0;

    @Column(name = "CONFIGID", nullable = false)
    private int configId;
    
    @Column(name = "TRANSPORTDETAILID" , nullable = false)
    private int transportDetailId;
    
    @Column(name = "FIELDNO", nullable = false)
    private int fieldNo;

    @Column(name = "FIELDDESC", nullable = true)
    private String fieldDesc;

    @NotEmpty
    @Column(name = "FIELDLABEL", nullable = false)
    private String fieldLabel;

    @Column(name = "VALIDATIONTYPE", nullable = true)
    private int validationType = 1;

    @Column(name = "REQUIRED", nullable = false)
    private boolean required = false;

    @Column(name = "BUCKETNO", nullable = false)
    private int bucketNo;

    @Column(name = "BUCKETDSPPOS", nullable = false)
    private int bucketDspPos;

    @Column(name = "USEFIELD", nullable = false)
    private boolean useField = false;
    
    @Column(name = "SAVETOTABLENAME", nullable = false)
    private String saveToTableName;
    
    @Column(name = "SAVETOTABLECOL", nullable = false)
    private String saveToTableCol;
    
    @Column(name = "AUTOPOPULATETABLENAME", nullable = false)
    private String autoPopulateTableName;
    
    @Column(name = "AUTOPOPULATETABLECOL", nullable = false)
    private String autoPopulateTableCol;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getmessageTypeFieldId() {
        return messageTypeFieldId;
    }

    public void setmessageTypeFieldId(int messageTypeFieldId) {
        this.messageTypeFieldId = messageTypeFieldId;
    }

    public int getconfigId() {
        return configId;
    }

    public void setconfigId(int configId) {
        this.configId = configId;
    }
    
    public int gettransportDetailId() {
        return transportDetailId;
    }

    public void settransportDetailId(int transportDetailId) {
        this.transportDetailId = transportDetailId;
    }
   
    public int getFieldNo() {
        return fieldNo;
    }

    public void setFieldNo(int fieldNo) {
        this.fieldNo = fieldNo;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public int getValidationType() {
        return validationType;
    }

    public void setValidationType(int validationType) {
        this.validationType = validationType;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getBucketNo() {
        return bucketNo;
    }

    public void setBucketNo(int bucketNo) {
        this.bucketNo = bucketNo;
    }

    public int getBucketDspPos() {
        return bucketDspPos;
    }

    public void setBucketDspPos(int bucketDspPos) {
        this.bucketDspPos = bucketDspPos;
    }

    public boolean getUseField() {
        return useField;
    }

    public void setUseField(boolean useField) {
        this.useField = useField;
    }
    
    public String getfieldValue() {
        return fieldValue;
    }
    
    public void setfieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
    
    public void setsaveToTableName(String saveToTableName) {
        this.saveToTableName = saveToTableName;
    }
    
    public String getsaveToTableName() {
        return saveToTableName;
    }
    
    public void setsaveToTableCol(String saveToTableCol) {
        this.saveToTableCol = saveToTableCol;
    }
    
    public String getsaveToTableCol() {
        return saveToTableCol;
    }
    
    public void setautoPopulateTableName(String autoPopulateTableName) {
        this.autoPopulateTableName = autoPopulateTableName;
    }
    
    public String getautoPopulateTableName() {
        return autoPopulateTableName;
    }
    
    public void setautoPopulateTableCol(String autoPopulateTableCol) {
        this.autoPopulateTableCol = autoPopulateTableCol;
    }
    
    public String getautoPopulateTableCol() {
        return autoPopulateTableCol;
    }

}
