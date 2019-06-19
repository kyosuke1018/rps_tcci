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
 * 商品狀態
 * @author Peter.pan
 */
public enum ProductStatusEnum {
    DRAF("D", "草稿", true, false, true, false, false),
    
    REJECT("AR", "審核不通過", false, true, true, false, false),
    APPLY("A", "申請上架審核", true, false, false, false, false),
    PASS("AP", "審核通過", false, true, true, true, true),
    
    RESERVED("S", "預約上架", true, false, false, true, true),
    PUBLISH("P", "已上架", false, false, false, false, true),
    REMOVE("R", "已下架", true, false, false, false, false);

    private String code;
    private String name;
    private boolean forSeller;// 賣家商品維護選單可出出現狀態
    private boolean forAdmin;// 管理這商品維護選單可出出現狀態
    private boolean canApply;// 可送審
    private boolean canPublish;// 可預約
    private boolean reapprove;// 此狀態變更商品資料要重審

    ProductStatusEnum(String code, String name, 
            boolean forSeller, boolean forAdmin, 
            boolean canApply, boolean canPublish, boolean reapprove){
        this.code = code;
        this.name = name;
        this.forSeller = forSeller;
        this.forAdmin = forAdmin;
        this.canApply = canApply;
        this.canPublish = canPublish;
        this.reapprove = reapprove;
    }
    
    public static ProductStatusEnum getFromCode(String code){
        for (ProductStatusEnum enum1 : ProductStatusEnum.values()) {
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

    public boolean isForSeller() {
        return forSeller;
    }

    public boolean isReapprove() {
        return reapprove;
    }

    public void setReapprove(boolean reapprove) {
        this.reapprove = reapprove;
    }

    public void setForSeller(boolean forSeller) {
        this.forSeller = forSeller;
    }

    public boolean isForAdmin() {
        return forAdmin;
    }

    public void setForAdmin(boolean forAdmin) {
        this.forAdmin = forAdmin;
    }

    public boolean isCanApply() {
        return canApply;
    }

    public void setCanApply(boolean canApply) {
        this.canApply = canApply;
    }

    public boolean isCanPublish() {
        return canPublish;
    }

    public void setCanPublish(boolean canPublish) {
        this.canPublish = canPublish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
    
}
