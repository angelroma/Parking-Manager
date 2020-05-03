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
@Table(name = "finance")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Finance.findAll", query = "SELECT f FROM Finance f"),
    @NamedQuery(name = "Finance.findById", query = "SELECT f FROM Finance f WHERE f.id = :id"),
    @NamedQuery(name = "Finance.findByPrice", query = "SELECT f FROM Finance f WHERE f.price = :price"),
    @NamedQuery(name = "Finance.findByRange", query = "SELECT f FROM Finance f WHERE f.range = :range"),
    @NamedQuery(name = "Finance.findByCreatedOn", query = "SELECT f FROM Finance f WHERE f.createdOn = :createdOn")})
public class Finance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Price")
    private float price;
    @Basic(optional = false)
    @Column(name = "Range")
    private float range;
    @Column(name = "CreatedOn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "financeID")
    private List<Vehicle> vehicleList;

    public Finance() {
    }

    public Finance(Integer id) {
        this.id = id;
    }

    public Finance(Integer id, float price, float range) {
        this.id = id;
        this.price = price;
        this.range = range;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
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
        if (!(object instanceof Finance)) {
            return false;
        }
        Finance other = (Finance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ParkingSystem.Finance[ id=" + id + " ]";
    }
    
}
