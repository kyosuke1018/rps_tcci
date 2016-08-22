/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.ar;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.sksp.entity.SkCustomer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
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
@Table(name = "SK_ACCOUNTS_RECEIVABLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkAccountsReceivable.findAll", query = "SELECT s FROM SkAccountsReceivable s"),
    @NamedQuery(name = "SkAccountsReceivable.findById", query = "SELECT s FROM SkAccountsReceivable s WHERE s.id = :id"),
    @NamedQuery(name = "SkAccountsReceivable.findByOrderNumber", query = "SELECT s FROM SkAccountsReceivable s WHERE s.orderNumber = :orderNumber"),
    @NamedQuery(name = "SkAccountsReceivable.findByInvoiceNumber", query = "SELECT s FROM SkAccountsReceivable s WHERE s.invoiceNumber = :invoiceNumber"),
    @NamedQuery(name = "SkAccountsReceivable.findByInvoiceTimestamp", query = "SELECT s FROM SkAccountsReceivable s WHERE s.invoiceTimestamp = :invoiceTimestamp"),
    @NamedQuery(name = "SkAccountsReceivable.findByAmount", query = "SELECT s FROM SkAccountsReceivable s WHERE s.amount = :amount"),
    @NamedQuery(name = "SkAccountsReceivable.findByPremiumDiscount", query = "SELECT s FROM SkAccountsReceivable s WHERE s.premiumDiscount = :premiumDiscount"),
    @NamedQuery(name = "SkAccountsReceivable.findBySapid", query = "SELECT s FROM SkAccountsReceivable s WHERE s.sapid = :sapid"),
    @NamedQuery(name = "SkAccountsReceivable.findByArDueDate", query = "SELECT s FROM SkAccountsReceivable s WHERE s.arDueDate = :arDueDate"),
    @NamedQuery(name = "SkAccountsReceivable.findByCreatetimestamp", query = "SELECT s FROM SkAccountsReceivable s WHERE s.createtimestamp = :createtimestamp"),
    @NamedQuery(name = "SkAccountsReceivable.findByModifytimestamp", query = "SELECT s FROM SkAccountsReceivable s WHERE s.modifytimestamp = :modifytimestamp")})
public class SkAccountsReceivable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    private Long id;
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name = "INVOICE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceTimestamp;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "PREMIUM_DISCOUNT")
    private BigDecimal premiumDiscount;
    @Column(name = "SAPID")
    private String sapid;
    @Column(name = "AR_DUE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arDueDate;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    /*
    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;
     * 
     */
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @JoinColumn(name = "CUSTOMER", referencedColumnName = "ID")
    @ManyToOne
    private SkCustomer customer;

    public SkAccountsReceivable() {
    }

    public SkAccountsReceivable(Long id) {
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

    public BigDecimal getPremiumDiscount() {
        return premiumDiscount;
    }

    public void setPremiumDiscount(BigDecimal premiumDiscount) {
        this.premiumDiscount = premiumDiscount;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public Date getArDueDate() {
        return arDueDate;
    }

    public void setArDueDate(Date arDueDate) {
        this.arDueDate = arDueDate;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

    /*
    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }
     */
    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
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
        if (!(object instanceof SkAccountsReceivable)) {
            return false;
        }
        SkAccountsReceivable other = (SkAccountsReceivable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + ":" + getId();
    }
    
}
