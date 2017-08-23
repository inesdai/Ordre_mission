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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Amine
 */
@Entity
@Table(name = "driver")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Driver.findAll", query = "SELECT d FROM Driver d"),
    @NamedQuery(name = "Driver.findByDriverid", query = "SELECT d FROM Driver d WHERE d.driverid = :driverid"),
    @NamedQuery(name = "Driver.findByFirstname", query = "SELECT d FROM Driver d WHERE d.firstname = :firstname"),
    @NamedQuery(name = "Driver.findByLastname", query = "SELECT d FROM Driver d WHERE d.lastname = :lastname"),
    @NamedQuery(name = "Driver.findByCinorpassport", query = "SELECT d FROM Driver d WHERE d.cinorpassport = :cinorpassport"),
    @NamedQuery(name = "Driver.findByTitle", query = "SELECT d FROM Driver d WHERE d.title = :title"),
    @NamedQuery(name = "Driver.findByGender", query = "SELECT d FROM Driver d WHERE d.gender = :gender"),
    @NamedQuery(name = "Driver.findByPhonenumber", query = "SELECT d FROM Driver d WHERE d.phonenumber = :phonenumber"),
    @NamedQuery(name = "Driver.findByEmail", query = "SELECT d FROM Driver d WHERE d.email = :email"),
    @NamedQuery(name = "Driver.findByPicturepath", query = "SELECT d FROM Driver d WHERE d.picturepath = :picturepath"),
    @NamedQuery(name = "Driver.findByDateofbirth", query = "SELECT d FROM Driver d WHERE d.dateofbirth = :dateofbirth"),
    @NamedQuery(name = "Driver.findByAddress", query = "SELECT d FROM Driver d WHERE d.address = :address"),
    @NamedQuery(name = "Driver.findByLicensevalidity", query = "SELECT d FROM Driver d WHERE d.licensevalidity = :licensevalidity"),
    @NamedQuery(name = "Driver.findByFuelvolume", query = "SELECT d FROM Driver d WHERE d.fuelvolume = :fuelvolume"),
    @NamedQuery(name = "Driver.findByCreatedate", query = "SELECT d FROM Driver d WHERE d.createdate = :createdate")})
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DRIVERID")
    private Integer driverid;
    @Size(max = 20)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Size(max = 20)
    @Column(name = "LASTNAME")
    private String lastname;
    @Size(max = 10)
    @Column(name = "CINORPASSPORT")
    private String cinorpassport;
    @Size(max = 30)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 30)
    @Column(name = "GENDER")
    private String gender;
    @Column(name = "PHONENUMBER")
        @Size(max = 20)
    private String phonenumber;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 30)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 100)
    @Column(name = "PICTUREPATH")
    private String picturepath;
    @Column(name = "DATEOFBIRTH")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateofbirth;
    @Size(max = 50)
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "LICENSEVALIDITY")
    @Temporal(TemporalType.DATE)
    private Date licensevalidity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "FUELVOLUME")
    private Double fuelvolume;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdate;
    @JoinTable(name = "drive", joinColumns = {
        @JoinColumn(name = "DRIVERID", referencedColumnName = "DRIVERID")}, inverseJoinColumns = {
        @JoinColumn(name = "VEHICLEID", referencedColumnName = "VEHICLEID")})
    @ManyToMany
    private Collection<Vehicle> vehicleCollection;

    public Driver() {
    }

    public Driver(Integer driverid) {
        this.driverid = driverid;
    }

    public Driver(Integer driverid, Date createdate) {
        this.driverid = driverid;
        this.createdate = createdate;
    }

    public Integer getDriverid() {
        return driverid;
    }

    public void setDriverid(Integer driverid) {
        this.driverid = driverid;
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

    public String getCinorpassport() {
        return cinorpassport;
    }

    public void setCinorpassport(String cinorpassport) {
        this.cinorpassport = cinorpassport;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicturepath() {
        return picturepath;
    }

    public void setPicturepath(String picturepath) {
        this.picturepath = picturepath;
    }

    public Date getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(Date dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getLicensevalidity() {
        return licensevalidity;
    }

    public void setLicensevalidity(Date licensevalidity) {
        this.licensevalidity = licensevalidity;
    }

    public Double getFuelvolume() {
        return fuelvolume;
    }

    public void setFuelvolume(Double fuelvolume) {
        this.fuelvolume = fuelvolume;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    @XmlTransient
    public Collection<Vehicle> getVehicleCollection() {
        return vehicleCollection;
    }

    public void setVehicleCollection(Collection<Vehicle> vehicleCollection) {
        this.vehicleCollection = vehicleCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (driverid != null ? driverid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Driver)) {
            return false;
        }
        Driver other = (Driver) object;
        if ((this.driverid == null && other.driverid != null) || (this.driverid != null && !this.driverid.equals(other.driverid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.veganet.gps.entities.Driver[ driverid=" + driverid + " ]";
    }

}
