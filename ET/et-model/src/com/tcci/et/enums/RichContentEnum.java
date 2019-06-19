/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 * 文章類型
 * @author Peter.Pan    
 */
public enum RichContentEnum {
    RECORD("R", "保種紀錄"),
    STORE("S", "植物的故事"),
    TENDER("T", "招標公告"),
    PUBLICATION("P", "網站刊登項目")
    ;

    private String code;
    private String name;
    
    RichContentEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static RichContentEnum getFromCode(String code){
        for (RichContentEnum enum1 : RichContentEnum.values()) {
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
