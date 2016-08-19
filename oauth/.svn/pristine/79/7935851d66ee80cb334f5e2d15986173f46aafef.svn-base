/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_CUSTOMER_SALES")
@NamedQueries({
    @NamedQuery(name = "EcCustomerSales.findAll", query = "SELECT e FROM EcCustomerSales e"),
    @NamedQuery(name = "EcCustomerSales.findByCustomer", query = "SELECT e FROM EcCustomerSales e WHERE e.ecCustomer=:ecCustomer")
})
public class EcCustomerSales implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcCustomerSalesPK ecCustomerSalesPK;
    @Size(max = 20)
    @Column(name = "DUMMY")
    private String dummy;
    @JoinColumn(name = "SALES_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcSales ecSales;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcCustomer ecCustomer;

    public EcCustomerSales() {
    }

    public EcCustomerSales(EcCustomer ecCustomer, EcSales ecSales) {
        this.ecCustomerSalesPK = new EcCustomerSalesPK(ecCustomer.getId(), ecSales.getId());
        this.ecCustomer = ecCustomer;
        this.ecSales = ecSales;
    }

    public EcCustomerSales(EcCustomerSalesPK ecCustomerSalesPK) {
        this.ecCustomerSalesPK = ecCustomerSalesPK;
    }

    public EcCustomerSales(long customerId, long salesId) {
        this.ecCustomerSalesPK = new EcCustomerSalesPK(customerId, salesId);
    }

    public EcCustomerSalesPK getEcCustomerSalesPK() {
        return ecCustomerSalesPK;
    }

    public void setEcCustomerSalesPK(EcCustomerSalesPK ecCustomerSalesPK) {
        this.ecCustomerSalesPK = ecCustomerSalesPK;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public EcSales getEcSales() {
        return ecSales;
    }

    public void setEcSales(EcSales ecSales) {
        this.ecSales = ecSales;
    }

    public EcCustomer getEcCustomer() {
        return ecCustomer;
    }

    public void setEcCustomer(EcCustomer ecCustomer) {
        this.ecCustomer = ecCustomer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecCustomerSalesPK != null ? ecCustomerSalesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcCustomerSales)) {
            return false;
        }
        EcCustomerSales other = (EcCustomerSales) object;
        if ((this.ecCustomerSalesPK == null && other.ecCustomerSalesPK != null) || (this.ecCustomerSalesPK != null && !this.ecCustomerSalesPK.equals(other.ecCustomerSalesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcCustomerSales[ ecCustomerSalesPK=" + ecCustomerSalesPK + " ]";
    }
    
}
