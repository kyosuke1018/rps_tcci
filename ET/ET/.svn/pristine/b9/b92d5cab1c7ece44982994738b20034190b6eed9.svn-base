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
@Table(name = "TC_ZTAB_EXP_T024")
@NamedQueries({
    @NamedQuery(name = "TcZtabExpT024.findAll", query = "SELECT t FROM TcZtabExpT024 t")})
public class TcZtabExpT024 implements TcRfcZtab, Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TcZtabExpT024PK tcZtabExpT024PK;
    @Size(max = 36)
    @Column(name = "EKNAM")
    private String eknam;
    @Size(max = 24)
    @Column(name = "EKTEL")
    private String ektel;
    @Size(max = 8)
    @Column(name = "LDEST")
    private String ldest;
    @Size(max = 62)
    @Column(name = "TELFX")
    private String telfx;
    @Size(max = 120)
    @Column(name = "TEL_NUMBER")
    private String telNumber;
    @Size(max = 20)
    @Column(name = "TEL_EXTENS")
    private String telExtens;
    @Size(max = 482)
    @Column(name = "SMTP_ADDR")
    private String smtpAddr;
    @Column(name = "SYNC_TIME_STAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date syncTimeStamp;

    @Override
    public Object getPrimaryKey() {
        return tcZtabExpT024PK;
    }

    @Override
    public String getMandt() {
        return tcZtabExpT024PK!=null?tcZtabExpT024PK.getMandt():null;
    }

    @Override
    public String getDelKey() {
        return tcZtabExpT024PK!=null?tcZtabExpT024PK.getEkgrp():null;
    }

    public TcZtabExpT024() {
    }

    public TcZtabExpT024(TcZtabExpT024PK tcZtabExpT024PK) {
        this.tcZtabExpT024PK = tcZtabExpT024PK;
    }

    public TcZtabExpT024(String mandt, String ekgrp) {
        this.tcZtabExpT024PK = new TcZtabExpT024PK(mandt, ekgrp);
    }

    public TcZtabExpT024PK getTcZtabExpT024PK() {
        return tcZtabExpT024PK;
    }

    public void setTcZtabExpT024PK(TcZtabExpT024PK tcZtabExpT024PK) {
        this.tcZtabExpT024PK = tcZtabExpT024PK;
    }

    public String getEknam() {
        return eknam;
    }

    public void setEknam(String eknam) {
        this.eknam = eknam;
    }

    public String getEktel() {
        return ektel;
    }

    public void setEktel(String ektel) {
        this.ektel = ektel;
    }

    public String getLdest() {
        return ldest;
    }

    public void setLdest(String ldest) {
        this.ldest = ldest;
    }

    public String getTelfx() {
        return telfx;
    }

    public void setTelfx(String telfx) {
        this.telfx = telfx;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getTelExtens() {
        return telExtens;
    }

    public void setTelExtens(String telExtens) {
        this.telExtens = telExtens;
    }

    public String getSmtpAddr() {
        return smtpAddr;
    }

    public void setSmtpAddr(String smtpAddr) {
        this.smtpAddr = smtpAddr;
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
        hash += (tcZtabExpT024PK != null ? tcZtabExpT024PK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcZtabExpT024)) {
            return false;
        }
        TcZtabExpT024 other = (TcZtabExpT024) object;
        if ((this.tcZtabExpT024PK == null && other.tcZtabExpT024PK != null) || (this.tcZtabExpT024PK != null && !this.tcZtabExpT024PK.equals(other.tcZtabExpT024PK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sap.jco.entity.base.TcZtabExpT024[ tcZtabExpT024PK=" + tcZtabExpT024PK + " ]";
    }
    
}
