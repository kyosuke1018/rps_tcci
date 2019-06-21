/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "ET_RFQ_EKKO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtRfqEkko.findAll", query = "SELECT e FROM EtRfqEkko e")
    , @NamedQuery(name = "EtRfqEkko.findById", query = "SELECT e FROM EtRfqEkko e WHERE e.id = :id")
    , @NamedQuery(name = "EtRfqEkko.findByRfqNo", query = "SELECT e FROM EtRfqEkko e WHERE e.rfqNo = :rfqNo")
    , @NamedQuery(name = "EtRfqEkko.findByTenderId", query = "SELECT e FROM EtRfqEkko e WHERE e.tenderId = :tenderId")
    , @NamedQuery(name = "EtRfqEkko.findByMandt", query = "SELECT e FROM EtRfqEkko e WHERE e.mandt = :mandt")
    , @NamedQuery(name = "EtRfqEkko.findByEbeln", query = "SELECT e FROM EtRfqEkko e WHERE e.ebeln = :ebeln")})
public class EtRfqEkko implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "RFQ_NO")
    private String rfqNo;
    @Column(name = "TENDER_ID")
    private Long tenderId;
    @Size(max = 6)
    @Column(name = "MANDT")
    private String mandt;
    @Size(max = 20)
    @Column(name = "EBELN")
    private String ebeln;
    @Size(max = 8)
    @Column(name = "BUKRS")
    private String bukrs;
    @Size(max = 2)
    @Column(name = "BSTYP")
    private String bstyp;
    @Size(max = 8)
    @Column(name = "BSART")
    private String bsart;
    @Column(name = "AEDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date aedat;
    @Size(max = 24)
    @Column(name = "ERNAM")
    private String ernam;
    @Size(max = 20)
    @Column(name = "LIFNR")
    private String lifnr;
    @Size(max = 2)
    @Column(name = "SPRAS")
    private String spras;
    @Size(max = 8)
    @Column(name = "ZTERM")
    private String zterm;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ZBD1T")
    private BigDecimal zbd1t;
    @Column(name = "ZBD2T")
    private BigDecimal zbd2t;
    @Column(name = "ZBD3T")
    private BigDecimal zbd3t;
    @Column(name = "ZBD1P")
    private BigDecimal zbd1p;
    @Column(name = "ZBD2P")
    private BigDecimal zbd2p;
    @Size(max = 8)
    @Column(name = "EKORG")
    private String ekorg;
    @Size(max = 6)
    @Column(name = "EKGRP")
    private String ekgrp;
    @Size(max = 10)
    @Column(name = "WAERS")
    private String waers;
    @Column(name = "WKURS")
    private BigDecimal wkurs;
    @Size(max = 2)
    @Column(name = "KUFIX")
    private String kufix;
    @Column(name = "BEDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bedat;
    @Size(max = 24)
    @Column(name = "IHREZ")
    private String ihrez;
    @Size(max = 60)
    @Column(name = "VERKF")
    private String verkf;
    @Size(max = 32)
    @Column(name = "TELF1")
    private String telf1;
    @Size(max = 6)
    @Column(name = "INCO1")
    private String inco1;
    @Size(max = 56)
    @Column(name = "INCO2")
    private String inco2;
    @Size(max = 20)
    @Column(name = "SUBMI")
    private String submi;
    @Size(max = 24)
    @Column(name = "UNSEZ")
    private String unsez;
    @Size(max = 4)
    @Column(name = "FRGGR")
    private String frggr;
    @Size(max = 4)
    @Column(name = "FRGSX")
    private String frgsx;
    @Size(max = 2)
    @Column(name = "FRGKE")
    private String frgke;
    @Size(max = 16)
    @Column(name = "FRGZU")
    private String frgzu;
    @Column(name = "RLWRT")
    private BigDecimal rlwrt;
    @Column(name = "UDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date udate;
    @Size(max = 80)
    @Column(name = "NAME1")
    private String name1;
    @Size(max = 80)
    @Column(name = "STR_SUPPL1")
    private String strSuppl1;
    @Size(max = 60)
    @Column(name = "TEL_NUMBER")
    private String telNumber;
    @Size(max = 60)
    @Column(name = "FAX_NUMBER")
    private String faxNumber;
    @Size(max = 20)
    @Column(name = "ADRNR")
    private String adrnr;
    @Column(name = "ANGDT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date angdt;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EtRfqEkko() {
    }

    public EtRfqEkko(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRfqNo() {
        return rfqNo;
    }

    public void setRfqNo(String rfqNo) {
        this.rfqNo = rfqNo;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    public String getBstyp() {
        return bstyp;
    }

    public void setBstyp(String bstyp) {
        this.bstyp = bstyp;
    }

    public String getBsart() {
        return bsart;
    }

    public void setBsart(String bsart) {
        this.bsart = bsart;
    }

    public Date getAedat() {
        return aedat;
    }

    public void setAedat(Date aedat) {
        this.aedat = aedat;
    }

    public String getErnam() {
        return ernam;
    }

    public void setErnam(String ernam) {
        this.ernam = ernam;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public String getSpras() {
        return spras;
    }

    public void setSpras(String spras) {
        this.spras = spras;
    }

    public String getZterm() {
        return zterm;
    }

    public void setZterm(String zterm) {
        this.zterm = zterm;
    }

    public BigDecimal getZbd1t() {
        return zbd1t;
    }

    public void setZbd1t(BigDecimal zbd1t) {
        this.zbd1t = zbd1t;
    }

    public BigDecimal getZbd2t() {
        return zbd2t;
    }

    public void setZbd2t(BigDecimal zbd2t) {
        this.zbd2t = zbd2t;
    }

    public BigDecimal getZbd3t() {
        return zbd3t;
    }

    public void setZbd3t(BigDecimal zbd3t) {
        this.zbd3t = zbd3t;
    }

    public BigDecimal getZbd1p() {
        return zbd1p;
    }

    public void setZbd1p(BigDecimal zbd1p) {
        this.zbd1p = zbd1p;
    }

    public BigDecimal getZbd2p() {
        return zbd2p;
    }

    public void setZbd2p(BigDecimal zbd2p) {
        this.zbd2p = zbd2p;
    }

    public String getEkorg() {
        return ekorg;
    }

    public void setEkorg(String ekorg) {
        this.ekorg = ekorg;
    }

    public String getEkgrp() {
        return ekgrp;
    }

    public void setEkgrp(String ekgrp) {
        this.ekgrp = ekgrp;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }

    public BigDecimal getWkurs() {
        return wkurs;
    }

    public void setWkurs(BigDecimal wkurs) {
        this.wkurs = wkurs;
    }

    public String getKufix() {
        return kufix;
    }

    public void setKufix(String kufix) {
        this.kufix = kufix;
    }

    public Date getBedat() {
        return bedat;
    }

    public void setBedat(Date bedat) {
        this.bedat = bedat;
    }

    public String getIhrez() {
        return ihrez;
    }

    public void setIhrez(String ihrez) {
        this.ihrez = ihrez;
    }

    public String getVerkf() {
        return verkf;
    }

    public void setVerkf(String verkf) {
        this.verkf = verkf;
    }

    public String getTelf1() {
        return telf1;
    }

    public void setTelf1(String telf1) {
        this.telf1 = telf1;
    }

    public String getInco1() {
        return inco1;
    }

    public void setInco1(String inco1) {
        this.inco1 = inco1;
    }

    public String getInco2() {
        return inco2;
    }

    public void setInco2(String inco2) {
        this.inco2 = inco2;
    }

    public String getSubmi() {
        return submi;
    }

    public void setSubmi(String submi) {
        this.submi = submi;
    }

    public String getUnsez() {
        return unsez;
    }

    public void setUnsez(String unsez) {
        this.unsez = unsez;
    }

    public String getFrggr() {
        return frggr;
    }

    public void setFrggr(String frggr) {
        this.frggr = frggr;
    }

    public String getFrgsx() {
        return frgsx;
    }

    public void setFrgsx(String frgsx) {
        this.frgsx = frgsx;
    }

    public String getFrgke() {
        return frgke;
    }

    public void setFrgke(String frgke) {
        this.frgke = frgke;
    }

    public String getFrgzu() {
        return frgzu;
    }

    public void setFrgzu(String frgzu) {
        this.frgzu = frgzu;
    }

    public BigDecimal getRlwrt() {
        return rlwrt;
    }

    public void setRlwrt(BigDecimal rlwrt) {
        this.rlwrt = rlwrt;
    }

    public Date getUdate() {
        return udate;
    }

    public void setUdate(Date udate) {
        this.udate = udate;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getStrSuppl1() {
        return strSuppl1;
    }

    public void setStrSuppl1(String strSuppl1) {
        this.strSuppl1 = strSuppl1;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getAdrnr() {
        return adrnr;
    }

    public void setAdrnr(String adrnr) {
        this.adrnr = adrnr;
    }

    public Date getAngdt() {
        return angdt;
    }

    public void setAngdt(Date angdt) {
        this.angdt = angdt;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EtRfqEkko)) {
            return false;
        }
        EtRfqEkko other = (EtRfqEkko) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.EtRfqEkko[ id=" + id + " ]";
    }
    
}
