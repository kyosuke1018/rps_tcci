/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.ar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "SK_SALES_DETAILS")
@Cacheable(value=false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkSalesDetails.findAll", query = "SELECT s FROM SkSalesDetails s")})
public class SkSalesDetails implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "BILLING_TYPE")
    private String billingType;
    @Column(name = "BILLING_DOC")
    private String billingDoc;
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name = "INVOICE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceTimestamp;
    @Column(name = "PAYER_CUSTOMER")
    private String payerCustomer;
    @Column(name = "BUYER_CUSTOMER")
    private String buyerCustomer;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "INVOICE_AMOUNT")
    private BigDecimal invoiceAmount;
    @Column(name = "SALES_ORG")
    private String salesOrg;
    @Column(name = "SALES_CHANNEL")
    private String salesChannel;
    @Column(name = "SETTLEMENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settlementTimestamp;
    @Column(name = "BILLING_ITEM")
    private Integer billingItem;
    @Column(name = "PREVIOUS_LAYER")
    private Integer previousLayer;
    @Column(name = "SAPID")
    private String sapid;
    @Column(name = "PRODUCT_NUMBER")
    private String productNumber;
    @Column(name = "LOGISTIC_SITE")
    private String logisticSite;
    @Column(name = "LOT_NUMBER")
    private String lotNumber;
    @Column(name = "UNIT")
    private String unit;
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @Column(name = "SELLING_PRICE")
    private BigDecimal sellingPrice;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "PREMIUM_DISCOUNT")
    private BigDecimal premiumDiscount;
    @Column(name = "PREMIUM_DISCOUNT_TAX")
    private BigDecimal premiumDiscountTax;
    @Column(name = "COST")
    private BigDecimal cost;
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
    @Column(name = "ORDER_ITEM")
    private Integer orderItem;
    @Column(name = "DELIVERY_NUMBER")
    private String deliveryNumber;
    @Column(name = "DELIVERY_ITEM")
    private Integer deliveryItem;
    @Column(name = "PREMIUM_DISCOUNT_NUMBER")
    private String premiumDiscountNumber;
    @Column(name = "PREMIUM_DISCOUNT_ITEM")
    private Integer premiumDiscountItem;
    @Column(name = "ORDER_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTimestamp;
    @Column(name = "ORDER_TYPE")
    private String orderType;
    @Column(name = "CONTRACT_NUMBER")
    private String contractNumber;
    @Column(name = "BUSINESS_CODE")
    private Integer businessCode;
    @Column(name = "SAP_CREATE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sapCreateTimestamp;
    @Column(name = "ORDER_TEXT")
    private String orderText;
    @Column(name = "RETURN_AMOUNT")
    private BigDecimal returnAmount;
    @Column(name = "RETURN_AMOUNT_TAX")
    private BigDecimal returnAmountTax;
    @Column(name = "RETURN_REASON")
    private String returnReason;
    @Column(name = "QUOTATION_NUMBER")
    private String quotationNumber;
    @Column(name = "QUOATION_ITEM")
    private String quoationItem;
    @Column(name = "RETURN_RATE")
    private BigInteger returnRate;
    @Column(name = "CHECK_DATE")
    private String checkDate;
    @Column(name = "CHECK_ID")
    private String checkId;
    @Column(name = "SHIPPING_CONDITIONS")
    private String shippingConditions;
    @Column(name = "VSBED")
    private String vsbed;
    @Column(name = "TAX_TYPE")
    private String taxType;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "INSERT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertTimestamp;
    @Column(name = "UPDATE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    public SkSalesDetails() {
    }

    public SkSalesDetails(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getBillingDoc() {
        return billingDoc;
    }

    public void setBillingDoc(String billingDoc) {
        this.billingDoc = billingDoc;
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

    public String getPayerCustomer() {
        return payerCustomer;
    }

    public void setPayerCustomer(String payerCustomer) {
        this.payerCustomer = payerCustomer;
    }

    public String getBuyerCustomer() {
        return buyerCustomer;
    }

    public void setBuyerCustomer(String buyerCustomer) {
        this.buyerCustomer = buyerCustomer;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getSalesOrg() {
        return salesOrg;
    }

    public void setSalesOrg(String salesOrg) {
        this.salesOrg = salesOrg;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public Date getSettlementTimestamp() {
        return settlementTimestamp;
    }

    public void setSettlementTimestamp(Date settlementTimestamp) {
        this.settlementTimestamp = settlementTimestamp;
    }

    public Integer getBillingItem() {
        return billingItem;
    }

    public void setBillingItem(Integer billingItem) {
        this.billingItem = billingItem;
    }

    public Integer getPreviousLayer() {
        return previousLayer;
    }

    public void setPreviousLayer(Integer previousLayer) {
        this.previousLayer = previousLayer;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getLogisticSite() {
        return logisticSite;
    }

    public void setLogisticSite(String logisticSite) {
        this.logisticSite = logisticSite;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(Integer orderItem) {
        this.orderItem = orderItem;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public Integer getDeliveryItem() {
        return deliveryItem;
    }

    public void setDeliveryItem(Integer deliveryItem) {
        this.deliveryItem = deliveryItem;
    }

    public String getPremiumDiscountNumber() {
        return premiumDiscountNumber;
    }

    public void setPremiumDiscountNumber(String premiumDiscountNumber) {
        this.premiumDiscountNumber = premiumDiscountNumber;
    }

    public Integer getPremiumDiscountItem() {
        return premiumDiscountItem;
    }

    public void setPremiumDiscountItem(Integer premiumDiscountItem) {
        this.premiumDiscountItem = premiumDiscountItem;
    }

    public Date getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(Date orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Integer getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(Integer businessCode) {
        this.businessCode = businessCode;
    }

    public Date getSapCreateTimestamp() {
        return sapCreateTimestamp;
    }

    public void setSapCreateTimestamp(Date sapCreateTimestamp) {
        this.sapCreateTimestamp = sapCreateTimestamp;
    }

    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public BigDecimal getReturnAmountTax() {
        return returnAmountTax;
    }

    public void setReturnAmountTax(BigDecimal returnAmountTax) {
        this.returnAmountTax = returnAmountTax;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getQuotationNumber() {
        return quotationNumber;
    }

    public void setQuotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public String getQuoationItem() {
        return quoationItem;
    }

    public void setQuoationItem(String quoationItem) {
        this.quoationItem = quoationItem;
    }

    public BigInteger getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(BigInteger returnRate) {
        this.returnRate = returnRate;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getShippingConditions() {
        return shippingConditions;
    }

    public void setShippingConditions(String shippingConditions) {
        this.shippingConditions = shippingConditions;
    }

    public String getVsbed() {
        return vsbed;
    }

    public void setVsbed(String vsbed) {
        this.vsbed = vsbed;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public Date getInsertTimestamp() {
        return insertTimestamp;
    }

    public void setInsertTimestamp(Date insertTimestamp) {
        this.insertTimestamp = insertTimestamp;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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
        if (!(object instanceof SkSalesDetails)) {
            return false;
        }
        SkSalesDetails other = (SkSalesDetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ar.SkSalesDetails[ id=" + id + " ]";
    }
    
}
