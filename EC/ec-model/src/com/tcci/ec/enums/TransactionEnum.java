/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

import com.tcci.cm.model.global.GlobalConstant;
//import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
public enum TransactionEnum {
    PAY_FIRST("A", "付款後出貨"),
    SHIP_FIRST("B", "貨到付款"),
    INSTALLMENT("C", "分期付款/出貨");

    private String code;
    private String name;
    
    TransactionEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static TransactionEnum getFromValue(Integer num){
        for (TransactionEnum enum1 : TransactionEnum.values()) {
            if( num!=null && Integer.parseInt(enum1.getCode())==num ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static TransactionEnum getFromCode(String code){
        for (TransactionEnum enum1 : TransactionEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
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
//        String res = ResourceBundleUtils.getDisplayName(GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
//        if( res==null ){
//            res = name;
//        }
//        return res;
        return name;
    } 
    public String getDisplayName(Locale locale){
//        String res = ResourceBundleUtils.getDisplayName(locale, GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
//        if( res==null ){
//            res = name;
//        }
//        return res;
        return name;
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

