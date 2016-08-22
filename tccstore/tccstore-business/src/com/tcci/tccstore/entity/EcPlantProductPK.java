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
public class EcPlantProductPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "PLANT_ID")
    private long plantId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCT_ID")
    private long productId;

    public EcPlantProductPK() {
    }

    public EcPlantProductPK(long plantId, long productId) {
        this.plantId = plantId;
        this.productId = productId;
    }

    public long getPlantId() {
        return plantId;
    }

    public void setPlantId(long plantId) {
        this.plantId = plantId;
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
        hash += (int) plantId;
        hash += (int) productId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcPlantProductPK)) {
            return false;
        }
        EcPlantProductPK other = (EcPlantProductPK) object;
        if (this.plantId != other.plantId) {
            return false;
        }
        if (this.productId != other.productId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcPlantProductPK[ plantId=" + plantId + ", productId=" + productId + " ]";
    }
    
}
