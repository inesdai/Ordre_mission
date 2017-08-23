package com.veganet.gps.entities;

import com.veganet.gps.entities.Vehicle;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-02T15:34:26")
@StaticMetamodel(Driver.class)
public class Driver_ { 

    public static volatile SingularAttribute<Driver, String> phonenumber;
    public static volatile SingularAttribute<Driver, Date> createdate;
    public static volatile SingularAttribute<Driver, Date> licensevalidity;
    public static volatile SingularAttribute<Driver, String> lastname;
    public static volatile SingularAttribute<Driver, Date> dateofbirth;
    public static volatile SingularAttribute<Driver, Double> fuelvolume;
    public static volatile SingularAttribute<Driver, String> firstname;
    public static volatile SingularAttribute<Driver, Integer> driverid;
    public static volatile SingularAttribute<Driver, String> cinorpassport;
    public static volatile SingularAttribute<Driver, String> picturepath;
    public static volatile SingularAttribute<Driver, String> title;
    public static volatile SingularAttribute<Driver, String> address;
    public static volatile SingularAttribute<Driver, String> email;
    public static volatile SingularAttribute<Driver, String> gender;
    public static volatile CollectionAttribute<Driver, Vehicle> vehicleCollection;

}