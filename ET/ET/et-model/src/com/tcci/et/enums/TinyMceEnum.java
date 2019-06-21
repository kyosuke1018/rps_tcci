/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 *　TinyMce Type
 * @author Peter.Pan    
 */
public enum TinyMceEnum {
    PHOTOGALLERY("P", "自圖庫取圖插入"),
    UPLOADIMAGE("U", "上傳圖片插入"),
    LINK("L", "插入網址"),
    VIDEO("V", "插入影片");

    private String code;
    private String name;
    
    TinyMceEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static TinyMceEnum getFromCode(String code){
        for (TinyMceEnum enum1 : TinyMceEnum.values()) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}

