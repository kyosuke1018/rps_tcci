/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.ar;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.sksp.entity.enums.PaymentTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lynn.Huang
 */
@Entity
@Table(name = "SK_AR_REMIT_ITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkArRemitItem.findAll", query = "SELECT s FROM SkArRemitItem s"),
    @NamedQuery(name = "SkArRemitItem.findById", query = "SELECT s FROM SkArRemitItem s WHERE s.id = :id"),
    @NamedQuery(name = "SkArRemitItem.findByOrderNumber", query = "SELECT s FROM SkArRemitItem s WHERE s.orderNumber = :orderNumber"),
    @NamedQuery(name = "SkArRemitItem.findByInvoiceNumber", query = "SELECT s FROM SkArRemitItem s WHERE s.invoiceNumber = :invoiceNumber"),
    @NamedQuery(name = "SkArRemitItem.findByInvoiceTimestamp", query = "SELECT s FROM SkArRemitItem s WHERE s.invoiceTimestamp = :invoiceTimestamp"),
    @NamedQuery(name = "SkArRemitItem.findByArAmount", query = "SELECT s FROM SkArRemitItem s WHERE s.arAmount = :arAmount"),
    @NamedQuery(name = "SkArRemitItem.findByPremiumDiscount", query = "SELECT s FROM SkArRemitItem s WHERE s.premiumDiscount = :premiumDiscount"),
    @NamedQuery(name = "SkArRemitItem.findByArDueDate", query = "SELECT s FROM SkArRemitItem s WHERE s.arDueDate = :arDueDate"),
    @NamedQuery(name = "SkArRemitItem.findBySalesDiscount", query = "SELECT s FROM SkArRemitItem s WHERE s.salesDiscount = :salesDiscount"),
    @NamedQuery(name = "SkArRemitItem.findBySalesReturn", query = "SELECT s FROM SkArRemitItem s WHERE s.salesReturn = :salesReturn"),
    @NamedQuery(name = "SkArRemitItem.findByNegativeAr", query = "SELECT s FROM SkArRemitItem s WHERE s.negativeAr = :negativeAr"),
    @NamedQuery(name = "SkArRemitItem.findByDifferenceCharge", query = "SELECT s FROM SkArRemitItem s WHERE s.differenceCharge = :differenceCharge"),
    @NamedQuery(name = "SkArRemitItem.findByPaymentType", query = "SELECT s FROM SkArRemitItem s WHERE s.paymentType = :paymentType"),
    @NamedQuery(name = "SkArRemitItem.findByCheckNumber", query = "SELECT s FROM SkArRemitItem s WHERE s.checkNumber = :checkNumber"),
    @NamedQuery(name = "SkArRemitItem.findByAmount", query = "SELECT s FROM SkArRemitItem s WHERE s.amount = :amount"),
    @NamedQuery(name = "SkArRemitItem.findByCreatetimestamp", query = "SELECT s FROM SkArRemitItem s WHERE s.createtimestamp = :createtimestamp"),
    @NamedQuery(name = "SkArRemitItem.findByModifytimestamp", query = "SELECT s FROM SkArRemitItem s WHERE s.modifytimestamp = :modifytimestamp")})
public class SkArRemitItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    private Long id;
    @Size(max = 30)
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
    @Size(max = 10)
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name = "INVOICE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceTimestamp;
    @Column(name = "AR_AMOUNT")
    private BigDecimal arAmount;
    @Column(name = "PREMIUM_DISCOUNT")
    private BigDecimal premiumDiscount;
    @Column(name = "AR_DUE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arDueDate;
    @Column(name = "SALES_DISCOUNT")
    private BigDecimal salesDiscount;
    @Column(name = "SALES_RETURN")
    private BigDecimal salesReturn;
    @Column(name = "NEGATIVE_AR")
    private BigDecimal negativeAr;
    @Column(name = "DIFFERENCE_CHARGE")
    private Short differenceCharge;
    @Column(name = "ADVANCE_RECEIPTS_A")
    private BigDecimal advanceReceiptsA;
    @Column(name = "ADVANCE_RECEIPTS_J")
    private BigDecimal advanceReceiptsJ;    
    @Column(name = "PAYMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private PaymentTypeEnum paymentType;
    @Column(name = "PAYMENT_TYPE2")
    @Enumerated(EnumType.STRING)
    private PaymentTypeEnum paymentType2;    
    @Size(max = 20)
    @Column(name = "CHECK_NUMBER")
    private String checkNumber;
    @Size(max = 20)
    @Column(name = "CHECK_NUMBER2")
    private String checkNumber2;    
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "AMOUNT2")
    private BigDecimal amount2;    
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @JoinColumn(name = "AR_REMIT_MASTER", referencedColumnName = "ID")
    @ManyToOne
    private SkArRemitMaster arRemitMaster;    
    
    public SkArRemitItem() {
    }

    public SkArRemitItem(Long id) {
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

    public Date getArDueDate() {
        return arDueDate;
    }

    public void setArDueDate(Date arDueDate) {
        this.arDueDate = arDueDate;
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

    public BigDecimal getNegativeAr() {
        return negativeAr;
    }

    public void setNegativeAr(BigDecimal negativeAr) {
        this.negativeAr = negativeAr;
    }

    public Short getDifferenceCharge() {
        return differenceCharge;
    }

    public void setDifferenceCharge(Short differenceCharge) {
        this.differenceCharge = differenceCharge;
    }

    public BigDecimal getAdvanceReceiptsA() {
        return advanceReceiptsA;
    }

    public void setAdvanceReceiptsA(BigDecimal advanceReceiptsA) {
        this.advanceReceiptsA = advanceReceiptsA;
    }
    
    public BigDecimal getAdvanceReceiptsJ() {
        return advanceReceiptsJ;
    }

    public void setAdvanceReceiptsJ(BigDecimal advanceReceiptsJ) {
        this.advanceReceiptsJ = advanceReceiptsJ;
    }    

    public PaymentTypeEnum getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeEnum paymentType) {
        this.paymentType = paymentType;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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


    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public SkArRemitMaster getArRemitMaster() {
        return arRemitMaster;
    }

    public void setArRemitMaster(SkArRemitMaster arRemitMaster) {
        this.arRemitMaster = arRemitMaster;
    }

    public BigDecimal getAmount2() {
        return amount2;
    }

    public void setAmount2(BigDecimal amount2) {
        this.amount2 = amount2;
    }

    public String getCheckNumber2() {
        return checkNumber2;
    }

    public void setCheckNumber2(String checkNumber2) {
        this.checkNumber2 = checkNumber2;
    }

    public PaymentTypeEnum getPaymentType2() {
        return paymentType2;
    }

    public void setPaymentType2(PaymentTypeEnum paymentType2) {
        this.paymentType2 = paymentType2;
    }  
    
    public BigDecimal getAmountSum() {
        if (getAmount() == null && getAmount2() == null) {
            return null;
        } else if (getAmount() != null && getAmount2() == null) {
            return getAmount();
        } else if (getAmount() == null && getAmount2() != null) {
            return getAmount2();
        } else {
            return getAmount().add(getAmount2());
        }       
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
        if (!(object instanceof SkArRemitItem)) {
            return false;
        }
        SkArRemitItem other = (SkArRemitItem) object;
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
