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
public class FormTest extends TestBase {

    public static void main(String[] args) throws SSOClientException {
        FormTest test = new FormTest();
        test.login("c1", "admin");
        test.formMember();
        test.formCustomer();
    }

    public void formMember() {
        Map<String, String> form = new HashMap<>();
        String service = "/form/member";
        form.put("name", "李四");
        form.put("email", "jimmy.lee@tcci.com.tw");
        form.put("phone", "28825252");
        executeForm(service, form, String.class, "申請成為會員");
    }
 
    public void formCustomer() {
        Map<String, String> form = new HashMap<>();
        String service = "/form/customer";
        form.put("name", "李四");
        form.put("email", "jimmy.lee@tcci.com.tw");
        form.put("phone", "28825252");
        form.put("province", "重庆市");
        executeForm(service, form, String.class, "申請成為簽約客戶");
    }

}
