/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

import com.tcci.tccstore.model.sales.Sales;

/**
 *
 * @author Jimmy.Lee
 */
public class SalesTest extends TestBase {

    public static void main(String[] args) throws SSOClientException {
        SalesTest test = new SalesTest();
        test.login("1609384080@qq.com", "admin");
        String service = "/sales/customer/4104";
        System.out.println(service);
        Sales[] sales = test.get(service, Sales[].class);
        System.out.println(test.toJson(sales));
    }

}
