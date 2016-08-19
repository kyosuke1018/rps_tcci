/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

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
@Table(name = "EC_PLANT_PRODUCT")
@NamedQueries({
    @NamedQuery(name = "EcPlantProduct.findAll", query = "SELECT e FROM EcPlantProduct e"),
    @NamedQuery(name = "EcPlantProduct.findByPlant", 
            query = "SELECT e.ecProduct FROM EcPlantProduct e"
                    + " WHERE e.active=TRUE"
                    + " AND e.ecPlant.active=TRUE"
                    + " AND e.ecProduct.active=TRUE"
                    + " AND e.ecPlant=:ecPlant"
                    + " ORDER BY e.ecProduct.name")
})
public class EcPlantProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcPlantProductPK ecPlantProductPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE")
    private boolean active;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcProduct ecProduct;
    @JoinColumn(name = "PLANT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcPlant ecPlant;

    public EcPlantProduct() {
    }

    public EcPlantProduct(EcPlantProductPK ecPlantProductPK) {
        this.ecPlantProductPK = ecPlantProductPK;
    }

    public EcPlantProduct(long plantId, long productId) {
        this.ecPlantProductPK = new EcPlantProductPK(plantId, productId);
    }

    public EcPlantProductPK getEcPlantProductPK() {
        return ecPlantProductPK;
    }

    public void setEcPlantProductPK(EcPlantProductPK ecPlantProductPK) {
        this.ecPlantProductPK = ecPlantProductPK;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public EcProduct getEcProduct() {
        return ecProduct;
    }

    public void setEcProduct(EcProduct ecProduct) {
        this.ecProduct = ecProduct;
    }

    public EcPlant getEcPlant() {
        return ecPlant;
    }

    public void setEcPlant(EcPlant ecPlant) {
        this.ecPlant = ecPlant;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecPlantProductPK != null ? ecPlantProductPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcPlantProduct)) {
            return false;
        }
        EcPlantProduct other = (EcPlantProduct) object;
        if ((this.ecPlantProductPK == null && other.ecPlantProductPK != null) || (this.ecPlantProductPK != null && !this.ecPlantProductPK.equals(other.ecPlantProductPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcPlantProduct[ ecPlantProductPK=" + ecPlantProductPK + " ]";
    }
    
}
