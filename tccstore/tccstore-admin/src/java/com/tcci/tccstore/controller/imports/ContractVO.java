/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcCustomer;

/**
 *
 * @author Jimmy.Lee
 */
public class ContractVO extends ExcelVOBase {

    private String code;
    private String name;
    private boolean active;
    private String customer;
    private boolean hide;

    private EcContract entity;
    private EcCustomer ecCustomer;

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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
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
