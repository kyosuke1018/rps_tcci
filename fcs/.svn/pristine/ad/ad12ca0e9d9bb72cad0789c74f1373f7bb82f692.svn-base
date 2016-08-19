/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.enums;

/**
 *
 * @author Greg.Chou
 */
public enum NumericPatternEnum {

    Digits2("#,##0.00", "2位小数"),
    Digits3("#,##0.000", "3位小数"),
    Integer("###0", "單號/項目");
    
    private String code;
    private String name;

    NumericPatternEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static NumericPatternEnum fromCode(String code) {
        if (code != null) {
            for (NumericPatternEnum numericPatternEnum : NumericPatternEnum.values()) {
                if (code.equals(numericPatternEnum.getCode())) {
                    return numericPatternEnum;
                }
            }
        }
        return null;
    }    
}
