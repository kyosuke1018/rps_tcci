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
public class EcMemberPartnerPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "MEMBER_ID")
    private long memberId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PARTNER_ID")
    private long partnerId;

    public EcMemberPartnerPK() {
    }

    public EcMemberPartnerPK(long memberId, long partnerId) {
        this.memberId = memberId;
        this.partnerId = partnerId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) memberId;
        hash += (int) partnerId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcMemberPartnerPK)) {
            return false;
        }
        EcMemberPartnerPK other = (EcMemberPartnerPK) object;
        if (this.memberId != other.memberId) {
            return false;
        }
        if (this.partnerId != other.partnerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcMemberPartnerPK[ memberId=" + memberId + ", partnerId=" + partnerId + " ]";
    }
    
}
