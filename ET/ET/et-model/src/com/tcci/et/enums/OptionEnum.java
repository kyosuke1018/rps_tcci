/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;

/**
 * ET_OPTION.TYPE
 * @author Peter.pan
 */
public enum OptionEnum {
    // enums
    
    // 獨立 Table
    FACTORYS("factorys", "廠別", null, "CM_FACTORY", 1)
    ,AREA("area", "招標地區", null, "ET_OPTION", 1)
    ,TENDER_CATEGORY("tenderCategory", "類別", null, "ET_OPTION", 1)
    ,COMPANY_TYPE("companyType", "供貨商類別", null, "ET_OPTION", 1)
    ,COMPANY_CATEGORY("companyCategory", "供應商行業別", null, "ET_OPTION", 1)
    ,COUNTRY("country", "國別", null, "TC_ZTAB_EXP_T005T", 1)
    ,CURRENCY("currency", "幣別", null, "TC_ZTAB_EXP_TCURT", 1)
    ,INDUSTRY("industry", "幣別", null, "ET_OPTION", 1)
    ;
    
    private String code;
    private String name;
    private Class enumClass;
    private String table;
    private int category;
    
    OptionEnum(String code, String name, Class enumClass, String table, int category){
        this.code = code;
        this.name = name;
        this.enumClass = enumClass;
        this.table = table;
        this.category = category;
    }
    
    public static OptionEnum getFromCode(String code){
        for (OptionEnum enum1 : OptionEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static OptionEnum getFromEnum(Class clazz){
        for (OptionEnum enum1 : OptionEnum.values()) {
            if( clazz!=null && enum1.getEnumClass()==clazz ) {
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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Class getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class enumClass) {
        this.enumClass = enumClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
    
}
