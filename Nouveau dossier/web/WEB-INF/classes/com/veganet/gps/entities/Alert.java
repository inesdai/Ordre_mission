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
@Table(name = "alert")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alert.findAll", query = "SELECT a FROM Alert a"),
    @NamedQuery(name = "Alert.findByAlertid", query = "SELECT a FROM Alert a WHERE a.alertid = :alertid"),
    @NamedQuery(name = "Alert.findByTypealert", query = "SELECT a FROM Alert a WHERE a.typealert = :typealert"),
    @NamedQuery(name = "Alert.findByAction", query = "SELECT a FROM Alert a WHERE a.action = :action"),
    @NamedQuery(name = "Alert.findByIsgenerated", query = "SELECT a FROM Alert a WHERE a.isgenerated = :isgenerated"),
    @NamedQuery(name = "Alert.findByReceiver", query = "SELECT a FROM Alert a WHERE a.receiver = :receiver"),
    @NamedQuery(name = "Alert.findByActiveted", query = "SELECT a FROM Alert a WHERE a.activeted = :activeted"),
    @NamedQuery(name = "Alert.findByStartdate", query = "SELECT a FROM Alert a WHERE a.startdate = :startdate"),
    @NamedQuery(name = "Alert.findByBetweenTwoDate", query = "SELECT a FROM Alert a WHERE a.startdate < CURRENT_DATE AND a.enddate >CURRENT_DATE AND a.isdeleted= :isdeleted"),
    @NamedQuery(name = "Alert.findByBeforeThisDate", query = "SELECT a FROM Alert a WHERE  a.isdeleted= :isdeleted "),
    @NamedQuery(name = "Alert.findByDuration", query = "SELECT a FROM Alert a WHERE a.duration = :duration"),
    @NamedQuery(name = "Alert.findByRecurencetype", query = "SELECT a FROM Alert a WHERE a.recurencetype = :recurencetype"),
    @NamedQuery(name = "Alert.findByEveryweek", query = "SELECT a FROM Alert a WHERE a.everyweek = :everyweek"),
    @NamedQuery(name = "Alert.findByEverydayonweek", query = "SELECT a FROM Alert a WHERE a.everydayonweek = :everydayonweek"),
    @NamedQuery(name = "Alert.findByEndoccurence", query = "SELECT a FROM Alert a WHERE a.endoccurence = :endoccurence"),
    @NamedQuery(name = "Alert.findByEnddate", query = "SELECT a FROM Alert a WHERE a.enddate = :enddate"),
    @NamedQuery(name = "Alert.findByCreatedate", query = "SELECT a FROM Alert a WHERE a.createdate = :createdate"),
    @NamedQuery(name = "Alert.findByStartkm", query = "SELECT a FROM Alert a WHERE a.startkm = :startkm"),
    @NamedQuery(name = "Alert.findByIsdeleted", query = "SELECT a FROM Alert a WHERE a.isdeleted = :isdeleted"),
    @NamedQuery(name = "Alert.findByEndkm", query = "SELECT a FROM Alert a WHERE a.endkm = :endkm")})
public class Alert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ALERTID")
    private Long alertid;

    @Column(name = "TYPEALERT")
    private Integer typealert;

    @Size(max = 200)
    @Column(name = "OTHERTYPE")
    private String othertype;

    @Size(max = 200)
    @Column(name = "ACTION")
    private String action;

    @Column(name = "ISGENERATED")
    private Short isgenerated;

    @Size(max = 60)
    @Column(name = "RECEIVER")
    private String receiver;

    @Column(name = "ACTIVETED")
    private Short activeted;

    @Column(name = "STARTDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startdate;

    @Column(name = "ENDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enddate;

    @Column(name = "DURATION")
    @Temporal(TemporalType.TIME)
    private Date duration;

    @Column(name = "RECURENCETYPE")
    private Short recurencetype;

    @Column(name = "EVERYWEEK")
    private Integer everyweek;

    @Column(name = "EVERYDAYONWEEK")
    private Short everydayonweek;

    @Column(name = "ENDOCCURENCE")
    private Integer endoccurence;

    @Column(name = "ISDELETED")
    private Short isdeleted;

    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdate;
    // @Max(value=?)  @Min(value=?)
//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "STARTKM")
    private Double startkm;
    @Column(name = "ENDKM")
    private Double endkm;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    @ManyToOne
    private User userid;

    public Alert() {
    }

    public Alert(Integer typealert, String action, Short isgenerated, String receiver, Short activeted, short isdeleted, Date startdate, Date starttime, Date endtime, Date duration, Short recurencetype, Integer everyweek, Short everydayonweek, Integer endoccurence, Date enddate, Date createdate, Double startkm, Double endkm, User userid) {
        this.typealert = typealert;
        this.action = action;
        this.isgenerated = isgenerated;
        this.receiver = receiver;
        this.activeted = activeted;
        this.startdate = startdate;
        this.duration = duration;
        this.recurencetype = recurencetype;
        this.everyweek = everyweek;
        this.everydayonweek = everydayonweek;
        this.endoccurence = endoccurence;
        this.createdate = createdate;
        this.startkm = startkm;
        this.endkm = endkm;
        this.userid = userid;
        this.isdeleted = isdeleted;
    }

    public Date getEnddate() {
        return enddate;
    }

    public String getOthertype() {
        return othertype;
    }

    public void setOthertype(String othertype) {
        this.othertype = othertype;
    }
    

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Short getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Short isdeleted) {
        this.isdeleted = isdeleted;
    }

    public Alert(Long alertid) {
        this.alertid = alertid;
    }

    public Alert(Long alertid, Date createdate) {
        this.alertid = alertid;
        this.createdate = createdate;
    }

    public Long getAlertid() {
        return alertid;
    }

    public void setAlertid(Long alertid) {
        this.alertid = alertid;
    }

    public Integer getTypealert() {
        return typealert;
    }

    public void setTypealert(Integer typealert) {
        this.typealert = typealert;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Short getIsgenerated() {
        return isgenerated;
    }

    public void setIsgenerated(Short isgenerated) {
        this.isgenerated = isgenerated;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Short getActiveted() {
        return activeted;
    }

    public void setActiveted(Short activeted) {
        this.activeted = activeted;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public Short getRecurencetype() {
        return recurencetype;
    }

    public void setRecurencetype(Short recurencetype) {
        this.recurencetype = recurencetype;
    }

    public Integer getEveryweek() {
        return everyweek;
    }

    public void setEveryweek(Integer everyweek) {
        this.everyweek = everyweek;
    }

    public Short getEverydayonweek() {
        return everydayonweek;
    }

    public void setEverydayonweek(Short everydayonweek) {
        this.everydayonweek = everydayonweek;
    }

    public Integer getEndoccurence() {
        return endoccurence;
    }

    public void setEndoccurence(Integer endoccurence) {
        this.endoccurence = endoccurence;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Double getStartkm() {
        return startkm;
    }

    public void setStartkm(Double startkm) {
        this.startkm = startkm;
    }

    public Double getEndkm() {
        return endkm;
    }

    public void setEndkm(Double endkm) {
        this.endkm = endkm;
    }

    public User getUserid() {
        return userid;
    }

    public void setUserid(User userid) {
        this.userid = userid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alertid != null ? alertid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alert)) {
            return false;
        }
        Alert other = (Alert) object;
        if ((this.alertid == null && other.alertid != null) || (this.alertid != null && !this.alertid.equals(other.alertid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.veganet.gps.entities.Alert[ alertid=" + alertid + " ]";
    }

}
