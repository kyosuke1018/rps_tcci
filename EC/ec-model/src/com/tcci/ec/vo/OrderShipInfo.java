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
public class OrderShipInfo {
    private Long id;
    private String recipient;//必填
    private String address;//必填
    private String phone;//必填
    private String carNo;//選擇自提時 必填  多筆以分號";"隔開
    private String driver;//選擇自提時 必填
    private float patrolLatitude;//緯度
    private float patrolLongitude;//經度

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public float getPatrolLatitude() {
        return patrolLatitude;
    }

    public void setPatrolLatitude(float patrolLatitude) {
        this.patrolLatitude = patrolLatitude;
    }

    public float getPatrolLongitude() {
        return patrolLongitude;
    }

    public void setPatrolLongitude(float patrolLongitude) {
        this.patrolLongitude = patrolLongitude;
    }
    
    
}
