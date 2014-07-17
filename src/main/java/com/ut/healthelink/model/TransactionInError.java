package com.ut.healthelink.model;

import java.util.Date;

import com.ut.healthelink.validator.NoHtml;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Entity
@Table(name = "transactionInErrors")
public class TransactionInError {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotEmpty
    @NoHtml
    @Column(name = "batchUploadId", nullable = false)
    private Integer batchUploadId;

    @NoHtml
    @Column(name = "configId", nullable = true)
    private Integer configId = 0;

    @NoHtml
    @Column(name = "transactionInId", nullable = true)
    private Integer transactionInId = 0;

    @NoHtml
    @Column(name = "fieldNo", nullable = true)
    private Integer fieldNo = 0;

    @NoHtml
    @Column(name = "required", nullable = true)
    private Integer required = 0;

    @NoHtml
    @Column(name = "errorId", nullable = true)
    private Integer errorId = 0;

    @NoHtml
    @Column(name = "cwId", nullable = true)
    private Integer cwId = 0;

    @NoHtml
    @Column(name = "macroId", nullable = true)
    private Integer macroId = 0;

    @NoHtml
    @Column(name = "validationTypeId", nullable = true)
    private Integer validationTypeId = 0;

    @NoHtml
    @Column(name = "stackTrace", nullable = true)
    private String stackTrace;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATECREATED", nullable = true)
    private Date dateCreated = new Date();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBatchUploadId() {
        return batchUploadId;
    }

    public void setBatchUploadId(Integer batchUploadId) {
        this.batchUploadId = batchUploadId;
    }

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public Integer getTransactionInId() {
        return transactionInId;
    }

    public void setTransactionInId(Integer transactionInId) {
        this.transactionInId = transactionInId;
    }

    public Integer getFieldNo() {
        return fieldNo;
    }

    public void setFieldNo(Integer fieldNo) {
        this.fieldNo = fieldNo;
    }

    public Integer getErrorId() {
        return errorId;
    }

    public void setErrorId(Integer errorId) {
        this.errorId = errorId;
    }

    public Integer getCwId() {
        return cwId;
    }

    public void setCwId(Integer cwId) {
        this.cwId = cwId;
    }

    public Integer getMacroId() {
        return macroId;
    }

    public void setMacroId(Integer macroId) {
        this.macroId = macroId;
    }

    public Integer getValidationTypeId() {
        return validationTypeId;
    }

    public void setValidationTypeId(Integer validationTypeId) {
        this.validationTypeId = validationTypeId;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getRequired() {
        return required;
    }

    public void setRequired(Integer required) {
        this.required = required;
    }

}
