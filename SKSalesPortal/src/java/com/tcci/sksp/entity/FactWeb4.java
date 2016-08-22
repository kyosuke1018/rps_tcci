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
@Table(name = "FACT_WEB_4")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FactWeb4.findAll", query = "SELECT f FROM FactWeb4 f"),
    @NamedQuery(name = "FactWeb4.findByYyyymm", query = "SELECT f FROM FactWeb4 f WHERE f.factWeb4PK.yyyymm = :yyyymm"),
    @NamedQuery(name = "FactWeb4.findByYyyymmSapid", 
        query = "SELECT f FROM FactWeb4 f WHERE f.factWeb4PK.yyyymm = :yyyymm "
        + " and f.factWeb4PK.sapid= :sapid "),    
    @NamedQuery(name = "FactWeb4.findByYyyymmSalesman", 
        query = "SELECT f FROM FactWeb4 f WHERE f.factWeb4PK.yyyymm = :yyyymm "
        + " and (f.salesman = :salesman or f.factWeb4PK.sapid = :salesman)"),    
    @NamedQuery(name = "FactWeb4.findByArea", query = "SELECT f FROM FactWeb4 f WHERE f.area = :area"),
    @NamedQuery(name = "FactWeb4.findByAccess1", query = "SELECT f FROM FactWeb4 f WHERE f.access1 = :access1"),
    @NamedQuery(name = "FactWeb4.findByDomain", query = "SELECT f FROM FactWeb4 f WHERE f.domain = :domain"),
    @NamedQuery(name = "FactWeb4.findBySalesman", query = "SELECT f FROM FactWeb4 f WHERE f.salesman = :salesman"),
    @NamedQuery(name = "FactWeb4.findByPdtype", query = "SELECT f FROM FactWeb4 f WHERE f.pdtype = :pdtype"),
    @NamedQuery(name = "FactWeb4.findByAreaid", query = "SELECT f FROM FactWeb4 f WHERE f.factWeb4PK.areaid = :areaid"),
    @NamedQuery(name = "FactWeb4.findBySapid", query = "SELECT f FROM FactWeb4 f WHERE f.factWeb4PK.sapid = :sapid"),
    @NamedQuery(name = "FactWeb4.findByDomainid", query = "SELECT f FROM FactWeb4 f WHERE f.factWeb4PK.domainid = :domainid"),
    @NamedQuery(name = "FactWeb4.findByAccessid", query = "SELECT f FROM FactWeb4 f WHERE f.factWeb4PK.accessid = :accessid"),
    @NamedQuery(name = "FactWeb4.findBySpart", query = "SELECT f FROM FactWeb4 f WHERE f.factWeb4PK.spart = :spart"),
    @NamedQuery(name = "FactWeb4.findByInvoiceAmount", query = "SELECT f FROM FactWeb4 f WHERE f.invoiceAmount = :invoiceAmount"),
    @NamedQuery(name = "FactWeb4.findByCost", query = "SELECT f FROM FactWeb4 f WHERE f.cost = :cost"),
    @NamedQuery(name = "FactWeb4.findBySalesReturn", query = "SELECT f FROM FactWeb4 f WHERE f.salesReturn = :salesReturn"),
    @NamedQuery(name = "FactWeb4.findByPremiumDiscount", query = "SELECT f FROM FactWeb4 f WHERE f.premiumDiscount = :premiumDiscount"),
    @NamedQuery(name = "FactWeb4.findByBudget", query = "SELECT f FROM FactWeb4 f WHERE f.budget = :budget"),
    @NamedQuery(name = "FactWeb4.findByBudgetTarget", query = "SELECT f FROM FactWeb4 f WHERE f.budgetTarget = :budgetTarget"),
    @NamedQuery(name = "FactWeb4.findByBcost", query = "SELECT f FROM FactWeb4 f WHERE f.bcost = :bcost"),
    @NamedQuery(name = "FactWeb4.findBySalesDiscount", query = "SELECT f FROM FactWeb4 f WHERE f.salesDiscount = :salesDiscount"),
    @NamedQuery(name = "FactWeb4.findBySalesAmount", query = "SELECT f FROM FactWeb4 f WHERE f.salesAmount = :salesAmount"),
    @NamedQuery(name = "FactWeb4.findByCumulativeAchivementRate", query = "SELECT f FROM FactWeb4 f WHERE f.cumulativeAchivementRate = :cumulativeAchivementRate"),
    @NamedQuery(name = "FactWeb4.findByMonthAchivementRate", query = "SELECT f FROM FactWeb4 f WHERE f.monthAchivementRate = :monthAchivementRate"),
    @NamedQuery(name = "FactWeb4.findByGrossProfitRate", query = "SELECT f FROM FactWeb4 f WHERE f.grossProfitRate = :grossProfitRate"),
    @NamedQuery(name = "FactWeb4.findByLight", query = "SELECT f FROM FactWeb4 f WHERE f.light = :light")})
public class FactWeb4 implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FactWeb4PK factWeb4PK;
    @Size(max = 6)
    @Column(name = "AREA")
    private String area;
    @Size(max = 15)
    @Column(name = "ACCESS_1")
    private String access1;
    @Size(max = 20)
    @Column(name = "DOMAIN")
    private String domain;
    @Size(max = 87)
    @Column(name = "SALESMAN")
    private String salesman;
    @Size(max = 40)
    @Column(name = "PDTYPE")
    private String pdtype;
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
    @Column(name = "LIGHT")
    private BigDecimal light;
    @Column(name = "RESPONSE_NAME")
    private String responseName;
    @Column(name = "RESPONSE_PHONE")
    private String responsePhone;

    public FactWeb4() {
    }

    public FactWeb4(FactWeb4PK factWeb4PK) {
        this.factWeb4PK = factWeb4PK;
    }

    public FactWeb4(String yyyymm, String areaid, String sapid, String domainid, String accessid, String spart) {
        this.factWeb4PK = new FactWeb4PK(yyyymm, areaid, sapid, domainid, accessid, spart);
    }

    public FactWeb4PK getFactWeb4PK() {
        return factWeb4PK;
    }

    public void setFactWeb4PK(FactWeb4PK factWeb4PK) {
        this.factWeb4PK = factWeb4PK;
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

    public String getPdtype() {
        return pdtype;
    }

    public void setPdtype(String pdtype) {
        this.pdtype = pdtype;
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
        hash += (factWeb4PK != null ? factWeb4PK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactWeb4)) {
            return false;
        }
        FactWeb4 other = (FactWeb4) object;
        if ((this.factWeb4PK == null && other.factWeb4PK != null) || (this.factWeb4PK != null && !this.factWeb4PK.equals(other.factWeb4PK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.FactWeb4[ factWeb4PK=" + factWeb4PK + " ]";
    }
    
}
