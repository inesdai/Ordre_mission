/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.service;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.UserVehicles;
import com.veganet.gps.entities.User;
import com.veganet.gps.entities.Vehicle;
import com.veganet.gps.entities.VehiculePositionWS;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.json.JsonObject;
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
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.core.MediaType;
import org.primefaces.json.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("com.veganet.gps.entities.user")
public class UserFacadeREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;
    @EJB
    private com.veganet.gps.session.UserFacade userFacade;
    @EJB
    private com.veganet.gps.session.DevicedistributorFacade devicedistributorFacade;
    @EJB
    private com.veganet.gps.session.ApplicationsettingFacade applicationsettingFacade;
    @EJB
    private com.veganet.gps.session.DeviceFacade deviceFacade;

    public UserFacadeREST() {
        super(User.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(User entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, User entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
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
    @Path("findByUsernamePassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserVehicles findByUsernamePassword(User user) {
        System.out.println("login walid test");
        User connectedUser = null;
        UserVehicles userVehicles = new UserVehicles();

        List<VehiculePositionWS> lVehiculePositionWS = new ArrayList<VehiculePositionWS>();

        try {
            connectedUser = (User) getEntityManager().createNamedQuery("User.findByUserPass")
                    .setParameter("username", user.getUsername())
                    .setParameter("password", user.getPassword())
                    .getSingleResult();

            List<Device> devices = new ArrayList<Device>();

            if (connectedUser.getAccesslevel() == 1 || connectedUser.getAccesslevel() == 3) {

                devices = devicedistributorFacade.findRange(null, (short) 1, null, connectedUser.getDistributerid(), (short) 0);

            } //admin
            else if (connectedUser.getAccesslevel() == 0) {

                devices = deviceFacade.findRange(null, (short) 0);

            } //customer
            else {
                devices = devicedistributorFacade.findRange(null, (short) 1, connectedUser, null, (short) 0);

            }

 
            for (int i = 0; i < devices.size(); i++) {
                VehiculePositionWS vpws = new VehiculePositionWS();
                try {

                    vpws.setImei(devices.get(i).getImei());
                    vpws.setVehicleid(devices.get(i).getVehicleid().getVehicleid());
                    vpws.setPower(devices.get(i).getPower());
                    vpws.setMaxSpeed(devices.get(i).getVehicleid().getMaxSpeed());
                    vpws.setFuelconsumption(devices.get(i).getVehicleid().getFuelconsumption());

                    try {
                        if (devices.get(i).getVehicleid().getIcon() == null) {
                            vpws.setIcon((short) 0);
                        } else {
                            vpws.setIcon(devices.get(i).getVehicleid().getIcon());
                        }

                    } catch (Exception e) {
                        vpws.setIcon((short) 0);
                    }

                    try {
                        if (devices.get(i).getVehicleid().getTypefuel() == null) {
                            vpws.setTypefuel("");
                        } else {
                            vpws.setTypefuel(devices.get(i).getVehicleid().getTypefuel());
                        }

                    } catch (Exception e) {
                        vpws.setTypefuel("");
                    }

                    try {
                        if (devices.get(i).getVehicleid().getModel() == null) {
                            vpws.setModel("");
                        } else {
                            vpws.setModel(devices.get(i).getVehicleid().getModel());
                        }

                    } catch (Exception e) {
                        vpws.setModel("");
                    }

                    try {
                        if (devices.get(i).getVehicleid().getMark() == null) {
                            vpws.setMark("");
                        } else {
                            vpws.setMark(devices.get(i).getVehicleid().getMark());
                        }

                    } catch (Exception e) {
                        vpws.setMark("");
                    }

                    try {
                        if (devices.get(i).getVehicleid().getDescription() == null) {
                            vpws.setDescription("");
                        } else {
                            vpws.setDescription(devices.get(i).getVehicleid().getDescription());
                        }
                    } catch (Exception e) {
                        vpws.setDescription("");
                    }

                    vpws.setTimecreate(ConvertCstToDate(devices.get(i).getLastpositionid().getTimeCreateWitoutconvertion()));
                    vpws.setPositionid(devices.get(i).getLastpositionid().getPositionid());
                    vpws.setAltitude(devices.get(i).getLastpositionid().getAltitude());
                    vpws.setLatitude(devices.get(i).getLastpositionid().getLatitude());
                    vpws.setLongitude(devices.get(i).getLastpositionid().getLongitude());
                    vpws.setSpeed(devices.get(i).getLastpositionid().getSpeed());
                    vpws.setOther(devices.get(i).getLastpositionid().getOther());

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
            userVehicles.setUserId(connectedUser.getUserid());
            userVehicles.setVehicule(lVehiculePositionWS);

        } catch (Exception e) {

            userVehicles.setVehicule(lVehiculePositionWS);
            userVehicles.setUserId(-1);

        }
        return userVehicles;

    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public User find(@PathParam("id") Integer id
    ) {
        return super.find(id);

    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<User> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to
    ) {
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

}
