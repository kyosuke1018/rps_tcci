/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author carl.lin
 */
@Entity
@Table(name = "ZMARA_CN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZmaraCn.findAll", query = "SELECT z FROM ZmaraCn z"),
    @NamedQuery(name = "ZmaraCn.findByMatnr", query = "SELECT z FROM ZmaraCn z WHERE z.matnr = :matnr"),
    @NamedQuery(name = "ZmaraCn.findByMaktx", query = "SELECT z FROM ZmaraCn z WHERE z.maktx = :maktx"),
    @NamedQuery(name = "ZmaraCn.findByMeins", query = "SELECT z FROM ZmaraCn z WHERE z.meins = :meins"),
    @NamedQuery(name = "ZmaraCn.findByLaeda", query = "SELECT z FROM ZmaraCn z WHERE z.laeda = :laeda"),
    @NamedQuery(name = "ZmaraCn.findByErsda", query = "SELECT z FROM ZmaraCn z WHERE z.ersda = :ersda"),
    @NamedQuery(name = "ZmaraCn.findById", query = "SELECT z FROM ZmaraCn z WHERE z.id = :id")})
public class ZmaraCn implements Serializable {
    private static final long serialVersionUID = 1L;
    @Size(max = 18)
    @Column(name = "MATNR")
    private String matnr;
    @Size(max = 80)
    @Column(name = "MAKTX")
    private String maktx;
    @Size(max = 3)
    @Column(name = "MEINS")
    private String meins;
    @Size(max = 8)
    @Column(name = "LAEDA")
    private String laeda;
    @Size(max = 8)
    @Column(name = "ERSDA")
    private String ersda;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;

    public ZmaraCn() {
    }

    public ZmaraCn(BigDecimal id) {
        this.id = id;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public String getLaeda() {
        return laeda;
    }

    public void setLaeda(String laeda) {
        this.laeda = laeda;
    }

    public String getErsda() {
        return ersda;
    }

    public void setErsda(String ersda) {
        this.ersda = ersda;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
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
        if (!(object instanceof ZmaraCn)) {
            return false;
        }
        ZmaraCn other = (ZmaraCn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.datawarehouse.ZmaraCn[ id=" + id + " ]";
    }
    
}
