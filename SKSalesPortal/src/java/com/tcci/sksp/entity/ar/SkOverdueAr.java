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
@Table(name = "SK_OVERDUE_AR")
@Cacheable(value=false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkOverdueAr.findAll", query = "SELECT s FROM SkOverdueAr s"),
    @NamedQuery(name = "SkOverdueAr.findById", query = "SELECT s FROM SkOverdueAr s WHERE s.id = :id")
    })
public class SkOverdueAr implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="SEQ_SK_SYNC")
    @Column(name = "ID")
    private Long id;
    @Column(name = "BASELINE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date baselineTimestamp;
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name = "INVOICE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceTimestamp;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "INVOICE_AMOUNT")
    private BigDecimal invoiceAmount;
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
    @Column(name = "SAPID")
    private String sapid;
    @Column(name = "OVERDUE_DAYS_NUMBER")
    private Integer overdueDaysNumber;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "CUSTOMER", referencedColumnName = "ID")
    @ManyToOne
    private SkCustomer customer;

    public SkOverdueAr() {
    }

    public SkOverdueAr(Long id) {
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

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public Integer getOverdueDaysNumber() {
        return overdueDaysNumber;
    }

    public void setOverdueDaysNumber(Integer overdueDaysNumber) {
        this.overdueDaysNumber = overdueDaysNumber;
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
        if (!(object instanceof SkOverdueAr)) {
            return false;
        }
        SkOverdueAr other = (SkOverdueAr) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ar.SkOverdueAr[ id=" + id + " ]";
    }
    
}
