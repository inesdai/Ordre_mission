package com.veganet.gps.entities;

import com.veganet.gps.entities.Devicedistributor;
import com.veganet.gps.entities.Geofence;
import com.veganet.gps.entities.Logs;
import com.veganet.gps.entities.Notification;
import com.veganet.gps.entities.Position;
import com.veganet.gps.entities.User;
import com.veganet.gps.entities.Vehicle;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-02T15:34:26")
@StaticMetamodel(Device.class)
public class Device_ { 

    public static volatile SingularAttribute<Device, Position> lastpositionid;
    public static volatile SingularAttribute<Device, Date> expirationdate;
    public static volatile SingularAttribute<Device, String> imei;
    public static volatile CollectionAttribute<Device, Position> positionCollection;
    public static volatile SingularAttribute<Device, Date> createdate;
    public static volatile CollectionAttribute<Device, Notification> notificationCollection;
    public static volatile SingularAttribute<Device, Date> activatedate;
    public static volatile SingularAttribute<Device, Short> geostatus;
    public static volatile SingularAttribute<Device, Integer> deviceid;
    public static volatile CollectionAttribute<Device, Devicedistributor> devicedistributorCollection;
    public static volatile SingularAttribute<Device, Vehicle> vehicleid;
    public static volatile SingularAttribute<Device, User> addedby;
    public static volatile SingularAttribute<Device, Short> isactive;
    public static volatile SingularAttribute<Device, String> urgentnumber3;
    public static volatile SingularAttribute<Device, String> urgentnumber2;
    public static volatile SingularAttribute<Device, Date> userexpirationdate;
    public static volatile SingularAttribute<Device, String> simcard;
    public static volatile SingularAttribute<Device, Short> isdeleted;
    public static volatile SingularAttribute<Device, String> name;
    public static volatile SingularAttribute<Device, Date> useractivationdate;
    public static volatile SingularAttribute<Device, Short> power;
    public static volatile SingularAttribute<Device, String> urgentnumber1;
    public static volatile CollectionAttribute<Device, Geofence> geofenceCollection;
    public static volatile CollectionAttribute<Device, Logs> logsCollection;

}