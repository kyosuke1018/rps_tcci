/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.e10;

/**
 *
 * @author Peter.pan
 */
public class ContractE10VO {
    private Long id; // PK
    private String code; // 合約號碼
    private Long customerId; // FK:EC_CUSTOMER.ID
    private Boolean active; // 1:啟用, 0:停用
    private String name; // 合約名稱
    private Boolean hide; // 0:顯示,1:不顯示
    private String auart; // 銷售文件類型

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public String getAuart() {
        return auart;
    }

    public void setAuart(String auart) {
        this.auart = auart;
    }

}
