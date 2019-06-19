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
public enum TodoEnum {
    PRD_ONSALES("1", "待上架商品", false),
    PRD_CORRECT("2", "待修正商品", false),
    QUOTATION("3", "待報價詢價單", false),
    PO_CONFIRM("4", "待確認訂單", false),
    PO_PAY_RECEIVED("5", "待確認收款訂單", false),
    PO_SHIP("6", "待出貨訂單", false),
    CUS_CREDITS("7", "信用額度申請", false),
    PO_MSG_REPLY("8", "待回覆訊息訂單", false),
    MEM_MSG_REPLY("9", "待回覆訪客留言", false),
    ;
    
    private String code;
    private String name;
    private boolean admin;// for admin
    
    TodoEnum(String code, String name, boolean admin){
        this.code = code;
        this.name = name;
        this.admin = admin;
    }
    
    public static TodoEnum getFromCode(String code){
        for (TodoEnum enum1 : TodoEnum.values()) {
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}

