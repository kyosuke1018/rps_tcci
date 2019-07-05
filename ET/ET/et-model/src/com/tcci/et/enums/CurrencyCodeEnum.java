/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.math.BigDecimal;

/**
 *
 * @author Peter.Pan    
 */
public enum CurrencyCodeEnum {
    //幣別代碼
    TWD("TWD", "台幣", 0, BigDecimal.ROUND_HALF_UP),
    RMB("RMB", "人民幣", 2, BigDecimal.ROUND_HALF_UP),
    USD("USD", "美金", 4, BigDecimal.ROUND_HALF_UP),
    JPY("JPY", "日幣", 0, BigDecimal.ROUND_HALF_UP),
    EUR("EUR", "歐元", 4, BigDecimal.ROUND_HALF_UP);
    
    private String code;
    private String name;
    private int scale;
    private int roundingMode;
    
    CurrencyCodeEnum(String code, String name, int scale, int roundingMode){
        this.code = code;
        this.name = name;
        this.scale = scale;
        this.roundingMode = roundingMode;
    }
    
    public static CurrencyCodeEnum getFromCode(String code){
        for (CurrencyCodeEnum enum1 : CurrencyCodeEnum.values()) {
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

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getRoundingMode() {
        return roundingMode;
    }

    public void setRoundingMode(int roundingMode) {
        this.roundingMode = roundingMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}
