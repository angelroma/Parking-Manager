/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marm.parkingprogram.Model;

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
@Table(name = "Vehicle", catalog = "ParkingCompany", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vehicle.findAll", query = "SELECT v FROM Vehicle v"),
    @NamedQuery(name = "Vehicle.findById", query = "SELECT v FROM Vehicle v WHERE v.id = :id"),
    @NamedQuery(name = "Vehicle.findByColor", query = "SELECT v FROM Vehicle v WHERE v.color = :color"),
    @NamedQuery(name = "Vehicle.findByBrand", query = "SELECT v FROM Vehicle v WHERE v.brand = :brand"),
    @NamedQuery(name = "Vehicle.findByModel", query = "SELECT v FROM Vehicle v WHERE v.model = :model"),
    @NamedQuery(name = "Vehicle.findByCarriagePlate", query = "SELECT v FROM Vehicle v WHERE v.carriagePlate = :carriagePlate"),
    @NamedQuery(name = "Vehicle.findByCreatedOn", query = "SELECT v FROM Vehicle v WHERE v.createdOn = :createdOn"),
    @NamedQuery(name = "Vehicle.findByDelivered", query = "SELECT v FROM Vehicle v WHERE v.delivered = :delivered")})
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Color", nullable = false, length = 45)
    private String color;
    @Basic(optional = false)
    @Column(name = "Brand", nullable = false, length = 45)
    private String brand;
    @Basic(optional = false)
    @Column(name = "Model", nullable = false, length = 45)
    private String model;
    @Basic(optional = false)
    @Column(name = "CarriagePlate", nullable = false, length = 45)
    private String carriagePlate;
    @Basic(optional = false)
    @Column(name = "CreatedOn", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "Delivered", nullable = false)
    private short delivered;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehicle")
    private List<Operation> operationList;

    public Vehicle() {
    }

    public Vehicle(Integer id) {
        this.id = id;
    }

    public Vehicle(Integer id, String color, String brand, String model, String carriagePlate, Date createdOn, short delivered) {
        this.id = id;
        this.color = color;
        this.brand = brand;
        this.model = model;
        this.carriagePlate = carriagePlate;
        this.createdOn = createdOn;
        this.delivered = delivered;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCarriagePlate() {
        return carriagePlate;
    }

    public void setCarriagePlate(String carriagePlate) {
        this.carriagePlate = carriagePlate;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public short getDelivered() {
        return delivered;
    }

    public void setDelivered(short delivered) {
        this.delivered = delivered;
    }

    @XmlTransient
    public List<Operation> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
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
        if (!(object instanceof Vehicle)) {
            return false;
        }
        Vehicle other = (Vehicle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.marm.parkingprogram.Model.Vehicle[ id=" + id + " ]";
    }
    
}
