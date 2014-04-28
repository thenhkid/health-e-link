package com.ut.dph.model;

import com.ut.dph.validator.NoHtml;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Entity
@Table(name = "EMEDAPPTRANSPORTDETAILS")
public class configurationEMedAppFields {
    
    @Transient
    private CommonsMultipartFile file;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;

    @Column(name = "TRANSPORTID", nullable = false)
    private int transportId = 0;
    
    @NoHtml
    @Column(name = "FILELOCATION", nullable = true)
    private String fileLocation = null;

    @NoHtml
    @Column(name = "HOST", nullable = true)
    private String host = null;

    @NoHtml
    @Column(name = "USER", nullable = true)
    private String user = null;

    @Column(name = "CERTIFICATION", nullable = true)
    private String certification = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int gettransportId() {
        return transportId;
    }

    public void settransportId(int transportId) {
        this.transportId = transportId;
    }

    public String gethost() {
        return host;
    }

    public void sethost(String host) {
        this.host = host;
    }

    public String getfileLocation() {
        return fileLocation;
    }

    public void setfileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getuser() {
        return user;
    }

    public void setuser(String user) {
        this.user = user;
    }

    public String getcertification() {
        return certification;
    }
    
    public void setcertification(String certification) {
        this.certification = certification;
    }
    
    public CommonsMultipartFile getfile() {
        return file;
    }

    public void setfile(CommonsMultipartFile file) {
        this.file = file;
    }

}
