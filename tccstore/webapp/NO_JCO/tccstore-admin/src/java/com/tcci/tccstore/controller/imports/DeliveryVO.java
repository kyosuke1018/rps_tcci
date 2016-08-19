/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcDeliveryVkorg;
import com.tcci.tccstore.entity.EcSalesarea;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jimmy.Lee
 */
public class DeliveryVO extends ExcelVOBase {

    private String code;
    private String name;
    private String province;
    private String city;
    private String district;
    private String town;
    private boolean active;
    private String salesarea;
    private String vksas; // vkorg1:salesarea1,vkorg2:salesarea2,...

    private EcDeliveryPlace entity;
    private EcSalesarea ecSalesarea;
    private List<EcDeliveryVkorg> ecVksas;

    private List<EcDeliveryVkorg> delVkorgs;
    private List<EcDeliveryVkorg> updVkorgs;
    private Map<String, String> insVkorgs;

    // getter, setter
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSalesarea() {
        return salesarea;
    }

    public void setSalesarea(String salesarea) {
        this.salesarea = salesarea;
    }

    public EcDeliveryPlace getEntity() {
        return entity;
    }

    public void setEntity(EcDeliveryPlace entity) {
        this.entity = entity;
    }

    public EcSalesarea getEcSalesarea() {
        return ecSalesarea;
    }

    public void setEcSalesarea(EcSalesarea ecSalesarea) {
        this.ecSalesarea = ecSalesarea;
    }

    public String getVksas() {
        return vksas;
    }

    public void setVksas(String vksas) {
        this.vksas = vksas;
    }

    public List<EcDeliveryVkorg> getEcVksas() {
        return ecVksas;
    }

    public void setEcVksas(List<EcDeliveryVkorg> ecVksas) {
        this.ecVksas = ecVksas;
    }

    public List<EcDeliveryVkorg> getDelVkorgs() {
        return delVkorgs;
    }

    public void setDelVkorgs(List<EcDeliveryVkorg> delVkorgs) {
        this.delVkorgs = delVkorgs;
    }

    public List<EcDeliveryVkorg> getUpdVkorgs() {
        return updVkorgs;
    }

    public void setUpdVkorgs(List<EcDeliveryVkorg> updVkorgs) {
        this.updVkorgs = updVkorgs;
    }

    public Map<String, String> getInsVkorgs() {
        return insVkorgs;
    }

    public void setInsVkorgs(Map<String, String> insVkorgs) {
        this.insVkorgs = insVkorgs;
    }

}
