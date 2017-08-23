/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.jsf;

import com.gpstracker.graphic.Polygon2D;
import com.veganet.gps.entities.Alert;
import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Driver;
import com.veganet.gps.entities.Geofence;
import com.veganet.gps.entities.Logs;
import com.veganet.gps.entities.Notification;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "applicationScopeController", eager = true)
@ApplicationScoped
public class ApplicationScopeController implements Serializable {

    @EJB
    private com.veganet.gps.session.AlertFacade alertFacade;
    @EJB
    private com.veganet.gps.session.LogsFacade logsFacade;
    @EJB
    private com.veganet.gps.session.DeviceFacade deviceFacade;
    @EJB
    private com.veganet.gps.session.NotificationFacade notificationFacade;
    @EJB
    private com.veganet.gps.session.DriverFacade driverFacade;
    @EJB
    private com.veganet.gps.session.GeofenceFacade geofenceFacade;
    private Iterator<Device> iDevices;
    private FacesContext context;
    private ELContext elContext;
    private Notification notification;
    private Device d;
    private Timer timer = new Timer("MyTimer");//create a new Timer
    private int lastGeoStatus;
    private boolean saveGeoStatus;
    private String description = "";
    private String typealert = "";
    private Geofence geofence;

    public ApplicationScopeController() {
        timerapplication();
    }

    public void timerapplication() {

        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                //generate notification:

                List<Alert> alerts = new ArrayList<Alert>();
                Iterator<Alert> iAlert = null;
                Alert a;

                alerts = alertFacade.findByBeforeThisDate("Alert.findByBetweenTwoDate", "isdeleted", (short) 0);

                if (!alerts.isEmpty()) {
                    iAlert = alerts.iterator();

                    for (int i = 0; i < alerts.size(); i++) {

                        a = (Alert) iAlert.next();

//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date dated = null;
//                    Date datef = null;
//                    try {
//                        dated = formatter.parse(start);
//                        datef = formatter.parse(end);
//                    } catch (ParseException ex) {
//                        Logger.getLogger(ApplicationScopeController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                        if (a.getIsgenerated() == 0) {
                            if (a.getTypealert() == 0) {
                                typealert = "Oil change";
                            }
                            if (a.getTypealert() == 1) {
                                typealert = "Washink car";
                            }
                            if (a.getTypealert() == 3) {
                                typealert = a.getOthertype();
                            }
                            Notification notification2 = new Notification(a.getTypealert(), typealert, new Date(), (short) 0, null, null);
                            Driver d = driverFacade.find(Integer.parseInt(a.getReceiver()));
                            notification2.setDeviceid(d.getVehicleCollection().iterator().next().getDevice());

                            a.setIsgenerated((short) 1);
                            alertFacade.edit(a);
                            notificationFacade.create(notification2);
                        }
                    }
                }

                //geofence alerts
                List<Geofence> geofences = null;

                geofences = geofenceFacade.findByTwoDate("Geofence.findByBetweenTwoDate", "activatetime", new Date(), "enddate", new Date());
                geofences.addAll(geofenceFacade.findByAllDate("Geofence.findByAllDate"));

                for (int v = 0; v < geofences.size(); v++) {
                    int typeNotif = 0;
                    geofence = geofences.get(v);

                    //circle geofence 
                    if (geofence.getTypegeofence() % 10 == 1) {
                        //geofence just created
                        if (geofence.getStatus() == 0) {
                            if (containFunctionCircle(geofence.getDeviceid().getLastpositionid().getLatitude(), geofence.getDeviceid().getLastpositionid().getLongitude(), geofence) == 1) {
                                petrolCommand(40, geofence.getDeviceid());
                                geofence.setStatus((short) 1);
                                geofenceFacade.edit(geofence);
                                description = "entered to geo-Fence";
                                typeNotif = 2;
                                notification = new Notification(typeNotif, description, new Date(), (short) 0, geofence.getDeviceid(), geofence.getDeviceid().getLastpositionid());
                                notificationFacade.create(notification);
                            } else if (containFunctionCircle(geofence.getDeviceid().getLastpositionid().getLatitude(), geofence.getDeviceid().getLastpositionid().getLongitude(), geofence) == 2) {
                                petrolCommand(geofence.getTypegeofence(), geofence.getDeviceid());
                                geofence.setStatus((short) 2);
                                geofenceFacade.edit(geofence);
                                description = "out of geo-Fence";
                                typeNotif = 1;
                                notification = new Notification(typeNotif, description, new Date(), (short) 0, geofence.getDeviceid(), geofence.getDeviceid().getLastpositionid());
                                notificationFacade.create(notification);
                            }

                        } //geofence created with first test
                        else if (geofence.getStatus() != containFunctionCircle(geofence.getDeviceid().getLastpositionid().getLatitude(), geofence.getDeviceid().getLastpositionid().getLongitude(), geofence)) {

                            if (geofence.getStatus() == (short) 1) {
                                petrolCommand(geofence.getTypegeofence(), geofence.getDeviceid());

                                geofence.setStatus((short) 2);
                                description = "out of geo-Fence";
                                typeNotif = 1;

                            } else {
                                petrolCommand(40, geofence.getDeviceid());

                                geofence.setStatus((short) 1);
                                description = "entered to geo-Fence";
                                typeNotif = 2;

                            }
                            context = FacesContext.getCurrentInstance();
                            notification = new Notification(typeNotif, description, new Date(), (short) 0, geofence.getDeviceid(), geofence.getDeviceid().getLastpositionid());

                            notificationFacade.create(notification);

                        }
                        //polygone geofence
                    } else if (geofence.getTypegeofence() % 10 == 2) {
                        petrolCommand(geofence.getTypegeofence(), geofence.getDeviceid());
                        //geofence just created
                        if (geofence.getStatus() == 0) {

                            if (containFunctionPolygone(geofence.getDeviceid().getLastpositionid().getLatitude(), geofence.getDeviceid().getLastpositionid().getLongitude(), geofence) == 1) {
                                petrolCommand(40, geofence.getDeviceid());

                                geofence.setStatus((short) 1);
                                geofenceFacade.edit(geofence);
                                description = "entered to geo-Fence";
                                typeNotif = 2;
                                notification = new Notification(typeNotif, description, new Date(), (short) 0, geofence.getDeviceid(), geofence.getDeviceid().getLastpositionid());
                                notificationFacade.create(notification);
                            } else if (containFunctionPolygone(geofence.getDeviceid().getLastpositionid().getLatitude(), geofence.getDeviceid().getLastpositionid().getLongitude(), geofence) == 2) {
                                petrolCommand(geofence.getTypegeofence(), geofence.getDeviceid());

                                geofence.setStatus((short) 2);
                                geofenceFacade.edit(geofence);
                                description = "out of geo-Fence";
                                typeNotif = 1;
                                notification = new Notification(typeNotif, description, new Date(), (short) 0, geofence.getDeviceid(), geofence.getDeviceid().getLastpositionid());
                                notificationFacade.create(notification);
                            }

                        } else if (geofence.getStatus() != containFunctionPolygone(geofence.getDeviceid().getLastpositionid().getLatitude(), geofence.getDeviceid().getLastpositionid().getLongitude(), geofence)) {

                            if (geofence.getStatus() == (short) 1) {
                                petrolCommand(geofence.getTypegeofence(), geofence.getDeviceid());

                                geofence.setStatus((short) 2);
                                description = "out of geo-Fence";
                                typeNotif = 1;

                            } else {
                                petrolCommand(40, geofence.getDeviceid());

                                geofence.setStatus((short) 1);
                                description = "entered to geo-Fence";
                                typeNotif = 2;

                            }
                            context = FacesContext.getCurrentInstance();
                            notification = new Notification(0, description, new Date(), (short) typeNotif, geofence.getDeviceid(), geofence.getDeviceid().getLastpositionid());

                            notificationFacade.create(notification);

                        }

                    }

                }

            }
        };

        timer.scheduleAtFixedRate(timerTask, 500, 30000);//this line starts the timer at the same time its executed
        //timer.scheduleAtFixedRate(timerTask, 0, 60000);
    }

    public int containFunctionPolygone(double xPoint, double yPoint, Geofence geoFence) {
        float[] xPoly = null;
        float[] yPoly = null;
        String s = null;
        int r = 2;
        Polygon2D p = null;
        s = geofence.getGeofencezone();
        s = s.replace("(", "");
        s = s.replace(")", "");
        String[] point = s.split(",");
        xPoly = new float[point.length];
        yPoly = new float[point.length];
        int i = 0;
        for (int k = 0; k < point.length; k = k + 2) {
            xPoly[i] = Float.parseFloat(point[k]);
            yPoly[i] = Float.parseFloat(point[k + 1]);
            i++;
        }
        p = new Polygon2D(xPoly, yPoly, xPoly.length / 2);

        if (p.contains(xPoint, yPoint)) {
            System.out.println("contain");
            r = 1;
        } else if (!p.contains(xPoint, yPoint)) {
            System.out.println("not container");
            r = 2;

        }
        return r;
    }

    public int containFunctionCircle(double xPoint, double yPoint, Geofence geofence) {
        String s = "";
        s = geofence.getGeofencezone();
        s = s.replace("(", "");
        s = s.replace(")", "");
        String[] point = s.split(",");
        if (point.length == 3) {

            if (Math.pow((Float.parseFloat(point[1]) - xPoint), 2) + Math.pow((Float.parseFloat(point[2]) - yPoint), 2) < Math.pow(Float.parseFloat(point[0]), 2)) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 2;
        }
    }

    public void petrolCommand(int typeAction, Device device) {
        //petrol  saved in  logs table
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress == null) {
            ipAddress = "non dispo";
        }
        Logs log = null ;

        // safety cut off petrol 
        if (typeAction == 21 || typeAction == 22) {
            device.setPower((short) 2);
            log = new Logs(new Date(), "Cut off Petrol", null, ipAddress,device);

        } //emergency cut off petrol
        else if (typeAction == 31 || typeAction == 32) {
            device.setPower((short) 4);
            log = new Logs(new Date(), "Cut off Petrol", null, ipAddress,device);

        } //restart petrol 
        else if (typeAction == 40) {
            device.setPower((short) 1);
            log = new Logs(new Date(), "Restore Petrol", null, ipAddress,device);

        }

        logsFacade.create(log);
        deviceFacade.edit(device);
    }

}
