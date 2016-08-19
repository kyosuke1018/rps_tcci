/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.enums;

/**
 *
 * @author Kyle.Cheng
 */
public enum CompanyGroupEnum {
    TCC("TCC", "台泥", "TWD"),
    CSRC("CSRC", "中橡", "TWD"),
//    CSRC_BVI("CSRC_BVI", "CSRC (BVI)", "USD"),
    OTHER("OTHER", "其他", "TWD");
    
    private String code;
    private String name;
    private String currencyCode;
    
    CompanyGroupEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    CompanyGroupEnum(String code, String name, String currencyCode){
        this.code = code;
        this.name = name;
        this.currencyCode = currencyCode;
    }
    
    public static CompanyGroupEnum getFromCode(String code){
        for (CompanyGroupEnum enum1 : CompanyGroupEnum.values()) {
            if (code.trim().equals(enum1.getCode())) {
                return enum1;
            }
        }
        return null; // default
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    //</editor-fold>

}
