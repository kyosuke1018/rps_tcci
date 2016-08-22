/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carl.lin
 */
@Entity
@Table(name = "FACT_WEB_1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FactWeb1.findAll", query = "SELECT f FROM FactWeb1 f"),
    @NamedQuery(name = "FactWeb1.findByYyyymm", query = "SELECT f FROM FactWeb1 f WHERE f.factWeb1PK.yyyymm = :yyyymm"),
    @NamedQuery(name = "FactWeb1.findByAccess1", query = "SELECT f FROM FactWeb1 f WHERE f.access1 = :access1"),
    @NamedQuery(name = "FactWeb1.findByAccessid", query = "SELECT f FROM FactWeb1 f WHERE f.factWeb1PK.accessid = :accessid"),
    @NamedQuery(name = "FactWeb1.findByPK", query = "SELECT f FROM FactWeb1 f WHERE f.factWeb1PK = :pk "),
    @NamedQuery(name = "FactWeb1.findByLight", query = "SELECT f FROM FactWeb1 f WHERE f.light = :light")})
public class FactWeb1 implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FactWeb1PK factWeb1PK;
    @Size(max = 15)
    @Column(name = "ACCESS_1")
    private String access1;
    @Column(name = "SHOULD_PAY_AMOUNT")
    private BigDecimal shouldPayAmount;
    @Column(name = "PAYMENT_AMOUNT")
    private BigDecimal paymentAmount;
    @Column(name = "INVOICE_AMOUNT")
    private BigDecimal invoiceAmount;
    @Column(name = "COST")
    private BigDecimal cost;
    @Column(name = "SALES_RETURN")
    private BigDecimal salesReturn;
    @Column(name = "PREMIUM_DISCOUNT")
    private BigDecimal premiumDiscount;
    @Column(name = "BUDGET")
    private BigDecimal budget;
    @Column(name = "BUDGET_TARGET")
    private BigDecimal budgetTarget;
    @Column(name = "BCOST")
    private BigDecimal bcost;
    @Column(name = "SALES_DISCOUNT")
    private BigDecimal salesDiscount;
    @Column(name = "SALES_AMOUNT")
    private BigDecimal salesAmount;
    @Column(name = "CUMULATIVE_ACHIVEMENT_RATE")
    private BigDecimal cumulativeAchivementRate;
    @Column(name = "MONTH_ACHIVEMENT_RATE")
    private BigDecimal monthAchivementRate;
    @Column(name = "GROSS_PROFIT_RATE")
    private BigDecimal grossProfitRate;
    @Column(name = "PAYMENT_RATE")
    private BigDecimal paymentRate;
    @Column(name = "LIGHT")
    private BigDecimal light;
    @Column(name = "RESPONSE_NAME")
    private String responseName;
    @Column(name = "RESPONSE_PHONE")
    private String responsePhone;
    
    public FactWeb1() {
    }

    public FactWeb1(FactWeb1PK factWeb1PK) {
        this.factWeb1PK = factWeb1PK;
    }

    public FactWeb1(String yyyymm, String accessid) {
        this.factWeb1PK = new FactWeb1PK(yyyymm, accessid);
    }

    public FactWeb1PK getFactWeb1PK() {
        return factWeb1PK;
    }

    public void setFactWeb1PK(FactWeb1PK factWeb1PK) {
        this.factWeb1PK = factWeb1PK;
    }

    public String getAccess1() {
        return access1;
    }

    public void setAccess1(String access1) {
        this.access1 = access1;
    }

    public BigDecimal getShouldPayAmount() {
        return shouldPayAmount;
    }

    public void setShouldPayAmount(BigDecimal shouldPayAmount) {
        this.shouldPayAmount = shouldPayAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getSalesReturn() {
        return salesReturn;
    }

    public void setSalesReturn(BigDecimal salesReturn) {
        this.salesReturn = salesReturn;
    }

    public BigDecimal getPremiumDiscount() {
        return premiumDiscount;
    }

    public void setPremiumDiscount(BigDecimal premiumDiscount) {
        this.premiumDiscount = premiumDiscount;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getBudgetTarget() {
        return budgetTarget;
    }

    public void setBudgetTarget(BigDecimal budgetTarget) {
        this.budgetTarget = budgetTarget;
    }

    public BigDecimal getBcost() {
        return bcost;
    }

    public void setBcost(BigDecimal bcost) {
        this.bcost = bcost;
    }

    public BigDecimal getSalesDiscount() {
        return salesDiscount;
    }

    public void setSalesDiscount(BigDecimal salesDiscount) {
        this.salesDiscount = salesDiscount;
    }

    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount) {
        this.salesAmount = salesAmount;
    }

    public BigDecimal getCumulativeAchivementRate() {
        return cumulativeAchivementRate;
    }

    public void setCumulativeAchivementRate(BigDecimal cumulativeAchivementRate) {
        this.cumulativeAchivementRate = cumulativeAchivementRate;
    }

    public BigDecimal getMonthAchivementRate() {
        return monthAchivementRate;
    }

    public void setMonthAchivementRate(BigDecimal monthAchivementRate) {
        this.monthAchivementRate = monthAchivementRate;
    }

    public BigDecimal getGrossProfitRate() {
        return grossProfitRate;
    }

    public void setGrossProfitRate(BigDecimal grossProfitRate) {
        this.grossProfitRate = grossProfitRate;
    }

    public BigDecimal getPaymentRate() {
        return paymentRate;
    }

    public void setPaymentRate(BigDecimal paymentRate) {
        this.paymentRate = paymentRate;
    }

    public BigDecimal getLight() {
        return light;
    }

    public void setLight(BigDecimal light) {
        this.light = light;
    }

    public String getResponseName() {
        return responseName;
    }

    public void setResponseName(String responseName) {
        this.responseName = responseName;
    }

    public String getResponsePhone() {
        return responsePhone;
    }

    public void setResponsePhone(String responsePhone) {
        this.responsePhone = responsePhone;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (factWeb1PK != null ? factWeb1PK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactWeb1)) {
            return false;
        }
        FactWeb1 other = (FactWeb1) object;
        if ((this.factWeb1PK == null && other.factWeb1PK != null) || (this.factWeb1PK != null && !this.factWeb1PK.equals(other.factWeb1PK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.FactWeb1[ factWeb1PK=" + factWeb1PK + " ]";
    }
    
}
