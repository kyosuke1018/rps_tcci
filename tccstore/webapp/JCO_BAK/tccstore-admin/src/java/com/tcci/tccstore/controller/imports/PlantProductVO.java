/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.entity.EcPlantProduct;
import com.tcci.tccstore.entity.EcProduct;

/**
 *
 * @author Jimmy.Lee
 */
public class PlantProductVO extends ExcelVOBase {

    private String plant;
    private String product;
    private boolean active;

    private EcPlantProduct entity;
    private EcPlant ecPlant;
    private EcProduct ecProduct;

    // getter, setter
    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public EcPlantProduct getEntity() {
        return entity;
    }

    public void setEntity(EcPlantProduct entity) {
        this.entity = entity;
    }

    public EcPlant getEcPlant() {
        return ecPlant;
    }

    public void setEcPlant(EcPlant ecPlant) {
        this.ecPlant = ecPlant;
    }

    public EcProduct getEcProduct() {
        return ecProduct;
    }

    public void setEcProduct(EcProduct ecProduct) {
        this.ecProduct = ecProduct;
    }

}
