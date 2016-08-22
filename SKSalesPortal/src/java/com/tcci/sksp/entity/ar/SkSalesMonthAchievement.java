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
@Table(name = "SK_SALES_MONTH_ACHIEVEMENT")
@Cacheable(value=false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkSalesMonthAchievement.findAll", query = "SELECT s FROM SkSalesMonthAchievement s"),
    @NamedQuery(name = "SkSalesMonthAchievement.findById", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.id = :id"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByBaselineTimestamp", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.baselineTimestamp = :baselineTimestamp"),
    @NamedQuery(name = "SkSalesMonthAchievement.findBySapid", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.sapid = :sapid"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByInvoiceAmount", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.invoiceAmount = :invoiceAmount"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByPremiumDiscount", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.premiumDiscount = :premiumDiscount"),
    @NamedQuery(name = "SkSalesMonthAchievement.findBySalesDiscount", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.salesDiscount = :salesDiscount"),
    @NamedQuery(name = "SkSalesMonthAchievement.findBySalesReturn", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.salesReturn = :salesReturn"),
    @NamedQuery(name = "SkSalesMonthAchievement.findBySalesAmount", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.salesAmount = :salesAmount"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByCost", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.cost = :cost"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByGrossProfitRate", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.grossProfitRate = :grossProfitRate"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByBudgetMonth", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.budgetMonth = :budgetMonth"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByMonthAchievementRate", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.monthAchievementRate = :monthAchievementRate"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByContractAmount", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.contractAmount = :contractAmount"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByPaymentRate", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.paymentRate = :paymentRate"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByWeight", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.weight = :weight"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByOverdueAmount", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.overdueAmount = :overdueAmount"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByContractCost", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.contractCost = :contractCost"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByReturnCost", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.returnCost = :returnCost"),
    @NamedQuery(name = "SkSalesMonthAchievement.findByReturnAmount", query = "SELECT s FROM SkSalesMonthAchievement s WHERE s.returnAmount = :returnAmount")})
public class SkSalesMonthAchievement implements Serializable {
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
    @Column(name = "MONTH_ACHIEVEMENT_RATE")
    private BigDecimal monthAchievementRate;
    @Column(name = "CONTRACT_AMOUNT")
    private BigDecimal contractAmount;
    @Column(name = "PAYMENT_RATE")
    private BigDecimal paymentRate;
    @Column(name = "WEIGHT")
    private BigDecimal weight;
    @Column(name = "OVERDUE_AMOUNT")
    private BigDecimal overdueAmount;
    @Column(name = "CONTRACT_COST")
    private BigDecimal contractCost;
    @Column(name = "RETURN_COST")
    private BigDecimal returnCost;
    @Column(name = "RETURN_AMOUNT")
    private BigDecimal returnAmount;

    public SkSalesMonthAchievement() {
    }

    public SkSalesMonthAchievement(Long id) {
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

    public BigDecimal getMonthAchievementRate() {
        return monthAchievementRate;
    }

    public void setMonthAchievementRate(BigDecimal monthAchievementRate) {
        this.monthAchievementRate = monthAchievementRate;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public BigDecimal getPaymentRate() {
        return paymentRate;
    }

    public void setPaymentRate(BigDecimal paymentRate) {
        this.paymentRate = paymentRate;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public BigDecimal getContractCost() {
        return contractCost;
    }

    public void setContractCost(BigDecimal contractCost) {
        this.contractCost = contractCost;
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
        if (!(object instanceof SkSalesMonthAchievement)) {
            return false;
        }
        SkSalesMonthAchievement other = (SkSalesMonthAchievement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ar.SkSalesMonthAchievement[ id=" + id + " ]";
    }
    
}
