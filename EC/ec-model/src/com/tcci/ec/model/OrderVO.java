/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.cm.annotation.InputCheckMeta;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.RfqStatusEnum;
import com.tcci.ec.model.rs.BaseResponseVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class OrderVO extends BaseResponseVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long storeId;

    @InputCheckMeta(key="EC_ORDER.ORDER_NUMBER")
    private String orderNumber;
    private BigDecimal total;
    private BigDecimal subTotal;
    private BigDecimal shippingTotal;
    private BigDecimal discountTotal;
    @InputCheckMeta(key="EC_ORDER.SHIPPING_DATE")
    private String shippingDate;
    private Integer bonus;
    @InputCheckMeta(key="EC_ORDER.STATUS")
    private String status;
    private String statusLabel;
    @InputCheckMeta(key="EC_ORDER.MESSAGE")
    private String message;
    
    @InputCheckMeta(key="EC_ORDER.PAY_STATUS")
    private String payStatus;
    @InputCheckMeta(key="EC_ORDER.SHIP_STATUS")
    private String shipStatus;
    @InputCheckMeta(key="EC_ORDER.PAYMENT_TYPE")
    private String paymentType;
    private Date approvetime;

    private Date createtime;
    private Date modifytime;
    private Long creatorId;
    private Long modifierId;
    
    // EC_ORDER_SHIP_INFO
    @InputCheckMeta(key="EC_ORDER_SHIP_INFO.RECIPIENT")
    private String recipient;
    @InputCheckMeta(key="EC_ORDER_SHIP_INFO.ADDRESS")
    private String address;
    @InputCheckMeta(key="EC_ORDER_SHIP_INFO.PHONE")
    private String phone;
    @InputCheckMeta(key="EC_ORDER_SHIP_INFO.CAR_NO")
    private String carNo;
    @InputCheckMeta(key="EC_ORDER_SHIP_INFO.DRIVER")
    private String driver;
    
    // customer
    private String cname;
    private String ename;
    private String email1;
    private String email2;
    private String tel1;
    private String tel2;
    //
    private String currency;// 幣別
    private String curName;
    private String shipping;// 運送方式
    private String payment;// 付款方式
    //
    private Integer itemCount;// 品項筆數
    
    private List<OrderDetailVO> items;
    private List<OrderProcessVO> records;
    private List<OrderMessageVO> messages;
    
    public OrderVO() {
    }

    public OrderVO(Long id) {
        this.id = id;
    }

    public OrderVO(Long id, String orderNumber, BigDecimal total, BigDecimal subTotal, BigDecimal shippingTotal, BigDecimal discountTotal, String status) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.total = total;
        this.subTotal = subTotal;
        this.shippingTotal = shippingTotal;
        this.discountTotal = discountTotal;
        this.status = status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public List<OrderMessageVO> getMessages() {
        return messages;
    }

    public void setMessages(List<OrderMessageVO> messages) {
        this.messages = messages;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderDetailVO> getItems() {
        return items;
    }

    public void setItems(List<OrderDetailVO> items) {
        this.items = items;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public List<OrderProcessVO> getRecords() {
        return records;
    }

    public void setRecords(List<OrderProcessVO> records) {
        this.records = records;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
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

    public String getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
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
        if( status!=null ){
            OrderStatusEnum enumPo = OrderStatusEnum.getFromCode(status);
            this.setStatusLabel(enumPo!=null?enumPo.getDisplayName():"");
            if( this.getStatusLabel().isEmpty() ){
                RfqStatusEnum enumRfq = RfqStatusEnum.getFromCode(status);
                this.setStatusLabel(enumRfq!=null?enumRfq.getDisplayName():"");
            }
        }else{
            this.setStatusLabel("");
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
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

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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
        if (!(object instanceof OrderVO)) {
            return false;
        }
        OrderVO other = (OrderVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.OrderVO[ id=" + id + " ]";
    }
    
}
