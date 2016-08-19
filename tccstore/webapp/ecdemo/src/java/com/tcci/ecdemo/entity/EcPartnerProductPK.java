/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.entity;

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
public class EcPartnerProductPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "PARTNER_ID")
    private long partnerId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCT_ID")
    private long productId;

    public EcPartnerProductPK() {
    }

    public EcPartnerProductPK(long partnerId, long productId) {
        this.partnerId = partnerId;
        this.productId = productId;
    }

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) partnerId;
        hash += (int) productId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcPartnerProductPK)) {
            return false;
        }
        EcPartnerProductPK other = (EcPartnerProductPK) object;
        if (this.partnerId != other.partnerId) {
            return false;
        }
        if (this.productId != other.productId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ecdemo.entity.EcPartnerProductPK[ partnerId=" + partnerId + ", productId=" + productId + " ]";
    }
    
}
