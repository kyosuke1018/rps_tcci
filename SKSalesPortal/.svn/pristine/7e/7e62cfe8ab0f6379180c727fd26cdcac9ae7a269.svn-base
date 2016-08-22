/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
@Table(name = "SK_PRODUCT_UPLOAD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkProductUpload.findAll", query = "SELECT s FROM SkProductUpload s"),
    @NamedQuery(name = "SkProductUpload.findById", query = "SELECT s FROM SkProductUpload s WHERE s.id = :id"),
    @NamedQuery(name = "SkProductUpload.findByYearMonth", query = "SELECT s FROM SkProductUpload s WHERE s.yearMonth = :yearMonth"),
    @NamedQuery(name = "SkProductUpload.findByMatnr", query = "SELECT s FROM SkProductUpload s WHERE s.matnr = :matnr"),
    @NamedQuery(name = "SkProductUpload.findByUnit", query = "SELECT s FROM SkProductUpload s WHERE s.unit = :unit")})
public class SkProductUpload implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "SEQ_TCC")
    @Column(name = "ID")
    private Long id;
    @Size(max = 10)
    @Column(name = "YEAR_MONTH")
    private String yearMonth;
    @Size(max = 16)
    @Column(name = "MATNR")
    private String matnr;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "UNIT")
    private BigDecimal unit;

    public SkProductUpload() {
    }

    public SkProductUpload(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public BigDecimal getUnit() {
        return unit;
    }

    public void setUnit(BigDecimal unit) {
        this.unit = unit;
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
        if (!(object instanceof SkProductUpload)) {
            return false;
        }
        SkProductUpload other = (SkProductUpload) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.SkProductUpload[ id=" + id + " ]";
    }
    
}
