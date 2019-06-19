/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.PayStatusEnum;
import com.tcci.ec.enums.PaymentTypeEnum;
import com.tcci.ec.enums.ShipStatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_ORDER")
@NamedQueries({
    @NamedQuery(name = "EcOrder.findAll", query = "SELECT e FROM EcOrder e")
//    @NamedQuery(name = "EcCustomer.findAll", query = "SELECT e FROM EcCustomer e ORDER by e.code"),
//    @NamedQuery(name = "EcCustomer.findByCode", query = "SELECT e FROM EcCustomer e WHERE e.code=:code")
})
public class EcOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_ORDER", sequenceName = "SEQ_ORDER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER")
    private Long id;
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
//    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
//    @ManyToOne
//    private EcCustomer customer;
//    @JoinColumn(name = "SELLER_ID", referencedColumnName = "ID")
//    @ManyToOne
//    private EcSeller seller;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID")
    @ManyToOne
    private EcMember member;
    @JoinColumn(name = "STORE_ID", referencedColumnName = "ID")
    @ManyToOne
    private EcStore store;
    @JoinColumn(name = "PAYMENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private EcPayment payment;
    @Column(name = "PAYMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private PaymentTypeEnum paymentType;
    @Column(name = "TOTAL")
    private BigDecimal total;
    @Column(name = "SUB_TOTAL")
    private BigDecimal subTotal;
    @Column(name = "SHIPPING_TOTAL")
    private BigDecimal shippingTotal;
    @Column(name = "DISCOUNT_TOTAL")
    private BigDecimal discountTotal;
//    @JoinColumn(name = "CURRENCY_ID", referencedColumnName = "ID")
//    @ManyToOne
//    private EcCurrency currency;
    @Column(name = "CURRENCY_ID")
    private Long currencyId;
//    @JoinColumn(name = "SHIPPING_ID", referencedColumnName = "ID")
//    @ManyToOne
//    private EcShipping shipping;
    
//    @JoinColumn(name = "DISCOUNT_ID", referencedColumnName = "ID")
//    @ManyToOne
//    private EcDiscount discount;
//    @JoinColumn(name = "COUPON_ID", referencedColumnName = "ID")
//    @ManyToOne
//    private EcCoupon coupon;
    @Column(name = "BONUS")
    private BigDecimal bonus;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;
//    private String status;
//    @Column(name = "RECIPIENT")
//    private String recipient;
//    @Column(name = "ADDRESS")
//    private String address;
//    @Column(name = "PHONE")
//    private String phone;
    @Column(name = "MESSAGE")
    private String message;
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
    @Column(name = "PAY_STATUS")
    @Enumerated(EnumType.STRING)
    private PayStatusEnum payStatus;
    @Column(name = "SHIP_STATUS")
    @Enumerated(EnumType.STRING)
    private ShipStatusEnum shipStatus;
    @Column(name = "APPROVETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvetime;
    @Column(name = "SHIPPING_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date shippingTime;
    @Column(name = "DELIVERY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;
    @Column(name = "ORI_TOTAL")
    private BigDecimal oriTotal;
    @Column(name = "BUYER_CHECK")
    private Boolean buyerCheck;//買方確認
    @Column(name = "DELIVERY_ID")
    private Long deliveryId;//送達地點 EC_DELIVERY_PLACE.ID
    @Column(name = "SALESAREA_ID")
    private Long salesareaId;//銷售區域 EC_SALESAREA.ID
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<EcOrderDetail> orderDetails;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<EcOrderMessage> orderMessages;

    public EcOrder() {
    }

    public EcOrder(Long id) {
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

    public EcMember getMember() {
        return member;
    }

    public void setMember(EcMember member) {
        this.member = member;
    }

    public EcStore getStore() {
        return store;
    }

    public void setStore(EcStore store) {
        this.store = store;
    }
    
    public EcPayment getPayment() {
        return payment;
    }

    public void setPayment(EcPayment payment) {
        this.payment = payment;
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

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
        this.modifier = modifier;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public List<EcOrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<EcOrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public PayStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }

    public ShipStatusEnum getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(ShipStatusEnum shipStatus) {
        this.shipStatus = shipStatus;
    }

    public PaymentTypeEnum getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeEnum paymentType) {
        this.paymentType = paymentType;
    }

    public List<EcOrderMessage> getOrderMessages() {
        return orderMessages;
    }

    public void setOrderMessages(List<EcOrderMessage> orderMessages) {
        this.orderMessages = orderMessages;
    }

    public Date getApprovetime() {
        return approvetime;
    }

    public void setApprovetime(Date approvetime) {
        this.approvetime = approvetime;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Date getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(Date shippingTime) {
        this.shippingTime = shippingTime;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public BigDecimal getOriTotal() {
        return oriTotal;
    }

    public void setOriTotal(BigDecimal oriTotal) {
        this.oriTotal = oriTotal;
    }

    public Boolean getBuyerCheck() {
        return buyerCheck;
    }

    public void setBuyerCheck(Boolean buyerCheck) {
        this.buyerCheck = buyerCheck;
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
