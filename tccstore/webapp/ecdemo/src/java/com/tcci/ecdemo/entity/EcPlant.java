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
@Table(name = "EC_PLANT")
@NamedQueries({
    @NamedQuery(name = "EcPlant.findAll", query = "SELECT e FROM EcPlant e")})
public class EcPlant implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CODE")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE")
    private boolean active;
    @JoinTable(name = "EC_PLANT_SALESAREA", joinColumns = {
        @JoinColumn(name = "PLANT_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "SALESAREA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EcSalesarea> ecSalesareaList;
    @JoinTable(name = "EC_PLANT_PRODUCT", joinColumns = {
        @JoinColumn(name = "PLANT_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EcProduct> ecProductList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plantId")
    private List<EcContract> ecContractList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plantId")
    private List<EcOrder> ecOrderList;

    public EcPlant() {
    }

    public EcPlant(Long id) {
        this.id = id;
    }

    public EcPlant(Long id, String code, String name, boolean active) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<EcSalesarea> getEcSalesareaList() {
        return ecSalesareaList;
    }

    public void setEcSalesareaList(List<EcSalesarea> ecSalesareaList) {
        this.ecSalesareaList = ecSalesareaList;
    }

    public List<EcProduct> getEcProductList() {
        return ecProductList;
    }

    public void setEcProductList(List<EcProduct> ecProductList) {
        this.ecProductList = ecProductList;
    }

    public List<EcContract> getEcContractList() {
        return ecContractList;
    }

    public void setEcContractList(List<EcContract> ecContractList) {
        this.ecContractList = ecContractList;
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
        if (!(object instanceof EcPlant)) {
            return false;
        }
        EcPlant other = (EcPlant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ecdemo.entity.EcPlant[ id=" + id + " ]";
    }
    
}
