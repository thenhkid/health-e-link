package com.ut.dph.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Entity
@Table(name = "CONFIGURATIONTRANSPORTDETAILS")
public class configurationTransport {

    @Transient
    private List<configurationFormFields> fields = null;

    @Transient
    private CommonsMultipartFile file = null;
    
    @Transient
    private List<configurationFTPFields> FTPfields = null;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;

    @Column(name = "CONFIGID", nullable = false)
    private int configId;

    @Column(name = "TRANSPORTMETHOD", nullable = false)
    private int transportMethod;

    @Column(name = "FILENAME", nullable = true)
    private String fileName = null;

    @Digits(integer = 4, fraction = 0, message = "Wrong Number")
    @Column(name = "MESSAGETYPECOLNO", nullable = true)
    private int messageTypeColNo = 0;

    @Column(name = "MESSAGETYPECUSTOMVAL", nullable = true)
    private String messageTypeCustomVal = null;

    @Digits(integer = 4, fraction = 0, message = "Wrong Number")
    @Column(name = "TARGETORGCOLNO", nullable = true)
    private int targetOrgColNo = 0;

    @Column(name = "FILETYPE", nullable = true)
    private int fileType = 0;

    @Column(name = "FILEDELIMITER", nullable = true)
    private int fileDelimiter = 0;

    @Column(name = "CONTAINSHEADER", nullable = false)
    private boolean containsHeader = false;

    @Column(name = "REPORTABLEFIELD1", nullable = false)
    private int reportableField1 = 0;

    @Column(name = "REPORTABLEFIELD2", nullable = false)
    private int reportableField2 = 0;

    @Column(name = "REPORTABLEFIELD3", nullable = false)
    private int reportableField3 = 0;
    
    @Column(name = "REPORTABLEFIELD4", nullable = false)
    private int reportableField4 = 0;
    
    @Column(name = "STATUS", nullable = false)
    private boolean status = true;
    
    @Column(name = "TARGETFILENAME", nullable = true)
    private String targetFileName = null;
    
    @Column(name = "APPENDDATETIME", nullable = false)
    private boolean appendDateTime = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getconfigId() {
        return configId;
    }

    public void setconfigId(int configId) {
        this.configId = configId;
    }

    public int gettransportMethod() {
        return transportMethod;
    }

    public void settransportMethod(int transportMethod) {
        this.transportMethod = transportMethod;
    }

    public int getmessageTypeColNo() {
        return messageTypeColNo;
    }

    public void setmessageTypeColNo(int messageTypeColNo) {
        this.messageTypeColNo = messageTypeColNo;
    }

    public String getmessageTypeCustomVal() {
        return messageTypeCustomVal;
    }

    public void setmessageTypeCustomVal(String messageTypeCustomVal) {
        this.messageTypeCustomVal = messageTypeCustomVal;
    }

    public int gettargetOrgColNo() {
        return targetOrgColNo;
    }

    public void settargetOrgColNo(int targetOrgColNo) {
        this.targetOrgColNo = targetOrgColNo;
    }

    public int getfileType() {
        return fileType;
    }

    public void setfileType(int fileType) {
        this.fileType = fileType;
    }

    public int getfileDelimiter() {
        return fileDelimiter;
    }

    public void setfileDelimiter(int fileDelimiter) {
        this.fileDelimiter = fileDelimiter;
    }

    public String getfileName() {
        return fileName;
    }

    public void setfileName(String fileName) {
        this.fileName = fileName;
    }

    public CommonsMultipartFile getFile() {
        return file;
    }

    public void setFile(CommonsMultipartFile file) {
        this.file = file;
    }

    public List<configurationFormFields> getFields() {
        return fields;
    }

    public void setFields(List<configurationFormFields> fields) {
        this.fields = fields;
    }

    public boolean getcontainsHeader() {
        return containsHeader;
    }

    public void setcontainsHeader(boolean containsHeader) {
        this.containsHeader = containsHeader;
    }
    
    public int getreportableField1() {
        return reportableField1;
    }

    public void setreportableField1(int reportableField1) {
        this.reportableField1 = reportableField1;
    }
    
    public int getreportableField2() {
        return reportableField2;
    }

    public void setreportableField2(int reportableField2) {
        this.reportableField2 = reportableField2;
    }
    
    public int getreportableField3() {
        return reportableField3;
    }

    public void setreportableField3(int reportableField3) {
        this.reportableField3 = reportableField3;
    }
    
    public int getreportableField4() {
        return reportableField4;
    }

    public void setreportableField4(int reportableField4) {
        this.reportableField4 = reportableField4;
    }
    
    public List<configurationFTPFields> getFTPFields() {
        return FTPfields;
    }
    
    public void setFTPFields(List<configurationFTPFields> FTPFields) {
        this.FTPfields = FTPFields;
    }
    
    public boolean getstatus() {
        return status;
    }

    public void setstatus(boolean status) {
        this.status = status;
    }
    
    public String gettargetFileName() {
        return targetFileName;
    }
    
    public void settargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }
    
     public boolean getappendDateTime() {
        return appendDateTime;
    }

    public void setappendDateTime(boolean appendDateTime) {
        this.appendDateTime = appendDateTime;
    }

}
