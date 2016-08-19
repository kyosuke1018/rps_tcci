/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.entity;

import com.tcci.fc.util.time.DateUtils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Kyle.Cheng
 */
@Entity
@Table(name = "FC_SAP_UPLOAD_RECORD")
public class FcSapUploadRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)    
    @Column(name = "ID")
    private Long id;
    @NotNull
    @Column(name = "FUNC")
    private String func;
    @NotNull
    @Column(name = "COMPANY_CODE")
    private String companyCode;
    @NotNull
    @Column(name = "YEARMONTH")
    private String yearmonth;
    @Column(name = "UPLOAD_DATE")
    private String uploadDate;
    @Column(name = "UPLOAD_TIME")
    private String uploadTime;
    @Column(name = "USER_NAME")
    private String userName;//SAP user 中文名
    
    public FcSapUploadRecord(){
    }

    // getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
    
    public Date getUploadTimestamp() {
        if(this.uploadDate == null){
            return null;
        }
        Date result = null;
        if(this.uploadTime == null){
            result = DateUtils.simpleISODateStringToDate(this.uploadDate);
        }else{
            result = DateUtils.simpleISODateTimeStringToDate(this.uploadDate, this.uploadTime);
        }
        return result; 
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
