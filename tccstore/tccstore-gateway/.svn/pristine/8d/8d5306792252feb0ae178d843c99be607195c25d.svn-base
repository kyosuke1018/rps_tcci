/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

import com.tcci.tccstore.model.home.Home;

/**
 *
 * @author Jimmy.Lee
 */
public class HomeTest extends TestBase {
    
    public static void main(String[] args) throws SSOClientException {
        HomeTest test = new HomeTest();
        String service = "/home";
        test.executeGet(service, Home.class, "home(未登入)");

        test.login("c1", "admin");
        service = "/home/login";
        test.executeGet(service, Home.class, "home(已登入)");
    }
    
}
