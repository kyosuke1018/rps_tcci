/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

import com.tcci.tccstore.model.contract.Contract;

/**
 *
 * @author Jimmy.Lee
 */
public class ContractTest extends TestBase {

    public static void main(String[] args) throws SSOClientException {
        testNormal();
        testNoLogin();
        testInvalidCustomer();
    }
    
    public static void testNormal() {
        System.out.println("testNormal");
        ContractTest test = new ContractTest();
        try {
            test.login("c1", "admin");
            Contract[] contracts = test.get("/contract/customer/2", Contract[].class);
            System.out.println(test.toJson(contracts));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage() + ", status=" + ex.getStatusCode());
        }
    }
    
    public static void testNoLogin() {
        System.out.println("testNoLogin");
        ContractTest test = new ContractTest();
        try {
            Contract[] contracts = test.get("/contract/customer/2", Contract[].class);
            System.out.println(test.toJson(contracts));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage() + ", status=" + ex.getStatusCode());
        }
    }
    
    public static void testInvalidCustomer() {
        System.out.println("testInvalidCustomer");
        ContractTest test = new ContractTest();
        try {
            test.login("c1", "admin");
            Contract[] contracts = test.get("/contract/customer/10000", Contract[].class);
            System.out.println(test.toJson(contracts));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage() + ", status=" + ex.getStatusCode());
        }
    }

}
