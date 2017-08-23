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
@Table(name = "notification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notification.findAll", query = "SELECT n FROM Notification n"),
    @NamedQuery(name = "Notification.findByNotificationid", query = "SELECT n FROM Notification n WHERE n.notificationid = :notificationid"),
    @NamedQuery(name = "Notification.findByTypenotification", query = "SELECT n FROM Notification n WHERE n.typenotification = :typenotification"),
    @NamedQuery(name = "Notification.findByDescription", query = "SELECT n FROM Notification n WHERE n.description = :description"),
    @NamedQuery(name = "Notification.findByTimenotification", query = "SELECT n FROM Notification n WHERE n.timenotification = :timenotification"),
    @NamedQuery(name = "Notification.findBySatus", query = "SELECT n FROM Notification n WHERE n.satus = :satus")})
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "NOTIFICATIONID")
    private Long notificationid;
    @Column(name = "TYPENOTIFICATION")
    private Integer typenotification;
    @Size(max = 30)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIMENOTIFICATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timenotification;
    @Column(name = "SATUS")
    private Short satus;

    @JoinColumn(name = "POSITIONID", referencedColumnName = "id")
    @ManyToOne
    private Position positionid;
    
    @JoinColumn(name = "DEVICEID", referencedColumnName = "id")
    @ManyToOne
    private Device deviceid;

    
    
    
    public Notification() {
    }

    public Notification(Integer typenotification, String description, Date timenotification, Short satus, Device deviceid,Position positionid) {
        this.typenotification = typenotification;
        this.description = description;
        this.timenotification = timenotification;
        this.satus = satus;
        this.deviceid = deviceid;
        this.positionid=positionid;
    }

    public Notification(Long notificationid) {
        this.notificationid = notificationid;
    }

    public Notification(Long notificationid, Date timenotification) {
        this.notificationid = notificationid;
        this.timenotification = timenotification;
    }

    public Long getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(Long notificationid) {
        this.notificationid = notificationid;
    }

    public Position getPositionid() {
        return positionid;
    }

    public void setPositionid(Position positionid) {
        this.positionid = positionid;
    }
    
    

    public Integer getTypenotification() {
        return typenotification;
    }

    public void setTypenotification(Integer typenotification) {
        this.typenotification = typenotification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimenotification() {
        return timenotification;
    }

    public void setTimenotification(Date timenotification) {
        this.timenotification = timenotification;
    }

    public Short getSatus() {
        return satus;
    }

    public void setSatus(Short satus) {
        this.satus = satus;
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
        hash += (notificationid != null ? notificationid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notification)) {
            return false;
        }
        Notification other = (Notification) object;
        if ((this.notificationid == null && other.notificationid != null) || (this.notificationid != null && !this.notificationid.equals(other.notificationid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.veganet.gps.entities.Notification[ notificationid=" + notificationid + " ]";
    }

}
