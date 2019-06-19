/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security.IdentityStore;

import com.tcci.security.credential.CasCredential;
import com.tcci.security.SecurityUtils;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.singleton;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.CredentialValidationResult.Status;
import javax.security.enterprise.identitystore.IdentityStore;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IdentityStore for Cas SSO Server 登入驗證
 * 
 * @author Peter.pan
 */
public class CasAuthIdentityStore implements IdentityStore {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    //private LoginContext loginContext;// for 參考 CAS SAM 的舊作法 (需設定 realm in login.conf)

    @PostConstruct
    public void init() {
        LOG.debug("init ...");
    }
    
    /**
     * 驗證主程式
     * @param credential
     * @return 
     */
    @Override
    public CredentialValidationResult validate(Credential credential) {
        LOG.debug("validate ...");
        if(credential==null || !(credential instanceof CasCredential)){
            LOG.info("validate not CasCredential ...");
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
        
        try{
            CasCredential casCredential = ((CasCredential) credential);
            String ticket = casCredential.getTicket();

            if( ticket==null || ticket.length()==0 ){// URL 無包含 Service Ticket
                LOG.info("STATUS 1 : No login, redirect to CAS ...");
                return CredentialValidationResult.NOT_VALIDATED_RESULT;
            }

            // URL 包含 Service Ticket (一律重新至CAS驗證)
            // 準備 CAS 驗證所需參數
            String serviceUrl = casCredential.getServiceUrl();
            LOG.info("validate serviceUrl = {}", serviceUrl);
            String casServerUrlPrefix = casCredential.getCasServerUrlPrefix();
            LOG.info("validate casServerUrlPrefix = {}", casServerUrlPrefix);
            if( serviceUrl==null || casServerUrlPrefix==null ){
                // 無 CAS 相關設定
                LOG.error("STATUS 2 : serviceUrl="+serviceUrl+", casServerUrlPrefix="+casServerUrlPrefix);
                return CredentialValidationResult.INVALID_RESULT;
            }

            // CAS 驗證 service ticket & get caller name
            TicketValidator validator = new Cas20ServiceTicketValidator(casServerUrlPrefix);
            Assertion assertion = validator.validate(ticket, serviceUrl);
            if( assertion!=null ){
                LOG.info("validate caller = {}", SecurityUtils.getCallerFromAssertion(assertion));
                // 利用 casCredential 回傳 Assertion, 方便 TcAuthenticationMechanism 使用
                casCredential.setAssertion(assertion);
                // 回傳驗證成功
                CredentialValidationResult res = SecurityUtils.getValidationResultByAssertion(assertion);
                return SecurityUtils.getValidationResultByAssertion(assertion);
            }
        }catch(Exception e){
            LOG.error("validate Exception :\n", e);
        }

        return CredentialValidationResult.NOT_VALIDATED_RESULT;
    }
    
    /**
     * 自定 IdentityStore 執行順序 - 預設 priority 為100，越小越先執行
     * @return 
     */
    @Override
    public int priority(){ 
         return 90;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return singleton(ValidationType.VALIDATE);
    }

    //<editor-fold defaultstate="collapsed" desc="for 參考 CAS SAM 的舊作法 (需設定 realm in login.conf)">
    /*
    @Override
    public CredentialValidationResult validate(Credential credential) {
        LOG.debug("validate ...");
        CredentialValidationResult result = INVALID_RESULT; // NOT_VALIDATED_RESULT;
        try{
            CasCredential casCredential = ((CasCredential) credential);
            String ticket = casCredential.getTicket();

            if( ticket==null || ticket.length()==0 ){// URL 無包含 Service Ticket
                LOG.info("STATUS 1 : No login, redirect to CAS ...");
                return CredentialValidationResult.NOT_VALIDATED_RESULT;
            }

            // URL 包含 Service Ticket (一律重新至CAS驗證)
            // 準備 CAS 驗證所需參數
            String jaasConfig = casCredential.getJaasConfig();
            LOG.info("validate jaasConfig = {}", jaasConfig);

            String serviceUrl = casCredential.getServiceUrl();
            LOG.info("validate serviceUrl = {}", serviceUrl);
            String casServerUrlPrefix = casCredential.getCasServerUrlPrefix();
            LOG.info("validate casServerUrlPrefix = {}", casServerUrlPrefix);
            if( serviceUrl==null || casServerUrlPrefix==null ){
                // 無 CAS 相關設定
                LOG.error("STATUS 2 : serviceUrl="+serviceUrl+", casServerUrlPrefix="+casServerUrlPrefix);
                return CredentialValidationResult.INVALID_RESULT;
            }

            // 原本使用 LoginContext.login() 的方式
            loginContext = new LoginContext(jaasConfig, new ServiceAndTicketCallbackHandler(serviceUrl, ticket));
            LOG.info("validate before login ... ");
            loginContext.login();
            LOG.info("validate login done!");
            // 取得登入資訊
            Subject subject = loginContext.getSubject();
            for(Principal p : subject.getPrincipals()){
                if(p instanceof AssertionPrincipal){
                    LOG.info("STATUS 3 : Validate service ticket & get caller from CAS ...");
                    Assertion assertion = ((AssertionPrincipal) p).getAssertion();
                    // 利用 casCredential 回傳 Assertion, 方便 TcAuthenticationMechanism 使用
                    casCredential.setAssertion(assertion);
                    // 回傳驗證成功
                    return SecurityUtils.getValidationResultByAssertion(assertion);
                }
            }
        }catch(Exception e){
            LOG.error("validate Exception :\n", e);
        }

        LOG.info("validate result ="+result.getStatus());
        return result;
    }
    */
    //</editor-fold>
}
