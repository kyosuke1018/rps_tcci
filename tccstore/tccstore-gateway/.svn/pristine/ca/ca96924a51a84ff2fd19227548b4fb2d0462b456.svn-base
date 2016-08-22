/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jimmy.Lee
 */
public class VehicleTest extends TestBase {
    
    public static void main(String[] args) throws SSOClientException {
        VehicleTest test = new VehicleTest();
        test.login("c1", "admin");
        
        test.preferenceAdd("湘1234567");
        test.preferenceRemove("湘1234567");
        test.preferenceList();
    }

    public void preferenceAdd(String vehicle) {
        String service = "/vehicle/preference/add";
        Map<String, String> form = new HashMap<>();
        form.put("vehicle", vehicle);
        this.executeForm(service, form, String.class, "增加常用車輛");
    }

    public void preferenceRemove(String vehicle) {
        String service = "/vehicle/preference/remove";
        Map<String, String> form = new HashMap<>();
        form.put("vehicle", vehicle);
        this.executeForm(service, form, String.class, "刪除常用車輛");
    }

    public void preferenceList() {
        String service = "/vehicle/preference/list";
        this.executeGet(service, String[].class, "常用車輛清單");
    }

}
