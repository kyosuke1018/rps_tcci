/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.tccstore.entity.EcSales;

/**
 *
 * @author Jimmy.Lee
 */
public class SalesVO extends ExcelVOBase {

    private String code;
    private String name;
    private boolean active;

    private EcSales entity;

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

    public EcSales getEntity() {
        return entity;
    }

    public void setEntity(EcSales entity) {
        this.entity = entity;
    }

}
