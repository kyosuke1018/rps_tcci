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
@Table(name = "FACT_WEB_3")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FactWeb3.findAll", query = "SELECT f FROM FactWeb3 f"),
    @NamedQuery(name = "FactWeb3.findByYyyymm", query = "SELECT f FROM FactWeb3 f WHERE f.factWeb3PK.yyyymm = :yyyymm"),
    @NamedQuery(name = "FactWeb3.findByYyyymmDomain", 
        query = "SELECT f FROM FactWeb3 f WHERE f.factWeb3PK.yyyymm = :yyyymm "
        + " and (f.domain = :domain or f.factWeb3PK.domainid = :domain)"),
    @NamedQuery(name = "FactWeb3.findByArea", query = "SELECT f FROM FactWeb3 f WHERE f.area = :area"),
    @NamedQuery(name = "FactWeb3.findByAccess1", query = "SELECT f FROM FactWeb3 f WHERE f.access1 = :access1"),
    @NamedQuery(name = "FactWeb3.findByDomain", query = "SELECT f FROM FactWeb3 f WHERE f.domain = :domain"),
    @NamedQuery(name = "FactWeb3.findBySalesman", query = "SELECT f FROM FactWeb3 f WHERE f.salesman = :salesman"),
    @NamedQuery(name = "FactWeb3.findByAreaid", query = "SELECT f FROM FactWeb3 f WHERE f.factWeb3PK.areaid = :areaid"),
    @NamedQuery(name = "FactWeb3.findBySapid", query = "SELECT f FROM FactWeb3 f WHERE f.factWeb3PK.sapid = :sapid"),
    @NamedQuery(name = "FactWeb3.findByDomainid", query = "SELECT f FROM FactWeb3 f WHERE f.factWeb3PK.domainid = :domainid"),
    @NamedQuery(name = "FactWeb3.findByAccessid", query = "SELECT f FROM FactWeb3 f WHERE f.factWeb3PK.accessid = :accessid"),
    @NamedQuery(name = "FactWeb3.findByShouldPayAmount", query = "SELECT f FROM FactWeb3 f WHERE f.shouldPayAmount = :shouldPayAmount"),
    @NamedQuery(name = "FactWeb3.findByPaymentAmount", query = "SELECT f FROM FactWeb3 f WHERE f.paymentAmount = :paymentAmount"),
    @NamedQuery(name = "FactWeb3.findByInvoiceAmount", query = "SELECT f FROM FactWeb3 f WHERE f.invoiceAmount = :invoiceAmount"),
    @NamedQuery(name = "FactWeb3.findByCost", query = "SELECT f FROM FactWeb3 f WHERE f.cost = :cost"),
    @NamedQuery(name = "FactWeb3.findBySalesReturn", query = "SELECT f FROM FactWeb3 f WHERE f.salesReturn = :salesReturn"),
    @NamedQuery(name = "FactWeb3.findByPremiumDiscount", query = "SELECT f FROM FactWeb3 f WHERE f.premiumDiscount = :premiumDiscount"),
    @NamedQuery(name = "FactWeb3.findByBudget", query = "SELECT f FROM FactWeb3 f WHERE f.budget = :budget"),
    @NamedQuery(name = "FactWeb3.findByBudgetTarget", query = "SELECT f FROM FactWeb3 f WHERE f.budgetTarget = :budgetTarget"),
    @NamedQuery(name = "FactWeb3.findByBcost", query = "SELECT f FROM FactWeb3 f WHERE f.bcost = :bcost"),
    @NamedQuery(name = "FactWeb3.findBySalesDiscount", query = "SELECT f FROM FactWeb3 f WHERE f.salesDiscount = :salesDiscount"),
    @NamedQuery(name = "FactWeb3.findBySalesAmount", query = "SELECT f FROM FactWeb3 f WHERE f.salesAmount = :salesAmount"),
    @NamedQuery(name = "FactWeb3.findByCumulativeAchivementRate", query = "SELECT f FROM FactWeb3 f WHERE f.cumulativeAchivementRate = :cumulativeAchivementRate"),
    @NamedQuery(name = "FactWeb3.findByMonthAchivementRate", query = "SELECT f FROM FactWeb3 f WHERE f.monthAchivementRate = :monthAchivementRate"),
    @NamedQuery(name = "FactWeb3.findByGrossProfitRate", query = "SELECT f FROM FactWeb3 f WHERE f.grossProfitRate = :grossProfitRate"),
    @NamedQuery(name = "FactWeb3.findByPaymentRate", query = "SELECT f FROM FactWeb3 f WHERE f.paymentRate = :paymentRate"),
    @NamedQuery(name = "FactWeb3.findByLight", query = "SELECT f FROM FactWeb3 f WHERE f.light = :light")})
public class FactWeb3 implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FactWeb3PK factWeb3PK;
    @Size(max = 6)
    @Column(name = "AREA")
    private String area;
    @Size(max = 15)
    @Column(name = "ACCESS_1")
    private String access1;
    @Size(max = 20)
    @Column(name = "DOMAIN")
    private String domain;
    @Size(max = 50)
    @Column(name = "SALESMAN")
    private String salesman;
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

    public FactWeb3() {
    }

    public FactWeb3(FactWeb3PK factWeb3PK) {
        this.factWeb3PK = factWeb3PK;
    }

    public FactWeb3(String yyyymm, String areaid, String sapid, String domainid, String accessid) {
        this.factWeb3PK = new FactWeb3PK(yyyymm, areaid, sapid, domainid, accessid);
    }

    public FactWeb3PK getFactWeb3PK() {
        return factWeb3PK;
    }

    public void setFactWeb3PK(FactWeb3PK factWeb3PK) {
        this.factWeb3PK = factWeb3PK;
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

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
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
        hash += (factWeb3PK != null ? factWeb3PK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactWeb3)) {
            return false;
        }
        FactWeb3 other = (FactWeb3) object;
        if ((this.factWeb3PK == null && other.factWeb3PK != null) || (this.factWeb3PK != null && !this.factWeb3PK.equals(other.factWeb3PK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.FactWeb3[ factWeb3PK=" + factWeb3PK + " ]";
    }
    
}
