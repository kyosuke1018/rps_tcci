/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.datawarehouse;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Neo.Fu
 */
@Embeddable
public class TcZtabExpTvv1tPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "SPRAS")
    private String spras;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "KVGR1")
    private String kvgr1;

    public TcZtabExpTvv1tPK() {
    }

    public TcZtabExpTvv1tPK(String mandt, String spras, String kvgr1) {
        this.mandt = mandt;
        this.spras = spras;
        this.kvgr1 = kvgr1;
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

    public String getKvgr1() {
        return kvgr1;
    }

    public void setKvgr1(String kvgr1) {
        this.kvgr1 = kvgr1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (spras != null ? spras.hashCode() : 0);
        hash += (kvgr1 != null ? kvgr1.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcZtabExpTvv1tPK)) {
            return false;
        }
        TcZtabExpTvv1tPK other = (TcZtabExpTvv1tPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.spras == null && other.spras != null) || (this.spras != null && !this.spras.equals(other.spras))) {
            return false;
        }
        if ((this.kvgr1 == null && other.kvgr1 != null) || (this.kvgr1 != null && !this.kvgr1.equals(other.kvgr1))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.datawarehouse.TcZtabExpTvv1tPK[ mandt=" + mandt + ", spras=" + spras + ", kvgr1=" + kvgr1 + " ]";
    }
    
}
