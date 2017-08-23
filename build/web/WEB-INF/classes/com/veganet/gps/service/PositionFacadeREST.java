/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.service;

import com.veganet.gps.entities.Position;
import com.veganet.gps.entities.UserVehicles;
import com.veganet.gps.entities.Vehicle;
import com.veganet.gps.entities.VehiculePositionWS;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("com.veganet.gps.entities.position")
public class PositionFacadeREST extends AbstractFacade<Position> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;
    @EJB
    private com.veganet.gps.session.VehicleFacade vehicleFacade;
    @EJB
    private com.veganet.gps.session.ApplicationsettingFacade applicationsettingFacade;

    public PositionFacadeREST() {
        super(Position.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Position entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Long id, Position entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Position find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Position> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Position> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    //convert time from current Time To Gps time
    public Date ConvertCstToDate(Date date) {
        int timezone = Integer.parseInt(applicationsettingFacade.findBy("Applicationsetting.findBySettingname", "settingName", "timeZone").getSettingValue());
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);

        calendar.add(Calendar.HOUR_OF_DAY, timezone - 9); // adds one hour
        return calendar.getTime();
    }

    @POST
    @Path("refreshPositionVehicles")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserVehicles refreshPositionVehicles(UserVehicles walid) {
        

        
        System.out.println("walidtest refresh");
        UserVehicles userVehicles = new UserVehicles();
        List<VehiculePositionWS> lVehiculePositionWS = new ArrayList<VehiculePositionWS>();
        VehiculePositionWS vpws = null;
        for (VehiculePositionWS vpWS : walid.getVehicule()) {

            try {
                Vehicle v = vehicleFacade.find(vpWS.getVehicleid());
                vpws = new VehiculePositionWS();

                vpws.setImei(v.getDevice().getImei());
                vpws.setVehicleid(v.getVehicleid());
                vpws.setPower(v.getDevice().getPower());
                vpws.setMaxSpeed(v.getDevice().getVehicleid().getMaxSpeed());
                vpws.setFuelconsumption(v.getDevice().getVehicleid().getFuelconsumption());

                try {
                    if (v.getDevice().getVehicleid().getIcon() == null) {
                        vpws.setIcon((short) 0);
                    } else {
                        vpws.setIcon(v.getDevice().getVehicleid().getIcon());
                    }

                } catch (Exception e) {
                    vpws.setIcon((short) 0);
                }

                try {
                    if (v.getDevice().getVehicleid().getTypefuel() == null) {
                        vpws.setTypefuel("");
                    } else {
                        vpws.setTypefuel(v.getDevice().getVehicleid().getTypefuel());
                    }

                } catch (Exception e) {
                    vpws.setTypefuel("");
                }

                try {
                    if (v.getDevice().getVehicleid().getModel() == null) {
                        vpws.setModel("");
                    } else {
                        vpws.setModel(v.getDevice().getVehicleid().getModel());
                    }

                } catch (Exception e) {
                    vpws.setModel("");
                }

                try {
                    if (v.getDevice().getVehicleid().getMark() == null) {
                        vpws.setMark("");
                    } else {
                        vpws.setMark(v.getDevice().getVehicleid().getMark());
                    }

                } catch (Exception e) {
                    vpws.setMark("");
                }

                try {
                    if (v.getDevice().getVehicleid().getDescription() == null) {
                        vpws.setDescription("");
                    } else {
                        vpws.setDescription(v.getDevice().getVehicleid().getDescription());
                    }
                } catch (Exception e) {
                    vpws.setDescription("");
                }

                vpws.setTimecreate(ConvertCstToDate(v.getDevice().getLastpositionid().getTimeCreateWitoutconvertion()));
                vpws.setPositionid(v.getDevice().getLastpositionid().getPositionid());
                vpws.setAltitude(v.getDevice().getLastpositionid().getAltitude());
                vpws.setLatitude(v.getDevice().getLastpositionid().getLatitude());
                vpws.setLongitude(v.getDevice().getLastpositionid().getLongitude());
                vpws.setSpeed(v.getDevice().getLastpositionid().getSpeed());
                vpws.setOther(v.getDevice().getLastpositionid().getOther());
                lVehiculePositionWS.add(vpws);
            } catch (Exception e) {
                vpws.setTimecreate(new Date());
                vpws.setPositionid(0L);
                vpws.setAltitude(0.0);
                vpws.setLatitude(0.0);
                vpws.setLongitude(0.0);
                vpws.setSpeed(0.0);
                vpws.setOther("");
                lVehiculePositionWS.add(vpws);
            }
        }
        userVehicles.setVehicule(lVehiculePositionWS);
        return userVehicles;
    }
}
