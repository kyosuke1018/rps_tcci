/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 * 供應商來源
 * @author Peter.Pan
 */
public enum VenderSrcEnum {
    SAP("S", "SAP供應商")
    //,ONCE("O", "一次性供應商")
    ,WEB("W", "陽光平台申請中供應商")
    ;

    private String code;
    private String name;
    
    VenderSrcEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static VenderSrcEnum getFromCode(String code){
        for (VenderSrcEnum enum1 : VenderSrcEnum.values()) {
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

