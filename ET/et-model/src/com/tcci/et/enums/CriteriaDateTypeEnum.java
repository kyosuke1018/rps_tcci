/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 * 日期時間條件類型
 * @author Peter.Pan    
 */
public enum CriteriaDateTypeEnum {
    CREATEDATE("CREATEDATE", "建立日期時間"),
    MODIFYDATE("MODIFYDATE", "最後更改時間")
    ;

    private String code;
    private String name;
    
    CriteriaDateTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static CriteriaDateTypeEnum getFromCode(String code){
        for (CriteriaDateTypeEnum enum1 : CriteriaDateTypeEnum.values()) {
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

