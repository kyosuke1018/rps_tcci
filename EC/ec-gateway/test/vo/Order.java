/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vo;

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
    private PaymentTypeEnum paymentType;
    private OrderStatusEnum status;
    private PayStatusEnum payStatus;
    private ShipStatusEnum shipStatus;
    private String message;
//    private String recipient;//必填
//    private String address;//必填
//    private String phone;//必填
    private Date createtime;
    private List<OrderMessage> orderMessages;
    private Shipping shipping;//出貨方式
    private OrderShipInfo shipInfo;//出貨資訊

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

    public PaymentTypeEnum getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeEnum paymentType) {
        this.paymentType = paymentType;
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
}

