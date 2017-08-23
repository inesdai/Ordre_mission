package com.veganet.gps.entities;

import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Driver;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-02T15:34:26")
@StaticMetamodel(Vehicle.class)
public class Vehicle_ { 

    public static volatile SingularAttribute<Vehicle, Float> startkm;
    public static volatile SingularAttribute<Vehicle, Short> icon;
    public static volatile CollectionAttribute<Vehicle, Driver> driverCollection;
    public static volatile SingularAttribute<Vehicle, String> model;
    public static volatile SingularAttribute<Vehicle, String> insurancephone;
    public static volatile SingularAttribute<Vehicle, Integer> periodassurance;
    public static volatile SingularAttribute<Vehicle, Date> createdate;
    public static volatile SingularAttribute<Vehicle, String> mark;
    public static volatile SingularAttribute<Vehicle, Integer> rememberbeforeoil;
    public static volatile SingularAttribute<Vehicle, Integer> periodoilchange;
    public static volatile SingularAttribute<Vehicle, Date> dateassurance;
    public static volatile SingularAttribute<Vehicle, Integer> vehicleid;
    public static volatile SingularAttribute<Vehicle, Date> lastplaybackdelete;
    public static volatile SingularAttribute<Vehicle, Float> lastoilchange;
    public static volatile SingularAttribute<Vehicle, Float> fuelconsumption;
    public static volatile SingularAttribute<Vehicle, Integer> rememberbeforetechnical;
    public static volatile SingularAttribute<Vehicle, Short> isactive;
    public static volatile SingularAttribute<Vehicle, Integer> maxSpeed;
    public static volatile SingularAttribute<Vehicle, String> description;
    public static volatile SingularAttribute<Vehicle, String> problemsvehicle;
    public static volatile SingularAttribute<Vehicle, Device> device;
    public static volatile SingularAttribute<Vehicle, String> typefuel;
    public static volatile SingularAttribute<Vehicle, Integer> durationplayback;
    public static volatile SingularAttribute<Vehicle, Short> technicalInspectiondPeriod;
    public static volatile SingularAttribute<Vehicle, Date> technicalInspectionDate;

}