/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author Jason.Yu
 */
public enum SapClientEnum {
    SAP_TW("tw", "tcc", "台泥台灣", "台灣", "TW","510"),
    SAP_CN("cn", "tcc_cn", "台泥大陸", "大陸", "CN","520"),
    SAP_CSRC("csrc", "csrc", "中橡", "中橡", "TW","368"), // 中橡
    SAP_HPPC("hppc", "hppc", "和電", "和電", "TW","850"), // 和電
    SAP_TPCC("tpcc", "tpcc", "信昌", "信昌", "TW","830"); // 信昌
    
    private String code;
    private String sapClientCode;
    private String name;
    private String area;
    private String land;
    private String client;
    
    SapClientEnum(String code, String sapClientCode, String name, String area, String land,String client){
        this.code = code;
        this.sapClientCode = sapClientCode;
        this.name = name;
        this.area = area;
        this.land = land;
        this.client = client;
    }
    
    public static SapClientEnum getFromCode(String code){
        SapClientEnum[] sapClientEnums = SapClientEnum.values();
        for (SapClientEnum sapClientEnum : sapClientEnums) {
            if( sapClientEnum.getCode().equals(code) ) {
                return sapClientEnum;
            }
        }

        return SAP_TW; // default
    }

    public static SapClientEnum getFromSapClientCode(String sapClientCode){
        SapClientEnum[] sapClientEnums = SapClientEnum.values();
        for (SapClientEnum sapClientEnum : sapClientEnums) {
            if (sapClientCode.equals(sapClientEnum.getSapClientCode())) {
                return sapClientEnum;
            }
        }

        return SAP_TW; // default
    }
    
    public String getDisplayName(){
        return ResourceBundleUtil.getDisplayName( this.getClass().getSimpleName() , this.toString() );
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSapClientCode() {
        return sapClientCode;
    }

    public void setSapClientCode(String sapClientCode) {
        this.sapClientCode = sapClientCode;
    }
}
