package com.tcci.security;

import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.ExpiredJwtException;
import java.text.ParseException;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.tcci.security.TokenProvider.BEARER;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.ws.rs.core.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author peter.pan
 */
@ApplicationScoped
public class JWTAuthenticationMechanism implements HttpAuthenticationMechanism {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private IdentityStoreHandler identityStoreHandler;
    @Inject
    private TokenProvider tokenProvider;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context) {
        logger.debug("validateRequest ... {} {}", request.getRequestURI(), context.isProtected());
        // 因 HttpAuthenticationMechanism 比 ContainerResponseFilter 更早 implements
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            //logger.debug("validateRequest header = {}", name);
        }
        
        // OPTIONS 沒有 authorization header
        if( !context.isProtected() || "OPTIONS".equals(request.getMethod()) ){
            return context.doNothing();
        }
        
        String token = extractToken(request);
        if (token != null) {
            logger.debug("validateToken ...");
            // validation of the jwt credential
            return validateToken(token, context);
        }
        
        return context.responseUnauthorized();
    }

    /**
     * To extract the JWT from Authorization HTTP header
     *
     * @param context
     * @return The JWT access tokens
     */
    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.debug("extractToken authorizationHeader = {}", authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            String token = authorizationHeader.substring(BEARER.length(), authorizationHeader.length());
            logger.debug("extractToken token = {}", token);
            return token;
        }
        return null;
    }
    
    /**
     * To validate the JWT token e.g Signature check, JWT claims
     * check(expiration) etc
     *
     * @param token The JWT access tokens
     * @param context
     * @return the AuthenticationStatus to notify the container
     */
    private AuthenticationStatus validateToken(String token, HttpMessageContext context) {
        try {
            if (tokenProvider.validateToken(token, true)) {
                JWTCredential credential = tokenProvider.getCredential(token);
                if( credential.getAuthorities()!=null ){
                    for(String role : credential.getAuthorities()){
                        logger.debug("validateToken role = {}", role);
                    }
                }
                CredentialValidationResult result = new CredentialValidationResult(credential.getCaller(), credential.getAuthorities());
                AuthenticationStatus status = context.notifyContainerAboutLogin(result);
                logger.debug("validateToken status = {}", status);
                return status;
            }
            // if token invalid, response with unauthorized status
            return context.responseUnauthorized();
        } catch (ExpiredJwtException ex) {
            logger.error("validateToken ExpiredJwtException \n", ex.getMessage());
            logger.debug("validateToken ExpiredJwtException \n", ex);
            return context.responseUnauthorized();
        } catch (ParseException ex) {
            logger.error("validateToken ParseException \n", ex.getMessage());
            logger.debug("validateToken ParseException \n", ex);
            return context.responseUnauthorized();
        } catch (JOSEException ex) {
            logger.error("validateToken JOSEException \n", ex.getMessage());
            logger.debug("validateToken JOSEException \n", ex);
            return context.responseUnauthorized();
        }
    }
}
