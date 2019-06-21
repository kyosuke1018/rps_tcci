/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 * 計數類別
 * @author Peter.Pan    
 */
public enum ActionEnum {
    CHOICE("C", "選取", false),
    QUERY("Q", "查詢", false),
    VIEW("V", "檢視", false),
    ADD("A", "新增", true),
    MODIFY("M", "修改", true),
    DELETE("D", "刪除", true),
    ADDSUB("S", "建立子項目", true),
    UPLOAD("U", "上傳", true),
    EDITUPLOAD("E", "編輯上傳資訊", true)
    ;

    private String code;
    private String name;
    private boolean editMode;
    
    ActionEnum(String code, String name, boolean editMode){
        this.code = code;
        this.name = name;
        this.editMode = editMode;
    }
    
    public static ActionEnum getFromCode(String code){
        for (ActionEnum enum1 : ActionEnum.values()) {
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
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}

