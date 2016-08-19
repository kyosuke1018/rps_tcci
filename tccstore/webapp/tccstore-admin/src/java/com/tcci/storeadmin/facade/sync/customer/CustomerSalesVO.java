/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync.customer;

import com.tcci.storeadmin.facade.sync.SyncBaseVO;
import com.tcci.tccstore.entity.EcCustomerSales;

/**
 *
 * @author Jimmy.Lee
 */
public class CustomerSalesVO extends SyncBaseVO {

    private String customer;
    private String sales;

    private EcCustomerSales entity;

    public CustomerSalesVO() {
    }

    public CustomerSalesVO(String customer, String sales) {
        this.customer = customer;
        this.sales = sales;
    }

    // getter, setter
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public EcCustomerSales getEntity() {
        return entity;
    }

    public void setEntity(EcCustomerSales entity) {
        this.entity = entity;
    }

}
