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
public enum ReportSheetEnum {
    A0102("A0102", "損益表"),
    A0102_IS("A0102_IS", "IS本表不填"),
    A0102_BS("A0102_BS", "BS本表不填"),
    A0214("A0214", "營業收入"),
    D0202("D0202", "資產科目_合併聯屬公司間"),
    D0204("D0204", "負債科目_合併聯屬公司間"),
    D0206("D0206", "營業及營業外收入_合併聯屬公司間"),
    D0208("D0208", "營業及營業外支出_合併聯屬公司間"),
    TB("TB", "試算表");
    
    private String code;
    private String name;
    
    ReportSheetEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static ReportSheetEnum getFromCode(String code){
        for (ReportSheetEnum enum1 : ReportSheetEnum.values()) {
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
    //</editor-fold>

}
