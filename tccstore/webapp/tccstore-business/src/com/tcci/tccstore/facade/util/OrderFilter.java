package com.tcci.tccstore.facade.util;

import com.tcci.tccstore.enums.OrderStatusEnum;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Neo.Fu
 */
public class OrderFilter {

    private String customerCode;
    private String yearMonth;
    private Long id;
    private String sapOrdernum;
    private Date createtimeBegin;
    private Date createtimeEnd;
    private OrderStatusEnum status;
    private List<OrderStatusEnum> statusList;
    private String plant;
    private List<String> plantList;
    private boolean excludeC1;
    private String deliveryDateBegin;
    private String deliveryDateEnd;
    private String vehicle;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

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

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public List<String> getPlantList() {
        return plantList;
    }

    public void setPlantList(List<String> plantList) {
        this.plantList = plantList;
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

    public String getSapOrdernum() {
        return sapOrdernum;
    }

    public void setSapOrdernum(String sapOrdernum) {
        this.sapOrdernum = sapOrdernum;
    }

    public boolean isExcludeC1() {
        return excludeC1;
    }

    public void setExcludeC1(boolean excludeC1) {
        this.excludeC1 = excludeC1;
    }

    public String getDeliveryDateBegin() {
        return deliveryDateBegin;
    }

    public void setDeliveryDateBegin(String deliveryDateBegin) {
        this.deliveryDateBegin = deliveryDateBegin;
    }

    public String getDeliveryDateEnd() {
        return deliveryDateEnd;
    }

    public void setDeliveryDateEnd(String deliveryDateEnd) {
        this.deliveryDateEnd = deliveryDateEnd;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
    
}
