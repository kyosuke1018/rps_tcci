/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_DELIVERY_VKORG")
@NamedQueries({
    @NamedQuery(name = "EcDeliveryVkorg.findAll", query = "SELECT e FROM EcDeliveryVkorg e"),
    @NamedQuery(name = "EcDeliveryVkorg.findByDelivery", query = "SELECT e FROM EcDeliveryVkorg e WHERE e.ecDeliveryPlace=:delivery")
})
public class EcDeliveryVkorg implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcDeliveryVkorgPK pk;
    @JoinColumn(name = "DELIVERY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcDeliveryPlace ecDeliveryPlace;
    @JoinColumn(name = "SALESAREA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcSalesarea ecSalesarea;

    public EcDeliveryVkorg() {
    }

    public EcDeliveryVkorg(EcDeliveryVkorgPK ecDeliveryVkorgPK) {
        this.pk = ecDeliveryVkorgPK;
    }

    public EcDeliveryVkorg(long deliveryId, String vkorg) {
        this.pk = new EcDeliveryVkorgPK(deliveryId, vkorg);
    }

    public EcDeliveryVkorgPK getPk() {
        return pk;
    }

    public void setPk(EcDeliveryVkorgPK pk) {
        this.pk = pk;
    }

    public EcDeliveryPlace getEcDeliveryPlace() {
        return ecDeliveryPlace;
    }

    public void setEcDeliveryPlace(EcDeliveryPlace ecDeliveryPlace) {
        this.ecDeliveryPlace = ecDeliveryPlace;
    }

    public EcSalesarea getEcSalesarea() {
        return ecSalesarea;
    }

    public void setEcSalesarea(EcSalesarea ecSalesarea) {
        this.ecSalesarea = ecSalesarea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pk != null ? pk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcDeliveryVkorg)) {
            return false;
        }
        EcDeliveryVkorg other = (EcDeliveryVkorg) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcDeliveryVkorg[ ecDeliveryVkorgPK=" + pk + " ]";
    }
    
}
