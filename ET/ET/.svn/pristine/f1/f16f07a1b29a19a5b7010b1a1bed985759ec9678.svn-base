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
    SAP_CN("cn", "tcc_cn", "台泥大陸", "大陸", "CN", "0A", false)
    ,SAP_TW("tw", "tcc", "台泥台灣", "台灣", "TW", "0A", true)
    ,SAP_CSRC("csrc", "csrc", "中橡", "中橡", "TW", "0A", false) // 中橡
    //,SAP_HPPC("hppc", "hppc", "和電", "和電", "TW") // 和電
    //,SAP_TPCC("tpcc", "tpcc", "信昌", "信昌", "TW") // 信昌
    ;
    
    private String code;
    private String sapClientCode;
    private String name;
    private String area;
    private String land;
    private String tenderCodePrefix;
    private boolean multiPlantRfq;// 單一詢價單可多廠別
    
    SapClientEnum(String code, String sapClientCode, String name, String area, String land, String tenderCodePrefix, boolean multiPlantRfq){
        this.code = code;
        this.sapClientCode = sapClientCode;
        this.name = name;
        this.area = area;
        this.land = land;
        this.tenderCodePrefix = tenderCodePrefix;
        this.multiPlantRfq = multiPlantRfq;
    }
    
    public static SapClientEnum getFromCode(String code){
        SapClientEnum[] sapClientEnums = SapClientEnum.values();
        for (SapClientEnum sapClientEnum : sapClientEnums) {
            if( code!=null && sapClientEnum.getCode().equals(code) ) {
                return sapClientEnum;
            }
        }

        return null;
    }

    public static SapClientEnum getFromSapClientCode(String sapClientCode){
        SapClientEnum[] sapClientEnums = SapClientEnum.values();
        for (SapClientEnum sapClientEnum : sapClientEnums) {
            if (sapClientCode.equals(sapClientEnum.getSapClientCode())) {
                return sapClientEnum;
            }
        }

        return null;
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

    public String getTenderCodePrefix() {
        return tenderCodePrefix;
    }

    public void setTenderCodePrefix(String tenderCodePrefix) {
        this.tenderCodePrefix = tenderCodePrefix;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public boolean isMultiPlantRfq() {
        return multiPlantRfq;
    }

    public void setMultiPlantRfq(boolean multiPlantRfq) {
        this.multiPlantRfq = multiPlantRfq;
    }

    public String getSapClientCode() {
        return sapClientCode;
    }

    public void setSapClientCode(String sapClientCode) {
        this.sapClientCode = sapClientCode;
    }
}
