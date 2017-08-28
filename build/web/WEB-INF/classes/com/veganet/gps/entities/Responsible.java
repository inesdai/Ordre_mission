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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ines
 */
@Entity
@Table(name = "responsible")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Responsible.findAll", query = "SELECT r FROM Responsible r")
    , @NamedQuery(name = "Responsible.findByRes1Id", query = "SELECT r FROM Responsible r WHERE r.res1Id = :res1Id")
    , @NamedQuery(name = "Responsible.findByNameResp", query = "SELECT r FROM Responsible r WHERE r.nameResp = :nameResp")
    , @NamedQuery(name = "Responsible.findByOrdre", query = "SELECT r FROM Responsible r WHERE r.ordre = :ordre")})
public class Responsible implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "RES1_ID")
    private Integer res1Id;
    @Size(max = 100)
    @Column(name = "NAME_RESP")
    private String nameResp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDRE")
    private int ordre;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    @ManyToOne
    private User userid;
    @JoinColumn(name = "RES_USERID", referencedColumnName = "USERID")
    @ManyToOne
    private User resUserid;

    public Responsible() {
    }

    public Responsible(Integer res1Id) {
        this.res1Id = res1Id;
    }

    public Responsible(Integer res1Id, int ordre) {
        this.res1Id = res1Id;
        this.ordre = ordre;
    }

    public Integer getRes1Id() {
        return res1Id;
    }

    public void setRes1Id(Integer res1Id) {
        this.res1Id = res1Id;
    }

    public String getNameResp() {
        return nameResp;
    }

    public void setNameResp(String nameResp) {
        this.nameResp = nameResp;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public User getUserid() {
        return userid;
    }

    public void setUserid(User userid) {
        this.userid = userid;
    }

    public User getResUserid() {
        return resUserid;
    }

    public void setResUserid(User resUserid) {
        this.resUserid = resUserid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (res1Id != null ? res1Id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Responsible)) {
            return false;
        }
        Responsible other = (Responsible) object;
        if ((this.res1Id == null && other.res1Id != null) || (this.res1Id != null && !this.res1Id.equals(other.res1Id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.veganet.gps.entities.Responsible[ res1Id=" + res1Id + " ]";
    }
    
}
