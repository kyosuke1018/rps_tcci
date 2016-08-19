/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.util;

import com.tcci.tccstore.entity.EcPartner;
import com.tcci.tccstore.entity.EcProduct;

/**
 *
 * @author Neo.Fu
 */
public class PartnerProductFilter {
    private EcPartner partner;
    private EcProduct product;
    private String partnerState;

    public EcPartner getPartner() {
        return partner;
    }

    public void setPartner(EcPartner partner) {
        this.partner = partner;
    }

    public EcProduct getProduct() {
        return product;
    }

    public void setProduct(EcProduct product) {
        this.product = product;
    }

    public String getPartnerState() {
        return partnerState;
    }

    public void setPartnerState(String partnerState) {
        this.partnerState = partnerState;
    }
    
}
