/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.ar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcci.sksp.entity.SkCustomer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_SALES_ORDER_MASTER")
@Cacheable(value = false)
@NamedQueries({
    @NamedQuery(name = "SkSalesOrderMaster.findAll", query = "SELECT s FROM SkSalesOrderMaster s"),
    @NamedQuery(name = "SkSalesOrderMaster.findById", query = "SELECT s FROM SkSalesOrderMaster s WHERE s.id = :id"),
    @NamedQuery(name = "SkSalesOrderMaster.findByOrderNumber", query = "SELECT s FROM SkSalesOrderMaster s WHERE s.orderNumber = :orderNumber"),
    @NamedQuery(name = "SkSalesOrderMaster.findByOrderTimestamp", query = "SELECT s FROM SkSalesOrderMaster s WHERE s.orderTimestamp = :orderTimestamp"),
    @NamedQuery(name = "SkSalesOrderMaster.findByInvoiceNumber", query = "SELECT s FROM SkSalesOrderMaster s WHERE s.invoiceNumber = :invoiceNumber"),
    @NamedQuery(name = "SkSalesOrderMaster.findByInvoiceTimestamp", query = "SELECT s FROM SkSalesOrderMaster s WHERE s.invoiceTimestamp = :invoiceTimestamp"),
    @NamedQuery(name = "SkSalesOrderMaster.findByAmount", query = "SELECT s FROM SkSalesOrderMaster s WHERE s.amount = :amount")})
public class SkSalesOrderMaster implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
    @Column(name = "ORDER_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTimestamp;
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name = "INVOICE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceTimestamp;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    /**
     * @deprecated
     */
    @Column(name = "SALES_GROUP")
    private String salesGroup;
    @Column(name = "SAPID")
    private String sapid;
    /**
     * @deprecated
     */
    @Column(name = "PREMIUM_DISCOUNT")
    private BigDecimal premiumDiscount;
    /**
     * @deprecated
     */
    @Column(name = "SALES_DISCOUNT")
    private BigDecimal salesDiscount;
    /**
     * @deprecated
     */
    @Column(name = "SALES_RETURN")
    private BigDecimal salesReturn;
    /**
     * @deprecated
     */
    @Column(name = "TOTAL_COST")
    private BigDecimal totalCost;
    @JoinColumn(name = "CUSTOMER", referencedColumnName = "ID")
    @ManyToOne
    private SkCustomer customer;
    @JsonIgnore
    @OneToMany(mappedBy = "salesOrder")
    private Collection<SkSalesOrderDetail> skSalesOrderDetailCollection;

    /*
    @OneToMany(mappedBy = "salesOrderMaster")
    private Collection<SkSalesAllowanceLog> skSalesAllowanceLogCollection;
     */
    public SkSalesOrderMaster() {
    }

    public SkSalesOrderMaster(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(Date orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceTimestamp() {
        return invoiceTimestamp;
    }

    public void setInvoiceTimestamp(Date invoiceTimestamp) {
        this.invoiceTimestamp = invoiceTimestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @deprecated
     */
    public BigDecimal getPremiumDiscount() {
        return premiumDiscount;
    }

    /**
     * @deprecated
     */
    public void setPremiumDiscount(BigDecimal premiumDiscount) {
        this.premiumDiscount = premiumDiscount;
    }

    /**
     * @deprecated
     */
    public BigDecimal getSalesDiscount() {
        return salesDiscount;
    }

    /**
     * @deprecated
     */
    public void setSalesDiscount(BigDecimal salesDiscount) {
        this.salesDiscount = salesDiscount;
    }

    /**
     * @deprecated
     */
    public String getSalesGroup() {
        return salesGroup;
    }

    /**
     * @deprecated
     */
    public void setSalesGroup(String salesGroup) {
        this.salesGroup = salesGroup;
    }

    /**
     * @deprecated
     */
    public BigDecimal getSalesReturn() {
        return salesReturn;
    }

    /**
     * @deprecated
     */
    public void setSalesReturn(BigDecimal salesReturn) {
        this.salesReturn = salesReturn;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    /**
     * @deprecated
     */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /**
     * @deprecated
     */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public SkCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SkCustomer customer) {
        this.customer = customer;
    }

    public Collection<SkSalesOrderDetail> getSkSalesOrderDetailCollection() {
        return skSalesOrderDetailCollection;
    }

    public void setSkSalesOrderDetailCollection(Collection<SkSalesOrderDetail> skSalesOrderDetailCollection) {
        this.skSalesOrderDetailCollection = skSalesOrderDetailCollection;
    }

    /*
    public Collection<SkSalesAllowanceLog> getSkSalesAllowanceLogCollection() {
        return skSalesAllowanceLogCollection;
    }

    public void setSkSalesAllowanceLogCollection(Collection<SkSalesAllowanceLog> skSalesAllowanceLogCollection) {
        this.skSalesAllowanceLogCollection = skSalesAllowanceLogCollection;
    }
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SkSalesOrderMaster)) {
            return false;
        }
        SkSalesOrderMaster other = (SkSalesOrderMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ar.SkSalesOrderMaster[ id=" + id + " ]";
    }
}
