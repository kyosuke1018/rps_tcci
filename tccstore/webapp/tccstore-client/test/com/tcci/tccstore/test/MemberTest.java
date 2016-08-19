/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.test;

import com.tcci.tccstore.client.SSOClientException;
import com.tcci.tccstore.model.member.Member;
import com.tcci.tccstore.model.member.MemberCredit;
import com.tcci.tccstore.model.member.RewardLog;

/**
 *
 * @author Jimmy.Lee
 */
public class MemberTest extends TestBase {

    public static void main(String[] args) throws SSOClientException {
        MemberTest test = new MemberTest();
        test.login("c1", "admin");
        String service = "/member";
        System.out.println(service);
        Member member = test.get(service, Member.class);
        System.out.println(test.toJson(member));

        /*
        service = "/member/customercredit/2";
        System.out.println(service);
        CreditInfo[] creditInfos = test.get(service, CreditInfo[].class);
        System.out.println(test.toJson(creditInfos));

        service = "/member/reward";
        System.out.println(service);
        MemberReward memberReward = test.get(service, MemberReward.class);
        System.out.println(test.toJson(memberReward));
        */
        
        service = "/member/credit/2";
        System.out.println(service);
        MemberCredit memberCredit = test.get(service, MemberCredit.class);
        System.out.println(test.toJson(memberCredit));

        service = "/member/bonuslist";
        System.out.println(service);
        RewardLog[] logs = test.get(service, RewardLog[].class);
        System.out.println(test.toJson(logs));

        service = "/member/goldlist";
        System.out.println(service);
        logs = test.get(service, RewardLog[].class);
        System.out.println(test.toJson(logs));
    }

}
