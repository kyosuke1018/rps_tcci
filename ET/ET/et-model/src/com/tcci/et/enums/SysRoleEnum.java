/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
public enum SysRoleEnum {
    ADMIN("ADMIN", "系統管理員"),
//    SELLER("SELLER", "網站賣家建立人"),
//    MANAGER("MANAGER", "商店管理人員"),
    MEMBER("MEMBER", "網站會員"),
//    TCC_DEALER("TCC_DEALER", "台泥經銷商"),
//    TCC_DS("TCC_DS", "台泥經銷商下游客戶(攪拌站、檔口)")
    VALID("MEMBER", "網站會員");
    
    private String code;
    private String name;
    
    SysRoleEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static SysRoleEnum getFromCode(String code){
        for (SysRoleEnum enum1 : SysRoleEnum.values()) {
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
