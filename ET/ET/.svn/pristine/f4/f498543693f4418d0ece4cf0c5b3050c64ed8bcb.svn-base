/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.entity.base;

import com.tcci.sap.jco.entity.TcRfcZtab;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author peter.pan
 */
@Entity
@Table(name = "TC_ZTAB_EXP_T023T")
@NamedQueries({
    @NamedQuery(name = "TcZtabExpT023t.findAll", query = "SELECT t FROM TcZtabExpT023t t")})
public class TcZtabExpT023t implements TcRfcZtab, Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TcZtabExpT023tPK tcZtabExpT023tPK;
    @Size(max = 80)
    @Column(name = "WGBEZ")
    private String wgbez;
    @Column(name = "SYNC_TIME_STAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date syncTimeStamp;

    @Override
    public Object getPrimaryKey() {
        return tcZtabExpT023tPK;
    }

    @Override
    public String getMandt() {
        return tcZtabExpT023tPK!=null?tcZtabExpT023tPK.getMandt():null;
    }

    @Override
    public String getDelKey() {
        return tcZtabExpT023tPK!=null?tcZtabExpT023tPK.getMatkl():null;
    }

    public TcZtabExpT023t() {
    }

    public TcZtabExpT023t(TcZtabExpT023tPK tcZtabExpT023tPK) {
        this.tcZtabExpT023tPK = tcZtabExpT023tPK;
    }

    public TcZtabExpT023t(String mandt, String spras, String matkl) {
        this.tcZtabExpT023tPK = new TcZtabExpT023tPK(mandt, spras, matkl);
    }

    public TcZtabExpT023tPK getTcZtabExpT023tPK() {
        return tcZtabExpT023tPK;
    }

    public void setTcZtabExpT023tPK(TcZtabExpT023tPK tcZtabExpT023tPK) {
        this.tcZtabExpT023tPK = tcZtabExpT023tPK;
    }

    public String getWgbez() {
        return wgbez;
    }

    public void setWgbez(String wgbez) {
        this.wgbez = wgbez;
    }

    public Date getSyncTimeStamp() {
        return syncTimeStamp;
    }

    @Override
    public void setSyncTimeStamp(Date syncTimeStamp) {
        this.syncTimeStamp = syncTimeStamp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tcZtabExpT023tPK != null ? tcZtabExpT023tPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcZtabExpT023t)) {
            return false;
        }
        TcZtabExpT023t other = (TcZtabExpT023t) object;
        if ((this.tcZtabExpT023tPK == null && other.tcZtabExpT023tPK != null) || (this.tcZtabExpT023tPK != null && !this.tcZtabExpT023tPK.equals(other.tcZtabExpT023tPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sap.jco.entity.base.TcZtabExpT023t[ tcZtabExpT023tPK=" + tcZtabExpT023tPK + " ]";
    }
    
}
