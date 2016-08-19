/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync.delivery;

import com.tcci.storeadmin.facade.sync.SyncBaseVO;
import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcDeliveryVkorg;
import com.tcci.tccstore.entity.EcSalesarea;

/**
 *
 * @author Jimmy.Lee
 */
public class DeliveryVkorgVO extends SyncBaseVO {

    private String delivery;
    private String vkorg;
    private String salesarea;

    private EcDeliveryVkorg entity;
    private EcDeliveryPlace ecDeliveryPlace;
    private EcSalesarea ecSalesarea;

    // c'tor
    public DeliveryVkorgVO() {
    }
    
    public DeliveryVkorgVO(String delivery, String vkorg, String salesarea, EcDeliveryVkorg entity, EcDeliveryPlace ecDeliveryPlace, EcSalesarea ecSalesarea) {
        this.delivery = delivery;
        this.vkorg = vkorg;
        this.salesarea = salesarea;
        this.entity = entity;
        this.ecDeliveryPlace = ecDeliveryPlace;
        this.ecSalesarea = ecSalesarea;
    }
    
    // getter, setter
    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public String getSalesarea() {
        return salesarea;
    }

    public void setSalesarea(String salesarea) {
        this.salesarea = salesarea;
    }

    public EcDeliveryVkorg getEntity() {
        return entity;
    }

    public void setEntity(EcDeliveryVkorg entity) {
        this.entity = entity;
    }

    public EcDeliveryPlace getEcDeliveryPlace() {
        return ecDeliveryPlace;
    }

    public void setEcDeliveryPlace(EcDeliveryPlace ecDeliveryPlace) {
        this.ecDeliveryPlace = ecDeliveryPlace;
    }

    public EcSalesarea getEcSalesarea() {
        return ecSalesarea;
    }

    public void setEcSalesarea(EcSalesarea ecSalesarea) {
        this.ecSalesarea = ecSalesarea;
    }

}
