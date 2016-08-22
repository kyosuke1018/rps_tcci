/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.test;

import com.tcci.tccstore.client.SSOClientException;
import com.tcci.tccstore.model.TestModel;
import com.tcci.tccstore.model.order.Order;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Jimmy.Lee
 */
public class Test extends TestBase {

    public static void main(String[] args) throws SSOClientException, ParseException {
        Test test = new Test();
        test.login("112841870@qq.com", "admin");
        TestModel model = new TestModel();
        model.setName("client端中文");
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
        //isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = isoFormat.parse("2010-05-23");
        //Date date = new Date();
        model.setCreatetime(date);
        String service = "/test";
        TestModel result = test.postJson(service, TestModel.class, model);
        System.out.println(result.getName());
        System.out.println(isoFormat.format(result.getCreatetime()));
        
        // TODO:訂單紅利
        Order order = new Order();
        order.setQuantity(new BigDecimal("56"));
        String bonus = test.postJson("/order/calcbonus", String.class, order);
        System.out.println(bonus);
    }

}
