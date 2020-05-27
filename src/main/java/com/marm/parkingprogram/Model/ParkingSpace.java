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
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author anr10
 */
@Entity
@Table(name = "ParkingSpace", catalog = "ParkingCompany", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ID"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParkingSpace.findAll", query = "SELECT p FROM ParkingSpace p"),
    @NamedQuery(name = "ParkingSpace.findById", query = "SELECT p FROM ParkingSpace p WHERE p.id = :id"),
    @NamedQuery(name = "ParkingSpace.findByCreatedOn", query = "SELECT p FROM ParkingSpace p WHERE p.createdOn = :createdOn"),
    @NamedQuery(name = "ParkingSpace.findByStatus", query = "SELECT p FROM ParkingSpace p WHERE p.status = :status"),
    @NamedQuery(name = "ParkingSpace.findByLevel", query = "SELECT p FROM ParkingSpace p WHERE p.level = :level")})
public class ParkingSpace implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "CreatedOn", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "Status", nullable = false)
    private boolean status;
    @Basic(optional = false)
    @Column(name = "Level", nullable = false)
    private int level;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parkingSpace")
    private List<Operation> operationList;

    public ParkingSpace() {
    }

    public ParkingSpace(Integer id) {
        this.id = id;
    }

    public ParkingSpace(Integer id, Date createdOn, boolean status, int level) {
        this.id = id;
        this.createdOn = createdOn;
        this.status = status;
        this.level = level;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
        if (!(object instanceof ParkingSpace)) {
            return false;
        }
        ParkingSpace other = (ParkingSpace) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.marm.parkingprogram.Model.ParkingSpace[ id=" + id + " ]";
    }
    
}
