package com.veganet.gps.entities;

import com.veganet.gps.entities.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-02T15:34:26")
@StaticMetamodel(Alert.class)
public class Alert_ { 

    public static volatile SingularAttribute<Alert, Double> startkm;
    public static volatile SingularAttribute<Alert, String> receiver;
    public static volatile SingularAttribute<Alert, User> userid;
    public static volatile SingularAttribute<Alert, Date> createdate;
    public static volatile SingularAttribute<Alert, Short> recurencetype;
    public static volatile SingularAttribute<Alert, Integer> everyweek;
    public static volatile SingularAttribute<Alert, Integer> typealert;
    public static volatile SingularAttribute<Alert, Short> isgenerated;
    public static volatile SingularAttribute<Alert, Date> duration;
    public static volatile SingularAttribute<Alert, Double> endkm;
    public static volatile SingularAttribute<Alert, Date> startdate;
    public static volatile SingularAttribute<Alert, Integer> endoccurence;
    public static volatile SingularAttribute<Alert, Long> alertid;
    public static volatile SingularAttribute<Alert, Short> isdeleted;
    public static volatile SingularAttribute<Alert, String> action;
    public static volatile SingularAttribute<Alert, Short> everydayonweek;
    public static volatile SingularAttribute<Alert, Date> enddate;
    public static volatile SingularAttribute<Alert, Short> activeted;
    public static volatile SingularAttribute<Alert, String> othertype;

}