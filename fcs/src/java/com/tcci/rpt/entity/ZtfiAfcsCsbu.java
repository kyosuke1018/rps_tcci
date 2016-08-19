/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.entity;

import java.io.Serializable;
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
@Table(name = "ZTFI_AFCS_CSBU")
@NamedQueries({
    @NamedQuery(name = "ZtfiAfcsCsbu.findAll", query = "SELECT z FROM ZtfiAfcsCsbu z"),
    @NamedQuery(name = "ZtfiAfcsCsbu.findByCompany", query = "SELECT z FROM ZtfiAfcsCsbu z where 1=1 and z.zbukfm = :compCode")})
public class ZtfiAfcsCsbu implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
//    @Basic(optional = false)
//    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)   
    @Column(name = "ID")
    private Long id;
    @NotNull
    @Column(name = "MANDT")
    private String mandt;
    @NotNull
    @Column(name = "ZCSLVL")
    private String zcslvl;
    @NotNull
    @Column(name = "ZBUKFM")
    private String zbukfm;
    @NotNull
    @Column(name = "ZBUKFM_NM")
    private String zbukfmNm;
    @Column(name = "ZCSTYP")
    private String zcstyp;
    @Column(name = "ZCSDTB")
    private String zcsdtb;
    @Column(name = "ZBUKTO")
    private String zbukto;
    @Column(name = "WAERS")
    private String waers;
    
    public ZtfiAfcsCsbu() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getZcslvl() {
        return zcslvl;
    }

    public void setZcslvl(String zcslvl) {
        this.zcslvl = zcslvl;
    }

    public String getZbukfm() {
        return zbukfm;
    }

    public void setZbukfm(String zbukfm) {
        this.zbukfm = zbukfm;
    }

    public String getZbukfmNm() {
        return zbukfmNm;
    }

    public void setZbukfmNm(String zbukfmNm) {
        this.zbukfmNm = zbukfmNm;
    }

    public String getZcstyp() {
        return zcstyp;
    }

    public void setZcstyp(String zcstyp) {
        this.zcstyp = zcstyp;
    }

    public String getZcsdtb() {
        return zcsdtb;
    }

    public void setZcsdtb(String zcsdtb) {
        this.zcsdtb = zcsdtb;
    }

    public String getZbukto() {
        return zbukto;
    }

    public void setZbukto(String zbukto) {
        this.zbukto = zbukto;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }
    
    
}
