/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.entities;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author user
 */
 
public class VehiculePositionWS {

 
    private String imei;
    private Integer vehicleid;
    private Date timecreate;
    private short power;
    private int maxSpeed;
    private Short icon;
    private String typefuel;
    private Float fuelconsumption;
    private String model;
    private String mark;
    private String description;
    private Long positionid;
    private Double altitude;
    private Double latitude;
    private Double longitude;
    private Double speed;
    private String other;

    public VehiculePositionWS() {
    }

 

    public VehiculePositionWS(String imei, Integer vehicleid, short power, int maxSpeed, Short icon, String typefuel, Float fuelconsumption, String model, String mark, String description, Date timecreate, Long positionid, Double altitude, Double latitude, Double longitude, Double speed, String other) {
        this.vehicleid = vehicleid;
        this.maxSpeed = maxSpeed;
        this.icon = icon;
        this.typefuel = typefuel;
        this.fuelconsumption = fuelconsumption;
        this.model = model;
        this.mark = mark;
        this.description = description;
        this.positionid = positionid;
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.other = other;
    }

 

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Date getTimecreate() {
        return timecreate;
    }

    public void setTimecreate(Date timecreate) {
        this.timecreate = timecreate;
    }

    public short getPower() {
        return power;
    }

    public void setPower(short power) {
        this.power = power;
    }

    public Long getPositionid() {
        return positionid;
    }

    public void setPositionid(Long positionid) {
        this.positionid = positionid;
    }

 

    public Integer getVehicleid() {
        return vehicleid;
    }

    public void setVehicleid(Integer vehicleid) {
        this.vehicleid = vehicleid;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Short getIcon() {
        return icon;
    }

    public void setIcon(Short icon) {
        this.icon = icon;
    }

    public String getTypefuel() {
        return typefuel;
    }

    public void setTypefuel(String typefuel) {
        this.typefuel = typefuel;
    }

    public Float getFuelconsumption() {
        return fuelconsumption;
    }

    public void setFuelconsumption(Float fuelconsumption) {
        this.fuelconsumption = fuelconsumption;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getLatitude() {
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

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

}
