/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.vo;

import java.util.List;

/**
 *
 * @author Kyle.Cheng
 */
public class Company {
    private Long id;//R
    private String cname;//C R
    private String idCode;//統一社會信用代碼 //C R
    private String tel1;//C R
    private String owner1;//負責人 預設帶member.name 可編輯 //C R
    // 所在區域選單
    private Long state;//salesAreaOption.id //C 
    private String salesArea;//所在區域//R
    
    private String startAt;//創立時間 yyyy/MM/dd HH:mm:ss //C R
    // 產業別選單
    private Long category;//產業別 industryOption.id  //C 
    private String industry;//產業別//R
    
    private Long capital;//資本額   //C R
    private Long yearIncome;//年收入    //C R
    //會員照片
    private String imageUrl;
    private List<Long> imageIdList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getOwner1() {
        return owner1;
    }

    public void setOwner1(String owner1) {
        this.owner1 = owner1;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public String getSalesArea() {
        return salesArea;
    }

    public void setSalesArea(String salesArea) {
        this.salesArea = salesArea;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Long getCapital() {
        return capital;
    }

    public void setCapital(Long capital) {
        this.capital = capital;
    }

    public Long getYearIncome() {
        return yearIncome;
    }

    public void setYearIncome(Long yearIncome) {
        this.yearIncome = yearIncome;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Long> getImageIdList() {
        return imageIdList;
    }

    public void setImageIdList(List<Long> imageIdList) {
        this.imageIdList = imageIdList;
    }
    
}
