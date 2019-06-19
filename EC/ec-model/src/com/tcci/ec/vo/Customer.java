/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.vo;

import java.math.BigDecimal;

/**
 *
 * @author Kyle.Cheng
 */
public class Customer {
    private Long id;
    private Long memberId;
    private Long storeId;
    private String storeName;
    private BigDecimal credits;//信用額度
    private BigDecimal expectedCredits;// 期望信用額度
    private String applyTime; // 申請信用額度時間 yyyy/MM/dd HH:mm:ss

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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public BigDecimal getExpectedCredits() {
        return expectedCredits;
    }

    public void setExpectedCredits(BigDecimal expectedCredits) {
        this.expectedCredits = expectedCredits;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

}
