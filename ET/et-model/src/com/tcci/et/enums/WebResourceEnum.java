/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 *　
 * @author Peter.Pan
 */
public enum WebResourceEnum {
    DOC_UPLOAD("U", "上傳檔案")
    ,DOC_LINK("L", "網址連結")
    ,DOC_HTML("H", "自製網頁")
    ,IMAGE("I", "圖片")
    ,VIDEO("V", "影片")
    ;

    private String code;
    private String name;
    
    WebResourceEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static WebResourceEnum getFromCode(String code){
        for (WebResourceEnum enum1 : WebResourceEnum.values()) {
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

