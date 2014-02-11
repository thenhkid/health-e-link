package com.ut.dph.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "USERS")
public class User {

    @Transient
    private List<Integer> sectionList;
    
    @Transient
    private Date dateOrgWasCreated = null;

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

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "DATECREATED", nullable = true)
    private Date dateCreated = new Date();
    
    @Column(name = "USERTYPE", nullable = false)
    private int userType = 1;
    
    @Column(name = "DELIVERAUTHORITY", nullable = false)
    private boolean deliverAuthority = false;
    
    @Column(name = "EDITAUTHORITY", nullable = false)
    private boolean editAuthority = false;
    
    @Column(name = "CREATEAUTHORITY", nullable = false)
    private boolean createAuthority = false;
    
    @Column(name = "CANCELAUTHORITY", nullable = false)
    private boolean cancelAuthority = false;

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
    
    public int getuserType() {
        return userType;
    }
    
    public void setuserType(int userType) {
        this.userType = userType;
    }
    
    public boolean getdeliverAuthority() {
        return deliverAuthority;
    }
    
    public void setdeliverAuthority(boolean deliverAuthority) {
        this.deliverAuthority = deliverAuthority;
    }
    
    public boolean geteditAuthority() {
        return editAuthority;
    }
    
    public void seteditAuthority(boolean editAuthority) {
        this.editAuthority = editAuthority;
    }
    
    public boolean getcreateAuthority() {
        return createAuthority;
    }
    
    public void setcreateAuthority(boolean createAuthority) {
        this.createAuthority = createAuthority;
    }
    
    public boolean getcancelAuthority() {
        return cancelAuthority;
    }
    
    public void setcancelAuthority(boolean cancelAuthority) {
        this.cancelAuthority = cancelAuthority;
    }
    
    public Date getdateOrgWasCreated() {
        return dateOrgWasCreated;
    }
    
    public void setdateOrgWasCreated(Date dateOrgWasCreated) {
        this.dateOrgWasCreated = dateOrgWasCreated;
    }
}
