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
 *
 * @author Peter.pan
 */
public enum CusOrderStatusEnum {
    SHIPPING("1", "有未出貨訂單"),
    PAYMENT("2", "有未付款訂單"),
    INVOICE("3", "有待寄發票訂單"),
    RETRUE("4", "有退貨的訂單"),
    REFUND("5", "有待退款訂單"),
    CANCEL("6", "有取消的訂單")
    ;
    
    private String code;
    private String name;
    
    CusOrderStatusEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static CusOrderStatusEnum getFromValue(Integer num){
        for (CusOrderStatusEnum enum1 : CusOrderStatusEnum.values()) {
            if( num!=null && Integer.parseInt(enum1.getCode())==num ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static CusOrderStatusEnum getFromCode(String code){
        for (CusOrderStatusEnum enum1 : CusOrderStatusEnum.values()) {
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
