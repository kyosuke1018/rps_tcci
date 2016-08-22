/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.ar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_SALES_DAY_ACHIEVEMENT")
@Cacheable(value=false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkSalesDayAchievement.findAll", query = "SELECT s FROM SkSalesDayAchievement s"),
    @NamedQuery(name = "SkSalesDayAchievement.findById", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.id = :id"),
    @NamedQuery(name = "SkSalesDayAchievement.findByBaselineTimestamp", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.baselineTimestamp = :baselineTimestamp"),
    @NamedQuery(name = "SkSalesDayAchievement.findBySapid", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.sapid = :sapid"),
    @NamedQuery(name = "SkSalesDayAchievement.findByInvoiceAmount", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.invoiceAmount = :invoiceAmount"),
    @NamedQuery(name = "SkSalesDayAchievement.findByPremiumDiscount", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.premiumDiscount = :premiumDiscount"),
    @NamedQuery(name = "SkSalesDayAchievement.findBySalesDiscount", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.salesDiscount = :salesDiscount"),
    @NamedQuery(name = "SkSalesDayAchievement.findBySalesReturn", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.salesReturn = :salesReturn"),
    @NamedQuery(name = "SkSalesDayAchievement.findBySalesAmount", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.salesAmount = :salesAmount"),
    @NamedQuery(name = "SkSalesDayAchievement.findByCost", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.cost = :cost"),
    @NamedQuery(name = "SkSalesDayAchievement.findByGrossProfitRate", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.grossProfitRate = :grossProfitRate"),
    @NamedQuery(name = "SkSalesDayAchievement.findByBudgetMonth", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.budgetMonth = :budgetMonth"),
    @NamedQuery(name = "SkSalesDayAchievement.findByDayAchievementRate", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.dayAchievementRate = :dayAchievementRate"),
    @NamedQuery(name = "SkSalesDayAchievement.findByMonthAchievementRate", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.monthAchievementRate = :monthAchievementRate"),
    @NamedQuery(name = "SkSalesDayAchievement.findByReturnCost", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.returnCost = :returnCost"),
    @NamedQuery(name = "SkSalesDayAchievement.findByReturnAmount", query = "SELECT s FROM SkSalesDayAchievement s WHERE s.returnAmount = :returnAmount")})
public class SkSalesDayAchievement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "BASELINE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date baselineTimestamp;
    @Column(name = "SAPID")
    private String sapid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "INVOICE_AMOUNT")
    private BigDecimal invoiceAmount;
    @Column(name = "PREMIUM_DISCOUNT")
    private BigDecimal premiumDiscount;
    @Column(name = "SALES_DISCOUNT")
    private BigDecimal salesDiscount;
    @Column(name = "SALES_RETURN")
    private BigDecimal salesReturn;
    @Column(name = "SALES_AMOUNT")
    private BigDecimal salesAmount;
    @Column(name = "COST")
    private BigDecimal cost;
    @Column(name = "GROSS_PROFIT_RATE")
    private BigDecimal grossProfitRate;
    @Column(name = "BUDGET_MONTH")
    private BigDecimal budgetMonth;
    @Column(name = "DAY_ACHIEVEMENT_RATE")
    private BigDecimal dayAchievementRate;
    @Column(name = "MONTH_ACHIEVEMENT_RATE")
    private BigDecimal monthAchievementRate;
    @Column(name = "RETURN_COST")
    private BigDecimal returnCost;
    @Column(name = "RETURN_AMOUNT")
    private BigDecimal returnAmount;

    public SkSalesDayAchievement() {
    }

    public SkSalesDayAchievement(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBaselineTimestamp() {
        return baselineTimestamp;
    }

    public void setBaselineTimestamp(Date baselineTimestamp) {
        this.baselineTimestamp = baselineTimestamp;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public BigDecimal getPremiumDiscount() {
        return premiumDiscount;
    }

    public void setPremiumDiscount(BigDecimal premiumDiscount) {
        this.premiumDiscount = premiumDiscount;
    }

    public BigDecimal getSalesDiscount() {
        return salesDiscount;
    }

    public void setSalesDiscount(BigDecimal salesDiscount) {
        this.salesDiscount = salesDiscount;
    }

    public BigDecimal getSalesReturn() {
        return salesReturn;
    }

    public void setSalesReturn(BigDecimal salesReturn) {
        this.salesReturn = salesReturn;
    }

    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount) {
        this.salesAmount = salesAmount;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getGrossProfitRate() {
        return grossProfitRate;
    }

    public void setGrossProfitRate(BigDecimal grossProfitRate) {
        this.grossProfitRate = grossProfitRate;
    }

    public BigDecimal getBudgetMonth() {
        return budgetMonth;
    }

    public void setBudgetMonth(BigDecimal budgetMonth) {
        this.budgetMonth = budgetMonth;
    }

    public BigDecimal getDayAchievementRate() {
        return dayAchievementRate;
    }

    public void setDayAchievementRate(BigDecimal dayAchievementRate) {
        this.dayAchievementRate = dayAchievementRate;
    }

    public BigDecimal getMonthAchievementRate() {
        return monthAchievementRate;
    }

    public void setMonthAchievementRate(BigDecimal monthAchievementRate) {
        this.monthAchievementRate = monthAchievementRate;
    }

    public BigDecimal getReturnCost() {
        return returnCost;
    }

    public void setReturnCost(BigDecimal returnCost) {
        this.returnCost = returnCost;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
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
        if (!(object instanceof SkSalesDayAchievement)) {
            return false;
        }
        SkSalesDayAchievement other = (SkSalesDayAchievement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ar.SkSalesDayAchievement[ id=" + id + " ]";
    }
    
}
