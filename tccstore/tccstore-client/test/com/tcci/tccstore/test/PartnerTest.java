/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.test;

import com.tcci.tccstore.model.member.Member;
import com.tcci.tccstore.model.partner.Partner;
import com.tcci.tccstore.model.partner.PartnerComment;
import com.tcci.tccstore.model.partner.PartnerProduct;
import com.tcci.tccstore.model.product.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Neo.Fu
 */
public class PartnerTest extends TestBase {

    private static Partner createPartner(int nameIndex, int productId) {
        Partner partner = new Partner();

        partner.setName("name" + nameIndex);
        partner.setDescription("description" + nameIndex);
        partner.setAddress("廣東省");
        partner.setProvince("廣東省");
        partner.setCity("廣州市");
        partner.setPhone("080000123");
        partner.setSocial("social");
        partner.setTheme("theme");
        partner.setMember("jimmy.lee;david.jen");
        Product product = new Product();
        product.setId(new Long(productId));
        PartnerProduct partnerProduct = new PartnerProduct();
        partnerProduct.setProduct(product);
        partnerProduct.setUnitPrice(BigDecimal.TEN.multiply(BigDecimal.TEN));
        List<PartnerProduct> productList = new ArrayList();

        productList.add(partnerProduct);

        partner.setPartnerProductList(productList);

        return partner;
    }

    private static Partner editPartner(int nameIndex, int productId) {
        Partner partner = new Partner();
        partner.setId(new Long(nameIndex));
        partner.setName("name" + nameIndex);
        partner.setDescription("description" + nameIndex);
        partner.setAddress("廣東省");
        partner.setProvince("廣東省");
        partner.setCity("廣州市");
        partner.setPhone("080000123");
        partner.setSocial("social");
        partner.setTheme("theme");
        partner.setMember("jimmy.lee;david.jen;peter.pan");
        partner.setActive(false);
        partner.setCreatetime(new Date());
        partner.setStatus("APPLY");

        Product product = new Product();
        product.setId(new Long(productId));
        PartnerProduct partnerProduct = new PartnerProduct();
        partnerProduct.setProduct(product);
        partnerProduct.setUnitPrice(BigDecimal.TEN.multiply(BigDecimal.TEN));
        List<PartnerProduct> productList = new ArrayList();
        productList.add(partnerProduct);

        partner.setPartnerProductList(productList);

        return partner;
    }

    private static PartnerComment createComment() {
        PartnerComment comment = new PartnerComment();
        comment.setRate(5);
        comment.setMessage("good");
        Partner partner = new Partner();
        partner.setId(new Long(65));
        comment.setPartner(partner);
        return comment;
    }

    private static PartnerComment editComment(int id) {
        PartnerComment comment = new PartnerComment();
        comment.setId(new Long(id));
        comment.setRate(2);
        comment.setMessage("average");
        Member member = new Member();
        member.setLoginAccount("neo.fu");
        comment.setMember(member);
        Partner partner = new Partner();
        partner.setId(new Long(65));
        comment.setPartner(partner);
        comment.setCreatetime(new Date());
        return comment;
    }

    public static void showResult(Object[] objects) {
        if (null == objects || 0 == objects.length) {
            System.out.println("empty");
        } else {
            for (Object object : objects) {
                if (object instanceof Partner) {
                    System.out.println("Partner=" + (Partner) object);
                } else if (object instanceof PartnerComment) {
                    System.out.println("PartnerComment=" + (PartnerComment) object);

                } else {
                    System.out.println("{}" + object);
                }
            }
        }
        System.out.println("--------------------");
    }

    public static void main(String[] args) {
        PartnerTest test = new PartnerTest();
        try {
            test.login("neo.fu", "admin");
            //建立
            //建立台泥夥伴
            System.out.println("Test: partner/create(建立台泥夥伴)");
            Partner partner = createPartner(1, 1);
            String result = test.postJson("/partner/create", String.class, partner);
            System.out.println("result=" + result);

            //建立台泥夥伴 (未輸入名稱)
            System.out.println("Test: partner/create(未輸入名稱)");
            Partner partner2 = createPartner(2, 1);
            partner2.setName(null);
            String result2 = test.postJson("/partner/create", String.class, partner2);
            System.out.println("result=" + result2);

            //建立台泥夥伴 (台泥夥伴已存在)
            System.out.println("Test: partner/create(台泥夥伴已存在)");
            Partner partner3 = createPartner(1, 1);
            String result3 = test.postJson("/partner/create", String.class, partner3);
            System.out.println("result=" + result3);

            //建立台泥夥伴 (未輸入夥伴販售的產品)
            System.out.println("Test: partner/create(未輸入夥伴販售的產品)");
            Partner partner4 = createPartner(4, 1);
            partner4.setPartnerProductList(new ArrayList());
            String result4 = test.postJson("/partner/create", String.class, partner4);
            System.out.println("result=" + result4);

            //建立台泥夥伴 (夥伴販售的產品不存在)
            System.out.println("Test: partner/create(夥伴販售的產品不存在)");
            Partner partner5 = createPartner(5, 1000001);
            String result5 = test.postJson("/partner/create", String.class, partner5);
            System.out.println("result=" + result5);

            //建立台泥夥伴 (未輸入電話)
            System.out.println("Test: partner/create(未輸入電話)");
            Partner partner7 = createPartner(7, 1);
            partner7.setPhone(null);
            String result7 = test.postJson("/partner/create", String.class, partner7);
            System.out.println("result=" + result7);

            //建立台泥夥伴 (未輸入省)
            System.out.println("Test: partner/create(未輸入省)");
            Partner partner71 = createPartner(2, 1);
            partner71.setProvince(null);
            String result71 = test.postJson("/partner/create", String.class, partner71);
            System.out.println("result=" + result71);

            //建立台泥夥伴 (未輸入市)
            System.out.println("Test: partner/create(未輸入市)");
            Partner partner72 = createPartner(2, 1);
            partner72.setCity(null);
            String result72 = test.postJson("/partner/create", String.class, partner72);
            System.out.println("result=" + result72);

            //修改
            //修改台泥夥伴 (未輸入狀態)
            System.out.println("Test: partner/edit(未輸入狀態)");
            Partner partner81 = editPartner(65, 1);
            partner81.setName("name3");
            partner81.setStatus(null);
            String result81 = test.postJson("/partner/edit", String.class, partner81);
            System.out.println("result=" + result81);

            //修改台泥夥伴 (台泥夥伴已存在)
            Partner partner82 = createPartner(2, 1);
            test.postJson("/partner/create", String.class, partner82);
            System.out.println("Test: partner/edit(台泥夥伴已存在)");
            Partner partner8 = editPartner(65, 1);
            partner8.setName("name2");
            String result8 = test.postJson("/partner/edit", String.class, partner8);
            System.out.println("result=" + result8);

            //修改台泥夥伴
            System.out.println("Test: partner/edit(修改台泥夥伴)");
            Partner partner9 = editPartner(87, 1);
            partner9.setName("name2_revise");
            String result9 = test.postJson("/partner/edit", String.class, partner9);
            System.out.println("result=" + result9);

            //修改台泥夥伴 (修改帶有評論的台泥夥伴)
            System.out.println("Test: partner/edit(修改帶有評論的台泥夥伴)");
            Partner partner10 = editPartner(55, 1);
            partner10.setName("name1_revise");
            String result10 = test.postJson("/partner/edit", String.class, partner10);
            System.out.println("result=" + result10);

            //新增
            //新增評分
            System.out.println("Test: partner/comment");
            PartnerComment comment1 = createComment();
            String commentResult1 = test.postJson("/partner/comment", String.class, comment1);
            System.out.println("commentResult=" + commentResult1);
            PartnerComment[] commentList = test.get("/partner/comment/list/65", PartnerComment[].class);
            showResult(commentList);

            //新增評分 (未輸入評分)
            System.out.println("Test: partner/comment(未輸入評分)");
            PartnerComment comment2 = new PartnerComment();
            comment2.setMessage("good");
            Member member = new Member();
            member.setLoginAccount("neo.fu");
            comment2.setMember(member);
            Partner partner1 = new Partner();
            partner1.setId(new Long(65));
            comment2.setPartner(partner1);
            String commentResult2 = test.postJson("/partner/comment", String.class, comment2);
            System.out.println("commentResult=" + commentResult2);

            //新增評分 (未輸入要評分的台泥夥伴)
            System.out.println("Test: partner/comment (未輸入要評分的台泥夥伴)");
            PartnerComment comment3 = createComment();
            comment3.setPartner(null);
            String commentResult3 = test.postJson("/partner/comment", String.class, comment3);
            System.out.println("commentResult=" + commentResult3);

            //新增評分(要評論的台泥夥伴不存在)
            System.out.println("Test: partner/comment (要評論的台泥夥伴不存在)");
            PartnerComment comment5 = createComment();
            Partner notExistsPartner = new Partner();
            notExistsPartner.setId(new Long(1));
            comment5.setPartner(notExistsPartner);
            String commentResult5 = test.postJson("/partner/comment", String.class, comment5);
            System.out.println("commentResult=" + commentResult5);

            //修改
            //修改評分
            System.out.println("Test: partner/comment/edit");
            PartnerComment comment8 = editComment(80);
            String commentResult8 = test.postJson("/partner/comment/edit", String.class, comment8);
            System.out.println("commentResult=" + commentResult8);

            //刪除評分
            System.out.println("Test: partner/comment/remove/{comment_id}");
            String commentResult10 = test.get("/partner/comment/remove/67", String.class);
            System.out.println("commentResult=" + commentResult10);

            //刪除評分 (評分不存在)
            System.out.println("Test: partner/comment/remove/{comment_id} (評分不存在)");
            String commentResult11 = test.get("/partner/comment/remove/60", String.class);
            System.out.println("commentResult=" + commentResult11);

            //取得台泥夥伴
            System.out.println("Test: /partner/{partner_id}");
            Partner returnPartner = test.get("/partner/65", Partner.class);
            System.out.println("partner=" + returnPartner);

            //取得台泥夥伴評分明細
            System.out.println("Test: /partner/comment/list/{partner_id}");
            PartnerComment[] commentList2 = test.get("/partner/comment/list/65", PartnerComment[].class);
            showResult(commentList2);

            //取得販售商品的 partner
            System.out.println("Test: /partner/product/{product_id}");
            Partner[] partnerList = test.get("/partner/product/1", Partner[].class);
            showResult(partnerList);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("e=" + e);
        }
    }
}
