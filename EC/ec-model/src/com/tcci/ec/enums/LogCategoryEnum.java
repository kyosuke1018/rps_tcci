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
public enum LogCategoryEnum {
    // APP功能
    ORDER_CREATE("ORDER_CREATE", "訂單成立", LogTypeEnum.FUNC_APP),
    ORDER_PAY("ORDER_PAY", "付款", LogTypeEnum.FUNC_APP),
    ORDER_SHIP("ORDER_SHIP", "出貨", LogTypeEnum.FUNC_APP),
    // 賣家功能
    HOME("home.html", "首頁", LogTypeEnum.FUNC_SELLER_WEB),
    MY("my.html", "個人資訊維護", LogTypeEnum.FUNC_SELLER_WEB),
    MY_PRD("myProducts.html", "商品資料維護", LogTypeEnum.FUNC_SELLER_WEB),
    MY_PRD_TYPE("myPrdType.html", "商品分類查詢", LogTypeEnum.FUNC_SELLER_WEB),
    IMP_PRD("importPrd.html", "批次商品匯入", LogTypeEnum.FUNC_SELLER_WEB),
    ORDER("orders.html", "訂單資訊維護", LogTypeEnum.FUNC_SELLER_WEB),
    RFQ("rfqs.html", "詢價單資訊維護", LogTypeEnum.FUNC_SELLER_WEB),
    CUSTOMER("customers.html", "客戶資訊維護", LogTypeEnum.FUNC_SELLER_WEB),
    MY_STORE("myStore.html", "商店資訊維護", LogTypeEnum.FUNC_SELLER_WEB),
    VENDOR("vendors.html", "供應商資訊維護", LogTypeEnum.FUNC_SELLER_WEB),
    NO_PAID("noPaid.html", "應收帳款資訊查詢", LogTypeEnum.FUNC_SELLER_WEB),

    // 管理者功能
    MEMBER("members.html", "會員管理", LogTypeEnum.FUNC_ADMIN_WEB),
    STORES("stores.html", "商店資訊維護", LogTypeEnum.FUNC_ADMIN_WEB),
    PRD_TYPE("prdType.html", "商品分類維護", LogTypeEnum.FUNC_ADMIN_WEB),
    PRODUCTS("products.html", "商品資料查詢", LogTypeEnum.FUNC_ADMIN_WEB),
    CAROUSEL("carousel.html", "首頁輪播圖示維護", LogTypeEnum.FUNC_ADMIN_WEB),
    HOT_PRD("hotPrd.html", "人氣商品維護", LogTypeEnum.FUNC_ADMIN_WEB),
    HOT_STORE("hotStore.html", "人氣商店維護", LogTypeEnum.FUNC_ADMIN_WEB),
    BULLETIN("bulletin.html", "系統公告維護", LogTypeEnum.FUNC_ADMIN_WEB)
    ;
    
    private String code;
    private String name;
    private LogTypeEnum type;
    
    LogCategoryEnum(String code, String name, LogTypeEnum type){
        this.code = code;
        this.name = name;
        this.type = type;
    }
    
    public static LogCategoryEnum getFromCode(String code){
        for (LogCategoryEnum enum1 : LogCategoryEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static LogCategoryEnum getFromUrl(String url){
        int i = url.lastIndexOf("/");
        if( i>=0 ){
            url = url.substring(i+1);
        }
        return getFromCode(url);
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

    public LogTypeEnum getType() {
        return type;
    }

    public void setType(LogTypeEnum type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>

}
