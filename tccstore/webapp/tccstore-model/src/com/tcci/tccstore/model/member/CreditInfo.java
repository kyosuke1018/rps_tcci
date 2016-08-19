/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.model.member;

import java.math.BigDecimal;

/**
 *
 * @author Jimmy.Lee
 */
public class CreditInfo {
    private String compCode; // 公司代碼: 2000, ...
    private String compName; // 公司名稱: 英德, ...
    private BigDecimal amount; // 預收款餘額
    private String currency; // 幣別
    private String datum; // 計算日期
    private String uzeit; // 計算時間
    private String notice = "非即时余额";

    public CreditInfo() {
    }
    
    public CreditInfo(String compCode, String compName, BigDecimal amount, String currency) {
        this.compCode = compCode;
        this.compName = compName;
        this.amount = amount;
        this.currency = currency;
        notice = "即时余额";
    }
    
    public CreditInfo(String compCode, String compName, BigDecimal amount, String currency, String datum, String uzeit) {
        this.compCode = compCode;
        this.compName = compName;
        this.amount = amount;
        this.currency = currency;
        this.datum = datum;
        this.uzeit = uzeit;
    }
    
    // getter, setter
    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getUzeit() {
        return uzeit;
    }

    public void setUzeit(String uzeit) {
        this.uzeit = uzeit;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

}
