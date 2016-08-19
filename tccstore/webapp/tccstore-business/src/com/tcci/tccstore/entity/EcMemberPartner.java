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
@Table(name = "EC_MEMBER_PARTNER")
@NamedQueries({
    @NamedQuery(name = "EcMemberPartner.findAll", query = "SELECT e FROM EcMemberPartner e"),
    @NamedQuery(name = "EcMemberPartner.findPartnerByMember",
            query = "SELECT e.ecPartner FROM EcMemberPartner e"
            + " WHERE e.ecMember=:ecMember"
            + " ORDER BY e.ecPartner.name")
})
public class EcMemberPartner implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcMemberPartnerPK ecMemberPartnerPK;
    @Size(max = 20)
    @Column(name = "DUMMY")
    private String dummy;
    @JoinColumn(name = "PARTNER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcPartner ecPartner;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcMember ecMember;

    public EcMemberPartner() {
    }

    public EcMemberPartner(EcMemberPartnerPK ecMemberPartnerPK) {
        this.ecMemberPartnerPK = ecMemberPartnerPK;
    }

    public EcMemberPartner(long memberId, long partnerId) {
        this.ecMemberPartnerPK = new EcMemberPartnerPK(memberId, partnerId);
    }

    public EcMemberPartnerPK getEcMemberPartnerPK() {
        return ecMemberPartnerPK;
    }

    public void setEcMemberPartnerPK(EcMemberPartnerPK ecMemberPartnerPK) {
        this.ecMemberPartnerPK = ecMemberPartnerPK;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public EcPartner getEcPartner() {
        return ecPartner;
    }

    public void setEcPartner(EcPartner ecPartner) {
        this.ecPartner = ecPartner;
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
        hash += (ecMemberPartnerPK != null ? ecMemberPartnerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcMemberPartner)) {
            return false;
        }
        EcMemberPartner other = (EcMemberPartner) object;
        if ((this.ecMemberPartnerPK == null && other.ecMemberPartnerPK != null) || (this.ecMemberPartnerPK != null && !this.ecMemberPartnerPK.equals(other.ecMemberPartnerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcMemberPartner[ ecMemberPartnerPK=" + ecMemberPartnerPK + " ]";
    }

}
