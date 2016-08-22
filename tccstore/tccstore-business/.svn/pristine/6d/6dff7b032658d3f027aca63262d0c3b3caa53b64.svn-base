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
public class EcDeliveryPreferencePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "MEMBER_ID")
    private long memberId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DELIVERY_ID")
    private long deliveryId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PLANT_ID")
    private long plantId;

    public EcDeliveryPreferencePK() {
    }

    public EcDeliveryPreferencePK(long memberId, long deliveryId, long plantId) {
        this.memberId = memberId;
        this.deliveryId = deliveryId;
        this.plantId = plantId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public long getPlantId() {
        return plantId;
    }

    public void setPlantId(long plantId) {
        this.plantId = plantId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) memberId;
        hash += (int) deliveryId;
        hash += (int) plantId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcDeliveryPreferencePK)) {
            return false;
        }
        EcDeliveryPreferencePK other = (EcDeliveryPreferencePK) object;
        if (this.memberId != other.memberId) {
            return false;
        }
        if (this.deliveryId != other.deliveryId) {
            return false;
        }
        if (this.plantId != other.plantId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcDeliveryPreferencePK[ memberId=" + memberId + ", deliveryId=" + deliveryId + ", plantId=" + plantId + " ]";
    }
    
}
