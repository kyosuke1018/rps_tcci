/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_CUSTOMER_VKORG")
@NamedQueries({
    @NamedQuery(name = "EcCustomerVkorg.findAll", query = "SELECT e FROM EcCustomerVkorg e")})
public class EcCustomerVkorg implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcCustomerVkorgPK ecCustomerVkorgPK;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcCustomer ecCustomer;

    public EcCustomerVkorg() {
    }

    public EcCustomerVkorg(EcCustomerVkorgPK ecCustomerVkorgPK) {
        this.ecCustomerVkorgPK = ecCustomerVkorgPK;
    }

    public EcCustomerVkorg(long customerId, String vkorg) {
        this.ecCustomerVkorgPK = new EcCustomerVkorgPK(customerId, vkorg);
    }

    public EcCustomerVkorgPK getEcCustomerVkorgPK() {
        return ecCustomerVkorgPK;
    }

    public void setEcCustomerVkorgPK(EcCustomerVkorgPK ecCustomerVkorgPK) {
        this.ecCustomerVkorgPK = ecCustomerVkorgPK;
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
        hash += (ecCustomerVkorgPK != null ? ecCustomerVkorgPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcCustomerVkorg)) {
            return false;
        }
        EcCustomerVkorg other = (EcCustomerVkorg) object;
        if ((this.ecCustomerVkorgPK == null && other.ecCustomerVkorgPK != null) || (this.ecCustomerVkorgPK != null && !this.ecCustomerVkorgPK.equals(other.ecCustomerVkorgPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcCustomerVkorg[ ecCustomerVkorgPK=" + ecCustomerVkorgPK + " ]";
    }
    
}
