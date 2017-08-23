package com.veganet.gps.entities;

import com.veganet.gps.entities.Alert;
import com.veganet.gps.entities.Device;
import com.veganet.gps.entities.Devicedistributor;
import com.veganet.gps.entities.Distributer;
import com.veganet.gps.entities.Logs;
import com.veganet.gps.entities.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-02T15:34:26")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> phonenumber;
    public static volatile SingularAttribute<User, Integer> userid;
    public static volatile SingularAttribute<User, Date> createdate;
    public static volatile SingularAttribute<User, String> lastname;
    public static volatile SingularAttribute<User, Date> dateofbirth;
    public static volatile SingularAttribute<User, String> firstname;
    public static volatile SingularAttribute<User, User> createdby;
    public static volatile SingularAttribute<User, Short> resetpassword;
    public static volatile SingularAttribute<User, String> password;
    public static volatile CollectionAttribute<User, User> userCollection;
    public static volatile CollectionAttribute<User, Distributer> distributerCollection;
    public static volatile CollectionAttribute<User, Devicedistributor> devicedistributoraddbymeCollection;
    public static volatile SingularAttribute<User, String> username;
    public static volatile SingularAttribute<User, Integer> accesslevel;
    public static volatile SingularAttribute<User, String> usedmap;
    public static volatile SingularAttribute<User, Short> isactive;
    public static volatile SingularAttribute<User, String> email;
    public static volatile SingularAttribute<User, Distributer> distributerid;
    public static volatile SingularAttribute<User, Short> isdeleted;
    public static volatile SingularAttribute<User, Short> admincreateaccess;
    public static volatile CollectionAttribute<User, Alert> alertCollection;
    public static volatile CollectionAttribute<User, Devicedistributor> mydevicedistributorCollection;
    public static volatile CollectionAttribute<User, Device> deviceCollection;
    public static volatile CollectionAttribute<User, Logs> logsCollection;

}