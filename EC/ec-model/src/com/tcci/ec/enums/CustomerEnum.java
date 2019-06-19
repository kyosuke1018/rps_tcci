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
public enum CustomerEnum {
    ORDER("O", "交易產生的客戶"),
    CREDITS("C", "申請信用額度產生的客戶"),
    TCC("T", "客戶本身為台泥"),
    ;
    
    private String code;
    private String name;
    
    CustomerEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static CustomerEnum getFromCode(String code){
        for (CustomerEnum enum1 : CustomerEnum.values()) {
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

