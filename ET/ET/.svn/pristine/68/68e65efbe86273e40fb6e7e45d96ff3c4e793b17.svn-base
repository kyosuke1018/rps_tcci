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
public class TcZtabExpT052uPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "SPRAS")
    private String spras;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "ZTERM")
    private String zterm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ZTAGG")
    private short ztagg;

    public TcZtabExpT052uPK() {
    }

    public TcZtabExpT052uPK(String mandt, String spras, String zterm, short ztagg) {
        this.mandt = mandt;
        this.spras = spras;
        this.zterm = zterm;
        this.ztagg = ztagg;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getSpras() {
        return spras;
    }

    public void setSpras(String spras) {
        this.spras = spras;
    }

    public String getZterm() {
        return zterm;
    }

    public void setZterm(String zterm) {
        this.zterm = zterm;
    }

    public short getZtagg() {
        return ztagg;
    }

    public void setZtagg(short ztagg) {
        this.ztagg = ztagg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (spras != null ? spras.hashCode() : 0);
        hash += (zterm != null ? zterm.hashCode() : 0);
        hash += (int) ztagg;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcZtabExpT052uPK)) {
            return false;
        }
        TcZtabExpT052uPK other = (TcZtabExpT052uPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.spras == null && other.spras != null) || (this.spras != null && !this.spras.equals(other.spras))) {
            return false;
        }
        if ((this.zterm == null && other.zterm != null) || (this.zterm != null && !this.zterm.equals(other.zterm))) {
            return false;
        }
        if (this.ztagg != other.ztagg) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sap.jco.entity.base.TcZtabExpT052uPK[ mandt=" + mandt + ", spras=" + spras + ", zterm=" + zterm + ", ztagg=" + ztagg + " ]";
    }
    
}
