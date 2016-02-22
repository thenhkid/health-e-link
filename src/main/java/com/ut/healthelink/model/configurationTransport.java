package com.ut.healthelink.model;

import com.ut.healthelink.validator.NoHtml;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Entity
@Table(name = "CONFIGURATIONTRANSPORTDETAILS")
public class configurationTransport {

    @Transient
    private List<configurationFormFields> fields = null;

    @Transient
    private List<configurationFTPFields> FTPfields = null;

    @Transient
    private List<configurationRhapsodyFields> rhapsodyFields = null;

    @Transient
    private List<configurationWebServiceFields> webServiceFields = null;

    @Transient
    private String delimChar = null;

    @Transient
    private boolean containsHeaderRow;

    @Transient
    private List<Integer> messageTypes = null;
    
    @Transient
    private CommonsMultipartFile ccdTemplatefile = null, hl7PDFTemplatefile = null;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;

    @Column(name = "CONFIGID", nullable = false)
    private int configId;

    @Column(name = "TRANSPORTMETHODID", nullable = false)
    private int transportMethodId;

    @Column(name = "FILETYPE", nullable = true)
    private int fileType = 1;

    @Column(name = "FILEDELIMITER", nullable = true)
    private int fileDelimiter = 2;

    @Column(name = "STATUS", nullable = false)
    private boolean status = true;

    @NoHtml
    @Column(name = "TARGETFILENAME", nullable = true)
    private String targetFileName = null;

    @Column(name = "APPENDDATETIME", nullable = false)
    private boolean appendDateTime = false;

    @Column(name = "MAXFILESIZE", nullable = false)
    private int maxFileSize = 0;

    @Column(name = "CLEARRECORDS", nullable = false)
    private boolean clearRecords = false;

    @Column(name = "FILELOCATION", nullable = true)
    private String fileLocation = null;

    @Column(name = "AUTORELEASE", nullable = false)
    private boolean autoRelease = true;

    @Column(name = "ERRORHANDLING", nullable = false)
    private int errorHandling = 2;

    @Column(name = "MERGEBATCHES", nullable = false)
    private boolean mergeBatches = true;

    @Column(name = "COPIEDTRANSPORTID", nullable = false)
    private int copiedTransportId = 0;
    
    @Column(name = "massTranslation", nullable = false)
    private boolean massTranslation = false;

    @NoHtml
    @Column(name = "FILEEXT", nullable = false)
    private String fileExt = null;

    @Column(name = "encodingId", nullable = false)
    private int encodingId = 1;

    @Column(name = "ccdSampleTemplate", nullable = true)
    private String ccdSampleTemplate = null;
    
    @Column(name = "attachmentLimit", nullable = true)
    private String attachmentLimit = "";
    
    @Column(name = "attachmentRequired", nullable = false)
    private Boolean attachmentRequired = false;
    
    @Column(name = "attachmentNote", nullable = true)
    private String attachmentNote = "";
    
    @Column(name = "HL7PDFSampleTemplate", nullable = true)
    private String HL7PDFSampleTemplate = null;

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

    public int gettransportMethodId() {
        return transportMethodId;
    }

    public void settransportMethodId(int transportMethodId) {
        this.transportMethodId = transportMethodId;
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

    public List<configurationFormFields> getFields() {
        return fields;
    }

    public void setFields(List<configurationFormFields> fields) {
        this.fields = fields;
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

    public int getmaxFileSize() {
        return maxFileSize;
    }

    public void setmaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public boolean getclearRecords() {
        return clearRecords;
    }

    public void setclearRecords(boolean clearRecords) {
        this.clearRecords = clearRecords;
    }

    public String getfileLocation() {
        return fileLocation;
    }

    public void setfileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public boolean getautoRelease() {
        return autoRelease;
    }

    public void setautoRelease(boolean autoRelease) {
        this.autoRelease = autoRelease;
    }

    public int geterrorHandling() {
        return errorHandling;
    }

    public void seterrorHandling(int errorHandling) {
        this.errorHandling = errorHandling;
    }

    public boolean getmergeBatches() {
        return mergeBatches;
    }

    public void setmergeBatches(boolean mergeBatches) {
        this.mergeBatches = mergeBatches;
    }

    public List<Integer> getmessageTypes() {
        return messageTypes;
    }

    public void setmessageTypes(List<Integer> messageTypes) {
        this.messageTypes = messageTypes;
    }

    public int getcopiedTransportId() {
        return copiedTransportId;
    }

    public void setcopiedTransportId(int copiedTransportId) {
        this.copiedTransportId = copiedTransportId;
    }

    public String getDelimChar() {
        return delimChar;
    }

    public void setDelimChar(String delimChar) {
        this.delimChar = delimChar;
    }

    public boolean getContainsHeaderRow() {
        return containsHeaderRow;
    }

    public void setContainsHeaderRow(boolean containsHeaderRow) {
        this.containsHeaderRow = containsHeaderRow;
    }

    public String getfileExt() {
        return fileExt;
    }

    public void setfileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public int getEncodingId() {
        return encodingId;
    }

    public void setEncodingId(int encodingId) {
        this.encodingId = encodingId;
    }

    public List<configurationRhapsodyFields> getRhapsodyFields() {
        return rhapsodyFields;
    }

    public void setRhapsodyFields(List<configurationRhapsodyFields> rhapsodyFields) {
        this.rhapsodyFields = rhapsodyFields;
    }

    public List<configurationWebServiceFields> getWebServiceFields() {
        return webServiceFields;
    }

    public void setWebServiceFields(
            List<configurationWebServiceFields> webServiceFields) {
        this.webServiceFields = webServiceFields;
    }
    
    public String getCcdSampleTemplate() {
        return ccdSampleTemplate;
    }

    public void setCcdSampleTemplate(String ccdSampleTemplate) {
        this.ccdSampleTemplate = ccdSampleTemplate;
    }

    public CommonsMultipartFile getCcdTemplatefile() {
        return ccdTemplatefile;
    }

    public void setCcdTemplatefile(CommonsMultipartFile ccdTemplatefile) {
        this.ccdTemplatefile = ccdTemplatefile;
    }

    public String getAttachmentLimit() {
        return attachmentLimit;
    }

    public void setAttachmentLimit(String attachmentLimit) {
        this.attachmentLimit = attachmentLimit;
    }

    public Boolean getAttachmentRequired() {
        return attachmentRequired;
    }

    public void setAttachmentRequired(Boolean attachmentRequired) {
        this.attachmentRequired = attachmentRequired;
    }

    public String getAttachmentNote() {
        return attachmentNote;
    }

    public void setAttachmentNote(String attachmentNote) {
        this.attachmentNote = attachmentNote;
    }

    public String getHL7PDFSampleTemplate() {
        return HL7PDFSampleTemplate;
    }

    public void setHL7PDFSampleTemplate(String HL7PDFSampleTemplate) {
        this.HL7PDFSampleTemplate = HL7PDFSampleTemplate;
    }

    public CommonsMultipartFile getHl7PDFTemplatefile() {
        return hl7PDFTemplatefile;
    }

    public void setHl7PDFTemplatefile(CommonsMultipartFile hl7PDFTemplatefile) {
        this.hl7PDFTemplatefile = hl7PDFTemplatefile;
    }

	public boolean isMassTranslation() {
		return massTranslation;
	}

	public void setMassTranslation(boolean massTranslation) {
		this.massTranslation = massTranslation;
	}

}
