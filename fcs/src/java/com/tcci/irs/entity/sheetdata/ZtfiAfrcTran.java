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
@Table(name = "ZTFI_AFRC_TRAN")
@NamedQueries({
    @NamedQuery(name = "ZtfiAfrcTran.findAll", query = "SELECT z FROM ZtfiAfrcTran z")})
public class ZtfiAfrcTran implements Serializable {
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
    private short zgjahr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ZMONAT")
    private short zmonat;
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
    @Size(min = 1, max = 5)
    @Column(name = "BUZEI")
    private String buzei;
    @Size(max = 10)
    @Column(name = "KUNNR")
    private String kunnr;
    @Size(max = 10)
    @Column(name = "LIFNR")
    private String lifnr;
    @Size(max = 10)
    @Column(name = "HKONT")
    private String hkont;
    @Column(name = "BUDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date budat;
    @Size(max = 2)
    @Column(name = "BLART")
    private String blart;
    @Size(max = 5)
    @Column(name = "WAERS")
    private String waers;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "WRBTR")
    private BigDecimal wrbtr;
    @Column(name = "DMBTR")
    private BigDecimal dmbtr;
    @Size(max = 18)
    @Column(name = "ZUONR")
    private String zuonr;
    @Size(max = 50)
    @Column(name = "SGTXT")
    private String sgtxt;
    @Size(max = 1)
    @Column(name = "ZAFTL_SACO")
    private String zaftlSaco;
    @Size(max = 40)
    @Column(name = "ZAFBUK_NM")
    private String zafbukNm;
    @Size(max = 10)
    @Column(name = "HKONT_IFRS")
    private String hkontIfrs;
    @Size(max = 50)
    @Column(name = "TXT50")
    private String txt50;
    @Size(max = 1)
    @Column(name = "ZCKFLG")
    private String zckflg;
    @Column(name = "ZLUPDT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date zlupdt;
    @Column(name = "SYNC_TIME_STAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date syncTimeStamp;
    @Column(name = "ACCOUNT_CODE")
    private String accountCode;//合併報表會計科目代碼

    public ZtfiAfrcTran() {
    }

    public ZtfiAfrcTran(Long id) {
        this.id = id;
    }

    public ZtfiAfrcTran(Long id, short zgjahr, short zmonat, String bukrs, String zafbuk, String zafcat, String zaftyp, short gjahr, String belnr, String buzei) {
        this.id = id;
        this.zgjahr = zgjahr;
        this.zmonat = zmonat;
        this.bukrs = bukrs;
        this.zafbuk = zafbuk;
        this.zafcat = zafcat;
        this.zaftyp = zaftyp;
        this.gjahr = gjahr;
        this.belnr = belnr;
        this.buzei = buzei;
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

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public String getHkont() {
        return hkont;
    }

    public void setHkont(String hkont) {
        this.hkont = hkont;
    }

    public Date getBudat() {
        return budat;
    }

    public void setBudat(Date budat) {
        this.budat = budat;
    }

    public String getBlart() {
        return blart;
    }

    public void setBlart(String blart) {
        this.blart = blart;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }

    public BigDecimal getWrbtr() {
        return wrbtr;
    }

    public void setWrbtr(BigDecimal wrbtr) {
        this.wrbtr = wrbtr;
    }

    public BigDecimal getDmbtr() {
        return dmbtr;
    }

    public void setDmbtr(BigDecimal dmbtr) {
        this.dmbtr = dmbtr;
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

    public String getZaftlSaco() {
        return zaftlSaco;
    }

    public void setZaftlSaco(String zaftlSaco) {
        this.zaftlSaco = zaftlSaco;
    }

    public String getZafbukNm() {
        return zafbukNm;
    }

    public void setZafbukNm(String zafbukNm) {
        this.zafbukNm = zafbukNm;
    }

    public String getHkontIfrs() {
	return hkontIfrs;
    }

    public void setHkontIfrs(String hkontIfrs) {
	this.hkontIfrs = hkontIfrs;
    }

    public String getTxt50() {
        return txt50;
    }

    public void setTxt50(String txt50) {
        this.txt50 = txt50;
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

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
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
        if (!(object instanceof ZtfiAfrcTran)) {
            return false;
        }
        ZtfiAfrcTran other = (ZtfiAfrcTran) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.irs.entity.sheetdata.ZtfiAfrcTran[ id=" + id + " ]";
    }
    
    public String getYMString() {
        Date dt = DateUtils.getDate(zgjahr, zmonat, 1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        return DateUtils.getYearMonth(calendar);
    }
    
}
