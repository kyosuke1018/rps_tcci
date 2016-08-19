/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync.delivery;

import com.tcci.storeadmin.facade.sync.SyncBaseVO;
import com.tcci.tccstore.entity.EcDeliveryPlace;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
public class DeliveryVO extends SyncBaseVO {

    private String code;
    @NotNull(message = "name不能為空")
    @Size(min=1, max = 100, message = "name至少1個字,最多100個字")
    private String name;
    @NotNull(message = "province不能為空")
    @Size(min=1, max = 20, message = "province至少1個字,最多20個字")
    private String province;
    @NotNull(message = "city不能為空")
    @Size(min=1, max = 20, message = "city至少1個字,最多20個字")
    private String city;
    @NotNull(message = "district不能為空")
    @Size(min=1, max = 20, message = "district至少1個字,最多20個字")
    private String district;
    @NotNull(message = "town不能為空")
    @Size(min=1, max = 20, message = "town至少1個字,最多20個字")
    private String town;
    private boolean active;
    
    private EcDeliveryPlace ecdp;

    // c'tor
    public DeliveryVO() {
    }

    public DeliveryVO(String code, String name) {
        this.code = code;
        this.name = name;
        this.active = true;
    }

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

    public EcDeliveryPlace getEcdp() {
        return ecdp;
    }

    public void setEcdp(EcDeliveryPlace ecdp) {
        this.ecdp = ecdp;
    }
    
}
