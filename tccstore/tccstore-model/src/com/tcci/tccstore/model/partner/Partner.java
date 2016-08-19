/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.model.partner;

import com.tcci.tccstore.model.goods.Goods;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Neo.Fu
 */
public class Partner {

    private Long id;
    private String name;
    private String description;
    private String phone;
    private String social;
    private String theme;
    private double averageRate;
    private String province;
    private String city;
    private String district;
    private String town;
    private String contact;
    private String address;
    private String status;
    private Date createtime;
    private boolean active;
    private String member;
    private String message;
    private String imageUrl;
    // private List<PartnerProduct> partnerProductList;
    private List<Goods> goodsList;
    private List<PartnerComment> comments;

    public Partner() {
    }

    public Partner(Long id) {
        this.id = id;
    }

    public Partner(Long id, String name, String status, Date createtime, boolean active) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createtime = createtime;
        this.active = active;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(double averageRate) {
        this.averageRate = averageRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

//    public List<PartnerProduct> getPartnerProductList() {
//        return partnerProductList;
//    }
//
//    public void setPartnerProductList(List<PartnerProduct> partnerProductList) {
//        this.partnerProductList = partnerProductList;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public List<PartnerComment> getComments() {
        return comments;
    }

    public void setComments(List<PartnerComment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Partner["
                + "id=" + id + "\n"
                + "name=" + name + "\n"
                + "description=" + description + "\n"
                + "phone=" + phone + "\n"
                + "social=" + social + "\n"
                + "province=" + province + "\n"
                + "city=" + city + "\n"
                + "address=" + address + "\n"
                + "averageRate=" + averageRate + "\n"
                + "theme=" + theme + "\n"
                + "status=" + status + "\n"
                + "member=" + member + "\n"
                + "active=" + active + "\n"
                // + "productList="+(partnerProductList!=null?"List(" + partnerProductList.size()+" product(s)": "null") + "\n"
                + "]";
    }
}
