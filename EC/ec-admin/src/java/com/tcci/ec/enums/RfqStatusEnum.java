/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
public enum RfqStatusEnum {
    Inquiry("Inquiry", "買方詢價"),
    Quotation("Quotation", "賣方報價"),
    Rejected("Rejected", "賣方拒絕報價"), // 賣方拒絕 詢價階段(未報價) 
    Deleted("Deleted", "刪除"),  //刪除(買家)
    ;

    private String code;
    private String name;
       
    RfqStatusEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static List<String> getCodes(){
        List<String> list = new ArrayList<String>();
        for (RfqStatusEnum enum1 : RfqStatusEnum.values()) {
            list.add(enum1.getCode());
        }
        return list;
    }

    public static RfqStatusEnum getFromCode(String code){
        for (RfqStatusEnum enum1 : RfqStatusEnum.values()) {
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

