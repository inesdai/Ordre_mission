package com.veganet.gps.entities;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Notification;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-02T15:34:26")
@StaticMetamodel(Position.class)
public class Position_ { 

    public static volatile SingularAttribute<Position, Short> valid;
    public static volatile SingularAttribute<Position, Short> relaystatus;
    public static volatile SingularAttribute<Position, Date> timecreate;
    public static volatile SingularAttribute<Position, Double> mileage;
    public static volatile SingularAttribute<Position, String> other;
    public static volatile SingularAttribute<Position, Double> speed;
    public static volatile SingularAttribute<Position, Double> altitude;
    public static volatile SingularAttribute<Position, Short> accstatus;
    public static volatile CollectionAttribute<Position, Notification> notificationCollection;
    public static volatile SingularAttribute<Position, Short> signalgpsstatus;
    public static volatile SingularAttribute<Position, Device> deviceid;
    public static volatile SingularAttribute<Position, Integer> batterylevel;
    public static volatile SingularAttribute<Position, Double> course;
    public static volatile SingularAttribute<Position, Short> gprsstatus;
    public static volatile SingularAttribute<Position, Short> vibrationstatus;
    public static volatile SingularAttribute<Position, Short> sosbuttonstatus;
    public static volatile SingularAttribute<Position, Long> positionid;
    public static volatile SingularAttribute<Position, String> address;
    public static volatile SingularAttribute<Position, Short> power;
    public static volatile SingularAttribute<Position, Double> longitude;
    public static volatile SingularAttribute<Position, Double> latitude;
    public static volatile CollectionAttribute<Position, Device> deviceCollection;
    public static volatile SingularAttribute<Position, Short> batteryincharge;

}