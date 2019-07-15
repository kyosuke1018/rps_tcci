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
@Table(name = "TC_ZTAB_EXP_T052U")
@NamedQueries({
    @NamedQuery(name = "TcZtabExpT052u.findAll", query = "SELECT t FROM TcZtabExpT052u t")})
public class TcZtabExpT052u implements TcRfcZtab, Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TcZtabExpT052uPK tcZtabExpT052uPK;
    @Size(max = 100)
    @Column(name = "TEXT1")
    private String text1;
    @Column(name = "SYNC_TIME_STAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date syncTimeStamp;

    @Override
    public Object getPrimaryKey() {
        return tcZtabExpT052uPK;
    }

    @Override
    public String getMandt() {
        return tcZtabExpT052uPK!=null?tcZtabExpT052uPK.getMandt():null;
    }

    @Override
    public String getDelKey() {
        return tcZtabExpT052uPK!=null?tcZtabExpT052uPK.getSpras():null;
    }

    public TcZtabExpT052u() {
    }

    public TcZtabExpT052u(TcZtabExpT052uPK tcZtabExpT052uPK) {
        this.tcZtabExpT052uPK = tcZtabExpT052uPK;
    }

    public TcZtabExpT052u(String mandt, String spras, String zterm, short ztagg) {
        this.tcZtabExpT052uPK = new TcZtabExpT052uPK(mandt, spras, zterm, ztagg);
    }

    public TcZtabExpT052uPK getTcZtabExpT052uPK() {
        return tcZtabExpT052uPK;
    }

    public void setTcZtabExpT052uPK(TcZtabExpT052uPK tcZtabExpT052uPK) {
        this.tcZtabExpT052uPK = tcZtabExpT052uPK;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
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
        hash += (tcZtabExpT052uPK != null ? tcZtabExpT052uPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcZtabExpT052u)) {
            return false;
        }
        TcZtabExpT052u other = (TcZtabExpT052u) object;
        if ((this.tcZtabExpT052uPK == null && other.tcZtabExpT052uPK != null) || (this.tcZtabExpT052uPK != null && !this.tcZtabExpT052uPK.equals(other.tcZtabExpT052uPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sap.jco.entity.base.TcZtabExpT052u[ tcZtabExpT052uPK=" + tcZtabExpT052uPK + " ]";
    }
    
}
