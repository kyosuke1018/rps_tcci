/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.controller.sheetdata;

import com.tcci.fc.controller.dataimport.ExcelVOBase2;
import com.tcci.irs.entity.sheetdata.ZtfiAfrcInvo;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gilbert.Lin
 */
public class ZtfiAfrcInvoVO extends ExcelVOBase2 {
//    @NotNull
    private short zgjahr;
//    @NotNull
    private short zmonat;
//    @NotNull(message = "[公司代碼]必填!")
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
    
    @NotNull(message = "[發票號碼]必填!")
    @Size(max = 10, message = "[發票號碼]最多10碼!")
    private String guino;
    
    @Size(max = 10, message = "[客戶編號]最多10碼!")
    private String kunnr;
    
    @Size(max = 10, message = "[供應商或貸方的帳號]最多10碼!")
    private String lifnr;
    
    @NotNull(message = "[統一編號]必填!")
    @Size(max = 8, message = "[統一編號]最多8碼!")
    private String guiRegis;
    
    @NotNull(message = "[文件中的過帳日期]必填!")
    @Size(min = 8, max = 8, message = "[文件中的過帳日期]須為8碼!")
    private String budat;
    
    @NotNull(message = "[幣別碼]必填!")
    @Size(max = 5, message = "[幣別碼]最多5碼!")
    private String waers;
    
    @NotNull(message = "[稅前金額]必填!")
    private BigDecimal baseamt;
    
    @NotNull(message = "[稅額]必填!")
    private BigDecimal taxamt;
    
    @NotNull(message = "[稅後金額]必填!")
    private BigDecimal totalamt;
    
    @NotNull(message = "[關係人公司名稱]必填!")
    @Size(max = 40, message = "[關係人公司名稱]最多40碼!")
    private String zafbukNm;
    
    
//    @Size(min = 1, max = 1, message = "[對帳註記]須為1碼!")
//    private String zckflg;//對帳註記
//    @Size(min = 8, max = 8, message = "[最近更新日期]須為8碼!")
//    private String zlupdt;//最近更新日期
    //
    // helper field
    private ZtfiAfrcInvo entity;    
    
    @Override
    public void updateStatus() {
        status = Status.ST_INSERT;
    }
    
    public ZtfiAfrcInvo getEntity() {
        return entity;
    }
    
    public void setEntity(ZtfiAfrcInvo entity) {
        this.entity = entity;
    }

//<editor-fold defaultstate="collapsed" desc="Getter and Setter">
//    public String getYearMonth() {
//        return yearMonth;
//    }
//
//    public void setYearMonth(String yearMonth) {
//        this.yearMonth = yearMonth;
//    }
//
//    public FcCompany getCompany() {
//        return company;
//    }
//
//    public void setCompany(FcCompany company) {
//        this.company = company;
//    }

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

    public String getBelnr() {
        return belnr;
    }

    public void setBelnr(String belnr) {
        this.belnr = belnr;
    }

    public String getGuino() {
        return guino;
    }

    public void setGuino(String guino) {
        this.guino = guino;
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

    public String getGuiRegis() {
        return guiRegis;
    }

    public void setGuiRegis(String guiRegis) {
        this.guiRegis = guiRegis;
    }

    public String getBudat() {
        return budat;
    }

    public void setBudat(String budat) {
        this.budat = budat;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }

    public BigDecimal getBaseamt() {
        return baseamt;
    }

    public void setBaseamt(BigDecimal baseamt) {
        this.baseamt = baseamt;
    }

    public BigDecimal getTaxamt() {
        return taxamt;
    }

    public void setTaxamt(BigDecimal taxamt) {
        this.taxamt = taxamt;
    }

    public BigDecimal getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(BigDecimal totalamt) {
        this.totalamt = totalamt;
    }

    public String getZafbukNm() {
        return zafbukNm;
    }

    public void setZafbukNm(String zafbukNm) {
        this.zafbukNm = zafbukNm;
    }

    public String getGjahr() {
        return gjahr;
    }

    public void setGjahr(String gjahr) {
        this.gjahr = gjahr;
    }
//</editor-fold>


    
}
