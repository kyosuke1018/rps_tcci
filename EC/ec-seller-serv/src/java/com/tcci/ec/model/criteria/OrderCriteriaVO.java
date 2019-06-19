/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.criteria;

import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class OrderCriteriaVO extends BaseCriteriaVO {
    private String cusKeyword;
    private Boolean closed;
    private Boolean buyerCheck;
    private Date shipStartAt;
    private Date shipEndAt;

    // EC1.0
    private String productCode;
    private Long customerId;
    private Long contractId;
    private Long salesareaId;
    private String province;
    private String city;
    private String district;
    private String town;
    private Long plantId;
    private Long tccOrderId;  
    private String shipMethod;
    private Long deliveryPlaceId;
    private Boolean tranToEC10; // 是否已轉單
    private Boolean forCombine; // for 併單
    private String combineKeys;// for 併單 

    public Boolean getForCombine() {
        return forCombine;
    }

    public void setForCombine(Boolean forCombine) {
        this.forCombine = forCombine;
    }
    
    public Boolean getTranToEC10() {
        return tranToEC10;
    }

    public void setTranToEC10(Boolean tranToEC10) {
        this.tranToEC10 = tranToEC10;
    }

    public Long getDeliveryPlaceId() {
        return deliveryPlaceId;
    }

    public void setDeliveryPlaceId(Long deliveryPlaceId) {
        this.deliveryPlaceId = deliveryPlaceId;
    }

    public String getCombineKeys() {
        return combineKeys;
    }

    public void setCombineKeys(String combineKeys) {
        this.combineKeys = combineKeys;
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
    }

    public Date getShipStartAt() {
        return shipStartAt;
    }

    public void setShipStartAt(Date shipStartAt) {
        this.shipStartAt = shipStartAt;
    }

    public Date getShipEndAt() {
        return shipEndAt;
    }

    public void setShipEndAt(Date shipEndAt) {
        this.shipEndAt = shipEndAt;
    }

    public String getShipMethod() {
        return shipMethod;
    }

    public void setShipMethod(String shipMethod) {
        this.shipMethod = shipMethod;
    }

    public Long getTccOrderId() {
        return tccOrderId;
    }

    public void setTccOrderId(Long tccOrderId) {
        this.tccOrderId = tccOrderId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getSalesareaId() {
        return salesareaId;
    }

    public void setSalesareaId(Long salesareaId) {
        this.salesareaId = salesareaId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
    
    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Boolean getBuyerCheck() {
        return buyerCheck;
    }

    public void setBuyerCheck(Boolean buyerCheck) {
        this.buyerCheck = buyerCheck;
    }

    public String getCusKeyword() {
        return cusKeyword;
    }

    public void setCusKeyword(String cusKeyword) {
        this.cusKeyword = cusKeyword;
    }

}
