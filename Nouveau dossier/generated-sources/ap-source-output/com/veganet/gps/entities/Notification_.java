package com.veganet.gps.entities;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Position;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-02T15:34:26")
@StaticMetamodel(Notification.class)
public class Notification_ { 

    public static volatile SingularAttribute<Notification, Position> positionid;
    public static volatile SingularAttribute<Notification, Date> timenotification;
    public static volatile SingularAttribute<Notification, String> description;
    public static volatile SingularAttribute<Notification, Long> notificationid;
    public static volatile SingularAttribute<Notification, Integer> typenotification;
    public static volatile SingularAttribute<Notification, Short> satus;
    public static volatile SingularAttribute<Notification, Device> deviceid;

}