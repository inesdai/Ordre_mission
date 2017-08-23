/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.service;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Notification;
import com.veganet.gps.entities.Position;
import com.veganet.gps.entities.User;
import com.veganet.gps.entities.Vehicle;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
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

/**
 *
 * @author user
 */
@Stateless
@Path("com.veganet.gps.entities.vehicle")
public class VehicleFacadeREST extends AbstractFacade<Vehicle> {

    @PersistenceContext(unitName = "gpstrackerPU")
    private EntityManager em;
    @EJB
    private com.veganet.gps.session.DevicedistributorFacade devicedistributorFacade;
    @EJB
    private com.veganet.gps.session.UserFacade userFacade;
    @EJB
    private com.veganet.gps.session.PositionFacade positionFacade;
    @EJB
    private com.veganet.gps.session.NotificationFacade notificationFacade;

    private Date toStatistics;
    private Date fromStatistics;
    private ArrayList<Device> myDevices;
    private int timezone;

    public VehicleFacadeREST() {
        super(Vehicle.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Vehicle entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, Vehicle entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Vehicle find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Vehicle> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Vehicle> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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

    @POST
    @Path("getMyVehicle")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Vehicle> getMyVehicle(User user) {
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        User connectedUser = userFacade.find(user.getUserid());
        for (Device d : devicedistributorFacade.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0)) {
            try {
                Vehicle v = new Vehicle();
                v.setVehicleid(d.getVehicleid().getVehicleid());
                v.setDescription(d.getVehicleid().getDescription());
                v.setFuelconsumption(d.getVehicleid().getFuelconsumption());
                v.setIcon(d.getVehicleid().getIcon());
                v.setMark(d.getVehicleid().getMark());
                v.setModel(d.getVehicleid().getModel());
                v.setMaxSpeed(d.getVehicleid().getMaxSpeed());
            } catch (Exception e) {
            }

            vehicles.add(d.getVehicleid());
        }
        return vehicles;
    }

    @POST
    @Path("defaultAlarmDetails")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Vehicle> prepareAlarmDetails(User user) {
        User connectedUser = userFacade.find(user.getUserid());
        myDevices = new ArrayList<>();
        myDevices.addAll(devicedistributorFacade.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0));

        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        fromStatistics = new Date();
        calendar.setTime(fromStatistics);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        fromStatistics = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        toStatistics = calendar.getTime();
        return alarmDetails(user.getStaticsType());

    }

    @POST
    @Path("prepareAlarmDetailsFilter")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
//    public void prepareAlarmDetailsFilter(Vehicle vehicle) {
//        User connectedUser = userFacade.find(user.getUserid());
//        myDevices = new ArrayList<Device>();
//        if (vehicle != null) {
//            myDevices.add(vehicle.getDevice());
//        } else {
//            myDevices.addAll(devicedistributorFacade.findRange(null, (short) 1, connectedUser, connectedUser.getDistributerid(), (short) 0));
//
//        }
//
//        alarmDetails();
//    }

    public List<Vehicle> alarmDetails(short staticsType) {
        timezone = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("timeZone"));

        List<Vehicle> vehicleItems = new ArrayList<Vehicle>();
        if (staticsType == (short) 0) {
            //all Alarms

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(geofenceInAlarms(myDevices.get(dev).getVehicleid()));
                vehicleItems.addAll(geofenceOutAlarms(myDevices.get(dev).getVehicleid()));
                vehicleItems.addAll(batteryShockSosAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 1) {
            //geo-fence in

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(geofenceInAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 2) {
            //geo-fence out

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(geofenceOutAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 3) {
            //low battery

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(batteryAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 4) {
            //sos alarm

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(sosAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 5) {
            //cut-off petrol

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(cutOffAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 6) {
            //restor petrol

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(restorPetrolAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else if (staticsType == (short) 7) {
            //vibration

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(shockAlarms(myDevices.get(dev).getVehicleid()));

            }
        } else {
            //displacement

            for (int dev = 0; dev < myDevices.size(); dev++) {

                vehicleItems.addAll(stopPoints(myDevices.get(dev).getVehicleid()));

            }
        }

        return vehicleItems;
    }

    public List<Vehicle> batteryShockSosAlarms(Vehicle vehicle) {
        ArrayList<Vehicle> MovingOverView = new ArrayList<Vehicle>();
        int lowBatteryCounter = 0, shockCounter = 0, sosCounter = 0;

        CharSequence sLowBattery = "<vol>2</vol>";
        CharSequence sMediumBattery = "<vol>3</vol>";

        CharSequence sShock = "<sh>1</sh>";
        CharSequence sSos = "<sos>1</sos>";

        //all positions for one device
        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), false, null, true, (short) 0);

        //all positions with precise date
        if (positionsFeedBack.size() > 0) {
            for (int posid = 0; posid < positionsFeedBack.size() - 1; posid++) {

                if (positionsFeedBack.get(posid + 1).getOther().contains(sLowBattery)) {
                    if (positionsFeedBack.get(posid).getOther().contains(sMediumBattery)) {
                        Vehicle vehicleItem = new Vehicle();
                        vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                        vehicleItem.setAlarmType((short) 3);
                        vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("LowBatteryAlarm"));
                        vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(posid + 1).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(posid + 1).getLongitude() * 100000) / 100000);
                        vehicleItem.setTimeStatistics(positionsFeedBack.get(posid + 1).getTimecreate());
                        MovingOverView.add(vehicleItem);
                    }
                }

                if (positionsFeedBack.get(posid).getOther().contains(sShock)) {
                    Vehicle vehicleItem = new Vehicle();
                    vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                    vehicleItem.setAlarmType((short) 7);
                    vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("VibrationAlarm"));
                    vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(posid).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(posid).getLongitude() * 100000) / 100000);
                    vehicleItem.setTimeStatistics(positionsFeedBack.get(posid).getTimecreate());
                    MovingOverView.add(vehicleItem);
                }
                if (positionsFeedBack.get(posid).getOther().contains(sSos)) {
                    Vehicle vehicleItem = new Vehicle();
                    vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                    vehicleItem.setAlarmType((short) 4);
                    vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("SOSAlarm"));
                    vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(posid).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(posid).getLongitude() * 100000) / 100000);
                    vehicleItem.setTimeStatistics(positionsFeedBack.get(posid).getTimecreate());
                    MovingOverView.add(vehicleItem);
                }
            }
            if (positionsFeedBack.get(positionsFeedBack.size() - 1).getOther().contains(sShock)) {
                Vehicle vehicleItem = new Vehicle();
                vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                vehicleItem.setAlarmType((short) 7);
                vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("VibrationAlarm"));
                vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLongitude() * 100000) / 100000);
                vehicleItem.setTimeStatistics(positionsFeedBack.get(positionsFeedBack.size() - 1).getTimecreate());
                MovingOverView.add(vehicleItem);
            }
            if (positionsFeedBack.get(positionsFeedBack.size() - 1).getOther().contains(sSos)) {
                Vehicle vehicleItem = new Vehicle();
                vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                vehicleItem.setAlarmType((short) 4);
                vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("SOSAlarm"));
                vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLongitude() * 100000) / 100000);
                vehicleItem.setTimeStatistics(positionsFeedBack.get(positionsFeedBack.size() - 1).getTimecreate());
                MovingOverView.add(vehicleItem);
            }
            if (positionsFeedBack.size() > 0) {
                if (positionsFeedBack.get(0).getOther().contains(sLowBattery)) {
                    if (positionFacade.findBy("Position.findByBeforePosition", "deviceid", positionsFeedBack.get(0).getDeviceid(), "timecreate", positionsFeedBack.get(0).getTimecreate()).getOther().contains(sMediumBattery)) {
                        Vehicle vehicleItem = new Vehicle();
                        vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                        vehicleItem.setAlarmType((short) 3);
                        vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("LowBatteryAlarm"));
                        vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(0).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(0).getLongitude() * 100000) / 100000);
                        vehicleItem.setTimeStatistics(positionsFeedBack.get(0).getTimecreate());
                        MovingOverView.add(0, vehicleItem);
                    }
                }
            }
        }

        return MovingOverView;
    }

    public List<Vehicle> geofenceInAlarms(Vehicle vehicle) {

        List<Vehicle> listAccDetails = new ArrayList<Vehicle>();

        List<Notification> notifications = notificationFacade.findRangeByDeviceDate(null, null, 2, vehicle.getDevice(), fromStatistics, toStatistics);
        if (notifications.size() > 0) {

            for (int i = 0; i < notifications.size(); i++) {
                Vehicle item = new Vehicle();
                item.setAlarmType((short) 1);
                item.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("GeoFenceIn"));
                item.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                item.setTimeStatistics(notifications.get(i).getTimenotification());
                item.setLangLat((double) Math.round(notifications.get(i).getPositionid().getLatitude() * 1000000) / 1000000 + "," + (double) Math.round(notifications.get(i).getPositionid().getLongitude() * 1000000) / 1000000);
                listAccDetails.add(item);
            }

        }
        return listAccDetails;
    }

    public List<Vehicle> geofenceOutAlarms(Vehicle vehicle) {

        List<Vehicle> listAccDetails = new ArrayList<Vehicle>();

        List<Notification> notifications = notificationFacade.findRangeByDeviceDate(null, null, 1, vehicle.getDevice(), fromStatistics, toStatistics);
        if (notifications.size() > 0) {

            for (int i = 0; i < notifications.size(); i++) {
                Vehicle item = new Vehicle();
                item.setAlarmType((short) 2);
                item.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("GeoFenceOut"));
                item.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                item.setTimeStatistics(notifications.get(i).getTimenotification());
                item.setLangLat((double) Math.round(notifications.get(i).getPositionid().getLatitude() * 1000000) / 1000000 + "," + (double) Math.round(notifications.get(i).getPositionid().getLongitude() * 1000000) / 1000000);
                listAccDetails.add(item);
            }

        }
        return listAccDetails;
    }

    public List<Vehicle> batteryAlarms(Vehicle vehicle) {
        ArrayList<Vehicle> MovingOverView = new ArrayList<Vehicle>();

        CharSequence sLowBattery = "<vol>2</vol>";
        CharSequence sMediumBattery = "<vol>3</vol>";

        //all positions for one device
        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), false, null, true, (short) 0);

        //all positions with precise date
        if (positionsFeedBack.size() > 0) {
            for (int posid = 0; posid < positionsFeedBack.size() - 1; posid++) {

                if (positionsFeedBack.get(posid + 1).getOther().contains(sLowBattery)) {
                    if (positionsFeedBack.get(posid).getOther().contains(sMediumBattery)) {
                        Vehicle vehicleItem = new Vehicle();
                        vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                        vehicleItem.setAlarmType((short) 3);
                        vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("LowBatteryAlarm"));
                        vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(posid + 1).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(posid + 1).getLongitude() * 100000) / 100000);
                        vehicleItem.setTimeStatistics(positionsFeedBack.get(posid + 1).getTimecreate());
                        MovingOverView.add(vehicleItem);
                    }
                }

            }
            if (positionsFeedBack.size() > 0) {
                if (positionsFeedBack.get(0).getOther().contains(sLowBattery)) {
                    if (positionFacade.findBy("Position.findByBeforePosition", "deviceid", positionsFeedBack.get(0).getDeviceid(), "timecreate", positionsFeedBack.get(0).getTimecreate()).getOther().contains(sMediumBattery)) {
                        Vehicle vehicleItem = new Vehicle();
                        vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                        vehicleItem.setAlarmType((short) 3);
                        vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("LowBatteryAlarm"));
                        vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(0).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(0).getLongitude() * 100000) / 100000);
                        vehicleItem.setTimeStatistics(positionsFeedBack.get(0).getTimecreate());
                        MovingOverView.add(0, vehicleItem);
                    }
                }
            }

        }

        return MovingOverView;
    }

    public List<Vehicle> shockAlarms(Vehicle vehicle) {
        ArrayList<Vehicle> MovingOverView = new ArrayList<Vehicle>();
        int lowBatteryCounter = 0, shockCounter = 0, sosCounter = 0;

        CharSequence sShock = "<sh>1</sh>";

        //all positions for one device
        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), false, null, true, (short) 0);

        //all positions with precise date
        if (positionsFeedBack.size() > 0) {
            for (int posid = 0; posid < positionsFeedBack.size() - 1; posid++) {

                if (positionsFeedBack.get(posid).getOther().contains(sShock)) {
                    Vehicle vehicleItem = new Vehicle();
                    vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                    vehicleItem.setAlarmType((short) 7);
                    vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("VibrationAlarm"));
                    vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(posid).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(posid).getLongitude() * 100000) / 100000);
                    vehicleItem.setTimeStatistics(positionsFeedBack.get(posid).getTimecreate());
                    MovingOverView.add(vehicleItem);
                }
            }
            if (positionsFeedBack.get(positionsFeedBack.size() - 1).getOther().contains(sShock)) {
                Vehicle vehicleItem = new Vehicle();
                vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                vehicleItem.setAlarmType((short) 7);
                vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("VibrationAlarm"));
                vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLongitude() * 100000) / 100000);
                vehicleItem.setTimeStatistics(positionsFeedBack.get(positionsFeedBack.size() - 1).getTimecreate());
                MovingOverView.add(vehicleItem);
            }
        }

        return MovingOverView;
    }

    public List<Vehicle> sosAlarms(Vehicle vehicle) {
        ArrayList<Vehicle> MovingOverView = new ArrayList<Vehicle>();
        CharSequence sSos = "<sos>1</sos>";

        //all positions for one device
        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), false, null, true, (short) 0);

        //all positions with precise date
        if (positionsFeedBack.size() > 0) {
            for (int posid = 0; posid < positionsFeedBack.size() - 1; posid++) {

                if (positionsFeedBack.get(posid).getOther().contains(sSos)) {
                    Vehicle vehicleItem = new Vehicle();
                    vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                    vehicleItem.setAlarmType((short) 4);
                    vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("SOSAlarm"));
                    vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(posid).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(posid).getLongitude() * 100000) / 100000);
                    vehicleItem.setTimeStatistics(positionsFeedBack.get(posid).getTimecreate());
                    MovingOverView.add(vehicleItem);
                }
            }

            if (positionsFeedBack.get(positionsFeedBack.size() - 1).getOther().contains(sSos)) {
                Vehicle vehicleItem = new Vehicle();
                vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                vehicleItem.setAlarmType((short) 4);
                vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("SOSAlarm"));
                vehicleItem.setLangLat((double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLatitude() * 100000) / 100000 + "," + (double) Math.round(positionsFeedBack.get(positionsFeedBack.size() - 1).getLongitude() * 100000) / 100000);
                vehicleItem.setTimeStatistics(positionsFeedBack.get(positionsFeedBack.size() - 1).getTimecreate());
                MovingOverView.add(vehicleItem);
            }

        }

        return MovingOverView;
    }

    public List<Vehicle> cutOffAlarms(Vehicle vehicle) {
        ArrayList<Vehicle> MovingOverView = new ArrayList<Vehicle>();

        //all positions for one device
        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), false, null, true, (short) 2);

        //all positions with precise date
        if (positionsFeedBack.size() > 0) {
            for (int posid = 0; posid < positionsFeedBack.size(); posid++) {
                Position lastp = positionFacade.findBy("Position.findByBeforePetrolPosition", "deviceid", vehicle.getDevice(), "timecreate", ConvertDateToCst(positionsFeedBack.get(posid).getTimeCreateWitoutconvertion()));
                Vehicle vehicleItem = new Vehicle();
                vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                vehicleItem.setAlarmType((short) 5);
                vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("CutoffAlarm"));
                vehicleItem.setLangLat((double) Math.round(lastp.getLatitude() * 100000) / 100000 + "," + (double) Math.round(lastp.getLongitude() * 100000) / 100000);
                vehicleItem.setTimeStatistics(positionsFeedBack.get(posid).getTimecreate());
                MovingOverView.add(vehicleItem);

            }

        }

        return MovingOverView;
    }

    public List<Vehicle> restorPetrolAlarms(Vehicle vehicle) {
        ArrayList<Vehicle> MovingOverView = new ArrayList<Vehicle>();

        //all positions for one device
        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), false, null, true, (short) 1);

        //all positions with precise date
        if (positionsFeedBack.size() > 0) {
            for (int posid = 0; posid < positionsFeedBack.size(); posid++) {
                Position lastp = positionFacade.findBy("Position.findByBeforePetrolPosition", "deviceid", vehicle.getDevice(), "timecreate", ConvertDateToCst(positionsFeedBack.get(posid).getTimeCreateWitoutconvertion()));
                Vehicle vehicleItem = new Vehicle();
                vehicleItem.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                vehicleItem.setAlarmType((short) 6);
                vehicleItem.setAlarmTypeText(ResourceBundle.getBundle("/Bundle").getString("RestorPetrolAlarm"));
                vehicleItem.setLangLat((double) Math.round(lastp.getLatitude() * 100000) / 100000 + "," + (double) Math.round(lastp.getLongitude() * 100000) / 100000);
                vehicleItem.setTimeStatistics(positionsFeedBack.get(posid).getTimecreate());
                MovingOverView.add(vehicleItem);

            }

        }

        return MovingOverView;
    }

    public List<Vehicle> stopPoints(Vehicle vehicle) {
        List<Vehicle> listStopPoints = new ArrayList<Vehicle>();

        Date dStart = null;
        Date dStop = null;
        Position precPosition;
        Position nexPosition;
        int i;

        List<Position> positionsFeedBack = positionFacade.findRange(null, ConvertDateToCst(fromStatistics), ConvertDateToCst(toStatistics), vehicle.getDevice(), true, null, false, (short) 0);
        if (positionsFeedBack.size() > 0) {
            precPosition = positionsFeedBack.get(0);

            for (i = 1; i < positionsFeedBack.size(); i++) {
                nexPosition = positionsFeedBack.get(i);

//     0.0001==11 Meter/0.00012==11 Meter
                if (Math.abs(precPosition.getLatitude() - nexPosition.getLatitude()) < 0.0003 && Math.abs(precPosition.getLongitude() - nexPosition.getLongitude()) < 0.00035) {

                } else {

                    if (Math.abs(precPosition.getTimecreate().getTime() - positionsFeedBack.get(i - 1).getTimecreate().getTime()) > 60000) {
                        //add this point stop
                        Vehicle item = new Vehicle();

                        item.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                        item.setStartTime(precPosition.getTimecreate());
                        item.setEndTime(positionsFeedBack.get(i - 1).getTimecreate());
                        item.setLangLat((double) Math.round(precPosition.getLatitude() * 1000000) / 1000000 + "," + (double) Math.round(precPosition.getLongitude() * 1000000) / 1000000);
                        item.setContinueTime(ConvertSecondToHHMMString((int) (item.getEndTime().getTime() - item.getStartTime().getTime()) / 1000));
                        listStopPoints.add(item);
                    }

                    precPosition = nexPosition;

                }
            }
            if (Math.abs(precPosition.getTimecreate().getTime() - positionsFeedBack.get(i - 1).getTimecreate().getTime()) > 60000) {
                //add this point stop
                Vehicle item = new Vehicle();

                item.setMark(vehicle.getDescription() + " " + vehicle.getMark() + "[" + vehicle.getModel() + "] ");
                item.setStartTime(precPosition.getTimecreate());
                item.setEndTime(positionsFeedBack.get(i - 1).getTimecreate());
                item.setLangLat((double) Math.round(precPosition.getLatitude() * 1000000) / 1000000 + "," + (double) Math.round(precPosition.getLongitude() * 1000000) / 1000000);
                item.setContinueTime(ConvertSecondToHHMMString((int) (item.getEndTime().getTime() - item.getStartTime().getTime()) / 1000));

                listStopPoints.add(item);
            }

        }
        return listStopPoints;
    }

    //conevrt seconds to time
    private String ConvertSecondToHHMMString(int secondtTime) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        String time = df.format(new Date(secondtTime * 1000L));

        return time;

    }

    //calcul distance between two positions
    //example System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, 'K') + " Kilometers\n");
    private double distance(Position p1, Position p2) {

        double lat1 = p1.getLatitude();
        double lon1 = p1.getLongitude();
        double lat2 = p2.getLatitude();
        double lon2 = p2.getLongitude();
        double theta = lon1 - lon2;
        if (theta != 0 && lat1 - lat2 != 0) {
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
//        if (unit == 'K') {
            dist = dist * 1.609344;
//        } else if (unit == 'N') {
//            dist = dist * 0.8684;
//        }
            return (dist);
        } else {
            return 0;
        }

    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    //convert time from current Time To Gps time
    public Date ConvertDateToCst(Date date) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -timezone + 9); // adds one hour
        return calendar.getTime();
    }

    //convert time from current Time To Gps time
    public Date ConvertDateCurrentTo(Date date) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -timezone + 9); // adds one hour
        return calendar.getTime();
    }

}
