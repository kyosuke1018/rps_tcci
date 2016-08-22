package com.tcci.sksp.entity.ar;

import com.tcci.sksp.entity.SkCustomer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_PAYMENT_ITEM")
@Cacheable(value=false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkPaymentItem.findAll", query = "SELECT s FROM SkPaymentItem s"),
    @NamedQuery(name = "SkPaymentItem.findById", query = "SELECT s FROM SkPaymentItem s WHERE s.id = :id")})
public class SkPaymentItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="SEQ_SK_SYNC")
    @Column(name = "ID")
    private Long id;
    @Column(name = "BASELINE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date baselineTimestamp;
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name = "SAPID")
    private String sapid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AR_AMOUNT")
    private BigDecimal arAmount;
    @Column(name = "PREMIUM_DISCOUNT")
    private BigDecimal premiumDiscount;
    @Column(name = "SALES_RETURN")
    private BigDecimal salesReturn;
    @Column(name = "SALES_DISCOUNT")
    private BigDecimal salesDiscount;
    @Column(name = "PAYMENT_AMOUNT")
    private BigDecimal paymentAmount;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "CUSTOMER", referencedColumnName = "ID")
    @ManyToOne
    private SkCustomer customer;

    public SkPaymentItem() {
    }

    public SkPaymentItem(Long id) {
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public BigDecimal getArAmount() {
        return arAmount;
    }

    public void setArAmount(BigDecimal arAmount) {
        this.arAmount = arAmount;
    }

    public BigDecimal getPremiumDiscount() {
        return premiumDiscount;
    }

    public void setPremiumDiscount(BigDecimal premiumDiscount) {
        this.premiumDiscount = premiumDiscount;
    }

    public BigDecimal getSalesReturn() {
        return salesReturn;
    }

    public void setSalesReturn(BigDecimal salesReturn) {
        this.salesReturn = salesReturn;
    }

    public BigDecimal getSalesDiscount() {
        return salesDiscount;
    }

    public void setSalesDiscount(BigDecimal salesDiscount) {
        this.salesDiscount = salesDiscount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public SkCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SkCustomer customer) {
        this.customer = customer;
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
        if (!(object instanceof SkPaymentItem)) {
            return false;
        }
        SkPaymentItem other = (SkPaymentItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ar.SkPaymentItem[ id=" + id + " ]";
    }
    
}
