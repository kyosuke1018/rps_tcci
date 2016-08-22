/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carl.lin
 */
@Entity
@Table(name = "FACT_SK_ACHIEVEMENT_MONTH")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FactSkAchievementMonth.findAll", query = "SELECT f FROM FactSkAchievementMonth f"),
    @NamedQuery(name = "FactSkAchievementMonth.findByYearMonth", query = "SELECT f FROM FactSkAchievementMonth f WHERE f.factSkAchievementMonthPK.yearMonth = :yearMonth"),
    @NamedQuery(name = "FactSkAchievementMonth.findBySapId", query = "SELECT f FROM FactSkAchievementMonth f WHERE f.factSkAchievementMonthPK.sapId = :sapId"),
    @NamedQuery(name = "FactSkAchievementMonth.findByInvoiceAmount", query = "SELECT f FROM FactSkAchievementMonth f WHERE f.invoiceAmount = :invoiceAmount"),
    @NamedQuery(name = "FactSkAchievementMonth.findByPremiumDiscount", query = "SELECT f FROM FactSkAchievementMonth f WHERE f.premiumDiscount = :premiumDiscount"),
    @NamedQuery(name = "FactSkAchievementMonth.findBySalesReturn", query = "SELECT f FROM FactSkAchievementMonth f WHERE f.salesReturn = :salesReturn"),
    @NamedQuery(name = "FactSkAchievementMonth.findBySalesDiscount", query = "SELECT f FROM FactSkAchievementMonth f WHERE f.salesDiscount = :salesDiscount"),
    @NamedQuery(name = "FactSkAchievementMonth.findBySalesAmount", query = "SELECT f FROM FactSkAchievementMonth f WHERE f.salesAmount = :salesAmount"),
    @NamedQuery(name = "FactSkAchievementMonth.findByGrossProfitRate", query = "SELECT f FROM FactSkAchievementMonth f WHERE f.grossProfitRate = :grossProfitRate"),
    @NamedQuery(name = "FactSkAchievementMonth.findByAchievementRate", query = "SELECT f FROM FactSkAchievementMonth f WHERE f.achievementRate = :achievementRate"),
    @NamedQuery(name = "FactSkAchievementMonth.findByBudget", query = "SELECT f FROM FactSkAchievementMonth f WHERE f.budget = :budget"),
    @NamedQuery(name = "FactSkAchievementMonth.findByTargetRate", query = "SELECT f FROM FactSkAchievementMonth f WHERE f.targetRate = :targetRate"),
    @NamedQuery(name = "FactSkAchievementMonth.findByCreatedatetime", query = "SELECT f FROM FactSkAchievementMonth f WHERE f.createdatetime = :createdatetime"),
    @NamedQuery(name = "FactSkAchievementMonth.findByCreatedate", query = "SELECT f FROM FactSkAchievementMonth f WHERE f.factSkAchievementMonthPK.createdate = :createdate")})
public class FactSkAchievementMonth implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FactSkAchievementMonthPK factSkAchievementMonthPK;
    @Column(name = "INVOICE_AMOUNT")
    private BigInteger invoiceAmount;
    @Column(name = "PREMIUM_DISCOUNT")
    private BigInteger premiumDiscount;
    @Column(name = "SALES_RETURN")
    private BigInteger salesReturn;
    @Column(name = "SALES_DISCOUNT")
    private BigInteger salesDiscount;
    @Column(name = "SALES_AMOUNT")
    private BigInteger salesAmount;
    @Column(name = "GROSS_PROFIT_RATE")
    private BigInteger grossProfitRate;
    @Column(name = "ACHIEVEMENT_RATE")
    private BigInteger achievementRate;
    @Column(name = "BUDGET")
    private BigInteger budget;
    @Column(name = "TARGET_RATE")
    private BigInteger targetRate;
    @Column(name = "CREATEDATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdatetime;
    @JoinColumn(name = "SAP_ID", referencedColumnName = "SAP_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DimSkSaleName dimSkSaleName;

    public FactSkAchievementMonth() {
    }

    public FactSkAchievementMonth(FactSkAchievementMonthPK factSkAchievementMonthPK) {
        this.factSkAchievementMonthPK = factSkAchievementMonthPK;
    }

    public FactSkAchievementMonth(String yearMonth, String sapId, String createdate) {
        this.factSkAchievementMonthPK = new FactSkAchievementMonthPK(yearMonth, sapId, createdate);
    }

    public FactSkAchievementMonthPK getFactSkAchievementMonthPK() {
        return factSkAchievementMonthPK;
    }

    public void setFactSkAchievementMonthPK(FactSkAchievementMonthPK factSkAchievementMonthPK) {
        this.factSkAchievementMonthPK = factSkAchievementMonthPK;
    }

    public BigInteger getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigInteger invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public BigInteger getPremiumDiscount() {
        return premiumDiscount;
    }

    public void setPremiumDiscount(BigInteger premiumDiscount) {
        this.premiumDiscount = premiumDiscount;
    }

    public BigInteger getSalesReturn() {
        return salesReturn;
    }

    public void setSalesReturn(BigInteger salesReturn) {
        this.salesReturn = salesReturn;
    }

    public BigInteger getSalesDiscount() {
        return salesDiscount;
    }

    public void setSalesDiscount(BigInteger salesDiscount) {
        this.salesDiscount = salesDiscount;
    }

    public BigInteger getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(BigInteger salesAmount) {
        this.salesAmount = salesAmount;
    }

    public BigInteger getGrossProfitRate() {
        return grossProfitRate;
    }

    public void setGrossProfitRate(BigInteger grossProfitRate) {
        this.grossProfitRate = grossProfitRate;
    }

    public BigInteger getAchievementRate() {
        return achievementRate;
    }

    public void setAchievementRate(BigInteger achievementRate) {
        this.achievementRate = achievementRate;
    }

    public BigInteger getBudget() {
        return budget;
    }

    public void setBudget(BigInteger budget) {
        this.budget = budget;
    }

    public BigInteger getTargetRate() {
        return targetRate;
    }

    public void setTargetRate(BigInteger targetRate) {
        this.targetRate = targetRate;
    }

    public Date getCreatedatetime() {
        return createdatetime;
    }

    public void setCreatedatetime(Date createdatetime) {
        this.createdatetime = createdatetime;
    }

    public DimSkSaleName getDimSkSaleName() {
        return dimSkSaleName;
    }

    public void setDimSkSaleName(DimSkSaleName dimSkSaleName) {
        this.dimSkSaleName = dimSkSaleName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (factSkAchievementMonthPK != null ? factSkAchievementMonthPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactSkAchievementMonth)) {
            return false;
        }
        FactSkAchievementMonth other = (FactSkAchievementMonth) object;
        if ((this.factSkAchievementMonthPK == null && other.factSkAchievementMonthPK != null) || (this.factSkAchievementMonthPK != null && !this.factSkAchievementMonthPK.equals(other.factSkAchievementMonthPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.FactSkAchievementMonth[ factSkAchievementMonthPK=" + factSkAchievementMonthPK + " ]";
    }
    
}
