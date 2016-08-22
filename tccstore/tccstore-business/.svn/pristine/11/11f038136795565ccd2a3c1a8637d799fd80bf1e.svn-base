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
public class EcPlantSalesareaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "PLANT_ID")
    private long plantId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALESAREA_ID")
    private long salesareaId;

    public EcPlantSalesareaPK() {
    }

    public EcPlantSalesareaPK(long plantId, long salesareaId) {
        this.plantId = plantId;
        this.salesareaId = salesareaId;
    }

    public long getPlantId() {
        return plantId;
    }

    public void setPlantId(long plantId) {
        this.plantId = plantId;
    }

    public long getSalesareaId() {
        return salesareaId;
    }

    public void setSalesareaId(long salesareaId) {
        this.salesareaId = salesareaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) plantId;
        hash += (int) salesareaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcPlantSalesareaPK)) {
            return false;
        }
        EcPlantSalesareaPK other = (EcPlantSalesareaPK) object;
        if (this.plantId != other.plantId) {
            return false;
        }
        if (this.salesareaId != other.salesareaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcPlantSalesareaPK[ plantId=" + plantId + ", salesareaId=" + salesareaId + " ]";
    }
    
}
