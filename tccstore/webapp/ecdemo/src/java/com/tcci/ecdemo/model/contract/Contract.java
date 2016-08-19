/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.model.contract;

import com.tcci.ecdemo.model.customer.Customer;
import com.tcci.ecdemo.model.plant.Plant;

/**
 *
 * @author Jimmy.Lee
 */
public class Contract {
    
    private Long id;
    private String code;
    private String name;
    private Customer customer;
    private Plant plant;

    public Contract() {
    }
    
    public Contract(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    // getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

}
