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
@Table(name = "EC_PLANT_SALESAREA")
@NamedQueries({
    @NamedQuery(name = "EcPlantSalesarea.findAll", query = "SELECT e FROM EcPlantSalesarea e ORDER BY e.ecPlant.code, e.ecSalesarea.code")})
public class EcPlantSalesarea implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcPlantSalesareaPK ecPlantSalesareaPK;
    @Size(max = 20)
    @Column(name = "DUMMY")
    private String dummy;
    @JoinColumn(name = "SALESAREA_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcSalesarea ecSalesarea;
    @JoinColumn(name = "PLANT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcPlant ecPlant;

    public EcPlantSalesarea() {
    }

    public EcPlantSalesarea(EcPlantSalesareaPK ecPlantSalesareaPK) {
        this.ecPlantSalesareaPK = ecPlantSalesareaPK;
    }

    public EcPlantSalesarea(long plantId, long salesareaId) {
        this.ecPlantSalesareaPK = new EcPlantSalesareaPK(plantId, salesareaId);
    }

    public EcPlantSalesareaPK getEcPlantSalesareaPK() {
        return ecPlantSalesareaPK;
    }

    public void setEcPlantSalesareaPK(EcPlantSalesareaPK ecPlantSalesareaPK) {
        this.ecPlantSalesareaPK = ecPlantSalesareaPK;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public EcSalesarea getEcSalesarea() {
        return ecSalesarea;
    }

    public void setEcSalesarea(EcSalesarea ecSalesarea) {
        this.ecSalesarea = ecSalesarea;
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
        hash += (ecPlantSalesareaPK != null ? ecPlantSalesareaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcPlantSalesarea)) {
            return false;
        }
        EcPlantSalesarea other = (EcPlantSalesarea) object;
        if ((this.ecPlantSalesareaPK == null && other.ecPlantSalesareaPK != null) || (this.ecPlantSalesareaPK != null && !this.ecPlantSalesareaPK.equals(other.ecPlantSalesareaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcPlantSalesarea[ ecPlantSalesareaPK=" + ecPlantSalesareaPK + " ]";
    }
    
}
