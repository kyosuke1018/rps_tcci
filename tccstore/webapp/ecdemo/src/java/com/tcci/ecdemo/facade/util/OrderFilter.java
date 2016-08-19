package com.tcci.ecdemo.facade.util;

import com.tcci.ecdemo.entity.OrderStatusEnum;

/**
 *
 * @author Neo.Fu
 */
public class OrderFilter {
    private String customerCode;
    private OrderStatusEnum status;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }
}
