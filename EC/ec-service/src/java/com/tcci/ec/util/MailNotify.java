/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.util;

import com.tcci.fc.util.VelocityMail;
import java.util.HashMap;

/**
 *
 * @author Jimmy.Lee
 */
public class MailNotify {

    public static void forgotPassword(String email, String password) {
        HashMap<String, Object> mailBean = new HashMap<>();
        String subject = "[台泥电商] 密码通知信";
        mailBean.put(VelocityMail.SUBJECT, subject);
        mailBean.put(VelocityMail.TO, email);
        mailBean.put("title", subject);
        mailBean.put("newPassword", password);
        VelocityMail.sendMail(mailBean, "/mail_forgotPassword.vm");
    }

    public static void memberRegister(String email, String password) {
        HashMap<String, Object> mailBean = new HashMap<>();
        String subject = "[台泥电商] 注册成功通知信";
        mailBean.put(VelocityMail.SUBJECT, subject);
        mailBean.put(VelocityMail.TO, email);
        mailBean.put("title", subject);
        mailBean.put("newPassword", password);
        VelocityMail.sendMail(mailBean, "/mail_memberRegister.vm");
    }

    /* 改由後台批次通知
    public static void orderCreate(EcOrder ecOrder, String to, String orderLink) {
        // mail notify
        HashMap<String, Object> mailBean = new HashMap<>();
        String subject = "[台泥电商] 新增订单待审核";
        mailBean.put(VelocityMail.SUBJECT, subject);
        mailBean.put(VelocityMail.TO, to);
        mailBean.put("ecOrder", ecOrder);
        mailBean.put("orderLink", orderLink);
        VelocityMail.sendMail(mailBean, "/mail_orderCreate.vm");
    }
    */

//    public static void formCustomerApply(EcForm ecForm, String province, String contact) {
//        // String to = approverByProvince(province);
//        HashMap<String, Object> mailBean = new HashMap<>();
//        String subject = "[台泥电商] 申请成为签约客户";
//        mailBean.put(VelocityMail.SUBJECT, subject);
//        mailBean.put(VelocityMail.TO, contact);
//        mailBean.put("title", subject);
//        mailBean.put("ecForm", ecForm);
//        mailBean.put("province", province);
//        VelocityMail.sendMail(mailBean, "/mail_formCustomerApply.vm");
//    }

//    public static void partnerApply(EcPartner ecPartner, String approveLink, String contact) {
//        // String to = approverByProvince(ecPartner.getProvince());
//        HashMap<String, Object> mailBean = new HashMap<>();
//        String subject = "[台泥电商] 申请成为台泥伙伴";
//        mailBean.put(VelocityMail.SUBJECT, subject);
//        mailBean.put(VelocityMail.TO, contact);
//        mailBean.put("title", subject);
//        mailBean.put("ecPartner", ecPartner);
//        mailBean.put("approveLink", approveLink);
//        VelocityMail.sendMail(mailBean, "/mail_partnerApply.vm");
//    }
    
//    public static void partnerCommentApprove(EcPartnerComment comment, String approveLink, String contact) {
//        // String to = approverByProvince(comment.getEcPartner().getProvince());
//        HashMap<String, Object> mailBean = new HashMap<>();
//        String subject = "[台泥电商] 台泥伙伴评语审核";
//        mailBean.put(VelocityMail.SUBJECT, subject);
//        mailBean.put(VelocityMail.TO, contact);
//        mailBean.put("title", subject);
//        mailBean.put("comment", comment);
//        mailBean.put("approveLink", approveLink);
//        VelocityMail.sendMail(mailBean, "/mail_partnerCommentApprove.vm");
//    }

    /*
    private static String approverByProvince(String province) {
        // 广东省: 通知 邹琳玲(zoulinling@taiwancement.com)
        // 广西壮族自治区: 通知 何敏(hemin@taiwancement.com)
        // 其它: 通知 宋正順(andy.sung@taiwancement.com)
        String approver = "广东省".equals(province) ? "zoulinling@taiwancement.com"
                : "广西壮族自治区".equals(province) ? "hemin@taiwancement.com" : "andy.sung@taiwancement.com";
        return approver;
    }
    */

}
