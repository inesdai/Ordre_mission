/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Amine
 */
@Entity
@Table(name = "application_settings")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Applicationsetting.findAll", query = "SELECT a FROM Applicationsetting a"),
    @NamedQuery(name = "Applicationsetting.findByApplicationsettingid", query = "SELECT a FROM Applicationsetting a WHERE a.applicationsettingid = :applicationsettingid"),
    @NamedQuery(name = "Applicationsetting.findBySettingname", query = "SELECT a FROM Applicationsetting a WHERE a.settingName = :settingName"),
    @NamedQuery(name = "Applicationsetting.findBySettingvalue", query = "SELECT a FROM Applicationsetting a WHERE a.settingValue = :settingValue")})
public class Applicationsetting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer applicationsettingid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SETTINGNAME")
    private String settingName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SETTINGVALUE")
    private String settingValue;

    public Integer getApplicationsettingid() {
        return applicationsettingid;
    }

    public void setApplicationsettingid(Integer applicationsettingid) {
        this.applicationsettingid = applicationsettingid;
    }

     

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (applicationsettingid != null ? applicationsettingid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Applicationsetting)) {
            return false;
        }
        Applicationsetting other = (Applicationsetting) object;
        if ((this.applicationsettingid == null && other.applicationsettingid != null) || (this.applicationsettingid != null && !this.applicationsettingid.equals(other.applicationsettingid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.veganet.gps.entities.Applicationsetting[ applicationsettingid=" + applicationsettingid + " ]";
    }

}
