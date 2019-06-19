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
 * 庫存異動記錄類別
 * @author Peter.pan
 */
public enum StockEnum {
    FIRST("F", "初始庫存", true, null),
    INCOME("I", "進貨", true, null),
    OUTCOME("O", "訂單出貨", false, null),
    SOLD("S", "已售出", false, "訂單編號：{0}"),
    MINUS("M", "其他庫存減項", false, null),
    ADD("A", "其他庫存增項", true, null),
    ;
    
    private String code;
    private String name;
    private boolean postive;
    private String defMemo;
    
    StockEnum(String code, String name, boolean postive, String defMemo){
        this.code = code;
        this.name = name;
        this.postive = postive;
        this.defMemo = defMemo;
    }
    
    public static StockEnum getFromCode(String code){
        for (StockEnum enum1 : StockEnum.values()) {
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
    public String getDisplayMemo(Locale locale){
//        String res = ResourceBundleUtils.getDisplayName(locale, GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString()+".MEMO" );
//        if( res==null ){
//            res = defMemo!=null?defMemo:"";
//        }
//        return res;
        return defMemo;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDefMemo() {
        return defMemo;
    }

    public void setDefMemo(String defMemo) {
        this.defMemo = defMemo;
    }

    public boolean isPostive() {
        return postive;
    }

    public void setPostive(boolean postive) {
        this.postive = postive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}
