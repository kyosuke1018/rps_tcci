/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.test;

import com.tcci.tccstore.model.home.Home;
import com.tcci.tccstore.model.notify.Notify;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Neo.Fu
 */
public class NotifyTest extends TestBase {

    public static void showResult(Object[] objects) {
        if (null == objects || 0 == objects.length) {
            System.out.println("empty");
        } else {
            for (Object object : objects) {
                if (object instanceof Notify) {
                    System.out.println("Notify=" + (Notify) object);
                } else {
                    System.out.println("{}" + object);
                }
            }
        }
        System.out.println("--------------------");
    }

    public static void main(String[] args) {
        NotifyTest test = new NotifyTest();
        try {
            test.login("neo.fu", "admin");
            //未讀通知
            System.out.println("test /notify/query?read=false (未讀通知)");
            Notify[] notifyList = test.get("/notify/query?read=false", Notify[].class);
            System.out.println("notifyList.length=" + notifyList.length);
            showResult(notifyList);

            //已讀通知
            System.out.println("test /notify/query?read=true (已讀通知)");
            Notify[] readNotifyList = test.get("/notify/query?read=true", Notify[].class);
            System.out.println("readNotifyList.length=" + readNotifyList.length);
            showResult(readNotifyList);

            //所有通知
            System.out.println("test /notify/query (所有通知)");
            Notify[] allNotifyList = test.get("/notify/query", Notify[].class);
            System.out.println("allNotifyList.length=" + allNotifyList.length);
            showResult(allNotifyList);

            //設已讀
            System.out.println("test /notify/read/{notify_id}");
            String result = test.get("/notify/read/155", String.class);
            System.out.println("result=" + result);

            //設已讀 (多筆)
            System.out.println("test /notify/read_all");
            List<Long> idList = new ArrayList();
            idList.add(new Long(155));
            idList.add(new Long(157));
            String result2 = test.postJson("/notify/read_all", String.class, idList);
            System.out.println("result=" + result2);

            //刪除通知
            System.out.println("test /notify/remove/{notify_id}");
            String result3 = test.get("/notify/remove/158", String.class);
            System.out.println("result=" + result3);
            
            //未讀通知數
            System.out.println("test /home/login/xxx");
            Home home = test.get("/home/login/xxx",Home.class);
            System.out.println("home.getUnreadNotifys()="+home.getUnreadNotifys());
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("e=" + e);
        }
    }
}
