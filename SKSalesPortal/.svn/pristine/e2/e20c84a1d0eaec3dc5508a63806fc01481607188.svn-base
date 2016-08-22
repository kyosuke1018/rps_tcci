package com.tcci.sksp.entity.ar;

import com.tcci.fc.entity.essential.Persistable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nEO.Fu
 */
@Entity
@Table(name = "SK_PREMIUM_DISCOUNT")
@XmlRootElement
public class SkPremiumDiscount implements Serializable,Persistable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="SEQ_TCC")
    @Column(name = "ID")
    private Long id;
    @Column(name="ORDER_NUMBER") 
    private String orderNumber;
    @Column(name="ORDER_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTimestamp;
    @Column(name="INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name="INVOICE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceTimestamp;
    @JoinColumn(name="CUSTOMER", referencedColumnName="ID")
    private SkCustomer customer;
    @Column(name = "DISCOUNT")
    private BigDecimal discount;
    @Column(name = "TAX")
    private BigDecimal tax;
    @Column(name="YEAR")
    private String year;
    @Column (name="MONTH")
    private String month;
    @JoinColumn(name = "INTERFACE_ID", referencedColumnName="ID")
    @ManyToOne
    private SkFiMasterInterface fiInterface;
    @JoinColumn (name="CREATOR", referencedColumnName="ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn (name="MODIFIER", referencedColumnName="ID")
    @ManyToOne
    private TcUser modifier;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @Column(name="sapid")
    private String sapid;

    public SkPremiumDiscount() {
    }

    public SkPremiumDiscount(Long id) {
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

    public SkCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SkCustomer customer) {
        this.customer = customer;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    
    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public SkFiMasterInterface getFiInterface() {
        return fiInterface;
    }

    public void setFiInterface(SkFiMasterInterface fiInterface) {
        this.fiInterface = fiInterface;
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
        if (!(object instanceof SkPremiumDiscount)) {
            return false;
        }
        SkPremiumDiscount other = (SkPremiumDiscount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + "" + getId();
    }
    
}
