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
@Table(name = "EC_MEMBER_CUSTOMER")
@NamedQueries({
    @NamedQuery(name = "EcMemberCustomer.findAll", query = "SELECT e FROM EcMemberCustomer e"),
    @NamedQuery(name = "EcMemberCustomer.findByMember", query = "SELECT e FROM EcMemberCustomer e WHERE e.ecMember=:ecMember")
})
public class EcMemberCustomer implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcMemberCustomerPK ecMemberCustomerPK;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcMember ecMember;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcCustomer ecCustomer;

    public EcMemberCustomer() {
    }
    
    public EcMemberCustomer(EcMember ecMember, EcCustomer ecCustomer) {
        this.ecMemberCustomerPK = new EcMemberCustomerPK(ecMember.getId(), ecCustomer.getId());
        this.ecMember = ecMember;
        this.ecCustomer = ecCustomer;
    }

    public EcMemberCustomer(EcMemberCustomerPK ecMemberCustomerPK) {
        this.ecMemberCustomerPK = ecMemberCustomerPK;
    }

    public EcMemberCustomer(long memberId, long customerId) {
        this.ecMemberCustomerPK = new EcMemberCustomerPK(memberId, customerId);
    }

    public EcMemberCustomerPK getEcMemberCustomerPK() {
        return ecMemberCustomerPK;
    }

    public void setEcMemberCustomerPK(EcMemberCustomerPK ecMemberCustomerPK) {
        this.ecMemberCustomerPK = ecMemberCustomerPK;
    }

    public EcMember getEcMember() {
        return ecMember;
    }

    public void setEcMember(EcMember ecMember) {
        this.ecMember = ecMember;
    }

    public EcCustomer getEcCustomer() {
        return ecCustomer;
    }

    public void setEcCustomer(EcCustomer ecCustomer) {
        this.ecCustomer = ecCustomer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecMemberCustomerPK != null ? ecMemberCustomerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcMemberCustomer)) {
            return false;
        }
        EcMemberCustomer other = (EcMemberCustomer) object;
        if ((this.ecMemberCustomerPK == null && other.ecMemberCustomerPK != null) || (this.ecMemberCustomerPK != null && !this.ecMemberCustomerPK.equals(other.ecMemberCustomerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcMemberCustomer[ ecMemberCustomerPK=" + ecMemberCustomerPK + " ]";
    }
    
}
