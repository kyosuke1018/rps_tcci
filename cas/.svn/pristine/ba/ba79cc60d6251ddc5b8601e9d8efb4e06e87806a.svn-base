/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security.credential;

import javax.security.enterprise.credential.AbstractClearableCredential;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Credential for Cas SSO Server 登入驗證
 * 
 * @author Peter.pan
 */
public class CasCredential extends AbstractClearableCredential {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    
    private String casServerUrlPrefix;
    //private String jaasConfig;// for 參考 CAS SAM 的舊作法 (需設定 realm in login.conf)
    private Assertion assertion;
    private String ticket;
    private String serviceUrl;    
    private String redirectURL;
    
    public CasCredential(String casServerUrlPrefix, String ticket, String serviceUrl){
        this.casServerUrlPrefix = casServerUrlPrefix;
        //this.jaasConfig = jaasConfig;
        this.ticket = ticket;
        this.serviceUrl = serviceUrl;
    }
    
    @Override
    protected void clearCredential() {
        assertion = null;
        ticket = null;
        serviceUrl = null;
    }

    @Override
    public boolean isValid() {
        return (assertion!=null 
                && assertion.getPrincipal()!=null 
                && assertion.getPrincipal().getName()!=null
                && !assertion.getPrincipal().getName().trim().isEmpty());
    }

    public Assertion getAssertion() {
        return assertion;
    }

    public void setAssertion(Assertion assertion) {
        this.assertion = assertion;
    }

    public String getCasServerUrlPrefix() {
        return casServerUrlPrefix;
    }

    public void setCasServerUrlPrefix(String casServerUrlPrefix) {
        this.casServerUrlPrefix = casServerUrlPrefix;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }
}
