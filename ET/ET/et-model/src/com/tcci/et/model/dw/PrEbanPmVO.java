/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.dw;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class PrEbanPmVO {
    private String mandt;// 用戶端
    private String banfn;// 請購單號碼
    private Integer bnfpo;// 請購單的項目號碼
    private Integer extrow;// 服務行號
    private String ktext1;// 說明

    private BigDecimal menge;// 數量
    private String meins;// 基礎計量單位
    private BigDecimal tbtwr;// 單價
    private String waers;// 幣別碼
    private BigDecimal netwr;// 總價
    private Date syncTimeStamp;

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public Integer getExtrow() {
        return extrow;
    }

    public void setExtrow(Integer extrow) {
        this.extrow = extrow;
    }

    public String getBanfn() {
        return banfn;
    }

    public void setBanfn(String banfn) {
        this.banfn = banfn;
    }

    public Integer getBnfpo() {
        return bnfpo;
    }

    public void setBnfpo(Integer bnfpo) {
        this.bnfpo = bnfpo;
    }

    public String getKtext1() {
        return ktext1;
    }

    public void setKtext1(String ktext1) {
        this.ktext1 = ktext1;
    }

    public BigDecimal getMenge() {
        return menge;
    }

    public void setMenge(BigDecimal menge) {
        this.menge = menge;
    }

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public BigDecimal getTbtwr() {
        return tbtwr;
    }

    public void setTbtwr(BigDecimal tbtwr) {
        this.tbtwr = tbtwr;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }

    public BigDecimal getNetwr() {
        return netwr;
    }

    public void setNetwr(BigDecimal netwr) {
        this.netwr = netwr;
    }

    public Date getSyncTimeStamp() {
        return syncTimeStamp;
    }

    public void setSyncTimeStamp(Date syncTimeStamp) {
        this.syncTimeStamp = syncTimeStamp;
    }
    
}
