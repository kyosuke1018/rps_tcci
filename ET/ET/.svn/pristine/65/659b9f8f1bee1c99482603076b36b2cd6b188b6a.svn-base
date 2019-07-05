/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.entity.base;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author peter.pan
 */
@Embeddable
public class TcZtabExpT024ePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "EKORG")
    private String ekorg;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "BUKRS")
    private String bukrs = "NA";// 中橡資料有NULL狀況

    public TcZtabExpT024ePK() {
    }

    public TcZtabExpT024ePK(String mandt, String ekorg, String bukrs) {
        this.mandt = mandt;
        this.ekorg = ekorg;
        this.bukrs = bukrs;
        if( bukrs == null || bukrs.trim().isEmpty() ){// 中橡資料有NULL狀況
            this.bukrs = "NA";
        }
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getEkorg() {
        return ekorg;
    }

    public void setEkorg(String ekorg) {
        this.ekorg = ekorg;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
        if( bukrs == null || bukrs.trim().isEmpty() ){// 中橡資料有NULL狀況
            this.bukrs = "NA";
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (ekorg != null ? ekorg.hashCode() : 0);
        hash += (bukrs != null ? bukrs.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcZtabExpT024ePK)) {
            return false;
        }
        TcZtabExpT024ePK other = (TcZtabExpT024ePK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.ekorg == null && other.ekorg != null) || (this.ekorg != null && !this.ekorg.equals(other.ekorg))) {
            return false;
        }
        if ((this.bukrs == null && other.bukrs != null) || (this.bukrs != null && !this.bukrs.equals(other.bukrs))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sap.jco.entity.base.TcZtabExpT024ePK[ mandt=" + mandt + ", ekorg=" + ekorg + ", bukrs=" + bukrs + " ]";
    }
    
}
