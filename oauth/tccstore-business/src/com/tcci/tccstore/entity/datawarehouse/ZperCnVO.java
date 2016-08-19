/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity.datawarehouse;

/**
 *
 * @author Jimmy.Lee
 */
public class ZperCnVO {

    private String customer; // 客戶代碼
    private String sales; // 業務代碼

    // c'tor
    public ZperCnVO() {
    }

    public ZperCnVO(String customer, String sales) {
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

}
