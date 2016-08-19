/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.service;

import com.tcci.ecdemo.entity.EcMember;
import com.tcci.ecdemo.facade.member.EcMemberFacade;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@HMACAuth
@Provider
@Priority(Priorities.AUTHORIZATION)
public class HMACRequestFilter implements ContainerRequestFilter {

    private final static Logger logger = LoggerFactory.getLogger(HMACRequestFilter.class);

    @Context
    ResourceInfo resourceInfo;

    @EJB
    private EcMemberFacade ecMemberFacade;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        logger.debug("HMACRequestFilter...");
        // IMPORTANT!!! First, Acknowledge any pre-flight test from browsers for this case before validating the headers (CORS stuff)
        if (requestContext.getRequest().getMethod().equals("OPTIONS")) {
            requestContext.abortWith(Response.status(Response.Status.OK).build());
            return;
        }
        // is Authentication required?
        AuthType authType = AuthType.REQUIRED;
        Method resourceMethod = resourceInfo.getResourceMethod();
        HMACAuth annotation = resourceMethod.getAnnotation(HMACAuth.class);
        if (annotation == null) {
            Class<?> resourceClass = resourceInfo.getResourceClass();
            annotation = resourceClass.getAnnotation(HMACAuth.class);
        }
        if (annotation != null) {
            authType = annotation.authType();
        }
        logger.debug("authType:{}", authType);
        boolean authenticated = authenticate(requestContext);
        if (authType == AuthType.REQUIRED && !authenticated) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    // HMCA Authorization header
    // Authorization:jimmy.lee:+9tn0CLfxXFbzPmbYwq/KYuUSUI=
    private boolean authenticate(ContainerRequestContext requestContext) throws IOException {
        String authorization = requestContext.getHeaderString("Authorization");
        if (authorization == null) {
            return false;
        }
        // TODO:驗證 name 及 token
        String[] nameToken = authorization.split(":", 2);
        if (nameToken.length != 2) {
            return false;
        }
        if (!"HMAC-SIGNED-TOKEN".equals(nameToken[1])) {
            return false;
        }
        EcMember authUser = ecMemberFacade.findActiveByLoginAccount(nameToken[0]);
        if (null == authUser) {
            return false;
        }
        // service 可以從 servletRequest.getAttribute("AuthMember") 取得 authUser
        requestContext.setProperty("AuthMember", authUser);
        return true;
    }

}
