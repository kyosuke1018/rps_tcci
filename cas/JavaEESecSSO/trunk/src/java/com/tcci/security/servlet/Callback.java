package com.tcci.security.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fish.payara.security.oauth2.api.OAuth2AccessToken;// in payara-api.jar
import fish.payara.security.openid.api.OpenIdClaims;
import fish.payara.security.openid.api.OpenIdContext;
import java.util.Enumeration;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://localhost:8080/JavaEESecSSO/Callback
 * @author jonathan coustick
 */
@WebServlet("/Callback")
public class Callback extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    
    @Inject
    private SecurityContext securityContext;
    @Inject
    OAuth2AccessToken token;
    @Inject
    private OpenIdContext context;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("Callback doGet ...");
        LOG.debug("Callback doGet token.getAccessToken = "+token.getAccessToken());

        String loginUser = null;
        if (request.getUserPrincipal() != null) {
            loginUser = request.getUserPrincipal().getName();
        }
        response.getWriter().println("OAuth2 authorization success ..."+loginUser);
        
        //response.getWriter().println(token.getAccessToken());
        response.getWriter().println("==== OpenId Connect ====");
        response.getWriter().println("== AccessToken ==");
        response.getWriter().println(context.getAccessToken());
        response.getWriter().println("== IdentityToken ==");
        response.getWriter().println(context.getIdentityToken());
        response.getWriter().println("== Subject ==");
        response.getWriter().println(context.getSubject());
        response.getWriter().println("== Claims ==");
        OpenIdClaims claims = context.getClaims();
        if( claims!=null ){
            response.getWriter().println("name = " + claims.getName());
            response.getWriter().println("email = " + claims.getEmail());
            response.getWriter().println("getProfile = " + claims.getProfile());
        }
    }
    
    private void printHeaders(HttpServletRequest request){
        LOG.debug("Callback doGet headers ...");
        Enumeration headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
          String headerName = (String)headerNames.nextElement();
          LOG.debug("doGet header: " + headerName + " = " + request.getHeader(headerName));
        }
    }
    
    private void printSessionAttrs(HttpServletRequest request){
        LOG.debug("Callback doGet session ...");
        HttpSession session = request.getSession();
        if( session!=null ){
            LOG.debug("Callback doGet session.getId ..."+session.getId());
            Enumeration names = session.getAttributeNames();
            while(names.hasMoreElements()) {
              String name = (String)names.nextElement();
              LOG.debug("doGet session: " + name + " = " + session.getAttribute(name));
            }
        }
    }
}
