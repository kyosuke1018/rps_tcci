/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.solr.client.enums;

/**
 * Solr Transaction Type
 * @author Peter
 */
public enum SolrTransactionEnum {
    CREATE("C", "建立"),
    UPDATE("U", "修改"),
    DELETE("D", "刪除");
    
    private String code;
    private String name;
    
    SolrTransactionEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public static SolrTransactionEnum getFromCode(String code){
        for (SolrTransactionEnum enum1 : SolrTransactionEnum.values()) {
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
