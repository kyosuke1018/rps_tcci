/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_SALESAREA")
@NamedQueries({
    @NamedQuery(name = "EcSalesarea.findAll", query = "SELECT e FROM EcSalesarea e")})
public class EcSalesarea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "CODE")
    private String code;
    @Size(max = 140)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE")
    private boolean active;
    @ManyToMany(mappedBy = "ecSalesareaList")
    private List<EcPlant> ecPlantList;
    @JoinTable(name = "EC_SALESAREA_DELIVERY", joinColumns = {
        @JoinColumn(name = "SALESAREA_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "DELIVERY_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EcDelivery> ecDeliveryList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "salesareaId")
    private List<EcSalesareaLocation> ecSalesareaLocationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "salesareaId")
    private List<EcOrder> ecOrderList;

    public EcSalesarea() {
    }

    public EcSalesarea(Long id) {
        this.id = id;
    }

    public EcSalesarea(Long id, String code, boolean active) {
        this.id = id;
        this.code = code;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<EcPlant> getEcPlantList() {
        return ecPlantList;
    }

    public void setEcPlantList(List<EcPlant> ecPlantList) {
        this.ecPlantList = ecPlantList;
    }

    public List<EcDelivery> getEcDeliveryList() {
        return ecDeliveryList;
    }

    public void setEcDeliveryList(List<EcDelivery> ecDeliveryList) {
        this.ecDeliveryList = ecDeliveryList;
    }

    public List<EcSalesareaLocation> getEcSalesareaLocationList() {
        return ecSalesareaLocationList;
    }

    public void setEcSalesareaLocationList(List<EcSalesareaLocation> ecSalesareaLocationList) {
        this.ecSalesareaLocationList = ecSalesareaLocationList;
    }

    public List<EcOrder> getEcOrderList() {
        return ecOrderList;
    }

    public void setEcOrderList(List<EcOrder> ecOrderList) {
        this.ecOrderList = ecOrderList;
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
        if (!(object instanceof EcSalesarea)) {
            return false;
        }
        EcSalesarea other = (EcSalesarea) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ecdemo.entity.EcSalesarea[ id=" + id + " ]";
    }
    
}
