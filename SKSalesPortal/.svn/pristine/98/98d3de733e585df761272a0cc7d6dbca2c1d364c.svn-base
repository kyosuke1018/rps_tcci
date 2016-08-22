package com.tcci.worklist.entity.datawarehouse;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author nEO.Fu
 */
@Embeddable
public class ZtabExpVbpaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "VBELN")
    private String vbeln;

    public ZtabExpVbpaPK() {
    }

    public ZtabExpVbpaPK(String mandt, String vbeln) {
        this.mandt = mandt;
        this.vbeln = vbeln;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getVbeln() {
        return vbeln;
    }

    public void setVbeln(String vbeln) {
        this.vbeln = vbeln;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (vbeln != null ? vbeln.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpVbpaPK)) {
            return false;
        }
        ZtabExpVbpaPK other = (ZtabExpVbpaPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.vbeln == null && other.vbeln != null) || (this.vbeln != null && !this.vbeln.equals(other.vbeln))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.mygui.entity.datawarehouse.ZtabExpVbpaPK[ mandt=" + mandt + ", vbeln=" + vbeln + " ]";
    }
    
}
