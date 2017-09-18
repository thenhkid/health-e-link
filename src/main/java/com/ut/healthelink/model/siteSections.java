package com.ut.healthelink.model;

import com.ut.healthelink.validator.NoHtml;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "UTSITEFEATURES")
public class siteSections {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;

    @NotEmpty
    @NoHtml
    @Column(name = "FEATURENAME", nullable = false)
    private String featureName;
    
    @Column(name = "url", nullable = false)
    private String url = "";
    
    @Column(name = "featureDesc", nullable = true)
    private String featureDesc = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfeatureName() {
        return featureName;
    }

    public void setfeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFeatureDesc() {
        return featureDesc;
    }

    public void setFeatureDesc(String featureDesc) {
        this.featureDesc = featureDesc;
    }
    
}
