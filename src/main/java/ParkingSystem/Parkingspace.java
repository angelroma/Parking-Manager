/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParkingSystem;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author anr10
 */
@Entity
@Table(name = "parkingspace")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parkingspace.findAll", query = "SELECT p FROM Parkingspace p"),
    @NamedQuery(name = "Parkingspace.findById", query = "SELECT p FROM Parkingspace p WHERE p.id = :id"),
    @NamedQuery(name = "Parkingspace.findByOccupied", query = "SELECT p FROM Parkingspace p WHERE p.occupied = :occupied"),
    @NamedQuery(name = "Parkingspace.findByCreatedOn", query = "SELECT p FROM Parkingspace p WHERE p.createdOn = :createdOn"),
    @NamedQuery(name = "Parkingspace.findByStatus", query = "SELECT p FROM Parkingspace p WHERE p.status = :status")})
public class Parkingspace implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Occupied")
    private boolean occupied;
    @Basic(optional = false)
    @Column(name = "CreatedOn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "Status")
    private boolean status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parkingSpaceID")
    private List<Vehicle> vehicleList;

    public Parkingspace() {
    }

    public Parkingspace(Integer id) {
        this.id = id;
    }

    public Parkingspace(Integer id, boolean occupied, Date createdOn, boolean status) {
        this.id = id;
        this.occupied = occupied;
        this.createdOn = createdOn;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @XmlTransient
    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parkingspace)) {
            return false;
        }
        Parkingspace other = (Parkingspace) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ParkingSystem.Parkingspace[ id=" + id + " ]";
    }
    
}
