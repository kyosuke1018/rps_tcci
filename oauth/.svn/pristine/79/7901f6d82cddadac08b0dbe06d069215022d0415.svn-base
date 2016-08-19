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
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Embeddable
public class EcDeliveryVkorgPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "DELIVERY_ID")
    private long deliveryId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "VKORG")
    private String vkorg;

    public EcDeliveryVkorgPK() {
    }

    public EcDeliveryVkorgPK(long deliveryId, String vkorg) {
        this.deliveryId = deliveryId;
        this.vkorg = vkorg;
    }

    public long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) deliveryId;
        hash += (vkorg != null ? vkorg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcDeliveryVkorgPK)) {
            return false;
        }
        EcDeliveryVkorgPK other = (EcDeliveryVkorgPK) object;
        if (this.deliveryId != other.deliveryId) {
            return false;
        }
        if ((this.vkorg == null && other.vkorg != null) || (this.vkorg != null && !this.vkorg.equals(other.vkorg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcDeliveryVkorgPK[ deliveryId=" + deliveryId + ", vkorg=" + vkorg + " ]";
    }
    
}
