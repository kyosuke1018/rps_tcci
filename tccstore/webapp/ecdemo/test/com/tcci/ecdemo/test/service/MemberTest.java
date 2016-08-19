/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.test.service;

import com.tcci.ecdemo.model.member.Member;
import javax.ws.rs.core.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class MemberTest {

    private final static Logger logger = LoggerFactory.getLogger(MemberTest.class);

    public static void main(String[] args) {
        logger.debug("Test: member/login success");
        Form form = new Form();
        form.param("username", "jimmy.lee");
        form.param("password", "abcd1234");
        Member member = ClientUtil.post(Member.class, "member/login", null, form);
        showResult(member);

        logger.debug("Test: member/login fail");
        form = new Form();
        form.param("username", "jimmy.lee");
        form.param("password", "wrongpassword");
        member = ClientUtil.post(Member.class, "member/login", null, form);
        showResult(member);

        logger.debug("Test: member/reload success");
        member = ClientUtil.get(Member.class, "member/reload", "jimmy.lee:HMAC-SIGNED-TOKEN");
        showResult(member);

        logger.debug("Test: member/reload fail");
        member = ClientUtil.get(Member.class, "member/reload", "jjimmy.lee:BAD-TOKEN");
        showResult(member);
    }

    public static void showResult(Member member) {
        if (null == member) {
            logger.debug("empty");
        } else {
            logger.debug("{} {}", member.getAccount(), member.getName());
        }
        logger.debug("--------------------");
    }
}
