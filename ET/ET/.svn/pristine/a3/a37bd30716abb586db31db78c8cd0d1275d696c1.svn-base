package com.tcci.security.mechanism;

import com.tcci.security.credential.CasCredential;
import com.tcci.security.CasUtils;
import com.tcci.security.SecurityConfig;
import com.tcci.security.SecurityConstants;
import com.tcci.security.SecurityUtils;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.NOT_VALIDATED;
import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static org.jasig.cas.client.util.AbstractCasFilter.CONST_CAS_ASSERTION;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * use SSO CAS Server Login
 * @author Peter.pan
 */
@Alternative
@Priority(Interceptor.Priority.APPLICATION+100) // 大=優先
@ApplicationScoped
public class TcAuthenticationMechanism implements HttpAuthenticationMechanism {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Inject
    private IdentityStoreHandler identityStoreHandler;
    
    //@Resource(mappedName = "jndi/JavaEESecSSO.config")// 包JAR檔不適用
    private Properties jndiConfig;
    
    public TcAuthenticationMechanism(){
        LOG.debug("init ...");
        jndiConfig = SecurityUtils.getJndiProperties(SecurityConfig.getJndiName());
        LOG.info("TcAuthenticationMechanism jndiConfig = "+jndiConfig);
    }
     
    /**
     * Request 驗證
     * @param request
     * @param response
     * @param context
     * @return
     * @throws AuthenticationException 
     */
    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, 
            HttpMessageContext context) throws AuthenticationException {
        try{
            HttpSession session = request.getSession(false);
            
            if( isLoginCas(session) ){// 已登入過
                CallerPrincipal caller = (CallerPrincipal)session.getAttribute(SecurityConstants.CALLER_ATTR);// CallerPrincipal
                Set<String> groups = (Set<String>)session.getAttribute(SecurityConstants.GROUPS_ATTR);// Set<String>
                //LOG.debug("*** validateRequest has login caller = "+(caller!=null?caller.getName():null));
                //LOG.debug("*** validateRequest has login groups = "+(groups!=null?groups.size():0));

                CredentialValidationResult result = new CredentialValidationResult(caller, groups);
                return context.notifyContainerAboutLogin(result);// 登錄認證與授權資訊至 SecurityContext
            }
            
            // 無保護
            if( !context.isProtected() ){
                LOG.debug("*** validateRequest No Protected ... ***");
                return context.doNothing();
            }
            
            // 需保護的 multipart post 
            // post multipart/form-data 不可執行 request.getParameter 會造成上傳無作用
            if( "POST".equals(request.getMethod()) 
             && request.getContentType()!=null 
             && request.getContentType().startsWith("multipart/form-data;") ){
                return context.responseUnauthorized();
            }
            // 要在 check request.getMethod() 之後再執行 request.getParameter
            String ticket = request.getParameter("ticket");// CAS Service Ticket
            String serviceUrl = CasUtils.constructServiceUrl(request, response, jndiConfig);// 驗證成功後導向 service URL

            // Delegate the {credentials in -> identity data out} function to the Identity Store
            String casServerUrlPrefix = SecurityUtils.getFromSysProps(SecurityConstants.SP_CAS_PREFIX, SecurityConstants.DEF_CAS_PREFIX);
            casServerUrlPrefix = SecurityUtils.getFromJNDI(jndiConfig, SecurityConstants.PN_CAS_PREFIX, casServerUrlPrefix);
            //String jaasConfig = SecurityUtils.getFromJNDI(jndiConfig, SecurityConstants.PN_CAS_JAAS_CONF, SecurityConstants.DEF_CAS_JAAS_CONF);
            //LOG.debug("validateRequest jaasConfig = "+jaasConfig);// for 參考 CAS SAM 的舊作法 (需設定 realm in login.conf)
            Credential credential = new CasCredential(casServerUrlPrefix, ticket, serviceUrl);
            LOG.debug("validateRequest credential = "+credential);
            // validate by identity stores
            CredentialValidationResult result;
            result = identityStoreHandler.validate(credential);

            LOG.debug("validateRequest result.getStatus() ="+result.getStatus());

            if( result.getStatus() == NOT_VALIDATED ){// 未驗證轉址至 CAS 登入頁
                String redirectToCAS = CasUtils.constructRedirectUrl(request, response, jndiConfig);
                LOG.debug("*** validateRequest sendRedirect to CAS redirectToCAS = {}", redirectToCAS);
                if( redirectToCAS!=null ){
                    response.sendRedirect(redirectToCAS);
                    return context.doNothing();
                }
            }else if( result.getStatus() == VALID ){// 驗證成功，儲存登入及授權資訊至 Session
                session = request.getSession(true);
                keepCallerInfoInSession(result, session);
                LOG.debug("*** validateRequest VALID ...");
                if( ticket!=null && !ticket.isEmpty() ){// 重新轉址，去除 URL 中的 Service Ticket
                    LOG.debug("validateRequest: Redirect from CAS, do redirect again for hidding service ticket in url.");
                    response.sendRedirect(serviceUrl);
                }
                return context.notifyContainerAboutLogin(result);// 登錄認證與授權資訊至 SecurityContext
            }
        }catch(IOException e){
            LOG.info("validateRequest IOException:\n", e);
        }
        
        return context.responseUnauthorized();
    }
    
    /**
     * 已登入過
     */
    private boolean isLoginCas(HttpSession session){
        return session!=null 
            && session.getAttribute(CONST_CAS_ASSERTION)!=null
            && session.getAttribute(SecurityConstants.CALLER_ATTR)!=null;
    }
    
    /**
     * 登入資訊儲存於 session
     * @param result
     * @param session 
     */
    private void keepCallerInfoInSession(CredentialValidationResult result, HttpSession session){
        Assertion assertion = new AssertionImpl(result.getCallerPrincipal().getName());
        session.setAttribute(CONST_CAS_ASSERTION, assertion);
        session.setAttribute(SecurityConstants.CALLER_ATTR, result.getCallerPrincipal());// CallerPrincipal
        session.setAttribute(SecurityConstants.GROUPS_ATTR, result.getCallerGroups());// Set<String>
    }
}
