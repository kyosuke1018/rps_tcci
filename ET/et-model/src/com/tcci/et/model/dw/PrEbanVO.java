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
public class PrEbanVO {
    private String mandt;// 用戶端
    private String banfn;// 請購單號碼
    private Integer bnfpo;// 請購單的項目號碼
    private String bsart;// 請購文件類型
    private String loekz;// 採購檔的刪除指示碼(請購單)
    private String statu;// 請購處理狀態
    private String frgkz;// 核發指示碼
    private String frgzu;// 核發狀態
    private String frgst;// 請購中的核發策略
    private String ekgrp;// 採購群組
    private String ernam;// 建立物件者的姓名
    private Date erdat;// 最後更改日期
    private String afnam;// 申請人的姓名
    private String txz01;// 短文
    private String matnr;// 物料號碼
    private String werks;// 工廠
    private String lgort;// 儲存地點
    private String bednr;// 需求追蹤號碼
    private String matkl;// 物料群組
    private BigDecimal menge;// 請購單數量
    private String meins;// 請購計量單位
    private Date badat;// 請購單（請求）日期
    private Date lfdat;// 項目交貨日期
    private BigDecimal preis;// 請購單中的價格
    private BigDecimal peinh;// 價格單位
    private String pstyp;// 採購文件中的項目種類
    private String knttp;// 科目指派種類
    private String ekorg;// 採購組織
    private String ebeln;// 採購單號碼
    private Integer ebelp;// 採購單項目號碼
    private String frggr;// 核發群組
    private BigDecimal rlwrt;// 核發時的總值
    private String waers;// 幣別碼
    private Date syncTimeStamp;

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
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

    public String getBsart() {
        return bsart;
    }

    public void setBsart(String bsart) {
        this.bsart = bsart;
    }

    public String getLoekz() {
        return loekz;
    }

    public void setLoekz(String loekz) {
        this.loekz = loekz;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public String getFrgkz() {
        return frgkz;
    }

    public void setFrgkz(String frgkz) {
        this.frgkz = frgkz;
    }

    public String getFrgzu() {
        return frgzu;
    }

    public void setFrgzu(String frgzu) {
        this.frgzu = frgzu;
    }

    public String getFrgst() {
        return frgst;
    }

    public void setFrgst(String frgst) {
        this.frgst = frgst;
    }

    public String getEkgrp() {
        return ekgrp;
    }

    public void setEkgrp(String ekgrp) {
        this.ekgrp = ekgrp;
    }

    public String getErnam() {
        return ernam;
    }

    public void setErnam(String ernam) {
        this.ernam = ernam;
    }

    public Date getErdat() {
        return erdat;
    }

    public void setErdat(Date erdat) {
        this.erdat = erdat;
    }

    public String getAfnam() {
        return afnam;
    }

    public void setAfnam(String afnam) {
        this.afnam = afnam;
    }

    public String getTxz01() {
        return txz01;
    }

    public void setTxz01(String txz01) {
        this.txz01 = txz01;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public String getLgort() {
        return lgort;
    }

    public void setLgort(String lgort) {
        this.lgort = lgort;
    }

    public String getBednr() {
        return bednr;
    }

    public void setBednr(String bednr) {
        this.bednr = bednr;
    }

    public String getMatkl() {
        return matkl;
    }

    public void setMatkl(String matkl) {
        this.matkl = matkl;
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

    public Date getBadat() {
        return badat;
    }

    public void setBadat(Date badat) {
        this.badat = badat;
    }

    public Date getLfdat() {
        return lfdat;
    }

    public void setLfdat(Date lfdat) {
        this.lfdat = lfdat;
    }

    public BigDecimal getPreis() {
        return preis;
    }

    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    public BigDecimal getPeinh() {
        return peinh;
    }

    public void setPeinh(BigDecimal peinh) {
        this.peinh = peinh;
    }

    public String getPstyp() {
        return pstyp;
    }

    public void setPstyp(String pstyp) {
        this.pstyp = pstyp;
    }

    public String getKnttp() {
        return knttp;
    }

    public void setKnttp(String knttp) {
        this.knttp = knttp;
    }

    public String getEkorg() {
        return ekorg;
    }

    public void setEkorg(String ekorg) {
        this.ekorg = ekorg;
    }

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
    }

    public Integer getEbelp() {
        return ebelp;
    }

    public void setEbelp(Integer ebelp) {
        this.ebelp = ebelp;
    }

    public String getFrggr() {
        return frggr;
    }

    public void setFrggr(String frggr) {
        this.frggr = frggr;
    }

    public BigDecimal getRlwrt() {
        return rlwrt;
    }

    public void setRlwrt(BigDecimal rlwrt) {
        this.rlwrt = rlwrt;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }

    public Date getSyncTimeStamp() {
        return syncTimeStamp;
    }

    public void setSyncTimeStamp(Date syncTimeStamp) {
        this.syncTimeStamp = syncTimeStamp;
    }

    
}
