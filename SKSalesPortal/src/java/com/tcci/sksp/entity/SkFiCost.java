/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "SK_FI_COST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkFiCost.findAll", query = "SELECT s FROM SkFiCost s"),
    @NamedQuery(name = "SkFiCost.findById", query = "SELECT s FROM SkFiCost s WHERE s.id = :id"),
    @NamedQuery(name = "SkFiCost.findByMatnr", query = "SELECT s FROM SkFiCost s WHERE s.matnr = :matnr"),
    @NamedQuery(name = "SkFiCost.findByUnitCost", query = "SELECT s FROM SkFiCost s WHERE s.unitCost = :unitCost")})
public class SkFiCost implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "SEQ_TCC")
    @Column(name = "ID")
    private Long id;
    @Size(max = 12)
    @Column(name = "MATNR")
    private String matnr;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "UNIT_COST")
    private BigDecimal unitCost;
    @Size(max = 4)
    @Column(name = "YEAR")
    private String year;

    public SkFiCost() {
    }

    public SkFiCost(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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
        if (!(object instanceof SkFiCost)) {
            return false;
        }
        SkFiCost other = (SkFiCost) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.SkFiCost[ id=" + id + " ]";
    }
    
}
