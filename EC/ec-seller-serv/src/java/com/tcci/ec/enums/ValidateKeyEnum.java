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
 * 單一欄位為 unikey
 * @author Peter.pan
 */
public enum ValidateKeyEnum {
    // (code, label, global, storeOnly, productOnly, typeOnly, parentOnly)
    EC_MEMBER_LOGIN_ACCOUNT("EC_MEMBER.LOGIN_ACCOUNT", "會員帳號", true, false, false, false, false)
    
    , EC_OPTION_CNAME("EC_OPTION.CNAME", "中文名稱", false, true, false, false, true)
    , EC_OPTION_ENAME("EC_OPTION.ENAME", "英文名稱", false, true, false, false, true)
    
    , EC_ORDER_ORDER_NUMBER("EC_ORDER.ORDER_NUMBER", "訂單編號", true, false, false, false, false)
    
    , EC_PAYMENT_TITLE("EC_PAYMENT.TITLE", "名稱", false, true, false, false, false)
    
    , EC_PRD_ATTR_CNAME("EC_PRD_ATTR.CNAME", "屬性名稱", false, true, true, true, false)
    
    , EC_PRD_TYPE_CNAME("EC_PRD_TYPE.CNAME", "分類名稱", false, true, false, false, true)
    
    , EC_PRD_VARIANT_CNAME("EC_PRD_VARIANT.CNAME", "商品型別名稱", false, true, true, false, false)
    
    , EC_PRD_VAR_OPTION_TYPE("EC_PRD_VAR_OPTION.TYPE", "型別類型", false, true, true, true, false)
    
    , EC_PRICE_RULE_TITLE("EC_PRICE_RULE.TITLE", "標題", false, true, false, false, false)
    
    , EC_PRODUCT_CNAME("EC_PRODUCT.CNAME", "商品名稱", false, true, false, false, false)
    , EC_PRODUCT_ENAME("EC_PRODUCT.ENAME", "英文名稱", false, true, false, false, false)
    //, EC_PRODUCT_CODE("EC_PRODUCT.CODE", "代碼", false, true, false, false, false)
    //, EC_PRODUCT_BARCODE("EC_PRODUCT.BARCODE", "BAR CODE", false, true, false, false, false)
    //, EC_PRODUCT_SKU("EC_PRODUCT.SKU", "庫存單位(Stock Keeping Unit)", false, true, false, false, false)
    
    , EC_SHIPPING_TITLE("EC_SHIPPING.TITLE", "名稱", false, true, false, false, false)

    , EC_STORE_CNAME("EC_STORE.CNAME", "中文名稱", true, false, false, false, false)
    , EC_STORE_ENAME("EC_STORE.ENAME", "英文名稱", true, false, false, false, false)
    
    , EC_VENDOR_CNAME("EC_VENDOR.CNAME", "中文名稱", false, true, false, false, false)
    , EC_VENDOR_ENAME("EC_VENDOR.ENAME", "英文名稱", false, true, false, false, false)
    ;

    private String code;
    private String label;
    private boolean global;
    private boolean storeOnly;// 同一商店
    private boolean productOnly;// 同一商品
    private boolean typeOnly;// 同一類別
    private boolean parentOnly;// 同一上層
    
    ValidateKeyEnum(String code, String label, boolean global, boolean storeOnly, 
            boolean productOnly, boolean typeOnly, boolean parentOnly){
        this.code = code;
        this.label = label;
        this.global = global;
        this.storeOnly = storeOnly;
        this.productOnly = productOnly;
        this.typeOnly = typeOnly;
        this.parentOnly = parentOnly;
    }
    public static ValidateKeyEnum getFromCode(String code){
        for (ValidateKeyEnum enum1 : ValidateKeyEnum.values()) {
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
            res = label;
        }
        return res;
    }
    public String getDisplayName(Locale locale){
        String res = ResourceBundleUtils.getDisplayName(locale, GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
        if( res==null ){
            res = label;
        }
        return res;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isParentOnly() {
        return parentOnly;
    }

    public void setParentOnly(boolean parentOnly) {
        this.parentOnly = parentOnly;
    }

    public boolean isStoreOnly() {
        return storeOnly;
    }

    public void setStoreOnly(boolean storeOnly) {
        this.storeOnly = storeOnly;
    }

    public boolean isProductOnly() {
        return productOnly;
    }

    public void setProductOnly(boolean productOnly) {
        this.productOnly = productOnly;
    }

    public boolean isTypeOnly() {
        return typeOnly;
    }

    public void setTypeOnly(boolean typeOnly) {
        this.typeOnly = typeOnly;
    }

    
}
