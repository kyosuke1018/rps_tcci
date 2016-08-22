/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carl.lin
 */
@Entity
@Table(name = "ZRT_BSEG_ARAG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZrtBsegArag.findAll", query = "SELECT z FROM ZrtBsegArag z"),
    @NamedQuery(name = "ZrtBsegArag.findByMandt", query = "SELECT z FROM ZrtBsegArag z WHERE z.mandt = :mandt"),
    @NamedQuery(name = "ZrtBsegArag.findByBukrs", query = "SELECT z FROM ZrtBsegArag z WHERE z.bukrs = :bukrs"),
    @NamedQuery(name = "ZrtBsegArag.findByBelnr", query = "SELECT z FROM ZrtBsegArag z WHERE z.belnr = :belnr"),
    @NamedQuery(name = "ZrtBsegArag.findByGjahr", query = "SELECT z FROM ZrtBsegArag z WHERE z.gjahr = :gjahr"),
    @NamedQuery(name = "ZrtBsegArag.findByBuzei", query = "SELECT z FROM ZrtBsegArag z WHERE z.buzei = :buzei"),
    @NamedQuery(name = "ZrtBsegArag.findByKunnr", query = "SELECT z FROM ZrtBsegArag z WHERE z.kunnr = :kunnr"),
    @NamedQuery(name = "ZrtBsegArag.findByZfbdt", query = "SELECT z FROM ZrtBsegArag z WHERE z.zfbdt = :zfbdt"),
    @NamedQuery(name = "ZrtBsegArag.findByZterm", query = "SELECT z FROM ZrtBsegArag z WHERE z.zterm = :zterm"),
    @NamedQuery(name = "ZrtBsegArag.findByZbd1t", query = "SELECT z FROM ZrtBsegArag z WHERE z.zbd1t = :zbd1t"),
    @NamedQuery(name = "ZrtBsegArag.findByZivdt", query = "SELECT z FROM ZrtBsegArag z WHERE z.zivdt = :zivdt"),
    @NamedQuery(name = "ZrtBsegArag.findByZbelnr", query = "SELECT z FROM ZrtBsegArag z WHERE z.zbelnr = :zbelnr"),
    @NamedQuery(name = "ZrtBsegArag.findByZgjahr", query = "SELECT z FROM ZrtBsegArag z WHERE z.zgjahr = :zgjahr"),
    @NamedQuery(name = "ZrtBsegArag.findByZhzuon", query = "SELECT z FROM ZrtBsegArag z WHERE z.zhzuon = :zhzuon"),
    @NamedQuery(name = "ZrtBsegArag.findByAntype", query = "SELECT z FROM ZrtBsegArag z WHERE z.antype = :antype"),
    @NamedQuery(name = "ZrtBsegArag.findByAndate", query = "SELECT z FROM ZrtBsegArag z WHERE z.andate = :andate"),
    @NamedQuery(name = "ZrtBsegArag.findBySarea", query = "SELECT z FROM ZrtBsegArag z WHERE z.sarea = :sarea"),
    @NamedQuery(name = "ZrtBsegArag.findByVkgrp", query = "SELECT z FROM ZrtBsegArag z WHERE z.vkgrp = :vkgrp"),
    @NamedQuery(name = "ZrtBsegArag.findByArday", query = "SELECT z FROM ZrtBsegArag z WHERE z.arday = :arday"),
    @NamedQuery(name = "ZrtBsegArag.findByAmttot", query = "SELECT z FROM ZrtBsegArag z WHERE z.amttot = :amttot"),
    @NamedQuery(name = "ZrtBsegArag.findByAmt000", query = "SELECT z FROM ZrtBsegArag z WHERE z.amt000 = :amt000"),
    @NamedQuery(name = "ZrtBsegArag.findByAmt030", query = "SELECT z FROM ZrtBsegArag z WHERE z.amt030 = :amt030"),
    @NamedQuery(name = "ZrtBsegArag.findByAmt060", query = "SELECT z FROM ZrtBsegArag z WHERE z.amt060 = :amt060"),
    @NamedQuery(name = "ZrtBsegArag.findByAmt090", query = "SELECT z FROM ZrtBsegArag z WHERE z.amt090 = :amt090"),
    @NamedQuery(name = "ZrtBsegArag.findByAmt120", query = "SELECT z FROM ZrtBsegArag z WHERE z.amt120 = :amt120"),
    @NamedQuery(name = "ZrtBsegArag.findByAmt150", query = "SELECT z FROM ZrtBsegArag z WHERE z.amt150 = :amt150"),
    @NamedQuery(name = "ZrtBsegArag.findByAmt180", query = "SELECT z FROM ZrtBsegArag z WHERE z.amt180 = :amt180"),
    @NamedQuery(name = "ZrtBsegArag.findByAmt270", query = "SELECT z FROM ZrtBsegArag z WHERE z.amt270 = :amt270"),
    @NamedQuery(name = "ZrtBsegArag.findByAmt365", query = "SELECT z FROM ZrtBsegArag z WHERE z.amt365 = :amt365"),
    @NamedQuery(name = "ZrtBsegArag.findByAmtlyr", query = "SELECT z FROM ZrtBsegArag z WHERE z.amtlyr = :amtlyr"),
    @NamedQuery(name = "ZrtBsegArag.findByName1", query = "SELECT z FROM ZrtBsegArag z WHERE z.name1 = :name1"),
    @NamedQuery(name = "ZrtBsegArag.findByZuonr", query = "SELECT z FROM ZrtBsegArag z WHERE z.zuonr = :zuonr"),
    @NamedQuery(name = "ZrtBsegArag.findBySgtxt", query = "SELECT z FROM ZrtBsegArag z WHERE z.sgtxt = :sgtxt"),
    @NamedQuery(name = "ZrtBsegArag.findById", query = "SELECT z FROM ZrtBsegArag z WHERE z.id = :id")})
public class ZrtBsegArag implements Serializable {
    private static final long serialVersionUID = 1L;
    @Size(max = 20)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "BUKRS")
    private String bukrs;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "BELNR")
    private String belnr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "GJAHR")
    private String gjahr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "BUZEI")
    private String buzei;
    @Size(max = 10)
    @Column(name = "KUNNR")
    private String kunnr;
    @Size(max = 8)
    @Column(name = "ZFBDT")
    private String zfbdt;
    @Size(max = 4)
    @Column(name = "ZTERM")
    private String zterm;
    @Column(name = "ZBD1T")
    private BigInteger zbd1t;
    @Size(max = 8)
    @Column(name = "ZIVDT")
    private String zivdt;
    @Size(max = 10)
    @Column(name = "ZBELNR")
    private String zbelnr;
    @Size(max = 4)
    @Column(name = "ZGJAHR")
    private String zgjahr;
    @Size(max = 10)
    @Column(name = "ZHZUON")
    private String zhzuon;
    @Size(max = 2)
    @Column(name = "ANTYPE")
    private String antype;
    @Size(max = 8)
    @Column(name = "ANDATE")
    private String andate;
    @Size(max = 2)
    @Column(name = "SAREA")
    private String sarea;
    @Size(max = 3)
    @Column(name = "VKGRP")
    private String vkgrp;
    @Column(name = "ARDAY")
    private Integer arday;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AMTTOT")
    private BigDecimal amttot;
    @Column(name = "AMT000")
    private BigDecimal amt000;
    @Column(name = "AMT030")
    private BigDecimal amt030;
    @Column(name = "AMT060")
    private BigDecimal amt060;
    @Column(name = "AMT090")
    private BigDecimal amt090;
    @Column(name = "AMT120")
    private BigDecimal amt120;
    @Column(name = "AMT150")
    private BigDecimal amt150;
    @Column(name = "AMT180")
    private BigDecimal amt180;
    @Column(name = "AMT270")
    private BigDecimal amt270;
    @Column(name = "AMT365")
    private BigDecimal amt365;
    @Column(name = "AMTLYR")
    private BigDecimal amtlyr;
    @Size(max = 35)
    @Column(name = "NAME1")
    private String name1;
    @Size(max = 18)
    @Column(name = "ZUONR")
    private String zuonr;
    @Size(max = 50)
    @Column(name = "SGTXT")
    private String sgtxt;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;

    public ZrtBsegArag() {
    }

    public ZrtBsegArag(Long id) {
        this.id = id;
    }

    public ZrtBsegArag(Long id, String bukrs, String belnr, String gjahr, String buzei) {
        this.id = id;
        this.bukrs = bukrs;
        this.belnr = belnr;
        this.gjahr = gjahr;
        this.buzei = buzei;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    public String getBelnr() {
        return belnr;
    }

    public void setBelnr(String belnr) {
        this.belnr = belnr;
    }

    public String getGjahr() {
        return gjahr;
    }

    public void setGjahr(String gjahr) {
        this.gjahr = gjahr;
    }

    public String getBuzei() {
        return buzei;
    }

    public void setBuzei(String buzei) {
        this.buzei = buzei;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getZfbdt() {
        return zfbdt;
    }

    public void setZfbdt(String zfbdt) {
        this.zfbdt = zfbdt;
    }

    public String getZterm() {
        return zterm;
    }

    public void setZterm(String zterm) {
        this.zterm = zterm;
    }

    public BigInteger getZbd1t() {
        return zbd1t;
    }

    public void setZbd1t(BigInteger zbd1t) {
        this.zbd1t = zbd1t;
    }

    public String getZivdt() {
        return zivdt;
    }

    public void setZivdt(String zivdt) {
        this.zivdt = zivdt;
    }

    public String getZbelnr() {
        return zbelnr;
    }

    public void setZbelnr(String zbelnr) {
        this.zbelnr = zbelnr;
    }

    public String getZgjahr() {
        return zgjahr;
    }

    public void setZgjahr(String zgjahr) {
        this.zgjahr = zgjahr;
    }

    public String getZhzuon() {
        return zhzuon;
    }

    public void setZhzuon(String zhzuon) {
        this.zhzuon = zhzuon;
    }

    public String getAntype() {
        return antype;
    }

    public void setAntype(String antype) {
        this.antype = antype;
    }

    public String getAndate() {
        return andate;
    }

    public void setAndate(String andate) {
        this.andate = andate;
    }

    public String getSarea() {
        return sarea;
    }

    public void setSarea(String sarea) {
        this.sarea = sarea;
    }

    public String getVkgrp() {
        return vkgrp;
    }

    public void setVkgrp(String vkgrp) {
        this.vkgrp = vkgrp;
    }

    public Integer getArday() {
        return arday;
    }

    public void setArday(Integer arday) {
        this.arday = arday;
    }

    public BigDecimal getAmttot() {
        return amttot;
    }

    public void setAmttot(BigDecimal amttot) {
        this.amttot = amttot;
    }

    public BigDecimal getAmt000() {
        return amt000;
    }

    public void setAmt000(BigDecimal amt000) {
        this.amt000 = amt000;
    }

    public BigDecimal getAmt030() {
        return amt030;
    }

    public void setAmt030(BigDecimal amt030) {
        this.amt030 = amt030;
    }

    public BigDecimal getAmt060() {
        return amt060;
    }

    public void setAmt060(BigDecimal amt060) {
        this.amt060 = amt060;
    }

    public BigDecimal getAmt090() {
        return amt090;
    }

    public void setAmt090(BigDecimal amt090) {
        this.amt090 = amt090;
    }

    public BigDecimal getAmt120() {
        return amt120;
    }

    public void setAmt120(BigDecimal amt120) {
        this.amt120 = amt120;
    }

    public BigDecimal getAmt150() {
        return amt150;
    }

    public void setAmt150(BigDecimal amt150) {
        this.amt150 = amt150;
    }

    public BigDecimal getAmt180() {
        return amt180;
    }

    public void setAmt180(BigDecimal amt180) {
        this.amt180 = amt180;
    }

    public BigDecimal getAmt270() {
        return amt270;
    }

    public void setAmt270(BigDecimal amt270) {
        this.amt270 = amt270;
    }

    public BigDecimal getAmt365() {
        return amt365;
    }

    public void setAmt365(BigDecimal amt365) {
        this.amt365 = amt365;
    }

    public BigDecimal getAmtlyr() {
        return amtlyr;
    }

    public void setAmtlyr(BigDecimal amtlyr) {
        this.amtlyr = amtlyr;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getZuonr() {
        return zuonr;
    }

    public void setZuonr(String zuonr) {
        this.zuonr = zuonr;
    }

    public String getSgtxt() {
        return sgtxt;
    }

    public void setSgtxt(String sgtxt) {
        this.sgtxt = sgtxt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof ZrtBsegArag)) {
            return false;
        }
        ZrtBsegArag other = (ZrtBsegArag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ZrtBsegArag[ id=" + id + " ]";
    }
    
}
