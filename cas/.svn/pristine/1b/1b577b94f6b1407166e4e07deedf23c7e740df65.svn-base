/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cas.sam;

import com.google.gson.Gson;
import static javax.security.auth.message.AuthStatus.SEND_CONTINUE;
import static javax.security.auth.message.AuthStatus.SEND_FAILURE;
import static javax.security.auth.message.AuthStatus.SEND_SUCCESS;
import static javax.security.auth.message.AuthStatus.SUCCESS;
import static org.jasig.cas.client.util.AbstractCasFilter.CONST_CAS_ASSERTION;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.jaas.AssertionPrincipal;
import org.jasig.cas.client.jaas.ServiceAndTicketCallbackHandler;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import com.tcci.cas.oauth.profile.BaseProfile;

/**
 *
 * @author Jason.Yu
 */
public class CasServerAuthModule implements ServerAuthModule {

    public static final String CONST_CAS_GROUPS = "_const_cas_groups_";
    public static final String CONST_CAS_OAUTH_PROFILE = "_const_cas_oauth_profile_";
    private static final String AUTH_TYPE_INFO_KEY = "javax.servlet.http.authType";
    private static final String PROPERTY_SERVER_NAME = "serverName";
    private static final String PROPERTY_SERVICE = "service";
    private static final String PROPERTY_ARTIFACT_PARAMATER_NAME = "artifactParameterName";
    private static final String PROPERTY_SERVICE_PARAMATER_NAME = "serviceParameterName";
    private static final String PROPERTY_CAS_SERVER_URL_PREFIX = "casServerUrlPrefix";
    private static final String PROPERTY_CAS_SERVER_LOGIN_URL = "casServerLoginUrl";
    private static final String PROPERTY_JAAS_CONTEXT = "jaas-context";
    private static final String PROPERTY_JNDI_DATASOURCE = "jndi-dataSource";
    private static final String PROPERTY_DB_USER = "db-user";
    private static final String PROPERTY_DB_PASSWORD = "db-password";
    private static final String PROPERTY_QUERY_GROUP = "query-group";
    //private static final String PROPERTY_DEFAULT_GROUPS = "defaultGroups";
    //private static final String PROPERTY_GROUP_ATTRIBUTE_NAMES = "groupAttributeNames";
    private static final String PROPERTY_DEFAULT_GROUP = "defaultGroup";
    private static final Class[] supportedMessageTypes = new Class[]{
        HttpServletRequest.class, HttpServletResponse.class};
    private static final Logger logger = Logger.getLogger(CasServerAuthModule.class.getName());
    private MessagePolicy requestPolicy;
    private MessagePolicy responsePolicy;
    private CallbackHandler handler;
    private Map options;
    private String service = null;
    private String serverName = null;
    private String artifactParameterName = "ticket";
    private String serviceParameterName = "service";
    private boolean encodeServiceUrl = false;
    private boolean redirectAfterValidation = true;
    private String casServerUrlPrefix = null;
    private String casServerLoginUrl = null;
    private boolean renew = false;
    private boolean gateway = false;
    private String jaasContext = "cas";
    //private String defaultGroups[] = null;
    //private String groupAttributeNames[] = null;
    private String defaultGroup = null;
    private LoginContext lc;
    private String jndiDataSource;
    private String dbUser;
    private String dbPassword;
    private String queryGroup;
    //private static final SingleSignOutHandler ssoHandler = new SingleSignOutHandler();

    public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler, Map options) throws AuthException {
        this.requestPolicy = requestPolicy;
        this.responsePolicy = responsePolicy;
        this.handler = handler;
        this.options = options;

        if (options != null) {
            //ssoHandler.setArtifactParameterName( (String)options.get("artifactParameterName") );
            //ssoHandler.setLogoutParameterName( (String)options.get("logoutParameterName") );
            this.service = (String) options.get(PROPERTY_SERVICE);
            this.serverName = (String) options.get(PROPERTY_SERVER_NAME);
//            if (this.serverName == null) {
//                serverName = getServerName();
//            }
            this.casServerUrlPrefix = (String) options.get(PROPERTY_CAS_SERVER_URL_PREFIX);
            this.casServerLoginUrl = (String) options.get(PROPERTY_CAS_SERVER_LOGIN_URL);
            if (options.containsKey(PROPERTY_JAAS_CONTEXT)) {
                this.jaasContext = (String) options.get(PROPERTY_JAAS_CONTEXT);
            }
            if (options.containsKey(PROPERTY_DEFAULT_GROUP)) {
                defaultGroup = (String) options.get(PROPERTY_DEFAULT_GROUP);
            }
            if (options.containsKey(PROPERTY_JNDI_DATASOURCE)) {
                jndiDataSource = (String) options.get(PROPERTY_JNDI_DATASOURCE);
            }
            if (options.containsKey(PROPERTY_DB_USER)) {
                dbUser = (String) options.get(PROPERTY_DB_USER);
            }
            if (options.containsKey(PROPERTY_DB_PASSWORD)) {
                dbPassword = (String) options.get(PROPERTY_DB_PASSWORD);
            }
            if (options.containsKey(PROPERTY_QUERY_GROUP)) {
                queryGroup = (String) options.get(PROPERTY_QUERY_GROUP);
            }
            logger.finest("groupQuery=" + queryGroup);
            //ssoHandler.init();
            /*
            if (options.containsKey(PROPERTY_DEFAULT_GROUPS)) {
            String value = (String) options.get(PROPERTY_DEFAULT_GROUPS);
            if (value != null) {
            this.defaultGroups = value.split(",\\s*");
            }
            }
            if (options.containsKey(PROPERTY_GROUP_ATTRIBUTE_NAMES)) {
            String value = (String) options.get(PROPERTY_GROUP_ATTRIBUTE_NAMES);
            if (value != null) {
            this.groupAttributeNames = value.split(",\\s*");
            }
            }
             */
        }
    }

    public Class[] getSupportedMessageTypes() {
        return supportedMessageTypes;
    }

    public AuthStatus validateRequest(MessageInfo msgInfo,
            Subject clientSubject, Subject serverSubject) throws AuthException {
        HttpServletRequest request = (HttpServletRequest) msgInfo.getRequestMessage();
        HttpServletResponse response = (HttpServletResponse) msgInfo.getResponseMessage();
        HttpSession session = request.getSession();
        /*
        if (ssoHandler.isTokenRequest(request)) {
            System.out.println("validateRequest isTokenRequest");
            ssoHandler.recordSession(request);
        } else if (ssoHandler.isLogoutRequest(request)) {
            System.out.println("validateRequest destroySession");
            ssoHandler.destroySession(request);
            // Do not continue up filter chain
            //return;
        } else {
            System.out.println("validateRequest else");
            logger.finest("Ignoring URI " + request.getRequestURI());
        }
         * 
         */
 /*
        System.out.println("validateRequest start session=" + session  );
        if( session != null )
        System.out.println("validateRequest start session=" + session  +",sessionId=" + session.getId() );
        //System.out.println("validateRequest start clientSubject=" + clientSubject +",serverSubject=" + serverSubject );
        /*
        if( clientSubject != null ){
            for (Principal p : clientSubject.getPrincipals()) 
                System.out.println("clientSubject p=" + p );
        }
        if( serverSubject != null ){
            for (Principal p : serverSubject.getPrincipals()) 
                System.out.println("serverSubject p=" + p );
        }
         */
        //{{ Jimmy 20150727, POST/PUT 不要redirect
        String method = request.getMethod();
        boolean isPostOrPut = "POST".equals(method) || "PUT".equals(method);
        boolean responseRedirect = this.redirectAfterValidation && !isPostOrPut;
        //}}
        try {
            Assertion assertion = (Assertion) session.getAttribute(CONST_CAS_ASSERTION);
            logger.finest("validateRequest validateRequest start =" + assertion);
            if (assertion != null) {
                if (/*this.redirectAfterValidation*/responseRedirect) {
                    String ticket = getTicket(request);
                    logger.finest("assertion != null ticket =" + ticket);
                    if (ticket != null) {
                        logger.fine("Redirecting after check assertion.");
                        logger.finest("validateRequest constructServiceUrl =" + constructServiceUrl(request, response));
                        response.sendRedirect(constructServiceUrl(request,
                                response));
                        return SEND_CONTINUE;
                    }
                }
                setAuthenticationResult(request, assertion, clientSubject, msgInfo);
                return SUCCESS;
            }
            /* Jimmy 20160204, 未保護的request無法取得principal.
             * 如果有ticket仍應認證
            if (!this.requestPolicy.isMandatory()) {
                return SUCCESS;
            }
             */
            String ticket = getTicket(request);
            logger.finest("ticket =" + ticket);
            if (ticket == null || ticket.length() == 0) {
                if (this.requestPolicy.isMandatory()) {
                    logger.finest("validateRequest constructRedirectUrl =" + constructRedirectUrl(request, response));
                    response.sendRedirect(constructRedirectUrl(request, response));
                    return SEND_CONTINUE;
                } else {
                    return SUCCESS;
                }
            }
            String serviceUrl = constructServiceUrl(request, response);
            this.lc = new LoginContext(this.jaasContext,
                    new ServiceAndTicketCallbackHandler(serviceUrl, ticket));
            lc.login();
            logger.finest("logincontext done!");
            Subject subject = lc.getSubject();
            for (Principal p : subject.getPrincipals()) {
                if (p instanceof AssertionPrincipal) {
                    assertion = ((AssertionPrincipal) p).getAssertion();
                    session.setAttribute(CONST_CAS_ASSERTION, assertion);
                    logger.finest("session.setAttribute assertion=" + assertion);
                    if (/*this.redirectAfterValidation*/responseRedirect) {
                        logger.fine("Redirecting after successful ticket validation.");
                        logger.finest("validateRequest login constructServiceUrl =" + constructServiceUrl(request, response));
                        response.sendRedirect(constructServiceUrl(request,
                                response));
                        return SEND_CONTINUE;
                    }
                    setAuthenticationResult(request, assertion, clientSubject, msgInfo);
                    return SUCCESS;
                }
            }
            return SEND_FAILURE;
        } catch (Exception e) {
            if (e.getCause() instanceof TicketValidationException) {
                logger.warning(e.getMessage());
                try {
                    logger.fine("Redirecting because of an invalid ticket.");
                    response.sendRedirect(constructRedirectUrl(request,
                            response));
                    return SEND_CONTINUE;
                } catch (IOException ioe) {
                    logger.throwing(CasServerAuthModule.class.getName(),
                            "validateRequest", ioe);
                }
            }
            logger.throwing(CasServerAuthModule.class.getName(),
                    "validateRequest", e);
            AuthException ae = new AuthException();
            ae.initCause(e);
            throw ae;
        }
    }

    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        return SEND_SUCCESS;
    }

    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
        if (subject != null) {
            subject.getPrincipals().clear();
        }
        if (this.lc != null) {
            try {
                this.lc.logout();
            } catch (LoginException e) {
                logger.throwing(CasServerAuthModule.class.getName(),
                        "cleanSubject", e);
                AuthException ae = new AuthException();
                ae.initCause(e);
                throw ae;
            }
        }
    }

    private String getTicket(HttpServletRequest request) {
        String ticket = CommonUtils.safeGetParameter(request, this.artifactParameterName);
        return ticket;
    }

    @SuppressWarnings("unchecked")
    private void setAuthenticationResult(HttpServletRequest request, Assertion assertion, Subject subject,
            MessageInfo m) throws IOException, UnsupportedCallbackException, Exception {
        try {
            if (assertion != null) {
                HttpSession session = request.getSession();
                Principal principal = assertion.getPrincipal();
                logger.fine("setAuthenticationResult start principal=" + principal.getName());
                this.handler.handle(new Callback[]{new CallerPrincipalCallback(
                    subject, principal)});
                m.getMap().put(AUTH_TYPE_INFO_KEY,
                        CasServerAuthModule.class.getName());
                // List groupList = new ArrayList(); // findbugs: DLS_DEAD_LOCAL_STORE
                String[] sessionGroups = (String[]) session.getAttribute(CONST_CAS_GROUPS);
                boolean groupFromSession = false;
                // logger.fine("setAuthenticationResult sessionGroups=" + sessionGroups); // findbugs: DMI_INVOKING_TOSTRING_ON_ARRAY
                List groups = new ArrayList();
                if (sessionGroups == null && queryGroup != null) {
                    sessionGroups = GroupUtil.getGroups(jndiDataSource, dbUser, dbPassword, queryGroup, assertion.getPrincipal().getName(), defaultGroup);
                    groupFromSession = false;
                } else {
                    groupFromSession = true;
                }
                if (sessionGroups != null) {
                    groups.addAll(Arrays.asList(sessionGroups));
                }
                if (groups.size() > 0) {

                    String[] group = new String[groups.size()];
                    group = (String[]) groups.toArray(group);
                    if (!groupFromSession) {
                        session.setAttribute(CONST_CAS_GROUPS, group);
                    }
                    this.handler.handle(new Callback[]{new GroupPrincipalCallback(
                        subject, group)});
                }
                //return profile to client application.
                if (principal instanceof AttributePrincipal) {
                    Map<String, Object> attributes = ((AttributePrincipal) principal).getAttributes();
//                    for (String key : attributes.keySet()) {
//                        System.out.println("attributes(" + key + ")=" + attributes.get(key));
//                        //logger.fine("attributes(" + key + ")=" + attributes.get(key));
//                    }
//                    System.out.println("principal.getName()=" + principal.getName());
                    BaseProfile profile = new BaseProfile(attributes);
                    String jsonProfile = new Gson().toJson(profile);
//                    System.out.println("jsonProfile=" + jsonProfile);
                    request.setAttribute(CONST_CAS_OAUTH_PROFILE, jsonProfile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private String constructServiceUrl(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("this.serverName=" + this.serverName);
        if (this.serverName == null) {
            String forward = request.getHeader("x-forwarded-host");
            //System.out.println("forward=" + forward);
            String host = request.getHeader("host");
            //System.out.println("host=" + host);
            this.serverName = forward != null ? forward : host;
        }
        //System.out.println("this.serverName=" + this.serverName);
        return CommonUtils.constructServiceUrl(request,
                response,
                this.service,
                this.serverName,
                this.artifactParameterName,
                this.encodeServiceUrl);
    }

    private String constructRedirectUrl(HttpServletRequest request,
            HttpServletResponse response) {
        return CommonUtils.constructRedirectUrl(this.casServerLoginUrl,
                this.serviceParameterName,
                constructServiceUrl(request, response), this.renew,
                this.gateway);
    }

    private String getServerName() {
        String name = null;
        try {
            java.net.InetAddress server = java.net.InetAddress.getLocalHost();
            name = server.getCanonicalHostName();
            //System.out.println("getServerName name=" + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
}
