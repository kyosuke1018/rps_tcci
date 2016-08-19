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
@Table(name = "EC_CUSTOMER")
@NamedQueries({
    @NamedQuery(name = "EcCustomer.findAll", query = "SELECT e FROM EcCustomer e")})
public class EcCustomer implements Serializable {
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
    @Size(min = 1, max = 64)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE")
    private boolean active;
    @JoinTable(name = "EC_CUSTOMER_SALES", joinColumns = {
        @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "SALES_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EcSales> ecSalesList;
    @ManyToMany(mappedBy = "ecCustomerList")
    private List<EcMember> ecMemberList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private List<EcContract> ecContractList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private List<EcOrder> ecOrderList;

    public EcCustomer() {
    }

    public EcCustomer(Long id) {
        this.id = id;
    }

    public EcCustomer(Long id, String code, String name, boolean active) {
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

    public List<EcSales> getEcSalesList() {
        return ecSalesList;
    }

    public void setEcSalesList(List<EcSales> ecSalesList) {
        this.ecSalesList = ecSalesList;
    }

    public List<EcMember> getEcMemberList() {
        return ecMemberList;
    }

    public void setEcMemberList(List<EcMember> ecMemberList) {
        this.ecMemberList = ecMemberList;
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
        if (!(object instanceof EcCustomer)) {
            return false;
        }
        EcCustomer other = (EcCustomer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ecdemo.entity.EcCustomer[ id=" + id + " ]";
    }
    
}
