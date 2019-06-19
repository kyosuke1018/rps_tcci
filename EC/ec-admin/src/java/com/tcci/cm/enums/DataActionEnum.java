/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 * 資料操作類型
 * 
 * @author peter.pan
 */
public enum DataActionEnum {
    CREATE("C", "新增"),
    READ("R", "讀取"),
    UPDATE("U", "修改"),  
    DELETE("D", "刪除");
    
    private String code;
    private String name;
    
    DataActionEnum(String code, String name){
        this.code = code;
        this.name = name;
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
    
    public static DataActionEnum getFromCode(String code){
        for (DataActionEnum enum1 : DataActionEnum.values()) {
            if( enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
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
