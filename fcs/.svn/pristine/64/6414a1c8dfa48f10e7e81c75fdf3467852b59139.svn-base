/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kyle.cheng
 */
@Entity
@Table(name = "ZTFI_AFCS_ACMA")
@NamedQueries({
    @NamedQuery(name = "ZtfiAfcsAcma.findByCode", query = "SELECT a FROM ZtfiAfcsAcma a WHERE a.zgroup=:group and a.zaadet=:code"),
    @NamedQuery(name = "ZtfiAfcsAcma.findAll", query = "SELECT a FROM ZtfiAfcsAcma a WHERE a.zgroup=:group")
})
public class ZtfiAfcsAcma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @NotNull
    @Column(name = "ZGROUP")
    private String zgroup;
    @NotNull
    @Size(min = 4, max = 6)
    @Column(name = "ZAADET")
    private String zaadet;
    @NotNull
    @Column(name = "ZAADET_NM")
    private String zaadetNm;
    @Column(name = "ZABNOT")
    private String zabnot;
    @Column(name = "ZABNOT_NM")
    private String zabnotNm;
    @Column(name = "ZACLIN")
    private String zaclin;
    @Column(name = "ZACLIN_NM")
    private String zaclinNm;
    @Column(name = "ZADSUB")
    private String zadsub;
    @Column(name = "ZADSUB_NM")
    private String zadsubNm;
    @Column(name = "ZAECLS")
    private String zaecls;
    @Column(name = "ZAECLS_NM")
    private String zaeclsNm;
    
    public ZtfiAfcsAcma() {
    }
    
    // getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZgroup() {
        return zgroup;
    }

    public void setZgroup(String zgroup) {
        this.zgroup = zgroup;
    }

    public String getZaadet() {
        return zaadet;
    }

    public void setZaadet(String zaadet) {
        this.zaadet = zaadet;
    }

    public String getZaadetNm() {
        return zaadetNm;
    }

    public void setZaadetNm(String zaadetNm) {
        this.zaadetNm = zaadetNm;
    }

    public String getZabnot() {
        return zabnot;
    }

    public void setZabnot(String zabnot) {
        this.zabnot = zabnot;
    }

    public String getZabnotNm() {
        return zabnotNm;
    }

    public void setZabnotNm(String zabnotNm) {
        this.zabnotNm = zabnotNm;
    }

    public String getZaclin() {
        return zaclin;
    }

    public void setZaclin(String zaclin) {
        this.zaclin = zaclin;
    }

    public String getZaclinNm() {
        return zaclinNm;
    }

    public void setZaclinNm(String zaclinNm) {
        this.zaclinNm = zaclinNm;
    }

    public String getZadsub() {
        return zadsub;
    }

    public void setZadsub(String zadsub) {
        this.zadsub = zadsub;
    }

    public String getZadsubNm() {
        return zadsubNm;
    }

    public void setZadsubNm(String zadsubNm) {
        this.zadsubNm = zadsubNm;
    }

    public String getZaecls() {
        return zaecls;
    }

    public void setZaecls(String zaecls) {
        this.zaecls = zaecls;
    }

    public String getZaeclsNm() {
        return zaeclsNm;
    }

    public void setZaeclsNm(String zaeclsNm) {
        this.zaeclsNm = zaeclsNm;
    }
    
}
