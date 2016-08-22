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
public class EcRewardPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "MEMBER_ID")
    private long memberId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TYPE")
    private int type;

    public EcRewardPK() {
    }

    public EcRewardPK(long memberId, int type) {
        this.memberId = memberId;
        this.type = type;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) memberId;
        hash += (int) type;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcRewardPK)) {
            return false;
        }
        EcRewardPK other = (EcRewardPK) object;
        if (this.memberId != other.memberId) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcRewardPK[ memberId=" + memberId + ", type=" + type + " ]";
    }
    
}
