/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs.enums;

import com.tcci.fc.util.ResourceBundleUtils;
import com.tcci.cm.model.global.GlobalConstant;

/**
 *
 * @author Peter.pan
 */
public enum ResStatusEnum {
    SUCCESS(1, "成功")
    , FAIL(0, "失敗")
    
    , IN_ERROR(-11, "輸入參數錯誤!(資料型態不符或長度超過限制)")
    , IN_ERROR_EMPTY(-12, "未輸入必要參數!")
    , IN_ERROR_LOGIN(-13, "帳號/密碼輸入錯誤!")
    , IN_ERROR_NOT_EXIST(-14, "資料不存在或已被刪除!")
    
    , LOGIN_AD_FAIL(-21, "AD登入失敗!")
    , LOGIN_KBCC_FAIL(-22, "KBCC登入失敗!")
    
    , TOKEN_EMPTY(-31, "無存取憑證!")
    , TOKEN_ERROR(-32, "存取憑證錯誤!")
    , TOKEN_EXPIRED(-33, "憑證過期!")
    
    , DENIED_PLANT(-41, "無此類植物編輯權限!")
    , DENIED_FUNC(-42, "無此項功能執行權限!")
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
