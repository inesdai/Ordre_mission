package com.veganet.gps.entities;

import com.veganet.gps.entities.Device;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-02T15:34:26")
@StaticMetamodel(Geofence.class)
public class Geofence_ { 

    public static volatile SingularAttribute<Geofence, String> geofencename;
    public static volatile SingularAttribute<Geofence, Short> status;
    public static volatile SingularAttribute<Geofence, Integer> geofenceid;
    public static volatile SingularAttribute<Geofence, String> geofencezone;
    public static volatile SingularAttribute<Geofence, Date> desactivatetime;
    public static volatile SingularAttribute<Geofence, Date> createdate;
    public static volatile SingularAttribute<Geofence, Short> typegeofence;
    public static volatile SingularAttribute<Geofence, Date> activatetime;
    public static volatile SingularAttribute<Geofence, Device> deviceid;

}