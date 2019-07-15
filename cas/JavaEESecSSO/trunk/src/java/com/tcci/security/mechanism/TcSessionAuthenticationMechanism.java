package com.tcci.security.mechanism;


import java.util.Set;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ex. login by 
 * http://localhost:8080/JavaEESecSSO/faces/protect/home.xhtml?name=admin&password=abcd1234
 * 
 * @author Peter.pan
 */
/*@LdapIdentityStoreDefinition(
   url = "ldap://localhost:33389/",
   callerBaseDn = "ou=caller,dc=jsr375,dc=net",
   groupSearchBase = "ou=group,dc=jsr375,dc=net"
)*/
/*@OAuth2AuthenticationDefinition(
    authEndpoint = "https://accounts.google.com/o/oauth2/auth", 
    tokenEndpoint = "https://www.googleapis.com/oauth2/v4/token",
    clientId = "992703581191-55nvg8ihqst9245uumj7dhct8mtqj1k3.apps.googleusercontent.com", 
    clientSecret = "SsIOgYQh4FvwdIhwkp_V_COt",
    scope="https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email",
    redirectURI = "http://localhost:8080/JavaEESecSSO/Callback"
)*/
// Database Definition for built-in DatabaseIdentityStore
@DatabaseIdentityStoreDefinition(
    dataSourceLookup = "jdbc/testDB", // ${'java:global/MyDS'}",
    callerQuery = "${authFacade.passwordSQL}",
    groupsQuery = "${authFacade.groupsSQL}",
    hashAlgorithm = Pbkdf2PasswordHash.class,
    priorityExpression = "#{100}",
    hashAlgorithmParameters = {
        "Pbkdf2PasswordHash.Iterations=3072",
        "${authFacade.dyna}"
    }
)
@Alternative
@Priority(Interceptor.Priority.APPLICATION+99) // 大=優先
@ApplicationScoped
public class TcSessionAuthenticationMechanism implements HttpAuthenticationMechanism {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Inject
    private IdentityStoreHandler identityStoreHandler;
    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        LOG.debug("validateRequest request = " + request.getRequestURI());

        // 1. for login
        if( request.getRequestURI().indexOf("/loginServlet")>0 
         && request.getParameter("loginAccount") != null 
         && request.getParameter("password") != null) {
            String loginAccount = request.getParameter("loginAccount");
            Password password = new Password(request.getParameter("password"));
            String encrypted = passwordHash.generate("password".toCharArray());
            LOG.debug("validateRequest encrypted = " + encrypted);

            CredentialValidationResult result = identityStoreHandler.validate(new UsernamePasswordCredential(loginAccount, password));
            if (result.getStatus() == VALID) {
                // use session protect
                HttpSession session = request.getSession(true);
                session.setAttribute("loginAccount", loginAccount);
                session.setAttribute("groups", result.getCallerGroups());
                LOG.debug("validateRequest login success .");
                return httpMessageContext.notifyContainerAboutLogin(
                            result.getCallerPrincipal(), result.getCallerGroups());
            } else {
                // 登入失敗
                LOG.debug("validateRequest login fail .");
                redirectToLogin(request, response, "fail");
                return httpMessageContext.doNothing();
            }
        }
        
        // 2. Authorized
        HttpSession session = request.getSession(false);
        if( session!=null && session.getAttribute("loginAccount")!=null &&  session.getAttribute("groups")!=null ){
            String loginAccount = (String)session.getAttribute("loginAccount");
            Set<String> groups = (Set<String>)session.getAttribute("groups");
            LOG.debug("validateRequest Authorized.");
            return httpMessageContext.notifyContainerAboutLogin(loginAccount, groups);
        }
        
        // 3. Unauthorized
        // 無保護
        if( !httpMessageContext.isProtected() ){
            LOG.debug("*** validateRequest No Protected ... ***");
            return httpMessageContext.doNothing();
        }else{
            // 未登入
            if( request.getRequestURI().indexOf("/resources")>0 ){// RESTful
                LOG.debug("Unauthorized RESTful ...");
                return httpMessageContext.responseUnauthorized();
            }else{// HTML
                LOG.debug("Unauthorized Page ...");
                redirectToLogin(request, response, "none");
                return httpMessageContext.doNothing();
            }
        }
    }
    
    /**
     * 導向登入頁
     * @param response
     * @param status 
     */
    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response, String status){
        try{
            response.sendRedirect(request.getContextPath()+"/html5/login.html?status="+status);
        }catch(Exception e){
            LOG.error("redirectToLogin Exception :\n", e);
        }
    }
}
