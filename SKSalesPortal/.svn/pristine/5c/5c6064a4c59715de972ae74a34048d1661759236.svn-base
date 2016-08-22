package com.tcci.worklist.entity.datawarehouse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nEO.Fu
 */
@Entity
@Cacheable(false)
@Table(name = "SD_ZTAB_EXP_VBAK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZtabExpVbak.findAll", query = "SELECT z FROM ZtabExpVbak z"),
    @NamedQuery(name = "ZtabExpVbak.findByMandt", query = "SELECT z FROM ZtabExpVbak z WHERE z.ztabExpVbakPK.mandt = :mandt"),
    @NamedQuery(name = "ZtabExpVbak.findByVbeln", query = "SELECT z FROM ZtabExpVbak z WHERE z.ztabExpVbakPK.vbeln = :vbeln"),
    @NamedQuery(name = "ZtabExpVbak.findByErdat", query = "SELECT z FROM ZtabExpVbak z WHERE z.erdat = :erdat"),
    @NamedQuery(name = "ZtabExpVbak.findByErzet", query = "SELECT z FROM ZtabExpVbak z WHERE z.erzet = :erzet"),
    @NamedQuery(name = "ZtabExpVbak.findByErnam", query = "SELECT z FROM ZtabExpVbak z WHERE z.ernam = :ernam"),
    @NamedQuery(name = "ZtabExpVbak.findByAudat", query = "SELECT z FROM ZtabExpVbak z WHERE z.audat = :audat"),
    @NamedQuery(name = "ZtabExpVbak.findByVbtyp", query = "SELECT z FROM ZtabExpVbak z WHERE z.vbtyp = :vbtyp"),
    @NamedQuery(name = "ZtabExpVbak.findByAuart", query = "SELECT z FROM ZtabExpVbak z WHERE z.auart = :auart"),
    @NamedQuery(name = "ZtabExpVbak.findByAugru", query = "SELECT z FROM ZtabExpVbak z WHERE z.augru = :augru"),
    @NamedQuery(name = "ZtabExpVbak.findByNetwr", query = "SELECT z FROM ZtabExpVbak z WHERE z.netwr = :netwr"),
    @NamedQuery(name = "ZtabExpVbak.findByWaerk", query = "SELECT z FROM ZtabExpVbak z WHERE z.waerk = :waerk"),
    @NamedQuery(name = "ZtabExpVbak.findByVkorg", query = "SELECT z FROM ZtabExpVbak z WHERE z.vkorg = :vkorg"),
    @NamedQuery(name = "ZtabExpVbak.findByVtweg", query = "SELECT z FROM ZtabExpVbak z WHERE z.vtweg = :vtweg"),
    @NamedQuery(name = "ZtabExpVbak.findBySpart", query = "SELECT z FROM ZtabExpVbak z WHERE z.spart = :spart"),
    @NamedQuery(name = "ZtabExpVbak.findByVkbur", query = "SELECT z FROM ZtabExpVbak z WHERE z.vkbur = :vkbur"),
    @NamedQuery(name = "ZtabExpVbak.findByVkgrp", query = "SELECT z FROM ZtabExpVbak z WHERE z.vkgrp = :vkgrp"),
    @NamedQuery(name = "ZtabExpVbak.findByVdatu", query = "SELECT z FROM ZtabExpVbak z WHERE z.vdatu = :vdatu"),
    @NamedQuery(name = "ZtabExpVbak.findByKunnr", query = "SELECT z FROM ZtabExpVbak z WHERE z.kunnr = :kunnr"),
    @NamedQuery(name = "ZtabExpVbak.findByAedat", query = "SELECT z FROM ZtabExpVbak z WHERE z.aedat = :aedat"),
    @NamedQuery(name = "ZtabExpVbak.findByVgbel", query = "SELECT z FROM ZtabExpVbak z WHERE z.vgbel = :vgbel"),
    @NamedQuery(name = "ZtabExpVbak.findByObjnr", query = "SELECT z FROM ZtabExpVbak z WHERE z.objnr = :objnr"),
    @NamedQuery(name = "ZtabExpVbak.findByXblnr", query = "SELECT z FROM ZtabExpVbak z WHERE z.xblnr = :xblnr"),
    @NamedQuery(name = "ZtabExpVbak.findByZuonr", query = "SELECT z FROM ZtabExpVbak z WHERE z.zuonr = :zuonr"),
    @NamedQuery(name = "ZtabExpVbak.findByVgtyp", query = "SELECT z FROM ZtabExpVbak z WHERE z.vgtyp = :vgtyp")})
public class ZtabExpVbak implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZtabExpVbakPK ztabExpVbakPK;
    @Column(name = "ERDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date erdat;
    @Column(name = "ERZET")
    @Temporal(TemporalType.TIMESTAMP)
    private Date erzet;
    @Size(max = 24)
    @Column(name = "ERNAM")
    private String ernam;
    @Column(name = "AUDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date audat;
    @Size(max = 2)
    @Column(name = "VBTYP")
    private String vbtyp;
    @Size(max = 8)
    @Column(name = "AUART")
    private String auart;
    @Size(max = 6)
    @Column(name = "AUGRU")
    private String augru;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "NETWR")
    private BigDecimal netwr;
    @Size(max = 10)
    @Column(name = "WAERK")
    private String waerk;
    @Size(max = 8)
    @Column(name = "VKORG")
    private String vkorg;
    @Size(max = 4)
    @Column(name = "VTWEG")
    private String vtweg;
    @Size(max = 4)
    @Column(name = "SPART")
    private String spart;
    @Size(max = 8)
    @Column(name = "VKBUR")
    private String vkbur;
    @Size(max = 6)
    @Column(name = "VKGRP")
    private String vkgrp;
    @Column(name = "VDATU")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vdatu;
    @Size(max = 20)
    @Column(name = "KUNNR")
    private String kunnr;
    @Column(name = "AEDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date aedat;
    @Size(max = 20)
    @Column(name = "VGBEL")
    private String vgbel;
    @Size(max = 44)
    @Column(name = "OBJNR")
    private String objnr;
    @Size(max = 32)
    @Column(name = "XBLNR")
    private String xblnr;
    @Size(max = 36)
    @Column(name = "ZUONR")
    private String zuonr;
    @Size(max = 2)
    @Column(name = "VGTYP")
    private String vgtyp;
    @JoinColumn(name = "SD_ZTAB_EXP_RELFILENO_SD_ID", referencedColumnName = "ID")
    @ManyToOne
    private ZtabExpRelfilenoSd sdZtabExpRelfilenoSdId;
    

    public ZtabExpVbak() {
    }

    public ZtabExpVbak(ZtabExpVbakPK ztabExpVbakPK) {
        this.ztabExpVbakPK = ztabExpVbakPK;
    }

    public ZtabExpVbak(String mandt, String vbeln) {
        this.ztabExpVbakPK = new ZtabExpVbakPK(mandt, vbeln);
    }

    public ZtabExpVbakPK getZtabExpVbakPK() {
        return ztabExpVbakPK;
    }

    public void setZtabExpVbakPK(ZtabExpVbakPK ztabExpVbakPK) {
        this.ztabExpVbakPK = ztabExpVbakPK;
    }

    public Date getErdat() {
        return erdat;
    }

    public void setErdat(Date erdat) {
        this.erdat = erdat;
    }

    public Date getErzet() {
        return erzet;
    }

    public void setErzet(Date erzet) {
        this.erzet = erzet;
    }

    public String getErnam() {
        return ernam;
    }

    public void setErnam(String ernam) {
        this.ernam = ernam;
    }

    public Date getAudat() {
        return audat;
    }

    public void setAudat(Date audat) {
        this.audat = audat;
    }

    public String getVbtyp() {
        return vbtyp;
    }

    public void setVbtyp(String vbtyp) {
        this.vbtyp = vbtyp;
    }

    public String getAuart() {
        return auart;
    }

    public void setAuart(String auart) {
        this.auart = auart;
    }

    public String getAugru() {
        return augru;
    }

    public void setAugru(String augru) {
        this.augru = augru;
    }

    public BigDecimal getNetwr() {
        return netwr;
    }

    public void setNetwr(BigDecimal netwr) {
        this.netwr = netwr;
    }

    public String getWaerk() {
        return waerk;
    }

    public void setWaerk(String waerk) {
        this.waerk = waerk;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public String getVtweg() {
        return vtweg;
    }

    public void setVtweg(String vtweg) {
        this.vtweg = vtweg;
    }

    public String getSpart() {
        return spart;
    }

    public void setSpart(String spart) {
        this.spart = spart;
    }

    public String getVkbur() {
        return vkbur;
    }

    public void setVkbur(String vkbur) {
        this.vkbur = vkbur;
    }

    public String getVkgrp() {
        return vkgrp;
    }

    public void setVkgrp(String vkgrp) {
        this.vkgrp = vkgrp;
    }

    public Date getVdatu() {
        return vdatu;
    }

    public void setVdatu(Date vdatu) {
        this.vdatu = vdatu;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public Date getAedat() {
        return aedat;
    }

    public void setAedat(Date aedat) {
        this.aedat = aedat;
    }

    public String getVgbel() {
        return vgbel;
    }

    public void setVgbel(String vgbel) {
        this.vgbel = vgbel;
    }

    public String getObjnr() {
        return objnr;
    }

    public void setObjnr(String objnr) {
        this.objnr = objnr;
    }

    public String getXblnr() {
        return xblnr;
    }

    public void setXblnr(String xblnr) {
        this.xblnr = xblnr;
    }

    public String getZuonr() {
        return zuonr;
    }

    public void setZuonr(String zuonr) {
        this.zuonr = zuonr;
    }

    public String getVgtyp() {
        return vgtyp;
    }

    public void setVgtyp(String vgtyp) {
        this.vgtyp = vgtyp;
    }

    public ZtabExpRelfilenoSd getSdZtabExpRelfilenoSdId() {
        return sdZtabExpRelfilenoSdId;
    }

    public void setSdZtabExpRelfilenoSdId(ZtabExpRelfilenoSd sdZtabExpRelfilenoSdId) {
        this.sdZtabExpRelfilenoSdId = sdZtabExpRelfilenoSdId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ztabExpVbakPK != null ? ztabExpVbakPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpVbak)) {
            return false;
        }
        ZtabExpVbak other = (ZtabExpVbak) object;
        if ((this.ztabExpVbakPK == null && other.ztabExpVbakPK != null) || (this.ztabExpVbakPK != null && !this.ztabExpVbakPK.equals(other.ztabExpVbakPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.mygui.entity.datawarehouse.ZtabExpVbak[ ztabExpVbakPK=" + ztabExpVbakPK + " ]";
    }
    
}
