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
public enum LogTypeEnum {
    // 手機APP
    LOGIN_APP("LA", "登入-手機APP"),
    FUNC_APP("FA", "APP功能使用"),
    // WEB
    LOGIN_SELLER("LS", "WEB登入-賣家後台"),
    LOGIN_ADMIN("LA", "WEB登入-管理者後台"),
    FUNC_SELLER_WEB("FSW", "WEB功能使用-賣家後台"),
    FUNC_ADMIN_WEB("FAW", "WEB功能使用-管理者後台"),
    ;
    
    private String code;
    private String name;
    
    LogTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static LogTypeEnum getFromCode(String code){
        for (LogTypeEnum enum1 : LogTypeEnum.values()) {
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
