/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;


/**
 * 通知類型
 * @author Peter.pan
 */
public enum BooleanTypeEnum {
    NONE("", "否", null, ""), // 未設定
    YES("1", "是", Boolean.TRUE, "Y"),
    NO("0", "否", Boolean.FALSE, "N");
    
    private String code;
    private String name;
    private Boolean value;
    private String yn;
    
    BooleanTypeEnum(String code, String name, Boolean value, String yn){
        this.code = code;
        this.name = name;
        this.value = value;
        this.yn = yn;
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
    
    public static BooleanTypeEnum getFromCode(String code){
        for (BooleanTypeEnum enum1 : BooleanTypeEnum.values()) {
            if(code!=null && enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static BooleanTypeEnum getFromYN(String yn){
        for (BooleanTypeEnum enum1 : BooleanTypeEnum.values()) {
            if (yn!=null && yn.equals(enum1.getYn())) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static BooleanTypeEnum getFromValue(Boolean value){
        if( value==null ){
            return BooleanTypeEnum.NONE;
        }else{
            for (BooleanTypeEnum enum1 : BooleanTypeEnum.values()) {
                if( value.equals(enum1.isValue()) ) {
                    return enum1;
                }
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

    public String getYn() {
        return yn;
    }

    public void setYn(String yn) {
        this.yn = yn;
    }

    public Boolean isValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>

}
