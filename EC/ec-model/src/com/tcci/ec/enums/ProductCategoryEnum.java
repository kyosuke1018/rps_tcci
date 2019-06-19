/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

public enum ProductCategoryEnum {

    Ａ("A", "建築材料"),
    Ｂ("B", "廚具衛浴"),
    Ｃ("C", "五金配件"),
    Ｄ("D", "家飾家具"),
    Ｅ("E", "照明燈飾"),
    Ｆ("F", "景觀美術");
    
    private String code;
    private String name;

    ProductCategoryEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static ProductCategoryEnum fromCode(String code) {
        if (code != null) {
            for (ProductCategoryEnum numericPatternEnum : ProductCategoryEnum.values()) {
                if (code.equals(numericPatternEnum.getCode())) {
                    return numericPatternEnum;
                }
            }
        }
        return null;
    }    
}
