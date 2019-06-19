/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.rs;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class CusAddrRsVO extends BaseResponseVO implements Serializable {
    private Long deliveryId;
    private Long salesareaId;
    
    // for 送達地點
    private String province; // 省
    private String city; // 市
    private String district; // 區
    private String town; // 鎮

    private List<StrOptionVO> provinceList; // 省
    private List<StrOptionVO> cityList; // 市
    private List<StrOptionVO> districtList; // 區
    private List<StrOptionVO> townList; // 鎮

    // for 車號
    private List<StrOptionVO> carList;
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<StrOptionVO> getCarList() {
        return carList;
    }

    public void setCarList(List<StrOptionVO> carList) {
        this.carList = carList;
    }

    public Long getSalesareaId() {
        return salesareaId;
    }

    public void setSalesareaId(Long salesareaId) {
        this.salesareaId = salesareaId;
    }

    public List<StrOptionVO> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<StrOptionVO> provinceList) {
        this.provinceList = provinceList;
    }

    public List<StrOptionVO> getCityList() {
        return cityList;
    }

    public void setCityList(List<StrOptionVO> cityList) {
        this.cityList = cityList;
    }

    public List<StrOptionVO> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<StrOptionVO> districtList) {
        this.districtList = districtList;
    }

    public List<StrOptionVO> getTownList() {
        return townList;
    }

    public void setTownList(List<StrOptionVO> townList) {
        this.townList = townList;
    }
    
    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

}
