/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.rs;

import com.tcci.ec.enums.SmsProviderEnum;

/**
 *
 * @author Peter.pan
 */
public class SmsVO {
    private SmsProviderEnum provider;
    private boolean enabled;
    private String account;
    private String password;
    private String subject;
    private String content;
    private String sendSmsUrl;
    private String getCreditUrl;
    private String receiver;
    private String resMsg;

    public SmsProviderEnum getProvider() {
        return provider;
    }

    public void setProvider(SmsProviderEnum provider) {
        this.provider = provider;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendSmsUrl() {
        return sendSmsUrl;
    }

    public void setSendSmsUrl(String sendSmsUrl) {
        this.sendSmsUrl = sendSmsUrl;
    }

    public String getGetCreditUrl() {
        return getCreditUrl;
    }

    public void setGetCreditUrl(String getCreditUrl) {
        this.getCreditUrl = getCreditUrl;
    }
    
    
}
