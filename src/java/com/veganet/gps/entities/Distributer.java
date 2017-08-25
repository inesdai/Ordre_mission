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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "distributer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Distributer.findAll", query = "SELECT d FROM Distributer d"),
    @NamedQuery(name = "Distributer.findByDistributerid", query = "SELECT d FROM Distributer d WHERE d.distributerid = :distributerid"),
    @NamedQuery(name = "Distributer.findByLogopath", query = "SELECT d FROM Distributer d WHERE d.logopath = :logopath"),
    @NamedQuery(name = "Distributer.findByContact", query = "SELECT d FROM Distributer d WHERE d.contact = :contact"),
    @NamedQuery(name = "Distributer.findByCreatedate", query = "SELECT d FROM Distributer d WHERE d.createdate = :createdate"),
    @NamedQuery(name = "Distributer.findByIsdeleted", query = "SELECT d FROM Distributer d WHERE d.isdeleted = :isdeleted"),
    @NamedQuery(name = "Distributer.findByParentid", query = "SELECT d FROM Distributer d WHERE d.parentid = :parentid AND d.isdeleted=0"),
    @NamedQuery(name = "Distributer.findByParentidNull", query = "SELECT d FROM Distributer d WHERE d.parentid  IS NULL AND d.isdeleted=0"),
    @NamedQuery(name = "Distributer.findByIsactive", query = "SELECT d FROM Distributer d WHERE d.isactive = :isactive")})
public class Distributer implements Serializable {

  

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DISTRIBUTERID")
    private Integer distributerid;
    @Size(max = 50)
    @Column(name = "LOGOPATH")
    private String logopath;
    @Size(max = 60)
    @Column(name = "CONTACT")
    private String contact;
    @Size(max = 60)
    @Column(name = "EMAIL")
    private String email;

    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdate;
    @Column(name = "ISDELETED")
    private Short isdeleted;
    @Column(name = "ISACTIVE")
    private Short isactive;

    //importe mapping
    @OneToMany(mappedBy = "distributerid")
    private Collection<Devicedistributor> devicedistributorCollection;

    @OneToMany(mappedBy = "parentid")
    private Collection<Distributer> distributerCollection;

    @JoinColumn(name = "PARENTID", referencedColumnName = "DISTRIBUTERID")
    @ManyToOne
    private Distributer parentid;

    @JoinColumn(name = "ADDEDDBY", referencedColumnName = "USERID")
    @ManyToOne(optional = false)
    private User addeddby;

    @OneToMany(mappedBy = "distributerid")
    private Collection<User> userCollection;

    public Distributer() {
    }

    public Distributer(Integer distributerid) {
        this.distributerid = distributerid;
    }

    public Distributer(Integer distributerid, Date createdate) {
        this.distributerid = distributerid;
        this.createdate = createdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getDistributerid() {
        return distributerid;
    }

    public void setDistributerid(Integer distributerid) {
        this.distributerid = distributerid;
    }

    public String getLogopath() {
        return logopath;
    }

    public void setLogopath(String logopath) {
        this.logopath = logopath;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Short getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Short isdeleted) {
        this.isdeleted = isdeleted;
    }

    public Short getIsactive() {
        return isactive;
    }

    public void setIsactive(Short isactive) {
        this.isactive = isactive;
    }

    @XmlTransient
    public Collection<Devicedistributor> getDevicedistributorCollection() {
        return devicedistributorCollection;
    }

    public void setDevicedistributorCollection(Collection<Devicedistributor> devicedistributorCollection) {
        this.devicedistributorCollection = devicedistributorCollection;
    }

    @XmlTransient
    public Collection<Distributer> getDistributerCollection() {
        return distributerCollection;
    }

    public void setDistributerCollection(Collection<Distributer> distributerCollection) {
        this.distributerCollection = distributerCollection;
    }

    public Distributer getParentid() {
        return parentid;
    }

    public void setParentid(Distributer parentid) {
        this.parentid = parentid;
    }

    public User getAddeddby() {
        return addeddby;
    }

    public void setAddeddby(User addeddby) {
        this.addeddby = addeddby;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (distributerid != null ? distributerid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Distributer)) {
            return false;
        }
        Distributer other = (Distributer) object;
        if ((this.distributerid == null && other.distributerid != null) || (this.distributerid != null && !this.distributerid.equals(other.distributerid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.veganet.gps.entities.Distributer[ distributerid=" + distributerid + " ]";
    }


}
