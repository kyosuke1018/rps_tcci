/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.criteria;

/**
 *
 * @author Peter.pan
 */
public class CustomerCriteriaVO extends BaseCriteriaVO {
    private Long cusLevel;
    private Long customerId;
    // 累積消費
    private Double cumulativeS;
    private Double cumulativeE;
    
    private String creditStatus;// 信用額度狀態
    
    public Long getCusLevel() {
        return cusLevel;
    }

    public void setCusLevel(Long cusLevel) {
        this.cusLevel = cusLevel;
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Double getCumulativeS() {
        return cumulativeS;
    }

    public void setCumulativeS(Double cumulativeS) {
        this.cumulativeS = cumulativeS;
    }

    public Double getCumulativeE() {
        return cumulativeE;
    }

    public void setCumulativeE(Double cumulativeE) {
        this.cumulativeE = cumulativeE;
    }
}
