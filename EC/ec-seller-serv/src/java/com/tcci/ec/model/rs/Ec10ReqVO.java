/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.rs;

import java.math.BigDecimal;

/**
 *
 * @author Peter.pan
 */
public class Ec10ReqVO {
    private String loginAccount;// 登入帳號
    private String token;// 認證 Token
    private Long srcOrderId;// EC 1.5 訂單ID:EC_USER.EC_ORDER.ID

    private Long customerId;// FK:EC_CUSTOMER.ID
    private String customerCode;// 客戶代碼
    
    private Long productId; // FK:EC_PRODUCT.ID
    private String productCode; // 料號
    private BigDecimal quantity; // 數量
    private String vehicle; // 車號
    private String deliveryDate; // 提貨日
    private String method; // 廠交自提/工地交自提
    private Long contractId; // FK:EC_CONTRACT.ID
    private String contractCode; // 合約號碼
    private Long plantId; // FK:EC_PLANT.ID
    private String plantCode; // 廠代碼
    private Long salesareaId; // FK:EC_SALESAREA.ID
    private String salesareaCode; // 銷售區代碼
    private Long deliveryId; // FK:EC_DELIVERY_PLACE.ID
    private String deliveryCode; // 送達地點代碼
    private Long salesId; // FK:EC_SALES.ID
    private String salesCode; // 業務員工號
    private Integer posnr; // 合約項次

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public Long getSrcOrderId() {
        return srcOrderId;
    }

    public void setSrcOrderId(Long srcOrderId) {
        this.srcOrderId = srcOrderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public Long getSalesareaId() {
        return salesareaId;
    }

    public void setSalesareaId(Long salesareaId) {
        this.salesareaId = salesareaId;
    }

    public String getSalesareaCode() {
        return salesareaCode;
    }

    public void setSalesareaCode(String salesareaCode) {
        this.salesareaCode = salesareaCode;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public Long getSalesId() {
        return salesId;
    }

    public void setSalesId(Long salesId) {
        this.salesId = salesId;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public Integer getPosnr() {
        return posnr;
    }

    public void setPosnr(Integer posnr) {
        this.posnr = posnr;
    }
    
    
}
