/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.enums;

/**
 *
 * @author Kyle.Cheng
 */
public enum AccountTypeEnum {
    RE("RE", "個體方"),
    PA("PA", "對帳方");
    
    private String code;
    private String name;
    
    AccountTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static AccountTypeEnum getFromCode(String code){
        for (AccountTypeEnum enum1 : AccountTypeEnum.values()) {
            if (code.trim().equals(enum1.getCode())) {
                return enum1;
            }
        }
        return null; // default
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
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
    //</editor-fold>

}
