/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.test;

import com.tcci.tccstore.client.SSOClientException;
import com.tcci.tccstore.model.plant.PlantQueryResult;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jimmy.Lee
 */
public class PlantTest extends TestBase {
    
    public static void main(String[] args) throws SSOClientException {
        PlantTest test = new PlantTest();
        test.login("c1", "admin");
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", "2");
        String service = "/plant/query";
        System.out.println("查詢省...");
        PlantQueryResult result = test.postForm(service, PlantQueryResult.class, form);
        test.showResult(result);

        form.put("province", "广西壮族自治区");
        System.out.println("广西壮族自治区查詢市...");
        result = test.postForm(service, PlantQueryResult.class, form);
        test.showResult(result);

        form.put("city", "北海市");
        System.out.println("北海市查詢區...");
        result = test.postForm(service, PlantQueryResult.class, form);
        test.showResult(result);

        form.put("district", "海城区");
        System.out.println("海城区查詢送達地點...");
        result = test.postForm(service, PlantQueryResult.class, form);
        test.showResult(result);
        
        // 只有一個區
        form.clear();
        form.put("customer_id", "2");
        form.put("province", "广东省");
        form.put("city", "河源市");
        System.out.println("广东省河源市只有一個區...");
        result = test.postForm(service, PlantQueryResult.class, form);
        test.showResult(result);
        
        // 區是null
        form.clear();
        form.put("customer_id", "2");
        form.put("province", "广东省");
        form.put("city", "东莞市");
        System.out.println("广东省东莞市區是null...");
        result = test.postForm(service, PlantQueryResult.class, form);
        test.showResult(result);

        form.clear();
        form.put("customer_id", "2");
        form.put("preference", "y");
        System.out.println("常用送達地點...");
        result = test.postForm(service, PlantQueryResult.class, form);
        test.showResult(result);

        form.clear();
        form.put("customer_id", "2");
        form.put("delivery_id", "820");
        System.out.println("增加常用送達地點...");
        String str = test.postForm("/plant/preference/add", String.class, form);
        System.out.println(str);

        form.clear();
        form.put("customer_id", "2");
        form.put("preference", "y");
        System.out.println("常用送達地點...");
        result = test.postForm(service, PlantQueryResult.class, form);
        test.showResult(result);

        form.clear();
        form.put("customer_id", "2");
        form.put("delivery_id", "820");
        System.out.println("刪除常用送達地點...");
        str = test.postForm("/plant/preference/remove", String.class, form);
        System.out.println(str);

        form.clear();
        form.put("customer_id", "2");
        form.put("preference", "y");
        System.out.println("常用送達地點...");
        result = test.postForm(service, PlantQueryResult.class, form);
        test.showResult(result);

        form.clear();
        form.put("customer_id", "2");
        form.put("delivery_id", "806");
        System.out.println("查詢廠(delivery_id)...");
        result = test.postForm(service, PlantQueryResult.class, form);
        test.showResult(result);
        
        form.clear();
        form.put("customer_id", "2");
        form.put("contract_id", "10");
        System.out.println("查詢廠(byContract)...");
        result = test.postForm(service, PlantQueryResult.class, form);
        test.showResult(result);
    }

    private void showResult(PlantQueryResult result) throws SSOClientException {
        System.out.println(toJson(result));
        System.out.println("------");
    }
}
