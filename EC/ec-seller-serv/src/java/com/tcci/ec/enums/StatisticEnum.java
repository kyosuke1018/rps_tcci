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
public enum StatisticEnum {
    TODO("todo", "待辦事項統計"),
    
    PO_STATUS("orderStatus", "訂單狀態"),
    RFQ_STATUS("rfqStatus", "詢價單狀態"),
    PO_CUMNLATIVE("orderCumulative", "訂單累計金額"),
    CUS_LEVEL("cusLevel", "客戶等級"),
    PRD_STATUS("prdStatus", "商品狀態"),
    PRD_TYPE("prdType", "銷售商品分類"),
    
    //營運管理：
    PRD_SALES("prdSales", "商品銷售量"),
    PRD_INV("prdInventory", "商品庫存量"),
    UNDELIVERED("undelivered", "商品未出貨量"),
    CASH_FLOW("cashFlow", "預計收款金額"),
    SALES_AMT("salesAmt", "銷售金額"),
    PO_DUE("orderDue", "逾期金額"),
    
    //銷售分析：
    ANA_CUS("anaByCustomer", "依客戶"),
    ANA_MARKET("anaByMarket", "依市場"),
    ANA_PRD("anaByProduct", "依商品"),
    ANA_AREA("anaByArea", "依區域"),
    ;
    
    private String code;
    private String name;

    StatisticEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static StatisticEnum getFromCode(String code){
        for (StatisticEnum enum1 : StatisticEnum.values()) {
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
