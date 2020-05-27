/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marm.parkingprogram.Model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author anr10
 */
@Embeddable
public class OperationPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private int id;
    @Basic(optional = false)
    @Column(name = "Vehicle_ID", nullable = false)
    private int vehicleID;
    @Basic(optional = false)
    @Column(name = "User_ID", nullable = false)
    private int userID;
    @Basic(optional = false)
    @Column(name = "ParkingSpace_ID", nullable = false)
    private int parkingSpaceID;

    public OperationPK() {
    }

    public OperationPK(int id, int vehicleID, int userID, int parkingSpaceID) {
        this.id = id;
        this.vehicleID = vehicleID;
        this.userID = userID;
        this.parkingSpaceID = parkingSpaceID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getParkingSpaceID() {
        return parkingSpaceID;
    }

    public void setParkingSpaceID(int parkingSpaceID) {
        this.parkingSpaceID = parkingSpaceID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) vehicleID;
        hash += (int) userID;
        hash += (int) parkingSpaceID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OperationPK)) {
            return false;
        }
        OperationPK other = (OperationPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.vehicleID != other.vehicleID) {
            return false;
        }
        if (this.userID != other.userID) {
            return false;
        }
        if (this.parkingSpaceID != other.parkingSpaceID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.marm.parkingprogram.Model.OperationPK[ id=" + id + ", vehicleID=" + vehicleID + ", userID=" + userID + ", parkingSpaceID=" + parkingSpaceID + " ]";
    }
    
}
