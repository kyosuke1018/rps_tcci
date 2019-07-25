package com.tcci.cm.facade.rs.filter;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.WebUtils;
//import com.tcci.security.TokenProvider;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {
    private final static Logger logger = LoggerFactory.getLogger(JWTTokenNeededFilter.class);
    @Context
    private HttpServletRequest httpServletRequest;

//    @Inject
//    private TokenProvider tokenProvider;
    //@Inject
    //private KeyGenerator keyGenerator;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if( !GlobalConstant.JWT_ENABLED ){
            return;
        }
    }
    /*
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if( !GlobalConstant.JWT_ENABLED ){
            return;
        }
        try {
            String token = null;
            String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            if( authHeader!=null && authHeader.startsWith(TokenProvider.BEARER) ){
                token = authHeader.substring(TokenProvider.BEARER.length());
                //logger.debug("filter token = \n"+token);
                if( tokenProvider.validateToken(token, true) ){// JWT validate
                    logger.debug("filter tokenProvider.validateToken pass ...");
                    if( httpServletRequest!=null ){
                        String fromIP = WebUtils.getClientIP(httpServletRequest);
                        logger.debug("filter fromIP = " + fromIP);
                        if( fromIP!=null && fromIP.equals(tokenProvider.getAudience(token)) ){
                            logger.debug("filter check IP pass ...");
                            if( tokenProvider.canRefreshToken(token) ){
                                httpServletRequest.setAttribute(GlobalConstant.JWT_REFRESH_FLAG, Boolean.TRUE);
                            }
                            return;
                        }else{
                            logger.debug("filter check IP fail !!!");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("filter Exception:\n", e);
        }
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
    */
}