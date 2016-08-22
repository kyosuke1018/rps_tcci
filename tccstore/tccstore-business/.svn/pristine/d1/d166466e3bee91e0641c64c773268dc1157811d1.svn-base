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
@Table(name = "EC_BANNER_PRODUCT")
@NamedQueries({
    @NamedQuery(name = "EcBannerProduct.findAll", query = "SELECT e FROM EcBannerProduct e")})
public class EcBannerProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcBannerProductPK ecBannerProductPK;
    @Size(max = 20)
    @Column(name = "DUMMY")
    private String dummy;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcProduct ecProduct;
    @JoinColumn(name = "BANNER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcBanner ecBanner;

    public EcBannerProduct() {
    }

    public EcBannerProduct(EcBannerProductPK ecBannerProductPK) {
        this.ecBannerProductPK = ecBannerProductPK;
    }

    public EcBannerProduct(long bannerId, long productId) {
        this.ecBannerProductPK = new EcBannerProductPK(bannerId, productId);
    }

    public EcBannerProductPK getEcBannerProductPK() {
        return ecBannerProductPK;
    }

    public void setEcBannerProductPK(EcBannerProductPK ecBannerProductPK) {
        this.ecBannerProductPK = ecBannerProductPK;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public EcProduct getEcProduct() {
        return ecProduct;
    }

    public void setEcProduct(EcProduct ecProduct) {
        this.ecProduct = ecProduct;
    }

    public EcBanner getEcBanner() {
        return ecBanner;
    }

    public void setEcBanner(EcBanner ecBanner) {
        this.ecBanner = ecBanner;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecBannerProductPK != null ? ecBannerProductPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcBannerProduct)) {
            return false;
        }
        EcBannerProduct other = (EcBannerProduct) object;
        if ((this.ecBannerProductPK == null && other.ecBannerProductPK != null) || (this.ecBannerProductPK != null && !this.ecBannerProductPK.equals(other.ecBannerProductPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcBannerProduct[ ecBannerProductPK=" + ecBannerProductPK + " ]";
    }
    
}
