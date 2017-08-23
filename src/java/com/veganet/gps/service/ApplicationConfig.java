/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author user
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.veganet.gps.service.AlertFacadeREST.class);
        resources.add(com.veganet.gps.service.ApplicationsettingFacadeREST.class);
        resources.add(com.veganet.gps.service.DeviceFacadeREST.class);
        resources.add(com.veganet.gps.service.DevicedistributorFacadeREST.class);
        resources.add(com.veganet.gps.service.DistributerFacadeREST.class);
        resources.add(com.veganet.gps.service.DriverFacadeREST.class);
        resources.add(com.veganet.gps.service.GeofenceFacadeREST.class);
        resources.add(com.veganet.gps.service.NotificationFacadeREST.class);
        resources.add(com.veganet.gps.service.PositionFacadeREST.class);
        resources.add(com.veganet.gps.service.UserFacadeREST.class);
        resources.add(com.veganet.gps.service.VehicleFacadeREST.class);
    }
    
}
