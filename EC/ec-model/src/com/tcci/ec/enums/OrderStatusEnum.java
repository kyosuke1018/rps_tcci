/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtil;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;

/**
 *
 * @author Neo.Fu
 */
public enum OrderStatusEnum {
    //詢價階段
    Unknown("Unknown", "購物車"),      // 未成立(購物車)
    Inquiry("Inquiry", "待報價"),   //買方詢價==> 待報價
    Quotation("Quotation", "已報價"), //賣方報價==>已報價
    Deleted("Deleted", "刪除"),  //買方刪除 詢價階段
    Rejected("Rejected", "賣方拒絕-報價"), // 賣方拒絕 詢價階段
    //訂單階段
    Pending("Pending", "待賣方確認"),  // 待確認 買方送出 待賣方確認
    Approve("Approve", "已確認"), // 已核准 賣方確認
    Declined("Declined", "賣方拒絕-訂單"), // 賣方拒絕 訂單階段(未確認)
    Returned("Returned", "買方收回-訂單"), //買方收回  訂單階段(未確認) 
    Cancelled("Cancelled", "取消"), // 取消 賣方未確認  已核准 未付款未出貨 賣家可取消訂單
    
    Authorized("Authorized", "已授權"),//已授權
//    Returned("Returned", "退回"),     //退回
    
    Close("Close", "結案");     //結案
    
    private String code;
    private String name;
    
    OrderStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
    
    public static OrderStatusEnum fromCode(String code) {
        if (code != null) {
            for (OrderStatusEnum thisEnum : OrderStatusEnum.values()) {
                if (code.equals(thisEnum.getCode())) {
                    return thisEnum;
                }
            }
        }
        return null;
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

    public static OrderStatusEnum fromString(String key) {
        OrderStatusEnum result = null;
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.toString().equals(key)) {
                result = status;
                break;
            }
        }
        return result;
    }
}
