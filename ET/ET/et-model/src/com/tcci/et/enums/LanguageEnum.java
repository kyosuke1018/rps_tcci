/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 *　語言別
 * @author Peter.Pan    
 */
public enum LanguageEnum {
    ALL("all", "跨語系", "A", true),
    TRANDITIONAL_CHINESE("tw", "繁體中文", "C", true),
    SIMPLIFIED_CHINESE("cn", "簡體中文", "S", false),
    ENGLISH("en", "英文", "E", true);

    private String code;
    private String name;
    private String shortCode;
    private boolean webSite;// 有此版本網站
    
    LanguageEnum(String code, String name, String shortCode, boolean webSite){
        this.code = code;
        this.name = name;
        this.shortCode = shortCode;
        this.webSite = webSite;
    }
    
    /**
     * 依簡碼條件產生 SQL (for restful)
     * @param colname
     * @param langs (= AE、AC or ...)
     * @param doAnd
     * @return 
     */
    public static String genLangCriteriaSQL(String colname, String langs, Map<String, Object> params){
        if( StringUtils.isBlank(colname) || StringUtils.isBlank(langs) ){
            return "";
        }
        if( langs.length()==1 ){// E, C, ...
            LanguageEnum langEnum = getFromShortCode(langs);
            if( langEnum==null ){
                return "";
            }
            return NativeSQLUtils.genEqualSQL(colname, langEnum.getShortCode(), params);
        }else{// AE, AC, ...
            List<String> langList = new ArrayList<String>();
            for(int i=0; i<langs.length(); i++){
                LanguageEnum langEnum = (i==langs.length()-1)?getFromShortCode(langs.substring(i)):getFromShortCode(langs.substring(i, i+1));
                if( langEnum!=null ){
                    langList.add(langEnum.getShortCode());
                }
            }
            if( langList.isEmpty() ){
                return "";
            }
            return NativeSQLUtils.getInSQL(colname, langList, params);
        }
    }
            
    public static LanguageEnum getFromCode(String code){
        for (LanguageEnum enum1 : LanguageEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static LanguageEnum getFromShortCode(String shortCode){
        if( shortCode==null ){
            return null;
        }
        for (LanguageEnum enum1 : LanguageEnum.values()) {
            if( enum1.getShortCode().equals(shortCode) ) {
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

    public boolean isWebSite() {
        return webSite;
    }

    public void setWebSite(boolean webSite) {
        this.webSite = webSite;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}

