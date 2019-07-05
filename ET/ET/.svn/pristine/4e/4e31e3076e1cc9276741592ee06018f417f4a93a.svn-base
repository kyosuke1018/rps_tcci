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
public class TcZtabExpT024PK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "EKGRP")
    private String ekgrp;

    public TcZtabExpT024PK() {
    }

    public TcZtabExpT024PK(String mandt, String ekgrp) {
        this.mandt = mandt;
        this.ekgrp = ekgrp;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getEkgrp() {
        return ekgrp;
    }

    public void setEkgrp(String ekgrp) {
        this.ekgrp = ekgrp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (ekgrp != null ? ekgrp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcZtabExpT024PK)) {
            return false;
        }
        TcZtabExpT024PK other = (TcZtabExpT024PK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.ekgrp == null && other.ekgrp != null) || (this.ekgrp != null && !this.ekgrp.equals(other.ekgrp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sap.jco.entity.base.TcZtabExpT024PK[ mandt=" + mandt + ", ekgrp=" + ekgrp + " ]";
    }
    
}
