/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.e10;

import java.math.BigDecimal;

/**
 *
 * @author Peter.pan
 */
public class ContractProductVO {
    private Long contractId;
    private long productId; // FK:EC_PRODUCT.ID
    private long plantId; // FK:EC_PLANT.ID
    private long salesareaId;
    private BigDecimal unitPrice;
    private String method;
    private Integer posnr;

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getPlantId() {
        return plantId;
    }

    public void setPlantId(long plantId) {
        this.plantId = plantId;
    }

    public long getSalesareaId() {
        return salesareaId;
    }

    public void setSalesareaId(long salesareaId) {
        this.salesareaId = salesareaId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getPosnr() {
        return posnr;
    }

    public void setPosnr(Integer posnr) {
        this.posnr = posnr;
    }
    
}
