package com.tcci.sksp.entity.ar;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nEO.Fu
 */
@Entity
@Table(name = "SK_SALES_ALLOWANCES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkSalesAllowances.findAll", query = "SELECT s FROM SkSalesAllowances s")})
public class SkSalesAllowances implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "SEQ_TCC")
    private Long id;
    @Size(max = 4)
    @Column(name = "TYPE")
    private String type;
    @Size(max = 4)
    @Column(name = "ORG")
    private String org;
    @Size(max = 2)
    @Column(name = "CHANNEL")
    private String channel;
    @Size(max = 2)
    @Column(name = "DEPT")
    private String dept;
    @Size(max = 1)
    @Column(name = "REF_ORDER")
    private String refOrder;
    @Size(max = 10)
    @Column(name = "BUYER")
    private String buyer;
    @Size(max = 10)
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
    @Size(max = 10)
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Size(max = 10)
    @Column(name = "APPLY_DATE")
    private String applyDate;
    @Size(max = 3)
    @Column(name = "REASON")
    private String reason;
    @Size(max = 6)
    @Column(name = "ITEM")
    private String item;
    @Size(max = 2)
    @Column(name = "BEGIN_END")
    private String beginEnd;
    @Size(max = 18)
    @Column(name = "PRODUCT")
    private String product;
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @Size(max = 3)
    @Column(name = "UNIT")
    private String unit;
    @Size(max = 4)
    @Column(name = "CONDITION")
    private String condition;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "SALES_ALLOWANCES")
    private BigDecimal salesAllowances;
    @Column(name = "PRICE_CONDITION")
    private Integer priceCondition;
    @Size(max = 3)
    @Column(name = "PRICE_UNIT")
    private String priceUnit;
    @Size(max = 3)
    @Column(name = "SAPID")
    private String sapid;
    @Column(name = "RETURN_NUMBER")
    private String returnNumber;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    private TcUser modifier;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;

    public SkSalesAllowances() {
    }

    public SkSalesAllowances(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getRefOrder() {
        return refOrder;
    }

    public void setRefOrder(String refOrder) {
        this.refOrder = refOrder;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
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

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getBeginEnd() {
        return beginEnd;
    }

    public void setBeginEnd(String beginEnd) {
        this.beginEnd = beginEnd;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public BigDecimal getSalesAllowances() {
        return salesAllowances;
    }

    public void setSalesAllowances(BigDecimal salesAllowances) {
        this.salesAllowances = salesAllowances;
    }

    public Integer getPriceCondition() {
        return priceCondition;
    }

    public void setPriceCondition(Integer priceCondition) {
        this.priceCondition = priceCondition;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public String getReturnNumber() {
        return returnNumber;
    }

    public void setReturnNumber(String returnNumber) {
        this.returnNumber = returnNumber;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
        if (!(object instanceof SkSalesAllowances)) {
            return false;
        }
        SkSalesAllowances other = (SkSalesAllowances) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ar.SkSalesAllowances[ id=" + id + " ]";
    }
}
