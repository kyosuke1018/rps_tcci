/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.fc.util.ResourceBundleUtils;
import com.tcci.cm.model.global.GlobalConstant;

/**
 *
 * @author Peter.pan
 */
public enum CacheTypeEnum {
    // 保種記錄關聯
    AWARD("AW", "授獎等級/單位的組合字串"),
    ;

    private String code;
    private String name;
    
    CacheTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static CacheTypeEnum getFromCode(String code){
        for (CacheTypeEnum enum1 : CacheTypeEnum.values()) {
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
