/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.vo;

/**
 *
 * @author Kyle.Cheng
 */
public class DeliveryPlace {
    private Long id;
    private String code;
    private String name;
    private String province;//省
    private String city;//市
    private String district;//區
    private String town;//鎮
    private Long salesareaId;//EC_SALESAREA.ID
    private String salesareaCode;//EC_SALESAREA.CODE

    // getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getSalesareaId() {
        return salesareaId;
    }

    public void setSalesareaId(Long salesareaId) {
        this.salesareaId = salesareaId;
    }

    public String getSalesareaCode() {
        return salesareaCode;
    }

    public void setSalesareaCode(String salesareaCode) {
        this.salesareaCode = salesareaCode;
    }

}
