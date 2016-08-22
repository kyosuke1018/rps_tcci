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
@Table(name = "SK_SALES_BUGET")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkSalesBuget.findAll", query = "SELECT s FROM SkSalesBuget s"),
    @NamedQuery(name = "SkSalesBuget.findById", query = "SELECT s FROM SkSalesBuget s WHERE s.id = :id"),
    @NamedQuery(name = "SkSalesBuget.findByYearMonth", query = "SELECT s FROM SkSalesBuget s WHERE s.yearMonth = :yearMonth"),
    @NamedQuery(name = "SkSalesBuget.findBySapId", query = "SELECT s FROM SkSalesBuget s WHERE s.sapId = :sapId"),
    @NamedQuery(name = "SkSalesBuget.findByCustCode", query = "SELECT s FROM SkSalesBuget s WHERE s.custCode = :custCode"),
    @NamedQuery(name = "SkSalesBuget.findByPrdType", query = "SELECT s FROM SkSalesBuget s WHERE s.prdType = :prdType"),
    @NamedQuery(name = "SkSalesBuget.findByMatnr", query = "SELECT s FROM SkSalesBuget s WHERE s.matnr = :matnr"),
    @NamedQuery(name = "SkSalesBuget.findByAmount", query = "SELECT s FROM SkSalesBuget s WHERE s.amount = :amount"),
    @NamedQuery(name = "SkSalesBuget.findByUnits", query = "SELECT s FROM SkSalesBuget s WHERE s.units = :units")})
public class SkSalesBuget implements Serializable {
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "SEQ_TCC")
    @Column(name = "ID")
    private Long id;
    @Size(max = 6)
    @Column(name = "YEAR_MONTH")
    private String yearMonth;
    @Size(max = 4)
    @Column(name = "SAP_ID")
    private String sapId;
    @Size(max = 6)
    @Column(name = "CUST_CODE")
    private String custCode;
    @Size(max = 2)
    @Column(name = "PRD_TYPE")
    private String prdType;
    @Size(max = 12)
    @Column(name = "MATNR")
    private String matnr;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "UNITS")
    private BigDecimal units;

    public SkSalesBuget() {
    }

    public SkSalesBuget(Long id) {
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

    public String getSapId() {
        return sapId;
    }

    public void setSapId(String sapId) {
        this.sapId = sapId;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getPrdType() {
        return prdType;
    }

    public void setPrdType(String prdType) {
        this.prdType = prdType;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
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
        if (!(object instanceof SkSalesBuget)) {
            return false;
        }
        SkSalesBuget other = (SkSalesBuget) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.SkSalesBuget[ id=" + id + " ]";
    }
}
