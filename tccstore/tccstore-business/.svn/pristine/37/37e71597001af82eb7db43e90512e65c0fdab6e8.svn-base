/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity.datawarehouse;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Neo.Fu
 */
@Entity
@Table(name = "ZORDER_CN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZorderCn.findAll", query = "SELECT z FROM ZorderCn z")})
public class ZorderCn implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "VKORG")
    private String vkorg;
    @Size(max = 40)
    @Column(name = "VKORG_TX")
    private String vkorgTx;
    @Size(max = 2)
    @Column(name = "VTWEG")
    private String vtweg;
    @Size(max = 40)
    @Column(name = "VTWEG_TX")
    private String vtwegTx;
    @Size(max = 2)
    @Column(name = "SPART")
    private String spart;
    @Size(max = 40)
    @Column(name = "SPART_TX")
    private String spartTx;
    @Size(max = 10)
    @Column(name = "VKBUR")
    private String vkbur;
    @Size(max = 70)
    @Column(name = "VKBUR_TX")
    private String vkburTx;
    @Size(max = 10)
    @Column(name = "KUNNR")
    private String kunnr;
    @Size(max = 70)
    @Column(name = "KUNNR_TX")
    private String kunnrTx;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "AUDAT")
    private String audat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "WADAT")
    private String wadat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "FKDAT")
    private String fkdat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "VBELN")
    private String vbeln;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "VGBEL")
    private String vgbel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "AUBEL")
    private String aubel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "AUPOS")
    private String aupos;
    @Size(max = 18)
    @Column(name = "MATNR")
    private String matnr;
    @Size(max = 80)
    @Column(name = "ARKTX")
    private String arktx;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "FKIMG")
    private BigDecimal fkimg;
    @Size(max = 3)
    @Column(name = "VRKME")
    private String vrkme;
    @Column(name = "CMPRE")
    private BigDecimal cmpre;
    @Column(name = "KWERT")
    private BigDecimal kwert;
    @Size(max = 5)
    @Column(name = "WAERS")
    private String waers;
    @Size(max = 10)
    @Column(name = "WERKS")
    private String werks;
    @Size(max = 70)
    @Column(name = "WERKS_TX")
    private String werksTx;
    @Size(max = 3)
    @Column(name = "INCO1")
    private String inco1;
    @Size(max = 60)
    @Column(name = "INCO1_TX")
    private String inco1Tx;
    @Size(max = 6)
    @Column(name = "BZIRK")
    private String bzirk;
    @Size(max = 40)
    @Column(name = "BZTXT")
    private String bztxt;
    @Size(max = 2)
    @Column(name = "KDGRP")
    private String kdgrp;
    @Size(max = 40)
    @Column(name = "KTEXT")
    private String ktext;
    @Size(max = 40)
    @Column(name = "XBLNR")
    private String xblnr;
    @Size(max = 110)
    @Column(name = "CHANGMAT2")
    private String changmat2;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 8)
    @Column(name = "ERDAT")
    private String erdat;
    @Size(max = 8)
    @Column(name = "AEDAT")
    private String aedat;
    @Size(max = 1)
    @Column(name = "FLAG")
    private String flag;
    @Size(max = 8)
    @Column(name = "UATBG")
    private String uatbg;
    @Size(max = 2)
    @Column(name = "VSART")
    private String vsart;
    @Size(max = 40)
    @Column(name = "BEZEI")
    private String bezei;
    @Size(max = 8)
    @Column(name = "PERNR")
    private String pernr;
    @Size(max = 40)
    @Column(name = "ENAME")
    private String ename;
    @Column(name = "Z5_KUNNR")
    private String z5Kunnr;
    @Column(name = "Z4_DESC")
    private String z4Desc;
    @Column(name = "Z5_DESC")
    private String z5Desc;
    @Column(name = "Z4_KUNNR")
    private String z4Kunnr;
    
    public ZorderCn() {
    }

    public ZorderCn(BigDecimal id) {
        this.id = id;
    }

    public ZorderCn(BigDecimal id, String vkorg, String audat, String wadat, String fkdat, String vbeln, String vgbel, String aubel, String aupos) {
        this.id = id;
        this.vkorg = vkorg;
        this.audat = audat;
        this.wadat = wadat;
        this.fkdat = fkdat;
        this.vbeln = vbeln;
        this.vgbel = vgbel;
        this.aubel = aubel;
        this.aupos = aupos;
    }

    public String getZ5Kunnr() {
        return z5Kunnr;
    }

    public void setZ5Kunnr(String z5Kunnr) {
        this.z5Kunnr = z5Kunnr;
    }

    public String getZ4Desc() {
        return z4Desc;
    }

    public void setZ4Desc(String z4Desc) {
        this.z4Desc = z4Desc;
    }

    public String getZ5Desc() {
        return z5Desc;
    }

    public void setZ5Desc(String z5Desc) {
        this.z5Desc = z5Desc;
    }

    public String getZ4Kunnr() {
        return z4Kunnr;
    }

    public void setZ4Kunnr(String z4Kunnr) {
        this.z4Kunnr = z4Kunnr;
    }
    
    public String getVsart() {
        return vsart;
    }

    public void setVsart(String vsart) {
        this.vsart = vsart;
    }

    public String getBezei() {
        return bezei;
    }

    public void setBezei(String bezei) {
        this.bezei = bezei;
    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getUatbg() {
        return uatbg;
    }

    public void setUatbg(String uatbg) {
        this.uatbg = uatbg;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public String getVkorgTx() {
        return vkorgTx;
    }

    public void setVkorgTx(String vkorgTx) {
        this.vkorgTx = vkorgTx;
    }

    public String getVtweg() {
        return vtweg;
    }

    public void setVtweg(String vtweg) {
        this.vtweg = vtweg;
    }

    public String getVtwegTx() {
        return vtwegTx;
    }

    public void setVtwegTx(String vtwegTx) {
        this.vtwegTx = vtwegTx;
    }

    public String getSpart() {
        return spart;
    }

    public void setSpart(String spart) {
        this.spart = spart;
    }

    public String getSpartTx() {
        return spartTx;
    }

    public void setSpartTx(String spartTx) {
        this.spartTx = spartTx;
    }

    public String getVkbur() {
        return vkbur;
    }

    public void setVkbur(String vkbur) {
        this.vkbur = vkbur;
    }

    public String getVkburTx() {
        return vkburTx;
    }

    public void setVkburTx(String vkburTx) {
        this.vkburTx = vkburTx;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getKunnrTx() {
        return kunnrTx;
    }

    public void setKunnrTx(String kunnrTx) {
        this.kunnrTx = kunnrTx;
    }

    public String getAudat() {
        return audat;
    }

    public void setAudat(String audat) {
        this.audat = audat;
    }

    public String getWadat() {
        return wadat;
    }

    public void setWadat(String wadat) {
        this.wadat = wadat;
    }

    public String getFkdat() {
        return fkdat;
    }

    public void setFkdat(String fkdat) {
        this.fkdat = fkdat;
    }

    public String getVbeln() {
        return vbeln;
    }

    public void setVbeln(String vbeln) {
        this.vbeln = vbeln;
    }

    public String getVgbel() {
        return vgbel;
    }

    public void setVgbel(String vgbel) {
        this.vgbel = vgbel;
    }

    public String getAubel() {
        return aubel;
    }

    public void setAubel(String aubel) {
        this.aubel = aubel;
    }

    public String getAupos() {
        return aupos;
    }

    public void setAupos(String aupos) {
        this.aupos = aupos;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getArktx() {
        return arktx;
    }

    public void setArktx(String arktx) {
        this.arktx = arktx;
    }

    public BigDecimal getFkimg() {
        return fkimg;
    }

    public void setFkimg(BigDecimal fkimg) {
        this.fkimg = fkimg;
    }

    public String getVrkme() {
        return vrkme;
    }

    public void setVrkme(String vrkme) {
        this.vrkme = vrkme;
    }

    public BigDecimal getCmpre() {
        return cmpre;
    }

    public void setCmpre(BigDecimal cmpre) {
        this.cmpre = cmpre;
    }

    public BigDecimal getKwert() {
        return kwert;
    }

    public void setKwert(BigDecimal kwert) {
        this.kwert = kwert;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public String getWerksTx() {
        return werksTx;
    }

    public void setWerksTx(String werksTx) {
        this.werksTx = werksTx;
    }

    public String getInco1() {
        return inco1;
    }

    public void setInco1(String inco1) {
        this.inco1 = inco1;
    }

    public String getInco1Tx() {
        return inco1Tx;
    }

    public void setInco1Tx(String inco1Tx) {
        this.inco1Tx = inco1Tx;
    }

    public String getBzirk() {
        return bzirk;
    }

    public void setBzirk(String bzirk) {
        this.bzirk = bzirk;
    }

    public String getBztxt() {
        return bztxt;
    }

    public void setBztxt(String bztxt) {
        this.bztxt = bztxt;
    }

    public String getKdgrp() {
        return kdgrp;
    }

    public void setKdgrp(String kdgrp) {
        this.kdgrp = kdgrp;
    }

    public String getKtext() {
        return ktext;
    }

    public void setKtext(String ktext) {
        this.ktext = ktext;
    }

    public String getXblnr() {
        return xblnr;
    }

    public void setXblnr(String xblnr) {
        this.xblnr = xblnr;
    }

    public String getChangmat2() {
        return changmat2;
    }

    public void setChangmat2(String changmat2) {
        this.changmat2 = changmat2;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getErdat() {
        return erdat;
    }

    public void setErdat(String erdat) {
        this.erdat = erdat;
    }

    public String getAedat() {
        return aedat;
    }

    public void setAedat(String aedat) {
        this.aedat = aedat;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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
        if (!(object instanceof ZorderCn)) {
            return false;
        }
        ZorderCn other = (ZorderCn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.crm.facade.ZorderCn[ id=" + id + " ]";
    }
    
}
