/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

import com.tcci.cm.model.global.GlobalConstant;
//import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
public enum RsErrEnum {
    INPUT("1", "輸入資料格式錯誤!")
    ,DATETIME("2", "輸入日期時間格式錯誤，或此日期時間不存在!")
    ,STR_LENGTH("3", "輸入資料長度不可超過")
    ,STR_LENGTH_CH("4", "有包含中文或全形時，輸入資料長度不可超過")
    ,INPUT_EXISTED("5", "輸入值已存在，不可重複!")
    ;
    
    private String code;
    private String name;
    
    RsErrEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static RsErrEnum getFromCode(String code){
        for (RsErrEnum enum1 : RsErrEnum.values()) {
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
//        String res = ResourceBundleUtils.getDisplayName(GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
//        if( res==null ){
//            res = name;
//        }
//        return res;
        return name;
    } 
    public String getDisplayName(Locale locale){
//        String res = ResourceBundleUtils.getDisplayName(locale, GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
//        if( res==null ){
//            res = name;
//        }
//        return res;
        return name;
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
