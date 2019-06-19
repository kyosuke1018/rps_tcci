/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 資料類型
 * @author Peter.Pan    
 */
public enum DataTypeEnum {
    HTML("H", "自製網頁", true, true),
    UPLOAD("U", "上傳檔案", true, true),
    LINK("L", "連結", true, true),
    // TEXT("T", "文字", true, true),
    FOLDER("F", "資料夾", false, false)
    ;

    private String code;
    private String name;
    private boolean isDoc;
    private boolean fullIndex;// 支援全文檢索
    
    DataTypeEnum(String code, String name, boolean isDoc, boolean fullIndex){
        this.code = code;
        this.name = name;
        this.isDoc = isDoc;
        this.fullIndex = fullIndex;
    }
    
    public static List<DataTypeEnum> findDocList(){
        List<DataTypeEnum> list = new ArrayList<DataTypeEnum>();
        for (DataTypeEnum enum1 : DataTypeEnum.values()) {
            if( enum1.isDoc ) {
                list.add(enum1);
            }
        }
        return list;
    }
    
    public static DataTypeEnum getFromCode(String code){
        for (DataTypeEnum enum1 : DataTypeEnum.values()) {
            if( enum1.getCode().equals(code) ) {
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

    public boolean isFullIndex() {
        return fullIndex;
    }

    public void setFullIndex(boolean fullIndex) {
        this.fullIndex = fullIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}

