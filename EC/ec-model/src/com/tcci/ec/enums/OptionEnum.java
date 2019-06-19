/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

//import com.tcci.cm.model.global.GlobalConstant;
//import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;

/**
 * EC_OPTION.TYPE
 * @author Peter.pan
 */
public enum OptionEnum {
    // enums
    PRD_STATUS("prdStatus", "商品狀態", ProductStatusEnum.class, null, false)
    ,PRD_STATUS_SELLER("prdStatusSeller", "商品狀態(賣家可編輯狀態)", ProductStatusEnum.class, null, false)
    ,PRD_STATUS_ADMIN("prdStatusAdmin", "商品狀態(管理員可編輯狀態)", ProductStatusEnum.class, null, false)
    ,ORDER_STATUS("orderStatus", "訂單狀態", OrderStatusEnum.class, null, false)
    ,RFQ_STATUS("rfqStatus", "訂單狀態", RfqStatusEnum.class, null, false)
    ,TRANSACTION_TYPE("transactionType", "訂單交易方式", TransactionEnum.class, null, false)
    ,PAY_STATUS("payStatus", "訂單付款狀態", PayStatusEnum.class, null, false)
    ,SHIP_STATUS("shipStatus", "訂單出貨狀態", ShipStatusEnum.class, null, false)
    ,MEMBER_TYPE("memberType", "會員類型", MemberTypeEnum.class, null, false)
    ,STORE_TYPE("storeType", "商家類型", StoreTypeEnum.class, null, false)
    ,ORDER_SRC("orderSrc", "訂單來源", OrderSrcEnum.class, null, false)
    ,STOCK_LOG_TYPE("stockLogType", "庫存異動類型", StockEnum.class, null, false)
    ,SHIPPING_DEF("shippingSys", "系統提供選擇的運貨方式", ShipMethodEnum.class, null, false)
    ,PAYMENT_DEF("paymentSys", "系統提供選擇的付款方式", PayMethodEnum.class, null, false)
    
    // 獨立 Table
    ,STORES("stores", "線上商店", null, "EC_STORE", false)
    ,PRODUCTS("products", "線上商品", null, "EC_PRODUCT", true)
    ,PRD_TYPE_TREE("prdTypeTree", "商品分類名錄", null, "EC_PRD_TYPE", true)
    ,PRD_COLOR("prdColor", "商品顏色", null, "EC_PRD_VAR_OPTION", true)
    ,PRD_SIZE("prdSize", "商品尺吋/大小", null, "EC_PRD_VAR_OPTION", true)
    ,SHIPPING("shipping", "運貨方式", null, "EC_SHIPPING", false)
    ,PAYMENT("payment", "付款方式", null, "EC_PAYMENT", false)
    ,CURRENCY("currency", "幣別", null, "EC_CURRENCY", false)
    ,TAX_TYPE("taxType", "稅務類型", null, "EC_TAX", false)
    ,SELLER_VENDOR("sellerVendor", "賣家商品供應商", null, "EC_VENDOR", true)
    
    // EC_OPTION
    ,PRD_UNIT("prdUnit", "商品計價單位", null, "EC_OPTION", true)
    ,PRD_BRAND("prdBrand", "商品品牌", null, "EC_OPTION", true)
    //,PAYMENT_GATWAY("paymentGateway", "付款機制", null, "EC_OPTION", false)
    ,CARD_TYPE("cardType", "信用卡類型", null, "EC_OPTION", false)
    //,PAY_THIRD_PARTY("payThirdParty", "第三方支付供應商", null, "EC_OPTION", false)
    ,WEIGHT_UNIT("weightUnit", "重量單位", null, "EC_OPTION", false)
    ,CUS_LEVEL("cusLevel", "客戶等級", null, "EC_OPTION", true)
    ,CUS_FEEDBACK("cusFeedback", "客戶意見類別", null, "EC_OPTION", true)
    ,SALES_AREA("salesArea", "銷售區域", null, "EC_OPTION", false)
    ,INDUSTRY("industry", "產業別", null, "EC_OPTION", false)
    ;
    
    private String code;
    private String name;
    private Class enumClass;
    private String table;
    private boolean store;// 區分店家
    
    OptionEnum(String code, String name, Class enumClass, String table, boolean store){
        this.code = code;
        this.name = name;
        this.enumClass = enumClass;
        this.table = table;
        this.store = store;
    }
    
    public static OptionEnum getFromCode(String code){
        for (OptionEnum enum1 : OptionEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static OptionEnum getFromEnum(Class clazz){
        for (OptionEnum enum1 : OptionEnum.values()) {
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
