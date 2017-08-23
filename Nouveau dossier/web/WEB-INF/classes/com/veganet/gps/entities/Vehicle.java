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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Amine
 */
@Entity
@Table(name = "vehicle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vehicle.findAll", query = "SELECT v FROM Vehicle v"),
    @NamedQuery(name = "Vehicle.findByVehicleid", query = "SELECT v FROM Vehicle v WHERE v.vehicleid = :vehicleid"),
    @NamedQuery(name = "Vehicle.findByDescription", query = "SELECT v FROM Vehicle v WHERE v.description = :description"),
    @NamedQuery(name = "Vehicle.findByMark", query = "SELECT v FROM Vehicle v WHERE v.mark = :mark"),
    @NamedQuery(name = "Vehicle.findByModel", query = "SELECT v FROM Vehicle v WHERE v.model = :model"),
    @NamedQuery(name = "Vehicle.findByIcon", query = "SELECT v FROM Vehicle v WHERE v.icon = :icon"),
    @NamedQuery(name = "Vehicle.findByLastoilchange", query = "SELECT v FROM Vehicle v WHERE v.lastoilchange = :lastoilchange"),
    @NamedQuery(name = "Vehicle.findByPeriodoilchange", query = "SELECT v FROM Vehicle v WHERE v.periodoilchange = :periodoilchange"),
    @NamedQuery(name = "Vehicle.findByProblemsvehicle", query = "SELECT v FROM Vehicle v WHERE v.problemsvehicle = :problemsvehicle"),
    @NamedQuery(name = "Vehicle.findByFuelconsumption", query = "SELECT v FROM Vehicle v WHERE v.fuelconsumption = :fuelconsumption"),
    @NamedQuery(name = "Vehicle.findByTypefuel", query = "SELECT v FROM Vehicle v WHERE v.typefuel = :typefuel"),
    @NamedQuery(name = "Vehicle.findByInsurancephone", query = "SELECT v FROM Vehicle v WHERE v.insurancephone = :insurancephone"),
    @NamedQuery(name = "Vehicle.findByCreatedate", query = "SELECT v FROM Vehicle v WHERE v.createdate = :createdate"),
    @NamedQuery(name = "Vehicle.findByIsactive", query = "SELECT v FROM Vehicle v WHERE v.isactive = :isactive"),
    @NamedQuery(name = "Vehicle.findByDateassurance", query = "SELECT v FROM Vehicle v WHERE v.dateassurance = :dateassurance"),
    @NamedQuery(name = "Vehicle.findByPeriodassurance", query = "SELECT v FROM Vehicle v WHERE v.periodassurance = :periodassurance"),
    @NamedQuery(name = "Vehicle.findByLastplaybackdelete", query = "SELECT v FROM Vehicle v WHERE v.lastplaybackdelete = :lastplaybackdelete"),
    @NamedQuery(name = "Vehicle.findByDurationplayback", query = "SELECT v FROM Vehicle v WHERE v.durationplayback = :durationplayback"),
    @NamedQuery(name = "Vehicle.findByStartkm", query = "SELECT v FROM Vehicle v WHERE v.startkm = :startkm")})
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "VEHICLEID")
    private Integer vehicleid;
    @Size(max = 50)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 20)
    @Column(name = "MARK")
    private String mark;
    @Size(max = 20)
    @Column(name = "MODEL")
    private String model;
    @Column(name = "ICON")
    private Short icon;
    @Column(name = "LASTOILCHANGE")
    private Float lastoilchange;
    @Column(name = "REMEMBERBEFOREOIL")
    private Integer rememberbeforeoil;
    @Column(name = "PERIODOILCHANGE")
    private Integer periodoilchange;
    @Size(max = 2000)
    @Column(name = "PROBLEMSVEHICLE")
    private String problemsvehicle;
    @Column(name = "FUELCONSUMPTION")
    private Float fuelconsumption;
    @Size(max = 15)
    @Column(name = "TYPEFUEL")
    private String typefuel;
    @Column(name = "INSURANCEPHONE")
    @Size(max = 20)
    private String insurancephone;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdate;
    @Column(name = "ISACTIVE")
    private Short isactive;
    @Column(name = "DATEASSURANCE")
    @Temporal(TemporalType.DATE)
    private Date dateassurance;
    @Column(name = "PERIODASSURANCE")
    private Integer periodassurance;
    @Column(name = "LASTPLAYBACKDELETE")
    @Temporal(TemporalType.DATE)
    private Date lastplaybackdelete;
    @Column(name = "TECHNICALINSPECTIONDATE")
    @Temporal(TemporalType.DATE)
    private Date technicalInspectionDate;
    @Column(name = "TECHNICALINSPECTIONPERIOD")
    private Short technicalInspectiondPeriod;
    @Column(name = "REMEMBERBEFORETECHNICAL")
    private Integer rememberbeforetechnical;
    @Column(name = "DURATIONPLAYBACK")
    private Integer durationplayback;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "STARTKM")
    private Float startkm;
    @Column(name = "MAXSPEED")
    private int maxSpeed;

    @ManyToMany(mappedBy = "vehicleCollection")
    private Collection<Driver> driverCollection;

    @OneToOne(mappedBy = "vehicleid")
    private Device device;
    @Transient
    private Date timeStatistics;
    @Transient
    private double mileage;
    @Transient
    private int stopPoints;
    @Transient
    private int overSpeed;
    @Transient
    private int workingTime;
    @Transient
    private float fuelConsumptionStatistics;
    @Transient
    private int lowBattery;
    @Transient
    private int cut_Off;
    @Transient
    private int shocks;
    @Transient
    private int sos;
    //statistics
    @Transient
    private Date startTime;
    @Transient
    private Date endTime;
    @Transient
    private String langLat;
    @Transient
    private double speedLimit;
    @Transient
    private String continueTime;
    @Transient
    private short alarmType;
    @Transient
    private String alarmTypeText;
    @Transient
    private short actionPetrol;

    public short getActionPetrol() {
        return actionPetrol;
    }

    public void setActionPetrol(short actionPetrol) {
        this.actionPetrol = actionPetrol;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Date getTechnicalInspectionDate() {
        return technicalInspectionDate;
    }

    public void setTechnicalInspectionDate(Date technicalInspectionDate) {
        this.technicalInspectionDate = technicalInspectionDate;
    }

    public Short getTechnicalInspectiondPeriod() {
        return technicalInspectiondPeriod;
    }

    public void setTechnicalInspectiondPeriod(Short technicalInspectiondPeriod) {
        this.technicalInspectiondPeriod = technicalInspectiondPeriod;
    }

    public String getAlarmTypeText() {
        return alarmTypeText;
    }

    public void setAlarmTypeText(String alarmTypeText) {
        this.alarmTypeText = alarmTypeText;
    }

    public short getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(short alarmType) {
        this.alarmType = alarmType;
    }

    public int getshocks() {
        return shocks;
    }

    public void setshocks(int shocks) {
        this.shocks = shocks;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLangLat() {
        return langLat;
    }

    public void setLangLat(String langLat) {
        this.langLat = langLat;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public String getContinueTime() {
        return continueTime;
    }

    public void setContinueTime(String continueTime) {
        this.continueTime = continueTime;
    }

    public int getLowBattery() {
        return lowBattery;
    }

    public void setLowBattery(int lowBattery) {
        this.lowBattery = lowBattery;
    }

    public int getCut_Off() {
        return cut_Off;
    }

    public void setCut_Off(int cut_Off) {
        this.cut_Off = cut_Off;
    }

    public int getSos() {
        return sos;
    }

    public void setSos(int sos) {
        this.sos = sos;
    }

    public Date getTimeStatistics() {
        return timeStatistics;
    }

    public void setTimeStatistics(Date timeStatistics) {
        this.timeStatistics = timeStatistics;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public int getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(int stopPoints) {
        this.stopPoints = stopPoints;
    }

    public int getOverSpeed() {
        return overSpeed;
    }

    public void setOverSpeed(int overSpeed) {
        this.overSpeed = overSpeed;
    }

    public int getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(int workingTime) {
        this.workingTime = workingTime;
    }

    public Integer getRememberbeforeoil() {
        return rememberbeforeoil;
    }

    public void setRememberbeforeoil(Integer rememberbeforeoil) {
        this.rememberbeforeoil = rememberbeforeoil;
    }

    public float getFuelConsumptionStatistics() {
        return fuelConsumptionStatistics;
    }

    public void setFuelConsumptionStatistics(float fuelConsumptionStatistics) {
        this.fuelConsumptionStatistics = fuelConsumptionStatistics;
    }

    public Vehicle() {
    }

    public Vehicle(Integer vehicleid) {
        this.vehicleid = vehicleid;
    }

    public Vehicle(Integer vehicleid, Date createdate) {
        this.vehicleid = vehicleid;
        this.createdate = createdate;
    }

    public Integer getVehicleid() {
        return vehicleid;
    }

    public void setVehicleid(Integer vehicleid) {
        this.vehicleid = vehicleid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Short getIcon() {
        return icon;
    }

    public void setIcon(Short icon) {
        this.icon = icon;
    }

    public Float getLastoilchange() {
        return lastoilchange;
    }

    public void setLastoilchange(Float lastoilchange) {
        this.lastoilchange = lastoilchange;
    }

    public Integer getPeriodoilchange() {
        return periodoilchange;
    }

    public void setPeriodoilchange(Integer periodoilchange) {
        this.periodoilchange = periodoilchange;
    }

    public String getProblemsvehicle() {
        return problemsvehicle;
    }

    public void setProblemsvehicle(String problemsvehicle) {
        this.problemsvehicle = problemsvehicle;
    }

    public Float getFuelconsumption() {
        return fuelconsumption;
    }

    public void setFuelconsumption(Float fuelconsumption) {
        this.fuelconsumption = fuelconsumption;
    }

    public String getTypefuel() {
        return typefuel;
    }

    public void setTypefuel(String typefuel) {
        this.typefuel = typefuel;
    }

    public String getInsurancephone() {
        return insurancephone;
    }

    public void setInsurancephone(String insurancephone) {
        this.insurancephone = insurancephone;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Short getIsactive() {
        return isactive;
    }

    public void setIsactive(Short isactive) {
        this.isactive = isactive;
    }

    public Date getDateassurance() {
        return dateassurance;
    }

    public void setDateassurance(Date dateassurance) {
        this.dateassurance = dateassurance;
    }

    public Integer getPeriodassurance() {
        return periodassurance;
    }

    public void setPeriodassurance(Integer periodassurance) {
        this.periodassurance = periodassurance;
    }

    public Date getLastplaybackdelete() {
        return lastplaybackdelete;
    }

    public void setLastplaybackdelete(Date lastplaybackdelete) {
        this.lastplaybackdelete = lastplaybackdelete;
    }

    public Integer getDurationplayback() {
        return durationplayback;
    }

    public void setDurationplayback(Integer durationplayback) {
        this.durationplayback = durationplayback;
    }

    public Float getStartkm() {
        return startkm;
    }

    public void setStartkm(Float startkm) {
        this.startkm = startkm;
    }

    public Integer getRememberbeforetechnical() {
        return rememberbeforetechnical;
    }

    public void setRememberbeforetechnical(Integer rememberbeforetechnical) {
        this.rememberbeforetechnical = rememberbeforetechnical;
    }

    @XmlTransient
    public Collection<Driver> getDriverCollection() {
        return driverCollection;
    }

    public void setDriverCollection(Collection<Driver> driverCollection) {
        this.driverCollection = driverCollection;
    }

    @XmlTransient
    public Device getDevice() {
        return device;
    }

    public void setDeviceCollection(Device device) {
        this.device = device;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vehicleid != null ? vehicleid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vehicle)) {
            return false;
        }
        Vehicle other = (Vehicle) object;
        if ((this.vehicleid == null && other.vehicleid != null) || (this.vehicleid != null && !this.vehicleid.equals(other.vehicleid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        // return "com.veganet.gps.entities.Vehicle[ vehicleid=" + vehicleid + " ]";
        return "-" + description + "-" + vehicleid;
    }

}
