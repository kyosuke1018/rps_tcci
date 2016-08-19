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
@Table(name = "ZTVKO_CN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZtvkoCn.findAll", query = "SELECT z FROM ZtvkoCn z"),
    @NamedQuery(name = "ZtvkoCn.findByVkorg", query = "SELECT z FROM ZtvkoCn z WHERE z.vkorg = :vkorg"),
    @NamedQuery(name = "ZtvkoCn.findByVtext", query = "SELECT z FROM ZtvkoCn z WHERE z.vtext = :vtext"),
    @NamedQuery(name = "ZtvkoCn.findById", query = "SELECT z FROM ZtvkoCn z WHERE z.id = :id")})
public class ZtvkoCn implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "VKORG")
    private String vkorg;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 140)
    @Column(name = "VTEXT")
    private String vtext;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;

    public ZtvkoCn() {
    }

    public ZtvkoCn(BigDecimal id) {
        this.id = id;
    }

    public ZtvkoCn(BigDecimal id, String vkorg, String vtext) {
        this.id = id;
        this.vkorg = vkorg;
        this.vtext = vtext;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public String getVtext() {
        return vtext;
    }

    public void setVtext(String vtext) {
        this.vtext = vtext;
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
        if (!(object instanceof ZtvkoCn)) {
            return false;
        }
        ZtvkoCn other = (ZtvkoCn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.datawarehouse.ZtvkoCn[ id=" + id + " ]";
    }
    
}
