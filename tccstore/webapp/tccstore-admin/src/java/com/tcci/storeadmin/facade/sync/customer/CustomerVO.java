/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync.customer;

import com.tcci.storeadmin.facade.sync.SyncBaseVO;
import com.tcci.tccstore.entity.EcCustomer;

/**
 *
 * @author Jimmy.Lee
 */
public class CustomerVO extends SyncBaseVO {

    private String code;
    private String name;
    private boolean active;

    private EcCustomer entity;

    public CustomerVO() {
    }

    public CustomerVO(String code, String name) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public EcCustomer getEntity() {
        return entity;
    }

    public void setEntity(EcCustomer entity) {
        this.entity = entity;
    }

}
