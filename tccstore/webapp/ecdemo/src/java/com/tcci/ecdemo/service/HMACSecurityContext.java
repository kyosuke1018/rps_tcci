/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.service;

import com.tcci.ecdemo.entity.EcMember;
import java.security.Principal;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author Jimmy.Lee
 */
public class HMACSecurityContext implements SecurityContext {

    private EcMemberPrincipal principal;

    public HMACSecurityContext(EcMember ecMember) {
        principal = new EcMemberPrincipal(ecMember);
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return "valid-users".equals(role);
    }

    @Override
    public boolean isSecure() {
        return true;
    }

    @Override
    public String getAuthenticationScheme() {
        return "HMAC";
    }

}
