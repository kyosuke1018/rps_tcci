/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity.datawarehouse;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "ZT001W_CN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zt001wCn.findAll", query = "SELECT z FROM Zt001wCn z"),
    @NamedQuery(name = "Zt001wCn.findByWerks", query = "SELECT z FROM Zt001wCn z WHERE z.werks = :werks"),
    @NamedQuery(name = "Zt001wCn.findByName1", query = "SELECT z FROM Zt001wCn z WHERE z.name1 = :name1"),
    @NamedQuery(name = "Zt001wCn.findById", query = "SELECT z FROM Zt001wCn z WHERE z.id = :id")})
public class Zt001wCn implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "WERKS")
    private String werks;
    @Size(max = 140)
    @Column(name = "NAME1")
    private String name1;
    @Column(name = "ID")
    private BigInteger id;

    public Zt001wCn() {
    }

    public Zt001wCn(String werks) {
        this.werks = werks;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (werks != null ? werks.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zt001wCn)) {
            return false;
        }
        Zt001wCn other = (Zt001wCn) object;
        if ((this.werks == null && other.werks != null) || (this.werks != null && !this.werks.equals(other.werks))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.datawarehouse.Zt001wCn[ werks=" + werks + " ]";
    }
    
}
