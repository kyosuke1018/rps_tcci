/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.enums;

/**
 * 特殊工廠群組分類
 * @author Peter.pan
 */
public enum FactoryGroupTypeEnum {
    COMMON("1", "權限公用群組");
    
    private String code;
    private String name;

    FactoryGroupTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static FactoryGroupTypeEnum fromCode(String code) {
        if (code != null) {
            for (FactoryGroupTypeEnum item : FactoryGroupTypeEnum.values()) {
                if (code.equals(item.getCode())) {
                    return item;
                }
            }
        }
        return null;
    }    
    
}
