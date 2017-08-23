package com.veganet.gps.entities;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Distributer;
import com.veganet.gps.entities.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-02T15:34:26")
@StaticMetamodel(Devicedistributor.class)
public class Devicedistributor_ { 

    public static volatile SingularAttribute<Devicedistributor, Integer> devicedistributerid;
    public static volatile SingularAttribute<Devicedistributor, User> addedby;
    public static volatile SingularAttribute<Devicedistributor, User> customerid;
    public static volatile SingularAttribute<Devicedistributor, Distributer> distributerid;
    public static volatile SingularAttribute<Devicedistributor, Short> assigned;
    public static volatile SingularAttribute<Devicedistributor, Date> createdate;
    public static volatile SingularAttribute<Devicedistributor, Device> deviceid;

}