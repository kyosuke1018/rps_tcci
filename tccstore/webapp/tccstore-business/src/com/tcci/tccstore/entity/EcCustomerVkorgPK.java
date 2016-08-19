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
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Embeddable
public class EcCustomerVkorgPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTOMER_ID")
    private long customerId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "VKORG")
    private String vkorg;

    public EcCustomerVkorgPK() {
    }

    public EcCustomerVkorgPK(long customerId, String vkorg) {
        this.customerId = customerId;
        this.vkorg = vkorg;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) customerId;
        hash += (vkorg != null ? vkorg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcCustomerVkorgPK)) {
            return false;
        }
        EcCustomerVkorgPK other = (EcCustomerVkorgPK) object;
        if (this.customerId != other.customerId) {
            return false;
        }
        if ((this.vkorg == null && other.vkorg != null) || (this.vkorg != null && !this.vkorg.equals(other.vkorg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcCustomerVkorgPK[ customerId=" + customerId + ", vkorg=" + vkorg + " ]";
    }
    
}
