package com.veganet.gps.entities;

import com.veganet.gps.entities.Devicedistributor;
import com.veganet.gps.entities.Distributer;
import com.veganet.gps.entities.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-02T15:34:26")
@StaticMetamodel(Distributer.class)
public class Distributer_ { 

    public static volatile CollectionAttribute<Distributer, Distributer> distributerCollection;
    public static volatile SingularAttribute<Distributer, Short> isactive;
    public static volatile SingularAttribute<Distributer, String> email;
    public static volatile SingularAttribute<Distributer, Integer> distributerid;
    public static volatile SingularAttribute<Distributer, Short> isdeleted;
    public static volatile SingularAttribute<Distributer, Date> createdate;
    public static volatile SingularAttribute<Distributer, Distributer> parentid;
    public static volatile SingularAttribute<Distributer, String> logopath;
    public static volatile SingularAttribute<Distributer, String> contact;
    public static volatile SingularAttribute<Distributer, User> addeddby;
    public static volatile CollectionAttribute<Distributer, Devicedistributor> devicedistributorCollection;
    public static volatile CollectionAttribute<Distributer, User> userCollection;

}