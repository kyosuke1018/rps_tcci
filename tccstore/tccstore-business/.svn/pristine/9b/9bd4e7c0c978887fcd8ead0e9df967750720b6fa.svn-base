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
@Table(name = "ZT171_CN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zt171Cn.findAll", query = "SELECT z FROM Zt171Cn z"),
    @NamedQuery(name = "Zt171Cn.findByBzirk", query = "SELECT z FROM Zt171Cn z WHERE z.bzirk = :bzirk"),
    @NamedQuery(name = "Zt171Cn.findByBztxt", query = "SELECT z FROM Zt171Cn z WHERE z.bztxt = :bztxt"),
    @NamedQuery(name = "Zt171Cn.findById", query = "SELECT z FROM Zt171Cn z WHERE z.id = :id")})
public class Zt171Cn implements Serializable {
    private static final long serialVersionUID = 1L;
    @Size(max = 10)
    @Column(name = "BZIRK")
    private String bzirk;
    @Size(max = 140)
    @Column(name = "BZTXT")
    private String bztxt;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;

    public Zt171Cn() {
    }

    public Zt171Cn(BigDecimal id) {
        this.id = id;
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
        if (!(object instanceof Zt171Cn)) {
            return false;
        }
        Zt171Cn other = (Zt171Cn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.datawarehouse.Zt171Cn[ id=" + id + " ]";
    }
    
}
