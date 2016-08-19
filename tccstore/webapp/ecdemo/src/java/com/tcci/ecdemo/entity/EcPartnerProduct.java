/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_PARTNER_PRODUCT")
@NamedQueries({
    @NamedQuery(name = "EcPartnerProduct.findAll", query = "SELECT e FROM EcPartnerProduct e")})
public class EcPartnerProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcPartnerProductPK ecPartnerProductPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UNIT_PRICE")
    private long unitPrice;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcProduct ecProduct;
    @JoinColumn(name = "PARTNER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcPartner ecPartner;

    public EcPartnerProduct() {
    }

    public EcPartnerProduct(EcPartnerProductPK ecPartnerProductPK) {
        this.ecPartnerProductPK = ecPartnerProductPK;
    }

    public EcPartnerProduct(EcPartnerProductPK ecPartnerProductPK, long unitPrice) {
        this.ecPartnerProductPK = ecPartnerProductPK;
        this.unitPrice = unitPrice;
    }

    public EcPartnerProduct(long partnerId, long productId) {
        this.ecPartnerProductPK = new EcPartnerProductPK(partnerId, productId);
    }

    public EcPartnerProductPK getEcPartnerProductPK() {
        return ecPartnerProductPK;
    }

    public void setEcPartnerProductPK(EcPartnerProductPK ecPartnerProductPK) {
        this.ecPartnerProductPK = ecPartnerProductPK;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public EcProduct getEcProduct() {
        return ecProduct;
    }

    public void setEcProduct(EcProduct ecProduct) {
        this.ecProduct = ecProduct;
    }

    public EcPartner getEcPartner() {
        return ecPartner;
    }

    public void setEcPartner(EcPartner ecPartner) {
        this.ecPartner = ecPartner;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecPartnerProductPK != null ? ecPartnerProductPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcPartnerProduct)) {
            return false;
        }
        EcPartnerProduct other = (EcPartnerProduct) object;
        if ((this.ecPartnerProductPK == null && other.ecPartnerProductPK != null) || (this.ecPartnerProductPK != null && !this.ecPartnerProductPK.equals(other.ecPartnerProductPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ecdemo.entity.EcPartnerProduct[ ecPartnerProductPK=" + ecPartnerProductPK + " ]";
    }
    
}
