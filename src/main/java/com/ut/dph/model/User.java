package com.ut.dph.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "USERS")
public class User {

    @Transient
    private List<Integer> sectionList;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;

    @Column(name = "STATUS", nullable = false)
    private boolean status = false;

    @Column(name = "ORGID", nullable = false)
    private int orgId;

    @NotEmpty
    @Column(name = "FIRSTNAME", nullable = false)
    private String firstName;

    @NotEmpty
    @Column(name = "LASTNAME", nullable = true)
    private String lastName;

    @NotEmpty
    @Size(min = 5, max = 10)
    @Column(name = "USERNAME", nullable = false)
    private String username;

    @NotEmpty
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ROLEID", nullable = false)
    private int roleId = 2;

    @Column(name = "MAINCONTACT", nullable = false)
    private int mainContact = 0;

    @Column(name = "SENDEMAILALERT", nullable = true)
    private boolean sendEmailAlert = false;

    @Email
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "DATECREATED", nullable = true)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateCreated = new Date();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getMainContact() {
        return mainContact;
    }

    public void setMainContact(int mainContact) {
        this.mainContact = mainContact;
    }

    public boolean getSendEmailAlert() {
        return sendEmailAlert;
    }

    public void setSendEmailAlert(boolean sendEmailAlert) {
        this.sendEmailAlert = sendEmailAlert;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Integer> getsectionList() {
        return this.sectionList;
    }

    public void setsectionList(List<Integer> sectionList) {
        this.sectionList = sectionList;
    }
}
