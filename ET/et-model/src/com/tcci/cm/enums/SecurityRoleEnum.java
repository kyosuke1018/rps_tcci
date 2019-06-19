/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 * 系統角色 (TC_GROUP 中會影響畫面與流程細部控制的角色)
 * 
 * @author peter.pan
 */
public enum SecurityRoleEnum {
    ADMINISTRATORS("ADMINISTRATORS", "系統管理者") 
    ,CSRC_USER("CSRC_USER", "中橡人員")
    ,CM_USER("CM_USER", "網站內容維護人員")
    ,MGN_USER("MGN_USER", "中橡網站系統管理人員")
    ;

    private String code;
    private String name;
    
    SecurityRoleEnum(String code, String name){
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
    
    public static SecurityRoleEnum getFromCode(String code){
        for (SecurityRoleEnum enum1 : SecurityRoleEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
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
