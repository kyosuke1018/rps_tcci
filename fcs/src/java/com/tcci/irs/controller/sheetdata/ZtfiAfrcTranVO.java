/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.controller.sheetdata;

import com.tcci.fc.controller.dataimport.ExcelVOBase2;
import com.tcci.irs.entity.sheetdata.ZtfiAfrcTran;
import java.math.BigDecimal;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gilbert.Lin
 */
public class ZtfiAfrcTranVO extends ExcelVOBase2 {
//    @NotNull
    private short zgjahr;
//    @NotNull
    private short zmonat;
//    @NotNull
//    @Size(min = 4, max = 4, message = "[公司代碼]須為4碼!")
//    private String bukrs;
    
    @NotNull(message = "[關係人公司代碼]必填!")
    @Size(min = 4, max = 4, message = "[關係人公司代碼]須為4碼!")
    private String zafbuk;
    
    @NotNull(message = "[對帳分類]必填!")
    @Size(min = 2, max = 2, message = "[對帳分類]須為2碼!")
    private String zafcat;
    
    @NotNull(message = "[對帳交易別]必填!")
    @Size(min = 4, max = 4, message = "[對帳交易別]須為4碼!")
    private String zaftyp;
    
    @NotNull(message = "[會計年度]必填!")
    @Size(min = 4, max = 4, message = "[會計年度]須為4碼!")
    private String gjahr;
    
    @NotNull(message = "[會計文件號碼]必填!")
    @Size(max = 12, message = "[會計文件號碼]最多12碼!")
    private String belnr;
    
    @NotNull(message = "[明細碼]必填!")
    @Size(max = 5, message = "[明細碼]最多5碼!")
    private String buzei;
    
    @Size(max = 10, message = "[客戶編號]最多10碼!")
    private String kunnr;
    
    @Size(max = 10, message = "[供應商或貸方的帳號]最多10碼!")
    private String lifnr;
    
    @Size(max = 10, message = "[總帳科目]最多10碼!")
    private String hkont;
    
//    @NotNull(message = "[總帳科目_IFRS]必填!")
//    @Size(max = 10, message = "[總帳科目_IFRS]最多10碼!")
//    private String hkontIfrs;
    
    @NotNull(message = "[文件過帳日期]必填!")
    @Size(min = 8, max = 8, message = "[文件過帳日期]須為8碼!")
    private String budat;
    
    @Size(max = 2, message = "[文件類型]最多2碼!")
    private String blart;
    
    @NotNull(message = "[幣別碼]必填!")
    @Size(max = 5, message = "[幣別碼]最多5碼!")
    private String waers;//幣別碼
    
    @NotNull(message = "[文件貨幣金額]必填!")
    @Digits(integer=10, fraction=2, message="[文件貨幣金額]小數最多兩位")
    private BigDecimal wrbtr;//文件貨幣金額
    
    @NotNull(message = "[本國貨幣金額]必填!")
    @Digits(integer=10, fraction=2, message="[本國貨幣金額]小數最多兩位")
    private BigDecimal dmbtr;//本國貨幣金額
    
    @Size(max = 18, message = "[指派號碼]最多18碼!")
    private String zuonr;
    
    @Size(max = 50, message = "[內文]最多50碼!")
    private String sgtxt;
    
//    @Size(max = 1, message = "[加總SACO]最多1碼!")
//    private String zaftlSaco;
    
    @NotNull(message = "[關係人公司名稱]必填!")
    @Size(max = 40, message = "[關係人公司名稱]最多40碼!")
    private String zafbukNm;
    
    @NotNull(message = "[總帳科目長文]必填!")
    @Size(max = 50, message = "[總帳科目長文]最多50碼!")
    private String txt50;
    
    @NotNull(message = "[會計科目]必填!")
    @Size(min = 4, max = 6, message = "[會計科目]最多6碼!")
    private String accountCode;
    
//    @Size(max = 1, message = "[對帳註記]須為1碼!")
//    private String zckflg;
//    @Size(min = 8, max = 8, message = "[最近更新日期]須為8碼!")
//    private String zlupdt;
    
    // helper field
    private ZtfiAfrcTran entity;    
    
    public ZtfiAfrcTran getEntity() {
        return entity;
    }
    public void setEntity(ZtfiAfrcTran entity) {
        this.entity = entity;
    }
    
    @Override
    public void updateStatus() {
        status = Status.ST_INSERT;
    }

//<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public short getZgjahr() {
        return zgjahr;
    }
    
    public void setZgjahr(short zgjahr) {
        this.zgjahr = zgjahr;
    }
    
    public short getZmonat() {
        return zmonat;
    }
    
    public void setZmonat(short zmonat) {
        this.zmonat = zmonat;
    }
    
    public String getZafbuk() {
        return zafbuk;
    }
    
    public void setZafbuk(String zafbuk) {
        this.zafbuk = zafbuk;
    }
    
    public String getZafcat() {
        return zafcat;
    }
    
    public void setZafcat(String zafcat) {
        this.zafcat = zafcat;
    }
    
    public String getZaftyp() {
        return zaftyp;
    }
    
    public void setZaftyp(String zaftyp) {
        this.zaftyp = zaftyp;
    }
    
    public String getGjahr() {
        return gjahr;
    }
    
    public void setGjahr(String gjahr) {
        this.gjahr = gjahr;
    }
    
    public String getBelnr() {
        return belnr;
    }
    
    public void setBelnr(String belnr) {
        this.belnr = belnr;
    }
    
    public String getBuzei() {
        return buzei;
    }
    
    public void setBuzei(String buzei) {
        this.buzei = buzei;
    }
    
    public String getKunnr() {
        return kunnr;
    }
    
    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }
    
    public String getLifnr() {
        return lifnr;
    }
    
    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }
    
    public String getHkont() {
        return hkont;
    }
    
    public void setHkont(String hkont) {
        this.hkont = hkont;
    }

//    public String getHkontIfrs() {
//	return hkontIfrs;
//    }
//
//    public void setHkontIfrs(String hkontIfrs) {
//	this.hkontIfrs = hkontIfrs;
//    }
    
    public String getBudat() {
        return budat;
    }
    
    public void setBudat(String budat) {
        this.budat = budat;
    }
    
    public String getBlart() {
        return blart;
    }
    
    public void setBlart(String blart) {
        this.blart = blart;
    }
    
    public String getWaers() {
        return waers;
    }
    
    public void setWaers(String waers) {
        this.waers = waers;
    }
    
    public BigDecimal getWrbtr() {
        return wrbtr;
    }
    
    public void setWrbtr(BigDecimal wrbtr) {
        this.wrbtr = wrbtr;
    }
    
    public BigDecimal getDmbtr() {
        return dmbtr;
    }
    
    public void setDmbtr(BigDecimal dmbtr) {
        this.dmbtr = dmbtr;
    }
    
    public String getZuonr() {
        return zuonr;
    }
    
    public void setZuonr(String zuonr) {
        this.zuonr = zuonr;
    }
    
    public String getSgtxt() {
        return sgtxt;
    }
    
    public void setSgtxt(String sgtxt) {
        this.sgtxt = sgtxt;
    }
    
    public String getZafbukNm() {
        return zafbukNm;
    }
    
    public void setZafbukNm(String zafbukNm) {
        this.zafbukNm = zafbukNm;
    }
    
    public String getTxt50() {
        return txt50;
    }
    
    public void setTxt50(String txt50) {
        this.txt50 = txt50;
    }

    public String getAccountCode() {
	return accountCode;
    }

    public void setAccountCode(String accountCode) {
	this.accountCode = accountCode;
    }
//</editor-fold>
}
