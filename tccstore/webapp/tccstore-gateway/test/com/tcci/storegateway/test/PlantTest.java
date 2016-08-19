/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

import com.tcci.tccstore.model.plant.PlantQueryResult;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jimmy.Lee
 */
public class PlantTest extends TestBase {
    
    private static final String USERNAME = "c1";
    private static final String PASSWORD = "admin";
    //private static final String CUSTOMER_ID = "2";
    private static final String CUSTOMER_ID = "5446"; // 中铁物贸（深圳）有限公司(19685)
    //private static final String CUSTOMER_ID = "5769"; // qa 中铁物贸（深圳）有限公司(19685)
    //private static final String CUSTOMER_ID = "3071"; // qa 英德市鸿盛贸易有限公司(16624)
    //private static final String CUSTOMER_ID = "8429"; // qa 重庆市秦杨建材有限公司(20295)
    
    public static void main(String[] args) throws SSOClientException {
        PlantTest test = new PlantTest();
        test.login(USERNAME, PASSWORD);

//        test.plantQueryByLocation();
//        test.plantQueryByPreference();
//        test.plantQueryByDeliveryId();
//        test.plantQueryByDeliveryCode();
//        test.plantQueryByContractId();
//        test.plantPreferenceAdd();
//        test.plantPreferenceRemove();
        
        //String contract_id=null;
        String contract_id="21"; // 深圳轨道交通11号线工程(50003954)
        //String contract_id="9697"; //qa 深圳轨道交通11号线工程(50003954)
        //test.plantQuery2ByCode(contract_id, "S1DD06"); // 多個廠
        //test.plantQuery2ByPreference(contract_id);
        //test.plantQuery2ByLocation(contract_id);
        //test.plantQuery2ByDeliveryId(contract_id);
        
        // contact_id = "21";
        test.plantQuery2ByCode(null, "S2BA02XXX");
        //test.plantQuery2ByCode(contact_id, "S1GC05");
        //test.plantQuery2ByCode(contract_id, "S2JB01");
        //test.plantQuery2ByPreference(contract_id);
        //test.plantQuery2ByLocation(contract_id);
        //test.plantQuery2ByDeliveryId(contract_id);

        //test.plantQuery2ByCode(contract_id, "ABCDEFG"); // 不存在

        //test.plantQuery2ByCode(contract_id, "S0GA01"); // 與合約不符
        
        //test.plantPreferenceAdd2();
        //test.plantPreferenceRemove2();
    }

    public void plantQueryByLocation() {
        String service = "/plant/query";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", CUSTOMER_ID);
        executeForm(service, form, PlantQueryResult.class, "一般購買, by地區");
        
        form.put("province", "广西壮族自治区");
        executeForm(service, form, PlantQueryResult.class, "一般購買, by地區");

        form.put("city", "北海市");
        executeForm(service, form, PlantQueryResult.class, "一般購買, by地區");
        
        form.put("district", "海城区");
        executeForm(service, form, PlantQueryResult.class, "一般購買, by地區");
    }
    
    public void plantQueryByPreference() {
        String service = "/plant/query";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", CUSTOMER_ID);
        form.put("preference", "y");
        executeForm(service, form, PlantQueryResult.class, "一般購買, by常用送達地點");
    }
    
    public void plantQueryByDeliveryId() {
        String service = "/plant/query";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", CUSTOMER_ID);
        form.put("delivery_id", "806");
        executeForm(service, form, PlantQueryResult.class, "一般購買, by送達地點id");
    }
    
    public void plantQueryByDeliveryCode() {
        String service = "/plant/query";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", CUSTOMER_ID);
        form.put("delivery_code", "S1DF09");
        executeForm(service, form, PlantQueryResult.class, "一般購買, by送達地點代碼");
    }
    
    public void plantQueryByContractId() {
        String service = "/plant/query";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", CUSTOMER_ID);
        form.put("contract_id", "38");
        executeForm(service, form, PlantQueryResult.class, "合約購買");
    }
    
    public void plantPreferenceAdd() {
        String service = "/plant/preference/add";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", CUSTOMER_ID);
        form.put("delivery_id", "820");
        executeForm(service, form, String.class, "新增常用送達地點");
    }
    
    public void plantPreferenceRemove() {
        String service = "/plant/preference/remove";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", CUSTOMER_ID);
        form.put("delivery_id", "820");
        executeForm(service, form, String.class, "移除常用送達地點");
    }
    
    public void plantQuery2ByCode(String contract_id, String delivery_code) {
        String service = "/plant/query2";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", CUSTOMER_ID);
        form.put("delivery_code", delivery_code);
        String description = "一般購買2, by送達地點代碼";
        if (contract_id != null) {
            form.put("contract_id", contract_id);
            description = "合約購買2, by送達地點代碼";
        }
        executeForm(service, form, PlantQueryResult.class, description);
    }

    public void plantQuery2ByPreference(String contract_id) {
        String service = "/plant/query2";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", CUSTOMER_ID);
        form.put("preference", "y");
        String description = "一般購買2, by常用送達地點";
        if (contract_id != null) {
            form.put("contract_id", contract_id);
            description = "合約購買2, by常用送達地點";
        }
        executeForm(service, form, PlantQueryResult.class, description);
    }
    
    public void plantQuery2ByLocation(String contract_id) {
        String service = "/plant/query2";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", CUSTOMER_ID);
        String description = "一般購買2, by地區";
        if (contract_id != null) {
            form.put("contract_id", contract_id);
            description = "合約購買2, by地區";
        }
        executeForm(service, form, PlantQueryResult.class, description);

        form.put("province", "广东省");
        executeForm(service, form, PlantQueryResult.class, description);

        form.put("city", "深圳市");
        executeForm(service, form, PlantQueryResult.class, description);
        
        form.put("district", "福田区");
        executeForm(service, form, PlantQueryResult.class, description);
    }

    public void plantQuery2ByDeliveryId(String contract_id) {
        String service = "/plant/query2";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", CUSTOMER_ID);
        form.put("delivery_id", "932");
        String description = "一般購買2, by送達地點id";
        if (contract_id != null) {
            form.put("contract_id", contract_id);
            description = "合約購買2, by送達地點id";
        }
        executeForm(service, form, PlantQueryResult.class, description);
    }
    
    public void plantPreferenceAdd2() {
        String service = "/plant/preference/add";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", CUSTOMER_ID);
        form.put("delivery_id", "820");
        form.put("plant_id", "1");
        executeForm(service, form, String.class, "新增常用送達地點");
    }
    
    public void plantPreferenceRemove2() {
        String service = "/plant/preference/remove";
        Map<String, String> form = new HashMap<>();
        form.put("customer_id", CUSTOMER_ID);
        form.put("delivery_id", "820");
        form.put("plant_id", "1");
        executeForm(service, form, String.class, "移除常用送達地點");
    }
    
}
