/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.enums;

import com.tcci.fc.util.ResourceBundleUtils;
import com.tcci.cm.model.global.GlobalConstant;

/**
 *
 * @author Peter.pan
 */
public enum DocTypeEnum {
    CONTRACT("C", "合約文件")
    ,RECONTRACT("R", "租賃合約文件")
    ,MACONTRACT("M", "物料合約文件")
    ,TEMP("T", "暫存文件")
    ,PAYMENT("P", "請(付)款作業文件")
    ;
    
    private String code;
    private String name;
    
    DocTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
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
    
    public static DocTypeEnum getFromCode(String code){
        for (DocTypeEnum enum1 : DocTypeEnum.values()) {
            if( enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
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
