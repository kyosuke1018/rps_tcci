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
public class EcBannerProductPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "BANNER_ID")
    private long bannerId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCT_ID")
    private long productId;

    public EcBannerProductPK() {
    }

    public EcBannerProductPK(long bannerId, long productId) {
        this.bannerId = bannerId;
        this.productId = productId;
    }

    public long getBannerId() {
        return bannerId;
    }

    public void setBannerId(long bannerId) {
        this.bannerId = bannerId;
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
        hash += (int) bannerId;
        hash += (int) productId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcBannerProductPK)) {
            return false;
        }
        EcBannerProductPK other = (EcBannerProductPK) object;
        if (this.bannerId != other.bannerId) {
            return false;
        }
        if (this.productId != other.productId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcBannerProductPK[ bannerId=" + bannerId + ", productId=" + productId + " ]";
    }
    
}
