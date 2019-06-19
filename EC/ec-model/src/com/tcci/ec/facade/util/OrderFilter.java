package com.tcci.ec.facade.util;

import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.PayStatusEnum;
import com.tcci.ec.enums.ShipStatusEnum;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Neo.Fu
 */
public class OrderFilter {

    private Long id;
    private Long memberId;//買方
    private Long storeId;//賣場
    private String orderNumber;
    private Date createtimeBegin;
    private Date createtimeEnd;
    private String createtimeBeginStr;//yyyyMMdd
    private String createtimeEndStr;//yyyyMMdd
    private OrderStatusEnum status;
    private List<OrderStatusEnum> statusList;
    private PayStatusEnum payStatus;
    private List<PayStatusEnum> payStatusList;
    private ShipStatusEnum shipStatus;
    private List<ShipStatusEnum> shipStatusList;
    private String carNo;//20190408 車號查詢
    private Long productId;//20190423 產品查詢

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public List<OrderStatusEnum> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<OrderStatusEnum> statusList) {
        this.statusList = statusList;
    }

    public Date getCreatetimeBegin() {
        return createtimeBegin;
    }

    public void setCreatetimeBegin(Date createtimeBegin) {
        this.createtimeBegin = createtimeBegin;
    }

    public Date getCreatetimeEnd() {
        return createtimeEnd;
    }

    public void setCreatetimeEnd(Date createtimeEnd) {
        this.createtimeEnd = createtimeEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public PayStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }

    public List<PayStatusEnum> getPayStatusList() {
        return payStatusList;
    }

    public void setPayStatusList(List<PayStatusEnum> payStatusList) {
        this.payStatusList = payStatusList;
    }

    public ShipStatusEnum getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(ShipStatusEnum shipStatus) {
        this.shipStatus = shipStatus;
    }

    public List<ShipStatusEnum> getShipStatusList() {
        return shipStatusList;
    }

    public void setShipStatusList(List<ShipStatusEnum> shipStatusList) {
        this.shipStatusList = shipStatusList;
    }

    public String getCreatetimeBeginStr() {
        return createtimeBeginStr;
    }

    public void setCreatetimeBeginStr(String createtimeBeginStr) {
        this.createtimeBeginStr = createtimeBeginStr;
    }

    public String getCreatetimeEndStr() {
        return createtimeEndStr;
    }

    public void setCreatetimeEndStr(String createtimeEndStr) {
        this.createtimeEndStr = createtimeEndStr;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
}
