/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

/**
 *
 * @author Kyle.Cheng
 */
public enum FormTypeEnum {
    M_V("M_V", "會員申請舊商"), 
    M_NV("M_NV", "會員申請新商"), 
    M_I("M_I", "會員基本資料變更"),
    V_C("V_C", "供應商類別變更");
    
    
    private String code;
    private String name;

    FormTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public static FormTypeEnum getFromCode(String code) {
        for (FormTypeEnum enum1 : FormTypeEnum.values()) {
            if (code!=null && code.equals(enum1.getCode())) {
                return enum1;
            }
        }
        return null; // default
    }

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



}
