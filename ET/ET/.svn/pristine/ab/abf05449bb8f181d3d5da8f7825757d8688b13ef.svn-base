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
 *
 * @author Peter.pan
 */
public enum ValidateNumEnum {
    EC_CUSTOMER_CREDITS("EC_CUSTOMER.CREDITS", "信用額度", true, true, true, 0L, 999999999999L)// 千億
    , EC_CUSTOMER_EXPECTED_CREDITS("EC_CUSTOMER.EXPECTED_CREDITS", "期望信用額度", true, true, true, 0L, 999999999999L)// 千億
    ;
    
    private String code;
    private String label;
    private boolean nullable;
    private boolean canZero;
    private boolean positiveOnly;
    private Long min;
    private Long max;
    
    ValidateNumEnum(String code, String label, boolean nullable, boolean canZero, boolean positiveOnly, Long min, Long max){
        this.code = code;
        this.label = label;
        this.nullable = nullable;
        this.canZero = canZero;
        this.positiveOnly = positiveOnly;
        this.min = min;
        this.max = max;
    }
    
    public static ValidateNumEnum getFromCode(String code){
        for (ValidateNumEnum enum1 : ValidateNumEnum.values()) {
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
            res = label;
        }
        return res;
    }
    public String getDisplayName(Locale locale){
        String res = ResourceBundleUtils.getDisplayName(locale, GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
        if( res==null ){
            res = label;
        }
        return res;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isPositiveOnly() {
        return positiveOnly;
    }

    public void setPositiveOnly(boolean positiveOnly) {
        this.positiveOnly = positiveOnly;
    }

    public Long getMin() {
        return min;
    }

    public void setMin(Long min) {
        this.min = min;
    }

    public boolean isCanZero() {
        return canZero;
    }

    public void setCanZero(boolean canZero) {
        this.canZero = canZero;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }
}
