/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums.rs;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
public enum ResStatusEnum {
    SUCCESS(1, "成功")
    , FAIL(0, "服務執行失敗")
    
    , IN_ERROR(-11, "輸入參數錯誤!(資料型態不符或長度超過限制)")
    , IN_ERROR_EMPTY(-12, "未輸入必要參數!")
    , IN_ERROR_LOGIN(-13, "帳號/密碼輸入錯誤!")
    , IN_ERROR_NOT_EXIST(-14, "資料不存在或已被刪除!")
    , IN_ERROR_NOT_STORE(-15, "未指定商店!")
    , IN_ERROR_EXIST(-16, "已存在!")
    
    , TOKEN_EMPTY(-31, "無存取憑證!")
    , TOKEN_ERROR(-32, "存取憑證錯誤!")
    , TOKEN_EXPIRED(-33, "憑證過期!")
    
    , NO_PERMISSION(-41, "無執行此功能權限!")

    , IN_ERROR_EXISTS_CUS(-51, "此會員已是本商店客戶!")
    , IN_ERROR_NOT_EXIST_MEM(-52, "會員不存在!")
    , IN_ERROR_EXIST_MEM(-53, "會員帳號已存在!")
    
    , IN_ERROR_LIMIT(-61, "已超過系統限制!")
    ;

    private int code;
    private String name;
    
    ResStatusEnum(int code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static ResStatusEnum getFromCode(int code){
        for (ResStatusEnum enum1 : ResStatusEnum.values()) {
            if( enum1.getCode()==code ){
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
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
