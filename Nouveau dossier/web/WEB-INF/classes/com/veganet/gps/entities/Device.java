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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "devices")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Device.findAll", query = "SELECT d FROM Device d"),
    @NamedQuery(name = "Device.findByDeviceid", query = "SELECT d FROM Device d WHERE d.deviceid = :deviceid"),
    @NamedQuery(name = "Device.findByName", query = "SELECT d FROM Device d WHERE d.name = :name"),
    @NamedQuery(name = "Device.findByImei", query = "SELECT d FROM Device d WHERE d.imei = :imei"),
    @NamedQuery(name = "Device.updatePetrolAction", query = "UPDATE Device d SET d.power =:action WHERE d.vehicleid.vehicleid = :vehicleid"),
    @NamedQuery(name = "Device.findByCreatedate", query = "SELECT d FROM Device d WHERE d.createdate = :createdate"),
    @NamedQuery(name = "Device.findByIsdeleted", query = "SELECT d FROM Device d WHERE d.isdeleted = :isdeleted"),
    @NamedQuery(name = "Device.findByIsactive", query = "SELECT d FROM Device d WHERE d.isactive = :isactive"),
    @NamedQuery(name = "Device.findBySimcard", query = "SELECT d FROM Device d WHERE d.simcard = :simcard"),
    @NamedQuery(name = "Device.findByUrgentnumber1", query = "SELECT d FROM Device d WHERE d.urgentnumber1 = :urgentnumber1"),
    @NamedQuery(name = "Device.findByUrgentnumber2", query = "SELECT d FROM Device d WHERE d.urgentnumber2 = :urgentnumber2"),
    @NamedQuery(name = "Device.findByUrgentnumber3", query = "SELECT d FROM Device d WHERE d.urgentnumber3 = :urgentnumber3"),
    @NamedQuery(name = "Device.findByActivatedate", query = "SELECT d FROM Device d WHERE d.activatedate = :activatedate"),
    @NamedQuery(name = "Device.findByExpirationdate", query = "SELECT d FROM Device d WHERE d.expirationdate = :expirationdate"),
    @NamedQuery(name = "Device.findByUseractivationdate", query = "SELECT d FROM Device d WHERE d.useractivationdate = :useractivationdate"),
    @NamedQuery(name = "Device.findByUserexpirationdate", query = "SELECT d FROM Device d WHERE d.userexpirationdate = :userexpirationdate")})
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Basic(optional = false)
    @Column(name = "id")
    private Integer deviceid;

    @Size(max = 20)
    @Column(name = "name")
    private String name;

    @Size(max = 20)
    @Column(name = "uniqueId")
    private String imei;

    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdate;

    @Column(name = "ISDELETED")
    private Short isdeleted;

    @Column(name = "ISACTIVE")
    private Short isactive;

    @Column(name = "SIMCARD")
    @Size(max = 20)
    private String simcard;

    @Column(name = "GEOSTATUS")
    private Short geostatus;

    @Column(name = "POWER")
    private Short power;

    @Column(name = "URGENTNUMBER1")
    @Size(max = 20)
    private String urgentnumber1;

    @Column(name = "URGENTNUMBER2")
    @Size(max = 20)
    private String urgentnumber2;

    @Column(name = "URGENTNUMBER3")
    @Size(max = 20)
    private String urgentnumber3;

    @Column(name = "ACTIVATEDATE")
    @Temporal(TemporalType.DATE)
    private Date activatedate;

    @Column(name = "EXPIRATIONDATE")
    @Temporal(TemporalType.DATE)
    private Date expirationdate;

    @Column(name = "USERACTIVATIONDATE")
    @Temporal(TemporalType.DATE)
    private Date useractivationdate;

    @Column(name = "USEREXPIRATIONDATE")
    @Temporal(TemporalType.DATE)
    private Date userexpirationdate;

    @JoinColumn(name = "latestPosition_id", referencedColumnName = "id")
    @ManyToOne

    private Position lastpositionid;
    @OneToMany(mappedBy = "deviceid")
    @JsonIgnore
    private Collection<Notification> notificationCollection;
    @OneToMany(mappedBy = "deviceid")
    @JsonIgnore
    private Collection<Geofence> geofenceCollection;
    @OneToMany(mappedBy = "deviceid")
    @JsonIgnore
    private Collection<Position> positionCollection;
//import mapping
    //every distributerdevice has device so every device can mahe many devicedistributer
    @OneToMany(mappedBy = "deviceid")
    @JsonIgnore
    private Collection<Devicedistributor> devicedistributorCollection;

//    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VEHICLEID")
    @JsonIgnore
    private Vehicle vehicleid;

    @JoinColumn(name = "ADDEDBY", referencedColumnName = "USERID")
    @ManyToOne(optional = false)
    @JsonIgnore
    private User addedby;

    @JsonIgnore
    @OneToMany(mappedBy = "deviceid")
    private Collection<Logs> logsCollection;

    public Short getPower() {
        return power;
    }

    public void setPower(Short power) {
        this.power = power;
    }

    public Short getGeostatus() {
        return geostatus;
    }

    @JsonIgnore
    public Collection<Logs> getLogsCollection() {
        return logsCollection;
    }

    @JsonIgnore
    public void setLogsCollection(Collection<Logs> logsCollection) {
        this.logsCollection = logsCollection;
    }

    public void setGeostatus(Short geostatus) {
        this.geostatus = geostatus;
    }

    public Device() {
    }

    public Device(Integer deviceid) {
        this.deviceid = deviceid;
    }

    public Device(Integer deviceid, Date createdate) {
        this.deviceid = deviceid;
        this.createdate = createdate;
    }

    public Integer getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
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

    public String getSimcard() {
        return simcard;
    }

    public void setSimcard(String simcard) {
        this.simcard = simcard;
    }

    public String getUrgentnumber1() {
        return urgentnumber1;
    }

    public void setUrgentnumber1(String urgentnumber1) {
        this.urgentnumber1 = urgentnumber1;
    }

    public String getUrgentnumber2() {
        return urgentnumber2;
    }

    public void setUrgentnumber2(String urgentnumber2) {
        this.urgentnumber2 = urgentnumber2;
    }

    public String getUrgentnumber3() {
        return urgentnumber3;
    }

    public void setUrgentnumber3(String urgentnumber3) {
        this.urgentnumber3 = urgentnumber3;
    }

    public Date getActivatedate() {
        return activatedate;
    }

    public void setActivatedate(Date activatedate) {
        this.activatedate = activatedate;
    }

    public Date getExpirationdate() {
        return expirationdate;
    }

    public void setExpirationdate(Date expirationdate) {
        this.expirationdate = expirationdate;
    }

    public Date getUseractivationdate() {
        return useractivationdate;
    }

    public void setUseractivationdate(Date useractivationdate) {
        this.useractivationdate = useractivationdate;
    }

    public Date getUserexpirationdate() {
        return userexpirationdate;
    }

    public void setUserexpirationdate(Date userexpirationdate) {
        this.userexpirationdate = userexpirationdate;
    }

    @XmlTransient
    public Collection<Geofence> getGeofenceCollection() {
        return geofenceCollection;
    }

    public void setGeofenceCollection(Collection<Geofence> geofenceCollection) {
        this.geofenceCollection = geofenceCollection;
    }

    @XmlTransient
    public Collection<Position> getPositionCollection() {
        return positionCollection;
    }

    public void setPositionCollection(Collection<Position> positionCollection) {
        this.positionCollection = positionCollection;
    }

    @XmlTransient
    public Collection<Devicedistributor> getDevicedistributorCollection() {
        return devicedistributorCollection;
    }

    public void setDevicedistributorCollection(Collection<Devicedistributor> devicedistributorCollection) {
        this.devicedistributorCollection = devicedistributorCollection;
    }

    public Vehicle getVehicleid() {
        return vehicleid;
    }

    public void setVehicleid(Vehicle vehicleid) {
        this.vehicleid = vehicleid;
    }

    public User getAddedby() {
        return addedby;
    }

    public void setAddedby(User addedby) {
        this.addedby = addedby;
    }

    public Position getLastpositionid() {
        return lastpositionid;
    }

    public void setLastpositionid(Position lastpositionid) {
        this.lastpositionid = lastpositionid;
    }

    @XmlTransient
    public Collection<Notification> getNotificationCollection() {
        return notificationCollection;
    }

    public void setNotificationCollection(Collection<Notification> notificationCollection) {
        this.notificationCollection = notificationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deviceid != null ? deviceid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Device)) {
            return false;
        }
        Device other = (Device) object;
        if ((this.deviceid == null && other.deviceid != null) || (this.deviceid != null && !this.deviceid.equals(other.deviceid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.veganet.gps.entities.Device[ deviceid=" + deviceid + " ]";
    }

}
