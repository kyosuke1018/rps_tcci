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
public class EcNotifyMemberPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "NOTIFY_ID")
    private long notifyId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MEMBER_ID")
    private long memberId;

    public EcNotifyMemberPK() {
    }

    public EcNotifyMemberPK(long notifyId, long memberId) {
        this.notifyId = notifyId;
        this.memberId = memberId;
    }

    public long getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(long notifyId) {
        this.notifyId = notifyId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) notifyId;
        hash += (int) memberId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcNotifyMemberPK)) {
            return false;
        }
        EcNotifyMemberPK other = (EcNotifyMemberPK) object;
        if (this.notifyId != other.notifyId) {
            return false;
        }
        if (this.memberId != other.memberId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcNotifyMemberPK[ notifyId=" + notifyId + ", memberId=" + memberId + " ]";
    }
    
}
