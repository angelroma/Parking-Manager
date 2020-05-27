package com.marm.parkingprogram.Model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-05-27T10:33:20")
@StaticMetamodel(ParkingSpace.class)
public class ParkingSpace_ { 

    public static volatile SingularAttribute<ParkingSpace, Integer> id;
    public static volatile SingularAttribute<ParkingSpace, Date> createdOn;
    public static volatile SingularAttribute<ParkingSpace, Boolean> occupied;
    public static volatile SingularAttribute<ParkingSpace, Boolean> status;

}