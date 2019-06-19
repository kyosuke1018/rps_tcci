package com.tcci.cm.facade.rs.filter;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.WebUtils;
import com.tcci.ec.facade.EcSessionFacade;
import com.tcci.fc.util.DateUtils;
import com.tcci.security.TokenProvider;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Date;
import javax.ejb.EJB;
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

    @Inject
    private TokenProvider tokenProvider;
    //@Inject
    //private KeyGenerator keyGenerator;
    @EJB EcSessionFacade sessionFacade;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if( !GlobalConstant.JWT_ENABLED ){
            return;
        }
        try {
            logger.debug("JWTTokenNeededFilter filter start : "+DateUtils.format(new Date()));
            String token = null;
            String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            if( authHeader!=null && authHeader.startsWith(TokenProvider.BEARER) ){
                token = authHeader.substring(TokenProvider.BEARER.length());
                //logger.debug("filter token = \n"+token);
                if( tokenProvider.validateToken(token, true) ){// JWT validate (含JWT 過期驗證)
                    logger.debug("filter tokenProvider.validateToken pass ...");
                    // 其他自訂檢查
                    if( httpServletRequest!=null ){
                        // 1. Client IP 檢核
                        String fromIP = WebUtils.getClientIP(httpServletRequest);
                        logger.debug("filter fromIP = " + fromIP);
                        if( fromIP!=null && fromIP.equals(tokenProvider.getAudience(token)) ){
                            logger.debug("filter check IP pass ...");
                            // 2. 自訂 Session Key 檢核
                            Long memberId = (Long)tokenProvider.getClaim(token, TokenProvider.MEMBER_ID);
                            String session = (String)tokenProvider.getClaim(token, TokenProvider.SESSION_KEY);
                            if( sessionFacade.checkSession(memberId, session) ){
                                logger.debug("filter check Session pass ...");
                                // 是否需 RefreshToken
                                if( tokenProvider.canRefreshToken(token) ){
                                    logger.debug("filter do refresh token ...");
                                    // 通知後續換 KEY
                                    httpServletRequest.setAttribute(GlobalConstant.JWT_REFRESH_FLAG, Boolean.TRUE);
                                }
                                logger.debug("JWTTokenNeededFilter filter end : "+DateUtils.format(new Date()));
                                return;
                            }else{
                                logger.error("filter check Session fail !!!");
                            }
                        }else{
                            logger.error("filter check IP fail !!!");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("filter Exception:\n", e);
        }
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
}