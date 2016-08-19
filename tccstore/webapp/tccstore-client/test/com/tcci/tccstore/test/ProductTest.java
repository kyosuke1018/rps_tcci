/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.test;

import com.tcci.tccstore.client.SSOClientException;
import com.tcci.tccstore.model.product.Product;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jimmy.Lee
 */
public class ProductTest extends TestBase {

    public static void main(String[] args) throws SSOClientException {
        ProductTest test = new ProductTest();
        test.login("c1", "admin");

        String service = "/product/list";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", "2");
        form.put("plant_id", "2");
        System.out.println(service + "(一般購買)");
        Product[] products = test.postForm(service, Product[].class, form);
        test.showResult(products);

        form.clear();
        form.put("customer_id", "2");
        form.put("plant_id", "1");
        form.put("contract_id", "10");
        System.out.println(service + "(合約購買)");
        products = test.postForm(service, Product[].class, form);
        test.showResult(products);
        
        service = "/product/batchcheck";
        form.clear();
        form.put("lotno", "GGBC3CD15100"); // 不分大小寫
        form.put("areacode", "1GD09"); // 不分大小寫
        System.out.println(service);
        String result = test.postForm(service, String.class, form);
        System.out.println(result);
        
        form.clear();
        form.put("product_id", "2");
        form.put("quantity", "100");
        service = "/product/buy";
        System.out.println(service);
        result = test.postForm(service, String.class, form);
        System.out.println(result);
    }
    
    private void showResult(Product[] products) throws SSOClientException {
        System.out.println(toJson(products));
        System.out.println("------");
    }

}
