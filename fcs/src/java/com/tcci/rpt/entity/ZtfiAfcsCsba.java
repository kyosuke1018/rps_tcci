/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.entity;

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
import javax.validation.constraints.NotNull;

/**
 *
 * @author Kyle.Cheng
 */
@Entity
@Table(name = "ZTFI_AFCS_CSBA")
@NamedQueries({
    @NamedQuery(name = "ZtfiAfcsCsba.findAll", 
            query = "SELECT z FROM ZtfiAfcsCsba z where 1=1 and z.zgjahr = :year and z.zmonat = :month"),
    @NamedQuery(name = "ZtfiAfcsCsba.findByCompany", 
            query = "SELECT z FROM ZtfiAfcsCsba z where 1=1 and z.zgjahr = :year and z.zmonat = :month and z.zbukfm = :compCode"),
    @NamedQuery(name = "ZtfiAfcsCsba.deleteByCompany",
            query = "DELETE FROM ZtfiAfcsCsba z where 1=1 and z.zgjahr = :year and z.zmonat = :month and z.zbukfm = :compCode")})
public class ZtfiAfcsCsba implements Serializable {
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
    @NotNull
    @Column(name = "ZBUKTO")
    private String zbukto;//目標合併公司代碼
    @NotNull
    @Column(name = "ZBUKFM")
    private String zbukfm;//合併公司代碼
    @NotNull
    @Column(name = "ZAADET")
    private String zaadet;//合併會科
    @Column(name = "WAERS")
    private String waers;//公司幣別
    @Column(name = "DMBTR")
    private BigDecimal dmbtr;//公司幣別金額
    @Column(name = "ZCSWAE")
    private String zcswae;//合併主體幣別
    @Column(name = "ZCSBTR")
    private BigDecimal zcsbtr;//合併主體幣別金額
    @Column(name = "ZCSBTR_CLR")
    private BigDecimal zcsbtrClr;//合併沖銷金額
    @Column(name = "ZTWBTR")
    private BigDecimal ztwbtr;//最後合併台幣
    @Column(name = "ZTWBTR_CLR")
    private BigDecimal ztwbtrClr;//最後合併台幣_沖銷
    
    
    public ZtfiAfcsCsba() {
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

    public String getZbukto() {
        return zbukto;
    }

    public void setZbukto(String zbukto) {
        this.zbukto = zbukto;
    }

    public String getZbukfm() {
        return zbukfm;
    }

    public void setZbukfm(String zbukfm) {
        this.zbukfm = zbukfm;
    }

    public String getZaadet() {
        return zaadet;
    }

    public void setZaadet(String zaadet) {
        this.zaadet = zaadet;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }

    public BigDecimal getDmbtr() {
        return dmbtr;
    }

    public void setDmbtr(BigDecimal dmbtr) {
        this.dmbtr = dmbtr;
    }

    public String getZcswae() {
        return zcswae;
    }

    public void setZcswae(String zcswae) {
        this.zcswae = zcswae;
    }

    public BigDecimal getZcsbtr() {
        return zcsbtr;
    }

    public void setZcsbtr(BigDecimal zcsbtr) {
        this.zcsbtr = zcsbtr;
    }

    public BigDecimal getZcsbtrClr() {
        return zcsbtrClr;
    }

    public void setZcsbtrClr(BigDecimal zcsbtrClr) {
        this.zcsbtrClr = zcsbtrClr;
    }

    public BigDecimal getZtwbtr() {
        return ztwbtr;
    }

    public void setZtwbtr(BigDecimal ztwbtr) {
        this.ztwbtr = ztwbtr;
    }

    public BigDecimal getZtwbtrClr() {
        return ztwbtrClr;
    }

    public void setZtwbtrClr(BigDecimal ztwbtrClr) {
        this.ztwbtrClr = ztwbtrClr;
    }
    
    public String getYMString() {
        Date dt = DateUtils.getDate(zgjahr, zmonat, 1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        return DateUtils.getYearMonth(calendar);
    }
}
