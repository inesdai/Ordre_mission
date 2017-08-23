/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.entities;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Amine
 */
@Entity
@Table(name = "devicedistributor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Devicedistributor.insertIsAssigned", query = "UPDATE  Devicedistributor d set d.assigned = :assigned WHERE d.customerid != :customerid AND d.deviceid = :deviceid"),
    @NamedQuery(name = "Devicedistributor.findByAssignedCustomer", query = "SELECT d FROM Devicedistributor d WHERE d.assigned = :assigned AND d.customerid = :idassignedcutomer"),
    @NamedQuery(name = "Devicedistributor.addedYet", query = "SELECT d FROM Devicedistributor d WHERE d.assigned = :assigned AND d.deviceid= :device"),
    @NamedQuery(name = "Devicedistributor.findByUserDevice", query = "SELECT d.customerid FROM Devicedistributor d WHERE d.assigned = :assigned AND d.deviceid= :deviceid"),
    @NamedQuery(name = "Devicedistributor.findDeviceByDestributor", query = "SELECT DISTINCT d.deviceid FROM Devicedistributor d WHERE d.distributerid= :distributerid"),
    @NamedQuery(name = "Devicedistributor.findAll", query = "SELECT d FROM Devicedistributor d"),
    @NamedQuery(name = "Devicedistributor.findByDevicedistributerid", query = "SELECT d FROM Devicedistributor d WHERE d.devicedistributerid = :devicedistributerid"),
    @NamedQuery(name = "Devicedistributor.findByAssigned", query = "SELECT d FROM Devicedistributor d WHERE d.assigned = :assigned"),
    @NamedQuery(name = "Devicedistributor.findByCreatedate", query = "SELECT d FROM Devicedistributor d WHERE d.createdate = :createdate")})
public class Devicedistributor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DEVICEDISTRIBUTERID")
    private Integer devicedistributerid;
    @Column(name = "ASSIGNED")
    private Short assigned;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdate;
    @JoinColumn(name = "ADDEDBY", referencedColumnName = "USERID")
    @ManyToOne
    private User addedby;
    @JoinColumn(name = "DISTRIBUTERID", referencedColumnName = "DISTRIBUTERID")
    @ManyToOne
    private Distributer distributerid;
    @JoinColumn(name = "DEVICEID", referencedColumnName = "id")
    @ManyToOne
    private Device deviceid;
    @JoinColumn(name = "CUSTOMERID", referencedColumnName = "USERID")
    @ManyToOne
    private User customerid;

    public Devicedistributor() {
    }

    public Devicedistributor(Integer devicedistributerid) {
        this.devicedistributerid = devicedistributerid;
    }

    public Devicedistributor(Integer devicedistributerid, Date createdate) {
        this.devicedistributerid = devicedistributerid;
        this.createdate = createdate;
    }

    public Integer getDevicedistributerid() {
        return devicedistributerid;
    }

    public void setDevicedistributerid(Integer devicedistributerid) {
        this.devicedistributerid = devicedistributerid;
    }

    public Short getAssigned() {
        return assigned;
    }

    public void setAssigned(Short assigned) {
        this.assigned = assigned;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public User getAddedby() {
        return addedby;
    }

    public void setAddedby(User addedby) {
        this.addedby = addedby;
    }

    public Distributer getDistributerid() {
        return distributerid;
    }

    public void setDistributerid(Distributer distributerid) {
        this.distributerid = distributerid;
    }

    public Device getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Device deviceid) {
        this.deviceid = deviceid;
    }

    public User getCustomerid() {
        return customerid;
    }

    public void setCustomerid(User customerid) {
        this.customerid = customerid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (devicedistributerid != null ? devicedistributerid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Devicedistributor)) {
            return false;
        }
        Devicedistributor other = (Devicedistributor) object;
        if ((this.devicedistributerid == null && other.devicedistributerid != null) || (this.devicedistributerid != null && !this.devicedistributerid.equals(other.devicedistributerid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.veganet.gps.entities.Devicedistributor[ devicedistributerid=" + devicedistributerid + " ]";
    }

}
