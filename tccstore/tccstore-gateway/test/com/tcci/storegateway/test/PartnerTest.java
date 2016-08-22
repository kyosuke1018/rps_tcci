/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

import com.tcci.tccstore.model.goods.Goods;
import com.tcci.tccstore.model.partner.Partner;
import com.tcci.tccstore.model.partner.PartnerComment;
import com.tcci.tccstore.model.partner.PartnerQueryResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

/**
 *
 * @author Jimmy.Lee
 */
public class PartnerTest extends TestBase {

    public static void main(String[] args) throws SSOClientException, FileNotFoundException {
        PartnerTest test = new PartnerTest();
        test.login("c1", "admin");
        
        // test.partnerGoodsList();
        // test.partnerGoodsBuy();
        // test.partnerQuery();
        // test.partnerView();
        // test.partnerCommentList();
        // test.partnerQueryAll();
        // test.partnerCommentAdd();
        
        // test.login("c1", "admin");
        // test.mylist();
        // test.partnerEdit();
        //test.partnerApply();
        
        //test.partnerQueryDivision();
        test.partnerQueryDivision2();
    }

    public void partnerQuery() {
        String service = "/partner/query";
        Map<String, String> form = new HashMap<>();
        form.put("goods_id", "1");
        try {
            System.out.println(service);
            PartnerQueryResult result = this.postForm(service, PartnerQueryResult.class, form);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
        form.put("province", "广西壮族自治区");
        try {
            System.out.println(service);
            PartnerQueryResult result = this.postForm(service, PartnerQueryResult.class, form);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
        form.put("city", "钦州市");
        try {
            System.out.println(service);
            PartnerQueryResult result = this.postForm(service, PartnerQueryResult.class, form);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
        form.put("district", "灵山县");
        try {
            System.out.println(service);
            PartnerQueryResult result = this.postForm(service, PartnerQueryResult.class, form);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void partnerQueryAll() {
        String service = "/partner/query";
        Map<String, String> form = new HashMap<>();
        form.put("allpartner", "y");
        try {
            System.out.println(service);
            PartnerQueryResult result = this.postForm(service, PartnerQueryResult.class, form);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
        form.put("province", "广西壮族自治区");
        try {
            System.out.println(service);
            PartnerQueryResult result = this.postForm(service, PartnerQueryResult.class, form);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
        form.put("city", "钦州市");
        try {
            System.out.println(service);
            PartnerQueryResult result = this.postForm(service, PartnerQueryResult.class, form);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
        form.put("district", "灵山县");
        try {
            System.out.println(service);
            PartnerQueryResult result = this.postForm(service, PartnerQueryResult.class, form);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void partnerQueryDivision() {
        String service = "/partner/query";
        Map<String, String> form = new HashMap<>();
        form.put("division", "y");
        this.executeForm(service, form, PartnerQueryResult.class, "台泥伙伴地址查詢");
        form.put("province", "广西壮族自治区");
        this.executeForm(service, form, PartnerQueryResult.class, "台泥伙伴地址查詢");
        form.put("city", "南宁市");
        this.executeForm(service, form, PartnerQueryResult.class, "台泥伙伴地址查詢");
    }
    
    public void partnerQueryDivision2() {
        String service = "/partner/query";
        Map<String, String> form = new HashMap<>();
        form.put("division", "y");
        this.executeForm(service, form, PartnerQueryResult.class, "台泥伙伴地址查詢");
        form.put("province", "北京市");
        this.executeForm(service, form, PartnerQueryResult.class, "台泥伙伴地址查詢");
        form.put("city", "北京市");
        this.executeForm(service, form, PartnerQueryResult.class, "台泥伙伴地址查詢");
    }
    
    public void partnerView() {
        String service = "/partner/view/502";
        try {
            System.out.println(service);
            Partner result = this.get(service, Partner.class);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void partnerGoodsList() {
        String service = "/partner/goods/list";
        try {
            System.out.println(service);
            Goods[] result = this.get(service, Goods[].class);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void partnerGoodsBuy() {
            String service = "/partner/goods/buy";
        Map<String, String> form = new HashMap<>();
        form.put("goods_id", "1");
        form.put("quantity", "100");
        form.put("uom", "噸");
        try {
            System.out.println(service);
            String result = this.postForm(service, String.class, form);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void mylist() {
        String service = "/partner/mylist";
        try {
            System.out.println(service);
            Partner[] result = this.get(service, Partner[].class);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void partnerEdit() {
        String service = "/partner/edit/153";
        try {
            System.out.println(service);
            Partner result = this.get(service, Partner.class);
            System.out.println(this.toJson(result));
            result.setDescription("測試啦");
            result.getGoodsList().get(1).setSelected(true);
            service = "/partner/save";
            System.out.println(service);
            File f = new File("C:\\tmp\\20151006024722.JPEG");
            // String info = this.toJson(result);
            String info = "{\"address\":\"廣東省\",\"averageRate\":\"5.0\",\"city\":\"廣州市\",\"contact\":\"柯搖擺\",\"createtime\":\"\",\"district\":\"廣州市\",\"goodsList\":[{\"code\":\"100C38332000\",\"id\":\"1\",\"name\":\"P.C 32.5R 袋装\",\"selected\":\"true\"},{\"code\":\"100C39432000\",\"id\":\"3\",\"name\":\"P.O 42.5R 袋装\",\"selected\":\"true\"}],\"id\":\"153\",\"imageUrl\":\"http://tccstore.taiwancement.com/tccstore/service/image?oid\\u003dpartner:153\",\"name\":\"name3\",\"phone\":\"0806449\",\"province\":\"广东省\",\"status\":\"\",\"town\":\"測試\"}";
            Part[] parts = {
                new StringPart("info", info, "UTF-8"),
                new FilePart("pic", f)};
            String result2 = this.postParts(service, String.class, parts);
            System.out.println(result2);
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void partnerApply() throws SSOClientException, FileNotFoundException {
        String service = "/partner/apply";
        Partner partner = this.executeGet(service, Partner.class, "台泥伙伴申請");
        partner.setName("Jimmy的店5");
        partner.setProvince("广西壮族自治区");
        partner.setCity("申請城市");
        partner.setDistrict("申請區");
        partner.setTown("申請鎮");
        partner.setAddress("申請地址");
        partner.setContact("申請聯絡人");
        partner.setPhone("申請電話");
        partner.setSocial("申請WeChat");
        partner.getGoodsList().get(0).setSelected(true);
        partner.getGoodsList().get(1).setSelected(true);
        service = "/partner/save";
        String info = this.toJson(partner);
        File f = new File("C:\\temp\\arvados.png");
        Part[] parts = {
            new StringPart("info", info, "UTF-8"),
            new FilePart("pic", f)};
        this.executeParts(service, String.class, parts, "台泥伙伴儲存");
    }
    
    public void partnerCommentAdd() {
        String service = "/partner/comment/add";
        Map<String, String> form = new HashMap<>();
        form.put("partner_id", "502");
        form.put("rate", "3");
        form.put("message", "普普啦");
        this.executeForm(service, form, String.class, "伙伴評語新增");
    }

    public void partnerCommentList() {
        String service = "/partner/comment/list/502";
        try {
            System.out.println(service);
            PartnerComment[] result = this.get(service, PartnerComment[].class);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
