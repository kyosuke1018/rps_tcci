/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.datawarehouse;

/**
 *
 * @author Jimmy.Lee
 */
public class CrmFilter {

    // null:不檢查

    private String kunnr; // 客戶代碼
    private String vkorg; // 公司代碼
    private String werks; // 廠別: equal
    private String bzirk; // 銷售地區: equal
    private String xblnr; // 車牌號碼: like
    private String vsart; // 運輸方式: equal
    private String inco1; // 國貿條件: like

    // getter, setter
    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public String getBzirk() {
        return bzirk;
    }

    public void setBzirk(String bzirk) {
        this.bzirk = bzirk;
    }

    public String getXblnr() {
        return xblnr;
    }

    public void setXblnr(String xblnr) {
        this.xblnr = xblnr;
    }

    public String getVsart() {
        return vsart;
    }

    public void setVsart(String vsart) {
        this.vsart = vsart;
    }

    public String getInco1() {
        return inco1;
    }

    public void setInco1(String inco1) {
        this.inco1 = inco1;
    }
}
