/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.tccstore.entity.EcPlant;

/**
 *
 * @author Jimmy.Lee
 */
public class PlantVO extends ExcelVOBase {

    private String code;
    private String name;
    private int incoFlag;
    private String vkorg;
    private boolean autoOrder;
    private boolean active;

    private EcPlant entity;

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

    public int getIncoFlag() {
        return incoFlag;
    }

    public void setIncoFlag(int incoFlag) {
        this.incoFlag = incoFlag;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public boolean isAutoOrder() {
        return autoOrder;
    }

    public void setAutoOrder(boolean autoOrder) {
        this.autoOrder = autoOrder;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public EcPlant getEntity() {
        return entity;
    }

    public void setEntity(EcPlant entity) {
        this.entity = entity;
    }

}
