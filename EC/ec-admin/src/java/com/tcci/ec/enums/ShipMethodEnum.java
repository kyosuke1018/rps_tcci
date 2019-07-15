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
public enum ShipMethodEnum {
    SELF("1", "自行取貨", "A"),
    DELIVERY("2", "宅配", "B");

    private String code;
    private String name;
    private String type; // 前端判斷是否輸入相關欄位用
    
    ShipMethodEnum(String code, String name, String type){
        this.code = code;
        this.name = name;
        this.type = type;
    }
    
    public static ShipMethodEnum getFromValue(Integer num){
        for (ShipMethodEnum enum1 : ShipMethodEnum.values()) {
            if( num!=null && Integer.parseInt(enum1.getCode())==num ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static ShipMethodEnum getFromCode(String code){
        for (ShipMethodEnum enum1 : ShipMethodEnum.values()) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
    
}
