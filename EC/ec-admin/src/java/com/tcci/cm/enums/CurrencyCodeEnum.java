/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 *
 * @author Peter.Pan    
 */
public enum CurrencyCodeEnum {
    //幣別代碼
    TWD("TWD", "台幣"),
    RMB("RMB", "人民幣"),
    USD("USD", "美金"),
    JPY("JPY", "日幣"),
    EUR("EUR", "歐元");
    
    private String code;
    private String name;
    
    CurrencyCodeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static CurrencyCodeEnum getFromCode(String code){
        for (CurrencyCodeEnum enum1 : CurrencyCodeEnum.values()) {
            if( enum1.getCode().equals(code) ) {
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
}
