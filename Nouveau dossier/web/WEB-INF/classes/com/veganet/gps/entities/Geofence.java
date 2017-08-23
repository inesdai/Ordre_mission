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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Amine
 */
@Entity
@Table(name = "geofence")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Geofence.findAll", query = "SELECT g FROM Geofence g"),
    @NamedQuery(name = "Geofence.findByGeofenceid", query = "SELECT g FROM Geofence g WHERE g.geofenceid = :geofenceid"),
    @NamedQuery(name = "Geofence.findByGeofencezone", query = "SELECT g FROM Geofence g WHERE g.geofencezone = :geofencezone"),
    @NamedQuery(name = "Geofence.findByStatus", query = "SELECT g FROM Geofence g WHERE g.status = :status"),
    @NamedQuery(name = "Geofence.findByActivatetime", query = "SELECT g FROM Geofence g WHERE g.activatetime = :activatetime"),
    @NamedQuery(name = "Geofence.findByDesactivatetime", query = "SELECT g FROM Geofence g WHERE g.desactivatetime = :desactivatetime"),
    @NamedQuery(name = "Geofence.findByTypegeofence", query = "SELECT g FROM Geofence g WHERE g.typegeofence = :typegeofence"),
    @NamedQuery(name = "Geofence.findByBetweenTwoDate", query = "SELECT g FROM Geofence g WHERE g.activatetime < :activatetime AND g.desactivatetime >:enddate"),
    @NamedQuery(name = "Geofence.findByAllDate", query = "SELECT g FROM Geofence g WHERE g.desactivatetime IS NULL"),
    @NamedQuery(name = "Geofence.findByCreatedate", query = "SELECT g FROM Geofence g WHERE g.createdate = :createdate")})
public class Geofence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "GEOFENCEID")
    private Integer geofenceid;

    @Column(name = "GEOFENCEZONE", columnDefinition = "TEXT")
    private String geofencezone;
    @Size(max = 30)
    @Column(name = "GEOFENCENAME")
    private String geofencename;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "ACTIVATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activatetime;
    @Column(name = "DESACTIVATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date desactivatetime;
    @Column(name = "TYPEGEOFENCE")
    private Short typegeofence;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdate;
    @JoinColumn(name = "DEVICEID", referencedColumnName = "id")
    @ManyToOne
    private Device deviceid;

    public Geofence() {
    }

    public Geofence(Integer geofenceid) {
        this.geofenceid = geofenceid;
    }

    public Geofence(Integer geofenceid, Date createdate) {
        this.geofenceid = geofenceid;
        this.createdate = createdate;
    }

    public Integer getGeofenceid() {
        return geofenceid;
    }

    public void setGeofenceid(Integer geofenceid) {
        this.geofenceid = geofenceid;
    }

    public String getGeofencename() {
        return geofencename;
    }

    public void setGeofencename(String geofencename) {
        this.geofencename = geofencename;
    }

    public String getGeofencezone() {
        return geofencezone;
    }

    public void setGeofencezone(String geofencezone) {
        this.geofencezone = geofencezone;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getActivatetime() {
        return activatetime;
    }

    public void setActivatetime(Date activatetime) {
        this.activatetime = activatetime;
    }

    public Date getDesactivatetime() {
        return desactivatetime;
    }

    public void setDesactivatetime(Date desactivatetime) {
        this.desactivatetime = desactivatetime;
    }

    public Short getTypegeofence() {
        return typegeofence;
    }

    public void setTypegeofence(Short typegeofence) {
        this.typegeofence = typegeofence;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Device getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Device deviceid) {
        this.deviceid = deviceid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (geofenceid != null ? geofenceid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Geofence)) {
            return false;
        }
        Geofence other = (Geofence) object;
        if ((this.geofenceid == null && other.geofenceid != null) || (this.geofenceid != null && !this.geofenceid.equals(other.geofenceid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.veganet.gps.entities.Geofence[ geofenceid=" + geofenceid + " ]";
    }

}
