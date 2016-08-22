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
public class ZtabExpTvkotPK implements Serializable {
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
    @Size(min = 1, max = 8)
    @Column(name = "VKORG")
    private String vkorg;

    public ZtabExpTvkotPK() {
    }

    public ZtabExpTvkotPK(String mandt, String spras, String vkorg) {
        this.mandt = mandt;
        this.spras = spras;
        this.vkorg = vkorg;
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

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (spras != null ? spras.hashCode() : 0);
        hash += (vkorg != null ? vkorg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpTvkotPK)) {
            return false;
        }
        ZtabExpTvkotPK other = (ZtabExpTvkotPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.spras == null && other.spras != null) || (this.spras != null && !this.spras.equals(other.spras))) {
            return false;
        }
        if ((this.vkorg == null && other.vkorg != null) || (this.vkorg != null && !this.vkorg.equals(other.vkorg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.worklist.entity.datawarehouse.ZtabExpTvkotPK[ mandt=" + mandt + ", spras=" + spras + ", vkorg=" + vkorg + " ]";
    }
    
}
