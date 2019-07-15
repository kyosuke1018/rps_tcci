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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "TC_ZTAB_EXP_T134T")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TcZtabExpT134t.findAll", query = "SELECT t FROM TcZtabExpT134t t")
    , @NamedQuery(name = "TcZtabExpT134t.findByMandt", query = "SELECT t FROM TcZtabExpT134t t WHERE t.tcZtabExpT134tPK.mandt = :mandt")
    , @NamedQuery(name = "TcZtabExpT134t.findBySpras", query = "SELECT t FROM TcZtabExpT134t t WHERE t.tcZtabExpT134tPK.spras = :spras")
    , @NamedQuery(name = "TcZtabExpT134t.findByMtart", query = "SELECT t FROM TcZtabExpT134t t WHERE t.tcZtabExpT134tPK.mtart = :mtart")
    , @NamedQuery(name = "TcZtabExpT134t.findByMtbez", query = "SELECT t FROM TcZtabExpT134t t WHERE t.mtbez = :mtbez")
    , @NamedQuery(name = "TcZtabExpT134t.findBySyncTimeStamp", query = "SELECT t FROM TcZtabExpT134t t WHERE t.syncTimeStamp = :syncTimeStamp")})
public class TcZtabExpT134t implements TcRfcZtab, Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TcZtabExpT134tPK tcZtabExpT134tPK;
    @Size(max = 64)
    @Column(name = "MTBEZ")
    private String mtbez;
    @Column(name = "SYNC_TIME_STAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date syncTimeStamp;

    @Override
    public Object getPrimaryKey() {
        return tcZtabExpT134tPK;
    }

    @Override
    public String getMandt() {
        return tcZtabExpT134tPK!=null?tcZtabExpT134tPK.getMandt():null;
    }

    @Override
    public String getDelKey() {
        return tcZtabExpT134tPK!=null?tcZtabExpT134tPK.getSpras():null;
    }

    public TcZtabExpT134t() {
    }

    public TcZtabExpT134t(TcZtabExpT134tPK tcZtabExpT134tPK) {
        this.tcZtabExpT134tPK = tcZtabExpT134tPK;
    }

    public TcZtabExpT134t(String mandt, String spras, String mtart) {
        this.tcZtabExpT134tPK = new TcZtabExpT134tPK(mandt, spras, mtart);
    }

    public TcZtabExpT134tPK getTcZtabExpT134tPK() {
        return tcZtabExpT134tPK;
    }

    public void setTcZtabExpT134tPK(TcZtabExpT134tPK tcZtabExpT134tPK) {
        this.tcZtabExpT134tPK = tcZtabExpT134tPK;
    }

    public String getMtbez() {
        return mtbez;
    }

    public void setMtbez(String mtbez) {
        this.mtbez = mtbez;
    }

    public Date getSyncTimeStamp() {
        return syncTimeStamp;
    }

    public void setSyncTimeStamp(Date syncTimeStamp) {
        this.syncTimeStamp = syncTimeStamp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tcZtabExpT134tPK != null ? tcZtabExpT134tPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcZtabExpT134t)) {
            return false;
        }
        TcZtabExpT134t other = (TcZtabExpT134t) object;
        if ((this.tcZtabExpT134tPK == null && other.tcZtabExpT134tPK != null) || (this.tcZtabExpT134tPK != null && !this.tcZtabExpT134tPK.equals(other.tcZtabExpT134tPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sap.jco.entity.base.TcZtabExpT134t[ tcZtabExpT134tPK=" + tcZtabExpT134tPK + " ]";
    }
    
}
