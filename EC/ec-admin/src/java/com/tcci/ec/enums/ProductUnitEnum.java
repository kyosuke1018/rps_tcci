/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;

/**
 * 預設提供單位
 * @author Peter.pan
 */
public enum ProductUnitEnum {
    METRIC_TON("m.t.", "TO", "公噸", "metric ton", "W"),
    //KILOGRAM("kg.", "公斤", "kilogram", "W"),
    //GRAM("g.", "公克", "gram", "W"),
    //CUBIC_METER("cu.m", "立方米", "cubic meter", "V"),
    //BALE("bale", "包", "bale", "B"),
    //BAG("bag", "袋", "bag", "B"),
    //CAR("car", "車", "car", "S")
    ;

    private String code;
    private String sapCode;
    private String name;
    private String ename;
    private String type; // 前端判斷是否輸入相關欄位用
    
    ProductUnitEnum(String code, String sapCode, String name, String ename, String type){
        this.code = code;
        this.sapCode = sapCode;
        this.name = name;
        this.ename = ename;
        this.type = type;
    }
    
    public static ProductUnitEnum getFromCode(String code){
        for (ProductUnitEnum enum1 : ProductUnitEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static ProductUnitEnum getFromSapCode(String sapCode){
        for (ProductUnitEnum enum1 : ProductUnitEnum.values()) {
            if( sapCode!=null && enum1.getSapCode().equals(sapCode) ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    /**
     * 顯示名稱 (取自enum.properties => [class name].[enum name])
     * @return 
     */
    public String getDisplayName(){
        String res = ResourceBundleUtils.getDisplayName(GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
        if( res==null ){
            res = name;
        }
        return res;
    } 
    public String getDisplayName(Locale locale){
        String res = ResourceBundleUtils.getDisplayName(locale, GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
        if( res==null ){
            res = name;
        }
        return res;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSapCode() {
        return sapCode;
    }

    public void setSapCode(String sapCode) {
        this.sapCode = sapCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
    
}

