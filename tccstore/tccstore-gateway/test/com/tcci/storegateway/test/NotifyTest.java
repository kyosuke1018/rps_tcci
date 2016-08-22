/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

import com.tcci.tccstore.model.notify.Notify;

/**
 *
 * @author Jimmy.Lee
 */
public class NotifyTest extends TestBase {
    
    public static void main(String[] args) throws SSOClientException {
        NotifyTest test = new NotifyTest();
        test.login("c1", "admin");
//        System.out.println("test /notify/query?read=false (未讀通知)");
//        Notify[] notifyList = test.get("/notify/query?read=false", Notify[].class);
//        System.out.println(test.toJson(notifyList));
        test.query();
    }
    
    public void query() {
        String service = "/notify/query";
        executeGet(service, Notify[].class, "通知清單");
    }

}
