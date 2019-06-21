/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 * 授權等級
 * 
 * 等級數字越小，代表等級越高；從 1 開始。
 * 等級盡量勿跳號，避免影響 PermissionFacade.fetchFunctionInfo 效能
 * @author Peter.Pan
 */
public enum AuthLevelEnum {
    ALL("A", "完整功能", 1)
    ,VIEW("V", "僅檢視", 2)
    ;

    private String code;
    private String name;
    private int level;
    
    AuthLevelEnum(String code, String name, int level){
        this.code = code;
        this.name = name;
        this.level = level;
    }
    
    public static AuthLevelEnum getFromCode(String code){
        for (AuthLevelEnum enum1 : AuthLevelEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static AuthLevelEnum getByLevel(int level){
        for (AuthLevelEnum enum1 : AuthLevelEnum.values()) {
            if( enum1.getLevel()==level ) {
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
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}

