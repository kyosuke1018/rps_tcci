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
public class EcMemberCustomerPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "MEMBER_ID")
    private long memberId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTOMER_ID")
    private long customerId;

    public EcMemberCustomerPK() {
    }

    public EcMemberCustomerPK(long memberId, long customerId) {
        this.memberId = memberId;
        this.customerId = customerId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) memberId;
        hash += (int) customerId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcMemberCustomerPK)) {
            return false;
        }
        EcMemberCustomerPK other = (EcMemberCustomerPK) object;
        if (this.memberId != other.memberId) {
            return false;
        }
        if (this.customerId != other.customerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcMemberCustomerPK[ memberId=" + memberId + ", customerId=" + customerId + " ]";
    }
    
}
