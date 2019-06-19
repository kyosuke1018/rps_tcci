/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class CusAddrVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long memberId;
    private String address;
    private String address2;
    private String type;
    private String alias;
    private String company;
    private String city;
    private String stateRegion;
    private String country;
    private String postalCode;
    private String fax;
    private String phone;
    private String phoneExtension;
    private Boolean primary;
    private String recipient;

    private Long storeId;
    private String carNo;
    private BigDecimal patrolLatitude;
    private BigDecimal patrolLongitude;
    private Long deliveryId;

    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;
    
    // for 送達地點
    private String province; // 省
    private String cityEC10; // 市
    private String district; // 區
    private String town; // 鎮
    private Long salesareaId;

    public String getDeliveryPlaceLabel(){
        StringBuilder sb = new StringBuilder();
        if( province!=null ){ sb.append(province); }
        if( cityEC10!=null ){ sb.append(cityEC10); }
        if( district!=null ){ sb.append(district); }
        if( town!=null ){ sb.append(town); }
        
        return sb.toString();
    }
    
    public CusAddrVO() {
    }

    public CusAddrVO(Long id) {
        this.id = id;
    }

    public CusAddrVO(Long id, Boolean primary) {
        this.id = id;
        this.primary = primary;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityEC10() {
        return cityEC10;
    }

    public void setCityEC10(String cityEC10) {
        this.cityEC10 = cityEC10;
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

    public Long getSalesareaId() {
        return salesareaId;
    }

    public void setSalesareaId(Long salesareaId) {
        this.salesareaId = salesareaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateRegion() {
        return stateRegion;
    }

    public void setStateRegion(String stateRegion) {
        this.stateRegion = stateRegion;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public BigDecimal getPatrolLatitude() {
        return patrolLatitude;
    }

    public void setPatrolLatitude(BigDecimal patrolLatitude) {
        this.patrolLatitude = patrolLatitude;
    }

    public BigDecimal getPatrolLongitude() {
        return patrolLongitude;
    }

    public void setPatrolLongitude(BigDecimal patrolLongitude) {
        this.patrolLongitude = patrolLongitude;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CusAddrVO)) {
            return false;
        }
        CusAddrVO other = (CusAddrVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.CusAddrVO[ id=" + id + " ]";
    }
    
}
