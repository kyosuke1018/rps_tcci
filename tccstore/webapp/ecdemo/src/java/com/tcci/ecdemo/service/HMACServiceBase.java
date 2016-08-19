/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.service;

import com.tcci.ecdemo.entity.EcMember;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@HMACAuth(authType = AuthType.REQUIRED)
public class HMACServiceBase {

    protected final static Logger logger = LoggerFactory.getLogger(HMACServiceBase.class);

    @Context
    protected HttpServletRequest servletRequest;

    public EcMember getAuthMember() {
        return (EcMember) servletRequest.getAttribute("AuthMember");
    }

}
