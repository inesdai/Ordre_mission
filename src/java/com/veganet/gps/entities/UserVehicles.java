/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.entities;

import java.util.List;

/**
 *
 * @author user
 */
public class UserVehicles {
    Integer userId;
    List<VehiculePositionWS> vehicule;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UserVehicles(List<VehiculePositionWS> vehiculePositionWSs) {
        this.vehicule = vehiculePositionWSs;
    }

    public UserVehicles() {
    }

    public List<VehiculePositionWS> getVehicule() {
        return vehicule;
    }

    public void setVehicule(List<VehiculePositionWS> vehiculePositionWSs) {
        this.vehicule = vehiculePositionWSs;
    }
    
}
