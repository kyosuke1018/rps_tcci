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
@Table(name = "EC_PARTNER_GOODS")
@NamedQueries({
    @NamedQuery(name = "EcPartnerGoods.findAll", query = "SELECT e FROM EcPartnerGoods e"),
    @NamedQuery(name = "EcPartnerGoods.findByPartner", 
            query = "SELECT e.ecGoods FROM EcPartnerGoods e"
                    + " WHERE e.ecPartner=:ecPartner"
                    + " AND e.ecGoods.active=TRUE"
                    + " ORDER BY e.ecGoods.name"),
    @NamedQuery(name = "EcPartnerGoods.findAllPartnerGoods", 
            query = "SELECT DISTINCT e.ecGoods FROM EcPartnerGoods e"
                    + " WHERE e.ecPartner.active=TRUE"
                    + " AND e.ecGoods.active=TRUE"
                    + " ORDER BY e.ecGoods.name")
})
public class EcPartnerGoods implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcPartnerGoodsPK ecPartnerGoodsPK;
    @Size(max = 20)
    @Column(name = "DUMMY")
    private String dummy;
    @JoinColumn(name = "GOODS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcGoods ecGoods;
    @JoinColumn(name = "PARTNER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcPartner ecPartner;

    public EcPartnerGoods() {
    }

    public EcPartnerGoods(EcPartnerGoodsPK ecPartnerGoodsPK) {
        this.ecPartnerGoodsPK = ecPartnerGoodsPK;
    }

    public EcPartnerGoods(long partnerId, long goodsId) {
        this.ecPartnerGoodsPK = new EcPartnerGoodsPK(partnerId, goodsId);
    }

    public EcPartnerGoodsPK getEcPartnerGoodsPK() {
        return ecPartnerGoodsPK;
    }

    public void setEcPartnerGoodsPK(EcPartnerGoodsPK ecPartnerGoodsPK) {
        this.ecPartnerGoodsPK = ecPartnerGoodsPK;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public EcGoods getEcGoods() {
        return ecGoods;
    }

    public void setEcGoods(EcGoods ecGoods) {
        this.ecGoods = ecGoods;
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
        hash += (ecPartnerGoodsPK != null ? ecPartnerGoodsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcPartnerGoods)) {
            return false;
        }
        EcPartnerGoods other = (EcPartnerGoods) object;
        if ((this.ecPartnerGoodsPK == null && other.ecPartnerGoodsPK != null) || (this.ecPartnerGoodsPK != null && !this.ecPartnerGoodsPK.equals(other.ecPartnerGoodsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcPartnerGoods[ ecPartnerGoodsPK=" + ecPartnerGoodsPK + " ]";
    }
    
}
