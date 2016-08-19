/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.entity.sheetdata;

import com.tcci.fc.util.time.DateUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gilbert.Lin
 */
@Entity
@Table(name = "ZTFI_AFRC_INVO")
@NamedQueries({
    @NamedQuery(name = "ZtfiAfrcInvo.findAll", query = "SELECT z FROM ZtfiAfrcInvo z")})
public class ZtfiAfrcInvo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
//    @Basic(optional = false)
//    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)    
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ZGJAHR")
    private short zgjahr;//對帳年度
    @Basic(optional = false)
    @NotNull
    @Column(name = "ZMONAT")
    private short zmonat;//對帳期間
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "BUKRS")
    private String bukrs;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "ZAFBUK")
    private String zafbuk;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ZAFCAT")
    private String zafcat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "ZAFTYP")
    private String zaftyp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GJAHR")
    private short gjahr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "BELNR")
    private String belnr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 14)
    @Column(name = "GUINO")
    private String guino;
    @Size(max = 10)
    @Column(name = "KUNNR")
    private String kunnr;
    @Size(max = 10)
    @Column(name = "LIFNR")
    private String lifnr;
    @Size(max = 8)
    @Column(name = "GUI_REGIS")
    private String guiRegis;
    @Column(name = "BUDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date budat;
    @Size(max = 5)
    @Column(name = "WAERS")
    private String waers;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "BASEAMT")
    private BigDecimal baseamt;
    @Column(name = "TAXAMT")
    private BigDecimal taxamt;
    @Column(name = "TOTALAMT")
    private BigDecimal totalamt;//稅後金額
    @Size(max = 40)
    @Column(name = "ZAFBUK_NM")
    private String zafbukNm;//關係人公司名稱
    @Size(max = 1)
    @Column(name = "ZCKFLG")
    private String zckflg;//對帳註記
    @Column(name = "ZLUPDT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date zlupdt;
    @Column(name = "SYNC_TIME_STAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date syncTimeStamp;

    public ZtfiAfrcInvo() {
    }

    public ZtfiAfrcInvo(Long id) {
        this.id = id;
    }

    public ZtfiAfrcInvo(Long id, short zgjahr, short zmonat, String bukrs, String zafbuk, String zafcat, String zaftyp, short gjahr, String belnr, String guino) {
        this.id = id;
        this.zgjahr = zgjahr;
        this.zmonat = zmonat;
        this.bukrs = bukrs;
        this.zafbuk = zafbuk;
        this.zafcat = zafcat;
        this.zaftyp = zaftyp;
        this.gjahr = gjahr;
        this.belnr = belnr;
        this.guino = guino;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getZgjahr() {
        return zgjahr;
    }

    public void setZgjahr(short zgjahr) {
        this.zgjahr = zgjahr;
    }

    public short getZmonat() {
        return zmonat;
    }

    public void setZmonat(short zmonat) {
        this.zmonat = zmonat;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    public String getZafbuk() {
        return zafbuk;
    }

    public void setZafbuk(String zafbuk) {
        this.zafbuk = zafbuk;
    }

    public String getZafcat() {
        return zafcat;
    }

    public void setZafcat(String zafcat) {
        this.zafcat = zafcat;
    }

    public String getZaftyp() {
        return zaftyp;
    }

    public void setZaftyp(String zaftyp) {
        this.zaftyp = zaftyp;
    }

    public short getGjahr() {
        return gjahr;
    }

    public void setGjahr(short gjahr) {
        this.gjahr = gjahr;
    }

    public String getBelnr() {
        return belnr;
    }

    public void setBelnr(String belnr) {
        this.belnr = belnr;
    }

    public String getGuino() {
        return guino;
    }

    public void setGuino(String guino) {
        this.guino = guino;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public String getGuiRegis() {
        return guiRegis;
    }

    public void setGuiRegis(String guiRegis) {
        this.guiRegis = guiRegis;
    }

    public Date getBudat() {
        return budat;
    }

    public void setBudat(Date budat) {
        this.budat = budat;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }

    public BigDecimal getBaseamt() {
        return baseamt;
    }

    public void setBaseamt(BigDecimal baseamt) {
        this.baseamt = baseamt;
    }

    public BigDecimal getTaxamt() {
        return taxamt;
    }

    public void setTaxamt(BigDecimal taxamt) {
        this.taxamt = taxamt;
    }

    public BigDecimal getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(BigDecimal totalamt) {
        this.totalamt = totalamt;
    }

    public String getZafbukNm() {
        return zafbukNm;
    }

    public void setZafbukNm(String zafbukNm) {
        this.zafbukNm = zafbukNm;
    }

    public String getZckflg() {
        return zckflg;
    }

    public void setZckflg(String zckflg) {
        this.zckflg = zckflg;
    }

    public Date getZlupdt() {
        return zlupdt;
    }

    public void setZlupdt(Date zlupdt) {
        this.zlupdt = zlupdt;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtfiAfrcInvo)) {
            return false;
        }
        ZtfiAfrcInvo other = (ZtfiAfrcInvo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.irs.entity.sheetdata.ZtfiAfrcInvo[ id=" + id + " ]";
    }
    
    public String getYMString() {
        Date dt = DateUtils.getDate(zgjahr, zmonat, 1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        return DateUtils.getYearMonth(calendar);
    }
    
}
