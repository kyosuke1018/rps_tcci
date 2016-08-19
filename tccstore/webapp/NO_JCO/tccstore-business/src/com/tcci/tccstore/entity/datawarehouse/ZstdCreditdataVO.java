/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity.datawarehouse;

import java.math.BigDecimal;

/**
 *
 * @author Jimmy.Lee
 */
public class ZstdCreditdataVO {

    private String kunnr;        // 客戶編號
    private String bukrs;        // 公司代碼嘞
    private String datum;        // 查詢日期
    private String uzeit;        // 查詢時間
    private BigDecimal skfor;    // 應收款總計 (應收帳款)
    private BigDecimal ssobl;    // 特殊負債   (預收款)
    private BigDecimal sauft;    // 銷售值總計 (銷售值)
    private BigDecimal oblig;    // 信用外曝   (客戶餘額, 負值:結餘, 正值:超支)
    private BigDecimal ssoblIn;  // 當日預收款 (當日入帳金額)

    // getter, setter
    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
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

    public BigDecimal getSkfor() {
        return skfor;
    }

    public void setSkfor(BigDecimal skfor) {
        this.skfor = skfor;
    }

    public BigDecimal getSsobl() {
        return ssobl;
    }

    public void setSsobl(BigDecimal ssobl) {
        this.ssobl = ssobl;
    }

    public BigDecimal getSauft() {
        return sauft;
    }

    public void setSauft(BigDecimal sauft) {
        this.sauft = sauft;
    }

    public BigDecimal getOblig() {
        return oblig;
    }

    public void setOblig(BigDecimal oblig) {
        this.oblig = oblig;
    }

    public BigDecimal getSsoblIn() {
        return ssoblIn;
    }

    public void setSsoblIn(BigDecimal ssoblIn) {
        this.ssoblIn = ssoblIn;
    }

}
