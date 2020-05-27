/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marm.parkingprogram.Model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author anr10
 */
@Entity
@Table(name = "Operation", catalog = "ParkingCompany", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Operation.findAll", query = "SELECT o FROM Operation o"),
    @NamedQuery(name = "Operation.findById", query = "SELECT o FROM Operation o WHERE o.operationPK.id = :id"),
    @NamedQuery(name = "Operation.findByCreatedOn", query = "SELECT o FROM Operation o WHERE o.createdOn = :createdOn"),
    @NamedQuery(name = "Operation.findByFinishedOn", query = "SELECT o FROM Operation o WHERE o.finishedOn = :finishedOn"),
    @NamedQuery(name = "Operation.findByTotalAmount", query = "SELECT o FROM Operation o WHERE o.totalAmount = :totalAmount"),
    @NamedQuery(name = "Operation.findByVehicleID", query = "SELECT o FROM Operation o WHERE o.operationPK.vehicleID = :vehicleID"),
    @NamedQuery(name = "Operation.findByUserID", query = "SELECT o FROM Operation o WHERE o.operationPK.userID = :userID"),
    @NamedQuery(name = "Operation.findByParkingSpaceID", query = "SELECT o FROM Operation o WHERE o.operationPK.parkingSpaceID = :parkingSpaceID")})
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OperationPK operationPK;
    @Basic(optional = false)
    @Column(name = "CreatedOn", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "FinishedOn", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedOn;
    @Basic(optional = false)
    @Column(name = "TotalAmount", nullable = false)
    private float totalAmount;
    @JoinColumn(name = "ParkingSpace_ID", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ParkingSpace parkingSpace;
    @JoinColumn(name = "User_ID", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;
    @JoinColumn(name = "Vehicle_ID", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Vehicle vehicle;

    public Operation() {
    }

    public Operation(OperationPK operationPK) {
        this.operationPK = operationPK;
    }

    public Operation(OperationPK operationPK, Date createdOn, Date finishedOn, float totalAmount) {
        this.operationPK = operationPK;
        this.createdOn = createdOn;
        this.finishedOn = finishedOn;
        this.totalAmount = totalAmount;
    }

    public Operation(int id, int vehicleID, int userID, int parkingSpaceID) {
        this.operationPK = new OperationPK(id, vehicleID, userID, parkingSpaceID);
    }

    public OperationPK getOperationPK() {
        return operationPK;
    }

    public void setOperationPK(OperationPK operationPK) {
        this.operationPK = operationPK;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getFinishedOn() {
        return finishedOn;
    }

    public void setFinishedOn(Date finishedOn) {
        this.finishedOn = finishedOn;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (operationPK != null ? operationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Operation)) {
            return false;
        }
        Operation other = (Operation) object;
        if ((this.operationPK == null && other.operationPK != null) || (this.operationPK != null && !this.operationPK.equals(other.operationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.marm.parkingprogram.Model.Operation[ operationPK=" + operationPK + " ]";
    }
    
}
