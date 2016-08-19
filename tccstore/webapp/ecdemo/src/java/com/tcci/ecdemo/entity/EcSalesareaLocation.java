/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_SALESAREA_LOCATION")
@NamedQueries({
    @NamedQuery(name = "EcSalesareaLocation.findAll", query = "SELECT e FROM EcSalesareaLocation e")})
public class EcSalesareaLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "PROVINCE")
    private String province;
    @Size(max = 20)
    @Column(name = "CITY")
    private String city;
    @Size(max = 20)
    @Column(name = "DISTRICT")
    private String district;
    @Size(max = 20)
    @Column(name = "TOWN")
    private String town;
    @JoinColumn(name = "SALESAREA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcSalesarea salesareaId;

    public EcSalesareaLocation() {
    }

    public EcSalesareaLocation(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public EcSalesarea getSalesareaId() {
        return salesareaId;
    }

    public void setSalesareaId(EcSalesarea salesareaId) {
        this.salesareaId = salesareaId;
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
        if (!(object instanceof EcSalesareaLocation)) {
            return false;
        }
        EcSalesareaLocation other = (EcSalesareaLocation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ecdemo.entity.EcSalesareaLocation[ id=" + id + " ]";
    }
    
}
