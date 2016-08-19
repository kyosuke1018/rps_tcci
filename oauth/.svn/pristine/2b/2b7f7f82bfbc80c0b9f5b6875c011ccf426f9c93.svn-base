/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Jimmy.Lee
 */
@Embeddable
public class EcCustomerSalesPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTOMER_ID")
    private long customerId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALES_ID")
    private long salesId;

    public EcCustomerSalesPK() {
    }

    public EcCustomerSalesPK(long customerId, long salesId) {
        this.customerId = customerId;
        this.salesId = salesId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getSalesId() {
        return salesId;
    }

    public void setSalesId(long salesId) {
        this.salesId = salesId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) customerId;
        hash += (int) salesId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcCustomerSalesPK)) {
            return false;
        }
        EcCustomerSalesPK other = (EcCustomerSalesPK) object;
        if (this.customerId != other.customerId) {
            return false;
        }
        if (this.salesId != other.salesId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcCustomerSalesPK[ customerId=" + customerId + ", salesId=" + salesId + " ]";
    }
    
}
