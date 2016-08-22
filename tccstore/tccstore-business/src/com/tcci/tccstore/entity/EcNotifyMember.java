/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import java.io.Serializable;
import java.util.Date;
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

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_NOTIFY_MEMBER")
@NamedQueries({
    @NamedQuery(name = "EcNotifyMember.findAll", query = "SELECT e FROM EcNotifyMember e"), 
    @NamedQuery(name = "EcNotifyMember.findLastDays", query = "SELECT e FROM EcNotifyMember e WHERE e.ecMember=:member AND e.ecNotify.createtime>=:createtime ORDER BY e.ecNotify.createtime DESC")
})
public class EcNotifyMember implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcNotifyMemberPK ecNotifyMemberPK;
    @Column(name = "READ_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date readTimestamp;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcMember ecMember;
    @JoinColumn(name = "NOTIFY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcNotify ecNotify;

    public EcNotifyMember() {
    }

    public EcNotifyMember(EcNotifyMemberPK ecNotifyMemberPK) {
        this.ecNotifyMemberPK = ecNotifyMemberPK;
    }

    public EcNotifyMember(EcNotifyMemberPK ecNotifyMemberPK, Date readTimestamp) {
        this.ecNotifyMemberPK = ecNotifyMemberPK;
        this.readTimestamp = readTimestamp;
    }

    public EcNotifyMember(long notifyId, long memberId) {
        this.ecNotifyMemberPK = new EcNotifyMemberPK(notifyId, memberId);
    }

    public EcNotifyMemberPK getEcNotifyMemberPK() {
        return ecNotifyMemberPK;
    }

    public void setEcNotifyMemberPK(EcNotifyMemberPK ecNotifyMemberPK) {
        this.ecNotifyMemberPK = ecNotifyMemberPK;
    }

    public Date getReadTimestamp() {
        return readTimestamp;
    }

    public void setReadTimestamp(Date readTimestamp) {
        this.readTimestamp = readTimestamp;
    }

    public EcMember getEcMember() {
        return ecMember;
    }

    public void setEcMember(EcMember ecMember) {
        this.ecMember = ecMember;
    }

    public EcNotify getEcNotify() {
        return ecNotify;
    }

    public void setEcNotify(EcNotify ecNotify) {
        this.ecNotify = ecNotify;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecNotifyMemberPK != null ? ecNotifyMemberPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcNotifyMember)) {
            return false;
        }
        EcNotifyMember other = (EcNotifyMember) object;
        if ((this.ecNotifyMemberPK == null && other.ecNotifyMemberPK != null) || (this.ecNotifyMemberPK != null && !this.ecNotifyMemberPK.equals(other.ecNotifyMemberPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcNotifyMember[ ecNotifyMemberPK=" + ecNotifyMemberPK + " ]";
    }
    
}
