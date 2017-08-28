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
import javax.persistence.Lob;
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
 * @author Ines
 */
@Entity
@Table(name = "mission")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mission.findAll", query = "SELECT m FROM Mission m")
    , @NamedQuery(name = "Mission.findByMissionId", query = "SELECT m FROM Mission m WHERE m.missionId = :missionId")
    , @NamedQuery(name = "Mission.findByVehiculeId", query = "SELECT m FROM Mission m WHERE m.vehiculeId = :vehiculeId")
    , @NamedQuery(name = "Mission.findByPersId", query = "SELECT m FROM Mission m WHERE m.persId = :persId")
    , @NamedQuery(name = "Mission.findByNumberOfNights", query = "SELECT m FROM Mission m WHERE m.numberOfNights = :numberOfNights")
    , @NamedQuery(name = "Mission.findByNumberOfTickets", query = "SELECT m FROM Mission m WHERE m.numberOfTickets = :numberOfTickets")
    , @NamedQuery(name = "Mission.findByTotalAmount", query = "SELECT m FROM Mission m WHERE m.totalAmount = :totalAmount")
    , @NamedQuery(name = "Mission.findByQuantityGs", query = "SELECT m FROM Mission m WHERE m.quantityGs = :quantityGs")
    , @NamedQuery(name = "Mission.findByKmCourseGs", query = "SELECT m FROM Mission m WHERE m.kmCourseGs = :kmCourseGs")
    , @NamedQuery(name = "Mission.findByAmountGs", query = "SELECT m FROM Mission m WHERE m.amountGs = :amountGs")
    , @NamedQuery(name = "Mission.findByCreationDate", query = "SELECT m FROM Mission m WHERE m.creationDate = :creationDate")
    , @NamedQuery(name = "Mission.findByStartDate", query = "SELECT m FROM Mission m WHERE m.startDate = :startDate")
    , @NamedQuery(name = "Mission.findByEndDate", query = "SELECT m FROM Mission m WHERE m.endDate = :endDate")
    , @NamedQuery(name = "Mission.findByValidationDate", query = "SELECT m FROM Mission m WHERE m.validationDate = :validationDate")
    , @NamedQuery(name = "Mission.findByAmountMeal", query = "SELECT m FROM Mission m WHERE m.amountMeal = :amountMeal")
    , @NamedQuery(name = "Mission.findByAmountHybergment", query = "SELECT m FROM Mission m WHERE m.amountHybergment = :amountHybergment")
    , @NamedQuery(name = "Mission.findByAmountPg", query = "SELECT m FROM Mission m WHERE m.amountPg = :amountPg")
    , @NamedQuery(name = "Mission.findByAmountParking", query = "SELECT m FROM Mission m WHERE m.amountParking = :amountParking")
    , @NamedQuery(name = "Mission.findByGlobalM", query = "SELECT m FROM Mission m WHERE m.globalM = :globalM")
    , @NamedQuery(name = "Mission.findByReimbursement", query = "SELECT m FROM Mission m WHERE m.reimbursement = :reimbursement")
    , @NamedQuery(name = "Mission.findByArrangment", query = "SELECT m FROM Mission m WHERE m.arrangment = :arrangment")
    , @NamedQuery(name = "Mission.findByStatus", query = "SELECT m FROM Mission m WHERE m.status = :status")})
public class Mission implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MISSION_ID")
    private Integer missionId;
    @Column(name = "VEHICULE_ID")
    private Integer vehiculeId;
    @Column(name = "PERS_ID")
    private Integer persId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "PATH")
    private String path;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "OBJECT")
    private String object;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMBER_OF_NIGHTS")
    private short numberOfNights;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMBER_OF_TICKETS")
    private short numberOfTickets;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_AMOUNT")
    private int totalAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY_GS")
    private float quantityGs;
    @Basic(optional = false)
    @NotNull
    @Column(name = "KM_COURSE_GS")
    private int kmCourseGs;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AMOUNT_GS")
    private int amountGs;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALIDATION_DATE")
    @Temporal(TemporalType.DATE)
    private Date validationDate;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "ACCOMPAYING")
    private String accompaying;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AMOUNT_MEAL")
    private int amountMeal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AMOUNT_HYBERGMENT")
    private int amountHybergment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AMOUNT_PG")
    private int amountPg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AMOUNT_PARKING")
    private int amountParking;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GLOBAL_M")
    private int globalM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "REIMBURSEMENT")
    private short reimbursement;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ARRANGMENT")
    private short arrangment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private short status;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "NOTE")
    private String note;
    @JoinColumn(name = "VEHICLEID", referencedColumnName = "VEHICLEID")
    @ManyToOne
    private Vehicle vehicleid;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    @ManyToOne
    private User userid;

    public Mission() {
    }

    public Mission(Integer missionId) {
        this.missionId = missionId;
    }

    public Mission(Integer missionId, String path, String object, short numberOfNights, short numberOfTickets, int totalAmount, float quantityGs, int kmCourseGs, int amountGs, Date creationDate, Date startDate, Date endDate, Date validationDate, String accompaying, int amountMeal, int amountHybergment, int amountPg, int amountParking, int globalM, short reimbursement, short arrangment, short status, String note) {
        this.missionId = missionId;
        this.path = path;
        this.object = object;
        this.numberOfNights = numberOfNights;
        this.numberOfTickets = numberOfTickets;
        this.totalAmount = totalAmount;
        this.quantityGs = quantityGs;
        this.kmCourseGs = kmCourseGs;
        this.amountGs = amountGs;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.validationDate = validationDate;
        this.accompaying = accompaying;
        this.amountMeal = amountMeal;
        this.amountHybergment = amountHybergment;
        this.amountPg = amountPg;
        this.amountParking = amountParking;
        this.globalM = globalM;
        this.reimbursement = reimbursement;
        this.arrangment = arrangment;
        this.status = status;
        this.note = note;
    }

    public Integer getMissionId() {
        return missionId;
    }

    public void setMissionId(Integer missionId) {
        this.missionId = missionId;
    }

    public Integer getVehiculeId() {
        return vehiculeId;
    }

    public void setVehiculeId(Integer vehiculeId) {
        this.vehiculeId = vehiculeId;
    }

    public Integer getPersId() {
        return persId;
    }

    public void setPersId(Integer persId) {
        this.persId = persId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public short getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(short numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public short getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(short numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getQuantityGs() {
        return quantityGs;
    }

    public void setQuantityGs(float quantityGs) {
        this.quantityGs = quantityGs;
    }

    public int getKmCourseGs() {
        return kmCourseGs;
    }

    public void setKmCourseGs(int kmCourseGs) {
        this.kmCourseGs = kmCourseGs;
    }

    public int getAmountGs() {
        return amountGs;
    }

    public void setAmountGs(int amountGs) {
        this.amountGs = amountGs;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(Date validationDate) {
        this.validationDate = validationDate;
    }

    public String getAccompaying() {
        return accompaying;
    }

    public void setAccompaying(String accompaying) {
        this.accompaying = accompaying;
    }

    public int getAmountMeal() {
        return amountMeal;
    }

    public void setAmountMeal(int amountMeal) {
        this.amountMeal = amountMeal;
    }

    public int getAmountHybergment() {
        return amountHybergment;
    }

    public void setAmountHybergment(int amountHybergment) {
        this.amountHybergment = amountHybergment;
    }

    public int getAmountPg() {
        return amountPg;
    }

    public void setAmountPg(int amountPg) {
        this.amountPg = amountPg;
    }

    public int getAmountParking() {
        return amountParking;
    }

    public void setAmountParking(int amountParking) {
        this.amountParking = amountParking;
    }

    public int getGlobalM() {
        return globalM;
    }

    public void setGlobalM(int globalM) {
        this.globalM = globalM;
    }

    public short getReimbursement() {
        return reimbursement;
    }

    public void setReimbursement(short reimbursement) {
        this.reimbursement = reimbursement;
    }

    public short getArrangment() {
        return arrangment;
    }

    public void setArrangment(short arrangment) {
        this.arrangment = arrangment;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Vehicle getVehicleid() {
        return vehicleid;
    }

    public void setVehicleid(Vehicle vehicleid) {
        this.vehicleid = vehicleid;
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
        hash += (missionId != null ? missionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mission)) {
            return false;
        }
        Mission other = (Mission) object;
        if ((this.missionId == null && other.missionId != null) || (this.missionId != null && !this.missionId.equals(other.missionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.veganet.gps.entities.Mission[ missionId=" + missionId + " ]";
    }
    
}
