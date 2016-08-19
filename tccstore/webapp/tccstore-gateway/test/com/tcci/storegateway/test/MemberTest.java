/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

import com.tcci.tccstore.model.member.CreditInfo;
import com.tcci.tccstore.model.member.Member;
import com.tcci.tccstore.model.member.MemberCredit;
import com.tcci.tccstore.model.member.MemberReward;
import com.tcci.tccstore.model.member.RewardLog;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jimmy.Lee
 */
public class MemberTest extends TestBase {

    public static void main(String[] args) throws SSOClientException {
        MemberTest test = new MemberTest();
        test.login("c1", "admin");
        
        //test.member();
        test.customerCredit();
        //test.reward();
        test.credit();
        //test.bonusList();
        //test.goldList();
        //test.resetPassword();
        //test.forgotPassword();

    }

    public void member() {
        String service = "/member";
        this.executeGet(service, Member.class, "使用者資料");
    }
    
    public void customerCredit() {
        String service = "/member/customercredit/3071";
        this.executeGet(service, CreditInfo[].class, "帳戶餘額");
    }

    public void reward() {
        String service = "/member/reward";
        this.executeGet(service, MemberReward.class, "使用者紅利金幣");
    }
    
    public void credit() {
        String service = "/member/credit/3071";
        this.executeGet(service, MemberCredit.class, "使用者紅利金幣及帳戶餘額");
    }
    
    public void bonusList() {
        String service = "/member/bonuslist?year_month=201603";
        this.executeGet(service, RewardLog[].class, "使用者紅利記錄");
    }

    public void goldList() {
        String service = "/member/goldlist?year_month=201601";
        this.executeGet(service, RewardLog[].class, "使用者金幣記錄");
    }
    
    public void resetPassword() {
        String service = "/ecsso/resetPassword";
        Map<String, String> form = new HashMap<>();
        form.put("old_password", "admin");
        form.put("new_password", "admin");
        this.executeForm(service, form, String.class, "重設密碼");
    }

    public void forgotPassword() {
        String service = "/ecsso/forgotPassword";
        Map<String, String> form = new HashMap<>();
        form.put("account", "gilbert.lin");
        this.executeForm(service, form, String.class, "忘計密碼");
    }

}
