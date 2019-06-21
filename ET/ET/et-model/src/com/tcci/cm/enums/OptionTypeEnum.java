/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 *　
 * @author Peter.Pan
 */
public enum OptionTypeEnum {
    AREA("area", "銷售區域", 1)
    ,TENDER_CAT("tenderCategory", "標案類別", 1)
    ,INDUSTRY("industry", "產業別", 1)
    ,CURRENCY("currency", "幣別", 1)
    ,COUNTRY("country", "國別", 1)
    ;

    private String code;
    private String name;
    private int category;
    
    OptionTypeEnum(String code, String name, int category){
        this.code = code;
        this.name = name;
        this.category = category;
    }
    
    public static OptionTypeEnum getFromCode(String code){
        for (OptionTypeEnum enum1 : OptionTypeEnum.values()) {
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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}

