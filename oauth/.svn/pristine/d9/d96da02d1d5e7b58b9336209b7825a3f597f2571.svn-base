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
@Table(name = "EC_PRODUCT")
@NamedQueries({
    @NamedQuery(name = "EcProduct.findAll", query = "SELECT e FROM EcProduct e"),
    @NamedQuery(name = "EcProduct.findByCode", query = "SELECT e FROM EcProduct e WHERE e.code=:code")
})
public class EcProduct implements Serializable {
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
    @Size(min = 1, max = 18)
    @Column(name = "CODE")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "NAME")
    private String name;
    @Size(max = 500)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE")
    private boolean active;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ecProduct")
//    private List<EcPartnerProduct> ecPartnerProductList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ecProduct")
//    private List<EcPlantProduct> ecPlantProductList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ecProduct")
//    private List<EcContractProduct> ecContractProductList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ecProduct")
//    private List<EcBannerProduct> ecBannerProductList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
//    private List<EcOrder> ecOrderList;

    public EcProduct() {
    }

    public EcProduct(Long id) {
        this.id = id;
    }

    public EcProduct(Long id, String code, String name, boolean active) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

//    public List<EcPartnerProduct> getEcPartnerProductList() {
//        return ecPartnerProductList;
//    }
//
//    public void setEcPartnerProductList(List<EcPartnerProduct> ecPartnerProductList) {
//        this.ecPartnerProductList = ecPartnerProductList;
//    }
//
//    public List<EcPlantProduct> getEcPlantProductList() {
//        return ecPlantProductList;
//    }
//
//    public void setEcPlantProductList(List<EcPlantProduct> ecPlantProductList) {
//        this.ecPlantProductList = ecPlantProductList;
//    }
//
//    public List<EcContractProduct> getEcContractProductList() {
//        return ecContractProductList;
//    }
//
//    public void setEcContractProductList(List<EcContractProduct> ecContractProductList) {
//        this.ecContractProductList = ecContractProductList;
//    }
//
//    public List<EcBannerProduct> getEcBannerProductList() {
//        return ecBannerProductList;
//    }
//
//    public void setEcBannerProductList(List<EcBannerProduct> ecBannerProductList) {
//        this.ecBannerProductList = ecBannerProductList;
//    }
//
//    public List<EcOrder> getEcOrderList() {
//        return ecOrderList;
//    }
//
//    public void setEcOrderList(List<EcOrder> ecOrderList) {
//        this.ecOrderList = ecOrderList;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcProduct)) {
            return false;
        }
        EcProduct other = (EcProduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcProduct[ id=" + id + " ]";
    }
    
}
