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
@Table(name = "FACT_WEB_2")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FactWeb2.findAll", query = "SELECT f FROM FactWeb2 f"),
    @NamedQuery(name = "FactWeb2.findByYyyymm", query = "SELECT f FROM FactWeb2 f WHERE f.factWeb2PK.yyyymm = :yyyymm"),
    @NamedQuery(name = "FactWeb2.findByYyyymmAccessid", 
        query = "SELECT f FROM FactWeb2 f WHERE f.factWeb2PK.yyyymm = :yyyymm"
        + " and f.factWeb2PK.accessid = :accessid"),
    @NamedQuery(name = "FactWeb2.findByYyyymmAreaid", 
        query = "SELECT f FROM FactWeb2 f WHERE f.factWeb2PK.yyyymm = :yyyymm "
        + " and f.factWeb2PK.areaid =:areaid"),    
    @NamedQuery(name = "FactWeb2.findByYyyymmAccess1", 
        query = "SELECT f FROM FactWeb2 f WHERE f.factWeb2PK.yyyymm = :yyyymm "
        + " and (f.access1 = :access1 or f.factWeb2PK.accessid =:access1 ) "),    
    @NamedQuery(name = "FactWeb2.findByArea", query = "SELECT f FROM FactWeb2 f WHERE f.area = :area"),
    @NamedQuery(name = "FactWeb2.findByAccess1", query = "SELECT f FROM FactWeb2 f WHERE f.access1 = :access1"),
    @NamedQuery(name = "FactWeb2.findByDomain", query = "SELECT f FROM FactWeb2 f WHERE f.domain = :domain"),
    @NamedQuery(name = "FactWeb2.findByAreaid", query = "SELECT f FROM FactWeb2 f WHERE f.factWeb2PK.areaid = :areaid"),
    @NamedQuery(name = "FactWeb2.findByDomainid", query = "SELECT f FROM FactWeb2 f WHERE f.factWeb2PK.domainid = :domainid"),
    @NamedQuery(name = "FactWeb2.findByAccessid", query = "SELECT f FROM FactWeb2 f WHERE f.factWeb2PK.accessid = :accessid"),
    @NamedQuery(name = "FactWeb2.findByShouldPayAmount", query = "SELECT f FROM FactWeb2 f WHERE f.shouldPayAmount = :shouldPayAmount"),
    @NamedQuery(name = "FactWeb2.findByPaymentAmount", query = "SELECT f FROM FactWeb2 f WHERE f.paymentAmount = :paymentAmount"),
    @NamedQuery(name = "FactWeb2.findByInvoiceAmount", query = "SELECT f FROM FactWeb2 f WHERE f.invoiceAmount = :invoiceAmount"),
    @NamedQuery(name = "FactWeb2.findByCost", query = "SELECT f FROM FactWeb2 f WHERE f.cost = :cost"),
    @NamedQuery(name = "FactWeb2.findBySalesReturn", query = "SELECT f FROM FactWeb2 f WHERE f.salesReturn = :salesReturn"),
    @NamedQuery(name = "FactWeb2.findByPremiumDiscount", query = "SELECT f FROM FactWeb2 f WHERE f.premiumDiscount = :premiumDiscount"),
    @NamedQuery(name = "FactWeb2.findByBudget", query = "SELECT f FROM FactWeb2 f WHERE f.budget = :budget"),
    @NamedQuery(name = "FactWeb2.findByBudgetTarget", query = "SELECT f FROM FactWeb2 f WHERE f.budgetTarget = :budgetTarget"),
    @NamedQuery(name = "FactWeb2.findByBcost", query = "SELECT f FROM FactWeb2 f WHERE f.bcost = :bcost"),
    @NamedQuery(name = "FactWeb2.findBySalesDiscount", query = "SELECT f FROM FactWeb2 f WHERE f.salesDiscount = :salesDiscount"),
    @NamedQuery(name = "FactWeb2.findBySalesAmount", query = "SELECT f FROM FactWeb2 f WHERE f.salesAmount = :salesAmount"),
    @NamedQuery(name = "FactWeb2.findByCumulativeAchivementRate", query = "SELECT f FROM FactWeb2 f WHERE f.cumulativeAchivementRate = :cumulativeAchivementRate"),
    @NamedQuery(name = "FactWeb2.findByMonthAchivementRate", query = "SELECT f FROM FactWeb2 f WHERE f.monthAchivementRate = :monthAchivementRate"),
    @NamedQuery(name = "FactWeb2.findByGrossProfitRate", query = "SELECT f FROM FactWeb2 f WHERE f.grossProfitRate = :grossProfitRate"),
    @NamedQuery(name = "FactWeb2.findByPaymentRate", query = "SELECT f FROM FactWeb2 f WHERE f.paymentRate = :paymentRate"),
    @NamedQuery(name = "FactWeb2.findByLight", query = "SELECT f FROM FactWeb2 f WHERE f.light = :light")})
public class FactWeb2 implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FactWeb2PK factWeb2PK;
    @Size(max = 6)
    @Column(name = "AREA")
    private String area;
    @Size(max = 15)
    @Column(name = "ACCESS_1")
    private String access1;
    @Size(max = 20)
    @Column(name = "DOMAIN")
    private String domain;
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

    public FactWeb2() {
    }

    public FactWeb2(FactWeb2PK factWeb2PK) {
        this.factWeb2PK = factWeb2PK;
    }

    public FactWeb2(String yyyymm, String areaid, String domainid, String accessid) {
        this.factWeb2PK = new FactWeb2PK(yyyymm, areaid, domainid, accessid);
    }

    public FactWeb2PK getFactWeb2PK() {
        return factWeb2PK;
    }

    public void setFactWeb2PK(FactWeb2PK factWeb2PK) {
        this.factWeb2PK = factWeb2PK;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAccess1() {
        return access1;
    }

    public void setAccess1(String access1) {
        this.access1 = access1;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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
        hash += (factWeb2PK != null ? factWeb2PK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactWeb2)) {
            return false;
        }
        FactWeb2 other = (FactWeb2) object;
        if ((this.factWeb2PK == null && other.factWeb2PK != null) || (this.factWeb2PK != null && !this.factWeb2PK.equals(other.factWeb2PK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.FactWeb2[ factWeb2PK=" + factWeb2PK + " ]";
    }
    
}
