/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.model.plant;

import com.tcci.tccstore.model.deliveryplace.DeliveryPlace;

/**
 *
 * @author Jimmy.Lee
 */
public class DeliveryPreference {

    private DeliveryPlace deliveryPlace;
    private Plant plant;

    // c'tor
    public DeliveryPreference() {
    }
    
    public DeliveryPreference(DeliveryPlace deliveryPlace, Plant plant) {
        this.deliveryPlace = deliveryPlace;
        this.plant = plant;
    }
    
    // getter, setter
    public DeliveryPlace getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(DeliveryPlace deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

}
