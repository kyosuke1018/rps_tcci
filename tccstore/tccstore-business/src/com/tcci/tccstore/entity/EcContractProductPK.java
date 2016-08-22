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
public class EcContractProductPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONTRACT_ID")
    private long contractId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "POSNR")
    private int posnr;

    public EcContractProductPK() {
    }

    public EcContractProductPK(long contractId, int posnr) {
        this.contractId = contractId;
        this.posnr = posnr;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public int getPosnr() {
        return posnr;
    }

    public void setPosnr(int posnr) {
        this.posnr = posnr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) contractId;
        hash += posnr;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcContractProductPK)) {
            return false;
        }
        EcContractProductPK other = (EcContractProductPK) object;
        if (this.contractId != other.contractId) {
            return false;
        }
        if (this.posnr != other.posnr) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcContractProductPK[ contractId=" + contractId + ", posnr=" + posnr + " ]";
    }
    
}
