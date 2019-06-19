/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;

public enum PayStatusEnum {

    A("A", "未付款"),
    B("B", "已收款"),
    C("C", "已收到部分款項"),
    D("D", "通知已付款");
    
    private String code;
    private String name;

    PayStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static PayStatusEnum fromCode(String code) {
        if (code != null) {
            for (PayStatusEnum thisEnum : PayStatusEnum.values()) {
                if (code.equals(thisEnum.getCode())) {
                    return thisEnum;
                }
            }
        }
        return null;
    }    
    
    public static PayStatusEnum getFromCode(String code){
        for (PayStatusEnum enum1 : PayStatusEnum.values()) {
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
    public String getDisplayName(Locale locale){
        String res = ResourceBundleUtils.getDisplayName(locale, GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
        if( res==null ){
            res = name;
        }
        return res;
    }
}
