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
 * 客戶信用額度異動類別
 * @author Peter.pan
 */
public enum CreditLogEnum {
    DIRECT_EDIT("1", "賣家直接編輯信用額度"),
    ADD_LOG("2", "賣家編輯異動記錄"),// 使用UI需輸入原因
    ORDER_PRE_PAY("3", "訂單預扣款"),
    ORDER_RETURE("4", "訂單回補預扣款"),
    ORDER_FINAL_PAY("5", "訂單最終扣款"), 
    OTHERS("9", "其他原因"), 
    ;
    
    private String code;
    private String name;
    
    CreditLogEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static CreditLogEnum getFromCode(String code){
        for (CreditLogEnum enum1 : CreditLogEnum.values()) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}
