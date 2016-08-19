/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_REWARD")
@NamedQueries({
    @NamedQuery(name = "EcReward.findAll", query = "SELECT e FROM EcReward e"),
    @NamedQuery(name = "EcReward.findByMember", query = "SELECT e FROM EcReward e WHERE e.ecMember=:ecMember"),
})
public class EcReward implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int TYPE_BONUS = 1;
    public static final int TYPE_GOLD = 2;
    
    @EmbeddedId
    protected EcRewardPK ecRewardPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "POINTS")
    private int points;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcMember ecMember;

    public EcReward() {
    }

    public EcReward(EcRewardPK ecRewardPK, EcMember ecMember) {
        this.ecRewardPK = ecRewardPK;
        this.ecMember = ecMember;
    }

    public EcReward(EcRewardPK ecRewardPK, int points, Date modifytime) {
        this.ecRewardPK = ecRewardPK;
        this.points = points;
        this.modifytime = modifytime;
    }

    public EcReward(long memberId, int type) {
        this.ecRewardPK = new EcRewardPK(memberId, type);
    }

    public EcRewardPK getEcRewardPK() {
        return ecRewardPK;
    }

    public void setEcRewardPK(EcRewardPK ecRewardPK) {
        this.ecRewardPK = ecRewardPK;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public EcMember getEcMember() {
        return ecMember;
    }

    public void setEcMember(EcMember ecMember) {
        this.ecMember = ecMember;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecRewardPK != null ? ecRewardPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcReward)) {
            return false;
        }
        EcReward other = (EcReward) object;
        if ((this.ecRewardPK == null && other.ecRewardPK != null) || (this.ecRewardPK != null && !this.ecRewardPK.equals(other.ecRewardPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcReward[ ecRewardPK=" + ecRewardPK + " ]";
    }
    
}
