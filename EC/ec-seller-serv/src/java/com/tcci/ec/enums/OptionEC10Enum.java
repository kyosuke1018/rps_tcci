/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.enums.ec10.ShipMethodEC10Enum;
import com.tcci.ec.enums.ec10.TranTypeEC10Enum;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;

/**
 * tranType,shipMethod,customer,contract,province,city,district,town,salesArea,plant
 * 
 * EC_OPTION.TYPE
 * @author Peter.pan
 */
public enum OptionEC10Enum {
    // enums
    TRAN_TYPE("tranType", "交易類型", TranTypeEC10Enum.class, null, false)
    ,SHIP_METHOD("shipMethod", "提貨方式", ShipMethodEC10Enum.class, null, false)
    
    // 獨立 Table
    ,CUSTOMER("customer", "下單客戶", null, "EC_CUSTOMER", false)
    ,CONTRACT("contract", "合約", null, "EC_CONTRACT", false)
    ,PROVINCE("province", "省", null, "EC_DELIVERY_PLACE", false)
    ,CITY("city", "市", null, "EC_DELIVERY_PLACE", false)
    ,DISTRICT("district", "區", null, "EC_DELIVERY_PLACE", false)
    ,TOWN("town", "鎮", null, "EC_DELIVERY_PLACE", false)
    ,SALES_AREA("salesArea", "銷售地區", null, "EC_SALESAREA", false)
    ,PLANT("plant", "出貨廠", null, "EC_PLANT", false)
    ,SALES("sales", "業務", null, "EC_SALES", false)
    ,CUS_ADDR("cusAddr", "常用設定", null, "EC_CUS_ADDR", true)
    
    // 特殊
    ,DELIVERY_DATE("deliveryDate", "提貨日期", null, null, false)
    ,COMBINE_OP("combineOptions", "可併單條件", null, null, false)
    ;
    
    private String code;
    private String name;
    private Class enumClass;
    private String table;
    private boolean store;// 區分店家
    
    OptionEC10Enum(String code, String name, Class enumClass, String table, boolean store){
        this.code = code;
        this.name = name;
        this.enumClass = enumClass;
        this.table = table;
        this.store = store;
    }
    
    public static OptionEC10Enum getFromCode(String code){
        for (OptionEC10Enum enum1 : OptionEC10Enum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static OptionEC10Enum getFromEnum(Class clazz){
        for (OptionEC10Enum enum1 : OptionEC10Enum.values()) {
            if( clazz!=null && enum1.getEnumClass()==clazz ) {
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

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Class getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class enumClass) {
        this.enumClass = enumClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
    
}
