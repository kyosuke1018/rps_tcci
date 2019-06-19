/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
public enum OrderStatusEnum {
    //Unknown("Unknown", "購物車"),// 後台不需顯示
    Pending("Pending", "待賣方確認"),
    Declined("Declined", "賣方回絕訂單"), // 賣方拒絕  訂單階段(未確認) 
    Returned("Returned", "買方收回訂單"), //買方收回  訂單階段(未確認) 
    Approve("Approve", "已核准"),
    Cancelled("Cancelled", "取消"), // 取消 訂單階段(未付款 未出貨)  買買雙方都可執行
    //Authorized("Authorized", "已授權"),
    Close("Close", "結案");     //結案  到貨
    ;

    private String code;
    private String name;
       
    OrderStatusEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static List<String> getCodes(){
        List<String> list = new ArrayList<String>();
        for (OrderStatusEnum enum1 : OrderStatusEnum.values()) {
            list.add(enum1.getCode());
        }
        return list;
    }

    public static OrderStatusEnum getFromCode(String code){
        for (OrderStatusEnum enum1 : OrderStatusEnum.values()) {
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

