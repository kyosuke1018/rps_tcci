/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 * 
 * @author Peter
 */
public enum ActivityLogEnum {
    D_USER("S1", "刪除使用者", "TC_USER"),
    U_FUNC_PERMISSION("S2", "變更功能權限", "TC_USERGROUP"),
    U_ORG_PERMISSION("S3", "變更組織權限", "TC_USER"), // CM_USER_ORG
    U_PLANT_PERMISSION("S5", "變更廠別權限", "CM_USERFACTORY"),
    U_USER_IMPORT("S4", "匯入使用者", "TC_USER"),
    E_INTERNET_ACCESS("E1", "外部網路登入", ""),
    
    C_DEALER("A1", "建立經銷商", "EC_MEMBER"),
    U_DEALER("A2", "修改經銷商", "EC_MEMBER"),
    D_DEALER("A3", "刪除經銷商", "EC_MEMBER"),
    
    C_DOWNSTREAM("A1", "建立下游客戶", "EC_MEMBER"),
    U_DOWNSTREAM("A2", "修改下游客戶", "EC_MEMBER"),
    D_DOWNSTREAM("A3", "刪除下游客戶", "EC_MEMBER"),
    ;
    
    private String code;
    private String name;
    private String table;// 主要變更 Table
    
    ActivityLogEnum(String code, String name, String table){
        this.code = code;
        this.name = name;
        this.table = table;
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
    
    /**
     * 依代碼取得 ActivityLogEnum
     * @param code
     * @return 
     */
    public static ActivityLogEnum getFromCode(String code){
        for (ActivityLogEnum enum1 : ActivityLogEnum.values()) {
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

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>

}
