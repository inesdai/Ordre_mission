/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.faces.context.FacesContext;
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
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Amine
 */
@Entity
@Table(name = "positions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Position.findAll", query = "SELECT p FROM Position p"),
    @NamedQuery(name = "Position.findByPositionid", query = "SELECT p FROM Position p WHERE p.positionid = :positionid"),
    @NamedQuery(name = "Position.findByBeforePosition", query = "SELECT p FROM Position p WHERE p.deviceid = :deviceid AND p.timecreate<:timecreate ORDER BY P.timecreate DESC"),
    @NamedQuery(name = "Position.findByNewIdPosition", query = "SELECT p FROM Position p WHERE p.deviceid = :deviceid AND (p.positionid >:positionid OR p.positionid =:positionid)  ORDER BY P.timecreate ASC"),
    @NamedQuery(name = "Position.findByBeforePetrolPosition", query = "SELECT p FROM Position p WHERE p.deviceid = :deviceid AND p.timecreate<:timecreate AND p.latitude!=0.0 ORDER BY P.timecreate DESC"),
    @NamedQuery(name = "Position.findByAddress", query = "SELECT p FROM Position p WHERE p.address = :address"),
    @NamedQuery(name = "Position.findByAltitude", query = "SELECT p FROM Position p WHERE p.altitude = :altitude"),
    @NamedQuery(name = "Position.findByCourse", query = "SELECT p FROM Position p WHERE p.course = :course"),
    @NamedQuery(name = "Position.findByLatitude", query = "SELECT p FROM Position p WHERE p.latitude = :latitude"),
    @NamedQuery(name = "Position.findByLongitude", query = "SELECT p FROM Position p WHERE p.longitude = :longitude"),
    @NamedQuery(name = "Position.findByOther", query = "SELECT p FROM Position p WHERE p.other = :other"),
    @NamedQuery(name = "Position.findByPowers", query = "SELECT p FROM Position p WHERE p.power = :power"),
    @NamedQuery(name = "Position.findBySpeed", query = "SELECT p FROM Position p WHERE p.speed = :speed"),
    @NamedQuery(name = "Position.findByTimecreate", query = "SELECT p FROM Position p WHERE p.timecreate = :timecreate"),
    @NamedQuery(name = "Position.findByValid", query = "SELECT p FROM Position p WHERE p.valid = :valid"),
    @NamedQuery(name = "Position.findByMileage", query = "SELECT p FROM Position p WHERE p.mileage = :mileage"),
    @NamedQuery(name = "Position.findByAccstatus", query = "SELECT p FROM Position p WHERE p.accstatus = :accstatus"),
    @NamedQuery(name = "Position.findByBatteryincharge", query = "SELECT p FROM Position p WHERE p.batteryincharge = :batteryincharge"),
    @NamedQuery(name = "Position.findByVibrationstatus", query = "SELECT p FROM Position p WHERE p.vibrationstatus = :vibrationstatus"),
    @NamedQuery(name = "Position.findByRelaystatus", query = "SELECT p FROM Position p WHERE p.relaystatus = :relaystatus"),
    @NamedQuery(name = "Position.findBySosbuttonstatus", query = "SELECT p FROM Position p WHERE p.sosbuttonstatus = :sosbuttonstatus"),
    @NamedQuery(name = "Position.findBySignalgpsstatus", query = "SELECT p FROM Position p WHERE p.signalgpsstatus = :signalgpsstatus"),
    @NamedQuery(name = "Position.findByGprsstatus", query = "SELECT p FROM Position p WHERE p.gprsstatus = :gprsstatus"),
    @NamedQuery(name = "Position.findByBatterylevel", query = "SELECT p FROM Position p WHERE p.batterylevel = :batterylevel")})
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long positionid;
    @Size(max = 30)
    @Column(name = "address")
    private String address;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "altitude")
    private Double altitude;
    @Column(name = "course")
    private Double course;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Size(max = 255)
    @Column(name = "other")
    @JsonIgnore
    private String other;
    @Column(name = "POWER")
    private short power;
    @Column(name = "speed")
    private Double speed;
    @Basic(optional = false)
    @NotNull
//    @Column(name = "timecreate")
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timecreate;
    @Column(name = "valid")
    private Short valid;
    @Column(name = "MILEAGE")
    private Double mileage;
    @Column(name = "ACCSTATUS")
    private Short accstatus;
    @Column(name = "BATTERYINCHARGE")
    private Short batteryincharge;
    @Column(name = "VIBRATIONSTATUS")
    private Short vibrationstatus;
    @Column(name = "RELAYSTATUS")
    private Short relaystatus;
    @Column(name = "SOSBUTTONSTATUS")
    private Short sosbuttonstatus;
    @Column(name = "SIGNALGPSSTATUS")
    private Short signalgpsstatus;
    @Column(name = "GPRSSTATUS")
    private Short gprsstatus;
    @Column(name = "BATTERYLEVEL")
    private Integer batterylevel;
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Device deviceid;
    @OneToMany(mappedBy = "lastpositionid")
    @JsonIgnore
    private Collection<Device> deviceCollection;

    @OneToMany(mappedBy = "positionid")
    @JsonIgnore
    private Collection<Notification> notificationCollection;

    public Position() {
    }

    public Position(Long positionid) {
        this.positionid = positionid;
    }

    public Position(Long positionid, Date timecreate) {
        this.positionid = positionid;
        this.timecreate = timecreate;
    }

    public Long getPositionid() {
        return positionid;
    }

    public void setPositionid(Long positionid) {
        this.positionid = positionid;
    }

    public short getPower() {
        return power;
    }

    public void setPower(short power) {
        this.power = power;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Notification> getNotificationCollection() {
        return notificationCollection;
    }

    public void setNotificationCollection(Collection<Notification> notificationCollection) {
        this.notificationCollection = notificationCollection;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getCourse() {
        return course;
    }

    public void setCourse(Double course) {
        this.course = course;
    }

    public Double getLatitude() {
//        System.out.println("gett/////////////" + this.latitude);
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public short getPowers() {
        return power;
    }

    public void setPowers(short powers) {
        this.power = powers;
    }
 
    public Double getSpeed() {
        return Math.floor(speed * 1000) / 1000;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }
 
    public Date getTimecreate() {
//        return this.timecreate;
             return   ConvertCstToDate(timecreate);

        /*       
         Date date = null;
         SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
         String mdy = mdyFormat.format(timecreate);
 
 

         try {
         //            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
         date = mdyFormat.parse(mdy);

         } catch (ParseException e) {
            
         e.printStackTrace();

         }
         System.out.println(date + "");
         return date;
         */
    }

    public Date getTimeCreateWitoutconvertion() {
        return timecreate;

    }

    //convert time from current Time To Gps time
    public Date ConvertCstToDate(Date date) {
        int timezone = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("timeZone"));
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);

        calendar.add(Calendar.HOUR_OF_DAY, timezone - 8); // adds one hour
        return calendar.getTime();
    }

    public void setTimecreate(Date timecreate) {
        this.timecreate = timecreate;
    }

    public Short getValid() {
        return valid;
    }

    public void setValid(Short valid) {
        this.valid = valid;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Short getAccstatus() {
        return accstatus;
    }

    public void setAccstatus(Short accstatus) {
        this.accstatus = accstatus;
    }

    public Short getBatteryincharge() {
        return batteryincharge;
    }

    public void setBatteryincharge(Short batteryincharge) {
        this.batteryincharge = batteryincharge;
    }

    public Short getVibrationstatus() {
        return vibrationstatus;
    }

    public void setVibrationstatus(Short vibrationstatus) {
        this.vibrationstatus = vibrationstatus;
    }

    public Short getRelaystatus() {
        return relaystatus;
    }

    public void setRelaystatus(Short relaystatus) {
        this.relaystatus = relaystatus;
    }

    public Short getSosbuttonstatus() {
        return sosbuttonstatus;
    }

    public void setSosbuttonstatus(Short sosbuttonstatus) {
        this.sosbuttonstatus = sosbuttonstatus;
    }

    public Short getSignalgpsstatus() {
        return signalgpsstatus;
    }

    public void setSignalgpsstatus(Short signalgpsstatus) {
        this.signalgpsstatus = signalgpsstatus;
    }

    public Short getGprsstatus() {
        return gprsstatus;
    }

    public void setGprsstatus(Short gprsstatus) {
        this.gprsstatus = gprsstatus;
    }

    public Integer getBatterylevel() {
        return batterylevel;
    }

    public void setBatterylevel(Integer batterylevel) {
        this.batterylevel = batterylevel;
    }

    public Device getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Device deviceid) {
        this.deviceid = deviceid;
    }

    @XmlTransient
    public Collection<Device> getDeviceCollection() {
        return deviceCollection;
    }

    public void setDeviceCollection(Collection<Device> deviceCollection) {
        this.deviceCollection = deviceCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (positionid != null ? positionid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Position)) {
            return false;
        }
        Position other = (Position) object;
        if ((this.positionid == null && other.positionid != null) || (this.positionid != null && !this.positionid.equals(other.positionid))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "com.veganet.gps.entities.Position[ positionid=" + positionid + " ]";
//    }
    @Override
    public String toString() {
        return "Position{" + "positionid=" + positionid + ", address=" + address + ", altitude=" + altitude + ", course=" + course + ", latitude=" + latitude + ", longitude=" + longitude + ", other=" + other + ", powers=" + power + ", speed=" + speed + ", timecreate=" + timecreate.toString() + ", valid=" + valid + ", mileage=" + mileage + ", accstatus=" + accstatus + ", batteryincharge=" + batteryincharge + ", vibrationstatus=" + vibrationstatus + ", relaystatus=" + relaystatus + ", sosbuttonstatus=" + sosbuttonstatus + ", signalgpsstatus=" + signalgpsstatus + ", gprsstatus=" + gprsstatus + ", batterylevel=" + batterylevel + '}';
    }

}
