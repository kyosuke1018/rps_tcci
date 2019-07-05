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
@Table(name = "TC_ZTAB_EXP_T024E")
@NamedQueries({
    @NamedQuery(name = "TcZtabExpT024e.findAll", query = "SELECT t FROM TcZtabExpT024e t")})
public class TcZtabExpT024e implements TcRfcZtab, Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TcZtabExpT024ePK tcZtabExpT024ePK;
    @Size(max = 40)
    @Column(name = "EKOTX")
    private String ekotx;
    @Column(name = "SYNC_TIME_STAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date syncTimeStamp;

    @Override
    public Object getPrimaryKey() {
        return tcZtabExpT024ePK;
    }

    @Override
    public String getMandt() {
        return tcZtabExpT024ePK!=null?tcZtabExpT024ePK.getMandt():null;
    }

    @Override
    public String getDelKey() {
        return tcZtabExpT024ePK!=null?tcZtabExpT024ePK.getEkorg():null;
    }

    public TcZtabExpT024e() {
    }

    public TcZtabExpT024e(TcZtabExpT024ePK tcZtabExpT024ePK) {
        this.tcZtabExpT024ePK = tcZtabExpT024ePK;
    }

    public TcZtabExpT024e(String mandt, String ekorg, String bukrs) {
        this.tcZtabExpT024ePK = new TcZtabExpT024ePK(mandt, ekorg, bukrs);
    }

    public TcZtabExpT024ePK getTcZtabExpT024ePK() {
        return tcZtabExpT024ePK;
    }

    public void setTcZtabExpT024ePK(TcZtabExpT024ePK tcZtabExpT024ePK) {
        this.tcZtabExpT024ePK = tcZtabExpT024ePK;
    }

    public String getEkotx() {
        return ekotx;
    }

    public void setEkotx(String ekotx) {
        this.ekotx = ekotx;
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
        hash += (tcZtabExpT024ePK != null ? tcZtabExpT024ePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcZtabExpT024e)) {
            return false;
        }
        TcZtabExpT024e other = (TcZtabExpT024e) object;
        if ((this.tcZtabExpT024ePK == null && other.tcZtabExpT024ePK != null) || (this.tcZtabExpT024ePK != null && !this.tcZtabExpT024ePK.equals(other.tcZtabExpT024ePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sap.jco.entity.base.TcZtabExpT024e[ tcZtabExpT024ePK=" + tcZtabExpT024ePK + " ]";
    }
    
}
