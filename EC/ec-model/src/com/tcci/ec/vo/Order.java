/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.vo;

import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.PayStatusEnum;
//import com.tcci.ec.enums.PaymentTypeEnum;
import com.tcci.ec.enums.ShipStatusEnum;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Kyle.Cheng
 */
public class Order {
    private Long id;
    private String orderNumber;//必填
    private Member member;
    private Store store;//store id必填
    private List<OrderDetail> orderDetails;//必填, product id必填
    private BigDecimal total;//訂單總金額 ex:小計+運費-折扣
    private BigDecimal subTotal;//小計
    private BigDecimal shippingTotal;//運費
//    private PaymentTypeEnum paymentType;Ｘ
    private OrderStatusEnum status;
    private PayStatusEnum payStatus;
    private ShipStatusEnum shipStatus;
    private String message;
    private Date createtime;
    private List<OrderMessage> orderMessages;
    private Shipping shipping;//出貨方式
    private OrderShipInfo shipInfo;//出貨資訊
    private Payment payment;//付款方式
    private String currCode;//幣別
    private OrderRate orderRate;
    private String deliveryDate;//交期 yyyyMMdd
    private BigDecimal oriTotal;//原始訂單金額
    private Boolean buyerCheck;//買方確認
    private Long deliveryId;//送達地點 EC_DELIVERY_PLACE.ID
    private Long salesareaId;//銷售區域 EC_SALESAREA.ID
    
    private String orderStatusDisplayName;
    private String payStatusDisplayName;
    private String shipStatusDisplayName;
    
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }


    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
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

    public List<OrderMessage> getOrderMessages() {
        return orderMessages;
    }

    public void setOrderMessages(List<OrderMessage> orderMessages) {
        this.orderMessages = orderMessages;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public OrderShipInfo getShipInfo() {
        return shipInfo;
    }

    public void setShipInfo(OrderShipInfo shipInfo) {
        this.shipInfo = shipInfo;
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getCurrCode() {
        return currCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    public OrderRate getOrderRate() {
        return orderRate;
    }

    public void setOrderRate(OrderRate orderRate) {
        this.orderRate = orderRate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrderStatusDisplayName() {
        return orderStatusDisplayName;
    }

    public void setOrderStatusDisplayName(String orderStatusDisplayName) {
        this.orderStatusDisplayName = orderStatusDisplayName;
    }

    public String getPayStatusDisplayName() {
        return payStatusDisplayName;
    }

    public void setPayStatusDisplayName(String payStatusDisplayName) {
        this.payStatusDisplayName = payStatusDisplayName;
    }

    public String getShipStatusDisplayName() {
        return shipStatusDisplayName;
    }

    public void setShipStatusDisplayName(String shipStatusDisplayName) {
        this.shipStatusDisplayName = shipStatusDisplayName;
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

}
