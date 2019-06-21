/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rfq;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ET_RFQ_EKKO
 * @author Peter.pan
 */
public class RfqEkkoVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id; // PK ID
    private String rfqNo; // WEB詢價文件號碼
    private Long tenderId; // 標案 ID
    private String mandt; // 用戶端
    private String ebeln; // SAP詢價文件號碼
    private String bukrs; // 公司代碼
    private String bstyp; // 詢價文件種類
    private String bsart; // 詢價文件類型
    private Date aedat; // 記錄建立日期
    private String ernam; // 建立物件者的姓名
    private String lifnr; // 供應商帳戶號碼
    private String spras; // 語言代碼
    private String zterm; // 付款條件碼
    private BigDecimal zbd1t; // 現金（即時付款）折扣日數
    private BigDecimal zbd2t; // 現金（即時付款）折扣日數
    private BigDecimal zbd3t; // 現金（即時付款）折扣日數
    private BigDecimal zbd1p; // 現金折扣百分比 1
    private BigDecimal zbd2p; // 現金折扣百分比 2
    private String ekorg; // 採購組織
    private String ekgrp; // 採購群組
    private String waers; // 幣別碼
    private BigDecimal wkurs; // 匯率
    private String kufix; // 指示碼：固定匯率
    
    private Date bedat; // 採購文件日期
    
    private String ihrez; // 客戶或供應商的內部參考
    private String verkf; // 對供應商辦公室負責的銷售員
    private String telf1; // 供應商電話號碼
    private String inco1; // 國貿條件 (第一部份)
    private String inco2; // 國貿條件（第 2 部分）
    private String submi; // 彙總號碼
    private String unsez; // 我方參考
    private String frggr; // 核發群組
    private String frgsx; // 核發策略
    private String frgke; // 核發指示碼：採購文件
    private String frgzu; // 核發狀態
    private BigDecimal rlwrt; // 核發時的總值
    private Date udate; // 採購核發日期
    private String name1; // 名稱 1
    private String strSuppl1; // 街道 2
    private String telNumber; // 第一個電話號碼：撥號代碼 + 號碼
    private String faxNumber; // 第一個傳真號碼 : 撥區域號碼 +電話號碼
    private String adrnr; // 地址號碼
    
    private Date angdt; // 投標/報價提交的截止日期
    
    private Long creatorId; // 建立人
    private Date createtime; // 建立時間
    private Long modifierId; // 修改人
    private Date modifytime; // 修改時間

    public RfqEkkoVO() {
    }

    public RfqEkkoVO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRfqNo() {
        return rfqNo;
    }

    public void setRfqNo(String rfqNo) {
        this.rfqNo = rfqNo;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    public String getBstyp() {
        return bstyp;
    }

    public void setBstyp(String bstyp) {
        this.bstyp = bstyp;
    }

    public String getBsart() {
        return bsart;
    }

    public void setBsart(String bsart) {
        this.bsart = bsart;
    }

    public Date getAedat() {
        return aedat;
    }

    public void setAedat(Date aedat) {
        this.aedat = aedat;
    }

    public String getErnam() {
        return ernam;
    }

    public void setErnam(String ernam) {
        this.ernam = ernam;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public String getSpras() {
        return spras;
    }

    public void setSpras(String spras) {
        this.spras = spras;
    }

    public String getZterm() {
        return zterm;
    }

    public void setZterm(String zterm) {
        this.zterm = zterm;
    }

    public BigDecimal getZbd1t() {
        return zbd1t;
    }

    public void setZbd1t(BigDecimal zbd1t) {
        this.zbd1t = zbd1t;
    }

    public BigDecimal getZbd2t() {
        return zbd2t;
    }

    public void setZbd2t(BigDecimal zbd2t) {
        this.zbd2t = zbd2t;
    }

    public BigDecimal getZbd3t() {
        return zbd3t;
    }

    public void setZbd3t(BigDecimal zbd3t) {
        this.zbd3t = zbd3t;
    }

    public BigDecimal getZbd1p() {
        return zbd1p;
    }

    public void setZbd1p(BigDecimal zbd1p) {
        this.zbd1p = zbd1p;
    }

    public BigDecimal getZbd2p() {
        return zbd2p;
    }

    public void setZbd2p(BigDecimal zbd2p) {
        this.zbd2p = zbd2p;
    }

    public String getEkorg() {
        return ekorg;
    }

    public void setEkorg(String ekorg) {
        this.ekorg = ekorg;
    }

    public String getEkgrp() {
        return ekgrp;
    }

    public void setEkgrp(String ekgrp) {
        this.ekgrp = ekgrp;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }

    public BigDecimal getWkurs() {
        return wkurs;
    }

    public void setWkurs(BigDecimal wkurs) {
        this.wkurs = wkurs;
    }

    public String getKufix() {
        return kufix;
    }

    public void setKufix(String kufix) {
        this.kufix = kufix;
    }

    public Date getBedat() {
        return bedat;
    }

    public void setBedat(Date bedat) {
        this.bedat = bedat;
    }

    public String getIhrez() {
        return ihrez;
    }

    public void setIhrez(String ihrez) {
        this.ihrez = ihrez;
    }

    public String getVerkf() {
        return verkf;
    }

    public void setVerkf(String verkf) {
        this.verkf = verkf;
    }

    public String getTelf1() {
        return telf1;
    }

    public void setTelf1(String telf1) {
        this.telf1 = telf1;
    }

    public String getInco1() {
        return inco1;
    }

    public void setInco1(String inco1) {
        this.inco1 = inco1;
    }

    public String getInco2() {
        return inco2;
    }

    public void setInco2(String inco2) {
        this.inco2 = inco2;
    }

    public String getSubmi() {
        return submi;
    }

    public void setSubmi(String submi) {
        this.submi = submi;
    }

    public String getUnsez() {
        return unsez;
    }

    public void setUnsez(String unsez) {
        this.unsez = unsez;
    }

    public String getFrggr() {
        return frggr;
    }

    public void setFrggr(String frggr) {
        this.frggr = frggr;
    }

    public String getFrgsx() {
        return frgsx;
    }

    public void setFrgsx(String frgsx) {
        this.frgsx = frgsx;
    }

    public String getFrgke() {
        return frgke;
    }

    public void setFrgke(String frgke) {
        this.frgke = frgke;
    }

    public String getFrgzu() {
        return frgzu;
    }

    public void setFrgzu(String frgzu) {
        this.frgzu = frgzu;
    }

    public BigDecimal getRlwrt() {
        return rlwrt;
    }

    public void setRlwrt(BigDecimal rlwrt) {
        this.rlwrt = rlwrt;
    }

    public Date getUdate() {
        return udate;
    }

    public void setUdate(Date udate) {
        this.udate = udate;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getStrSuppl1() {
        return strSuppl1;
    }

    public void setStrSuppl1(String strSuppl1) {
        this.strSuppl1 = strSuppl1;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getAdrnr() {
        return adrnr;
    }

    public void setAdrnr(String adrnr) {
        this.adrnr = adrnr;
    }

    public Date getAngdt() {
        return angdt;
    }

    public void setAngdt(Date angdt) {
        this.angdt = angdt;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RfqEkkoVO)) {
            return false;
        }
        RfqEkkoVO other = (RfqEkkoVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.RfqEkkoVO[ id=" + id + " ]";
    }
    
}
