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
    ///Unknown("Unknown", "購物車"),// 後台不需顯示
    Pending("Pending", "待賣方確認", false, false, false),
    Declined("Declined", "賣方回絕訂單", false, false, false), // 賣方拒絕  訂單階段(未確認) 
    Returned("Returned", "買方收回訂單", false, false, false), //買方收回  訂單階段(未確認) 
    Approve("Approve", "已核准", true, true, true),
    Waiting("Waiting", "待買方確認訂單量、價", true, true, true), // 不出現在 STATUS 欄位，已 BUYER_CHECK 控制
    Cancelled("Cancelled", "取消", false, false, false), // 取消 訂單階段(未付款 未出貨)  買買雙方都可執行
    ///Authorized("Authorized", "已授權"),
    Close("Close", "結案", false, true, false); //結案 到貨

    private String code;
    private String name;
    private boolean waiting;// 在途的有效PO
    private boolean canCount;// 可加總的有效PO
    private boolean canChange;// for EC1.5 可改價、量
       
    OrderStatusEnum(String code, String name, boolean waiting, boolean canCount, boolean canChange){
        this.code = code;
        this.name = name;
        this.waiting = waiting;
        this.canCount = canCount;
        this.canChange = canChange;
    }
    
    public static String getWaitingListStr(){
        StringBuilder sb = new StringBuilder();
        for(String code : getWaitingCodes()){
            sb.append(sb.toString().isEmpty()?"'":" ,'").append(code).append("'");
        }
        return sb.toString();
    }
    
    public static String getCanCountListStr(){
        StringBuilder sb = new StringBuilder();
        for(String code : getCanCountCodes()){
            sb.append(sb.toString().isEmpty()?"'":" ,'").append(code).append("'");
        }
        return sb.toString();
    }
    
    public static String getCanChangeListStr(){
        StringBuilder sb = new StringBuilder();
        for(String code : getCanChangeCodes()){
            sb.append(sb.toString().isEmpty()?"'":" ,'").append(code).append("'");
        }
        return sb.toString();
    }
    
    public static String getStatusListStr(){
        StringBuilder sb = new StringBuilder();
        for(String code : getCodes()){
            sb.append(sb.toString().isEmpty()?"'":" ,'").append(code).append("'");
        }
        return sb.toString();
    }
    
    public static List<String> getWaitingCodes(){
        List<String> list = new ArrayList<String>();
        for (OrderStatusEnum enum1 : OrderStatusEnum.values()) {
            if( enum1.isWaiting() ){
                list.add(enum1.getCode());
            }
        }
        return list;
    }
    
    public static List<String> getCanCountCodes(){
        List<String> list = new ArrayList<String>();
        for (OrderStatusEnum enum1 : OrderStatusEnum.values()) {
            if( enum1.isCanCount() ){
                list.add(enum1.getCode());
            }
        }
        return list;
    }
    
    public static List<String> getCanChangeCodes(){
        List<String> list = new ArrayList<String>();
        for (OrderStatusEnum enum1 : OrderStatusEnum.values()) {
            if( enum1.isCanChange() ){
                list.add(enum1.getCode());
            }
        }
        return list;
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

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public boolean isCanChange() {
        return canChange;
    }

    public void setCanChange(boolean canChange) {
        this.canChange = canChange;
    }

    public boolean isCanCount() {
        return canCount;
    }

    public void setCanCount(boolean canCount) {
        this.canCount = canCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
    
}

