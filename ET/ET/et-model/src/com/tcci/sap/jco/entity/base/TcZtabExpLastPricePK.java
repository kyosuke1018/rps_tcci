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
 * @author Peter.pan
 */
@Embeddable
public class TcZtabExpLastPricePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "MATNR")
    private String matnr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "EBELN")
    private String ebeln;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EBELP")
    private long ebelp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "WERKS")
    private String werks;

    public TcZtabExpLastPricePK() {
    }

    public TcZtabExpLastPricePK(String mandt, String matnr, String ebeln, long ebelp, String werks) {
        this.mandt = mandt;
        this.matnr = matnr;
        this.ebeln = ebeln;
        this.ebelp = ebelp;
        this.werks = werks;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
    }

    public long getEbelp() {
        return ebelp;
    }

    public void setEbelp(long ebelp) {
        this.ebelp = ebelp;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (matnr != null ? matnr.hashCode() : 0);
        hash += (ebeln != null ? ebeln.hashCode() : 0);
        hash += (int) ebelp;
        hash += (werks != null ? werks.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcZtabExpLastPricePK)) {
            return false;
        }
        TcZtabExpLastPricePK other = (TcZtabExpLastPricePK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.matnr == null && other.matnr != null) || (this.matnr != null && !this.matnr.equals(other.matnr))) {
            return false;
        }
        if ((this.ebeln == null && other.ebeln != null) || (this.ebeln != null && !this.ebeln.equals(other.ebeln))) {
            return false;
        }
        if (this.ebelp != other.ebelp) {
            return false;
        }
        if ((this.werks == null && other.werks != null) || (this.werks != null && !this.werks.equals(other.werks))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sap.jco.entity.base.TcZtabExpLastPricePK[ mandt=" + mandt + ", matnr=" + matnr + ", ebeln=" + ebeln + ", ebelp=" + ebelp + ", werks=" + werks + " ]";
    }
    
}
