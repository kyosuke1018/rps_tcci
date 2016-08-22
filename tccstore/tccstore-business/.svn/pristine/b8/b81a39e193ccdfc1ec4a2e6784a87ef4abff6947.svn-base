/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_DELIVERY_PLACE")
@NamedQueries({
    @NamedQuery(name = "EcDeliveryPlace.findAll", query = "SELECT e FROM EcDeliveryPlace e"),
    @NamedQuery(name = "EcDeliveryPlace.findByCode", query = "SELECT e FROM EcDeliveryPlace e WHERE UPPER(e.code)=UPPER(:code)")
})
public class EcDeliveryPlace implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_EC",sequenceName = "SEQ_EC", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_EC")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CODE")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NAME")
    private String name;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE")
    private boolean active;
    @JoinColumn(name = "SALESAREA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcSalesarea ecSalesarea;

    public EcDeliveryPlace() {
    }

    public EcDeliveryPlace(Long id) {
        this.id = id;
    }

    public EcDeliveryPlace(Long id, String code, String name, boolean active) {
        this.id = id;
        this.code = code;
        this.name = name;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public EcSalesarea getEcSalesarea() {
        return ecSalesarea;
    }

    public void setEcSalesarea(EcSalesarea ecSalesarea) {
        this.ecSalesarea = ecSalesarea;
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
        if (!(object instanceof EcDeliveryPlace)) {
            return false;
        }
        EcDeliveryPlace other = (EcDeliveryPlace) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcDeliveryPlace[ id=" + id + " ]";
    }
    
}
