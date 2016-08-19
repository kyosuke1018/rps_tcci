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
public class EcPartnerGoodsPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "PARTNER_ID")
    private long partnerId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GOODS_ID")
    private long goodsId;

    public EcPartnerGoodsPK() {
    }

    public EcPartnerGoodsPK(long partnerId, long goodsId) {
        this.partnerId = partnerId;
        this.goodsId = goodsId;
    }

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) partnerId;
        hash += (int) goodsId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcPartnerGoodsPK)) {
            return false;
        }
        EcPartnerGoodsPK other = (EcPartnerGoodsPK) object;
        if (this.partnerId != other.partnerId) {
            return false;
        }
        if (this.goodsId != other.goodsId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcPartnerGoodsPK[ partnerId=" + partnerId + ", goodsId=" + goodsId + " ]";
    }
    
}
