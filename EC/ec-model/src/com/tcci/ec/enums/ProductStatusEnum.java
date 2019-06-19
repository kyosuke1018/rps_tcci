/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

//import com.tcci.cm.model.global.GlobalConstant;

import java.util.Locale;

//import com.tcci.fc.util.ResourceBundleUtils;

/**
 *
 * @author Peter.pan
 */
public enum ProductStatusEnum {
    DRAF("D", "草稿", true),
    RESERVED("S", "已預約上架", true),
    PUBLISH("P", "已上架", false),
    REMOVE("R", "已下架", false);
    //OUT_OF_STOCK("O", "缺貨中");

    private String code;
    private String name;
    private boolean canReserve;

    ProductStatusEnum(String code, String name, boolean canReserve){
        this.code = code;
        this.name = name;
        this.canReserve = canReserve;
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

    public boolean isCanReserve() {
        return canReserve;
    }

    public void setCanReserve(boolean canReserve) {
        this.canReserve = canReserve;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
    
}
