/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carl.lin
 */
@Entity
@Table(name = "ZRT_BSEG_ARTL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZrtBsegArtl.findAll", query = "SELECT z FROM ZrtBsegArtl z"),
    @NamedQuery(name = "ZrtBsegArtl.findByMandt", query = "SELECT z FROM ZrtBsegArtl z WHERE z.mandt = :mandt"),
    @NamedQuery(name = "ZrtBsegArtl.findByBukrs", query = "SELECT z FROM ZrtBsegArtl z WHERE z.bukrs = :bukrs"),
    @NamedQuery(name = "ZrtBsegArtl.findByGjahr", query = "SELECT z FROM ZrtBsegArtl z WHERE z.gjahr = :gjahr"),
    @NamedQuery(name = "ZrtBsegArtl.findByMonat", query = "SELECT z FROM ZrtBsegArtl z WHERE z.monat = :monat"),
    @NamedQuery(name = "ZrtBsegArtl.findBySarea", query = "SELECT z FROM ZrtBsegArtl z WHERE z.sarea = :sarea"),
    @NamedQuery(name = "ZrtBsegArtl.findByVkgrp", query = "SELECT z FROM ZrtBsegArtl z WHERE z.vkgrp = :vkgrp"),
    @NamedQuery(name = "ZrtBsegArtl.findByKunnr", query = "SELECT z FROM ZrtBsegArtl z WHERE z.kunnr = :kunnr"),
    @NamedQuery(name = "ZrtBsegArtl.findByAmttot", query = "SELECT z FROM ZrtBsegArtl z WHERE z.amttot = :amttot"),
    @NamedQuery(name = "ZrtBsegArtl.findByAmt000", query = "SELECT z FROM ZrtBsegArtl z WHERE z.amt000 = :amt000"),
    @NamedQuery(name = "ZrtBsegArtl.findByAmt030", query = "SELECT z FROM ZrtBsegArtl z WHERE z.amt030 = :amt030"),
    @NamedQuery(name = "ZrtBsegArtl.findByAmt060", query = "SELECT z FROM ZrtBsegArtl z WHERE z.amt060 = :amt060"),
    @NamedQuery(name = "ZrtBsegArtl.findByAmt090", query = "SELECT z FROM ZrtBsegArtl z WHERE z.amt090 = :amt090"),
    @NamedQuery(name = "ZrtBsegArtl.findByAmt120", query = "SELECT z FROM ZrtBsegArtl z WHERE z.amt120 = :amt120"),
    @NamedQuery(name = "ZrtBsegArtl.findByAmt150", query = "SELECT z FROM ZrtBsegArtl z WHERE z.amt150 = :amt150"),
    @NamedQuery(name = "ZrtBsegArtl.findByAmt180", query = "SELECT z FROM ZrtBsegArtl z WHERE z.amt180 = :amt180"),
    @NamedQuery(name = "ZrtBsegArtl.findByAmt270", query = "SELECT z FROM ZrtBsegArtl z WHERE z.amt270 = :amt270"),
    @NamedQuery(name = "ZrtBsegArtl.findByAmt365", query = "SELECT z FROM ZrtBsegArtl z WHERE z.amt365 = :amt365"),
    @NamedQuery(name = "ZrtBsegArtl.findByAmtlyr", query = "SELECT z FROM ZrtBsegArtl z WHERE z.amtlyr = :amtlyr"),
    @NamedQuery(name = "ZrtBsegArtl.findByName1", query = "SELECT z FROM ZrtBsegArtl z WHERE z.name1 = :name1"),
    @NamedQuery(name = "ZrtBsegArtl.findById", query = "SELECT z FROM ZrtBsegArtl z WHERE z.id = :id")})
public class ZrtBsegArtl implements Serializable {
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
    @Size(min = 1, max = 4)
    @Column(name = "GJAHR")
    private String gjahr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "MONAT")
    private String monat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "SAREA")
    private String sarea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "VKGRP")
    private String vkgrp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "KUNNR")
    private String kunnr;
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
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;

    public ZrtBsegArtl() {
    }

    public ZrtBsegArtl(Long id) {
        this.id = id;
    }

    public ZrtBsegArtl(Long id, String bukrs, String gjahr, String monat, String sarea, String vkgrp, String kunnr) {
        this.id = id;
        this.bukrs = bukrs;
        this.gjahr = gjahr;
        this.monat = monat;
        this.sarea = sarea;
        this.vkgrp = vkgrp;
        this.kunnr = kunnr;
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

    public String getGjahr() {
        return gjahr;
    }

    public void setGjahr(String gjahr) {
        this.gjahr = gjahr;
    }

    public String getMonat() {
        return monat;
    }

    public void setMonat(String monat) {
        this.monat = monat;
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

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
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
        if (!(object instanceof ZrtBsegArtl)) {
            return false;
        }
        ZrtBsegArtl other = (ZrtBsegArtl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ZrtBsegArtl[ id=" + id + " ]";
    }
    
}
