package com.marm.parkingprogram.Model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-05-27T10:33:20")
@StaticMetamodel(Vehicle.class)
public class Vehicle_ { 

    public static volatile SingularAttribute<Vehicle, String> color;
    public static volatile SingularAttribute<Vehicle, String> model;
    public static volatile SingularAttribute<Vehicle, Integer> id;
    public static volatile SingularAttribute<Vehicle, Date> finishedOn;
    public static volatile SingularAttribute<Vehicle, String> brand;
    public static volatile SingularAttribute<Vehicle, String> carriagePlate;
    public static volatile SingularAttribute<Vehicle, Date> createdOn;

}