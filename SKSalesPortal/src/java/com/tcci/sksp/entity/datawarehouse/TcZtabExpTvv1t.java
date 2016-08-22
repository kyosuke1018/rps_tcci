/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.datawarehouse;

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
 * @author Neo.Fu
 */
@Entity
@Table(name = "TC_ZTAB_EXP_TVV1T")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TcZtabExpTvv1t.findAll", query = "SELECT t FROM TcZtabExpTvv1t t"),
    @NamedQuery(name = "TcZtabExpTvv1t.findByMandt", query = "SELECT t FROM TcZtabExpTvv1t t WHERE t.tcZtabExpTvv1tPK.mandt = :mandt"),
    @NamedQuery(name = "TcZtabExpTvv1t.findBySpras", query = "SELECT t FROM TcZtabExpTvv1t t WHERE t.tcZtabExpTvv1tPK.spras = :spras"),
    @NamedQuery(name = "TcZtabExpTvv1t.findByKvgr1", query = "SELECT t FROM TcZtabExpTvv1t t WHERE t.tcZtabExpTvv1tPK.kvgr1 = :kvgr1"),
    @NamedQuery(name = "TcZtabExpTvv1t.findByBezei", query = "SELECT t FROM TcZtabExpTvv1t t WHERE t.bezei = :bezei"),
    @NamedQuery(name = "TcZtabExpTvv1t.findBySyncTimeStamp", query = "SELECT t FROM TcZtabExpTvv1t t WHERE t.syncTimeStamp = :syncTimeStamp")})
public class TcZtabExpTvv1t implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TcZtabExpTvv1tPK tcZtabExpTvv1tPK;
    @Size(max = 255)
    @Column(name = "BEZEI")
    private String bezei;
    @Column(name = "SYNC_TIME_STAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date syncTimeStamp;

    public TcZtabExpTvv1t() {
    }

    public TcZtabExpTvv1t(TcZtabExpTvv1tPK tcZtabExpTvv1tPK) {
        this.tcZtabExpTvv1tPK = tcZtabExpTvv1tPK;
    }

    public TcZtabExpTvv1t(String mandt, String spras, String kvgr1) {
        this.tcZtabExpTvv1tPK = new TcZtabExpTvv1tPK(mandt, spras, kvgr1);
    }

    public TcZtabExpTvv1tPK getTcZtabExpTvv1tPK() {
        return tcZtabExpTvv1tPK;
    }

    public void setTcZtabExpTvv1tPK(TcZtabExpTvv1tPK tcZtabExpTvv1tPK) {
        this.tcZtabExpTvv1tPK = tcZtabExpTvv1tPK;
    }

    public String getBezei() {
        return bezei;
    }

    public void setBezei(String bezei) {
        this.bezei = bezei;
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
        hash += (tcZtabExpTvv1tPK != null ? tcZtabExpTvv1tPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcZtabExpTvv1t)) {
            return false;
        }
        TcZtabExpTvv1t other = (TcZtabExpTvv1t) object;
        if ((this.tcZtabExpTvv1tPK == null && other.tcZtabExpTvv1tPK != null) || (this.tcZtabExpTvv1tPK != null && !this.tcZtabExpTvv1tPK.equals(other.tcZtabExpTvv1tPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.datawarehouse.TcZtabExpTvv1t[ tcZtabExpTvv1tPK=" + tcZtabExpTvv1tPK + " ]";
    }
    
}
