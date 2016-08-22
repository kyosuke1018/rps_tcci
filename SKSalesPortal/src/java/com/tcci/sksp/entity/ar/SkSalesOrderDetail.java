/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.ar;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_SALES_ORDER_DETAIL")
@Cacheable(value = false)
@NamedQueries({
    @NamedQuery(name = "SkSalesOrderDetail.findAll", query = "SELECT s FROM SkSalesOrderDetail s"),
    @NamedQuery(name = "SkSalesOrderDetail.findById", query = "SELECT s FROM SkSalesOrderDetail s WHERE s.id = :id"),
    @NamedQuery(name = "SkSalesOrderDetail.findByProductNumber", query = "SELECT s FROM SkSalesOrderDetail s WHERE s.productNumber = :productNumber"),
    @NamedQuery(name = "SkSalesOrderDetail.findByPremiumDiscount", query = "SELECT s FROM SkSalesOrderDetail s WHERE s.premiumDiscount = :premiumDiscount"),
    @NamedQuery(name = "SkSalesOrderDetail.findByPremiumDiscountTax", query = "SELECT s FROM SkSalesOrderDetail s WHERE s.premiumDiscountTax = :premiumDiscountTax")})
public class SkSalesOrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "PRODUCT_NUMBER")
    private String productNumber;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PREMIUM_DISCOUNT")
    private BigDecimal premiumDiscount;
    @Column(name = "PREMIUM_DISCOUNT_TAX")
    private BigDecimal premiumDiscountTax;
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @Column(name = "PRICE")
    private BigDecimal price;
    @JoinColumn(name = "SALES_ORDER", referencedColumnName = "ID")
    @ManyToOne
    private SkSalesOrderMaster salesOrder;

    public SkSalesOrderDetail() {
    }

    public SkSalesOrderDetail(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public BigDecimal getPremiumDiscount() {
        return premiumDiscount;
    }

    public void setPremiumDiscount(BigDecimal premiumDiscount) {
        this.premiumDiscount = premiumDiscount;
    }

    public BigDecimal getPremiumDiscountTax() {
        return premiumDiscountTax;
    }

    public void setPremiumDiscountTax(BigDecimal premiumDiscountTax) {
        this.premiumDiscountTax = premiumDiscountTax;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public SkSalesOrderMaster getSalesOrder() {
        return salesOrder;
    }
    
    public void setSalesOrder(SkSalesOrderMaster salesOrder) {
        this.salesOrder = salesOrder;
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
        if (!(object instanceof SkSalesOrderDetail)) {
            return false;
        }
        SkSalesOrderDetail other = (SkSalesOrderDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ar.SkSalesOrderDetail[ id=" + id + " ]";
    }
}
