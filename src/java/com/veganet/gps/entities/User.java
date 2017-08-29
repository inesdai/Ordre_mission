/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Amine
 */
@Entity
@Table(name = "usergps")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUserid", query = "SELECT u FROM User u WHERE u.userid = :userid"),
    @NamedQuery(name = "User.findByAdmincreateaccess", query = "SELECT u FROM User u WHERE u.admincreateaccess = :admincreateaccess"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),

    @NamedQuery(name = "User.findByUserPass", query = "SELECT u FROM User u WHERE u.password = :password AND u.username = :username "),
    @NamedQuery(name = "User.findByResetpassword", query = "SELECT u FROM User u WHERE u.resetpassword = :resetpassword"),
    @NamedQuery(name = "User.findByFirstname", query = "SELECT u FROM User u WHERE u.firstname = :firstname"),
    @NamedQuery(name = "User.findByLastname", query = "SELECT u FROM User u WHERE u.lastname = :lastname"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByAccesslevel", query = "SELECT u FROM User u WHERE u.accesslevel = :accesslevel"),
    @NamedQuery(name = "User.findByDateofbirth", query = "SELECT u FROM User u WHERE u.dateofbirth = :dateofbirth"),
    @NamedQuery(name = "User.findByPhonenumber", query = "SELECT u FROM User u WHERE u.phonenumber = :phonenumber"),
    @NamedQuery(name = "User.findByCreatedate", query = "SELECT u FROM User u WHERE u.createdate = :createdate"),
    @NamedQuery(name = "User.findByIsactive", query = "SELECT u FROM User u WHERE u.isactive = :isactive"),
    @NamedQuery(name = "User.findByDistributerId", query = "SELECT u FROM User u WHERE u.distributerid = :distributer"),
    @NamedQuery(name = "User.findAdminDistributerForDistributer", query = "SELECT u FROM User u WHERE u.distributerid = :distributerid AND u.accesslevel= :accesslevel"),
    @NamedQuery(name = "User.findByIsdeleted", query = "SELECT u FROM User u WHERE u.isdeleted = :isdeleted")})
public class User implements Serializable {

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "CINORPASSPORT")
    private String cinorpassport;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "VILLE")
    private String ville;
    @Column(name = "LICENSEVALIDITY")
    @Temporal(TemporalType.DATE)
    private Date licensevalidity;
    @Size(max = 10)
    @Column(name = "Colonne_23")
    private String colonne23;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "FUELVOLUME")
    private Double fuelvolume;
    @Size(max = 255)
    @Column(name = "GENDER")
    private String gender;
    @Column(name = "POSTAL_CODE")
    private Integer postalCode;
    @Size(max = 255)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 255)
    @Column(name = "PICTUREPATH")
    private String picturepath;
    @OneToMany(mappedBy = "userid")
    private Collection<Mission> missionCollection;
    @OneToMany(mappedBy = "userid")
    private Collection<Responsible> responsibleCollection;
    @OneToMany(mappedBy = "resUserid")
    private Collection<Responsible> responsibleCollection1;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USERID")
    private Integer userid;

    @Column(name = "ADMINCREATEACCESS")
    private Short admincreateaccess;
    @Size(max = 128)
    @Column(name = "PASSWORD")
    private String password;

    @Size(max = 128)
    @Transient
    private String confirmPassword;

    @Column(name = "RESETPASSWORD")
    private Short resetpassword;
    @Size(max = 20)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Size(max = 20)
    @Column(name = "LASTNAME")
    private String lastname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")
//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 30)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 20)
    @Column(name = "USERNAME")
    private String username;

    @Size(max = 30)
    @Column(name = "USEDMAP")
    private String usedmap;
    @Column(name = "ACCESSLEVEL")
    private Integer accesslevel;
    @Column(name = "DATEOFBIRTH")
    @Temporal(TemporalType.DATE)
    private Date dateofbirth;
    @Size(max = 20)
    @Column(name = "PHONENUMBER")
    private String phonenumber;
    @Basic(optional = false)
    @Column(name = "CREATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdate;
    @Column(name = "ISACTIVE")
    private Short isactive;
    @Column(name = "ISDELETED")
    private Short isdeleted;
    @Transient
    
    private short staticsType;

    @OneToMany(mappedBy = "userid")
    private Collection<Alert> alertCollection;
    
    @OneToMany(mappedBy = "userid")
    private Collection<Logs> logsCollection;
   /////////////////////////////////importe mapping

    //every user makes many devu=icedistrutor(mapped with addedby in Devicedistributer)
    @OneToMany(mappedBy = "addedby")
    private Collection<Devicedistributor> devicedistributoraddbymeCollection;
    //every devicedistributer are affect to user (affectation to me)
    @OneToMany(mappedBy = "customerid")
    private Collection<Devicedistributor> mydevicedistributorCollection;
    //every user has many devices
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addedby")
    private Collection<Device> deviceCollection;
    //admin can create many distributer
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addeddby")
    private Collection<Distributer> distributerCollection;
    //admin can creaty manu other users
    @OneToMany(mappedBy = "createdby")
    private Collection<User> userCollection;
    //every user is created by one user
    @JoinColumn(name = "CREATEDBY", referencedColumnName = "USERID")
    @ManyToOne
    @JsonIgnore
    private User createdby;
    //every user appart to distributer
    @JoinColumn(name = "DISTRIBUTERID", referencedColumnName = "DISTRIBUTERID")
    @ManyToOne
    @JsonIgnore
    private Distributer distributerid;

    public String getUsedmap() {

        return usedmap;
    }

    public void setUsedmap(String usedmap) {

        this.usedmap = usedmap;
    }

    public User() {

    }

    public User(Integer userid) {
        this.userid = userid;
    }

    public User(Integer userid, Date createdate) {
        this.userid = userid;
        this.createdate = createdate;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Short getAdmincreateaccess() {

        return admincreateaccess;
    }

    public void setAdmincreateaccess(Short admincreateaccess) {
        this.admincreateaccess = admincreateaccess;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Short getResetpassword() {
        return resetpassword;
    }

    public void setResetpassword(Short resetpassword) {
        this.resetpassword = resetpassword;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAccesslevel() {
        return accesslevel;
    }

    public void setAccesslevel(Integer accesslevel) {
        this.accesslevel = accesslevel;
    }

    public Date getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(Date dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Short getIsactive() {
        return isactive;
    }

    public void setIsactive(Short isactive) {
        this.isactive = isactive;
    }

    public Short getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Short isdeleted) {
        this.isdeleted = isdeleted;
    }

    @XmlTransient
    public Collection<Devicedistributor> getDevicedistributoraddbymeCollection() {
        return devicedistributoraddbymeCollection;
    }

    public void setDevicedistributoraddbymeCollection(Collection<Devicedistributor> devicedistributoraddbymeCollection) {
        this.devicedistributoraddbymeCollection = devicedistributoraddbymeCollection;
    }

    @XmlTransient
    public Collection<Devicedistributor> getMydevicedistributorCollection() {
        return mydevicedistributorCollection;
    }

    public void setMydevicedistributorCollection(Collection<Devicedistributor> mydevicedistributorCollection) {
        this.mydevicedistributorCollection = mydevicedistributorCollection;
    }

    @XmlTransient
    public Collection<Alert> getAlertCollection() {
        return alertCollection;
    }

    public void setAlertCollection(Collection<Alert> alertCollection) {
        this.alertCollection = alertCollection;
    }

    @XmlTransient
    public Collection<Device> getDeviceCollection() {
        return deviceCollection;
    }

    public void setDeviceCollection(Collection<Device> deviceCollection) {
        this.deviceCollection = deviceCollection;
    }

    @XmlTransient
    public Collection<Logs> getLogsCollection() {
        return logsCollection;
    }

    public void setLogsCollection(Collection<Logs> logsCollection) {
        this.logsCollection = logsCollection;
    }

    @XmlTransient
    public Collection<Distributer> getDistributerCollection() {
        return distributerCollection;
    }

    public void setDistributerCollection(Collection<Distributer> distributerCollection) {
        this.distributerCollection = distributerCollection;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    public User getCreatedby() {
        return createdby;
    }

    public void setCreatedby(User createdby) {
        this.createdby = createdby;
    }

    public Distributer getDistributerid() {
        return distributerid;
    }

    public void setDistributerid(Distributer distributerid) {
        this.distributerid = distributerid;
    }
  
    public short getStaticsType() {
        return staticsType;
    }

    public void setStaticsType(short staticsType) {
        this.staticsType = staticsType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  firstname + " "+lastname;
    }

    /**
     * @return the confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * @param confirmPassword the confirmPassword to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCinorpassport() {
        return cinorpassport;
    }

    public void setCinorpassport(String cinorpassport) {
        this.cinorpassport = cinorpassport;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Date getLicensevalidity() {
        return licensevalidity;
    }

    public void setLicensevalidity(Date licensevalidity) {
        this.licensevalidity = licensevalidity;
    }

    public String getColonne23() {
        return colonne23;
    }

    public void setColonne23(String colonne23) {
        this.colonne23 = colonne23;
    }

    public Double getFuelvolume() {
        return fuelvolume;
    }

    public void setFuelvolume(Double fuelvolume) {
        this.fuelvolume = fuelvolume;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicturepath() {
        return picturepath;
    }

    public void setPicturepath(String picturepath) {
        this.picturepath = picturepath;
    }



    @XmlTransient
    @JsonIgnore
    public Collection<Mission> getMissionCollection() {
        return missionCollection;
    }

    public void setMissionCollection(Collection<Mission> missionCollection) {
        this.missionCollection = missionCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Responsible> getResponsibleCollection() {
        return responsibleCollection;
    }

    public void setResponsibleCollection(Collection<Responsible> responsibleCollection) {
        this.responsibleCollection = responsibleCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Responsible> getResponsibleCollection1() {
        return responsibleCollection1;
    }

    public void setResponsibleCollection1(Collection<Responsible> responsibleCollection1) {
        this.responsibleCollection1 = responsibleCollection1;
    }

}
