/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync.customer;

import com.tcci.storeadmin.facade.sync.SyncBaseVO;
import com.tcci.tccstore.entity.EcCustomerVkorg;

/**
 *
 * @author Jimmy.Lee
 */
public class CustomerVkorgVO extends SyncBaseVO {

    private String customer;
    private String vkorg;

    private EcCustomerVkorg entity;

    public CustomerVkorgVO() {
    }

    public CustomerVkorgVO(String customer, String vkorg) {
        this.customer = customer;
        this.vkorg = vkorg;
    }

    // getter, setter
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public EcCustomerVkorg getEntity() {
        return entity;
    }

    public void setEntity(EcCustomerVkorg entity) {
        this.entity = entity;
    }

}
