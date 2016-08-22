/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync.contract;

import com.tcci.storeadmin.facade.sync.SyncBaseVO;
import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcCustomer;

/**
 *
 * @author Jimmy.Lee
 */
public class ContractVO extends SyncBaseVO {

    private String code; // 合約代碼
    private String name; // 合約名稱
    private String customer; // 客戶代碼
    private boolean active; // 啟用

    private EcContract entity;
    private EcCustomer ecCustomer;

    // c'tor
    public ContractVO() {
    }
    
    public ContractVO(String code, String name, String customer, boolean active) {
        this.code = code;
        this.name = name;
        this.customer = customer;
        this.active = active;
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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public EcContract getEntity() {
        return entity;
    }

    public void setEntity(EcContract entity) {
        this.entity = entity;
    }

    public EcCustomer getEcCustomer() {
        return ecCustomer;
    }

    public void setEcCustomer(EcCustomer ecCustomer) {
        this.ecCustomer = ecCustomer;
    }

}
