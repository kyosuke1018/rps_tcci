/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 * 報價單狀態(T:暫存;C:確認;I:無效)
 * @author Peter.pan
 */
public enum QuoteStatusEnum {
    TEMP("T", "暫存"),
    CONFIRM("C", "確認"),
    EXPIRED("I", "過期"),
    ;

    private String code;
    private String name;
    
    QuoteStatusEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static QuoteStatusEnum getFromCode(String code){
        for (QuoteStatusEnum enum1 : QuoteStatusEnum.values()) {
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
