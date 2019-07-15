/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.entity.base;

import com.tcci.sap.jco.entity.TcRfcZtab;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "TC_ZTAB_EXP_LAST_PRICE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TcZtabExpLastPrice.findAll", query = "SELECT t FROM TcZtabExpLastPrice t")
    , @NamedQuery(name = "TcZtabExpLastPrice.findByMandt", query = "SELECT t FROM TcZtabExpLastPrice t WHERE t.tcZtabExpLastPricePK.mandt = :mandt")
    , @NamedQuery(name = "TcZtabExpLastPrice.findByMatnr", query = "SELECT t FROM TcZtabExpLastPrice t WHERE t.tcZtabExpLastPricePK.matnr = :matnr")
    , @NamedQuery(name = "TcZtabExpLastPrice.findByEbeln", query = "SELECT t FROM TcZtabExpLastPrice t WHERE t.tcZtabExpLastPricePK.ebeln = :ebeln")
    , @NamedQuery(name = "TcZtabExpLastPrice.findByEbelp", query = "SELECT t FROM TcZtabExpLastPrice t WHERE t.tcZtabExpLastPricePK.ebelp = :ebelp")
    , @NamedQuery(name = "TcZtabExpLastPrice.findByWerks", query = "SELECT t FROM TcZtabExpLastPrice t WHERE t.tcZtabExpLastPricePK.werks = :werks")
    , @NamedQuery(name = "TcZtabExpLastPrice.findByBedat", query = "SELECT t FROM TcZtabExpLastPrice t WHERE t.bedat = :bedat")
    , @NamedQuery(name = "TcZtabExpLastPrice.findByNetpr", query = "SELECT t FROM TcZtabExpLastPrice t WHERE t.netpr = :netpr")
    , @NamedQuery(name = "TcZtabExpLastPrice.findByWaers", query = "SELECT t FROM TcZtabExpLastPrice t WHERE t.waers = :waers")
    , @NamedQuery(name = "TcZtabExpLastPrice.findBySyncTimeStamp", query = "SELECT t FROM TcZtabExpLastPrice t WHERE t.syncTimeStamp = :syncTimeStamp")})
public class TcZtabExpLastPrice implements TcRfcZtab, Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TcZtabExpLastPricePK tcZtabExpLastPricePK;
    @Column(name = "BEDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bedat;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "NETPR")
    private BigDecimal netpr;
    @Size(max = 10)
    @Column(name = "WAERS")
    private String waers;
    @Column(name = "SYNC_TIME_STAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date syncTimeStamp;

    @Override
    public Object getPrimaryKey() {
        return tcZtabExpLastPricePK;
    }

    @Override
    public String getMandt() {
        return tcZtabExpLastPricePK!=null?tcZtabExpLastPricePK.getMandt():null;
    }

    @Override
    public String getDelKey() {
        return tcZtabExpLastPricePK!=null?tcZtabExpLastPricePK.getMatnr():null;
    }

    public TcZtabExpLastPrice() {
    }

    public TcZtabExpLastPrice(TcZtabExpLastPricePK tcZtabExpLastPricePK) {
        this.tcZtabExpLastPricePK = tcZtabExpLastPricePK;
    }

    public TcZtabExpLastPrice(String mandt, String matnr, String ebeln, long ebelp, String werks) {
        this.tcZtabExpLastPricePK = new TcZtabExpLastPricePK(mandt, matnr, ebeln, ebelp, werks);
    }

    public TcZtabExpLastPricePK getTcZtabExpLastPricePK() {
        return tcZtabExpLastPricePK;
    }

    public void setTcZtabExpLastPricePK(TcZtabExpLastPricePK tcZtabExpLastPricePK) {
        this.tcZtabExpLastPricePK = tcZtabExpLastPricePK;
    }

    public Date getBedat() {
        return bedat;
    }

    public void setBedat(Date bedat) {
        this.bedat = bedat;
    }

    public BigDecimal getNetpr() {
        return netpr;
    }

    public void setNetpr(BigDecimal netpr) {
        this.netpr = netpr;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
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
        hash += (tcZtabExpLastPricePK != null ? tcZtabExpLastPricePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcZtabExpLastPrice)) {
            return false;
        }
        TcZtabExpLastPrice other = (TcZtabExpLastPrice) object;
        if ((this.tcZtabExpLastPricePK == null && other.tcZtabExpLastPricePK != null) || (this.tcZtabExpLastPricePK != null && !this.tcZtabExpLastPricePK.equals(other.tcZtabExpLastPricePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sap.jco.entity.base.TcZtabExpLastPrice[ tcZtabExpLastPricePK=" + tcZtabExpLastPricePK + " ]";
    }
    
}
