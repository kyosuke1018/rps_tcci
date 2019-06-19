/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security;

import com.tcci.ec.enums.SysRoleEnum;
import java.io.IOException;
import java.security.Principal;
import java.util.Properties;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.NOT_VALIDATED;
import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static org.jasig.cas.client.util.AbstractCasFilter.CONST_CAS_ASSERTION;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@ApplicationScoped
public class TcAuthenticationMechanism implements HttpAuthenticationMechanism {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Inject
    private IdentityStoreHandler identityStoreHandler;
    
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
            
            if( isLogin(session) ){// 已登入過
                CallerPrincipal caller = (CallerPrincipal)session.getAttribute(SecurityConstants.CALLER_ATTR);// CallerPrincipal
                Set<String> groups = (Set<String>)session.getAttribute(SecurityConstants.GROUPS_ATTR);// Set<String>
                LOG.debug("*** validateRequest has login caller = "+(caller!=null?caller.getName():null));
                
                CredentialValidationResult result = new CredentialValidationResult(caller, groups);
                return context.notifyContainerAboutLogin(result);// 登錄認證與授權資訊至 SecurityContext
            }

            if( request.getUserPrincipal()!=null ){
                Principal p = request.getUserPrincipal();
                LOG.debug("*** validateRequest adaccount {}"+p.getName());
                LOG.debug("*** validateRequest isUserInRole {}"+request.isUserInRole("valid-users"));
            }
            if (request.getParameter("loginAccount") != null && request.getParameter("password") != null) {
                String name = request.getParameter("loginAccount");
                Password password = new Password(request.getParameter("password"));
                LOG.info("validateRequest loginAccount ="+request.getParameter("loginAccount"));
                LOG.info("validateRequest password ="+request.getParameter("password"));
                try{
                    CredentialValidationResult result = identityStoreHandler.validate(
                            new UsernamePasswordCredential(name, password));
                    LOG.debug("validateRequest result.getStatus() ="+result.getStatus());
                    if( result.getStatus() == VALID ){
                        // Communicate the details of the authenticated user to the
                        // container. In many cases the underlying handler will just store the details
                        // and the container will actually handle the login after we return from
                        // this method.
//                        return createToken(result, httpMessageContext);

                        CallerPrincipal caller = result.getCallerPrincipal();
                        if( caller!=null ){
                            Principal p = request.getUserPrincipal();
                            LOG.debug("*** validateRequest caller {}",caller.getName());
                            LOG.debug("*** validateRequest CallerGroups {}",result.getCallerGroups().size());
                            for(String group:result.getCallerGroups()){
                                LOG.debug("*** validateRequest Groups {}",group);
                            }
                        }
                        session.setAttribute(SecurityConstants.CALLER_ATTR, result.getCallerPrincipal());// CallerPrincipal
                        session.setAttribute(SecurityConstants.GROUPS_ATTR, result.getCallerGroups());
//                        context.notifyContainerAboutLogin(result);
                        
//                        String url = context..getExternalContext().getRequestContextPath();
//                        response.sendRedirect(serviceUrl);
//                        response.sendRedirect("member/member.xhtml");
                        return context.notifyContainerAboutLogin(result);
                    } else {
                        response.sendRedirect("index.xhtml");
                        return context.doNothing();
//                        return context.responseUnauthorized();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    LOG.error("validateRequest Exception:"+e);
                    return context.responseUnauthorized();
                }
            }
            
            // 無保護
            if( !context.isProtected() ){
//                LOG.debug("*** validateRequest No Protected ... ***");
                return context.doNothing();
            }else{
                LOG.debug("*** validateRequest is Protected ... ***");
//                CallerPrincipal caller = (CallerPrincipal)session.getAttribute(SecurityConstants.CALLER_ATTR);
//                if(caller!=null){
//                    Set<String> groups = (Set<String>) session.getAttribute(SecurityConstants.GROUPS_ATTR);
//                    for(String group:groups){
//                        LOG.debug("*** isProtected Groups {}",group);
//                        if(SysRoleEnum.MEMBER.getCode().equals(group)){
//                            return context.doNothing();
//                        }
//                    }
//                }
//                
//                response.sendRedirect("index.xhtml");
//                context.responseUnauthorized();
            }
        }catch(Exception e){    
//        }catch(IOException e){
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
    private boolean isLogin(HttpSession session){
        return session!=null 
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
    
    /**
     * 產生導向 CAS Login Page 的 URL
     * @param request
     * @param response
     * @return 
     */
    private String constructRedirectUrl(HttpServletRequest request, HttpServletResponse response){
        //String casServerLoginUrl = SecurityUtils.getContextParam(request, PN_CAS_LOGIN, DEF_CAS_LOGIN);
        String casServerLoginUrl = SecurityUtils.getFromJNDI(jndiConfig, SecurityConstants.PN_CAS_LOGIN, SecurityConstants.DEF_CAS_LOGIN);
        if( casServerLoginUrl==null ){
            LOG.error("constructRedirectUrl casServerLoginUrl == null !");
            return null;
        }
        
        // CAS 尚無應用的變數，如下固定即可
        String serviceParameterName = "service";
        boolean renew = false;
        boolean gateway = false;

        return CommonUtils.constructRedirectUrl(casServerLoginUrl,
                serviceParameterName,
                constructServiceUrl(request, response), 
                renew,
                gateway);
    }

    /**
     * 產生目標 Service 的 URL
     * @param request
     * @param response
     * @return 
     */
    private String constructServiceUrl(HttpServletRequest request, HttpServletResponse response){
        String serverName = SecurityUtils.getFromJNDI(jndiConfig, SecurityConstants.PN_SERVER_NAME, null);
        // CAS 尚無應用的變數，如下固定即可
        String service = null;
        String artifactParameterName = "ticket";
        boolean encodeServiceUrl = false;

        LOG.info("constructServiceUrl serverName = {}", serverName);
        if( serverName == null ){
            String forward = request.getHeader("x-forwarded-host");
            LOG.info("constructServiceUrl forward = {}", forward);
            String host = request.getHeader("host");
            LOG.info("constructServiceUrl host = {}", host);
            serverName = forward != null ? forward : host;
        }

        return CommonUtils.constructServiceUrl(request,
                response,
                service,
                serverName,
                artifactParameterName,
                encodeServiceUrl);
    }
}
