package com.veganet.gps.entities;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-02T15:34:26")
@StaticMetamodel(Logs.class)
public class Logs_ { 

    public static volatile SingularAttribute<Logs, String> description;
    public static volatile SingularAttribute<Logs, Integer> logID;
    public static volatile SingularAttribute<Logs, User> userid;
    public static volatile SingularAttribute<Logs, Date> datetime;
    public static volatile SingularAttribute<Logs, String> ipaddress;
    public static volatile SingularAttribute<Logs, Device> deviceid;

}