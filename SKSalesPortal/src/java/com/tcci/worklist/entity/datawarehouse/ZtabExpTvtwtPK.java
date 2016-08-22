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
 * @author nEO
 */
@Embeddable
public class ZtabExpTvtwtPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 24)
    @Column(name = "SPRAS")
    private String spras;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "VTWEG")
    private String vtweg;

    public ZtabExpTvtwtPK() {
    }

    public ZtabExpTvtwtPK(String mandt, String spras, String vtweg) {
        this.mandt = mandt;
        this.spras = spras;
        this.vtweg = vtweg;
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

    public String getVtweg() {
        return vtweg;
    }

    public void setVtweg(String vtweg) {
        this.vtweg = vtweg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (spras != null ? spras.hashCode() : 0);
        hash += (vtweg != null ? vtweg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpTvtwtPK)) {
            return false;
        }
        ZtabExpTvtwtPK other = (ZtabExpTvtwtPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.spras == null && other.spras != null) || (this.spras != null && !this.spras.equals(other.spras))) {
            return false;
        }
        if ((this.vtweg == null && other.vtweg != null) || (this.vtweg != null && !this.vtweg.equals(other.vtweg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.worklist.entity.datawarehouse.ZtabExpTvtwtPK[ mandt=" + mandt + ", spras=" + spras + ", vtweg=" + vtweg + " ]";
    }
    
}
