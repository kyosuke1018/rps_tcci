/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

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

        test.productList(null, "820", "1"); // 一般購買
        test.productList("11547", "820", "1"); // 合約購買
        
        test.batchCheck("YDAC3RE15011", null); // 防偽檢查
    }
    
    public void productList(String contract_id, String delivery_id, String plant_id)  {
        String service = "/product/list";
        Map<String, String> form = new HashMap<>();
        String descripition = "一般購買商品列表";
        if (contract_id != null) {
            form.put("contract_id", contract_id);
            descripition = "合約購買商品列表";
        }
        form.put("delivery_id", delivery_id);
        form.put("plant_id", plant_id);
        form.put("customer_id", "2");
        executeForm(service, form, Product[].class, descripition);
    }
    
    public void batchCheck(String lotno, String areacode) {
        String service = "/product/batchcheck";
        Map<String, String> form = new HashMap<>();
        form.put("lotno", lotno);
        form.put("areacode", areacode);
        executeForm(service, form, String.class, "防偽檢查");
    }

}
