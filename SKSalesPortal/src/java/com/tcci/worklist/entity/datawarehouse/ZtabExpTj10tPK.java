/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.worklist.entity.datawarehouse;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author neo
 */
@Embeddable
public class ZtabExpTj10tPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 24)
    @Column(name = "BERSL")
    private String bersl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "SPRAS")
    private String spras;

    public ZtabExpTj10tPK() {
    }

    public ZtabExpTj10tPK(String mandt, String bersl, String spras) {
        this.mandt = mandt;
        this.bersl = bersl;
        this.spras = spras;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getBersl() {
        return bersl;
    }

    public void setBersl(String bersl) {
        this.bersl = bersl;
    }

    public String getSpras() {
        return spras;
    }

    public void setSpras(String spras) {
        this.spras = spras;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (bersl != null ? bersl.hashCode() : 0);
        hash += (spras != null ? spras.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpTj10tPK)) {
            return false;
        }
        ZtabExpTj10tPK other = (ZtabExpTj10tPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.bersl == null && other.bersl != null) || (this.bersl != null && !this.bersl.equals(other.bersl))) {
            return false;
        }
        if ((this.spras == null && other.spras != null) || (this.spras != null && !this.spras.equals(other.spras))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sapapprover.entity.datawarehouse.ZtabExpTj10tPK[ mandt=" + mandt + ", bersl=" + bersl + ", spras=" + spras + " ]";
    }
    
}
