/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.enums;

/**
 *
 * @author Greg.Chou
 */
public enum NumericPatternEnum {

    Digits0("#,##0", "#,##0_);(#,##0)", "整数"),
    Digits2("#,##0.00", "#,##0.00_);(#,##0.00)", "2位小数"),
    Digits3("#,##0.000", "#,##0.000_);(#,##0.000)", "3位小数"),
    Digits3_General("#,##0:#,##0.###", "", "3位小数不補零"),// G/通用格式
    Digits7("#,##0.000####", "#,##0.000####_);(#,##0.000####)", "最多7位小数"),
    DigitsCode("###0", "###0_);(###0)", "單號/項目");
    
    private String code;
    private String xlsFormat;
    private String name;

    NumericPatternEnum(String code, String xlsFormat, String name) {
        this.code = code;
        this.name = name;
        this.xlsFormat = xlsFormat;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
    
    public String getXlsFormat() {
        return this.xlsFormat;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setXlsFormat(String xlsFormat) {
        this.xlsFormat = xlsFormat;
    }

    public void setName(String name) {
        this.name = name;
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
