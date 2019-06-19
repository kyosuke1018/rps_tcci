/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.enums;

import com.tcci.cm.model.global.GlobalConstant;
//import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;

/**
 * 支援語系
 *
 * @author Peter.pan
 */
public enum LocaleEnum {
    SIMPLIFIED_CHINESE("zh-CN", "簡體中文", "zh", "CN", Locale.SIMPLIFIED_CHINESE),
    TRADITIONAL_CHINESE("zh-TW", "繁體中文", "zh", "TW", Locale.TRADITIONAL_CHINESE),
//    ENGLISH("en", "英文", "en", "EN", Locale.ENGLISH),
    ENGLISH_US("en-US", "英文", "en", "US", Locale.US),
//    TURKISH("tr-TR", "土耳其文", "tr", "TR", new Locale("tr", "TR"));
    TURKISH("tr-TR", "土耳其文", "tr", "TR", Locale.forLanguageTag("tr-TR"));
//	TURKEY("tr-TR", "土耳其文", "tr", "TR", Locale.ENGLISH);// 因 Locale 無土耳其文，借用 ENGLISH(en) 設定

    private String code;// get from client browser
    private String name;
    private String language;
    private String country;
    private Locale locale;

    LocaleEnum(String code, String name, String language, String country, Locale locale) {
        this.code = code;
        this.name = name;
        this.language = language;
        this.country = country;
        this.locale = locale;
    }

    public static LocaleEnum getFromCode(String code) {
        return getFromCode(code, GlobalConstant.DEF_LOCALE);
    }

    public static LocaleEnum getFromCode(String code, LocaleEnum def) {
        for (LocaleEnum enum1 : LocaleEnum.values()) {
            if (code != null && enum1.getCode().equals(code)) {
                return enum1;
            }
        }
        return def;
    }

    /**
     * 顯示名稱 (取自enum.properties => [class name].[enum name])
     *
     * @return
     */
    public String getDisplayName() {
//        String res = ResourceBundleUtils.getDisplayName(GlobalConstant.ENUM_RESOURCE, this.getClass().getSimpleName() + "." + this.toString());
//        if (res == null) {
//            res = name;
//        }
//        return res;
        return name;
    }
    public String getDisplayName(Locale locale){
//        String res = ResourceBundleUtils.getDisplayName(locale, GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
//        if( res==null ){
//            res = name;
//        }
//        return res;
        return name;
    }

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>

}
