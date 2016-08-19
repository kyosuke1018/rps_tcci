/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.deliveryplace;

import com.tcci.tccstore.entity.EcSalesarea;

/**
 *
 * @author Jimmy.Lee
 */
public class QueryFilter {
    private String codeName; // code name like
    private String province; // like
    private String city; // like
    private String district; // like
    private String town; // like
    private EcSalesarea ecSalesarea; // equal

    // getter, setter
    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
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

    public EcSalesarea getEcSalesarea() {
        return ecSalesarea;
    }

    public void setEcSalesarea(EcSalesarea ecSalesarea) {
        this.ecSalesarea = ecSalesarea;
    }
    
    
}
