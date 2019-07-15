package com.tcci.security.mechanism;


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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ex. login by 
 * http://localhost:8080/JavaEESecSSO/faces/protect/home.xhtml?name=admin&password=abcd1234
 * 
 * @author Peter.pan
 */
// Database Definition for built-in DatabaseIdentityStore
/*@DatabaseIdentityStoreDefinition(
    dataSourceLookup = "jdbc/testDB", // ${'java:global/MyDS'}",
    callerQuery = "${authFacade.passwordSQL}",
    groupsQuery = "${authFacade.groupsSQL}",
    hashAlgorithm = Pbkdf2PasswordHash.class,
    priorityExpression = "#{100}",
    hashAlgorithmParameters = {
        "Pbkdf2PasswordHash.Iterations=3072",
        "${authFacade.dyna}"
    }
)*/
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
@Alternative
@Priority(Interceptor.Priority.APPLICATION+99) // 大=優先
@ApplicationScoped
public class SimpleAuthenticationMechanism {// implements HttpAuthenticationMechanism {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Inject
    private IdentityStoreHandler identityStoreHandler;

    //@Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        LOG.debug("validateRequest ...");

        if (request.getParameter("name") != null && request.getParameter("password") != null) {
            String name = request.getParameter("name");
            Password password = new Password(request.getParameter("password"));

            // Delegate the {credentials in -> identity data out} function to the Identity Store
            CredentialValidationResult result = identityStoreHandler.validate(new UsernamePasswordCredential(name, password));
            if (result.getStatus() == VALID) {
                return httpMessageContext.notifyContainerAboutLogin(
                            result.getCallerPrincipal(), result.getCallerGroups());
            } else {
                return httpMessageContext.responseUnauthorized();
            }
        }else{
            // 無保護
            if( !httpMessageContext.isProtected() ){
                LOG.debug("*** validateRequest No Protected ... ***");
                return httpMessageContext.doNothing();
            }
        } 

        return httpMessageContext.doNothing();
    }
}
