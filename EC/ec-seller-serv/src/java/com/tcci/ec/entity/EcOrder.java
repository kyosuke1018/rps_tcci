/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_ORDER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcOrder.findAll", query = "SELECT e FROM EcOrder e")
    , @NamedQuery(name = "EcOrder.findById", query = "SELECT e FROM EcOrder e WHERE e.id = :id")})
public class EcOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_ORDER", sequenceName = "SEQ_ORDER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER")        
    private Long id;
    @Basic(optional = false)
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
    @Column(name = "STORE_ID")
    private Long storeId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "TOTAL")
    private BigDecimal total;
    @Basic(optional = false)
    @Column(name = "SUB_TOTAL")
    private BigDecimal subTotal;
    @Basic(optional = false)
    @Column(name = "SHIPPING_TOTAL")
    private BigDecimal shippingTotal;
    @Basic(optional = false)
    @Column(name = "DISCOUNT_TOTAL")
    private BigDecimal discountTotal;
    //@Column(name = "SHIPPING_DATE")
    //private String shippingDate;
    @Column(name = "BONUS")
    private Integer bonus;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "MESSAGE")
    private String message;
    //@Column(name = "RECIPIENT")
    //private String recipient;
    //@Column(name = "ADDRESS")
    //private String address;
    //@Column(name = "PHONE")
    //private String phone;

    @Column(name = "PAY_STATUS")
    private String payStatus;
    @Column(name = "SHIP_STATUS")
    private String shipStatus;
    @Column(name = "PAYMENT_TYPE")
    private String paymentType;
    @Column(name = "APPROVETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvetime;
    @Column(name = "SHIPPING_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date shippingTime;
   
    @Column(name = "COUPON_ID")
    private Long couponId;
    @Column(name = "CURRENCY_ID")
    private Long currencyId;
    @Column(name = "MEMBER_ID")
    private Long memberId;
    @Column(name = "DISCOUNT_ID")
    private Long discountId;
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Column(name = "DELIVERY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;// 交貨日期

    @Column(name = "ORI_TOTAL")
    private BigDecimal oriTotal;
    @Column(name = "BUYER_CHECK")
    private Boolean buyerCheck;

    @Column(name = "DELIVERY_ID")
    private Long deliveryId;
    @Column(name = "SALESAREA_ID")
    private Long salesareaId;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EcMember modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EcOrder() {
    }

    public EcOrder(Long id) {
        this.id = id;
    }

    public EcOrder(Long id, Long storeId, String orderNumber, BigDecimal total, BigDecimal subTotal, BigDecimal shippingTotal, BigDecimal discountTotal, String status) {
        this.id = id;
        this.storeId = storeId;
        this.orderNumber = orderNumber;
        this.total = total;
        this.subTotal = subTotal;
        this.shippingTotal = shippingTotal;
        this.discountTotal = discountTotal;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Long getSalesareaId() {
        return salesareaId;
    }

    public void setSalesareaId(Long salesareaId) {
        this.salesareaId = salesareaId;
    }

    public Boolean getBuyerCheck() {
        return buyerCheck;
    }

    public void setBuyerCheck(Boolean buyerCheck) {
        this.buyerCheck = buyerCheck;
    }

    public BigDecimal getOriTotal() {
        return oriTotal;
    }

    public void setOriTotal(BigDecimal oriTotal) {
        this.oriTotal = oriTotal;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(Date shippingTime) {
        this.shippingTime = shippingTime;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(String shipStatus) {
        this.shipStatus = shipStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Date getApprovetime() {
        return approvetime;
    }

    public void setApprovetime(Date approvetime) {
        this.approvetime = approvetime;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getShippingTotal() {
        return shippingTotal;
    }

    public void setShippingTotal(BigDecimal shippingTotal) {
        this.shippingTotal = shippingTotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
        this.modifier = modifier;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
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
        if (!(object instanceof EcOrder)) {
            return false;
        }
        EcOrder other = (EcOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcOrder[ id=" + id + " ]";
    }
    
}
